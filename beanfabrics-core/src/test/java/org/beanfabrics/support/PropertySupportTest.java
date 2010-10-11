/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.support;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.junit.Test;

/**
 * @author Michael Karneim
 * @author Max Gensthaler
 */
public class PropertySupportTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PropertySupportTest.class);
    }

    @Test
    public void basicAnnotated() {
        AnnotatedPersonModel mdl = new AnnotatedPersonModel();
        assertNotNull("getProperty()", PropertySupport.get(mdl).getProperty("firstname"));
    }

    @Test
    public void basicNonAnnotated() {
        PersonModel mdl = new PersonModel();
        assertNotNull("getProperty()", PropertySupport.get(mdl).getProperty("firstname"));

        //		// Check that only public non annotated fields are accessable
        //		assertNull("getProperty()", PropertySupport.get(mdl).getProperty("firstname"));
        //		try {
        //			Field f = mdl.getClass().getDeclaredField("firstname");
        //			f.setAccessible(true);
        //			assertNotNull("getProperty()", PropertySupport.get(mdl).getProperty("firstname"));
        //		} catch (IllegalArgumentException e) {
        //			fail();
        //		} catch (SecurityException e) {
        //			fail();
        //		} catch (NoSuchFieldException e) {
        //			fail();
        //		}
    }

    @Test
    public void explicitNameAnnotated() {
        AnnotatedCarModel mdl = new AnnotatedCarModel();
        assertNotNull("getProperty()", PropertySupport.get(mdl).getProperty("model"));
        mdl.carmodel.setText("foo");
        assertEquals("mdl.changeCount", 1, mdl.changeCount);
    }

    @Test
    public void explicitNameNonAnnotated() {
        CarModel mdl = new CarModel();
        assertNotNull("getProperty()", PropertySupport.get(mdl).getProperty("model"));
        mdl.carmodel.setText("foo");
        assertEquals("mdl.changeCount", 1, mdl.changeCount);
    }

    public static class AnnotatedPersonModel extends AbstractPM {
        @Property
        private final TextPM firstname = new TextPM();

        public AnnotatedPersonModel() {
            PMManager.setup(this);
        }
    }

    public static class PersonModel extends AbstractPM {
        private final TextPM firstname = new TextPM();

        public PersonModel() {
            PMManager.setup(this);
        }
    }

    public static class AnnotatedCarModel extends AbstractPM {
        @Property("model")
        private final TextPM carmodel = new TextPM();
        public int changeCount = 0;

        public AnnotatedCarModel() {
            PMManager.setup(this);
        }

        @OnChange(path = "model")
        void onChange() {
            changeCount++;
        }
    }

    public static class CarModel extends AbstractPM {
        private final TextPM carmodel = new TextPM();
        public int changeCount = 0;

        public CarModel() {
            PMManager.setup(this);
        }

        @OnChange(path = "model")
        void onChange() {
            changeCount++;
        }

        private TextPM getModel() {
            return carmodel;
        }
    }

    @Test
    public void interfaceMethodAnnotated() {
        AnnotatedImplModel mdl = new AnnotatedImplModel();
        assertNotNull("getProperty(\"firstname\")", PropertySupport.get(mdl).getProperty("firstname"));
        assertNotNull("getProperty(\"lastname\")", PropertySupport.get(mdl).getProperty("lastname"));
    }

    public static interface AnnotatedInterface {
        @Property
        TextPM getFirstname();
    }

    public static class AnnotatedImplModel extends AbstractPM implements AnnotatedInterface {
        @Property
        protected final TextPM lastname = new TextPM();
        private ReferencedClass ref = new ReferencedClass();

        public AnnotatedImplModel() {
            PMManager.setup(this);
        }

        @Property
        public TextPM getFirstname() {
            return ref.getTextPM();
        }
    }

    public static class ReferencedClass {
        private final TextPM textPM = new TextPM();

        public TextPM getTextPM() {
            return textPM;
        }
    }

    @Test
    public void hierarchyOfUnAndAnnotatedClasses() {
        AnnotatedChildModel mdl = new AnnotatedChildModel();
        assertNotNull("getProperty(\"firstname\")", PropertySupport.get(mdl).getProperty("firstname"));
        assertNotNull("getProperty(\"lastname\")", PropertySupport.get(mdl).getProperty("lastname"));
        assertNotNull("getProperty(\"fathersname\")", PropertySupport.get(mdl).getProperty("fathersname"));
        assertNotNull("getProperty(\"mothersname\")", PropertySupport.get(mdl).getProperty("mothersname"));
    }

    public static class UnAnnotatedParentModel extends AbstractPM {
        protected final TextPM fathersname = new TextPM();
        private static final ReferencedClass ref = new ReferencedClass();

        public UnAnnotatedParentModel() {
            PMManager.setup(this);
        }

        public TextPM getMothersname() {
            return ref.getTextPM();
        }
    }

    public static class AnnotatedChildModel extends UnAnnotatedParentModel implements AnnotatedInterface {
        @Property
        protected final TextPM lastname = new TextPM();
        private static final ReferencedClass ref = new ReferencedClass();

        public AnnotatedChildModel() {
            PMManager.setup(this);
        }

        @Property
        public TextPM getFirstname() {
            return ref.getTextPM();
        }
    }

    private static class EmptyPM extends AbstractPM {
        public EmptyPM() {
            PMManager.setup(this);
        }
    }

    private static class MyListener implements PropertyChangeListener {
        public int count = 0;

        public void propertyChange(PropertyChangeEvent evt) {
            count++;
        }

    }

    @Test
    public void putVirtualProperty1() {
        EmptyPM model = new EmptyPM();
        PropertySupport.get(model).putProperty("propertyA", new TextPM(), TextPM.class);

        TextPM pA = (TextPM)PropertySupport.get(model).getProperty("propertyA");
        assertNotNull("pA", pA);

        MyListener listener = new MyListener();
        model.addPropertyChangeListener(listener);
        assertEquals("listener.count", 0, listener.count);

        pA.setText("huhu");
        assertEquals("listener.count", 1, listener.count);
    }

}