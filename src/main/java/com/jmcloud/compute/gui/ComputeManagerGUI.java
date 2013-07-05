package com.jmcloud.compute.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.annotation.Resource;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JPopupMenu.Separator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.gui.action.JMCloudGUIAction;
import com.jmcloud.compute.gui.component.SystemInfoViewPanel;
import com.jmcloud.compute.gui.component.TableViewPanel;
import com.jmcloud.compute.gui.component.TreeViewPanel;
import com.jmcloud.compute.gui.model.ComputeManagerGUIModel;
import com.jmcloud.compute.sys.SystemImages;

@Service("computeManagerGUI")
public class ComputeManagerGUI extends JFrame {

	private final Action saveRegionAction = new SaveAction();
	private final Action loadRegionAction = new ReloadAction();
	private final Action exitAction = new ExitAction();

	private final Action createGroupAction = new CreateGroupAction();
	private final Action deleteGroupAction = new DeleteGroupAction();
	private final Action renameGroupAction = new RenameGroupAction();
	private final Action provisionGroupAction = new ProvisioningGroupAction();
	private final Action stopGroupAction = new StopGroupAction();
	private final Action startGroupAction = new StartGroupAction();
	private final Action rebootGroupAction = new RebootGroupAction();
	private final Action terminateGroupAction = new TerminateGroupAction();
	private final Action downloadKeypairAction = new DownloadKeypairAction();
	private final Action createKeypairAction = new CreateGroupKeypairAction();

	private final Action showFWRulesAction = new ShowFWRulesAction();
	private final Action addFWRuleAction = new AddFWRuleAction();
	private final Action removeFWRuleAction = new RemoveFWRuleAction();

	private final Action createComputeAction = new CreateComputeAction();
	private final Action createComputesAction = new CreateComputesAction();
	private final Action deleteComputesAction = new DeleteComputeAction();
	private final Action renameComputeAction = new RenameComputeAction();
	private final Action provisionComputesAction = new ProvisioningComputeAction();
	private final Action stopComputesAction = new StopComputeAction();
	private final Action startComputesAction = new StartComputeAction();
	private final Action rebootComputesAction = new RebootComputeAction();
	private final Action terminateComputesAction = new TerminateComputeAction();

