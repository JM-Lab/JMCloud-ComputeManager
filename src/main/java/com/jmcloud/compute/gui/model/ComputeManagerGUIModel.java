package com.jmcloud.compute.gui.model;

import static com.jmcloud.compute.aws.ec2.manager.EC2Manager.NO_PROVISION_STATUS;
import static com.jmcloud.compute.aws.ec2.manager.EC2Manager.RUNNING_STATUS;
import static com.jmcloud.compute.aws.ec2.manager.EC2Manager.STOPPED_STATUS;
import static com.jmcloud.compute.aws.ec2.manager.EC2Manager.TERMINATED_STATUS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.JMCloudComputeManager;
import com.jmcloud.compute.aws.ec2.sys.EC2EnviromentVO;
import com.jmcloud.compute.util.SysUtils;
import com.jmcloud.compute.vo.ComputeGroupVO;
import com.jmcloud.compute.vo.ComputeVO;

@Service("computeManagerGUIModel")
public class ComputeManagerGUIModel {

	@Resource(name = "jMCloudEC2Manager")
	private JMCloudComputeManager jMCloudComputeManager;

	@Resource(name = "eC2EnviromentVO")
	private EC2EnviromentVO eC2EnviromentVO;

	private JTree tree;

	private JTable table;

	private DefaultTreeModel treeModel;

	private TableViewPanelModel jMCloudTableModel;

	private JTextArea jMCloudComputeInfoTextArea;

	public void initModel(JTree tree, JTable table,
			JTextArea jMCloudComputeInfoTextArea) {
		this.tree = tree;
		this.table = table;
		this.jMCloudComputeInfoTextArea = jMCloudComputeInfoTextArea;
		this.treeModel = (DefaultTreeModel) tree.getModel();
		this.jMCloudTableModel = (TableViewPanelModel) table.getModel();
		jMCloudComputeManager.addRegions(eC2EnviromentVO.getDefaultRegions()
				.split(" "));
		jMCloudComputeManager.loadComputeGroupFromLocal();
		updateTree();
	}

	synchronized public void updateTree() {
		int[] selectedRows = tree.getSelectionRows();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel
				.getRoot();
		root.removeAllChildren();
		treeModel.reload(root);
		for (String region : jMCloudComputeManager.getRegions()) {
			DefaultMutableTreeNode regionNode = new DefaultMutableTreeNode(
					region, true);
			treeModel.insertNodeInto(regionNode, root, root.getChildCount());
			for (ComputeGroupVO computeGroupVO : jMCloudComputeManager
					.getAllComputeGroups()) {
				if (region.equals(computeGroupVO.getRegion())) {
					DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(
							computeGroupVO.getComputeGroupName(), false);
					treeModel.insertNodeInto(groupNode, regionNode,
							regionNode.getChildCount());
				}
			}
			tree.expandRow(tree.getRowCount() - 1);
		}
		if (selectedRows != null && selectedRows.length > 0) {
			for (int selectedRow : selectedRows) {
				tree.setSelectionRow(selectedRow);
			}
		}
	}

	synchronized public void seletionTreeNode(TreeSelectionEvent e) {
		updateTableInfo();
		TreePath selectionTreePath = e.getPath();
		if (getTreeNode(selectionTreePath).getAllowsChildren()) {
			tree.expandPath(selectionTreePath);
		}
	}

	synchronized public void updateTableInfo() {
		jMCloudTableModel.clearTable();
		for (ComputeGroupVO selectionComputeGroupVO : getSelectionComputeGroupVOs()) {
			jMCloudTableModel.addRows(selectionComputeGroupVO);
		}
	}

	private List<ComputeGroupVO> getSelectionComputeGroupVOs() {
		List<ComputeGroupVO> selectionComputeGroupVOsList = new ArrayList<ComputeGroupVO>();
		for (TreePath selectionPath : getSelectionGroupTreePaths()) {
			selectionComputeGroupVOsList.add(jMCloudComputeManager
					.getComputeGroup(
							getTreeNodeName(selectionPath.getParentPath()),
							getTreeNodeName(selectionPath)));
		}
		return selectionComputeGroupVOsList;
	}

	private DefaultMutableTreeNode getTreeNode(TreePath treePath) {
		return (DefaultMutableTreeNode) treePath.getLastPathComponent();
	}

