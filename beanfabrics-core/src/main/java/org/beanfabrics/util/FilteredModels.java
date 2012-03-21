/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.util;

import java.util.Iterator;

import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.model.PresentationModelFilter;

/**
 * The <code>FilteredModels</code> is an filtered decorator of an Iterable
 * object of presentation models.
 * 
 * @author Michael Karneim
 */
public class FilteredModels implements Iterable<PresentationModel> {
    private final Iterable<PresentationModel> delegate;
    private final PresentationModelFilter filter;

    public FilteredModels(Iterable<PresentationModel> iterable, PresentationModelFilter filter) {
        if (iterable == null) {
            throw new IllegalArgumentException("iterable==null");
        }
        this.delegate = iterable;
        this.filter = filter; // null is allowed
    }

    public Iterator<PresentationModel> iterator() {
        return new FilteredModelsIterator(delegate.iterator(), filter);
    }
}