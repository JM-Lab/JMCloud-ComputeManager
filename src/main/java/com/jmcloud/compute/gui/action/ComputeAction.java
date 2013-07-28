package com.jmcloud.compute.gui.action;

import static com.jmcloud.compute.aws.ec2.manager.EC2Manager.NO_PROVISION_STATUS;
import static com.jmcloud.compute.aws.ec2.manager.EC2Manager.RUNNING_STATUS;
import static com.jmcloud.compute.aws.ec2.manager.EC2Manager.STOPPED_STATUS;
import static com.jmcloud.compute.aws.ec2.manager.EC2Manager.TERMINATED_STATUS;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.COMPUTE_ID_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.GROUP_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.KEYPAIR_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.PUBLIC_IP_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.REGION_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.STATUS_INDEX;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.swing.tree.TreePath;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.aws.ec2.sys.EC2EnviromentVO;
import com.jmcloud.compute.gui.component.DialogsUtil;
import com.jmcloud.compute.sys.SystemEnviroment;
import com.jmcloud.compute.vo.ComputeVO;

@Service("computeAction")
public class ComputeAction extends AbstractJMCloudGUIAction {

	@Resource(name = "eC2EnviromentVO")
	private EC2EnviromentVO eC2EnviromentVO;

	protected int selectionRow;
	protected int[] selectionRows;

	@Override
	protected void initAction(ActionEvent e) {
		super.initAction(e);
		this.selectionRow = computeManagerGUIModel.getSelectionRow();
		this.selectionRows = computeManagerGUIModel.getSelectionRows();
	}

	@Override
	protected String doAbstractAction(ActionEvent e) {
		String result;
		if ("Create Compute".equals(e.getActionCommand())
				|| "Create Computes".equals(e.getActionCommand())) {
			result = createComputeOrComputes();
		} else if ("Rename Compute".equals(e.getActionCommand())) {
			result = renameCompute();
		} else if ("Connect Compute".equals(e.getActionCommand())) {
			result = connectCompute();
		} else {
			result = computesAction(e.getActionCommand());
		}
		saveTable();
		computeManagerGUIModel.updateTableInfo();
		return result;
	}

