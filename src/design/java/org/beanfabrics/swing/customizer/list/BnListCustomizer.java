/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.list;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.Customizer;

import javax.swing.JPanel;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.customizer.path.PathPanel;
import org.beanfabrics.swing.customizer.util.CustomizerBasePanel;
import org.beanfabrics.swing.customizer.util.SeparatorLabel;
import org.beanfabrics.swing.list.BnList;
import org.beanfabrics.swing.list.CellConfig;

/**
 * The <code>BnListCustomizer</code> is a JavaBeans {@link Customizer} for a
 * {@link BnList}.
 *
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BnListCustomizer extends CustomizerBasePanel implements Customizer, View<BnListCustomizerPM> {
    private PathPanel pathPanel_1;
    private SeparatorLabel separatorLabel_1;
    private SeparatorLabel separatorLabel;
    private JPanel panel;
    private PathPanel pathPanel;
    private ModelProvider localProvider;
    private BnListCustomizerPM pModel;

    public BnListCustomizer() {
        this.pModel = new BnListCustomizerPM();
        getLocalProvider().setPresentationModel(this.pModel);
        setLayout(new BorderLayout());
        add(getPanel(), BorderLayout.CENTER);
        //
    }

    public void setObject(Object bean) {
        if (bean == null || bean instanceof BnList) {
            try {
                setBnList((BnList)bean);
            } catch (Throwable t) {
                showException(t);
            }
        } else {
            showMessage("Can't customize this instance of " + bean.getClass().getName() + ". Expected instance of " + BnList.class.getName());
        }
    }

    public void setBnList(final BnList bnList) {
        this.pModel.setFunctions(new BnListCustomizerPM.Functions() {
            public void setPathToList(Path newValue) {
                Path oldValue = bnList.getPath();
                bnList.setPath(newValue);
                BnListCustomizer.this.firePropertyChange("path", oldValue, newValue);
            }

            public void setCellConfig(CellConfig newValue) {
                CellConfig oldValue = bnList.getCellConfig();
                bnList.setCellConfig(newValue);
                BnListCustomizer.this.firePropertyChange("cellConfig", oldValue, newValue);
            }
        });
        this.pModel.setBnList(bnList);
    }

    public BnListCustomizerPM getPresentationModel() {
        return getLocalProvider().getPresentationModel();
    }

    public void setPresentationModel(BnListCustomizerPM pModel) {
        getLocalProvider().setPresentationModel(pModel);
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
     *
     * @return the local <code>ModelProvider</code>
     */
    protected ModelProvider getLocalProvider() {
        if (localProvider == null) {
            localProvider = new ModelProvider(); // @wb:location=10,430
            localProvider.setPresentationModelType(org.beanfabrics.swing.customizer.list.BnListCustomizerPM.class);
        }
        return localProvider;
    }

    private PathPanel getPathPanel() {
        if (pathPanel == null) {
            pathPanel = new PathPanel();
            pathPanel.setPath(new org.beanfabrics.Path("this.pathToList"));
            pathPanel.setModelProvider(getLocalProvider());
        }
        return pathPanel;
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            final GridBagLayout gridBagLayout = new GridBagLayout();
            gridBagLayout.rowHeights = new int[] { 7, 0, 7, 7 };
            panel.setLayout(gridBagLayout);
            final GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.weightx = 1;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new Insets(4, 4, 4, 4);
            final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
            gridBagConstraints_1.weightx = 1;
            gridBagConstraints_1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints_1.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_1.gridx = 0;
            gridBagConstraints_1.gridy = 0;
            panel.add(getSeparatorLabel(), gridBagConstraints_1);
            panel.add(getPathPanel(), gridBagConstraints);
            final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
            gridBagConstraints_2.weightx = 1;
            gridBagConstraints_2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints_2.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_2.gridy = 2;
            gridBagConstraints_2.gridx = 0;
            panel.add(getSeparatorLabel_1(), gridBagConstraints_2);
            final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
            gridBagConstraints_3.weightx = 1;
            gridBagConstraints_3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints_3.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_3.gridy = 3;
            gridBagConstraints_3.gridx = 0;
            panel.add(getPathPanel_1(), gridBagConstraints_3);
        }
        return panel;
    }

    private SeparatorLabel getSeparatorLabel() {
        if (separatorLabel == null) {
            separatorLabel = new SeparatorLabel();
            separatorLabel.setText("Choose path to IListPM");
        }
        return separatorLabel;
    }

    private SeparatorLabel getSeparatorLabel_1() {
        if (separatorLabel_1 == null) {
            separatorLabel_1 = new SeparatorLabel();
            separatorLabel_1.setText("Choose path to visible row model");
        }
        return separatorLabel_1;
    }

    private PathPanel getPathPanel_1() {
        if (pathPanel_1 == null) {
            pathPanel_1 = new PathPanel();
            pathPanel_1.setPath(new org.beanfabrics.Path("this.pathToRowCell"));
            pathPanel_1.setModelProvider(getLocalProvider());
        }
        return pathPanel_1;
    }
}