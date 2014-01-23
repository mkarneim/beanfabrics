/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.list;

import java.awt.BorderLayout;
import java.beans.Customizer;

import javax.swing.JPanel;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.swing.customizer.CustomizerBasePanel;
import org.beanfabrics.swing.list.BnList;

/**
 * The <code>BnListCustomizer</code> is a JavaBeans {@link Customizer} for a {@link BnList}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BnListCustomizer<PM extends BnListCustomizerPM> extends CustomizerBasePanel<PM> {
    private ModelProvider localProvider;
    private JPanel centerPanel;
    private BnListCustomizerPanel bnListCustomizerPanel;

    protected BnListCustomizer(PM pm) {
        super(pm);
        getLocalProvider().setPresentationModel(pm);
        setLayout(new BorderLayout());
        add(getCenterPanel(), BorderLayout.CENTER);
    }

    public BnListCustomizer() {
        this((PM) new BnListCustomizerPM());
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
     * 
     * @wbp.nonvisual location=10,430
     * @return the local <code>ModelProvider</code>
     */
    protected ModelProvider getLocalProvider() {
        if (localProvider == null) {
            localProvider = new ModelProvider(); // @wb:location=10,430
            localProvider.setPresentationModelType(org.beanfabrics.swing.customizer.list.BnListCustomizerPM.class);
        }
        return localProvider;
    }

    private JPanel getCenterPanel() {
        if (centerPanel == null) {
            centerPanel = new JPanel();
            centerPanel.setBorder(null);
            centerPanel.setLayout(new BorderLayout(0, 0));
            centerPanel.add(getBnListCustomizerPanel(), BorderLayout.CENTER);
        }
        return centerPanel;
    }

    public BnListCustomizerPanel getBnListCustomizerPanel() {
        if (bnListCustomizerPanel == null) {
            bnListCustomizerPanel = new BnListCustomizerPanel();
            bnListCustomizerPanel.setPath(new Path("this"));
            bnListCustomizerPanel.setModelProvider(getLocalProvider());
        }
        return bnListCustomizerPanel;
    }
}