/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.event;

import java.util.EventObject;
import java.util.Iterator;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BnPropertyChangeEvent extends java.beans.PropertyChangeEvent implements FollowUpEvent {
	/**
	 * The (optional) cause of this event.
	 */
	private EventObject cause;

	public BnPropertyChangeEvent(Object source, String propertyName,
			Object oldValue, Object newValue) {
		this(source, propertyName, oldValue, newValue, null);
	}

	public BnPropertyChangeEvent(Object source, String propertyName,
			Object oldValue, Object newValue, EventObject cause) {
		super(source, propertyName, oldValue, newValue);
		this.cause = cause;
	}

	/**
	 * Returns the cause of this event.
	 * @return the cause of this event
	 */
	public EventObject getCause() {
		return cause;
	}

	/**
	 * Returns an {@link Iterator} over all events that caused this event, starting with this event.
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
				if ( next instanceof FollowUpEvent) {
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
		return getClass().getName()+"[source="+source+", propertyName="+this.getPropertyName()+", oldValue="+this.getOldValue()+", newValue="+this.getNewValue()+"]";
	}
}