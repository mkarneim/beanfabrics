/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.IOperationPM;

/**
 * The <code>BnMouseClickAction</code> is a {@link MouseListener} that can
 * subscribe to an {@link IOperationPM}.
 * 
 * @author Michael Karneim
 * @beaninfo
 */
@SuppressWarnings("serial")
public class BnMouseClickAction extends BnAction implements MouseListener {
    private int clickCount = 2;

    /**
     * Constructs a <code>BnMouseClickAction</code>.
     */
    public BnMouseClickAction() {
        super();
    }

    /**
     * Constructs a <code>BnMouseClickAction</code> and binds it to the
     * specified model.
     * 
     * @param pModel the model
     */
    public BnMouseClickAction(IOperationPM pModel) {
        super(pModel);
    }

    /**
     * Constructs a <code>BnMouseClickAction</code> and subscribes it for the
     * model at the specified Path provided by the given provider.
     * 
     * @param provider the <code>ModelProvider</code> to set initially
     * @param path the <code>Path</code> to set initially
     */
    public BnMouseClickAction(IModelProvider provider, Path path) {
        super(provider, path);
    }

    /**
     * Constructs a <code>BnMouseClickAction</code> and subscribes it for the
     * model at the root level provided by the given provider.
     * 
     * @param provider the <code>ModelProvider</code> to set initially
     */
    public BnMouseClickAction(IModelProvider provider) {
        this.setModelProvider(provider);
        setPath(new Path());
    }

    public void setClickCount(int aClickCount) {
        this.clickCount = aClickCount;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == this.clickCount) {
            ActionEvent evt = new ActionEvent(e.getSource(), e.getID(), "");
            this.actionPerformed(evt);
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}