package com.jmcloud.compute.gui.action;

import static com.jmcloud.compute.aws.ec2.manager.EC2Manager.RUNNING_STATUS;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.GROUP_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.KEYPAIR_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.PUBLIC_IP_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.REGION_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.STATUS_INDEX;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.annotation.Resource;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.gui.action.cloudapps.CloudApp;
import com.jmcloud.compute.sys.SystemEnviroment;
import com.jmcloud.compute.util.SysUtils;

@Service("luanchCloudAppAction")
public class LuanchCloudAppAction extends AbstractJMCloudGUIAction {
	@Resource(name = "rServer2")
	private CloudApp rServer;

	private CloudApp hadoopPsudo;

	// id 는 현재 ubuntu 만 지원
	private final String id = "ubuntu";

	private final String cloudAppRootDir = "cloudapp/";

	protected int selectionRow;
	protected int[] selectionRows;

	private String keypair;

	private String publicIP;

	private String region;

	private String group;

	@Override
	protected void initAction(ActionEvent e) {
		super.initAction(e);
		this.selectionRow = computeManagerGUIModel.getSelectionRow();
		this.selectionRows = computeManagerGUIModel.getSelectionRows();
	}

	@Override
	protected String doAbstractAction(ActionEvent e) {
		String result = checkEnv(e);
		if (result.contains(FAILURE_SIGNATURE)) {
			return result;
		}
		
		publicIP = computeManagerGUIModel.getComputeInfo(selectionRow,
				PUBLIC_IP_INDEX);
		keypair = SystemEnviroment.getKeypairDir()
				+ computeManagerGUIModel.getComputeInfo(selectionRow,
						KEYPAIR_INDEX);
		region = computeManagerGUIModel.getComputeInfo(selectionRow,
				REGION_INDEX);
		group = computeManagerGUIModel
				.getComputeInfo(selectionRow, GROUP_INDEX);

		if ("Luanch R Server".equals(e.getActionCommand())) {
			result = luanchApp(rServer);
		} else if ("Luanch Hadoop Psudo".equals(e.getActionCommand())) {
			result = luanchApp(hadoopPsudo);
		} else {
			result = FAILURE_SIGNATURE;
		}
		return result;
	}

	private String checkEnv(ActionEvent e) {
		if (selectionRows.length != 1) {
			return returnErrorMessage("Must Select Only One Compute On The Table View!!!");
		}
		if (!RUNNING_STATUS.equals(computeManagerGUIModel.getComputeInfo(
				selectionRow, STATUS_INDEX))) {
			return returnErrorMessage("Only Running Status, Can Connet Compute!!!");
		}

		return SUCCESS_SIGNATURE;
	}

	private String luanchApp(final CloudApp cloudApp) {
		int result = JOptionPane.showConfirmDialog(mainFrame,
				"Do you want to set security rule for the app?",
				"Set Security Rule For The App", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE);
		startProgressSpinner();
		if (result == JOptionPane.OK_OPTION) {
			setSecurityRuleForApp(cloudApp.getPortRange());
		}
		cloudApp.initCloudApp(mainFrame, region, group, cloudAppRootDir,
				publicIP, keypair, id);
		showLineOnInfoView(resultHeader + "\tStart Cloud App");
		stopProgressSpinner();
		cloudApp.startCloudApp();
		cloudApp.luanchCloudApp();
		
		return SUCCESS_SIGNATURE;
	}

	private void setSecurityRuleForApp(String portRange) {
		if (computeManagerGUIModel.addSecurityRule(regionOfselectionGroup,
				selectionGroup, "tcp", portRange, "0.0.0.0/0")) {
			showLineOnInfoView(resultHeader + "\tCreate A Security Rule For Cloud App");
		} else {
			showLineOnInfoView(resultHeader
					+ "\tCan't Create A Security Rule !!!");
		}
	}

}
