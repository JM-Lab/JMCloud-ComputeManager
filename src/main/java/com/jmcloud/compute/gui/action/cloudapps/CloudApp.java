package com.jmcloud.compute.gui.action.cloudapps;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

public interface CloudApp {
	
	public static Logger logger = Logger.getLogger(CloudApp.class);

	public void luanchApp();
	
	public void initAppManager(JFrame mainFrame, String region, String group, String cloudAppRootDir,
			String publicIP, String keypair, String id);
	
	public void showCloudAppManagerView();

	public String getPortRange();
}