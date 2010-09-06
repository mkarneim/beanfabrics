/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.goodies.calendar;

import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.DatePM;
import org.beanfabrics.swing.BnTextField;

/**
 * @author Max Gensthaler
 */
public class BnCalendarChooserButtonTestGUI {
    public static void main(String[] args) {
        Locale.setDefault(Locale.UK);

        JFrame f = new JFrame();
        JPanel pnl = new JPanel();
        f.getContentPane().add(pnl);
        BnCalendarChooserButton btn = new BnCalendarChooserButton();
        BnTextField tf = new BnTextField();
        tf.setColumns(10);

        pnl.add(tf);
        pnl.add(btn);
        f.setSize(300, 300);
        f.setLocationRelativeTo(null);

        DatePM cell = new DatePM();

        ModelProvider provider = new ModelProvider(cell);
        btn.setModelProvider(provider);
        btn.setPath(new Path("this"));
        tf.setModelProvider(provider);
        tf.setPath(new Path("this"));

        f.setVisible(true);
    }
}
