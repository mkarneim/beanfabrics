/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
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
 * @author Michael Karneim
 */
public class DefaultContext implements Context {
    private final static Logger LOG = LoggerFactory.getLogger(DefaultContext.class);

    private ContextListener parentListener;
    private final CopyOnWriteArrayList<ContextListener> listener = new CopyOnWriteArrayList<ContextListener>();;
    private final List<ServiceEntry> serviceEntries = new LinkedList<ServiceEntry>();
    private final List<ServiceEntry> unmodifiableServiceEntries;
    private final List<Context> parents;
    private final List<Context> unmodifiableParents;

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

            }

            public void parentAdded(ParentAddedEvent evt) {

            }
        };
        unmodifiableParents = Collections.unmodifiableList(parents);
        unmodifiableServiceEntries = Collections.unmodifiableList(serviceEntries);
    }

    public List<Context> getParents() {
        return unmodifiableParents;
    }

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

    public void addContextListener(ContextListener l) {
        if (l == null) {
            throw new IllegalArgumentException("l must not be null.");
        }
        listener.add(l);
    }

    public void removeContextListener(ContextListener l) {
        if (l == null) {
            throw new IllegalArgumentException("l must not be null.");
        }
        listener.remove(l);
    }

    protected void fireParentAdded(Context parent) {
        ParentAddedEvent evt = new ParentAddedEvent(this, parent);
        for (ContextListener l : listener) {
            l.parentAdded(evt);
        }
    }

    protected void fireParentRemoved(Context parent) {
        ParentRemovedEvent evt = new ParentRemovedEvent(this, parent);
        for (ContextListener l : listener) {
            l.parentRemoved(evt);
        }
    }

    protected void fireServiceAdded(ServiceEntry entry) {
        ServiceAddedEvent evt = new ServiceAddedEvent(this, entry);
        for (ContextListener l : listener) {
            l.serviceAdded(evt);
        }
    }

    protected void fireServiceRemoved(ServiceEntry entry) {
        ServiceRemovedEvent evt = new ServiceRemovedEvent(this, entry);
        for (ContextListener l : listener) {
            l.serviceRemoved(evt);
        }
    }

    public <T> boolean addService(Class<T> type, T service) {
        ServiceEntry entry = new ServiceEntry(0, this, service, type);
        return addServiceEntry(entry);
    }

    private boolean addServiceEntry(ServiceEntry entry) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("adding service '" + entry.getType().getName() + "' to " + this);
        }
        boolean result = serviceEntries.add(entry);
        fireServiceAdded(entry);
        return result;
    }

    protected Object removeServiceEntry(Context origin, Class type) {
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

    public Object removeService(Class type) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("removeService(" + type.getName() + ")");
        }
        return removeServiceEntry(this, type);
    }

    public List<ServiceEntry> getServiceEntries() {
        return this.unmodifiableServiceEntries;
    }

    public ServiceEntry findService(Class type) {
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
}