/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.context;

import java.util.EventObject;

/**
 * An event which indicates that some change has occurred in a context.
 * <p>
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ContextEvent extends EventObject {
    /**
     * Constructs a {@link ContextEvent} object.
     * 
     * @param source the {@link Context} that originated the event
     */
    public ContextEvent(Context source) {
        super(source);
    }
}