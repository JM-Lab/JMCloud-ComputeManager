package com.jmcloud.compute.gui.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class CreateComputeGroupInputJPanel extends JPanel {
	private JTextField computeGroupNameTextField;
	private JCheckBox chckbxDefaultSecurityRules;
	private JLabel messageLabel;

	/**
	 * Create the panel.
	 */
	public CreateComputeGroupInputJPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 150, 150 };
		this.setLayout(gridBagLayout);

		messageLabel = new JLabel("");
		GridBagConstraints gbc_messageLabel = new GridBagConstraints();
		gbc_messageLabel.gridwidth = 2;
		gbc_messageLabel.insets = new Insets(0, 0, 5, 5);
		gbc_messageLabel.gridx = 0;
		gbc_messageLabel.gridy = 0;
		add(messageLabel, gbc_messageLabel);

		JLabel computeGroupNameLabel = new JLabel("Compute Group Name");
		computeGroupNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_computeGroupNameLabel = new GridBagConstraints();
		gbc_computeGroupNameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_computeGroupNameLabel.gridx = 0;
		gbc_computeGroupNameLabel.gridy = 1;
		this.add(computeGroupNameLabel, gbc_computeGroupNameLabel);

		computeGroupNameTextField = new JTextField();
		GridBagConstraints gbc_computeGroupNameTextField = new GridBagConstraints();
		gbc_computeGroupNameTextField.insets = new Insets(0, 0, 5, 0);
		gbc_computeGroupNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_computeGroupNameTextField.gridx = 1;
		gbc_computeGroupNameTextField.gridy = 1;
		this.add(computeGroupNameTextField, gbc_computeGroupNameTextField);

		chckbxDefaultSecurityRules = new JCheckBox(
				"Set Default Security Rules (Ping, SSH from any)");
		chckbxDefaultSecurityRules.setSelected(true);
		GridBagConstraints gbc_chckbxDefaultSecurityRules = new GridBagConstraints();
		gbc_chckbxDefaultSecurityRules.gridwidth = 2;
		gbc_chckbxDefaultSecurityRules.gridx = 0;
		gbc_chckbxDefaultSecurityRules.gridy = 2;
		add(chckbxDefaultSecurityRules, gbc_chckbxDefaultSecurityRules);
	}

	public JTextField getComputeNameTextField() {
		return computeGroupNameTextField;
	}

	public JCheckBox getSetDefaultSecurityChckbx() {
		return chckbxDefaultSecurityRules;
	}

	public JLabel getMessageLabel() {
		return messageLabel;
	}
}
