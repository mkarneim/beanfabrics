/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.event;

import org.beanfabrics.model.Options;

/**
 * The listener interface for receiving {@link ListEvent}s.
 * <p>
 * A listener object created from this class can be registered with a
 * {@link Options} using the {@link Options#addOptionsListener(OptionsListener)}
 * method.
 * 
 * @author Michael Karneim
 */
public interface OptionsListener {
    /**
     * Invoked after some change has occured.
     * 
     * @param evt
     */
    public void changed(OptionsEvent evt);
}