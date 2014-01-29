/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer;

import java.awt.BorderLayout;
import java.awt.Color;
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
import org.beanfabrics.swing.customizer.util.CustomizerUtil;
import org.beanfabrics.swing.customizer.util.TitlePanel;

/**
 * The {@link ModelSubscriberCustomizerPanel} is a {@link View} on a {@link ModelSubscriberCustomizerPM}.
 * 
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
@SuppressWarnings("serial")
public class ModelSubscriberCustomizerPanel extends JPanel implements View<ModelSubscriberCustomizerPM>,
        ModelSubscriber {
    private final Link link = new Link(this);
    private ModelProvider localModelProvider;
    private JPanel contentPanel;
    private ModelSubscriberCustomizerPM modelSubscriberCustomizerPM;
    private JLabel lblPathToPresentation;
    private PathPanel pathPanel;
    private TitlePanel titlePanel;

    public ModelSubscriberCustomizerPanel() {
        setPresentationModel(getModelSubscriberCustomizerPM());
        setLayout(new BorderLayout());
        this.add(getContentPanel());
        setBackground(getDefaultBackground());

        add(getTitlePanel(), BorderLayout.NORTH);
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
            localModelProvider.setPresentationModelType(ModelSubscriberCustomizerPM.class);
        }
        return localModelProvider;
    }

    /** {@inheritDoc} */
    public ModelSubscriberCustomizerPM getPresentationModel() {
        return getLocalModelProvider().getPresentationModel();
    }

    /** {@inheritDoc} */
    public void setPresentationModel(ModelSubscriberCustomizerPM pModel) {
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

    public JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            contentPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
            contentPanel.setOpaque(false);
            GridBagLayout gbl_contentPanel = new GridBagLayout();
            gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
            gbl_contentPanel.rowHeights = new int[] { 0, 0, 0 };
            gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
            gbl_contentPanel.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
            contentPanel.setLayout(gbl_contentPanel);
            GridBagConstraints gbc_lblPathToPresentation = new GridBagConstraints();
            gbc_lblPathToPresentation.insets = new Insets(0, 0, 5, 5);
            gbc_lblPathToPresentation.gridx = 0;
            gbc_lblPathToPresentation.gridy = 0;
            contentPanel.add(getLblPathToPresentation(), gbc_lblPathToPresentation);
            GridBagConstraints gbc_pathPanel = new GridBagConstraints();
            gbc_pathPanel.insets = new Insets(0, 0, 5, 0);
            gbc_pathPanel.fill = GridBagConstraints.BOTH;
            gbc_pathPanel.gridx = 1;
            gbc_pathPanel.gridy = 0;
            contentPanel.add(getPathPanel(), gbc_pathPanel);
        }
        return contentPanel;
    }

    private Color getDefaultBackground() {
        if (CustomizerUtil.isAquaLookAndFeel()) {
            return new Color(232, 232, 232);
        } else {
            return getBackground();
        }
    }

    /**
     * @wbp.nonvisual location=-4,481
     */
    private ModelSubscriberCustomizerPM getModelSubscriberCustomizerPM() {
        if (modelSubscriberCustomizerPM == null) {
            modelSubscriberCustomizerPM = new ModelSubscriberCustomizerPM();
        }
        return modelSubscriberCustomizerPM;
    }

    private JLabel getLblPathToPresentation() {
        if (lblPathToPresentation == null) {
            lblPathToPresentation = new JLabel("Path to Presentation Model");
        }
        return lblPathToPresentation;
    }

    private PathPanel getPathPanel() {
        if (pathPanel == null) {
            pathPanel = new PathPanel();
            pathPanel.setPath(new Path("this.path"));
            pathPanel.setModelProvider(getLocalModelProvider());
        }
        return pathPanel;
    }

    private TitlePanel getTitlePanel() {
        if (titlePanel == null) {
            titlePanel = new TitlePanel();
            titlePanel.setPath(new Path("this.title"));
            titlePanel.setModelProvider(getLocalModelProvider());
        }
        return titlePanel;
    }
}