/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
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
public class ElementsAddedEvent extends ListEvent {
    private int beginIndex;
    private int length;

    public ElementsAddedEvent(IListPM source, int beginIndex, int length) {
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