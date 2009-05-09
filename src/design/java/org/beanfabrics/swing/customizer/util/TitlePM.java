/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.swing.customizer.util;

import java.awt.Toolkit;

import javax.swing.ImageIcon;

import org.beanfabrics.model.IconTextPM;
import org.beanfabrics.model.PMManager;

/**
 * The <code>TitlePM</code> is the presentation model for the {@link TitlePanel}.
 * 
 * @author Michael Karneim
 */
public class TitlePM extends IconTextPM {
	
	public TitlePM() {
		PMManager.setup(this);
		this.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/org/beanfabrics/swing/beanfabrics48c.png"))));		
	}
	
}