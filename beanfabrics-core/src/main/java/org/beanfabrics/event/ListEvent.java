/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.event;

import java.util.EventObject;

import org.beanfabrics.model.IListPM;

/**
 * An event which indicates that some change has occurred in some
 * {@link IListPM}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ListEvent extends EventObject {
    /**
     * Constructs a ListEvent
     * 
     * @param source
     */
    protected ListEvent(IListPM<?> source) {
        super(source);
    }

    /** {@inheritDoc} */
    public String paramString() {
        return "source=" + super.getSource();
    }

    /**
     * Returns a string representation of this event.
     * 
     * @return a string representation of this event
     */
    public String toString() {
        return getClass().getName() + "[" + this.paramString() + "]";
    }
}