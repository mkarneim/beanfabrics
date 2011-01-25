/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics;

import static org.junit.Assert.assertEquals;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Property;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class ValidationTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ValidationTest.class);
    }

    private static class EventCounter implements PropertyChangeListener {
        int count;

        public void propertyChange(PropertyChangeEvent evt) {
            count++;
        }
    }

    private static class ProductPM extends AbstractPM {
        public int saved = 0;
        @Property
        public final TextPM name = new TextPM();
        @Property
        public final IntegerPM volume = new IntegerPM();
        @Property
        public final ListPM<PartPM> parts = new ListPM<PartPM>();
        @Property
        public final IOperationPM save = new OperationPM();

        public ProductPM() {
            PMManager.setup(this);
            volume.setMandatory(true);
            save.getValidator().add(new ValidationRule() {
                public ValidationState validate() {
                    if (ProductPM.this.isValid() == false) {
                        return new ValidationState("There are some invalid properties");
                    }
                    return null;
                }
            });
            name.setTitle("Name");
            volume.setTitle("Volume");
            save.setTitle("Save");
        }

        @Operation
        public void save() {
            save.check();
            saved++;
        }
    }

    private static class PartPM extends AbstractPM {
        @Property
        public final TextPM name = new TextPM();

        public PartPM() {
            PMManager.setup(this);
            name.setMandatory(true);
        }
    }

    private ProductPM fixture;

    public ValidationTest() {
    }

    @Before
    public void setUpFixture()
        throws Exception {
        fixture = new ProductPM();
        fixture.name.setText("Box");
        fixture.volume.setInteger(12);
        // Populate
        PartPM partOne = new PartPM();
        partOne.name.setText("body");
        fixture.parts.add(partOne);
        PartPM partTwo = new PartPM();
        partTwo.name.setText("wheel");
        fixture.parts.add(partTwo);
    }

    @Test
    public void revalidate() {
        EventCounter counter = new EventCounter();
        ListPM<IntegerPM> list = new ListPM<IntegerPM>();
        list.addPropertyChangeListener(counter);
        list.add(new IntegerPM());
        assertEquals("list.getAt(0).isValid()", true, list.getAt(0).isValid());
        assertEquals("counter.count", 1, counter.count);

        list.getAt(0).setText("abc");
        assertEquals("list.getAt(0).isValid()", false, list.getAt(0).isValid());
        assertEquals("counter.count", 3, counter.count);
    }

    @Test
    public void isValid() {
        assertEquals("fixture.isValid()", true, fixture.isValid());
        assertEquals("fixture.save.isValid()", true, fixture.save.isValid());
    }

    @Test
    public void isValid2() {
        fixture.volume.setInteger(null);
        assertEquals("fixture.volume.isValid()", false, fixture.volume.isValid());
        assertEquals("fixture.isValid()", false, fixture.isValid());
        assertEquals("fixture.save.isValid()", false, fixture.save.isValid());
    }

    @Test
    public void isValid3() {
        fixture.parts.getAt(0).name.setText(null);
        assertEquals("fixture.parts.getAt(0).name.isValid()", false, fixture.parts.getAt(0).name.isValid());
        assertEquals("fixture.isValid()", false, fixture.isValid());
        assertEquals("fixture.save.isValid()", false, fixture.save.isValid());
    }
}