/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.meta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.beanfabrics.Path;
import org.beanfabrics.util.GenericType;

/**
 * The {@link PathTree} represents the meta data for all available {@link Path}s relative to a concrete root
 * {@link TypeInfo}. With a {@link PathTree} you can navigate from a specific node upwards until it's root node and
 * downwards until is't leaf nodes.
 * 
 * @author Michael Karneim
 */
public class PathTree {
    private final PathTree parent;
    private final TypeInfo modelType;
    private final String name;
    private final Path path;
    private final GenericType genericType;

    /**
     * Constructs a {@link PathTree} with the given attributes.
     * 
     * @param elementName
     *            the path element name
     * @param elementTypeInfo
     *            the type info of the element
     * @param parent
     *            the path element info of the parent element (may be <code>null</code>)
     * @param elementGenericType
     *            the generic type represented by this element
     */
    PathTree(String elementName, TypeInfo elementTypeInfo, PathTree parent, GenericType elementGenericType) {
        super();
        this.name = elementName;
        this.parent = parent;
        this.modelType = elementTypeInfo;
        this.path = parent == null ? new Path() : Path.concat(this.parent.getPath(), new Path(elementName));
        this.genericType = elementGenericType;
    }

    /**
     * Constructs a {@link PathTree} with the given type info as root element.
     * 
     * @param aTypeInfo
     */
    PathTree(TypeInfo aTypeInfo) {
        this(Path.THIS_PATH_ELEMENT, aTypeInfo, null, new GenericType(aTypeInfo.getJavaType()));
    }

    /**
     * Returns the path element info of this element's parent.
     * 
     * @return the path element info of this element's parent
     */
    public PathTree getParent() {
        return parent;
    }

    /**
     * Returns the type info of this element.
     * 
     * @return the type info of this element
     */
    public TypeInfo getTypeInfo() {
        return modelType;
    }

    /**
     * Returns the element name of this path element.
     * 
     * @return the element name of this path element
     */
    public String getName() {
        return name;
    }

    /**
     * Returns whether this element's type info has children.
     * 
     * @return whether this element's type info has children
     */
    public boolean hasChildren() {
        return this.modelType.hasProperties();
    }

    /**
     * Returns the path element infos for for all children.
     * 
     * @return the path element infos for for all children
     */
    public Collection<PathTree> getChildren() {
        Collection<PropertyInfo> props = this.modelType.getProperties();
        List<PathTree> result = new ArrayList<PathTree>();
        for (PropertyInfo prop : props) {
            GenericType childGT = getGenericTypeOfChild(prop);
            PathTree child = new PathTree(prop.getName(), prop.getTypeInfo(), this, childGT);
            result.add(child);
        }
        return result;
    }

    /**
     * Returns the path element info for the child with he given name.
     * 
     * @param name
     *            the property name of the child
     * @return the path element info for the child with he given name
     */
    public PathTree getChild(String name) {
        PropertyInfo prop = this.modelType.getProperty(name);
        if (prop == null) {
            return null;
        } else {
            GenericType childGT = getGenericTypeOfChild(prop);
            PathTree child = new PathTree(prop.getName(), prop.getTypeInfo(), this, childGT);
            return child;
        }
    }

    public PathTree asRoot() {
        return new PathTree("this", getTypeInfo(), null, getGenericType());
    }

    /**
     * Returns the path element info for the node at the end of the given path.
     * 
     * @param pathToNode
     * @return the path element info for the node at the end of the given path
     */
    public PathTree getPathInfo(Path pathToNode) {
        if (pathToNode == null) {
            return null;
        } else if (this.path.equals(pathToNode)) {
            return this;
        } else {
            String nextChildName = pathToNode.getElement(0);
            PathTree nodeDesc = this.getChild(nextChildName);
            if (nodeDesc == null) {
                return null;
            } else if (pathToNode.length() == 1) {
                return nodeDesc;
            } else {
                return nodeDesc.getPathInfo(pathToNode.getSubPath(1));
            }
        }
    }

    /**
     * Returns the relative path from the root element to this path element.
     * 
     * @return the relative path from the root element to this path element
     */
    public Path getPath() {
        return this.path;
    }

    /**
     * Returns the generic type representing the Java class of this path element.
     * 
     * @return the generic type representing the Java class of this path element
     */
    public GenericType getGenericType() {
        return genericType;
    }

    /**
     * Returns the root element.
     * 
     * @return the root element
     */
    public PathTree getRoot() {
        if (this.parent == null) {
            return this;
        } else {
            return this.parent.getRoot();
        }
    }

    /**
     * Returns the generic type of the child represented by the given property info.
     * 
     * @param childPropertyInfo
     *            the property info of the child
     * @return the generic type of the child represented by the given property info
     */
    private GenericType getGenericTypeOfChild(PropertyInfo childPropertyInfo) {
        if (childPropertyInfo.getMember() instanceof Field) {
            Field f = (Field) childPropertyInfo.getMember();
            GenericType result = genericType.getFieldType(f.getName());
            return result;
        } else if (childPropertyInfo.getMember() instanceof Method) {
            Method m = (Method) childPropertyInfo.getMember();
            GenericType result = genericType.getMethodReturnType(m.getName());
            return result;
        } else {
            throw new Error("Unexpected member type: " + childPropertyInfo.getMember().getClass().getName());
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PathTree other = (PathTree) obj;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        return true;
    }
}