/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.goodies.calendar;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;

/**
 * @author Max Gensthaler
 */
public class BnCalendarChooserButton extends DatePMCalendarChooserButton implements ModelSubscriber {
    private final Link link = new Link(this);

    public BnCalendarChooserButton() {
        super();
    }

    public BnCalendarChooserButton(int prevVisMonths, int subsVisMonths) {
        super(prevVisMonths, subsVisMonths);
    }

    /** {@inheritDoc} */
    public IModelProvider getModelProvider() {
        return link.getModelProvider();
    }

    /** {@inheritDoc} */
    public void setModelProvider(IModelProvider provider) {
        this.link.setModelProvider(provider);
    }

    /** {@inheritDoc} */
    public Path getPath() {
        return link.getPath();
    }

    /** {@inheritDoc} */
    public void setPath(Path path) {
        this.link.setPath(path);
    }
}