	private String getTreeNodeName(TreePath targetPath) {
		if (targetPath == null) {
			return "";
		}
		return getTreeNode(targetPath).getUserObject().toString();
	}

	synchronized public void showProgressResult(String progress) {
		jMCloudComputeInfoTextArea.append(progress);
		jMCloudComputeInfoTextArea.setCaretPosition(jMCloudComputeInfoTextArea
				.getText().length());
	}

	synchronized public void showLineOnInfoView(String result) {
		jMCloudComputeInfoTextArea.insert(result + "\n",
				jMCloudComputeInfoTextArea.getText().length());
		jMCloudComputeInfoTextArea.setCaretPosition(jMCloudComputeInfoTextArea
				.getText().length());
	}

	public TreePath getSelectionGroupTreePath() {
		TreePath selectionTreePath = tree.getSelectionPath();
		return (selectionTreePath != null && !changeIntoNode(selectionTreePath)
				.getAllowsChildren()) ? selectionTreePath : null;
	}

	public TreePath getSelectionRegionTreePath() {
		TreePath selectionTreePath = tree.getSelectionPath();
		return (selectionTreePath != null && changeIntoNode(selectionTreePath)
				.getAllowsChildren()) ? selectionTreePath : null;
	}

	private DefaultMutableTreeNode changeIntoNode(TreePath selectionTreePath) {
		return (DefaultMutableTreeNode) selectionTreePath
				.getLastPathComponent();
	}

	public List<TreePath> getSelectionRegionTreePaths() {
		TreePath[] selectionPaths = tree.getSelectionPaths();
		List<TreePath> selectionRegionTreePathsList = new ArrayList<TreePath>();
		if (!(selectionPaths == null || selectionPaths.length == 0)) {

			for (TreePath selectionPath : selectionPaths) {
				if (getTreeNode(selectionPath).getAllowsChildren()) {
					selectionRegionTreePathsList.add(selectionPath);
				}
			}
		}
		return selectionRegionTreePathsList;
	}

	public List<TreePath> getSelectionGroupTreePaths() {
		TreePath[] selectionPaths = tree.getSelectionPaths();
		List<TreePath> selectionGroupTreePathsList = new ArrayList<TreePath>();
		if (!(selectionPaths == null || selectionPaths.length == 0)) {
			for (TreePath selectionPath : selectionPaths) {
				if (!getTreeNode(selectionPath).getAllowsChildren()) {
					selectionGroupTreePathsList.add(selectionPath);
				}
			}
		}
		return selectionGroupTreePathsList;
	}

	public boolean createGroup(String newGroupName) {
		DefaultMutableTreeNode parent = changeIntoNode(getSelectionRegionTreePath());
		return parent != null
				&& jMCloudComputeManager.createGroup(
						changeIntoNodeName(parent), newGroupName);
	}

	public boolean createSecurityGroup(String region, String groupName) {
		ComputeGroupVO computeGroupVO = jMCloudComputeManager.getComputeGroup(
				region, groupName);
		return jMCloudComputeManager.createSecurityGroup(
				computeGroupVO.getRegion(), computeGroupVO.getSecurityGroup());
	}

	public boolean createKeypair(String region, String groupName) {
		ComputeGroupVO computeGroupVO = jMCloudComputeManager.getComputeGroup(
				region, groupName);
		return jMCloudComputeManager.createKeypair(computeGroupVO.getRegion(),
				computeGroupVO.getGroupKeyPair());
	}

	private String changeIntoNodeName(DefaultMutableTreeNode treeNode) {
		return treeNode.getUserObject().toString();
	}

	public boolean renameGroup(String region, String targetGroupName,
			String newGroupName) {
		if (!jMCloudComputeManager.renameGroup(region, targetGroupName,
				newGroupName)) {
			return false;
		}
		treeModel
				.valueForPathChanged(getSelectionGroupTreePath(), newGroupName);
		return true;
	}

	public boolean deleteGroup(String region, String groupName) {
		if (!jMCloudComputeManager.deleteGroup(region, groupName)) {
			return false;
		}
		for (TreePath treePath : getSelectionGroupTreePaths()) {
			if (groupName.equals(getTreeNodeName(treePath))
					&& region.equals(getTreeNodeName(treePath.getParentPath()))) {
				treeModel.removeNodeFromParent(changeIntoNode(treePath));
				return true;
			}
		}
		return false;
	}

