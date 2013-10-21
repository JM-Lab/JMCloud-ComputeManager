package com.jmcloud.compute.sys;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SystemCloudApp {

	private static final String CLOUD_APP_DIR = SystemEnviroment.getCloudAppDir();

	private static URL getLuanchPackFileURL(String luanchPackName) {
		return ClassLoader.getSystemResource(CLOUD_APP_DIR + luanchPackName);
	}

	public static Path getLuanchPackFile(String luanchPackName) {
		try {
			return Paths.get(getLuanchPackFileURL(luanchPackName).toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
}
