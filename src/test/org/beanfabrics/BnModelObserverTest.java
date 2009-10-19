package org.beanfabrics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.event.BnPropertyChangeEvent;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Validation;
import org.junit.Test;
import java.util.EventObject;
/**
 * 
 * @author Michael Karneim
 */
public class BnModelObserverTest {

    static class MyPersonPM extends AbstractPM {
        TextPM name = new TextPM();

        public MyPersonPM() {
            PMManager.setup(this);
        }

        @Validation(path = "name")
        boolean startsWithM() {
            return name.getText().toLowerCase().startsWith("m");
        }
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BnModelObserverTest.class);
    }

    //@Test
    public void validation() {
        MyPersonPM pm = new MyPersonPM();
        pm.name.setText("michael");
        assertTrue("pm.name.isValid()", pm.name.isValid());
        assertTrue("pm.isValid()", pm.isValid());

        pm.name.setText("argh");
        assertFalse("pm.name.isValid()", pm.name.isValid());
        assertFalse("pm.isValid()", pm.isValid());

    }

    @Test
    public void fireEvents() {
        final MyPersonPM pm = new MyPersonPM();

        ModelProvider prov = new ModelProvider();
        prov.setPresentationModel(pm);

        BnModelObserver observer = new BnModelObserver();
        observer.setModelProvider(prov);
        observer.setPath(new Path("this"));

        final int[] count = new int[1];
        final boolean[] isValid = new boolean[] { true };
        

        pm.name.setText("michael");
        assertTrue("pm.name.isValid()", pm.name.isValid());
        assertTrue("pm.isValid()", pm.isValid());        
        assertEquals("isValid[0]", true, isValid[0]);
        
        observer.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                //printEvent("", evt);

                if ("presentationModel".equals(evt.getPropertyName())) {
                    count[0]++;
                    isValid[0] = pm.isValid();
                }
            }

            private void printEvent(String prefix, EventObject evt) {
                System.out.println(prefix + evt);
                if (evt instanceof BnPropertyChangeEvent) {
                    printEvent(prefix + "  ", ((BnPropertyChangeEvent)evt).getCause());
                }
            }
        });

        pm.name.setText("argh");
        assertFalse("pm.name.isValid()", pm.name.isValid());
        assertFalse("pm.isValid()", pm.isValid());
        assertEquals("count[0]", 3, count[0]);
        assertEquals("isValid[0]", false, isValid[0]);
    }

}
