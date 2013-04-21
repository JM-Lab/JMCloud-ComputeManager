package com.jmcloud.compute;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.JFrame;
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

	private static final String SPING_CONF = SystemEnviroment
			.getSpringConfPath();

	private static final String EC2_CLI_HOME = "EC2_CLI_HOME";
	private static final String AWS_ACCESS_KEY = "AWS_ACCESS_KEY";
	private static final String AWS_SECRET_KEY = "AWS_SECRET_KEY";

	public static void main(String[] args) {
		
		// set required system properties
		System.setProperty("user.dir", SystemEnviroment.getUserDir());
		if(!new File(SystemEnviroment.getUserDir()).exists()){
			new File(SystemEnviroment.getUserDir()).mkdirs();
		}	

		// set spring context
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				SPING_CONF);
		context.registerShutdownHook();
		
		// set initial GUI
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}		
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
		
		// set user properties
		Path userEC2EnvPath = Paths.get(SystemEnviroment.getUserEC2EnvPath());
		if (!userEC2EnvPath.toFile().exists()) {
			Properties defaultAWSEC2EnvProperties = context.getBean(
					"defaultAWSEC2EnvProperties", Properties.class);

			Properties userProperties = DialogsUtil
					.showUserPropertiesInputDialog(computeManagerGUI,
							"Input Your AWS Information",
							defaultAWSEC2EnvProperties);

			SysUtils.saveProperties(userProperties, userEC2EnvPath,
					"User AWS EC2 Properties");
		}

		EC2EnviromentVO eC2EnviromentVO = context.getBean("eC2EnviromentVO",
				EC2EnviromentVO.class);

		Properties userProperties = SysUtils.getProperties(userEC2EnvPath
				.toUri());
		eC2EnviromentVO.setEC2CLIHome(userProperties.getProperty(EC2_CLI_HOME));
		eC2EnviromentVO
				.setAccessKey(userProperties.getProperty(AWS_ACCESS_KEY));
		eC2EnviromentVO
				.setSecretKey(userProperties.getProperty(AWS_SECRET_KEY));

		// GUI run
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				computeManagerGUI.setVisible(true);
			}
		});
	}

}
