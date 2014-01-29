/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.swing.customizer.util.TitlePM;

/**
 * The <code>AbstractCustomizerPM</code> is the abstract base class for customizer presentation models.
 * 
 * @author Michael Karneim
 */
public abstract class AbstractCustomizerPM extends AbstractPM implements CustomizerPM {
    protected final TitlePM title = new TitlePM();
    private CustomizerBase customizer;

    public AbstractCustomizerPM() {
        PMManager.setup(this);
    }

    public void setCustomizer(CustomizerBase customizer) {
        this.customizer = customizer;
    }

    public CustomizerBase getCustomizer() {
        return customizer;
    }
    
}