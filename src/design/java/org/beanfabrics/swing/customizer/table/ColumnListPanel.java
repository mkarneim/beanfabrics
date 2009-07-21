/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.table.BnTable;

/**
 * The <code>ColumnListPanel</code> is a view on a {@link ColumnListPM}.
 * 
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
@SuppressWarnings("serial")
public class ColumnListPanel extends JPanel implements View<ColumnListPM>, ModelSubscriber {
    private BnButton moveDownButton;
    private BnButton moveUpButton;
    private BnButton removeButton;
    private BnButton addButton;
    private JPanel buttonPanel;
    private BnTable bnTable;
    private JScrollPane scrollPane;
    private JPanel panel;
    private final Link link = new Link(this);
    private ModelProvider localProvider;

    /**
     * Constructs a new <code>ColumnListPanel</code>.
     */
    public ColumnListPanel() {
        super();
        setLayout(new BorderLayout());
        add(getPanel(), BorderLayout.CENTER);
        //
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
     * 
     * @return the local <code>ModelProvider</code>
     */
    protected ModelProvider getLocalProvider() {
        if (localProvider == null) {
            localProvider = new ModelProvider(); // @wb:location=10,430
            localProvider.setPresentationModelType(ColumnListPM.class);
        }
        return localProvider;
    }

    /** {@inheritDoc} */
    public ColumnListPM getPresentationModel() {
        return getLocalProvider().getPresentationModel();
    }

    /** {@inheritDoc} */
    public void setPresentationModel(ColumnListPM pModel) {
        getLocalProvider().setPresentationModel(pModel);
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

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(getScrollPane(), BorderLayout.CENTER);
            panel.add(getButtonPanel(), BorderLayout.EAST);
            panel.setOpaque(false);
        }
        return panel;
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(getBnTable());
            scrollPane.getViewport().setBackground(getBnTable().getBackground());
        }
        return scrollPane;
    }

    private BnTable getBnTable() {
        if (bnTable == null) {
            bnTable = new BnTable();
            bnTable.setColumns(new org.beanfabrics.swing.table.BnColumn[] { new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this.path"), "Path", 120, false, new org.beanfabrics.Path("this.path.choosePath")),
                    new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this.columnName"), "ColumnName", 100, true), new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this.width"), "Width", 55, true),
                    new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this.fixedWidth"), "fixed Width", 45, true),
                    new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this.operationPath"), "OperationPM", 80, true, new org.beanfabrics.Path("this.operationPath.choosePath")) });
            bnTable.setPath(new org.beanfabrics.Path("this"));
            bnTable.setModelProvider(getLocalProvider());
            // Customize rendering
            bnTable.setBackground(Color.white);
            bnTable.setGridColor(Color.white);
            bnTable.setIntercellSpacing(new Dimension(0, 0));
        }
        return bnTable;
    }

    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            final GridBagLayout gridBagLayout = new GridBagLayout();
            gridBagLayout.rowHeights = new int[] { 0, 7, 7, 7, 7 };
            buttonPanel.setLayout(gridBagLayout);
            final GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.insets = new Insets(4, 4, 4, 4);
            buttonPanel.add(getAddButton(), gridBagConstraints);
            final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
            gridBagConstraints_1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints_1.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_1.gridy = 1;
            gridBagConstraints_1.gridx = 0;
            buttonPanel.add(getRemoveButton(), gridBagConstraints_1);
            buttonPanel.setOpaque(false);
            final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
            gridBagConstraints_3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints_3.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_3.gridy = 2;
            gridBagConstraints_3.gridx = 0;
            buttonPanel.add(getMoveUpButton(), gridBagConstraints_3);
            final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
            gridBagConstraints_4.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints_4.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_4.gridy = 3;
            gridBagConstraints_4.gridx = 0;
            buttonPanel.add(getMoveDownButton(), gridBagConstraints_4);
        }
        return buttonPanel;
    }

    private BnButton getAddButton() {
        if (addButton == null) {
            addButton = new BnButton();
            addButton.setPath(new org.beanfabrics.Path("this.addColumn"));
            addButton.setModelProvider(getLocalProvider());
            addButton.setText("Add ...");
            setTexturedButtonType(addButton);
        }
        return addButton;
    }

    private BnButton getRemoveButton() {
        if (removeButton == null) {
            removeButton = new BnButton();
            removeButton.setPath(new org.beanfabrics.Path("this.removeColumns"));
            removeButton.setModelProvider(getLocalProvider());
            removeButton.setText("Remove");
            setTexturedButtonType(removeButton);
        }
        return removeButton;
    }

    protected BnButton getMoveUpButton() {
        if (moveUpButton == null) {
            moveUpButton = new BnButton();
            moveUpButton.setPath(new org.beanfabrics.Path("this.moveUp"));
            moveUpButton.setModelProvider(getLocalProvider());
            moveUpButton.setText("Up");
            setTexturedButtonType(moveUpButton);
        }
        return moveUpButton;
    }

    protected BnButton getMoveDownButton() {
        if (moveDownButton == null) {
            moveDownButton = new BnButton();
            moveDownButton.setPath(new org.beanfabrics.Path("this.moveDown"));
            moveDownButton.setModelProvider(getLocalProvider());
            moveDownButton.setText("Down");
            setTexturedButtonType(moveDownButton);
        }
        return moveDownButton;
    }

    /////

    private static void setTexturedButtonType(AbstractButton button) {
        // see http://developer.apple.com/technotes/tn2007/tn2196.html
        button.putClientProperty("JButton.buttonType", "gradient");
    }
}