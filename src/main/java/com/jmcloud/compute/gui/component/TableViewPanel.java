package com.jmcloud.compute.gui.component;

import static com.jmcloud.compute.gui.model.TableViewPanelModel.COMPUTE_ID_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.COMPUTE_TYPE_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.GROUP_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.KEYPAIR_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.NAME_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.REGION_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.SECURITY_GROUP_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.STATUS_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.TIME_INDEX;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.jmcloud.compute.aws.ec2.manager.EC2Manager;
import com.jmcloud.compute.gui.model.TableViewPanelModel;
import com.jmcloud.compute.sys.SystemImages;

public class TableViewPanel extends JPanel {

	private JTable table;
	private Action doubleClickAction;

	public TableViewPanel() {
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setBackground(Color.WHITE);

		add(scrollPane);

		table = new JTable();
		table.setModel(new TableViewPanelModel());
		table.setCellSelectionEnabled(true);
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setAutoCreateRowSorter(true);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
				table.getModel());
		table.setRowSorter(sorter);
		scrollPane.setViewportView(table);
		JLabel lblNewLabel = new JLabel("Compute Infomation");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 8));
		add(lblNewLabel, BorderLayout.NORTH);

		// DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table
		// .getTableHeader().getDefaultRenderer();
		// renderer.setHorizontalAlignment(JLabel.CENTER);
		setColumeSize();
		setColumnCellRenderer(STATUS_INDEX, new StatusCellRenderer());
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() != 2) {
					return;
				}
				doubleClickAction.actionPerformed(new ActionEvent(e, e.getID(), "Connect Compute"));
				
//				if(EC2Manager.RUNNING_STATUS.equals(table.getValueAt(row, STATUS_INDEX).toString())){	
//					new RunFileOnWindows().run(SystemEnviroment.getConsoleFilePath());
//					String pasteCommand = "ssh -i " + SystemEnviroment.getKeypairDir()+table.getValueAt(row, KEYPAIR_INDEX) + " " + table.getValueAt(row, PUBLIC_IP_INDEX) + " -l ubuntu";
//					pasteCommand = pasteCommand.replace("\\", "\\\\");
//					pasteCommand = pasteCommand.replace("/", "\\\\");
//					pasteCommand = "cmd /c start cmd /c "+ pasteCommand;
//					try {
//						new ProcessBuilder(pasteCommand.split(" ")).start();
//						
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}

//					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//					StringSelection stringSelection = new StringSelection(pasteCommand);
//	                clipboard.setContents(stringSelection, stringSelection);

					
//					new RunCLISimple().run("cmd.exe /c start \"" + SystemEnviroment.getConsoleFilePath() + "\"");
//					new RunCLISimple().run("cmd.exe /c \"" + SystemEnviroment.getConsoleFilePath() + "\"");
//					try {
//						System.out.println("run : " + SystemEnviroment.getConsoleFilePath());
//						Process process = new ProcessBuilder(command4.split(" ")).start();
//						Runtime.getRuntime().exec("cmd.exe /c "+SystemEnviroment.getConsoleFilePath());
//						Runtime.getRuntime().exec(SystemEnviroment.getConsoleFilePath());
//					} catch (IOException ioe) {
//						// TODO Auto-generated catch block
//						ioe.printStackTrace();
//					}
//				}
			}
		});
	}

	private void setColumnCellRenderer(int col, TableCellRenderer renderer) {
		table.getColumnModel().getColumn(col).setCellRenderer(renderer);
	}

	private void setColumeSize() {
		TableModel tableModel = table.getModel();
		TableColumnModel tableColumnModel = table.getColumnModel();
		TableColumn column;
		for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {
			column = tableColumnModel.getColumn(i);
			switch (i) {
			case NAME_INDEX:
				column.setPreferredWidth(100);
				break;
			case GROUP_INDEX:
				column.setPreferredWidth(100);
				break;
			case SECURITY_GROUP_INDEX:
				column.setPreferredWidth(100);
				break;
			case KEYPAIR_INDEX:
				column.setPreferredWidth(100);
				break;
			case REGION_INDEX:
				column.setPreferredWidth(90);
				break;
			case COMPUTE_ID_INDEX:
				column.setPreferredWidth(80);
				break;
			case COMPUTE_TYPE_INDEX:
				column.setPreferredWidth(100);
				break;
			case TIME_INDEX:
				column.setPreferredWidth(150);
				break;
			default:
				column.setPreferredWidth(tableModel.getColumnName(i).length() * 10);
			}

		}
	}

	private class StatusCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (EC2Manager.PENDING_STATUS.equals(value)) {
				ImageIcon animatedImage = SystemImages.getSpinnerImage();
				animatedImage.setImageObserver(new CellImageObserver(table,
						row, column));
				JLabel spinnerJLabel = new JLabel(animatedImage);
				spinnerJLabel.setToolTipText(value.toString());
				return spinnerJLabel;
			}
			return super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
		}
	}

	class CellImageObserver implements ImageObserver {
		JTable table;
		int row;
		int col;

		CellImageObserver(JTable table, int row, int col) {
			this.table = table;
			this.row = row;
			this.col = col;
		}

		@Override
		public boolean imageUpdate(Image img, int flags, int x, int y, int w,
				int h) {
			if ((flags & (FRAMEBITS | ALLBITS)) != 0) {
				Rectangle rect = table.getCellRect(row, col, false);
				table.repaint(rect);
			}
			return (flags & (ALLBITS | ABORT)) == 0;
		}
	}

	public JTable getTable() {
		return table;
	}

	public void setDoubleClickAction(Action connectComputeAction) {
		doubleClickAction = connectComputeAction;
	}
}
