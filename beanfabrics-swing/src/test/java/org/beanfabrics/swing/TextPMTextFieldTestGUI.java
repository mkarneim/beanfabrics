/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import java.awt.GridLayout;

import javax.swing.JFrame;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.swing.internal.TextPMTextField;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * @author Michael Karneim
 */
public class TextPMTextFieldTestGUI {
    public static void main(String[] args) {
        PersonModel model = new PersonModel();

        TextPMTextField textfield1 = new TextPMTextField();
        textfield1.setPresentationModel(model.name);

        TextPMTextField textfield2 = new TextPMTextField();
        textfield2.setPresentationModel(model.name);

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.getContentPane().setLayout(new GridLayout());
        f.add(textfield1);
        f.add(textfield2);
        f.pack();
        f.setVisible(true);
    }

    static class PersonModel extends AbstractPM {
        protected final TextPM name = new TextPM();

        public PersonModel() {
            PMManager.setup(this);
            name.getValidator().add(new ValidationRule() {
                public ValidationState validate() {
                    if (name.getText().length() < 5) {
                        return new ValidationState("name must have 5 characters at least");
                    }
                    return null;
                }
            });
        }
    }
}