/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

import java.awt.BorderLayout;
import java.beans.Customizer;

import javax.swing.JPanel;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.swing.customizer.CustomizerBasePanel;
import org.beanfabrics.swing.table.BnTable;

/**
 * The <code>BnTableCustomizer</code> is a JavaBeans {@link Customizer} for a {@link BnTable}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BnTableCustomizer<PM extends BnTableCustomizerPM> extends CustomizerBasePanel<PM> {
    private ModelProvider localProvider;
    private JPanel centerPanel;
    private BnTableCustomizerPanel bnTableCustomizerPanel;

    protected BnTableCustomizer(PM pm) {
        super(pm);
        getLocalProvider().setPresentationModel(pm);
        setLayout(new BorderLayout());
        add(getCenterPanel(), BorderLayout.CENTER);
    }

    public BnTableCustomizer() {
        this((PM) new BnTableCustomizerPM());
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
     * 
     * @wbp.nonvisual location=16,357
     * @return the local <code>ModelProvider</code>
     */
    private ModelProvider getLocalProvider() {
        if (localProvider == null) {
            localProvider = new ModelProvider(); // @wb:location=16,577
        }
        return localProvider;
    }

    public JPanel getCenterPanel() {
        if (centerPanel == null) {
            centerPanel = new JPanel();
            centerPanel.setBorder(null);
            centerPanel.setLayout(new BorderLayout(0, 0));
            centerPanel.add(getBnTableCustomizerPanel(), BorderLayout.CENTER);
        }
        return centerPanel;
    }

    public BnTableCustomizerPanel getBnTableCustomizerPanel() {
        if (bnTableCustomizerPanel == null) {
            bnTableCustomizerPanel = new BnTableCustomizerPanel();
            bnTableCustomizerPanel.setPath(new Path("this"));
            bnTableCustomizerPanel.setModelProvider(getLocalProvider());
        }
        return bnTableCustomizerPanel;
    }
}