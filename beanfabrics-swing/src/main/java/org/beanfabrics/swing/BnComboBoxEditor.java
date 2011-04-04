/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing;

import java.awt.Component;

import javax.swing.ComboBoxEditor;
import javax.swing.JTextField;

import org.beanfabrics.swing.internal.TextPMComboBox;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BnComboBoxEditor extends BnTextField implements ComboBoxEditor {
    public BnComboBoxEditor(TextPMComboBox combo) {
        super();
        setColumns(9);
        if (combo == null) {
            throw new IllegalArgumentException("combo==null");
        }
        final ComboBoxEditor editor = combo.getEditor();
        if (editor.getEditorComponent() instanceof JTextField) {
            final JTextField field = (JTextField)editor.getEditorComponent();
            this.setBorder(field.getBorder());
            this.setPreferredSize(field.getPreferredSize());
            this.setMinimumSize(field.getMinimumSize());
        }
        this.setPresentationModel(combo.getPresentationModel());
    }

    public Component getEditorComponent() {
        return this;
    }

    public Object getItem() {
        return this.getText();
    }

    public void setItem(Object anObject) {
        String newText = (String)anObject;
        if ( getText() == null && newText == null ) {
            return;            
        }
        if ( getText() != null && getText().equals(newText)) {
            return;            
        }
        this.setText(newText);
    }
}