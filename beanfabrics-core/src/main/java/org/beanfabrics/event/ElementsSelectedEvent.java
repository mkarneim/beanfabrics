/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.event;

import org.beanfabrics.model.IListPM;

/**
 * An event which indicates that some elements have been selected in an
 * {@link IListPM}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ElementsSelectedEvent extends ListEvent {
    private final int beginIndex;
    private final int length;

    /**
     * Constructs a {@link ElementsSelectedEvent}.
     * 
     * @param source the source of this event
     * @param beginIndex the smallest index of the elements involved
     * @param length the number of the involved elements
     */
    public ElementsSelectedEvent(IListPM<?> source, final int beginIndex, final int length) {
        super(source);
        this.beginIndex = beginIndex;
        this.length = length;
    }

    /**
     * Returns the smalles index of the elements involved in this event.
     * 
     * @return the smalles index of the elements involved in this event
     */
    public int getBeginIndex() {
        return beginIndex;
    }

    /**
     * Returns the number of elements involved in this event.
     * 
     * @return the number of elements involved in this event
     */
    public int getLength() {
        return length;
    }

    /** {@inheritDoc} */
    public String paramString() {
        return super.paramString() + ", beginIndex=" + beginIndex + ", length=" + length;
    }
}