/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.PMManager;

/**
 * @author Michael Karneim
 */
public class BnRadioButtonTestGUI extends JFrame {
	private BnLabel bnLabel_2;
	private BnLabel bnLabel_1;
	private BnLabel bnLabel;
	private JButton toggleEdgreenButton;
	private MyModel myModel;
	private ModelProvider provider;

	public static class MyModel extends AbstractPM {
		private transient WeakPropertyChangeListener listener = new WeakPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println("red="+red.getText());
				System.out.println("green="+green.getText());
				System.out.println("blue="+blue.getText());
			}
		};

		private BooleanPM red = new BooleanPM();
		private BooleanPM green = new BooleanPM();
		private BooleanPM blue = new BooleanPM();

		public MyModel() {
			PMManager.setup(this);
			red.setBoolean( true);
			this.addPropertyChangeListener( listener);
		}
	}

	private ButtonGroup buttonGroup = new ButtonGroup();
	private BnRadioButton blueBnRadioButton;
	private BnRadioButton greenBnRadioButton;
	private BnRadioButton redBnRadioButton;
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			BnRadioButtonTestGUI frame = new BnRadioButtonTestGUI();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public BnRadioButtonTestGUI() {
		super();
		final GridBagConstraints gridBagConstraints;
		final GridBagConstraints gridBagConstraints_1;
		final GridBagConstraints gridBagConstraints_2;
		final GridBagConstraints gridBagConstraints_3;
		final GridBagConstraints gridBagConstraints_4;
		final GridBagConstraints gridBagConstraints_5;
		final GridBagConstraints gridBagConstraints_6;
		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0,7};
		gridBagLayout.rowHeights = new int[] {0,7,7,7};
		getContentPane().setLayout(gridBagLayout);
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridx = 0;
		getContentPane().add(getRedBnRadioButton(), gridBagConstraints);
		gridBagConstraints_1 = new GridBagConstraints();
		gridBagConstraints_1.anchor = GridBagConstraints.WEST;
		gridBagConstraints_1.gridy = 1;
		gridBagConstraints_1.gridx = 0;
		gridBagConstraints_4 = new GridBagConstraints();
		gridBagConstraints_4.weightx = 1;
		gridBagConstraints_4.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints_4.anchor = GridBagConstraints.WEST;
		gridBagConstraints_4.gridy = 0;
		gridBagConstraints_4.gridx = 1;
		getContentPane().add(getBnLabel(), gridBagConstraints_4);
		getContentPane().add(getGreenBnRadioButton(), gridBagConstraints_1);
		gridBagConstraints_2 = new GridBagConstraints();
		gridBagConstraints_2.anchor = GridBagConstraints.WEST;
		gridBagConstraints_2.gridy = 2;
		gridBagConstraints_2.gridx = 0;
		gridBagConstraints_5 = new GridBagConstraints();
		gridBagConstraints_5.anchor = GridBagConstraints.WEST;
		gridBagConstraints_5.gridy = 1;
		gridBagConstraints_5.gridx = 1;
		getContentPane().add(getBnLabel_1(), gridBagConstraints_5);
		getContentPane().add(getBlueBnRadioButton(), gridBagConstraints_2);
		gridBagConstraints_3 = new GridBagConstraints();
		gridBagConstraints_3.gridy = 3;
		gridBagConstraints_3.gridx = 0;
		gridBagConstraints_6 = new GridBagConstraints();
		gridBagConstraints_6.gridwidth = 100;
		gridBagConstraints_6.anchor = GridBagConstraints.WEST;
		gridBagConstraints_6.gridy = 2;
		gridBagConstraints_6.gridx = 1;
		getContentPane().add(getBnLabel_2(), gridBagConstraints_6);
		getContentPane().add(getToggleEdgreenButton(), gridBagConstraints_3);
		//
	}


	protected BnRadioButton getRedBnRadioButton() {
		if (redBnRadioButton == null) {
			provider = new ModelProvider(); // @wb:location=162,400
			provider.setPresentationModel(getMyModel());
			redBnRadioButton = new BnRadioButton();
			redBnRadioButton.setPath(new org.beanfabrics.Path("this.red"));
			redBnRadioButton.setModelProvider(provider);
			buttonGroup.add(redBnRadioButton);
			redBnRadioButton.setText("Red");
		}
		return redBnRadioButton;
	}

	protected BnRadioButton getGreenBnRadioButton() {
		if (greenBnRadioButton == null) {
			greenBnRadioButton = new BnRadioButton();
			greenBnRadioButton.setPath(new org.beanfabrics.Path("this.green"));
			greenBnRadioButton.setModelProvider(getLocalProvider());
			buttonGroup.add(greenBnRadioButton);
			greenBnRadioButton.setText("Green");
		}
		return greenBnRadioButton;
	}

	protected BnRadioButton getBlueBnRadioButton() {
		if (blueBnRadioButton == null) {
			blueBnRadioButton = new BnRadioButton();
			blueBnRadioButton.setPath(new org.beanfabrics.Path("this.blue"));
			blueBnRadioButton.setModelProvider(getLocalProvider());
			buttonGroup.add(blueBnRadioButton);
			blueBnRadioButton.setText("Blue");
		}
		return blueBnRadioButton;
	}

	protected ModelProvider getLocalProvider() {
		if (provider == null) {
		}
		return provider;
	}

	protected MyModel getMyModel() {
		if (myModel == null) {
			myModel = new MyModel(); // @wb:location=214,403
		}
		return myModel;
	}

	protected JButton getToggleEdgreenButton() {
		if (toggleEdgreenButton == null) {
			toggleEdgreenButton = new JButton();
			toggleEdgreenButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					boolean sel = myModel.green.getBoolean();
					myModel.green.setBoolean( ! sel);
				}
			});
			toggleEdgreenButton.setText("toggle model.green");
		}
		return toggleEdgreenButton;
	}

	protected BnLabel getBnLabel() {
		if (bnLabel == null) {
			bnLabel = new BnLabel();
			bnLabel.setPath(new org.beanfabrics.Path("this.red"));
			bnLabel.setModelProvider(getLocalProvider());
		}
		return bnLabel;
	}

	protected BnLabel getBnLabel_1() {
		if (bnLabel_1 == null) {
			bnLabel_1 = new BnLabel();
			bnLabel_1.setPath(new org.beanfabrics.Path("this.green"));
			bnLabel_1.setModelProvider(getLocalProvider());
		}
		return bnLabel_1;
	}

	protected BnLabel getBnLabel_2() {
		if (bnLabel_2 == null) {
			bnLabel_2 = new BnLabel();
			bnLabel_2.setPath(new org.beanfabrics.Path("this.blue"));
			bnLabel_2.setModelProvider(getLocalProvider());
		}
		return bnLabel_2;
	}
}