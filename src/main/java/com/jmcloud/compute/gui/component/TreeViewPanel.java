package com.jmcloud.compute.gui.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.jmcloud.compute.sys.SystemImages;
import com.jmcloud.compute.sys.SystemString;

public class TreeViewPanel extends JPanel {
	private JTree tree;

	public TreeViewPanel() {
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);

		tree = new JTree(new DefaultMutableTreeNode(new String(
				"JM Cloud Compute")), true);
		tree.setRootVisible(false);
		tree.setCellRenderer(new TreeRendererOfTreeView());
		scrollPane.setViewportView(tree);

		JLabel lblRegionngroup = new JLabel("<html>Region<br>|---Group<html>");
		lblRegionngroup.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 8));
		scrollPane.setColumnHeaderView(lblRegionngroup);
	}

	public JTree getTree() {
		return tree;
	}

	private class TreeRendererOfTreeView extends DefaultTreeCellRenderer {
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded,
					leaf, row, hasFocus);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			if (node.getParent() != null) {
				if (leaf) {
					setIcon(SystemImages.getComputeGroupImage());
				} else {
					String region = ((DefaultMutableTreeNode) value)
							.getUserObject().toString();
					setText(region + " " + SystemString.getCity(region));
					setIcon(new ImageIcon(
							SystemImages.getFlagImageURL(SystemString
									.getContry(region))));
				}
			}
			return this;
		}
	}
}
