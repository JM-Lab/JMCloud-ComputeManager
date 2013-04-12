package com.jmcloud.compute;

import java.util.List;

import com.jmcloud.compute.vo.ComputeGroupVO;
import com.jmcloud.compute.vo.ComputeVO;

public interface JMCloudComputeManager {

	public boolean loadRegionFromCloud(String region);

	public void loadComputeGroupFromLocal();

	public List<ComputeVO> loadAllComputesOnCloud(String region);

	public String[] loadComputeGroupNames(String region);

	public String[] getRegions();

	public String[] getAllRegions();

	public void addRegions(String... regions);

	public void removeRegion(String region);

	public boolean createSecurityGroup(String region, String securityGroup);

	public boolean createKeypair(String region, String keypair);

	public String getFWRules(String region, String computeGroupName);

	public boolean addFWRule(String region, String computeGroupName,
			String tcpOrUdp, String portRange, String cidrRange);

	public boolean removeFWRule(String region, String computeGroupName,
			String tcpOrUdpOrIcmp, String portRange, String cidrRange);

	public ComputeGroupVO getComputeGroup(String region, String computeGroupName);

	public ComputeGroupVO[] getAllComputeGroups();

	public boolean saveComputeGroup(String region, String computeGroupName);

	public boolean createGroup(String region, String computeGroupName);

	public boolean deleteGroup(String region, String computeGroupName);

	public boolean renameGroup(String region, String targetComputeGroupName,
			String newComputeGroupName);

	public ComputeVO[] provisionGroup(String region, String computeGroupName);

	public boolean stopGroup(String region, String computeGroupName);

	public boolean startGroup(String region, String computeGroupName);

	public boolean rebootGroup(String region, String computeGroupName);

	public boolean terminateGroup(String region, String computeGroupName);

	public String[] getGroupStatus(String region, String computeGroupName);

	public ComputeVO[] getComputesOfGroup(String region, String computeGroupName);

	public ComputeVO updateComputeStatus(String region,
			String computeGroupName, String computeName);

	public boolean createCompute(String region, String computeGroupName,
			String computeName, String imageID, String computeType);

	public boolean createComputes(String region, String computeGroupName,
			String computeName, String imageID, int numOfComputes,
			String computeType);

	public boolean deleteCompute(String region, String computeGroupName,
			String computeName);

	public boolean renameCompute(String region, String computeGroupName,
			String computeName, String newComputeName);

	public boolean provisionCompute(String region, String computeGroupName,
			String computeName);

	public boolean stopCompute(String region, String computeGroupName,
			String computeName);

	public boolean startCompute(String region, String computeGroupName,
			String computeName);

	public boolean rebootCompute(String region, String computeGroupName,
			String computeName);

	public boolean terminateCompute(String region, String computeGroupName,
			String computeName);

	public boolean deleteSecurityGroup(String region, String computeGroupName);

	public boolean deleteKeypair(String region, String computeGroupName);

}