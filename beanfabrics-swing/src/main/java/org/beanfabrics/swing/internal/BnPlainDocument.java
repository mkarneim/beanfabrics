/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.internal;

import java.io.Serializable;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.util.ExceptionUtil;

/**
 * The <code>BnPlainDocument</code> is a {@link PlainDocument} which is a
 * {@link View} on a {@link ITextPM}.
 * 
 * @author Michael Karneim
 * @author Max Gensthaler
 */
@SuppressWarnings("serial")
public class BnPlainDocument extends PlainDocument implements View<ITextPM> {
    /**
     * Value <code>true</code> avoids event cycles between the document and the
     * {@link TextPM}.
     */
    private boolean pending_modelChange = false;
    /**
     * Setting this to <code>true</code> disables the delegation (to the model)
     * inside the {@link #remove(int, int)} method.
     */
    private boolean suppressRemoveEvent = false;

    protected ITextPM pModel;

    private final WeakPropertyChangeListener propertyListener = new MyWeakPropertyChangeListener();
    
    private class MyWeakPropertyChangeListener implements WeakPropertyChangeListener, Serializable {
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (pending_modelChange == false) { // avoid event cycle
                try {
                    pending_modelChange = true;
                    BnPlainDocument.this.refresh();
                } finally {
                    pending_modelChange = false;
                }
            }
        }
    }

    public BnPlainDocument() {
        super();
    }

    public BnPlainDocument(ITextPM pModel) {
        this();
        this.setPresentationModel(pModel);
    }

    /** {@inheritDoc} */
    public ITextPM getPresentationModel() {
        return this.pModel;
    }

    /** {@inheritDoc} */
    public void setPresentationModel(ITextPM pModel) {
        if (this.isConnected()) {
            this.pModel.removePropertyChangeListener("text", this.propertyListener);
        }
        this.pModel = pModel;
        if (pModel != null) {
            pModel.addPropertyChangeListener("text", this.propertyListener);
        }
        try {
            pending_modelChange = true;
            this.refresh();
        } finally {
            pending_modelChange = false;
        }
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
        return pModel != null;
    }

    private void disconnect() {
        if (this.isConnected()) {
            this.pModel.removePropertyChangeListener("text", this.propertyListener);
        }
    }

    private void connect() {
        if (this.pModel != null) {
            this.pModel.addPropertyChangeListener("text", this.propertyListener);
        }
    }

    /**
     * Configures this component depending on the target {@link AbstractPM}s
     * attributes.
     */
    protected void refresh() {
        try {
            String edText = this.isConnected() ? this.pModel.getText() : "";
            this.applyText(edText);
        } catch (BadLocationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void applyText(String text)
        throws BadLocationException {
        String oldText = this.getText(0, this.getLength());
        if (oldText.equals(text) == false) {
            this.suppressRemoveEvent = true; // do not synchronize
            try {
                // since'model.insertString' follows
                this.remove(0, this.getLength());
            } finally {
                this.suppressRemoveEvent = false;
            }
            this.insertString(0, text, null);
        }
    }

    public void remove(int offs, int len)
        throws BadLocationException {
        try {
            String edText = isConnected() ? this.pModel.getText() : "";
            super.remove(offs, len);
            String newText = this.getText(0, this.getLength());
            if (edText.equals(newText) == false) {
                // the removal to an empty string needs to be synchronized (no
                // model.insertString follows)
                if (suppressRemoveEvent == false) {
                    if (this.isConnected()) {
                        this.pModel.setText(newText);
                    }
                }
            }
        } catch (BadLocationException ex) {
            throw ex;
        } catch (Throwable t) {
            ExceptionUtil.getInstance().handleException("Error during editing.", t);
        }
    }

    public void insertString(int offs, String str, AttributeSet a)
        throws BadLocationException {
        if (this.isConnected()) {
            String edText = this.pModel.getText();
            super.insertString(offs, str, a);
            String newText = this.getText(0, this.getLength());
            if (edText.equals(newText) == false) {
                this.pModel.setText(newText);
            }
        }
    }

    public void setSuppressRemoveEvent(boolean suppressRemoveEvent) {
        this.suppressRemoveEvent = suppressRemoveEvent;
    }
}