/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.validation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Michael Karneim
 */
public class CompositeValidationState extends ValidationState {
    private final List<ValidationState> children = new LinkedList<ValidationState>();

    public CompositeValidationState(String message, ValidationState... states)
        throws IllegalArgumentException {
        super(message);
        for (ValidationState state : states) {
            children.add(state);
        }
    }

    public CompositeValidationState(String message, Collection<ValidationState> states)
        throws IllegalArgumentException {
        super(message);
        children.addAll(states);
    }

    public List<ValidationState> getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!super.equals(o)) {
            return false;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        CompositeValidationState castedObj = (CompositeValidationState)o;
        return ((this.children == null ? castedObj.children == null : this.children.equals(castedObj.children)));
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode = 31 * hashCode + (children == null ? 0 : children.hashCode());
        return hashCode;
    }

    /**
     * Creates a new {@link CompositeValidationState} only if the specified list
     * of states contains at least one {@link ValidationState} object.
     * 
     * @param messagePrefix
     * @param states
     * @return the new {@link CompositeValidationState}
     */
    public static CompositeValidationState create(String messagePrefix, ValidationState... states) {
        if (states == null) {
            return null;
        } else {
            ValidationState detailState = null;
            for (ValidationState state : states) {
                if (state != null) {
                    detailState = state;
                    break;
                }
            }
            if (detailState == null) {
                return null;
            } else {
                return new CompositeValidationState(messagePrefix + detailState.getMessage(), states);
            }
        }
    }
}