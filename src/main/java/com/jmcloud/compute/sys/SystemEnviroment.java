package com.jmcloud.compute.sys;

import java.io.File;
import java.nio.file.Path;
import java.util.Properties;

import com.jmcloud.compute.util.SysUtils;

public class SystemEnviroment {

	private static final Properties sysEnvProperty = SysUtils
			.getProperties("sys/sys-env.properties");

	private static final String userHome = System.getProperty("user.home") == null ? ""
			: System.getProperty("user.home") + File.separatorChar;

	private static String getString(String key) {
		return sysEnvProperty.getProperty(key);
	}

	public static String getImagesDir() {
		return getString("IMAGES_DIR");
	}

	public static String getFlagImagesDir() {
		return getString("FLAG_IMAGES_DIR");
	}

	public static String getSpringConfPath() {
		return getString("SPRING_CONF_PATH");
	}

	public static String getRegionCityMappingPath() {
		return getString("REGION_CITY_MAPPING_PATH");
	}

	public static String getRegionContryMappingPath() {
		return getString("REGION_CONTRY_MAPPING_PATH");
	}

	public static String getDefaultEC2EnvPath() {
		return getString("DEFAULT_EC2_ENV_PATH");
	}

	public static String getUserEC2EnvPath() {
		System.out.println(userHome + getString("USER_EC2_ENV_PATH"));
		return userHome + getString("USER_EC2_ENV_PATH");
	}

	public static String getKeypairDir() {
		return userHome + getString("KEY_PAIR_DIR");
	}

	public static String getDataSaveDir() {
		return userHome + getString("DATA_SAVE_DIR");
	}
}
