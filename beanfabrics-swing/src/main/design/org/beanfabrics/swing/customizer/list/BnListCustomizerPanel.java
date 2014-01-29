/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.list;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.customizer.path.PathPanel;
import org.beanfabrics.swing.customizer.util.TitlePanel;

/**
 * The {@link BnListCustomizerPanel} is a {@link View} on a {@link BnListCustomizerPM}.
 * 
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
@SuppressWarnings("serial")
public class BnListCustomizerPanel extends JPanel implements View<BnListCustomizerPM>, ModelSubscriber {
    private final Link link = new Link(this);
    private ModelProvider localModelProvider;

    private PathPanel pathPanel_1;
    private JPanel contentPanel;
    private PathPanel pathPanel;
    private JLabel lblPathToPresentation;
    private JLabel lblPathToRow;
    private TitlePanel titlePanel;
    private BnListCustomizerPM bnListCustomizerPM;

    public BnListCustomizerPanel() {
        setPresentationModel(getBnListCustomizerPM());
        setLayout(new BorderLayout());
        add(getContentPanel(), BorderLayout.CENTER);
        add(getTitlePanel(), BorderLayout.NORTH);
        //
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
     * 
     * @return the local <code>ModelProvider</code>
     * @wbp.nonvisual location=10,430
     */
    protected ModelProvider getLocalModelProvider() {
        if (localModelProvider == null) {
            localModelProvider = new ModelProvider(); // @wb:location=10,430
            localModelProvider.setPresentationModelType(BnListCustomizerPM.class);
        }
        return localModelProvider;
    }

    /** {@inheritDoc} */
    public BnListCustomizerPM getPresentationModel() {
        return getLocalModelProvider().getPresentationModel();
    }

    /** {@inheritDoc} */
    public void setPresentationModel(BnListCustomizerPM pModel) {
        getLocalModelProvider().setPresentationModel(pModel);
    }

    /** {@inheritDoc} */
    public IModelProvider getModelProvider() {
        return this.link.getModelProvider();
    }

    /** {@inheritDoc} */
    public void setModelProvider(IModelProvider modelProvider) {
        this.link.setModelProvider(modelProvider);
    }

    /** {@inheritDoc} */
    public Path getPath() {
        return this.link.getPath();
    }

    /** {@inheritDoc} */
    public void setPath(Path path) {
        this.link.setPath(path);
    }

    private PathPanel getPathPanel() {
        if (pathPanel == null) {
            pathPanel = new PathPanel();
            pathPanel.setPath(new org.beanfabrics.Path("this.pathToList"));
            pathPanel.setModelProvider(getLocalModelProvider());
        }
        return pathPanel;
    }

    public JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            contentPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
            final GridBagLayout gbl_contentPanel = new GridBagLayout();
            gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 1.0 };
            gbl_contentPanel.rowHeights = new int[] { 0, 7, 0 };
            contentPanel.setLayout(gbl_contentPanel);
            GridBagConstraints gbc_lblPathToPresentation = new GridBagConstraints();
            gbc_lblPathToPresentation.insets = new Insets(4, 4, 5, 5);
            gbc_lblPathToPresentation.gridx = 0;
            gbc_lblPathToPresentation.gridy = 0;
            contentPanel.add(getLblPathToPresentation(), gbc_lblPathToPresentation);
            final GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new Insets(4, 4, 5, 4);
            gridBagConstraints.weightx = 1;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridx = 1;
            contentPanel.add(getPathPanel(), gridBagConstraints);
            GridBagConstraints gbc_lblPathToRow = new GridBagConstraints();
            gbc_lblPathToRow.anchor = GridBagConstraints.EAST;
            gbc_lblPathToRow.insets = new Insets(4, 4, 5, 5);
            gbc_lblPathToRow.gridx = 0;
            gbc_lblPathToRow.gridy = 1;
            contentPanel.add(getLblPathToRow(), gbc_lblPathToRow);
            final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
            gridBagConstraints_3.weightx = 1;
            gridBagConstraints_3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints_3.insets = new Insets(4, 4, 5, 4);
            gridBagConstraints_3.gridy = 1;
            gridBagConstraints_3.gridx = 1;
            contentPanel.add(getPathPanel_1(), gridBagConstraints_3);
        }
        return contentPanel;
    }

    private PathPanel getPathPanel_1() {
        if (pathPanel_1 == null) {
            pathPanel_1 = new PathPanel();
            pathPanel_1.setPath(new org.beanfabrics.Path("this.cellConfigPath"));
            pathPanel_1.setModelProvider(getLocalModelProvider());
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
            titlePanel.setModelProvider(getLocalModelProvider());
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