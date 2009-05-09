/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.swing.table;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.beanfabrics.Path;
import org.beanfabrics.log.Logger;
import org.beanfabrics.log.LoggerFactory;
import org.beanfabrics.model.IListPM;
import org.beanfabrics.model.SortKey;

/**
 * The <code>Java5SortingTableHeader</code> is a {@link JTableHeader} that adds
 * a basic sorting ability to the java5 table header.
 *
 * When the user clicks on a column header the underlying {@link IListPM} is sorted
 * by the associated property's comparator.
 * <pM>
 * Please note:
 * <ul>
 * <li>that this header does not change the look of the table header - no
 * sorting arrow is added to the header.</li>
 * <li>the sorting feature works only for tables with a
 * {@link BnTableModel}.</li>
 * </ul>
 *
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class Java5SortingTableHeader extends JTableHeader {
	private final static Logger LOG = LoggerFactory.getLogger(Java5SortingTableHeader.class);
	private final MouseListener mouseListener = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			onClick(e);
		}
	};

	public Java5SortingTableHeader() {
		super();
		installListeners();
	}

	public Java5SortingTableHeader(TableColumnModel cm) {
		super(cm);
		installListeners();
	}

	private void installListeners() {
		this.addMouseListener( mouseListener);
	}

	/**
	 * Handles the click event and sorts the table.
	 * @param e
	 */
	protected void onClick(MouseEvent e) {
		int visColIndex = getTable().columnAtPoint( e.getPoint());
		if ( visColIndex >= 0) {
			int modelColIndex = getColumnModel().getColumn(visColIndex).getModelIndex();
			toggleSortOrder( modelColIndex);
		}
	}

	private void toggleSortOrder(int modelColIndex) {
		if(LOG.isDebugEnabled()) {
			LOG.debug("toggling sort order for column index "+modelColIndex);
		}
		TableModel tblModel = getTable().getModel();
		if ( tblModel instanceof BnTableModel) {
			BnTableModel model = (BnTableModel)tblModel;
			IListPM listPM = model.getPresentationModel();
			Path path = model.getColumnPath(modelColIndex);
			SortKey newSortKey = getNewSortKey(listPM, path);

			listPM.sortBy(newSortKey);
		} else {
			LOG.error("Can't sort unknown table model: "+tblModel.getClass().getName());
		}
	}

	private SortKey getNewSortKey(IListPM listMdl, Path path) {
		Collection<SortKey> sortKeys = listMdl.getSortKeys();
		for( SortKey sortKey: sortKeys) {
			if ( sortKey.getSortPath().equals(path)) {
				return new SortKey( !sortKey.isAscending(), path);
			}
		}
		// else sort ascending
		return new SortKey( /*ascending=*/true, path);
	}
}