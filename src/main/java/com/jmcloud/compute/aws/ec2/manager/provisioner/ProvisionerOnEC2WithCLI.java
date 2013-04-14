package com.jmcloud.compute.aws.ec2.manager.provisioner;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.commons.RunCLI;
import com.jmcloud.compute.manager.provisioner.ProvisionerOnCloud;
import com.jmcloud.compute.sys.SystemString;

@Service("provisionerOnEC2WithCLI")
public class ProvisionerOnEC2WithCLI implements ProvisionerOnCloud {

	@Resource(name = "runEC2CLI")
	private RunCLI runEC2CLI;

	private final String falseReasonA = "Error or exeption occur!";

	private final String falseReasonB = "There is no answer!";

	private String buildFinalCommand(String... strings) {
		return returnSpaceSeperatedString(strings);
	}

	private String returnSpaceSeperatedString(String[] strings) {
		StringBuffer sb = new StringBuffer();
		for (String s : strings) {
			sb.append(s);
			sb.append(" ");
		}
		return sb.substring(0, sb.length() - 1);
	}

	private boolean returnBooleanAfterRun(String command) {
		if (!runEC2CLI.run(command)) {
			RunCLI.logger.error(falseReasonA);
			return false;
		}
		if ("".equals(runEC2CLI.getResultOut())) {
			RunCLI.logger.error(falseReasonB);
			return false;
		}
		return true;
	}

	@Override
	public String runEC2CLI(String command) {
		return returnBooleanAfterRun(command) ? runEC2CLI.getResultOut()
				: SystemString.FALSE;
	}

	@Override
	public String describeRegions() {
		return runEC2CLI("ec2-describe-regions");
	}

	@Override
	public String describeAvailabilityZones(String region) {
		return runEC2CLI(buildFinalCommand("ec2-describe-availability-zones",
				"--region", region));
	}

	@Override
	public String createKeypair(String region, String keypair) {
		return runEC2CLI(buildFinalCommand("ec2-create-keypair", keypair,
				"--region", region));
	}

	@Override
	public boolean deleteKeypair(String region, String keypair) {
		return returnBooleanAfterRun(buildFinalCommand("ec2-delete-keypair",
				keypair, "--region", region));
	}

	@Override
	public String getAllKeypairs(String region) {
		return runEC2CLI(buildFinalCommand("ec2-describe-keypairs", "--region",
				region));
	}

	@Override
	public String searchKeypair(String region, String keypairWithWildcard) {
		return runEC2CLI(buildFinalCommand("ec2-describe-keypairs",
				"-F \"key-name=" + keypairWithWildcard + "\"", "--region",
				region));
	}

	@Override
	public String createSecurityGroup(String region, String securityGroup) {
		return runEC2CLI(buildFinalCommand("ec2-create-group", securityGroup,
				"-d", securityGroup, "--region", region));
	}

	@Override
	public boolean deleteSecurityGroup(String region, String securityGroup) {
		return returnBooleanAfterRun(buildFinalCommand("ec2-delete-group",
				securityGroup, "--region", region));
	}

	@Override
	public String getAllSecurityGroups(String region) {
		return runEC2CLI(buildFinalCommand("ec2-describe-group", "--region",
				region));
	}

	@Override
	public String searchSecurityGroup(String region, String keypairWithWildcard) {
		return runEC2CLI(buildFinalCommand("ec2-describe-group",
				"-F \"group-name=" + keypairWithWildcard + "\"", "--region",
				region));
	}

	@Override
	public boolean registerSecurityRule(String region, String securityGroup,
			String tcpOrUdp, String portRange, String cidrRange) {
		return returnBooleanAfterRun(buildFinalCommand("ec2-authorize",
				securityGroup, "-P", tcpOrUdp, "-p", portRange, "-s",
				cidrRange, "--region", region));
	}

	@Override
	public boolean removeSecurityRule(String region, String securityGroup,
			String tcpOrUdp, String portRange, String cidrRange) {
		return returnBooleanAfterRun(buildFinalCommand("ec2-revoke",
				securityGroup, "-P", tcpOrUdp, "-p", portRange, "-s",
				cidrRange, "--region", region));
	}

	@Override
	public boolean registerSecurityRuleForICMP(String region,
			String securityGroup, String cidrRange) {
		return returnBooleanAfterRun(buildFinalCommand("ec2-authorize",
				securityGroup, "-P icmp", "-s", cidrRange, "-t -1:-1",
				"--region", region));
	}

