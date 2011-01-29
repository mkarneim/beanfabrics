/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.beanfabrics.Path;
import org.beanfabrics.PathEvaluation;
import org.beanfabrics.util.OrderPreservingMap;

/**
 * Internal helper class for sorting models by one ore more of their properties.
 *
 * @author Michael Karneim
 */
class SortingHelper {
    public <K, V extends PresentationModel> void sortBy(OrderPreservingMap<K, V> map, SortKey... sortKeys) {
        if (map.isEmpty()) {
            return;
        }
        final int len = map.size();
        List<Entry<K, V>> list = new ArrayList<Entry<K, V>>(len);
        for (int i = 0; i < len; ++i) {
            K key = map.getKey(i);
            V value = map.get(i);
            Entry<K, V> entry = new Entry<K, V>(key, value);
            list.add(entry);
        }

        List<SortKey> reverseKeys = new ArrayList<SortKey>(Arrays.asList(sortKeys));
        Collections.reverse(reverseKeys);
        for (SortKey key : reverseKeys) {
            Path path = key.getSortPath();
            boolean ascending = key.isAscending();
            Collections.sort(list, new EntryComparator(new PathComparator(new ModelComparatorImpl(ascending ? 1 : -1), path)));
        }
        Collection<K> keys = new LinkedList<K>();
        for (Entry<K, V> entry : list) {
            keys.add(entry.getKey());
        }
        map.reorder(keys);
    }

    public <T extends PresentationModel> void sortBy(List<T> list, SortKey... sortKeys) {
        if (list.isEmpty()) {
            return; // nothing to do
        }
        List<SortKey> reverseKeys = new ArrayList<SortKey>(Arrays.asList(sortKeys));
        Collections.reverse(reverseKeys);
        for (SortKey key : reverseKeys) {
            Path path = key.getSortPath();
            boolean ascending = key.isAscending();
            Collections.sort(list, new RowComparator(new PathComparator(new ModelComparatorImpl(ascending ? 1 : -1), path)));
        }
    }

    private static class ComparableComparator implements Comparator<Comparable> {
        public int compare(Comparable c1, Comparable c2) {
            final int result;
            if (c1 == null) {
                if (c2 == null) {
                    result = 0;
                } else {
                    result = -1;
                }
            } else {
                if (c2 == null) {
                    result = 1;
                } else {
                    int classnameComparison = c1.getClass().getName().compareTo(c2.getClass().getName());
                    if (classnameComparison != 0) {
                        result = classnameComparison;
                    } else {
                        result = c1.compareTo(c2);
                    }
                }
            }
            return result;
        }
    }

    private static class ModelComparatorImpl implements Comparator<PresentationModel> {
        private final int orderFactor;
        private final ComparableComparator comparableComparator = new ComparableComparator();

        public ModelComparatorImpl(int orderFactor) {
            this.orderFactor = orderFactor;
        }

        public int compare(PresentationModel pm1, PresentationModel pm2) {
            final int result;
            if (pm1 == null) {
                if (pm2 == null) {
                    return 0;
                }
                result = -1;
            } else {
                Comparable<?> c1 = pm1.getComparable();
                if (c1 == null) {
                    result = -1;
                } else if (pm2 == null) {
                    result = +1;
                } else {
                    Comparable<?> c2 = pm2.getComparable();
                    if (c2 == null) {
                        result = +1;
                    } else {
                        result = comparableComparator.compare(c1, c2);
                    }
                }
            }
            return result * orderFactor;
        }
    }

    private static class PathComparator implements Comparator<PresentationModel> {
        private final Path path;
        private final Comparator<PresentationModel> delegate;

        public PathComparator(Comparator<PresentationModel> delegate, Path path) {
            this.delegate = delegate;
            this.path = path;
        }

        public int compare(PresentationModel c1, PresentationModel c2) {
            PresentationModel pModel1 = PathEvaluation.evaluateOrNull(c1, path);
            PresentationModel pModel2 = PathEvaluation.evaluateOrNull(c2, path);

            return delegate.compare(pModel1, pModel2);
        }
    }

    private static class Entry<K, V> {
        private final K key;
        private final V value;

        public Entry(K key, V value) {
            super();
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    private static class EntryComparator implements Comparator<Entry> {
        final PathComparator delegate;

        public EntryComparator(PathComparator delegate) {
            this.delegate = delegate;
        }

        public int compare(Entry e1, Entry e2) {
            PresentationModel row1 = (PresentationModel)e1.getValue();
            PresentationModel row2 = (PresentationModel)e2.getValue();
            return delegate.compare(row1, row2);
        }
    }

    private static class RowComparator implements Comparator<PresentationModel> {
        private final PathComparator delegate;

        public RowComparator(PathComparator delegate) {
            this.delegate = delegate;
        }

        public int compare(PresentationModel row1, PresentationModel row2) {
            return delegate.compare(row1, row2);
        }
    }
}