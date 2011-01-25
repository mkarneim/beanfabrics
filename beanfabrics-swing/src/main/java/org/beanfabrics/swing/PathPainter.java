/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.beanfabrics.Path;

/**
 * Experimental. Currently Unused.
 * 
 * @author Michael Karneim
 */
public class PathPainter {
    private Color foreground = new Color(125, 91, 47);
    private Color background = new Color(241, 204, 156);
    private Color borderColor = new Color(200, 151, 86);
    //	private Color foreground = new Color(80, 103, 135);
    //	private Color background = new Color(133, 175, 233);
    //	private Color borderColor = new Color(107, 136, 177);

    private Font font;
    private int verticalAlignment = SwingConstants.CENTER;
    private int horizontalAlignment = SwingConstants.LEADING;
    private int verticalTextPosition = SwingConstants.CENTER;
    private int horizontalTextPosition = SwingConstants.LEADING;
    private final Rectangle emptyR = new Rectangle(0, 0, 0, 0);

    public PathPainter() {
        Font basefont = UIManager.getLookAndFeelDefaults().getFont("Label.font");
        font = basefont.deriveFont(basefont.getSize2D() * 0.8f).deriveFont(Font.PLAIN);
    }

    public void paintPath(Graphics g, int x, int y, Path path, JComponent c) {
        if (path != null) {
            Rectangle viewR = c.getBounds();
            FontMetrics fm = c.getFontMetrics(font);
            Rectangle textR = new Rectangle(viewR.x, viewR.y, viewR.width - 20, viewR.height);
            String text = path.toString();

            String clippedText = layoutCL(c, fm, text, textR, textR);
            int dx = c.getInsets().left;
            int dy = c.getInsets().top;
            int h = fm.getHeight();
            int w = fm.stringWidth(clippedText);
            g.setColor(background);
            g.fillRect(x + dx, y + dy, w + 2, h);
            g.setColor(borderColor);
            g.drawRect(x + dx, y + dy, w + 2, h);
            g.setColor(foreground);
            g.setFont(font);
            g.drawString(clippedText, x + dy + 2, y + dy + h - 1);

        }
    }

    private String layoutCL(JComponent c, FontMetrics fontMetrics, String text, Rectangle viewR, Rectangle textR) {
        Rectangle iconR = emptyR;
        return SwingUtilities.layoutCompoundLabel(c, fontMetrics, text, /* icon */
        null, verticalAlignment, horizontalAlignment, verticalTextPosition, horizontalTextPosition, viewR, iconR, textR,
        /* textIconGap */0);
    }
}