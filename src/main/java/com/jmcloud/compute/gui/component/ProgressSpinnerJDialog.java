package com.jmcloud.compute.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.jmcloud.compute.sys.SystemImages;

public class ProgressSpinnerJDialog extends JDialog {
	/**
	 * Create the dialog.
	 */
	public ProgressSpinnerJDialog(JFrame frame) {
		setBounds(100, 100, 65, 50);
		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		setUndecorated(true);
		setOpacity(0.5f);
		setAlwaysOnTop(true);
		setModal(true);

		JLabel spinnerImageLabel = new JLabel(SystemImages.getSpinnerImage());
		spinnerImageLabel.setBackground(Color.WHITE);
		spinnerImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		spinnerImageLabel.setOpaque(true);
		contentPanel.add(spinnerImageLabel, BorderLayout.CENTER);
		JLabel progressLabel = new JLabel("Progress...");
		progressLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 12));
		progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(progressLabel, BorderLayout.SOUTH);
	}

}
