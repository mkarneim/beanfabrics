package org.beanfabrics.support;

import static junit.framework.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.junit.Test;

public class OnChangeSupportTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(OnChangeSupportTest.class);
    }

    private static class SomeClassPM extends AbstractPM {
        TextPM name = new TextPM("Hello");
        IntegerPM lengthOfName = new IntegerPM(0);
        int callCountOfUpdateLengthOfName = 0;

        TextPM note = new TextPM();
        int callCountOfNoteHasChanged = 0;

        public SomeClassPM() {
            PMManager.setup(this);
        }

        @OnChange(path = "name")
        public void updateLengthOfName() {
            callCountOfUpdateLengthOfName++;
            lengthOfName.setInteger(name.getText().length());
        }

        @OnChange(path = "note")
        public void noteHasChanged() {
            callCountOfNoteHasChanged++;
        }

    }

    @Test
    public void onCangeMethodIsNotCalledOnSetup() {
        SomeClassPM pm = new SomeClassPM();
        // Name
        assertEquals("pm.callCountOfUpdateLengthOfName", 0, pm.callCountOfUpdateLengthOfName);
        assertEquals("pm.lengthOfName.getInteger()", 0, (int)pm.lengthOfName.getInteger());
        // Note
        assertEquals("pm.callCountOfNoteHasChanged", 0, pm.callCountOfNoteHasChanged);
    }

    @Test
    public void onCangeMethodIsCalledWhenPropertyHasCanged() {
        SomeClassPM pm = new SomeClassPM();
        // Name
        pm.name.setText("Bye");
        assertEquals("pm.callCountOfUpdateLengthOfName", 1, pm.callCountOfUpdateLengthOfName);
        assertEquals("pm.lengthOfName.getInteger()", 3, (int)pm.lengthOfName.getInteger());
        // Note
        assertEquals("pm.callCountOfNoteHasChanged", 0, pm.callCountOfNoteHasChanged);
        pm.note.setText("Some note");
        assertEquals("pm.callCountOfNoteHasChanged", 1, pm.callCountOfNoteHasChanged);
    }

}
