/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.table.BnTable;

@SuppressWarnings("serial")
public class ProductListFrame extends JFrame {
    private ModelProvider provider;
    private BnButton countryBnButton;
    private BnButton priceBnButton;
    private BnButton typeBnButton;
    private BnButton nameBnButton;
    private JPanel panel;
    private BnTable bnTable;
    private JScrollPane scrollPane;
    private ProductListPM productListModel;

    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ProductListFrame frame = new ProductListFrame();
                    frame.setVisible(true);
                    frame.productListModel.populate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }

    /**
     * Create the frame
     */
    public ProductListFrame() {
        super();
        setBounds(100, 100, 552, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(getScrollPane(), BorderLayout.CENTER);
        getContentPane().add(getPanel(), BorderLayout.NORTH);
        //
    }

    protected ModelProvider getLocalProvider() {
        if (provider == null) {
            provider = new ModelProvider(); // @wb:location=38,265
            provider.setPresentationModel(getProductListModel());
        }
        return provider;
    }

    protected ProductListPM getProductListModel() {
        if (productListModel == null) {
            productListModel = new ProductListPM(); // @wb:location=146,273
        }
        return productListModel;
    }

    protected JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(getBnTable());
        }
        return scrollPane;
    }

    protected BnTable getBnTable() {
        if (bnTable == null) {
            bnTable = new BnTable();
            bnTable.setPath(new org.beanfabrics.Path("this.elements"));
            bnTable.setColumns(new org.beanfabrics.swing.table.BnColumn[] { new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this.name"), "Name", 100, false),
                    new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this.type"), "Type", 80, true), new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this.country"), "Country", 80, true),
                    new org.beanfabrics.swing.table.BnColumn(new org.beanfabrics.Path("this.price"), "Price", 70, true) });
            bnTable.setModelProvider(getLocalProvider());
        }
        return bnTable;
    }

    protected JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.add(getNameBnButton());
            panel.add(getTypeBnButton());
            panel.add(getPriceBnButton());
            panel.add(getCountryBnButton());
        }
        return panel;
    }

    protected BnButton getNameBnButton() {
        if (nameBnButton == null) {
            nameBnButton = new BnButton();
            nameBnButton.setPath(new org.beanfabrics.Path("this.sortByName"));
            nameBnButton.setModelProvider(getLocalProvider());
            nameBnButton.setText("Name");
        }
        return nameBnButton;
    }

    protected BnButton getTypeBnButton() {
        if (typeBnButton == null) {
            typeBnButton = new BnButton();
            typeBnButton.setPath(new org.beanfabrics.Path("this.sortByType"));
            typeBnButton.setModelProvider(getLocalProvider());
            typeBnButton.setText("Type");
        }
        return typeBnButton;
    }

    protected BnButton getPriceBnButton() {
        if (priceBnButton == null) {
            priceBnButton = new BnButton();
            priceBnButton.setPath(new org.beanfabrics.Path("this.sortByPrice"));
            priceBnButton.setModelProvider(getLocalProvider());
            priceBnButton.setText("Price");
        }
        return priceBnButton;
    }

    protected BnButton getCountryBnButton() {
        if (countryBnButton == null) {
            countryBnButton = new BnButton();
            countryBnButton.setPath(new org.beanfabrics.Path("this.sortByCountry"));
            countryBnButton.setModelProvider(getLocalProvider());
            countryBnButton.setText("Country");
        }
        return countryBnButton;
    }
}