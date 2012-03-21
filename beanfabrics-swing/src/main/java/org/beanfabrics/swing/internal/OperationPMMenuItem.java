/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.internal;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JMenuItem;

import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.util.ExceptionUtil;

/**
 * The <code>OperationPMMenuItem</code> is a {@link JMenuItem} that is a view on
 * an {@link IOperationPM}.
 * 
 * @author Max Gensthaler
 */
@SuppressWarnings("serial")
public class OperationPMMenuItem extends JMenuItem implements View<IOperationPM> {
    private IOperationPM pModel;

    private boolean autoExecute = true;

    private boolean iconSetManually = false;
    private boolean textSetManually = false;

    private transient final WeakPropertyChangeListener listener = new WeakPropertyChangeListener() {
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            refresh();
        }
    };

    public OperationPMMenuItem() {
        super();
        init();
    }

    public OperationPMMenuItem(IOperationPM pModel) {
        this();
        setPresentationModel(pModel);
    }

    private void init() {
        setEnabled(false);
    }

    /** {@inheritDoc} */
    public IOperationPM getPresentationModel() {
        return pModel;
    }

    /** {@inheritDoc} */
    public void setPresentationModel(IOperationPM newModel) {
        IOperationPM oldModel = this.pModel;
        if (this.isConnected()) {
            this.pModel.removePropertyChangeListener(this.listener);
        }
        this.pModel = newModel;
        if (newModel != null) {
            this.pModel.addPropertyChangeListener(this.listener);
        }
        this.refresh();
        this.firePropertyChange("presentationModel", oldModel, newModel);
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
        if (this.isConnected() && this.isAutoExecute()) {
            try {
            	boolean doContinue = this.execute();
                if ( doContinue) {
                	super.fireActionPerformed(evt);
                }
            } catch (Throwable t) {
                ExceptionUtil.getInstance().handleException("Error during invocation of operation", t);
            }
        } else {
            super.fireActionPerformed(evt);
        }
    }

    protected boolean execute()
        throws Throwable {
        return this.pModel.execute();
    }

    /**
     * Configures this component depending on the target {@link AbstractPM}s
     * attributes.
     */
    protected void refresh() {
        if (this.isConnected()) {
            final boolean isValid = this.pModel.isValid();
            this.setEnabled(isValid);
            this.setToolTipText(isValid ? this.pModel.getDescription() : this.pModel.getValidationState().getMessage());
            if (iconSetManually == false) {
                Icon icon = pModel.getIcon();
                super.setIcon(icon);
            }
            if (textSetManually == false) {
                String title = pModel.getTitle();
                if (title == null || title.length() == 0) {
                    title = "untitled operation";
                }
                super.setText(title);
            }
        } else {
            this.setEnabled(false);
            this.setToolTipText(null);
            if (iconSetManually == false) {
                super.setIcon(null);
            }
            if (textSetManually == false) {
                super.setText(null);
            }
        }
    }

    public boolean isAutoExecute() {
        return autoExecute;
    }

    public void setAutoExecute(boolean autoExecute) {
        this.autoExecute = autoExecute;
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
}