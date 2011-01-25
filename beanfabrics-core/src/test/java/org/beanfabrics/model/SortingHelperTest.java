/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.Path;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class SortingHelperTest {
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

    @Test
    public void sortHeterogeneousElements() {
        // Heterogenous elements will be sorted in groups that are grouped by their comparable class.
        ListPM hetList = new ListPM();
        hetList.add(new TextPM("1"));
        hetList.add(new TextPM("2"));
        hetList.add(new TextPM("11"));
        hetList.add(new TextPM("21"));
        hetList.add(new TextPM("10"));
        hetList.add(new IntegerPM(30));
        hetList.add(new IntegerPM(3));
        hetList.add(new IntegerPM(0));
        hetList.add(new IntegerPM(31));
        hetList.add(new IntegerPM(20));
        hetList.sortBy(true, new Path("this"));
        String[] expectedOrder = new String[] { //
        "0", "3", "20", "30", "31", // <-- IntegerPM-Objects
                "1", "10", "11", "2", "21" }; // <-- TextPM-Objects

        String[] actualOrder = getTextValues(hetList);

        assertArrayEquals("actualOrder", expectedOrder, actualOrder);
        //System.out.println(Arrays.toString(actualOrder));
    }

    @Test
    public void sortHeterogeneousElementsWithHomogenousComparables() {
        ListPM hetList = new ListPM();
        hetList.add(new TextPM("4"));
        hetList.add(new TextPM("5"));
        hetList.add(new ProductPM("a", 11, 12.3));
        hetList.add(new ProductPM("1", 11, 12.3));
        hetList.add(new ProductPM("z", 11, 12.3));
        hetList.add(new ProductPM("x", 11, 12.3));
        hetList.add(new ProductPM("9", 11, 12.3));
        hetList.add(new ProductPM("2", 11, 12.3));
        hetList.add(new TextPM("7"));
        hetList.add(new TextPM("b"));
        hetList.sortBy(true, new Path("this"));

        String[] expectedOrder = new String[] { //
        "1", "2", "4", "5", "7", "9", "a", "b", "x", "z" };
        String[] actualOrder = getTextValues(hetList);
        assertArrayEquals("actualOrder", expectedOrder, actualOrder);
        //System.out.println(Arrays.toString(actualOrder));
    }

    private String[] getTextValues(ListPM<PresentationModel> list) {
        String[] result = new String[list.size()];
        int i = 0;
        for (PresentationModel pm : list) {
            result[i++] = getTextValue(pm);
        }
        return result;
    }

    private String getTextValue(PresentationModel pm) {
        if (pm instanceof ITextPM) {
            return ((ITextPM)pm).getText();
        } else if (pm instanceof ProductPM) {
            return ((ProductPM)pm).name.getText();
        } else {
            return null;
        }
    }

    private <T> void assertSameOrder(String message, Collection<T> a, Collection<T> b) {
        assertEquals(message + ": b.size()", a.size(), b.size());
        Iterator<T> itA = a.iterator();
        Iterator<T> itB = b.iterator();
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

        @Override
        public Comparable<?> getComparable() {
            return name.getComparable();
        }

    }

}