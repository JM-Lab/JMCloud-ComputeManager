package com.jmcloud.compute.gui.action.cloudapps;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import com.jmcloud.compute.commons.UtilWithRunCLI;
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
	protected JToolBar appManagementButtonToolBar;
	protected String luanchCommand;
	protected Process process;

	abstract protected void showCloudAppView();

	abstract protected void showNextSteps();

	abstract protected void addManagementButten();

	@Override
	public void luanchApp(final JFrame mainFrame, String cloudAppRootDir,
			String publicIP, String keypair, String id, String luanchPackName) {
		initBeforeLuanchApp(mainFrame, cloudAppRootDir, publicIP, keypair, id,
				luanchPackName);
		addManagementButten();
		for (Component c : appManagementButtonToolBar.getComponents()) {
			if (c instanceof JComponent) {
				((JComponent) c).setBorder(new BevelBorder(BevelBorder.RAISED));
			}
		}

		Thread luanchAppThread = new Thread(new Runnable() {

			@Override
			public void run() {

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						showCloudAppView();
					}
				});

				writeOut(LUANCH_PROGRESS_INFO + "Command To Luanch An App : "
						+ luanchCommand);
				long startTime = System.currentTimeMillis();
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
					writeErr(LUANCH_PROGRESS_INFO + "Can't Luanch An App!!!");
				} finally {
					showelasoptime(startTime);
				}
			}
		});

		luanchAppThread.start();

	}

	private void showelasoptime(long startTime) {
		long endTime = System.currentTimeMillis();
		long elapsedTime = (endTime - startTime) / 1000;
		writeErr(LUANCH_PROGRESS_INFO + "Elapsed Time(s) : " + elapsedTime);
	}

	private void initBeforeLuanchApp(JFrame mainFrame, String cloudAppRootDir,
			final String publicIP, final String keypair, final String id,
			String luanchPackName) {
		this.mainFrame = mainFrame;
		this.cloudAppRootDir = cloudAppRootDir;
		this.publicIP = publicIP;
		this.keypair = keypair;
		this.id = id;
		this.luanchCommand = "ssh -o StrictHostKeyChecking=no -i " + keypair
				+ " " + id + "@" + publicIP + " sudo sh " + cloudAppRootDir
				+ luanchPackName + "/" + luanchPackName + ".sh";

		viewPanel = new CloudAppViewPanel();
		installProgressView = viewPanel.getTextArea();
		appManagementButtonToolBar = viewPanel.getAppManagementButtonToolBar();
		appManagementButtonToolBar.add(new AbstractAction("Connect Compute") {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (UtilWithRunCLI.connectServerWithSSH(keypair, id, publicIP,
						logger)) {
					writeOut(LUANCH_PROGRESS_INFO
							+ "Connect Compute With SSH Console");
				} else {
					writeErr(LUANCH_PROGRESS_INFO
							+ "Can't Connect Compute With SSH Console!!!");
				}
			}
		});
	}

	protected void connectAppWithBrowser(String appURL) {
		writeOut(LUANCH_PROGRESS_INFO + "Connect An App With Browser : "
				+ appURL);
		try {
			Desktop.getDesktop().browse(new URI(appURL));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			writeErr(LUANCH_PROGRESS_INFO
					+ "Can't Connect An App With Browser : " + appURL);
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