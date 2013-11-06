package com.jmcloud.compute.gui.action;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.jmcloud.compute.gui.component.DialogsUtil;
import com.jmcloud.compute.gui.component.ProgressSpinnerJDialog;
import com.jmcloud.compute.gui.model.ComputeManagerGUIModel;
import com.jmcloud.compute.util.SysUtils;

public abstract class AbstractJMCloudGUIAction implements JMCloudGUIAction {
	@Resource(name = "computeManagerGUIModel")
	protected ComputeManagerGUIModel computeManagerGUIModel;

	@Resource(name = "computeManagerGUI")
	protected JFrame mainFrame;
	
	protected final String START_SIGNATURE = "\t[START]";
	protected final String FAILURE_SIGNATURE = "\t[FAILURE]";
	protected final String SUCCESS_SIGNATURE = "\t[SUCCESS]";
	protected final String END_SIGNATURE = "\t[END]";

	protected final String PROGRESS_FAILURE_SIGNATURE = " Failure\n";
	protected final String PROGRESS_SUCCESS_SIGNATURE = " Success\n";
	protected final String CANCEL_SIGNATURE = "Cancel!!!";
	protected final String NO_INPUT_SIGNATURE = "No Group Name!!!";

	protected String selectionGroup;
	protected String regionOfselectionGroup;
	protected TreePath selectionRegionTreePath;
	protected TreePath selectionGroupTreePath;
	protected List<TreePath> selectionRegionTreePaths;
	protected List<TreePath> selectionGroupTreePaths;

	protected String actionCommand;
	protected ActionEvent actionEvent;

	protected String resultHeader;
	protected String endHeader;

	protected void initAction(ActionEvent e) {
		this.actionEvent = e;
		this.actionCommand = e.getActionCommand();

		this.resultHeader = "[" + this.actionCommand + "]";
		this.endHeader = this.resultHeader + END_SIGNATURE;
		this.selectionRegionTreePath = computeManagerGUIModel
				.getSelectionRegionTreePath();
		this.selectionGroupTreePath = computeManagerGUIModel
				.getSelectionGroupTreePath();
		this.selectionRegionTreePaths = computeManagerGUIModel
				.getSelectionRegionTreePaths();
		this.selectionGroupTreePaths = computeManagerGUIModel
				.getSelectionGroupTreePaths();
		this.selectionGroup = getTreeNodeName(selectionGroupTreePath);
		if (selectionGroupTreePath != null) {
			this.regionOfselectionGroup = getTreeNodeName(selectionGroupTreePath
					.getParentPath());
		}
	}
	
	protected void showLineOnInfoView(String resultString) {
		computeManagerGUIModel.showLineOnInfoView(resultString);
	}

	protected String showInputDialog(String message) {
		return JOptionPane.showInputDialog(mainFrame, message, actionCommand,
				JOptionPane.QUESTION_MESSAGE);
	}

	protected int showComfirmDialog(String message) {
		return JOptionPane.showConfirmDialog(mainFrame, message, actionCommand,
				JOptionPane.WARNING_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
	}

	protected String returnErrorMessage(String errorMessage) {
		return FAILURE_SIGNATURE + "\t" + errorMessage;
	}

	protected String returnSuccessMessage(String successMessage) {
		return SUCCESS_SIGNATURE + "\t" + successMessage;
	}

	private String returnEndMessage(String message) {
		return endHeader + "\t" + message;
	}

	protected boolean isSelectedOnlyRegion() {
		return (selectionGroupTreePaths == null || selectionGroupTreePaths
				.size() == 0)
				&& selectionRegionTreePaths != null
				&& selectionRegionTreePaths.size() == 1;
	}

	protected boolean isSelectedOnlyGroup() {
		return (selectionRegionTreePaths == null || selectionRegionTreePaths
				.size() == 0)
				&& selectionGroupTreePaths != null
				&& selectionGroupTreePaths.size() == 1;
	}

	protected String getTreeNodeName(TreePath targetPath) {
		if (targetPath == null) {
			return "";
		}
		return ((DefaultMutableTreeNode) targetPath.getLastPathComponent())
				.getUserObject().toString();
	}

	protected boolean doProgressMethod(boolean method) {
		if (method) {
			computeManagerGUIModel
					.showProgressResult(PROGRESS_SUCCESS_SIGNATURE);
		} else {
			computeManagerGUIModel
					.showProgressResult(PROGRESS_FAILURE_SIGNATURE);
		}
		return method;
	}

	private ProgressSpinnerRunnable progressSpinnerRunnable = new ProgressSpinnerRunnable();

	protected void startProgressSpinner() {
		SwingUtilities.invokeLater(progressSpinnerRunnable);
		while (!progressSpinnerRunnable.isShowing()) {
			SysUtils.sleep(1);
		}
	}

	protected void stopProgressSpinner() {
		SysUtils.sleep(1);
		progressSpinnerRunnable.stop();
	}

	@Override
	public void doAction(final ActionEvent e) {
		Thread doActionThread = new Thread(new Runnable() {
			@Override
			public void run() {
				initAction(e);
				computeManagerGUIModel.showLineOnInfoView(resultHeader
						+ START_SIGNATURE);
				try {
					computeManagerGUIModel
							.showLineOnInfoView(returnEndMessage(doAbstractAction(e)));
				} finally {
					if (progressSpinnerRunnable != null
							&& progressSpinnerRunnable.isShowing()) {
						stopProgressSpinner();
					}
				}
			}
		});
		doActionThread.start();
	}

	abstract protected String doAbstractAction(ActionEvent e);

	class ProgressSpinnerRunnable implements Runnable {
		private JDialog progressSpinnerJdialog = new ProgressSpinnerJDialog(mainFrame);

		@Override
		public void run() {
			progressSpinnerJdialog.setLocationRelativeTo(mainFrame);
			progressSpinnerJdialog.setVisible(true);
		}

		public void stop() {
			progressSpinnerJdialog.setVisible(false);
		}

		public boolean isShowing() {
			return progressSpinnerJdialog.isShowing();
		}
	}

}
