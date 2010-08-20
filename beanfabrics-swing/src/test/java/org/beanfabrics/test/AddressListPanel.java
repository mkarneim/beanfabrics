/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.swing.table.BnTable;
import org.beanfabrics.swing.table.BnColumnBuilder;

public class AddressListPanel extends JPanel implements View<AddressListPM>, ModelSubscriber {
    private final Link link = new Link(this);
    private JPanel buttonPnl = null;
    private BnButton bnButton = null;
    private JScrollPane jScrollPane = null;
    private BnTable bnTable = null;
    private ModelProvider localModelProvider = null; //@jve:decl-index=0:visual-constraint
    // ="441,27"
    private JSplitPane jSplitPane = null;
    private AddressPanel addressPanel = null;

    /**
     * This is the default constructor
     */
    public AddressListPanel() {
        super();
        init();
        getLocalModelProvider().setPresentationModel(new AddressListPM());
    }

    /** {@inheritDoc} */
    public AddressListPM getPresentationModel() {
        return getLocalModelProvider().getPresentationModel();
    }

    /** {@inheritDoc} */
    public void setPresentationModel(AddressListPM model) {
        getLocalModelProvider().setPresentationModel(model);
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
     * This method initializes this instance.
     */
    private void init() {
        this.setSize(412, 260);
        this.setLayout(new BorderLayout());
        this.add(getButtonPnl(), BorderLayout.NORTH);
        this.add(getJSplitPane(), BorderLayout.CENTER);
    }

    /**
     * This method initializes buttonPnl.
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getButtonPnl() {
        if (buttonPnl == null) {
            buttonPnl = new JPanel();
            buttonPnl.setLayout(new FlowLayout());
            buttonPnl.add(getBnButton(), null);
        }
        return buttonPnl;
    }

    /**
     * This method initializes bnButton.
     * 
     * @return org.beanfabrics.gui.swing.BnButton
     */
    private BnButton getBnButton() {
        if (bnButton == null) {
            bnButton = new BnButton();
            bnButton.setPath(new Path("addSome"));
            bnButton.setModelProvider(getLocalModelProvider());
        }
        return bnButton;
    }

    /**
     * This method initializes jScrollPane.
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getBnTable());
        }
        return jScrollPane;
    }

    /**
     * This method initializes bnTable.
     * 
     * @return org.beanfabrics.gui.swing.table.BnTable
     */
    private BnTable getBnTable() {
        if (bnTable == null) {
            bnTable = new BnTable();
            bnTable.setPath(new Path("elements"));
            bnTable.setModelProvider(getLocalModelProvider());
            bnTable.setColumns(new BnColumnBuilder()
            	      .addColumn().withPath("appartment").withName("Appartment")
            	      .addColumn().withPath("street").withName("Street")
            	      .addColumn().withPath("city").withName("City")
            	      .addColumn().withPath("zip").withName("ZIP")
            	      .addColumn().withPath("country").withName("Country")
            	      .build());
        }
        return bnTable;
    }

    /**
     * This method initializes <code>myDataSource</code>.
     * 
     * @return the <code>ModelProvider</code>
     * @wbp.nonvisual location=10,430
     */
    private ModelProvider getLocalModelProvider() {
        if (localModelProvider == null) {
            localModelProvider = new ModelProvider();
            localModelProvider.setPresentationModelType(org.beanfabrics.test.AddressListPM.class);
        }
        return localModelProvider;
    }

    /**
     * This method initializes jSplitPane.
     * 
     * @return javax.swing.JSplitPane
     */
    private JSplitPane getJSplitPane() {
        if (jSplitPane == null) {
            jSplitPane = new JSplitPane();
            jSplitPane.setDividerLocation(200);
            jSplitPane.setRightComponent(getAddressPanel());
            jSplitPane.setLeftComponent(getJScrollPane());
        }
        return jSplitPane;
    }

    /**
     * This method initializes addressPanel.
     * 
     * @return org.beanfabrics.test.AddressPanel
     */
    private AddressPanel getAddressPanel() {
        if (addressPanel == null) {
            addressPanel = new AddressPanel();
            addressPanel.setPath(new Path("selected"));
            addressPanel.setModelProvider(getLocalModelProvider());
        }
        return addressPanel;
    }
}