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
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * @author Michael Karneim
 */
public abstract class AbstractPM extends ValidatableBean implements PresentationModel {
    private final SupportMap supportMap = new SupportMap(this);
    private final ModelContext context = new ModelContext(this);

    public AbstractPM() {
        ValidationRule childrenValidationRule = createPropertiesValidationRule();
        if (childrenValidationRule != null) {
            getValidator().add(childrenValidationRule);
        }
    }

    public SupportMap getSupportMap() {
        return this.supportMap;
    }

    public Context getContext() {
        return context;
    }

    public void revalidateProperties() {
        PropertySupport.get(this).revalidateProperties();
    }

    protected ValidationRule createPropertiesValidationRule() {
        return new PropertiesValidationRule();
    }

    /**
     * This rule defines that this object is invalid if any of its properties
     * (that are NOT instances of {@link IOperationPM}) is invalid.
     * 
     * @author Michel Karneim
     */
    public final class PropertiesValidationRule implements ValidationRule {
        private final PresentationModelFilter filter = new PresentationModelFilter() {
            public boolean accept(PresentationModel mdl) {
                return mdl != null && (mdl instanceof IOperationPM) == false;
            }
        };

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