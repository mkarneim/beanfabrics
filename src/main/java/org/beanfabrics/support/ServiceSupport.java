/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.support;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Map;

import org.beanfabrics.context.ContextListener;
import org.beanfabrics.context.ContextOwner;
import org.beanfabrics.context.ParentAddedEvent;
import org.beanfabrics.context.ParentRemovedEvent;
import org.beanfabrics.context.ServiceAddedEvent;
import org.beanfabrics.context.ServiceEntry;
import org.beanfabrics.context.ServiceRemovedEvent;
import org.beanfabrics.log.Logger;
import org.beanfabrics.log.LoggerFactory;
import org.beanfabrics.util.ReflectionUtil;

/**
 * @author Michael Karneim
 */
public class ServiceSupport implements Support {
    private final static Logger LOG = LoggerFactory.getLogger(ServiceSupport.class);

    public static ServiceSupport get(ContextOwner owner) {
        if (!(owner instanceof Supportable)) {
            throw new IllegalArgumentException("owner must implement " + Supportable.class.getName());
        }
        Supportable s = (Supportable)owner;

        ServiceSupport support = s.getSupportMap().get(ServiceSupport.class);
        if (support == null) {
            support = new ServiceSupport(owner);
            s.getSupportMap().put(ServiceSupport.class, support);
        }
        return support;
    }

    private final ContextOwner owner;
    private Map<Member, ServiceMemberSupport> map = new HashMap<Member, ServiceMemberSupport>();

    public ServiceSupport(ContextOwner owner) {
        if (owner == null) {
            throw new IllegalArgumentException("owner==null");
        }
        this.owner = owner;
    }

    public void setup(Method method) {
        if (map.containsKey(method) == false) {
            ServiceMethodSupport support = new ServiceMethodSupport(owner, method);
            map.put(method, support);
        }
    }

    public void setup(Field field) {
        if (map.containsKey(field) == false) {
            ServiceFieldSupport support = new ServiceFieldSupport(owner, field);
            map.put(field, support);
        }
    }

    private static class ServiceMemberSupport implements ContextListener {
        protected final ContextOwner owner;

        protected Class type;
        private ServiceEntry serviceEntry;

        public ServiceMemberSupport(ContextOwner owner) {
            super();
            this.owner = owner;
            (this.owner).getContext().addContextListener(this);
        }

        protected void findServiceEntry() {
            ServiceEntry found = this.owner.getContext().findService(type);
            if (found != null) {
                this.setServiceEntry(found);
            }
        }

        public void serviceRemoved(ServiceRemovedEvent evt) {
            if (serviceEntry != null && serviceEntry == evt.getServiceEntry()) {
                ServiceEntry nextService = this.owner.getContext().findService(type);
                this.setServiceEntry(nextService);
            }
        }

        public void serviceAdded(ServiceAddedEvent evt) {
            if (evt.getServiceEntry().getType().equals(type)) {
                if (serviceEntry == null || evt.getServiceEntry().getDistance() < serviceEntry.getDistance()) {
                    setServiceEntry(evt.getServiceEntry());
                }
            }
        }

        public void parentRemoved(ParentRemovedEvent evt) {
        }

        public void parentAdded(ParentAddedEvent evt) {
        }

        protected void setServiceEntry(ServiceEntry entry) {
            if (LOG.isDebugEnabled()) {
                if (entry == null) {
                    LOG.debug("unsetting service for type " + type.getName());
                } else {
                    LOG.debug("setting service for type " + type.getName() + " with entry " + entry.getType().getName());
                }
            }
            this.serviceEntry = entry;
        }
    }

    private static class ServiceFieldSupport extends ServiceMemberSupport {
        private final Field annotatedField;

        public ServiceFieldSupport(ContextOwner owner, Field annotatedField) {
            super(owner);
            this.annotatedField = annotatedField;
            Service anno = annotatedField.getAnnotation(Service.class);
            if (Object.class.equals(anno.value())) {
                this.type = annotatedField.getType();
            } else {
                // TODO (mk) ensure that anno.value() is (sub)type of "type"
                this.type = anno.value();
            }
            findServiceEntry();
        }

        @Override
        protected void setServiceEntry(ServiceEntry entry) {
            super.setServiceEntry(entry);
            updateAnnotatedField(entry == null ? null : entry.getService());
        }

        private void updateAnnotatedField(Object service) {
            try {
                ReflectionUtil.setFieldValue(owner, annotatedField, service);
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (IllegalAccessException e) {
                throw new UndeclaredThrowableException(e);
            }
        }
    }

    private static class ServiceMethodSupport extends ServiceMemberSupport {
        private final Method annotatedMethod;

        private ServiceMethodSupport(ContextOwner owner, Method annotatedMethod) {
            super(owner);
            // TODO (mk) ensure that method is a setter-Method and has exactly one parameter
            this.annotatedMethod = annotatedMethod;
            Service anno = annotatedMethod.getAnnotation(Service.class);

            if (Object.class.equals(anno.value())) {
                this.type = annotatedMethod.getParameterTypes()[0];
            } else {
                // TODO (mk) ensure that anno.value() is (sub)type of "type"
                this.type = anno.value();
            }
            findServiceEntry();
        }

        @Override
        protected void setServiceEntry(ServiceEntry entry) {
            super.setServiceEntry(entry);
            callAnnotatedMethod(entry == null ? null : entry.getService());
        }

        private void callAnnotatedMethod(Object service) {
            try {
                ReflectionUtil.invokeMethod(owner, annotatedMethod, new Object[] { service });
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (IllegalAccessException e) {
                throw new UndeclaredThrowableException(e, annotatedMethod.getName());
            } catch (InvocationTargetException e) {
                throw new UndeclaredThrowableException(e, annotatedMethod.getName());
            }
        }
    }

}