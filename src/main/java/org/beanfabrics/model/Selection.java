/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.model;

import java.util.Collection;

/**
 * The <code>Selection</code> maintains a subset of the elements of a {@link IListPM} that
 * are selected.
 *
 * <p>
 * Please note: an index parameter always refers to the element's position
 * inside the reference collection and not to the position inside the selection.
 * </p>
 *
 * @param <T>
 *            the element type of this selection.
 * @see IListPM
 * @author Michael Karneim
 */
public interface Selection<T extends PresentationModel> extends Collection<T> {
	/**
	 * Returns <code>true</code> if this the element at the specified index in
	 * the related collection is selected.
	 *
	 * @param index
	 *            the index to check
	 * @return <code>true</code> if this the element at the specified index in
	 *         the related collection is selected
	 */
	public boolean contains(int index);

	/**
	 * Change the selection to be the set union of the current selection and the
	 * elements with indices between <code>beginIndex</code> and
	 * <code>endIndex</code> inclusive. <code>beginIndex</code> must be less or
	 * equal <code>endIndex</code>.
	 *
	 * @param beginIndex
	 *            the index of the first element to select
	 * @param endIndex
	 *            the index of the last element to select (included in the
	 *            selection)
	 * @return <code>true</code> if this selection has been changed
	 */
	public boolean addInterval(int beginIndex, int endIndex);

	/**
	 * Change the selection to be the elements with indices between beginIndex
	 * and endIndex inclusive. <code>beginIndex</code> must be less or equal
	 * <code>endIndex</code>.
	 *
	 * @param beginIndex
	 *            the index of the first element to select
	 * @param endIndex
	 *            the index of the last element to select (included in the
	 *            selection)
	 * @return <code>true</code> if this selection has been changed
	 */
	public boolean setInterval(int beginIndex, int endIndex);
	
	/**
	 * Change the selection to be the set difference of the current selection
	 * and the elements with indices between beginIndex and endIndex inclusive.
	 * <code>beginIndex</code> must be less or equal <code>endIndex</code>.
	 *
	 * @param beginIndex
	 *            the index of the first element to unselect
	 * @param endIndex
	 *            the index of the last element to unselect (excluded from the
	 *            selection)
	 * @return <code>true</code> if this selection has been changed
	 */
	public boolean removeInterval(int beginIndex, int endIndex);

	/**
	 * Returns the element index of the first selected element in the reference
	 * collection. The first selected element is the selected element with the
	 * smallest index.
	 *
	 * @return the element index of the first selected element or
	 *         <code>-1</code> if none is selected
	 */
	public int getMinIndex();

	/**
	 * Returns the element index of the last selected element in the reference
	 * collection. The last selected element is the selected element with the
	 * greatest index.
	 *
	 * @return the element index of the last selected element or <code>-1</code>
	 *         if none is selected
	 */
	public int getMaxIndex();

	/**
	 * Change the selection to be equal the complete reference collection.
	 */
	public void addAll();

	/**
	 * Returns the first selected element in the reference collection. The first
	 * selected element is the selected element with the smallest index.
	 *
	 * @return the first selected element
	 * @see #getMinIndex()
	 */
	public T getFirst();

	/**
	 * Returns the indexes of all selected elements in the reference collection.
	 *
	 * @return the indexes of all selected elements
	 */
	public int[] getIndexes();
	
	/**
	 * Returns the indexes of the selected elements with indices between beginIndex and endIndex inclusive.
	 * @param beginIndex
	 * @param endIndex
	 * @return the indexes of the selected elements within the specified bounds
	 */
	public int[] getIndexes(int beginIndex, int endIndex);
	
	/**
	 * Change the selection to be the elements with the given indices. 
	 *
	 * @return <code>true</code> if this selection has been changed
	 * @param selIndices
	 *            the indices of the elements to select
	 */
	public boolean setIndexes(int[] selIndices);
	
	/**
	 * Returns a new Collection with all selected elements. Modification on this
	 * collection will not influence the original selection.
	 *
	 * @return a new <code>Collection</code> with all selected elements
	 */
	public Collection<T> toCollection();
	
}