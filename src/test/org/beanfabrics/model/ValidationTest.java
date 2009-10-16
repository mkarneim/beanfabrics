package org.beanfabrics.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;

import org.beanfabrics.event.BnPropertyChangeEvent;
import org.beanfabrics.support.Validation;
import org.junit.Test;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.*;

/**
 * @author Michael Karneim
 */
public class ValidationTest {
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
		return new JUnit4TestAdapter(ValidationTest.class);
	}

	@Test
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
		final int[] count = new int[1];
		final boolean[] isValid = new boolean[] { true };
		pm.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				printEvent("", evt);
				if ("validationState".equals(evt.getPropertyName())) {
					count[0]++;
					isValid[0] = pm.isValid();
				}
			}

			private void printEvent(String prefix, EventObject evt) {
				System.out.println(prefix + evt);
				if (evt instanceof BnPropertyChangeEvent) {
					printEvent(prefix + "  ", ((BnPropertyChangeEvent) evt).getCause());
				}
			}
		});

		pm.name.setText("michael");
		assertTrue("pm.name.isValid()", pm.name.isValid());
		assertTrue("pm.isValid()", pm.isValid());
		assertEquals("count[0]", 0, count[0]);
		assertEquals("isValid[0]", true, isValid[0]);

		pm.name.setText("argh");
		assertFalse("pm.name.isValid()", pm.name.isValid());
		assertFalse("pm.isValid()", pm.isValid());
		assertEquals("count[0]", 1, count[0]);
		assertEquals("isValid[0]", false, isValid[0]);
	}
}
