/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.list;

import java.util.concurrent.CopyOnWriteArrayList;

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

/**
 * The <code>BnListSelectionModel</code> is a {@link ListSelectionModel} that
 * decorates a {@link IListPM}.
 * 
 * @author Michael Karneim
 */
public class BnListSelectionModel implements ListSelectionModel {
    private IListPM<? extends PresentationModel> list;

    private ListListener listener = new WeakListListener() {
        /*
         * TODO (mk) currently we don't care about the selection mode when
         * receiving and forwarding events from the IListPM to the JList. So
         * it't possible to have multiple selections even if the mode is
         * SINGLE_SELECTION. This might annoy some people. We have to think it
         * over.
         */
        public void elementsDeselected(ElementsDeselectedEvent evt) {
            //fireValueChanged(evt.getBeginIndex(), evt.getLength() );
            int index0 = Math.min(leadSelectionIndex, anchorSelectionIndex);
            index0 = Math.min(index0, evt.getBeginIndex());
            int index1 = Math.max(leadSelectionIndex, anchorSelectionIndex);
            index1 = Math.max(index1, evt.getBeginIndex() + evt.getLength() - 1);
            fireValueChanged(index0, index1 - index0 + 1);
        }

        public void elementsSelected(ElementsSelectedEvent evt) {
            //fireValueChanged(evt.getBeginIndex(), evt.getLength() );
            int index0 = Math.min(leadSelectionIndex, anchorSelectionIndex);
            index0 = Math.min(index0, evt.getBeginIndex());
            int index1 = Math.max(leadSelectionIndex, anchorSelectionIndex);
            index1 = Math.max(index1, evt.getBeginIndex() + evt.getLength() - 1);
            if (leadSelectionIndex == -1) {
                leadSelectionIndex = evt.getBeginIndex();
            }
            fireValueChanged(index0, index1 - index0 + 1);
        }

        public void elementChanged(ElementChangedEvent evt) {
            // we can ignore that
        }

        public void elementsAdded(ElementsAddedEvent evt) {
            // TODO (mk) Do we need to do this here ?
            if (leadSelectionIndex >= evt.getBeginIndex()) {
                leadSelectionIndex += evt.getLength();
            }
        }

        public void elementsRemoved(ElementsRemovedEvent evt) {
            // TODO (mk) Do we need to do this here ?
            if (leadSelectionIndex >= evt.getBeginIndex() && leadSelectionIndex <= evt.getBeginIndex() + evt.getLength()) {
                leadSelectionIndex = -1;
            }
        }

        public void elementsReplaced(ElementsReplacedEvent evt) {
            // we can ignore that
        }
    };

    private final CopyOnWriteArrayList<ListSelectionListener> eventListeners = new CopyOnWriteArrayList<ListSelectionListener>();
    private int selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
    private boolean valueIsAdjusting = false;

    // Holding the first and the second index argument from the most recent call
    // to setSelectionInterval(), addSelectionInterval() or
    // removeSelectionInterval().
    private int anchorSelectionIndex = -1;
    private int leadSelectionIndex = -1;

    // Dirty indices for adjusting mode
    private int minLastChangedIndex = -1;
    private int maxLastChangedIndex = -1;

    public BnListSelectionModel(IListPM<? extends PresentationModel> aList) {
        if (aList == null)
            throw new IllegalArgumentException("list must not be null");
        this.list = aList;
        this.list.addListListener(this.listener);
    }

    /**
     * Disconnect <code>this</code> object from the underlying IListPM.
     */
    public void dismiss() {
        this.list.removeListListener(this.listener);
    }

    public void addListSelectionListener(ListSelectionListener l) {
        if (l == null) {
            throw new IllegalArgumentException("l must not be null.");
        }
        eventListeners.add(l);
    }

    public void removeListSelectionListener(ListSelectionListener l) {
        if (l == null) {
            throw new IllegalArgumentException("l must not be null.");
        }
        eventListeners.remove(l);
    }

    public int getSelectionMode() {
        return this.selectionMode;
    }

    public void setSelectionMode(int selectionMode) {
        switch (selectionMode) {
            case SINGLE_SELECTION:
            case SINGLE_INTERVAL_SELECTION:
            case MULTIPLE_INTERVAL_SELECTION:
                this.selectionMode = selectionMode;
                break;
            default:
                throw new IllegalArgumentException("invalid selectionMode");
        }
    }

    public boolean getValueIsAdjusting() {
        return valueIsAdjusting;
    }

    public void setValueIsAdjusting(boolean valueIsAdjusting) {
        // out("BnTableSelectionModel.setValueIsAdjusting("+valueIsAdjusting+")");
        if (valueIsAdjusting == this.valueIsAdjusting) {
            return;
        }
        this.valueIsAdjusting = valueIsAdjusting;
        int index0 = minLastChangedIndex;
        int index1 = maxLastChangedIndex;

        minLastChangedIndex = -1;
        maxLastChangedIndex = -1;

        if (this.valueIsAdjusting == false && index0 != -1 && index1 != -1) {
            fireValueChangedBetween(index0, index1);
            // fireValueChangedBetween(0, Integer.MAX_VALUE);
        }
    }

