package com.jmcloud.compute.gui.action.cloudapps;

import java.awt.Component;
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
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import com.jmcloud.compute.commons.UtilWithRunCLI;
import com.jmcloud.compute.gui.action.cloudapps.views.CloudAppViewPanel;
import com.jmcloud.compute.sys.SystemCloudApp;
import com.jmcloud.compute.sys.SystemEnviroment;
import com.jmcloud.compute.util.SysUtils;

public abstract class AbstractCloudApp implements CloudApp {

	protected final String LUANCH_PROGRESS_INFO = "[Luanch Progress Info]\t";
	protected String keypair;
	protected String id;
	protected String publicIP;
	protected String cloudAppRootDir;
	protected JFrame mainFrame;
	protected CloudAppViewPanel viewPanel;
	protected JTextArea installProgressView;
	protected JToolBar appManagementButtonToolBar;
	protected String luanchCommand;
	protected Process process;
	private String luanchPackFile;
	private long startTime;

	abstract protected void showNextSteps();

	abstract protected void addAppActions();

	abstract protected String getLuanchPackName();

	abstract protected boolean setSecurityRules();

	@Override
	public void initCloudApp(JFrame mainFrame, String region, String group,
			String cloudAppRootDir, String publicIP, String keypair, String id) {
		this.mainFrame = mainFrame;
		this.cloudAppRootDir = cloudAppRootDir;
		this.publicIP = publicIP;
		this.keypair = keypair;
		this.id = id;
		this.luanchPackFile = getLuanchPackName() + ".tar.gz";
		this.luanchCommand = "ssh -o StrictHostKeyChecking=no -i " + keypair
				+ " " + id + "@" + publicIP + " sudo sh " + cloudAppRootDir
				+ getLuanchPackName() + "/" + getLuanchPackName() + ".sh";
	}

	
	public void luanchCloudApp() {

				setLuanchPackEnvOnVM();

				writeOutInfo("Start To Luanch An App");
				writeOutInfo("Command : " + luanchCommand);
				startTime = System.currentTimeMillis();
				try {
					process = new ProcessBuilder(luanchCommand.split(" "))
							.start();
					BufferedReader stdOut = new BufferedReader(
							new InputStreamReader(process.getInputStream()));

					BufferedReader stdErr = new BufferedReader(
							new InputStreamReader(process.getErrorStream()));
					writeOut(LUANCH_PROGRESS_INFO + "Start To Install");
					Thread stdOutThread = getStdOutThread(stdOut);
					Thread stdErrThread = getStdErrThread(stdErr);
					stdOutThread.start();
					stdErrThread.start();

					if (process.waitFor() != 0) {
						writeErrInfo("Can't Luanch An App!!!");
					} else {
						writeOutInfo("Finish Luanching An App!!!");
						showNextSteps();
					}

				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
					writeErrInfo("Can't Luanch An App!!!");
				} finally {
					cleanUpLuanchPack();
					showelasoptime(startTime);
				}
	}

	private void setLuanchPackEnvOnVM() {
//		this.viewPanel = new CloudAppViewPanel();
		this.installProgressView = viewPanel.getTextArea();
		appManagementButtonToolBar = viewPanel.getAppManagementButtonToolBar();
		appManagementButtonToolBar.add(getConnectComputeWithSSHConsoleAction());
		addAppActions();
		for (Component c : appManagementButtonToolBar.getComponents()) {
			if (c instanceof JComponent) {
				((JComponent) c).setBorder(new BevelBorder(BevelBorder.RAISED));
			}
		}
		writeOutInfo("Make A Working Directory");
		if (!mkdirWorkingDir()) {
			writeErrInfo("Can't Make Cloud App Directory!!!");
		}

		writeOutInfo("Send A Luanch Pack File");
		if (!sendRaunchPack()) {
			writeErrInfo("Can't Send A Luanch Pack File");
		}

		writeOutInfo("Unpack A Luanch Pack File");
		if (!unpackRaunchPack()) {
			writeErrInfo("Can't Unpack A Luanch Pack File!!!");
		}

		writeOutInfo("Set Security Rules For The App");
		if (!setSecurityRules()) {
			writeErrInfo("Can't Set Security Rules!!!");
		}

		writeOutInfo("Ready To Luanch");
	}

	protected void writeErrInfo(String line) {
		writeErr(LUANCH_PROGRESS_INFO + "[ERROR]\t" + line);
	}

	protected void writeOutInfo(String line) {
		writeOut(LUANCH_PROGRESS_INFO + line);
	}

