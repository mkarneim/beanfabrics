/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class AbstractValuePMTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AbstractValuePMTest.class);
    }

    AbstractValuePM create(final boolean isEmpty) {
        AbstractValuePM result = new AbstractValuePM() {

            public boolean isEmpty() {
                return isEmpty;
            }

            public Comparable getComparable() {
                // TODO Auto-generated method stub
                return null;
            }
        };
        return result;
    }

    @Test
    public void setMandatory() {
        AbstractValuePM pM = create(true);
        pM.setMandatory(true);
        assertEquals("pM.isMandatory()", true, pM.isMandatory());
        pM.setMandatory(false);
        assertEquals("pM.isMandatory()", false, pM.isMandatory());
    }

    @Test
    public void setEditable() {
        AbstractValuePM pM = create(true);
        pM.setEditable(true);
        assertEquals("pM.isEditable()", true, pM.isEditable());
        pM.setEditable(false);
        assertEquals("pM.isEditable()", false, pM.isEditable());
    }

    @Test
    public void isValid() {
        AbstractValuePM pM = create(true);
        pM.setMandatory(true);
        assertEquals("pM.isValid()", false, pM.isValid());
        pM.setMandatory(false);
        assertEquals("pM.isValid()", true, pM.isValid());
    }

    @Test
    public void isValid2() {
        AbstractValuePM pM = create(true);
        pM.getValidator().add(new ValidationRule() {
            public ValidationState validate() {
                return new ValidationState("Always invalid");
            }
        });
        pM.setMandatory(false);
        assertEquals("pM.isValid()", true, pM.isValid());
    }
}