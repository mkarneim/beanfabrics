/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.event;

import java.beans.PropertyChangeListener;

/**
 * The {@link WeakPropertyChangeListener} is a {@link PropertyChangeListener}
 * marked with the {@link WeakListener} interface.
 * 
 * @see WeakListener What is a weak listener?
 * @author Michael Karneim
 */
public interface WeakPropertyChangeListener extends PropertyChangeListener, WeakListener {
}