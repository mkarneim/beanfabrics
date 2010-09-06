/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
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
 * @author Michael Karneim
 */
public class PathInfo {
    private final PathInfo parent;
    private final PresentationModelInfo modelInfo;
    private final String name;
    private final Path path;
    private final GenericType genericType;

    PathInfo(String name, PresentationModelInfo type, PathInfo parent, GenericType aGenericType) {
        super();
        this.name = name;
        this.parent = parent;
        this.modelInfo = type;
        this.path = parent == null ? new Path() : Path.concat(this.parent.getPath(), new Path(name));
        this.genericType = aGenericType;
    }

    PathInfo(PresentationModelInfo type) {
        this(Path.THIS_PATH_ELEMENT, type, null, new GenericType(type.getJavaType()));
    }

    public PathInfo getParent() {
        return parent;
    }

    public PresentationModelInfo getModelInfo() {
        return modelInfo;
    }

    public String getName() {
        return name;
    }

    public boolean hasChildren() {
        return this.modelInfo.hasProperties();
    }

    private GenericType getGenericTypeOfChild(PropertyInfo prop) {
        if (prop.getMember() instanceof Field) {
            Field f = (Field)prop.getMember();
            GenericType result = genericType.getFieldType(f.getName());
            return result;
        } else if (prop.getMember() instanceof Method) {
            Method m = (Method)prop.getMember();
            GenericType result = genericType.getMethodReturnType(m.getName());
            return result;
        } else {
            throw new Error("Unexpected member type: " + prop.getMember().getClass().getName());
        }
    }

    public Collection<PathInfo> getChildren() {
        Collection<PropertyInfo> props = this.modelInfo.getProperties();
        List<PathInfo> result = new ArrayList<PathInfo>();
        for (PropertyInfo prop : props) {
            GenericType childGT = getGenericTypeOfChild(prop);
            PathInfo child = new PathInfo(prop.getName(), prop.getType(), this, childGT);
            result.add(child);
        }
        return result;
    }

    public PathInfo getChild(String name) {
        PropertyInfo prop = this.modelInfo.getProperty(name);
        if (prop == null) {
            return null;
        } else {
            GenericType childGT = getGenericTypeOfChild(prop);
            PathInfo child = new PathInfo(prop.getName(), prop.getType(), this, childGT);
            return child;
        }
    }

    public PathInfo getPathInfo(Path pathToChild) {
        if (pathToChild == null) {
            return null;
        } else if (this.path.equals(pathToChild)) {
            return this;
        } else {
            String nextChildName = pathToChild.getElement(0);
            PathInfo nodeDesc = this.getChild(nextChildName);
            if (nodeDesc == null) {
                return null;
            } else if (pathToChild.length() == 1) {
                return nodeDesc;
            } else {
                return nodeDesc.getPathInfo(pathToChild.getSubPath(1));
            }
        }
    }

    public Path getPath() {
        return this.path;
    }

    public GenericType getGenericType() {
        return genericType;
    }

    public PathInfo getRoot() {
        if (this.parent == null) {
            return this;
        } else {
            return this.parent.getRoot();
        }
    }

    //    /**
    //     * Returns the type arguments that are used to parameterize the given
    //     * generic class which must be a supertype of this children modelInfo. To
    //     * find the type arguments this methods tries first the node's indirect
    //     * reference (via parent and name) to it's property info and returns the
    //     * type arguments found there. If this can't be resolved it returns the type
    //     * arguments of this objects model info.
    //     * 
    //     * @param genericClass the Class of the generic type
    //     * @return the type arguments that are used to parameterize the given
    //     *         generic class
    //     */
    //    public Type[] getTypeArguments(Class genericClass) {
    //        // The best way is to find the type argument for the given generic class
    //        // is by navigating via the parent
    //        // to the respective property info since it holds the most accurate static type info.
    //        PathInfo parentPathInfo = this.getParent();
    //        if (parentPathInfo == null) {
    //            // nope - this is the topmost node
    //            // but we still can access the type arguments of the model info
    //            PresentationModelInfo modelInfo = this.getModelInfo();
    //            Type[] result = modelInfo.getTypeArguments(genericClass);
    //            return result;
    //        } else {
    //            // fine. We now can navigate to the property info
    //            String propName = this.getName();
    //            PropertyInfo propInfo = parentPathInfo.getModelInfo().getProperty(propName);
    //            if (propInfo == null) {
    //                throw new Error("Unexpected error: can't find property info for '" + propName + "' in " + parentPathInfo.getName());
    //            } else {
    //                Type[] result = propInfo.getTypeArguments(genericClass);
    //                return result;
    //            }
    //        }
    //    }

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
        PathInfo other = (PathInfo)obj;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        return true;
    }
}