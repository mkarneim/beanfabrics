/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
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
import org.beanfabrics.model.IIntegerPM;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author Max Gensthaler
 */
public class BnSpinnerDecorator extends AbstractDecorator<Spinner> implements View<IIntegerPM>, ModelSubscriber {
    private final Link link = new Link(this);
    private final PropertyChangeListener listener = new WeakPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            refreshUI();
        }
    };

    private ValidationIndicator validationIndicator;
    private IIntegerPM pModel;
    private Spinner spinner;

    public BnSpinnerDecorator(Spinner spinner) {
        super(spinner);
        this.spinner = spinner;
        hookControl(spinner);
    }

    private void hookControl(Spinner spinner) {
        Composite parent = spinner.getParent();
        if (parent instanceof ValidationIndicator) {
            setValidationIndicator((ValidationIndicator)parent);
        }
        spinner.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent evt) {
                updateModel();
            }
        });
        refreshUI();
    }

    public BnSpinnerDecorator(Composite parent, int style) {
        this(new Spinner(parent, style));
    }

    protected void updateModel() {
        if (pModel != null) {
            pModel.setText(String.valueOf(spinner.getSelection()));
        }
    }

    /** {@inheritDoc} */
    public IIntegerPM getPresentationModel() {
        return pModel;
    }

    /** {@inheritDoc} */
    public void setPresentationModel(IIntegerPM pModel) {
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
        refreshMinMax();
        refreshTooltip();
        refreshValidationIndicator();
    }

    protected void refreshEditable() {
        if (pModel != null) {
            spinner.setEnabled(pModel.isEditable());
        } else {
            spinner.setEnabled(false);
        }
    }

    protected void refreshText() {
        if (pModel != null) {
            Integer pModelInteger = pModel.getInteger();
            int pModelInt = (pModelInteger == null ? 0 : pModelInteger.intValue());
            if (spinner.getSelection() != pModelInt) {
                spinner.setSelection(pModelInt);
            }
        } else {
            spinner.setSelection(0);
        }
    }

    private void refreshMinMax() {
        if (pModel != null) {
            spinner.setMinimum((int)((IIntegerPM)pModel).getMinValue());
            spinner.setMaximum((int)((IIntegerPM)pModel).getMaxValue());
        } else {
            spinner.setMinimum(Integer.MIN_VALUE);
            spinner.setMaximum(Integer.MAX_VALUE);
        }
    }

    protected void refreshTooltip() {
        if (pModel != null) {
            if (pModel.isValid() == false) {
                spinner.setToolTipText(pModel.getValidationState().getMessage());
            } else {
                spinner.setToolTipText(pModel.getDescription());
            }
        } else {
            spinner.setToolTipText(null);
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
