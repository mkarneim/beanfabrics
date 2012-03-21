/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.table;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.test.AddressListPM;

@SuppressWarnings("serial")
public class BnTableCustomizerTestGUI extends JFrame {
    private ModelProvider provider;
    private AddressListPM addressListEditor;

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BnTableCustomizerTestGUI frame = new BnTableCustomizerTestGUI();
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
    public BnTableCustomizerTestGUI() {
        super();

        addressListEditor = new AddressListPM(); // @wb:location=80,404

        provider = new ModelProvider(); // @wb:location=203,403
        provider.setPresentationModel(addressListEditor);
        provider.setPresentationModelType(org.beanfabrics.test.AddressListPM.class);
        getContentPane().setLayout(new BorderLayout());
        setBounds(100, 100, 500, 375);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);

        final JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane);

        final BnTable bnTable = new BnTable();
        bnTable.setColumns(new org.beanfabrics.swing.table.BnColumn[] { new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this.appartment"), "Appartment", 100, false),
                new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this.city"), "City", 100, false) });

        bnTable.setPath(new Path("elements"));
        bnTable.setModelProvider(provider);
        scrollPane.setViewportView(bnTable);

        final BnButton addRowBnButton = new BnButton();
        addRowBnButton.setPath(new Path("addSome"));
        addRowBnButton.setModelProvider(provider);
        panel.add(addRowBnButton, BorderLayout.NORTH);
        addRowBnButton.setText("Add row");
    }
}