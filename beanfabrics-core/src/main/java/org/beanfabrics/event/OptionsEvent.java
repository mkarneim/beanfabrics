/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.event;

import java.util.EventObject;

import org.beanfabrics.model.Options;

/**
 * An event which indicates that some change has occurred in some
 * {@link Options}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class OptionsEvent extends EventObject {
    /**
     * Consructs a new {@link OptionsEvent}.
     * 
     * @param source
     */
    public OptionsEvent(Options<?> source) {
        super(source);
    }
}