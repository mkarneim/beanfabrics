package org.beanfabrics.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.ConversionException;
import org.beanfabrics.model.IBooleanPM;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Michael Karneim
 */
public class BnButtonDecorator extends AbstractDecorator<Button> implements View<IBooleanPM>, ModelSubscriber {
	private final Link link = new Link(this);
	private final PropertyChangeListener listener = new WeakPropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			refreshUI();
		}
	};
	private final SelectionListener uiListener = new SelectionListener() {
		public void widgetSelected(SelectionEvent arg0) {
			//System.out.println(".widgetSelected()");
			updateModel();
		}

		public void widgetDefaultSelected(SelectionEvent arg0) {
			//System.out.println("BnButton.widgetDefaultSelected()");
			// ignore
		}
	};

	private ValidationIndicator validationIndicator;
	private IBooleanPM pModel;
	private Button button;

	public BnButtonDecorator(Button button) {
		super(button);
		checkStyle(button.getStyle());
		this.button = button;
		hookControl(button);
	}

	private void checkStyle(int style) {
		if ((style & SWT.PUSH) != 0) {
			throw new IllegalArgumentException("Button must not be styled with SWT.PUSH");
		}
	}

	/**
	 * Create the composite
	 *
	 * @param parent
	 * @param style
	 *            is ignored
	 */
	public BnButtonDecorator(Composite parent, int style) {
		this(new Button(parent, style));
	}

	private void hookControl(Button button) {
		Composite parent = button.getParent();
		if (parent instanceof ValidationIndicator) {
			setValidationIndicator((ValidationIndicator) parent);
		}
		button.addSelectionListener(uiListener);
		refreshUI();
	}

	/** {@inheritDoc} */
	public IBooleanPM getPresentationModel() {
		return pModel;
	}

	/** {@inheritDoc} */
	public void setPresentationModel(IBooleanPM pModel) {
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
		refreshSelection();
		refreshValidationIndicator();
	}

	protected void refreshEnabled() {
		if (pModel != null) {
			button.setEnabled(pModel.isEditable());
		} else {
			button.setEnabled(false);
		}
	}

	protected void refreshTooltip() {
		if (pModel != null) {
			if (pModel.isValid() == false) {
				button.setToolTipText(pModel.getValidationState().getMessage());
			} else {
				button.setToolTipText(pModel.getDescription());
			}
		} else {
			button.setToolTipText(null);
		}
	}

	protected void refreshSelection() {
		if (pModel != null) {
			try {
				Boolean value = pModel.getBoolean();
				button.setSelection(value != null && value.booleanValue());
			} catch (ConversionException ex) {
				// ignore
			}
		} else {
			button.setSelection(false);
		}
	}

	protected void refreshValidationIndicator() {
		if (validationIndicator != null) {
			if (pModel != null) {
				validationIndicator.setValidationState(pModel.getValidationState());
			} else {
				validationIndicator.setValidationState(null);
			}
		}
	}

	protected void updateModel() {
		if (pModel != null) {
			pModel.setBoolean(button.getSelection());
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

	public ValidationIndicator getValidationIndicator() {
		return validationIndicator;
	}

	public void setValidationIndicator(ValidationIndicator validationIndicator) {
		this.validationIndicator = validationIndicator;
	}
}