	private String connectCompute() {
		if (selectionRows.length != 1) {
			return returnErrorMessage("Must Select Only One Compute On The Table View!!!");
		}
		if(!RUNNING_STATUS.equals(computeManagerGUIModel.getComputeInfo(selectionRow,
				STATUS_INDEX))){
			return returnErrorMessage("Only Running Status, Can Connet Compute!!!");
		}		
		
		String id = showInputDialog("Input Compute's Login ID!!!");
		if (id.contains(" ") || id == null || "".equals(id)) {
			return returnErrorMessage("Login ID Is Not Proper!!!");
		}
		startProgressSpinner();
		String publicIP = computeManagerGUIModel.getComputeInfo(selectionRow,
				PUBLIC_IP_INDEX);
		String keypair = SystemEnviroment.getKeypairDir()
				+ computeManagerGUIModel.getComputeInfo(selectionRow,
						KEYPAIR_INDEX);

		String command = "ssh -i ";
		if (SystemEnviroment.getOS().contains("Windows")) {
			keypair = keypair.replace("\\", "\\\\");
			keypair = keypair.replace("/", "\\\\");

			command = "cmd /c start cmd /c " + command + keypair + " " + id
					+ "@" + publicIP;
		}
		logger.info("Command To Connect Compute : " + command);
		try {
			new ProcessBuilder(command.split(" ")).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnSuccessMessage(publicIP);
	}

	private void saveTable() {
		Set<String> groupList = new HashSet<String>();
		String region;
		String group;
		for (int selectionRow : computeManagerGUIModel.getSelectionRows()) {
			region = computeManagerGUIModel.getComputeInfo(selectionRow,
					REGION_INDEX);
			group = computeManagerGUIModel.getComputeInfo(selectionRow,
					GROUP_INDEX);
			if (!groupList.contains(region + group)) {
				computeManagerGUIModel.saveGroup(region, group);
				groupList.add(region + group);
			}
		}
		for (TreePath treepath : selectionGroupTreePaths) {
			region = treepath.getParentPath().getLastPathComponent().toString();
			group = treepath.getLastPathComponent().toString();
			if (!groupList.contains(region + group)) {
				computeManagerGUIModel.saveGroup(region, group);
				groupList.add(region + group);
			}
		}
	}

	private String renameCompute() {
		if (selectionRows.length != 1) {
			return returnErrorMessage("Must Select Only One Compute On The Table View!!!");
		}
		String newComputeName = showInputDialog("Input A New Compute Name!");
		if (newComputeName.contains(" ")) {
			return returnErrorMessage("Not Allowed To Change Compute Name With a Space!!!");
		}
		if (newComputeName == null || "".equals(newComputeName)) {
			return returnErrorMessage("No Compute Name!!!");
		}
		startProgressSpinner();
		String region = computeManagerGUIModel.getComputeInfo(selectionRow,
				REGION_INDEX);
		String groupName = computeManagerGUIModel.getComputeInfo(selectionRow,
				GROUP_INDEX);
		String computeID = computeManagerGUIModel.getComputeInfo(selectionRow,
				COMPUTE_ID_INDEX);
		if (!computeManagerGUIModel.renameCompute(region, groupName, computeID,
				newComputeName)) {
			return returnErrorMessage("Can't Rename The Compute Name!!!, Warn : Not Allowed To Create Same Compute Name!");
		}
		return returnSuccessMessage(newComputeName);
	}

	private String createComputeOrComputes() {
		if (!isSelectedOnlyGroup()) {
			return returnErrorMessage("Must Select Only one Group On The Tree View!!!");
		}
		String[] defaultRegions = eC2EnviromentVO.getInstanceTypes().split(" ");
		String[] computeInfos = DialogsUtil.showCreateComputeInputDialog(
				mainFrame, actionCommand, defaultRegions,
				regionOfselectionGroup, selectionGroup);
		if (computeInfos.length == 0) {
			return returnErrorMessage(CANCEL_SIGNATURE);
		}
		String computeName = computeInfos[0];
		if (computeName.contains(" ")) {
			return returnErrorMessage("Not Allowed To Create Compute Name With a Space!!!");
		}

		String imageID = computeInfos[1];
		String computeType = computeInfos[2];
		int numOfComputes = new Integer(computeInfos[3]);

		startProgressSpinner();
		computeManagerGUIModel.showProgressResult("[" + actionCommand + "] ");
		if ("Create Compute".equals(actionCommand)) {
			computeManagerGUIModel.showProgressResult(computeName + " ");
			if (!doProgressMethod(computeManagerGUIModel.createCompute(
					regionOfselectionGroup, selectionGroup, computeName,
					imageID, computeType))) {
				return returnErrorMessage("Can't Create A Compute !!!");
			}
		} else {
			computeManagerGUIModel.showProgressResult(selectionGroup + " ");
			if (!doProgressMethod(computeManagerGUIModel.createComputes(
					regionOfselectionGroup, selectionGroup, computeName,
					imageID, numOfComputes, computeType))) {
				return returnErrorMessage("Can't Create One Or More Computes !!!");
			}
		}
		return computeName;
	}

	private String computesAction(String actionCommand) {
		if (selectionRows.length == 0) {
			return returnErrorMessage("Must Select Compute(s) On The Compute Table View!!!");
		}
		List<ComputeVO> tempComputeVOList = new ArrayList<ComputeVO>();
		StringBuffer computeNames = new StringBuffer();
		ComputesAction computesAction = getComputesActionList().get(
				actionCommand);
		startProgressSpinner();
		for (int row : selectionRows) {
			String status = computeManagerGUIModel.getComputeInfo(row,
					STATUS_INDEX);
			String groupName = computeManagerGUIModel.getComputeInfo(row,
					GROUP_INDEX);
			String computeID = computeManagerGUIModel.getComputeInfo(row,
					COMPUTE_ID_INDEX);
			String region = computeManagerGUIModel.getComputeInfo(row,
					REGION_INDEX);
			computeManagerGUIModel.showProgressResult("[" + actionCommand
					+ "] " + computeID);
			if (!doProgressMethod(computesAction.checkStatus(status)
					&& computesAction.doComputesAction(region, groupName,
							computeID))) {
				continue;
			}

			String newComputeID = computeManagerGUIModel.getComputeInfo(row,
					COMPUTE_ID_INDEX);
			if (!computeID.equals(newComputeID)) {
				computeManagerGUIModel.showProgressResult("[" + actionCommand
						+ "] " + computeID + " ComputeID has changed into "
						+ newComputeID + " !!!\n");
			}
			tempComputeVOList.add(getTempComputeVO(region, groupName,
					newComputeID));
			computeNames.append("\t");
			computeNames.append(newComputeID);
		}
		computesAction.updateStatus(tempComputeVOList);
		return computeNames.toString();
	}

	private ComputeVO getTempComputeVO(String region, String groupName,
			String computeID) {
		ComputeVO computeVO = new ComputeVO();
		computeVO.setRegion(region);
		computeVO.setComputeGroupName(groupName);
		computeVO.setComputeID(computeID);
		return computeVO;
	}

	private Map<String, ComputesAction> computeActionsList;

	private Map<String, ComputesAction> getComputesActionList() {
		if (computeActionsList == null) {
			computeActionsList = new HashMap<String, ComputesAction>();
			computeActionsList.put("Delete Computes", new deleteCompute());
			computeActionsList
					.put("Provision Computes", new provisionCompute());
			computeActionsList.put("Stop Computes", new stopCompute());
			computeActionsList.put("Start Computes", new startCompute());
			computeActionsList.put("Reboot Computes", new rebootcompute());
			computeActionsList
					.put("Terminate Computes", new terminateCompute());
		}
		return computeActionsList;
	}

	private interface ComputesAction {
		public boolean checkStatus(String status);

		public boolean doComputesAction(String region, String groupName,
				String computeID);

		public void updateStatus(List<ComputeVO> tempComputeVOList);
	}

	public class deleteCompute implements ComputesAction {

		@Override
		public boolean checkStatus(String status) {
			return NO_PROVISION_STATUS.equals(status)
					|| TERMINATED_STATUS.equals(status);
		}

		@Override
		public boolean doComputesAction(String region, String groupName,
				String computeID) {
			return computeManagerGUIModel.deleteCompute(region, groupName,
					computeID);
		}

		@Override
		public void updateStatus(List<ComputeVO> tempComputeVOList) {
		}

	}

	private class provisionCompute implements ComputesAction {

		@Override
		public boolean checkStatus(String status) {
			return NO_PROVISION_STATUS.equals(status);
		}

		@Override
		public boolean doComputesAction(String region, String groupName,
				String computeID) {
			if (computeManagerGUIModel.provisionCompute(region, groupName,
					computeID)) {
				computeManagerGUIModel.updateTableInfo();
				return true;
			}
			return false;
		}

		@Override
		public void updateStatus(List<ComputeVO> tempComputeVOList) {
			computeManagerGUIModel.updateComputesStatusAsync(30,
					tempComputeVOList, RUNNING_STATUS);
		}

	}

	private class stopCompute implements ComputesAction {

		@Override
		public boolean checkStatus(String status) {
			return RUNNING_STATUS.equals(status);
		}

		@Override
		public boolean doComputesAction(String region, String groupName,
				String computeID) {
			return computeManagerGUIModel.stopCompute(region, groupName,
					computeID);
		}

		@Override
		public void updateStatus(List<ComputeVO> tempComputeVOList) {
			computeManagerGUIModel.updateComputesStatusAsync(90,
					tempComputeVOList, STOPPED_STATUS);
		}

	}

	private class startCompute implements ComputesAction {

		@Override
		public boolean checkStatus(String status) {
			return STOPPED_STATUS.equals(status);
		}

		@Override
		public boolean doComputesAction(String region, String groupName,
				String computeID) {
			return computeManagerGUIModel.startCompute(region, groupName,
					computeID);
		}

		@Override
		public void updateStatus(List<ComputeVO> tempComputeVOList) {
			computeManagerGUIModel.updateComputesStatusAsync(30,
					tempComputeVOList, RUNNING_STATUS);
		}

	}

	private class rebootcompute implements ComputesAction {

		@Override
		public boolean checkStatus(String status) {
			return RUNNING_STATUS.equals(status);
		}

		@Override
		public boolean doComputesAction(String region, String groupName,
				String computeID) {
			return computeManagerGUIModel.rebootCompute(region, groupName,
					computeID);
		}

		@Override
		public void updateStatus(List<ComputeVO> tempComputeVOList) {
			computeManagerGUIModel.updateComputesStatusAsync(120,
					tempComputeVOList, RUNNING_STATUS);
		}

	}

	private class terminateCompute implements ComputesAction {

		@Override
		public boolean checkStatus(String status) {
			return !NO_PROVISION_STATUS.equals(status);
		}

		@Override
		public boolean doComputesAction(String region, String groupName,
				String computeID) {
			return computeManagerGUIModel.terminateCompute(region, groupName,
					computeID);
		}

		@Override
		public void updateStatus(List<ComputeVO> tempComputeVOList) {
			// computeManagerGUIModel.updateComputesStatusAsync(40,
			// tempComputeVOList, TERMINATED_STATUS);
		}

	}

}
