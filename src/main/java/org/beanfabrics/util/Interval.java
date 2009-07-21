/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * The <code>Interval</code> represents a closed interval of integer numbers.
 * 
 * @author Michael Karneim
 */
public class Interval {
    public final int startIndex;

    public final int endIndex;

    public Interval(final int startIndex, final int endIndex) {
        if (startIndex > endIndex) {
            throw new IllegalArgumentException("startIndex>endIndex");
        }
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    // TODO (mk) write TEST
    public boolean intersects(Interval other) {
        if (this.startIndex <= other.startIndex && other.startIndex <= this.endIndex) {
            return true;
        }
        if (this.startIndex <= other.endIndex && other.endIndex <= this.endIndex) {
            return true;
        }
        if (other.startIndex <= this.startIndex && this.startIndex <= other.endIndex) {
            return true;
        }
        if (other.startIndex <= this.endIndex && this.endIndex <= other.endIndex) {
            return true;
        }
        return false;
    }

    public boolean intersects(Collection<Interval> col) {
        for (Interval i : col) {
            if (this.intersects(i)) {
                return true;
            }
        }
        return false;
    }

    // STATIC //
    public static Interval[] createIntervals(Collection<Integer> indices) {
        int[] array = new int[indices.size()];
        int i = 0;
        for (Integer index : indices) {
            array[i++] = index;
        }
        return createIntervals(array);
    }

    public static Interval[] createIntervals(int[] indices) {
        Arrays.sort(indices);

        List<Interval> result = new LinkedList<Interval>();
        Integer startIndex = null;
        Integer lastIndex = null;
        for (int i = 0; i < indices.length; ++i) {
            if (startIndex == null) {
                startIndex = indices[i];
                lastIndex = startIndex;
            } else {
                if (indices[i] != lastIndex + 1) {
                    Interval newInterval = new Interval(startIndex, lastIndex);
                    result.add(newInterval);
                    startIndex = indices[i];
                    lastIndex = startIndex;
                } else {
                    lastIndex = indices[i];
                }
            }
        }
        if (startIndex != null) {
            Interval newInterval = new Interval(startIndex, lastIndex);
            result.add(newInterval);
        }
        return result.toArray(new Interval[result.size()]);
    }

    public String toString() {
        return "[" + startIndex + "," + endIndex + "]";
    }
}