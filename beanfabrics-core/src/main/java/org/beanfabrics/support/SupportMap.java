/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.support;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael Karneim
 */
public class SupportMap {
    private final Supportable supportable;
    private final Map<Class<? extends Support>, Support> map = new HashMap<Class<? extends Support>, Support>();

    public SupportMap(Supportable supportable) {
        super();
        this.supportable = supportable;
    }

    public void put(Class<? extends Support> type, Support support) {
        map.put(type, support);
    }

    public <T extends Support> T get(Class<T> type) {
        return (T)map.get(type);
    }
}