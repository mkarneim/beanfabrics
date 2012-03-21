/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.util.Collection;
import java.util.ListIterator;

import org.beanfabrics.Path;
import org.beanfabrics.event.ListListener;

/**
 * Minimal interface for presentation models with list-editing capabilities.
 * 
 * @author Michael Karneim
 * @param <T> the element type parameter
 */
public interface IListPM<T extends PresentationModel> extends PresentationModel, Iterable<T> {
    /**
     * Adds the given listener.
     * 
     * @param listener
     */
    public void addListListener(ListListener listener);

    /**
     * Removes the given listener.
     * 
     * @param listener
     */
    public void removeListListener(ListListener listener);

    /**
     * Returns the number of elements in this collection.
     * 
     * @return the number of elements in this collection
     */
    public int size();

    /**
     * Returns <code>true</code> if this collection contains no elements.
     * 
     * @return <code>true</code> if this collection contains no elements
     */
    public boolean isEmpty();

    /**
     * Returns <code>true</code> if this collection contains the specified
     * element.
     * 
     * @param obj
     * @return <code>true</code> if this collection contains the specified
     *         element
     */
    public boolean contains(T obj);

    /**
     * Returns the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element.
     * 
     * @param element
     * @return the index of the first occurrence of the specified element in
     *         this list, or -1 if this list does not contain the element
     */
    public int indexOf(T element);

    /**
     * Returns the element at the specified position in this list.
     * 
     * @param index
     * @return the element at the specified position in this list
     */
    public T getAt(int index);

    /**
     * Swaps the position of the two elements specified by the given indexes.
     * 
     * @param indexA
     * @param indexB
     */
    public void swap(int indexA, int indexB);

    /**
     * Swaps the position of the two specified elements.
     * 
     * @param elemA
     * @param elemB
     */
    public void swap(T elemA, T elemB);

    /**
     * Sorts the elements of this list PM by comparing the child nodes at the
     * end of the given paths.
     * 
     * @param ascending if true, the resulting order will be ascending,
     *            otherwise descending.
     * @param paths one or more Path objects that define the sort keys
     */
    public void sortBy(boolean ascending, Path... path);

    /**
     * Sorts the elements of this list PM by the specified sort keys.
     * 
     * @param newSortKeys the sort keys used for sorting the elements of this
     *            list PM
     */
    public void sortBy(Collection<SortKey> newSortKeys);

    /**
     * Sorts the elements of this list PM by the specified sort keys.
     * 
     * @param newSortKeys the sort keys used for sorting the elements of this
     *            list PM
     */
    public void sortBy(SortKey... sortKeys);

    /**
     * Returns the (immutable) collection of {@link SortKey} objects that
     * reflect the current sorting state of this list.
     * 
     * @return the (immutable) collection of {@link SortKey} objects that
     *         reflect the current sorting state of this list
     */
    public Collection<SortKey> getSortKeys();

    /**
     * Returns a new {@link Collection} of all elements.
     * 
     * @return a new <code>Collection</code> of all elements
     */
    public Collection<T> toCollection();

    /**
     * Returns a list iterator of the elements in this list (in proper
     * sequence), starting at the specified position in this list.
     * 
     * @param index
     * @return a list iterator of the elements in this list (in proper
     *         sequence), starting at the specified position in this list
     */
    public ListIterator<T> listIterator(int index);

    /**
     * Returns an array containing all of the elements in this list in proper
     * sequence (from first to last element).
     * 
     * @return an array containing all of the elements in this list in proper
     *         sequence (from first to last element)
     */
    public Object[] toArray();

    /**
     * Returns the selection of this list.
     * 
     * @return the selection of this list
     */
    public Selection<T> getSelection();
}