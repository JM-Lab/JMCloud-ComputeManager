package com.jmcloud.compute.aws.ec2.sys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("eC2EnviromentVO")
public class EC2EnviromentVO {

	private String eC2CLIHome;

	private String accessKey;

	private String secretKey;

	@Value("#{defaultAWSEC2EnvProperties['DEFAULT_REGIONS']}")
	private String defaultRegions;

	@Value("#{defaultAWSEC2EnvProperties['INSTANCE_TYPES']}")
	private String instanceTypes;

	public String getEC2CLIHome() {
		return eC2CLIHome;
	}

	public void setEC2CLIHome(String eC2CLIHome) {
		this.eC2CLIHome = eC2CLIHome;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getDefaultRegions() {
		return defaultRegions;
	}

	public void setDefaultRegions(String defaultRegions) {
		this.defaultRegions = defaultRegions;
	}

	public String getInstanceTypes() {
		return instanceTypes;
	}

	public void setInstanceTypes(String instanceTypes) {
		this.instanceTypes = instanceTypes;
	}

}
