/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.event;

import java.util.EventObject;

import org.beanfabrics.model.IListPM;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ListEvent extends EventObject {
    public ListEvent(IListPM source) {
        super(source);
    }

    public String paramString() {
        return "source=" + super.getSource();
    }

    public String toString() {
        return getClass().getName() + "[" + this.paramString() + "]";
    }
}