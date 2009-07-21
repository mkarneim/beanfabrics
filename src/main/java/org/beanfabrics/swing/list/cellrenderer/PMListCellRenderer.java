/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.list.cellrenderer;

import javax.swing.ListCellRenderer;

import org.beanfabrics.model.PresentationModel;

/**
 * This interface has to be implemented by {@link ListCellRenderer
 * ListCellRenderers} which should be added to the {@link BnListCellRenderer} to
 * support a certain {@link PresentationModel}.
 * 
 * @author Max Gensthaler
 */
public interface PMListCellRenderer extends ListCellRenderer {
    /**
     * Returns if this list cell renderer supports a certain
     * {@link PresentationModel}.
     * 
     * @param pModel the {@link PresentationModel} to support
     * @return <code>true</code> if the given {@link PresentationModel} is
     *         supported, else <code>false</code>
     */
    public boolean supportsPresentationModel(PresentationModel pModel);
}