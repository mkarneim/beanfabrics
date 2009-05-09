/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.swing.customizer.util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.beanfabrics.swing.BnButton;
/**
 * The <code>ToolbarButton</code> is a {@link BnButton} with a popup border.
 * 
 * @author Michael Karneim
 */
public class ToolbarButton extends BnButton {
	public ToolbarButton() {
		this.addMouseListener( new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				setBorderPainted(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setBorderPainted(false);
			}
			
		});
	}
}