/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.meta;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * The {@link PropertyInfo} is component of a {@link TypeInfo}. It represents
 * meta information about a specific property of the corresponding model class.
 * A property is either mapped to a {@link Field} or a Getter-{@link Method}.
 * 
 * @author Michael Karneim
 */
public class PropertyInfo {
    private final TypeInfo owner;
    private final String name;
    private final TypeInfo type;
    private final Member member;

    /**
     * Constructs a {@link PropertyInfo} with the given attributes.
     * 
     * @param owner the type info this info is part of
     * @param name the name of the represented property
     * @param member the java member that defined the represented property
     *            (either a field or (getter)-method)
     * @param typeInfo the model type of the represented property
     */
    PropertyInfo(TypeInfo owner, String name, Member member, TypeInfo typeInfo) {
        super();
        this.owner = owner;
        this.name = name;
        this.member = member;
        this.type = typeInfo;
    }

    /**
     * Returns the type info this info is part of.
     * 
     * @return the type info this info is part of
     */
    public TypeInfo getOwner() {
        return owner;
    }

    /**
     * Returns the java member that defined the represented property (either a
     * {@link Field} or (getter)-{@link Method}).
     * 
     * @return the java member that defined the represented property
     */
    public Member getMember() {
        return member;
    }

    /**
     * Returns the name of the represented property.
     * 
     * @return the name of the represented property
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type info of the represented property.
     * 
     * @return the model type of the represented property
     */
    public TypeInfo getTypeInfo() {
        return type;
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