	private Action getConnectComputeWithSSHConsoleAction() {
		return new AbstractAction("Run SSH Console") {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (UtilWithRunCLI.connectServerWithSSH(keypair, id, publicIP,
						logger)) {
					writeOutInfo("Connect Compute With SSH Console");
				} else {
					writeErrInfo("Can't Connect Compute With SSH Console!!!");
				}
			}
		};
	}

	private boolean unpackRaunchPack() {
		String luanchPackFile = getLuanchPackName() + ".tar.gz";
		String commonCommand = "ssh -o StrictHostKeyChecking=no -i " + keypair
				+ " " + id + "@" + publicIP + " tar xvzf " + cloudAppRootDir
				+ luanchPackFile + " -C " + cloudAppRootDir;
		writeOutInfo("Command : " + commonCommand);
		Process process;
		try {
			process = new ProcessBuilder(commonCommand.split(" ")).start();
			if (process.waitFor() != 0) {
				return false;
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			logger.fatal(e);
			return false;
		}

		return true;
	}

	private boolean mkdirWorkingDir() {

		if (!new File(keypair).exists()) {
			writeErrInfo(keypair + " File Dosen't Exist!!!");
			return false;
		}
		if (SystemEnviroment.getOS().contains("Windows")) {
			keypair = SysUtils.convertIntoCygwinPath(keypair);
		}
		String commonCommand = "ssh -o StrictHostKeyChecking=no -i " + keypair
				+ " " + id + "@" + publicIP + " mkdir -p " + cloudAppRootDir;
		writeOutInfo("Command : " + commonCommand);
		Process process;
		try {
			process = new ProcessBuilder(commonCommand.split(" ")).start();
			if (process.waitFor() != 0) {
				return false;
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			logger.fatal(e);
			return false;
		}

		return true;
	}

	private boolean sendRaunchPack() {

		Path luanchPack = SystemCloudApp.getLuanchPackFile(luanchPackFile);
		if (luanchPack == null) {
			writeErrInfo(" Doesn't Exist!!!");
			return false;
		}
		String luanchPackFilePath = luanchPack.toFile().getAbsolutePath();
		if (SystemEnviroment.getOS().contains("Windows")) {
			luanchPackFilePath = SysUtils
					.convertIntoCygwinPath(luanchPackFilePath);
		}
		String commonCommand = "scp -o StrictHostKeyChecking=no -i " + keypair
				+ " " + luanchPackFilePath + " " + id + "@" + publicIP + ":~/"
				+ cloudAppRootDir;
		writeOutInfo("Command : " + commonCommand);
		Process process;
		try {
			process = new ProcessBuilder(commonCommand.split(" ")).start();
			if (process.waitFor() != 0) {
				return false;
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			logger.fatal(e);
			return false;
		}

		return true;

	}
	
	private void cleanUpLuanchPack() {
		writeOutInfo("Clean Up Luanch Pack Directory");
		if (SystemEnviroment.getOS().contains("Windows")) {
			keypair = SysUtils.convertIntoCygwinPath(keypair);
		}
		String commonCommand = "ssh -o StrictHostKeyChecking=no -i " + keypair
				+ " " + id + "@" + publicIP + " rm -rf " + cloudAppRootDir;
		writeOutInfo("Command : " + commonCommand);
		Process process;
		try {
			process = new ProcessBuilder(commonCommand.split(" ")).start();
		} catch (IOException e) {
			e.printStackTrace();
			logger.fatal(e);
		}
	}

	private void showelasoptime(long startTime) {
		long endTime = System.currentTimeMillis();
		long elapsedTime = (endTime - startTime) / 1000;
		writeOutInfo("Elapsed Time (sec) : " + elapsedTime);
	}

	protected void connectAppWithBrowser(String appURL) {
		writeOutInfo("Connect An App With Browser : " + appURL);
		try {
			Desktop.getDesktop().browse(new URI(appURL));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			writeErrInfo("Can't Connect An App With Browser : " + appURL);
		}
	}

	protected Thread getStdOutThread(final BufferedReader stdOut) {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				String resultLine;
				try {
					while ((resultLine = stdOut.readLine()) != null) {
						writeOut(resultLine);
					}
				} catch (IOException e) {
					e.printStackTrace();
					writeErr(e.toString());
				}
			}
		});
	}

	protected Thread getStdErrThread(final BufferedReader stdErr) {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				String resultLine;
				try {
					while ((resultLine = stdErr.readLine()) != null) {
						writeErr(resultLine);
					}
				} catch (IOException e) {
					e.printStackTrace();
					writeErr(e.toString());
				}
			}
		});
	}

	protected void stdOutAndErr(BufferedReader stdOut, BufferedReader stdErr)
			throws IOException {
		String resultLine;

		while ((resultLine = stdOut.readLine()) != null) {
			writeOut(resultLine);
		}

		while ((resultLine = stdErr.readLine()) != null) {
			writeErr(resultLine);
		}
	}

	synchronized protected void writeOut(String line) {
		line += "\n";
		installProgressView.append(line);
		logger.info(line);
	}

	synchronized protected void writeErr(String line) {
		line += "\n";
		installProgressView.append(line);
		logger.fatal(line);
	}

}