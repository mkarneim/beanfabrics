/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics;

import java.io.Serializable;
import java.lang.reflect.Type;

import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.util.GenericType;

/**
 * Temporary class that will be deleted soon. Decorates a view class and contains the logic to find out the generic type
 * argument of a given {@link View} class.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ViewClassDecorator implements Serializable {
    private final Class<? extends View> viewClass;
    private Class<? extends PresentationModel> expectedModelType;

    public ViewClassDecorator(Class<? extends View> viewClass) {
        this.viewClass = viewClass;
    }

    public Class<? extends PresentationModel> getExpectedModelType() {
        if (this.expectedModelType == null) {

            GenericType gt = new GenericType(viewClass);
            GenericType typeParam = gt.getTypeParameter(View.class.getTypeParameters()[0]);
            Type narrowedType = typeParam.narrow(typeParam.getType(), PresentationModel.class);

            if (narrowedType instanceof Class) {
                @SuppressWarnings("unchecked")
                Class<? extends PresentationModel> casted = (Class<? extends PresentationModel>) narrowedType;
                this.expectedModelType = casted;
            } else {
                // Can't resolve model type
                this.expectedModelType = PresentationModel.class;
            }
        }
        return this.expectedModelType;
    }
}