package com.jmcloud.compute;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jmcloud.compute.aws.ec2.sys.EC2EnviromentVO;
import com.jmcloud.compute.gui.ComputeManagerGUI;
import com.jmcloud.compute.gui.component.DialogsUtil;
import com.jmcloud.compute.sys.SystemEnviroment;
import com.jmcloud.compute.util.SysUtils;

public class JMCloudComputeMain {

	private static final String EC2_HOME = "EC2_HOME";
	private static final String CYGWIN_HOME = "CYGWIN_HOME";
	private static final String AWS_ACCESS_KEY = "AWS_ACCESS_KEY";
	private static final String AWS_SECRET_KEY = "AWS_SECRET_KEY";

	// private static final String CONSOLE_FILE_PATH = "CONSOLE_FILE_PATH";

	public static void main(String[] args) {

		// set required system properties for log4j
		System.setProperty("user.app.dir", SystemEnviroment.getUserDir());

		if (!new File(SystemEnviroment.getUserDir()).exists()) {
			new File(SystemEnviroment.getUserDir()).mkdirs();
		}

		// set GUI LookAndFeel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// set spring context
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				SystemEnviroment.getSpringConfPath());
		context.registerShutdownHook();

		// set GUI initial Configuration
		final ComputeManagerGUI computeManagerGUI = context.getBean(
				"computeManagerGUI", ComputeManagerGUI.class);
		computeManagerGUI.setBounds(100, 100, 800, 600);
		computeManagerGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		computeManagerGUI.initComponent();

		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = computeManagerGUI.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		computeManagerGUI.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);

		// CYGWIN_HOME env variable check only windows
		if (SystemEnviroment.getOS().contains("Windows")) {
			String path = System.getenv("path");
			if (path == null) {
				path = System.getenv("PATH");
			}
			String cygwinHome = System.getenv(CYGWIN_HOME);

			if (cygwinHome == null || cygwinHome.equals("")
					|| !path.contains(cygwinHome)) {
				DialogsUtil.showErrorDialogAndConnetWikiAndExit(computeManagerGUI,
								"<html>Set CYGWIN_HOME & bin path environment variable properly!!!<br><center>*** See Requirements on the Wiki ***</center></html>");
			}
		}

		EC2EnviromentVO eC2EnviromentVO = context.getBean("eC2EnviromentVO",
				EC2EnviromentVO.class);

		// EC2_HOME env variable check
		String ec2Home = System.getenv(EC2_HOME);
		if (ec2Home == null || ec2Home.equals("")) {
			DialogsUtil.showErrorDialogAndConnetWikiAndExit(computeManagerGUI,
					"<html>Set EC2_HOME & bin path environment variable properly!!!<br><center>*** See Requirements on the Wiki ***</center></html>");
		}
		eC2EnviromentVO.setEC2CLIHome(ec2Home);

		// set user properties
		Path userEnvPath = Paths.get(SystemEnviroment.getUserEnvPath());
		if (!userEnvPath.toFile().exists()) {
			Properties defaultAWSEC2EnvProperties = context.getBean(
					"defaultAWSEC2EnvProperties", Properties.class);

			Properties userProperties = DialogsUtil
					.showUserPropertiesInputDialog(computeManagerGUI,
							"Set Your AWS Information",
							defaultAWSEC2EnvProperties);
			if (userProperties == null
					|| "".equals(userProperties.get(AWS_ACCESS_KEY))
					|| "".equals(userProperties.get(AWS_SECRET_KEY))) {
				DialogsUtil.showErrorDialogAndConnetWikiAndExit(computeManagerGUI,
						"Set AWS KEYs properly!!!");
			}

			// if (SystemEnviroment.getConsoleFilePath() != null) {
			// userProperties.setProperty(CONSOLE_FILE_PATH,
			// SystemEnviroment.getConsoleFilePath());
			// }

			if (!SysUtils
					.saveProperties(
							userProperties,
							userEnvPath,
							"########### If you change a value, you should restart ###########\nUser Enviroment Properties")) {
				DialogsUtil.showErrorDialogAndConnetWikiAndExit(computeManagerGUI,
						"Can't Save!!! : "
								+ userEnvPath.toFile().getAbsolutePath());
			}
		}
		Properties userProperties = SysUtils.getProperties(userEnvPath.toUri());
		eC2EnviromentVO
				.setAccessKey(userProperties.getProperty(AWS_ACCESS_KEY));
		eC2EnviromentVO
				.setSecretKey(userProperties.getProperty(AWS_SECRET_KEY));
		// SystemEnviroment.setConsoleFilePath(userProperties.getProperty(CONSOLE_FILE_PATH));

		// GUI run
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				computeManagerGUI.setVisible(true);
			}
		});
	}

}