	@Override
	public boolean removeSecurityRuleForICMP(String region,
			String securityGroup, String cidrRange) {
		return returnBooleanAfterRun(buildFinalCommand("ec2-revoke",
				securityGroup, "-P icmp", "-s", cidrRange, "-t -1:-1",
				"--region", region));
	}

	@Override
	public String serchImageID(String region, String i386ORx86_64,
			String nameWithWildcard, String otherOptions, boolean isWindows) {
		return runEC2CLI(buildFinalCommand("ec2-describe-images",
				"-a -F \"image-type=machine\"", "-F \"architecture="
						+ i386ORx86_64 + "\"", "-F \"manifest-location="
						+ nameWithWildcard + "\"", otherOptions,
				isWindows ? "-F \"platform=windows\"" : "", "--region", region));
	}

	@Override
	public String createCompute(String region, String imageID,
			String numOfCompute, String securityGroup, String keypair,
			String computeType) {
		return runEC2CLI(buildFinalCommand("ec2-run-instances", imageID, "-n",
				numOfCompute, "-g", securityGroup, "-k", keypair, "-t",
				computeType, "--region", region));
	}

	@Override
	public String getAllComputesInfo(String region) {
		return runEC2CLI(buildFinalCommand("ec2-describe-instances",
				"--region", region));
	}

	@Override
	public String getComputeInfo(String region, String computeID) {
		return runEC2CLI(buildFinalCommand("ec2-describe-instances", computeID,
				"--region", region));
	}

	@Override
	public String getComputesInfoByTag(String region, String tagKey,
			String tagValue) {
		return runEC2CLI(buildFinalCommand("ec2-describe-instances",
				"-F \"tag-key=" + tagKey + "\"", "-F \"tag-value=" + tagValue
						+ "\"", "--region", region));
	}

	@Override
	public String createTag(String region, String tagKey, String tagValue,
			String... resourceIDs) {
		return runEC2CLI(buildFinalCommand("ec2-create-tags",
				returnSpaceSeperatedString(resourceIDs), "--tag", "\"" + tagKey
						+ "=" + tagValue + "\"", "--region", region));
	}

	@Override
	public boolean deleteTag(String region, String tagKey, String tagValue,
			String... resourceIDs) {
		return returnBooleanAfterRun(buildFinalCommand("ec2-delete-tags",
				returnSpaceSeperatedString(resourceIDs), "--tag", "\"" + tagKey
						+ "=" + tagValue + "\"", "--region", region));
	}

	@Override
	public String findComputesWithTagKeyValue(String region, String tagKey,
			String tagValue) {
		return runEC2CLI(buildFinalCommand("ec2-describe-tags", "-F \"key="
				+ tagKey + "\"", "-F \"value=" + tagValue + "\"",
				"-F \"resource-type=instance\"", "--region", region));
	}

	@Override
	public String findComputesWithTagKey(String region, String tagKey) {
		return runEC2CLI(buildFinalCommand("ec2-describe-tags", "-F \"key="
				+ tagKey + "\"", "-F \"resource-type=instance\"", "--region",
				region));
	}

	@Override
	public String findComputesWithTagValue(String region, String tagValue) {
		return runEC2CLI(buildFinalCommand("ec2-describe-tags", "-F \"value="
				+ tagValue + "\"", "-F \"resource-type=instance\"", "--region",
				region));
	}

	@Override
	public String findComputesWithAllTags(String region) {
		return runEC2CLI(buildFinalCommand("ec2-describe-tags",
				"-F \"resource-type=instance\"", "--region", region));
	}

	@Override
	public boolean stopCompute(String region, String... computeIDs) {
		return returnBooleanAfterRun(buildFinalCommand("ec2-stop-instances",
				returnSpaceSeperatedString(computeIDs), "--region", region));
	}

	@Override
	public boolean startCompute(String region, String... computeIDs) {
		return returnBooleanAfterRun(buildFinalCommand("ec2-start-instances",
				returnSpaceSeperatedString(computeIDs), "--region", region));
	}

	@Override
	public boolean rebootCompute(String region, String... computeIDs) {
		return returnBooleanAfterRun(buildFinalCommand("ec2-reboot-instances",
				returnSpaceSeperatedString(computeIDs), "--region", region));
	}

	@Override
	public boolean terminateCompute(String region, String... computeIDs) {
		return returnBooleanAfterRun(buildFinalCommand(
				"ec2-terminate-instances",
				returnSpaceSeperatedString(computeIDs), "--region", region));
	}
}
