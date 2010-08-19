package org.beanfabrics.swt.old;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.swt.ValidationIndicator;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @author Michael Karneim
 */
public class BnText extends Text implements View<ITextPM>, ModelSubscriber {
	private final Link link = new Link(this);
	private final PropertyChangeListener listener = new WeakPropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			refreshUI();
		}
	};

	private ValidationIndicator validationIndicator;
	private ITextPM pModel;

	public BnText(Composite parent, int style) {
		super(parent, style);
		if (parent instanceof ValidationIndicator) {
			setValidationIndicator((ValidationIndicator)parent);
		}
		addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent evt) {
				updateModel();
			}
		});
		refreshUI();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	protected void updateModel() {
		if (pModel !=null) {
			pModel.setText(getText());
		}
	}

	/** {@inheritDoc} */
	public ITextPM getPresentationModel() {
		return pModel;
	}

	/** {@inheritDoc} */
	public void setPresentationModel(ITextPM pModel) {
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
		refreshEditable();
		refreshText();
		refreshTooltip();
		refreshValidationIndicator();
	}

	protected void refreshEditable() {
		if (pModel != null) {
			setEnabled( pModel.isEditable());
		} else {
			setEnabled(false);
		}
	}

	protected void refreshText() {
		if (pModel != null) {
			if (!getText().equals( pModel.getText())) {
				setText( pModel.getText());
			}
		} else {
			setText("");
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

	protected void refreshValidationIndicator() {
		if (validationIndicator != null ) {
			if (pModel != null) {
				validationIndicator.setValidationState(pModel.getValidationState());
			} else {
				validationIndicator.setValidationState(null);
			}
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
