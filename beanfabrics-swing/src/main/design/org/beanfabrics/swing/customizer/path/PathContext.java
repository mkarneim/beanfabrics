/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.path;

import org.beanfabrics.meta.PathNode;
import org.beanfabrics.meta.TypeInfo;

/**
 * @author Michael Karneim
 */
public class PathContext {
    public final PathNode root;
    public final TypeInfo requiredModelTypeInfo;

    public PathContext(PathNode rootPathNode, TypeInfo requiredModelTypeInfo) {
        super();
        this.requiredModelTypeInfo = requiredModelTypeInfo;
        this.root = rootPathNode;
    }

}