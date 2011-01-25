/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;

import org.beanfabrics.model.PresentationModel;

/**
 * @author Michael Karneim
 */
class Properties {
    private final PropertiesListener listener;
    private final LinkedHashSet<String> names = new LinkedHashSet<String>();
    private final Map<String, PresentationModel> valueMap = new HashMap<String, PresentationModel>();
    private final Map<String, Class<? extends PresentationModel>> typeMap = new HashMap<String, Class<? extends PresentationModel>>();

    public Properties() {
        this(null);
    }

    public Properties(PropertiesListener listener) {
        super();
        this.listener = listener;
    }

    public PresentationModel put(String name, PresentationModel value, Class<? extends PresentationModel> type) {
        names.add(name);
        typeMap.put(name, type);
        PresentationModel oldValue = valueMap.put(name, value);
        if (oldValue != value) {
            onChange(name, oldValue, value);
        }
        return oldValue;
    }

    public void put(String name, Class<? extends PresentationModel> type) {
        names.add(name);
        typeMap.put(name, type);
    }

    public LinkedHashSet<String> names() {
        return names;
    }

    public PresentationModel get(String name) {
        return valueMap.get(name);
    }

    public String getName(PresentationModel value) {
        for (Map.Entry<String, PresentationModel> entry : valueMap.entrySet()) {
            if (value == entry.getValue()) {
                return entry.getKey();
            }
        }
        return null; // TODO (mk) better to throw IllegalArgumentException
        // ??
    }

    public Class<? extends PresentationModel> getType(String name) {
        return typeMap.get(name);
    }

    public Collection<PresentationModel> models() {
        return models(false);
    }

    public Collection<PresentationModel> models(boolean skipNullValues) {
        Collection<PresentationModel> result = new LinkedList<PresentationModel>();
        for (String name : names) {
            PresentationModel value = valueMap.get(name);
            if (skipNullValues && value == null) {
                continue;
            }
            result.add(value);
        }
        return result;
    }

    public PresentationModel remove(String name) {
        PresentationModel result = valueMap.remove(name);
        typeMap.remove(name);
        names.remove(name);
        onChange(name, result, null);
        return result;
    }

    private void onChange(String name, PresentationModel oldValue, PresentationModel newValue) {
        if (listener != null) {
            listener.changed(name, oldValue, newValue);
        }
    }
}