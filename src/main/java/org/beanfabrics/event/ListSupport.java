/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.event;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;

import org.beanfabrics.model.IListPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.util.Interval;

/**
 * @author Michael Karneim
 */
public class ListSupport {
    private final IListPM source;
    private List<ListListener> listeners = new LinkedList<ListListener>();
    transient WeakHashMap<ListListener, WeakWrapper> weakWrapperMap = new WeakHashMap<ListListener, WeakWrapper>();

    public ListSupport(IListPM source) {
        this.source = source;
    }

    public synchronized void addListListener(ListListener l) {
        if (l == null) {
            throw new IllegalArgumentException("l==null");
        }
        if (l instanceof WeakListener) {
            l = this.getOrCreateWeakWrapper(l);
        }

        LinkedList<ListListener> newList = new LinkedList<ListListener>(this.listeners);
        removeUnusedWeakWrappers(newList.iterator());
        newList.add(l);
        this.listeners = newList;
    }

    public synchronized void removeListListener(ListListener l) {
        LinkedList<ListListener> newList = new LinkedList<ListListener>(listeners);
        if (l instanceof WeakListener) {
            l = this.getOrCreateWeakWrapper(l);
            this.removeWeakWrapper((WeakWrapper)l);
        }
        newList.remove(l);
        this.listeners = newList;
    }

    public void fireElementsAdded(ElementsAddedEvent evt) {
        for (ListListener l : this.listeners) {
            l.elementsAdded(evt);
        }
    }

    public void fireElementsAdded(int beginIndex, int length) {
        this.fireElementsAdded(new ElementsAddedEvent(this.source, beginIndex, length));
    }

    public void fireElementsRemoved(ElementsRemovedEvent evt) {
        for (ListListener l : this.listeners) {
            l.elementsRemoved(evt);
        }
    }

    public void fireElementsRemoved(int beginIndex, int length, Collection<PresentationModel> removed) {
        this.fireElementsRemoved(new ElementsRemovedEvent(this.source, beginIndex, length, removed));
    }

    public void fireElementsRemoved(int index, PresentationModel removed) {
        ArrayList<PresentationModel> list = new ArrayList<PresentationModel>(1);
        list.add(removed);
        this.fireElementsRemoved(new ElementsRemovedEvent(this.source, index, 1, list));
    }

    public void fireElementsReplaced(ElementsReplacedEvent evt) {
        for (ListListener l : this.listeners) {
            l.elementsReplaced(evt);
        }
    }

    public void fireElementsReplaced(int beginIndex, int length, Collection<PresentationModel> old) {
        this.fireElementsReplaced(new ElementsReplacedEvent(this.source, beginIndex, length, old));
    }

    public void fireElementsReplaced(int index, PresentationModel old) {
        ArrayList<PresentationModel> oldList = new ArrayList<PresentationModel>(1);
        oldList.add(old);
        this.fireElementsReplaced(new ElementsReplacedEvent(this.source, index, 1, oldList));
    }

    public void fireElementChanged(ElementChangedEvent evt) {
        for (ListListener l : this.listeners) {
            l.elementChanged(evt);
        }
    }

    public void fireElementChanged(int index) {
        this.fireElementChanged(new ElementChangedEvent(this.source, index));
    }

    public void fireElementChanged(int index, EventObject cause) {
        this.fireElementChanged(new ElementChangedEvent(this.source, index, cause));
    }

    public void fireElementsSelected(ElementsSelectedEvent evt) {
        for (ListListener l : this.listeners) {
            l.elementsSelected(evt);
        }
    }

    public void fireElementsSelected(int beginIndex, int length) {
        this.fireElementsSelected(new ElementsSelectedEvent(this.source, beginIndex, length));
    }

    public void fireElementsSelected(int[] indices) {
        Interval[] intervals = Interval.createIntervals(indices);
        for (Interval i : intervals) {
            fireElementsSelected(i.startIndex, 1 + i.endIndex - i.startIndex);
        }
    }

    public void fireElementsSelected(Collection<Integer> indices) {
        Interval[] intervals = Interval.createIntervals(indices);
        for (Interval i : intervals) {
            fireElementsSelected(i.startIndex, 1 + i.endIndex - i.startIndex);
        }
    }

    public void fireElementsDeselected(ElementsDeselectedEvent evt) {
        for (ListListener l : this.listeners) {
            l.elementsDeselected(evt);
        }
    }

    public void fireElementsDeselected(int beginIndex, int length) {
        this.fireElementsDeselected(new ElementsDeselectedEvent(this.source, beginIndex, length));
    }

    public void fireElementsDeselected(int[] indices) {
        Interval[] intervals = Interval.createIntervals(indices);
        for (Interval i : intervals) {
            fireElementsDeselected(i.startIndex, 1 + i.endIndex - i.startIndex);
        }
    }

    public void fireElementsDeselected(Collection<Integer> indices) {
        Interval[] intervals = Interval.createIntervals(indices);
        for (Interval i : intervals) {
            fireElementsDeselected(i.startIndex, 1 + i.endIndex - i.startIndex);
        }
    }

    private WeakWrapper getOrCreateWeakWrapper(ListListener l) {
        WeakWrapper result = this.weakWrapperMap.get(l);
        if (result == null) {
            result = new WeakWrapper(l);
            this.weakWrapperMap.put(l, result);
        }
        return result;
    }

    private void removeWeakWrapper(WeakWrapper wrapper) {
        this.weakWrapperMap.remove(wrapper);
    }

    private static void removeUnusedWeakWrappers(Iterator<ListListener> it) {
        while (it.hasNext()) {
            ListListener l = it.next();
            if (l instanceof WeakWrapper && !((WeakWrapper)l).hasTarget()) {
                it.remove();
            }
        }
    }

    ////

    private static class WeakWrapper implements ListListener {
        private WeakReference<ListListener> ref;

        public WeakWrapper(ListListener l) {
            this.setTarget(l);
        }

        public boolean hasTarget() {
            return this.ref == null || this.ref.get() != null;
        }

        protected ListListener getTarget() {
            if (this.ref == null) {
                return null;
            }
            return this.ref.get();
        }

        protected void setTarget(ListListener l) {
            if (l == null) {
                this.ref = null;
            } else {
                this.ref = new WeakReference<ListListener>(l);
            }
        }

        public void elementChanged(ElementChangedEvent evt) {
            if (hasTarget()) {
                this.getTarget().elementChanged(evt);
            }
        }

        public void elementsAdded(ElementsAddedEvent evt) {
            if (hasTarget()) {
                this.getTarget().elementsAdded(evt);
            }
        }

        public void elementsDeselected(ElementsDeselectedEvent evt) {
            if (hasTarget()) {
                this.getTarget().elementsDeselected(evt);
            }
        }

        public void elementsRemoved(ElementsRemovedEvent evt) {
            if (hasTarget()) {
                this.getTarget().elementsRemoved(evt);
            }
        }

        public void elementsReplaced(ElementsReplacedEvent evt) {
            if (hasTarget()) {
                this.getTarget().elementsReplaced(evt);
            }
        }

        public void elementsSelected(ElementsSelectedEvent evt) {
            if (hasTarget()) {
                this.getTarget().elementsSelected(evt);
            }
        }
    }

}