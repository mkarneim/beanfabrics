/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics;

import org.beanfabrics.model.PresentationModel;

/**
 * Basic interface for classes that represent a view on a presentation model.
 * 
 * @author Michael Karneim
 * @param <M>
 */
public interface View<M extends PresentationModel> {
    /**
     * Returns the {@link PresentationModel} with that this component is
     * synchronized.
     * 
     * @return the {@link PresentationModel}
     */
    public M getPresentationModel();

    /**
     * Sets the {@link PresentationModel} to synchronize this component with.
     * 
     * @param pModel the <code>PresentationModel</code> to set
     */
    public void setPresentationModel(M pModel);
}