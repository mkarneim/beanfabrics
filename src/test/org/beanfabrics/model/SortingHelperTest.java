/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.Binder;
import org.beanfabrics.Path;
import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.swing.table.BnTable;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class SortingHelperTest {

    public static void main(String[] args) {
        SortingHelperTest test = new SortingHelperTest();
        test.setupList();
        BnTable tbl = new BnTable();
        tbl.setPath(new Path("this"));
        tbl.setColumns(new BnColumn[] { new BnColumn(new Path("name"), "Name"), new BnColumn(new Path("number"), "Number"), new BnColumn(new Path("price"), "Price") });
        Binder.bind(tbl, test.list);
        JFrame f = new JFrame("Sorting Sample");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane scr = new JScrollPane(tbl);
        f.getContentPane().add(scr);
        f.setSize(400, 400);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SortingHelperTest.class);
    }

    ListPM<ProductPM> list;

    @Before
    public void setupList() {
        list = new ListPM<ProductPM>();
        list.add(new ProductPM("Apple", 5, 2.20));
        list.add(new ProductPM("Banana", 5, 1.20));
        list.add(new ProductPM("Orange", 5, 1.20));
        list.add(new ProductPM("Kiwi", 5, 1.20));
        list.add(new ProductPM("Pineapple", 5, 2.20));
    }

    @Test
    public void sortingTextCell() {
        // TODO (mk) 
    }

    @Test
    public void stableSort1() {
        list.sortBy(true, new Path("name"));
        Collection<ProductPM> sortedByName = list.toCollection();
        list.sortBy(true, new Path("number"));
        Collection<ProductPM> sortedByNumber = list.toCollection();

        assertSameOrder("sortedByNumber", sortedByName, sortedByNumber);
    }

    @Test
    public void stableSort2() {
        list.sortBy(true, new Path("price"));
        Collection<ProductPM> sortedByPrice = list.toCollection();
        list.sortBy(true, new Path("number"));
        Collection<ProductPM> sortedByNumber = list.toCollection();

        assertSameOrder("sortedByNumber", sortedByPrice, sortedByNumber);
    }

    private void assertSameOrder(String message, Collection a, Collection b) {
        assertEquals(message + ": b.size()", a.size(), b.size());
        Iterator itA = a.iterator();
        Iterator itB = b.iterator();
        while (itA.hasNext()) {
            Object objA = itA.next();
            Object objB = itB.next();
            assertEquals(message + ": objB", objA, objB);
        }
    }

    private static class ProductPM extends AbstractPM {
        private TextPM name = new TextPM();
        private IntegerPM number = new IntegerPM();
        private DecimalPM price = new DecimalPM();

        public ProductPM() {
            PMManager.setup(this);
        }

        public ProductPM(String name, int number, double price) {
            this();
            this.name.setText(name);
            this.number.setInteger(number);
            this.price.setDouble(price);
        }
    }
}