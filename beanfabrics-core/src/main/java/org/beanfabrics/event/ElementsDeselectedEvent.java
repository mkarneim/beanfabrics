/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.event;

import org.beanfabrics.model.IListPM;

/**
 * An event which indicates that some elements have been deselected in an
 * {@link IListPM}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ElementsDeselectedEvent extends ListEvent {
    private final int beginIndex;
    private final int length;

    /**
     * Constructs a {@link ElementsDeselectedEvent}.
     * 
     * @param source the source of this event
     * @param beginIndex the smallest index of the elements involved
     * @param length the number of the involved elements
     */
    public ElementsDeselectedEvent(IListPM<?> source, int beginIndex, int length) {
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