/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
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
import org.beanfabrics.util.OrderPreservingMap;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * The MapPM is a map of presentation models. Basically it provides methods for
 * adding, removing, accessing and iterating elements and informs listeners
 * about changes. It also maintains a {@link Selection}.
 *
 * @author Michael Karneim
 */
public class MapPM<K, V extends PresentationModel> extends AbstractPM implements IMapPM<K, V> {

    private static final int NONE = -1;

    private final OrderPreservingMap<K, V> entries = new OrderPreservingMap<K, V>();

    private final SelectedKeysImpl selectedKeys = new SelectedKeysImpl();

    private final Selection selection = new SelectionImpl();

    private final ListSupport support = new ListSupport(this);

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

    private boolean revalidateElementsOnChangeEnabled = false;

    /**
     * The sort keys reflect the sorting state of this list.
     */
    private Collection<SortKey> sortKeys = Collections.emptyList();

    public MapPM() {
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

    protected void onEntriesChanged(EventObject evt) {
        if (revalidateElementsOnChangeEnabled) {
            revalidateAllExcept(null);
        }
        revalidateProperties();
        BnPropertyChangeEvent newEvent = new BnPropertyChangeEvent(this, null, null, null, evt);
        this.getPropertyChangeSupport().firePropertyChange(newEvent);
    }

    /**
     * Revalidates all elements of this map.
     */
    // TODO (mk) write TEST
    public void revalidateElements() {
        revalidateAllExcept(null);
    }

    protected void revalidateAllExcept(V element) {
        for (V e : entries.valuesReference()) {
            if (element == e)
                continue;
            //e.revalidateAllProperties();

            // TODO (mk) I think here "revalidate" should be enough
            PropertySupport.get(e).revalidateProperties();

            //((Validatable)e).revalidate();

        }
    }

    protected ListSupport getListPropertySupport() {
        return this.support;
    }

    private void checkElementsType(Class aType) {
        for (PresentationModel element : entries.values()) {
            if (aType.isInstance(element) == false) {
                throw new IllegalArgumentException("map is not empty, so new element type must be superclass of " + element.getClass().getName());
            }
        }
    }

    public boolean isEmpty() {
        return entries == null || entries.isEmpty();
    }

    public void clear() {
        selectedKeys.clear();
        int oldLen = this.size();
        if (oldLen > 0) {
            Collection<PresentationModel> oldEds = (Collection<PresentationModel>)this.entries.toCollection();
            for (PresentationModel element : oldEds) {
                onRemove((V)element);
            }
            entries.clear();
            this.support.fireElementsRemoved(0, oldLen, oldEds);
        }
    }

    public V put(K key, V newElement) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        if (newElement == null) {
            throw new IllegalArgumentException("element must not be null");
        }

        V result = entries.put(key, newElement);
        if ( result == newElement) {
            return result;
        }
        onAdd(newElement);
        int index = indexOfKey(key);
        if (result != null) {
            // an old element with equal key has been replaced
            onRemove(result);

            support.fireElementsReplaced(index, result);
        } else {
            // a new element was added
            support.fireElementsAdded(index, 1);
        }
        return result;
    }

    private void onRemove(V element) {
        element.removePropertyChangeListener(this.elementsPcl);
        if (element instanceof ContextOwner) {
            ((ContextOwner)element).getContext().removeParent(this.getContext());
        }
    }

    private void onAdd(V element) {
        element.addPropertyChangeListener(this.elementsPcl);
        if (element instanceof ContextOwner) {
            ((ContextOwner)element).getContext().addParent(this.getContext());
        }
    }

    // TODO (mk) TEST
    public V put(K key, V newElement, int toIndex) {
        if (toIndex < 0 || toIndex > entries.size()) {
            throw new IndexOutOfBoundsException("toIndex=" + toIndex);
        }
        V oldElement = entries.get(key);
        boolean selected = selectedKeys.contains(key);
        if (oldElement == null) {
            // this is a new key
            entries.put(key, newElement, toIndex);
            onAdd(newElement);
            support.fireElementsAdded(toIndex, 1);
            return null;
        } else {
            // this was an old key
            int oldIndex = entries.indexOfKey(key);
            if (oldIndex == toIndex) {
                // don't move, just replace
                entries.put(key, newElement);
                onAdd(newElement);
                onRemove(oldElement);
                support.fireElementsReplaced(oldIndex, oldElement);
                return oldElement;
            } else {
                // first remove old one
                if (selected) {
                    selection.remove(oldIndex);
                }
                entries.remove(oldIndex);
                onRemove(oldElement);
                // then insert new one
                support.fireElementsRemoved(oldIndex, oldElement);
                entries.put(key, newElement, toIndex);
                onAdd(newElement);
                support.fireElementsAdded(toIndex, 1);
                if (selected) {
                    selection.addInterval(toIndex, toIndex);
                }
                return oldElement;
            }
        }
    }

