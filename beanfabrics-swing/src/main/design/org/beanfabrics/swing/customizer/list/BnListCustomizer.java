/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.list;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.Customizer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.customizer.path.PathPanel;
import org.beanfabrics.swing.customizer.util.CustomizerBasePanel;
import org.beanfabrics.swing.customizer.util.TitlePanel;
import org.beanfabrics.swing.list.BnList;
import org.beanfabrics.swing.list.CellConfig;

/**
 * The <code>BnListCustomizer</code> is a JavaBeans {@link Customizer} for a
 * {@link BnList}.
 *
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BnListCustomizer extends CustomizerBasePanel<BnListCustomizerPM> implements Customizer, View<BnListCustomizerPM> {
    private PathPanel pathPanel_1;
    private JPanel panel;
    private PathPanel pathPanel;
    private ModelProvider localProvider;
    private JLabel lblPathToPresentation;
    private JLabel lblPathToRow;
    private TitlePanel titlePanel;
    private BnListCustomizerPM bnListCustomizerPM;

    public BnListCustomizer() {
        setPresentationModel(getBnListCustomizerPM());
        setLayout(new BorderLayout());
        add(getPanel(), BorderLayout.CENTER);
        add(getTitlePanel(), BorderLayout.NORTH);
        //
    }

    @Override
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
        getPresentationModel().setFunctions(new BnListCustomizerPM.Functions() {
            @Override
			public void setPathToList(Path newValue) {
                Path oldValue = (bnList == null ? null : bnList.getPath());
                bnList.setPath(newValue);
                BnListCustomizer.this.firePropertyChange("path", oldValue, newValue);
            }

            @Override
			public void setCellConfig(CellConfig newValue) {
                CellConfig oldValue = (bnList == null ? null : bnList.getCellConfig());
                bnList.setCellConfig(newValue);
                BnListCustomizer.this.firePropertyChange("cellConfig", oldValue, newValue);
            }
        });
        getPresentationModel().setBnList(bnList);
    }

    @Override
	public BnListCustomizerPM getPresentationModel() {
        return getLocalProvider().getPresentationModel();
    }

    @Override
	public void setPresentationModel(BnListCustomizerPM pModel) {
        super.setPresentationModel(pModel);
        getLocalProvider().setPresentationModel(pModel);
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
            panel.setBorder(new EmptyBorder(4, 4, 4, 4));
            final GridBagLayout gridBagLayout = new GridBagLayout();
            gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0 };
            gridBagLayout.rowHeights = new int[] { 0, 7, 0 };
            panel.setLayout(gridBagLayout);
            GridBagConstraints gbc_lblPathToPresentation = new GridBagConstraints();
            gbc_lblPathToPresentation.insets = new Insets(4, 4, 5, 5);
            gbc_lblPathToPresentation.gridx = 0;
            gbc_lblPathToPresentation.gridy = 0;
            panel.add(getLblPathToPresentation(), gbc_lblPathToPresentation);
            final GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new Insets(4, 4, 5, 4);
            gridBagConstraints.weightx = 1;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridx = 1;
            panel.add(getPathPanel(), gridBagConstraints);
            GridBagConstraints gbc_lblPathToRow = new GridBagConstraints();
            gbc_lblPathToRow.anchor = GridBagConstraints.EAST;
            gbc_lblPathToRow.insets = new Insets(4, 4, 5, 5);
            gbc_lblPathToRow.gridx = 0;
            gbc_lblPathToRow.gridy = 1;
            panel.add(getLblPathToRow(), gbc_lblPathToRow);
            final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
            gridBagConstraints_3.weightx = 1;
            gridBagConstraints_3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints_3.insets = new Insets(4, 4, 5, 4);
            gridBagConstraints_3.gridy = 1;
            gridBagConstraints_3.gridx = 1;
            panel.add(getPathPanel_1(), gridBagConstraints_3);
        }
        return panel;
    }

    private PathPanel getPathPanel_1() {
        if (pathPanel_1 == null) {
            pathPanel_1 = new PathPanel();
            pathPanel_1.setPath(new org.beanfabrics.Path("this.pathToRowCell"));
            pathPanel_1.setModelProvider(getLocalProvider());
        }
        return pathPanel_1;
    }

    private JLabel getLblPathToPresentation() {
        if (lblPathToPresentation == null) {
            lblPathToPresentation = new JLabel("Path to Presentation Model");
        }
        return lblPathToPresentation;
    }

    private JLabel getLblPathToRow() {
        if (lblPathToRow == null) {
            lblPathToRow = new JLabel("Path to Row Model");
        }
        return lblPathToRow;
    }

    private TitlePanel getTitlePanel() {
        if (titlePanel == null) {
            titlePanel = new TitlePanel();
            titlePanel.setPath(new Path("this.title"));
            titlePanel.setModelProvider(getLocalProvider());
        }
        return titlePanel;
    }

    /**
     * @wbp.nonvisual location=1,481
     */
    private BnListCustomizerPM getBnListCustomizerPM() {
        if (bnListCustomizerPM == null) {
            bnListCustomizerPM = new BnListCustomizerPM();
        }
        return bnListCustomizerPM;
    }
}