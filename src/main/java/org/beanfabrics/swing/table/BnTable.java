/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.table;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.event.ElementsAddedEvent;
import org.beanfabrics.event.ElementsRemovedEvent;
import org.beanfabrics.event.ListAdapter;
import org.beanfabrics.event.WeakListAdapter;
import org.beanfabrics.log.Logger;
import org.beanfabrics.log.LoggerFactory;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IListPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.table.celleditor.BnTableCellEditor;
import org.beanfabrics.swing.table.cellrenderer.BnTableCellRenderer;

/**
 * The <code>BnTable</code> is a {@link JTable} that can subscribe to an
 * {@link IListPM}.
 * <p>
 * For an example about using BnTable, please see <a
 * href="http://www.beanfabrics.org/index.php/BnTable"
 * target="parent">http://www.beanfabrics.org/index.php/BnTable</a>
 * </p>
 * 
 * @author Michael Karneim
 * @beaninfo
 */
@SuppressWarnings( { "serial" })
public class BnTable extends JTable implements View<IListPM<? extends PresentationModel>>, ModelSubscriber {
    private final static Logger LOG = LoggerFactory.getLogger(BnTable.class);

    private transient final ListAdapter listListener = new WeakListAdapter() {
        public void elementsAdded(ElementsAddedEvent evt) {
            cancelCellEditing();
        }

        public void elementsRemoved(ElementsRemovedEvent evt) {
            cancelCellEditing();
        }

        private void cancelCellEditing() {
            if (getCellEditor() != null)
                getCellEditor().cancelCellEditing();
        }
    };
    private final Link link = new Link(this);
    private IListPM<? extends PresentationModel> presentationModel;
    private List<BnColumn> columns = Collections.EMPTY_LIST;

    // Extensions
    private final AutoResizeExtension autoResizeExtension = createAutoResizeExtension();

