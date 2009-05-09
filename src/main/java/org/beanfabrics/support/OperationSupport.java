/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.support;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.beanfabrics.Observation;
import org.beanfabrics.Path;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.util.ReflectionUtil;

/**
 * @author Michael Karneim
 */
// TODO (mk) UNIT TEST
public class OperationSupport implements Support {
	public static OperationSupport get(PresentationModel model) {
		Supportable s = (Supportable)model;

		OperationSupport support = s.getSupportMap().get( OperationSupport.class);
		if ( support == null) {
			support = new OperationSupport(model);
			s.getSupportMap().put(OperationSupport.class, support);
		}
		return support;
	}

	private final PresentationModel model;
	private Map<Method, ExecutionMethodSupport> map = new HashMap<Method, ExecutionMethodSupport>();

	public OperationSupport(PresentationModel model) {
		this.model = model;
	}

	public void setup(Method method) {
		if ( map.containsKey(method)==false) {
			ExecutionMethodSupport support = support( model, method);
			map.put(method, support);
		}
	}

	private static ExecutionMethodSupport support(PresentationModel pModel, Method m) {
		ExecutionMethodSupport result = new ExecutionMethodSupport(pModel, m);
		return result;
	}

	private static class ExecutionMethodSupport {
		private final PresentationModel owner;

		private final List<Observation> obervations = new LinkedList<Observation>();
		private final Method annotatedMethod;
		private final org.beanfabrics.model.ExecutionMethod executionMethod = new org.beanfabrics.model.ExecutionMethod() {

			public void execute() throws Throwable {
				callAnnotatedMethod();
			}

		};


		private ExecutionMethodSupport(PresentationModel owner, Method annotatedMethod) {
			Operation anno = annotatedMethod.getAnnotation(Operation.class);

			if ( annotatedMethod.getParameterTypes()!=null && annotatedMethod.getParameterTypes().length>0) {
				throw new IllegalArgumentException("method '"+annotatedMethod.getName()+"' must not declare any parameter when annotated with @ExecutionMethod");
			}

			this.owner = owner;
			this.annotatedMethod = annotatedMethod;


			if ( anno.path().length==1 && "#default".equals(anno.path()[0])) {
				// "default" handling
				if ( owner instanceof IOperationPM) {
					Path path = Path.parse(Path.THIS_PATH_ELEMENT);
					this.obervations.add(new OperationTargetObservation(owner, path));
				} else {
					Path path = Path.parse(annotatedMethod.getName());
					this.obervations.add(new OperationTargetObservation(owner, path));
				}
			} else {
				for( int i=0; i<anno.path().length; ++i) {
					Path path = Path.parse( anno.path()[i]);
					this.obervations.add(new OperationTargetObservation(owner, path));
				}
			}

		}

		private class OperationTargetObservation extends Observation {
			private PresentationModel currentTarget = null;
			public OperationTargetObservation(PresentationModel root, Path path) {
				super(root, path);
				this.addPropertyChangeListener( new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						addExecutionToTarget();
					}
				});
				this.addExecutionToTarget();
			}

			public void addExecutionToTarget() {
				PresentationModel newTarget = this.getTarget();
				if ( newTarget == this.currentTarget) {
					return; // nothing to do
				}
				if ( this.currentTarget != null && (this.currentTarget instanceof IOperationPM)) {
					// TODO (mk) merge IOperationPM and OperationPM
					if ( this.currentTarget instanceof OperationPM) {
						((OperationPM)this.currentTarget).removeExecutionMethod(ExecutionMethodSupport.this.executionMethod);
					}

				}
				this.currentTarget = newTarget;
				if ( this.currentTarget != null && (this.currentTarget instanceof IOperationPM)) {
					// TODO (mk) merge IOperationPM and OperationPM
					if ( this.currentTarget instanceof OperationPM) {
						((OperationPM)this.currentTarget).addExecutionMethod(ExecutionMethodSupport.this.executionMethod);
					}
				}
			}
		}

		private void callAnnotatedMethod() {
			try {
				ReflectionUtil.invokeMethod(owner, annotatedMethod, (Object[])null);
			} catch (IllegalArgumentException e) {
				throw new UndeclaredThrowableException(e);
			} catch (IllegalAccessException e) {
				throw new UndeclaredThrowableException(e);
			} catch (InvocationTargetException e) {
				throw new UndeclaredThrowableException(e);
			}
		}
	}
}