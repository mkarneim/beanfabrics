/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.support;

import org.beanfabrics.model.PresentationModel;

/**
 * @author Michael Karneim
 */
interface PropertiesListener {
    void changed(String name, PresentationModel oldValue, PresentationModel newValue);
}
