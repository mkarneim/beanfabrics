/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.event;

/**
 * The {@link WeakListListener} is a {@link ListListener} marked with the
 * {@link WeakListener} interface.
 * 
 * @see WeakListener What is a weak listener?
 * @author Michael Karneim
 */
public interface WeakListListener extends ListListener, WeakListener {
}