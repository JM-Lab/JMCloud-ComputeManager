package com.jmcloud.compute.gui.action;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

public interface JMCloudGUIAction {
	public static Logger logger = Logger.getLogger(JMCloudGUIAction.class);

	public void doAction(ActionEvent e);
}
