/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.model;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.NoSuchElementException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.BnComboBox;
import org.beanfabrics.swing.BnTextField;

@SuppressWarnings("serial")
public class DatePMTestGUI extends JFrame implements View<DatePMTestGUI.DateTestModel>, ModelSubscriber {
	private BnButton defaultLocaleDateButton;
	private JSeparator separator;
	private BnTextField dateTimeTextField;
	private JLabel dateTimeLabel;
	private final Link link = new Link(this);
	private ModelProvider localProvider;
	private JLabel defaultLocaleDateLabel;
	private BnTextField defaultLocaleDateTextField;
	private BnTextField autoConvertDateTextField;
	private JLabel autoConvertDateLabel;
	private DateTestModel dateTestModel;
	private JLabel dateLabel;
	private JLabel localeLabel;
	private BnTextField dateTextField;
	private BnComboBox localeComboBox;
	private JPanel panel;

	/**
	 * Launch the application
	 *
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			DatePMTestGUI frame = new DatePMTestGUI();
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public DatePMTestGUI() {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(getPanel(), BorderLayout.CENTER);
		//
	}

	private ModelProvider getLocalProvider() {
		if (localProvider == null) {
			localProvider = new ModelProvider(); // @wb:location=35,247
			localProvider.setPresentationModel(getDateTestModel());
			localProvider.setPresentationModelType(DateTestModel.class);
		}
		return localProvider;
	}

	/** {@inheritDoc} */
	public DateTestModel getPresentationModel() {
		return getLocalProvider().getPresentationModel();
	}

	/** {@inheritDoc} */
	public void setPresentationModel(DateTestModel pModel) {
		getLocalProvider().setPresentationModel(pModel);
	}

	/** {@inheritDoc} */
	public IModelProvider getModelProvider() {
		return this.link.getModelProvider();
	}

	/** {@inheritDoc} */
	public void setModelProvider(IModelProvider provider) {
		this.link.setModelProvider(provider);
	}

	public Path getPath() {
		/** {@inheritDoc} */
		return this.link.getPath();
	}

