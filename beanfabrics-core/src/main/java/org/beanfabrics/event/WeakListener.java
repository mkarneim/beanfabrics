/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.event;

import java.lang.ref.WeakReference;

/**
 * The {@link WeakListener} is a marker interface for listener classes.
 * <p>
 * When registered with an event source the listener should be stored using a
 * {@link WeakReference}. When an listener is found to be only weak reachable it
 * can be unregistered safely from the event source.
 * 
 * @author Michael Karneim
 */
public interface WeakListener {
}