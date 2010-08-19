/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.internal;

import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.ConversionException;
import org.beanfabrics.model.IBooleanPM;
import org.beanfabrics.model.IValuePM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.ErrorIconPainter;

/**
 * The <code>BooleanPMCheckBox</code> is a {@link JCheckBox} that is a view on
 * an {@link IBooleanPM}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BooleanPMCheckBox extends JCheckBox implements View<IBooleanPM> {
    private IBooleanPM pModel;
    private transient final WeakPropertyChangeListener listener = new WeakPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            refresh();
        }
    };
    private ErrorIconPainter errorIconPainter = createDefaultErrorIconPainter();

    public BooleanPMCheckBox() {
        this.setEnabled(false);
        this.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (isConnected() == false)
                    return;
                final boolean newValue = isSelected();
                pModel.setBoolean(newValue);
            }
        });
    }

    public BooleanPMCheckBox(IBooleanPM pModel) {
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
        this.pModel = null;
        // invoke 'refresh' in between in order to clear old attributes
        // this is necessary for correct display in tables
        this.refresh();
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
            this.setEnabled(pModel.isEditable());
        }
    }

    private ErrorIconPainter createDefaultErrorIconPainter() {
        ErrorIconPainter result = new ErrorIconPainter();
        result.setHorizontalAlignment(invertHorizontalTextPosition(getHorizontalTextPosition()));
        return result;
    }

    public ErrorIconPainter getErrorIconPainter() {
        return errorIconPainter;
    }

    public void setErrorIconPainter(ErrorIconPainter aErrorIconPainter) {
        if (aErrorIconPainter == null) {
            throw new IllegalArgumentException("aErrorIconPainter == null");
        }
        this.errorIconPainter = aErrorIconPainter;
    }

    /** {@inheritDoc} */
    @Override
    public void paintChildren(Graphics g) {
        super.paintChildren(g);
        if (shouldPaintErrorIcon()) {
            errorIconPainter.paint(g, this);
        }
    }

    private boolean shouldPaintErrorIcon() {
        IValuePM pModel = this.getPresentationModel();
        if (pModel == null) {
            return false;
        }
        return (pModel.isValid() == false);
    }

    @Override
    public void setHorizontalTextPosition(int textPosition) {
        super.setHorizontalTextPosition(textPosition);
        if (errorIconPainter != null) {
            this.errorIconPainter.setHorizontalAlignment(invertHorizontalTextPosition(textPosition));
        }
    }

    private int invertHorizontalTextPosition(int textPosition) {
        switch (textPosition) {
            case SwingConstants.LEFT:
                return SwingConstants.RIGHT;
            case SwingConstants.RIGHT:
                return SwingConstants.LEFT;
            case SwingConstants.LEADING:
                return SwingConstants.TRAILING;
            case SwingConstants.TRAILING:
                return SwingConstants.LEADING;
            default:
                return textPosition;
        }
    }

}