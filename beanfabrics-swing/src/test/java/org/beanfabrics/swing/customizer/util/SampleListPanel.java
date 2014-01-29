package org.beanfabrics.swing.customizer.util;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import javax.swing.JScrollPane;
import org.beanfabrics.swing.table.BnTable;
import org.beanfabrics.swing.table.BnColumnBuilder;

/**
 * The {@link SampleListPanel} is a {@link View} on a {@link SampleListPM}.
 * 
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
@SuppressWarnings("serial")
public class SampleListPanel extends JPanel implements View<SampleListPM>, ModelSubscriber {
    private final Link link = new Link(this);
    private ModelProvider localModelProvider;
    private JScrollPane scrollPane;
    private BnTable bnTable;

    /**
     * Constructs a new <code>SampleListPanel</code>.
     */
    public SampleListPanel() {
        super();
        setLayout(new BorderLayout());
        add(getScrollPane(), BorderLayout.CENTER);
        //
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
     * 
     * @return the local <code>ModelProvider</code>
     * @wbp.nonvisual location=10,430
     */
    protected ModelProvider getLocalModelProvider() {
        if (localModelProvider == null) {
            localModelProvider = new ModelProvider(); // @wb:location=10,430
            localModelProvider.setPresentationModelType(SampleListPM.class);
        }
        return localModelProvider;
    }

    /** {@inheritDoc} */
    public SampleListPM getPresentationModel() {
        return getLocalModelProvider().getPresentationModel();
    }

    /** {@inheritDoc} */
    public void setPresentationModel(SampleListPM pModel) {
        getLocalModelProvider().setPresentationModel(pModel);
    }

    /** {@inheritDoc} */
    public IModelProvider getModelProvider() {
        return this.link.getModelProvider();
    }

    /** {@inheritDoc} */
    public void setModelProvider(IModelProvider modelProvider) {
        this.link.setModelProvider(modelProvider);
    }

    /** {@inheritDoc} */
    public Path getPath() {
        return this.link.getPath();
    }

    /** {@inheritDoc} */
    public void setPath(Path path) {
        this.link.setPath(path);
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
        	scrollPane = new JScrollPane();
        	scrollPane.setViewportView(getBnTable());
        }
        return scrollPane;
    }
    private BnTable getBnTable() {
        if (bnTable == null) {
        	bnTable = new BnTable();
        	bnTable.setPath(new Path("this"));
        	bnTable.setColumns(new BnColumnBuilder()
        	          .addColumn().withPath("this.colA").withName("ColA")
        	          .addColumn().withPath("this.colB").withName("ColB")
        	          .build());
        	bnTable.setModelProvider(getLocalModelProvider());
        }
        return bnTable;
    }
}