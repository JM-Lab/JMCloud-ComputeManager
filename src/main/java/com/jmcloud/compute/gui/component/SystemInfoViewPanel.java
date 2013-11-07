package com.jmcloud.compute.gui.component;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

public class SystemInfoViewPanel extends JPanel {
	private JTextArea textArea;

	public SystemInfoViewPanel() {
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane, BorderLayout.CENTER);

		textArea = new JTextArea();
		textArea.setRows(5);
		textArea.setEditable(false);
		textArea.setTabSize(4);
//		((DefaultCaret) textArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollPane.setViewportView(textArea);
	}

	public JTextArea getTextArea() {
		return textArea;
	}
}
