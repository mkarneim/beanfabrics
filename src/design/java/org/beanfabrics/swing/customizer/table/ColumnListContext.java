/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

import org.beanfabrics.meta.PathInfo;
import org.beanfabrics.swing.table.BnColumn;

/**
 * @author Michael Karneim
 */
public class ColumnListContext {
    public final PathInfo elementRoot;
    public final BnColumn[] initialColumns;

    public ColumnListContext(PathInfo elementRoot, BnColumn[] initialColumns) {
        super();
        this.elementRoot = elementRoot;
        this.initialColumns = initialColumns;
    }

}