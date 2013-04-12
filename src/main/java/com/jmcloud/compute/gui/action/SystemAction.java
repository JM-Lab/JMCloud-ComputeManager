package com.jmcloud.compute.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.tree.TreePath;

import org.springframework.stereotype.Service;

@Service("systemAction")
public class SystemAction extends AbstractJMCloudGUIAction {

	@Override
	protected String doAbstractAction(ActionEvent e) {

		String result;
		switch (e.getActionCommand()) {
		case "Save Region":
			result = saveRegionAction();
			break;
		case "Load Region":
			result = loadRegionAction();
			break;
		case "Exit":
			result = exitAction();
			break;
		default:
			return returnErrorMessage(FAILURE_SIGNATURE);
		}
		computeManagerGUIModel.updateTree();
		return result;
	}

	private String saveRegionAction() {
		if (isSelectedRegions()) {
			return returnErrorMessage("Must Select Region(s) On The Tree View!!!");
		}
		startProgressSpinner();
		StringBuffer actionRegions = new StringBuffer();
		for (TreePath regionTreePath : selectionRegionTreePaths) {
			String region = getTreeNodeName(regionTreePath);
			saveRegion(region);
			actionRegions.append("\t" + region);
		}
		return returnSuccessMessage(actionRegions.toString());
	}

	private void saveRegion(String region) {
		for (String groupName : computeManagerGUIModel.getGroups(region)) {
			computeManagerGUIModel.showProgressResult("[Save Group] "
					+ groupName + " ");
			doProgressMethod(computeManagerGUIModel
					.saveGroup(region, groupName));
		}
	}

	private boolean isSelectedRegions() {
		return selectionRegionTreePaths == null
				|| selectionRegionTreePaths.size() == 0;
	}

	private String loadRegionAction() {
		if (isSelectedRegions()) {
			return returnErrorMessage("Must Select Region(s) On The Tree View!!!");
		}
		startProgressSpinner();
		StringBuffer actionRegions = new StringBuffer();
		for (TreePath regionTreePath : selectionRegionTreePaths) {
			String region = getTreeNodeName(regionTreePath);
			computeManagerGUIModel.showProgressResult("[" + actionCommand
					+ "] " + region + " ");
			if (doProgressMethod(computeManagerGUIModel.loadRegion(region))) {
				saveRegion(region);
				actionRegions.append("\t" + region);
			}
		}
		return actionRegions.toString();
	}

	private String exitAction() {
		System.exit(0);
		return null;
	}

}
