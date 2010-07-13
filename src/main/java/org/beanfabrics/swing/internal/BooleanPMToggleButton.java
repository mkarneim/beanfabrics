/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.internal;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JToggleButton;

import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.ConversionException;
import org.beanfabrics.model.IBooleanPM;
import org.beanfabrics.model.PresentationModel;

/**
 * The <code>BooleanPMToggleButton</code> is a {@link JToggleButton} that is a
 * view on an {@link IBooleanPM}.
 * 
 * @author Max Gensthaler
 */
@SuppressWarnings("serial")
public class BooleanPMToggleButton extends JToggleButton implements View<IBooleanPM> {
    private IBooleanPM pModel;
    private boolean textSetManually = false;

    private transient final PropertyChangeListener listener = new WeakPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            refresh();
        }
    };

    public BooleanPMToggleButton() {
        this.setEnabled(false);
        this.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (isConnected())
                    pModel.setBoolean(isSelected());
            }
        });
    }

    public BooleanPMToggleButton(IBooleanPM pModel) {
        this();
        setPresentationModel(pModel);
    }

    @Override
    public void setText(String text) {
        textSetManually = (text != null);
        super.setText(text);
        refresh();
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
        this.refresh();
    }

    /**
     * Returns whether this component is connected to the target
     * {@link PresentationModel} to synchronize with. This is a convenience
     * method.
     * 
     * @return <code>true</code> when this component is connected, else
     *         <code>false</code>
     */
    public boolean isConnected() {
        return this.pModel != null;
    }

    /**
     * Configures this component depending on the target {@link AbstractPM}s
     * attributes.
     */
    private void refresh() {
        if (pModel == null) {
            this.setSelected(false);
            this.setToolTipText(null);
            this.setEnabled(false);
            if (textSetManually == false) {
                super.setText(null);
            }
        } else {
            try {
                final Boolean value = pModel.getBoolean();
                if (value != null) {
                    this.setSelected(value);
                }
            } catch (ConversionException ex) {
                // not a "boolean text"
                // -> keep the old state
            }
            final String tooltip;
            if (pModel.isValid()) {
                tooltip = pModel.getDescription();
            } else {
                tooltip = pModel.getValidationState().getMessage();
            }
            if (textSetManually == false) {
                String title = pModel.getTitle();
                super.setText(title);
            }
            this.setToolTipText(tooltip);
            this.setEnabled(true);
        }
    }
}