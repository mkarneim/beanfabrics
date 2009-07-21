/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.event;

/**
 * @author Michael Karneim
 */
public interface ModelProviderListener {
    public void modelGained(ModelProviderEvent evt);

    public void modelLost(ModelProviderEvent evt);
}