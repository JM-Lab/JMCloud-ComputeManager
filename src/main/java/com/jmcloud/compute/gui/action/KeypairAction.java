package com.jmcloud.compute.gui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.sys.SystemEnviroment;

@Service("keypairAction")
public class KeypairAction extends AbstractJMCloudGUIAction {

	private JFileChooser jFileChooser;

	private final String keypairExtention = ".pem";

	@Override
	protected String doAbstractAction(ActionEvent e) {
		if (!isSelectedOnlyGroup()) {
			return returnErrorMessage("Must Select Only One Group On The Tree View!!!");
		}
		switch (e.getActionCommand()) {
		case "Download Keypair":
			return downloadKeypairAction();
		case "Create Keypair":
			return createGroupKeypairAction(e.getActionCommand());
		default:
			return returnErrorMessage(FAILURE_SIGNATURE);
		}
	}

	private String downloadKeypairAction() {
		String keypair = computeManagerGUIModel.getGroupKeypair(
				regionOfselectionGroup, selectionGroup);
		Path keypairFilePath = Paths.get(SystemEnviroment.getKeypairDir(),
				keypair);
		if (keypairFilePath.toFile().isDirectory()
				|| !keypairFilePath.toFile().exists()) {
			return returnErrorMessage(keypair
					+ " Never Created A Group Keypair By JMCloud Compute Manager!!!");
		}

		URI targetURI = getTargetFilePath();

		if (targetURI == null) {
			return returnErrorMessage(CANCEL_SIGNATURE);
		}
		try {
			Path targetFilePath = Paths.get(targetURI);
			Files.copy(keypairFilePath, targetFilePath);
			Set<PosixFilePermission> perms = PosixFilePermissions
					.fromString("r--------");
			Files.setPosixFilePermissions(targetFilePath, perms);
		} catch (UnsupportedOperationException ue) {
			keypairFilePath.toFile().setReadOnly();
		} catch (IOException e) {
			e.printStackTrace();
			return returnErrorMessage(keypair + " Can't Download!!!");
		}
		return returnSuccessMessage(targetURI.toString());
	}

	private URI getTargetFilePath() {

		if (jFileChooser == null) {
			jFileChooser = new JFileChooser();
		}
		jFileChooser.setSelectedFile(new File(computeManagerGUIModel
				.getGroupKeypair(regionOfselectionGroup, selectionGroup)
				+ keypairExtention));

		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {

					if (jFileChooser.showSaveDialog(mainFrame) != JFileChooser.APPROVE_OPTION) {
						jFileChooser.setSelectedFile(null);
					}
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
			logger.fatal(e);
		}
		return jFileChooser.getSelectedFile() != null ? jFileChooser
				.getSelectedFile().toURI() : null;
	}

	private String createGroupKeypairAction(String command) {
		int option = showComfirmDialog("Do You Want To " + command + "?");
		if (option != JOptionPane.OK_OPTION) {
			return returnErrorMessage(CANCEL_SIGNATURE);
		}
		startProgressSpinner();
		String keypair = computeManagerGUIModel.getGroupKeypair(
				regionOfselectionGroup, selectionGroup);
		Path keypairPath = Paths.get(SystemEnviroment.getKeypairDir(), keypair);
		if (keypairPath.toFile().exists()
				&& !keypairPath.toFile().isDirectory()) {
			return returnErrorMessage(keypair + " Is Already Existing !!!");
		}
		computeManagerGUIModel.showProgressResult("[Create Keypair] " + keypair
				+ " ");
		doProgressMethod(computeManagerGUIModel.createKeypair(
				regionOfselectionGroup, selectionGroup));
		computeManagerGUIModel
				.saveGroup(regionOfselectionGroup, selectionGroup);
		return keypair;
	}

}
