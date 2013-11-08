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
	private final Action loadRegionAction = new LoadAction();
	// private final Action setConsoleAction = new SetConsoleAction();
	private final Action exitAction = new ExitAction();

	private final Action createGroupAction = new CreateGroupAction();
	private final Action deleteGroupAction = new DeleteGroupAction();
	// private final Action renameGroupAction = new RenameGroupAction();
	private final Action provisionGroupAction = new ProvisioningGroupAction();
	private final Action stopGroupAction = new StopGroupAction();
	private final Action startGroupAction = new StartGroupAction();
	// private final Action rebootGroupAction = new RebootGroupAction();
	private final Action terminateGroupAction = new TerminateGroupAction();
	private final Action downloadKeypairAction = new DownloadKeypairAction();
	private final Action createKeypairAction = new CreateGroupKeypairAction();

	private final Action showFWRulesAction = new ShowFWRulesAction();
	private final Action addFWRuleAction = new AddFWRuleAction();
	private final Action removeFWRuleAction = new RemoveFWRuleAction();

	private final Action createComputeAction = new CreateComputeAction();
	private final Action createComputesAction = new CreateComputesAction();
	private final Action deleteComputesAction = new DeleteComputesAction();
	private final Action renameComputeAction = new RenameComputeAction();
	private final Action provisionComputesAction = new ProvisioningComputesAction();
	private final Action stopComputesAction = new StopComputesAction();
	private final Action startComputesAction = new StartComputesAction();
	// private final Action rebootComputesAction = new RebootComputesAction();
	private final Action terminateComputesAction = new TerminateComputesAction();
	private final Action connectComputeAction = new ConnectComputeAction();

	private final Action launchRStudioServer = new LuanchRServerAction();

	private final Action openUserInformationAction = new OpenUserInformationAction();
	private final Action aboutJMCloudComputeManagerAction = new AboutJMCloudComputeManagerAction();

	private class AbstractComputeManagerGUIAction extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent e) {
			action.doAction(e);
		}

	}

	private class SaveAction extends AbstractComputeManagerGUIAction {
		public SaveAction() {
			putValue(NAME, "Save Region");
		}
	}

	private class LoadAction extends AbstractComputeManagerGUIAction {
		public LoadAction() {
			putValue(NAME, "Load Region");
		}
	}

	public class SetConsoleAction extends AbstractComputeManagerGUIAction {

		public SetConsoleAction() {
			putValue(NAME, "Set Console");
		}
	}

	private class ExitAction extends AbstractComputeManagerGUIAction {
		public ExitAction() {
			putValue(NAME, "Exit");
		}
	}

	private class CreateGroupAction extends AbstractComputeManagerGUIAction {
		public CreateGroupAction() {
			putValue(NAME, "Create Group");
		}
	}

	private class DeleteGroupAction extends AbstractComputeManagerGUIAction {
		public DeleteGroupAction() {
			putValue(NAME, "Delete Group");
		}
	}

	// private class RenameGroupAction extends AbstractComputeManagerGUIAction {
	// public RenameGroupAction() {
	// putValue(NAME, "Rename Group");
	// }
	// }

	private class ProvisioningGroupAction extends
			AbstractComputeManagerGUIAction {
		public ProvisioningGroupAction() {
			putValue(NAME, "Provision Group");
		}
	}

	private class StopGroupAction extends AbstractComputeManagerGUIAction {
		public StopGroupAction() {
			putValue(NAME, "Stop Group");
		}
	}

	private class StartGroupAction extends AbstractComputeManagerGUIAction {
		public StartGroupAction() {
			putValue(NAME, "Start Group");
		}
	}

	private class RebootGroupAction extends AbstractComputeManagerGUIAction {
		public RebootGroupAction() {
			putValue(NAME, "Reboot Group");
		}
	}

	private class TerminateGroupAction extends AbstractComputeManagerGUIAction {
		public TerminateGroupAction() {
			putValue(NAME, "Terminate Group");
		}
	}

	private class DownloadKeypairAction extends AbstractComputeManagerGUIAction {
		public DownloadKeypairAction() {
			putValue(NAME, "Download Keypair");
		}
	}

	private class CreateGroupKeypairAction extends
			AbstractComputeManagerGUIAction {
		public CreateGroupKeypairAction() {
			putValue(NAME, "Create Keypair");
		}
	}

	private class ShowFWRulesAction extends AbstractComputeManagerGUIAction {
		public ShowFWRulesAction() {
			putValue(NAME, "Show Rules");
		}
	}

	private class AddFWRuleAction extends AbstractComputeManagerGUIAction {
		public AddFWRuleAction() {
			putValue(NAME, "Add Rule");
		}
	}

	private class RemoveFWRuleAction extends AbstractComputeManagerGUIAction {
		public RemoveFWRuleAction() {
			putValue(NAME, "Remove Rule");
		}
	}

	private class CreateComputeAction extends AbstractComputeManagerGUIAction {
		public CreateComputeAction() {
			putValue(NAME, "Create Compute");
		}
	}

	private class CreateComputesAction extends AbstractComputeManagerGUIAction {
		public CreateComputesAction() {
			putValue(NAME, "Create Computes");
		}
	}

	private class DeleteComputesAction extends AbstractComputeManagerGUIAction {
		public DeleteComputesAction() {
			putValue(NAME, "Delete Computes");
		}
	}

	private class RenameComputeAction extends AbstractComputeManagerGUIAction {
		public RenameComputeAction() {
			putValue(NAME, "Rename Compute");
		}
	}

	private class ProvisioningComputesAction extends
			AbstractComputeManagerGUIAction {
		public ProvisioningComputesAction() {
			putValue(NAME, "Provision Computes");
		}
	}

	private class StopComputesAction extends AbstractComputeManagerGUIAction {
		public StopComputesAction() {
			putValue(NAME, "Stop Computes");
		}
	}

	private class StartComputesAction extends AbstractComputeManagerGUIAction {
		public StartComputesAction() {
			putValue(NAME, "Start Computes");
		}
	}

	// private class RebootComputesAction extends
	// AbstractComputeManagerGUIAction {
	// public RebootComputesAction() {
	// putValue(NAME, "Reboot Computes");
	// }
	// }

	private class TerminateComputesAction extends
			AbstractComputeManagerGUIAction {
		public TerminateComputesAction() {
			putValue(NAME, "Terminate Computes");
		}
	}

	private class ConnectComputeAction extends AbstractComputeManagerGUIAction {
		public ConnectComputeAction() {
			putValue(NAME, "Connect Compute");
		}
	}

	private class LuanchRServerAction extends AbstractComputeManagerGUIAction {
		public LuanchRServerAction() {
			putValue(NAME, "Luanch RStudio Server");
		}
	}

	private class OpenUserInformationAction extends
			AbstractComputeManagerGUIAction {
		public OpenUserInformationAction() {
			putValue(NAME, "Open User Information");
		}
	}

	private class AboutJMCloudComputeManagerAction extends
			AbstractComputeManagerGUIAction {
		public AboutJMCloudComputeManagerAction() {
			putValue(NAME, "About JMCloud-ComputeManager");
		}
	}

	@Resource(name = "computeManagerGUIModel")
	private ComputeManagerGUIModel computeManagerGUIModel;

	@Resource(name = "jMCloudGUIActionSelector")
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

		Separator separator_2 = new Separator();
		mnFile.add(separator_2);

		// JMenuItem mntmSetConsole = new JMenuItem("Set Console");
		// mntmSetConsole.setAction(setConsoleAction);
		// mnFile.add(mntmSetConsole);
		//
		// Separator separator_3 = new Separator();
		// mnFile.add(separator_3);

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

		// JMenuItem mntmRenameGroup = new JMenuItem("Rename Group");
		// mntmRenameGroup.setAction(renameGroupAction);
		// mnGroup.add(mntmRenameGroup);

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

		// JMenuItem mntmRebootGroup = new JMenuItem("Reboot Group");
		// mntmRebootGroup.setAction(rebootGroupAction);
		// mnGroup.add(mntmRebootGroup);

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

		// JMenuItem mntmRebootComputes = new JMenuItem("Reboot Computes");
		// mntmRebootComputes.setAction(rebootComputesAction);
		// mnCompute.add(mntmRebootComputes);

		JMenuItem mntmTerminateComputes = new JMenuItem("Terminate Computes");
		mntmTerminateComputes.setAction(terminateComputesAction);
		mnCompute.add(mntmTerminateComputes);

		Separator separator_5 = new Separator();
		mnCompute.add(separator_5);

		JMenuItem mntmConnectCompute = new JMenuItem("Connect Compute");
		mntmConnectCompute.setAction(connectComputeAction);
		mnCompute.add(mntmConnectCompute);

		JMenu mnCloudApps = new JMenu("Cloud Apps");
		mainMenuBar.add(mnCloudApps);

		JMenuItem mntmLuanchRStudioServer = new JMenuItem(
				"Luanch RStudio Server");
		mntmLuanchRStudioServer.setSelected(true);
		mntmLuanchRStudioServer.setAction(launchRStudioServer);
		mnCloudApps.add(mntmLuanchRStudioServer);

		JMenu mnHelp = new JMenu("Help");
		mainMenuBar.add(mnHelp);

		JMenuItem mntmOpenUserInformation = new JMenuItem(
				"Open User Information");
		mntmOpenUserInformation.setAction(openUserInformationAction);
		mnHelp.add(mntmOpenUserInformation);

		Separator separator_6 = new Separator();
		mnHelp.add(separator_6);

		JMenuItem mntmAboutJmcloudcomputemanager = new JMenuItem(
				"About JMCloud-ComputeManager");
		mntmAboutJmcloudcomputemanager
				.setAction(aboutJMCloudComputeManagerAction);
		mnHelp.add(mntmAboutJmcloudcomputemanager);

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
		tableViewPanel.setDoubleClickAction(connectComputeAction);
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

		// JMenuItem mntmRebootComputes = new JMenuItem("Reboot Computes");
		// mntmRebootComputes.setAction(rebootComputesAction);
		// tablePopupMenu.add(mntmRebootComputes);

		JMenuItem mntmTerminateComputes = new JMenuItem("Terminate Computes");
		mntmTerminateComputes.setAction(terminateComputesAction);
		tablePopupMenu.add(mntmTerminateComputes);

		Separator separator = new Separator();
		tablePopupMenu.add(separator);

		JMenuItem mntmConnectCompute_1 = new JMenuItem("Connect Compute");
		mntmConnectCompute_1.setAction(connectComputeAction);
		tablePopupMenu.add(mntmConnectCompute_1);
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

		// JMenuItem mntmRenameGroup = new JMenuItem("Rename Group");
		// mntmRenameGroup.setAction(renameGroupAction);
		// treePopupMenu.add(mntmRenameGroup);

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

		// JMenuItem mntmRebootGroup = new JMenuItem("Reboot Group");
		// mntmRebootGroup.setAction(rebootGroupAction);
		// treePopupMenu.add(mntmRebootGroup);

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
