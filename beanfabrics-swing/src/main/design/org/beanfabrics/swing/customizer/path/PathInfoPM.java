/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.path;

import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.beanfabrics.Path;
import org.beanfabrics.meta.PathElementInfo;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IconTextPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;

/**
 * The <code>PathInfoPM</code> is a presentation model presenting a
 * {@link PathElementInfo}.
 * 
 * @author Michael Karneim
 */
public class PathInfoPM extends AbstractPM {
    private static URL LEAF_ICON_URL = PathInfoPM.class.getResource("/org/beanfabrics/model/model16x16.gif");
    IconTextPM name = new IconTextPM();
    TextPM pathString = new TextPM();
    TextPM type = new TextPM();

    private PathElementInfo pathElementInfo;
    private Path pathObj;

    public PathInfoPM() {
        PMManager.setup(this);
        name.setEditable(false);
        pathString.setEditable(false);
        type.setEditable(false);
    }

    public PathElementInfo getPathElementInfo() {
        return pathElementInfo;
    }

    public void setPathElementInfo(PathElementInfo aPathElementInfo) {
        this.pathElementInfo = aPathElementInfo;
        this.pathObj = this.pathElementInfo.getPath();
        name.setText(aPathElementInfo.getName());
        pathString.setText(Path.getPathString(pathObj));
        name.setDescription(pathString.getText());
        String classname = this.pathElementInfo.getTypeInfo().getName();
        type.setText(getBasename(classname));
        type.setDescription(classname);
        if (aPathElementInfo.hasChildren()) {
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