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
 * {@link Slf4jLogger} is produced as default if the SLF4J api is included in
 * the classpath. Otherwise a {@link Jdk14Logger} is produced.
 * <p>
 * You can configure this factory to produceFa custom <code>Logger</code> by
 * <ul>
 * <li>calling the method {@link #setLoggerClass(Class)}
 * <li>or by setting the system property
 * "org.beanfabrics.log.LoggerFactory.loggerclass" to the <code>Logger</code>'s
 * classname.
 * </ul>
 * Beanfabrics uses this 'own' <code>LoggerFactory</code> and
 * <code>Logger</code> to be runtime independent of any foreign library.
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
	private static ReflectiveFactoryHolder internalFactoryHolder = new ReflectiveFactoryHolder();

	/**
	 * Use this method to access <code>internalFactory</code> so that it is
	 * constructed at first access not at construction time of
	 * <code>LoggerFactory</code>.
	 *
	 * @return the <code>internalFactory</code>
	 */
	private static ReflectiveFactory getInternalFactory() {
		return internalFactoryHolder.getInternalFactory();
	}

	/**
	 * Returns a the {@link Logger} instance for a certain {@link Class}.
	 *
	 * @param clazz
	 *            the <code>Class</code> the <code>Logger</code> is for
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
	 * @param clazz
	 *            the <code>Class</code> implementing the {@link Logger}
	 * @throws IllegalArgumentException
	 *             if <code>clazz</code> is <code>null</code>, does not
	 *             implement {@link Logger} or has no constructor with the
	 *             argument "{@link Class} <code>clazz</code>"
	 */
	@SuppressWarnings("unchecked")
	public static void setLoggerClass(Class clazz) throws IllegalArgumentException {
		if (clazz == null) {
			throw new IllegalArgumentException("class must not be null");
		}
		if (Logger.class.isAssignableFrom(clazz) == false) {
			// clazz does not implement Logger
			throw new IllegalArgumentException("Can only use loggers that implement " + Logger.class.getName());
		}
		try {
			Constructor constr = clazz.getConstructor(new Class[] { Class.class });
			getInternalFactory().setConstr(constr);
			map.clear(); // Newly build all following Logger instances
		} catch (Exception e) {
			throw new IllegalArgumentException("The constructor \"public " + clazz.getName() + "(Class clazz)\" doesn't exist. Please implement it.");
		}
	}

	/**
	 * Using the holder pattern to lazy create the {@link ReflectiveFactory} on
	 * first access to the holder.
	 */
	private static class ReflectiveFactoryHolder {
		private ReflectiveFactory internalFactory = new ReflectiveFactory();

		public ReflectiveFactory getInternalFactory() {
			return internalFactory;
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
					try {
						// try if slf4j is available
						Class.forName("org.slf4j.Logger");
						clazz = Class.forName("org.beanfabrics.log.Slf4jLogger");
					} catch (ClassNotFoundException e) {
						// else use the java logging framework
						clazz = Class.forName("org.beanfabrics.log.Jdk14Logger");
					}
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
					return new NopLogger();
				} else {
					return constr.newInstance(new Object[] { clazz });
				}
			} catch (Exception e) {
				throw new UndeclaredThrowableException(e, "Can't create an instance of the logger");
			}
		}
	}
}
