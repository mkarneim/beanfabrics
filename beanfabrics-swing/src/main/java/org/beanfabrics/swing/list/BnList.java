/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.list;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IListPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.list.cellrenderer.BnListCellRenderer;

/**
 * The <code>BnList</code> is a {@link JList} that can subscribe to an
 * {@link IListPM}.
 * 
 * @author Max Gensthaler
 * @author Michael Karneim
 * @beaninfo
 */
@SuppressWarnings("serial")
public class BnList extends JList implements View<IListPM>, ModelSubscriber {
    private final Link link = new Link(this);
    private IListPM<? extends PresentationModel> presentationModel;
    private CellConfig cellConfig;

    /**
     * Constructs a <code>BnList</code> with a default pM renderer.
     */
    public BnList() {
        this.setCellRenderer(this.createDefaultCellRenderer());
    }

    /**
     * Creates a new default pM renderer.
     * 
     * @return the new default pM renderer
     */
    protected ListCellRenderer createDefaultCellRenderer() {
        return new BnListCellRenderer();
    }

    /** {@inheritDoc} */
    public IListPM getPresentationModel() {
        return this.presentationModel;
    }

    /** {@inheritDoc} */
    public void setPresentationModel(IListPM newModel) {
        IListPM oldModel = this.presentationModel;
        if (newModel == oldModel) {
            return;
        }
        disconnect();
        this.presentationModel = newModel;
        connect();
        this.firePropertyChange("presentationModel", oldModel, newModel);
    }

    /** {@inheritDoc} */
    public IModelProvider getModelProvider() {
        return this.link.getModelProvider();
    }

    /** {@inheritDoc} */
    public void setModelProvider(IModelProvider provider) {
        this.link.setModelProvider(provider);
    }

    /** {@inheritDoc} */
    public Path getPath() {
        return this.link.getPath();
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
        return this.presentationModel != null;
    }

    public CellConfig getCellConfig() {
        return cellConfig;
    }

    public void setCellConfig(CellConfig cellConfig) {
        disconnect();
        this.cellConfig = cellConfig;
        connect();
    }

    protected void connect() {
        final IListPM currListMdl;
        if (this.presentationModel == null) {
            currListMdl = new ListPM();
        } else {
            currListMdl = this.presentationModel;
        }
        CellConfig currCellConfig;
        if (this.cellConfig == null) {
            currCellConfig = new CellConfig(new Path("this"));
        } else {
            currCellConfig = this.cellConfig;
        }
        this.setModel(new BnListModel(currListMdl, currCellConfig));

        int currentSelectionMode = getSelectionModel().getSelectionMode();
        BnListSelectionModel newModel = new BnListSelectionModel(currListMdl);
        newModel.setSelectionMode(currentSelectionMode);

        this.setSelectionModel(newModel);
    }

    protected void disconnect() {
        // process selection model
        ListSelectionModel selModel = getSelectionModel();
        int currentSelectionMode = selModel.getSelectionMode();
        if (selModel instanceof BnListSelectionModel) {
            ((BnListSelectionModel)selModel).dismiss();
        }
        this.setSelectionModel(this.createSelectionModel());
        getSelectionModel().setSelectionMode(currentSelectionMode);
        // process list model
        ListModel listModel = getModel();
        if (listModel instanceof BnListModel) {
            ((BnListModel)listModel).dismiss();
        }
        this.setModel(new DefaultListModel());
    }
}