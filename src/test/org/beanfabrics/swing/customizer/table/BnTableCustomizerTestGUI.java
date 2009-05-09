/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.swing.customizer.table;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.swing.table.BnTable;

/**
 * @author Michael Karneim
 */
public class BnTableCustomizerTestGUI extends JFrame {
	
	private static class FilePM extends AbstractPM {
		TextPM name = new TextPM();
		IntegerPM size = new IntegerPM();
		AccessRightsPM userRights = new AccessRightsPM();
		AccessRightsPM groupRights = new AccessRightsPM();
		AccessRightsPM otherRights = new AccessRightsPM();
		
		public FilePM() {
			PMManager.setup(this);
		}
	}
	private static class AccessRightsPM extends AbstractPM {
		BooleanPM read = new BooleanPM();
		BooleanPM write = new BooleanPM();
		BooleanPM execute = new BooleanPM();
		
		public AccessRightsPM() {
			PMManager.setup(this);
		}
	}
	private static class ListOfFilesPM extends ListPM<FilePM> {
		
	}
	private static class BrowserPM extends AbstractPM {
		ListOfFilesPM files = new ListOfFilesPM();
		ListPM<TextPM> lastSearchPatterns = new ListPM<TextPM>();
		TextPM searchPattern = new TextPM();
		
		public BrowserPM() {
			PMManager.setup(this);
		}
	}
	
	
	private JButton closeButton;
	private JPanel panel;
	private BnTableCustomizer bnTableCustomizer;

	public BnTableCustomizerTestGUI() {
		super();
		getContentPane().add(getBnTableCustomizer(), BorderLayout.CENTER);
		getContentPane().add(getPanel(), BorderLayout.SOUTH);
	}

	protected BnTableCustomizer getBnTableCustomizer() {
		if (bnTableCustomizer == null) {
			bnTableCustomizer = new BnTableCustomizer();
		}
		return bnTableCustomizer;
	}

	protected JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.add(getCloseButton());
		}
		return panel;
	}

	protected JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					dispose();
				}
			});
			closeButton.setText("Close");
		}
		return closeButton;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// prepare the bean for customization
				BnTable bean = new BnTable();
				ModelProvider provider = new ModelProvider();
				provider.setPresentationModelType(BrowserPM.class);
				
				bean.setModelProvider(provider);
				bean.setPath(new Path("this.lastSearchPatterns"));
//
//				bean.setColumns( new BnColumn[] {
//						new BnColumn(new Path("street"), "Street")
//						,new BnColumn(new Path("country"), "Country")
//						,new BnColumn(new Path("zip"), "ZIP")
//						,new BnColumn(new Path("city"), "City")
//				} );
				// then create the customizer frame
				BnTableCustomizerTestGUI f = new BnTableCustomizerTestGUI();
				f.bnTableCustomizer.setObject(bean);

				// finally show the frame
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.pack();
				f.setLocationRelativeTo(null);
				f.setVisible(true);
			}
		});
	}
}