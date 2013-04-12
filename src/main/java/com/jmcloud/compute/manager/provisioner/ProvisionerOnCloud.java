package com.jmcloud.compute.manager.provisioner;

public interface ProvisionerOnCloud {

	public String runEC2CLI(String command);

	public String describeRegions();

	public String describeAvailabilityZones(String region);

	public String createKeypair(String region, String keypair);

	public boolean deleteKeypair(String region, String keypair);

	public String getAllKeypairs(String region);

	public String searchKeypair(String region, String keypairWithWildcard);

	public String createSecurityGroup(String region, String securityGroup);

	public boolean deleteSecurityGroup(String region, String securityGroup);

	public String getAllSecurityGroups(String region);

	public String searchSecurityGroup(String region, String keypairWithWildcard);

	public boolean registerSecurityRule(String region, String securityGroup,
			String tcpOrUdp, String portRange, String cidrRange);

	public boolean removeSecurityRule(String region, String securityGroup,
			String tcpOrUdp, String portRange, String cidrRange);

	public boolean registerSecurityRuleForICMP(String region,
			String securityGroup, String cidrRange);

	public boolean removeSecurityRuleForICMP(String region,
			String securityGroup, String cidrRange);

	public String serchImageID(String region, String i386ORx86_64,
			String nameWithWildcard, String otherOptions, boolean isWindows);

	public String createCompute(String region, String imageID,
			String numOfCompute, String securityGroup, String keypair,
			String computeType);

	public String getAllComputesInfo(String region);

	public String getComputeInfo(String region, String computeID);

	public String getComputesInfoByTag(String region, String tagKey,
			String tagValue);

	public String createTag(String region, String tagKey, String tagValue,
			String... resourceIDs);

	public boolean deleteTag(String region, String tagKey, String tagValue,
			String... resourceIDs);

	public String findComputesWithTagKeyValue(String region, String tagKey,
			String tagValue);

	public String findComputesWithTagKey(String region, String tagKey);

	public String findComputesWithTagValue(String region, String tagValue);

	public String findComputesWithAllTags(String region);

	public boolean stopCompute(String region, String... computeIDs);

	public boolean startCompute(String region, String... computeIDs);

	public boolean rebootCompute(String region, String... computeIDs);

	public boolean terminateCompute(String region, String... computeIDs);

}