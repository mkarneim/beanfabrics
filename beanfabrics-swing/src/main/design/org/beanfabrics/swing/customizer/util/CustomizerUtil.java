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
import org.beanfabrics.meta.PathTree;
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

    public static TypeInfo toTypeInfo(Class<? extends PresentationModel> pmClass) {
        if (pmClass == null) {
            return null;
        }
        return PMManager.getInstance().getMetadata().getTypeInfo(pmClass);
    }

    public static PathTree toRootPathTree(Class<? extends PresentationModel> pmClass) {
        if (pmClass == null) {
            return null;
        }
        return PMManager.getInstance().getMetadata().getPathTree(pmClass);
    }

    static Class<? extends PresentationModel> getExpectedPmTypeFromView(ModelSubscriber aSubscriber) {
        if (aSubscriber instanceof View<?>) {
            View<?> view = (View<?>) aSubscriber;
            return getExpectedPmTypeFromView(view);
        }
        return PresentationModel.class;
    }

    public static Class<? extends PresentationModel> getExpectedPmTypeFromView(View<?> view) {
        @SuppressWarnings("unchecked")
        Class<? extends View<?>> viewClass = (Class<? extends View<?>>) view.getClass();
        return getExpectedPmTypeFromViewClass(viewClass);
    }

    public static Class<? extends PresentationModel> getExpectedPmTypeFromViewClass(
            Class<? extends View<?>> viewClass) {
        ViewClassDecorator viewDeco = new ViewClassDecorator(viewClass);
        return viewDeco.getExpectedModelType();
    }

    public static Class<? extends PresentationModel> getRowPmType(ModelSubscriber subscriber) {
        PathTree listPmMetaData = getRootPathTreeFromSubscriber(subscriber);
        if (listPmMetaData == null && subscriber instanceof View<?>) {
            listPmMetaData = getPathTreeFromView((View<?>) subscriber);
        }
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

    public static PathTree getRootPathTreeFromSubscriber(ModelSubscriber subscriber) {
        Path path = subscriber.getPath();
        IModelProvider provider = subscriber.getModelProvider();
        if (path != null && provider != null) {
            Class<? extends PresentationModel> pmClass = provider.getPresentationModelType();
            if (pmClass != null && PresentationModel.class.equals(pmClass) == false) {
                return toRootPathTree(pmClass).getPathInfo(path);
            }
            PresentationModel pm = provider.getPresentationModel();
            if (pm != null) {
                return toRootPathTree(pm.getClass());
            }
        }
        return null;
    }

    static PathTree getPathTreeFromView(View<?> view) {
        PresentationModel pm = view.getPresentationModel();
        if (pm != null) {
            return toRootPathTree(pm.getClass());
        }
        @SuppressWarnings("unchecked")
        Class<? extends View<?>> viewClass = (Class<? extends View<?>>) view.getClass();
        Class<? extends PresentationModel> pmClass = getExpectedPmTypeFromViewClass(viewClass);
        return toRootPathTree(pmClass);
    }

    public static PathContext getPathContextToCustomizeModelSubscriber(ModelSubscriber theSubscriber) {
        return getPathContextToCustomizeModelSubscriber(theSubscriber,
                getExpectedPmTypeFromView(theSubscriber));
    }

    public static PathContext getPathContextToCustomizeModelSubscriber(ModelSubscriber theSubscriber,
            Class<? extends PresentationModel> expectedModelType) {
        PathTree rootInfo = getRootPathTreeFromSubscriber(theSubscriber);
        if (rootInfo == null) {
            return null;
        }
        return new PathContext(rootInfo, toTypeInfo(expectedModelType), theSubscriber.getPath());
    }

}