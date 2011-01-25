/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
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