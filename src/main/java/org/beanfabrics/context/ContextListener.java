/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.context;

import java.util.EventListener;

/**
 * @author Michael Karneim
 */
public interface ContextListener extends EventListener {
    void parentAdded(ParentAddedEvent evt);

    void parentRemoved(ParentRemovedEvent evt);

    void serviceAdded(ServiceAddedEvent evt);

    void serviceRemoved(ServiceRemovedEvent evt);
}