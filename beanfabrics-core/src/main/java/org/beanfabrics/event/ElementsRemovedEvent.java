/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.event;

import java.util.Collection;

import org.beanfabrics.model.IListPM;
import org.beanfabrics.model.PresentationModel;

/**
 * An event which indicates that some elements have been removed from an
 * {@link IListPM}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ElementsRemovedEvent extends ListEvent {
    private final int beginIndex;
    private final int length;
    private final Collection<? extends PresentationModel> removed;

    /**
     * Constructs a {@link ElementsRemovedEvent}.
     * 
     * @param source the source of this event
     * @param beginIndex the smallest index of the elements involved
     * @param length the number of the involved elements
     * @param removed the collection of elements that have been removed
     */
    public ElementsRemovedEvent(IListPM<?> source, int beginIndex, int length, Collection<? extends PresentationModel> removed) {
        super(source);
        this.beginIndex = beginIndex;
        this.length = length;
        this.removed = removed;
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

    /**
     * Returns the collection of elements that have been removed.
     * 
     * @return the collection of elements that have been removed
     */
    public Collection<? extends PresentationModel> getRemoved() {
        return removed;
    }

    /** {@inheritDoc} */
    public String paramString() {
        return super.paramString() + ", beginIndex=" + beginIndex + ", length=" + length;
    }
}