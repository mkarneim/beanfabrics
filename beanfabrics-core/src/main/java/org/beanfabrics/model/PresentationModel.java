/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import org.beanfabrics.Bean;
import org.beanfabrics.context.ContextOwner;
import org.beanfabrics.support.Supportable;
import org.beanfabrics.validation.Validatable;

/**
 * Base interface for a presentation model.
 * 
 * @author Michael Karneim
 */
public interface PresentationModel extends Bean, Validatable, Supportable, ContextOwner {

}