/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.context;

/**
 * An event which indicates that a parent context has been removed from the
 * event source.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ParentRemovedEvent extends ContextEvent {
    private final Context parent;

    /**
     * Contructs a new {@link ParentRemovedEvent}.
     * 
     * @param source
     * @param parent
     */
    public ParentRemovedEvent(Context source, Context parent) {
        super(source);
        this.parent = parent;
    }

    /**
     * Returns the context that has been removed from the event source's list of
     * parents.
     * 
     * @return the context that has been removed from the event source's list of
     *         parents
     */
    public Context getParent() {
        return parent;
    }
}