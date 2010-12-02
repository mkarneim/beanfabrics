/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.event;

import java.util.Collection;

import org.beanfabrics.model.IListPM;
import org.beanfabrics.model.PresentationModel;

/**
 * An event which indicates that some elements have been replaced in an
 * {@link IListPM}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ElementsReplacedEvent extends ListEvent {
    private final int beginIndex;
    private final int length;
    private final Collection<? extends PresentationModel> replaced;

    /**
     * Constructs a {@link ElementsReplacedEvent}.
     * 
     * @param source the source of this event
     * @param beginIndex the smallest index of the elements involved
     * @param length the number of the involved elements
     * @param replaced the collection of elements that have been replaced
     */
    public ElementsReplacedEvent(IListPM<?> source, int beginIndex, int length, final Collection<? extends PresentationModel> replaced) {
        super(source);
        this.beginIndex = beginIndex;
        this.length = length;
        this.replaced = replaced;
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
     * Returns the collection of elements that have been replaced.
     * 
     * @return the collection of elements that have been replaced
     */
    public Collection<? extends PresentationModel> getReplaced() {
        return replaced;
    }

    /** {@inheritDoc} */
    public String paramString() {
        return super.paramString() + ", beginIndex=" + beginIndex + ", length=" + length;
    }
}