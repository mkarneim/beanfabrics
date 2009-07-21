/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.beanfabrics.event.OptionsEvent;
import org.beanfabrics.event.OptionsListener;

/**
 * Note:
 * <ul>
 * <li>keys must be unique</li>
 * <li>keys can be null</li>
 * <li>values must be unique</li>
 * <li>values must not be null</li>
 * </ul>
 * 
 * @param <K> the key type used in this options.
 * @author Michael Karneim
 */
//TODO (rk) Options should have an index
public class Options<K> {
    private final Map<K, String> map = new LinkedHashMap<K, String>();
    private final List<String> values = new ArrayList<String>();

    private final CopyOnWriteArrayList<OptionsListener> listeners = new CopyOnWriteArrayList<OptionsListener>();

    public Map<K, String> toMap() {
        return new LinkedHashMap<K, String>(map);
    }

    public void clear() {
        boolean isEmpty = this.isEmpty();
        if (!isEmpty) {
            map.clear();
            values.clear();
            this.fireChanged();
        }
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public String get(Object key) {
        return map.get(key);
    }

    public K getKey(String value) {
        if (value == null) {
            throw new IllegalArgumentException("value==null");
        }
        for (K key : map.keySet()) {
            String val = map.get(key);
            if (value.equals(val)) {
                return key;
            }
        }
        throw new NoSuchElementException("value='" + value + "' not found");
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Set<K> keySet() {
        return new LinkedHashSet<K>(map.keySet());
    }

    public String put(K key, String value) {
        if (value == null) {
            throw new IllegalArgumentException("value==null");
        }
        boolean noChange = equals(map.get(key), value);
        if (noChange) {
            return value;
        }
        if (values.contains(value)) {
            throw new IllegalArgumentException("duplicate value='" + value + "' ");
        }
        String result = map.put(key, value);
        values.add(value);
        if (result != null) {
            values.remove(result);
        }
        this.fireChanged();
        return result;
    }

    public void putAll(Options other) {
        this.putAll(other.map);
    }

    public void putAll(Map<K, String> aMap) {
        LinkedHashMap<K, String> tmp = new LinkedHashMap<K, String>(this.map);
        for (Map.Entry<K, String> entry : aMap.entrySet()) {
            if (entry.getValue() == null) {
                throw new IllegalArgumentException("value for entry with key='" + entry.getKey() + "' is null");
            }
            tmp.put(entry.getKey(), entry.getValue());
        }
        Collection<String> tmpValues = tmp.values();
        Set<String> tmpValuesSet = new LinkedHashSet<String>(tmpValues);
        if (tmpValues.size() != tmpValuesSet.size()) {
            // some value(s) are duplicates
            throw new IllegalArgumentException("some values are duplicates");
        }
        this.map.clear();
        this.map.putAll(tmp);
        this.values.clear();
        this.values.addAll(tmpValuesSet);
        this.fireChanged();
    }

    public String remove(Object key) {
        String result = map.remove(key);
        if (result != null) {
            values.remove(result);
            this.fireChanged();
        }
        return result;
    }

    public Set<String> removeAll(Set<K> keys) {
        LinkedHashSet<String> result = new LinkedHashSet<String>();
        for (K key : keys) {
            String removed = map.remove(key);
            if (removed != null) {
                result.add(removed);
            }
        }
        if (!values.isEmpty()) {
            values.removeAll(result);
            this.fireChanged();
        }
        return result;
    }

    public int size() {
        return map.size();
    }

    public Set<String> values() {
        return new LinkedHashSet<String>(this.values);
    }

    public String[] getValues() {
        return (String[])values.toArray(new String[values.size()]);
    }

    public int indexOf(String value) {
        return this.values.indexOf(value);
    }

    public String getValue(int index) {
        return this.values.get(index);
    }

    public void addOptionsListener(OptionsListener l) {
        if (l == null) {
            throw new IllegalArgumentException("l must not be null.");
        }
        listeners.add(l);
    }

    public void removeOptionsListener(OptionsListener l) {
        if (l == null) {
            throw new IllegalArgumentException("l must not be null.");
        }
        listeners.remove(l);
    }

    protected void fireChanged() {
        OptionsEvent evt = new OptionsEvent(this);
        for (OptionsListener l : listeners) {
            l.changed(evt);
        }
    }

    protected static boolean equals(Object a, Object b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }

    public static <T> Options<T> create(T... values) {
        Options<T> result = new Options<T>();
        for (T value : values) {
            result.put(value, String.valueOf(value));
        }
        return result;
    }
}