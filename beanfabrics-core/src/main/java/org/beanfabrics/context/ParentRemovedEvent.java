/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.context;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ParentRemovedEvent extends ContextEvent {
    private final Context parent;

    public ParentRemovedEvent(Context source, Context parent) {
        super(source);
        this.parent = parent;
    }

    public Context getParent() {
        return parent;
    }
}