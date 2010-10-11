/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.support;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.beanfabrics.Path;
import org.beanfabrics.PathEvaluation;
import org.beanfabrics.context.ContextOwner;
import org.beanfabrics.log.Logger;
import org.beanfabrics.log.LoggerFactory;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.model.PresentationModelFilter;
import org.beanfabrics.model.PresentationModelVisitor;
import org.beanfabrics.util.FilteredModels;
import org.beanfabrics.util.ReflectionUtil;

/**
 * @author Michael Karneim
 * @author Max Gensthaler
 */
public class PropertySupport implements Support {
    private final static Logger LOG = LoggerFactory.getLogger(PropertySupport.class);

    private static final String DEFAULT_NAME = "#default";
    private static final Map<Class, List<PropertyDeclaration>> DECLARATION_CACHE = new HashMap<Class, List<PropertyDeclaration>>();

    public static PropertySupport get(PresentationModel model) {
        Supportable s = (Supportable)model;

        PropertySupport support = s.getSupportMap().get(PropertySupport.class);
        if (support == null) {
            support = new PropertySupport(model);
            s.getSupportMap().put(PropertySupport.class, support);
        }
        return support;
    }

    private PropertyChangeListener modelListener;
    private Map<String, PropertyChangeListener> pclMap = new HashMap<String, PropertyChangeListener>();

    private final PresentationModel presentationModel;
    private final Properties properties = new Properties(new PropertiesListener() {

        /**
         * This method is called whenever a property is added or removed.
         */
        public void changed(String propertyName, PresentationModel oldValue, PresentationModel newValue) {
            if (oldValue != newValue) {
                onRemove(oldValue, propertyName);
                onAdd(newValue, propertyName);
                onPropertyChange(propertyName);
                presentationModel.getPropertyChangeSupport().firePropertyChange(propertyName, oldValue, newValue);
            }
        }

        private void onRemove(PresentationModel pModel, String name) {
            if (pModel == null) {
                return;
            } else {
                PropertyChangeListener pcListener = pclMap.remove(name);
                pModel.removePropertyChangeListener(pcListener);

                if (pModel instanceof ContextOwner && presentationModel instanceof ContextOwner) {
                    ((ContextOwner)pModel).getContext().removeParent(((ContextOwner)presentationModel).getContext());
                }
            }
        }

        private void onAdd(PresentationModel pModel, String name) {
            if (pModel == null) {
                return;
            } else {
                PropertyChangeListener pcListener = new MyPropertyChangeListener(name);
                pclMap.put(name, pcListener);
                pModel.addPropertyChangeListener(pcListener);

                if (pModel instanceof ContextOwner && presentationModel instanceof ContextOwner) {
                    ((ContextOwner)pModel).getContext().addParent(((ContextOwner)presentationModel).getContext());
                }
            }
        }
    });

    private void onPropertyChange(String propertyName) {
        // revalidate other properties
        List<IOperationPM> ops = new LinkedList<IOperationPM>();
        for (String name : properties.names()) {
            if (name.equals(propertyName)) {
                // skip; since we don't want to revalidate the property that has
                // been changed
                continue;
            }
            PresentationModel pModel = properties.get(name);

            if (pModel instanceof IOperationPM) {
                ops.add((IOperationPM)pModel); // remember it for later revalidation
            } else if (pModel != null) {
                pModel.revalidate();
            }

        }

        // now revalidate the operations
        for (IOperationPM op : ops) {
            op.revalidate();
        }

        // revalidate
        this.presentationModel.revalidate();
    }

    public PropertySupport(PresentationModel pModel) {
        if (pModel == null) {
            throw new IllegalArgumentException("pModel == null");
        }
        this.presentationModel = pModel;
    }

    public void refresh() {
        // TODO (mk) refresh should only update known properties...
        setup(presentationModel.getClass());
    }

    /**
     * Setup this <code>PropertySupport</code> by processing all
     * {@link Property} annotations found in the supported
     * {@link PresentationModel}.
     */
    public void setup() {
        setup(presentationModel.getClass());
    }

