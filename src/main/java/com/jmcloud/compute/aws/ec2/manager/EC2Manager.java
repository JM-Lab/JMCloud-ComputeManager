package com.jmcloud.compute.aws.ec2.manager;

import static com.jmcloud.compute.aws.ec2.util.EC2Util.COMPUTE_GROUP_NAME;
import static com.jmcloud.compute.aws.ec2.util.EC2Util.COMPUTE_NAME;
import static com.jmcloud.compute.aws.ec2.util.EC2Util.COMPUTE_TYPE;
import static com.jmcloud.compute.aws.ec2.util.EC2Util.IMAGE_ID;
import static com.jmcloud.compute.aws.ec2.util.EC2Util.KEYPAIR;
import static com.jmcloud.compute.aws.ec2.util.EC2Util.REGION;
import static com.jmcloud.compute.aws.ec2.util.EC2Util.SECURITYGROUP;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.aws.ec2.util.EC2Util;
import com.jmcloud.compute.manager.ComputeManager;
import com.jmcloud.compute.manager.provisioner.ProvisionerOnCloud;
import com.jmcloud.compute.sys.SystemString;
import com.jmcloud.compute.vo.ComputeVO;

@Service("eC2Manager")
public class EC2Manager implements ComputeManager<ComputeVO, String> {

	public static final String RUNNING_STATUS = "running";
	public static final String STOPPED_STATUS = "stopped";
	public static final String TERMINATED_STATUS = "terminated";
	public static final String PENDING_STATUS = "pending";
	public static final String NO_PROVISION_STATUS = "noProvision";

	@Resource(name = "provisionerOnEC2WithCLI")
	private ProvisionerOnCloud provisioniner;

	@Override
	public ComputeVO createCompute(Map<String, String> requiredValues) {
		ComputeVO computeVO = new ComputeVO();
		computeVO.setComputeID(new Integer(computeVO.hashCode()).toString());
		computeVO.setComputeName(requiredValues.get(COMPUTE_NAME));
		computeVO.setImageID(requiredValues.get(IMAGE_ID));
		computeVO.setSecurityGroup(requiredValues.get(SECURITYGROUP));
		computeVO.setKeypairName(requiredValues.get(KEYPAIR));
		computeVO.setComputeType(requiredValues.get(COMPUTE_TYPE));
		computeVO.setRegion(requiredValues.get(REGION));
		computeVO.setComputeGroupName(requiredValues.get(COMPUTE_GROUP_NAME));
		computeVO.setStatus(NO_PROVISION_STATUS);
		return computeVO;
	}

	@Override
	public ComputeVO[] createComputes(Map<String, String> requiredValues,
			int numOfComputes) {
		ComputeVO[] computeVOs = new ComputeVO[numOfComputes];
		for (int i = 0; i < numOfComputes; i++) {
			String name = requiredValues.get(COMPUTE_NAME);
			requiredValues.put(COMPUTE_NAME, name + (i + 1));
			computeVOs[i] = createCompute(requiredValues);
			requiredValues.put(COMPUTE_NAME, name);
		}
		return computeVOs;
	}

	@Override
	public ComputeVO provisionCompute(ComputeVO computeVO) {

		if (!NO_PROVISION_STATUS.equals(computeVO.getStatus())) {
			return null;
		}

		String createInfo = provisioniner.createCompute(computeVO.getRegion(),
				computeVO.getImageID(), "1", computeVO.getSecurityGroup(),
				computeVO.getKeypairName(), computeVO.getComputeType());

		if (EC2Util.isExistFalse(createInfo)) {
			return null;
		}

		String computeID = EC2Util.extractComputeID(createInfo);

		if (EC2Util.isExistFalse(computeID)
				|| EC2Util.isExistFalse(provisioniner.createTag(
						computeVO.getRegion(), "Name",
						computeVO.getComputeName(), computeID))) {
			provisioniner.terminateCompute(computeVO.getRegion(),
					computeVO.getComputeID());
			return null;
		}
		computeVO.setComputeID(computeID);
		computeVO.setComputeLaunchTime(EC2Util
				.extractComputeLaunchTime(createInfo));
		String platform = EC2Util.extractPlatform(createInfo);
		if (platform != null && !"".equals(platform)) {
			computeVO.setPlatform(EC2Util.extractPlatform(createInfo));
		}
		computeVO.setStatus(PENDING_STATUS);
		return computeVO;
	}

	@Override
	public ComputeVO[] provisionComputes(ComputeVO... computeVOs) {
		for (int i = 0; i < computeVOs.length; i++) {
			computeVOs[i] = provisionCompute(computeVOs[i]);
		}
		return computeVOs;
	}

