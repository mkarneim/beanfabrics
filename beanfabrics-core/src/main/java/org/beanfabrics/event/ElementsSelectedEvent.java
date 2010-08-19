/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.event;

import org.beanfabrics.model.IListPM;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ElementsSelectedEvent extends ListEvent {
    private final int beginIndex;

    private final int length;

    public ElementsSelectedEvent(IListPM source, final int beginIndex, final int length) {
        super(source);
        this.beginIndex = beginIndex;
        this.length = length;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public int getLength() {
        return length;
    }

    public String paramString() {
        return super.paramString() + ", beginIndex=" + beginIndex + ", length=" + length;
    }
}