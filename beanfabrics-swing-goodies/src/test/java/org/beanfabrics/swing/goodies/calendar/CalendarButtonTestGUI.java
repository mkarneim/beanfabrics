/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.goodies.calendar;

import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * @author Michael Karneim
 */
public class CalendarButtonTestGUI {
    /**
     * This method invokes a simple test program that demonstrates the calendar
     * button.
     */
    public static void main(String[] args) {
        System.out.println("test program for CalendarButton");

        JTextField dateField = new JTextField("14.01.1971");
        CalendarButton dateButton = new CalendarButton();
        dateButton.setText("...");
        dateButton.setTextComponent(dateField);
        dateButton.setPreferredSize(new java.awt.Dimension(20, dateField.getPreferredSize().height));
        JFrame frame1 = new JFrame();
        frame1.getContentPane().setLayout(new java.awt.FlowLayout(0));
        frame1.getContentPane().add(dateField);
        frame1.getContentPane().add(dateButton);
        frame1.pack();
        frame1.setVisible(true);
    }
}
