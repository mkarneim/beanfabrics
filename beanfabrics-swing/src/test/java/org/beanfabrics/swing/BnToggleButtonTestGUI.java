/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.PMManager;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BnToggleButtonTestGUI extends JFrame {
    private ModelProvider provider;
    private MyModel myModel;
    private BnTextField tfTest;
    private BnToggleButton btnValue;

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
            BnToggleButtonTestGUI frame = new BnToggleButtonTestGUI();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the frame
     */
    public BnToggleButtonTestGUI() {
        super();
        final GridBagConstraints gbc_btnValue;
        final GridBagConstraints gbc_tfTest;
        final GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 7 };
        gridBagLayout.rowHeights = new int[] { 0, 7 };
        getContentPane().setLayout(gridBagLayout);
        setBounds(100, 100, 500, 375);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gbc_btnValue = new GridBagConstraints();
        gbc_btnValue.insets = new Insets(4, 0, 4, 4);
        gbc_btnValue.anchor = GridBagConstraints.WEST;
        gbc_btnValue.gridy = 0;
        gbc_btnValue.gridx = 0;
        getContentPane().add(getBtnValue(), gbc_btnValue);
        gbc_tfTest = new GridBagConstraints();
        gbc_tfTest.insets = new Insets(4, 4, 4, 4);
        gbc_tfTest.fill = GridBagConstraints.HORIZONTAL;
        gbc_tfTest.anchor = GridBagConstraints.WEST;
        gbc_tfTest.gridy = 1;
        gbc_tfTest.gridx = 0;
        getContentPane().add(getTfTest(), gbc_tfTest);
        //
    }

    protected BnToggleButton getBtnValue() {
        if (btnValue == null) {

            btnValue = new BnToggleButton();
            btnValue.setModelProvider(getLocalProvider());
            btnValue.setPath(new Path("this.test"));
            btnValue.setText("Value");
        }
        return btnValue;
    }

    protected BnTextField getTfTest() {
        if (tfTest == null) {
            tfTest = new BnTextField();
            tfTest.setModelProvider(getLocalProvider());
            tfTest.setPath(new Path("this.test"));
        }
        return tfTest;
    }

    /**
     * @wbp.nonvisual location=201,398
     * @return
     */
    protected MyModel getMyModel() {
        if (myModel == null) {
            myModel = new MyModel(); // @wb:location=201,398
        }
        return myModel;
    }

    /**
     * @wbp.nonvisual location=243,401
     * @return
     */
    protected ModelProvider getLocalProvider() {
        if (provider == null) {
            provider = new ModelProvider(); // @wb:location=243,401
            provider.setPresentationModel(getMyModel());
        }
        return provider;
    }
}