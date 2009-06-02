package org.beanfabrics.samples.timespan;

import java.util.Date;
import java.util.GregorianCalendar;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.ConversionException;
import org.beanfabrics.model.DatePM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Validation;

/**
 * The TimeSpanPM is a presentation model for editing a period of time. The
 * start date must be before or equal the end date.
 */
public class TimeSpanPM extends AbstractPM {
	private static final long MS_PER_DAY = 1000 * 60 * 60 * 24;
	public final DatePM start = new DatePM();
	public final DatePM end = new DatePM();
	public final IntegerPM days = new IntegerPM();

	public TimeSpanPM() {
		PMManager.setup(this);
		start.setMandatory(true);
		end.setMandatory(true);
		days.setEditable(false);
	}

	public TimeSpanPM(Date startDate, Date endDate) {
		this();
		start.setDate(startDate);
		end.setDate(endDate);
	}

	/**
	 * Returns <code>true</code>, if the time span is a positive period.
	 *
	 * @return <code>true</code>, if the time span is a positive period
	 */
	@Validation(path = { "start", "end" }, message = "This is not a valid time span")
	public boolean isPositivePeriod() {
		try {
			Date startDate = start.getDate();
			Date endDate = end.getDate();
			if (startDate == null || endDate == null) {
				// open periods are 'positive'
				return true;
			}
			return startDate.before(endDate) || startDate.equals(endDate);
		} catch (ConversionException ex) {
			return false;
		}
	}

	public Date getStartDate() {
		return start.getDate();
	}

	public void setStartDate(Date startDate) {
		this.start.setDate(startDate);
	}

	public Date getEndDate() {
		return end.getDate();
	}

	public void setEndDate(Date endDate) {
		this.end.setDate(endDate);
	}

	@OnChange(path = { "start", "end" })
	public void onSetTimeSpan() {
		if (!isValid()) {
			days.setText("");
		} else {
			GregorianCalendar startCal = new GregorianCalendar();
			startCal.setTime(start.getDate());
			GregorianCalendar endCal = new GregorianCalendar();
			endCal.setTime(end.getDate());
			days.setLong((endCal.getTimeInMillis() - startCal.getTimeInMillis()) / MS_PER_DAY);
		}
	}
}
