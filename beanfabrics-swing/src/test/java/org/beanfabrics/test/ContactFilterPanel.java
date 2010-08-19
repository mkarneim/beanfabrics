/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.BnTextField;

public class ContactFilterPanel extends JPanel implements View<ContactFilterPM>, ModelSubscriber {
    private final Link link = new Link(this);
    private JLabel jLabel = null;
    private BnTextField bnTextField = null;
    private BnButton bnButton = null;
    private ModelProvider myDataSource = null; //@jve:decl-index=0:visual-constraint="397,50"

    /**
     * This is the default constructor
     */
    public ContactFilterPanel() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        jLabel = new JLabel();
        jLabel.setText("Filter");
        this.setSize(300, 200);
        this.setLayout(new FlowLayout());
        this.add(jLabel, null);
        this.add(getBnTextField(), null);
        this.add(getBnButton(), null);
    }

    /**
     * This method initializes bnTextField
     * 
     * @return org.beanfabrics.gui.swing.BnTextField
     */
    private BnTextField getBnTextField() {
        if (bnTextField == null) {
            bnTextField = new BnTextField();
            bnTextField.setColumns(8);
            bnTextField.setPath(new Path("searchString"));
            bnTextField.setModelProvider(getMyDataSource());
        }
        return bnTextField;
    }

    /**
     * This method initializes bnButton
     * 
     * @return org.beanfabrics.gui.swing.BnButton
     */
    private BnButton getBnButton() {
        if (bnButton == null) {
            bnButton = new BnButton();
            bnButton.setPath(new Path("filter"));
            bnButton.setText("Filter");
            bnButton.setModelProvider(getMyDataSource());
        }
        return bnButton;
    }

    /**
     * This method initializes <code>myDataSource</code>.
     * 
     * @return the <code>ModelProvider</code>
     */
    private ModelProvider getMyDataSource() {
        if (myDataSource == null) {
            myDataSource = new ModelProvider();
            myDataSource.setPresentationModelType(ContactFilterPM.class);
        }
        return myDataSource;
    }

    /** {@inheritDoc} */
    public ContactFilterPM getPresentationModel() {
        return getMyDataSource().getPresentationModel();
    }

    /** {@inheritDoc} */
    public void setPresentationModel(ContactFilterPM pModel) {
        getMyDataSource().setPresentationModel(pModel);
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
}