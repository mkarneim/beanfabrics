/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.support;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.swing.BnTextField;

/**
 * This is a visual test for a presentation model with some virtual properties.
 * 
 * @author Michael Karneim
 */
public class VirtualPropertyTestGUI extends JFrame {

    private ModelProvider modelProvider;
    private JLabel propertyBLabel;
    private JLabel propertyALabel;
    private BnTextField tfB;
    private BnTextField tfA;
    private JPanel panel;

    private static class SamplePM extends AbstractPM {
        public SamplePM() {
            PMManager.setup(this);
            PropertySupport.get(this).putProperty("propertyA", new TextPM(), TextPM.class);
            TextPM b = PropertySupport.get(this).putProperty("propertyB", new TextPM(), TextPM.class);
            b.setMandatory(true);
        }

        @OnChange(path = "propertyA")
        void updateB() {
            TextPM a = (TextPM)PropertySupport.get(this).getProperty("propertyA");
            TextPM b = (TextPM)PropertySupport.get(this).getProperty("propertyB");
            if (a != null && b != null) {
                b.setText(a.getText());
            }
        }

        @Validation(path = "propertyA")
        boolean propertyAEqualsTest() {
            TextPM a = (TextPM)PropertySupport.get(this).getProperty("propertyA");
            return a == null || a.getText().equals("test") == false;
        }
    }

    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    VirtualPropertyTestGUI frame = new VirtualPropertyTestGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame
     */
    public VirtualPropertyTestGUI() {
        super();
        setBounds(100, 100, 500, 375);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(getPanel(), BorderLayout.CENTER);

        //
        SamplePM model = new SamplePM();
        this.modelProvider.setPresentationModel(model);
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            final GridBagLayout gridBagLayout = new GridBagLayout();
            gridBagLayout.columnWidths = new int[] { 7 };
            gridBagLayout.rowHeights = new int[] { 0, 7 };
            panel.setLayout(gridBagLayout);
            final GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridx = 1;
            final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
            gridBagConstraints_2.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_2.gridy = 0;
            gridBagConstraints_2.gridx = 0;
            panel.add(getPropertyALabel(), gridBagConstraints_2);
            panel.add(getTfA(), gridBagConstraints);
            final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
            gridBagConstraints_1.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_1.gridy = 1;
            gridBagConstraints_1.gridx = 1;
            final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
            gridBagConstraints_3.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_3.gridy = 1;
            gridBagConstraints_3.gridx = 0;
            panel.add(getPropertyBLabel(), gridBagConstraints_3);
            panel.add(getTfB(), gridBagConstraints_1);
        }
        return panel;
    }

    private BnTextField getTfA() {
        if (tfA == null) {
            tfA = new BnTextField();
            tfA.setModelProvider(getModelProvider());
            tfA.setPath(new Path("propertyA"));
            tfA.setColumns(10);
        }
        return tfA;
    }

    private BnTextField getTfB() {
        if (tfB == null) {
            tfB = new BnTextField();
            tfB.setModelProvider(getModelProvider());
            tfB.setPath(new Path("propertyB"));
            tfB.setColumns(10);
        }
        return tfB;
    }

    private JLabel getPropertyALabel() {
        if (propertyALabel == null) {
            propertyALabel = new JLabel();
            propertyALabel.setText("Property A");
        }
        return propertyALabel;
    }

    private JLabel getPropertyBLabel() {
        if (propertyBLabel == null) {
            propertyBLabel = new JLabel();
            propertyBLabel.setText("Property B");
        }
        return propertyBLabel;
    }

    private ModelProvider getModelProvider() {
        if (modelProvider == null) {
            modelProvider = new ModelProvider(); // @wb:location=32,414
        }
        return modelProvider;
    }
}