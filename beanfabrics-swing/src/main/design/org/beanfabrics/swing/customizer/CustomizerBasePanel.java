/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.beanfabrics.swing.customizer.util.CustomizerUtil;
import org.beanfabrics.swing.customizer.util.RootWindowLocator;

/**
 * The <code>CustomizerBasePanel</code> is the base class for JavaBeans Customizers in Beanfabrics. It provides methods
 * for showing messages on the customizer's panel.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class CustomizerBasePanel<PM extends CustomizerPM> extends JPanel implements CustomizerBase {
    private Object bean;
    private PM pModel;

    public CustomizerBasePanel(PM aPm) {
        this.pModel = aPm;
        this.pModel.getContext().addService(RootWindowLocator.class, CustomizerUtil.getRootWindowLocator(this));
    }

    @Override
    public void setObject(Object bean) {
        this.bean = bean;
        try {
            this.pModel.setCustomizer(this);
        } catch (Throwable t) {
            showException(t);
        }
    }

    @Override
    public Object getObject() {
        return bean;
    }

    public void showMessage(String message) {
        removeAll();
        JTextArea ta = new JTextArea(message);
        ta.setForeground(Color.red);
        ta.setEditable(false);
        JScrollPane scroll = new JScrollPane(ta);
        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
    }

    public void showException(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("Caught " + t.getClass().getName() + ": '" + t.getMessage() + "'");
        t.printStackTrace(pw);
        showMessage(sw.toString());
    }

    @Override
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

}