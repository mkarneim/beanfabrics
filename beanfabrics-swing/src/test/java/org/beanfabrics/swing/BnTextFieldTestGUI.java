/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Property;

public class BnTextFieldTestGUI extends JFrame {
    private BnTextField bnTextField_1;
    private BnTextField bnTextField;
    private JPanel panel;
    private ModelProvider provider;

    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        try {
            SampleModel pModel = new SampleModel();

            BnTextFieldTestGUI frame = new BnTextFieldTestGUI();
            frame.provider.setPresentationModel(pModel);

            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the frame
     */
    public BnTextFieldTestGUI() {
        super();
        setBounds(100, 100, 500, 375);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(getPanel(), BorderLayout.CENTER);
        //
    }

    private ModelProvider getLocalProvider() {
        if (provider == null) {
            provider = new ModelProvider(); // @wb:location=51,437
            provider.setPresentationModelType(SampleModel.class);
        }
        return provider;
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.add(getBnTextField());
            panel.add(getBnTextField_1());
        }
        return panel;
    }

    private BnTextField getBnTextField() {
        if (bnTextField == null) {
            bnTextField = new BnTextField();
            bnTextField.setPath(new org.beanfabrics.Path("this.text"));
            bnTextField.setModelProvider(getLocalProvider());
            bnTextField.setColumns(10);
        }
        return bnTextField;
    }

    public static class SampleModel extends AbstractPM {
        @Property
        TextPM text = new TextPM();
        @Property
        IntegerPM columns = new IntegerPM();

        public SampleModel() {
            PMManager.setup(this);
        }

        @OnChange(path = "text")
        private void updateColumns() {
            columns.setInteger(text.getText().length());
        }
    }

    private BnTextField getBnTextField_1() {
        if (bnTextField_1 == null) {
            bnTextField_1 = new BnTextField();
            bnTextField_1.setPath(new org.beanfabrics.Path("this.columns"));
            bnTextField_1.setModelProvider(getLocalProvider());
            bnTextField_1.setColumns(4);
        }
        return bnTextField_1;
    }
}