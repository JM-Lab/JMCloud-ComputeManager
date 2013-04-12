package com.jmcloud.compute.gui.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChangePropertiesJPanel extends JPanel {
	private Map<String, JTextField> textFieldMap;
	private JLabel messageLabel;

	/**
	 * Create the panel.
	 */
	public ChangePropertiesJPanel(Properties properties) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		messageLabel = new JLabel("");
		GridBagConstraints gbc_messageLabel = new GridBagConstraints();
		gbc_messageLabel.gridwidth = 2;
		gbc_messageLabel.insets = new Insets(0, 0, 5, 5);
		gbc_messageLabel.gridx = 0;
		gbc_messageLabel.gridy = 0;
		add(messageLabel, gbc_messageLabel);

		Map<String, JTextField> textFieldMap = new HashMap<>();

		int i = 1;
		Enumeration<Object> keyLabelList = properties.keys();
		while (keyLabelList.hasMoreElements()) {
			String key = keyLabelList.nextElement().toString();
			textFieldMap.put(key, new JTextField(properties.getProperty(key)));

			GridBagConstraints gbc_propertyKeyLabel = new GridBagConstraints();
			gbc_propertyKeyLabel.insets = new Insets(0, 0, 0, 5);
			gbc_propertyKeyLabel.anchor = GridBagConstraints.EAST;
			gbc_propertyKeyLabel.gridx = 0;
			gbc_propertyKeyLabel.gridy = i;
			add(new JLabel(key), gbc_propertyKeyLabel);

			GridBagConstraints gbc_propertyValueField = new GridBagConstraints();
			gbc_propertyValueField.fill = GridBagConstraints.HORIZONTAL;
			gbc_propertyValueField.gridx = 1;
			gbc_propertyValueField.gridy = i;
			add(textFieldMap.get(key), gbc_propertyValueField);
			textFieldMap.get(key).setColumns(20);

			i++;
		}

	}

	public JLabel getMessageLabel() {
		return messageLabel;
	}

	public Map<String, JTextField> getTextFieldMap() {
		return textFieldMap;
	}

}
