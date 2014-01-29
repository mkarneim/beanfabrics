/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documentedpackage
// org.beanfabrics.swing.table.celleditor;
package org.beanfabrics.swing.table.celleditor;

import java.awt.Component;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.beanfabrics.model.ITextPM;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class EmptyCellEditor extends AbstractCellEditor implements TableCellEditor {

    private transient WeakReference<JLabel> cacheEntry = new WeakReference<JLabel>(null);

    public EmptyCellEditor() {
        //
    }

    private JLabel createJLabel() {
        JLabel result = new JLabel();
        result.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0));
        return result;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof ITextPM) {
            ITextPM pm = (ITextPM) value;
            JLabel label = cacheEntry.get();
            // we can reuse the label from cache
            if (label == null) {
                label = createJLabel();
                cacheEntry = new WeakReference<JLabel>(label);
            }
            return label;
        }
        return null;
    }

    public Object getCellEditorValue() {
        // in Beanfabrics we don't need to return a value
        return null;
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
        cacheEntry = new WeakReference<JLabel>(null);
    }

    // Serialization support.
    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        cacheEntry = new WeakReference<JLabel>(null);
    }

}