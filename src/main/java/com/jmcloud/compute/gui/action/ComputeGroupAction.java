package com.jmcloud.compute.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.gui.component.DialogsUtil;

@Service("computeGroupAction")
public class ComputeGroupAction extends AbstractJMCloudGUIAction {

	@Override
	protected String doAbstractAction(ActionEvent e) {
		String result;
		switch (e.getActionCommand()) {
		case "Create Group":
			result = createGroup();
			break;
		case "Rename Group":
			result = renameGroup();
			break;
		default:
			result = groupsAction(e.getActionCommand());
			break;
		}
		computeManagerGUIModel.updateTree();
		saveAfterGroupAction();
		return result;
	}

	private void saveAfterGroupAction() {
		selectionGroupTreePaths = computeManagerGUIModel
				.getSelectionGroupTreePaths();
		if (selectionGroupTreePaths != null
				&& selectionGroupTreePaths.size() > 0) {
			for (TreePath groupTreePath : selectionGroupTreePaths) {
				computeManagerGUIModel.saveGroup(
						getTreeNodeName(groupTreePath.getParentPath()),
						getTreeNodeName(groupTreePath));
			}
		}

	}

	private String createGroup() {
		if (!isSelectedOnlyRegion()) {
			return returnErrorMessage("Must Select Only A Region On The Tree View!!!");
		}
		String selectionRegion = getTreeNodeName(selectionRegionTreePath);
		String[] inputResults = DialogsUtil.showCreateComputeGroupInputDialog(
				mainFrame, actionCommand, selectionRegion);
		if (inputResults.length == 0) {
			return returnErrorMessage(CANCEL_SIGNATURE);
		}
		String computeGroupName = inputResults[0];
		if (computeGroupName.contains(" ")) {
			return returnErrorMessage("Can't Create The Compute Group!!!, Warn : Not Allowed To Create Group Name With a Space!");
		}

		boolean isSelectedDefaultSecurityRules = new Boolean(inputResults[1])
				.booleanValue();

		startProgressSpinner();
		if (!computeManagerGUIModel.createGroup(computeGroupName)) {
			return returnErrorMessage("Can't Create The Compute Group!!!, Warn : Not Allowed To Create Same Group Name!");
		}

		computeManagerGUIModel.showProgressResult("[Create Security Group] "
				+ computeManagerGUIModel.getSecurityGroup(selectionRegion,
						computeGroupName));
		if (!doProgressMethod(computeManagerGUIModel.createSecurityGroup(
				selectionRegion, computeGroupName))) {
			computeManagerGUIModel.deleteGroup(selectionRegion,
					computeGroupName);
		}

		if (isSelectedDefaultSecurityRules) {
			computeManagerGUIModel
					.showProgressResult("[Create Security Rule] SSH From Any ");
			doProgressMethod(computeManagerGUIModel
					.addSecurityRule(selectionRegion, computeGroupName, "TCP",
							"22", "0.0.0.0/0"));
			computeManagerGUIModel
					.showProgressResult("[Create Security Rule] Ping From Any ");
			doProgressMethod(computeManagerGUIModel.addSecurityRule(
					selectionRegion, computeGroupName, "ICMP", "", "0.0.0.0/0"));

		}

		computeManagerGUIModel.showProgressResult("[Create Keypair] "
				+ computeManagerGUIModel.getGroupKeypair(selectionRegion,
						computeGroupName));
		if (!doProgressMethod(computeManagerGUIModel.createKeypair(
				selectionRegion, computeGroupName))) {
			computeManagerGUIModel.deleteSecurityGroup(selectionRegion,
					computeGroupName);
			computeManagerGUIModel.deleteGroup(selectionRegion,
					computeGroupName);
		}
		computeManagerGUIModel.updateTree();
		return returnSuccessMessage(computeGroupName);
	}

