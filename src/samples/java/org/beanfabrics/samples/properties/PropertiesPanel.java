package org.beanfabrics.samples.properties;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnLabel;
import org.beanfabrics.swing.table.BnTable;

/*
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
@SuppressWarnings("serial")
public class PropertiesPanel extends JPanel implements View<PropertiesPM>, ModelSubscriber {
    private BnLabel statusLabel;
    private JPanel footerPanel;
    private BnTable bnTable;
    private JScrollPane scrollPane;
    private JPanel centerPanel;
    private final Link link = new Link(this);
    private ModelProvider localModelProvider;

    /**
     * Constructs a new <code>PropertiesPanel</code>.
     */
    public PropertiesPanel() {
        super();
        setLayout(new BorderLayout());
        add(getCenterPanel(), BorderLayout.CENTER);
        //
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
     * 
     * @return the local <code>ModelProvider</code>
     */
    protected ModelProvider getLocalModelProvider() {
        if (localModelProvider == null) {
            localModelProvider = new ModelProvider(); // @wb:location=10,430
            localModelProvider.setPresentationModelType(PropertiesPM.class);
        }
        return localModelProvider;
    }

    /** {@inheritDoc} */
    public PropertiesPM getPresentationModel() {
        return getLocalModelProvider().getPresentationModel();
    }

    /** {@inheritDoc} */
    public void setPresentationModel(PropertiesPM pModel) {
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

    /**
     * @return
     */
    private JPanel getCenterPanel() {
        if (centerPanel == null) {
            centerPanel = new JPanel();
            centerPanel.setLayout(new BorderLayout());
            centerPanel.add(getScrollPane(), BorderLayout.CENTER);
            centerPanel.add(getFooterPanel(), BorderLayout.SOUTH);
        }
        return centerPanel;
    }

    /**
     * @return
     */
    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(getBnTable());
            scrollPane.getViewport().setBackground(getBnTable().getBackground());
        }
        return scrollPane;
    }

    /**
     * @return
     */
    private BnTable getBnTable() {
        if (bnTable == null) {
            bnTable = new BnTable();
            bnTable.setPath(new org.beanfabrics.Path("this"));
            bnTable.setColumns(new org.beanfabrics.swing.table.BnColumn[] { new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this.key"), "Key", 200, true),
                    new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this.value"), "Value", 100, false) });
            bnTable.setModelProvider(getLocalModelProvider());
            bnTable.setBackground(Color.WHITE);
            bnTable.setShowVerticalLines(true);
            bnTable.setShowHorizontalLines(false);
            bnTable.setIntercellSpacing(new Dimension(0, 0));
        }
        return bnTable;
    }

    /**
     * @return
     */
    private JPanel getFooterPanel() {
        if (footerPanel == null) {
            footerPanel = new JPanel();
            footerPanel.setLayout(new BorderLayout());
            footerPanel.add(getStatusLabel());
        }
        return footerPanel;
    }

    /**
     * @return
     */
    private BnLabel getStatusLabel() {
        if (statusLabel == null) {
            statusLabel = new BnLabel();
            statusLabel.setPath(new org.beanfabrics.Path("this.status"));
            statusLabel.setModelProvider(getLocalModelProvider());
        }
        return statusLabel;
    }

}