/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.table.cellrenderer;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.beanfabrics.model.IIconPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.ErrorImagePainter;

/**
 * The <code>IconPMTableCellRenderer</code> is a {@link TableCellRenderer} for
 * an {@link IIconPM}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class IconPMTableCellRenderer extends DefaultTableCellRenderer {
    private IIconPM model;

    public IconPMTableCellRenderer() {
        super();
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof IIconPM) {
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        } else {
            return null;
        }
    }

    @Override
    protected void setValue(Object value) {
        if (value != null && value instanceof IIconPM) {
            model = (IIconPM)value;
            setIcon(model.getIcon());
            setToolTipText(model.isValid() == false ? model.getValidationState().getMessage() : model.getDescription());
        } else {
            model = null;
            setIcon(null);
            setToolTipText(null);
        }
        setInheritsPopupMenu(true);
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