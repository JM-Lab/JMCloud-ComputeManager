package com.jmcloud.compute.gui.action.cloudapps;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import com.jmcloud.compute.commons.UtilWithRunCLI;
import com.jmcloud.compute.gui.action.cloudapps.views.CloudAppJDialog;
import com.jmcloud.compute.gui.component.DialogsUtil;
import com.jmcloud.compute.sys.SystemCloudApp;
import com.jmcloud.compute.sys.SystemEnviroment;
import com.jmcloud.compute.util.SysUtils;

public abstract class AbstractCloudApp implements CloudApp {
	private class InstallCloudApp implements Runnable {

		@Override
		public void run() {
			if (!mkdirWorkingDir() || !sendLaunchPack() || !setPermissionOfLaunchPack() || !unpackLaunchPack()) {
				writeErrOnView("Can't Ready To Install A Cloud App!!!");
				return;
			}
			writeOutOnView("[START] Ready To Install A Cloud App");

			runLuanchPack();
		}

		private void runLuanchPack() {
			writeOutOnView("Start To Install A Cloud App");
			String command = "ssh -o StrictHostKeyChecking=no -i "
					+ keypairPath + " " + id + "@" + publicIP + " sudo sh "
					+ cloudAppRootDir + getLuanchPackName() + "/"
					+ getLuanchPackName() + ".sh";
			writeErrLog("Command To Run Process: " + command);
			writeOutOnView("Install Logs Are As Follows...");
			startTime = System.currentTimeMillis();
			try {
				Process process = new ProcessBuilder(command.split(" "))
						.start();
				BufferedReader stdOut = new BufferedReader(
						new InputStreamReader(process.getInputStream()));

				BufferedReader stdErr = new BufferedReader(
						new InputStreamReader(process.getErrorStream()));
				Thread stdOutThread = getStdOutThread(stdOut);
				Thread stdErrThread = getStdErrThread(stdErr);
				stdOutThread.start();
				stdErrThread.start();
				try {
					if (process.waitFor() == 0) {
						writeOutOnView("Finish Luanching A Cloud App!!!");
						showNextSteps();
					} else {
						writeErrOnView("Can't Luanch A Cloud App!!!");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					writeErrOnView("Can't Luanch A Cloud App!!!");
				}
			} catch (IOException e) {
				e.printStackTrace();
				writeErrOnView("Can't Luanch An App!!!");
			} finally {
				cleanUpLuanchPack();
				showElasoptime(startTime);
			}
		}

		private Thread getStdOutThread(final BufferedReader stdOut) {
			return new Thread(new Runnable() {
				@Override
				public void run() {
					String resultLine;
					try {
						while ((resultLine = stdOut.readLine()) != null) {
							writeStdOutLog(resultLine);
						}
					} catch (IOException e) {
						e.printStackTrace();
						writeErrLog(e.toString());
					}
				}
			});
		}

		private Thread getStdErrThread(final BufferedReader stdErr) {
			return new Thread(new Runnable() {
				@Override
				public void run() {
					String resultLine;
					try {
						while ((resultLine = stdErr.readLine()) != null) {
							writeErrLog(resultLine);
						}
					} catch (IOException e) {
						e.printStackTrace();
						writeErrLog(e.toString());
					}
				}
			});
		}

		private boolean mkdirWorkingDir() {
			writeOutOnView("Make A Working Directory");
			if (!new File(keypair).exists()) {
				writeErrOnView(keypair + " File Dosen't Exist!!!");
				return false;
			}
			String command = "ssh -o StrictHostKeyChecking=no -i "
					+ keypairPath + " " + id + "@" + publicIP + " mkdir -p "
					+ cloudAppRootDir;
			return runProcess(command);
		}
		
		private boolean setPermissionOfLaunchPack() {
			writeOutOnView("Set permission of A Luanch Pack File");
			String command = "ssh -o StrictHostKeyChecking=no -i "
					+ keypairPath + " " + id + "@" + publicIP + " sudo chmod 755 "
					+ cloudAppRootDir + luanchPackFile;
			return runProcess(command);
		}

		private boolean unpackLaunchPack() {
			writeOutOnView("Unpack A Luanch Pack File");
			String command = "ssh -o StrictHostKeyChecking=no -i "
					+ keypairPath + " " + id + "@" + publicIP + " tar xvzf "
					+ cloudAppRootDir + luanchPackFile + " -C "
					+ cloudAppRootDir;
			return runProcess(command);
		}

		private boolean sendLaunchPack() {
			writeOutOnView("Send A Luanch Pack File");
			Path luanchPack = SystemCloudApp.getLuanchPackFile(luanchPackFile);
			if (luanchPack == null) {
				writeErrOnView("A Luanch Pack File Doesn't Exist!!!");
				return false;
			}
			String luanchPackFilePath = luanchPack.toFile().getAbsolutePath();
			if (SystemEnviroment.getOS().contains("Windows")) {
				luanchPackFilePath = SysUtils
						.convertIntoCygwinPath(luanchPackFilePath);
			}
			String command = "scp -o StrictHostKeyChecking=no -i "
					+ keypairPath + " " + luanchPackFilePath + " " + id + "@"
					+ publicIP + ":" + cloudAppRootDir;
			return runProcess(command);

		}

		private void cleanUpLuanchPack() {
			writeOutOnView("Clean Up Luanch Pack Directory");
			String command = "ssh -o StrictHostKeyChecking=no -i "
					+ keypairPath + " " + id + "@" + publicIP + " rm -rf "
					+ cloudAppRootDir;
			runProcess(command);
		}

		private void showElasoptime(long startTime) {
			long endTime = System.currentTimeMillis();
			long elapsedTime = (endTime - startTime) / 1000;
			writeOutOnView("[END]\tElapsed Time (sec) : " + elapsedTime);
		}

	}

