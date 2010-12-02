/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import java.util.LinkedList;
import java.util.List;

import org.beanfabrics.ValidatableBean;
import org.beanfabrics.context.Context;
import org.beanfabrics.support.PropertySupport;
import org.beanfabrics.support.SupportMap;
import org.beanfabrics.validation.CompositeValidationState;
import org.beanfabrics.validation.Validatable;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * The {@link AbstractPM} is the general superclass of PM components.
 * <p>
 * Usually you will extend this class to create new PM components, or any of its
 * descendents.
 * 
 * @author Michael Karneim
 */
public abstract class AbstractPM extends ValidatableBean implements PresentationModel {
    private final SupportMap supportMap = new SupportMap(this);
    private final ModelContext context = new ModelContext(this);

    /**
     * Constructs a {@link AbstractPM}.
     */
    protected AbstractPM() {
        // Please note: to disable default validation rules just call getValidator().clear();
        getValidator().add(new PropertiesValidationRule());
    }

    /** {@inheritDoc} */
    public SupportMap getSupportMap() {
        return this.supportMap;
    }

    /** {@inheritDoc} */
    public Context getContext() {
        return context;
    }

    /** {@inheritDoc} */
    public Comparable<?> getComparable() {
        return null;
    }

    /**
     * Revalidates all properties of this PM.
     * 
     * @see Validatable#revalidate()
     */
    public void revalidateProperties() {
        PropertySupport.get(this).revalidateProperties();
    }

    /**
     * This rule evaluates to invalid if at least one of this PM's direct
     * children is invalid (but not an instance of {@link IOperationPM}).
     * 
     * @author Michel Karneim
     */
    public final class PropertiesValidationRule implements ValidationRule {
        private final PresentationModelFilter filter = new PresentationModelFilter() {
            public boolean accept(PresentationModel mdl) {
                return mdl != null && (mdl instanceof IOperationPM) == false;
            }
        };

        /** {@inheritDoc} */
        public ValidationState validate() {
            List<ValidationState> states = new LinkedList<ValidationState>();
            for (PresentationModel child : PropertySupport.get(AbstractPM.this).filterProperties(this.filter)) {
                if (child.isValid() == false) {
                    states.add(child.getValidationState());
                }
            }
            if (states.isEmpty()) {
                return null;
            } else {
                // TODO (mk) i18n
                return new CompositeValidationState("One or more properties are invalid", states);
            }
        }
    }
}