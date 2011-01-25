/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.model;

import org.beanfabrics.Path;

/**
 * The SortKey describes the sort order for a field accessible by a particular
 * {@link Path} relative to the element model.
 * 
 * @author Michael Karneim
 */
public class SortKey {

    private final Path sortPath;
    private final boolean ascending;

    /**
     * Creates a new <code>SortKey</code> for the specified {@link Path} with
     * the specified sort direction.
     * 
     * @param ascending the sort direction
     * @param sortPath the {@link Path} to the field relative to the element's
     *            root
     */
    public SortKey(boolean ascending, Path sortPath) {
        super();
        if (sortPath == null) {
            throw new IllegalArgumentException("sortPath==null");
        }
        this.ascending = ascending;
        this.sortPath = sortPath;
    }

    /**
     * Returns the sort path.
     * 
     * @return the sort path
     */
    public Path getSortPath() {
        return sortPath;
    }

    /**
     * Returns <code>true</code> if the sort direction is ascending,
     * <code>false</code> otherwise.
     * 
     * @return <code>true</code> if the sort direction is ascending,
     *         <code>false</code> otherwise
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (ascending ? 1231 : 1237);
        result = prime * result + ((sortPath == null) ? 0 : sortPath.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SortKey other = (SortKey)obj;
        if (ascending != other.ascending)
            return false;
        if (sortPath == null) {
            if (other.sortPath != null)
                return false;
        } else if (!sortPath.equals(other.sortPath))
            return false;
        return true;
    }
}