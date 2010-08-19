/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

/**
 * The {@link ErrorIconPainter} is a helper class for painting an error icon on
 * top of any component.
 * 
 * @author Michael Karneim
 */
public class ErrorIconPainter {

    private static final URL ERROR_ICON = ErrorIconPainter.class.getResource("error_overlay.gif"); // TODO configure via file
    private static final URL ERROR_ICON_FOCUSED = ErrorIconPainter.class.getResource("error_overlay.gif"); // TODO configure via file

    private ImageIcon errorIcon;
    private ImageIcon errorIconFocused;

    private Insets margin = new Insets(2, 4, 2, 4);
    private int horizontalAlignment = SwingConstants.RIGHT;
    private int verticalAlignment = SwingConstants.CENTER;
    private Point offset = new Point(0, 0);
    private Dimension dimension;

    /**
     * Creates a new {@link ErrorIconPainter} with the default settings.
     */
    public ErrorIconPainter() {
        errorIcon = new ImageIcon(ERROR_ICON);
        errorIconFocused = new ImageIcon(ERROR_ICON_FOCUSED);
        updateDimension();
    }

    /**
     * Returns the margin of the error icon.
     * 
     * @return
     */
    public Insets getMargin() {
        return margin;
    }

    /**
     * Sets the margin of the error icon.
     * 
     * @param margin
     */
    public void setMargin(Insets margin) {
        if (margin == null) {
            throw new IllegalArgumentException("margin==null");
        }
        this.margin = margin;
    }

    /**
     * Returns the horizontal alignment of the icon.
     * 
     * @return the horizontal alignment of the icon.
     */
    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    /**
     * Sets the horizontal alignment of the icon.
     * 
     * @param horizontalAlignment
     */
    public void setHorizontalAlignment(int horizontalAlignment) {
        checkHorizontalKey(horizontalAlignment);
        this.horizontalAlignment = horizontalAlignment;
    }

    /**
     * Checks if the given integer is a valid key for horizontal alignment and
     * throws an {@link IllegalArgumentException} if the key is invalid.
     * 
     * @param key
     */
    protected void checkHorizontalKey(int key) {
        if ((key == SwingConstants.LEFT) || (key == SwingConstants.CENTER) || (key == SwingConstants.RIGHT) || (key == SwingConstants.LEADING) || (key == SwingConstants.TRAILING)) {
            return;
        } else {
            throw new IllegalArgumentException("unknown value for horizontalAlignment: " + key);
        }
    }

    /**
     * Returns the vertical alignment of the icon.
     * 
     * @return the vertical alignment of the icon
     */
    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    /**
     * Sets the vertical alignment of the icon.
     * 
     * @param verticalAlignment
     */
    public void setVerticalAlignment(int verticalAlignment) {
        checkVerticalKey(verticalAlignment);
        this.verticalAlignment = verticalAlignment;
    }

    /**
     * Checks if the given integer is a valid key for vertical alignment and
     * throws an {@link IllegalArgumentException} if the key is invalid.
     * 
     * @param key
     */
    protected void checkVerticalKey(int key) {
        if ((key == SwingConstants.TOP) || key == SwingConstants.BOTTOM || key == SwingConstants.CENTER) {
            return;
        } else {
            throw new IllegalArgumentException("unknown value for verticalAlignment: " + key);
        }
    }

    /**
     * Returns the icon's offset position.
     * 
     * @return
     */
    public Point getOffset() {
        return offset;
    }

    /**
     * Sets the icon's offset position.
     * 
     * @param offset
     */
    public void setOffset(Point offset) {
        if (offset == null) {
            throw new IllegalArgumentException("offset == null");
        }
        this.offset = offset;
    }

    /**
     * Gets the size of the error icon.
     * 
     * @return size of the error icon.
     */
    public Dimension getDimension() {
        return this.dimension;
    }