	public int getSelectionRow() {
		return table.getSelectedRow();
	}

	public int[] getSelectionRows() {
		return table.getSelectedRows();
	}

	public boolean createCompute(String region, String computeGroupName,
			String computeName, String imageID, String computeType) {
		return jMCloudComputeManager.createCompute(region, computeGroupName,
				computeName, imageID, computeType);
	}

	public boolean createComputes(String region, String computeGroupName,
			String computeName, String imageID, int numOfComputes,
			String computeType) {
		return jMCloudComputeManager.createComputes(region, computeGroupName,
				computeName, imageID, numOfComputes, computeType);
	}

	public String getComputeInfo(int row, int statusIndex) {
		if (table.getRowCount() > row) {
			return (String) table.getValueAt(row, statusIndex);
		}
		return "";
	}

	public void updateComputesStatusAsync(final int initSec,
			final List<ComputeVO> checkComputeList, final String expectedStatus) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int timeout = 600;
				int interval = 5;
				SysUtils.sleep(initSec);
				timeout -= initSec;
				List<ComputeVO> copyCheckComputeList = new ArrayList<>();
				copyCheckComputeList.addAll(checkComputeList);
				while (timeout > 0 && copyCheckComputeList.size() > 0) {
					for (ComputeVO computeVO : copyCheckComputeList
							.toArray(new ComputeVO[copyCheckComputeList.size()])) {
						ComputeVO realComputeVO = jMCloudComputeManager
								.updateComputeStatus(computeVO.getRegion(),
										computeVO.getComputeGroupName(),
										computeVO.getComputeID());
						if (realComputeVO == null) {
							return;
						}
						if (expectedStatus.equals(realComputeVO.getStatus())) {
							copyCheckComputeList.remove(computeVO);
							updateTableInfo();
							showProgressResult("[Update Compute Information] "
									+ realComputeVO.getComputeName() + "\n");
						}
					}
					SysUtils.sleep(interval);
					timeout -= interval;
					interval += interval;
				}
			}
		}).start();
	}

	public void updateComputesStatus(String region, String groupName,
			String computeID) {
		jMCloudComputeManager.updateComputeStatus(region, groupName, computeID);
		updateTableInfo();
	}

	public boolean deleteCompute(String region, String groupName,
			String computeID) {
		return jMCloudComputeManager
				.deleteCompute(region, groupName, computeID);
	}

	public boolean provisionCompute(String region, String groupName,
			String computeID) {
		return jMCloudComputeManager.provisionCompute(region, groupName,
				computeID);
	}

	public boolean stopCompute(String region, String groupName, String computeID) {
		return jMCloudComputeManager.stopCompute(region, groupName, computeID);
	}

	public boolean startCompute(String region, String groupName,
			String computeID) {
		return jMCloudComputeManager.startCompute(region, groupName, computeID);
	}

	public boolean rebootCompute(String region, String groupName,
			String computeID) {
		return jMCloudComputeManager
				.rebootCompute(region, groupName, computeID);
	}

	public boolean terminateCompute(String region, String groupName,
			String computeID) {
		return jMCloudComputeManager.terminateCompute(region, groupName,
				computeID);
	}

	public boolean renameCompute(String region, String groupName,
			String computeID, String newComputeName) {
		return jMCloudComputeManager.renameCompute(region, groupName,
				computeID, newComputeName);
	}

	public boolean deleteSecurityGroup(String region, String groupName) {
		return jMCloudComputeManager.deleteSecurityGroup(region, groupName);
	}

	public boolean deleteKeypair(String region, String groupName) {
		return jMCloudComputeManager.deleteKeypair(region, groupName);
	}

	public boolean isGroupTerminated(String region, String groupName) {
		for (String status : jMCloudComputeManager.getGroupStatus(region,
				groupName)) {
			if (TERMINATED_STATUS.equals(status)
					|| NO_PROVISION_STATUS.equals(status)) {
				continue;
			}
			return false;
		}
		return true;
	}

	private boolean updateGroupActionStatus(List<ComputeVO> computesList,
			String expectedStatus, int initSec) {
		if (computesList.size() == 0) {
			return false;
		}
		updateComputesStatusAsync(initSec, computesList, expectedStatus);
		return true;
	}

	private List<ComputeVO> getTargetComputes(String region, String groupName,
			String targetStatus) {
		ComputeVO[] computes = jMCloudComputeManager.getComputesOfGroup(region,
				groupName);
		List<ComputeVO> computesList = new ArrayList<ComputeVO>();
		if (computes == null || computes.length == 0) {
			return computesList;
		}
		for (ComputeVO computeVO : computes) {
			if (targetStatus.equals(computeVO.getStatus())) {
				computesList.add(computeVO);
			}
		}
		return computesList;
	}

	public boolean provisionGroup(String region, String groupName) {
		List<ComputeVO> targetComputesList = getTargetComputes(region,
				groupName, NO_PROVISION_STATUS);
		if (targetComputesList.size() > 0) {
			ComputeVO[] targetComputeVOs = jMCloudComputeManager
					.provisionGroup(region, groupName);
			if (targetComputeVOs != null && targetComputeVOs.length > 0) {
				updateGroupActionStatus(Arrays.asList(targetComputeVOs),
						RUNNING_STATUS, 30);
			}
			return true;
		}
		return false;
	}

	public boolean stopGroup(String region, String groupName) {
		List<ComputeVO> targetComputesList = getTargetComputes(region,
				groupName, RUNNING_STATUS);
		if (targetComputesList.size() > 0
				&& jMCloudComputeManager.stopGroup(region, groupName)) {
			updateGroupActionStatus(targetComputesList, STOPPED_STATUS, 120);
			return true;
		}
		return false;
	}

	public boolean startGroup(String region, String groupName) {
		List<ComputeVO> targetComputesList = getTargetComputes(region,
				groupName, STOPPED_STATUS);
		if (targetComputesList.size() > 0
				&& jMCloudComputeManager.startGroup(region, groupName)) {
			updateGroupActionStatus(targetComputesList, RUNNING_STATUS, 30);
			return true;
		}
		return false;
	}

	public boolean rebootGroup(String region, String groupName) {
		List<ComputeVO> targetComputesList = getTargetComputes(region,
				groupName, RUNNING_STATUS);
		if (targetComputesList.size() > 0
				&& jMCloudComputeManager.rebootGroup(region, groupName)) {
			updateGroupActionStatus(targetComputesList, RUNNING_STATUS, 120);
			return true;
		}
		return false;
	}

	public boolean terminateGroup(String region, String groupName) {
		return jMCloudComputeManager.terminateGroup(region, groupName);
	}

	public String getSecurityGroup(String region, String groupName) {
		return jMCloudComputeManager.getComputeGroup(region, groupName)
				.getSecurityGroup();
	}

	public String getGroupKeypair(String region, String groupName) {
		String groupKeypair = jMCloudComputeManager.getComputeGroup(region,
				groupName).getGroupKeyPair();
		return groupKeypair == null ? "" : groupKeypair;
	}

	public String[] getGroups(String region) {
		List<String> groupNameList = new ArrayList<String>();
		for (ComputeGroupVO computeGroupVO : jMCloudComputeManager
				.getAllComputeGroups()) {
			if (region.equals(computeGroupVO.getRegion())) {
				groupNameList.add(computeGroupVO.getComputeGroupName());
			}
		}
		return groupNameList.toArray(new String[groupNameList.size()]);
	}

	public int getTableRowCount() {
		return table.getRowCount();
	}

	public boolean saveGroup(String region, String groupName) {
		return jMCloudComputeManager.saveComputeGroup(region, groupName);
	}

	public boolean loadRegion(String region) {
		return jMCloudComputeManager.loadRegionFromCloud(region);
	}

	public String getSecurityGroupInfo(String region, String groupName) {
		return jMCloudComputeManager.getFWRules(region, groupName);
	}

	public boolean addSecurityRule(String region, String groupName,
			String tcpOrUdpOrIcmp, String portRange, String cidrRange) {
		return jMCloudComputeManager.addFWRule(region, groupName,
				tcpOrUdpOrIcmp, portRange, cidrRange);
	}

	public boolean removeSecurityRule(String region, String groupName,
			String tcpOrUdpOrIcmp, String portRange, String cidrRange) {
		return jMCloudComputeManager.removeFWRule(region, groupName,
				tcpOrUdpOrIcmp, portRange, cidrRange);
	}
}
