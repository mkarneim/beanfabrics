/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.log;

import java.lang.reflect.Constructor;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Map;

/**
 * Beanfabrics classes use this <code>LoggerFactory</code> for producing a
 * {@link Logger} instance.
 * <p>
 * {@link Log4JLogger} is produced as default if log4j is included into the
 * classpath. Otherwise a {@link DummyLogger} is produced.
 * <p>
 * You can configure this factory to produce a custom <code>Logger</code> by
 * <ul>
 * <li>calling the method {@link #setLoggerClass(Class)}
 * <li>or by setting the system property
 * "org.beanfabrics.log.LoggerFactory.loggerclass" to the <code>Logger</code>'s
 * classname.
 * </ul>
 * 
 * @see <a href="http://www.beanfabrics.org/index.php/Logging_Example">Logging
 *      Example</a>
 * @author Michael Karneim
 * @author Max Gensthaler
 */
public class LoggerFactory {
    /** The key of the system property to set a custom {@link Logger}. */
    public static final String SYSPROPKEY_LOGGER = LoggerFactory.class.getName() + ".loggerclass";
    @SuppressWarnings("unchecked")
    private static final Map<Class, Logger> map = new HashMap<Class, Logger>();
    private static ReflectiveFactory internalFactory;

    /**
     * Use this method to access <code>internalFactory</code> so that it is
     * constructed at first access not at construction time of
     * <code>LoggerFactory</code>.
     * 
     * @return the <code>internalFactory</code>
     */
    private static ReflectiveFactory getInternalFactory() {
        if (internalFactory == null) {
            internalFactory = new ReflectiveFactory();
        }
        return internalFactory;
    }

    /**
     * Returns a the {@link Logger} instance for a certain {@link Class}.
     * 
     * @param clazz the <code>Class</code> the <code>Logger</code> is for
     * @return the <code>Logger</code> instance
     */
    @SuppressWarnings("unchecked")
    public static Logger getLogger(Class clazz) {
        Logger result = map.get(clazz);
        if (result == null) {
            result = getInternalFactory().createLogger(clazz);
            map.put(clazz, result);
        }
        return result;
    }

    /**
     * Set the {@link Class} implementing the {@link Logger}. This class will be
     * used to create new instances by {@link #getLogger(Class)}.
     * 
     * @param clazz the <code>Class</code> implementing the {@link Logger}
     * @throws IllegalArgumentException if <code>clazz</code> is
     *             <code>null</code>, does not implement {@link Logger} or has
     *             no constructor with the argument "{@link Class}
     *             <code>clazz</code>"
     */
    @SuppressWarnings("unchecked")
    public static void setLoggerClass(Class clazz)
        throws IllegalArgumentException {
        if (clazz == null) {
            throw new IllegalArgumentException("class must not be null");
        }
        if (Logger.class.isAssignableFrom(clazz) == false) {
            // clazz does not implement Logger
            throw new IllegalArgumentException("Can only use loggers that implement " + Logger.class.getName());
        }
        try {
            Constructor constr = clazz.getConstructor(new Class[] { Class.class });
            internalFactory.setConstr(constr);
            map.clear(); // Newly build all following Logger instances
        } catch (Exception e) {
            throw new IllegalArgumentException("The constructor \"public " + clazz.getName() + "(Class clazz)\" doesn't exist. Please implement it.");
        }
    }

    private static class ReflectiveFactory {
        private Constructor<Logger> constr;

        @SuppressWarnings("unchecked")
        public ReflectiveFactory() {
            try {
                Class clazz;
                String loggerclassSysProp = System.getProperty(SYSPROPKEY_LOGGER);
                if (loggerclassSysProp != null) {
                    clazz = Class.forName(loggerclassSysProp);
                } else {
                    Class.forName("org.apache.log4j.Logger");
                    clazz = Class.forName("org.beanfabrics.log.Log4JLogger");
                }
                constr = clazz.getConstructor(new Class[] { Class.class });
            } catch (ClassCastException e) {
                e.printStackTrace(System.err);
            } catch (Exception e) {
                // Logging not available
            }
        }

        public void setConstr(Constructor<Logger> constr) {
            this.constr = constr;
        }

        @SuppressWarnings("unchecked")
        public Logger createLogger(Class clazz) {
            try {
                if (constr == null) {
                    return new DummyLogger();
                } else {
                    return constr.newInstance(new Object[] { clazz });
                }
            } catch (Exception e) {
                throw new UndeclaredThrowableException(e, "Can't create an instance of the logger");
            }
        }
    }
}