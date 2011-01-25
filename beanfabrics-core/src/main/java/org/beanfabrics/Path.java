/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Immutable representation of a path inside a presentation object model.
 * 
 * @author Michael Karneim
 */
// TODO (mk) we need some methods for design time purpose, e.g.
// TODO (mk) make Path allow multiple results like XPath
// getting all path elements by using sample instances of (null)
// targets in EditorProperty objects
public class Path implements Iterable<String> {
    public static final String THIS_PATH_ELEMENT = "this";
    public static final String PATH_SEPARATOR = ".";
    public static final char PATH_SEPARATOR_CHAR = '.';

    /**
     * the elements of this path. Do not modify it's content after creation.
     */
    private final String[] elements;
    private final String pathStr;
    private final int hashCode;

    /**
     * Creates a new identity ("this") path.
     */
    public Path() {
        this(THIS_PATH_ELEMENT);
    }

    /**
     * Creates a new path from the given path string.
     * 
     * @param pathStr a string of path elements delimited by the dot "."
     *            character.
     * @throws IllegalArgumentException
     */
    public Path(String pathStr)
        throws IllegalArgumentException {
        if (pathStr == null || pathStr == "") {
            throw new IllegalArgumentException("pathStr==null");
        }
        StringTokenizer st = new StringTokenizer(pathStr, PATH_SEPARATOR);
        int len = st.countTokens();
        if (len == 0) {
            throw new IllegalArgumentException("pathStr must contain at least one element but was '" + pathStr + "'");
        }
        List<String> elementsList = new LinkedList<String>();
        int index = 0;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (THIS_PATH_ELEMENT.equals(token)) {
                if (index == 0) {
                    // skip "this"
                } else {
                    throw new IllegalArgumentException("only first element of path can be be 'this'");
                }

            } else {
                elementsList.add(token);
            }
            index++;
        }
        if (pathStr.endsWith(PATH_SEPARATOR)) {
            // TODO (mk) why is this allowed? Why adding an empty element? We should throw an exception instead!?!
            elementsList.add(""); // add empty element to the end
        }
        this.elements = elementsList.toArray(new String[elementsList.size()]);
        this.pathStr = pathStr;
        this.hashCode = calculateHashCode(elements);
    }

    private Path(String[] elements, String pathStr) {
        if (elements == null) {
            throw new IllegalArgumentException("elements==null");
        }
        this.elements = elements;
        this.pathStr = pathStr;
        this.hashCode = calculateHashCode(elements);
    }

    /**
     * Returns the path to the parent node of this path.
     * 
     * @return the path to the parent node of this path
     */
    public Path getParent() {
        if (this.length() > 0) {
            return getSubPath(0, this.length() - 1);
        } else {
            return null;
        }
    }

    /**
     * Returns a new path that is a subpath of this path. The subpath begins
     * with the element at the specified <code>fromPosition</code> and extends
     * to the end of this path. <pM>
     * 
     * @param fromPosition returned path's first character's position in this
     *            path
     * @return a new path that is a subpath of this path
     * @throws IllegalArgumentException
     */
    public Path getSubPath(int fromPosition)
        throws IllegalArgumentException {
        int length = this.length() - fromPosition;
        return this.getSubPath(fromPosition, length);
    }

    /**
     * Returns a new path that is a subpath of this path. The subpath begins at
     * the specified <code>fromPosition</code> and the length of the subpath is
     * <code>length</code>.
     * 
     * @param fromPosition returned path's first character's position in this
     *            path
     * @param length returned path's number of characters
     * @return a new path that is a subpath of this path
     * @throws IllegalArgumentException thrown if <code>length < 0</code> or
     *             <code>fromPosition + length > length</code>
     */
    public Path getSubPath(int fromPosition, int length)
        throws IllegalArgumentException {
        if (length < 0) {
            throw new IllegalArgumentException("length must not be less than 0 but was " + length);
        }
        if (fromPosition + length > length()) {
            throw new IllegalArgumentException("length must not be greater than " + (fromPosition + length()) + " but was " + length);
        }
        String[] newElements = new String[length];
        System.arraycopy(elements, fromPosition, newElements, 0, length);
        boolean prefixWithTHIS = fromPosition == 0 && this.pathStr.startsWith(THIS_PATH_ELEMENT) && this.length() > 0;
        String newPathStr = toPathString(newElements, prefixWithTHIS);
        return new Path(newElements, newPathStr);
    }

    /**
     * Returns the length of this path. The length is the distance from the root
     * node to the target node. Therefor the length of the identity ("this")
     * path is 0.
     * 
     * @return the length of this path
     */
    public int length() {
        return elements.length;
    }

    /**
     * Returns an iterator over this path's elements.
     */
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            final String[] elements = Path.this.elements;
            int nextIndex = 0;

            public boolean hasNext() {
                return nextIndex < elements.length;
            }

            public String next() {
                if (hasNext() == false) {
                    throw new NoSuchElementException();
                }
                return elements[nextIndex++];
            }

            public void remove() {
                throw new UnsupportedOperationException("remove is not supported by this iterator");
            }
        };
    }

    /**
     * Returns a collection of this path's elements.
     * 
     * @return a collection of this path's elements
     */
    public Collection<String> getElements() {
        List<String> result = Arrays.asList(this.elements);
        return result;
    }

    /**
     * Returns the element at the specified index.
     * 
     * @param index
     * @return
     */
    public String getElement(int index) {
        return this.elements[index];
    }

    /**
     * Returns the last element of this path or <code>null</code> if there are
     * no elements.
     * 
     * @return the last element of this path or <code>null</code> if there are
     *         no elements
     */
    public String getLastElement() {
        if (elements.length == 0) {
            return null;
        } else {
            return elements[elements.length - 1];
        }
    }

    /**
     * Returns the canonical String representation of this path.
     * 
     * @return the canonical String representation of this path
     */
    public String toString() {
        return this.getPathString();
    }

    /**
     * Returns the canonical String representation of this path.
     * 
     * @return the canonical String representation of this path
     */
    public String getPathString() {
        return this.pathStr;
    }

    /**
     * Returns the hash code of this path.
     */
    public int hashCode() {
        return this.hashCode;
    }

    /**
     * Indicates whether some other object is "equal to" this path.
     * 
     * @return <code>true</code> if the obj argument is a {@link Path} and has
     *         the equal elements as this path
     */
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass().equals(this.getClass()) == false) {
            return false;
        } else {
            Path otherPath = (Path)obj;
            if (this.hashCode != otherPath.hashCode || this.elements.length != otherPath.elements.length) {
                return false;
            }
            for (int i = 0; i < elements.length; ++i) {
                if (this.elements[i].equals(otherPath.elements[i]) == false) {
                    return false;
                }
            }
            return true;
        }
    }

    // STATIC METHODS //
    /**
     * Returns a Path object that represents a path defined by the given path
     * string, or <code>null</code> if the path string is <code>null</code>.
     * 
     * @return a Path object that represents a path defined by the given path
     *         string
     */
    public static Path parse(String pathStr) {
        if (pathStr == null || pathStr.trim().length() == 0) {
            return null;
        }
        return new Path(pathStr);
    }

    /**
     * Returns the canonical path string of the given path object, or
     * <code>null</code> if the path object is <code>null</code>.
     * 
     * @param path
     * @return the canonical path string of the given path object
     */
    public static String getPathString(Path path) {
        if (path == null) {
            return null;
        } else {
            return path.pathStr;
        }
    }

    /**
     * Concatenates the given path objects to a new path.
     * 
     * @param paths
     * @return the concatenated path
     */
    public static Path concat(Path... paths) {
        if (paths == null) {
            throw new IllegalArgumentException("paths==null");
        }
        if (paths.length == 0) {
            return null;
        }
        Path first = paths[0];
        boolean prefixWithTHIS = first == null || first.pathStr.startsWith(THIS_PATH_ELEMENT);
        List<String> elements = new LinkedList<String>();
        for (Path p : paths) {
            if (p != null) {
                elements.addAll(p.getElements());
            }
        }
        String[] elemArr = elements.toArray(new String[elements.size()]);
        String pathStr = toPathString(elemArr, prefixWithTHIS);
        return new Path(elemArr, pathStr);
    }

    /**
     * Concatenates the given strings to a path string delimited by dot '.'
     * characters.
     * 
     * @param listOfStrings
     * @return the concatenated strings delimited by dot characters
     */
    public static String toPathString(List<String> listOfStrings) {
        if (listOfStrings == null) {
            throw new IllegalArgumentException("pathElements==null");
        }
        return toPathString((String[])listOfStrings.toArray(new String[listOfStrings.size()]));
    }

    /**
     * Concatenates the given strings to a path string delimited by dot '.'
     * characters.
     * 
     * @param strings
     * @return the concatenated strings delimited by dot characters
     */
    public static String toPathString(String[] strings) {
        return toPathString(strings, true);
    }

    /**
     * Concatenates the given strings to a path string delimited by dot '.'
     * characters.
     * 
     * @param strings
     * @param alwaysPrefixWithTHIS defines that the result string has to start
     *            with the identity ("this") element.
     * @return the concatenated strings delimited by dot characters
     */
    private static String toPathString(String[] strings, boolean alwaysPrefixWithTHIS) {
        if (strings == null) {
            throw new IllegalArgumentException("elements==null");
        }
        StringBuilder sb = new StringBuilder();
        if (strings.length == 0) {
            return THIS_PATH_ELEMENT;
        }
        for (int i = 0; i < strings.length; ++i) {
            if (sb.length() > 0) {
                sb.append(PATH_SEPARATOR);
            } else if (alwaysPrefixWithTHIS) {
                sb.append(THIS_PATH_ELEMENT).append(PATH_SEPARATOR);
            }
            sb.append(strings[i]);
        }
        return sb.toString();
    }

    private static int calculateHashCode(String[] elements) {
        return toPathString(elements).hashCode();
    }
}