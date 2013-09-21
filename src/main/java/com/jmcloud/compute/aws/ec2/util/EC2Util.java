package com.jmcloud.compute.aws.ec2.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.jmcloud.compute.sys.SystemString;

public class EC2Util {

	public static Gson gson = new Gson();

	public static boolean isExistFalse(String returnString) {
		return returnString.startsWith(SystemString.FALSE);
	}

	public static final String GROUP_INFO_SPLIT_KEY = "RESERVATION\t";

	public static String[] extractGroupsInfo(String computesInfo) {
		List<String> groupsInfoList = new ArrayList<String>();
		String[] tempString = computesInfo.split(GROUP_INFO_SPLIT_KEY);
		for (int i = 1; i < tempString.length; i++) {
			groupsInfoList.add(GROUP_INFO_SPLIT_KEY + tempString[i]);
		}
		return groupsInfoList.toArray(new String[groupsInfoList.size()]);
	}

	public static final String INSTANCE_INFO_SPLIT_KEY = "INSTANCE\t";

	public static String[] extractInstancesInfo(String groupInfo) {
		List<String> computesInfoList = new ArrayList<String>();
		String[] tempString = groupInfo.split(INSTANCE_INFO_SPLIT_KEY);
		for (int i = 1; i < tempString.length; i++) {
			computesInfoList.add(INSTANCE_INFO_SPLIT_KEY + tempString[i]);
		}
		return computesInfoList.toArray(new String[computesInfoList.size()]);
	}

	// public static String[] extractInstancesInfo(String groupInfo) {
	// List<String> computesInfoList = new ArrayList<String>();
	// String[] tempStrings = groupInfo.split("\n");
	// for (String tempString : tempStrings) {
	// if(tempString.startsWith(INSTANCE_INFO_SPLIT_KEY)){
	// computesInfoList.add(tempString);
	// }
	// }
	// return computesInfoList.toArray(new String[computesInfoList.size()]);
	// }

	public static String extractSecurityRules(String securityGroupInfo) {
		StringBuffer sb = new StringBuffer();
		for (String rule : securityGroupInfo.split("\n")) {
			if (rule.startsWith("PERMISSION")) {
				sb.append(rule.split("\t", 3)[2]);
				sb.append("\n");
			}
		}
		return sb.substring(0, sb.length() - 1);
	}

	public static String[] extractSecurityGroups(String securityGroupsInfo) {
		List<String> securityGroupList = new ArrayList<String>();
		for (String rule : securityGroupsInfo.split("\n")) {
			if (rule.startsWith("GROUP")) {
				securityGroupList.add(rule.split("\t", 5)[3]);
			}
		}
		return securityGroupList.toArray(new String[securityGroupList.size()]);
	}

	public static String[] extractKeypairs(String keypairsInfo) {
		List<String> keypairsList = new ArrayList<String>();
		for (String keypairInfo : keypairsInfo.split("\n")) {
			if (keypairInfo.startsWith("KEYPAIR")) {
				keypairsList.add(keypairInfo.split("\t", 3)[1]);
			}
		}
		return keypairsList.toArray(new String[keypairsList.size()]);
	}

	public final static String COMPUTE_NAME = "computeName";
	public final static String IMAGE_ID = "imageID";
	public final static String SECURITYGROUP = "securityGroup";
	public final static String KEYPAIR = "keypair";
	public final static String COMPUTE_TYPE = "computeType";
	public final static String REGION = "region";
	public final static String COMPUTE_GROUP_NAME = "computeGroupName";

	public static Map<String, String> makeRequiredValus(String computeName,
			String imageID, String securityGroup, String keypair,
			String computeType, String region, String computeGroupName) {
		Map<String, String> requiredValues = new HashMap<String, String>();
		requiredValues.put(COMPUTE_NAME, computeName);
		requiredValues.put(IMAGE_ID, imageID);
		requiredValues.put(SECURITYGROUP, securityGroup);
		requiredValues.put(KEYPAIR, keypair);
		requiredValues.put(COMPUTE_TYPE, computeType);
		requiredValues.put(REGION, region);
		requiredValues.put(COMPUTE_GROUP_NAME, computeGroupName);
		return requiredValues;
	}

	public static String extractComputeName(String computeInfo) {
		for (String s : computeInfo.split("\n")) {
			if (s.startsWith("TAG\t") && s.contains("Name\t")) {
				return s.split("\t")[4];
			}
		}
		return SystemString.FALSE;
	}

	public static String extractSecurityGroup(String instanceInfo) {
		for (String s : instanceInfo.split("\n")) {
			if (s.startsWith("RESERVATION\t")) {
				String[] tempStrings = s.split("\t");
				if(tempStrings != null && tempStrings.length >= 4){
					return s.split("\t")[3];
				}
			}else if(s.startsWith("GROUP\t")){
				String[] tempStrings = s.split("\t");
				if(tempStrings != null && tempStrings.length >= 3){
					return s.split("\t")[2];
				}
			}
		}
		return SystemString.FALSE;
	}

	private final static String INSTANCE_INDENTIFIER = "INSTANCE\t";

	private static String extractTargetInfo(String statusString,
			String identifier, int indexNum) {
		for (String s : statusString.split("\n")) {
			if (s.startsWith(identifier)) {
				return s.split("\t")[indexNum];
			}
		}
		return SystemString.FALSE;
	}

	public static String extractPublicIP(String instanceInfo) {
		String ec2HostName = extractTargetInfo(instanceInfo,
				INSTANCE_INDENTIFIER, 3);
		return extractIPFromHostName(ec2HostName);
	}

	private static String extractIPFromHostName(String ec2HostName) {
		if (ec2HostName == null || "".equals(ec2HostName)) {
			return "";
		}
		String[] tempIPs = ec2HostName.split("\\.");

		return tempIPs[0].substring(tempIPs[0].indexOf("-") + 1).replace("-",
				".");
	}

	public static String extractPrivateIP(String instanceInfo) {
		// String ec2HostName = extractTargetInfo(computeInfo,
		// INSTANCE_INDENTIFIER, 4);
		// return extractIPFromHostName(ec2HostName);
		return extractTargetInfo(instanceInfo, INSTANCE_INDENTIFIER, 17);
	}

	public static String extractStatus(String instanceInfo) {
		return extractTargetInfo(instanceInfo, INSTANCE_INDENTIFIER, 5);
	}

	public static String extractComputeID(String instanceInfo) {
		return extractTargetInfo(instanceInfo, INSTANCE_INDENTIFIER, 1);
	}

	public static String extractImageID(String instanceInfo) {
		return extractTargetInfo(instanceInfo, INSTANCE_INDENTIFIER, 2);
	}

	public static String extractkeypair(String instanceInfo) {
		return extractTargetInfo(instanceInfo, INSTANCE_INDENTIFIER, 6);
	}

	public static String extractComputeType(String instanceInfo) {
		return extractTargetInfo(instanceInfo, INSTANCE_INDENTIFIER, 9);
	}

	public static String extractComputeLaunchTime(String instanceInfo) {
		return extractTargetInfo(instanceInfo, INSTANCE_INDENTIFIER, 10);
	}

	public static String extractPlatform(String instanceInfo) {
		return extractTargetInfo(instanceInfo, INSTANCE_INDENTIFIER, 14);
	}

	public static boolean saveStringIntoFile(Path filePath, String string) {
		if (filePath.toFile().exists()) {
			return false;
		}
		try {
			Files.createDirectories(filePath.getParent());
			Files.write(filePath, string.getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
