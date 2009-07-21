/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.util;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnLabel;

/**
 * @author Michael Karneim
 */
public class TitlePanel extends JPanel implements View<TitlePM>, ModelSubscriber {
    private final Link link = new Link(this);
    private BnLabel bnLabel;
    private ModelProvider localProvider;

    /**
     * Create the panel
     */
    public TitlePanel() {
        super();
        setBackground(Color.WHITE);
        final GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 7 };
        setLayout(gridBagLayout);
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(4, 4, 4, 4);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(getBnLabel(), gridBagConstraints);
        //
    }

    protected ModelProvider getLocalProvider() {
        if (localProvider == null) {
            localProvider = new ModelProvider(); // @wb:location=87,273
            localProvider.setPresentationModelType(TitlePM.class);
        }
        return localProvider;
    }

    /** {@inheritDoc} */
    public TitlePM getPresentationModel() {
        return getLocalProvider().getPresentationModel();
    }

    /** {@inheritDoc} */
    public void setPresentationModel(TitlePM pModel) {
        getLocalProvider().setPresentationModel(pModel);
    }

    /** {@inheritDoc} */
    public IModelProvider getModelProvider() {
        return link.getModelProvider();
    }

    /** {@inheritDoc} */
    public void setModelProvider(IModelProvider provider) {
        this.link.setModelProvider(provider);
    }

    /** {@inheritDoc} */
    public Path getPath() {
        return link.getPath();
    }

    /** {@inheritDoc} */
    public void setPath(Path path) {
        this.link.setPath(path);
    }

    protected BnLabel getBnLabel() {
        if (bnLabel == null) {
            bnLabel = new BnLabel();
            bnLabel.setPath(new Path("this"));
            bnLabel.setModelProvider(getLocalProvider());
        }
        return bnLabel;
    }
}