/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swt;

import org.beanfabrics.validation.ValidationState;

/**
 * @author Michael Karneim
 */
public interface ValidationIndicator {
    public void setValidationState(ValidationState vState);
}
