/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.internal;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;

import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IBooleanPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.util.ExceptionUtil;

/**
 * The <code>BooleanPMMenuItem</code> is a {@link JCheckBoxMenuItem} that is a
 * view on an {@link IBooleanPM}.
 * 
 * @author Max Gensthaler
 */
// TODO unit test
@SuppressWarnings("serial")
public class BooleanPMMenuItem extends JCheckBoxMenuItem implements View<IBooleanPM> {
    private IBooleanPM pModel;

    private boolean iconSetManually = false;
    private boolean textSetManually = false;

    private transient final WeakPropertyChangeListener listener = new WeakPropertyChangeListener() {
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            refresh();
        }
    };

    public BooleanPMMenuItem() {
        super();
        init();
    }

    public BooleanPMMenuItem(IBooleanPM pModel) {
        this();
        setPresentationModel(pModel);
    }

    private void init() {
        setEnabled(false);
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

    @Override
    protected void fireActionPerformed(ActionEvent evt) {
        if (this.isConnected()) {
            try {
                this.execute();
                super.fireActionPerformed(evt);
            } catch (Throwable t) {
                ExceptionUtil.getInstance().handleException("Error during invocation of operation", t);
            }
        } else {
            super.fireActionPerformed(evt);
        }
    }

    protected void execute()
        throws Throwable {
        this.pModel.setBoolean(this.getState());
    }

    /**
     * Configures this component depending on the target {@link AbstractPM}s
     * attributes.
     */
    protected void refresh() {
        if (this.isConnected()) {
            try {
                final Boolean value = pModel.getBoolean();
                if (value != null) {
                    this.setSelected(value);
                }
            } catch (IllegalStateException ex) {
                // not a "boolean text"
                // -> keep the old state
            }
            final boolean isValid = this.pModel.isValid();
            this.setEnabled(isValid);
            this.setToolTipText(isValid ? this.pModel.getDescription() : this.pModel.getValidationState().getMessage());
            //			if (iconSetManually == false) {
            //				Icon icon = pModel.getIcon();
            //				super.setIcon(icon);
            //			}
            if (textSetManually == false) {
                String title = pModel.getTitle();
                if (title == null || title.length() == 0) {
                    title = "untitled operation";
                }
                super.setText(title);
            }
        } else {
            this.setSelected(false);
            this.setToolTipText(null);
            this.setEnabled(false);
            if (iconSetManually == false) {
                super.setIcon(null);
            }
            if (textSetManually == false) {
                super.setText(null);
            }
        }
    }

    @Override
    public void setIcon(Icon icon) {
        iconSetManually = (icon != null);
        super.setIcon(icon);
        refresh();
    }

    @Override
    public void setText(String text) {
        textSetManually = (text != null);
        super.setText(text);
        refresh();
    }

    @Override
    public synchronized void setState(boolean b) {
        super.setState(b);
        pModel.setBoolean(b);
    }
}