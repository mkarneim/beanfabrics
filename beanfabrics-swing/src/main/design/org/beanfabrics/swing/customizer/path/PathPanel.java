/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.path;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.BnTextField;

/**
 * The <code>PathPanel</code> is a view on a {@link PathPM}.
 * 
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
@SuppressWarnings("serial")
public class PathPanel extends JPanel implements View<org.beanfabrics.swing.customizer.path.PathPM>, ModelSubscriber {
    private BnButton bnButton;
    private BnTextField bnTextField;
    private JPanel panel;
    private final Link link = new Link(this);
    private ModelProvider localProvider;

    /**
     * Constructs a new <code>PathPanel</code>.
     */
    public PathPanel() {
        super();
        setLayout(new BorderLayout());
        add(getPanel(), BorderLayout.CENTER);
        //
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
     * 
     * @return the local <code>ModelProvider</code>
     */
    protected ModelProvider getLocalProvider() {
        if (localProvider == null) {
            localProvider = new ModelProvider(); // @wb:location=10,430
            localProvider.setPresentationModelType(org.beanfabrics.swing.customizer.path.PathPM.class);
        }
        return localProvider;
    }

    /** {@inheritDoc} */
    public org.beanfabrics.swing.customizer.path.PathPM getPresentationModel() {
        return getLocalProvider().getPresentationModel();
    }

    /** {@inheritDoc} */
    public void setPresentationModel(org.beanfabrics.swing.customizer.path.PathPM pModel) {
        getLocalProvider().setPresentationModel(pModel);
    }

    /** {@inheritDoc} */
    public IModelProvider getModelProvider() {
        return this.link.getModelProvider();
    }

    /** {@inheritDoc} */
    public void setModelProvider(IModelProvider provider) {
        this.link.setModelProvider(provider);
    }

    /** {@inheritDoc} */
    public Path getPath() {
        return this.link.getPath();
    }

    /** {@inheritDoc} */
    public void setPath(Path path) {
        this.link.setPath(path);
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            final GridBagLayout gridBagLayout = new GridBagLayout();
            gridBagLayout.columnWidths = new int[] { 0, 7 };
            panel.setLayout(gridBagLayout);
            final GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 1;
            gridBagConstraints.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridx = 0;
            panel.add(getBnTextField(), gridBagConstraints);
            final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
            gridBagConstraints_1.insets = new Insets(0, 4, 0, 0);
            gridBagConstraints_1.gridy = 0;
            gridBagConstraints_1.gridx = 1;
            panel.add(getBnButton(), gridBagConstraints_1);
            panel.setOpaque(false);
        }
        return panel;
    }

    private BnTextField getBnTextField() {
        if (bnTextField == null) {
            bnTextField = new BnTextField();
            bnTextField.setPath(new org.beanfabrics.Path("this"));
            bnTextField.setModelProvider(getLocalProvider());
            bnTextField.setColumns(10);
        }
        return bnTextField;
    }

    private BnButton getBnButton() {
        if (bnButton == null) {
            bnButton = new BnButton();
            bnButton.setPath(new org.beanfabrics.Path("this.choosePath"));
            bnButton.setModelProvider(getLocalProvider());
            bnButton.setText("Browse ...");
            setTexturedButtonType(bnButton);
        }
        return bnButton;
    }

    private static void setTexturedButtonType(AbstractButton button) {
        // see http://developer.apple.com/technotes/tn2007/tn2196.html
        button.putClientProperty("JButton.buttonType", "gradient");
    }
}