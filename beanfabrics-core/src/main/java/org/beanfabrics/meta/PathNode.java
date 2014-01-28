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
 * The {@link PathNode} represents the meta data for a specific node inside a PM's tree-like structure, reachable by a
 * given {@link Path} relative to a arbitratily defined root node.
 * <p>
 * With a {@link PathNode} you can navigate from a specific node upwards until it's defined root node and downwards
 * until it's leaf nodes.
 * <p>
 * Note: There is <i>no natural root node</i> for any given PM, because there is no top-level PM class. PM classes can
 * always be combined into greater structures. In this sense any node inside a PMs structure can be arbitrarily choosen
 * to be a root node.
 * 
 * @author Michael Karneim
 */
public class PathNode {
    private final PathNode parent;
    private final TypeInfo modelType;
    private final String name;
    private final Path path;
    private final GenericType genericType;

    /**
     * Constructs a {@link PathNode} with the given attributes.
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
    PathNode(String elementName, TypeInfo elementTypeInfo, PathNode parent, GenericType elementGenericType) {
        super();
        this.name = elementName;
        this.parent = parent;
        this.modelType = elementTypeInfo;
        this.path = parent == null ? new Path() : Path.concat(this.parent.getPath(), new Path(elementName));
        this.genericType = elementGenericType;
    }

    /**
     * Constructs a {@link PathNode} with the given type info as root element.
     * 
     * @param aTypeInfo
     */
    PathNode(TypeInfo aTypeInfo) {
        this(Path.THIS_PATH_ELEMENT, aTypeInfo, null, new GenericType(aTypeInfo.getJavaType()));
    }

    /**
     * Returns the path element info of this element's parent.
     * 
     * @return the path element info of this element's parent
     */
    public PathNode getParent() {
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
    public Collection<PathNode> getChildren() {
        Collection<PropertyInfo> props = this.modelType.getProperties();
        List<PathNode> result = new ArrayList<PathNode>();
        for (PropertyInfo prop : props) {
            GenericType childGT = getGenericTypeOfChild(prop);
            PathNode child = new PathNode(prop.getName(), prop.getTypeInfo(), this, childGT);
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
    public PathNode getChild(String name) {
        PropertyInfo prop = this.modelType.getProperty(name);
        if (prop == null) {
            return null;
        } else {
            GenericType childGT = getGenericTypeOfChild(prop);
            PathNode child = new PathNode(prop.getName(), prop.getTypeInfo(), this, childGT);
            return child;
        }
    }

    /**
     * Returns a new {@link PathNode} with this node defined as the new root node.
     * 
     * @return a new {@link PathNode} with this node defined as the new root node
     */
    public PathNode asRoot() {
        return new PathNode("this", getTypeInfo(), null, getGenericType());
    }

    /**
     * Returns the path element info for the node at the end of the given path.
     * 
     * @param pathToNode
     * @return the path element info for the node at the end of the given path
     */
    public PathNode getNode(Path pathToNode) {
        if (pathToNode == null) {
            return null;
        } else if (this.path.equals(pathToNode)) {
            return this;
        } else {
            String nextChildName = pathToNode.getElement(0);
            PathNode nodeDesc = this.getChild(nextChildName);
            if (nodeDesc == null) {
                return null;
            } else if (pathToNode.length() == 1) {
                return nodeDesc;
            } else {
                return nodeDesc.getNode(pathToNode.getSubPath(1));
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
    public PathNode getRoot() {
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
        PathNode other = (PathNode) obj;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        return true;
    }
}