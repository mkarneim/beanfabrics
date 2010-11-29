/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.context;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.beanfabrics.log.Logger;
import org.beanfabrics.log.LoggerFactory;

/**
 * The {@link DefaultContext} is the default implementation of the
 * {@link Context} interface.
 * 
 * @author Michael Karneim
 */
public class DefaultContext implements Context {
    private final static Logger LOG = LoggerFactory.getLogger(DefaultContext.class);

    private ContextListener parentListener;
    private final CopyOnWriteArrayList<ContextListener> contextListeners = new CopyOnWriteArrayList<ContextListener>();;
    private final List<ServiceEntry> serviceEntries = new LinkedList<ServiceEntry>();
    private final List<ServiceEntry> unmodifiableServiceEntries;
    private final List<Context> parents;
    private final List<Context> unmodifiableParents;

    /**
     * Constructs a {@link DefaultContext}.
     */
    public DefaultContext() {
        parents = new LinkedList<Context>();
        parentListener = new ContextListener() {
            public void serviceRemoved(ServiceRemovedEvent evt) {
                removeServiceEntry(evt.getServiceEntry().getOrigin(), evt.getServiceEntry().getType());
            }

            public void serviceAdded(ServiceAddedEvent evt) {
                ServiceEntry entry = evt.getServiceEntry();
                addServiceEntry(new ServiceEntry(entry.getDistance() + 1, entry.getOrigin(), entry.getService(), entry.getType()));
            }

            public void parentRemoved(ParentRemovedEvent evt) {
                // nothing to do
            }

            public void parentAdded(ParentAddedEvent evt) {
                // nothing to do
            }
        };
        unmodifiableParents = Collections.unmodifiableList(parents);
        unmodifiableServiceEntries = Collections.unmodifiableList(serviceEntries);
    }

    /** {@inheritDoc} */
    public List<Context> getParents() {
        return unmodifiableParents;
    }

    /** {@inheritDoc} */
    public synchronized void addParent(Context parent) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("adding parent " + parent + " to " + this);
        }
        parent.addContextListener(this.parentListener);
        parents.add(parent);
        for (ServiceEntry entry : parent.getServiceEntries()) {
            addServiceEntry(new ServiceEntry(entry.getDistance() + 1, entry.getOrigin(), entry.getService(), entry.getType()));
        }
        this.fireParentAdded(parent);
    }

    /** {@inheritDoc} */
    public synchronized void removeParent(Context parent) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("removing parent " + parent + " from " + this);
        }
        parent.removeContextListener(this.parentListener);
        parents.remove(parent);
        for (ServiceEntry entry : parent.getServiceEntries()) {
            removeServiceEntry(entry.getOrigin(), entry.getType());
        }
        this.fireParentRemoved(parent);
    }

    /** {@inheritDoc} */
    public void addContextListener(ContextListener l) {
        if (l == null) {
            throw new IllegalArgumentException("l must not be null.");
        }
        contextListeners.add(l);
    }

    /** {@inheritDoc} */
    public void removeContextListener(ContextListener l) {
        if (l == null) {
            throw new IllegalArgumentException("l must not be null.");
        }
        contextListeners.remove(l);
    }

    /**
     * Fires a parentAdded-event to each registered listener.
     * 
     * @param parent the parent this context has been added to
     */
    protected void fireParentAdded(Context parent) {
        ParentAddedEvent evt = new ParentAddedEvent(this, parent);
        for (ContextListener l : contextListeners) {
            l.parentAdded(evt);
        }
    }

    /**
     * Fires a parentRemoved-event to each registered listener.
     * 
     * @param parent the parent this context has been removed from.
     */
    protected void fireParentRemoved(Context parent) {
        ParentRemovedEvent evt = new ParentRemovedEvent(this, parent);
        for (ContextListener l : contextListeners) {
            l.parentRemoved(evt);
        }
    }

    /**
     * Fires a service-added-event to each registered listener.
     * 
     * @param entry the service entry that has been added to this context
     */
    protected void fireServiceAdded(ServiceEntry entry) {
        ServiceAddedEvent evt = new ServiceAddedEvent(this, entry);
        for (ContextListener l : contextListeners) {
            l.serviceAdded(evt);
        }
    }

    /**
     * Fires a service-removed-event to each registered listener.
     * 
     * @param entry the service entry that has been removed from this context
     */
    protected void fireServiceRemoved(ServiceEntry entry) {
        ServiceRemovedEvent evt = new ServiceRemovedEvent(this, entry);
        for (ContextListener l : contextListeners) {
            l.serviceRemoved(evt);
        }
    }

    /** {@inheritDoc} */
    public <T> boolean addService(Class<? super T> type, T service) {
        ServiceEntry entry = new ServiceEntry(0, this, service, type);
        return addServiceEntry(entry);
    }

    /**
     * Adds the given entry to the list of services available in this context.
     * 
     * @param entry
     * @return <code>true</code>, if the entry was added sucessfully
     */
    private boolean addServiceEntry(ServiceEntry entry) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("adding service '" + entry.getType().getName() + "' to " + this);
        }
        boolean result = serviceEntries.add(entry);
        fireServiceAdded(entry);
        return result;
    }

    /**
     * Removes the first service entry from this context that matches the given
     * type and origially was placed into the given "origin" context.
     * 
     * @param origin
     * @param type
     * @return the removed entry
     */
    protected ServiceEntry removeServiceEntry(Context origin, Class<?> type) {
        Iterator<ServiceEntry> it = serviceEntries.iterator();
        while (it.hasNext()) {
            ServiceEntry entry = it.next();
            if (entry.getType().equals(type) && entry.getOrigin() == origin) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("removing service '" + entry.getType().getName() + "' from " + this);
                }
                it.remove();

                fireServiceRemoved(entry);
                return entry;
            }
        }
        // no service with that type and origin found.
        // -> nothing to do
        return null;
        // throw new IllegalArgumentException("Service with type='" +type.getName() + "' not found");
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public <T> T removeService(Class<? extends T> type) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("removeService(" + type.getName() + ")");
        }
        ServiceEntry result = removeServiceEntry(this, type);
        if (result == null) {
            return null;
        } else {
            return (T)result.getService();
        }
    }

    /** {@inheritDoc} */
    public List<ServiceEntry> getServiceEntries() {
        return this.unmodifiableServiceEntries;
    }

    /** {@inheritDoc} */
    public ServiceEntry findService(Class<?> type) {
        ServiceEntry result = null;
        for (ServiceEntry entry : serviceEntries) {
            if (type.equals(entry.getType())) {
                if (result == null || result.getDistance() > entry.getDistance()) {
                    result = entry;
                }
            }
        }
        return result;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<? extends T> type) {
        ServiceEntry entry = findService(type);
        if (entry == null) {
            return null;
        } else {
            return (T)entry.getService();
        }
    }
}