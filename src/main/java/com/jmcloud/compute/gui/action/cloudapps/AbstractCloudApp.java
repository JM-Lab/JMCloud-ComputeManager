package com.jmcloud.compute.gui.action.cloudapps;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.jmcloud.compute.gui.action.cloudapps.views.CloudAppViewPanel;

public abstract class AbstractCloudApp implements CloudApp {

	protected final String LUANCH_PROGRESS_INFO = "[Luanch Progress Info]\t";
	protected String keypair;
	protected String id;
	protected String publicIP;
	protected String cloudAppRootDir;
	protected String luanchPackName;
	protected JFrame mainFrame;
	protected CloudAppViewPanel viewPanel;
	protected JTextArea installProgressView;
	protected String luanchCommand;

	abstract protected void showCloudAppView();
	abstract protected void showNextSteps();

	@Override
	public void luanchApp(final JFrame mainFrame, String cloudAppRootDir,
			String publicIP, String keypair, String id, String luanchPackName) {
		initBeforeLuanch(mainFrame, cloudAppRootDir, publicIP, keypair, id, luanchPackName);
		
		Thread luanchAppThread = new Thread(new Runnable() {
			@Override
			public void run() {
				viewPanel = new CloudAppViewPanel();
				installProgressView = viewPanel.getTextArea();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						showCloudAppView();
					}			
				});
				
				writeOut(LUANCH_PROGRESS_INFO
						+ "Command To Luanch An App : " + luanchCommand);
				Process process;
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
						writeErr(LUANCH_PROGRESS_INFO
								+ "Can't Luanch An App!!!");
					} else {
						writeOut(LUANCH_PROGRESS_INFO
								+ "Finish Luanching An App!!!");
						showNextSteps();
					}

				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
					writeErr(LUANCH_PROGRESS_INFO
							+ "Can't Luanch An App!!!");
				}
			}
		});
		
		luanchAppThread.start();

	}

	private void initBeforeLuanch(JFrame mainFrame, String cloudAppRootDir, String publicIP,
			String keypair, String id, String luanchPackName) {
		this.mainFrame = mainFrame;
		this.cloudAppRootDir = cloudAppRootDir;
		this.publicIP = publicIP;
		this.keypair = keypair;
		this.id = id;
		this.luanchCommand = "ssh -o StrictHostKeyChecking=no -i " + keypair
				+ " " + id + "@" + publicIP + " sudo sh " + cloudAppRootDir
				+ luanchPackName + "/" + luanchPackName + ".sh";
	}

	@Override
	public void connectApp() {
		String AppURL = "http://" + publicIP + ":8787";
		logger.info("Connect An App : " + AppURL);
		try {
			Desktop.getDesktop().browse(new URI(AppURL));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			writeErr(LUANCH_PROGRESS_INFO + "Can't Connect An App : " + AppURL);
		}
		writeOut(LUANCH_PROGRESS_INFO + AppURL);
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
				// TODO Auto-generated method stub
				String resultLine;
				try {
					while ((resultLine = stdErr.readLine()) != null) {
						System.err.println(resultLine);
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

	protected void writeOut(String line) {
		line += "\n";
		installProgressView.append(line);
		logger.info(line);
	}

	protected void writeErr(String line) {
		line += "\n";
		installProgressView.append(line);
		logger.fatal(line);
	}

}