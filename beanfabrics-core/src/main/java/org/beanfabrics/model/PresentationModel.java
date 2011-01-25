/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import org.beanfabrics.Bean;
import org.beanfabrics.HasComparable;
import org.beanfabrics.context.ContextOwner;
import org.beanfabrics.support.Supportable;
import org.beanfabrics.validation.Validatable;

/**
 * The {@link PresentationModel} is the basic interface for a presentation model
 * (PM).
 * <p>
 * A PM is a tree-like combination of PM components that represent the state of
 * an application's user interface independent from a concrete GUI framework
 * like Swing or SWT.
 * 
 * @author Michael Karneim
 */
public interface PresentationModel extends Bean, Validatable, Supportable, ContextOwner, HasComparable {

}