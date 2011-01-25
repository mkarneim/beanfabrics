/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.list.cellrenderer;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import org.beanfabrics.model.IIconPM;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.ErrorIconPainter;

/**
 * The <code>TextPMListCellRenderer</code> is a {@link ListCellRenderer} that
 * renders a {@link ITextPM}.
 * 
 * @author Michael Karneim
 * @author Max Gensthaler
 */
@SuppressWarnings("serial")
public class TextPMListCellRenderer extends DefaultListCellRenderer implements PMListCellRenderer {
    private ITextPM model;
    private ErrorIconPainter errorIconPainter = createDefaultErrorIconPainter();

    public TextPMListCellRenderer() {
        setInheritsPopupMenu(true);
    }

    /** {@inheritDoc} */
    public boolean supportsPresentationModel(PresentationModel pModel) {
        return pModel instanceof ITextPM;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null && value instanceof ITextPM) {
            model = (ITextPM)value;
            setText(model.getText());
            setToolTipText(model.isValid() == false ? model.getValidationState().getMessage() : model.getDescription());
            if (model instanceof IIconPM) {
                IIconPM iconCell = (IIconPM)model;
                this.setIcon(iconCell.getIcon());
            } else {
                this.setIcon(null);
            }
        } else {
            setText("");
            setToolTipText(null);
            if (getIcon() != null) {
                setIcon(null);
            }
        }
        return this;
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
        if (model == null) {
            return false;
        }
        return (model.isValid() == false);
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