	private String renameGroup() {
		if (!isSelectedOnlyGroup()) {
			return returnErrorMessage("Must Select Only One Group On The Tree View!!!");
		}
		String newGroupName = showInputDialog("Input A New Group Name!");
		if (newGroupName == null) {
			return returnErrorMessage(CANCEL_SIGNATURE);
		} else if ("".equals(newGroupName)) {
			return returnErrorMessage("NO_INPUT_SIGNATURE");
		}
		startProgressSpinner();
		TreePath selectionGroupTreePath = computeManagerGUIModel
				.getSelectionGroupTreePaths().get(0);
		if (!computeManagerGUIModel.renameGroup(
				getTreeNodeName(selectionGroupTreePath.getParentPath()),
				getTreeNodeName(selectionGroupTreePath), newGroupName)) {
			return returnErrorMessage("Can't Rename A Group Name!!!, Warn : Not Allowed To Create Same Group Name!");
		}
		return returnSuccessMessage(newGroupName);
	}

	private String groupsAction(String command) {
		if (selectionGroupTreePaths.size() == 0
				|| selectionRegionTreePaths.size() != 0) {
			return returnErrorMessage("Must Select Only Group(s) On The Tree View!!!");
		}

		int option = showComfirmDialog("Do You Want To " + command + "?");
		if (option != JOptionPane.OK_OPTION) {
			return returnErrorMessage(CANCEL_SIGNATURE);
		}
		startProgressSpinner();
		StringBuffer actionGroups = new StringBuffer();
		for (TreePath groupTreePath : selectionGroupTreePaths) {
			String groupName = getTreeNodeName(groupTreePath);
			if (doGroupAction(command, groupTreePath)) {
				actionGroups.append("\t" + groupName);
			}
		}
		return returnSuccessMessage(actionGroups.toString());

	}

	private boolean doGroupAction(String command, TreePath groupTreePath) {
		String region = getTreeNodeName(groupTreePath.getParentPath());
		String groupName = getTreeNodeName(groupTreePath);
		switch (command) {
		case "Delete Group":
			return deleteGroup(region, groupName);
		case "Provision Group":
			return provisionGroup(region, groupName);
		case "Stop Group":
			return stopGroup(region, groupName);
		case "Start Group":
			return startGroup(region, groupName);
		case "Reboot Group":
			return rebootGroup(region, groupName);
		case "Terminate Group":
			return terminateGroup(region, groupName);
		default:
			return false;
		}

	}

	private boolean terminateGroup(String region, String groupName) {
		return computeManagerGUIModel.terminateGroup(region, groupName);
	}

	private boolean rebootGroup(String region, String groupName) {
		return computeManagerGUIModel.rebootGroup(region, groupName);
	}

	private boolean startGroup(String region, String groupName) {
		return computeManagerGUIModel.startGroup(region, groupName);
	}

	private boolean stopGroup(String region, String groupName) {
		return computeManagerGUIModel.stopGroup(region, groupName);
	}

	private boolean provisionGroup(String region, String groupName) {
		return computeManagerGUIModel.provisionGroup(region, groupName);
	}

	private boolean deleteGroup(String region, String groupName) {
		if (!computeManagerGUIModel.isGroupTerminated(region, groupName)) {
			computeManagerGUIModel
					.showResult(returnErrorMessage(groupName
							+ "Can't Be Deleted!!!, You Should Terminate Group First!"));
			return false;
		}
		computeManagerGUIModel.showProgressResult("[Delete Security Group] "
				+ computeManagerGUIModel.getSecurityGroup(region, groupName));
		doProgressMethod(computeManagerGUIModel.deleteSecurityGroup(region,
				groupName));
		computeManagerGUIModel.showProgressResult("[Delete Keypair] "
				+ computeManagerGUIModel.getGroupKeypair(region, groupName));
		doProgressMethod(computeManagerGUIModel
				.deleteKeypair(region, groupName));
		return computeManagerGUIModel.deleteGroup(region, groupName);
	}

}
