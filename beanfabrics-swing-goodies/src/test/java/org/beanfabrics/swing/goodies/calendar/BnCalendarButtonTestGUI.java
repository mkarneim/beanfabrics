/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.goodies.calendar;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.model.DatePM;
import org.beanfabrics.swing.BnTextField;

public class BnCalendarButtonTestGUI extends JFrame {
    private DatePM dateCell;
    private ModelProvider provider;
    private JSeparator separator;
    private CalendarChooser calendarChooser;
    private JLabel insertADateLabel;
    private BnCalendarChooserButton bnCalendarChooserButton;
    private BnTextField bnTextField;
    private JPanel panel;

    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        try {
            //			try {
            //				UIManager
            //						.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            //			} catch (ClassNotFoundException ex) {
            //				ex.printStackTrace();
            //				UIManager.setLookAndFeel(UIManager
            //						.getSystemLookAndFeelClassName());
            //			}

            //			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            BnCalendarButtonTestGUI frame = new BnCalendarButtonTestGUI();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the frame
     */
    public BnCalendarButtonTestGUI() {
        super();
        setBounds(100, 100, 500, 375);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(getPanel(), BorderLayout.CENTER);
        //
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            final GridBagLayout gridBagLayout = new GridBagLayout();
            gridBagLayout.rowHeights = new int[] { 7, 7, 7, 0 };
            gridBagLayout.columnWidths = new int[] { 7, 0, 7 };
            panel.setLayout(gridBagLayout);
            final GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints.gridy = 2;
            gridBagConstraints.gridx = 1;
            final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
            gridBagConstraints_2.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_2.gridy = 2;
            gridBagConstraints_2.gridx = 0;
            final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
            gridBagConstraints_4.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_4.gridx = 1;
            gridBagConstraints_4.gridy = 0;
            panel.add(getCalendarChooser(), gridBagConstraints_4);
            final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
            gridBagConstraints_5.fill = GridBagConstraints.BOTH;
            gridBagConstraints_5.weightx = 1;
            gridBagConstraints_5.gridwidth = 3;
            gridBagConstraints_5.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_5.gridy = 1;
            gridBagConstraints_5.gridx = 0;
            panel.add(getSeparator(), gridBagConstraints_5);
            panel.add(getInsertADateLabel(), gridBagConstraints_2);
            panel.add(getBnTextField(), gridBagConstraints);
            final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
            gridBagConstraints_1.anchor = GridBagConstraints.WEST;
            gridBagConstraints_1.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_1.gridy = 2;
            gridBagConstraints_1.gridx = 2;
            panel.add(getBnCalendarChooserButton(), gridBagConstraints_1);
        }
        return panel;
    }

    private BnTextField getBnTextField() {
        if (bnTextField == null) {
            bnTextField = new BnTextField();
            bnTextField.setPath(new org.beanfabrics.Path("this"));
            bnTextField.setModelProvider(getLocalProvider());
            bnTextField.setColumns(10);
        }
        return bnTextField;
    }

    private BnCalendarChooserButton getBnCalendarChooserButton() {
        if (bnCalendarChooserButton == null) {
            bnCalendarChooserButton = new BnCalendarChooserButton();
            bnCalendarChooserButton.setPath(new org.beanfabrics.Path("this"));
            bnCalendarChooserButton.setModelProvider(getLocalProvider());
            //			bnCalendarChooserButton.getCalendarChooser().setNumberOfPreviousVisibleMonths(3);
        }
        return bnCalendarChooserButton;
    }

    private DatePM getDateCell() {
        if (dateCell == null) {
            dateCell = new DatePM(); // @wb:location=68,430
        }
        return dateCell;
    }

    private ModelProvider getLocalProvider() {
        if (provider == null) {
            provider = new ModelProvider(); // @wb:location=165,415
            provider.setPresentationModelType(DatePM.class);
            provider.setPresentationModel(getDateCell());
        }
        return provider;
    }

    private JLabel getInsertADateLabel() {
        if (insertADateLabel == null) {
            insertADateLabel = new JLabel();
            insertADateLabel.setText("Insert a date");
        }
        return insertADateLabel;
    }

    private CalendarChooser getCalendarChooser() {
        if (calendarChooser == null) {
            calendarChooser = new CalendarChooser();
        }
        return calendarChooser;
    }

    private JSeparator getSeparator() {
        if (separator == null) {
            separator = new JSeparator();
        }
        return separator;
    }
}
