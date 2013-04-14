package com.jmcloud.compute.gui.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SecurityRuleInputJPanel extends JPanel {
	private JTextField portRangeTextField;
	private JTextField ipRangeTextField;
	private JComboBox<String> protocalComboBox;
	private JLabel messageLabel;

	/**
	 * Create the panel.
	 */
	public SecurityRuleInputJPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 131, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		messageLabel = new JLabel("");
		GridBagConstraints gbc_messageLabel = new GridBagConstraints();
		gbc_messageLabel.gridwidth = 2;
		gbc_messageLabel.insets = new Insets(0, 0, 5, 5);
		gbc_messageLabel.gridx = 0;
		gbc_messageLabel.gridy = 0;
		add(messageLabel, gbc_messageLabel);

		JLabel lblProtocalLabel = new JLabel("Protocal");
		GridBagConstraints gbc_lblProtocalLabel = new GridBagConstraints();
		gbc_lblProtocalLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblProtocalLabel.gridx = 0;
		gbc_lblProtocalLabel.gridy = 1;
		add(lblProtocalLabel, gbc_lblProtocalLabel);

		protocalComboBox = new JComboBox<String>();
		protocalComboBox.setModel(new DefaultComboBoxModel<String>(
				new String[] { "TCP", "UDP", "ICMP (Ping)" }));
		GridBagConstraints gbc_protocalComboBox = new GridBagConstraints();
		gbc_protocalComboBox.anchor = GridBagConstraints.WEST;
		gbc_protocalComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_protocalComboBox.gridx = 1;
		gbc_protocalComboBox.gridy = 1;
		add(protocalComboBox, gbc_protocalComboBox);

		JLabel lblPortRangeLabel = new JLabel("Port Range");
		GridBagConstraints gbc_lblPortRangeLabel = new GridBagConstraints();
		gbc_lblPortRangeLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblPortRangeLabel.gridx = 0;
		gbc_lblPortRangeLabel.gridy = 2;
		add(lblPortRangeLabel, gbc_lblPortRangeLabel);

		portRangeTextField = new JTextField();
		GridBagConstraints gbc_portRangeTextField = new GridBagConstraints();
		gbc_portRangeTextField.insets = new Insets(0, 0, 5, 0);
		gbc_portRangeTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_portRangeTextField.gridx = 1;
		gbc_portRangeTextField.gridy = 2;
		add(portRangeTextField, gbc_portRangeTextField);
		portRangeTextField.setColumns(10);

		JLabel lblIpRangeLabel = new JLabel("IP Range (CIDR)");
		GridBagConstraints gbc_lblIpRangeLabel = new GridBagConstraints();
		gbc_lblIpRangeLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblIpRangeLabel.gridx = 0;
		gbc_lblIpRangeLabel.gridy = 3;
		add(lblIpRangeLabel, gbc_lblIpRangeLabel);

		ipRangeTextField = new JTextField();
		GridBagConstraints gbc_ipRangeTextField = new GridBagConstraints();
		gbc_ipRangeTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_ipRangeTextField.gridx = 1;
		gbc_ipRangeTextField.gridy = 3;
		add(ipRangeTextField, gbc_ipRangeTextField);
		ipRangeTextField.setColumns(10);

		protocalComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (protocalComboBox.getSelectedIndex() != 2) {
					portRangeTextField.setEnabled(true);
				} else {
					portRangeTextField.setEnabled(false);
				}
			}
		});

	}

	public JComboBox<String> getProtocalComboBox() {
		return protocalComboBox;
	}

	public JTextField getPortRangeTextField() {
		return portRangeTextField;
	}

	public JTextField getIpRangeTextField() {
		return ipRangeTextField;
	}

	public JLabel getMessageLabel() {
		return messageLabel;
	}
}
