package com.jmcloud.compute.aws.ec2;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.JMCloudComputeManager;
import com.jmcloud.compute.aws.ec2.manager.EC2Manager;
import com.jmcloud.compute.aws.ec2.util.EC2Util;
import com.jmcloud.compute.manager.ComputeGroupManager;
import com.jmcloud.compute.manager.RegionManager;
import com.jmcloud.compute.manager.provisioner.ProvisionerOnCloud;
import com.jmcloud.compute.sys.SystemEnviroment;
import com.jmcloud.compute.sys.SystemString;
import com.jmcloud.compute.vo.ComputeGroupVO;
import com.jmcloud.compute.vo.ComputeVO;

@Service("jMCloudEC2Manager")
public class JMCloudEC2Manager implements JMCloudComputeManager {

	@Resource(name = "eC2GroupManager")
	private ComputeGroupManager<ComputeGroupVO, Path, ComputeVO> eC2GroupManager;

	@Resource(name = "eC2RegionManager")
	private RegionManager eC2RegionManager;

	@Resource(name = "provisionerOnEC2WithCLI")
	private ProvisionerOnCloud provisioner;

	@Override
	public ComputeGroupVO getComputeGroup(String region, String computeGroupName) {
		return eC2GroupManager.getComputeGroup(region, computeGroupName);
	}

