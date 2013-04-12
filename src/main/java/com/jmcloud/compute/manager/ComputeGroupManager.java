package com.jmcloud.compute.manager;

import java.util.Map;

import com.jmcloud.compute.vo.ComputeVO;

public interface ComputeGroupManager<CGVO, DS, CVO> {

	public String getComputeGroupKey(String region, String computeGroupName);

	public CGVO createComputeGroup(String region, String computeGroupName);

	public CGVO changeComputeGroupName(CGVO targetComputeGroupVO,
			String newComputeGroupName);

	public CGVO loadComputeGroup(DS dataSource);

	public boolean saveComputeGroup(CGVO computeGroupVO);

	public boolean deleteComputeGroup(CGVO computeGroupVO);

	public CGVO getComputeGroup(String region, String computeGroupName);

	public boolean addComputeGroup(CGVO computeGroupVO);

	public boolean addCompute(CGVO computeGroupVO, CVO computeVO);

	public CVO getCompute(CGVO computeGroupVO, String computeID);

	public boolean removeCompute(CGVO computeGroupVO, String computeID);

	public boolean changeComputeName(CGVO computeGroupVO, String computeID,
			String newComputeName);

	public boolean createSecurityGroup(String region, String securityGroup);

	public boolean isExistingSecurityGroup(String region, String securityGroup);

	public boolean deleteSecurityGroup(String region, String securityGroup);

	public String getSecurityGroupRules(String region, String securityGroup);

	public String getAllSecurityGroupsRules(String region);

	public boolean registerSecurityRule(String region, String securityGroup,
			String tcpOrUdp, String portRange, String cidrRange);

	public boolean removeSecurityRule(String region, String securityGroup,
			String tcpOrUdp, String portRange, String cidrRange);

	public boolean registerSecurityICMPRule(String region,
			String securityGroup, String cidrRange);

	public boolean removeSecurityRuleForICMP(String region,
			String securityGroup, String cidrRange);

	public String createKeypair(String region, String keypair);

	public boolean deleteKeypair(String region, String keypair);

	public String[] getAllKeypairs(String region);

	public boolean isExistingKeypair(String region, String keypair);

	public boolean startComputeGroup(CGVO computeGroupVO);

	public boolean stopComputeGroup(CGVO computeGroupVO);

	public boolean rebootComputeGroup(CGVO computeGroupVO);

	public boolean terminateComputeGroup(CGVO computeGroupVO);

	public ComputeVO[] provisionComputeGroup(CGVO computeGroupVO);

	public CVO[] getComputes(CGVO computeGroupVO);

	public Map<String, CGVO> getComputeGroupList();

	public CVO createCompute(Map<String, String> requiredValues);

	public CVO[] createComputes(Map<String, String> requiredValues,
			int numOfComputes);

	public CVO updateComputeStatus(ComputeVO computeVO);

	public CVO provisionCompute(ComputeVO computeVO);

	public boolean stopCompute(ComputeVO computeVO);

	public boolean startCompute(ComputeVO computeVO);

	public boolean rebootCompute(ComputeVO computeVO);

	public boolean terminateCompute(ComputeVO computeVO);

}
