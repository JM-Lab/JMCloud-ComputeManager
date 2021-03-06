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

		JLabel accessKeyLabel = new JLabel("AWS ACCESS KEY");
		accessKeyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_accessKeyLabel = new GridBagConstraints();
		gbc_accessKeyLabel.insets = new Insets(0, 0, 5, 5);
		gbc_accessKeyLabel.gridx = 0;
		gbc_accessKeyLabel.gridy = 1;
		this.add(accessKeyLabel, gbc_accessKeyLabel);

		accessKeyTextField = new JTextField();
		GridBagConstraints gbc_accessKeyTextField = new GridBagConstraints();
		gbc_accessKeyTextField.insets = new Insets(0, 0, 5, 0);
		gbc_accessKeyTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_accessKeyTextField.gridx = 1;
		gbc_accessKeyTextField.gridy = 1;
		this.add(accessKeyTextField, gbc_accessKeyTextField);

		JLabel secretKeyLabel = new JLabel("AWS SECRET KEY");
		secretKeyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_secretKeyLabel = new GridBagConstraints();
		gbc_secretKeyLabel.insets = new Insets(0, 0, 0, 5);
		gbc_secretKeyLabel.gridx = 0;
		gbc_secretKeyLabel.gridy = 2;
		this.add(secretKeyLabel, gbc_secretKeyLabel);
		secretKeyLabel.setHorizontalAlignment(SwingConstants.CENTER);

		secretKeyTextField = new JTextField();
		GridBagConstraints gbc_secretKeyTextField = new GridBagConstraints();
		gbc_secretKeyTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_secretKeyTextField.gridx = 1;
		gbc_secretKeyTextField.gridy = 2;
		add(secretKeyTextField, gbc_secretKeyTextField);
		secretKeyTextField.setColumns(10);
	}

	public JLabel getMessageLabel() {
		return messageLabel;
	}

	public JTextField getAccessKeyTextField() {
		return accessKeyTextField;
	}

	public JTextField getSecretKeyTextField() {
		return secretKeyTextField;
	}
}
