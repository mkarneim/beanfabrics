package org.beanfabrics.samples.timespan;

import java.util.Date;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.ConversionException;
import org.beanfabrics.model.DatePM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.Validation;
/**
 * The TimeSpanPM is a presentation model for editing a period of time.  
 * The start date must be before or equal the end date.
 */
public class TimeSpanPM extends AbstractPM {
    public final DatePM start = new DatePM();
    public final DatePM end = new DatePM();
    
    public TimeSpanPM() {
        PMManager.setup(this);
    }
    
    public TimeSpanPM( Date startDate, Date endDate) {
        this();
        start.setDate(startDate);
        end.setDate(endDate);
    }
    
    /**
     * Returns true, if the time span is a positive period.
     * @return true, if the time span is a positive period
     */
    @Validation(path={"start","end"},message="This is not a valid time span")
    public boolean isPositivePeriod() {
        try {
            Date startDate = start.getDate();
            Date endDate = end.getDate();
            if ( startDate == null || endDate == null) {
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
    
}
