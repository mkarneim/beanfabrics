/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
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