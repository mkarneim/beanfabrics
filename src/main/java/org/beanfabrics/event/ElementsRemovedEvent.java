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
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ElementsRemovedEvent extends ListEvent {
    private final int beginIndex;
    private final int length;
    private final Collection<? extends PresentationModel> removed;

    public ElementsRemovedEvent(IListPM source, int beginIndex, int length, Collection<? extends PresentationModel> removed) {
        super(source);
        this.beginIndex = beginIndex;
        this.length = length;
        this.removed = removed;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public int getLength() {
        return length;
    }

    public Collection<? extends PresentationModel> getRemoved() {
        return removed;
    }

    public String paramString() {
        return super.paramString() + ", beginIndex=" + beginIndex + ", length=" + length;
    }
}