    public BnTable() {
        this.setSurrendersFocusOnKeystroke(true);        
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    /** {@inheritDoc} */
    public IListPM<? extends PresentationModel> getPresentationModel() {
        return this.presentationModel;
    }

    /** {@inheritDoc} */
    public void setPresentationModel(IListPM<? extends PresentationModel> pModel) {
        if (pModel == this.presentationModel) {
            return;
        }
        disconnect();

        this.presentationModel = pModel;

        connect();
    }

    /** {@inheritDoc} */
    public IModelProvider getModelProvider() {
        return link.getModelProvider();
    }

    /** {@inheritDoc} */
    public void setModelProvider(IModelProvider provider) {
        this.link.setModelProvider(provider);
    }

    /** {@inheritDoc} */
    public Path getPath() {
        return link.getPath();
    }

    /** {@inheritDoc} */
    public void setPath(Path path) {
        this.link.setPath(path);
    }

    /**
     * Returns whether this component is connected to the target
     * {@link AbstractPM} to synchronize with.
     * 
     * @return <code>true</code> when this component is connected, else
     *         <code>false</code>
     */
    private boolean isConnected() {
        return this.columns != null && this.presentationModel != null;
    }

    protected void connect() {
        // if (this.columns == null || this.listEditor == null) {
        // return;
        // }
        if (this.columns == null) {
            return;
        }
        final IListPM<? extends PresentationModel> currListMdl;
        if (this.presentationModel == null) {
            currListMdl = new ListPM<PresentationModel>();
        } else {
            currListMdl = this.presentationModel;
        }
        if (this.presentationModel != null) {
            this.presentationModel.addListListener(listListener);
        }

        this.setModel(new BnTableModel(currListMdl, this.columns));
        
        // install the row sorter        
        {
            // When intalling a row sorter in jre1.6 the selection model is cleared
            // To prevent this to change the presentation model we temporary install a dummy selection model
            int oldSelectionMode = getSelectionModel().getSelectionMode();
            setSelectionModel(createDefaultSelectionModel());
            setSelectionMode(oldSelectionMode);
        }
        installRowSorter();
        // now install the real selection model 
        int currentSelectionMode = getSelectionModel().getSelectionMode();
        BnTableSelectionModel newModel = new BnTableSelectionModel(currListMdl);
        newModel.setSelectionMode(currentSelectionMode);

        this.setSelectionModel(newModel);

        this.autoResizeExtension.resizeColumns();                
    }

    private void installRowSorter() {
        // TODO (mk) do not install the row sorter automatically.
        // TODO (mk) make this configurable (e.g. with a client property)
        if (isJava5()) {
            return;
        }
        LOG.debug("trying to install BnTableRowSorter");
        try {
            Class rsClass = Class.forName("org.beanfabrics.swing.table.BnTableRowSorter");
            Method install = rsClass.getMethod("install", new Class[] { BnTable.class });
            install.invoke(null, new Object[] { this });
        } catch (ClassNotFoundException ex) {
            // not found. Ok, we do not install the row sorter
            LOG.warn("Can't install BnTableRowSorter", ex);
        } catch (Exception ex) {
            throw new UndeclaredThrowableException(ex);
        }
    }

    private void uninstallRowSorter() {
        if (isJava5()) {
            return;
        }
        LOG.debug("uninstalling BnTableRowSorter");
        try {
            Class rsClass = Class.forName("org.beanfabrics.swing.table.BnTableRowSorter");
            Method uninstall = rsClass.getMethod("uninstall", new Class[] { BnTable.class });
            uninstall.invoke(null, new Object[] { this });
        } catch (ClassNotFoundException ex) {
            // not found. ignore.
            LOG.error("Can't uninstall BnTableRowSorter", ex);
        } catch (Exception ex) {
            throw new UndeclaredThrowableException(ex);
        }
    }

    /**
     * Returns the default table header object, which is dependent on the Java
     * version. In Java 5 a {@link Java5SortingTableHeader} is returned. In Java
     * 6 and later the standard {@link JTableHeader} is returned.
     * 
     * @return the default table header object
     * @see JTableHeader
     */
    @Override
    protected JTableHeader createDefaultTableHeader() {
        if (useJava5SortingTableHeader()) {
            Java5SortingTableHeader header = new Java5SortingTableHeader();
            header.setColumnModel(this.getColumnModel());
            return header;
        } else {
            return super.createDefaultTableHeader();
        }
    }

    private boolean useJava5SortingTableHeader() {
        return isJava5();
    }

    private boolean isJava5() {
        return System.getProperty("java.version").startsWith("1.5.");
    }

    @Override
    protected void createDefaultRenderers() {
        super.createDefaultRenderers();
        setDefaultRenderer(PresentationModel.class, new BnTableCellRenderer());
    }

    @Override
    protected void createDefaultEditors() {
        super.createDefaultEditors();
        setDefaultEditor(PresentationModel.class, new BnTableCellEditor());
    }

    protected void disconnect() {
        if (this.presentationModel != null) {
            this.presentationModel.removeListListener(listListener);
        }

        // process selection model
        ListSelectionModel selModel = getSelectionModel();
        int currentSelectionMode = selModel.getSelectionMode();
        if (selModel instanceof BnTableSelectionModel) {
            ((BnTableSelectionModel)selModel).dismiss();
        }
        setSelectionModel(createDefaultSelectionModel());
        getSelectionModel().setSelectionMode(currentSelectionMode);
        // process table model
        TableModel tblModel = getModel();
        if (tblModel instanceof BnTableModel) {
            ((BnTableModel)tblModel).dismiss();
        }
        setModel(createDefaultDataModel());

        uninstallRowSorter();
    }

    public BnColumn[] getColumns() {
        return this.columns.toArray(new BnColumn[this.columns.size()]);
    }

    public void addColumn(BnColumn newCol) {
        if (this.columns != null) {
            final List<BnColumn> list = new ArrayList<BnColumn>();
            list.add(newCol);
            this.setColumns(list.toArray(new BnColumn[list.size()]));
        } else {
            this.setColumns(new BnColumn[] { newCol });
        }
    }

    // public void setColumnsE( BnColumn ... newCols) { // (rk) VE won't work
    // with this param
    public void setColumns(BnColumn[] newCols) {
        BnColumn[] old = null;
        if (this.columns != null) {
            old = this.columns.toArray(new BnColumn[this.columns.size()]);
            this.disconnect();
        }
        this.columns = Arrays.asList(newCols);
        this.connect();
        super.firePropertyChange("columns", old, newCols);
    }

    @Override
    public void columnMarginChanged(ChangeEvent e) {
        super.columnMarginChanged(e);
        if (autoResizeExtension != null) {
            autoResizeExtension.columnMarginChanged(e);
        }
    }

    /* ------------ AutoResize ------------- */

    protected AutoResizeExtension createAutoResizeExtension() {
        return new AutoResizeExtension();
    }

    /**
     * Extension that auto-resizes the columns according to the settings of the
     * columns.
     * 
     * @author Michael Karneim
     */
    class AutoResizeExtension {
        private boolean pending_columnMarginChanged = false;

        AutoResizeExtension() {
            final ComponentListener parentComponentListener = new ComponentAdapter() {
                // this method is called whenever an enclosing scrollpane is
                // resized
                public void componentResized(ComponentEvent e) {
                    resizeColumns();
                }
            };

            BnTable.this.addHierarchyListener(new HierarchyListener() {
                private JViewport viewport;

                public void hierarchyChanged(HierarchyEvent e) {
                    if (viewport != null) {
                        viewport.removeComponentListener(parentComponentListener);
                    }
                    viewport = getEnclosingViewport();
                    if (viewport != null) {
                        viewport.addComponentListener(parentComponentListener);
                    }
                }
            });
        }

        /**
         * This extension is only enabled if the standard auto resize mode is
         * off.
         * 
         * @return <code>true</code> if this extension is enabled, else
         *         <code>false</code>
         */
        private boolean isEnabled() {
            return getAutoResizeMode() == JTable.AUTO_RESIZE_OFF && columns != null;
        }

        public void columnMarginChanged(ChangeEvent e) {
            if (pending_columnMarginChanged || !isEnabled()) {
                return;
            } else {
                pending_columnMarginChanged = true;
                try {
                    int toviewindex = getResizingColumnViewIndex();
                    for (int viewindex = 0; viewindex <= toviewindex; ++viewindex) {
                        int colIndex = convertColumnIndexToModel(viewindex);
                        columns.get(colIndex).setWidth(getColumnModel().getColumn(viewindex).getWidth());
                        columns.get(colIndex).setWidthFixed(true);
                    }
                    resizeColumns();
                } finally {
                    pending_columnMarginChanged = false;
                }
            }
        }

        int getResizingColumnViewIndex() {
            int result = -1;
            if (tableHeader != null) {
                TableColumn resizingColumn = tableHeader.getResizingColumn();
                if (resizingColumn != null) {
                    result = convertColumnIndexToView(resizingColumn.getModelIndex());
                }
            }
            return result;
        }

        public void resizeColumns() {
            if (!isEnabled()) {
                return;
            }
            // how much space is there to fill?
            int totalWidth = getTotalWidth();
            if (totalWidth <= 0) {
                return; // no space to distribute
            }
            // how much space do all columns prefer in total?
            int totalPreferredWith = getTotalPreferredWidth();
            // is there space left?
            int spaceLeft = totalWidth - totalPreferredWith;
            // if so, how much space can we give each column?
            int additionalSpacePerColumn = 0;
            if (spaceLeft > 0 && getColumnCount() - getColumnsWithFixedWidth() > 0) {
                additionalSpacePerColumn = spaceLeft / (getColumnCount() - getColumnsWithFixedWidth());
            }
            // is there still space left?
            // this space we will give to the last column
            int stillSpaceLeft = spaceLeft - additionalSpacePerColumn * (getColumnCount() - getColumnsWithFixedWidth());

            // now distribute the space over all columns
            //			int resizingColumnViewIndex = getResizingColumnViewIndex();
            // TODO (mk) use resizingColumnViewIndex
            for (int viewIndex = 0; viewIndex < getColumnCount(); ++viewIndex) {
                TableColumn col = getColumnModel().getColumn(viewIndex);
                int modelIndex = col.getModelIndex();
                int delta = 0;
                if (viewIndex == getColumnCount() - 1 && stillSpaceLeft > 0) {
                    delta = stillSpaceLeft;
                }
                BnColumn colAtIndex = columns.get(modelIndex);
                if (colAtIndex.isWidthFixed() == false) {
                    int newWidth = colAtIndex.getWidth() + additionalSpacePerColumn + delta;
                    this.setColumnWidth(viewIndex, newWidth);
                } else {
                    int prefWidth = colAtIndex.getWidth() + delta;
                    this.setColumnWidth(viewIndex, prefWidth);
                }
            }
        }

        int getTotalWidth() {
            Component parent = getParent();
            if (parent == null || parent instanceof JViewport == false) {
                return -1;
            }
            int result = ((JViewport)parent).getSize().width;
            return result;
        }

        int getColumnsWithFixedWidth() {
            int count = 0;
            for (BnColumn col : columns) {
                if (col.isWidthFixed()) {
                    count++;
                }
            }
            return count;
        }

        int getTotalPreferredWidth() {
            int sum = 0;
            for (BnColumn col : columns) {
                sum = sum + col.getWidth();
            }
            return sum;
        }

        void setColumnWidth(int columnViewIndex, int width) {
            final TableColumn col = getColumnModel().getColumn(columnViewIndex);
            final int minw = col.getMinWidth();
            final int maxw = col.getMaxWidth();
            if (col.getWidth() == width) {
                return;
            }
            col.setMinWidth(width);
            col.setMaxWidth(width);
            col.setWidth(width);
            if (minw > width) {
                col.setMinWidth(width);
            }
            // default min. of TableColumn = 15 => if width < 15 => display
            // errors when columns get resized
            else {
                col.setMinWidth(minw);
            }
            col.setMaxWidth(maxw);
        }

        JViewport getEnclosingViewport() {
            Container p = getParent();
            if (p instanceof JViewport) {
                Container gp = p.getParent();
                if (gp instanceof JScrollPane) {
                    JScrollPane scrollPane = (JScrollPane)gp;
                    // Make certain we are the viewPort's view and not, for
                    // example, the rowHeaderView of the scrollPane -
                    // an implementor of fixed columns might do this.
                    JViewport viewport = scrollPane.getViewport();
                    if (viewport == null || viewport.getView() != BnTable.this) {
                        return null;
                    }
                    return viewport;
                }
            }
            return null;
        }
    }
}