/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.ImageObserver;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * Paints an error state image to a components Graphics object.
 * 
 * @author Michael Karneim
 * @author Max Gensthaler
 */
public class ErrorImagePainter {
    // private static final URL ERROR_ICON = ErrorImagePainter.class
    // .getResource("alert-16.png"); // TODO configure via file
    // private static final URL ERROR_ICON_FOCUSED = ErrorImagePainter.class
    // .getResource("alert-16alpha.png"); // TODO configure via file
    private static final URL ERROR_ICON = ErrorImagePainter.class.getResource("error_overlay.gif"); // TODO configure via file
    private static final URL ERROR_ICON_FOCUSED = ErrorImagePainter.class.getResource("error_overlay.gif"); // TODO configure via file

    private final ImageIcon errorIcon;
    private final ImageIcon errorIconFocused;

    private final Insets margin = new Insets(2, 4, 2, 4);
    private final Dimension dimension;

    private static final ErrorImagePainter INSTANCE = new ErrorImagePainter();

    public static ErrorImagePainter getInstance() {
        return INSTANCE;
    }

    private ErrorImagePainter() {
        errorIcon = new ImageIcon(ERROR_ICON);
        errorIconFocused = new ImageIcon(ERROR_ICON_FOCUSED);
        dimension = new Dimension(errorIcon.getIconHeight(), errorIcon.getIconWidth());
    }

    /**
     * Get the size of the error icon.
     * 
     * @return size of the error icon.
     */
    public Dimension getDimension() {
        return this.dimension;
    }

    /**
     * Get the margin of the error icon. This margin is/should always kept free
     * between the location where the error icon is painted and the border of
     * the component.
     * 
     * @return margin of the error icon.
     */
    public Insets getMargin() {
        return this.margin;
    }

    /**
     * Paints an error state image to the components to a vertically centered
     * position at the components horizontal trailing side.
     * 
     * @param g graphics object to paint to, should be the {@link Graphics} of
     *            {@link Component} c
     * @param c component to paint the icon for
     * @param isRightAligned <code>true</code> if the text in the
     *            {@link Component} c is right aligned, else <code>false</code>
     */
    public void paintTrailingErrorImage(Graphics g, Component c, boolean isRightAligned) {
        int x = isRightAligned ? margin.left : c.getWidth() - (dimension.width + margin.right);
        int y = (c.getHeight() - dimension.height) / 2;
        boolean hasFocus = c.hasFocus();
        paintErrorImage(g, x, y, hasFocus, c);
    }

    /**
     * Paints an error state image to a components Graphics object.
     * 
     * @param g graphics object to paint to
     * @param x horizontal position of the top left corner of the error icon in
     *            {@link Graphics} <code>g</code>
     * @param y vertical position of the top left corner of the error icon in
     *            {@link Graphics} <code>g</code>
     * @param hasFocus <code>true</code> if the component has the focus of which
     *            the {@link Graphics} object <code>g</code> is from. If
     *            <code>true</code> {@link #ERROR_ICON_ALPHA} is painted, else
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