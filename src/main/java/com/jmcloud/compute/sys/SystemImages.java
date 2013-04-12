package com.jmcloud.compute.sys;

import java.net.URL;

import javax.swing.ImageIcon;

public class SystemImages {
	private static final String imagesDir = SystemEnviroment.getImagesDir();

	private static final String flagImagesDir = SystemEnviroment
			.getFlagImagesDir();

	public static URL getFlagImageURL(String contryCodeDomain) {
		return ClassLoader.getSystemResource(flagImagesDir + contryCodeDomain
				+ ".png");
	}

	private static ImageIcon getImage(String imageFileName) {
		return new ImageIcon(ClassLoader.getSystemResource(imagesDir
				+ imageFileName));
	}

	public static ImageIcon getSpinnerImage() {
		return getImage("pending-spinner.gif");
	}

	private static ImageIcon mainImage = getImage("jmcloud-main.png");

	public static ImageIcon getMainImage() {
		return mainImage;
	}

	private static ImageIcon computeGroupImage = getImage("compute-group-small.png");

	public static ImageIcon getComputeGroupImage() {
		return computeGroupImage;
	}

}
