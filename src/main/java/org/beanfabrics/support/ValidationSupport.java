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
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.util.ReflectionUtil;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * This class supports the processing of {@link Validation} annotations for
 * {@link PresentationModel} classes. Annotated methods are constructed into
 * {@link ValidationRule}s and included into the validation process of the
 * referenced model.
 *
 * @see <a href="http://www.beanfabrics.org/wiki/index.php/Validation_Tutorial">
 *      beanfabrics tutorial on the validation framework< /a>
 * @author Michael Karneim
 */
public class ValidationSupport implements Support {
	public static ValidationSupport get(PresentationModel model) {
		Supportable s = (Supportable)model;

		ValidationSupport support = s.getSupportMap().get( ValidationSupport.class);
		if ( support == null) {
			support = new ValidationSupport(model);
			s.getSupportMap().put(ValidationSupport.class, support);
		}
		return support;
	}

	private PresentationModel model;
	private Map<Method, ValidationMethodSupport> map = new HashMap<Method, ValidationMethodSupport>();

	private ValidationSupport(PresentationModel model) {
		if ( model == null) {
			throw new IllegalArgumentException("model==null");
		}
		this.model = model;
	}

	public void setup(Method method) {
		if ( map.containsKey(method)==false) {
			ValidationMethodSupport support = support( model, method);
			map.put(method, support);
		}
	}

	private static ValidationMethodSupport support(PresentationModel pModel, Method m) {
		return new ValidationMethodSupport(pModel, m);
	}

	private static class ValidationMethodSupport {
		private final PresentationModel owner;
		private final List<Observation> obervations = new LinkedList<Observation>();
		private final Method annotatedMethod;
		private final ValidationRule rule;

		private ValidationMethodSupport(PresentationModel owner, Method annotatedMethod) {
			Validation anno = annotatedMethod.getAnnotation(Validation.class);
			String message = anno.message();
			boolean validWhen = anno.validWhen();

			if ( annotatedMethod.getParameterTypes()!=null && annotatedMethod.getParameterTypes().length>0) {
				throw new IllegalArgumentException("method '"+annotatedMethod.getName()+"' must not declare any parameter when annotated with @Validation");
			}
			if ( Boolean.TYPE.isAssignableFrom(annotatedMethod.getReturnType())) {
				if ( message == null || message.length()==0) {
					//throw new IllegalArgumentException("@Validation on boolean method '"+annotatedMethod.getName()+"' must define the message attribute");
					message = getDefaultMessage();
				}
				rule = new BooleanMethodValidationRule(validWhen, message);
			} else if ( ValidationState.class.isAssignableFrom(annotatedMethod.getReturnType()) ) {
				if ( message != null && message.length()>0) {
					throw new IllegalArgumentException("@Validation on method '"+annotatedMethod.getName()+"' must NOT define the message attribute");
				}
				rule = new ValidationStateMethodValidationRule();
			} else {
				throw new IllegalArgumentException("the return type of method '"+annotatedMethod.getName()+"' must either be boolean or a ValidationState");
			}

			this.owner = owner;
			this.annotatedMethod = annotatedMethod;

			for( int i=0; i<anno.path().length; ++i) {
				Path path = Path.parse( anno.path()[i]);
				this.obervations.add(new ValidationTargetObservation(owner, path));
			}

		}

		private String getDefaultMessage() {
			return "Validation failed"; // TODO i18n
		}

		private class ValidationTargetObservation extends Observation {
			private PresentationModel currentTarget = null;
			public ValidationTargetObservation(PresentationModel root, Path path) {
				super(root, path);
				this.addPropertyChangeListener( new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						addValidationRuleToTarget();
					}
				});
				this.addValidationRuleToTarget();
			}

			public void addValidationRuleToTarget() {
				PresentationModel newTarget = this.getTarget();
				if ( newTarget == this.currentTarget) {
					return; // nothing to do
				}
				if ( this.currentTarget != null ) {
					this.currentTarget.getValidator().remove(ValidationMethodSupport.this.rule);
				}
				this.currentTarget = newTarget;
				if ( this.currentTarget != null ) {
					this.currentTarget.getValidator().add(ValidationMethodSupport.this.rule);
				}
			}
		}

		private class BooleanMethodValidationRule implements ValidationRule {
			private final boolean validWhen;
			private final String message;

			public BooleanMethodValidationRule(boolean validWhen, String message) {
				this.validWhen = validWhen;
				this.message = message;
			}
			public ValidationState validate() {
				boolean isValid = callAnnotatedMethod();
				if ( isValid == validWhen) {
					return null;
				} else {
					return ValidationState.create(message);
				}
			}
			private boolean callAnnotatedMethod() {
				boolean result;
				try {
					result = (Boolean) ReflectionUtil.invokeMethod(owner, annotatedMethod, (Object[])null);
				} catch (IllegalArgumentException e) {
					throw new UndeclaredThrowableException(e);
				} catch (IllegalAccessException e) {
					throw new UndeclaredThrowableException(e);
				} catch (InvocationTargetException e) {
					throw new UndeclaredThrowableException(e);
				}
				return result;
			}
		}

		private class ValidationStateMethodValidationRule implements ValidationRule {
			public ValidationState validate() {
				ValidationState result = callAnnotatedMethod();
				return result;
			}
			private ValidationState callAnnotatedMethod() {
				ValidationState result;
				try {
					result = (ValidationState) ReflectionUtil.invokeMethod(owner, annotatedMethod, (Object[])null);
				} catch (IllegalArgumentException e) {
					throw new UndeclaredThrowableException(e);
				} catch (IllegalAccessException e) {
					throw new UndeclaredThrowableException(e);
				} catch (InvocationTargetException e) {
					throw new UndeclaredThrowableException(e);
				}
				return result;
			}
		}
	}
}