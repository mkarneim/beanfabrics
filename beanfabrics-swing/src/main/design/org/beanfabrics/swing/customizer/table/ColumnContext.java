/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

import org.beanfabrics.meta.PathElementInfo;
import org.beanfabrics.swing.table.BnColumn;

/**
 * @author Michael Karneim
 */
public class ColumnContext {
    public final PathElementInfo rootPathElementInfo;
    public final BnColumn initialColumn;

    public ColumnContext(PathElementInfo aRootPathElementInfo, BnColumn aInitialColumn) {
        super();
        this.rootPathElementInfo = aRootPathElementInfo;
        this.initialColumn = aInitialColumn;
    }

}