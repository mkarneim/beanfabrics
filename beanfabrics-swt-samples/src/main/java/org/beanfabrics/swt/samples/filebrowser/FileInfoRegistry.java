/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swt.samples.filebrowser;

import java.io.File;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/**
 * The FileInfoRegistry is a helper class that offers OS specific information
 * for {@link File} objects. For instance it provides an {@link Image} object
 * for a given {@link File} showing the filesystem's file icon of that file.
 * <p>
 * Since there is no convenient support for this functionality in plain SWT this
 * class uses the Swing class {@link JFileChooser}. For converting a Swing
 * {@link Icon} to an SWT {@link Image} this class uses the
 * {@link ImageConverter} which is part of this package.
 * 
 * @author Michael Karneim
 */
public class FileInfoRegistry {
    private JFileChooser jfilechooser;
    private Map<Icon, Image> images = new HashMap<Icon, Image>();
    private ImageConverter imageConverter = new ImageConverter();

    public FileInfoRegistry() {
        this(true);
    }

    public FileInfoRegistry(boolean useSystemLookAndFeel) {
        if (useSystemLookAndFeel) {
            try {
                LookAndFeel oldLAF = UIManager.getLookAndFeel();
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                jfilechooser = new JFileChooser();
                UIManager.setLookAndFeel(oldLAF);
            } catch (Exception e) {
                throw new UndeclaredThrowableException(e, "Unexpected Failure");
            }
        }
    }

    public String getName(File file) {
        return jfilechooser.getName(file);
    }

    public Image getImage(File file) {
        Icon icon = jfilechooser.getIcon(file);
        Image result = images.get(icon);
        if (result == null) {
            result = createImage(icon);
            images.put(icon, result);
        }
        return result;
    }

    private Image createImage(Icon icon) {
        ImageData data = imageConverter.convertToImageData(icon);
        return new Image(null, data);
    }

    public void clear() {
        for (Image img : images.values()) {
            img.dispose();
        }
        images.clear();
    }
}