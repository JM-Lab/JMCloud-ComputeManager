package com.jmcloud.compute.sys;

import java.util.Properties;

import com.jmcloud.compute.util.SysUtils;

public class SystemString {

	public static final String FALSE = "[FALSE]\t";

	public static final String KEYPAIR_SIGNATURE = "JM";

	public static final String JMCLOUD_SIGNATURE = ".jm";

	private static final Properties regionCityProperty = SysUtils
			.getProperties(SystemEnviroment.getRegionCityMappingPath());

	public static String getCity(String region) {
		return regionCityProperty.getProperty(region);
	}

	private static final Properties regionContryProperty = SysUtils
			.getProperties(SystemEnviroment.getRegionContryMappingPath());

	public static String getContry(String region) {
		return regionContryProperty.getProperty(region);
	}

}
