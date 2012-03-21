/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.support;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
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
     * and all superclasses of the current {@link PresentationModel}.
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
     * This method adds the {@link #modelListener} to the current
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

    /**
     * Returns a list of all non-conflicting, explicit and inplicit property
     * declarations that can be found in the given class (and its superclasses
     * and interfaces).
     * 
     * @param cls
     * @return a list of all non-conflicting, explicit and inplicit property
     *         declarations of the given class
     */
    public static List<PropertyDeclaration> getPropertyDeclarations(Class cls) {
        // Find all members that can be interpreted as Property declarations
        List<PropertyDeclaration> decls = findAllPropertyDeclarations(cls);
        // Resolve any conflicts in these declarations
        List<PropertyDeclaration> result = resolveConflicts(decls);
        log(cls, result);
        return result;
    }

    private static List<PropertyDeclaration> resolveConflicts(List<PropertyDeclaration> decls) {
        List<PropertyDeclaration> result = new ArrayList<PropertyDeclaration>(decls.size());
        // Check for conflicting property declarations based on fields
        // Only the maximum of one field property declaration for the same key is allowd
        Map<String, PropertyDeclaration> fieldMap = new HashMap<String, PropertyDeclaration>();
        for (PropertyDeclaration currentDecl : new ArrayList<PropertyDeclaration>(decls)) {
            if (!(currentDecl.getMember() instanceof Field)) {
                continue;
            }
            String key = currentDecl.getName();
            PropertyDeclaration oldDecl = fieldMap.get(key);
            if (oldDecl == null) {
                // No conflict
                fieldMap.put(key, currentDecl);
            } else {
                // Conflict: property is already declared based on a field member                 
                // -> We can't solve this conflict
                throw new IllegalStateException("Illegal property declaration:\nmember " + currentDecl.getMember() + " shadows " + oldDecl.getMember() + "!");

            }
        }

        // Check for conflicting property declarations based on fields and methods
        Map<String, PropertyDeclaration> map = new HashMap<String, PropertyDeclaration>();
        for (PropertyDeclaration currentDecl : new ArrayList<PropertyDeclaration>(decls)) {
            String key = currentDecl.getName();
            PropertyDeclaration oldDecl = map.get(key);
            if (oldDecl == null) {
                // No conflict
                map.put(key, currentDecl);
                result.add(currentDecl);
            } else {
                // Conflict: property is already declared
                // Can we solve it?
                if (currentDecl.getMember() instanceof Method) {
                    // Method wins
                    // -> replace old declaration
                    map.put(key, currentDecl);
                    result.remove(oldDecl);
                    result.add(currentDecl);
                    result.add(currentDecl);
                } else if (oldDecl.getMember() instanceof Method) {
                    // Method wins
                    // -> ignore current declaration
                    // Nothing to do
                } else {
                    // Both declarations are based on field members
                    // -> We can't solve this conflict
                    throw new IllegalStateException("Illegal property declaration:\nmember " + currentDecl.getMember() + " shadows " + oldDecl.getMember() + "!");
                }
            }
        }
        return result;
    }

    private static List<PropertyDeclaration> findAllPropertyDeclarations(Class cls) {
        List<PropertyDeclaration> result = DECLARATION_CACHE.get(cls);
        if (result == null) {
            result = new ArrayList<PropertyDeclaration>();
            findAllPropertyDeclarations(cls, result);
            DECLARATION_CACHE.put(cls, result);
        }
        return result;
    }

    private static void findAllPropertyDeclarations(Class currentClass, List<PropertyDeclaration> result) {
        if (!PresentationModel.class.isAssignableFrom(currentClass)) {
            // Skip classes that are not of type PresentationModel
            return;
        }
        // Process superclass and interfaces
        Class superCls = currentClass.getSuperclass();
        if (superCls != null) {
            findAllPropertyDeclarations(superCls, result);
        }
        Class[] interfaces = currentClass.getInterfaces();
        for (Class i : interfaces) {
            findAllPropertyDeclarations(i, result);
        }

        //// Now process the current class        
        // Check for Convention-over-Configuration
        boolean hasPropertyAnnotations = hasPropertyAnnotations(currentClass);
        if (hasPropertyAnnotations) {
            // The current class contains members that are annotated with @Property
            // -> Since we implement Convention-over-Configuration we only process these members

            // Search for methods that are annotated with @Property  
            Method[] allDeclMethods = currentClass.getDeclaredMethods();
            Method[] annoDeclMethods = (Method[])filterAnnotatedMembers(allDeclMethods, Property.class).toArray(new Method[0]);
            List<MethodDecl> methodsWithPropertyDecls = filterPropertyMethods(annoDeclMethods);
            // Add search result
            result.addAll(methodsWithPropertyDecls);

            // Search for fields that are annotated with @Property
            Field[] allDeclFields = currentClass.getDeclaredFields();
            Field[] annoDeclFields = (Field[])filterAnnotatedMembers(allDeclFields, Property.class).toArray(new Field[0]);
            List<FieldDecl> fieldsWithPropertyDecls = filterPropertyFields(annoDeclFields);
            // Add search result
            result.addAll(fieldsWithPropertyDecls);
        } else {
            // The current class contains no @Property annotation.
            // -> We will process all members of type PresentationModel

            // Search for methods with return type PresentationModel  
            Method[] allDeclMethods = currentClass.getDeclaredMethods();
            List<MethodDecl> methodsWithPropertyDecls = filterPropertyMethods(allDeclMethods);
            // Add search result
            result.addAll(methodsWithPropertyDecls);

            // Search for fields of type PresentationModel
            Field[] allDeclFields = currentClass.getDeclaredFields();
            List<FieldDecl> fieldsWithPropertyDecls = filterPropertyFields(allDeclFields);
            // Add search result
            result.addAll(fieldsWithPropertyDecls);
        }
    }

    /**
     * Checks whether the given class has at least one declared member with a
     * Property annotation.
     * 
     * @param cls
     * @return <code>true</code> if the given class has at least one declared
     *         member with a Property annotation.
     */
    private static boolean hasPropertyAnnotations(Class cls) {
        for (Field field : cls.getDeclaredFields()) {
            if (hasPropertyAnnotation(field)) {
                return true;
            }
        }
        for (Method method : cls.getDeclaredMethods()) {
            if (hasPropertyAnnotation(method)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the given element is annotated with Property.
     * 
     * @param element
     * @return <code>true</code> if the given element is annotated with Property
     */
    private static boolean hasPropertyAnnotation(AnnotatedElement element) {
        return element.isAnnotationPresent(Property.class);
    }

    private static <T extends AccessibleObject> List<T> filterAnnotatedMembers(T[] allDecl, Class<Property> annoType) {
        ArrayList<T> result = new ArrayList<T>();
        for (T decl : allDecl) {
            Annotation anno = decl.getAnnotation(annoType);
            if (anno != null) {
                result.add(decl);
            }
        }
        return result;
    }

    private static List<MethodDecl> filterPropertyMethods(Method[] potentialPropMethods) {
        List<MethodDecl> result = new LinkedList<MethodDecl>();
        for (Method method : potentialPropMethods) {
            // The name of this method has to begin with "get"
            if (false == method.getName().startsWith("get")) {
                continue;
            }
            // The return type of this method has to be assignable from PresentationModel
            if (false == PresentationModel.class.isAssignableFrom(method.getReturnType())) {
                continue;
            }
            // This method must not have any parameters
            if (method.getParameterTypes().length != 0) {
                continue;
            }
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
        return result;
    }

    private static List<FieldDecl> filterPropertyFields(Field[] potentialPropFields) {
        List<FieldDecl> result = new LinkedList<FieldDecl>();
        for (Field field : potentialPropFields) {
            // The type of this field has to be assignable from PresentationModel
            if (false == PresentationModel.class.isAssignableFrom(field.getType())) {
                continue;
            }
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
        return result;
    }

    private Properties loadProperties(Class cls) {
        try {
            List<PropertyDeclaration> decls = getPropertyDeclarations(cls);

            Properties result = new Properties();
            for (PropertyDeclaration decl : decls) {
                Object value = null;
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
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Defining property " + decl.getName() + " with: " + value);
                    }
                    result.put(name, (PresentationModel)value, type);
                } else {
                    throw new IllegalStateException("Return type of member '" + decl.getMember() + "' must implement " + PresentationModel.class.getName());
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