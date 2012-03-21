/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.validation;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.beanfabrics.AbstractBean;

/**
 * The <code>Validator</code> is responsible for creating a
 * {@link ValidationState} by evaluating a list of {@link ValidationRule}
 * instances. <pM> It is a composite of validation rules. Rules can be added and
 * removed via the collection interface. <pM> A call to the {@link #validate()}
 * method is delegated to each validation rule in the order they have been
 * added, until the first not-<code>null</code> {@link ValidationState} is
 * returned.
 * 
 * @author Michael Karneim
 * @see ValidationRule
 */
public class Validator extends AbstractBean implements Collection<ValidationRule>, ValidationRule {
    private final List<ValidationRule> rules = new LinkedList<ValidationRule>();

    /**
     * Validates the current context. That context is invalid when at least one
     * validation rule detects an invalid state. Produces a
     * {@link ValidationState} by delegating this call to the validation rules
     * in the same order they have been added, until a not-null value is
     * returned.
     */
    public ValidationState validate() {
        for (ValidationRule rule : this) {
            ValidationState state = rule.validate();
            if (state != null) {
                return state;
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    public boolean add(ValidationRule o) {
        boolean result = rules.add(o);
        if (result) {
            fireChanged();
        }
        return result;
    }

    /**
     * Inserts the specified rule at the specified position in this validator.
     * 
     * @param index
     * @param o
     */
    public void add(int index, ValidationRule o) {
        rules.add(index, o);
        fireChanged();
    }

    /** {@inheritDoc} */
    public boolean addAll(Collection<? extends ValidationRule> c) {
        boolean result = rules.addAll(c);
        if (result) {
            fireChanged();
        }
        return result;
    }

    /** {@inheritDoc} */
    public void clear() {
        if (!isEmpty()) {
            rules.clear();
            fireChanged();
        }
    }

    /** {@inheritDoc} */
    public boolean contains(Object o) {
        return rules.contains(o);
    }

    /** {@inheritDoc} */
    public boolean containsAll(Collection<?> c) {
        return rules.containsAll(c);
    }

    /**
     * Returns the rule at the specified position in this <code>Validator</code>
     * .
     * 
     * @param index index of the rule to return
     * @return rule at the given index
     */
    public ValidationRule get(int index) {
        return rules.get(index);
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        return rules.isEmpty();
    }

    /** {@inheritDoc} */
    public Iterator<ValidationRule> iterator() {
        return new Iterator<ValidationRule>() {
            Iterator<ValidationRule> delegate = rules.iterator();

            public void remove() {
                delegate.remove();
                fireChanged();
            }

            public ValidationRule next() {
                return delegate.next();
            }

            public boolean hasNext() {
                return delegate.hasNext();
            }
        };
    }

    /** {@inheritDoc} */
    public boolean remove(Object o) {
        boolean result = rules.remove(o);
        if (result) {
            fireChanged();
        }
        return result;
    }

    /**
     * Removes the rule at the specified position in this <code>Validator</code>
     * .
     * 
     * @param index index of the rule to remove
     * @return removed rule from the given index
     */
    public ValidationRule remove(int index) {
        int oldSize = this.size();
        ValidationRule result = rules.remove(index);
        if (oldSize != this.size()) {
            fireChanged();
        }
        return result;
    }

    /** {@inheritDoc} */
    public boolean removeAll(Collection<?> c) {
        boolean result = rules.removeAll(c);
        if (result) {
            fireChanged();
        }
        return result;
    }

    /** {@inheritDoc} */
    public boolean retainAll(Collection<?> c) {
        boolean result = rules.retainAll(c);
        if (result) {
            fireChanged();
        }
        return result;
    }

    /** {@inheritDoc} */
    public int size() {
        return rules.size();
    }

    /** {@inheritDoc} */
    public Object[] toArray() {
        return rules.toArray();
    }

    /** {@inheritDoc} */
    public <T> T[] toArray(T[] a) {
        return rules.toArray(a);
    }

    /**
     * Fires a {@link PropertyChangeEvent} for the "rules" property.
     */
    private void fireChanged() {
        this.getPropertyChangeSupport().firePropertyChange("rules", null, null);
    }
}