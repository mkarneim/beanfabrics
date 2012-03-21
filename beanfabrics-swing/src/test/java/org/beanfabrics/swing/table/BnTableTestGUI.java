/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.table;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractOperationPM;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.DatePM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.Options;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.swing.BnButton;

/**
 * @author Michael Karneim
 */
public class BnTableTestGUI extends JFrame {
    private JPanel panel_1;

    /**
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                BnTableTestGUI thisClass = new BnTableTestGUI();
                thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                thisClass.setLocationRelativeTo(null);
                thisClass.setVisible(true);
            }
        });
    }

    public static class PersonModel extends AbstractPM {
        TextPM name = new TextPM();
        DatePM birthdate = new DatePM();
        BooleanPM active = new BooleanPM();
        TextPM eyeColor = new TextPM();

        public PersonModel() {
            Options<String> colors = Options.create("black","brown","blue","green");
            eyeColor.setOptions(colors);
            PMManager.setup(this);
            name.setEditable(false);
        }
    }

    public static class GroupModel extends AbstractPM {
        ListPM<PersonModel> persons = new ListPM<PersonModel>();
        IOperationPM remove = new AbstractOperationPM() {
            public boolean execute()
                throws Throwable {
                GroupModel.this.remove();
                return true; // success
            }
        };

        public GroupModel() {
            PMManager.setup(this);
            this.populate();
        }

        private void populate() {
            for (int i = 0; i < 5; ++i) {
                PersonModel p = new PersonModel();
                p.name.setText("Name " + i);
                p.birthdate.setDate(new Date());
                persons.add(p);
            }
        }

        public void remove() {
            this.remove.check();
            persons.removeAll(persons.getSelection());
        }
    }

    private ModelProvider provider = null;
    private JPanel jContentPane = null;
    private JPanel jPanel = null;
    private JScrollPane jScrollPane = null;
    private BnTable bnTable = null;
    private BnButton bnButton = null;

    private GroupModel pModel = new GroupModel(); // @wb:location=573,142

    /**
     * This is the default constructor
     */
    public BnTableTestGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialize();
        this.getBnButton().setPath(new Path("remove"));
    }

    /**
     * This method initializes jPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.anchor = GridBagConstraints.EAST;
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(getBnButton(), gridBagConstraints);
        }
        return jPanel;
    }

    /**
     * This method initializes the {@link ModelProvider}.
     * 
     * @wbp.nonvisual location=570,76
     */
    private ModelProvider getLocalProvider() {
        if (provider == null) {
            provider = new ModelProvider(); // @wb:location=570,76
            provider.setPresentationModel(pModel);
        }
        return provider;
    }

    /**
     * This method initializes jScrollPane
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
     */
    private BnTable getBnTable() {
        if (bnTable == null) {
            bnTable = new BnTable();
            bnTable.setColumns(new BnColumnBuilder().addColumn().withPath("name").withName("Name").addColumn().withPath("birthdate").withName("Birthdate").addColumn().withPath("active").withName("Active").addColumn().withPath("eyeColor").withName("Eye Color").build());
            bnTable.setModelProvider(getLocalProvider());
            bnTable.setPath(new Path("this.persons"));
        }
        return bnTable;
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        this.setSize(532, 244);
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJPanel(), BorderLayout.SOUTH);
            jContentPane.add(getPanel_1());
        }
        return jContentPane;
    }

    /**
     * This method initializes bnButton
     */
    private BnButton getBnButton() {
        if (bnButton == null) {
            bnButton = new BnButton();
            bnButton.setText("remove");
            bnButton.setModelProvider(getLocalProvider());
        }
        return bnButton;
    }

    private JPanel getPanel_1() {
        if (panel_1 == null) {
            panel_1 = new JPanel();
            panel_1.setLayout(new BorderLayout());
            panel_1.add(getJScrollPane());
        }
        return panel_1;
    }

}