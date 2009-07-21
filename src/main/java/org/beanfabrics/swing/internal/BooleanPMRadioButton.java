/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.internal;

import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IBooleanPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.ErrorImagePainter;

/**
 * The <code>BooleanPMRadioButton</code> is a {@link JRadioButton} that is a
 * view on an {@link IBooleanPM}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BooleanPMRadioButton extends JRadioButton implements View<IBooleanPM> {
    private IBooleanPM pModel;

    private final PropertyChangeListener listener = new WeakPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            refresh();
        }
    };

    public BooleanPMRadioButton() {
        this.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (isConnected() == false)
                    return;
                final boolean newValue = isSelected();
                pModel.setBoolean(newValue);
            }
        });
    }

    public BooleanPMRadioButton(IBooleanPM pModel) {
        this();
        setPresentationModel(pModel);
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
    boolean isConnected() {
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
        } else {
            try {
                final Boolean value = pModel.getBoolean();
                if (value != null) {
                    this.setSelected(value);
                }
            } catch (IllegalStateException ex) {
                // not a "boolean text"
                // -> keep the old state
            }
            //TODO jdk 1.6 (rk)
            //			if (getModel() instanceof DefaultButtonModel) {
            //                final ButtonGroup group = (ButtonGroup)
            //                        ((DefaultButtonModel)getModel()).getGroup();
            //                if (group != null) {
            //                    if ( this.isSelected() && value == false)
            //                    	group.clearSelection();
            //                }
            //            }
            final String tooltip;
            if (pModel.isValid()) {
                tooltip = pModel.getDescription();
            } else {
                tooltip = pModel.getValidationState().getMessage();
            }
            this.setToolTipText(tooltip);
            this.setEnabled(true);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.paintErrorIcon(g);
    }

    /**
     * Paints an error icon on top of the given {@link Graphics} if this
     * component is connected to an {@link PresentationModel} and this
     * <code>PresentationModel</code> has an invalid validation state.
     * 
     * @param g the <code>Graphics</code> to paint the error icon to
     */
    protected void paintErrorIcon(Graphics g) {
        IBooleanPM pModel = this.getPresentationModel();
        if (pModel != null && pModel.isValid() == false) {
            boolean isRightAligned = this.getHorizontalAlignment() == SwingConstants.RIGHT || this.getHorizontalAlignment() == SwingConstants.TRAILING;
            ErrorImagePainter.getInstance().paintTrailingErrorImage(g, this, isRightAligned);
        }
    }
}