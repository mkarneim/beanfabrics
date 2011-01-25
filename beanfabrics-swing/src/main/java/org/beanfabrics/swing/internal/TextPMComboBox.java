/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.internal;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.Options;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.BnComboBoxEditor;
import org.beanfabrics.swing.ErrorIconPainter;
import org.beanfabrics.swing.KeyBindingProcessor;

/**
 * The <code>TextPMComboBox</code> is a {@link JComboBox} that is a view on an
 * {@link ITextPM}.
 * 
 * @author Michael Karneim
 * @author Max Gensthaler
 */
@SuppressWarnings("serial")
public class TextPMComboBox extends JComboBox implements KeyBindingProcessor, View<ITextPM> {
    private static final Point DEFAULT_ERROR_ICON_OFFSET = new Point(-20, 0);
    private ITextPM pModel;
    private ErrorIconPainter errorIconPainter = createDefaultErrorIconPainter();

    private transient final PropertyChangeListener propertyListener = new WeakPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            refresh();
            if ("options".equals(evt.getPropertyName()))
                ((TextEditorComboBoxModel)getModel()).refresh(); // informs the textPM listeners (gui)
        }
    };
    private transient final ActionListener clearAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int items = getItemCount();
            for (int i = 0; i < items; ++i) {
                Object item = getItemAt(i);
                if ("".trim().equals(item)) {
                    setSelectedIndex(i);
                    break;
                }
            }
        }
    };

    public TextPMComboBox() {
        this.setEnabled(false);
        this.setModel(this.createDefaultModel());
        this.registerKeyboardAction(clearAction, KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), JComponent.WHEN_FOCUSED);
        this.registerKeyboardAction(clearAction, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_FOCUSED);
    }

    public TextPMComboBox(ITextPM pModel) {
        this();
        setPresentationModel(pModel);
    }

    /** {@inheritDoc} */
    public ITextPM getPresentationModel() {
        return pModel;
    }

    /** {@inheritDoc} */
    public void setPresentationModel(ITextPM pModel) {
        if (this.pModel != null) {
            this.pModel.removePropertyChangeListener(propertyListener);
        }
        this.pModel = pModel;
        if (this.pModel != null) {
            this.pModel.addPropertyChangeListener(propertyListener);
            this.setEditor(createBnComboBoxEditor());
        }
        refresh();
    }

    /**
     * Constructs a new {@link BnComboBoxEditor} for this
     * <code>BnComboBox</code>. This new <code>BnComboBoxEditor</code> is used
     * for editing the value of this <code>BnComboBox</code>.
     * 
     * @return a new {@link BnComboBoxEditor}
     */
    protected BnComboBoxEditor createBnComboBoxEditor() {
        return new BnComboBoxEditor(this);
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
     * Returns whether this component is connected to a PM that provides
     * {@link Options}.
     * 
     * @return <code>true</code> when this component has access to some
     *         {@link Options}
     */
    protected boolean hasOptions() {
        return isConnected() && pModel.getOptions() != null;
    }

    /**
     * Configures this component depending on the target {@link AbstractPM}s
     * attributes.
     */
    protected void refresh() {
        final ITextPM pModel = this.getPresentationModel();
        if (pModel != null) {
            this.setEnabled(pModel.isEditable());
            this.setToolTipText(pModel.isValid() == false ? pModel.getValidationState().getMessage() : pModel.getDescription());
        } else {
            this.setToolTipText(null);
            this.setEnabled(false);
        }
        this.repaint();
    }

    protected TextEditorComboBoxModel createDefaultModel() {
        return new TextEditorComboBoxModel();
    }

    private ErrorIconPainter createDefaultErrorIconPainter() {
        ErrorIconPainter result = new ErrorIconPainter();
        result.setOffset(DEFAULT_ERROR_ICON_OFFSET);
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
        if (this.isEditable()) {
            return false; // editable => error icon gets painted by BnComboBoxEditor
        }
        ITextPM pModel = this.getPresentationModel();
        if (pModel == null) {
            return false;
        }
        return (pModel.isValid() == false);
    }

    /** {@inheritDoc} */
    public boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        boolean result = super.processKeyBinding(ks, e, condition, pressed);
        if (result == false) {
            selectWithKeyChar(ks.getKeyChar());
        }
        return result;
    }

    protected class TextEditorComboBoxModel extends AbstractListModel implements ComboBoxModel {

        public Object getElementAt(int index) {
            if (isConnected() == false) {
                return null;
            }
            if (index < 0 || index >= pModel.getOptions().size()) {
                return null;
            }
            return pModel.getOptions().getValue(index);
        }

        public int getSize() {
            if (isConnected() == false)
                return 0;
            if (pModel.getOptions() == null)
                return 0;
            return pModel.getOptions().size();
        }

        public Object getSelectedItem() {
            if (isConnected() == false)
                return null;
            return pModel.getText();
        }

        public void setSelectedItem(Object anItem) {
            if (anItem == null && this.getSelectedItem() == null)
                return;
            if (anItem != null && anItem.equals(this.getSelectedItem())) {
                return;
            }
            pModel.setText(anItem != null ? anItem.toString() : "");
            this.fireContentsChanged(this, -1, -1);
        }

        public void refresh() {
            this.fireContentsChanged(this, -1, -1);
        }
    }
}