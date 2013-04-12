package com.jmcloud.compute.manager;

public interface RegionManager {
	public String[] getRegions();

	public String[] getAllRegions();

	public void addRegions(String... regions);

	public void removeRegion(String region);
}
