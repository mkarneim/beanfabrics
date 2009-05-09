/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.list;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.math.BigDecimal;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.beanfabrics.Binder;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BigDecimalPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.IconPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.MoneyPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Property;
import org.beanfabrics.support.Validation;
import org.beanfabrics.swing.BnButton;

/**
 * @author Max Gensthaler
 */
public class BnListTestGUI {
	private static BnListTestGUI instance = null;
	private MainFrame mainFrame;
	private MainModel mainModel;

	public static void main(String[] args) throws Exception {
		Runnable r = new Runnable() {
			public void run() {
				try {
					try {
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					} catch (Exception e) {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					}
					BnListTestGUI gui = BnListTestGUI.getInstance();
					MainFrame f = gui.getMainFrame();
					f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					MainModel pModel = gui.getMainModel();
					Binder.bind(f, pModel);
					f.setSize(new Dimension(600, 500));
					f.setLocationRelativeTo(null); // center on screen
					f.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		SwingUtilities.invokeLater(r);
	}

	private BnListTestGUI() {
	}

	public static BnListTestGUI getInstance() {
		if (instance == null) {
			instance = new BnListTestGUI();
		}
		return instance;
	}

	public MainFrame getMainFrame() {
		if (mainFrame == null) {
			mainFrame = new MainFrame();
		}
		return mainFrame;
	}

	public MainModel getMainModel() {
		if (mainModel == null) {
			mainModel = new MainModel();
		}
		return mainModel;
	}

	public class MainModel extends AbstractPM {
		private int textIdx = 0;
		@Property
		private final ListPM<TextPM> textList = new ListPM<TextPM>();
		@Property
		private final IOperationPM addText = new OperationPM();
		@Property
		private final IOperationPM removeText = new OperationPM();

		private int booleanIdx = 0;
		@Property
		private final ListPM<BooleanPM> booleanList = new ListPM<BooleanPM>();
		@Property
		private final IOperationPM addBoolean = new OperationPM();
		@Property
		private final IOperationPM removeBoolean = new OperationPM();

		private int integerIdx = 0;
		@Property
		private final ListPM<IntegerPM> integerList = new ListPM<IntegerPM>();
		@Property
		private final IOperationPM addInteger = new OperationPM();
		@Property
		private final IOperationPM removeInteger = new OperationPM();

		private int decimalIdx = 0;
		@Property
		private final ListPM<BigDecimalPM> decimalList = new ListPM<BigDecimalPM>();
		@Property
		private final IOperationPM addDecimal = new OperationPM();
		@Property
		private final IOperationPM removeDecimal = new OperationPM();

		private int moneyIdx = 0;
		@Property
		private final ListPM<MoneyPM> moneyList = new ListPM<MoneyPM>();
		@Property
		private final IOperationPM addMoney = new OperationPM();
		@Property
		private final IOperationPM removeMoney = new OperationPM();

		private int numberIdx = 0;
		@Property
		private final ListPM<IntegerPM> numberList = new ListPM<IntegerPM>();
		@Property
		private final IOperationPM addNumber = new OperationPM();
		@Property
		private final IOperationPM removeNumber = new OperationPM();

		private int iconIdx = 0;
		@Property
		private final ListPM<IconPM> iconList = new ListPM<IconPM>();
		@Property
		private final IOperationPM addIcon = new OperationPM();
		@Property
		private final IOperationPM removeIcon = new OperationPM();
		private/* static */final JFileChooser FILE_CHOOSER = new JFileChooser();

		public MainModel() {
			PMManager.setup(this);

			while (textIdx < 10) {
				TextPM pM = new TextPM();
				pM.setText("text " + textIdx++);
				textList.add(pM);
			}
			addText.setTitle("+");
			removeText.setTitle("-");

			while (booleanIdx < 10) {
				BooleanPM cell = new BooleanPM();
				cell.setBoolean(booleanIdx++ % 2 == 0);
				booleanList.add(cell);
			}
			addBoolean.setTitle("+");
			removeBoolean.setTitle("-");

			while (integerIdx < 10) {
				IntegerPM cell = new IntegerPM();
				cell.setInteger(integerIdx++);
				integerList.add(cell);
			}
			addInteger.setTitle("+");
			removeInteger.setTitle("-");

			while (decimalIdx < 10) {
				BigDecimalPM cell = new BigDecimalPM();
				cell.setBigDecimal(new BigDecimal(decimalIdx++));
				decimalList.add(cell);
			}
			addDecimal.setTitle("+");
			removeDecimal.setTitle("-");

			while (moneyIdx < 10) {
				MoneyPM cell = new MoneyPM();
				cell.setBigDecimal(new BigDecimal(moneyIdx++));
				moneyList.add(cell);
				moneyList.getSelection().addAll();
			}
			addMoney.setTitle("+");
			removeMoney.setTitle("-");

			while (numberIdx < 10) {
				IntegerPM cell = new IntegerPM();
				cell.setInteger(numberIdx++);
				numberList.add(cell);
			}
			addNumber.setTitle("+");
			removeNumber.setTitle("-");

			File[] fileList = new File(".").listFiles();
			while (iconIdx < fileList.length) {
				IconPM cell = new IconPM();
				cell.setIcon(FILE_CHOOSER.getIcon(fileList[iconIdx++]));
				iconList.add(cell);
			}
			addIcon.setTitle("+");
			removeIcon.setTitle("-");
		}

		@Operation
		public void addText() {
			addText.check();
			TextPM pM = new TextPM();
			pM.setText("text " + textIdx++);
			textList.add(pM);
		}

		@Operation
		public void removeText() {
			removeText.check();
			for (TextPM pM : textList.getSelection().toCollection()) {
				textList.remove(pM);
			}
		}

		@Validation(path = "removeText", message = "Nothing selected")
		public boolean isRemoveTextEdValid() {
			return textList.getSelection().size() > 0;
		}

		@Operation
		public void addBoolean() {
			addBoolean.check();
			BooleanPM cell = new BooleanPM();
			cell.setBoolean(booleanIdx++ % 2 == 0);
			booleanList.add(cell);
		}

		@Operation
		public void removeBoolean() {
			removeBoolean.check();
			for (BooleanPM cell : booleanList.getSelection().toCollection()) {
				booleanList.remove(cell);
			}
		}

		@Validation(path = "removeBoolean", message = "Nothing selected")
		public boolean isRemoveBooleanEdValid() {
			return booleanList.getSelection().size() > 0;
		}

		@Operation
		public void addInteger() {
			addInteger.check();
			IntegerPM cell = new IntegerPM();
			cell.setInteger(integerIdx++);
			integerList.add(cell);
		}

		@Operation
		public void removeInteger() {
			removeInteger.check();
			for (IntegerPM cell : integerList.getSelection().toCollection()) {
				integerList.remove(cell);
			}
		}

		@Validation(path = "removeInteger", message = "Nothing selected")
		public boolean isRemoveIntegerEdValid() {
			return integerList.getSelection().size() > 0;
		}

		@Operation
		public void addDecimal() {
			addDecimal.check();
			BigDecimalPM cell = new BigDecimalPM();
			cell.setBigDecimal(new BigDecimal(decimalIdx++));
			decimalList.add(cell);
		}

		@Operation
		public void removeDecimal() {
			removeDecimal.check();
			for (BigDecimalPM cell : decimalList.getSelection().toCollection()) {
				decimalList.remove(cell);
			}
		}

		@Validation(path = "removeDecimal", message = "Nothing selected")
		public boolean isRemoveDecimalEdValid() {
			return decimalList.getSelection().size() > 0;
		}

		@Operation
		public void addMoney() {
			addMoney.check();
			MoneyPM cell = new MoneyPM();
			cell.setBigDecimal(new BigDecimal(moneyIdx++));
			moneyList.add(cell);
		}

		@Operation
		public void removeMoney() {
			removeMoney.check();
			for (MoneyPM cell : moneyList.getSelection().toCollection()) {
				moneyList.remove(cell);
			}
		}

		@Validation(path = "removeMoney", message = "Nothing selected")
		public boolean isRemoveMoneyEdValid() {
			return moneyList.getSelection().size() > 0;
		}

		@Operation
		public void addNumber() {
			addNumber.check();
			IntegerPM cell = new IntegerPM();
			cell.setInteger(numberIdx++);
			numberList.add(cell);
		}

		@Operation
		public void removeNumber() {
			removeNumber.check();
			for (IntegerPM cell : numberList.getSelection().toCollection()) {
				numberList.remove(cell);
			}
		}

		@Validation(path = "removeNumber", message = "Nothing selected")
		public boolean isRemoveNumberEdValid() {
			return numberList.getSelection().size() > 0;
		}

		@Operation
		public void addIcon() {
			addIcon.check();
			IconPM cell = new IconPM();
			cell.setIcon(FILE_CHOOSER.getIcon(new File(".").listFiles()[iconIdx++]));
			iconList.add(cell);
		}

		@Operation
		public void removeIcon() {
			removeIcon.check();
			for (IconPM dml : iconList.getSelection().toCollection()) {
				iconList.remove(dml);
			}
		}

		@Validation(path = "removeIcon", message = "Nothing selected")
		public boolean isRemoveIconEdValid() {
			return iconList.getSelection().size() > 0;
		}

		@Validation(path = "addIcon", message = "No more files in actual dir")
		public boolean isAddIconEdValid() {
			return iconIdx < new File(".").listFiles().length;
		}
	}

	public static class MainFrame extends JFrame implements View<MainModel>, ModelSubscriber {
		private final Link link = new Link(this);
		private ModelProvider localProvider;
		private BnButton bnButton_13;
		private BnButton bnButton_12;
		private BnButton bnButton_11;
		private BnButton bnButton_10;
		private BnButton bnButton_9;
		private BnButton bnButton_8;
		private BnButton bnButton_7;
		private BnButton bnButton_6;
		private BnButton bnButton_5;
		private BnButton bnButton_4;
		private BnButton bnButton_3;
		private BnButton bnButton_2;
		private BnButton bnButton_1;
		private BnButton bnButton;
		private BnList bnList_6;
		private BnList bnList_5;
		private JScrollPane scrollPane_6;
		private JScrollPane scrollPane_5;
		private BnList bnList_4;
		private BnList bnList_3;
		private BnList bnList_2;
		private JScrollPane scrollPane_4;
		private JScrollPane scrollPane_3;
		private JScrollPane scrollPane_2;
		private BnList bnList_1;
		private JScrollPane scrollPane_1;
		private BnList bnList;
		private JScrollPane scrollPane;
		private JPanel panel;

		public MainFrame() {
			super();
			setLayout(new BorderLayout());
			getContentPane().add(getPanel(), BorderLayout.CENTER);
			//
		}

		protected ModelProvider getLocalProvider() {
			if (localProvider == null) {
				localProvider = new ModelProvider(); // @wb:location=11,242
				localProvider.setPresentationModelType(MainModel.class);
			}
			return localProvider;
		}

		/** {@inheritDoc} */
		public MainModel getPresentationModel() {
			return getLocalProvider().getPresentationModel();
		}

		/** {@inheritDoc} */
		public void setPresentationModel(MainModel pModel) {
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

		private JPanel getPanel() {
			if (panel == null) {
				panel = new JPanel();
				final GridBagLayout gridBagLayout = new GridBagLayout();
				gridBagLayout.rowHeights = new int[] { 0, 7, 7 };
				gridBagLayout.columnWidths = new int[] { 0, 7, 7, 7, 7, 7, 7 };
				panel.setLayout(gridBagLayout);
				final GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.fill = GridBagConstraints.BOTH;
				gridBagConstraints.weighty = 1;
				gridBagConstraints.weightx = 1;
				gridBagConstraints.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints.gridy = 0;
				gridBagConstraints.gridx = 0;
				panel.add(getScrollPane(), gridBagConstraints);
				final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
				gridBagConstraints_1.fill = GridBagConstraints.BOTH;
				gridBagConstraints_1.weighty = 1;
				gridBagConstraints_1.weightx = 1;
				gridBagConstraints_1.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_1.gridy = 0;
				gridBagConstraints_1.gridx = 1;
				panel.add(getScrollPane_1(), gridBagConstraints_1);
				final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
				gridBagConstraints_2.fill = GridBagConstraints.BOTH;
				gridBagConstraints_2.weighty = 1;
				gridBagConstraints_2.weightx = 1;
				gridBagConstraints_2.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_2.gridy = 0;
				gridBagConstraints_2.gridx = 2;
				panel.add(getScrollPane_2(), gridBagConstraints_2);
				final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
				gridBagConstraints_3.fill = GridBagConstraints.BOTH;
				gridBagConstraints_3.weighty = 1;
				gridBagConstraints_3.weightx = 1;
				gridBagConstraints_3.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_3.gridy = 0;
				gridBagConstraints_3.gridx = 3;
				panel.add(getScrollPane_3(), gridBagConstraints_3);
				final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
				gridBagConstraints_4.fill = GridBagConstraints.BOTH;
				gridBagConstraints_4.weighty = 1;
				gridBagConstraints_4.weightx = 1;
				gridBagConstraints_4.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_4.gridy = 0;
				gridBagConstraints_4.gridx = 4;
				panel.add(getScrollPane_4(), gridBagConstraints_4);
				final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
				gridBagConstraints_5.fill = GridBagConstraints.BOTH;
				gridBagConstraints_5.weighty = 1;
				gridBagConstraints_5.weightx = 1;
				gridBagConstraints_5.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_5.gridy = 0;
				gridBagConstraints_5.gridx = 5;
				panel.add(getScrollPane_5(), gridBagConstraints_5);
				final GridBagConstraints gridBagConstraints_6 = new GridBagConstraints();
				gridBagConstraints_6.fill = GridBagConstraints.BOTH;
				gridBagConstraints_6.weighty = 1;
				gridBagConstraints_6.weightx = 1;
				gridBagConstraints_6.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_6.gridy = 0;
				gridBagConstraints_6.gridx = 6;
				panel.add(getScrollPane_6(), gridBagConstraints_6);
				final GridBagConstraints gridBagConstraints_7 = new GridBagConstraints();
				gridBagConstraints_7.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_7.gridy = 1;
				gridBagConstraints_7.gridx = 0;
				panel.add(getBnButton(), gridBagConstraints_7);
				final GridBagConstraints gridBagConstraints_8 = new GridBagConstraints();
				gridBagConstraints_8.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_8.gridy = 2;
				gridBagConstraints_8.gridx = 0;
				final GridBagConstraints gridBagConstraints_9 = new GridBagConstraints();
				gridBagConstraints_9.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_9.gridy = 1;
				gridBagConstraints_9.gridx = 1;
				panel.add(getBnButton_2(), gridBagConstraints_9);
				final GridBagConstraints gridBagConstraints_11 = new GridBagConstraints();
				gridBagConstraints_11.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_11.gridy = 1;
				gridBagConstraints_11.gridx = 2;
				panel.add(getBnButton_4(), gridBagConstraints_11);
				final GridBagConstraints gridBagConstraints_13 = new GridBagConstraints();
				gridBagConstraints_13.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_13.gridy = 1;
				gridBagConstraints_13.gridx = 3;
				panel.add(getBnButton_6(), gridBagConstraints_13);
				final GridBagConstraints gridBagConstraints_15 = new GridBagConstraints();
				gridBagConstraints_15.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_15.gridy = 1;
				gridBagConstraints_15.gridx = 4;
				panel.add(getBnButton_8(), gridBagConstraints_15);
				final GridBagConstraints gridBagConstraints_17 = new GridBagConstraints();
				gridBagConstraints_17.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_17.gridy = 1;
				gridBagConstraints_17.gridx = 5;
				panel.add(getBnButton_10(), gridBagConstraints_17);
				final GridBagConstraints gridBagConstraints_19 = new GridBagConstraints();
				gridBagConstraints_19.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_19.gridy = 1;
				gridBagConstraints_19.gridx = 6;
				panel.add(getBnButton_12(), gridBagConstraints_19);
				panel.add(getBnButton_1(), gridBagConstraints_8);
				final GridBagConstraints gridBagConstraints_10 = new GridBagConstraints();
				gridBagConstraints_10.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_10.gridy = 2;
				gridBagConstraints_10.gridx = 1;
				panel.add(getBnButton_3(), gridBagConstraints_10);
				final GridBagConstraints gridBagConstraints_12 = new GridBagConstraints();
				gridBagConstraints_12.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_12.gridy = 2;
				gridBagConstraints_12.gridx = 2;
				panel.add(getBnButton_5(), gridBagConstraints_12);
				final GridBagConstraints gridBagConstraints_14 = new GridBagConstraints();
				gridBagConstraints_14.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_14.gridy = 2;
				gridBagConstraints_14.gridx = 3;
				panel.add(getBnButton_7(), gridBagConstraints_14);
				final GridBagConstraints gridBagConstraints_16 = new GridBagConstraints();
				gridBagConstraints_16.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_16.gridy = 2;
				gridBagConstraints_16.gridx = 4;
				panel.add(getBnButton_9(), gridBagConstraints_16);
				final GridBagConstraints gridBagConstraints_18 = new GridBagConstraints();
				gridBagConstraints_18.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_18.gridy = 2;
				gridBagConstraints_18.gridx = 5;
				panel.add(getBnButton_11(), gridBagConstraints_18);
				final GridBagConstraints gridBagConstraints_20 = new GridBagConstraints();
				gridBagConstraints_20.insets = new Insets(2, 2, 2, 2);
				gridBagConstraints_20.gridy = 2;
				gridBagConstraints_20.gridx = 6;
				panel.add(getBnButton_13(), gridBagConstraints_20);
			}
			return panel;
		}

		private JScrollPane getScrollPane() {
			if (scrollPane == null) {
				scrollPane = new JScrollPane();
				scrollPane.setViewportView(getBnList());
			}
			return scrollPane;
		}

		private BnList getBnList() {
			if (bnList == null) {
				bnList = new BnList();
				bnList.setModelProvider(getLocalProvider());
				bnList.setPath(new Path("this.textList"));
				bnList.setCellConfig(new CellConfig(new Path("this")));
			}
			return bnList;
		}

		private JScrollPane getScrollPane_1() {
			if (scrollPane_1 == null) {
				scrollPane_1 = new JScrollPane();
				scrollPane_1.setViewportView(getBnList_1());
			}
			return scrollPane_1;
		}

		private BnList getBnList_1() {
			if (bnList_1 == null) {
				bnList_1 = new BnList();
				bnList_1.setModelProvider(getLocalProvider());
				bnList_1.setPath(new Path("this.booleanList"));
				bnList_1.setCellConfig(new CellConfig(new Path("this")));
			}
			return bnList_1;
		}

		private JScrollPane getScrollPane_2() {
			if (scrollPane_2 == null) {
				scrollPane_2 = new JScrollPane();
				scrollPane_2.setViewportView(getBnList_2());
			}
			return scrollPane_2;
		}

		private JScrollPane getScrollPane_3() {
			if (scrollPane_3 == null) {
				scrollPane_3 = new JScrollPane();
				scrollPane_3.setViewportView(getBnList_3());
			}
			return scrollPane_3;
		}

		private JScrollPane getScrollPane_4() {
			if (scrollPane_4 == null) {
				scrollPane_4 = new JScrollPane();
				scrollPane_4.setViewportView(getBnList_4());
			}
			return scrollPane_4;
		}

		private BnList getBnList_2() {
			if (bnList_2 == null) {
				bnList_2 = new BnList();
				bnList_2.setModelProvider(getLocalProvider());
				bnList_2.setPath(new Path("this.integerList"));
				bnList_2.setCellConfig(new CellConfig(new Path("this")));
			}
			return bnList_2;
		}

		private BnList getBnList_3() {
			if (bnList_3 == null) {
				bnList_3 = new BnList();
				bnList_3.setModelProvider(getLocalProvider());
				bnList_3.setPath(new Path("this.decimalList"));
				bnList_3.setCellConfig(new CellConfig(new Path("this")));
			}
			return bnList_3;
		}

		private BnList getBnList_4() {
			if (bnList_4 == null) {
				bnList_4 = new BnList();
				bnList_4.setModelProvider(getLocalProvider());
				bnList_4.setPath(new Path("this.moneyList"));
				bnList_4.setCellConfig(new CellConfig(new Path("this")));
			}
			return bnList_4;
		}

		private JScrollPane getScrollPane_5() {
			if (scrollPane_5 == null) {
				scrollPane_5 = new JScrollPane();
				scrollPane_5.setViewportView(getBnList_5());
				bnList_5.setCellConfig(new CellConfig(new Path("this")));
			}
			return scrollPane_5;
		}

		private JScrollPane getScrollPane_6() {
			if (scrollPane_6 == null) {
				scrollPane_6 = new JScrollPane();
				scrollPane_6.setViewportView(getBnList_6());
				bnList_6.setCellConfig(new CellConfig(new Path("this")));
			}
			return scrollPane_6;
		}

		private BnList getBnList_5() {
			if (bnList_5 == null) {
				bnList_5 = new BnList();
				bnList_5.setModelProvider(getLocalProvider());
				bnList_5.setPath(new Path("this.numberList"));
				bnList_5.setCellConfig(new CellConfig(new Path("this")));
			}
			return bnList_5;
		}

		private BnList getBnList_6() {
			if (bnList_6 == null) {
				bnList_6 = new BnList();
				bnList_6.setModelProvider(getLocalProvider());
				bnList_6.setPath(new Path("this.iconList"));
				bnList_6.setCellConfig(new CellConfig(new Path("this")));
			}
			return bnList_6;
		}

		private BnButton getBnButton() {
			if (bnButton == null) {
				bnButton = new BnButton();
				bnButton.setPath(new org.beanfabrics.Path("this.addText"));
				bnButton.setModelProvider(getLocalProvider());
			}
			return bnButton;
		}

		private BnButton getBnButton_1() {
			if (bnButton_1 == null) {
				bnButton_1 = new BnButton();
				bnButton_1.setPath(new org.beanfabrics.Path("this.removeText"));
				bnButton_1.setModelProvider(getLocalProvider());
			}
			return bnButton_1;
		}

		private BnButton getBnButton_2() {
			if (bnButton_2 == null) {
				bnButton_2 = new BnButton();
				bnButton_2.setPath(new org.beanfabrics.Path("this.addBoolean"));
				bnButton_2.setModelProvider(getLocalProvider());
			}
			return bnButton_2;
		}

		private BnButton getBnButton_3() {
			if (bnButton_3 == null) {
				bnButton_3 = new BnButton();
				bnButton_3.setPath(new org.beanfabrics.Path("this.removeBoolean"));
				bnButton_3.setModelProvider(getLocalProvider());
			}
			return bnButton_3;
		}

		private BnButton getBnButton_4() {
			if (bnButton_4 == null) {
				bnButton_4 = new BnButton();
				bnButton_4.setPath(new org.beanfabrics.Path("this.addInteger"));
				bnButton_4.setModelProvider(getLocalProvider());
			}
			return bnButton_4;
		}

		private BnButton getBnButton_5() {
			if (bnButton_5 == null) {
				bnButton_5 = new BnButton();
				bnButton_5.setPath(new org.beanfabrics.Path("this.removeInteger"));
				bnButton_5.setModelProvider(getLocalProvider());
			}
			return bnButton_5;
		}

		private BnButton getBnButton_6() {
			if (bnButton_6 == null) {
				bnButton_6 = new BnButton();
				bnButton_6.setPath(new org.beanfabrics.Path("this.addDecimal"));
				bnButton_6.setModelProvider(getLocalProvider());
			}
			return bnButton_6;
		}

		private BnButton getBnButton_7() {
			if (bnButton_7 == null) {
				bnButton_7 = new BnButton();
				bnButton_7.setPath(new org.beanfabrics.Path("this.removeDecimal"));
				bnButton_7.setModelProvider(getLocalProvider());
			}
			return bnButton_7;
		}

		private BnButton getBnButton_8() {
			if (bnButton_8 == null) {
				bnButton_8 = new BnButton();
				bnButton_8.setPath(new org.beanfabrics.Path("this.addMoney"));
				bnButton_8.setModelProvider(getLocalProvider());
			}
			return bnButton_8;
		}

		private BnButton getBnButton_9() {
			if (bnButton_9 == null) {
				bnButton_9 = new BnButton();
				bnButton_9.setPath(new org.beanfabrics.Path("this.removeMoney"));
				bnButton_9.setModelProvider(getLocalProvider());
			}
			return bnButton_9;
		}

		private BnButton getBnButton_10() {
			if (bnButton_10 == null) {
				bnButton_10 = new BnButton();
				bnButton_10.setPath(new org.beanfabrics.Path("this.addNumber"));
				bnButton_10.setModelProvider(getLocalProvider());
			}
			return bnButton_10;
		}

		private BnButton getBnButton_11() {
			if (bnButton_11 == null) {
				bnButton_11 = new BnButton();
				bnButton_11.setPath(new org.beanfabrics.Path("this.removeNumber"));
				bnButton_11.setModelProvider(getLocalProvider());
			}
			return bnButton_11;
		}

		private BnButton getBnButton_12() {
			if (bnButton_12 == null) {
				bnButton_12 = new BnButton();
				bnButton_12.setPath(new org.beanfabrics.Path("this.addIcon"));
				bnButton_12.setModelProvider(getLocalProvider());
			}
			return bnButton_12;
		}

		private BnButton getBnButton_13() {
			if (bnButton_13 == null) {
				bnButton_13 = new BnButton();
				bnButton_13.setPath(new org.beanfabrics.Path("this.removeIcon"));
				bnButton_13.setModelProvider(getLocalProvider());
			}
			return bnButton_13;
		}
	}
}