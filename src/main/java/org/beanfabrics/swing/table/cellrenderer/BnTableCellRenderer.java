/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.table.cellrenderer;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.beanfabrics.swing.table.BnTable;

/**
 * The <code>BnTableCellRenderer</code> is the general {@link TableCellRenderer}
 * for the {@link BnTable}.
 * <p>
 * It delegates calls of
 * {@link #getListCellRendererComponent(JList, Object, int, boolean, boolean)}
 * to each of it's installed renderers (see {@link #getInstalledRenderers()})
 * until it receives a not-<code>null</code> result.
 * </p>
 * <p>
 * If no renderer component is found at all it returns an {@link EmptyRenderer}
 * as fallback.
 * </p>
 * <p>
 * To install a custom renderer just add it to the list of installed renderers.
 * </p>
 * 
 * @author Michael Karneim
 */
public class BnTableCellRenderer implements TableCellRenderer {
    private final TableCellRenderer FALLBACK_RENDERER = new EmptyRenderer();
    private final List<TableCellRenderer> installedRenderers = new ArrayList<TableCellRenderer>();

    public BnTableCellRenderer() {
        installDefaultRenderers();
    }

    /** {@inheritDoc} */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        for (TableCellRenderer renderer : installedRenderers) {
            Component comp = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (comp != null) {
                return comp;
            }
        }
        return FALLBACK_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

    private void installDefaultRenderers() {
        installedRenderers.add(new BooleanPMTableCellRenderer());
        installedRenderers.add(new TextPMTableCellRenderer());
        installedRenderers.add(new IconPMTableCellRenderer());
    }

    public List<TableCellRenderer> getInstalledRenderers() {
        return installedRenderers;
    }
}