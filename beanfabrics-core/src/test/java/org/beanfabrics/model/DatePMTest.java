/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.Path;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class DatePMTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DatePMTest.class);
    }

    public DatePMTest() {
    }

    static Locale oldLocale;
    static DatePM.DateFormatProvider oldDateFormatProvider;

    @BeforeClass
    public static void setUpClass()
        throws Exception {
        oldLocale = Locale.getDefault();
        Locale.setDefault(Locale.GERMANY);
        oldDateFormatProvider = DatePM.getDefaultDateFormatProvider();
    }

    @AfterClass
    public static void tearDownClass()
        throws Exception {
        Locale.setDefault(oldLocale);
        DatePM.setDefaultDateFormatProvider(oldDateFormatProvider);
    }

    @Test
    public void create() {
        new DatePM();
    }

    @Test
    public void validate() {
        DatePM model = new DatePM();
        model.setText("14.01.1971");
        assertEquals("pM.isValid()", true, model.isValid());
        model.setText("31.02");
        assertEquals("pM.isValid()", false, model.isValid());

        model.setText("aaa");
        assertEquals("pM.isValid()", false, model.isValid());
        try {
            model.getDate();
            fail("ecpected ConversionException");
        } catch (ConversionException ex) {
            // expected
        }

        model.setText("14.01.1971a");
        assertEquals("pM.isValid()", false, model.isValid());
        try {
            model.getDate();
            fail("ecpected ConversionException");
        } catch (ConversionException ex) {
            // expected
        }

    }

    @Test
    public void events() {
        DatePM model = new DatePM();
        MyPropertyChangeListener l = new MyPropertyChangeListener();
        model.addPropertyChangeListener(l);

        model.setText("14.01.1971");
        assertEquals("model.isValid()", true, model.isValid());
        assertEquals("l.eventCount", 2, l.eventCount);
        model.setText("31.02");
        assertEquals("model.isValid()", false, model.isValid());
        assertEquals("l.eventCount", 4, l.eventCount);

        model.setDate(new Date());
        assertEquals("l.eventCount", 6, l.eventCount);
    }

    @Test
    public void setGermanFormat() {
        Locale.setDefault(Locale.GERMANY);
        DatePM pm = new DatePM();
        pm.setText("14.01.2011");
        assertEquals("pm.isValid()", true, pm.isValid());
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
        pm.setFormat(format);

        assertEquals("1/14/11", pm.getText());
    }

    @Test
    public void setUSFormat() {
        Locale.setDefault(Locale.US);
        DatePM pm = new DatePM();
        Calendar cal = Calendar.getInstance();
        cal.set(2011, 0, 14);
        pm.setDate(cal.getTime());
        assertEquals("pm.isValid()", true, pm.isValid());
        assertEquals("pm.getText()", "Jan 14, 2011", pm.getText());
        Locale.setDefault(Locale.GERMAN);
        DateFormat format = DateFormat.getDateInstance();
        pm.setFormat(format);

        assertEquals("14.01.2011", pm.getText());
    }

    @Test
    public void setUSFormat2() {
        Locale.setDefault(Locale.US);
        DatePM pm = new DatePM();
        pm.setText("Jan 14, 2011");
        assertEquals("pm.isValid()", true, pm.isValid());
        Locale.setDefault(Locale.GERMAN);
        DateFormat format = DateFormat.getDateInstance();
        pm.setFormat(format);

        assertEquals("14.01.2011", pm.getText());
    }

    @Test
    public void useCustomDateFormatProvider() {
        DatePM.setDefaultDateFormatProvider(new DatePM.DateFormatProvider() {
            @Override
            public DateFormat getDateFormat() {
                DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
                format.setLenient(false);
                return format;
            }
        });

        Locale.setDefault(Locale.US);
        DatePM pm = new DatePM();
        Calendar cal = Calendar.getInstance();
        cal.set(2011, 0, 14);
        pm.setDate(cal.getTime());
        assertEquals("pm.isValid()", true, pm.isValid());
        assertEquals("pm.getText()", "1/14/11", pm.getText());
        Locale.setDefault(Locale.GERMAN);
        DateFormat format = DateFormat.getDateInstance();
        pm.setFormat(format);

        assertEquals("14.01.2011", pm.getText());
    }

    @Test
    public void setFormatWithInvalidDate() {
        Locale.setDefault(Locale.GERMANY);
        DatePM pm = new DatePM();
        pm.setText("14.0");
        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        pm.setFormat(format);

        assertEquals("14.0", pm.getText());
    }

    @Test
    public void validateTimeSpan() {
        final DatePM from = new DatePM();
        final DatePM to = new DatePM();
        final ValidationRule v = new ValidationRule() {
            public ValidationState validate() {
                // -> check time span
                try {
                    Date fromDate = from.getDate();
                    Date toDate = to.getDate();
                    if (fromDate != null && toDate != null && fromDate.before(toDate) == false) {
                        return new ValidationState("from-Date has to be before to-Date");
                    } else {
                        return null;
                    }
                } catch (ConversionException ex) {
                    // can't read other date
                    // -> in dubio pro reo
                    return null;
                }
            }
        };
        from.getValidator().add(v);
        to.getValidator().add(v);

        from.setText("14.01.2001");
        to.setText("10.03.1995");
        // Since there is no model that forces a revalidation on
        // from-property...
        assertEquals("from.isValid()", true, from.isValid()); // from-property
        // hasn't been
        // revalidated
        assertEquals("to.isValid()", false, to.isValid());

        // now let's see how that changes when we use an model
        class MyModel extends AbstractPM {
            final DatePM from = new DatePM();
            final DatePM to = new DatePM();
            final ValidationRule rule = new ValidationRule() {
                public ValidationState validate() {
                    // -> check time span
                    try {
                        Date fromDate = from.getDate();
                        Date toDate = to.getDate();
                        if (fromDate != null && toDate != null && fromDate.before(toDate) == false) {
                            return new ValidationState("from-Date has to be before to-Date");
                        } else {
                            return null;
                        }
                    } catch (ConversionException ex) {
                        // can't read other date
                        // -> in dubio pro reo
                        return null;
                    }
                }
            };

            public MyModel() {
                PMManager.setup(this);
                from.getValidator().add(rule);
                to.getValidator().add(rule);
                from.setText("14.01.2001");
                to.setText("10.03.1995");
            }
        }

        MyModel model = new MyModel();

        assertEquals("model.from.isValid()", false, model.from.isValid()); // that
        // assertion has
        // changed
        assertEquals("model.to.isValid()", false, model.to.isValid());

        // test valid time span
        model.from.setText("14.01.1971");
        model.to.setText("10.03.1972");
        assertEquals("model.from.isValid()", true, model.from.isValid());
        assertEquals("model.to.isValid()", true, model.to.isValid());

        model.from.setText("14.01"); // not a date
        assertEquals("model.from.isValid()", false, model.from.isValid());
        assertEquals("model.to.isValid()", true, model.to.isValid());

        model.to.setText("10.03"); // not a date
        assertEquals("model.from.isValid()", false, model.from.isValid());
        assertEquals("model.sto.isValid()", false, model.to.isValid());

    }

    private static final long MILLISECONDS_PER_DAY = 1000L * 3600 * 24;

    @Test
    public void getComparable() {
        DatePM[] array = new DatePM[10];
        for (int i = 1; i < 10; ++i) {
            array[i] = new DatePM();
            Date date = new Date();
            date.setTime(date.getTime() + MILLISECONDS_PER_DAY * i);
            array[i].setDate(date);
        }
        array[0] = new DatePM();

        List<Comparable> cmpList = new ArrayList<Comparable>(array.length);
        for (int i = 0; i < array.length; ++i) {
            cmpList.add(array[i].getComparable());
        }

        List<Comparable> tmp = new ArrayList<Comparable>(cmpList);

        Collections.shuffle(tmp);
        Collections.sort(tmp);

        for (int i = 0; i < cmpList.size(); ++i) {
            assertSame("tmp.get(i='" + i + "')", cmpList.get(i), tmp.get(i));
        }
    }

    @Test
    public void sortInListModel() {
        ListPM<DatePM> list = new ListPM<DatePM>();
        // create some models with random dates
        long now = System.currentTimeMillis();
        for (int i = 0; i < 10; ++i) {
            list.add(new DatePM());
            list.getAt(i).setDate(new Date(now + (long)(Math.random() * 10000000000.0)));
            //System.out.println(list.getAt(i).getText());
        }
        list.sortBy(true, new Path("this"));
        long lastTime = -1;
        for (DatePM dc : list) {
            assertEquals("dc.getDate().getTime()>=lastTime", true, dc.getDate().getTime() >= lastTime);
            lastTime = dc.getDate().getTime();
        }
    }

    private static class MyPropertyChangeListener implements PropertyChangeListener {
        int eventCount = 0;

        public void propertyChange(PropertyChangeEvent evt) {
            eventCount++;
        }
    }
}