	/** {@inheritDoc} */
	public void setPath(Path path) {
		this.link.setPath(path);
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			final GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[] { 0, 0, 7 };
			panel.setLayout(gridBagLayout);
			final GridBagConstraints gridBagConstraints_6 = new GridBagConstraints();
			gridBagConstraints_6.gridwidth = 2;
			gridBagConstraints_6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints_6.gridx = 1;
			gridBagConstraints_6.gridy = 3;
			gridBagConstraints_6.insets = new Insets(2, 2, 2, 2);
			panel.add(getDateTextField(), gridBagConstraints_6);
			final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
			gridBagConstraints_4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints_4.gridx = 0;
			gridBagConstraints_4.gridy = 4;
			gridBagConstraints_4.insets = new Insets(2, 2, 2, 2);
			panel.add(getAutoConvertDateLabel(), gridBagConstraints_4);
			final GridBagConstraints gridBagConstraints_7 = new GridBagConstraints();
			gridBagConstraints_7.gridwidth = 2;
			gridBagConstraints_7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints_7.gridx = 1;
			gridBagConstraints_7.gridy = 4;
			gridBagConstraints_7.insets = new Insets(2, 2, 2, 2);
			panel.add(getAutoConvertDateTextField(), gridBagConstraints_7);
			final GridBagConstraints gridBagConstraints_8 = new GridBagConstraints();
			gridBagConstraints_8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints_8.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints_8.gridy = 5;
			gridBagConstraints_8.gridx = 0;
			panel.add(getDateTimeLabel(), gridBagConstraints_8);
			final GridBagConstraints gridBagConstraints_9 = new GridBagConstraints();
			gridBagConstraints_9.gridwidth = 2;
			gridBagConstraints_9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints_9.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints_9.gridy = 5;
			gridBagConstraints_9.gridx = 1;
			panel.add(getDateTimeTextField(), gridBagConstraints_9);
		}
		return panel;
	}

	private BnComboBox getLocaleComboBox() {
		if (localeComboBox == null) {
			localeComboBox = new BnComboBox();
			localeComboBox.setPath(new org.beanfabrics.Path("this.locale"));
			localeComboBox.setModelProvider(getLocalProvider());
		}
		return localeComboBox;
	}

	private BnTextField getDateTextField() {
		if (dateTextField == null) {
			final GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new Insets(2, 2, 2, 2);
			panel.add(getLocaleLabel(), gridBagConstraints);
			final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
			gridBagConstraints_1.gridwidth = 2;
			gridBagConstraints_1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints_1.gridx = 1;
			gridBagConstraints_1.gridy = 0;
			gridBagConstraints_1.insets = new Insets(2, 2, 2, 2);
			panel.add(getLocaleComboBox(), gridBagConstraints_1);
			final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
			gridBagConstraints_2.gridwidth = 3;
			gridBagConstraints_2.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints_2.gridy = 1;
			gridBagConstraints_2.gridx = 0;
			panel.add(getSeparator(), gridBagConstraints_2);
			final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
			gridBagConstraints_3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints_3.gridx = 0;
			gridBagConstraints_3.gridy = 2;
			gridBagConstraints_3.insets = new Insets(2, 2, 2, 2);
			panel.add(getDefaultLocaleDateLabel(), gridBagConstraints_3);
			final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
			gridBagConstraints_5.weightx = 1;
			gridBagConstraints_5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints_5.gridx = 1;
			gridBagConstraints_5.gridy = 2;
			gridBagConstraints_5.insets = new Insets(2, 2, 2, 2);
			panel.add(getDefaultLocaleDateTextField(), gridBagConstraints_5);
			final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
			gridBagConstraints_4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints_4.gridy = 2;
			gridBagConstraints_4.gridx = 2;
			panel.add(getDefaultLocaleDateButton(), gridBagConstraints_4);
			final GridBagConstraints gridBagConstraints_6 = new GridBagConstraints();
			gridBagConstraints_6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints_6.gridx = 0;
			gridBagConstraints_6.gridy = 3;
			gridBagConstraints_6.insets = new Insets(2, 2, 2, 2);
			panel.add(getDateLabel(), gridBagConstraints_6);
			dateTextField = new BnTextField();
			dateTextField.setPath(new org.beanfabrics.Path("this.date"));
			dateTextField.setModelProvider(getLocalProvider());
		}
		return dateTextField;
	}

	private JLabel getLocaleLabel() {
		if (localeLabel == null) {
			localeLabel = new JLabel();
			localeLabel.setText("Locale");
		}
		return localeLabel;
	}

	private JLabel getDateLabel() {
		if (dateLabel == null) {
			dateLabel = new JLabel();
			dateLabel.setText("Date");
		}
		return dateLabel;
	}

	private DateTestModel getDateTestModel() {
		if (dateTestModel == null) {
			dateTestModel = new DateTestModel(); // @wb:location=159,255
		}
		return dateTestModel;
	}

	private JLabel getAutoConvertDateLabel() {
		if (autoConvertDateLabel == null) {
			autoConvertDateLabel = new JLabel();
			autoConvertDateLabel.setText("Auto Convert. Date");
		}
		return autoConvertDateLabel;
	}

	private BnTextField getAutoConvertDateTextField() {
		if (autoConvertDateTextField == null) {
			autoConvertDateTextField = new BnTextField();
			autoConvertDateTextField.setPath(new org.beanfabrics.Path("this.autoConvertDate"));
			autoConvertDateTextField.setModelProvider(getLocalProvider());
		}
		return autoConvertDateTextField;
	}

	private BnTextField getDefaultLocaleDateTextField() {
		if (defaultLocaleDateTextField == null) {
			defaultLocaleDateTextField = new BnTextField();
			defaultLocaleDateTextField.setColumns(10);
			defaultLocaleDateTextField.setPath(new org.beanfabrics.Path("this.defaultLocaleDate"));
			defaultLocaleDateTextField.setModelProvider(getLocalProvider());
		}
		return defaultLocaleDateTextField;
	}

	private JLabel getDefaultLocaleDateLabel() {
		if (defaultLocaleDateLabel == null) {
			defaultLocaleDateLabel = new JLabel();
			defaultLocaleDateLabel.setText("Default Locale Date");
		}
		return defaultLocaleDateLabel;
	}

	private JLabel getDateTimeLabel() {
		if (dateTimeLabel == null) {
			dateTimeLabel = new JLabel();
			dateTimeLabel.setText("Date & Time");
		}
		return dateTimeLabel;
	}

	private BnTextField getDateTimeTextField() {
		if (dateTimeTextField == null) {
			dateTimeTextField = new BnTextField();
			dateTimeTextField.setPath(new org.beanfabrics.Path("this.dateTime"));
			dateTimeTextField.setModelProvider(getLocalProvider());
			dateTimeTextField.setColumns(12);
		}
		return dateTimeTextField;
	}

	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
		}
		return separator;
	}

	private BnButton getDefaultLocaleDateButton() {
		if (defaultLocaleDateButton == null) {
			defaultLocaleDateButton = new BnButton();
			defaultLocaleDateButton.setPath(new org.beanfabrics.Path("this.setDefaultLocaleDate"));
			defaultLocaleDateButton.setModelProvider(getLocalProvider());
		}
		return defaultLocaleDateButton;
	}

	public static class DateTestModel extends AbstractPM {
		protected final IDatePM defaultLocaleDate = new DatePM();
		protected final IOperationPM setDefaultLocaleDate = new OperationPM();
		protected final IDatePM date = new DatePM();
		protected final IDatePM autoConvertDate = new DatePM();
		protected final ITextPM locale = new TextPM();
		protected final IDatePM dateTime = new DatePM();

		public DateTestModel() {
			PMManager.setup(this);
			setDefaultLocaleDate.setTitle("now");
			locale.setOptions(getLocaleOptions(new Locale[] { Locale.GERMAN, Locale.US, Locale.UK }));
			locale.setText(Locale.getDefault().getDisplayName());
			date.setDate(new Date());
			dateTime.setFormat(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT));
		}

		private Options<Locale> getLocaleOptions(Locale[] locales) {
			Options<Locale> opts = new Options<Locale>();
			for (Locale locale : locales) {
				opts.put(locale, locale.getDisplayName());
			}
			return opts;
		}

		@Operation
		public void setDefaultLocaleDate() {
			Date now = new Date();
			defaultLocaleDate.setDate(now);
		}

		@OnChange(path = "defaultLocaleDate")
		private void updateDates() {
			try {
				Date dateValue = defaultLocaleDate.getDate();
				date.setDate(dateValue);
				autoConvertDate.setDate(dateValue);
			} catch (ConversionException e) {
				// ignore
			}
		}

		@OnChange(path = "locale")
		private void updateDate() {
			try {
				Locale localeValue = (Locale) locale.getOptions().getKey(locale.getText());
				date.setFormat(DatePM.getDateFormat(localeValue));
			} catch (NoSuchElementException ex) {
				// ignore
			}
		}

		@OnChange(path = "locale")
		private void updateAutoConvertDate() {
			try {
				Locale localeValue = (Locale) locale.getOptions().getKey(locale.getText());

				Date date;
				try {
					date = autoConvertDate.getDate();
				} catch (ConversionException e) {
					date = null;
				}
				autoConvertDate.setFormat(DatePM.getDateFormat(localeValue));
				if (date != null) {
					autoConvertDate.setDate(date);
				}
			} catch (NoSuchElementException ex) {
				// ignore
			}
		}
	}
}