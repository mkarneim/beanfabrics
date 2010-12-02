/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
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
 * The {@link ListSupport} is a utility class for handling {@link ListListener}
 * objects.
 * 
 * @author Michael Karneim
 */
public class ListSupport {
    private final IListPM<?> source;
    private List<ListListener> listeners = new LinkedList<ListListener>();
    private transient WeakHashMap<ListListener, WeakWrapper> weakWrapperMap = new WeakHashMap<ListListener, WeakWrapper>();

    /**
     * Constructs a {@link ListSupport} for the given source.
     * 
     * @param source
     */
    public ListSupport(IListPM<?> source) {
        this.source = source;
    }

    /**
     * Adds the given listener.
     * 
     * @param listener
     */
    public synchronized void addListListener(ListListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("l==null");
        }
        if (listener instanceof WeakListener) {
            listener = this.getOrCreateWeakWrapper(listener);
        }

        LinkedList<ListListener> newList = new LinkedList<ListListener>(this.listeners);
        removeUnusedWeakWrappers(newList.iterator());
        newList.add(listener);
        this.listeners = newList;
    }

    /**
     * Removes the given listener.
     * 
     * @param listener
     */
    public synchronized void removeListListener(ListListener listener) {
        LinkedList<ListListener> newList = new LinkedList<ListListener>(listeners);
        if (listener instanceof WeakListener) {
            listener = this.getOrCreateWeakWrapper(listener);
            this.removeWeakWrapper((WeakWrapper)listener);
        }
        newList.remove(listener);
        this.listeners = newList;
    }

    /**
     * Fires the given {@link ElementsAddedEvent} to each registered listener.
     * 
     * @param evt
     */
    public void fireElementsAdded(ElementsAddedEvent evt) {
        for (ListListener l : this.listeners) {
            l.elementsAdded(evt);
        }
    }

    /**
     * Fires a {@link ElementsAddedEvent} with the given attributes to each
     * registered listener.
     * 
     * @param beginIndex
     * @param length
     */
    public void fireElementsAdded(int beginIndex, int length) {
        this.fireElementsAdded(new ElementsAddedEvent(this.source, beginIndex, length));
    }

    /**
     * Fires the given {@link ElementsRemovedEvent} to each registered listener.
     * 
     * @param evt
     */
    public void fireElementsRemoved(ElementsRemovedEvent evt) {
        for (ListListener l : this.listeners) {
            l.elementsRemoved(evt);
        }
    }

    /**
     * Fires a {@link ElementsRemovedEvent} with the given attributes to each
     * registered listener.
     * 
     * @param beginIndex
     * @param length
     * @param removed
     */
    public void fireElementsRemoved(int beginIndex, int length, Collection<PresentationModel> removed) {
        this.fireElementsRemoved(new ElementsRemovedEvent(this.source, beginIndex, length, removed));
    }

    /**
     * Fires a {@link ElementsRemovedEvent} with the given attributes to each
     * registered listener.
     * 
     * @param index
     * @param removed
     */
    public void fireElementsRemoved(int index, PresentationModel removed) {
        ArrayList<PresentationModel> list = new ArrayList<PresentationModel>(1);
        list.add(removed);
        this.fireElementsRemoved(new ElementsRemovedEvent(this.source, index, 1, list));
    }

    /**
     * Fires the given {@link ElementsReplacedEvent} to each registered
     * listener.
     * 
     * @param evt
     */
    public void fireElementsReplaced(ElementsReplacedEvent evt) {
        for (ListListener l : this.listeners) {
            l.elementsReplaced(evt);
        }
    }

    /**
     * Fires a {@link ElementsReplacedEvent} with the given attributes to each
     * registered listener.
     * 
     * @param beginIndex
     * @param length
     * @param old
     */
    public void fireElementsReplaced(int beginIndex, int length, Collection<PresentationModel> old) {
        this.fireElementsReplaced(new ElementsReplacedEvent(this.source, beginIndex, length, old));
    }

    /**
     * Fires a {@link ElementsReplacedEvent} with the given attributes to each
     * registered listener.
     * 
     * @param index
     * @param old
     */
    public void fireElementsReplaced(int index, PresentationModel old) {
        ArrayList<PresentationModel> oldList = new ArrayList<PresentationModel>(1);
        oldList.add(old);
        this.fireElementsReplaced(new ElementsReplacedEvent(this.source, index, 1, oldList));
    }

    /**
     * Fires the given {@link ElementChangedEvent} to each registered listener.
     * 
     * @param evt
     */
    public void fireElementChanged(ElementChangedEvent evt) {
        for (ListListener l : this.listeners) {
            l.elementChanged(evt);
        }
    }

    /**
     * Fires a {@link ElementChangedEvent} with the given index to each
     * registered listener.
     * 
     * @param index
     */
    public void fireElementChanged(int index) {
        this.fireElementChanged(new ElementChangedEvent(this.source, index));
    }

    /**
     * Fires a {@link ElementChangedEvent} with the given attributes to each
     * registered listener.
     * 
     * @param index
     * @param cause
     */
    public void fireElementChanged(int index, EventObject cause) {
        this.fireElementChanged(new ElementChangedEvent(this.source, index, cause));
    }

    /**
     * Fires the given {@link ElementsSelectedEvent} to each registered
     * listener.
     * 
     * @param evt
     */
    public void fireElementsSelected(ElementsSelectedEvent evt) {
        for (ListListener l : this.listeners) {
            l.elementsSelected(evt);
        }
    }

    /**
     * Fires a {@link ElementsSelectedEvent} with the given attributes to each
     * registered listener.
     * 
     * @param beginIndex
     * @param length
     */
    public void fireElementsSelected(int beginIndex, int length) {
        this.fireElementsSelected(new ElementsSelectedEvent(this.source, beginIndex, length));
    }

    /**
     * Fires a {@link ElementsSelectedEvent} with the given indices to each
     * registered listener.
     * 
     * @param indices
     */
    public void fireElementsSelected(int[] indices) {
        Interval[] intervals = Interval.createIntervals(indices);
        for (Interval i : intervals) {
            fireElementsSelected(i.startIndex, 1 + i.endIndex - i.startIndex);
        }
    }

    /**
     * Fires a {@link ElementsSelectedEvent} with the given indices to each
     * registered listener.
     * 
     * @param indices
     */
    public void fireElementsSelected(Collection<Integer> indices) {
        Interval[] intervals = Interval.createIntervals(indices);
        for (Interval i : intervals) {
            fireElementsSelected(i.startIndex, 1 + i.endIndex - i.startIndex);
        }
    }

    /**
     * Fires the given {@link ElementsDeselectedEvent} to each registered
     * listener.
     * 
     * @param evt
     */
    public void fireElementsDeselected(ElementsDeselectedEvent evt) {
        for (ListListener l : this.listeners) {
            l.elementsDeselected(evt);
        }
    }

    /**
     * Fires a {@link ElementsDeselectedEvent} with the given attributes to each
     * registered listener.
     * 
     * @param beginIndex
     * @param length
     */
    public void fireElementsDeselected(int beginIndex, int length) {
        this.fireElementsDeselected(new ElementsDeselectedEvent(this.source, beginIndex, length));
    }

    /**
     * Fires a {@link ElementsDeselectedEvent} with the given indices to each
     * registered listener.
     * 
     * @param indices
     */
    public void fireElementsDeselected(int[] indices) {
        Interval[] intervals = Interval.createIntervals(indices);
        for (Interval i : intervals) {
            fireElementsDeselected(i.startIndex, 1 + i.endIndex - i.startIndex);
        }
    }

    /**
     * Fires a {@link ElementsDeselectedEvent} with the given indices to each
     * registered listener.
     * 
     * @param indices
     */
    public void fireElementsDeselected(Collection<Integer> indices) {
        Interval[] intervals = Interval.createIntervals(indices);
        for (Interval i : intervals) {
            fireElementsDeselected(i.startIndex, 1 + i.endIndex - i.startIndex);
        }
    }

    /**
     * Returns the {@link WeakWrapper} for the given listener.
     * 
     * @param listener
     * @return the {@link WeakWrapper} for the given listener
     */
    private WeakWrapper getOrCreateWeakWrapper(ListListener listener) {
        WeakWrapper result = this.weakWrapperMap.get(listener);
        if (result == null) {
            result = new WeakWrapper(listener);
            this.weakWrapperMap.put(listener, result);
        }
        return result;
    }

    /**
     * Removes the given {@link WeakWrapper} from the weakWrapper cache.
     * 
     * @param wrapper
     */
    private void removeWeakWrapper(WeakWrapper wrapper) {
        this.weakWrapperMap.remove(wrapper);
    }

    /**
     * Removes all unused WeakWrapper instances from the given iterator.
     * 
     * @param it
     */
    private static void removeUnusedWeakWrappers(Iterator<ListListener> it) {
        while (it.hasNext()) {
            ListListener l = it.next();
            if (l instanceof WeakWrapper && ((WeakWrapper)l).isCleared()) {
                it.remove();
            }
        }
    }

    /**
     * The {@link WeakWrapper} is a delegator that forwards {@link ListEvent}s
     * to a weakly referenced delegate object as long as that reference has not
     * been cleared.
     */
    private static class WeakWrapper implements ListListener {
        private WeakReference<ListListener> ref;

        /**
         * Constructs a {@link WeakWrapper} for the given delegate.
         * 
         * @param aDelegate
         */
        WeakWrapper(ListListener aDelegate) {
            this.setDelegate(aDelegate);
        }

        /**
         * Returns <code>true</code>, if the delegate reference has been
         * cleared.
         * 
         * @return <code>true</code>, if the delegate reference has been cleared
         */
        boolean isCleared() {
            return this.ref == null || this.ref.get() == null;
        }

        /**
         * Returns the delegate.
         * 
         * @return the delegate
         */
        ListListener getDelegate() {
            if (this.ref == null) {
                return null;
            }
            return this.ref.get();
        }

        /**
         * Sets the delegate.
         * 
         * @param aDelegate
         */
        void setDelegate(ListListener aDelegate) {
            if (aDelegate == null) {
                this.ref = null;
            } else {
                this.ref = new WeakReference<ListListener>(aDelegate);
            }
        }

        /** {@inheritDoc} */
        public void elementChanged(ElementChangedEvent evt) {
            if (!isCleared()) {
                this.getDelegate().elementChanged(evt);
            }
        }

        /** {@inheritDoc} */
        public void elementsAdded(ElementsAddedEvent evt) {
            if (!isCleared()) {
                this.getDelegate().elementsAdded(evt);
            }
        }

        /** {@inheritDoc} */
        public void elementsDeselected(ElementsDeselectedEvent evt) {
            if (!isCleared()) {
                this.getDelegate().elementsDeselected(evt);
            }
        }

        /** {@inheritDoc} */
        public void elementsRemoved(ElementsRemovedEvent evt) {
            if (!isCleared()) {
                this.getDelegate().elementsRemoved(evt);
            }
        }

        /** {@inheritDoc} */
        public void elementsReplaced(ElementsReplacedEvent evt) {
            if (!isCleared()) {
                this.getDelegate().elementsReplaced(evt);
            }
        }

        /** {@inheritDoc} */
        public void elementsSelected(ElementsSelectedEvent evt) {
            if (!isCleared()) {
                this.getDelegate().elementsSelected(evt);
            }
        }
    }

}