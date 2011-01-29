/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.goodies.calendar;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * @author Michael Karneim
 */
public class CalendarChooserTestGUI {
    /**
     * This method invokes a simple test program that demonstrates the calendar
     * bean.
     */
    public static void main(String[] args)
        throws Exception {
        System.out.println("test program for CalendarBean");
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        // UIManager.setLookAndFeel(UIManager
        // .getSystemLookAndFeelClassName());

        Locale locale = Locale.GERMAN;

        final CalendarChooser cal = new CalendarChooser(new Date(), locale);
        cal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.out.println(cal.getSelectedDate());
            }
        });
        cal.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(CalendarChooser.SELECTEDDATE_PROPERTYNAME)) {
                    DateFormat format = DateFormat.getInstance();
                    System.out.println("Selected Date changed: " + format.format((Date)evt.getOldValue()) + " -> " + format.format((Date)evt.getNewValue()));
                }
            }
        });
        cal.setNumberOfPreviousVisibleMonths(3);
        cal.setNumberOfSubsequentVisibleMonths(3);

        final JFrame frame2 = new JFrame();
        frame2.getContentPane().setLayout(new BorderLayout());
        frame2.getContentPane().add("Center", cal);
        JButton change = new JButton("Change xxx");
        change.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cal.setDayFont(new Font("Helvetica", Font.PLAIN, 22));
                cal.setDateFont(new Font("Helvetica", Font.PLAIN, 22));
                cal.setHeaderFont(new Font("Helvetica", Font.PLAIN, 22));
                cal.setRollButtonSize(new java.awt.Dimension(20, 20));
                frame2.pack();
            }
        });
        frame2.getContentPane().add("South", change);
        frame2.pack();
        frame2.setVisible(true);
    }
}
