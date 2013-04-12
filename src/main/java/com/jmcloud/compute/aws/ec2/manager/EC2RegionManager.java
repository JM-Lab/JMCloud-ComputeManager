package com.jmcloud.compute.aws.ec2.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.aws.ec2.util.EC2Util;
import com.jmcloud.compute.manager.RegionManager;
import com.jmcloud.compute.manager.provisioner.ProvisionerOnCloud;

@Service("eC2RegionManager")
public class EC2RegionManager implements RegionManager {

	@Resource(name = "provisionerOnEC2WithCLI")
	private ProvisionerOnCloud provisioner;

	private List<String> eC2RegionList = new ArrayList<String>();

	@Override
	public String[] getAllRegions() {
		String returnString = provisioner.describeRegions();
		if (EC2Util.isExistFalse(returnString)) {
			return new String[0];
		}
		String[] regions = returnString.split("\n");
		for (int i = 0; i < regions.length; i++) {
			regions[i] = regions[i].split("\t")[1];
		}
		return regions;
	}

	@Override
	public void addRegions(String... regions) {
		for (String region : regions) {
			eC2RegionList.add(region);
		}
	}

	@Override
	public void removeRegion(String region) {
		eC2RegionList.remove(region);
	}

	@Override
	public String[] getRegions() {
		return eC2RegionList.toArray(new String[eC2RegionList.size()]);
	}

}
