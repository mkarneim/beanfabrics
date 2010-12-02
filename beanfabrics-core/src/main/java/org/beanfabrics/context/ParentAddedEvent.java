/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.context;

/**
 * An event which indicates that another context has been added as as a parent
 * context to the event source.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ParentAddedEvent extends ContextEvent {
    private final Context parent;

    /**
     * Constructs a {@link ParentAddedEvent}.
     * 
     * @param source
     * @param parent
     */
    public ParentAddedEvent(Context source, Context parent) {
        super(source);
        this.parent = parent;
    }

    /**
     * Returns the context that has been added as a parent to the event source.
     * 
     * @return the context that has been added as a parent
     */
    public Context getParent() {
        return parent;
    }
}