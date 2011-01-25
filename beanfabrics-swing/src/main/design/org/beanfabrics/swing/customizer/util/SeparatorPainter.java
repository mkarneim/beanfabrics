/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.util;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

/**
 * The <code>SeparatorPainter</code> is a utility class for painting a
 * horizontal line.
 * 
 * @author Michael Karneim
 */
class SeparatorPainter {
    public static void paint(JComponent comp, Graphics g, String text) {
        paint(comp, g, text, 0, 0);
    }

    public static void paint(JComponent comp, Graphics g, String text, int xOffset, int yOffset) {
        FontMetrics fm = comp.getFontMetrics(comp.getFont());
        if (fm != null) {
            Rectangle2D d = fm.getStringBounds(text, g);
            if (d == null)
                return;
            int w = (int)d.getWidth();
            int h = comp.getHeight() / 2;
            g.setColor(Color.gray);
            g.drawLine(w + 3 + xOffset, h + yOffset, comp.getWidth(), h + yOffset);
            g.setColor(Color.white);
            g.drawLine(w + 3 + xOffset, h + yOffset + 1, comp.getWidth(), h + yOffset + 1);

        }
    }
}