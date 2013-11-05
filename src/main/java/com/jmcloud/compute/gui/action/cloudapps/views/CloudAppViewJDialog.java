package com.jmcloud.compute.gui.action.cloudapps.views;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CloudAppViewJDialog extends JDialog {
	public CloudAppViewJDialog(JFrame frame, JPanel contentPanel, String title ) {
		super(frame);
		setBounds(100, 100, 600, 400);
		setLocationRelativeTo(frame);
		setTitle(title);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setAlwaysOnTop(true);
		setModal(false);
	}

}
