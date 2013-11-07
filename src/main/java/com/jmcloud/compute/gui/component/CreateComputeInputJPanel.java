package com.jmcloud.compute.gui.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class CreateComputeInputJPanel extends JPanel {
	private JLabel messageLabel;
	private JTextField computeNameTextField;
	private JComboBox<String> imageIDComboBox;
	private JComboBox<String> computeTypesComboBox;
	private JComboBox<String> numOfComputeComboBox;

	/**
	 * Create the panel.
	 */
	public CreateComputeInputJPanel() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 150, 150 };
		this.setLayout(gridBagLayout);
		// multiInputDialogPanel.setSize(300, 300);

		messageLabel = new JLabel("");
		GridBagConstraints gbc_messageLabel = new GridBagConstraints();
		gbc_messageLabel.anchor = GridBagConstraints.WEST;
		gbc_messageLabel.gridwidth = 2;
		gbc_messageLabel.insets = new Insets(0, 0, 5, 0);
		gbc_messageLabel.gridx = 0;
		gbc_messageLabel.gridy = 0;
		add(messageLabel, gbc_messageLabel);

		JLabel computeNameLabel = new JLabel("Compute Name");
		computeNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbcl1 = new GridBagConstraints();
		gbcl1.insets = new Insets(0, 0, 5, 5);
		gbcl1.gridx = 0;
		gbcl1.gridy = 1;
		this.add(computeNameLabel, gbcl1);

		computeNameTextField = new JTextField();
		GridBagConstraints gbc_computeNameTextField = new GridBagConstraints();
		gbc_computeNameTextField.insets = new Insets(0, 0, 5, 0);
		gbc_computeNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_computeNameTextField.gridx = 1;
		gbc_computeNameTextField.gridy = 1;
		this.add(computeNameTextField, gbc_computeNameTextField);

		JLabel imageIDLabel = new JLabel("Image ID");
		imageIDLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbcl2 = new GridBagConstraints();
		gbcl2.insets = new Insets(0, 0, 5, 5);
		gbcl2.gridx = 0;
		gbcl2.gridy = 2;
		this.add(imageIDLabel, gbcl2);

		imageIDComboBox = new JComboBox<String>();
		imageIDComboBox.setEditable(true);
		GridBagConstraints gbc_imageIDTextField = new GridBagConstraints();
		gbc_imageIDTextField.insets = new Insets(0, 0, 5, 0);
		gbc_imageIDTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_imageIDTextField.gridx = 1;
		gbc_imageIDTextField.gridy = 2;
		this.add(imageIDComboBox, gbc_imageIDTextField);

		JLabel computeTypeLabel = new JLabel("Compute Type");
		computeTypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbcl3 = new GridBagConstraints();
		gbcl3.insets = new Insets(0, 0, 5, 5);
		gbcl3.gridx = 0;
		gbcl3.gridy = 3;
		this.add(computeTypeLabel, gbcl3);

		computeTypesComboBox = new JComboBox<String>();
		computeTypesComboBox.setEditable(true);
		GridBagConstraints gbc_computeTypesComboBox = new GridBagConstraints();
		gbc_computeTypesComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_computeTypesComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_computeTypesComboBox.gridx = 1;
		gbc_computeTypesComboBox.gridy = 3;
		this.add(computeTypesComboBox, gbc_computeTypesComboBox);

		JLabel createNumOfComputeLabel = new JLabel("Number Of Computes");
		computeTypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbcl4 = new GridBagConstraints();
		gbcl4.insets = new Insets(0, 0, 0, 5);
		gbcl4.gridx = 0;
		gbcl4.gridy = 4;
		this.add(createNumOfComputeLabel, gbcl4);

		numOfComputeComboBox = new JComboBox<String>();
		numOfComputeComboBox.setEditable(true);
		numOfComputeComboBox.setModel(new DefaultComboBoxModel<String>(
				new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9",
						"10" }));
		GridBagConstraints gbc_numOfComputeComboBox = new GridBagConstraints();
		gbc_numOfComputeComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_numOfComputeComboBox.gridx = 1;
		gbc_numOfComputeComboBox.gridy = 4;
		this.add(numOfComputeComboBox, gbc_numOfComputeComboBox);
	}

	public JLabel getMessageLabel() {
		return messageLabel;
	}

	public JTextField getComputeNameTextField() {
		return computeNameTextField;
	}

	public JComboBox<String> getImageIDComboBox() {
		return imageIDComboBox;
	}

	public JComboBox<String> getComputeTypeComboBox() {
		return computeTypesComboBox;
	}

	public JComboBox<String> getNumOfComputeComboBox() {
		return numOfComputeComboBox;
	}
}
