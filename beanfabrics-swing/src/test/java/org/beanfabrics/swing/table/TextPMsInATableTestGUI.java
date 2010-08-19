/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.table;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Property;

public class TextPMsInATableTestGUI extends JFrame {
    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        try {
            TextPMsInATableTestGUI frame = new TextPMsInATableTestGUI();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class TableModel extends AbstractPM {
        @Property
        ListPM<TextPM> elements = new ListPM<TextPM>();

        public TableModel() {
            PMManager.setup(this);
            populate();
        }

        public void populate() {
            final int num = 10;
            for (int i = 0; i < num; ++i) {
                TextPM pM = new TextPM();
                pM.setText("row " + i);
                elements.add(pM);
            }
        }
    }

    private ModelProvider provider;
    private BnTable bnTable;
    private JScrollPane scrollPane;
    private JPanel panel;

    /**
     * Create the frame
     */
    public TextPMsInATableTestGUI() {
        super();
        setBounds(100, 100, 500, 375);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(getPanel(), BorderLayout.CENTER);
        //
    }

    private ModelProvider getLocalProvider() {
        if (provider == null) {
            provider = new ModelProvider(); // @wb:location=28,415
            provider.setPresentationModelType(TableModel.class);
            provider.setPresentationModel(new TableModel());
        }
        return provider;
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(getScrollPane(), BorderLayout.CENTER);
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
            bnTable.setColumns(new org.beanfabrics.swing.table.BnColumn[] { new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this"), "Text", 100, false) });
            bnTable.setPath(new org.beanfabrics.Path("this.elements"));
            bnTable.setModelProvider(getLocalProvider());
        }
        return bnTable;
    }
}