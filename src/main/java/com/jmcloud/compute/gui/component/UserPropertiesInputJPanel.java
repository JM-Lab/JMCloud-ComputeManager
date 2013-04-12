package com.jmcloud.compute.gui.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class UserPropertiesInputJPanel extends JPanel {
	private JLabel messageLabel;
	private JTextField eC2CLIHomeTextField;
	private JTextField accessKeyTextField;
	private JTextField secretKeyTextField;

	/**
	 * Create the panel.
	 */
	public UserPropertiesInputJPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0 };
		gridBagLayout.columnWidths = new int[] { 150, 150 };
		this.setLayout(gridBagLayout);
		// multiInputDialogPanel.setSize(300, 300);

		messageLabel = new JLabel("");
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_messageLabel = new GridBagConstraints();
		gbc_messageLabel.gridwidth = 2;
		gbc_messageLabel.insets = new Insets(0, 0, 5, 0);
		gbc_messageLabel.gridx = 0;
		gbc_messageLabel.gridy = 0;
		add(messageLabel, gbc_messageLabel);

		JLabel eC2CLIHomeLabel = new JLabel("EC2 CLI Home");
		eC2CLIHomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_eC2CLIHomeLabel = new GridBagConstraints();
		gbc_eC2CLIHomeLabel.insets = new Insets(0, 0, 5, 5);
		gbc_eC2CLIHomeLabel.gridx = 0;
		gbc_eC2CLIHomeLabel.gridy = 1;
		this.add(eC2CLIHomeLabel, gbc_eC2CLIHomeLabel);

		eC2CLIHomeTextField = new JTextField();
		GridBagConstraints gbc_eC2CLIHomeTextField = new GridBagConstraints();
		gbc_eC2CLIHomeTextField.insets = new Insets(0, 0, 5, 0);
		gbc_eC2CLIHomeTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_eC2CLIHomeTextField.gridx = 1;
		gbc_eC2CLIHomeTextField.gridy = 1;
		this.add(eC2CLIHomeTextField, gbc_eC2CLIHomeTextField);

		JLabel accessKeyLabel = new JLabel("AWS ACCESS KEY");
		accessKeyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_accessKeyLabel = new GridBagConstraints();
		gbc_accessKeyLabel.insets = new Insets(0, 0, 5, 5);
		gbc_accessKeyLabel.gridx = 0;
		gbc_accessKeyLabel.gridy = 2;
		this.add(accessKeyLabel, gbc_accessKeyLabel);

		accessKeyTextField = new JTextField();
		GridBagConstraints gbc_accessKeyTextField = new GridBagConstraints();
		gbc_accessKeyTextField.insets = new Insets(0, 0, 5, 0);
		gbc_accessKeyTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_accessKeyTextField.gridx = 1;
		gbc_accessKeyTextField.gridy = 2;
		this.add(accessKeyTextField, gbc_accessKeyTextField);

		JLabel secretKeyLabel = new JLabel("AWS SECRET KEY");
		secretKeyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_secretKeyLabel = new GridBagConstraints();
		gbc_secretKeyLabel.insets = new Insets(0, 0, 0, 5);
		gbc_secretKeyLabel.gridx = 0;
		gbc_secretKeyLabel.gridy = 3;
		this.add(secretKeyLabel, gbc_secretKeyLabel);
		secretKeyLabel.setHorizontalAlignment(SwingConstants.CENTER);

		secretKeyTextField = new JTextField();
		GridBagConstraints gbc_secretKeyTextField = new GridBagConstraints();
		gbc_secretKeyTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_secretKeyTextField.gridx = 1;
		gbc_secretKeyTextField.gridy = 3;
		add(secretKeyTextField, gbc_secretKeyTextField);
		secretKeyTextField.setColumns(10);
	}

	public JLabel getMessageLabel() {
		return messageLabel;
	}

	public JTextField getEC2CLIHomeTextField() {
		return eC2CLIHomeTextField;
	}

	public JTextField getAccessKeyTextField() {
		return accessKeyTextField;
	}

	public JTextField getSecretKeyTextField() {
		return secretKeyTextField;
	}
}
