/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.event;

/**
 * An abstract adapter class for receiving list events. The methods in this
 * class are empty. This class exists as convenience for creating listener
 * objects.
 * <p>
 * Extend this class to create {@link ListListener} and override the methods for
 * the events of interest.
 * 
 * @author Michael Karneim
 */
public class ListAdapter implements ListListener {
    /** {@inheritDoc} */
    public void elementChanged(ElementChangedEvent evt) {
    }

    /** {@inheritDoc} */
    public void elementsReplaced(ElementsReplacedEvent evt) {
    }

    /** {@inheritDoc} */
    public void elementsAdded(ElementsAddedEvent evt) {
    }

    /** {@inheritDoc} */
    public void elementsDeselected(ElementsDeselectedEvent evt) {
    }

    /** {@inheritDoc} */
    public void elementsRemoved(ElementsRemovedEvent evt) {
    }

    /** {@inheritDoc} */
    public void elementsSelected(ElementsSelectedEvent evt) {
    }
}