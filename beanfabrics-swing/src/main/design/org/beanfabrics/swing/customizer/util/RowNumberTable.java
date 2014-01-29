package org.beanfabrics.swing.customizer.util;

import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 * The {@link RowNumberTable} is a decorator component that adds row numbers to any {@link JTable} component.
 * <p>
 * It uses a JTable as a renderer for row numbers of a given main table. This table must be added to the row header of
 * the scrollpane that contains the main table.
 * 
 * <pre>
 * RowNumberTable rowTable = new RowNumberTable(mainTable);
 * rowTable.setPreferredRowHeaderWidth(30);
 * scrollPane.setRowHeaderView(rowTable);
 * scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowTable.getTableHeader());
 * JPanel corner = new JPanel();
 * corner.setBorder(UIManager.getBorder(&quot;TableHeader.cellBorder&quot;));
 * scrollPane.setCorner(JScrollPane.LOWER_LEFT_CORNER, corner);
 * </pre>
 * 
 * @author Rob Camick
 * @author KarneimM
 * @see http://tips4java.wordpress.com/2008/11/18/row-number-table/
 */
@SuppressWarnings("serial")
public class RowNumberTable extends JTable implements ChangeListener, PropertyChangeListener {
    private JTable main;
    private TableModel mainTableModel;
    private TableModelListener mainTableModelListener = new TableModelListener() {
        @Override
        public void tableChanged(TableModelEvent e) {
            RowNumberTable.this.tableChanged(new TableModelEvent(dataModel, TableModelEvent.ALL_COLUMNS));
        }
    };

    public RowNumberTable(JTable table) {
        main = table;
        main.addPropertyChangeListener(this);
        setMainTableModel(main.getModel());

        setFocusable(false);
        setAutoCreateColumnsFromModel(false);
        setSelectionModel(main.getSelectionModel());

        TableColumn column = new TableColumn();
        column.setHeaderValue(" ");
        addColumn(column);
        column.setCellRenderer(new RowNumberRenderer());

        getColumnModel().getColumn(0).setPreferredWidth(50);
        setPreferredScrollableViewportSize(getPreferredSize());
    }

    public void setPreferredRowHeaderWidth(int value) {
        getColumnModel().getColumn(0).setPreferredWidth(value);
        getColumnModel().getColumn(0).setWidth(value);
        setPreferredScrollableViewportSize(getPreferredSize());
    }

    public int getPreferredRowHeaderWidth() {
        return getColumnModel().getColumn(0).getPreferredWidth();
    }

    @Override
    public void addNotify() {
        super.addNotify();

        Component c = getParent();

        // Keep scrolling of the row table in sync with the main table.

        if (c instanceof JViewport) {
            JViewport viewport = (JViewport) c;
            viewport.addChangeListener(this);
        }
    }

    /*
     * Delegate method to main table
     */
    @Override
    public int getRowCount() {
        return main.getRowCount();
    }

    @Override
    public int getRowHeight(int row) {
        int rowHeight = main.getRowHeight(row);

        if (rowHeight != super.getRowHeight(row)) {
            super.setRowHeight(row, rowHeight);
        }

        return rowHeight;
    }

    /*
     * No model is being used for this table so just use the row number as the value of the cell.
     */
    @Override
    public Object getValueAt(int row, int column) {
        return Integer.toString(row + 1);
    }

    /*
     * Don't edit data in the main TableModel by mistake
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /*
     * Do nothing since the table ignores the model
     */
    @Override
    public void setValueAt(Object value, int row, int column) {
    }

    //
    // Implement the ChangeListener
    //
    public void stateChanged(ChangeEvent e) {
        // Keep the scrolling of the row table in sync with main table

        JViewport viewport = (JViewport) e.getSource();
        JScrollPane scrollPane = (JScrollPane) viewport.getParent();
        scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
    }

    //
    // Implement the PropertyChangeListener
    //
    public void propertyChange(PropertyChangeEvent e) {
        // Keep the row table in sync with the main table

        if ("selectionModel".equals(e.getPropertyName())) {
            setSelectionModel(main.getSelectionModel());
        }

        if ("rowHeight".equals(e.getPropertyName())) {
            repaint();
        }
        if ("model".equals(e.getPropertyName())) {
            setMainTableModel(main.getModel());
        }
        tableChanged(new TableModelEvent(dataModel, TableModelEvent.ALL_COLUMNS));
    }

    private void setMainTableModel(TableModel newModel) {
        if (newModel == mainTableModel) {
            return;
        }
        if (mainTableModel != null) {
            mainTableModel.removeTableModelListener(mainTableModelListener);
        }
        mainTableModel = newModel;
        if (mainTableModel != null) {
            mainTableModel.addTableModelListener(mainTableModelListener);
        }
        tableChanged(new TableModelEvent(dataModel, TableModelEvent.ALL_COLUMNS));
    }

    /*
     * Attempt to mimic the table header renderer
     */
    private static class RowNumberRenderer extends DefaultTableCellRenderer {
        public RowNumberRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            if (table != null) {
                JTableHeader header = table.getTableHeader();

                if (header != null) {
                    setForeground(header.getForeground());
                    setBackground(header.getBackground());
                    setFont(header.getFont());
                }
            }

            if (isSelected) {
                setFont(getFont().deriveFont(Font.BOLD));
            }

            setText((value == null) ? "" : value.toString());
            // setBorder(UIManager.getBorder("TableHeader.cellBorder"));

            return this;
        }
    }
}
