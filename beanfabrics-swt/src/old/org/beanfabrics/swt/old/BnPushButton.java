package org.beanfabrics.swt.old;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.util.ExceptionUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Michael Karneim
 */
public class BnPushButton extends Composite implements View<IOperationPM>, ModelSubscriber{
	private final Link link = new Link(this);
	private final PropertyChangeListener listener = new WeakPropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			refreshUI();
		}
	};

	private Button button;
	private IOperationPM pModel;

	/**
	 * Create the composite
	 * @param parent
	 * @param style is ignored
	 */
	public BnPushButton(Composite parent, int style) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout());
		button = new Button(this, SWT.PUSH);
		button.addSelectionListener( new SelectionListener() {
			public void widgetSelected(SelectionEvent evt) {
				executeOperation();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// ignore
			}
		});
		//
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/** {@inheritDoc} */
	public IOperationPM getPresentationModel() {
		return pModel;
	}

	/** {@inheritDoc} */
	public void setPresentationModel(IOperationPM pModel) {
		if (this.pModel != null) {
			this.pModel.removePropertyChangeListener(listener);
		}
		this.pModel = pModel;
		if (this.pModel != null) {
			this.pModel.addPropertyChangeListener(listener);
		}
		refreshUI();
	}

	protected void refreshUI() {
		refreshEnabled();
		refreshTooltip();
	}

	protected void refreshEnabled() {
		if (pModel != null) {
			button.setEnabled( pModel.isEnabled());
		} else {
			button.setEnabled(false);
		}
	}

	protected void refreshTooltip() {
		if (pModel != null) {
			if (pModel.isValid()==false) {
				setToolTipText(pModel.getValidationState().getMessage());
			} else {
				setToolTipText(pModel.getDescription());
			}
		} else {
			setToolTipText(null);
		}
	}

	private void executeOperation() {
		try {
			if (pModel != null) {
				pModel.execute();
			}
		} catch (Throwable e) {
			ExceptionUtil.getInstance().handleException("", e);
		}
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

	public Button getButton() {
		return button;
	}
}
