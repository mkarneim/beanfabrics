/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics;

import org.beanfabrics.model.PresentationModel;

/**
 * The {@link View} is the interface for classes that can be bound to a
 * (specific, see M) {@link PresentationModel}.
 * <p>
 * That doesn't imply that an implementor must be visual at any kind.
 * 
 * @author Michael Karneim
 * @param <M> the concrete {@link PresentationModel} type
 */
public interface View<M extends PresentationModel> {
    /**
     * Returns the {@link PresentationModel} of this view.
     * 
     * @return the {@link PresentationModel}
     */
    public M getPresentationModel();

    /**
     * Sets the {@link PresentationModel} of this view.
     * 
     * @param pModel the <code>PresentationModel</code>
     */
    public void setPresentationModel(M pModel);
}