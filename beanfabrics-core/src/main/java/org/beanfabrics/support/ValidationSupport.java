/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
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
import java.util.ResourceBundle;

import org.beanfabrics.Path;
import org.beanfabrics.PathObservation;
import org.beanfabrics.log.Logger;
import org.beanfabrics.log.LoggerFactory;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.util.ReflectionUtil;
import org.beanfabrics.util.ResourceBundleFactory;
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
    private static Logger LOG = LoggerFactory.getLogger(ValidationSupport.class);
    private static final String KEY_MESSAGE_VALIDATION_FAILED = "message.validationFailed";

    public static ValidationSupport get(PresentationModel model) {

        ValidationSupport support = model.getSupportMap().get(ValidationSupport.class);
        if (support == null) {
            support = new ValidationSupport(model);
            model.getSupportMap().put(ValidationSupport.class, support);
        }
        return support;
    }

    private final ResourceBundle resourceBundle = ResourceBundleFactory.getBundle(ValidationSupport.class);
    private PresentationModel model;
    private Map<Method, ValidationMethodSupport> map = new HashMap<Method, ValidationMethodSupport>();

    private ValidationSupport(PresentationModel model) {
        if (model == null) {
            throw new IllegalArgumentException("model==null");
        }
        this.model = model;
    }

    public void setup(Method method) {
        if (map.containsKey(method) == false) {
            ValidationMethodSupport support = support(model, method, resourceBundle);
            map.put(method, support);
        }
    }

    private static ValidationMethodSupport support(PresentationModel pModel, Method m, ResourceBundle resourceBundle) {
        return new ValidationMethodSupport(pModel, m, resourceBundle);
    }

    private static class ValidationMethodSupport {
        private final ResourceBundle resourceBundle;
        private final PresentationModel owner;
        private final List<PathObservation> obervations = new LinkedList<PathObservation>();
        private final Method annotatedMethod;
        private final ValidationRule rule;

        private ValidationMethodSupport(PresentationModel owner, Method annotatedMethod, ResourceBundle bundle) {
            this.resourceBundle = bundle;
            Validation anno = annotatedMethod.getAnnotation(Validation.class);
            String message = anno.message();
            boolean validWhen = anno.validWhen();

            if (annotatedMethod.getParameterTypes() != null && annotatedMethod.getParameterTypes().length > 0) {
                throw new IllegalArgumentException("method '" + annotatedMethod.getName() + "' must not declare any parameter when annotated with @Validation");
            }
            if (Boolean.TYPE.isAssignableFrom(annotatedMethod.getReturnType())) {
                if (message == null || message.length() == 0) {
                    //throw new IllegalArgumentException("@Validation on boolean method '"+annotatedMethod.getName()+"' must define the message attribute");
                    message = getDefaultMessage();
                }
                rule = new BooleanMethodValidationRule(validWhen, message);
            } else if (ValidationState.class.isAssignableFrom(annotatedMethod.getReturnType())) {
                if (message != null && message.length() > 0) {
                    throw new IllegalArgumentException("@Validation on method '" + annotatedMethod.getName() + "' must NOT define the message attribute");
                }
                rule = new ValidationStateMethodValidationRule();
            } else {
                throw new IllegalArgumentException("the return type of method '" + annotatedMethod.getName() + "' must either be boolean or a ValidationState");
            }

            this.owner = owner;
            this.annotatedMethod = annotatedMethod;

            for (int i = 0; i < anno.path().length; ++i) {
                Path path = Path.parse(anno.path()[i]);
                LOG.debug("Observing " + owner + " at " + path + ".");
                this.obervations.add(new ValidationTargetObservation(owner, path));
            }

        }

        private String getDefaultMessage() {
            String message = resourceBundle.getString(KEY_MESSAGE_VALIDATION_FAILED);
            return message;
        }

        private class ValidationTargetObservation extends PathObservation {
            private PresentationModel currentTarget = null;

            public ValidationTargetObservation(PresentationModel root, Path path) {
                super(root, path);
                this.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        addValidationRuleToTarget();
                    }
                });
                this.addValidationRuleToTarget();
            }

            public void addValidationRuleToTarget() {
                PresentationModel newTarget = this.getTarget();
                if (newTarget == this.currentTarget) {
                    return; // nothing to do
                }
                if (this.currentTarget != null) {
                    this.currentTarget.getValidator().remove(ValidationMethodSupport.this.rule);
                }
                this.currentTarget = newTarget;
                if (this.currentTarget != null) {
                    LOG.debug("Adding validation rule to " + newTarget + " in " + getRootNode() + " at " + getPath() + ".");
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
                if (isValid == validWhen) {
                    return null;
                } else {
                    return ValidationState.create(message);
                }
            }

            private boolean callAnnotatedMethod() {
                boolean result;
                try {
                    result = (Boolean)ReflectionUtil.invokeMethod(owner, annotatedMethod, (Object[])null);
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
                    result = (ValidationState)ReflectionUtil.invokeMethod(owner, annotatedMethod, (Object[])null);
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