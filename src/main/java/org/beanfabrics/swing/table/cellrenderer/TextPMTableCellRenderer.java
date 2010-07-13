/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
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
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.swing.ErrorIconPainter;
import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.swing.table.BnTable;

/**
 * The <code>TextPMTableCellRenderer</code> is a {@link TableCellRenderer} that
 * renders a {@link ITextPM}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class TextPMTableCellRenderer extends DefaultTableCellRenderer {
    private ITextPM model;
    private BnColumn column;
    private ErrorIconPainter errorIconPainter = createDefaultErrorIconPainter();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof ITextPM) {
            this.column = getBnColumn(table, column);
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        } else {
            return null;
        }
    }

    private BnColumn getBnColumn(JTable table, int columnIndex) {
        if (table instanceof BnTable) {
            BnTable bnTable = (BnTable)table;
            return bnTable.getColumns()[bnTable.convertColumnIndexToModel(columnIndex)];
        }
        return null;
    }

    /**
     * Sets the <code>String</code> object for the model being rendered to
     * <code>value</code>.
     * 
     * @param value the value for this model; if value is <code>null</code> it
     *            sets the text value to an empty string
     */
    @Override
    protected void setValue(Object value) {
        model = (ITextPM)value;
        if (model != null) {
            setText(model.getText());
            setToolTipText(model.isValid() == false ? model.getValidationState().getMessage() : model.getDescription());
            if (model instanceof IIconPM) {
                IIconPM iconCell = (IIconPM)model;
                this.setIcon(iconCell.getIcon());
            } else {
                this.setIcon(null);
            }

            if (column != null) {
                if (column.getAlignment() != null) {
                    this.setHorizontalAlignment(column.getAlignment());
                } else {
                    this.setHorizontalAlignment(SwingConstants.LEADING);
                }
            }
        } else {
            this.setText("");
            this.setToolTipText(null);
            this.setHorizontalAlignment(SwingConstants.LEADING);
            if (this.getIcon() != null) {
                this.setIcon(null);
            }
        }
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