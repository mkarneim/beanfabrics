/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.swing.table.cellrenderer;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * The <code>EmptyRenderer</code> is a {@link TableCellRenderer} that renders an empty area.
 *
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class EmptyRenderer extends DefaultTableCellRenderer {
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		return super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
	}
}