	private String getKeypairPath() {
		if (SystemEnviroment.getOS().contains("Windows")) {
			return SysUtils.convertIntoCygwinPath(keypair);
		}
		return keypair;
	}

	private boolean runProcess(String command) {
		writeErrLog("Command To Run Process: " + command);
		Process process;
		try {
			process = new ProcessBuilder(command.split(" ")).start();
			BufferedReader stdOut = new BufferedReader(new InputStreamReader(
					process.getInputStream()));

			BufferedReader stdErr = new BufferedReader(new InputStreamReader(
					process.getErrorStream()));
			stdOutAndErr(stdOut, stdErr);
			if (process.waitFor() != 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal(e);
			return false;
		}
		return true;
	}

	private void stdOutAndErr(BufferedReader stdOut, BufferedReader stdErr)
			throws IOException {
		String resultLine;

		while ((resultLine = stdOut.readLine()) != null) {
			writeStdOutLog(resultLine);
		}

		while ((resultLine = stdErr.readLine()) != null) {
			writeErrLog(resultLine);
		}
	}

	protected final String LUANCH_PROGRESS_INFO = "[Luanch Progress Info] ";
	protected JFrame mainFrame;
	protected String region;
	protected String group;
	protected String cloudAppRootDir;
	protected String publicIP;
	protected String keypair;
	protected String id;

	private JTextArea cloudAppLogView;
	private String keypairPath;
	private String luanchPackFile;
	private long startTime;
	private CloudAppJDialog cloudAppJDialog;

	abstract protected String getLuanchPackName();

	abstract protected void showNextSteps();

	abstract protected void addCloudAppActions(JToolBar cloudAppActiontoolBar);

	abstract protected String getTitle();

	@Override
	public void initCloudApp(JFrame mainFrame, String region, String group,
			String cloudAppRootDir, String publicIP, String keypair, String id) {
		this.mainFrame = mainFrame;
		this.region = region;
		this.group = group;
		this.cloudAppRootDir = cloudAppRootDir;
		this.publicIP = publicIP;
		this.keypair = keypair;
		this.id = id;
		this.keypairPath = getKeypairPath();
		this.luanchPackFile = getLuanchPackName() + ".tar.gz";

		cloudAppJDialog = new CloudAppJDialog(mainFrame);
		cloudAppJDialog.setTitle(getTitle() + " - " + publicIP);
		cloudAppLogView = cloudAppJDialog.getCloudAppLogView();
		addActions(cloudAppJDialog.getCloudAppActiontoolBar());
		// for (Component c :
		// cloudAppJDialog.getCloudAppActiontoolBar().getComponents()) {
		// if (c instanceof JComponent) {
		// ((JComponent) c).setBorder(new BevelBorder(BevelBorder.RAISED));
		// }
		// }
	}

	@Override
	public void startCloudApp() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				cloudAppJDialog.setVisible(true);
			}
		});
	}

	public void luanchCloudApp() {
		InstallCloudApp runnable = new InstallCloudApp();
		Thread thread = new Thread(runnable);
		thread.start();
	}

	private void addActions(JToolBar cloudAppActiontoolBar) {
		cloudAppActiontoolBar.add(getConnectComputeWithSSHConsoleAction());
		addCloudAppActions(cloudAppActiontoolBar);
	}

	private Action getConnectComputeWithSSHConsoleAction() {
		return new AbstractAction("Run SSH Console") {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (UtilWithRunCLI.connectServerWithSSH(getKeypairPath(), id,
						publicIP, logger)) {
					writeOutOnView("Connect Compute With SSH Console");
				} else {
					writeErrOnView("Can't Connect Compute With SSH Console!!!");
				}
			}
		};
	}

	protected void connectAppWithBrowser(String appURL) {
		writeOutOnView("Connect An App With Browser : " + appURL);
		try {
			Desktop.getDesktop().browse(new URI(appURL));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			writeErrOnView("Can't Connect An App With Browser : " + appURL);
		}
	}

	protected void createAccount() {
		writeOutOnView("Input Your A New Account");
		String[] accountInfo = DialogsUtil.showAccountInputDialog(mainFrame,
				"Create A New Account", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (accountInfo == null) {
			writeErrOnView("Can't Create A New Account");
			return;
		}

		String newId = accountInfo[0];
		String password = accountInfo[1];

		String command = "ssh -o StrictHostKeyChecking=no -i " + keypairPath
				+ " " + id + "@" + publicIP + " sudo useradd -m " + newId
				+ " && echo " + newId + ":" + password + " | sudo chpasswd";
		if (runProcess(command)) {
			writeOutOnView("Create A New Account : " + newId);
		} else {
			writeErrOnView("Can't Create A New Account!!!");
		}
	}

	synchronized protected void writeErrOnView(String line) {
		line = LUANCH_PROGRESS_INFO + "[ERROR] " + line;
		writeErrLog(line);
		writeCloudAppLogView(line);
	}

	synchronized protected void writeOutOnView(String line) {
		line = LUANCH_PROGRESS_INFO + line;
		writeStdOutLog(line);
	}

	synchronized protected void writeErrLog(String line) {
		logger.fatal(line);
	}

	synchronized protected void writeStdOutLog(String line) {
		logger.info(line);
		writeCloudAppLogView(line);
	}

	private void writeCloudAppLogView(String line) {
		cloudAppLogView.append(line += "\n");
		cloudAppLogView.setCaretPosition(cloudAppLogView.getText().length());
	}

}
