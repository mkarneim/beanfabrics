/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.util;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * The <code>OrderPreservingMap</code> is a {@link Map} that preserves the oder
 * in which the elements where added. It gives access to it's values by a hash
 * key but also by an index.
 * 
 * @author Michael Karneim
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class OrderPreservingMap<K, V> implements Map<K, V>, Cloneable {
    private HashMap<K, V> map = new HashMap<K, V>();
    private ArrayList<K> orderKeys = new ArrayList<K>();
    private ArrayList<V> orderValues = new ArrayList<V>();

    public OrderPreservingMap() {
    }

    @SuppressWarnings("unchecked")
    public OrderPreservingMap(OrderPreservingMap<K, V> other) {
        this.map = (HashMap<K, V>)other.map.clone();
        this.orderKeys = (ArrayList<K>)other.orderKeys.clone();
        this.orderValues = (ArrayList<V>)other.orderValues.clone();
    }

    public OrderPreservingMap(Map<K, V> map, Collection<K> orderedKeys, Collection<V> orderedEntries) {
        this.map = new HashMap<K, V>(map);
        this.orderKeys = new ArrayList<K>(orderedKeys);
        this.orderValues = new ArrayList<V>(orderedEntries);
    }

    @SuppressWarnings("unchecked")
    public Object clone() {
        try {
            final OrderPreservingMap<K, V> other = (OrderPreservingMap<K, V>)super.clone();
            other.map = (HashMap<K, V>)this.map.clone();
            other.orderKeys = (ArrayList<K>)this.orderKeys.clone();
            other.orderValues = (ArrayList<V>)this.orderValues.clone();
            return other;
        } catch (CloneNotSupportedException ex) {
            // this never should happen since we implemnet Cloneable
            throw new UndeclaredThrowableException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public void putAll(Map<? extends K, ? extends V> other) {
        if (other == null || other.size() == 0) {
            return;
        }
        if (other instanceof OrderPreservingMap) {
            // fill in the entries in the given order (if not contained here and therefore being replaced).
            // Attention: not only copies the ordering but the index, too
            final OrderPreservingMap<? extends K, ? extends V> otherHashMap = (OrderPreservingMap<? extends K, ? extends V>)other;
            final Object[] orderedKeys = otherHashMap.keyArray();
            for (int i = 0; i < orderedKeys.length; i++) {
                this.put((K)orderedKeys[i], (V)other.get(orderedKeys[i]));
            }
        } else {
            for (Entry<? extends K, ? extends V> entry : other.entrySet()) {
                this.put(entry.getKey(), entry.getValue());
            }
        }
    }
    
    public void putAll(Collection<Entry<K,V>> entries) {
        if ( entries == null ) {
            return;
        }
        for (Entry<K,V> entry : entries) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    public V put(K key, V value) {
        if (value == null) {
            throw new IllegalArgumentException("value==null");
        }
        if (key == null) {
            throw new IllegalArgumentException("key==null");
        }
        final V old = this.map.put(key, value);
        if (old == value) {
            return old;
        }
        if (old != null) {
            final int index = this.orderKeys.indexOf(key);
            this.orderValues.remove(index);
            this.orderValues.add(index, value);
            return old;
        }
        this.orderKeys.add(key);
        this.orderValues.add(value);
        return null;
    }

    public V put(K key, V value, int toIndex) {
        if (value == null) {
            throw new IllegalArgumentException("value==null");
        }
        if (key == null) {
            throw new IllegalArgumentException("key==null");
        }

        final V old = this.map.put(key, value);
        if (old == null) {
            this.orderKeys.add(toIndex, key);
            this.orderValues.add(toIndex, value);
            return null;
        } else {
            int oldIndex = this.orderKeys.indexOf(key);
            if (oldIndex == toIndex) {
                return old;
            } else {
                this.orderKeys.add(toIndex, key);
                this.orderKeys.remove(oldIndex);
                this.orderValues.add(toIndex, value);
                this.orderValues.remove(oldIndex);
                return old;
            }
        }
    }

    public Set<K> keySet() {
        return new HashSet<K>(this.map.keySet());
    }

    public Set<K> keySetReference() {
        return this.map.keySet();
    }

    public Object[] keyArray() {
        return this.orderKeys.toArray();
    }

    public Collection<K> orderedKeys() {
        return new ArrayList<K>(this.orderKeys);
    }

    public Collection<K> orderedKeysReference() {
        return this.orderKeys;
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return new HashSet<Map.Entry<K, V>>(this.map.entrySet());
    }

    public Set<Map.Entry<K, V>> entrySetReference() {
        return this.map.entrySet();
    }

    public Collection<V> values() {
        return new ArrayList<V>(this.orderValues);
    }

    public Collection<V> valuesReference() {
        return this.orderValues;
    }

    public V get(Object key) {
        return this.map.get(key);
    }

    public V get(int i) {
        return this.orderValues.get(i);
    }

    public K getKey(int index) {
        return this.orderKeys.get(index);
    }

    public Collection<V> getAll(Set<K> keys) {
        final LinkedList<V> result = new LinkedList<V>();
        for (K key : keys) {
            final V value = this.map.get(key);
            if (value == null) {
                continue;
            }
            result.add(value);
        }
        return result;
    }

    public int indexOfKey(Object key) {
        return this.orderKeys.indexOf(key);
    }

    // public int moveTo(K key, int newIndex) {
    // final int oldIndex = this.orderKeys.indexOf(key);
    // if (oldIndex == -1) {
    // return -1;
    // }
    // this.orderKeys.remove(oldIndex);
    // this.orderKeys.add(newIndex, key);
    // final V entry = this.orderEntries.remove(oldIndex);
    // this.orderEntries.add(newIndex, entry);
    // return oldIndex;
    // }

    // public void replace(int index, K key, V value) {
    // if (index < 0 || index >= this.size()) {
    // throw new IllegalArgumentException(
    // "Index out of bounds. Index must be between 0 and "
    // + this.size() + ". But index was " + index);
    // }
    // final V oldAtIndex = this.get(index);
    // final V oldAtKey = this.get(key);
    // if (oldAtKey != null) {
    // throw new IllegalArgumentException(
    // "Can't replace value, because key is already used. The key was "
    // + key + ".");
    // }
    // this.remove(index);
    // this.put(key, value);
    // this.moveTo(key, index);
    // }

    public int indexOf(Object object) {
        return this.orderValues.indexOf(object);
    }

    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    public boolean containsAll(Collection<V> list) {
        return this.orderValues.containsAll(list);
    }

    public void reverseOrder() {
        final int len = this.orderKeys.size();
        final ArrayList<K> newOrderKeys = new ArrayList<K>(len);
        final ArrayList<V> newOrderEntries = new ArrayList<V>(len);
        final ListIterator<K> itKeys = this.orderKeys.listIterator(len);
        final ListIterator<V> itEntries = this.orderValues.listIterator(len);
        for (int i = 0; i < len; i++) {
            final K key = itKeys.previous();
            final V entry = itEntries.previous();
            newOrderKeys.add(key);
            newOrderEntries.add(entry);
        }
        this.orderValues = newOrderEntries;
        this.orderKeys = newOrderKeys;
    }

    public void reorder(Collection<K> keys) {
        final int len = keys.size();
        if (len != this.size()) {
            throw new IllegalArgumentException("Can't reorder listCell with provided keys. The number of the provided keys must match this listCell's size.");
        }
        final ArrayList<K> newOrderKeys = new ArrayList<K>(len);
        final ArrayList<V> newOrderEntries = new ArrayList<V>(len);
        for (K key : keys) {
            final V entry = this.get(key);
            if (entry == null) {
                throw new IllegalArgumentException("Can't reorder listCell with provided keys. Key '" + key + "' is not in this listCell.");
            }
            newOrderKeys.add(key);
            newOrderEntries.add(entry);
        }
        this.orderValues = newOrderEntries;
        this.orderKeys = newOrderKeys;
    }

    public void reorder(K[] keys) {
        final int len = keys.length;
        if (len != this.size()) {
            throw new IllegalArgumentException("Can't reorder listCell with provided keys. The number of the provided keys must match this listCell's size.");
        }
        final ArrayList<K> newOrderKeys = new ArrayList<K>(len);
        final ArrayList<V> newOrderEntries = new ArrayList<V>(len);
        for (int i = 0; i < len; i++) {
            final K key = keys[i];
            final V entry = this.get(key);
            if (entry == null) {
                throw new IllegalArgumentException("Can't reorder listCell with provided keys. Key '" + key + "' is not in this listCell.");
            }
            newOrderKeys.add(key);
            newOrderEntries.add(entry);
        }
        this.orderValues = newOrderEntries;
        this.orderKeys = newOrderKeys;
    }

    public V remove(Object key) {
        final V result = this.map.remove(key);
        if (result == null) {
            return null;
        }
        final int index = this.orderKeys.indexOf(key);
        this.orderKeys.remove(index);
        this.orderValues.remove(index);
        return result;
    }

    public V remove(int index) {
        final V result = this.orderValues.remove(index);
        if (result == null) {
            return null;
        }
        final K key = this.orderKeys.remove(index);
        this.map.remove(key);
        return result;
    }

    public boolean removeAllKeys(Set<K> keys) {
        boolean result = false;
        for (K key : keys) {
            final V value = this.map.remove(key);
            if (value == null) {
                continue;
            }
            result = true;
            int index = this.orderKeys.indexOf(key);
            this.orderKeys.remove(index);
            this.orderValues.remove(index);
        }
        return result;
    }

    public void retainAllKeys(Set<K> keys) {
        final HashSet<K> keepKeys = new HashSet<K>(keys.size());
        keepKeys.addAll(this.map.keySet());
        keepKeys.retainAll(keys);
        final HashMap<K, V> newMap = new HashMap<K, V>(keepKeys.size());
        for (K key : keepKeys) {
            final V value = this.map.remove(key);
            newMap.put(key, value);
        }
        final HashMap<K, V> toRemoveMap = this.map;
        this.map = newMap;
        for (K key : toRemoveMap.keySet()) {
            final int index = this.orderKeys.indexOf(key);
            this.orderKeys.remove(index);
            this.orderValues.remove(index);
        }
    }

    public void clear() {
        this.orderKeys.clear();
        this.orderValues.clear();
        this.map.clear();
    }

    public int size() {
        return this.orderKeys.size();
    }

    public boolean isEmpty() {
        return this.orderKeys.isEmpty();
    }

    public V[] toArray(V[] dest) {
        return this.orderValues.toArray(dest);
    }

    public Object[] toArray() {
        return this.orderValues.toArray();
    }

    @SuppressWarnings("unchecked")
    public Collection<V> toCollection() {
        return (ArrayList<V>)this.orderValues.clone();
    }

    public ListIterator<V> listIterator(int index) {
        return this.orderValues.listIterator(index);
    }

    public ListIterator<K> keyListIterator(int index) {
        return this.orderKeys.listIterator(index);
    }
}