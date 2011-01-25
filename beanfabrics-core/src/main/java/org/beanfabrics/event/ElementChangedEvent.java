/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.event;

import java.util.EventObject;
import java.util.Iterator;

import org.beanfabrics.model.IListPM;

/**
 * An event which indicates that some change has occurred in a {@link IListPM}
 * element.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ElementChangedEvent extends ListEvent implements FollowUpEvent {
    private final int index;
    private final EventObject cause;

    /**
     * Constructs a {@link ElementChangedEvent}.
     * 
     * @param source the list where the element belongs to
     * @param index the index of the element
     * @param cause the (optional) triggering event
     */
    public ElementChangedEvent(IListPM<?> source, int index, EventObject cause) {
        super(source);
        this.index = index;
        this.cause = cause;
    }

    /**
     * Constructs a {@link ElementChangedEvent}.
     * 
     * @param source
     * @param index
     */
    public ElementChangedEvent(IListPM<?> source, int index) {
        this(source, index, null);
    }

    /**
     * Returns the index of the changed element.
     * 
     * @return the index of the changed element
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the triggering event.
     * 
     * @return the triggering event
     */
    public EventObject getCause() {
        return cause;
    }

    /** {@inheritDoc} */
    public String paramString() {
        return super.paramString() + ", index=" + index;
    }

    /**
     * Returns an {@link Iterator} of all events that caused this event,
     * beginning with this event and ending with the original trigger.
     * 
     * @return an {@link Iterator} of all events that caused this event
     */
    public Iterator<EventObject> iterator() {
        return new Iterator<EventObject>() {
            EventObject next = ElementChangedEvent.this;

            /** {@inheritDoc} */
            public void remove() {
                throw new UnsupportedOperationException("remove is not supported by this iterator");
            }

            /** {@inheritDoc} */
            public EventObject next() {
                EventObject result = next;
                if (next instanceof FollowUpEvent) {
                    next = ((FollowUpEvent)next).getCause();
                } else {
                    next = null;
                }
                return result;
            }

            /** {@inheritDoc} */
            public boolean hasNext() {
                return next != null;
            }
        };
    }
}