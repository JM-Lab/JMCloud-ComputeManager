package com.jmcloud.compute.gui.action;

import java.awt.event.ActionEvent;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service("computeActionSelector")
public class ComputeActionSelector implements JMCloudGUIAction {

	private static final String GUI_ACTION_LOG = "[GUI Action]\t";

	@Resource(name = "computeGroupAction")
	JMCloudGUIAction computeGroupAction;

	@Resource(name = "computeAction")
	JMCloudGUIAction computeAction;

	@Resource(name = "keypairAction")
	JMCloudGUIAction computeKeypairAction;

	@Resource(name = "securityGroupAction")
	JMCloudGUIAction securityGroupAction;

	@Resource(name = "systemAction")
	JMCloudGUIAction computeSystemAction;

	@Override
	public void doAction(ActionEvent e) {
		logger.info(GUI_ACTION_LOG + e.getActionCommand());
		if (e.getActionCommand().matches(".*Group.*")) {
			computeGroupAction.doAction(e);
		} else if (e.getActionCommand().matches(".*Compute.*")) {
			computeAction.doAction(e);
		} else if (e.getActionCommand().matches(".*Keypair.*")) {
			computeKeypairAction.doAction(e);
		} else if (e.getActionCommand().matches(".*Rule.*")) {
			securityGroupAction.doAction(e);
		} else {
			computeSystemAction.doAction(e);
		}
	}

}
