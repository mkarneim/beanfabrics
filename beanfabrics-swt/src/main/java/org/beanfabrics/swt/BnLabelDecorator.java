package org.beanfabrics.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.ITextPM;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author Michael Karneim
 */
public class BnLabelDecorator extends AbstractDecorator<Label> implements View<ITextPM>, ModelSubscriber {
	private final Link link = new Link(this);
	private final PropertyChangeListener listener = new WeakPropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			refreshUI();
		}
	};

	private ValidationIndicator validationIndicator;
	private ITextPM pModel;
	private Label label;

	public BnLabelDecorator(Label label) {
		super(label);
		this.label = label;
		hookControl(label);
	}

	private void hookControl(Label label) {
		Composite parent = label.getParent();
		if (parent instanceof ValidationIndicator) {
			setValidationIndicator((ValidationIndicator) parent);
		}
		refreshUI();
	}

	public BnLabelDecorator(Composite parent, int style) {
		this(new Label(parent, style));
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

		refreshText();
		refreshTooltip();
		refreshValidationIndicator();
	}

	protected void refreshText() {
		if (pModel != null) {
			if (!label.getText().equals(pModel.getText())) {
				label.setText(pModel.getText());
			}
		} else {
			label.setText("");
		}
	}

	protected void refreshTooltip() {
		if (pModel != null) {
			if (pModel.isValid() == false) {
				label.setToolTipText(pModel.getValidationState().getMessage());
			} else {
				label.setToolTipText(pModel.getDescription());
			}
		} else {
			label.setToolTipText(null);
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
