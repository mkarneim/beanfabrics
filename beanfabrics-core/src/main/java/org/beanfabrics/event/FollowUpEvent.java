/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.event;

import java.util.EventObject;

/**
 * An interface for an event that is fired because of another "triggering"
 * event.
 * 
 * @author Michael Karneim
 */
public interface FollowUpEvent extends Iterable<EventObject> {
    /**
     * Returns the triggering event.
     * 
     * @return the triggering event
     */
    public EventObject getCause();
}