/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.context;

import java.util.List;

/**
 * @author Michael Karneim
 */
public interface Context {
    public void addParent(Context parent);

    public void removeParent(Context parent);

    public List<Context> getParents();

    public void addContextListener(ContextListener l);

    public void removeContextListener(ContextListener l);

    public <T> boolean addService(Class<? super T> type, T service);

    @SuppressWarnings("unchecked")
    public Object removeService(Class type);

    public List<ServiceEntry> getServiceEntries();

    @SuppressWarnings("unchecked")
    public ServiceEntry findService(Class type);

    public <T> T getService(Class<? extends T> type);
}