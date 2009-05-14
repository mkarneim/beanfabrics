/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import static org.junit.Assert.assertEquals;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

import junit.framework.JUnit4TestAdapter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class ContactPMTest {
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(ContactPMTest.class);
	}

	public ContactPMTest() {
	}

	static Locale oldLocale;
	@BeforeClass
	public static void setUpClass() throws Exception {
		oldLocale = Locale.getDefault();
		Locale.setDefault( Locale.GERMANY);
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		Locale.setDefault(oldLocale);
	}

	@Test
	public void create() throws Throwable {
		ContactPM contact = new ContactPM();
		contact.firstname.setText("Michael");
		contact.lastname.setText("Karneim");
		contact.birthday.setText("14.01.1971");
		contact.hasWife.setBoolean(false);
    	assertEquals("contact.addChild.isEnabled()", false, contact.addChild.isEnabled());

    	contact.hasWife.setBoolean(true);
    	assertEquals("contact.addChild.isEnabled()", true, contact.addChild.isEnabled());

    	contact.addChild.execute();
    	assertEquals("contact.children.getInteger()",1, (int)contact.children.getInteger());

	}

	@Test
	public void events() throws Throwable {
		ContactPM contact = new ContactPM();
		MyPropertyChangeListener l = new MyPropertyChangeListener();
		contact.addPropertyChangeListener(l);
		assertEquals("l.eventCount", 0, l.eventCount);

		contact.firstname.setText("Michael");
		assertEquals("l.eventCount", 1, l.eventCount);
		contact.lastname.setText("Karneim");
		assertEquals("l.eventCount", 2, l.eventCount);
		contact.birthday.setText("14.01.1971");
		assertEquals("l.eventCount", 3, l.eventCount);
	}

	private static class MyPropertyChangeListener implements PropertyChangeListener {
		int eventCount = 0;

		public void propertyChange(PropertyChangeEvent evt) {
			eventCount++;
		}
	}
}