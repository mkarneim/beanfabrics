/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.internal;

import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.Document;

import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.ErrorImagePainter;

/**
 * The <code>TextPMTextField</code> is a {@link JTextField} that is a view on an
 * {@link ITextPM}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class TextPMTextField extends JTextField implements View<ITextPM> {
    private boolean selectAllOnFocusGainedEnabled = true;
    private boolean reformatOnFocusLostEnabled = true;
    private BnPlainDocument document;
    private final PropertyChangeListener listener = new WeakPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            refresh();
        }
    };

    public TextPMTextField() {
        this.init();
    }

    public TextPMTextField(int columns) {
        super(columns);
        this.init();
    }

    public TextPMTextField(ITextPM pModel) {
        this.init();
        setPresentationModel(pModel);
    }

    private void init() {
        this.setEnabled(false);
        this.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(FocusEvent e) {
                TextPMTextField.this.onFocusGained();
            }

            public void focusLost(FocusEvent e) {
                TextPMTextField.this.onFocusLost();
            }
        });
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

    @Override
    protected Document createDefaultModel() {
        if (this.document != null && this.document.isConnected()) {
            throw new IllegalStateException("The document was initialized already.");
        }
        return this.document = new BnPlainDocument();
    }

    @Override
    public void setDocument(Document document) {
        if (this.isConnected()) {
            throw new IllegalStateException("The document was initialized already.");
        }
        if (document instanceof BnPlainDocument) {
            this.document = (BnPlainDocument)document;
        }
        super.setDocument(document);
    }

    /** {@inheritDoc} */
    public ITextPM getPresentationModel() {
        if (this.document == null) {
            return null;
        }
        return this.document.getPresentationModel();
    }

    /** {@inheritDoc} */
    public void setPresentationModel(ITextPM pModel) {
        if (this.isConnected()) {
            this.document.getPresentationModel().removePropertyChangeListener(this.listener);
        }
        this.document.setPresentationModel(pModel);
        if (pModel != null) {
            pModel.addPropertyChangeListener(listener);
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
        return this.document != null && this.document.getPresentationModel() != null;
    }

    /**
     * Configures the text field depending on the properties attributes.
     */
    protected void refresh() {
        final ITextPM pModel = this.getPresentationModel();
        if (pModel != null) {
            this.setEnabled(true);
            this.setToolTipText(pModel.isValid() == false ? pModel.getValidationState().getMessage() : pModel.getDescription());
            this.setEditable(pModel.isEditable());
        } else {
            this.setToolTipText(null);
            this.setEnabled(false);
        }
        this.repaint();
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
        ITextPM pModel = this.getPresentationModel();
        if (pModel != null && pModel.isValid() == false) {
            boolean isRightAligned = this.getHorizontalAlignment() == SwingConstants.RIGHT || this.getHorizontalAlignment() == SwingConstants.TRAILING;
            ErrorImagePainter.getInstance().paintTrailingErrorImage(g, this, isRightAligned);
        }
    }
}