/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.BnLabel;
import org.beanfabrics.swing.BnTextField;
import org.beanfabrics.swing.table.BnTable;

@SuppressWarnings("serial")
public class ContactListFrame extends JFrame {
    private BnButton insertBnButton;
    private BnButton doSomethingBnButton;
    private BnButton runGcBnButton;
    private BnLabel bnLabel_1;

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(new FlowLayout());
            jPanel.add(getInsertBnButton());
            jPanel.add(getBnButton(), null);
            jPanel.add(getBnTextField(), null);
            jPanel.add(getContactFilterPanel(), null);
            jPanel.add(getDoSomethingBnButton());
        }
        return jPanel;
    }

    /**
     * This method initializes bnButton
     * 
     * @return org.beanfabrics.gui.swing.BnButton
     */
    private BnButton getBnButton() {
        if (bnButton == null) {
            bnButton = new BnButton();
            bnButton.setPath(new Path("addContact"));
            bnButton.setText("Add");
            bnButton.setModelProvider(getLocalProvider());
        }
        return bnButton;
    }

    /**
     * This method initializes jPanel1
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            jPanel1 = new JPanel();
            jPanel1.setLayout(new BorderLayout());
            jPanel1.add(getJTabbedPane(), BorderLayout.CENTER);
        }
        return jPanel1;
    }

    /**
     * This method initializes jTabbedPane
     * 
     * @return javax.swing.JTabbedPane
     */
    private JTabbedPane getJTabbedPane() {
        if (jTabbedPane == null) {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.addTab("Contact", null, getContactPanel(), null);
            jTabbedPane.addTab("Address", null, getAddressPanel(), null);
        }
        return jTabbedPane;
    }

    /**
     * This method initializes addressPanel
     * 
     * @return org.beanfabrics.test.AddressPanel
     */
    private AddressPanel getAddressPanel() {
        if (addressPanel == null) {
            addressPanel = new AddressPanel();
            addressPanel.setPath(new Path("selectedContact.address"));
            addressPanel.setModelProvider(getLocalProvider());
        }
        return addressPanel;
    }

    /**
     * This method initializes jPanel2
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            bnLabel = new BnLabel();
            bnLabel.setPath(new Path("numberOfContacts"));
            bnLabel.setModelProvider(getLocalProvider());
            jPanel2 = new JPanel();
            jPanel2.setLayout(new FlowLayout());
            jPanel2.add(bnLabel, null);
            jPanel2.add(getRunGcBnButton());
            jPanel2.add(getBnLabel_1());
        }
        return jPanel2;
    }

    /**
     * This method initializes bnTextField
     * 
     * @return org.beanfabrics.gui.swing.BnTextField
     */
    private BnTextField getBnTextField() {
        if (bnTextField == null) {
            bnTextField = new BnTextField();
            bnTextField.setColumns(5);
            bnTextField.setPath(new Path("numberOfContactsToCreate"));
            bnTextField.setModelProvider(getLocalProvider());
        }
        return bnTextField;
    }

    /**
     * This method initializes contactFilterPanel
     * 
     * @return org.beanfabrics.test.ContactFilterPanel
     */
    private ContactFilterPanel getContactFilterPanel() {
        if (contactFilterPanel == null) {
            contactFilterPanel = new ContactFilterPanel();
            contactFilterPanel.setPath(new Path("filter"));
            contactFilterPanel.setModelProvider(getLocalProvider());
        }
        return contactFilterPanel;
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.UK);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                ContactListFrame f = new ContactListFrame();
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
                f.provider.setPresentationModel(new ContactMapPM());
            }
        });
    }

    private ModelProvider provider = null; //  @jve:decl-index=0:visual-constraint="546,14"
    private JPanel jContentPane = null;
    private JSplitPane jSplitPane = null;
    private JScrollPane jScrollPane = null;
    private BnTable bnTable = null;
    private ContactPanel contactPanel = null;
    private JPanel jPanel = null;
    private BnButton bnButton = null;
    private JPanel jPanel1 = null;
    private JTabbedPane jTabbedPane = null;
    private AddressPanel addressPanel = null;
    private JPanel jPanel2 = null;
    private BnLabel bnLabel = null;
    private BnTextField bnTextField = null;
    private ContactFilterPanel contactFilterPanel = null;

    /**
     * This is the default constructor
     */
    public ContactListFrame() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(599, 288);
        this.setContentPane(getJContentPane());
        this.setTitle("JFrame");
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJSplitPane(), BorderLayout.CENTER);
            jContentPane.add(getJPanel(), BorderLayout.NORTH);
            jContentPane.add(getJPanel2(), BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    /**
     * This method initializes jSplitPane
     * 
     * @return javax.swing.JSplitPane
     */
    private JSplitPane getJSplitPane() {
        if (jSplitPane == null) {
            jSplitPane = new JSplitPane();
            jSplitPane.setDividerLocation(300);
            jSplitPane.setRightComponent(getJPanel1());
            jSplitPane.setLeftComponent(getJScrollPane());
        }
        return jSplitPane;
    }

    /**
     * This method initializes jScrollPane
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
     * This method initializes bnTable
     * 
     * @return org.beanfabrics.gui.swing.table.BnTable
     */
    private BnTable getBnTable() {
        if (bnTable == null) {
            bnTable = new BnTable();
            //bnTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            bnTable.setPath(new Path("elements"));
            //bnTable.setPath(new Path("elementsList"));
            bnTable.setModelProvider(getLocalProvider());
            bnTable.setColumns(new org.beanfabrics.swing.table.BnColumn[] { new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this.icon"), "", 25, true),
                    new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("lastname"), "Name", 100, false), new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("birthday"), "Birthday", 80, true),
                    new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("address.street"), "Street", 100, false), new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this.children"), "Children", 100, false),
                    new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this.notes.content"), "Notes", 100, false) });
        }
        return bnTable;
    }

    /**
     * This method initializes contactPanel
     * 
     * @return org.beanfabrics.test.ContactPanel
     */
    private ContactPanel getContactPanel() {
        if (contactPanel == null) {
            contactPanel = new ContactPanel();
            contactPanel.setPath(new Path("this.selectedContact"));
            contactPanel.setModelProvider(getLocalProvider());
        }
        return contactPanel;
    }

    /**
     * This method initializes provider
     * 
     * @return the <code>ModelProvider</code>
     */
    private ModelProvider getLocalProvider() {
        if (provider == null) {
            provider = new ModelProvider(); // @wb:location=63,314
            provider.setPresentationModelType(org.beanfabrics.test.ContactMapPM.class);
        }
        return provider;
    }

    protected BnLabel getBnLabel_1() {
        if (bnLabel_1 == null) {
            bnLabel_1 = new BnLabel();
            bnLabel_1.setPath(new Path("memory"));
            bnLabel_1.setModelProvider(getLocalProvider());
        }
        return bnLabel_1;
    }

    protected BnButton getRunGcBnButton() {
        if (runGcBnButton == null) {
            runGcBnButton = new BnButton();
            runGcBnButton.setPath(new Path("runGC"));
            runGcBnButton.setModelProvider(getLocalProvider());
            runGcBnButton.setText("Run GC");
        }
        return runGcBnButton;
    }

    protected BnButton getDoSomethingBnButton() {
        if (doSomethingBnButton == null) {
            doSomethingBnButton = new BnButton();
            doSomethingBnButton.setPath(new org.beanfabrics.Path("this.doSomething"));
            doSomethingBnButton.setModelProvider(getLocalProvider());
            doSomethingBnButton.setText("Do Something");
        }
        return doSomethingBnButton;
    }

    private BnButton getInsertBnButton() {
        if (insertBnButton == null) {
            insertBnButton = new BnButton();
            insertBnButton.setPath(new org.beanfabrics.Path("this.insertContacts"));
            insertBnButton.setModelProvider(getLocalProvider());
            insertBnButton.setText("Insert");
        }
        return insertBnButton;
    }
}