/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Validation;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.BnTextField;
import org.beanfabrics.swing.table.BnTable;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ListPMWithChildrenTestGUI extends JFrame {
    private BnTextField bnTextField_2;
    private BnButton populateBnButton;
    private BnTextField bnTextField_1;
    private BnButton removeBnButton;
    private BnTextField bnTextField;
    private JPanel panel_1;
    private ModelProvider localProvider;
    private BnTable bnTable;
    private JScrollPane scrollPane;
    private JPanel panel;

    public static class ContactRow extends AbstractPM {
        TextPM name = new TextPM();

        public ContactRow() {
            PMManager.setup(this);
            name.setEditable(false);
        }

        public ContactRow(String name) {
            this();
            this.name.setText(name);
        }
    }

    public static class ContactListPM extends ListPM<ContactRow> {
        IntegerPM size = new IntegerPM();
        IntegerPM selectionSize = new IntegerPM();
        OperationPM removeSelected = new OperationPM();

        public ContactListPM() {
            PMManager.setup(this);
            size.setEditable(false);
            selectionSize.setEditable(false);
            populate();
        }

        void populate() {
            for (int i = 0; i < 10; ++i) {
                this.add(new ContactRow("Zeile " + i));
            }
        }

        @OnChange(path = "this")
        void uddateSizes() {
            size.setInteger(this.size());
            selectionSize.setInteger(this.getSelection().size());
        }

        @Operation
        void removeSelected() {
            this.removeAll(this.getSelection());
        }

        @Validation(path = "removeSelected", message = "To remove please select an entry")
        boolean canRemove() {
            return this.getSelection().isEmpty() == false;
        }

    }

    public static class BrowserModel extends AbstractPM {
        ContactListPM contacts = new ContactListPM();
        OperationPM populate = new OperationPM();
        IntegerPM numberOfChanges = new IntegerPM();

        public BrowserModel() {
            numberOfChanges.setInteger(0);
            PMManager.setup(this);
        }

        @Operation
        public void populate() {
            contacts.populate();
        }

        @OnChange(path = "contacts")
        void updateNumberOfChanges() {
            this.numberOfChanges.setInteger(this.numberOfChanges.getInteger() + 1);
        }
    }

    /**
     * Launch the application
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ListPMWithChildrenTestGUI frame = new ListPMWithChildrenTestGUI();
                    frame.localProvider.setPresentationModel(new BrowserModel());
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame
     */
    public ListPMWithChildrenTestGUI() {
        super();
        setBounds(100, 100, 500, 375);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(getPanel(), BorderLayout.CENTER);
        //
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(getScrollPane(), BorderLayout.CENTER);
            panel.add(getPanel_1(), BorderLayout.NORTH);
        }
        return panel;
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
            bnTable.setPath(new org.beanfabrics.Path("this.contacts"));
            bnTable.setColumns(new org.beanfabrics.swing.table.BnColumn[] { new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this.name"), "Name", 100, false) });
            bnTable.setModelProvider(localProvider);
        }
        return bnTable;
    }

    private ModelProvider getLocalProvider() {
        if (localProvider == null) {
            localProvider = new ModelProvider(); // @wb:location=46,463
            localProvider.setPresentationModelType(BrowserModel.class);
        }
        return localProvider;
    }

    private JPanel getPanel_1() {
        if (panel_1 == null) {
            panel_1 = new JPanel();
            panel_1.add(getPopulateBnButton());
            panel_1.add(getRemoveBnButton());
            panel_1.add(getBnTextField());
            panel_1.add(getBnTextField_1());
            panel_1.add(getBnTextField_2());
        }
        return panel_1;
    }

    private BnTextField getBnTextField() {
        if (bnTextField == null) {
            bnTextField = new BnTextField();
            bnTextField.setPath(new Path("this.contacts.size"));
            bnTextField.setModelProvider(getLocalProvider());
            bnTextField.setColumns(3);
        }
        return bnTextField;
    }

    private BnButton getRemoveBnButton() {
        if (removeBnButton == null) {
            removeBnButton = new BnButton();
            removeBnButton.setModelProvider(getLocalProvider());
            removeBnButton.setText("Remove");
            removeBnButton.setPath(new Path("contacts.removeSelected"));
        }
        return removeBnButton;
    }

    private BnTextField getBnTextField_1() {
        if (bnTextField_1 == null) {
            bnTextField_1 = new BnTextField();
            bnTextField_1.setPath(new org.beanfabrics.Path("this.contacts.selectionSize"));
            bnTextField_1.setModelProvider(getLocalProvider());
            bnTextField_1.setColumns(3);
        }
        return bnTextField_1;
    }

    private BnButton getPopulateBnButton() {
        if (populateBnButton == null) {
            populateBnButton = new BnButton();
            populateBnButton.setPath(new org.beanfabrics.Path("this.populate"));
            populateBnButton.setModelProvider(getLocalProvider());
            populateBnButton.setText("Populate");
        }
        return populateBnButton;
    }

    private BnTextField getBnTextField_2() {
        if (bnTextField_2 == null) {
            bnTextField_2 = new BnTextField();
            bnTextField_2.setPath(new org.beanfabrics.Path("this.numberOfChanges"));
            bnTextField_2.setModelProvider(getLocalProvider());
            bnTextField_2.setColumns(3);
        }
        return bnTextField_2;
    }
}