/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.swing.internal;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.util.ExceptionUtil;

/**
 * The <code>OperationPMAction</code> is an {@link Action} that is a view on an {@link IOperationPM}.
 *
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class OperationPMAction extends AbstractAction implements View<IOperationPM> {
	private IOperationPM pModel;
	private transient WeakPropertyChangeListener listener = new WeakPropertyChangeListener() {
		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			refresh();
		}
	};

	public OperationPMAction() {

	}

	public OperationPMAction( IOperationPM pModel) {
		this.setPresentationModel(pModel);
	}


	/** {@inheritDoc} */
	public IOperationPM getPresentationModel() {
		return this.pModel;
	}

	/** {@inheritDoc} */
	public void setPresentationModel(IOperationPM pModel) {
		if (this.isConnected()) {
			this.pModel.removePropertyChangeListener(this.listener);
		}
		this.pModel = pModel;
		if (pModel != null) {
			this.pModel.addPropertyChangeListener(this.listener);
		}
		this.refresh();
	}

	/**
	 * Returns whether this component is connected to the target {@link PresentationModel}
	 * to synchronize with. This is a convenience method.
	 *
	 * @return <code>true</code> when this component is connected, else
	 *         <code>false</code>
	 */
	boolean isConnected() {
		return this.pModel != null;
	}

	public void actionPerformed(ActionEvent evt) {
		if (this.isConnected()) {
			if (this.isEnabled()) {
				try {
					this.execute();
				} catch (Throwable t) {
					ExceptionUtil.getInstance().handleException("Error during invocation of pModel", t);
				}
			}
		}
	}

	protected void execute() throws Throwable {
		this.pModel.execute();
	}

	/**
	 * Configures this component depending on the target {@link AbstractPM}s
	 * attributes.
	 */
	protected void refresh() {
		if (this.isConnected()) {
			final boolean isValid = this.pModel.isValid();
			this.setEnabled(isValid);
			this.setToolTipText(isValid ? this.pModel.getDescription() : this.pModel.getValidationState().getMessage());

			Icon icon = pModel.getIcon();
			this.setIcon(icon);

			String title = pModel.getTitle();
			this.setText(title);

		} else {
			this.setEnabled(false);
			this.setToolTipText(null);

			this.setIcon(null);
			this.setText(null);
		}
	}

	private void setText(String title) {
		putValue(Action.NAME, title);
	}

	private void setIcon(Icon icon) {
		putValue(Action.SMALL_ICON, icon);

	}

	private void setToolTipText(String value) {
		putValue(Action.SHORT_DESCRIPTION, value);
	}
}