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
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.swing.customizer.util.CustomizerBasePanel;
import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.swing.table.BnTable;

/**
 * The <code>BnTableCustomizer</code> is a JavaBeans {@link Customizer} for a {@link BnTable}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BnTableCustomizer extends CustomizerBasePanel<BnTableCustomizerPM> implements Customizer {
    private JPanel centerPanel;
    private ModelProvider localProvider;
    private BnTableCustomizerPM bnTableCustomizerPM;
    private BnTableCustomizerPanel bnTableCustomizerPanel;

    public BnTableCustomizer() {
        setPresentationModel(getBnTableCustomizerPM());
        setLayout(new BorderLayout());
        add(getCenterPanel(), BorderLayout.CENTER);
    }

    @Override
    public void setObject(Object bean) {
        if (bean == null || bean instanceof BnTable) {
            try {
                setBnTable((BnTable) bean);
            } catch (Throwable t) {
                showException(t);
            }
        } else {
            showMessage("Can't customize this instance of " + bean.getClass().getName() + ". Expected instance of "
                    + ModelSubscriber.class.getName());
        }
    }

    public void setBnTable(final BnTable bnTable) {
        getBnTableCustomizerPM().setFunctions(new BnTableCustomizerPM.Functions() {
            @Override
            public void setPath(Path newValue) {
                Path oldValue = (bnTable == null ? null : bnTable.getPath());
                bnTable.setPath(newValue);
                BnTableCustomizer.this.firePropertyChange("path", oldValue, newValue);
            }

            @Override
            public void setBnColumns(BnColumn[] newValue) {
                BnColumn[] oldValue = (bnTable == null ? null : bnTable.getColumns());
                bnTable.setColumns(newValue);
                BnTableCustomizer.this.firePropertyChange("columns", oldValue, newValue);
            }
        });
        getBnTableCustomizerPM().setBnTable(bnTable);
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
        if (bnTableCustomizerPM == null) {
            bnTableCustomizerPM = new BnTableCustomizerPM();
        }
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