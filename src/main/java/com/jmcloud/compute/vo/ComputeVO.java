package com.jmcloud.compute.vo;

import com.jmcloud.compute.aws.ec2.util.EC2Util;

public class ComputeVO {

	private String computeName;

	private String computeID;

	private String imageID;

	private String computeType;

	private String keypairName;

	private String securityGroup;

	private String computeLaunchTime;

	private String privateIP;

	private String publicIP;

	private String region;

	private String computeGroupName;

	private String platform;

	private String status;

	@Override
	public String toString() {
		return EC2Util.gson.toJson(this);
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}

	public String getComputeName() {
		return computeName;
	}

	public void setComputeName(String computeName) {
		this.computeName = computeName;
	}

	public String getComputeID() {
		return computeID;
	}

	public void setComputeID(String computeID) {
		this.computeID = computeID;
	}

	public String getImageID() {
		return imageID;
	}

	public void setImageID(String imageID) {
		this.imageID = imageID;
	}

	public String getComputeType() {
		return computeType;
	}

	public void setComputeType(String computeType) {
		this.computeType = computeType;
	}

	public String getKeypairName() {
		return keypairName;
	}

	public void setKeypairName(String keypairName) {
		this.keypairName = keypairName;
	}

	public String getSecurityGroup() {
		return securityGroup;
	}

	public void setSecurityGroup(String securityGroup) {
		this.securityGroup = securityGroup;
	}

	public String getComputeLaunchTime() {
		return computeLaunchTime;
	}

	public void setComputeLaunchTime(String computeLaunchTime) {
		this.computeLaunchTime = computeLaunchTime;
	}

	public String getPrivateIP() {
		return privateIP;
	}

	public void setPrivateIP(String privateIP) {
		this.privateIP = privateIP;
	}

	public String getPublicIP() {
		return publicIP;
	}

	public void setPublicIP(String publicIP) {
		this.publicIP = publicIP;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getComputeGroupName() {
		return computeGroupName;
	}

	public void setComputeGroupName(String computeGroupName) {
		this.computeGroupName = computeGroupName;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
