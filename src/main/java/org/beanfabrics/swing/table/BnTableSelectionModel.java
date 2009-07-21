/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.table;

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
 * The <code>BnTableSelectionModel</code> is a {@link ListSelectionModel} that
 * decorates a {@link IListPM}.
 * 
 * @author Michael Karneim
 */
public class BnTableSelectionModel implements ListSelectionModel {
    private IListPM<? extends PresentationModel> list;

    private ListListener listener = new WeakListListener() {
        /* TODO (mk) currently we don't care about the selection mode
         * when receiving and forwarding events from the IListPM
         * to the JTable. So it't possible to have multiple selections
         * even if the mode is SINGLE_SELECTION.
         * This might annoy some people. We have to think it over.
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

    private final CopyOnWriteArrayList<ListSelectionListener> eventListener = new CopyOnWriteArrayList<ListSelectionListener>();
    private int selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
    private boolean valueIsAdjusting = false;

    // Holding the first and the second index argument from the most recent call to
    // setSelectionInterval(), addSelectionInterval() or removeSelectionInterval().
    private int anchorSelectionIndex = -1;
    private int leadSelectionIndex = -1;

    // Dirty indices for adjusting mode
    private int minLastChangedIndex = -1;
    private int maxLastChangedIndex = -1;

    public BnTableSelectionModel(IListPM<? extends PresentationModel> pModel) {
        if (pModel == null) {
            throw new IllegalArgumentException("pModel must not be null");
        }
        this.list = pModel;

        this.list.addListListener(this.listener);
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
        eventListener.add(l);
    }

    /** {@inheritDoc} */
    public void removeListSelectionListener(ListSelectionListener l) {
        if (l == null) {
            throw new IllegalArgumentException("l must not be null.");
        }
        eventListener.remove(l);
    }

    /** {@inheritDoc} */
    public int getSelectionMode() {
        return this.selectionMode;
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    public boolean getValueIsAdjusting() {
        return valueIsAdjusting;
    }

    /** {@inheritDoc} */
    public void setValueIsAdjusting(boolean valueIsAdjusting) {
        //		out("BnTableSelectionModel.setValueIsAdjusting("+valueIsAdjusting+")");
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
            //fireValueChangedBetween(0, Integer.MAX_VALUE);
        }
    }

    /** {@inheritDoc} */
    public int getMaxSelectionIndex() {
        return list.getSelection().getMaxIndex();
    }

    /** {@inheritDoc} */
    public int getMinSelectionIndex() {
        return list.getSelection().getMinIndex();
    }

    /** {@inheritDoc} */
    public int getAnchorSelectionIndex() {
        //		out("BnTableSelectionModel.getAnchorSelectionIndex() "+anchorSelectionIndex);
        // Return the first index argument from the most recent call to
        // setSelectionInterval(), addSelectionInterval() or removeSelectionInterval().
        return anchorSelectionIndex;
    }

    /** {@inheritDoc} */
    public void setAnchorSelectionIndex(int index) {
        //		out("BnTableSelectionModel.setAnchorSelectionIndex("+index+")###");
        int oldAnchor = this.anchorSelectionIndex;
        this.anchorSelectionIndex = index;
        fireValueChangedBetween(oldAnchor, anchorSelectionIndex);
    }

    /** {@inheritDoc} */
    public int getLeadSelectionIndex() {
        //		out("BnTableSelectionModel.getLeadSelectionIndex() "+leadSelectionIndex);

        return leadSelectionIndex;
    }

    /** {@inheritDoc} */
    public void setLeadSelectionIndex(int index) {
        //		out("BnTableSelectionModel.setLeadSelectionIndex("+index+")###");
        int oldLead = this.leadSelectionIndex;
        this.leadSelectionIndex = index;
        fireValueChangedBetween(oldLead, anchorSelectionIndex);
    }

    /** {@inheritDoc} */
    public boolean isSelectedIndex(int index) {
        return list.getSelection().contains(index);
    }

    /** {@inheritDoc} */
    public boolean isSelectionEmpty() {
        return list.getSelection().isEmpty();
    }

    /** {@inheritDoc} */
    public void clearSelection() {
        list.getSelection().clear();
    }

    /** {@inheritDoc} */
    public void setSelectionInterval(int index0, int index1) {
        //		out("BnTableSelectionModel.setSelectionInterval("+index0+","+index1+")");

        // To be compatible with standard Swing:
        if (getSelectionMode() == SINGLE_SELECTION) {
            index0 = index1;
        }

        int beginIndex = Math.min(index0, index1);
        int endIndex = Math.max(index0, index1);

        updateLeadAnchorIndices(index0, index1);

        list.getSelection().setInterval(beginIndex, endIndex);
    }

    /** {@inheritDoc} */
    public void addSelectionInterval(int index0, int index1) {
        //		out("BnTableSelectionModel.addSelectionInterval("+index0+","+index1+")");
        if (index0 == -1 || index1 == -1) {
            return;
        }
        int beginIndex = Math.min(index0, index1);
        int endIndex = Math.max(index0, index1);

        // To be compatible with standard Swing:
        // if we only allow a single selection, channel through
        // setSelectionInterval() to enforce the rule.
        if (getSelectionMode() == SINGLE_SELECTION) {
            setSelectionInterval(index0, index1);
            return;
        }
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

    /** {@inheritDoc} */
    public void removeSelectionInterval(int index0, int index1) {
        //		out("BnTableSelectionModel.removeSelectionInterval("+index0+","+index1+")");
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

    /** {@inheritDoc} */
    public void insertIndexInterval(int index, int length, boolean before) {
        // Within beanfabrics we don't need to do anything here.
    }

    /** {@inheritDoc} */
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
            markDirty(beginIndex);
            markDirty(beginIndex + length - 1);
        }
        if (eventListener.isEmpty()) {
            return;
        }
        ListSelectionEvent evt = new ListSelectionEvent(this, beginIndex, beginIndex + length - 1, this.valueIsAdjusting);
        for (ListSelectionListener l : eventListener) {
            l.valueChanged(evt);
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

    //	long lasttime = 0;
    //	String lasttext = "";
    //	private void out( String text) {
    //		long now = System.currentTimeMillis();
    //		if ( now - lasttime > 1000) {
    //			System.out.println();
    //		}
    //		lasttime = now;
    //		if ( lasttext.equals(text)) {
    //			System.out.print(".");
    //		} else {
    //			lasttext = text;
    //			System.out.print("\n"+text);
    //		}
    //		System.out.flush();
    //	}
}