/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.beanfabrics.Path;
import org.beanfabrics.PathEvaluation;
import org.beanfabrics.event.ElementChangedEvent;
import org.beanfabrics.event.ElementsAddedEvent;
import org.beanfabrics.event.ElementsDeselectedEvent;
import org.beanfabrics.event.ElementsRemovedEvent;
import org.beanfabrics.event.ElementsReplacedEvent;
import org.beanfabrics.event.ElementsSelectedEvent;
import org.beanfabrics.event.ListListener;
import org.beanfabrics.event.WeakListListener;
import org.beanfabrics.model.IListPM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.IValuePM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.model.SortKey;

/**
 * The <code>BnTableModel</code> is a {@link TableModel} that decorates a
 * {@link IListPM}.
 * <p>
 * To reduce work we decided to extend the {@link AbstractTableModel} in order
 * to get the event listener handling. All other methods are overridden.
 * </p>
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BnTableModel extends AbstractTableModel {
    private IListPM list;
    private boolean cellEditingAllowed;

    private List<BnColumn> colDefs = new ArrayList<BnColumn>();

    private transient ListListener listener = new WeakListListener() {
        public void elementsSelected(ElementsSelectedEvent evt) {
            // ignore
        }

        public void elementsDeselected(ElementsDeselectedEvent evt) {
            // ignore
        }

        public void elementChanged(ElementChangedEvent evt) {
            fireTableRowsUpdated(evt.getIndex(), evt.getIndex());
        }

        public void elementsReplaced(ElementsReplacedEvent evt) {
            fireTableRowsUpdated(evt.getBeginIndex(), evt.getBeginIndex() + evt.getLength() - 1);
        }

        public void elementsAdded(ElementsAddedEvent evt) {
            fireTableRowsInserted(evt.getBeginIndex(), evt.getBeginIndex() + evt.getLength() - 1);
        }

        public void elementsRemoved(ElementsRemovedEvent evt) {
            fireTableRowsDeleted(evt.getBeginIndex(), evt.getBeginIndex() + evt.getLength() - 1);
        }
    };

    public BnTableModel(IListPM aListModel, List<BnColumn> colDefs, boolean editingAllowed) {
        if (aListModel == null) {
            throw new IllegalArgumentException("aListModel must not be null");
        }
        if (colDefs == null) {
            throw new IllegalArgumentException("colDefs must not be null");
        }
        this.list = aListModel;
        this.cellEditingAllowed = editingAllowed;
        this.colDefs.addAll(colDefs);
        this.list.addListListener(this.listener);
    }

    /**
     * Disconnect <code>this</code> object from the underlying IListPM.
     */
    public void dismiss() {
        this.list.removeListListener(this.listener);
    }

    public void setCellEditingAllowed(boolean editingAllowed) {
        boolean oldValue = this.cellEditingAllowed;
        this.cellEditingAllowed = editingAllowed;
        if (oldValue != this.cellEditingAllowed) {
            this.fireTableStructureChanged();
        }
    }

    public boolean isCellEditingAllowed() {
        return cellEditingAllowed;
    }

    public List<BnColumn> getColDefs() {
        return Collections.unmodifiableList(colDefs);
    }

    public void setColDefs(List<BnColumn> colDefs) {
        if (colDefs == null) {
            throw new IllegalArgumentException("colDefs==null");
        }
        this.colDefs.clear();
        this.colDefs.addAll(colDefs);
        this.fireTableStructureChanged();
    }

    @Override
    public int getColumnCount() {
        return this.colDefs.size();
    }

    @Override
    public int getRowCount() {
        return this.list.size();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // we don't support changing a value with this method
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final Path path = this.colDefs.get(columnIndex).getPath();
        PresentationModel rowMdl = this.list.getAt(rowIndex);
        return PathEvaluation.evaluateOrNull(rowMdl, path);
    }

    @Override
    public String getColumnName(int column) {
        final BnColumn def = this.colDefs.get(column);
        if (def == null)
            return null;
        return def.getColumnName();
    }

    public Path getColumnPath(int column) {
        final BnColumn def = this.colDefs.get(column);
        if (def == null)
            return null;
        return def.getPath();
    }

    @Override
    public int findColumn(String columnName) {
        for (int i = 0; i < getColumnCount(); i++) {
            if (columnName.equals(getColumnName(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return PresentationModel.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (!cellEditingAllowed) {
            return false;
        } else {
            final Object value = this.getValueAt(rowIndex, columnIndex);
            if (value instanceof IValuePM) {
                return ((IValuePM)value).isEditable();
            } else if (value instanceof IOperationPM) {
                return ((IOperationPM)value).isEnabled();
            } else {
                return false;
            }
        }
    }

    /**
     * Returns the underlying presentation model of this {@link TableModel}.
     * 
     * @return the underlying presentation model
     */
    public IListPM getPresentationModel() {
        return this.list;
    }

    /**
     * Returns the {@link SortKey} of the specified column or <code>null</code>
     * if that column is not sorted.
     * 
     * @param col
     * @return the {@link SortKey} of the specified column
     */
    public SortKey getSortKey(int col) {
        Path colPath = getColumnPath(col);
        if (colPath == null) {
            return null;
        }
        Collection<SortKey> sortKeys = list.getSortKeys();
        for (SortKey sortKey : sortKeys) {
            if (colPath.equals(sortKey.getSortPath())) {
                return sortKey;
            }
        }
        return null;
    }

}