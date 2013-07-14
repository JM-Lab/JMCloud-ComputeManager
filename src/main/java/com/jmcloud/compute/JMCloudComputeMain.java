package com.jmcloud.compute;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
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
	private static final String AWS_ACCESS_KEY = "AWS_ACCESS_KEY";
	private static final String AWS_SECRET_KEY = "AWS_SECRET_KEY";

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

		// EC2_HOME env variable check
		String ec2Home = System.getenv(EC2_HOME);
		
		if(ec2Home == null || ec2Home.equals("")){
			JOptionPane.showMessageDialog(computeManagerGUI, "Set EC2_HOME environment variable properly!!!", "JMCloud-ComputeManager", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		// set user properties
		Path userEC2EnvPath = Paths.get(SystemEnviroment.getUserEC2EnvPath());
		if (!userEC2EnvPath.toFile().exists()) {
			Properties defaultAWSEC2EnvProperties = context.getBean(
					"defaultAWSEC2EnvProperties", Properties.class);

			Properties userProperties = DialogsUtil
					.showUserPropertiesInputDialog(computeManagerGUI,
							"Input Your AWS Information",
							defaultAWSEC2EnvProperties);

			userProperties.setProperty(EC2_HOME, System.getenv(EC2_HOME));

			SysUtils.saveProperties(userProperties, userEC2EnvPath,
					"User AWS EC2 Properties");

		}

		EC2EnviromentVO eC2EnviromentVO = context.getBean("eC2EnviromentVO",
				EC2EnviromentVO.class);

		Properties userProperties = SysUtils.getProperties(userEC2EnvPath
				.toUri());
		eC2EnviromentVO.setEC2CLIHome(userProperties.getProperty(EC2_HOME));
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
