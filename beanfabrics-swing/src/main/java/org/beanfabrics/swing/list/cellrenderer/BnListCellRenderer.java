/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.list.cellrenderer;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.list.BnList;

/**
 * The <code>BnListCellRenderer</code> is the general {@link ListCellRenderer}
 * for the {@link BnList}.
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
 * <p>
 * For an example about using BnListCellRenderer, please see <a
 * href="http://www.beanfabrics.org/index.php/BnListCellRenderer"
 * target="parent">http://www.beanfabrics.org/index.php/BnListCellRenderer</a>
 * </p>
 * 
 * @author Michael Karneim
 */
public class BnListCellRenderer implements ListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof PresentationModel) {
            for (PMListCellRenderer renderer : installedRenderers) {
                if (renderer.supportsPresentationModel((PresentationModel)value)) {
                    return renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                }
            }
        }
        return FALLBACK_RENDERER.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }

    private final ListCellRenderer FALLBACK_RENDERER = new EmptyRenderer();
    private final List<PMListCellRenderer> installedRenderers = new ArrayList<PMListCellRenderer>();

    public BnListCellRenderer() {
        installDefaultRenderers();
    }

    private void installDefaultRenderers() {
        // TODO (mk) I think a checkbox style renderer makes no sense here since
        // we can't click on it to change the value
        // installedRenderers.add( new BooleanPMListCellRenderer());
        installedRenderers.add(new TextPMListCellRenderer());
        installedRenderers.add(new IconPMListCellRenderer());
    }

    public List<PMListCellRenderer> getInstalledRenderers() {
        return this.installedRenderers;
    }
}