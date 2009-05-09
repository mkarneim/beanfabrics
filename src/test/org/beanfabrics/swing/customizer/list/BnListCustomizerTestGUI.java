/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.list;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.swing.list.BnList;

/**
 *
 * @author Michael Karneim
 */
public class BnListCustomizerTestGUI extends JFrame {

	public static void main(String[] args) {
		BnList bean = new BnList();
		ModelProvider ds = new ModelProvider();
		ds.setPresentationModelType(BrowserPM.class);
		bean.setModelProvider(ds);

		BnListCustomizerTestGUI frame = new BnListCustomizerTestGUI();
		frame.bnListCustomizer.setObject( bean);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

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
	private JPanel buttonPanel;
	private BnListCustomizer bnListCustomizer;

	public BnListCustomizerTestGUI() {
		setLayout(new BorderLayout());
		getContentPane().add(getBnListCustomizer(), BorderLayout.CENTER);
		getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
		//
		pack();
	}

	private BnListCustomizer getBnListCustomizer() {
		if (bnListCustomizer == null) {
			bnListCustomizer = new BnListCustomizer();
		}
		return bnListCustomizer;
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getCloseButton());
		}
		return buttonPanel;
	}

	private JButton getCloseButton() {
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
}