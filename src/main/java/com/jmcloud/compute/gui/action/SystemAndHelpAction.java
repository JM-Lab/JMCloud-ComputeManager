package com.jmcloud.compute.gui.action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.sys.SystemEnviroment;
import com.jmcloud.compute.util.SysUtils;

@Service("systemAction")
public class SystemAndHelpAction extends AbstractJMCloudGUIAction {

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
		case "Set Console":
			result = setConsoleAction();
			break;
		case "Exit":
			result = exitAction();
			break;
		case "Open User Information":
			result = openUserInformationAction();
			break;
			
		default:
			return returnErrorMessage(FAILURE_SIGNATURE);
		}
		computeManagerGUIModel.updateTree();
		return result;
	}

	private String openUserInformationAction() {
		File userEnvFile = Paths.get(SystemEnviroment.getUserEnvPath()).toFile();
		try {
			Desktop.getDesktop().open(userEnvFile);
		} catch (IOException e) {
			e.printStackTrace();
			return returnErrorMessage("Can't Open User Infomation File : "
					+ userEnvFile.getAbsolutePath());
		}
		return returnSuccessMessage("Open User Infomation File : "
				+ userEnvFile.getAbsolutePath());
	}

	private JFileChooser fileChooser;
	private File consoleExecutableFile;

	private String setConsoleAction() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Choose Console Execution File!!!");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setMultiSelectionEnabled(false);
		}
		if (consoleExecutableFile == null) {
			consoleExecutableFile = getDefaultCurrentSelectedFile();
		}
		if (consoleExecutableFile != null) {
			fileChooser.setSelectedFile(consoleExecutableFile);
		}

		int status = fileChooser.showOpenDialog(mainFrame);

		if (status == JFileChooser.APPROVE_OPTION) {
			consoleExecutableFile = fileChooser.getSelectedFile();
			fileChooser.setSelectedFile(consoleExecutableFile);
			SystemEnviroment.setConsoleFilePath(consoleExecutableFile
					.getAbsolutePath());
			Path userEnvPath = Paths.get(SystemEnviroment.getUserEnvPath());
			Properties userProperties = SysUtils.getProperties(userEnvPath
					.toUri());
			userProperties.put("CONSOLE_FILE_PATH",
					consoleExecutableFile.getAbsolutePath());
			if (SysUtils.saveProperties(userProperties, userEnvPath,
					"User Enviroment Properties")) {
				return returnSuccessMessage("Set Console Execution File : "
						+ consoleExecutableFile.getAbsolutePath());
			}else{
				return returnErrorMessage("Set Console Execution File : "
						+ consoleExecutableFile.getAbsolutePath());
			}
		}

		return returnErrorMessage("Cancel");
	}

	private File getDefaultCurrentSelectedFile() {
		if (SystemEnviroment.getConsoleFilePath() == null) {
			return null;
		}
		File tempFile = new File(SystemEnviroment.getConsoleFilePath());
		return tempFile.exists() ? tempFile : null;
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
			computeManagerGUIModel.showProgressResult("[" + actionCommand + "]"
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
		return returnSuccessMessage(actionRegions.toString());
	}

	private String exitAction() {
		System.exit(0);
		return null;
	}

}
