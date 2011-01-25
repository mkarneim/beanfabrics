/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.swing.GroupModel.AddressModel;

/**
 * @author Michael Karneim
 */
public class ModelSubscriberCustomizerTestGUI extends JFrame {
    private ModelProvider provider;
    private AddressModel addressModel;

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    public ModelSubscriberCustomizerTestGUI() {
        super();

        addressModel = new AddressModel(); // @wb:location=107,395

        provider = new ModelProvider(); // @wb:location=206,386
        provider.setPresentationModel(addressModel);
        final JLabel customizeLabel;
        final BnTextField bnTextField;
        final GridBagConstraints gridBagConstraints;
        final GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 7 };
        getContentPane().setLayout(gridBagLayout);

        customizeLabel = new JLabel();
        customizeLabel.setText("Customize");
        getContentPane().add(customizeLabel, new GridBagConstraints());

        bnTextField = new BnTextField();
        bnTextField.setPath(new Path("this.street"));
        bnTextField.setModelProvider(provider);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(4, 4, 4, 4);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridx = 1;
        getContentPane().add(bnTextField, gridBagConstraints);
    }
}