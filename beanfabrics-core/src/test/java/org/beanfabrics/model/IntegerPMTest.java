package org.beanfabrics.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;

public class IntegerPMTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BigDecimalPMTest.class);
    }

    static Locale oldLocale;

    @BeforeClass
    public static void setupClass() {
        oldLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    public static void tearDownClass() {
        Locale.setDefault(oldLocale);
    }

    @Test
    public void setDefaultText() {
        IntegerPM model = new IntegerPM();

        Long defaultValue = 1000l;
        Long currentValue = 500l;

        model.setLong(defaultValue);
        model.preset();
        model.setLong(currentValue);

        assertTrue("model.isModified()", model.isModified());

        model.reset();

        assertFalse("model.isModified()", model.isModified());
    }

}
