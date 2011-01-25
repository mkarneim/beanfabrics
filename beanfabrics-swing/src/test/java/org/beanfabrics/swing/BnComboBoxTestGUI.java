/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.Options;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * @author Michael Karneim
 */
public class BnComboBoxTestGUI extends JFrame {
    static class MyModel extends AbstractPM {
        protected final TextPM text = new TextPM();
        private final Options<String> options = new Options<String>();

        public MyModel() {
            PMManager.setup(this);
            options.put("First", "First");
            options.put("Second", "Second");
            text.setOptions(options);
            text.getValidator().add(new ValidationRule() {
                public ValidationState validate() {
                    if (text.getText().length() > 5)
                        return new ValidationState("Test error: size > 5");
                    return null;
                }
            });
        }
    }

    /**
     * This method initializes jCheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getJCheckBox() {
        if (jCheckBox == null) {
            jCheckBox = new JCheckBox();
            jCheckBox.setText("Editable");
            jCheckBox.setSelected(true);
            jCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    getBnComboBox().setEditable(jCheckBox.isSelected());
                }
            });
        }
        return jCheckBox;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        final BnComboBoxTestGUI f = new BnComboBoxTestGUI();
        f.pack();
        f.setVisible(true);

    }

    private JPanel jContentPane = null;
    private JPanel jPanel = null;
    private JLabel jLabel = null;
    private JLabel jLabel1 = null;
    private BnTextField bnTextField = null;
    private BnComboBox bnComboBox = null;
    private ModelProvider provider;
    private MyModel model = new MyModel(); // @wb:location=356,43
    private JCheckBox jCheckBox = null;

    /**
     * This is the default constructor
     */
    public BnComboBoxTestGUI() {
        super();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialize();
        System.out.println("" + bnComboBox.isConnected());
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(300, 200);
        this.setContentPane(getJContentPane());
        this.setTitle("JFrame");
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
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.anchor = GridBagConstraints.WEST;
            gridBagConstraints11.gridx = 1;
            gridBagConstraints11.gridy = 1;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 1;
            gridBagConstraints5.fill = GridBagConstraints.NONE;
            gridBagConstraints5.anchor = GridBagConstraints.WEST;
            gridBagConstraints5.gridy = 3;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints3.anchor = GridBagConstraints.WEST;
            gridBagConstraints3.gridx = 1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = GridBagConstraints.BOTH;
            gridBagConstraints2.gridy = 2;
            gridBagConstraints2.weightx = 1.0;
            gridBagConstraints2.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints2.anchor = GridBagConstraints.WEST;
            gridBagConstraints2.gridx = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 2;
            jLabel1 = new JLabel();
            jLabel1.setText("Text");
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            jLabel = new JLabel();
            jLabel.setText("Text");
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(jLabel, gridBagConstraints);
            jPanel.add(jLabel1, gridBagConstraints1);
            jPanel.add(getBnTextField(), gridBagConstraints2);
            jPanel.add(getBnComboBox(), gridBagConstraints3);
            jPanel.add(getJCheckBox(), gridBagConstraints11);
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
            bnTextField.setModelProvider(this.getLocalProvider());
            bnTextField.setPath(new org.beanfabrics.Path("this.text"));
        }
        return bnTextField;
    }

    /**
     * This method initializes bnComboBox
     * 
     * @return org.beanfabrics.gui.swing.BnComboBox
     */
    private BnComboBox getBnComboBox() {
        if (bnComboBox == null) {
            bnComboBox = new BnComboBox();
            bnComboBox.setPath(new org.beanfabrics.Path("this.text"));
            bnComboBox.setModelProvider(getLocalProvider());
            bnComboBox.setEditable(true);
        }
        return bnComboBox;
    }

    /**
     * This method initializes <code>provider</code>.
     * 
     * @return the <code>ModelProvider</code>
     */
    private ModelProvider getLocalProvider() {
        if (provider == null) {
            provider = new ModelProvider(); // @wb:location=341,113
            provider.setPresentationModel(model);
        }
        return provider;
    }
}