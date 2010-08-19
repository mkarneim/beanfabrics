/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.util;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;

/**
 * The <code>AbstractCustomizerPM</code> is the abstract base class for
 * customizer presentation models.
 * 
 * @author Michael Karneim
 */
public abstract class AbstractCustomizerPM extends AbstractPM {
    protected final TitlePM title = new TitlePM();

    public AbstractCustomizerPM() {
        PMManager.setup(this);
    }
}