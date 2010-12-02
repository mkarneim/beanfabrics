/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.context;

import java.util.List;

/**
 * A {@link Context} is an hierarchical environment for service objects.
 * <p>
 * Call {@link #addService(Class, Object)} to put a service object into this
 * context. Call {@link #getService(Class)} or {@link #findService(Class)} to
 * receive a reference to a service.
 * <p>
 * A context can be part of Zero-to-Many parents, and can itself be parent of
 * Zero-to-Many children. Any service made available for a parent is also
 * available for all children but not vice-versa.
 * 
 * @author Michael Karneim
 */
public interface Context {
    /**
     * Adds the given {@link Context} as a parent to the this context.
     * 
     * @param parent
     */
    public void addParent(Context parent);

    /**
     * Removes the given parent {@link Context} from this context.
     * 
     * @param parent
     */
    public void removeParent(Context parent);

    /**
     * Returns a list of all parents.
     * 
     * @return a list of all parents
     */
    public List<Context> getParents();

    /**
     * Add the given {@link ContextListener}.
     * 
     * @param l
     */
    public void addContextListener(ContextListener l);

    /**
     * Remove the given {@link ContextListener}.
     * 
     * @param l
     */
    public void removeContextListener(ContextListener l);

    /**
     * Place the given service into this {@link Context} and make it available
     * for clients.
     * 
     * @param <T> the generic type of the service
     * @param type the type of the service. Clients will find the service by
     *            using this type.
     * @param service the service implementation
     * @return <code>true</code>, if the service has been added sucessfully
     */
    public <T> boolean addService(Class<? super T> type, T service);

    /**
     * Removes the first service of the given type.
     * 
     * @param type
     * @return the service that has been removed, or <code>null</code>
     *         otherwise.
     */
    public <T> T removeService(Class<? extends T> type);

    /**
     * Returns a list of all service entries that are available in this context.
     * This includes services that are placed into this service directly as well
     * as services that are placed into any of its parents.
     * 
     * @return a list of all service entries
     */
    public List<ServiceEntry> getServiceEntries();

    /**
     * Finds and returns the first service entry in this context that matches
     * the given type. If more than one service is available, this method
     * returns the nearest entry whereby distance is defined as the number of
     * hierarchical steps between this context and the context the service was
     * originally placed into. If more services have the same distance this
     * method returns that entry that was added first.
     * 
     * @param type
     * @return the first service entry that matches the given type
     */
    public ServiceEntry findService(Class<?> type);

    /**
     * Finds and returns the first service implementation in this context that
     * matches the given type. This method is a shortcut of
     * {@link #findService(Class)}. It returns the same result as
     * <code>ctx.findService(aType).getService()</code>.
     * 
     * @param <T>
     * @param type
     * @return the first service implementation that matches the given type
     */
    public <T> T getService(Class<? extends T> type);
}