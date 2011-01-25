/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.table;

import org.beanfabrics.Path;

/**
 * The <code>BnColumn</code> is a configuration object used in a {@link BnTable}
 * to configure a column.
 * 
 * @author Michael Karneim
 */
public class BnColumn {
    public static final int DEFAULT_WIDTH = 100;
    public static final Integer DEFAULT_ALIGNEMNT = null;
    public static final boolean DEFAULT_WIDTH_FIXED = false;

    private final Path path;
    private final String columnName;
    private int width;
    private boolean widthFixed;
    private final Path operationPath;
    private final Integer alignment;

    public BnColumn() {
        this(new Path(), "empty column");
    }

    public BnColumn(Path path, String columnName) {
        this(path, columnName, DEFAULT_WIDTH, DEFAULT_WIDTH_FIXED);
    }

    public BnColumn(Path path, String columnName, int width) {
        this(path, columnName, width, DEFAULT_WIDTH_FIXED, null);
    }

    public BnColumn(Path path, String columnName, int width, boolean widthFixed) {
        this(path, columnName, width, widthFixed, null);
    }

    public BnColumn(Path path, String columnName, int width, boolean widthFixed, Path operationPath) {
        this(path, columnName, width, widthFixed, operationPath, DEFAULT_ALIGNEMNT);
    }

    public BnColumn(Path path, String columnName, int width, boolean widthFixed, Path operationPath, Integer alignment) {
        super();
        this.path = path;
        this.columnName = columnName;
        this.width = width;
        this.widthFixed = widthFixed;
        this.operationPath = operationPath;
        this.alignment = alignment;
    }

    public Path getPath() {
        return path;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getWidth() {
        return width;
    }

    public boolean isWidthFixed() {
        return widthFixed;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setWidthFixed(boolean b) {
        this.widthFixed = b;
    }

    public Path getOperationPath() {
        return this.operationPath;
    }

    public Integer getAlignment() {
        return alignment;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
        result = prime * result + ((operationPath == null) ? 0 : operationPath.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + width;
        result = prime * result + ((alignment == null) ? 0 : alignment.hashCode());
        result = prime * result + (widthFixed ? 1231 : 1237);
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BnColumn other = (BnColumn)obj;
        if (columnName == null) {
            if (other.columnName != null)
                return false;
        } else if (!columnName.equals(other.columnName))
            return false;
        if (operationPath == null) {
            if (other.operationPath != null)
                return false;
        } else if (!operationPath.equals(other.operationPath))
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (width != other.width)
            return false;
        if (alignment == null) {
            if (other.alignment != null)
                return false;
        } else if (!alignment.equals(other.alignment))
            return false;
        if (widthFixed != other.widthFixed)
            return false;
        return true;
    }

}