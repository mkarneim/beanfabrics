/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.table;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class SwingTableTestGUI extends JFrame {
    private JTable table;
    private JScrollPane scrollPane;
    private JPanel panel;

    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        try {
            SwingTableTestGUI frame = new SwingTableTestGUI();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the frame
     */
    public SwingTableTestGUI() {
        super();
        setBounds(100, 100, 500, 375);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(getPanel(), BorderLayout.CENTER);
        //
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(getScrollPane(), BorderLayout.CENTER);
        }
        return panel;
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(getTable());
        }
        return scrollPane;
    }

    private JTable getTable() {
        if (table == null) {
            Object[] columnNames = new Object[] { "One", "Two" };
            Object[][] rowData = new Object[10][];
            for (int i = 0; i < rowData.length; ++i) {
                rowData[i] = new Object[] { "a " + i, "b " + i };
            }
            table = new JTable(rowData, columnNames);
        }
        return table;
    }
}