	private class SaveAction extends AbstractAction {
		public SaveAction() {
			putValue(NAME, "Save Region");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class ReloadAction extends AbstractAction {
		public ReloadAction() {
			putValue(NAME, "Load Region");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class ExitAction extends AbstractAction {
		public ExitAction() {
			putValue(NAME, "Exit");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class CreateGroupAction extends AbstractAction {
		public CreateGroupAction() {
			putValue(NAME, "Create Group");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class DeleteGroupAction extends AbstractAction {
		public DeleteGroupAction() {
			putValue(NAME, "Delete Group");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class RenameGroupAction extends AbstractAction {
		public RenameGroupAction() {
			putValue(NAME, "Rename Group");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class ProvisioningGroupAction extends AbstractAction {
		public ProvisioningGroupAction() {
			putValue(NAME, "Provision Group");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class StopGroupAction extends AbstractAction {
		public StopGroupAction() {
			putValue(NAME, "Stop Group");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class StartGroupAction extends AbstractAction {
		public StartGroupAction() {
			putValue(NAME, "Start Group");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class RebootGroupAction extends AbstractAction {
		public RebootGroupAction() {
			putValue(NAME, "Reboot Group");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class TerminateGroupAction extends AbstractAction {
		public TerminateGroupAction() {
			putValue(NAME, "Terminate Group");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class DownloadKeypairAction extends AbstractAction {
		public DownloadKeypairAction() {
			putValue(NAME, "Download Keypair");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class CreateGroupKeypairAction extends AbstractAction {
		public CreateGroupKeypairAction() {
			putValue(NAME, "Create Keypair");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class ShowFWRulesAction extends AbstractAction {
		public ShowFWRulesAction() {
			putValue(NAME, "Show Rules");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class AddFWRuleAction extends AbstractAction {
		public AddFWRuleAction() {
			putValue(NAME, "Add Rule");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class RemoveFWRuleAction extends AbstractAction {
		public RemoveFWRuleAction() {
			putValue(NAME, "Remove Rule");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class CreateComputeAction extends AbstractAction {
		public CreateComputeAction() {
			putValue(NAME, "Create Compute");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class CreateComputesAction extends AbstractAction {
		public CreateComputesAction() {
			putValue(NAME, "Create Computes");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class DeleteComputeAction extends AbstractAction {
		public DeleteComputeAction() {
			putValue(NAME, "Delete Computes");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class RenameComputeAction extends AbstractAction {
		public RenameComputeAction() {
			putValue(NAME, "Rename Compute");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class ProvisioningComputeAction extends AbstractAction {
		public ProvisioningComputeAction() {
			putValue(NAME, "Provision Computes");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class StopComputeAction extends AbstractAction {
		public StopComputeAction() {
			putValue(NAME, "Stop Computes");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class StartComputeAction extends AbstractAction {
		public StartComputeAction() {
			putValue(NAME, "Start Computes");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class RebootComputeAction extends AbstractAction {
		public RebootComputeAction() {
			putValue(NAME, "Reboot Computes");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	private class TerminateComputeAction extends AbstractAction {
		public TerminateComputeAction() {
			putValue(NAME, "Terminate Computes");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}
	}

	@Resource(name = "computeManagerGUIModel")
	private ComputeManagerGUIModel computeManagerGUIModel;

	@Resource(name = "computeActionSelector")
	private JMCloudGUIAction action;

	private JTree tree;

	private JTable table;

	private JTextArea systemInfoViewTextArea;

	private JMenuBar mainMenuBar;

	/**
	 * Create the application.
	 */
	public ComputeManagerGUI() {
		this.initializeGUI();
		this.initializeMainMenu();
		this.initializeListener();
	}

	private void initializeMainMenu() {
		mainMenuBar = new JMenuBar();
		this.setJMenuBar(mainMenuBar);

		JMenu mnFile = new JMenu("File");
		mainMenuBar.add(mnFile);

		JMenuItem mntmSaveRegion = new JMenuItem("Save Region");
		mntmSaveRegion.setAction(saveRegionAction);
		mnFile.add(mntmSaveRegion);

		JMenuItem mntmLoadRegion = new JMenuItem("Load Region");
		mntmLoadRegion.setAction(loadRegionAction);
		mnFile.add(mntmLoadRegion);

		Separator separator_3 = new Separator();
		mnFile.add(separator_3);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setAction(exitAction);
		mnFile.add(mntmExit);

		JMenu mnGroup = new JMenu("Group");
		mainMenuBar.add(mnGroup);

		JMenuItem mntmCreateGroup = new JMenuItem("Create Group");
		mntmCreateGroup.setAction(createGroupAction);
		mnGroup.add(mntmCreateGroup);

		JMenuItem mntmDeleteGroup = new JMenuItem("Delete Group");
		mntmDeleteGroup.setAction(deleteGroupAction);
		mnGroup.add(mntmDeleteGroup);

		JMenuItem mntmRenameGroup = new JMenuItem("Rename Group");
		mntmRenameGroup.setAction(renameGroupAction);
		mnGroup.add(mntmRenameGroup);

		Separator separator = new JPopupMenu.Separator();
		mnGroup.add(separator);

		JMenuItem mntmProvisionGroup = new JMenuItem("Provision Group");
		mntmProvisionGroup.setAction(provisionGroupAction);
		mnGroup.add(mntmProvisionGroup);

		JMenuItem mntmStopGroup = new JMenuItem("Stop Group");
		mntmStopGroup.setAction(stopGroupAction);
		mnGroup.add(mntmStopGroup);

		JMenuItem mntmStartGroup = new JMenuItem("Start Group");
		mntmStartGroup.setAction(startGroupAction);
		mnGroup.add(mntmStartGroup);

		JMenuItem mntmRebootGroup = new JMenuItem("Reboot Group");
		mntmRebootGroup.setAction(rebootGroupAction);
		mnGroup.add(mntmRebootGroup);

		JMenuItem mntmTerminateGroup = new JMenuItem("Terminate Group");
		mntmTerminateGroup.setAction(terminateGroupAction);
		mnGroup.add(mntmTerminateGroup);

		Separator separator_1 = new Separator();
		mnGroup.add(separator_1);

		JMenu mnKeypair = new JMenu("Keypair");
		mnGroup.add(mnKeypair);

		JMenuItem mntmDownloadKeypair = new JMenuItem("Download Keypair");
		mntmDownloadKeypair.setAction(downloadKeypairAction);
		mnKeypair.add(mntmDownloadKeypair);

		JMenuItem mntmCreateKeypair = new JMenuItem("Create Keypair");
		mntmCreateKeypair.setAction(createKeypairAction);
		mnKeypair.add(mntmCreateKeypair);

		JMenu mnSecurityGroup = new JMenu("Security Group");
		mnGroup.add(mnSecurityGroup);

		JMenuItem mntmShowRules = new JMenuItem("Show Rules");
		mntmShowRules.setAction(showFWRulesAction);
		mnSecurityGroup.add(mntmShowRules);

		JMenuItem mntmAddRule = new JMenuItem("Add Rule");
		mntmAddRule.setAction(addFWRuleAction);
		mnSecurityGroup.add(mntmAddRule);

		JMenuItem mntmRemoveRule = new JMenuItem("Remove Rule");
		mntmRemoveRule.setAction(removeFWRuleAction);
		mnSecurityGroup.add(mntmRemoveRule);

		JMenu mnCompute = new JMenu("Compute");
		mainMenuBar.add(mnCompute);

		JMenuItem mntmCreateCompute = new JMenuItem("Create Compute");
		mntmCreateCompute.setAction(createComputeAction);
		mnCompute.add(mntmCreateCompute);

		JMenuItem mntmCreateComputes = new JMenuItem("Create Computes");
		mntmCreateComputes.setAction(createComputesAction);
		mnCompute.add(mntmCreateComputes);

		JMenuItem mntmDeleteComputes = new JMenuItem("Delete Computes");
		mntmDeleteComputes.setAction(deleteComputesAction);
		mnCompute.add(mntmDeleteComputes);

		JMenuItem mntmRenameCompute = new JMenuItem("Rename Compute");
		mntmRenameCompute.setAction(renameComputeAction);
		mnCompute.add(mntmRenameCompute);

		Separator separator_4 = new Separator();
		mnCompute.add(separator_4);

		JMenuItem mntmProvisionComputes = new JMenuItem("Provision Computes");
		mntmProvisionComputes.setAction(provisionComputesAction);
		mnCompute.add(mntmProvisionComputes);

		JMenuItem mntmStopComputes = new JMenuItem("Stop Computes");
		mntmStopComputes.setAction(stopComputesAction);
		mnCompute.add(mntmStopComputes);

		JMenuItem mntmStartComputes = new JMenuItem("Start Computes");
		mntmStartComputes.setAction(startComputesAction);
		mnCompute.add(mntmStartComputes);

		JMenuItem mntmRebootComputes = new JMenuItem("Reboot Computes");
		mntmRebootComputes.setAction(rebootComputesAction);
		mnCompute.add(mntmRebootComputes);

		JMenuItem mntmTerminateComputes = new JMenuItem("Terminate Computes");
		mntmTerminateComputes.setAction(terminateComputesAction);
		mnCompute.add(mntmTerminateComputes);

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeGUI() {
		this.setTitle("JMCloud-ComputeManager");
		this.setIconImage(SystemImages.getMainImage().getImage());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel mainPanel = new JPanel();
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));

		JSplitPane mainBottomSplitPane = new JSplitPane();
		mainBottomSplitPane.setResizeWeight(0.8);
		mainBottomSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mainPanel.add(mainBottomSplitPane, BorderLayout.CENTER);

		JPanel mainTopPanel = new JPanel();
		mainBottomSplitPane.setLeftComponent(mainTopPanel);
		mainTopPanel.setLayout(new BorderLayout(0, 0));

		JSplitPane mainTopSplitPane = new JSplitPane();
		mainTopPanel.add(mainTopSplitPane);

		TreeViewPanel treeViewPanel = new TreeViewPanel();
		mainTopSplitPane.setLeftComponent(treeViewPanel);
		tree = treeViewPanel.getTree();
		initializeTreePopUpMenu(tree);

		TableViewPanel tableViewPanel = new TableViewPanel();
		mainTopSplitPane.setRightComponent(tableViewPanel);
		table = tableViewPanel.getTable();
		initializeTablePopUpMenu(table);

		SystemInfoViewPanel systemInfoViewPanel = new SystemInfoViewPanel();
		mainBottomSplitPane.setRightComponent(systemInfoViewPanel);
		systemInfoViewTextArea = systemInfoViewPanel.getTextArea();
	}

	private void initializeTablePopUpMenu(JTable table) {
		JPopupMenu tablePopupMenu = new JPopupMenu();
		addPopup(table, tablePopupMenu);

		JMenuItem mntmCreateCompute = new JMenuItem("Create Compute");
		mntmCreateCompute.setAction(createComputeAction);
		tablePopupMenu.add(mntmCreateCompute);

		JMenuItem mntmCreateComputes = new JMenuItem("Create Computes");
		mntmCreateComputes.setAction(createComputesAction);
		tablePopupMenu.add(mntmCreateComputes);

		JMenuItem mntmDeleteComputes = new JMenuItem("Delete Computes");
		mntmDeleteComputes.setAction(deleteComputesAction);
		tablePopupMenu.add(mntmDeleteComputes);

		JMenuItem mntmRenameCompute = new JMenuItem("Rename Compute");
		mntmRenameCompute.setAction(renameComputeAction);
		tablePopupMenu.add(mntmRenameCompute);

		Separator separator_4 = new Separator();
		tablePopupMenu.add(separator_4);

		JMenuItem mntmProvisionComputes = new JMenuItem("Provision Computes");
		mntmProvisionComputes.setAction(provisionComputesAction);
		tablePopupMenu.add(mntmProvisionComputes);

		JMenuItem mntmStopComputes = new JMenuItem("Stop Computes");
		mntmStopComputes.setAction(stopComputesAction);
		tablePopupMenu.add(mntmStopComputes);

		JMenuItem mntmStartComputes = new JMenuItem("Start Computes");
		mntmStartComputes.setAction(startComputesAction);
		tablePopupMenu.add(mntmStartComputes);

		JMenuItem mntmRebootComputes = new JMenuItem("Reboot Computes");
		mntmRebootComputes.setAction(rebootComputesAction);
		tablePopupMenu.add(mntmRebootComputes);

		JMenuItem mntmTerminateComputes = new JMenuItem("Terminate Computes");
		mntmTerminateComputes.setAction(terminateComputesAction);
		tablePopupMenu.add(mntmTerminateComputes);
	}

	private void initializeTreePopUpMenu(JTree tree) {
		JPopupMenu treePopupMenu = new JPopupMenu();
		addPopup(tree, treePopupMenu);

		JMenuItem mntmCreateGroup = new JMenuItem("Create Group");
		mntmCreateGroup.setAction(createGroupAction);
		treePopupMenu.add(mntmCreateGroup);

		JMenuItem mntmDeleteGroup = new JMenuItem("Delete Group");
		mntmDeleteGroup.setAction(deleteGroupAction);
		treePopupMenu.add(mntmDeleteGroup);

		JMenuItem mntmRenameGroup = new JMenuItem("Rename Group");
		mntmRenameGroup.setAction(renameGroupAction);
		treePopupMenu.add(mntmRenameGroup);

		Separator separator = new JPopupMenu.Separator();
		treePopupMenu.add(separator);

		JMenuItem mntmProvisionGroup = new JMenuItem("Provision Group");
		mntmProvisionGroup.setAction(provisionGroupAction);
		treePopupMenu.add(mntmProvisionGroup);

		JMenuItem mntmStopGroup = new JMenuItem("Stop Group");
		mntmStopGroup.setAction(stopGroupAction);
		treePopupMenu.add(mntmStopGroup);

		JMenuItem mntmStartGroup = new JMenuItem("Start Group");
		mntmStartGroup.setAction(startGroupAction);
		treePopupMenu.add(mntmStartGroup);

		JMenuItem mntmRebootGroup = new JMenuItem("Reboot Group");
		mntmRebootGroup.setAction(rebootGroupAction);
		treePopupMenu.add(mntmRebootGroup);

		JMenuItem mntmTerminateGroup = new JMenuItem("Terminate Group");
		mntmTerminateGroup.setAction(terminateGroupAction);
		treePopupMenu.add(mntmTerminateGroup);

		Separator separator_1 = new Separator();
		treePopupMenu.add(separator_1);

		JMenu mnKeypair = new JMenu("Keypair");
		treePopupMenu.add(mnKeypair);

		JMenuItem mntmDownloadKeypair = new JMenuItem("Download Keypair");
		mntmDownloadKeypair.setAction(downloadKeypairAction);
		mnKeypair.add(mntmDownloadKeypair);

		JMenuItem mntmCreateKeypair = new JMenuItem("Create Keypair");
		mntmCreateKeypair.setAction(createKeypairAction);
		mnKeypair.add(mntmCreateKeypair);

		JMenu mnSecurityGroup = new JMenu("Security Group");
		treePopupMenu.add(mnSecurityGroup);

		JMenuItem mntmShowRules = new JMenuItem("Show Rules");
		mntmShowRules.setAction(showFWRulesAction);
		mnSecurityGroup.add(mntmShowRules);

		JMenuItem mntmAddRule = new JMenuItem("Add Rule");
		mntmAddRule.setAction(addFWRuleAction);
		mnSecurityGroup.add(mntmAddRule);

		JMenuItem mntmRemoveRule = new JMenuItem("Remove Rule");
		mntmRemoveRule.setAction(removeFWRuleAction);
		mnSecurityGroup.add(mntmRemoveRule);

	}

	private void initializeListener() {
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				computeManagerGUIModel.seletionTreeNode(e);
			}
		});
	}

	public void initComponent() {
		computeManagerGUIModel.initModel(tree, table, systemInfoViewTextArea);
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
