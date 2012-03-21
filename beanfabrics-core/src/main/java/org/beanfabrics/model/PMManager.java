/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

import org.beanfabrics.meta.MetadataRegistry;
import org.beanfabrics.support.AnnotatedClassProcessor;
import org.beanfabrics.support.AnnotatedFieldProcessor;
import org.beanfabrics.support.AnnotatedMethodProcessor;
import org.beanfabrics.support.AnnotationProcessor;
import org.beanfabrics.support.Processor;
import org.beanfabrics.support.PropertySupport;
import org.beanfabrics.support.SupportUtil;
import org.beanfabrics.util.ReflectionUtil;

/**
 * @author Michael Karneim
 */
public class PMManager {
    private static final PMManager INSTANCE = new PMManager();
    private final MetadataRegistry metadata = new MetadataRegistry();

    public static void setup(PresentationModel model) {
        getInstance().processPresentationModel(model);
    }

    public static PMManager getInstance() {
        return INSTANCE;
    }

    private PMManager() {
        //
    }

    public MetadataRegistry getMetadata() {
        return this.metadata;
    }

    protected void processPresentationModel(PresentationModel model) {
        PropertySupport.get(model).setup();

        //		OperationSupport.get(model).setup();
        //		ServiceSupport.get(model).setup();
        //		ValidationSupport.get(model).setup();
        //		OnChangeSupport.get(model).setup();

        Class cls = model.getClass();

        List<Class> classes = ReflectionUtil.getAllClasses(cls);
        processClasses(model, classes);

        List<Member> members = ReflectionUtil.getAllMembers(cls);
        members = SupportUtil.sortMembers(members);
        processMembers(model, members);
    }

    private void processClasses(PresentationModel model, List<Class> classes) {
        for (Class cls : classes) {
            processClass(model, cls);
        }
    }

    private void processMembers(PresentationModel model, List<Member> members) {
        for (Member member : members) {
            if (member instanceof Field) {
                Field f = (Field)member;
                processField(model, f);
            } else if (member instanceof Method) {
                Method m = (Method)member;
                processMethod(model, m);
            } else {
                throw new Error("Unexpected Member type: " + member.getClass().getName());
            }
        }
    }

    private void processClass(PresentationModel model, Class cls) {
        Annotation[] annos = cls.getAnnotations();
        for (Annotation anno : annos) {
            processClassAnnotation(model, cls, anno);
        }
    }

    private void processField(PresentationModel model, Field field) {
        Annotation[] annos = field.getAnnotations();
        for (Annotation anno : annos) {
            processFieldAnnotation(model, field, anno);
        }
    }

    private void processMethod(PresentationModel model, Method method) {
        Annotation[] annos = method.getAnnotations();
        for (Annotation anno : annos) {
            processMethodAnnotation(model, method, anno);
        }
    }

    private void processClassAnnotation(PresentationModel model, Class cls, Annotation anno) {
        AnnotationProcessor proc = getProcessor(anno);
        if (proc == null) {
            return;
        } else if (proc instanceof AnnotatedClassProcessor) {
            call((AnnotatedClassProcessor)proc, model, cls, anno);
        } else {
            throw new IllegalStateException("Can't process annotation '" + anno.getClass().getName() + "' on class '" + cls.getName() + "' with processor '" + proc.getClass().getName() + "'");
        }
    }

    private void processFieldAnnotation(PresentationModel model, Field field, Annotation anno) {
        AnnotationProcessor proc = getProcessor(anno);
        if (proc == null) {
            return;
        } else if (proc instanceof AnnotatedFieldProcessor) {
            call((AnnotatedFieldProcessor)proc, model, field, anno);
        } else {
            throw new IllegalStateException("Can't process annotation '" + anno.getClass().getName() + "' on field '" + field.getName() + "' of class '" + field.getDeclaringClass().getName() + "' with processor '" + proc.getClass().getName() + "'");
        }
    }

    private void processMethodAnnotation(PresentationModel model, Method method, Annotation anno) {
        AnnotationProcessor proc = getProcessor(anno);
        if (proc == null) {
            return;
        } else if (proc instanceof AnnotatedMethodProcessor) {
            call((AnnotatedMethodProcessor)proc, model, method, anno);
        } else {
            throw new IllegalStateException("Can't process annotation '" + anno.getClass().getName() + "' on method '" + method.getName() + "' of class '" + method.getDeclaringClass().getName() + "' with processor '" + proc.getClass().getName()
                    + "'");
        }
    }

    private void call(AnnotatedClassProcessor proc, PresentationModel model, Class cls, Annotation anno) {
        proc.process(model, cls, anno);

    }

    private void call(AnnotatedFieldProcessor proc, PresentationModel model, Field field, Annotation anno) {
        proc.process(model, field, anno);
    }

    private void call(AnnotatedMethodProcessor proc, PresentationModel model, Method method, Annotation anno) {
        proc.process(model, method, anno);
    }

    private AnnotationProcessor getProcessor(Annotation anno) {
        try {
            Processor processor = anno.annotationType().getAnnotation(Processor.class);
            if (processor == null) {
                return null;
            }
            AnnotationProcessor result = (AnnotationProcessor)processor.value().newInstance();
            return result;
        } catch (InstantiationException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        }
    }
}