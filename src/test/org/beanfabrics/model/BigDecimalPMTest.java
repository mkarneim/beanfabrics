/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Locale;

import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;
/**
 *
 * @author Michael Karneim
 */
public class BigDecimalPMTest {
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(BigDecimalPMTest.class);
	}

	static Locale oldLocale;
	@BeforeClass
	public static void setupClass() {
		oldLocale = Locale.getDefault();
		Locale.setDefault( Locale.US);
	}

	public static void tearDownClass() {
		Locale.setDefault(oldLocale);
	}

	@Test
	public void create() {
		new BigDecimalPM();
	}

	@Test
	public void setText1() {
		BigDecimalPM model = new BigDecimalPM();
		model.setText("1234.56");
		BigDecimal bd = new BigDecimal("1234.56");
		assertEquals("pM.getBigDecimal()", bd, model.getBigDecimal());
	}

	@Test
	public void setText2() {
		BigDecimalPM model = new BigDecimalPM();
		model.setText("1234abc");
		assertEquals("pM.isValid()", false,model.isValid());
	}

	@Test
	public void setText3() {
		BigDecimalPM model= new BigDecimalPM();
		model.setText("9223372036854775809");
		assertEquals("pM.isValid()", true,model.isValid());
	}
}