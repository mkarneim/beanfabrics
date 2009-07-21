package org.beanfabrics.util;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ResourceBundle;

/**
 * The ResourceBundleFactory is a factory for ResourceBundle objects by class
 * key.
 * <p>
 * You can configure this factory to produce a custom
 * <code>ResourceBundle</code> by extending ResourceBundleFactory and using your
 * new class as default factory. This can be done by
 * <ul>
 * <li>calling the method {@link #setFactoryClass(Class)}
 * <li>or by setting the system property
 * "org.beanfabrics.util.ResourceBundleFactory.impl" to the factory's classname.
 * </ul>
 * 
 * @author Michael Karneim
 */
public class ResourceBundleFactory {
    public static final String SYSPROPKEY_RESOURCE_BUNDLE_FACTORY = ResourceBundleFactory.class.getName() + ".impl";
    private static Class clazz;
    static {
        try {
            String prop = System.getProperty(SYSPROPKEY_RESOURCE_BUNDLE_FACTORY);
            if (prop != null) {
                clazz = Class.forName(prop);
            } else {
                clazz = ResourceBundleFactory.class;
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new UndeclaredThrowableException(e);
        }
    }
    private static ResourceBundleFactory instance;

    /**
     * Sets the type of the default ResourceBundleFactory to use.
     * 
     * @param <T>
     * @param cls the type of the default ResourceBundleFactory to use
     */
    public static final synchronized <T extends ResourceBundleFactory> void setFactoryClass(Class<T> cls) {
        ResourceBundleFactory.clazz = cls;
        ResourceBundleFactory.instance = null;
    }

    /**
     * Returns the ResourceBundleFactory.
     * 
     * @return the ResourceBundleFactory
     */
    protected static final synchronized ResourceBundleFactory getInstance() {
        if (instance == null) {
            try {
                instance = (ResourceBundleFactory)clazz.newInstance();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new UndeclaredThrowableException(e);
            }
        }
        return instance;
    }

    /**
     * Returns the ResourceBundle for the given class. It is not guaranteed that
     * subsequent calls with the same parameter will return the same instance.
     * 
     * @param forClass
     * @return the ResourceBundle for the given class
     */
    public static final ResourceBundle getBundle(Class forClass) {
        return getInstance().getBundleImpl(forClass);
    }

    /**
     * Returns the ResourceBundle for the given class. It is not guaranteed that
     * subsequent calls with the same parameter will return the same instance.
     * <p>
     * This method can be overriden to alter the default implementation.
     * 
     * @param forClass
     * @return the ResourceBundle for the given class
     */
    protected ResourceBundle getBundleImpl(Class forClass) {
        ResourceBundle result = ResourceBundle.getBundle(forClass.getName() + "ResourceBundle");
        return result;
    }
}
