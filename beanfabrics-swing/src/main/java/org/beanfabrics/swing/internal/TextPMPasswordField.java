/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.internal;

import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.text.Document;

import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.ErrorIconPainter;

/**
 * The <code>TextPMPasswordField</code> is a view on an {@link ITextPM}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class TextPMPasswordField extends JPasswordField implements View<ITextPM> {
    private boolean selectAllOnFocusGainedEnabled = true;
    private boolean reformatOnFocusLostEnabled = true;
    private BnPlainDocument document;
    private final PropertyChangeListener listener = new MyWeakPropertyChangeListener();

    private class MyWeakPropertyChangeListener implements WeakPropertyChangeListener, Serializable {
        public void propertyChange(PropertyChangeEvent evt) {
            refresh();
        }
    }

    private ErrorIconPainter errorIconPainter = createDefaultErrorIconPainter();

    /**
	 *
	 */
    public TextPMPasswordField() {
        this.init();
    }

    public TextPMPasswordField(ITextPM pModel) {
        this();
        setPresentationModel(pModel);
    }

    public boolean isSelectAllOnFocusGainedEnabled() {
        return selectAllOnFocusGainedEnabled;
    }

    public void setSelectAllOnFocusGainedEnabled(boolean selectAllOnFocusGainedEnabled) {
        this.selectAllOnFocusGainedEnabled = selectAllOnFocusGainedEnabled;
    }

    public boolean isReformatOnFocusLostEnabled() {
        return reformatOnFocusLostEnabled;
    }

    public void setReformatOnFocusLostEnabled(boolean reformatOnFocusLostEnabled) {
        this.reformatOnFocusLostEnabled = reformatOnFocusLostEnabled;
    }

    private void init() {
        this.setEnabled(false);
        this.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(FocusEvent e) {
                TextPMPasswordField.this.onFocusGained();
            }

            public void focusLost(FocusEvent e) {
                TextPMPasswordField.this.onFocusLost();
            }
        });
    }

    protected void onFocusGained() {
        this.repaint();
        if (this.isSelectAllOnFocusGainedEnabled()) {
            this.selectAll();
        }
    }

    protected void onFocusLost() {
        this.repaint();
        if (this.isReformatOnFocusLostEnabled() && this.isConnected()) {
            this.document.getPresentationModel().reformat();
        }
    }

    protected Document createDefaultModel() {
        if (this.document != null && this.document.isConnected())
            throw new IllegalStateException("The document was initialized already.");
        return this.document = new BnPlainDocument();
    }

    public void setDocument(Document document) {
        if (this.isConnected())
            throw new IllegalStateException("The document was initialized already.");
        if (document instanceof BnPlainDocument)
            this.document = (BnPlainDocument) document;
        super.setDocument(document);
    }

    /** {@inheritDoc} */
    public ITextPM getPresentationModel() {
        if (this.document == null)
            return null;
        return this.document.getPresentationModel();
    }

    /** {@inheritDoc} */
    public void setPresentationModel(ITextPM newModel) {
        ITextPM oldModel = this.document.getPresentationModel();
        if (oldModel != null) {
            oldModel.removePropertyChangeListener(this.listener);
        }
        this.document.setPresentationModel(newModel);
        if (newModel != null) {
            newModel.addPropertyChangeListener(listener);
        }
        this.refresh();
        this.firePropertyChange("presentationModel", oldModel, newModel);
    }

    /**
     * Returns whether this component is connected to the target {@link PresentationModel} to synchronize with. This is
     * a convenience method.
     * 
     * @return <code>true</code> when this component is connected, else <code>false</code>
     */
    boolean isConnected() {
        return this.document != null && this.document.getPresentationModel() != null;
    }

    /**
     * Configures the text field depending on the properties attributes
     */
    protected void refresh() {
        final ITextPM pModel = this.getPresentationModel();
        if (pModel != null) {
            this.setEnabled(true);
            this.setToolTipText(pModel.isValid() == false ? pModel.getValidationState().getMessage() : pModel
                    .getDescription());
            this.setEditable(pModel.isEditable());
        } else {
            this.setEnabled(false);
        }
        this.repaint();
    }

    private ErrorIconPainter createDefaultErrorIconPainter() {
        ErrorIconPainter result = new ErrorIconPainter();
        result.setHorizontalAlignment(invertHorizontalAlignment(getHorizontalAlignment()));
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
        ITextPM pModel = this.getPresentationModel();
        if (pModel == null) {
            return false;
        }
        return (pModel.isValid() == false);
    }

    @Override
    public void setHorizontalAlignment(int alignment) {
        super.setHorizontalAlignment(alignment);
        if (errorIconPainter != null) {
            this.errorIconPainter.setHorizontalAlignment(invertHorizontalAlignment(alignment));
        }
    }

    private int invertHorizontalAlignment(int alignment) {
        switch (alignment) {
        case SwingConstants.LEFT:
            return SwingConstants.RIGHT;
        case SwingConstants.RIGHT:
            return SwingConstants.LEFT;
        case SwingConstants.LEADING:
            return SwingConstants.TRAILING;
        case SwingConstants.TRAILING:
            return SwingConstants.LEADING;
        default:
            return alignment;
        }
    }
}