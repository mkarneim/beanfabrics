/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnTextField;

public class AddressPanel extends JPanel implements View<AddressPM>, ModelSubscriber {
    private final Link link = new Link(this);
    private ModelProvider myDataSource = null; //@jve:decl-index=0:visual-constraint ="367,61"
    private JPanel contentPanel = null;
    private JLabel jLabel = null;
    private JLabel jLabel1 = null;
    private JLabel jLabel2 = null;
    private JLabel jLabel3 = null;
    private BnTextField bnTextField = null;
    private BnTextField bnTextField1 = null;
    private BnTextField bnTextField2 = null;
    private BnTextField bnTextField3 = null;
    private JLabel jLabel4 = null;
    private BnTextField bnTextField4 = null;

    /**
     * This is the default constructor
     */
    public AddressPanel() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(300, 200);
        this.setLayout(new BorderLayout());
        this.add(getContentPanel(), BorderLayout.CENTER);
    }

    /**
     * This method initializes myDataSource
     * 
     * @return the <code>ModelProvider</code>
     */
    private ModelProvider getMyDataSource() {
        if (myDataSource == null) {
            myDataSource = new ModelProvider();
            myDataSource.setPresentationModelType(org.beanfabrics.test.AddressPM.class);
        }
        return myDataSource;
    }

    /** {@inheritDoc} */
    public AddressPM getPresentationModel() {
        return getMyDataSource().getPresentationModel();
    }

    /** {@inheritDoc} */
    public void setPresentationModel(AddressPM pModel) {
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

    /**
     * This method initializes contentPanel.
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getContentPanel() {
        if (contentPanel == null) {
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints9.gridy = 4;
            gridBagConstraints9.weightx = 1.0;
            gridBagConstraints9.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints9.anchor = GridBagConstraints.WEST;
            gridBagConstraints9.gridx = 1;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 0;
            gridBagConstraints8.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints8.anchor = GridBagConstraints.EAST;
            gridBagConstraints8.gridy = 4;
            jLabel4 = new JLabel();
            jLabel4.setText("Appartment");
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints7.gridy = 3;
            gridBagConstraints7.weightx = 1.0;
            gridBagConstraints7.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints7.anchor = GridBagConstraints.WEST;
            gridBagConstraints7.gridx = 1;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.gridy = 2;
            gridBagConstraints6.weightx = 1.0;
            gridBagConstraints6.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints6.anchor = GridBagConstraints.WEST;
            gridBagConstraints6.gridx = 1;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.gridy = 1;
            gridBagConstraints5.weightx = 1.0;
            gridBagConstraints5.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints5.anchor = GridBagConstraints.WEST;
            gridBagConstraints5.gridx = 1;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints4.anchor = GridBagConstraints.WEST;
            gridBagConstraints4.gridx = 1;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints3.anchor = GridBagConstraints.EAST;
            gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridy = 3;
            jLabel3 = new JLabel();
            jLabel3.setText("Street");
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints2.anchor = GridBagConstraints.EAST;
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.gridy = 2;
            jLabel2 = new JLabel();
            jLabel2.setText("City");
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints1.anchor = GridBagConstraints.EAST;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 1;
            jLabel1 = new JLabel();
            jLabel1.setText("ZIP");
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints.anchor = GridBagConstraints.EAST;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridy = 0;
            jLabel = new JLabel();
            jLabel.setText("Country");
            contentPanel = new JPanel();
            contentPanel.setLayout(new GridBagLayout());
            contentPanel.add(jLabel, gridBagConstraints);
            contentPanel.add(jLabel1, gridBagConstraints1);
            contentPanel.add(jLabel2, gridBagConstraints2);
            contentPanel.add(jLabel3, gridBagConstraints3);
            contentPanel.add(getBnTextField(), gridBagConstraints4);
            contentPanel.add(getBnTextField1(), gridBagConstraints5);
            contentPanel.add(getBnTextField2(), gridBagConstraints6);
            contentPanel.add(getBnTextField3(), gridBagConstraints7);
            contentPanel.add(jLabel4, gridBagConstraints8);
            contentPanel.add(getBnTextField4(), gridBagConstraints9);
        }
        return contentPanel;
    }

    /**
     * This method initializes bnTextField.
     * 
     * @return org.beanfabrics.gui.swing.BnTextField
     */
    private BnTextField getBnTextField() {
        if (bnTextField == null) {
            bnTextField = new BnTextField();
            bnTextField.setPath(new Path("this.country"));
            bnTextField.setModelProvider(getMyDataSource());
        }
        return bnTextField;
    }

    /**
     * This method initializes bnTextField1.
     * 
     * @return org.beanfabrics.gui.swing.BnTextField
     */
    private BnTextField getBnTextField1() {
        if (bnTextField1 == null) {
            bnTextField1 = new BnTextField();
            bnTextField1.setPath(new Path("this.zip"));
            bnTextField1.setModelProvider(getMyDataSource());
        }
        return bnTextField1;
    }

    /**
     * This method initializes bnTextField2.
     * 
     * @return org.beanfabrics.gui.swing.BnTextField
     */
    private BnTextField getBnTextField2() {
        if (bnTextField2 == null) {
            bnTextField2 = new BnTextField();
            bnTextField2.setPath(new Path("this.city"));
            bnTextField2.setModelProvider(getMyDataSource());
        }
        return bnTextField2;
    }

    /**
     * This method initializes bnTextField3.
     * 
     * @return org.beanfabrics.gui.swing.BnTextField
     */
    private BnTextField getBnTextField3() {
        if (bnTextField3 == null) {
            bnTextField3 = new BnTextField();
            bnTextField3.setPath(new Path("this.street"));
            bnTextField3.setModelProvider(getMyDataSource());
        }
        return bnTextField3;
    }

    /**
     * This method initializes bnTextField4.
     * 
     * @return org.beanfabrics.gui.swing.BnTextField
     */
    private BnTextField getBnTextField4() {
        if (bnTextField4 == null) {
            bnTextField4 = new BnTextField();
            bnTextField4.setPath(new Path("this.appartment"));
            bnTextField4.setModelProvider(getMyDataSource());
        }
        return bnTextField4;
    }
}