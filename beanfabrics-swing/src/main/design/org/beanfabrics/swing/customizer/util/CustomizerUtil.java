/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.util;

import java.awt.Component;
import java.awt.Window;
import java.beans.Customizer;
import java.lang.reflect.Type;

import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.ViewClassDecorator;
import org.beanfabrics.context.Context;
import org.beanfabrics.meta.PathElementInfo;
import org.beanfabrics.meta.TypeInfo;
import org.beanfabrics.model.IListPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.customizer.path.PathChooserController;
import org.beanfabrics.swing.customizer.path.PathContext;
import org.beanfabrics.util.GenericType;

/**
 * <p>
 * ATTENTION: This class is experimental and should only be used inside beanfabrics.
 * </p>
 * The <code>CustomizerUtil</code> is a utility class used by JavaBeans {@link Customizer} classes in Beanfabrics.
 * 
 * @author Michael Karneim
 */
public class CustomizerUtil {
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    private CustomizerUtil() {

    }

    private static void setLookAndFeel(Component comp) throws Exception {
        if (isInEclipse()) {
            LookAndFeel oldLaF = UIManager.getLookAndFeel();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(comp);
            UIManager.setLookAndFeel(oldLaF);
        }
    }

    private static boolean isInEclipse() {
        try {
            Class.forName("org.eclipse.core.runtime.Platform");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isWindows() {
        return OS_NAME.startsWith("windows");
    }

    public static boolean isMacOSX() {
        return OS_NAME.startsWith("mac os");
    }

    public static boolean isAquaLookAndFeel() {
        return UIManager.getLookAndFeel().getID().equals("Aqua");
    }

    public static PathChooserController createPathChooser(Context context, PathContext pathContext) {
        PathChooserController ctrl = new PathChooserController(context, pathContext);
        ctrl.getView().setModal(true);
        return ctrl;
    }

    public static RootWindowLocator getRootWindowLocator(final Component comp) {
        return new RootWindowLocator() {
            public Window getRootWindow() {
                Component result = SwingUtilities.getRoot(comp);
                if (result instanceof Window) {
                    return (Window) result;
                }
                return null;
            }
        };
    }

    public static Window locateRootWindow(PresentationModel pm) {
        return locateRootWindow(pm.getContext());
    }

    public static Window locateRootWindow(Context context) {
        Window result;
        RootWindowLocator loc = context.getService(RootWindowLocator.class);
        if (loc == null) {
            result = null;
        } else {
            result = loc.getRootWindow();
        }
        return result;
    }

    public static TypeInfo getTypeInfo(Class<? extends PresentationModel> pmClass) {
        if (pmClass == null) {
            return null;
        }
        return PMManager.getInstance().getMetadata().getTypeInfo(pmClass);
    }

    public static PathElementInfo getPathInfo(Class<? extends PresentationModel> pmClass) {
        if (pmClass == null) {
            return null;
        }
        return PMManager.getInstance().getMetadata().getPathElementInfo(pmClass);
    }
    
    public static Class<? extends PresentationModel> getRowPmType(ModelSubscriber component) {
        PathElementInfo listPmMetaData = getPathInfoOfTarget(component);
        if (listPmMetaData == null) {
            return null;
        } else {
            GenericType gt = listPmMetaData.getGenericType();
            GenericType typeParam = gt.getTypeParameter(IListPM.class.getTypeParameters()[0]);
            Type tArg = typeParam.narrow(typeParam.getType(), PresentationModel.class);
            if (tArg instanceof Class) {
                @SuppressWarnings("unchecked")
                Class<? extends PresentationModel> result = (Class<? extends PresentationModel>) tArg;
                return result;
            } else {
                throw new IllegalStateException("Unexpected type: " + tArg.getClass().getName());
            }
        }
    }

    public static PathElementInfo getPathInfoOfTarget(ModelSubscriber subscriber) {
        Path path = subscriber.getPath();
        IModelProvider provider = subscriber.getModelProvider();
        if (path != null && provider != null ) {
            Class<? extends PresentationModel> pmClass = provider.getPresentationModelType();
            if (pmClass != null) {
                return getPathInfo(pmClass).getPathInfo(path);                
            }
            PresentationModel pm = provider.getPresentationModel();
            if ( pm != null) {
                return getPathInfo(pm.getClass());
            }
        }
        if (subscriber instanceof View<?>) {
            View<?> view = (View<?>) subscriber;
            PresentationModel pm = view.getPresentationModel();
            if (pm != null) {
                return getPathInfo(pm.getClass());                
            }
        }
        return null;
    }

    public static PathContext getPathContextFromSubscriber(ModelSubscriber aSubscriber) {
        if (aSubscriber instanceof View) {
            return getPathContextFromBnComponent(aSubscriber);
        }
        // fallback
        IModelProvider provider = aSubscriber.getModelProvider();
        if (provider == null) {
            return null;
        }
        // we know the root pm type but we don't know what is required by the view (because there is no view)
        return new PathContext(getPathInfo(provider.getPresentationModelType()), getTypeInfo(PresentationModel.class),
                aSubscriber.getPath());
    }

    public static PathContext getPathContextFromBnComponent(ModelSubscriber component) {
        Class<? extends PresentationModel> rootModelType = extractBoundRootModelTypeFromComponent(component);
        @SuppressWarnings("unchecked")
        Class<? extends PresentationModel> requiredModelType = getExpectedModelTypeFromViewClass((Class<? extends View<?>>) component
                .getClass());
        PathContext result = new PathContext(PMManager.getInstance().getMetadata().getPathElementInfo(rootModelType),
                PMManager.getInstance().getMetadata().getTypeInfo(requiredModelType), component.getPath());
        PathContext pathContext = result;
        return pathContext;
    }

    private static Class<? extends PresentationModel> extractBoundRootModelTypeFromComponent(ModelSubscriber component) {
        IModelProvider provider = component.getModelProvider();
        if (provider != null) {
            return provider.getPresentationModelType();
        } else if (component instanceof View<?>) {
            View<?> view = (View<?>) component;
            PresentationModel pm = view.getPresentationModel();
            if (pm != null) {
                return pm.getClass();
            }
        }
        return null;
    }

    private static Class<? extends PresentationModel> getExpectedModelTypeFromViewClass(
            Class<? extends View<?>> viewClass) {
        ViewClassDecorator viewDeco = new ViewClassDecorator(viewClass);
        return viewDeco.getExpectedModelType();
    }

}