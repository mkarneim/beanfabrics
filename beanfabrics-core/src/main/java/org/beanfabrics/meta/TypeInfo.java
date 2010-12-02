/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.meta;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.beanfabrics.model.PresentationModel;

/**
 * The {@link TypeInfo} represents the structural meta data of a specific
 * {@link PresentationModel} class.
 * 
 * @author Michael Karneim
 */
public class TypeInfo {

    private final Collection<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();
    private final Collection<PropertyInfo> unmodifiablePropertyInfos = Collections.unmodifiableCollection(propertyInfos);
    private final String name;
    private final Class<? extends PresentationModel> javaType;

    /**
     * Constructs an empty {@link TypeInfo} for the given model class. Use
     * {@link #addProperty(String, Member, TypeInfo)} to add meta data for each
     * property of the model class.
     * 
     * @param aModelClass
     */
    TypeInfo(Class<? extends PresentationModel> aModelClass) {
        if (aModelClass == null) {
            throw new IllegalArgumentException("aModelClass==null");
        }
        javaType = aModelClass;
        name = this.javaType.getName();
    }

    /**
     * Adds a new {@link PropertyInfo} with the given attributes.
     * 
     * @param name the name of that property
     * @param member the java member that defined that property (either a field
     *            or (getter)-method)
     * @param type the type description of that property
     */
    void addProperty(String name, Member member, TypeInfo type) {
        propertyInfos.add(new PropertyInfo(this, name, member, type));
    }

    /**
     * Returns the Java type of the model class.
     * 
     * @return the Java type of the model class
     */
    public Class<? extends PresentationModel> getJavaType() {
        return javaType;
    }

    /**
     * Returns the Java classname of the model class.
     * 
     * @return the Java classname of the model class
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an unmodifiable collection of all properties belonging to in this
     * type info.
     * 
     * @return an unmodifiable collection of all properties
     */
    public Collection<PropertyInfo> getProperties() {
        return unmodifiablePropertyInfos;
    }

    /**
     * Returns whether this type info contains any properties.
     * 
     * @return <code>true</code> if this type info contains any properties,
     *         otherwise <code>false</code>.
     */
    public boolean hasProperties() {
        return propertyInfos != null && !propertyInfos.isEmpty();
    }

    /**
     * Returns the {@link PropertyInfo} for the property with the given name.
     * 
     * @param name the name of the property
     * @return the {@link PropertyInfo} for the property with the given name, or
     *         <code>null</code> if this type info contains no property with
     *         that name.
     */
    public PropertyInfo getProperty(String name) {
        for (PropertyInfo pDesc : propertyInfos) {
            if (pDesc.getName().equals(name)) {
                return pDesc;
            }
        }
        return null;
    }

    /**
     * Returns whether the Java type of this type info is assignable from the
     * Java type of the given other type info.
     * <p>
     * The result of this method is <code>true</code> only if
     * <code>this.getJavaType().isAssignableFrom(otherModelType.getJavaType())</code>.
     * 
     * @param otherTypeInfo
     * @return whether the Java type of this type info is assignable from the
     *         Java type of the given other type info
     */
    public boolean isAssignableFrom(TypeInfo otherTypeInfo) {
        return this.getJavaType().isAssignableFrom(otherTypeInfo.getJavaType());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((javaType == null) ? 0 : javaType.hashCode());
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
        TypeInfo other = (TypeInfo)obj;
        if (javaType == null) {
            if (other.javaType != null)
                return false;
        } else if (javaType != other.javaType)
            return false;
        return true;
    }

}