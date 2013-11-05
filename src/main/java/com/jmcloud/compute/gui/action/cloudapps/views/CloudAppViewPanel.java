package com.jmcloud.compute.gui.action.cloudapps.views;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import java.awt.BorderLayout;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.text.DefaultCaret;

public class CloudAppViewPanel extends JPanel {

	private JTextArea textArea;
	private JButton btnConnectApp;
	private JButton btnConnectSsh;

	/**
	 * Create the panel.
	 */
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
		
		JToolBar toolBar = new JToolBar();
		add(toolBar, BorderLayout.SOUTH);
		
		btnConnectApp = new JButton("Connect App");
		toolBar.add(btnConnectApp);
		
		btnConnectSsh = new JButton("Connect SSH");
		toolBar.add(btnConnectSsh);

	}
	
	public JTextArea getTextArea() {
		return textArea;
	}

	public JButton getBtnConnectApp() {
		return btnConnectApp;
	}
	public JButton getBtnConnectSsh() {
		return btnConnectSsh;
	}
}
