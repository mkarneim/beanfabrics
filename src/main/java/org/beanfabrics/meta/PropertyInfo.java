/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.meta;

import java.lang.reflect.Type;
import java.util.List;

import org.beanfabrics.util.GenericsUtil;

/**
 * @author Michael Karneim
 */
public class PropertyInfo {
    private PresentationModelInfo owner;
    private String name;
    private PresentationModelInfo type;

    public PropertyInfo(PresentationModelInfo owner, String name, PresentationModelInfo type) {
        super();
        this.owner = owner;
        this.name = name;
        this.type = type;
    }

    public PresentationModelInfo getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public PresentationModelInfo getType() {
        return type;
    }

    public Type[] getTypeArguments(Class genericClass) {
        List<Type> typeArgs = GenericsUtil.getFieldTypeArguments(genericClass, getOwner().getJavaType(), this.name);
        return (Type[])typeArgs.toArray(new Type[0]);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
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
        PropertyInfo other = (PropertyInfo)obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (owner == null) {
            if (other.owner != null)
                return false;
        } else if (!owner.equals(other.owner))
            return false;
        return true;
    }
}