	@Override
	public ComputeVO updateComputeStatus(ComputeVO computeVO, String computeInfo) {
		String status = EC2Util.extractStatus(computeInfo);
		String publicIP = EC2Util.extractPublicIP(computeInfo);
		String privateIP = EC2Util.extractPrivateIP(computeInfo);
		String computeLaunchTime = EC2Util
				.extractComputeLaunchTime(computeInfo);
		if (status != null && !status.equals(computeVO.getStatus())) {
			computeVO.setStatus(status);
		}
		if (publicIP != null && !publicIP.equals(computeVO.getPublicIP())) {
			computeVO.setPublicIP(publicIP);
		}
		if (privateIP != null && !privateIP.equals(computeVO.getPrivateIP())) {
			computeVO.setPrivateIP(privateIP);
		}
		if (computeLaunchTime != null
				&& !computeLaunchTime.equals(computeVO.getComputeLaunchTime())) {
			computeVO.setComputeLaunchTime(computeLaunchTime);
		}
		return computeVO;
	}

	@Override
	public String getComputeInfo(ComputeVO computeVO) {
		String computeInfo = provisioniner.getComputeInfo(
				computeVO.getRegion(), computeVO.getComputeID());
		return !EC2Util.isExistFalse(computeInfo) ? computeInfo
				: SystemString.FALSE;
	}

	@Override
	public String[] getComputesInfo(ComputeVO... computeVOs) {
		String[] computesInfo = new String[computeVOs.length];
		for (int i = 0; i < computeVOs.length; i++) {
			computesInfo[i] = getComputeInfo(computeVOs[i]);
		}
		return computesInfo;
	}

	@Override
	public ComputeVO changeComputeName(ComputeVO computeVO,
			String newComputeName) {
		computeVO.setComputeName(newComputeName);
		return computeVO;
	}

	@Override
	public String monitoringCompute(ComputeVO computeVO) {
		return EC2Util.extractStatus(getComputeInfo(computeVO));
	}

	@Override
	public String[] monitoringComputes(ComputeVO... computeVOs) {
		String[] statuses = getComputesInfo(computeVOs);
		for (int i = 0; i < statuses.length; i++) {
			statuses[i] = EC2Util.extractStatus(statuses[i]);
		}
		return statuses;
	}

	private interface ComputeOperation {
		public boolean isProperStatus(String status);

		public boolean operationCompute(String region, String computeID);
	}

	private boolean operationComputes(ComputeVO[] computeVOs,
			ComputeOperation computeOperation, String afterStatus) {
		boolean allSuccesses = true;
		for (ComputeVO computeVO : computeVOs) {
			if (computeOperation.isProperStatus(computeVO.getStatus())
					&& computeOperation.operationCompute(computeVO.getRegion(),
							computeVO.getComputeID())) {
				computeVO.setStatus(afterStatus);
				continue;
			}
			allSuccesses = false;
		}
		return allSuccesses;
	}

	private class TerminateComputes implements ComputeOperation {
		@Override
		public boolean operationCompute(String region, String computeID) {
			return provisioniner.terminateCompute(region, computeID);
		}

		@Override
		public boolean isProperStatus(String status) {
			return !NO_PROVISION_STATUS.equals(status);
		}
	}

	private class StartComputes implements ComputeOperation {
		@Override
		public boolean operationCompute(String region, String computeID) {
			return provisioniner.startCompute(region, computeID);
		}

		@Override
		public boolean isProperStatus(String status) {
			return STOPPED_STATUS.equals(status);
		}
	}

	private class RebootComputes implements ComputeOperation {
		@Override
		public boolean operationCompute(String region, String computeID) {
			return provisioniner.rebootCompute(region, computeID);
		}

		@Override
		public boolean isProperStatus(String status) {
			return RUNNING_STATUS.equals(status);
		}
	}

	private class StopComputes implements ComputeOperation {
		@Override
		public boolean operationCompute(String region, String computeID) {
			return provisioniner.stopCompute(region, computeID);
		}

		@Override
		public boolean isProperStatus(String status) {
			return RUNNING_STATUS.equals(status);
		}
	}

	@Override
	public boolean terminateComputes(ComputeVO... computeVOs) {
		return operationComputes(computeVOs, new TerminateComputes(),
				TERMINATED_STATUS);
	}

	@Override
	public boolean startComputes(ComputeVO... computeVOs) {
		return operationComputes(computeVOs, new StartComputes(),
				PENDING_STATUS);
	}

	@Override
	public boolean rebootComputes(ComputeVO... computeVOs) {
		return operationComputes(computeVOs, new RebootComputes(),
				PENDING_STATUS);
	}

	@Override
	public boolean stopComputes(ComputeVO... computeVOs) {
		return operationComputes(computeVOs, new StopComputes(), PENDING_STATUS);
	}

}
