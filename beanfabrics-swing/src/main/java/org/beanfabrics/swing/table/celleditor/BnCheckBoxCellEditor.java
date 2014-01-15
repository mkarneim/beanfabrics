/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documentedpackage
// org.beanfabrics.swing.table.celleditor;
package org.beanfabrics.swing.table.celleditor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
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
    private final ActionListener stopAction = new StopActionListener();

    private class StopActionListener implements ActionListener, Serializable {
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
        }
    };

    private transient WeakReference<BnCheckBox> cacheEntry = new WeakReference<BnCheckBox>(null);

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
            IBooleanPM pm = (IBooleanPM) value;
            BnCheckBox checkBox = cacheEntry.get();
            // we can reuse the checkbox from cache if it is bound to the same pm
            if (checkBox == null || checkBox.getPresentationModel() != pm) {
                checkBox = createBnCheckBox();
                checkBox.setPresentationModel((IBooleanPM) value);
                cacheEntry = new WeakReference<BnCheckBox>(checkBox);
            }
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

    @Override
    public boolean stopCellEditing() {
        clearCacheEntry();
        return super.stopCellEditing();
    }

    @Override
    public void cancelCellEditing() {
        clearCacheEntry();
        super.cancelCellEditing();
    }

    private void clearCacheEntry() {
        cacheEntry = new WeakReference<BnCheckBox>(null);
    }

    // Serialization support.
    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        cacheEntry = new WeakReference<BnCheckBox>(null);
    }

}