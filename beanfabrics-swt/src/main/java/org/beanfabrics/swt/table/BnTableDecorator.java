/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swt.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.PathEvaluation;
import org.beanfabrics.View;
import org.beanfabrics.event.ElementChangedEvent;
import org.beanfabrics.event.ElementsAddedEvent;
import org.beanfabrics.event.ElementsDeselectedEvent;
import org.beanfabrics.event.ElementsRemovedEvent;
import org.beanfabrics.event.ElementsReplacedEvent;
import org.beanfabrics.event.ElementsSelectedEvent;
import org.beanfabrics.event.ListListener;
import org.beanfabrics.event.WeakListListener;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.log.Logger;
import org.beanfabrics.log.LoggerFactory;
import org.beanfabrics.model.IListPM;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swt.AbstractDecorator;
import org.beanfabrics.swt.model.IImagePM;
import org.beanfabrics.swt.table.ViewConfig.Column;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * This is a Beanfabrics view on a {@link IListPM} using an SWT {@link Table}.
 *
 * @author Michael Karneim
 */
public class BnTableDecorator extends AbstractDecorator<Table> implements View<IListPM<? extends PresentationModel>>, ModelSubscriber {
	private static Logger LOG = LoggerFactory.getLogger(BnTableDecorator.class);

	private final Link link = new Link(this);
	private final PropertyChangeListener listener = new WeakPropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			if (LOG.isDebugEnabled()) {
				LOG.debug(".propertyChange()");
			}
			//refreshUI();
		}
	};
	private final ListListener listListener = new WeakListListener() {
		public void elementsSelected(ElementsSelectedEvent evt) {
			int start = evt.getBeginIndex();
			int end = start + evt.getLength() - 1;
			if (LOG.isDebugEnabled()) {
				LOG.debug(".elementsSelected() "+start+"-"+end);
			}
			table.select(start, end);
			table.showSelection(); // scroll to first selected index
		}

		public void elementsReplaced(ElementsReplacedEvent evt) {
			int start = evt.getBeginIndex();
			int end = start + evt.getLength() - 1;
			if (LOG.isDebugEnabled()) {
				LOG.debug(".elementsReplaced() "+start+"-"+end);
			}
			table.remove(start, end);
			for (int idx = start; idx <= end; ++idx) {
				PresentationModel rowModel = pModel.getAt(idx);
				addTableItem( rowModel, idx);
				if (pModel.getSelection().contains(idx)) {
					table.select(idx);
				}
			}
		}

		public void elementsRemoved(ElementsRemovedEvent evt) {
			int start = evt.getBeginIndex();
			int end = start + evt.getLength() - 1;
			table.remove(start, end);
		}

		public void elementsDeselected(ElementsDeselectedEvent evt) {
			int start = evt.getBeginIndex();
			int end = start + evt.getLength() - 1;
			if (LOG.isDebugEnabled()) {
				LOG.debug(".elementsDeselected() "+start+"-"+end);
			}
			table.deselect(start, end);
		}

		public void elementsAdded(ElementsAddedEvent evt) {
			int start = evt.getBeginIndex();
			int end = start + evt.getLength() - 1;
			for( int idx = start; idx <= end; ++idx) {
				PresentationModel rowModel = pModel.getAt(idx);
				addTableItem(rowModel, idx);
				if (pModel.getSelection().contains(idx)) {
					table.select(idx);
				}
			}
		}

		public void elementChanged(ElementChangedEvent evt) {
			int index = evt.getIndex();
			if (LOG.isDebugEnabled()) {
				LOG.debug(".elementChanged() "+index);
			}
			if (index >= table.getItemCount()) {
				// this happens when rarely when an event listener has changed the element
				// during the process of adding the element.
				return;
			}
			refreshTableItem( index);
		}

	};
	private final SelectionListener uiSelectionListener = new SelectionListener() {
		public void widgetSelected(SelectionEvent evt) {
			if (LOG.isDebugEnabled()) {
				LOG.debug(".widgetSelected() evt="+evt);
			}
			updateModelSelection();
		}

		public void widgetDefaultSelected(SelectionEvent evt) {
			if (LOG.isDebugEnabled()) {
				LOG.debug(".widgetDefaultSelected() evt="+evt);
			}
			updateModelSelection();
		}
	};


	private final Table table;
	private IListPM<? extends PresentationModel> pModel;
	private ViewConfig config;
	private Collection<TableColumn> columns;

	/**
	 * Create the BnTable
	 * @param parent
	 * @param style
	 */
	public BnTableDecorator(Table table) {
		super(table);
		this.table = table;
		hookControl(this.table);
	}

	/**
	 * Create the BnTable
	 * @param parent
	 * @param style
	 */
	public BnTableDecorator(Composite parent, int style) {
		this(new Table(parent, style));
	}

	/**
	 * Create the BnTable
	 * @param parent
	 */
	public BnTableDecorator(Composite parent) {
		this(new Table(parent, SWT.SINGLE | SWT.FULL_SELECTION));
	}

	protected void hookControl( Table table) {
		table.addSelectionListener( uiSelectionListener);
	}

	public void dispose() {
		table.removeSelectionListener( uiSelectionListener);
		table.dispose();
	}

	/** {@inheritDoc} */
	public IListPM getPresentationModel() {
		return pModel;
	}

	/** {@inheritDoc} */
	public void setPresentationModel(IListPM pModel) {
		if (this.pModel != null) {
			this.pModel.removePropertyChangeListener(listener);
			this.pModel.removeListListener( listListener);
		}
		this.pModel = pModel;
		if (this.pModel != null) {
			this.pModel.addPropertyChangeListener(listener);
			this.pModel.addListListener( listListener);
		}
		refreshUI();
	}

	public void setViewConfig( ViewConfig aConfig) {
		this.config = aConfig;
		refreshUI();
	}

	/** {@inheritDoc} */
	public void setModelProvider(IModelProvider provider) {
		this.link.setModelProvider(provider);
	}

	/** {@inheritDoc} */
	public IModelProvider getModelProvider() {
		return link.getModelProvider();
	}

	/** {@inheritDoc} */
	public void setPath(Path path) {
		this.link.setPath(path);
	}

	/** {@inheritDoc} */
	public Path getPath() {
		return link.getPath();
	}

	public Table getTable() {
		return table;
	}

	protected void refreshUI() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("BnTable.refreshUI()");
		}
		// clear UI
		dropColumns();
		table.removeAll(); // remove rows
		// init UI
		if (pModel != null && config != null) {
			createColumns();
			createRows();
			refreshUISelection();
		}
	}

	protected void updateModelSelection() {
		int[] selIndices = table.getSelectionIndices();
		pModel.getSelection().setIndexes( selIndices);
	}

	protected void refreshUISelection() {
		int[] selIndices = pModel.getSelection().getIndexes();
		table.setSelection( selIndices);
	}

	private void dropColumns() {
		if (this.columns != null) {
			for( TableColumn col: this.columns) {
				col.dispose();
			}
		}
		this.columns = null;
	}

	private void createColumns() {
		this.columns = new ArrayList<TableColumn>();
		for( ViewConfig.Column col: config.getColumns()) {
			TableColumn newCol = new TableColumn(getTable(), SWT.NULL);
			newCol.setMoveable(true);
			newCol.setResizable(true);
			newCol.setText(col.header);
			newCol.setWidth(col.width);
			this.columns.add(newCol);
		}
	}

	private void createRows() {
		int rowIndex = 0;
		for (Iterator<? extends PresentationModel> it = pModel.iterator(); it.hasNext(); rowIndex++) {
			PresentationModel rowModel = it.next();
			addTableItem(rowModel, rowIndex);
		}
	}

	private TableItem addTableItem(PresentationModel rowModel, int rowIndex) {
		TableItem result = new TableItem(table, SWT.NULL, rowIndex);
		result.setData( rowModel);
		int colIndex = -1;
		for (ViewConfig.Column col : config.getColumns()) {
			colIndex++;
			result.setText( colIndex, getText(rowModel, col));
			result.setImage( colIndex, getImage( rowModel, col));
		}
		return result;
	}

	private String getText( PresentationModel rowModel, ViewConfig.Column col) {
		Path path = col.path;
		PresentationModel target = PathEvaluation.evaluateOrNull(rowModel, path);
		if (target != null && target instanceof ITextPM) {
			ITextPM textModel = (ITextPM)target;
			return textModel.getText();
		} else {
			// unsupported target type
			return "";
		}
	}

	private Image getImage(PresentationModel rowModel, Column col) {
		Path path = col.path;
		PresentationModel target = PathEvaluation.evaluateOrNull(rowModel, path);
		if (target != null && target instanceof IImagePM) {
			IImagePM imgModel = (IImagePM)target;
			return imgModel.getImage();
		} else {
			// unsupported target type
			return null;
		}
	}

	protected void refreshTableItem(int index) {
		TableItem item = table.getItem(index);
		PresentationModel rowModel = (PresentationModel)item.getData();
		int colIndex = -1;
		for( ViewConfig.Column col :config.getColumns()) {
			colIndex++;
			item.setText( colIndex, getText(rowModel, col));
			item.setImage( colIndex, getImage( rowModel, col));
		}
	}
}
