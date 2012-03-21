/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.swing.BnTextField;

public class OfficeDialog extends JDialog {
    private ModelProvider provider; //  @jve:decl-index=0:visual-constraint="431,121"
    private JPanel jContentPane;
    private JPanel jPanel;
    private JLabel jLabel;
    private BnTextField bnTextField;
    private BnTextField bnTextField1;
    private JLabel jLabel1;
    private OfficePM officeModel; //  @jve:decl-index=0:visual-constraint="511,45"
    private AddressPanel addressPanel;

    public static void main(String[] args) {
        OfficeDialog dlg = new OfficeDialog(new JFrame());
        dlg.setSize(300, 400);
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }

    /**
     * @param owner
     */
    public OfficeDialog(Frame owner) {
        super(owner);
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(358, 294);
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJPanel(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 1;
            gridBagConstraints3.fill = GridBagConstraints.BOTH;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.weighty = 1.0;
            gridBagConstraints3.gridy = 2;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 1;
            jLabel1 = new JLabel();
            jLabel1.setText("Street");
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.gridy = 1;
            gridBagConstraints11.weightx = 1.0;
            gridBagConstraints11.insets = new Insets(5, 5, 5, 0);
            gridBagConstraints11.gridx = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.insets = new Insets(5, 5, 5, 0);
            gridBagConstraints1.gridx = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            jLabel = new JLabel();
            jLabel.setText("Name");
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(jLabel, gridBagConstraints);
            jPanel.add(getBnTextField(), gridBagConstraints1);
            jPanel.add(getBnTextField1(), gridBagConstraints11);
            jPanel.add(jLabel1, gridBagConstraints2);
            jPanel.add(getAddressPanel(), gridBagConstraints3);
        }
        return jPanel;
    }

    /**
     * This method initializes bnTextField
     * 
     * @return org.beanfabrics.gui.swing.BnTextField
     */
    private BnTextField getBnTextField() {
        if (bnTextField == null) {
            bnTextField = new BnTextField();
            bnTextField.setPath(new Path("this.name"));
            bnTextField.setModelProvider(getLocalProvider());
        }
        return bnTextField;
    }

    /**
     * This method initializes bnTextField1
     * 
     * @return org.beanfabrics.gui.swing.BnTextField
     */
    private BnTextField getBnTextField1() {
        if (bnTextField1 == null) {
            bnTextField1 = new BnTextField();
            bnTextField1.setPath(new Path("this.address.street"));
            bnTextField1.setModelProvider(getLocalProvider());
        }
        return bnTextField1;
    }

    /**
     * This method initializes <code>provider</code>.
     * 
     * @return the <code>ModelProvider</code>.
     */
    private ModelProvider getLocalProvider() {
        if (provider == null) {
            provider = new ModelProvider();
            provider.setPresentationModel(getOfficeModel());
        }
        return provider;
    }

    /**
     * This method initializes officeModel
     * 
     * @return org.beanfabrics.test.OfficePM
     */
    private OfficePM getOfficeModel() {
        if (officeModel == null) {
            officeModel = new OfficePM();
            officeModel.name.setText("Software Development");
            officeModel.address.street.setText("Main Street");
        }
        return officeModel;
    }

    /**
     * This method initializes addressPanel
     * 
     * @return org.beanfabrics.test.AddressPanel
     */
    private AddressPanel getAddressPanel() {
        if (addressPanel == null) {
            addressPanel = new AddressPanel();
            addressPanel.setPath(new Path("this.address"));
            addressPanel.setModelProvider(getLocalProvider());
        }
        return addressPanel;
    }
}