    public int getMaxSelectionIndex() {
        return list.getSelection().getMaxIndex();
    }

    public int getMinSelectionIndex() {
        return list.getSelection().getMinIndex();
    }

    public int getAnchorSelectionIndex() {
        return anchorSelectionIndex;
    }

    public void setAnchorSelectionIndex(int index) {
        int oldAnchor = this.anchorSelectionIndex;
        this.anchorSelectionIndex = index;
        fireValueChangedBetween(oldAnchor, anchorSelectionIndex);
    }

    public int getLeadSelectionIndex() {
        return leadSelectionIndex;
    }

    public void setLeadSelectionIndex(int index) {
        int oldLead = this.leadSelectionIndex;
        this.leadSelectionIndex = index;
        fireValueChangedBetween(oldLead, anchorSelectionIndex);
    }

    public boolean isSelectedIndex(int index) {
        return list.getSelection().contains(index);
    }

    public boolean isSelectionEmpty() {
        return list.getSelection().isEmpty();
    }

    public void clearSelection() {
        list.getSelection().clear();
    }

    public void setSelectionInterval(int index0, int index1) {
        // To be compatible with standard Swing:
        if (getSelectionMode() == SINGLE_SELECTION) {
            index0 = index1;
        }
        // update BnList selection
        updateLeadAnchorIndices(index0, index1);
        // update ListPM selection
        int beginIndex = Math.min(index0, index1);
        int endIndex = Math.max(index0, index1);
        list.getSelection().setInterval(beginIndex, endIndex);
    }

    public void addSelectionInterval(int index0, int index1) {
        if (index0 == -1 || index1 == -1) {
            return;
        }
        // To be compatible with standard Swing:
        // if we only allow a single selection, channel through
        // setSelectionInterval() to enforce the rule.
        if (getSelectionMode() == SINGLE_SELECTION) {
            setSelectionInterval(index0, index1);
            return;
        }

        int beginIndex = Math.min(index0, index1);
        int endIndex = Math.max(index0, index1);
        // If we only allow a single interval and this would result
        // in multiple intervals, then set the selection to be just
        // the new range.
        if (getSelectionMode() == SINGLE_INTERVAL_SELECTION && list.getSelection().isEmpty() == false) {
            if (list.getSelection().getMaxIndex() < beginIndex || endIndex < list.getSelection().getMinIndex()) {
                setSelectionInterval(index0, index1);
                return;
            }
        }

        updateLeadAnchorIndices(index0, index1);
        list.getSelection().addInterval(beginIndex, endIndex);
    }

    public void removeSelectionInterval(int index0, int index1) {
        // out("BnTableSelectionModel.removeSelectionInterval("+index0+","+index1+")");
        int beginIndex = Math.min(index0, index1);
        int endIndex = Math.max(index0, index1);

        // To be compatible with standard Swing:
        // if the removal would produce to two disjoint selections in a mode
        // that only allows one, extend the removal to the end of the selection.
        if (getSelectionMode() != MULTIPLE_INTERVAL_SELECTION && list.getSelection().isEmpty() == false) {
            if (list.getSelection().getMinIndex() < beginIndex && endIndex < list.getSelection().getMaxIndex()) {
                endIndex = Math.max(endIndex, list.getSelection().getMaxIndex());
            }
        }

        updateLeadAnchorIndices(index0, index1);

        list.getSelection().removeInterval(beginIndex, endIndex);
    }

    public void insertIndexInterval(int index, int length, boolean before) {
        // Within beanfabrics we don't need to do anything here.
    }

    public void removeIndexInterval(int index0, int index1) {
        // Within beanfabrics we don't need to do anything here.
    }

    protected void fireValueChangedBetween(int index0, int index1) {
        int beginIndex = Math.min(index0, index1);
        int endIndex = Math.max(index0, index1);
        int len = endIndex - beginIndex;
        fireValueChanged(beginIndex, len);
    }

    protected void fireValueChanged(int beginIndex, int length) {
        if (valueIsAdjusting) {
            // mark lower boundary of dirty indexes
            markDirty(beginIndex);
            // mark lower boundary of dirty indexes
            markDirty(beginIndex + length - 1);
        }
        if (eventListeners.isEmpty() == false) {
            ListSelectionEvent evt = new ListSelectionEvent(this, beginIndex, beginIndex + length - 1, this.valueIsAdjusting);
            for (ListSelectionListener l : eventListeners) {
                l.valueChanged(evt);
            }
        }
    }

    protected void updateLeadAnchorIndices(int index0, int index1) {
        if (index0 != index1 && valueIsAdjusting) {
            if (anchorSelectionIndex == -1) {
                anchorSelectionIndex = index0;
            }
            leadSelectionIndex = index1;
        } else {
            anchorSelectionIndex = index0;
            leadSelectionIndex = index1;
        }
    }

    protected void markDirty(int index) {
        if (valueIsAdjusting) {
            if (minLastChangedIndex == -1 || minLastChangedIndex > index) {
                minLastChangedIndex = index;
            }
            if (maxLastChangedIndex == -1 || maxLastChangedIndex < index) {
                maxLastChangedIndex = index;
            }
        }
    }
}