/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.list;

import java.io.Serializable;

import org.beanfabrics.Path;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class CellConfig implements Serializable {

    private final Path path;

    public CellConfig() {
        this(new Path());
    }

    /**
     * Constructs a <code>CellConfig</code> with the given path.
     * 
     * @param path
     *            the path of the configured pM
     */
    public CellConfig(Path path) {
        super();
        if (path == null) {
            throw new IllegalArgumentException("path==null");
        }
        this.path = path;
    }

    /**
     * Returns the {@link Path} of the configured pM.
     * 
     * @return the path
     */
    public Path getPath() {
        return path;
    }
}