    // TODO (mk) TEST
    public void swap(int indexA, int indexB) {
        if (indexA < 0 || indexA > entries.size()) {
            throw new IndexOutOfBoundsException("indexA=" + indexA);
        }
        if (indexB < 0 || indexB > entries.size()) {
            throw new IndexOutOfBoundsException("indexB=" + indexB);
        }
        if (indexA == indexB) {
            return;
        }
        int index1 = Math.min(indexA, indexB);
        int index2 = Math.max(indexA, indexB);

        boolean isSelected1 = this.selection.contains(index1);
        boolean isSelected2 = this.selection.contains(index2);

        K key2 = getKey(index2);
        V element2 = removeAt(index2);
        K key1 = getKey(index1);
        V element1 = removeAt(index1);
        this.put(key2, element2, index1);
        this.put(key1, element1, index2);
        if (isSelected1) {
            this.selectedKeys.add(key1);
        } else if (selectedKeys.contains(key1)) {
            this.selectedKeys.remove(key1);
        }
        if (isSelected2) {
            this.selectedKeys.add(key2);
        } else if (selectedKeys.contains(key2)) {
            this.selectedKeys.remove(key2);
        }
    }

    /**
     * Swaps the position of these elements.
     */
    public void swap(V elemA, V elemB) {
        int indexA = indexOf(elemA);
        int indexB = indexOf(elemB);
        swap(indexA, indexB);
    }

