package com.jmcloud.compute.gui.action.cloudapps.views;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.text.DefaultCaret;

public class CloudAppViewPanel extends JPanel {

	private JTextArea textArea;
	private JToolBar appManagementButtonToolBar;

	public CloudAppViewPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane, BorderLayout.CENTER);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setTabSize(4);
		((DefaultCaret) textArea.getCaret())
				.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollPane.setViewportView(textArea);
		
		appManagementButtonToolBar = new JToolBar();
		add(appManagementButtonToolBar, BorderLayout.SOUTH);
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}

	public JToolBar getAppManagementButtonToolBar() {
		return appManagementButtonToolBar;
	}
}