    /**
     * Setup this <code>PropertySupport</code> by processing only those
     * {@link Property} annotations that are found in the given {@link Class}
     * and all superclasses of the supported {@link PresentationModel}.
     * 
     * @param cls
     */
    public void setup(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException("cls==null");
        }
        putProperties(cls);
        addPropertyChangeListenerToPM();
    }

    /**
     * This method adds the {@link #modelListener} to the supported
     * {@link PresentationModel}.
     */
    private void addPropertyChangeListenerToPM() {
        if (modelListener == null) {
            modelListener = new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    // This method has been called when any of the models properties has changed,
                    // including 'conventional' bean properties.
                    // Since some properties validation rules could be dependent on these properties, we
                    // have to revalidate them.
                    revalidateProperties();
                }
            };
            this.presentationModel.addPropertyChangeListener(modelListener);
        }
    }

    void putProperties(Class cls) {
        Properties newProps = loadProperties(cls);
        // Set<String> oldNames = new HashSet<String>(this.properties.names());
        for (String name : newProps.names()) {
            // oldNames.remove(name);
            PresentationModel newValue = newProps.get(name);
            Class<? extends PresentationModel> type = newProps.getType(name);
            this.properties.put(name, newValue, type);

        }
        // for (String oldName : oldNames) {
        // this.properties.remove(oldName);
        // }
    }

    public Iterable<PresentationModel> filterProperties(final PresentationModelFilter filter) {
        return new FilteredModels(this.properties.models(), filter);
    }

    public Collection<PresentationModel> getProperties(PresentationModelFilter filter) {
        List<PresentationModel> result = new LinkedList<PresentationModel>();
        for (PresentationModel pModel : this.properties.models()) {
            if (filter == null || filter.accept(pModel)) {
                result.add(pModel);
            }
        }
        return result;
    }

    public Collection<String> getPropertyNames() {
        return properties.names();
    }

    public Class<? extends PresentationModel> getPropertyType(String name) {
        return properties.getType(name);
    }

    public String getName(PresentationModel child) {
        return properties.getName(child);
    }

    public void revalidateProperties() {
        for (PresentationModel pModel : this.properties.models(true)) {
            pModel.revalidate();
        }
    }

    public Collection<PresentationModel> getProperties() {
        return this.properties.models(true);
    }

    public PresentationModel getProperty(String name) {
        return properties.get(name);
    }

    public PresentationModel getProperty(Path path) {
        PathEvaluation eval = new PathEvaluation(this.presentationModel, path);
        PathEvaluation.Entry result = eval.getResult();
        return result.getValue();
    }

    public <M extends PresentationModel> M putProperty(String name, M value, Class<M> type) {
        this.properties.put(name, value, type);
        return value;
    }

    public PresentationModel removeProperty(String name) {
        return this.properties.remove(name);
    }

    public void accept(PresentationModelFilter filter, PresentationModelVisitor visitor) {
        if (visitor == null) {
            throw new IllegalArgumentException("visitor==null");
        }
        for (PresentationModel child : this.getProperties()) {
            if (filter == null || filter.accept(child)) {
                visitor.visit(child);
            }
            PropertySupport.get(child).accept(filter, visitor);
        }
    }

    public static abstract class PropertyDeclaration {
        private final Member member;
        private final String name;

        public PropertyDeclaration(String name, Member member) {
            super();
            if (name == null) {
                throw new IllegalArgumentException("name==null");
            }
            this.name = name;
            if (member == null) {
                throw new IllegalArgumentException("member==null");
            }
            this.member = member;
        }

        public String getName() {
            return name;
        }

        public Class<? extends PresentationModel> getType() {
            if (member instanceof Field) {
                return (Class<? extends PresentationModel>)((Field)member).getType();
            } else if (member instanceof Method) {
                return (Class<? extends PresentationModel>)((Method)member).getReturnType();
            } else {
                throw new Error("Unknown member type: " + member.getClass().getName());
            }
        }

        public Member getMember() {
            return member;
        }

        public boolean isAbstract() {
            if (member instanceof Method) {
                Method m = (Method)member;
                return Modifier.isAbstract(m.getModifiers());
            } else {
                return false;
            }
        }
    }

    public static class FieldDecl extends PropertyDeclaration {
        private final Field field;

        public FieldDecl(String name, Field field) {
            super(name, field);
            this.field = field;
        }

        Field getField() {
            return field;
        }

        @Override
        public String toString() {
            return "Name: " + super.getName() + "\tField:" + field.toString();
        }
    }

    public static class MethodDecl extends PropertyDeclaration {
        private final Method method;

        public MethodDecl(String name, Method method) {
            super(name, method);
            this.method = method;
        }

        Method getMethod() {
            return method;
        }

        @Override
        public String toString() {
            return "Name: " + super.getName() + "\tMethod: " + method.toString();
        }
    }

    public static List<PropertyDeclaration> findAllPropertyDeclarations(Class cls) {
        List<PropertyDeclaration> result = DECLARATION_CACHE.get(cls);
        if (result == null) {
            result = new LinkedList<PropertyDeclaration>();
            findAllPropertyDeclarations(cls, result);
            check(cls, result);
            log(cls, result);
            DECLARATION_CACHE.put(cls, result);
        }
        return result;
    }

    private static void check(Class cls, List<PropertyDeclaration> decls) {
        Map<String, PropertyDeclaration> map = new HashMap<String, PropertyDeclaration>();
        for (PropertyDeclaration currentDecl : decls) {
            String key = currentDecl.getName();
            if (map.containsKey(key)) {
                PropertyDeclaration firstDecl = map.get(key);
                if (currentDecl.isAbstract()) {
                    // abstract declarations are always allowed since they do not shadow a property
                } else if (firstDecl.isAbstract()) {
                    // an abstract declaration can be refined by a concrete declaration
                    map.put(key, currentDecl);
                } else {
                    throw new IllegalStateException("Illegal property declaration found in class '" + cls.getName() + "':\nmember " + currentDecl.getMember() + " shadows " + firstDecl.getMember() + "!");
                }
            } else {
                map.put(key, currentDecl);
            }
        }
    }

    private static void log(Class cls, List<PropertyDeclaration> decls) {
        if (LOG.isDebugEnabled()) {
            StringBuilder builder = new StringBuilder();
            builder.append("Properties of class ").append(cls.getName()).append(":\n");
            for (PropertyDeclaration decl : decls) {
                builder.append(decl.getName()).append(":").append(decl.getMember()).append("\n");
            }
            LOG.debug(builder.toString());
        }
    }

    private static void findAllPropertyDeclarations(Class cls, List<PropertyDeclaration> result) {
        // First process members declared in superclass and interfaces
        Class superCls = cls.getSuperclass();
        if (superCls != null) {
            findAllPropertyDeclarations(superCls, result);
        }
        Class[] interfaces = cls.getInterfaces();
        for (Class i : interfaces) {
            findAllPropertyDeclarations(i, result);
        }

        // now process members declared in current class
        Method[] allDeclMethods = cls.getDeclaredMethods();
        Field[] allDeclFields = cls.getDeclaredFields();
        Method[] annoDeclMethods = (Method[])getAnnotated(allDeclMethods, Property.class).toArray(new Method[0]);
        Field[] annoDeclFields = (Field[])getAnnotated(allDeclFields, Property.class).toArray(new Field[0]);
        Method[] potentialPropMethods;
        Field[] potentialPropFields;
        if (annoDeclMethods.length == 0 && annoDeclFields.length == 0) {
            potentialPropMethods = allDeclMethods;
            potentialPropFields = allDeclFields;
        } else {
            potentialPropMethods = annoDeclMethods;
            potentialPropFields = annoDeclFields;
        }
        List<MethodDecl> methodDecls = findMethodDecls(potentialPropMethods);
        result.addAll(methodDecls);
        List<FieldDecl> fieldDecls = findFieldDecls(potentialPropFields);
        result.addAll(fieldDecls);

    }

    private static <T extends AccessibleObject> List<T> getAnnotated(T[] allDecl, Class<Property> annoType) {
        ArrayList<T> result = new ArrayList<T>();
        for (T decl : allDecl) {
            Annotation anno = decl.getAnnotation(annoType);
            if (anno != null) {
                result.add(decl);
            }
        }
        return result;
    }

    private static List<MethodDecl> findMethodDecls(Method[] potentialPropMethods) {
        List<MethodDecl> result = new LinkedList<MethodDecl>();
        for (Method method : potentialPropMethods) {
            GetterChecker checker = new GetterChecker(method);
            if (checker.isValid()) {
                Property anno = method.getAnnotation(Property.class);
                String name;
                if (anno != null && false == DEFAULT_NAME.equals(anno.value())) {
                    name = anno.value();
                } else {
                    name = getFieldname(method);
                }
                MethodDecl element = new MethodDecl(name, method);
                result.add(element);
            }
        }
        return result;
    }

    private static class GetterChecker {
        private IllegalAnnotationException exception;

        public GetterChecker(Method method) {
            if (false == method.getName().startsWith("get")) {
                this.exception = new IllegalAnnotationException("The name of the annotated method hast to begin with \"get\".");
            }
            if (false == PresentationModel.class.isAssignableFrom(method.getReturnType())) {
                this.exception = new IllegalAnnotationException("The return type of the annotated method has to be " + PresentationModel.class.getName() + ".");
            }
            if (method.getParameterTypes().length != 0) {
                this.exception = new IllegalAnnotationException("The annotated method must not have any parameters.");
            }
        }

        public boolean isValid() {
            return exception == null;
        }

        public IllegalAnnotationException getException() {
            return exception;
        }
    }

    private static List<FieldDecl> findFieldDecls(Field[] potentialPropFields) {
        List<FieldDecl> result = new LinkedList<FieldDecl>();
        for (Field field : potentialPropFields) {
            if (PresentationModel.class.isAssignableFrom(field.getType())) {
                Property anno = field.getAnnotation(Property.class);
                String name;
                if (anno != null && false == DEFAULT_NAME.equals(anno.value())) {
                    name = anno.value();
                } else {
                    name = field.getName();
                }
                FieldDecl element = new FieldDecl(name, field);
                result.add(element);
            }
        }
        return result;
    }

    private static List<FieldDecl> findAllFieldDecls(Class cls) {
        Collection<Field> allFields = ReflectionUtil.getAllFields(cls);
        Field[] array = (Field[])allFields.toArray(new Field[allFields.size()]);
        return findFieldDecls(array);
    }

    private Properties loadProperties(Class cls) {
        try {
            List<PropertyDeclaration> decls = findAllPropertyDeclarations(cls);
            Properties result = new Properties();
            for (PropertyDeclaration decl : decls) {
                Object value = null;
                if (!decl.isAbstract()) {
                    if (decl instanceof MethodDecl) {
                        value = ReflectionUtil.invokeMethod(presentationModel, ((MethodDecl)decl).getMethod());
                    } else if (decl instanceof FieldDecl) {
                        value = ReflectionUtil.getFieldValue(presentationModel, ((FieldDecl)decl).getField());
                    } else {
                        throw new RuntimeException("Should never be thrown. What should decl else be?");
                    }

                    if (value == null || value instanceof PresentationModel) {
                        String name = decl.getName();
                        Class<? extends PresentationModel> type = decl.getType();
                        LOG.debug("Defining property " + decl.getName() + " with: " + value);
                        result.put(name, (PresentationModel)value, type);
                    } else {
                        throw new IllegalStateException("Return type of member '" + decl.getMember() + "' must implement " + PresentationModel.class.getName());
                    }
                } else {
                    String name = decl.getName();
                    if (result.getType(name) == null) {
                        LOG.debug("Declaring property " + decl.getName() + " with type: " + decl.getType());
                        Class<? extends PresentationModel> type = decl.getType();
                        result.put(name, type);
                    }
                }
            }
            return result;
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        } catch (InvocationTargetException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    private static String getFieldname(Method getterMethod) {
        String name = getterMethod.getName();
        if (name.startsWith("get")) {
            return Character.toLowerCase(name.charAt(3)) + name.substring(4);
        } else if (name.startsWith("is")) {
            return Character.toLowerCase(name.charAt(2)) + name.substring(3);
        } else {
            return name;
        }
    }

    private class MyPropertyChangeListener implements PropertyChangeListener {
        String propertyName;

        public MyPropertyChangeListener(String propertyName) {
            this.propertyName = propertyName;
        }

        public void propertyChange(PropertyChangeEvent evt) {
            onPropertyChange(this.propertyName);
            // 'forward' this event to listeners on presentationModel
            // (optimized: do not forward events about the 'modified' property, since that doubles the
            // number of events)
            if ("modified".equals(evt.getPropertyName()) == false) {
                presentationModel.getPropertyChangeSupport().firePropertyChange(this.propertyName, null, null, evt);
            }
        }
    }
}