    // TODO (mk) TEST
    /**
     * Sorts the entries of this map by comparing the {@link PresentationModel}s
     * at the end of the given paths.
     *
     * @param ascending if true, the resulting order will be ascending,
     *            otherwise descending.
     * @param paths one or more Path objects must be specified to define which
     *            {@link PresentationModel} properties will be used for
     *            comparison.
     */
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
        OrderPreservingMap<K, V> map = new OrderPreservingMap<K, V>(entries);
        new SortingHelper().sortBy(map, newSortKeys);
        Set<K> oldSelection = new HashSet<K>(this.selectedKeys);
        this.clear();
        this.putAll(map);
        this.getSelectedKeys().addAll(oldSelection);
        setSortKeys(newSortKeys);
    }

    private void setSortKeys(SortKey[] newSortKeys) {
        Collection<SortKey> oldValue = this.sortKeys;
        if (newSortKeys == null) {
            this.sortKeys = Collections.emptyList();
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

    public int size() {
        return entries.size();
    }

    public Collection<V> getAll(int startIndex, int endIndex) {
        Collection<V> result = new LinkedList<V>();
        for (int i = startIndex; i <= endIndex; ++i) {
            result.add(entries.get(i));
        }
        return result;
    }

    public Collection<V> getAll(int[] indices) {
        Collection<V> result = new LinkedList<V>();
        for (int index : indices) {
            result.add(entries.get(index));
        }
        return result;
    }

    public V getAt(int index) {
        return entries.get(index);
    }

    public V get(K key) {
        return entries.get(key);
    }

    public V removeAt(int index) {
        K key = entries.getKey(index);
        selectedKeys.remove(key);
        V result = entries.remove(index);
        onRemove(result);
        if (result != null) {
            // the element has been removed
            Collection<PresentationModel> oldEds = new ArrayList<PresentationModel>();
            oldEds.add(result);
            support.fireElementsRemoved(index, 1, oldEds);
        }
        return result;
    }

    public V removeKey(K key) {
        selectedKeys.remove(key);
        int index = indexOfKey(key);
        if (index >= 0) {
            // element found. remove it
            V result = entries.remove(key);
            assert (result != null);
            onRemove(result);
            Collection<PresentationModel> oldEds = new ArrayList<PresentationModel>();
            oldEds.add(result);
            support.fireElementsRemoved(index, 1, oldEds);
            return result;
        } else {
            // element not found
            return null;
        }
    }

    public boolean remove(V elem) {
        int index = entries.indexOf(elem);
        if (index == -1) {
            return false;
        } else {
            selection.remove(elem);
            entries.remove(index);
            onRemove(elem);
            Collection<PresentationModel> removedCol = new ArrayList<PresentationModel>();
            removedCol.add(elem);
            support.fireElementsRemoved(index, 1, removedCol);
            return true;
        }
    }

    // TODO (mk) UNIT TEST
    public boolean removeAll(Collection<? extends V> col) {
        int[] indexes = indicesOf(col);
        if (indexes.length == 0) {
            return false;
        } else {
            return removeAllIndices(indexes).size() > 0;
        }
    }

    public Collection<V> toCollection() {
        return entries.toCollection();
    }

    public Object[] toArray() {
        return entries.toArray();
    }

    public Set<K> keySet() {
        return entries.keySet();
    }

    public boolean containsKey(K key) {
        return entries.containsKey(key);
    }

    public boolean contains(V element) {
        return entries.containsValue(element);
    }

    public int indexOfKey(K key) {
        return entries.indexOfKey(key);
    }

    public int indexOf(V element) {
        return entries.indexOf(element);
    }

    // TODO (mk) spelling: rename to "indexes"
    /**
     * Returns an sorted array of all indices of the elements with the given
     * keys starting with the smallest index.
     */
    public int[] indicesOfKeys(Collection<K> keys) {
        final int len = keys.size();
        int[] result = new int[len];
        int i = 0;
        for (K key : keys) {
            result[i] = indexOfKey(key);
            ++i;
        }
        if (len != i) {
            int[] newResult = new int[i];
            System.arraycopy(result, 0, newResult, 0, i);
            result = newResult;
        }
        Arrays.sort(result);
        return result;
    }

    /**
     * Returns an sorted array of all indices of the given elements starting
     * with the smallest index.
     */
    public int[] indicesOf(Collection elements) {
        final int len = elements.size();
        int[] result = new int[len];
        int i = 0;
        for (Object elem : elements) {
            result[i] = indexOf((V)elem);
            ++i;
        }
        if (len != i) {
            int[] newResult = new int[i];
            System.arraycopy(result, 0, newResult, 0, i);
            result = newResult;
        }
        Arrays.sort(result);
        return result;
    }

    public K getKey(V element) {
        int index = entries.indexOf(element);
        if (index == -1) {
            throw new NoSuchElementException("No such element");
        }
        K result = entries.getKey(index);
        return result;
    }

    public K getKey(int index) {
        return this.entries.getKey(index);
    }

    public List<K> getKeys(int beginIndex, int endIndex) {
        List<K> result = new ArrayList<K>();
        for (int index = beginIndex; index <= endIndex; ++index) {
            K key = getKey(index);
            result.add(key);
        }
        return result;
    }

    public Set<K> getKeys(int[] indices) {
        Set<K> result = new HashSet<K>();
        for (int index : indices) {
            K key = getKey(index);
            result.add(key);
        }
        return result;
    }

    /**
     * Returns a set of all keys of the elements in the given collection. For
     * elements that are not in this map no key is inserted into the result.
     *
     * @param col
     * @return a set of all keys of the elements in the given collection
     */
    public Set<K> getKeys(Collection<?> col) {
        HashSet<K> result = new HashSet<K>();
        for (Object elem : col) {
            if (elem instanceof PresentationModel) {
                int index = indexOf((V)elem);
                if (index != -1) {
                    K key = getKey(index);
                    if (key != null) {
                        result.add(key);
                    }
                }
            }
        }
        return result;
    }

    private void checkContainsKey(K key) {
        if (!entries.containsKey(key)) {
            throw new NoSuchElementException("No element with key='" + key + "' in this map");
        }
    }

    public SelectedKeys<K> getSelectedKeys() {
        return this.selectedKeys;
    }

    public Selection<V> getSelection() {
        return this.selection;
    }

    private class SelectionImpl implements Selection<V> {
        /** {@inheritDoc} */
        public boolean addInterval(int beginIndex, int endIndex) {
            if (beginIndex > endIndex) {
                throw new IllegalArgumentException("beginIndex > endIndex");
            }
            ListIterator<K> it = entries.keyListIterator(beginIndex);
            HashSet<K> keys = new HashSet<K>();
            for (int i = beginIndex; it.hasNext() && i <= endIndex; i++) {
                keys.add(it.next());
            }
            return selectedKeys.addAll(keys);
        }

        /** {@inheritDoc} */
        public boolean setInterval(int beginIndex, int endIndex) {
            if (beginIndex > endIndex) {
                throw new IllegalArgumentException("beginIndex > endIndex");
            }
            ListIterator<K> it = entries.keyListIterator(beginIndex);
            HashSet<K> keys = new HashSet<K>();
            for (int i = beginIndex; it.hasNext() && i <= endIndex; i++) {
                keys.add(it.next());
            }
            return selectedKeys.setAll(keys);
        }

        /** {@inheritDoc} */
        // TODO (mk) UNIT TEST
        public boolean setIndexes(int[] selIndices) {
            Set<K> newSelection = getKeys(selIndices);
            return selectedKeys.setAll(newSelection);
        }

        /** {@inheritDoc} */
        public void clear() {
            selectedKeys.clear();
        }

        /** {@inheritDoc} */
        public boolean contains(int index) {
            if (index == NONE) {
                return false;
            }
            K key = getKey(index);
            return selectedKeys.contains(key);
        }

        /** {@inheritDoc} */
        // TODO (mk) TEST
        public int[] getIndexes() {
            int[] result = indicesOfKeys(selectedKeys.elements);
            return result;
        }

        /** {@inheritDoc} */
        public int[] getIndexes(int beginIndex, int endIndex) {
            if (beginIndex > endIndex) {
                throw new IllegalArgumentException("beginIndex > endIndex");
            }
            List<K> keys = getKeys(beginIndex, endIndex);
            keys.retainAll(selectedKeys.elements);
            int[] result = indicesOfKeys(keys);
            return result;
        }

        /** {@inheritDoc} */
        public int getMinIndex() {
            return selectedKeys.getMinSelIndex();
        }

        /** {@inheritDoc} */
        // TODO (mk) UNIT TEST
        public int getMaxIndex() {
            return selectedKeys.getMaxSelIndex();
        }

        /** {@inheritDoc} */
        public V getFirst() {
            int firstIndex = this.getMinIndex();
            if (firstIndex == -1) {
                return null;
            } else {
                return entries.get(firstIndex);
            }
        }

        /** {@inheritDoc} */
        public boolean removeInterval(int beginIndex, int endIndex) {
            if (beginIndex > endIndex) {
                throw new IllegalArgumentException("beginIndex > endIndex");
            }
            ListIterator<K> it = entries.keyListIterator(beginIndex);
            HashSet<K> keys = new HashSet<K>();
            for (int i = beginIndex; it.hasNext() && i <= endIndex; i++) {
                keys.add(it.next());
            }
            return selectedKeys.removeAll(keys);
        }

        /** {@inheritDoc} */
        public boolean add(V o) {
            K key = getKey(o);
            return selectedKeys.add(key);
        }

        /** {@inheritDoc} */
        public boolean addAll(Collection<? extends V> c) {
            Set<K> keys = getKeys(c);
            return selectedKeys.addAll(keys);
        }

        /** {@inheritDoc} */
        public boolean contains(Object o) {
            if (o instanceof PresentationModel) {
                K key = getKey((V)o);
                return selectedKeys.contains(key);
            } else {
                return false;
            }
        }

        /** {@inheritDoc} */
        public boolean containsAll(Collection<?> c) {
            Set<K> keys = getKeys(c);
            if (keys.size() != c.size()) {
                return false;
            }
            return selectedKeys.containsAll(keys);
        }

        /** {@inheritDoc} */
        public boolean isEmpty() {
            return selectedKeys.isEmpty();
        }

        /** {@inheritDoc} */
        public Iterator<V> iterator() {
            return new Iterator<V>() {
                Iterator<K> keyIterator = selectedKeys.iterator();

                public boolean hasNext() {
                    return keyIterator.hasNext();
                }

                public V next() {
                    K key = keyIterator.next();
                    return entries.get(key);
                }

                public void remove() {
                    keyIterator.remove();
                }

            };
        }

        /** {@inheritDoc} */
        public boolean remove(Object o) {
            if (o instanceof PresentationModel) {
                K key = getKey((V)o);
                return selectedKeys.remove(key);
            } else {
                // nothing to do
                return false;
            }
        }

        /** {@inheritDoc} */
        public boolean removeAll(Collection<?> c) {
            Set<K> keys = getKeys(c);
            return selectedKeys.removeAll(keys);
        }

        /** {@inheritDoc} */
        public boolean retainAll(Collection<?> c) {
            Set<K> keys = getKeys(c);
            return selectedKeys.retainAll(keys);
        }

        /** {@inheritDoc} */
        public void addAll() {
            selectedKeys.addAll(entries.orderedKeysReference());
        }

        /** {@inheritDoc} */
        public int size() {
            return selectedKeys.size();
        }

        /** {@inheritDoc} */
        public Object[] toArray() {
            Set<K> keys = selectedKeys.elements;
            return entries.getAll(keys).toArray();
        }

        /** {@inheritDoc} */
        public <T> T[] toArray(T[] a) {
            Set<K> keys = selectedKeys.elements;
            return entries.getAll(keys).toArray(a);
        }

        /**
         * Returns a new Collection with all selected elements. Modification on
         * this collection will not influence the original selection.
         *
         * @return a new Collection with all selected elements.
         */
        public Collection<V> toCollection() {
            Set<K> keys = selectedKeys.elements;
            return entries.getAll(keys);
        }

    }

    private class SelectedKeysImpl implements SelectedKeys<K> {
        /**
         * Keys of all selected elements. Iteration ordering is the order in
         * which elements were inserted into the set.
         */
        Set<K> elements = new LinkedHashSet<K>();
        Integer minSelIndex;
        Integer maxSelIndex;

        public Integer getMinSelIndex() {
            if (minSelIndex == null) {
                if (isEmpty()) {
                    minSelIndex = NONE;
                } else {
                    K firstSelectedKey = min();
                    int index = indexOfKey(firstSelectedKey);
                    minSelIndex = index;
                }
            }
            Integer result = minSelIndex;
            minSelIndex = null; // TODO (mk) enable caching for minSelIndex
            return result;
        }

        public Integer getMaxSelIndex() {
            if (maxSelIndex == null) {
                if (isEmpty()) {
                    maxSelIndex = NONE;
                } else {
                    K lastSelectedKey = max();
                    int index = indexOfKey(lastSelectedKey);
                    maxSelIndex = index;
                }
            }
            Integer result = maxSelIndex;
            maxSelIndex = null; // TODO (mk) enable caching for maxSelIndex
            return result;
        }

        public K getFirst() {
            return min();
        }

        public K min() {
            if (elements.isEmpty()) {
                return null;
            } else if (elements.size() == 1) {
                return elements.iterator().next();
            } else {
                ListIterator<K> it = entries.keyListIterator(0);
                while (it.hasNext()) {
                    K key = it.next();
                    if (elements.contains(key)) {
                        return key;
                    }
                }
            }
            return null;
        }

        public K max() {
            if (elements.isEmpty()) {
                return null;
            } else if (elements.size() == 1) {
                return elements.iterator().next();
            } else {
                ListIterator<K> it = entries.keyListIterator(entries.size());
                while (it.hasPrevious()) {
                    K key = it.previous();
                    if (elements.contains(key)) {
                        return key;
                    }
                }
            }
            return null;
        }

        public boolean add(K key) {
            // check if key is valid
            int index = indexOfKey(key);
            // index <0 means it doesn't exists in the outer map.
            if (index < 0) {
                throw new NoSuchElementException("Can't select unknown element: key=" + key);
            }
            boolean result = elements.add(key);
            if (result) {
                support.fireElementsSelected(index, 1);
            }
            return result;
        }

        public boolean addAll(Collection<? extends K> c) {
            Collection<K> diff = new LinkedHashSet<K>(c);
            diff.removeAll(elements);
            if (!diff.isEmpty()) {
                // check for existence of all elements.
                int[] indices = indicesOfKeys(diff);
                for (int index : indices) {
                    if (index < 0) {
                        // index <0 means it doesn't exists in the outer map.
                        // TODO (mk) we should include the invalid key into the
                        // exception message
                        throw new IllegalArgumentException("key is no element of this map model");
                    }
                }
                elements.addAll(diff);
                support.fireElementsSelected(indices);
                return true;
            } else {
                // nothing to do
                return false;
            }
        }

        public void clear() {
            int[] indices = indicesOfKeys(this);
            elements.clear();
            support.fireElementsDeselected(indices);
        }

        public boolean contains(Object o) {
            return elements.contains(o);
        }

        public boolean containsAll(Collection<?> c) {
            return elements.containsAll(c);
        }

        public boolean isEmpty() {
            return elements.isEmpty();
        }

        public Iterator<K> iterator() {
            return new Iterator<K>() {
                private Iterator<K> impl = elements.iterator();
                private K last;

                public boolean hasNext() {
                    return impl.hasNext();
                }

                public K next() {
                    last = impl.next();
                    return last;
                }

                public void remove() {
                    int index = indexOfKey(last);
                    impl.remove();
                    support.fireElementsDeselected(new int[] { index });
                }
            };
        }

        public boolean remove(Object o) {
            boolean result = elements.remove(o);
            // we won't throw any exception for invalid elements
            if (result) {
                int index = indexOfKey((K)o);
                assert (index >= 0);
                support.fireElementsDeselected(new int[] { index });
            }
            return result;
        }

        public boolean removeAll(Collection<?> c) {
            Collection diff = new LinkedHashSet(c);
            // we won't throw any exception for invalid elements
            // -> get rid of all elements in c that are not element of this
            // selectedKeys
            diff.retainAll(elements);
            if (!diff.isEmpty()) {
                boolean changed = elements.removeAll(diff);
                if (changed) {
                    int[] indices = indicesOfKeys(diff);
                    support.fireElementsDeselected(indices);
                }
                return changed;
            } else {
                // nothing to do
                return false;
            }
        }

        public boolean retainAll(Collection<?> c) {
            Collection diff = new LinkedHashSet(elements);
            diff.removeAll(c);
            if (!diff.isEmpty()) {
                return this.removeAll(diff);
            } else {
                // nothing to do
                return false;
            }
        }

        public boolean setAll(Collection<?> c) {
            Collection toRemove = new LinkedHashSet(elements);
            toRemove.removeAll(c);
            Collection toAdd = new LinkedHashSet(c);
            toAdd.removeAll(elements);
            boolean changed = false;
            if (!toRemove.isEmpty()) {
                boolean removed = elements.removeAll(toRemove);
                if (removed) {
                    int[] indices = indicesOfKeys(toRemove);
                    support.fireElementsDeselected(indices);
                }
                changed = removed;
            }
            if (!toAdd.isEmpty()) {
                int[] indices = indicesOfKeys(toAdd);
                for (int index : indices) {
                    if (index < 0) {
                        // index <0 means it doesn't exists in the outer map.
                        // TODO (mk) we should include the invalid key into the
                        // exception message
                        throw new IllegalArgumentException("key is no element of this map model");
                    }
                }
                boolean added = elements.addAll(toAdd);
                if (added) {
                    support.fireElementsSelected(indices);
                }
                changed = changed || added;
            }
            return changed;
        }

        public int size() {
            return elements.size();
        }

        public Object[] toArray() {
            return elements.toArray();
        }

        public <T> T[] toArray(T[] a) {
            return elements.toArray(a);
        }

        /**
         * Returns a new Collection with all selected keys. Modification on this
         * collection will not influence the original selection.
         *
         * @return a new Collection with all selected keys.
         */
        public Collection<K> toCollection() {
            return new ArrayList<K>(elements);
        }

    }

    public void addListListener(ListListener l) {
        this.support.addListListener(l);
    }

    public void removeListListener(ListListener l) {
        this.support.removeListListener(l);
    }

    private void onElementChanged(PropertyChangeEvent evt) {
        if ("modified".equals(evt.getPropertyName()) == false) {
            V element = (V)evt.getSource();
            this.onElementChanged(element, evt);
        }
    }

    private void onElementChanged(V element, EventObject cause) {
        int index = indexOf(element);
        // fire a property change event to inform all listeners up to the
        // root(s)
        this.support.fireElementChanged(index, cause);
    }

    public Iterator<V> iterator() {
        return new Iterator<V>() {
            int next = 0;
            int last = -1;

            public boolean hasNext() {
                return next < entries.size();
            }

            public V next() {
                V result = entries.get(next);
                last = next;
                next++;
                return result;
            }

            public void remove() {
                removeAt(last);
                next = last;
                last = -1;
            }
        };
    }

    // TODO (mk) TEST
    public ListIterator<V> listIterator(final int index) {
        return new ListIterator<V>() {
            int next = index;
            int last = -1;

            public void add(V o) {
                throw new UnsupportedOperationException("'add' ist not supported by this ListIterator");
            }

            public boolean hasNext() {
                return next < entries.size();
            }

            public boolean hasPrevious() {
                return next - 1 >= 0;
            }

            public V next() {
                V result = entries.get(next);
                last = next;
                next++;
                return result;
            }

            public int nextIndex() {
                return next;
            }

            public V previous() {
                int previous = next - 1;
                V result = entries.get(previous);
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
                last = -1;
            }

            public void set(V o) {
                throw new UnsupportedOperationException("'set' ist not supported by this ListIterator");
            }
        };
    }

    public Iterator<K> keyiterator() {
        return new Iterator<K>() {
            int next = 0;
            int last = -1;

            public boolean hasNext() {
                return next < entries.size();
            }

            public K next() {
                K result = entries.getKey(next);
                last = next;
                next++;
                return result;
            }

            public void remove() {
                removeAt(last);
                next = last;
                last = -1;
            }
        };
    }

    public Collection<V> removeAllKeys(Set<K> keySet) {
        int[] indices = indicesOfKeys(keySet);
        Collection<V> result = this.removeAllIndices(indices);
        return result;
    }

    public Collection<V> retainAllKeys(Set<K> keySet) {
        Set<K> toRemove = entries.keySet();
        toRemove.removeAll(keySet);
        return removeAllKeys(toRemove);
    }

    public Collection<V> removeAllIndices(int[] indices) {
        Interval[] aintv = Interval.createIntervals(indices);
        LinkedList<V> result = new LinkedList<V>();

        for (int iv = aintv.length - 1; iv >= 0; --iv) {
            Interval intv = aintv[iv];
            selection.removeInterval(intv.startIndex, intv.endIndex);
            LinkedList<V> removed = new LinkedList<V>();
            for (int i = intv.endIndex; i >= intv.startIndex; --i) {
                V element = entries.remove(i);
                onRemove(element);
                removed.add(element);
            }
            support.fireElementsRemoved(intv.startIndex, intv.endIndex - intv.startIndex + 1, (Collection<PresentationModel>)removed);
            result.addAll(removed);
        }
        return result;
    }

    public void putAll(Map<K, V> aMap) {
        HashSet<K> duplicateKeys = new HashSet<K>(aMap.keySet());
        duplicateKeys.retainAll(this.entries.keySetReference());
        // first: replace all elements with duplicate keys
        int[] indices = indicesOfKeys(duplicateKeys);
        Interval[] aintv = Interval.createIntervals(indices);
        for (Interval intv : aintv) {
            LinkedList<V> replaced = new LinkedList<V>();
            for (int index = intv.startIndex; index <= intv.endIndex; index++) {
                K key = getKey(index);
                V newValue = aMap.get(key);
                V old = this.entries.put(key, newValue);
                onRemove(old);
                replaced.add(old);
                onAdd(newValue);
            }
            support.fireElementsReplaced(intv.startIndex, intv.endIndex - intv.startIndex + 1, (Collection<PresentationModel>)replaced);
        }
        // second: add all elements with new keys
        Map<K, V> newMap = new HashMap<K, V>(aMap);
        newMap.keySet().removeAll(duplicateKeys);
        if (newMap.size() > 0) {
            int beginIndex = entries.size();
            entries.putAll(aMap);
            for (V element : aMap.values()) {
                onAdd(element);
            }
            support.fireElementsAdded(beginIndex, newMap.size());
        }

    }

    public void setAll(Map<K, V> newMap) {
        retainAllKeys(newMap.keySet());
        putAll(newMap);
    }

    protected ValidationRule createDefaultValidationRule() {
        return new DefaultValidationRule();
    }

    public class DefaultValidationRule implements ValidationRule {
        public ValidationState validate() {
            if (isEmpty()) {
                return null;
            }
            for (V element : MapPM.this) {
                if (element.isValid() == false) {
                    // TODO (mk) i18n
                    return new ValidationState("One or more elements are invalid");
                }
            }
            return null;
        }
    }
}