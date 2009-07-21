/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.event;

import java.util.EventListener;

import org.beanfabrics.model.IListPM;

/**
 * Listener for changes to {@link IListPM} objects.
 * 
 * @author Michael Karneim
 */
public interface ListListener extends EventListener {
    /**
     * Invoked after elements have been added to the list.
     * 
     * @param evt
     */
    public void elementsAdded(ElementsAddedEvent evt);

    /**
     * Invoked after elements have been removed from the list.
     * 
     * @param evt
     */
    public void elementsRemoved(ElementsRemovedEvent evt);

    /**
     * Invoked after elements have been replaced.
     * 
     * @param evt
     */
    public void elementsReplaced(ElementsReplacedEvent evt);

    /**
     * Invoked after elements have been changed in some way.
     * 
     * @param evt
     */
    public void elementChanged(ElementChangedEvent evt);

    /**
     * Invoked after elements have been selected.
     * 
     * @param evt
     */
    public void elementsSelected(ElementsSelectedEvent evt);

    /**
     * Invoked after elements have been deselected.
     * 
     * @param evt
     */
    public void elementsDeselected(ElementsDeselectedEvent evt);
}