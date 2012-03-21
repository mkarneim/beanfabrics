/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.goodies.calendar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

/**
 * Instances of this class represent an icon displaying an arrow pointing up,
 * down, left or right.
 * 
 * @author Michael Karneim
 */
class ArrowIcon implements Icon {
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int DOWN = 3;
    public static final int RIGHT = 4;
    private static final int nPoints = 3;

    private int orientation;
    private Dimension preferredSize = new Dimension(10, 10);
    private int[] xPoints = new int[nPoints];
    private int[] yPoints = new int[nPoints];
    private Color color;

    /**
     * Creates an instance of ArrowIcon with the specified arrow orientation.
     * 
     * @param orientation defines the direction the arrow points to. Can be:
     *            ArrowIcon.UP, ArrowIcon.DOWN, ArrowIcon.LEFT, ArrowIcon.RIGHT
     */
    public ArrowIcon(int orientation) {
        this(orientation, Color.BLACK);
    }

    /**
     * Creates an instance of ArrowIcon with the specified arrow orientation.
     * 
     * @param orientation defines the direction the arrow points to. Can be:
     *            ArrowIcon.UP, ArrowIcon.DOWN, ArrowIcon.LEFT, ArrowIcon.RIGHT
     * @param color color to paint this arrow
     */
    public ArrowIcon(int orientation, Color color) {
        this.orientation = orientation;
        this.color = color;
    }

    /**
     * Sets the size of the icon to the given dimension
     * 
     * @param size the new size of the icon
     */

    public void setPreferredSize(Dimension size) {
        this.preferredSize = size;
    }

    /**
     * Returns the icon's height.
     * 
     * @return the icon's height
     */
    public int getIconHeight() {
        return this.preferredSize.height;
    }

    /**
     * Returns the icon's width.
     * 
     * @return the icon's width
     */
    public int getIconWidth() {
        return this.preferredSize.width;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Draws the icon at the specified location.
     * 
     * @param c the component on which the icon should be painted
     * @param g the Graphics instance that should be used to paint
     * @param x the x-position where the icon should to be painted
     * @param y the y-position where the icon should to be painted
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        int w = this.getIconWidth();
        int h = this.getIconHeight();
        this.recalculateShape(x, y, w, h);
        Object oldValueAntiAliasing = null;
        if (g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D)g;
            oldValueAntiAliasing = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, nPoints);
        if (g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldValueAntiAliasing);
        }
    }

    /**
     * Recalculates the shape of the arrow icon accoring to the specified
     * location and dimension.
     * 
     * @param x the x-position where the icon should to be painted
     * @param y the y-position where the icon should to be painted
     * @param w the width the icon should have
     * @param h the height the icon should have
     */
    protected void recalculateShape(int x, int y, int w, int h) {
        switch (this.orientation) {
            case UP:
                xPoints[0] = x;
                xPoints[1] = x + (w - 1) / 2;
                xPoints[2] = x + w - 1;
                yPoints[0] = y + h - 1;
                yPoints[1] = y;
                yPoints[2] = y + h - 1;
                break;
            case LEFT:
                xPoints[0] = x;
                xPoints[1] = x + w - 1;
                xPoints[2] = x + w - 1;
                yPoints[0] = y + (h - 1) / 2;
                yPoints[1] = y;
                yPoints[2] = y + h - 1;
                break;
            case DOWN:
                xPoints[0] = x;
                xPoints[1] = x + (w - 1) / 2;
                xPoints[2] = x + w - 1;
                yPoints[0] = y;
                yPoints[1] = y + h - 1;
                yPoints[2] = y;
                break;
            case RIGHT:
                xPoints[0] = x;
                xPoints[1] = x + w - 1;
                xPoints[2] = x;
                yPoints[0] = y;
                yPoints[1] = y + (h - 1) / 2;
                yPoints[2] = y + h - 1;
                break;
            default:
                break;
        }
    }
}
