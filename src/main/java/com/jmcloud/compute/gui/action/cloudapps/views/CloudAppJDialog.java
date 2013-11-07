package com.jmcloud.compute.gui.action.cloudapps.views;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

public class CloudAppJDialog extends JDialog {
	private JTextArea cloudAppLogView;
	private JToolBar cloudAppActiontoolBar;
	public CloudAppJDialog(JFrame frame) {
		super(frame);
		setBounds(100, 100, 600, 400);
		setLocationRelativeTo(frame);
		getContentPane().setLayout(new BorderLayout());
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		cloudAppLogView = new JTextArea();
		cloudAppLogView.setEditable(false);
//		((DefaultCaret) cloudAppLogView.getCaret())
//				.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollPane.setViewportView(cloudAppLogView);

		cloudAppActiontoolBar = new JToolBar();
		cloudAppActiontoolBar.setRollover(true);
		getContentPane().add(cloudAppActiontoolBar, BorderLayout.SOUTH);
		setModal(false);
	}

	public JTextArea getCloudAppLogView() {
		return cloudAppLogView;
	}
	public JToolBar getCloudAppActiontoolBar() {
		return cloudAppActiontoolBar;
	}
}
