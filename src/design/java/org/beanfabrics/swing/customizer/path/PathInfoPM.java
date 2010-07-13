/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.path;

import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.beanfabrics.Path;
import org.beanfabrics.meta.PathInfo;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IconTextPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;

/**
 * The <code>PathInfoPM</code> is a presentation model presenting a
 * {@link PathInfo}.
 * 
 * @author Michael Karneim
 */
public class PathInfoPM extends AbstractPM {
    private static URL LEAF_ICON_URL = PathInfoPM.class.getResource("/org/beanfabrics/model/model16x16.gif");
    IconTextPM name = new IconTextPM();
    TextPM pathString = new TextPM();
    TextPM type = new TextPM();

    private PathInfo pathInfo;
    private Path pathObj;

    public PathInfoPM() {
        PMManager.setup(this);
        name.setEditable(false);
        pathString.setEditable(false);
        type.setEditable(false);
    }

    public PathInfo getPathInfo() {
        return pathInfo;
    }

    public void setPathInfo(PathInfo aPathInfo) {
        this.pathInfo = aPathInfo;
        this.pathObj = this.pathInfo.getPath();
        name.setText(aPathInfo.getName());
        pathString.setText(Path.getPathString(pathObj));
        name.setDescription(pathString.getText());
        String classname = this.pathInfo.getModelInfo().getName();
        type.setText(getBasename(classname));
        type.setDescription(classname);
        if (aPathInfo.hasChildren()) {
            this.name.setIcon(UIManager.getIcon("Tree.closedIcon"));
        } else {
            this.name.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(LEAF_ICON_URL)));
        }
    }

    private static String getBasename(String classname) {
        int idx = classname.lastIndexOf('$');
        if (idx == -1) {
            idx = classname.lastIndexOf('.');
        }
        if (idx == -1) {
            return classname;
        } else {
            return classname.substring(idx + 1);
        }
    }
}