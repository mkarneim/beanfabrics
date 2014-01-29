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
import org.beanfabrics.Path;

public class BnTextFieldTestGUI extends JFrame {
    private BnTextField tfColumns;
    private BnTextField tfText;
    private JPanel panel;
    private ModelProvider provider;

    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        try {
            SamplePM pModel = new SamplePM();

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
    /**
     * @wbp.nonvisual location=51,437
     * @return
     */
    private ModelProvider getLocalProvider() {
        if (provider == null) {
            provider = new ModelProvider(); // @wb:location=51,437
            provider.setPresentationModelType(SamplePM.class);
        }
        return provider;
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.add(getTfText());
            panel.add(getTfColumns());
        }
        return panel;
    }

    private BnTextField getTfText() {
        if (tfText == null) {
            tfText = new BnTextField();
            tfText.setPath(new org.beanfabrics.Path("this.text"));
            tfText.setModelProvider(getLocalProvider());
            tfText.setColumns(10);
        }
        return tfText;
    }

    public static class SamplePM extends AbstractPM {
        @Property
        TextPM text = new TextPM();
        @Property
        IntegerPM columns = new IntegerPM();

        public SamplePM() {
            PMManager.setup(this);
        }

        @OnChange(path = "text")
        private void updateColumns() {
            columns.setInteger(text.getText().length());
        }
    }

    private BnTextField getTfColumns() {
        if (tfColumns == null) {
            tfColumns = new BnTextField();
            tfColumns.setPath(new Path("this.columns"));
            tfColumns.setModelProvider(getLocalProvider());
            tfColumns.setColumns(4);
        }
        return tfColumns;
    }
}