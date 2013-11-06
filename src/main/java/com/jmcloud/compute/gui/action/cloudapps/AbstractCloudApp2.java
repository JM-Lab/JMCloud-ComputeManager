package com.jmcloud.compute.gui.action.cloudapps;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import com.jmcloud.compute.commons.UtilWithRunCLI;
import com.jmcloud.compute.gui.action.cloudapps.views.CloudAppJDialog;
import com.jmcloud.compute.sys.SystemCloudApp;
import com.jmcloud.compute.sys.SystemEnviroment;
import com.jmcloud.compute.util.SysUtils;

public abstract class AbstractCloudApp2 implements CloudApp {
	private class InstallCloudApp implements Runnable {

		@Override
		public void run() {
			if (!mkdirWorkingDir() || !sendLaunchPack() || !unpackLaunchPack()) {
				writeErrLog("Can't Ready To Install A Cloud App!!!");
				return;
			}
			writeOutLog("Ready To Install A Cloud App");
			
			runLuanchPack();			
		}
		
		private void runLuanchPack() {
			writeOutLog("Start To Install A Cloud App");
			String command = "ssh -o StrictHostKeyChecking=no -i " + keypair
					+ " " + id + "@" + publicIP + " sudo sh " + cloudAppRootDir
					+ getLuanchPackName() + "/" + getLuanchPackName() + ".sh";
			writeOutLog("Command To Run Process: " + command);
			writeOutLog("Install Logs Are As Follows...");
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
					writeErrLog("Can't Luanch A Cloud App!!!");
				} else {
					writeOutLog("Finish Luanching A Cloud App!!!");
					showNextSteps();
				}

			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				writeErrLog("Can't Luanch An App!!!");
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
							writeOut(resultLine);
						}
					} catch (IOException e) {
						e.printStackTrace();
						writeErr(e.toString());
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
							writeErr(resultLine);
						}
					} catch (IOException e) {
						e.printStackTrace();
						writeErr(e.toString());
					}
				}
			});
		}

		private boolean runProcess(String command) {
			writeOutLog("Command To Run Process: " + command);
			Process process;
			try {
				process = new ProcessBuilder(command.split(" ")).start();
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
		
		private boolean unpackLaunchPack() {
			writeOutLog("Unpack A Luanch Pack File");
			String keypairPath = getKeypairPath();
			String command = "ssh -o StrictHostKeyChecking=no -i " + keypairPath
					+ " " + id + "@" + publicIP + " tar xvzf " + cloudAppRootDir
					+ luanchPackFile + " -C " + cloudAppRootDir;
			return runProcess(command);
		}

		private boolean mkdirWorkingDir() {
			writeOutLog("Make A Working Directory");
			if (!new File(keypair).exists()) {
				writeErrLog(keypair + " File Dosen't Exist!!!");
				return false;
			}
			String keypairPath = getKeypairPath();
			String command = "ssh -o StrictHostKeyChecking=no -i " + keypairPath
					+ " " + id + "@" + publicIP + " mkdir -p " + cloudAppRootDir;
			return runProcess(command); 
		}

		private boolean sendLaunchPack() {
			writeOutLog("Send A Luanch Pack File");
			Path luanchPack = SystemCloudApp.getLuanchPackFile(luanchPackFile);
			if (luanchPack == null) {
				writeErrLog("A Luanch Pack File Doesn't Exist!!!");
				return false;
			}
			String luanchPackFilePath = luanchPack.toFile().getAbsolutePath();
			if (SystemEnviroment.getOS().contains("Windows")) {
				luanchPackFilePath = SysUtils
						.convertIntoCygwinPath(luanchPackFilePath);
			}
			String keypairPath = getKeypairPath();
			String command = "ssh -o StrictHostKeyChecking=no -i " + keypairPath
					+ " " + luanchPackFilePath + " " + id + "@" + publicIP + ":"
					+ cloudAppRootDir;
			return runProcess(command); 

		}
		
		private void cleanUpLuanchPack() {
			writeOutLog("Clean Up Luanch Pack Directory");
			String keypairPath = getKeypairPath();
			String command = "ssh -o StrictHostKeyChecking=no -i " + keypairPath
					+ " " + id + "@" + publicIP + " rm -rf " + cloudAppRootDir;
			runProcess(command); 
		}

		private void showElasoptime(long startTime) {
			long endTime = System.currentTimeMillis();
			long elapsedTime = (endTime - startTime) / 1000;
			writeOutLog("Elapsed Time (sec) : " + elapsedTime);
		}
		
		private String getKeypairPath() {
			if (SystemEnviroment.getOS().contains("Windows")) {
				return SysUtils.convertIntoCygwinPath(keypair);
			}
			return keypair;
		}
	}

	protected final String LUANCH_PROGRESS_INFO = "[Luanch Progress Info]\t";
	protected JFrame mainFrame;
	protected String region;
	protected String group;
	protected String cloudAppRootDir;
	protected String publicIP;
	protected String keypair;
	protected String id;

	private JTextArea cloudAppLogView;
	protected String luanchCommand;
	protected Process process;

	private String luanchPackFile;
	private long startTime;

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
		this.luanchPackFile = getLuanchPackName() + ".tar.gz";
	}

	@Override
	public void startCloudApp() {
		CloudAppJDialog cloudAppJDialog = new CloudAppJDialog(mainFrame);
		cloudAppJDialog.setTitle(getTitle() + " - " + publicIP);
		cloudAppLogView = cloudAppJDialog.getCloudAppLogView();
		addActions(cloudAppJDialog.getCloudAppActiontoolBar());
		cloudAppJDialog.setVisible(true);	
		
		installCloudApp();
	}

	private void installCloudApp() {
		new Thread(new InstallCloudApp()).start();		
	}

	private void addActions(JToolBar cloudAppActiontoolBar) {
		cloudAppActiontoolBar.add(getConnectComputeWithSSHConsoleAction());
		addCloudAppActions(cloudAppActiontoolBar);
	}
	
	private Action getConnectComputeWithSSHConsoleAction() {
		return new AbstractAction("Run SSH Console") {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (UtilWithRunCLI.connectServerWithSSH(keypair, id, publicIP,
						logger)) {
					writeOutLog("Connect Compute With SSH Console");
				} else {
					writeErrLog("Can't Connect Compute With SSH Console!!!");
				}
			}
		};
	}
	
	
	protected void writeErrLog(String line) {
		writeErr(LUANCH_PROGRESS_INFO + "[ERROR]\t" + line + "\n");
	}

	protected void writeOutLog(String line) {
		writeOut(LUANCH_PROGRESS_INFO + line + "\n");
	}
	
	synchronized protected void writeOut(String line) {
		cloudAppLogView.append(line);
		logger.info(line);
	}

	synchronized protected void writeErr(String line) {
		cloudAppLogView.append(line);
		logger.fatal(line);
	}


//	synchronized protected void showLineOnInfoView(String result) {
//		jMCloudComputeInfoTextArea.insert(result + "\n",
//				jMCloudComputeInfoTextArea.getText().length());
//		jMCloudComputeInfoTextArea.setCaretPosition(jMCloudComputeInfoTextArea
//				.getText().length());
//	}
	


}
