/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertEquals;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class TextPMTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TextPMTest.class);
    }

    public TextPMTest() {
        //
    }

    @Test
    public void create() {
        new TextPM();
    }

    @Test
    public void setText() {
        TextPM pModel = new TextPM();
        Counter l = new Counter();
        pModel.addPropertyChangeListener(l);

        pModel.setText("hello, world!");
        assertEquals("l.eventCount", 2, l.eventCount);
        pModel.setText("hello, moon!");
        assertEquals("l.eventCount", 3, l.eventCount);
        pModel.setText("hello, moon!");
        assertEquals("l.eventCount", 3, l.eventCount);
    }

    @Test
    public void setMandatory() {
        TextPM pModel = new TextPM();
        Counter l = new Counter();
        pModel.addPropertyChangeListener("mandatory", l);

        pModel.setMandatory(true);
        assertEquals("l.eventCount", 1, l.eventCount);
        pModel.setMandatory(false);
        assertEquals("l.eventCount", 2, l.eventCount);
        pModel.setMandatory(false);
        assertEquals("l.eventCount", 2, l.eventCount);
    }

    @Test
    public void setEditable() {
        TextPM pModel = new TextPM();
        Counter l = new Counter();
        pModel.addPropertyChangeListener("editable", l);

        pModel.setEditable(true); // this is the default
        assertEquals("l.eventCount", 0, l.eventCount);
        pModel.setEditable(false);
        assertEquals("l.eventCount", 1, l.eventCount);
        pModel.setEditable(false);
        assertEquals("l.eventCount", 1, l.eventCount);
    }

    @Test
    public void validate() {
        final TextPM pModel = new TextPM();
        pModel.getValidator().add(new ValidationRule() {
            public ValidationState validate() {
                if (pModel.getText().startsWith("wrong")) {
                    return new ValidationState("This is wrong!");
                }
                return null;
            }
        });

        Counter l = new Counter();
        pModel.addPropertyChangeListener(l);

        pModel.setText("Hello, world!");
        assertEquals("pModel.isValid()", true, pModel.isValid());
        pModel.setText("wrong");
        assertEquals("pModel.isValid()", false, pModel.isValid());

    }

    @Test
    public void changeSomethingDuringEventCycle() {
        final TextPM pModel = new TextPM();
        PropertyChangeListener l = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                pModel.setText("bla");
            }
        };
        pModel.addPropertyChangeListener(l);
        Counter counter = new Counter();
        pModel.addPropertyChangeListener(counter);

        pModel.setText("hello");
        assertEquals("pModel.getText()", "bla", pModel.getText());
        assertEquals("l.eventCount", 3, counter.eventCount);
    }

    @Test
    public void isModified() {
        ITextPM pModel = new TextPM();
        pModel.setText("hello, world!");
        pModel.preset();
        assertEquals("pModel.isModified()", false, pModel.isModified());
        pModel.setText("hello");
        assertEquals("pModel.isModified()", true, pModel.isModified());
        pModel.reset();
        assertEquals("pModel.isModified()", false, pModel.isModified());
    }

    @Test
    public void setOptions() {
        ITextPM pModel = new TextPM();
        Options<Integer> opt = new Options<Integer>();
        opt.put(1, "one");
        opt.put(2, "two");
        opt.put(3, "three");

        Counter counter = new Counter();
        pModel.addPropertyChangeListener(counter);
        pModel.setOptions(opt);
        assertEquals("eventCount", 1, counter.eventCount);

        opt.put(4, "four");
        assertEquals("eventCount", 2, counter.eventCount);
    }

    @Test
    public void removeOptions() {
        ITextPM pModel = new TextPM();
        Options<Integer> opt = new Options<Integer>();

        pModel.setOptions(opt);
        Counter counter = new Counter();
        pModel.addPropertyChangeListener(counter);

        assertEquals("counter.eventCount", 0, counter.eventCount);

        pModel.setOptions(null);
        assertEquals("counter.eventCount", 1, counter.eventCount);

        opt.put(1, "one"); // no event should be forwarded to pM
        assertEquals("counter.eventCount", 1, counter.eventCount);
    }

    @Test
    public void setRestrictedToOptions() {
        TextPM pModel = new TextPM();
        pModel.setRestrictedToOptions(true);
        Options<Integer> opt = new Options<Integer>();
        opt.put(1, "one");
        opt.put(2, "two");
        opt.put(3, "three");
        pModel.setOptions(opt);
        pModel.setText("one");
        assertEquals("pModel.isValid()", true, pModel.isValid());
        pModel.setText("blah");
        assertEquals("pModel.isValid()", false, pModel.isValid());
        pModel.setRestrictedToOptions(false);
        assertEquals("pModel.isValid()", true, pModel.isValid());
    }

    @Test
    public void isValidWhenEmptyAndOptional() {
        TextPM pModel = new TextPM();
        pModel.setMandatory(false);
        pModel.setText(null);
        assertEquals("pModel.isValid()", true, pModel.isValid());
        pModel.getValidator().add(new ValidationRule() {
            public ValidationState validate() {
                return new ValidationState("baeh!");
            }
        });
        assertEquals("pModel.isValid()", true, pModel.isValid());
    }

    private static class Counter implements PropertyChangeListener {
        private int eventCount = 0;

        public void propertyChange(PropertyChangeEvent evt) {
            eventCount++;
        }
    }
}