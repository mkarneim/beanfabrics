/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.path;

import org.beanfabrics.Path;
import org.beanfabrics.meta.PathInfo;
import org.beanfabrics.meta.PresentationModelInfo;

/**
 * @author Michael Karneim
 */
public class PathContext {
    public final PathInfo root;
    public final PresentationModelInfo requiredModelInfo;
    public final Path initialPath;

    public PathContext(PathInfo root, PresentationModelInfo requiredModelInfo, Path initialPath) {
        super();
        this.initialPath = initialPath;
        this.requiredModelInfo = requiredModelInfo;
        this.root = root;
    }

}