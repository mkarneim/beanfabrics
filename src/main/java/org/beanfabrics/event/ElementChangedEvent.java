/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.event;

import java.util.EventObject;
import java.util.Iterator;

import org.beanfabrics.model.IListPM;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ElementChangedEvent extends ListEvent implements FollowUpEvent {
	private final int index;
	private final EventObject cause;

	public ElementChangedEvent(IListPM source, int index, EventObject cause) {
		super(source);
		this.index = index;
		this.cause = cause;
	}

	public ElementChangedEvent(IListPM source, int index) {
		this(source, index, null);
	}

	public int getIndex() {
		return index;
	}

	public EventObject getCause() {
		return cause;
	}

	public String paramString() {
		return super.paramString()+", index="+index;
	}

	/**
	 * Returns an {@link Iterator} over all events that caused this event, starting with this event.
	 * @return an {@link Iterator} over all events that caused this event
	 */
	public Iterator<EventObject> iterator() {
		return new Iterator<EventObject>() {
			EventObject next = ElementChangedEvent.this;
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
}