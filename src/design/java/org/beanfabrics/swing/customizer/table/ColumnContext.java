/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.swing.customizer.table;

import org.beanfabrics.meta.PathInfo;
import org.beanfabrics.swing.table.BnColumn;
/**
 * 
 * @author Michael Karneim
 *
 */
public class ColumnContext {
	public final PathInfo elementRoot;
	public final BnColumn initialColumn;
	
	public ColumnContext(PathInfo rootNodeDescriptor, BnColumn initialColumn) {
		super();
		this.elementRoot = rootNodeDescriptor;
		this.initialColumn = initialColumn;
	}
	
}