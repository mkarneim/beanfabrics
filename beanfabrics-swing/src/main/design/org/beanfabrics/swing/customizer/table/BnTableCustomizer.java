/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

import java.awt.BorderLayout;
import java.beans.Customizer;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.swing.customizer.util.CustomizerBasePanel;
import org.beanfabrics.swing.table.BnTable;

/**
 * The <code>BnTableCustomizer</code> is a JavaBeans {@link Customizer} for a {@link BnTable}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BnTableCustomizer extends CustomizerBasePanel<BnTableCustomizerPM> implements CustomizerBase {
    private JPanel centerPanel;
    private ModelProvider localProvider;
    private BnTableCustomizerPM bnTableCustomizerPM;
    private BnTableCustomizerPanel bnTableCustomizerPanel;

    private Object bean;

    protected BnTableCustomizer(BnTableCustomizerPM pm) {
        setPresentationModel(pm);
        this.bnTableCustomizerPM = pm;
        setLayout(new BorderLayout());
        add(getCenterPanel(), BorderLayout.CENTER);
    }

    public BnTableCustomizer() {
        this(new BnTableCustomizerPM());
    }

    @Override
    public void setObject(Object aBean) {
        this.bean = aBean;
        try {
            getBnTableCustomizerPM().setCustomizer(this);
        } catch (Throwable t) {
            showException(t);
        }
    }

    @Override
    public Object getObject() {
        return this.bean;
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
     * 
     * @wbp.nonvisual location=16,357
     * @return the local <code>ModelProvider</code>
     */
    protected ModelProvider getLocalProvider() {
        if (localProvider == null) {
            localProvider = new ModelProvider(); // @wb:location=16,577
            localProvider.setPresentationModel(getBnTableCustomizerPM());
            localProvider.setPresentationModelType(BnTableCustomizerPM.class);
        }
        return localProvider;
    }

    public JPanel getCenterPanel() {
        if (centerPanel == null) {
            centerPanel = new JPanel();
            centerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            centerPanel.setLayout(new BorderLayout(0, 0));
            centerPanel.add(getBnTableCustomizerPanel(), BorderLayout.CENTER);
        }
        return centerPanel;
    }

    /**
     * @wbp.nonvisual location=16,411
     */
    private BnTableCustomizerPM getBnTableCustomizerPM() {
        return bnTableCustomizerPM;
    }

    protected BnTableCustomizerPanel getBnTableCustomizerPanel() {
        if (bnTableCustomizerPanel == null) {
            bnTableCustomizerPanel = new BnTableCustomizerPanel();
            bnTableCustomizerPanel.setPath(new Path("this"));
            bnTableCustomizerPanel.setModelProvider(getLocalProvider());
        }
        return bnTableCustomizerPanel;
    }
}