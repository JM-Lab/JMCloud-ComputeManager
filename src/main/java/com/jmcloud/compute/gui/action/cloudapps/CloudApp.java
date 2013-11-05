package com.jmcloud.compute.gui.action.cloudapps;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

public interface CloudApp {
	
	public static Logger logger = Logger.getLogger(CloudApp.class);

	abstract public void luanchApp(JFrame mainFrame, String cloudAppRootDir, String publicIP, String keypair, String id, String luanchPackName);
	abstract public void connectApp();

}