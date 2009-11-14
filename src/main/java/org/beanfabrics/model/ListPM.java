/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.beanfabrics.Path;
import org.beanfabrics.context.ContextOwner;
import org.beanfabrics.event.BnPropertyChangeEvent;
import org.beanfabrics.event.ElementChangedEvent;
import org.beanfabrics.event.ElementsAddedEvent;
import org.beanfabrics.event.ElementsDeselectedEvent;
import org.beanfabrics.event.ElementsRemovedEvent;
import org.beanfabrics.event.ElementsReplacedEvent;
import org.beanfabrics.event.ElementsSelectedEvent;
import org.beanfabrics.event.ListListener;
import org.beanfabrics.event.ListSupport;
import org.beanfabrics.support.PropertySupport;
import org.beanfabrics.util.Interval;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * The ListPM is a list of presentation models. Basically it provides methods
 * for adding, removing, accessing and iterating elements and informs listeners
 * about changes. It also maintains a {@link Selection}.
 *
 * @author Michael Karneim
 */
public class ListPM<T extends PresentationModel> extends AbstractPM implements IListPM<T> {
    private static final Integer UNKNOWN = null;
    private static final int NONE = -1;

    private final ListSupport support = new ListSupport(this);
    private final SelectionImpl selection = new SelectionImpl();
    private final PropertyChangeListener elementsPcl = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            onElementChanged(evt);
        }
    };
    private final ListListener selfListener = new ListListener() {
        public void elementsSelected(ElementsSelectedEvent evt) {
            onEntriesChanged(evt);
        }

        public void elementsReplaced(ElementsReplacedEvent evt) {
            onEntriesChanged(evt);
            setSortKeys(null);
        }

        public void elementsRemoved(ElementsRemovedEvent evt) {
            onEntriesChanged(evt);
        }

        public void elementsDeselected(ElementsDeselectedEvent evt) {
            onEntriesChanged(evt);
        }

        public void elementsAdded(ElementsAddedEvent evt) {
            onEntriesChanged(evt);
            setSortKeys(null);
        }

        public void elementChanged(ElementChangedEvent evt) {
            onEntriesChanged(evt);
            setSortKeys(null);
        }
    };

    protected boolean revalidateElementsOnChangeEnabled = false;

    /**
     * This list holds all elements of this <code>ListPM</code>. All elements
     * are wrapped into {@link Entry} objects that hold a
     * {@link Entry#isSelected} flag. This list is an {@link ArrayList} to
     * ensure quick index-base access.
     */
    private final List<Entry> entries;

    /**
     * The sort keys reflect the sorting state of this list.
     */
    private Collection<SortKey> sortKeys = Collections.EMPTY_LIST;

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public ListPM() {
        this(10);
    }

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list.
     */
    public ListPM(int initialCapacity) {
        this(new ArrayList<Entry>(initialCapacity));
    }

    /**
     * Constructs a <code>ListPM</code> with the specified list of entries.
     *
     * @param list the initial list of entries
     */
    public ListPM(ArrayList<Entry> list) {
        this((List<Entry>)list);
    }

    /**
     * Constructs a <code>ListPM</code> with the specified list of entries.
     *
     * @param list the initial list of entries
     */
    protected ListPM(List<Entry> list) {
        entries = list;
        this.support.addListListener(this.selfListener);
        this.getValidator().add(this.createDefaultValidationRule());
    }

    public boolean isRevalidateElementsOnChangeEnabled() {
        return revalidateElementsOnChangeEnabled;
    }

    // TODO (mk) write TEST
    public void setRevalidateElementsOnChangeEnabled(boolean enabled) {
        this.revalidateElementsOnChangeEnabled = enabled;
        // TODO (mk) fire pce
    }

    public void addListListener(ListListener l) {
        support.addListListener(l);
    }

    public void removeListListener(ListListener l) {
        support.removeListListener(l);
    }

    protected void onEntriesChanged(EventObject evt) {
        if (revalidateElementsOnChangeEnabled) {
            revalidateAllExcept(null);
        }
        revalidateProperties();
        BnPropertyChangeEvent newEvent = new BnPropertyChangeEvent(this, null, null, null, evt);
        this.getPropertyChangeSupport().firePropertyChange(newEvent);
    }

    /**
     * Revalidates all elements of this <code>ListPM</code>.
     */
    // TODO (mk) TEST
    public void revalidateElements() {
        revalidateAllExcept(null);
    }

    protected void revalidateAllExcept(T element) {
        for (Entry entry : entries) {
            if (element == entry.element)
                continue;
            //entry.element.revalidateAllProperties();

            // TODO (mk) I think here "revalidate" should be enough
            PropertySupport.get(entry.element).revalidateProperties();

            //((Validatable)entry.element).revalidate();
        }
    }

    // TODO (mk) UNIT TEST
    public void replace(T oldElement, T newElement) {
        int index = indexOf(oldElement);
        replace(index, newElement);
    }

    // TODO (mk) UNIT TEST
    public void replace(int index, T newElement) {
        Entry newEntry = new Entry(newElement);
        Entry oldEntry = entries.remove(index);
        newEntry.isSelected = oldEntry.isSelected;
        entries.add(index, newEntry);
        onRemove(oldEntry.element);
        onAdd(newElement);
        support.fireElementsReplaced(index, oldEntry.element);
    }

    public void add(T element) {
        int nextIndex = entries.size();
        Entry entry = new Entry(element);
        entries.add(entry);
        onAdd(element);
        support.fireElementsAdded(nextIndex, 1);
    }

    private void onAdd(T element) {
        element.addPropertyChangeListener(this.elementsPcl);
        if (element instanceof ContextOwner) {
            ((ContextOwner)element).getContext().addParent(this.getContext());
        }

    }

    private void onRemove(T element) {
        element.removePropertyChangeListener(this.elementsPcl);
        if (element instanceof ContextOwner) {
            ((ContextOwner)element).getContext().removeParent(this.getContext());
        }
    }

    public void add(int index, T element) {
        if (index < 0 || index > entries.size()) {
            throw new IndexOutOfBoundsException("index=" + index);
        }
        Entry entry = new Entry(element);
        entries.add(index, entry);
        // update min and max
        //		if (selection.selectionSize>0) {
        //			if (selection.minSelIndex!=UNKNOWN && selection.minSelIndex>=index) {
        //				selection.minSelIndex++;
        //			}
        //			if (selection.maxSelIndex!=UNKNOWN && selection.maxSelIndex<=index) {
        //				selection.maxSelIndex++;
        //			}
        //		}
        onAdd(element);
        support.fireElementsAdded(index, 1);
    }

    // TODO (mk) TEST
    public void addAll(Collection<T> col) {
        int startIndex = size();
        for (T elem : col) {
            Entry entry = new Entry(elem);
            entries.add(entry);
            onAdd(elem);
        }
        support.fireElementsAdded(startIndex, col.size());
    }

    public boolean contains(T element) {
        for (Entry e : entries) {
            if (e.element == element) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAll(Collection<T> col) {
        if (col.isEmpty()) {
            return true;
        } else if (col.size() == 1) {
            return contains(col.iterator().next());
        } else {

        }
        Set<T> set = new HashSet<T>();
        for (Entry e : entries) {
            set.add(e.element);
        }
        return set.containsAll(col);
    }

    public void clear() {
        if (entries.isEmpty()) {
            return; // nothing to do
        } else {
            this.selection.clear();
            int len = entries.size();
            Collection<PresentationModel> removed = (Collection<PresentationModel>)this.toCollection();
            for (Entry e : entries) {
                onRemove(e.element);
            }
            this.entries.clear();
            support.fireElementsRemoved(0, len, removed);
        }
    }

    // TODO (mk) write TEST
    public boolean removeAll(Collection<? extends T> col) {
        int[] indexes = indicesOf(col);
        Interval[] intv = Interval.createIntervals(indexes);
        // we  process the intervals backwards
        for (int i = intv.length - 1; i >= 0; --i) {
            Interval iv = intv[i];
            this.selection.removeInterval(iv.startIndex, iv.endIndex);
            List<PresentationModel> removed = new LinkedList<PresentationModel>();
            for (int index = iv.endIndex; index >= iv.startIndex; --index) {
                Entry e = entries.remove(index);
                onRemove(e.element);
                removed.add(e.element);
            }
            support.fireElementsRemoved(iv.startIndex, iv.endIndex - iv.startIndex + 1, removed);
        }
        return indexes.length > 0;
    }

    public boolean remove(T element) {
        int index = indexOf(element);
        if (index == NONE) {
            return false; // nothing found, nothing changed.
        }
        removeAt(index);
        return true;
    }

    public T removeAt(int index) {
        if (index < 0 || index >= entries.size()) {
            throw new IndexOutOfBoundsException("index=" + index);
        }
        Entry e = entries.get(index);
        assert (e != null);
        if (e.isSelected) {
            e.isSelected = false;
            selection.selectionSize--;
            // update min and max
            //			if (selection.selectionSize>0) {
            //				if (selection.minSelIndex != UNKNOWN && selection.minSelIndex >= index) {
            //					selection.minSelIndex = UNKNOWN;
            //				}
            //				if (selection.maxSelIndex != UNKNOWN && selection.maxSelIndex >= index) {
            //					selection.maxSelIndex = UNKNOWN;
            //				}
            //			} else {
            //				selection.minSelIndex = NONE;
            //				selection.maxSelIndex = NONE;
            //			}
            support.fireElementsDeselected(index, 1);
        }
        onRemove(e.element);
        entries.remove(index);
        support.fireElementsRemoved(index, e.element);
        return e.element;
    }

    public void swap(int indexA, int indexB) {
        if (indexA < 0 || indexA >= entries.size()) {
            throw new IndexOutOfBoundsException("indexA=" + indexA);
        }
        if (indexB < 0 || indexB >= entries.size()) {
            throw new IndexOutOfBoundsException("indexB=" + indexB);
        }
        if (indexA == indexB) {
            return;
        }
        int index1 = Math.min(indexA, indexB);
        int index2 = Math.max(indexA, indexB);

        Entry e1 = entries.get(index1);
        assert (e1 != null);
        T elem1 = e1.element;
        boolean e1Selected = e1.isSelected;

        Entry e2 = entries.get(index2);
        assert (e2 != null);
        T elem2 = e2.element;
        boolean e2Selected = e2.isSelected;

        this.removeAt(index2);
        this.removeAt(index1);
        this.add(index1, elem2);
        this.add(index2, elem1);
        if (e1Selected) {
            selection.add(elem1);
        } else if (selection.contains(index2)) {
            selection.remove(elem1);
        }
        if (e2Selected) {
            selection.add(elem2);
        } else if (selection.contains(index1)) {
            selection.remove(elem2);
        }
    }

    public void swap(T elemA, T elemB) {
        int indexA = indexOf(elemA);
        int indexB = indexOf(elemB);
        swap(indexA, indexB);
    }

    /**
     * Sorts the entries of this list pM by comparing the cells at the end of
     * the given paths.
     *
     * @param ascending if true, the resulting order will be ascending,
     *            otherwise descending.
     * @param paths one or more Path objects must be specified to define which
     *            pM properties will be used for comparison.
     */
    // TODO (mk) TEST
    public void sortBy(boolean ascending, Path... paths) {
        SortKey[] newSortKeys = new SortKey[paths.length];
        for (int i = 0; i < paths.length; ++i) {
            newSortKeys[i] = new SortKey(ascending, paths[i]);
        }
        sortBy(newSortKeys);
    }

    public void sortBy(Collection<SortKey> newSortKeys) {
    	sortBy( newSortKeys.toArray(new SortKey[newSortKeys.size()]));
    }
    
    public void sortBy(SortKey... newSortKeys) {
        ArrayList<T> list = new ArrayList<T>();
        for (Entry entry : entries) {
            list.add(entry.element);
        }
        new SortingHelper().sortBy(list, newSortKeys);
        Collection<T> oldSelection = new ArrayList<T>(this.selection);
        this.clear();
        this.addAll(list);
        this.selection.addAll(oldSelection);
        setSortKeys(newSortKeys);
    }

    private void setSortKeys(SortKey[] newSortKeys) {
        Collection<SortKey> oldValue = this.sortKeys;
        if (newSortKeys == null) {
            this.sortKeys = Collections.EMPTY_LIST;
        } else {
            this.sortKeys = Collections.unmodifiableCollection(Arrays.asList(newSortKeys));
        }
        this.getPropertyChangeSupport().firePropertyChange("sortKeys", oldValue, this.sortKeys);
    }

    /**
     * Returns the (immutable) collection of {@link SortKey} objects that
     * reflect the current sorting state of this list.
     */
    public Collection<SortKey> getSortKeys() {
        return sortKeys;
    }

    public T getAt(int index) {
        return entries.get(index).element;
    }

    public Collection<T> toCollection() {
        ArrayList<T> result = new ArrayList<T>();
        for (Entry entry : entries) {
            result.add(entry.element);
        }
        return result;
    }

    public Object[] toArray() {
        Object[] result = new Object[size()];
        int i = 0;
        for (Entry entry : entries) {
            result[i++] = entry.element;
        }
        return result;
    }

    public Selection<T> getSelection() {
        return this.selection;
    }

    public int indexOf(T element) {
        if (element == null) {
            return NONE;
        }
        int i = 0;
        for (Entry entry : entries) {
            if (element.equals(entry.element)) {
                return i;
            }
            ++i;
        }
        return NONE;
    }

    /**
     * Returns a sorted array of all indices of the given elements starting with
     * the smallest index.
     *
     * @param col all elements to get the index from
     * @return a sorted array of all indices of the given elements
     */
    public int[] indicesOf(Collection<? extends T> col) {
        if (col == null || col.size() == 0 || isEmpty()) {
            return new int[0];
        }
        final int len = col.size();
        int[] result = new int[len];
        int count = 0;
        for (T elem : col) {
            int index = indexOf(elem);
            if (index != -1) {
                result[count] = index;
                count++;
            }
        }
        if (count != len) {
            int[] newResult = new int[count];
            System.arraycopy(result, 0, newResult, 0, count);
            result = newResult;
        }
        Arrays.sort(result);
        return result;
    }

    public int size() {
        return entries.size();
    }

    public boolean isEmpty() {
        return entries == null || entries.isEmpty();
    }

    private void onElementChanged(PropertyChangeEvent evt) {
        if ("modified".equals(evt.getPropertyName()) == false) {
            T element = (T)evt.getSource();
            this.onElementChanged(element, evt);
        }
    }

    private void onElementChanged(T element, EventObject cause) {
        int index = indexOf(element);

        // fire a property change event to inform all listeners up to the root(s)
        this.support.fireElementChanged(index, cause);
    }

    // TODO (mk) write TEST
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int next = 0;
            int last = NONE;

            public boolean hasNext() {
                return next < entries.size();
            }

            public T next() {
                T result = entries.get(next).element;
                last = next;
                next++;
                return result;
            }

            public void remove() {
                removeAt(last);
                next = last;
                last = NONE;
            }
        };
    }

    // TODO (mk) TEST
    public ListIterator<T> listIterator(final int index) {
        return new ListIterator<T>() {
            int next = index;
            int last = NONE;

            public void add(T o) {
                throw new UnsupportedOperationException("'add' ist not supported by this ListIterator");
            }

            public boolean hasNext() {
                return next < entries.size();
            }

            public boolean hasPrevious() {
                return next - 1 >= 0;
            }

            public T next() {
                T result = entries.get(next).element;
                last = next;
                next++;
                return result;
            }

            public int nextIndex() {
                return next;
            }

            public T previous() {
                int previous = next - 1;
                T result = entries.get(previous).element;
                last = previous;
                next--;
                return result;
            }

            public int previousIndex() {
                return next - 1;
            }

            public void remove() {
                removeAt(last);
                next = last;
                last = NONE;
            }

            public void set(T o) {
                throw new UnsupportedOperationException("'set' ist not supported by this ListIterator");
            }
        };
    }

    protected ValidationRule createDefaultValidationRule() {
        return new DefaultValidationRule();
    }   

    private class SelectionImpl implements Selection<T> {
        int selectionSize = 0;

        //		private Integer minSelIndex = NONE;
        //		private Integer maxSelIndex = NONE;

        /** {@inheritDoc} */
        public void addAll() {
            List<Integer> indices = new LinkedList<Integer>();
            int index = 0;
            for (Entry entry : entries) {
                if (entry.isSelected == false) {
                    entry.isSelected = true;
                    selectionSize++;
                    indices.add(index);
                }
                index++;
            }
            //			// update min and max
            //			if (selectionSize>0) {
            //				minSelIndex = 0;
            //				maxSelIndex = entries.size()-1;
            //			} else {
            //				minSelIndex = NONE;
            //				maxSelIndex = NONE;
            //			}
            // TODO (mk) hmm. I know that this event could be fired easily, by just
            // providing startIndex=0, length=size(). But is that ok? Any drawbacks?
            if (!indices.isEmpty()) {
                support.fireElementsSelected(indices);
            }
        }

        /** {@inheritDoc} */
        public boolean setInterval(int beginIndex, int endIndex) {
            if (beginIndex > endIndex) {
                throw new IllegalArgumentException("beginIndex > endIndex");
            }
            List<Integer> addIndices = new LinkedList<Integer>();
            List<Integer> removeIndices = new LinkedList<Integer>();
            int index = 0;
            for (Entry entry : entries) {
                if (beginIndex <= index && index <= endIndex) {
                    if (entry.isSelected == false) {
                        entry.isSelected = true;
                        selectionSize++;
                        addIndices.add(index);
                    }
                } else {
                    if (entry.isSelected == true) {
                        entry.isSelected = false;
                        selectionSize--;
                        removeIndices.add(index);
                    }
                }
                index++;
            }
            // update min and max
            //			if (selectionSize>0) {
            //				minSelIndex = beginIndex;
            //				maxSelIndex = endIndex;
            //			} else {
            //				minSelIndex = NONE;
            //				maxSelIndex = NONE;
            //			}
            if (!removeIndices.isEmpty()) {
                support.fireElementsDeselected(removeIndices);
            }
            if (!addIndices.isEmpty()) {
                support.fireElementsSelected(addIndices);
            }
            return !removeIndices.isEmpty() || !addIndices.isEmpty();
        }

        /**
         * {@inheritDoc} TODO (mk) UNIT TEST
         */
        public boolean setIndexes(int[] selIndices) {
            Set<Integer> indexSet = new HashSet<Integer>();
            for (int index : selIndices) {
                indexSet.add(index);
            }
            List<Integer> addIndices = new LinkedList<Integer>();
            List<Integer> removeIndices = new LinkedList<Integer>();
            int index = 0;
            //			minSelIndex = NONE;
            //			maxSelIndex = NONE;
            for (Entry entry : entries) {
                if (indexSet.contains(index)) {
                    if (entry.isSelected == false) {
                        entry.isSelected = true;
                        selectionSize++;
                        addIndices.add(index);
                    }
                    //					if (minSelIndex == NONE || minSelIndex > index) {
                    //						minSelIndex = index;
                    //					}
                    //					if (maxSelIndex == NONE || maxSelIndex < index) {
                    //						maxSelIndex = index;
                    //					}
                } else {
                    if (entry.isSelected == true) {
                        entry.isSelected = false;
                        selectionSize--;
                        removeIndices.add(index);
                    }
                }
                index++;
            }

            if (!removeIndices.isEmpty()) {
                support.fireElementsDeselected(removeIndices);
            }
            if (!addIndices.isEmpty()) {
                support.fireElementsSelected(addIndices);
            }
            return !removeIndices.isEmpty() || !addIndices.isEmpty();
        }

        /** {@inheritDoc} */
        // TODO (mk) UNIT TEST
        public int[] getIndexes() {
            int[] result = new int[selectionSize];
            int entryIndex = 0;
            int selPos = 0;
            for (Entry entry : entries) {
                if (entry.isSelected) {
                    result[selPos] = entryIndex;
                    selPos++;
                }
                entryIndex++;
            }
            return result;
        }

        /** {@inheritDoc} */
        // TODO (mk) UNIT TEST
        public int[] getIndexes(int beginIndex, int endIndex) {
            if (beginIndex > endIndex) {
                throw new IllegalArgumentException("beginIndex > endIndex");
            }
            int[] tmpresult = new int[selectionSize];

            ListIterator<Entry> it = entries.listIterator(beginIndex);
            int entryIndex = beginIndex;
            int selPos = 0;
            while (it.hasNext() && entryIndex <= endIndex) {
                Entry entry = it.next();
                if (entry.isSelected) {
                    tmpresult[selPos] = entryIndex;
                    selPos++;
                }
                entryIndex++;
            }
            int[] result = new int[selPos];
            System.arraycopy(tmpresult, 0, result, 0, result.length);
            return result;
        }

        /** {@inheritDoc} */
        public boolean addInterval(int beginIndex, int endIndex) {
            if (beginIndex > endIndex) {
                throw new IllegalArgumentException("beginIndex > endIndex");
            }
            ListIterator<Entry> listIt = entries.listIterator(beginIndex);
            int index = beginIndex;
            List<Integer> indices = new LinkedList<Integer>();
            while (listIt.hasNext() && index <= endIndex) {
                Entry e = listIt.next();
                if (e.isSelected == false) {
                    e.isSelected = true;
                    selectionSize++;
                    indices.add(index);
                }
                index++;
            }
            // update min and max
            //			if (!indices.isEmpty()) {
            //				if (selectionSize>0) {
            //					if (minSelIndex==UNKNOWN || minSelIndex==NONE || minSelIndex>beginIndex) {
            //						minSelIndex = beginIndex;
            //					}
            //					if (maxSelIndex==UNKNOWN || maxSelIndex==NONE || maxSelIndex<endIndex) {
            //						maxSelIndex = endIndex;
            //					}
            //				} else {
            //					minSelIndex = NONE;
            //					maxSelIndex = NONE;
            //				}
            //			} // else nothing changed -> no update needed

            // TODO (mk) hmm. I know that this event could be fired easily, by just
            // providing startIndex=beginIndex, length=endIndex-beginIndex. But is that ok? Any drawbacks?
            if (!indices.isEmpty()) {
                support.fireElementsSelected(indices);
                return true;
            } else {
                return false;
            }
        }

        /** {@inheritDoc} */
        public boolean contains(int index) {
            if (index >= entries.size() || index < 0) {
                return false;
            }
            Entry e = entries.get(index);
            return e.isSelected;
        }

        /** {@inheritDoc} */
        public T getFirst() {
            int index = getMinIndex();
            if (index == NONE) {
                return null;
            } else {
                return entries.get(index).element;
            }
        }

        /** {@inheritDoc} */
        public int getMinIndex() {
            //			if (minSelIndex!=UNKNOWN) {
            //				return minSelIndex;
            //			}
            int index = 0;
            for (Entry entry : entries) {
                if (entry.isSelected) {
                    //					minSelIndex = index;
                    //					return minSelIndex;
                    return index;
                }
                index++;
            }
            //			minSelIndex = NONE;
            //			return minSelIndex;
            return NONE;
        }

        /** {@inheritDoc} */
        public int getMaxIndex() {
            //			if (maxSelIndex != UNKNOWN) {
            //				return maxSelIndex;
            //			}
            ListIterator<Entry> it = entries.listIterator(entries.size());
            for (int index = entries.size() - 1; it.hasPrevious(); index--) {
                Entry entry = it.previous();
                if (entry.isSelected) {
                    //					maxSelIndex = index;
                    //					return maxSelIndex;
                    return index;
                }
            }
            //			maxSelIndex = NONE;
            //			return maxSelIndex;
            return NONE;
        }

        /**
         * Returns the index of the next selected element AFTER the given index,
         * or -1 if no element is found.
         *
         * @param index
         * @return the index of the next selected element after the given index,
         *         or -1 if no element is found.
         */
        public int getNextIndex(int index) {
            if (index < 0 || index >= entries.size()) {
                throw new IndexOutOfBoundsException("index=" + index);
            }
            if (selectionSize == 0 || index == entries.size() - 1) {
                return NONE;
            }
            //			if (maxSelIndex != UNKNOWN && maxSelIndex<=index) {
            //				return NONE;
            //			}
            ListIterator<Entry> it = entries.listIterator(++index);
            while (it.hasNext()) {
                Entry entry = it.next();
                if (entry.isSelected) {
                    return index;
                }
                index++;
            }
            return NONE;
        }

        /** {@inheritDoc} */
        public boolean removeInterval(int beginIndex, int endIndex) {
            if (beginIndex > endIndex) {
                throw new IllegalArgumentException("beginIndex > endIndex");
            }
            ListIterator<Entry> listIt = entries.listIterator(beginIndex);
            int index = beginIndex;
            List<Integer> indices = new LinkedList<Integer>();
            while (listIt.hasNext() && index <= endIndex) {
                Entry e = listIt.next();
                if (e.isSelected == true) {
                    e.isSelected = false;
                    selectionSize--;
                    indices.add(index);
                }
                index++;
            }
            // update min and max
            //			if (!indices.isEmpty()) {
            //				if (selectionSize>0) {
            //					assert(minSelIndex!=NONE);
            //					if (minSelIndex!=UNKNOWN && minSelIndex>=beginIndex) {
            //						minSelIndex = UNKNOWN;
            //					}
            //					assert(maxSelIndex!=NONE);
            //					if (maxSelIndex!=UNKNOWN && maxSelIndex<=endIndex) {
            //						maxSelIndex = UNKNOWN;
            //					}
            //				} else {
            //					minSelIndex = NONE;
            //					maxSelIndex = NONE;
            //				}
            //			} // else nothing changed -> no updated needed

            if (!indices.isEmpty()) {
                support.fireElementsDeselected(indices);
                return true;
            } else {
                return false;
            }
        }

        /** {@inheritDoc} */
        public boolean add(T o) {
            int index = indexOf(o);
            if (index == NONE) {
                throw new NoSuchElementException("Can't select unknown element: o=" + o);
            }
            Entry e = entries.get(index);
            if (e.isSelected == false) {
                e.isSelected = true;
                selectionSize++;
                //				// update min and max
                //				if (minSelIndex==UNKNOWN || minSelIndex==NONE || minSelIndex>index) {
                //					minSelIndex = index;
                //				}
                //				if (maxSelIndex==UNKNOWN || maxSelIndex==NONE || maxSelIndex<index) {
                //					maxSelIndex = index;
                //				}
                support.fireElementsSelected(index, 1);
                return true;
            } else {
                return false;
            }
        }

        /** {@inheritDoc} */
        public boolean addAll(Collection<? extends T> c) {
            Set argSet;
            if (c instanceof Set) {
                argSet = (Set)c;
            } else {
                argSet = new HashSet(c);
            }
            List<Integer> indices = new LinkedList<Integer>();
            int index = 0;
            for (Entry e : entries) {
                if (argSet.remove(e.element)) {
                    if (e.isSelected == false) {
                        e.isSelected = true;
                        selectionSize++;
                        // update min and max
                        //						if (minSelIndex==UNKNOWN || minSelIndex==NONE || minSelIndex>index) {
                        //							minSelIndex = index;
                        //						}
                        //						if (maxSelIndex==UNKNOWN || maxSelIndex==NONE || maxSelIndex<index) {
                        //							maxSelIndex = index;
                        //						}
                        indices.add(index);
                    }
                }
                index++;
            }
            if (!indices.isEmpty()) {
                support.fireElementsSelected(indices);
                return true;
            } else {
                return false;
            }
        }

        /** {@inheritDoc} */
        public void clear() {
            boolean changed = false;
            for (Entry e : entries) {
                if (e.isSelected) {
                    e.isSelected = false;
                    changed = true;
                    selectionSize--;
                }
            }
            assert (selectionSize == 0);

            // update min and max
            //			minSelIndex = NONE;
            //			maxSelIndex = NONE;

            if (changed) {
                // TODO (mk) is this 'generic' event allowed here even if
                // not all elements were selected?
                support.fireElementsDeselected(0, entries.size());
            }
        }

        /** {@inheritDoc} */
        public boolean contains(Object o) {
            for (Entry e : entries) {
                if (e.element.equals(o)) {
                    return e.isSelected;
                }
            }
            return false;
        }

        /** {@inheritDoc} */
        public boolean containsAll(Collection<?> c) {
            Set argSet;
            if (c instanceof Set) {
                argSet = (Set)c;
            } else {
                argSet = new HashSet(c);
            }
            for (Entry e : entries) {
                if (argSet.remove(e.element)) {
                    if (e.isSelected == false) {
                        return false;
                    } else {
                        continue;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        /** {@inheritDoc} */
        public boolean isEmpty() {
            return selectionSize == 0;
        }

        /** {@inheritDoc} */
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                int next = getMinIndex();
                int last;

                public boolean hasNext() {
                    return next != NONE;
                }

                public T next() {
                    Entry e = entries.get(next);
                    last = next;
                    next = getNextIndex(next);
                    return e.element;
                }

                public void remove() {
                    Entry e = entries.get(last);
                    e.isSelected = false;
                    selectionSize--;
                    // update min and max
                    //					if (selectionSize==0) {
                    //						minSelIndex = NONE;
                    //						maxSelIndex = NONE;
                    //					} else {
                    //						if (minSelIndex != UNKNOWN && minSelIndex == last) {
                    //							minSelIndex = UNKNOWN;
                    //						}
                    //						if (maxSelIndex != UNKNOWN && maxSelIndex == last) {
                    //							maxSelIndex = UNKNOWN;
                    //						}
                    //					}
                    support.fireElementsDeselected(last, 1);
                }
            };
        }

        /** {@inheritDoc} */
        public boolean remove(Object o) {
            int index = 0;
            for (Entry e : entries) {
                if (e.element.equals(o)) {
                    if (e.isSelected == true) {
                        e.isSelected = false;
                        selectionSize--;
                        // update min and max
                        //						if (selectionSize==0) {
                        //							minSelIndex = NONE;
                        //							maxSelIndex = NONE;
                        //						} else {
                        //							if (minSelIndex == null || minSelIndex==index) {
                        //								minSelIndex = UNKNOWN;
                        //							}
                        //							if (maxSelIndex == null || maxSelIndex == index) {
                        //								maxSelIndex = UNKNOWN;
                        //							}
                        //						}
                        support.fireElementsDeselected(index, 1);
                        return true;
                    } else {
                        return false;
                    }
                }
                index++;
            }
            return false;
        }

        /** {@inheritDoc} */
        public boolean removeAll(Collection<?> c) {
            Set argSet;
            if (c instanceof Set) {
                argSet = (Set)c;
            } else {
                argSet = new HashSet(c);
            }
            List<Integer> indices = new LinkedList<Integer>();
            int index = 0;
            for (Entry e : entries) {
                if (argSet.remove(e.element)) {
                    if (e.isSelected) {
                        e.isSelected = false;
                        selectionSize--;
                        // update min and max
                        //						if (selectionSize == 0) {
                        //							minSelIndex = NONE;
                        //							maxSelIndex = NONE;
                        //						} else {
                        //							if (minSelIndex != UNKNOWN && minSelIndex == index) {
                        //								minSelIndex = UNKNOWN;
                        //							}
                        //							if (maxSelIndex != UNKNOWN && maxSelIndex == index) {
                        //								maxSelIndex = UNKNOWN;
                        //							}
                        //						}
                        indices.add(index);
                    }
                } // else ignore, since the result is not affected
                index++;
            }

            if (!indices.isEmpty()) {
                support.fireElementsDeselected(indices);
                return true;
            } else {
                return false;
            }
        }

        /** {@inheritDoc} */
        public boolean retainAll(Collection<?> c) {
            Set argSet;
            if (c instanceof Set) {
                argSet = (Set)c;
            } else {
                argSet = new HashSet(c);
            }
            List<Integer> indices = new LinkedList<Integer>();
            int index = 0;
            for (Entry e : entries) {
                if (argSet.remove(e.element) == false) {
                    if (e.isSelected == true) {
                        e.isSelected = false;
                        selectionSize--;
                        indices.add(index);
                        // update min and max
                        //						if (selectionSize>0) {
                        //							if (minSelIndex != UNKNOWN && minSelIndex == index) {
                        //								minSelIndex = UNKNOWN;
                        //							}
                        //							if (maxSelIndex != UNKNOWN && maxSelIndex == index) {
                        //								maxSelIndex = UNKNOWN;
                        //							}
                        //						} else {
                        //							minSelIndex = NONE;
                        //							maxSelIndex = NONE;
                        //						}
                    }
                }
            }
            if (!indices.isEmpty()) {
                support.fireElementsDeselected(indices);
                return true;
            } else {
                return false;
            }
        }

        /** {@inheritDoc} */
        public int size() {
            return selectionSize;
        }

        /** {@inheritDoc} */
        public Object[] toArray() {
            Object[] result = new Object[selectionSize];
            int index = 0;
            for (Entry e : entries) {
                if (e.isSelected) {
                    result[index] = e.element;
                    index++;
                }
            }
            return result;
        }

        /** {@inheritDoc} */
        public <T> T[] toArray(T[] a) {
            if (a.length != selectionSize) {
                a = (T[])Array.newInstance(a.getClass().getComponentType(), selectionSize);
            }
            int index = 0;
            for (Entry e : entries) {
                if (e.isSelected) {
                    a[index] = (T)e.element;
                    index++;
                }
            }
            return a;
        }

        /**
         * Returns a new Collection with all selected elements. Modification on
         * this collection will not influence the original selection.
         *
         * @return a new Collection with all selected elements.
         */
        public Collection<T> toCollection() {
            return new ArrayList<T>(this);
        }
    }

    private class Entry {
        T element;
        boolean isSelected;

        Entry(T element) {
            this.element = element;
        }

        @Override
        public String toString() {
            return element.toString();
        }
    }

    /**
     * This validation rule defines that this model is invalid whenever a list
     * element is invalid.
     *
     * @author Michael Karneim
     */
    public class DefaultValidationRule implements ValidationRule {
        public ValidationState validate() {
            if (isEmpty()) {
                return null;
            }
            for (T pModel1 : ListPM.this) {
                if (pModel1.isValid() == false) {
                    // TODO (mk) i18n
                    return new ValidationState("One or more elements are invalid");
                }
            }
            return null;
        }
    }
}