	private FilenameFilter groupInfoFileFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(SystemString.JMCLOUD_SIGNATURE);
		}
	};

	@Override
	public ComputeVO updateComputeStatus(String region,
			String computeGroupName, String computeID) {
		ComputeGroupVO computeGroupVO = getComputeGroup(region,
				computeGroupName);
		ComputeVO computeVO = eC2GroupManager.getCompute(computeGroupVO,
				computeID);
		return eC2GroupManager.updateComputeStatus(computeVO);
	}

	@Override
	public ComputeGroupVO[] getAllComputeGroups() {
		Map<String, ComputeGroupVO> computeGroupList = eC2GroupManager
				.getComputeGroupList();
		return computeGroupList.values().toArray(
				new ComputeGroupVO[computeGroupList.size()]);
	}

	@Override
	public void addRegions(String... regions) {
		eC2RegionManager.addRegions(regions);
	}

	@Override
	public void removeRegion(String region) {
		eC2RegionManager.removeRegion(region);
	}

	@Override
	public String[] getRegions() {
		return eC2RegionManager.getRegions();
	}

	@Override
	public String[] getAllRegions() {
		return eC2RegionManager.getAllRegions();
	}

	@Override
	public List<ComputeVO> loadAllComputesOnCloud(String region) {
		String[] groupsInfo = EC2Util.extractGroupsInfo(provisioner
				.getAllComputesInfo(region));
		List<ComputeVO> computeList = new ArrayList<ComputeVO>();
		for (String groupInfo : groupsInfo) {
			String securityGroup = EC2Util.extractSecurityGroup(groupInfo);
			if(SystemString.FALSE.equals(securityGroup)){
				continue;
			}
			String[] computesInfo = EC2Util.extractInstancesInfo(groupInfo);
			for (String computeInfo : computesInfo) {
				if (!computeInfo.contains("terminated")) {
					String computeName = EC2Util
							.extractComputeName(computeInfo);
					computeName = EC2Util.isExistFalse(computeName) ? ""
							: computeName;
					String imageID = EC2Util.extractImageID(computeInfo);
					String keypair = EC2Util.extractkeypair(computeInfo);
					String computeType = EC2Util
							.extractComputeType(computeInfo);
					String computeGroupName = securityGroup;
					Map<String, String> requiredValues = EC2Util
							.makeRequiredValus(computeName, imageID,
									securityGroup, keypair, computeType,
									region, computeGroupName);
					ComputeVO computeVO = eC2GroupManager
							.createCompute(requiredValues);
					String computeID = EC2Util.extractComputeID(computeInfo);
					String status = EC2Util.extractStatus(computeInfo);
					String publicIP = EC2Util.extractPublicIP(computeInfo);
					String privateIP = EC2Util.extractPrivateIP(computeInfo);
					String computeLaunchTime = EC2Util
							.extractComputeLaunchTime(computeInfo);
					computeVO.setComputeID(computeID);
					computeVO.setStatus(status);
					computeVO.setPublicIP(publicIP);
					computeVO.setPrivateIP(privateIP);
					computeVO.setComputeLaunchTime(computeLaunchTime);
					computeList.add(computeVO);
				}
			}

		}
		return computeList;
	}

	@Override
	public boolean loadRegionFromCloud(String region) {
		String[] computeGroupNames = loadComputeGroupNames(region);
		if (computeGroupNames == null || computeGroupNames.length == 0) {
			return false;
		}

		Map<String, ComputeGroupVO> oldComputeGroupVOList = new HashMap<String, ComputeGroupVO>();
		for (ComputeGroupVO computeGroupVO : eC2GroupManager
				.getComputeGroupList().values()) {
			if (region.equals(computeGroupVO.getRegion())) {
				oldComputeGroupVOList.put(computeGroupVO.getComputeGroupName(),
						computeGroupVO);
			}
		}

		for (ComputeGroupVO computeGroupVO : oldComputeGroupVOList.values()) {
			computeGroupVO.getComputesList().clear();
			eC2GroupManager.deleteComputeGroup(computeGroupVO);
		}

		for (String computeGroupName : computeGroupNames) {
			if (oldComputeGroupVOList.containsKey(computeGroupName)) {
				eC2GroupManager.addComputeGroup(oldComputeGroupVOList
						.get(computeGroupName));
			} else {
				eC2GroupManager.addComputeGroup(eC2GroupManager
						.createComputeGroup(region, computeGroupName));
			}
		}

		List<ComputeVO> computeVOList = loadAllComputesOnCloud(region);
		for (ComputeVO computeVO : computeVOList) {
			if (eC2GroupManager.getComputeGroup(region,
					computeVO.getComputeGroupName()) == null) {
				eC2GroupManager.addComputeGroup(eC2GroupManager
						.createComputeGroup(region,
								computeVO.getComputeGroupName()));
			}
			eC2GroupManager.addCompute(
					getComputeGroup(region, computeVO.getComputeGroupName()),
					computeVO);
		}
		return true;
	}

	@Override
	public String[] loadComputeGroupNames(String region) {
		return EC2Util.extractSecurityGroups(provisioner
				.getAllSecurityGroups(region));
	}

	@Override
	public boolean saveComputeGroup(String region, String computeGroupName) {
		return eC2GroupManager.saveComputeGroup(getComputeGroup(region,
				computeGroupName));
	}

	@Override
	public void loadComputeGroupFromLocal() {
		Path groupInfoPath = Paths.get(SystemEnviroment.getDataSaveDir());
		File gropuInfoDir = groupInfoPath.toFile();
		if (!gropuInfoDir.exists()) {
			if (!gropuInfoDir.mkdirs()) {
				return;
			}
		}

		File[] groupInfoFiles = gropuInfoDir.listFiles(groupInfoFileFilter);
		if (groupInfoFiles == null || groupInfoFiles.length == 0) {
			return;
		}

		for (File infoFile : groupInfoFiles) {
			ComputeGroupVO computeGroupVO = eC2GroupManager
					.loadComputeGroup(infoFile.toPath());
			eC2GroupManager.addComputeGroup(computeGroupVO);
		}
	}

	@Override
	public boolean createGroup(String region, String computeGroupName) {
		ComputeGroupVO computeGroupVO = eC2GroupManager.createComputeGroup(
				region, computeGroupName);
		if (computeGroupVO == null) {
			return false;
		}
		return eC2GroupManager.addComputeGroup(computeGroupVO);
	}

	@Override
	public boolean deleteGroup(String region, String computeGroupName) {
		return eC2GroupManager.deleteComputeGroup(getComputeGroup(region,
				computeGroupName));
	}

	@Override
	public boolean renameGroup(String region, String targetComputeGroupName,
			String newComputeGroupName) {
		ComputeGroupVO newComputeGroupVO = eC2GroupManager
				.changeComputeGroupName(
						getComputeGroup(region, targetComputeGroupName),
						newComputeGroupName);
		return saveComputeGroup(region, newComputeGroupVO.getComputeGroupName());
	}

	@Override
	public ComputeVO[] provisionGroup(String region, String computeGroupName) {
		return eC2GroupManager.provisionComputeGroup(getComputeGroup(region,
				computeGroupName));
	}

	@Override
	public boolean stopGroup(String region, String computeGroupName) {
		return eC2GroupManager.stopComputeGroup(getComputeGroup(region,
				computeGroupName));
	}

	@Override
	public boolean startGroup(String region, String computeGroupName) {
		return eC2GroupManager.startComputeGroup(getComputeGroup(region,
				computeGroupName));
	}

	@Override
	public boolean rebootGroup(String region, String computeGroupName) {
		return eC2GroupManager.rebootComputeGroup(getComputeGroup(region,
				computeGroupName));
	}

	@Override
	public boolean terminateGroup(String region, String computeGroupName) {
		return eC2GroupManager.terminateComputeGroup(getComputeGroup(region,
				computeGroupName));
	}

	@Override
	public String getFWRules(String region, String computeGroupName) {
		ComputeGroupVO computeGroupVO = getComputeGroup(region,
				computeGroupName);
		return eC2GroupManager.getSecurityGroupRules(
				computeGroupVO.getRegion(), computeGroupVO.getSecurityGroup());
	}

	@Override
	public boolean addFWRule(String region, String computeGroupName,
			String tcpOrUdpOrIcmp, String portRange, String cidrRange) {
		ComputeGroupVO computeGroupVO = getComputeGroup(region,
				computeGroupName);
		if ("ICMP".equals(tcpOrUdpOrIcmp)) {
			return eC2GroupManager.registerSecurityICMPRule(
					computeGroupVO.getRegion(),
					computeGroupVO.getSecurityGroup(), cidrRange);
		}
		return eC2GroupManager.registerSecurityRule(computeGroupVO.getRegion(),
				computeGroupVO.getSecurityGroup(), tcpOrUdpOrIcmp, portRange,
				cidrRange);
	}

	@Override
	public boolean removeFWRule(String region, String computeGroupName,
			String tcpOrUdpOrIcmp, String portRange, String cidrRange) {
		ComputeGroupVO computeGroupVO = getComputeGroup(region,
				computeGroupName);
		if ("ICMP".equals(tcpOrUdpOrIcmp)) {
			return eC2GroupManager.removeSecurityRuleForICMP(
					computeGroupVO.getRegion(),
					computeGroupVO.getSecurityGroup(), cidrRange);
		}
		return eC2GroupManager.removeSecurityRule(computeGroupVO.getRegion(),
				computeGroupVO.getSecurityGroup(), tcpOrUdpOrIcmp, portRange,
				cidrRange);
	}

	@Override
	public boolean createCompute(String region, String computeGroupName,
			String computeName, String imageID, String computeType) {
		ComputeGroupVO computeGroupVO = eC2GroupManager.getComputeGroup(region,
				computeGroupName);
		ComputeVO computeVO = eC2GroupManager.createCompute(EC2Util
				.makeRequiredValus(computeName, imageID,
						computeGroupVO.getSecurityGroup(),
						computeGroupVO.getGroupKeyPair(), computeType,
						computeGroupVO.getRegion(), computeGroupName));
		return eC2GroupManager.addCompute(
				getComputeGroup(region, computeGroupName), computeVO);
	}

	@Override
	public boolean createComputes(String region, String computeGroupName,
			String computeName, String imageID, int numOfComputes,
			String computeType) {
		ComputeGroupVO computeGroupVO = eC2GroupManager.getComputeGroup(region,
				computeGroupName);
		ComputeVO[] computeVOs = eC2GroupManager.createComputes(EC2Util
				.makeRequiredValus(computeName, imageID,
						computeGroupVO.getSecurityGroup(),
						computeGroupVO.getGroupKeyPair(), computeType,
						computeGroupVO.getRegion(), computeGroupName),
				numOfComputes);
		boolean allSuccess = true;
		for (ComputeVO computeVO : computeVOs) {
			allSuccess = eC2GroupManager.addCompute(
					getComputeGroup(region, computeGroupName), computeVO);
		}
		return allSuccess;
	}

	@Override
	public boolean deleteCompute(String region, String computeGroupName,
			String computeID) {
		return eC2GroupManager.removeCompute(
				getComputeGroup(region, computeGroupName), computeID);
	}

	@Override
	public boolean renameCompute(String region, String computeGroupName,
			String computeID, String newComputeName) {
		return eC2GroupManager.changeComputeName(
				getComputeGroup(region, computeGroupName), computeID,
				newComputeName);
	}

	@Override
	public boolean provisionCompute(String region, String computeGroupName,
			String computeID) {
		ComputeVO realComputeVO = eC2GroupManager
				.provisionCompute(eC2GroupManager.getCompute(
						getComputeGroup(region, computeGroupName), computeID));
		if (realComputeVO != null
				&& EC2Manager.PENDING_STATUS.equals(realComputeVO.getStatus())) {
			eC2GroupManager.saveComputeGroup(getComputeGroup(region,
					realComputeVO.getComputeGroupName()));
			return true;
		}
		return false;
	}

	@Override
	public boolean stopCompute(String region, String computeGroupName,
			String computeID) {
		return eC2GroupManager.stopCompute(eC2GroupManager.getCompute(
				getComputeGroup(region, computeGroupName), computeID));
	}

	@Override
	public boolean startCompute(String region, String computeGroupName,
			String computeID) {
		return eC2GroupManager.startCompute(eC2GroupManager.getCompute(
				getComputeGroup(region, computeGroupName), computeID));
	}

	@Override
	public boolean rebootCompute(String region, String computeGroupName,
			String computeID) {
		return eC2GroupManager.rebootCompute(eC2GroupManager.getCompute(
				getComputeGroup(region, computeGroupName), computeID));
	}

	@Override
	public boolean terminateCompute(String region, String computeGroupName,
			String computeID) {
		return eC2GroupManager.terminateCompute(eC2GroupManager.getCompute(
				getComputeGroup(region, computeGroupName), computeID));
	}

	@Override
	public boolean createSecurityGroup(String region, String securityGroup) {
		return eC2GroupManager.createSecurityGroup(region, securityGroup);
	}

	@Override
	public boolean createKeypair(String region, String keypair) {
		return !EC2Util.isExistFalse(eC2GroupManager.createKeypair(region,
				keypair));
	}

	@Override
	public boolean deleteSecurityGroup(String region, String computeGroupName) {
		ComputeGroupVO computeGroupVO = getComputeGroup(region,
				computeGroupName);
		return eC2GroupManager.deleteSecurityGroup(computeGroupVO.getRegion(),
				computeGroupVO.getSecurityGroup());
	}

	@Override
	public boolean deleteKeypair(String region, String computeGroupName) {
		ComputeGroupVO computeGroupVO = getComputeGroup(region,
				computeGroupName);
		return eC2GroupManager.deleteKeypair(computeGroupVO.getRegion(),
				computeGroupVO.getGroupKeyPair());
	}

	@Override
	public String[] getGroupStatus(String region, String computeGroupName) {
		ComputeVO[] computes = getComputesOfGroup(region, computeGroupName);
		String[] statuses = new String[computes.length];
		for (int i = 0; i < statuses.length; i++) {
			statuses[i] = computes[i].getStatus();
		}
		return statuses;
	}

	@Override
	public ComputeVO[] getComputesOfGroup(String region, String computeGroupName) {
		return eC2GroupManager.getComputes(getComputeGroup(region,
				computeGroupName));
	}

}
