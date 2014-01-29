/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
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
import org.beanfabrics.swing.GroupModel.AddressPM;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ModelSubscriberCustomizerTestGUI extends JFrame {
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    private ModelProvider provider;
    private AddressPM addressModel;

    public ModelSubscriberCustomizerTestGUI() {
        super();

        final JLabel customizeLabel;
        final BnTextField tfStreet;
        final GridBagConstraints gbc_tfStreet;
        final GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 7 };
        getContentPane().setLayout(gridBagLayout);

        customizeLabel = new JLabel();
        customizeLabel.setText("Customize");
        getContentPane().add(customizeLabel, new GridBagConstraints());

        tfStreet = new BnTextField();
        tfStreet.setPath(new Path("this.street"));
        tfStreet.setModelProvider(getProvider());
        gbc_tfStreet = new GridBagConstraints();
        gbc_tfStreet.weightx = 1;
        gbc_tfStreet.fill = GridBagConstraints.HORIZONTAL;
        gbc_tfStreet.insets = new Insets(4, 4, 4, 4);
        gbc_tfStreet.gridy = 0;
        gbc_tfStreet.gridx = 1;
        getContentPane().add(tfStreet, gbc_tfStreet);
    }

    /**
     * @wbp.nonvisual location=206,386
     * @return
     */
    public ModelProvider getProvider() {
        if (provider == null) {
            provider = new ModelProvider(); // @wb:location=206,386
            provider.setPresentationModel(getAddressPM());
        }
        return provider;
    }

    /**
     * @wbp.nonvisual location=107,395
     * @return
     */
    public AddressPM getAddressPM() {
        if (addressModel == null) {
            addressModel = new AddressPM(); // @wb:location=107,395
        }
        return addressModel;
    }

}