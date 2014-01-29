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
import org.beanfabrics.meta.PathNode;
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

    public static PathChooserController createPathChooser(Context context, PathContext pathContext, Path initialPath) {
        PathChooserController ctrl = new PathChooserController(context, pathContext);
        ctrl.getPresentationModel().setData( initialPath);
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

    public static PathNode asRootNode(Class<? extends PresentationModel> pmClass) {
        if (pmClass == null) {
            return null;
        }
        return PMManager.getInstance().getMetadata().getPathNode(pmClass);
    }

    static Class<? extends PresentationModel> getDeclaredPmTypeFromView(ModelSubscriber aSubscriber) {
        if (aSubscriber instanceof View<?>) {
            View<?> view = (View<?>) aSubscriber;
            return getDeclaredPmTypeFromView(view);
        }
        return PresentationModel.class;
    }

    public static Class<? extends PresentationModel> getDeclaredPmTypeFromView(View<?> view) {
        @SuppressWarnings("unchecked")
        Class<? extends View<?>> viewClass = (Class<? extends View<?>>) view.getClass();
        return getDeclaredPmTypeFromView(viewClass);
    }

    public static Class<? extends PresentationModel> getDeclaredPmTypeFromView(Class<? extends View<?>> viewClass) {
        ViewClassDecorator viewDeco = new ViewClassDecorator(viewClass);
        return viewDeco.getExpectedModelType();
    }

    public static Class<? extends PresentationModel> getElementTypeOfSubscribedOrActualIListPM(ModelSubscriber subscriber) {
        PathNode pmNode = getSubscribedNode(subscriber);
        if (pmNode == null && subscriber instanceof View<?>) {
            pmNode = getRootNodeOfActualOrDeclaredPMFromView((View<?>) subscriber);
        }
        if (isIListPM(pmNode)) {
            return getElementTypeOfIListPM(pmNode);
        }
        return null;
    }

    private static Class<? extends PresentationModel> getElementTypeOfIListPM(PathNode pmNode) {
        if ( !isIListPM(pmNode)) {
            throw new IllegalArgumentException(String.format("pmNode must represent %s, but was %s!", IListPM.class.getSimpleName(), pmNode.getTypeInfo()));
        }
        GenericType gt = pmNode.getGenericType();
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

    private static boolean isIListPM(PathNode pmNode) {
        return pmNode != null && IListPM.class.isAssignableFrom(pmNode.getTypeInfo().getJavaType());
    }

    public static PathNode getSubscribedNode(ModelSubscriber subscriber) {
        IModelProvider provider = subscriber.getModelProvider();
        Path path = subscriber.getPath();

        if (path != null && provider != null) {
            return asRootNode(provider.getPresentationModelType()).getNode(path);
        }
        return null;
    }
    
    public static PathNode getProvidedRootNode(ModelSubscriber subscriber) {
        IModelProvider provider = subscriber.getModelProvider();
        if (provider != null) {
            return asRootNode(provider.getPresentationModelType());
        }
        return null;
    }

    public static PathNode getRootNodeOfActualOrDeclaredPMFromView(View<?> view) {
        PresentationModel actualPM = view.getPresentationModel();
        if (actualPM != null) {
            return asRootNode(actualPM.getClass());
        }
        return asRootNode(getDeclaredPmTypeFromView(view));
    }

    public static PathContext getPathContextToCustomizeModelSubscriber(ModelSubscriber theSubscriber) {
        return getPathContextToCustomizeModelSubscriber(theSubscriber, getDeclaredPmTypeFromView(theSubscriber));
    }

    public static PathContext getPathContextToCustomizeModelSubscriber(ModelSubscriber theSubscriber,
            Class<? extends PresentationModel> expectedModelType) {
        PathNode rootInfo = getProvidedRootNode(theSubscriber);
        if (rootInfo == null) {
            return null;
        }
        return new PathContext(rootInfo, getTypeInfo(expectedModelType));
    }

    

}