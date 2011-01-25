/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.model.PresentationModelFilter;

/**
 * The <code>FilteredModelsIterator</code> is a filtered {@link Iterator} on a
 * given {@link Iterator}.
 * 
 * @author Michael Karneim
 */
public class FilteredModelsIterator implements Iterator<PresentationModel> {
    private final PresentationModelFilter filter;
    private final Iterator<PresentationModel> delegate;
    Boolean hasNext = null;
    PresentationModel next = null;

    public FilteredModelsIterator(Iterator<PresentationModel> iterator, PresentationModelFilter filter) {
        if (iterator == null) {
            throw new IllegalArgumentException("iterator == null");
        }
        this.delegate = iterator;
        this.filter = filter; // null is allowed
    }

    public void remove() {
        throw new UnsupportedOperationException("remove is not supported");
    }

    public PresentationModel next() {
        if (hasNext()) {
            PresentationModel result = next;
            next = null;
            hasNext = null;
            return result;
        } else {
            throw new NoSuchElementException();
        }
    }

    public boolean hasNext() {
        if (hasNext == null) {
            while (delegate.hasNext()) {
                PresentationModel nextModel = delegate.next();
                if (filter == null || filter.accept(nextModel)) {
                    hasNext = true;
                    next = nextModel;
                    return true;
                }
            }
            hasNext = false;
            next = null;
        }
        return hasNext;
    }
}