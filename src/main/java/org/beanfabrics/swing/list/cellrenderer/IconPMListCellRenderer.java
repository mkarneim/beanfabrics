/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
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
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.ErrorImagePainter;

/**
 * The <code>IconPMListCellRenderer</code> is a {@link ListCellRenderer} for an
 * {@link IIconPM}.
 * 
 * @author Michael Karneim
 * @author Max Gensthaler
 */
@SuppressWarnings("serial")
public class IconPMListCellRenderer extends DefaultListCellRenderer implements PMListCellRenderer {
    private IIconPM model;

    public IconPMListCellRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setInheritsPopupMenu(true);
    }

    /** {@inheritDoc} */
    public boolean supportsPresentationModel(PresentationModel pModel) {
        return pModel instanceof IIconPM;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        setText("");
        if (value != null && value instanceof IIconPM) {
            model = (IIconPM)value;
            setIcon(model.getIcon());
            setToolTipText(model.isValid() == false ? model.getValidationState().getMessage() : model.getDescription());
        } else {
            setIcon(null);
            setToolTipText(null);
        }
        return this;
    }

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
        if (model != null && model.isValid() == false) {
            boolean isRightAligned = this.getHorizontalAlignment() == SwingConstants.RIGHT || this.getHorizontalAlignment() == SwingConstants.TRAILING;
            ErrorImagePainter.getInstance().paintTrailingErrorImage(g, this, isRightAligned);
        }
    }
}