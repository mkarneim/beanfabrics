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
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.Options;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Michael Karneim
 */
public class BnComboDecorator extends AbstractDecorator<Combo> implements View<ITextPM>, ModelSubscriber {
    private final Link link = new Link(this);
    private final PropertyChangeListener listener = new WeakPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            refreshUI();
        }
    };

    private class MyModifyListener implements ModifyListener {
        public boolean enabled = true;

        public void modifyText(ModifyEvent evt) {
            if (enabled) {
                updateModel();
            }
        }
    };

    private final MyModifyListener uiListener = new MyModifyListener();

    private ITextPM pModel;
    private ValidationIndicator validationIndicator;
    private Combo combo;

    public BnComboDecorator(Combo combo) {
        super(combo);
        this.combo = combo;
        hookControl(combo);
    }

    public BnComboDecorator(Composite parent, int style) {
        this(new Combo(parent, style));
    }

    protected void hookControl(Combo combo) {
        Composite parent = combo.getParent();
        if (parent instanceof ValidationIndicator) {
            setValidationIndicator((ValidationIndicator)parent);
        }
        combo.addModifyListener(uiListener);
        refreshUI();
    }

    protected void updateModel() {
        if (pModel != null) {
            pModel.setText(combo.getText());
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
        uiListener.enabled = false;
        try {
            refreshEditable();
            refreshOptions();
            refreshText();
            refreshToolTip();
            refreshValidationIndicator();
        } finally {
            uiListener.enabled = true;
        }
    }

    protected void refreshEditable() {
        if (pModel != null) {
            combo.setEnabled(pModel.isEditable());
        } else {
            combo.setEnabled(false);
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

    protected void refreshToolTip() {
        if (pModel != null) {
            if (pModel.isValid() == false) {
                combo.setToolTipText(pModel.getValidationState().getMessage());
            } else {
                combo.setToolTipText(pModel.getDescription());
            }
        } else {
            combo.setToolTipText(null);
        }
    }

    protected void refreshText() {
        if (pModel != null) {
            final String textInModel = pModel.getText();
            if (!combo.getText().equals(textInModel)) {

                // if ((combo.getStyle() & SWT.READ_ONLY)!=0) {
                if (true) {
                    if (combo.indexOf(textInModel) == -1) {
                        combo.add(textInModel, 0);
                        combo.select(0);
                    }
                }
                combo.setText(textInModel);
            }
        } else {
            combo.setText("");
        }
    }

    protected void refreshOptions() {
        if (pModel != null) {
            Options<?> opts = pModel.getOptions();
            if (opts != null) {
                if (equals(combo.getItems(), opts.getValues()) == false) {
                    combo.remove(0, combo.getItemCount() - 1);
                    for (String value : opts.getValues()) {
                        combo.add(value);
                    }
                }
            } else {
                combo.removeAll();
            }
        } else {
            combo.removeAll();
        }
    }

    private boolean equals(String[] a, String[] b) {
        if (a == b) {
            return true;
        } else if (a == null || b == null) {
            return false;
        } else if (a.length != b.length) {
            return false;
        } else {
            for (int i = 0; i < a.length; ++i) {
                if (!equals(a[i], b[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean equals(String a, String b) {
        if (a == b) {
            return true;
        } else if (a == null) {
            return false;
        } else {
            return a.equals(b);
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
