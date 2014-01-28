/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.meta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.support.PropertySupport;
import org.beanfabrics.support.PropertySupport.PropertyDeclaration;

/**
 * The {@link MetadataRegistry} is a registry for presentation model meta data.
 * 
 * @author Michael Karneim
 */
public class MetadataRegistry {
    /**
     * Constructs a {@link MetadataRegistry}.
     */
    public MetadataRegistry() {
        super();
    }

    private final Map<Class<? extends PresentationModel>, TypeInfo> modelTypes = new HashMap<Class<? extends PresentationModel>, TypeInfo>();

    /**
     * Returns the type information for the given model class. If the type is
     * not already registered it is created and registered on-the-fly.
     * 
     * @param modelClass
     * @return the type information for the given model class
     */
    public TypeInfo getTypeInfo(Class<? extends PresentationModel> modelClass) {
        TypeInfo result = modelTypes.get(modelClass);
        if (result == null) {
            result = createTypeInfo(modelClass);
        }
        return result;
    }

    /**
     * Returns the path tree with the given model class as root node.
     * 
     * @param modelClass
     * @return the path tree with the given model class as root node
     */
    public PathNode getPathNode(Class<? extends PresentationModel> modelClass) {
        if (modelClass == null) {
            throw new IllegalArgumentException("modelClass==null");
        }
        TypeInfo typeDesc = getTypeInfo(modelClass);
        return new PathNode(typeDesc);
    }

    /**
     * Returns the path tree with the given type info as root element.
     * 
     * @param typeInfo
     * @return the path tree with the given type info as root element
     */
    public PathNode getPathNode(TypeInfo typeInfo) {
        if (typeInfo == null) {
            throw new IllegalArgumentException("modelType==null");
        }
        return new PathNode(typeInfo);
    }

    /**
     * Creates the type information of the given model class.
     * 
     * @param modelClass
     * @return the type information of the given model class
     */
    private TypeInfo createTypeInfo(Class<? extends PresentationModel> modelClass) {
        if (modelClass == null) {
            throw new IllegalArgumentException("modelClass==null");
        }
        TypeInfo result = new TypeInfo(modelClass);
        modelTypes.put(modelClass, result);

        List<PropertyDeclaration> propDecls = PropertySupport.getPropertyDeclarations(modelClass);
        for (PropertySupport.PropertyDeclaration decl : propDecls) {
            String name = decl.getName();
            TypeInfo typeDesc = getTypeInfo(decl.getType());
            result.addProperty(name, decl.getMember(), typeDesc);
        }
        return result;
    }

}