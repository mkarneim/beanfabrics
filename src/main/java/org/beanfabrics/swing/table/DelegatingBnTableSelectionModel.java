/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.table;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.beanfabrics.event.ElementChangedEvent;
import org.beanfabrics.event.ElementsAddedEvent;
import org.beanfabrics.event.ElementsDeselectedEvent;
import org.beanfabrics.event.ElementsRemovedEvent;
import org.beanfabrics.event.ElementsReplacedEvent;
import org.beanfabrics.event.ElementsSelectedEvent;
import org.beanfabrics.event.ListListener;
import org.beanfabrics.event.WeakListListener;
import org.beanfabrics.model.IListPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.util.Interval;

/**
 * Temporary class.<p>! DO NOT USE !</p>
 *
 * The <code>DelegatingBnTableSelectionModel</code> is a {@link ListSelectionModel} that
 * decorates a {@link IListPM}.
 *
 * @author Michael Karneim
 */
class DelegatingBnTableSelectionModel implements ListSelectionModel {
    private ListSelectionModel delegate = new DefaultListSelectionModel();
    private IListPM<? extends PresentationModel> list;

    private final ListSelectionListener delegateListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
            fireValueChanged(e.getFirstIndex(), e.getLastIndex()-e.getFirstIndex()+1, e.getValueIsAdjusting());
        }
    };

    private final ListListener listener = new WeakListListener() {
        /* TODO (mk) currently we don't care about the selection mode
         * when receiving and forwarding events from the IListPM
         * to the JTable. So it't possible to have multiple selections
         * even if the mode is SINGLE_SELECTION.
         * This might annoy some people. We have to think it over.
         */
        public void elementsDeselected(ElementsDeselectedEvent evt) {
            delegate.removeSelectionInterval(evt.getBeginIndex(), evt.getBeginIndex()+evt.getLength()-1);
        }

        public void elementsSelected(ElementsSelectedEvent evt) {
            delegate.addSelectionInterval(evt.getBeginIndex(), evt.getBeginIndex()+evt.getLength()-1);
        }

        public void elementChanged(ElementChangedEvent evt) {
            // we can ignore that
        }

        public void elementsAdded(ElementsAddedEvent evt) {
            // we don't have to do anything here
            //delegate.insertIndexInterval(evt.getBeginIndex(), evt.getLength(), true);
        }

        public void elementsRemoved(ElementsRemovedEvent evt) {
            // we don't have to do anything here
            //delegate.removeIndexInterval(evt.getBeginIndex(), evt.getBeginIndex()+evt.getLength()-1);
        }

        public void elementsReplaced(ElementsReplacedEvent evt) {
            // we can ignore that
        }
    };

    private final CopyOnWriteArrayList<ListSelectionListener> eventListeners = new CopyOnWriteArrayList<ListSelectionListener>();

    public DelegatingBnTableSelectionModel(IListPM<? extends PresentationModel> pModel) {
        if (pModel == null) {
            throw new IllegalArgumentException("pModel must not be null");
        }
        this.list = pModel;

        int[] indexes = this.list.getSelection().getIndexes();
        Interval[] intervals = Interval.createIntervals(indexes);
        for( Interval interval: intervals) {
            delegate.addSelectionInterval(interval.startIndex, interval.endIndex);
        }

        this.list.addListListener(this.listener);
        this.delegate.addListSelectionListener(this.delegateListener);
    }

    /**
     * Disconnect <code>this</code> object from the underlying IListPM.
     */
    public void dismiss() {
        this.list.removeListListener(this.listener);
    }

    /** {@inheritDoc} */
    public void addListSelectionListener(ListSelectionListener l) {
        if (l == null) {
            throw new IllegalArgumentException("l must not be null.");
        }
        eventListeners.add(l);
    }

    /** {@inheritDoc} */
    public void removeListSelectionListener(ListSelectionListener l) {
        if (l == null) {
            throw new IllegalArgumentException("l must not be null.");
        }
        eventListeners.remove(l);
    }

    protected void fireValueChangedBetween(int index0, int index1, boolean valueIsAdjusting) {
        int beginIndex = Math.min(index0, index1);
        int endIndex = Math.max(index0, index1);
        int len = endIndex - beginIndex;
        fireValueChanged(beginIndex, len, valueIsAdjusting);
    }

    protected void fireValueChanged(int beginIndex, int length, boolean valueIsAdjusting) {
        if (eventListeners.isEmpty()) {
            return;
        }
        ListSelectionEvent evt = new ListSelectionEvent(this, beginIndex, beginIndex + length - 1, valueIsAdjusting);
        for (ListSelectionListener l : eventListeners) {
            l.valueChanged(evt);
        }
    }

    public void addSelectionInterval(int index0, int index1) {
        System.out.println("BnTableSelectionModel.addSelectionInterval()");
        delegate.addSelectionInterval(index0, index1);
        int beginIndex = Math.min(index0, index1);
        int endIndex = Math.max(index0, index1);
        list.getSelection().addInterval(beginIndex, endIndex);
    }

    public void clearSelection() {
        delegate.clearSelection();
        list.getSelection().clear();
    }

    public int getAnchorSelectionIndex() {
        return delegate.getAnchorSelectionIndex();
    }

    public int getLeadSelectionIndex() {
        return delegate.getLeadSelectionIndex();
    }

    public int getMaxSelectionIndex() {
        return delegate.getMaxSelectionIndex();
    }

    public int getMinSelectionIndex() {
        return delegate.getMinSelectionIndex();
    }

    public int getSelectionMode() {
        return delegate.getSelectionMode();
    }

    public boolean getValueIsAdjusting() {
        return delegate.getValueIsAdjusting();
    }

    public void insertIndexInterval(int index, int length, boolean before) {
        System.out.println("BnTableSelectionModel.insertIndexInterval()");
        delegate.insertIndexInterval(index, length, before);
        // we can ignore thin in beanfabrics savely
    }

    public boolean isSelectedIndex(int index) {
        return delegate.isSelectedIndex(index);
    }

    public boolean isSelectionEmpty() {
        return delegate.isSelectionEmpty();
    }

    public void removeIndexInterval(int index0, int index1) {
        delegate.removeIndexInterval(index0, index1);
        // we can ignore thin in beanfabrics savely
    }

    public void removeSelectionInterval(int index0, int index1) {
        delegate.removeSelectionInterval(index0, index1);
        int beginIndex = Math.min(index0, index1);
        int endIndex = Math.max(index0, index1);
        list.getSelection().removeInterval(beginIndex, endIndex);
    }

    public void setAnchorSelectionIndex(int index) {
        delegate.setAnchorSelectionIndex(index);
    }

    public void setLeadSelectionIndex(int index) {
        delegate.setLeadSelectionIndex(index);
    }

    public void setSelectionInterval(int index0, int index1) {
        System.out.println("BnTableSelectionModel.setSelectionInterval()");
        delegate.setSelectionInterval(index0, index1);
        int beginIndex = Math.min(index0, index1);
        int endIndex = Math.max(index0, index1);
        list.getSelection().setInterval(beginIndex, endIndex);
    }

    public void setSelectionMode(int selectionMode) {
        delegate.setSelectionMode(selectionMode);
    }

    public void setValueIsAdjusting(boolean valueIsAdjusting) {
        delegate.setValueIsAdjusting(valueIsAdjusting);
    }
}
