/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @author Michael Karneim
 */
public class BnTextDecorator extends AbstractDecorator<Text> implements View<ITextPM>, ModelSubscriber {
	private final Link link = new Link(this);
	private final PropertyChangeListener listener = new WeakPropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			refreshUI();
		}
	};

	private ValidationIndicator validationIndicator;
	private ITextPM pModel;
	private Text text;

	public BnTextDecorator(Text text) {
		super(text);
		this.text = text;
		hookControl(text);
	}

	private void hookControl(Text text) {
		Composite parent = text.getParent();
		if (parent instanceof ValidationIndicator) {
			setValidationIndicator((ValidationIndicator) parent);
		}
		text.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent evt) {
				updateModel();
			}
		});
		refreshUI();
	}

	public BnTextDecorator(Composite parent, int style) {
		this(new Text(parent, style));
	}

	protected void updateModel() {
		if (pModel != null) {
			pModel.setText(text.getText());
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
			text.setEnabled(pModel.isEditable());
		} else {
			text.setEnabled(false);
		}
	}

	protected void refreshText() {
		if (pModel != null) {
			if (!text.getText().equals(pModel.getText())) {
				text.setText(pModel.getText());
			}
		} else {
			text.setText("");
		}
	}

	protected void refreshTooltip() {
		if (pModel != null) {
			if (pModel.isValid() == false) {
				text.setToolTipText(pModel.getValidationState().getMessage());
			} else {
				text.setToolTipText(pModel.getDescription());
			}
		} else {
			text.setToolTipText(null);
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
