package com.jmcloud.compute.gui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.jmcloud.compute.vo.ComputeGroupVO;
import com.jmcloud.compute.vo.ComputeVO;

public class TableViewPanelModel extends AbstractTableModel {

	public static final int STATUS_INDEX = 0;
	public static final int NAME_INDEX = 1;
	public static final int GROUP_INDEX = 2;
	public static final int PUBLIC_IP_INDEX = 3;
	public static final int PRIVATE_IP_INDEX = 4;
	public static final int SECURITY_GROUP_INDEX = 5;
	public static final int KEYPAIR_INDEX = 6;
	public static final int REGION_INDEX = 7;
	public static final int COMPUTE_ID_INDEX = 8;
	public static final int IMAGE_ID_INDEX = 9;
	public static final int COMPUTE_TYPE_INDEX = 10;
	public static final int TIME_INDEX = 11;

	private String[] columnNames = { "Status", "Name", "Group", "Public IP",
			"Private IP", "Security Group", "Keypair", "Region", "Compute ID",
			"Image ID", "Compute Type", "Time" };

	volatile private List<String[]> dataList = new ArrayList<String[]>();

	public void addRows(ComputeGroupVO cgvo) {
		Collection<ComputeVO> computes = cgvo.getComputesList().values();
		if (computes == null || computes.size() == 0) {
			return;
		}
		for (ComputeVO cvo : computes) {
			String[] row = { cvo.getStatus(), cvo.getComputeName(),
					cvo.getComputeGroupName(), cvo.getPublicIP(),
					cvo.getPrivateIP(), cvo.getSecurityGroup(),
					cvo.getKeypairName(), cvo.getRegion(), cvo.getComputeID(),
					cvo.getImageID(), cvo.getComputeType(),
					cvo.getComputeLaunchTime() };
			dataList.add(row);
		}
		fireTableDataChanged();
	}

	public void clearTable() {
		dataList = new ArrayList<String[]>();
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public int getRowCount() {
		return dataList.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (dataList.size() == 0) {
			return "";
		}
		return dataList.get(rowIndex)[columnIndex];
	}

}
