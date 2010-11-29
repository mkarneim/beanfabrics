/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.event;

import java.beans.PropertyChangeEvent;
import java.util.EventObject;
import java.util.Iterator;

import org.beanfabrics.model.PresentationModel;

/**
 * An event which indicates that some change has occurred in some
 * {@link PresentationModel}.
 * <p>
 * This event is a subclass of {@link PropertyChangeEvent} that implements the
 * {@link FollowUpEvent}. A {@link FollowUpEvent} is created when an event is
 * passed from a child node to a parent node. By calling {@link #getCause()} you
 * can track the chain of events from the received event to the orignal trigger.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BnPropertyChangeEvent extends java.beans.PropertyChangeEvent implements FollowUpEvent {
    /**
     * The (optional) cause of this event.
     */
    private EventObject cause;

    /**
     * Constructs a {@link BnPropertyChangeEvent} without a trigger.
     * 
     * @param source
     * @param propertyName
     * @param oldValue
     * @param newValue
     */
    public BnPropertyChangeEvent(Object source, String propertyName, Object oldValue, Object newValue) {
        this(source, propertyName, oldValue, newValue, null);
    }

    /**
     * Constructs a {@link BnPropertyChangeEvent}.
     * 
     * @param source
     * @param propertyName
     * @param oldValue
     * @param newValue
     * @param cause
     */
    public BnPropertyChangeEvent(Object source, String propertyName, Object oldValue, Object newValue, EventObject cause) {
        super(source, propertyName, oldValue, newValue);
        this.cause = cause;
    }

    /**
     * Returns the cause of this event.
     * 
     * @return the cause of this event
     */
    public EventObject getCause() {
        return cause;
    }

    /**
     * Returns an {@link Iterator} over all events that caused this event,
     * starting with this event.
     * 
     * @return an {@link Iterator} over all events that caused this event
     */
    public Iterator<EventObject> iterator() {
        return new Iterator<EventObject>() {
            EventObject next = BnPropertyChangeEvent.this;

            public void remove() {
                throw new UnsupportedOperationException("remove is not supported by this iterator");
            }

            public EventObject next() {
                EventObject result = next;
                if (next instanceof FollowUpEvent) {
                    next = ((FollowUpEvent)next).getCause();
                } else {
                    next = null;
                }
                return result;
            }

            public boolean hasNext() {
                return next != null;
            }
        };
    }

    public String toString() {
        return getClass().getName() + "[source=" + source + ", propertyName=" + this.getPropertyName() + ", oldValue=" + this.getOldValue() + ", newValue=" + this.getNewValue() + "]";
    }
}