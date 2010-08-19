/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

import org.beanfabrics.util.GenericsUtil;

/**
 * Temporary class that will be deleted soon. Decorates a view class and
 * contains the logic to find out the generic type argument of a given
 * {@link View} class.
 * 
 * @author Michael Karneim
 */
public class ViewClassDecorator {
    private final Class<? extends View> viewClass;
    private Type expectedModelType;

    public ViewClassDecorator(Class<? extends View> viewClass) {
        this.viewClass = viewClass;
    }

    public Class getExpectedModelType() {
        if (this.expectedModelType == null) {
            List<Type> args = GenericsUtil.getTypeArguments(View.class, this.viewClass);
            Type arg = args.get(0); // View declares exactly one single type parameter.
            if (arg instanceof TypeVariable) {
                // we just take the first bound.
                // TODO (mk) perhaps we should support  *all* bounds?
                this.expectedModelType = ((TypeVariable)arg).getBounds()[0];
            } else if (arg instanceof Class) {
                this.expectedModelType = (Class)arg;
            } else if (arg instanceof ParameterizedType) {
                ParameterizedType pt = ((ParameterizedType)arg);
                // TODO (mk) this is possibly wrong !?!
                this.expectedModelType = pt.getRawType();
            } else {
                // this is totally unexpected
                throw new Error("Unexpected type argument: " + arg.getClass().getName());
            }
        }
        if (this.expectedModelType instanceof Class) {
            return (Class)this.expectedModelType;
        } else {
            return null;
        }
    }
}