    /**
     * Gets the error icon that is used when the component has no focus.
     * 
     * @return the error icon that is used when the component has no focus
     */
    public ImageIcon getErrorIcon() {
        return errorIcon;
    }

    /**
     * Sets the error icon that sould be rendered when the component has no
     * focus.
     * 
     * @param aErrorIcon
     */
    public void setErrorIcon(ImageIcon aErrorIcon) {
        if (aErrorIcon == null) {
            throw new IllegalArgumentException("aErrorIcon == null");
        }
        this.errorIcon = aErrorIcon;
        updateDimension();
    }

    /**
     * Returns the error icon that is used when the component has the focus.
     * 
     * @return the error icon that is used when the component has the focus
     */
    public ImageIcon getErrorIconFocused() {
        return errorIconFocused;
    }

    /**
     * Sets the error icon that sould be rendered when the component has the
     * focus.
     * 
     * @param aErrorIconFocused
     */
    public void setErrorIconFocused(ImageIcon aErrorIconFocused) {
        if (aErrorIconFocused == null) {
            throw new IllegalArgumentException("aErrorIconFocused == null");
        }
        this.errorIconFocused = aErrorIconFocused;
        updateDimension();
    }

    /**
     * Recalculates the size of the painting area.
     */
    private void updateDimension() {
        this.dimension = new Dimension(Math.max(errorIcon.getIconWidth(), errorIconFocused.getIconWidth()), Math.max(errorIcon.getIconHeight(), errorIconFocused.getIconHeight()));
    }

    /**
     * Paints the error icon onto the given {@link Graphics} object by using the
     * given component to calculate the icons loacation.
     * 
     * @param g
     * @param c
     */
    public void paint(Graphics g, Component c) {
        int x;
        if (horizontalAlignment == SwingConstants.RIGHT || horizontalAlignment == SwingConstants.TRAILING) {
            x = c.getWidth() - (dimension.width + margin.right) + offset.x;
        } else if (horizontalAlignment == SwingConstants.LEFT || horizontalAlignment == SwingConstants.LEADING) {
            x = margin.left + offset.x;
        } else if (horizontalAlignment == SwingConstants.CENTER) {
            x = (c.getWidth() - (dimension.width + margin.left + margin.right)) / 2 + margin.left + offset.x;
        } else {
            throw new IllegalStateException("Unexpected value for horizontalAlignment: " + horizontalAlignment);
        }
        int y;
        if (verticalAlignment == SwingConstants.BOTTOM) {
            y = c.getHeight() - (dimension.height + margin.bottom) + offset.y;
        } else if (verticalAlignment == SwingConstants.TOP) {
            y = margin.top + offset.y;
        } else if (verticalAlignment == SwingConstants.CENTER) {
            y = (c.getHeight() - (dimension.height + margin.top + margin.bottom)) / 2 + margin.top + offset.y;
        } else {
            throw new IllegalStateException("Unexpected value for verticalAlignment: " + verticalAlignment);
        }
        boolean hasFocus = c.hasFocus();
        paintErrorImage(g, x, y, hasFocus, c);
    }

    /**
     * Paints the error icon onto the given {@link Graphics} object.
     * 
     * @param g graphics object to paint to
     * @param x horizontal position of the top left corner of the error icon in
     *            {@link Graphics} <code>g</code>
     * @param y vertical position of the top left corner of the error icon in
     *            {@link Graphics} <code>g</code>
     * @param hasFocus <code>true</code> if the component has the focus of which
     *            the {@link Graphics} object <code>g</code> is from. If
     *            <code>true</code> {@link #ERROR_ICON_FOCUSED} is painted, else
     *            {@link #ERROR_ICON}
     * @param observer object to be notified as more of the image is converted
     */
    private void paintErrorImage(Graphics g, int x, int y, boolean hasFocus, ImageObserver observer) {
        if (g == null) {
            return;
        }
        ImageIcon icon = hasFocus ? errorIconFocused : errorIcon;
        g.drawImage(icon.getImage(), x, y, observer);
    }
}