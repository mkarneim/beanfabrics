/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.RowSorter;
import javax.swing.SortOrder;

import org.beanfabrics.Path;
import org.beanfabrics.model.IListPM;

/**
 * @author Michael Karneim
 */
public class BnTableRowSorter extends RowSorter<BnTableModel> {

    public static BnTableRowSorter install(BnTable table) {
        table.setUpdateSelectionOnSort(false);
        BnTableModel model = (BnTableModel)table.getModel();
        BnTableRowSorter rowSorter = new BnTableRowSorter(model);
        table.setRowSorter(rowSorter);
        return rowSorter;
    }

    public static void uninstall(BnTable table) {
        RowSorter rowSorter = table.getRowSorter();
        if (rowSorter instanceof BnTableRowSorter) {
            ((BnTableRowSorter)rowSorter).dismiss();
        }
        table.setRowSorter(null);
    }

    private final BnTableModel model;
    private final PropertyChangeListener pcl = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            sortKeys.refresh();
            fireSortOrderChanged();
        }
    };

    private class SortKeys {
        private RowSorter.SortKey[] sortKeyByColumn;
        private List<RowSorter.SortKey> sortKeyList;

        public SortKeys(int columns) {
            sortKeyByColumn = new RowSorter.SortKey[columns];
        }

        public int size() {
            return asList().size();
        }

        public RowSorter.SortKey getSortKey(int col) {
            return sortKeyByColumn[col];
        }

        public void setSortKey(RowSorter.SortKey sortKey) {
            sortKeyByColumn[sortKey.getColumn()] = sortKey;
            this.sortKeyList = null;
        }

        public void clear() {
            Arrays.fill(this.sortKeyByColumn, null);
            this.sortKeyList = null;
        }

        public List<RowSorter.SortKey> asList() {
            if (sortKeyList == null) {
                ArrayList<RowSorter.SortKey> newSortKeyList = new ArrayList<RowSorter.SortKey>();
                for (int i = 0; i < sortKeyByColumn.length; ++i) {
                    if (sortKeyByColumn[i] != null) {
                        newSortKeyList.add(sortKeyByColumn[i]);
                    }
                }
                this.sortKeyList = Collections.unmodifiableList(newSortKeyList);
            }
            return this.sortKeyList;
        }

        public void refresh() {
            clear();
            for (int col = 0; col < sortKeyByColumn.length; ++col) {
                org.beanfabrics.model.SortKey modelSortKey = model.getSortKey(col);
                if (modelSortKey == null) {
                    sortKeyByColumn[col] = null;
                } else {
                    SortOrder sortOrder = modelSortKey.isAscending() ? SortOrder.ASCENDING : SortOrder.DESCENDING;
                    RowSorter.SortKey viewSortKey = new RowSorter.SortKey(col, sortOrder);
                    sortKeyByColumn[col] = viewSortKey;
                }
            }
        }
    }

    private final SortKeys sortKeys;
    private final IListPM list;

    public BnTableRowSorter(BnTableModel model) {
        super();
        this.model = model;
        int colNum = model.getColumnCount();
        this.sortKeys = new SortKeys(colNum);
        this.sortKeys.refresh();
        this.list = model.getPresentationModel();
        this.list.addPropertyChangeListener("sortKeys", pcl);
    }

    public void dismiss() {
        this.list.removePropertyChangeListener("sortKeys", pcl);
    }

    @Override
    public int convertRowIndexToModel(int index) {
        return index;
    }

    @Override
    public int convertRowIndexToView(int index) {
        return index;
    }

    @Override
    public BnTableModel getModel() {
        return model;
    }

    @Override
    public int getModelRowCount() {
        return model.getRowCount();
    }

    @Override
    public List<? extends RowSorter.SortKey> getSortKeys() {
        return this.sortKeys.asList();
    }

    @Override
    public int getViewRowCount() {
        return model.getRowCount();
    }

    @Override
    public void setSortKeys(List<? extends RowSorter.SortKey> keys) {
        this.sortKeys.clear();
        for (SortKey sortKey : keys) {
            this.sortKeys.setSortKey(sortKey);
        }
        org.beanfabrics.model.SortKey[] modelSortKeys = new org.beanfabrics.model.SortKey[this.sortKeys.size()];
        Iterator<? extends RowSorter.SortKey> it = this.sortKeys.asList().iterator();

        for (int i = 0; i < modelSortKeys.length; ++i) {
            RowSorter.SortKey key = it.next();
            Path path = model.getColumnPath(key.getColumn());
            boolean ascending = key.getSortOrder() == SortOrder.ASCENDING;
            modelSortKeys[i] = new org.beanfabrics.model.SortKey(ascending, path);
        }
        list.sortBy(modelSortKeys);

        fireRowSorterChanged(null);
    }

    @Override
    public void toggleSortOrder(int column) {
        RowSorter.SortKey sk = this.sortKeys.getSortKey(column);
        boolean ascending;
        if (sk == null) {
            ascending = true;
        } else {
            ascending = sk.getSortOrder() != SortOrder.ASCENDING;
        }

        this.sortKeys.clear();

        RowSorter.SortKey newKey = new RowSorter.SortKey(column, ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING);
        this.sortKeys.setSortKey(newKey);

        Path path = model.getColumnPath(column);
        list.sortBy(ascending, path);

        fireRowSorterChanged(null);
    }

    /// Callback methods that we don't use in Beanfabrics:

    @Override
    public void modelStructureChanged() {
        // nothing to do here
        //		fireSortOrderChanged();
        //		fireRowSorterChanged(null);
    }

    @Override
    public void rowsDeleted(int firstRow, int endRow) {
        // nothing to do here
        //		fireSortOrderChanged();
        //		fireRowSorterChanged(null);
    }

    @Override
    public void rowsInserted(int firstRow, int endRow) {
        // nothing to do here
        //		fireSortOrderChanged();
        //		fireRowSorterChanged(null);
    }

    @Override
    public void rowsUpdated(int firstRow, int endRow) {
        // nothing to do here
        //		fireSortOrderChanged();
        //		fireRowSorterChanged(null);
    }

    @Override
    public void rowsUpdated(int firstRow, int endRow, int column) {
        // nothing to do here
        //		fireSortOrderChanged();
        //		fireRowSorterChanged(null);
    }

    @Override
    public void allRowsChanged() {
        // nothing to do here
        //		fireSortOrderChanged();
        //		fireRowSorterChanged(null);
    }
}