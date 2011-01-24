package org.beanfabrics.model;

import static junit.framework.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Validation;
import org.junit.Test;

public class PMManagerTest {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PMManagerTest.class);
    }

    private static interface SomeInterfacePM extends PresentationModel {
        @Validation(path = "name")
        public boolean isNameValid();

        @OnChange(path = "name")
        void updateLengthOfName();
    }

    private static class SomeClassPM extends AbstractPM implements SomeInterfacePM {
        TextPM name = new TextPM("invalid");
        IntegerPM lengthOfName = new IntegerPM(0);

        public SomeClassPM() {
            PMManager.setup(this);
        }

        public boolean isNameValid() {
            if (name.getText().startsWith("valid")) {
                return true;
            } else {
                return false;
            }
        }

        public void updateLengthOfName() {
            lengthOfName.setInteger(name.getText().length());
        }

    }

    @Test
    public void overriddenValidation() {
        SomeClassPM pm = new SomeClassPM();
        assertEquals("pm.name.isValid()", false, pm.name.isValid());
        assertEquals("pm.lengthOfName.getInteger()", 7, (int)pm.lengthOfName.getInteger());
        pm.name.setText("valid");
        assertEquals("pm.name.isValid()", true, pm.name.isValid());
    }
}
