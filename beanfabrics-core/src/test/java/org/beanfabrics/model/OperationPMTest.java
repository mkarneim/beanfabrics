/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Property;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class OperationPMTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(OperationPMTest.class);
    }

    public static class SamplePM extends AbstractPM {
        @Property
        IOperationPM foo = new OperationPM();
        @Property
        IOperationPM bar = new OperationPM();
        @Property
        IIntegerPM fooCount = new IntegerPM();
        @Property
        IIntegerPM barCount = new IntegerPM();

        public SamplePM() {
            PMManager.setup(this);
            fooCount.setInteger(0);
            barCount.setInteger(0);
        }

        @Operation(path = "foo")
        public void foo() {
            fooCount.setInteger(fooCount.getInteger() + 1);
        }

        @Operation
        public void bar() {
            barCount.setInteger(barCount.getInteger() + 1);
        }
    }

    public OperationPMTest() {
    }

    @BeforeClass
    public static void setUpClass()
        throws Exception {
    }

    @AfterClass
    public static void tearDownClass()
        throws Exception {
    }

    @Test
    public void executeFoo()
        throws Throwable {
        SamplePM pModel = new SamplePM();
        pModel.foo.execute();
        assertEquals("pModel.fooCount.getInteger()", 1, (int)pModel.fooCount.getInteger());
    }

    @Test
    public void executeBar()
        throws Throwable {
        SamplePM pModel = new SamplePM();
        pModel.bar.execute();
        assertEquals("pModel.barCount.getInteger()", 1, (int)pModel.barCount.getInteger());
    }
}