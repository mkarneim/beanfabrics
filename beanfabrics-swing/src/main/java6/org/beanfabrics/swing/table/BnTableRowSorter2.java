/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.RowSorter;
import javax.swing.SortOrder;

import org.beanfabrics.Path;
import org.beanfabrics.log.Logger;
import org.beanfabrics.log.LoggerFactory;
import org.beanfabrics.model.IListPM;

/**
 * @author Michael Karneim
 */
public class BnTableRowSorter2 extends RowSorter<BnTableModel> {
    private final static Logger LOG = LoggerFactory.getLogger(BnTableRowSorter.class);

    public static BnTableRowSorter2 install(BnTable table) {
        BnTableModel model = (BnTableModel)table.getModel();
        BnTableRowSorter2 rowSorter = new BnTableRowSorter2(model);
        table.setRowSorter(rowSorter);
        return rowSorter;
    }

    public static void uninstall(BnTable table) {
        RowSorter rowSorter = table.getRowSorter();
        if (rowSorter instanceof BnTableRowSorter2) {
            ((BnTableRowSorter2)rowSorter).dismiss();
        }
        table.setRowSorter(null);
    }

    private final PropertyChangeListener pcl = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            onSortKeysChanged();
        }
    };
    private final BnTableModel model;
    private final IListPM list;
    private List<? extends RowSorter.SortKey> viewSortKeys;

    public BnTableRowSorter2(BnTableModel model) {
        super();
        this.model = model;
        list = model.getPresentationModel();
        updateViewSortKeys();
        list.addPropertyChangeListener("sortKeys", pcl);
    }

    public void dismiss() {
        list.removePropertyChangeListener("sortKeys", pcl);
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
        System.out.println("BnTableRowSorter.getSortKeys()");
        return viewSortKeys;
    }

    private void updateViewSortKeys() {
        System.out.println("BnTableRowSorter.updateViewSortKeys()");
        final int colNum = model.getColumnCount();

        RowSorter.SortKey[] viewSortKeys = new RowSorter.SortKey[colNum];
        for (int col = 0; col < colNum; ++col) {
            org.beanfabrics.model.SortKey modelSortKey = model.getSortKey(col);
            if (modelSortKey == null) {
                viewSortKeys[col] = new RowSorter.SortKey(col, SortOrder.UNSORTED);
            } else {
                SortOrder sortOrder = modelSortKey.isAscending() ? SortOrder.ASCENDING : SortOrder.DESCENDING;
                RowSorter.SortKey viewSortKey = new RowSorter.SortKey(col, sortOrder);
                viewSortKeys[col] = viewSortKey;
            }
        }

        if (LOG.isDebugEnabled()) {
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < viewSortKeys.length; ++i) {
                if (i > 0) {
                    buf.append("\t");
                }
                buf.append(viewSortKeys[i].getColumn() + ":" + viewSortKeys[i].getSortOrder().name());
            }
            LOG.debug(buf.toString());
        }

        this.viewSortKeys = Arrays.asList(viewSortKeys);
    }

    @Override
    public int getViewRowCount() {
        return model.getRowCount();
    }

    @Override
    public void setSortKeys(List<? extends RowSorter.SortKey> keys) {
        System.out.println("BnTableRowSorter.setSortKeys()");
        sortExistingData(keys);
    }

    @Override
    public void toggleSortOrder(int column) {
        System.out.println("BnTableRowSorter.toggleSortOrder()");

        org.beanfabrics.model.SortKey modelSortKey = model.getSortKey(column);
        org.beanfabrics.model.SortKey newModelSortKey;
        if (modelSortKey == null) {
            Path path = model.getColumnPath(column);
            newModelSortKey = new org.beanfabrics.model.SortKey(/* ascending= */true, path);
        } else {
            newModelSortKey = new org.beanfabrics.model.SortKey(!modelSortKey.isAscending(), modelSortKey.getSortPath());
        }
        fireSortOrderChanged();
        list.sortBy(newModelSortKey);
    }

    private void sortExistingData(List<? extends RowSorter.SortKey> viewSortKeys) {
        org.beanfabrics.model.SortKey[] modelSortKeys = new org.beanfabrics.model.SortKey[viewSortKeys.size()];
        int i = 0;
        for (RowSorter.SortKey viewSortKey : viewSortKeys) {
            Path path = model.getColumnPath(viewSortKey.getColumn());
            boolean ascending = viewSortKey.getSortOrder() == SortOrder.ASCENDING;
            modelSortKeys[i] = new org.beanfabrics.model.SortKey(ascending, path);
            i++;
        }
        fireSortOrderChanged();
        list.sortBy(modelSortKeys);
    }

    /**
     * This method is called whenever the IListPM has changed it's sort keys.
     */
    protected void onSortKeysChanged() {
        updateViewSortKeys();
        fireSortOrderChanged();
        fireRowSorterChanged(null);
    }

    // / Callback methods that we don't use in Beanfabrics:

    @Override
    public void modelStructureChanged() {
        // nothing to do here
        fireSortOrderChanged();
        fireRowSorterChanged(null);
    }

    @Override
    public void rowsDeleted(int firstRow, int endRow) {
        // nothing to do here
        fireSortOrderChanged();
        fireRowSorterChanged(null);
    }

    @Override
    public void rowsInserted(int firstRow, int endRow) {
        // nothing to do here
        fireSortOrderChanged();
        fireRowSorterChanged(null);
    }

    @Override
    public void rowsUpdated(int firstRow, int endRow) {
        // nothing to do here
        fireSortOrderChanged();
        fireRowSorterChanged(null);
    }

    @Override
    public void rowsUpdated(int firstRow, int endRow, int column) {
        // nothing to do here
        fireSortOrderChanged();
        fireRowSorterChanged(null);
    }

    @Override
    public void allRowsChanged() {
        // nothing to do here
        fireSortOrderChanged();
        fireRowSorterChanged(null);
    }
}