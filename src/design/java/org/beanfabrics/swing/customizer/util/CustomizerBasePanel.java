/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * The <code>CustomizerBasePanel</code> is the base class for JavaBeans
 * Customizers in Beanfabrics. It provides methods for showing messages on the
 * customizer's panel.
 * 
 * @author Michael Karneim
 */
public class CustomizerBasePanel extends JPanel {
    public CustomizerBasePanel() {
    }

    protected void showMessage(String message) {
        removeAll();
        JTextArea ta = new JTextArea(message);
        ta.setForeground(Color.red);
        ta.setEditable(false);
        JScrollPane scroll = new JScrollPane(ta);
        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
    }

    protected void showException(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("Caught " + t.getClass().getName() + ": '" + t.getMessage() + "'");
        t.printStackTrace(pw);
        showMessage(sw.toString());
    }
}