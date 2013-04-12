package com.jmcloud.compute.aws.ec2.manager;

import static com.jmcloud.compute.aws.ec2.manager.EC2Manager.NO_PROVISION_STATUS;
import static com.jmcloud.compute.aws.ec2.manager.EC2Manager.RUNNING_STATUS;
import static com.jmcloud.compute.aws.ec2.manager.EC2Manager.STOPPED_STATUS;
import static com.jmcloud.compute.aws.ec2.manager.EC2Manager.TERMINATED_STATUS;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.aws.ec2.util.EC2Util;
import com.jmcloud.compute.dao.ComputeGroupDAO;
import com.jmcloud.compute.manager.ComputeGroupManager;
import com.jmcloud.compute.manager.ComputeManager;
import com.jmcloud.compute.manager.provisioner.ProvisionerOnCloud;
import com.jmcloud.compute.sys.SystemEnviroment;
import com.jmcloud.compute.sys.SystemString;
import com.jmcloud.compute.vo.ComputeGroupVO;
import com.jmcloud.compute.vo.ComputeVO;

@Service("eC2GroupManager")
public class EC2GroupManager implements
		ComputeGroupManager<ComputeGroupVO, Path, ComputeVO> {

	@Resource(name = "eC2GroupDAOWithJSON")
	private ComputeGroupDAO<ComputeGroupVO, Path> computeGroupDAO;

	@Resource(name = "provisionerOnEC2WithCLI")
	private ProvisionerOnCloud provisioner;

	@Resource(name = "eC2Manager")
	private ComputeManager<ComputeVO, String> computeManager;

	private Map<String, ComputeGroupVO> computeGroupList = new HashMap<String, ComputeGroupVO>();

	@Override
	public Map<String, ComputeGroupVO> getComputeGroupList() {
		return computeGroupList;
	}

	@Override
	public ComputeGroupVO createComputeGroup(String region,
			String computeGroupName) {
		ComputeGroupVO computeGroupVO = new ComputeGroupVO();
		computeGroupVO.setComputeGroupName(computeGroupName);
		computeGroupVO.setRegion(region);
		computeGroupVO.setCreationTime(System.currentTimeMillis());
		computeGroupVO.setSecurityGroup(computeGroupName);
		computeGroupVO.setComputeGroupKeyPair(SystemString.KEYPAIR_SIGNATURE
				+ computeGroupVO.getCreationTime());
		computeGroupDAO.createComputeGroupData(computeGroupVO);
		return computeGroupVO;
	}

	@Override
	public ComputeGroupVO changeComputeGroupName(
			ComputeGroupVO targetComputeGroupVO, String newComputeGroupName) {
		String newComputeGroupKey = getComputeGroupKey(
				targetComputeGroupVO.getRegion(), newComputeGroupName);
		if (computeGroupList.containsKey(newComputeGroupKey)) {
			return null;
		}
		String oldComputeGroupKey = getComputeGroupKey(
				targetComputeGroupVO.getRegion(),
				targetComputeGroupVO.getComputeGroupName());
		targetComputeGroupVO.setComputeGroupName(newComputeGroupName);
		for (ComputeVO computeVO : targetComputeGroupVO.getComputesList()
				.values()) {
			computeVO.setComputeGroupName(newComputeGroupName);
		}
		computeGroupList.put(newComputeGroupKey, targetComputeGroupVO);
		computeGroupList.remove(oldComputeGroupKey);
		return targetComputeGroupVO;
	}

	@Override
	public ComputeGroupVO loadComputeGroup(Path dataSource) {
		return computeGroupDAO.readComputeGroupData(dataSource);
	}

	@Override
	public boolean saveComputeGroup(ComputeGroupVO computeGroupVO) {
		return computeGroupDAO.updateComputeGroupData(computeGroupVO);
	}

	@Override
	public boolean deleteComputeGroup(ComputeGroupVO computeGroupVO) {
		if (computeGroupVO == null
				|| !computeGroupDAO.deleteComputeGroupData(computeGroupVO)) {
			// ("Can't delete the computeGroup data!");
			return false;
		}
		return computeGroupList.remove(getComputeGroupKey(
				computeGroupVO.getRegion(),
				computeGroupVO.getComputeGroupName())) != null;
	}

	@Override
	public ComputeGroupVO getComputeGroup(String region, String computeGroupName) {
		return computeGroupList
				.get(getComputeGroupKey(region, computeGroupName));
	}

	@Override
	public boolean addComputeGroup(ComputeGroupVO computeGroupVO) {
		String computeGroupKey = getComputeGroupKey(computeGroupVO.getRegion(),
				computeGroupVO.getComputeGroupName());
		if (computeGroupList.containsKey(computeGroupKey)) {
			return false;
		}
		return computeGroupList.put(computeGroupKey, computeGroupVO) == null;
	}

	@Override
	public boolean addCompute(ComputeGroupVO computeGroupVO, ComputeVO computeVO) {
		String computeVOKey = getComputeVOKey(
				computeGroupVO.getComputeGroupName(), computeVO.getComputeID());
		if (computeGroupVO.getComputesList().containsKey(computeVOKey)) {
			// ("The same computeName is existed!");
			return false;
		}
		if (computeVO.getKeypairName() == null
				|| "".equals(computeVO.getKeypairName())) {
			computeVO.setKeypairName(computeGroupVO.getGroupKeyPair());
		}
		computeVO.setSecurityGroup(computeGroupVO.getSecurityGroup());
		computeVO.setRegion(computeGroupVO.getRegion());

		return computeGroupVO.getComputesList().put(computeVOKey, computeVO) == null;
	}

	private String getComputeVOKey(String computeGroupName, String computeID) {
		return computeGroupName + computeID;
	}

	@Override
	public ComputeVO getCompute(ComputeGroupVO computeGroupVO, String computeID) {
		return computeGroupVO.getComputesList()
				.get(getComputeVOKey(computeGroupVO.getComputeGroupName(),
						computeID));
	}

	@Override
	public boolean removeCompute(ComputeGroupVO computeGroupVO, String computeID) {
		return computeGroupVO.getComputesList()
				.remove(getComputeVOKey(computeGroupVO.getComputeGroupName(),
						computeID)) != null;
	}

	@Override
	public boolean changeComputeName(ComputeGroupVO computeGroupVO,
			String computeID, String newComputeName) {
		ComputeVO computeVO = getCompute(computeGroupVO, computeID);
		String oldComputeName = computeVO.getComputeName();
		if (computeManager.changeComputeName(computeVO, newComputeName) == null
				|| !removeCompute(computeGroupVO, oldComputeName)
				|| !saveComputeGroup(computeGroupVO)) {
			computeManager.changeComputeName(computeVO, oldComputeName);
			return false;
		}
		addCompute(computeGroupVO, computeVO);
		provisioner.createTag(computeVO.getRegion(), "Name", newComputeName,
				computeVO.getComputeID());
		return true;
	}

	@Override
	public boolean createSecurityGroup(String region, String securityGroup) {
		return !EC2Util.isExistFalse(provisioner.createSecurityGroup(region,
				securityGroup));
	}

	@Override
	public boolean deleteSecurityGroup(String region, String securityGroup) {
		return provisioner.deleteSecurityGroup(region, securityGroup);
	}

	@Override
	public boolean isExistingSecurityGroup(String region, String securityGroup) {
		return !EC2Util.isExistFalse(provisioner.searchSecurityGroup(region,
				securityGroup));
	}

	@Override
	public boolean registerSecurityRule(String region, String securityGroup,
			String tcpOrUdp, String portRange, String cidrRange) {
		return provisioner.registerSecurityRule(region, securityGroup,
				tcpOrUdp, portRange, cidrRange);
	}

	@Override
	public boolean removeSecurityRule(String region, String securityGroup,
			String tcpOrUdp, String portRange, String cidrRange) {
		return provisioner.removeSecurityRule(region, securityGroup, tcpOrUdp,
				portRange, cidrRange);
	}

	@Override
	public boolean registerSecurityICMPRule(String region,
			String securityGroup, String cidrRange) {
		return provisioner.registerSecurityRuleForICMP(region, securityGroup,
				cidrRange);
	}

	@Override
	public boolean removeSecurityRuleForICMP(String region,
			String securityGroup, String cidrRange) {
		return provisioner.removeSecurityRuleForICMP(region, securityGroup,
				cidrRange);
	}

	@Override
	public String getSecurityGroupRules(String region, String securityGroup) {
		return EC2Util.extractSecurityRules(provisioner.searchSecurityGroup(
				region, securityGroup));
	}

	@Override
	public String getAllSecurityGroupsRules(String region) {
		return EC2Util.extractSecurityRules(provisioner
				.getAllSecurityGroups(region));
	}

	@Override
	public boolean startComputeGroup(ComputeGroupVO computeGroupVO) {
		return computeManager.startComputes(getComputesOnProperStatus(
				computeGroupVO, STOPPED_STATUS));
	}

	@Override
	public boolean stopComputeGroup(ComputeGroupVO computeGroupVO) {
		return computeManager.stopComputes(getComputesOnProperStatus(
				computeGroupVO, RUNNING_STATUS));
	}

	@Override
	public boolean rebootComputeGroup(ComputeGroupVO computeGroupVO) {
		return computeManager.rebootComputes(getComputesOnProperStatus(
				computeGroupVO, RUNNING_STATUS));
	}

	@Override
	public boolean terminateComputeGroup(ComputeGroupVO computeGroupVO) {
		List<ComputeVO> computeList = new ArrayList<ComputeVO>();
		for (ComputeVO computeVO : getComputes(computeGroupVO)) {
			if (!TERMINATED_STATUS.equals(computeVO.getStatus())
					&& !NO_PROVISION_STATUS.equals(computeVO.getStatus())) {
				computeList.add(computeVO);
			}
		}
		return computeManager.terminateComputes(computeList
				.toArray(new ComputeVO[computeList.size()]));
	}

	@Override
	public ComputeVO[] provisionComputeGroup(ComputeGroupVO computeGroupVO) {
		ComputeVO[] oldComputeVOs = getComputesOnProperStatus(computeGroupVO,
				NO_PROVISION_STATUS);
		String[] oldComputeIDs = new String[oldComputeVOs.length];
		for (int i = 0; i < oldComputeVOs.length; i++) {
			oldComputeIDs[i] = oldComputeVOs[i].getComputeID();
		}

		ComputeVO[] newComputeVOs = computeManager
				.provisionComputes(oldComputeVOs);

		for (int i = 0; i < oldComputeIDs.length; i++) {
			if (newComputeVOs[i] != null
					&& !oldComputeIDs[i]
							.equals(newComputeVOs[i].getComputeID())) {
				removeCompute(computeGroupVO, oldComputeIDs[i]);
				addCompute(computeGroupVO, newComputeVOs[i]);
			}

		}
		return newComputeVOs;
	}

	private ComputeVO[] getComputesOnProperStatus(
			ComputeGroupVO computeGroupVO, String status) {
		List<ComputeVO> computeList = new ArrayList<ComputeVO>();
		for (ComputeVO computeVO : getComputes(computeGroupVO)) {
			if (status.equals(computeVO.getStatus())) {
				computeList.add(computeVO);
			}
		}
		return computeList.toArray(new ComputeVO[computeList.size()]);
	}

	@Override
	public ComputeVO[] getComputes(ComputeGroupVO computeGroupVO) {
		Collection<ComputeVO> computeList = computeGroupVO.getComputesList()
				.values();
		return computeList.toArray(new ComputeVO[computeList.size()]);
	}

	private Path getKeypairFilePath(String keypair) {
		if (keypair == null) {
			return null;
		}
		return Paths.get(SystemEnviroment.getKeypairDir(), keypair);
	}

	@Override
	public String createKeypair(String region, String keypair) {
		String[] returnStrings = provisioner.createKeypair(region, keypair)
				.split("\n", 2);
		if (returnStrings.length < 2) {
			return SystemString.FALSE;
		}
		String privateKey = returnStrings[1];
		if (!EC2Util
				.saveStringIntoFile(getKeypairFilePath(keypair), privateKey)) {
			return SystemString.FALSE;
		}
		return privateKey;
	}

	@Override
	public boolean deleteKeypair(String region, String keypair) {
		Path keypairPath = getKeypairFilePath(keypair);
		if (keypairPath == null || !provisioner.deleteKeypair(region, keypair)
				|| !keypairPath.toFile().exists()) {
			return false;
		}
		try {
			Files.delete(keypairPath);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public String[] getAllKeypairs(String region) {
		return EC2Util.extractKeypairs(provisioner.getAllKeypairs(region));
	}

	@Override
	public boolean isExistingKeypair(String region, String keypair) {
		return !EC2Util
				.isExistFalse(provisioner.searchKeypair(region, keypair));
	}

	@Override
	public ComputeVO updateComputeStatus(ComputeVO computeVO) {
		String computeInfo = computeManager.getComputeInfo(computeVO);
		if (EC2Util.isExistFalse(computeInfo)) {
			return null;
		}
		String oldStatus = computeVO.getStatus();
		if (!oldStatus.equals(computeManager.updateComputeStatus(computeVO,
				computeInfo).getStatus())) {
			saveComputeGroup(getComputeGroup(computeVO.getRegion(),
					computeVO.getComputeGroupName()));
		}
		return computeVO;
	}

	@Override
	public ComputeVO createCompute(Map<String, String> requiredValues) {
		return computeManager.createCompute(requiredValues);
	}

	@Override
	public ComputeVO[] createComputes(Map<String, String> requiredValues,
			int numOfComputes) {
		return computeManager.createComputes(requiredValues, numOfComputes);
	}

	@Override
	public ComputeVO provisionCompute(ComputeVO computeVO) {
		String oldComputeID = computeVO.getComputeID();
		ComputeVO newComputeVO = computeManager.provisionCompute(computeVO);

		if (newComputeVO != null
				&& !oldComputeID.equals(newComputeVO.getComputeID())) {
			ComputeGroupVO computeGroupVO = getComputeGroup(
					newComputeVO.getRegion(),
					newComputeVO.getComputeGroupName());
			removeCompute(computeGroupVO, oldComputeID);
			addCompute(computeGroupVO, newComputeVO);
		}
		return newComputeVO;
	}

	@Override
	public boolean stopCompute(ComputeVO computeVO) {
		return computeManager.stopComputes(computeVO);
	}

	@Override
	public boolean startCompute(ComputeVO computeVO) {
		return computeManager.startComputes(computeVO);
	}

	@Override
	public boolean rebootCompute(ComputeVO computeVO) {
		return computeManager.rebootComputes(computeVO);
	}

	@Override
	public boolean terminateCompute(ComputeVO computeVO) {
		return computeManager.terminateComputes(computeVO);
	}

	@Override
	public String getComputeGroupKey(String region, String computeGroupName) {
		return region + computeGroupName;
	}

}
