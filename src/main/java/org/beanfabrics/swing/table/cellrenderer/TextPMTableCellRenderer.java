/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.swing.table.cellrenderer;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.beanfabrics.model.IIconPM;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.ErrorImagePainter;

/**
 * The <code>TextPMTableCellRenderer</code> is a {@link TableCellRenderer} that renders a {@link ITextPM}.
 *
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class TextPMTableCellRenderer extends DefaultTableCellRenderer {
	private ITextPM model;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if ( value instanceof ITextPM) {
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		} else {
			return null;
		}
	}

	/**
	 * Sets the <code>String</code> object for the model being rendered to
	 * <code>value</code>.
	 *
	 * @param value
	 *            the value for this model; if value is <code>null</code> it
	 *            sets the text value to an empty string
	 */
	@Override
	protected void setValue(Object value) {
		model = (ITextPM) value;
		if (model != null) {
			setText(model.getText());
			setToolTipText(model.isValid() == false ? model.getValidationState().getMessage() : model.getDescription());
			if ( model instanceof IIconPM) {
				IIconPM iconCell = (IIconPM)model;
				this.setIcon( iconCell.getIcon());
			} else {
				this.setIcon(null);
			}
		} else {
			this.setText("");
			this.setToolTipText(null);
			if ( this.getIcon()!=null) {
				this.setIcon(null);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.paintErrorIcon(g);
	}

	/**
	 * Paints an error icon on top of the given {@link Graphics} if this
	 * component is connected to an {@link PresentationModel} and this <code>PresentationModel</code>
	 * has an invalid validation state.
	 *
	 * @param g
	 *            the <code>Graphics</code> to paint the error icon to
	 */
	protected void paintErrorIcon(Graphics g) {
		if (model != null && model.isValid() == false) {
			boolean isRightAligned = this.getHorizontalAlignment() == SwingConstants.RIGHT || this.getHorizontalAlignment() == SwingConstants.TRAILING;
			ErrorImagePainter.getInstance().paintTrailingErrorImage(g, this, isRightAligned);
		}
	}
}