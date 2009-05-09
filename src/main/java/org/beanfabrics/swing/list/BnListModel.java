/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.swing.list;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import org.beanfabrics.PathEvaluation;
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
 * The <code>BnListModel</code> is a {@link ListModel} that decorates a {@link IListPM}
 * 
 * @author Max Gensthaler
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BnListModel extends AbstractListModel {
	private final IListPM list;
	private final CellConfig cellConfig;
	
	private ListListener listener = new WeakListListener() {
		public void elementsSelected(ElementsSelectedEvent evt) {
			// ignore
		}

		public void elementsDeselected(ElementsDeselectedEvent evt) {
			// ignore
		}

		public void elementChanged(ElementChangedEvent evt) {
			fireContentsChanged(evt.getSource(), evt.getIndex(), evt.getIndex());
		}

		public void elementsReplaced(ElementsReplacedEvent evt) {
			fireContentsChanged(evt.getSource(), evt.getBeginIndex(), evt.getBeginIndex() + evt.getLength() - 1);
		}

		public void elementsAdded(ElementsAddedEvent evt) {
			fireIntervalAdded(evt.getSource(), evt.getBeginIndex(), evt.getBeginIndex() + evt.getLength() - 1);
		}

		public void elementsRemoved(ElementsRemovedEvent evt) {
			fireIntervalRemoved(evt.getSource(), evt.getBeginIndex(), evt.getBeginIndex() + evt.getLength() - 1);
		}
	};

	public BnListModel(IListPM listCell, CellConfig cellConfig) {
		if (listCell == null)
			throw new IllegalArgumentException("list must not be null");
		this.list = listCell;
		this.list.addListListener(this.listener);
		this.cellConfig = cellConfig;
	}
	
	/**
	 * Disconnect <code>this</code> object from the underlying IListPM. 
	 */
	public void dismiss() {
		this.list.removeListListener(this.listener);
	}

	public CellConfig getCellConfig() {
		return cellConfig;
	}

	public Object getElementAt(int index) {
		PresentationModel rowMdl = list.getAt(index);
		return PathEvaluation.evaluateOrNull(rowMdl, this.cellConfig.getPath());
	}

	public int getSize() {
		return this.list.size();
	}

	public IListPM getListCell() {
		return this.list;
	}
}