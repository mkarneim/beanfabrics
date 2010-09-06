/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.support.PropertySupport;
import org.beanfabrics.support.PropertySupport.PropertyDeclaration;

/**
 * The <code>MetadataRegistry</code> is a registry for presentation model meta
 * data.
 * 
 * @author Michael Karneim
 */
public class MetadataRegistry {

    public MetadataRegistry() {
        super();
    }

    private final Map<Class<? extends PresentationModel>, PresentationModelInfo> modelInfos = new HashMap<Class<? extends PresentationModel>, PresentationModelInfo>();

    public PresentationModelInfo getPresentationModelInfo(Class<? extends PresentationModel> modelClass) {
        PresentationModelInfo result = modelInfos.get(modelClass);
        if (result == null) {
            result = createPresentationModelInfo(modelClass);
        }
        return result;
    }

    public PathInfo getPathInfo(Class<? extends PresentationModel> modelClass) {
        if (modelClass == null) {
            throw new IllegalArgumentException("modelClass==null");
        }
        PresentationModelInfo typeDesc = getPresentationModelInfo(modelClass);
        return new PathInfo(typeDesc);
    }

    public PathInfo getPathInfo(PresentationModelInfo modelInfo) {
        if (modelInfo == null) {
            throw new IllegalArgumentException("modelInfo==null");
        }
        return new PathInfo(modelInfo);
    }

    private PresentationModelInfo createPresentationModelInfo(Class<? extends PresentationModel> modelClass) {
        if (modelClass == null) {
            throw new IllegalArgumentException("modelClass==null");
        }
        PresentationModelInfo result = new PresentationModelInfo(modelClass);
        modelInfos.put(modelClass, result);
        List<PropertyInfo> propertyInfos = createPropertyInfos(modelClass, result);
        result.setProperties(propertyInfos);
        return result;
    }

    private List<PropertyInfo> createPropertyInfos(Class<? extends PresentationModel> modelClass, PresentationModelInfo modelInfo) {
        List<PropertyDeclaration> propDecls = PropertySupport.findAllPropertyDeclarations(modelClass);
        List<PropertyInfo> result = new ArrayList<PropertyInfo>(propDecls.size());
        for (PropertySupport.PropertyDeclaration decl : propDecls) {
            String name = decl.getName();
            PresentationModelInfo typeDesc = getPresentationModelInfo(decl.getType());
            result.add(new PropertyInfo(modelInfo, name, decl.getMember(), typeDesc));
        }
        return result;
    }
}