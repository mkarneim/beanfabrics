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
public class ColumnListContext {
    public final PathElementInfo rootPathElementInfo;
    public final BnColumn[] initialColumns;

    public ColumnListContext(PathElementInfo aRootPathElementInfo, BnColumn[] aInitialColumns) {
        super();
        this.rootPathElementInfo = aRootPathElementInfo;
        this.initialColumns = aInitialColumns;
    }

}