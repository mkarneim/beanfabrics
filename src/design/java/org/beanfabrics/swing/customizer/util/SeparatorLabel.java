/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.util;

import java.awt.Font;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.UIManager;

/**
 * The <code>SeparatorLabel</code> is a {@link JLabel} with a horizontal
 * separator line.
 * 
 * @author Michael Karneim
 */
public class SeparatorLabel extends JLabel {

    public SeparatorLabel() {
        super();
    }

    public SeparatorLabel(String p0) {
        super(p0);
    }

    public SeparatorLabel(String p0, int p1) {
        super(p0, p1);
    }

    public SeparatorLabel(Icon p0) {
        super(p0);
    }

    public SeparatorLabel(Icon p0, int p1) {
        super(p0, p1);
    }

    public SeparatorLabel(String p0, Icon p1, int p2) {
        super(p0, p1, p2);
    }

    public void paint(Graphics g) {
        super.paint(g);
        SeparatorPainter.paint(this, g, this.getText());
    }

    public void updateUI() {
        super.updateUI();
        this.setFont(this.getFont().deriveFont(Font.BOLD));
        this.setForeground(UIManager.getColor("TitledBorder.titleColor"));
    }
}