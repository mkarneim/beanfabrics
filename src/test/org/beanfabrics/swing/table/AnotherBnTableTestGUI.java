/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.table;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.DatePM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Operation;
import org.beanfabrics.swing.BnButton;

import quicktime.sound.SICompletion;

public class AnotherBnTableTestGUI extends JFrame implements View<AnotherBnTableTestGUI.TableModel>, ModelSubscriber {
	public static void main(String[] args) throws Exception {
		System.out.println( System.getProperty("java.version"));

		//UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName());

		final TableModel model = new TableModel();
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				AnotherBnTableTestGUI frame = new AnotherBnTableTestGUI();
				frame.setPresentationModel(model);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(400, 400);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});

	}
	private final Link link = new Link(this);
	private JScrollPane scrollPane;
	private BnTable bnTable;
	private JPanel panel_1;
	private JScrollPane scrollPane_2;
	private BnTable bnTable_2;
	private JPanel panel_2;
	private JSplitPane splitPane;
	private JPanel headerPanel;
	private BnButton sortButton;
	private JPanel panel;
	private ModelProvider localProvider;

	public AnotherBnTableTestGUI() {
		super();
		setLayout( new BorderLayout());
		getContentPane().add(getPanel(), BorderLayout.CENTER);
		//
	}

	protected ModelProvider getLocalProvider() {
		if (localProvider == null) {
			localProvider = new ModelProvider(); // @wb:location=11,442
			localProvider.setPresentationModelType(TableModel.class);
			//localProvider.setModel( new TableModel());
		}
		return localProvider;
	}

	/** {@inheritDoc} */
	public TableModel getPresentationModel() {
		return getLocalProvider().getPresentationModel();
	}

	/** {@inheritDoc} */
	public void setPresentationModel(TableModel pModel) {
		getLocalProvider().setPresentationModel(pModel);
	}

	/** {@inheritDoc} */
	public IModelProvider getModelProvider() {
		return link.getModelProvider();
	}

	/** {@inheritDoc} */
	public void setModelProvider(IModelProvider provider) {
		this.link.setModelProvider(provider);
	}

	/** {@inheritDoc} */
	public Path getPath() {
		return link.getPath();
	}

	/** {@inheritDoc} */
	public void setPath(Path path) {
		this.link.setPath(path);
	}

	private static class RowModel extends AbstractPM {
		TextPM colA = new TextPM();
		TextPM colB = new TextPM();
		IntegerPM colC = new IntegerPM();
		DatePM colD = new DatePM();
		OperationPM setCurrentDate = new OperationPM();


		public RowModel() {
			PMManager.setup(this);
			colA.setEditable(false);
		}
		@Operation
		void setCurrentDate() {
			colD.setDate( new Date());
		}

	}

	private static class TableModel extends AbstractPM {
		ListPM<RowModel> elements = new ListPM<RowModel>();
		OperationPM sortByColA = new OperationPM();

		public TableModel() {
			PMManager.setup(this);
			populate();
		}

		public void populate() {
			int rows = 20;
			for( int i=0; i<rows; ++i) {
				RowModel pModel = new RowModel();
				pModel.colA.setText("A "+i);
				pModel.colB.setText("B "+i);
				pModel.colC.setInteger(i);
				pModel.colD.setText("");
				elements.add(pModel);
			}
		}
		@Operation
		void sortByColA() {
			elements.sortBy( true, new Path("colA"));
		}
		@OnChange(path="elements")
		void updateSelection() {
			RowModel row = elements.getSelection().getFirst();
			if ( row != null) {
				int len = row.colA.getText().length();
				for( RowModel row2: elements) {
					int len2 = row2.colA.getText().length();
					if ( len == len2) {
						elements.getSelection().add(row2);
					} else {
						elements.getSelection().remove(row2);
					}
				}
			}
		}
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.add(getSplitPane(), BorderLayout.CENTER);
			panel.add(getHeaderPanel(), BorderLayout.NORTH);
		}
		return panel;
	}

	private JPanel getHeaderPanel() {
		if (headerPanel == null) {
			headerPanel = new JPanel();
			headerPanel.setLayout(new FlowLayout());
			headerPanel.add(getSortButton());
		}
		return headerPanel;
	}

	private BnButton getSortButton() {
		if (sortButton == null) {
			sortButton = new BnButton();
			sortButton.setModelProvider( getLocalProvider());
			sortButton.setPath( new Path("sortByColA"));
			sortButton.setText("Sort");
		}
		return sortButton;
	}

	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane();
			splitPane.setLeftComponent(getPanel_2());
			splitPane.setRightComponent(getPanel_1());
			splitPane.setDividerLocation(200);
		}
		return splitPane;
	}

	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.setLayout(new BorderLayout());
			panel_1.add(getScrollPane());
		}
		return panel_1;
	}

	private BnTable getBnTable() {
		if (bnTable == null) {
			bnTable = new BnTable();
			bnTable.setPath(new org.beanfabrics.Path("this.elements"));
			bnTable.setColumns(new org.beanfabrics.swing.table.BnColumn[] {
	       new org.beanfabrics.swing.table.BnColumn( new org.beanfabrics.Path("this.colA"), "ColA", 40, true    )
	       , new org.beanfabrics.swing.table.BnColumn( new org.beanfabrics.Path("this.colB"), "ColB", 100, false    )
	       , new org.beanfabrics.swing.table.BnColumn( new org.beanfabrics.Path("this.colC"), "ColC", 100, false    )
	       , new org.beanfabrics.swing.table.BnColumn( new org.beanfabrics.Path("this.colD"), "ColD", 100, false, new Path("this.setCurrentDate")    )});
			bnTable.setModelProvider(getLocalProvider());
		}
		return bnTable;
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getBnTable());
		}
		return scrollPane;
	}

	private JPanel getPanel_2() {
		if (panel_2 == null) {
			panel_2 = new JPanel();
			panel_2.setLayout(new BorderLayout());
			panel_2.add(getScrollPane_2());
		}
		return panel_2;
	}

	private BnTable getBnTable_2() {
		if (bnTable_2 == null) {
			bnTable_2 = new BnTable();
			bnTable_2.setPath(new org.beanfabrics.Path("this.elements"));
			bnTable_2.setColumns(new org.beanfabrics.swing.table.BnColumn[] {
	       new org.beanfabrics.swing.table.BnColumn( new org.beanfabrics.Path("this.colA"), "ColA", 40, true    )
	       , new org.beanfabrics.swing.table.BnColumn( new org.beanfabrics.Path("this.colB"), "ColB", 100, false    )
	       , new org.beanfabrics.swing.table.BnColumn( new org.beanfabrics.Path("this.colC"), "ColC", 100, false    )
	       , new org.beanfabrics.swing.table.BnColumn( new org.beanfabrics.Path("this.colD"), "ColD", 100, false, new Path("this.setCurrentDate")    )});
			bnTable_2.setModelProvider(getLocalProvider());
		}
		return bnTable_2;
	}

	private JScrollPane getScrollPane_2() {
		if (scrollPane_2 == null) {
			scrollPane_2 = new JScrollPane();
			scrollPane_2.setViewportView(getBnTable_2());
		}
		return scrollPane_2;
	}
}