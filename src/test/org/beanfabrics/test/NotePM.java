/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;

public class NotePM extends AbstractPM {
    final TextPM content = new TextPM();

    public NotePM() {
        PMManager.setup(this);
    }
}