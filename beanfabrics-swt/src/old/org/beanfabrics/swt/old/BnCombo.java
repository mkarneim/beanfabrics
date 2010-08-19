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
import org.beanfabrics.model.Options;
import org.beanfabrics.swt.ValidationIndicator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Michael Karneim
 */
public class BnCombo extends Combo implements View<ITextPM>, ModelSubscriber {
	private final Link link = new Link(this);

	private final PropertyChangeListener listener = new WeakPropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			refreshUI();
		}
	};
	private final ModifyListener uiListener = new ModifyListener() {
		public void modifyText(ModifyEvent evt) {
			updateModel();
		}
	};
	private ITextPM pModel;
	private ValidationIndicator validationIndicator;

	public BnCombo(Composite parent, int style) {
		super(parent, style);
		if (parent instanceof ValidationIndicator) {
			setValidationIndicator((ValidationIndicator)parent);
		}
		this.addModifyListener(uiListener);
		refreshUI();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	protected void updateModel() {
		checkWidget ();
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
		checkWidget ();
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
		refreshOptions();
		refreshText();
		refreshToolTip();
		refreshValidationIndicator();
	}

	protected void refreshEditable() {
		if (pModel != null) {
			setEnabled( pModel.isEditable());
		} else {
			setEnabled(false);
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

	protected void refreshToolTip() {
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

	protected void refreshText() {
		if (pModel != null) {
			final String txtModel = pModel.getText();
			if (!getText().equals( txtModel)) {
				setText( txtModel);
				if ((getStyle() & SWT.READ_ONLY)!=0) {
					if (indexOf(txtModel)==-1) {
						add(txtModel,0);
						select( 0);
					}
				}
			}
		} else {
			setText("");
		}
	}

	protected void refreshOptions() {
		if (pModel != null) {
			Options<?> opts = pModel.getOptions();
			if (opts != null) {
				if (equals( getItems(), opts.getValues())==false) {
					remove(0, getItemCount()-1);
					for( String value: opts.getValues()) {
						add(value);
					}
				}
			} else {
				removeAll();
			}
		} else {
			removeAll();
		}
	}

	private boolean equals(String[] a, String[] b) {
		if (a == b ) {
			return true;
		} else if (a == null || b == null) {
			return false;
		} else if (a.length != b.length) {
			return false;
		} else {
			for( int i=0; i<a.length; ++i) {
				if (!equals(a[i], b[i])) {
					return false;
				}
			}
			return true;
		}
	}

	private boolean equals( String a, String b) {
		if (a == b ) {
			return true;
		} else if (a== null ) {
			return false;
		} else {
			return a.equals(b);
		}
	}

	/** {@inheritDoc} */
	public void setModelProvider(IModelProvider provider) {
		checkWidget ();
		this.link.setModelProvider(provider);
	}

	/** {@inheritDoc} */
	public IModelProvider getModelProvider() {
		checkWidget ();
		return link.getModelProvider();
	}

	/** {@inheritDoc} */
	public void setPath(Path path) {
		checkWidget ();
		this.link.setPath(path);
	}

	/** {@inheritDoc} */
	public Path getPath() {
		checkWidget ();
		return link.getPath();
	}

	public ValidationIndicator getValidationIndicator() {
		return validationIndicator;
	}

	public void setValidationIndicator(ValidationIndicator validationIndicator) {
		this.validationIndicator = validationIndicator;
	}
}
