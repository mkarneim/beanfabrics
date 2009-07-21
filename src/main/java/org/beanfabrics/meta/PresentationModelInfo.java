/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.meta;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.util.GenericsUtil;

/**
 * @author Michael Karneim
 */
public class PresentationModelInfo {

    private Collection<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();
    private final String name;
    private final Class<? extends PresentationModel> cls;

    public PresentationModelInfo(Class<? extends PresentationModel> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("cls==null");
        }
        this.cls = cls;
        this.name = this.cls.getName();
    }

    public Class<? extends PresentationModel> getJavaType() {
        return this.cls;
    }

    public String getName() {
        return name;
    }

    public Collection<PropertyInfo> getProperties() {
        return propertyInfos;
    }

    public boolean hasProperties() {
        return this.propertyInfos != null && !this.propertyInfos.isEmpty();
    }

    void setProperties(Collection<PropertyInfo> propertyInfos) {
        this.propertyInfos = Collections.unmodifiableCollection(propertyInfos);
    }

    public PropertyInfo getProperty(String name) {
        for (PropertyInfo pDesc : propertyInfos) {
            if (pDesc.getName().equals(name)) {
                return pDesc;
            }
        }
        return null;
    }

    public boolean isAssignableFrom(PresentationModelInfo otherModelInfo) {
        return this.getJavaType().isAssignableFrom(otherModelInfo.getJavaType());
    }

    // TODO (mk/generic) change method return type to Collection<Type>
    public Type[] getTypeArguments(Class genericClass) {
        return (Type[])GenericsUtil.getTypeArguments(genericClass, this.cls).toArray(new Type[0]);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cls == null) ? 0 : cls.hashCode());
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
        PresentationModelInfo other = (PresentationModelInfo)obj;
        if (cls == null) {
            if (other.cls != null)
                return false;
        } else if (cls != other.cls)
            return false;
        return true;
    }

}