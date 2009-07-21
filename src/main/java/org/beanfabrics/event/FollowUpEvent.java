/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.event;

import java.util.EventObject;

public interface FollowUpEvent extends Iterable<EventObject> {
    public EventObject getCause();
}