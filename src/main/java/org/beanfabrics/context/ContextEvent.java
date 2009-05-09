/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.context;

import java.util.EventObject;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ContextEvent extends EventObject {
	public ContextEvent(Context source) {
		super(source);
	}
}