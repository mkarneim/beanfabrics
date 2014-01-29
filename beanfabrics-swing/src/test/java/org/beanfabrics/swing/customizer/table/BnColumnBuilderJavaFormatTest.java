/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.swing.table.BnColumnBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class BnColumnBuilderJavaFormatTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BnColumnBuilderJavaFormatTest.class);
    }

    private static final String BUILDER_CLASSNAME = BnColumnBuilder.class.getName();
    private static final String BUILDER_CONSTRUCTOR = "new " + BnColumnBuilder.class.getName() + "()";

    private BnColumnBuilderJavaFormat format;

    @Before
    public void createBnColumnBuilderFormat() {
        this.format = new BnColumnBuilderJavaFormat();
        this.format.setColumnDelimiter("");
    }

    @Test
    public void formatNullArray() {
        BnColumn[] columns = null;
        String result = format.format(columns);
        assertNull("", result);
    }

    @Test
    public void formatEmptyArray() {
        BnColumn[] columns = new BnColumn[0];
        String result = format.format(columns);
        assertNull("", result);
    }

    @Test
    public void formatColumnWithPath() {
        BnColumn[] columns = new BnColumnBuilder().addColumn().withPath("this.name").build();
        String result = format.format(columns);
        assertEquals("result", BUILDER_CONSTRUCTOR + ".addColumn().withPath(\"this.name\").build()", result);
    }

    @Test
    public void formatColumnWithPathAndName() {
        BnColumn[] columns = new BnColumnBuilder().addColumn().withPath("this.name").withName("Name").build();
        String result = format.format(columns);
        assertEquals("result", BUILDER_CONSTRUCTOR + ".addColumn().withPath(\"this.name\").withName(\"Name\").build()", result);
    }

    @Test
    public void formatColumnWithPathNameWidth() {
        BnColumn[] columns = new BnColumnBuilder().addColumn().withPath("this.name").withName("Name").withWidth(10).build();
        String result = format.format(columns);
        assertEquals("result", BUILDER_CONSTRUCTOR + ".addColumn().withPath(\"this.name\").withName(\"Name\").withWidth(10).build()", result);
    }

    @Test
    public void formatColumnWithPathNameWidthFixedWidth() {
        BnColumn[] columns = new BnColumnBuilder().addColumn().withPath("this.name").withName("Name").withWidth(10).withWidthFixed(true).build();
        String result = format.format(columns);
        assertEquals("result", BUILDER_CONSTRUCTOR + ".addColumn().withPath(\"this.name\").withName(\"Name\").withWidth(10).withWidthFixed(true).build()", result);
    }

    @Test
    public void formatColumnWithPathNameWidthFixedWidthOperationPath() {
        BnColumn[] columns = new BnColumnBuilder().addColumn().withPath("this.name").withName("Name").withWidth(10).withWidthFixed(true).withOperationPath("this.choose").build();
        String result = format.format(columns);
        assertEquals("result", BUILDER_CONSTRUCTOR + ".addColumn().withPath(\"this.name\").withName(\"Name\").withWidth(10).withWidthFixed(true).withOperationPath(\"this.choose\").build()", result);
    }

    @Test
    public void formatColumnWithPathNameWidthFixedWidthOperationPathAlignment() {
        BnColumn[] columns = new BnColumnBuilder().addColumn().withPath("this.name").withName("Name").withWidth(10).withWidthFixed(true).withOperationPath("this.choose").withAlignment(BnColumnBuilder.ALIGNMENT_LEFT).build();
        String result = format.format(columns);
        assertEquals("result", BUILDER_CONSTRUCTOR + ".addColumn().withPath(\"this.name\").withName(\"Name\").withWidth(10).withWidthFixed(true).withOperationPath(\"this.choose\").withAlignment(" + BUILDER_CLASSNAME + ".ALIGNMENT_LEFT).build()",
                result);
    }

}