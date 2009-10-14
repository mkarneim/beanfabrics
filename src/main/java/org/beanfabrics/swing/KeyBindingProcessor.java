/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.table.TableCellEditor;

import org.beanfabrics.swing.table.celleditor.BnTableCellEditor;
/**
 * The KeyBindingProcessor declares the method {@link JComponent#processKeyBinding} as "public"
 * method. It is used by the {@link BnTableCellEditor.ButtonDecorator} to forward activation key events 
 * to {@link TableCellEditor} components.
 *  
 * @author Michael Karneim
 */
public interface KeyBindingProcessor {
    /**
     * This is invoked as the result of a <code>KeyEvent</code>
     * that was not consumed by the <code>FocusManager</code>,
     * <code>KeyListeners</code>, or the component. It will first try
     * <code>WHEN_FOCUSED</code> bindings,
     * then <code>WHEN_ANCESTOR_OF_FOCUSED_COMPONENT</code> bindings,
     * and finally <code>WHEN_IN_FOCUSED_WINDOW</code> bindings.
     *
     * @param e the unconsumed <code>KeyEvent</code>
     * @param pressed true if the key is pressed
     * @return true if there is a key binding for <code>e</code>
     */
    public boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed);
}
