/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.PMManager;

/**
 * @author Michael Karneim
 */
public class BnCheckBoxTestGUI extends JFrame {
    private ModelProvider provider;
    private MyModel myModel;
    private BnTextField bnTextField;
    private BnCheckBox valueBnCheckBox;

    public static class MyModel extends AbstractPM {
        protected final BooleanPM test = new BooleanPM();

        public MyModel() {
            PMManager.setup(this);
        }
    }

    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        try {
            BnCheckBoxTestGUI frame = new BnCheckBoxTestGUI();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the frame
     */
    public BnCheckBoxTestGUI() {
        super();
        final GridBagConstraints gridBagConstraints_2;
        final GridBagConstraints gridBagConstraints_3;
        final GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 7 };
        gridBagLayout.rowHeights = new int[] { 0, 7 };
        getContentPane().setLayout(gridBagLayout);
        setBounds(100, 100, 500, 375);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gridBagConstraints_2 = new GridBagConstraints();
        gridBagConstraints_2.insets = new Insets(4, 0, 4, 4);
        gridBagConstraints_2.anchor = GridBagConstraints.WEST;
        gridBagConstraints_2.gridy = 0;
        gridBagConstraints_2.gridx = 0;
        getContentPane().add(getValueBnCheckBox(), gridBagConstraints_2);
        gridBagConstraints_3 = new GridBagConstraints();
        gridBagConstraints_3.insets = new Insets(4, 4, 4, 4);
        gridBagConstraints_3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints_3.anchor = GridBagConstraints.WEST;
        gridBagConstraints_3.gridy = 1;
        gridBagConstraints_3.gridx = 0;
        getContentPane().add(getBnTextField(), gridBagConstraints_3);
        //
    }

    protected BnCheckBox getValueBnCheckBox() {
        if (valueBnCheckBox == null) {
            valueBnCheckBox = new BnCheckBox();
            valueBnCheckBox.setPath(new org.beanfabrics.Path("this.test"));
            valueBnCheckBox.setModelProvider(provider);
            valueBnCheckBox.setText("Value");
        }
        return valueBnCheckBox;
    }

    protected BnTextField getBnTextField() {
        if (bnTextField == null) {
            bnTextField = new BnTextField();
            bnTextField.setPath(new org.beanfabrics.Path("this.test"));
            bnTextField.setModelProvider(provider);
        }
        return bnTextField;
    }

    protected MyModel getMyModel() {
        if (myModel == null) {
            myModel = new MyModel(); // @wb:location=201,398
        }
        return myModel;
    }

    protected ModelProvider getLocalProvider() {
        if (provider == null) {
            provider = new ModelProvider(); // @wb:location=243,401
            provider.setPresentationModel(getMyModel());
        }
        return provider;
    }
}