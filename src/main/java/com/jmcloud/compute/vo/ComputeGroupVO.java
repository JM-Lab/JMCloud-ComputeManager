package com.jmcloud.compute.vo;

import java.util.Hashtable;
import java.util.Map;

import com.jmcloud.compute.aws.ec2.util.EC2Util;

public class ComputeGroupVO {

	private String computeGroupName;

	private long creationTime;

	private String securityGroup;

	private String region;

	private String computeGroupKeypairName;

	private Map<String, ComputeVO> computesList = new Hashtable<String, ComputeVO>();

	@Override
	public String toString() {
		return EC2Util.gson.toJson(this);
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return toString().equals(obj.toString());
	}

	public String getComputeGroupName() {
		return computeGroupName;
	}

	public void setComputeGroupName(String computeGroupName) {
		this.computeGroupName = computeGroupName;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public String getSecurityGroup() {
		return securityGroup;
	}

	public void setSecurityGroup(String securityGroup) {
		this.securityGroup = securityGroup;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getGroupKeyPair() {
		return computeGroupKeypairName;
	}

	public void setComputeGroupKeyPair(String computeGroupKeypairName) {
		this.computeGroupKeypairName = computeGroupKeypairName;
	}

	public Map<String, ComputeVO> getComputesList() {
		return computesList;
	}

	public void setComputeList(Map<String, ComputeVO> computeList) {
		this.computesList = computeList;
	}

}
