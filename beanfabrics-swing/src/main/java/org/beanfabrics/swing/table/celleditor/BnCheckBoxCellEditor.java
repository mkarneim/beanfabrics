/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documentedpackage
// org.beanfabrics.swing.table.celleditor;
package org.beanfabrics.swing.table.celleditor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;

import org.beanfabrics.model.IBooleanPM;
import org.beanfabrics.swing.BnCheckBox;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BnCheckBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
    private transient ActionListener stopAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
        }
    };

    public BnCheckBoxCellEditor() {

    }

    private BnCheckBox createBnCheckBox() {
        BnCheckBox checkBox = new BnCheckBox();
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        checkBox.addActionListener(stopAction);
        return checkBox;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof IBooleanPM) {
            BnCheckBox checkBox = createBnCheckBox();
            checkBox.setPresentationModel((IBooleanPM)value);
            return checkBox;
        }
        return null;
    }

    public Object getCellEditorValue() {
        // in Beanfabrics we don't need to return a value
        return null;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return false;
    }

}