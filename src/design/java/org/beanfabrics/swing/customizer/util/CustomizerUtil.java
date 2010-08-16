/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.util;

import java.awt.Component;
import java.awt.Window;
import java.beans.Customizer;

import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.customizer.path.PathChooserDialog;
import org.beanfabrics.swing.customizer.path.PathChooserPM;
import org.beanfabrics.swing.customizer.table.ColumnListConfigurationDialog;
import org.beanfabrics.swing.customizer.table.ColumnListConfigurationPM;

/**
 * <p>
 * ATTENTION: This class is experimental and should only be used inside
 * beanfabrics.
 * </p>
 * The <code>CustomizerUtil</code> is a utility class used by JavaBeans
 * {@link Customizer} classes in Beanfabrics.
 * 
 * @author Michael Karneim
 */
public class CustomizerUtil {
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    private static CustomizerUtil INSTANCE = new CustomizerUtil();

    public static CustomizerUtil get() {
        return INSTANCE;
    }

    private CustomizerUtil() {

    }

    public void setupGUI(Component comp) {
        //		try {
        //			setLookAndFeel(comp);
        //		} catch ( Exception ex) {
        //			ExceptionUtil.getInstance().handleException("", ex);
        //		}
    }

    private void setLookAndFeel(Component comp)
        throws Exception {
        if (isInEclipse()) {
            LookAndFeel oldLaF = UIManager.getLookAndFeel();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(comp);
            UIManager.setLookAndFeel(oldLaF);
        }
    }

    private boolean isInEclipse() {
        try {
            Class.forName("org.eclipse.core.runtime.Platform");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isWindows() {
        return OS_NAME.startsWith("windows");
    }

    public boolean isMacOSX() {
        return OS_NAME.startsWith("mac os");
    }

    public boolean isAquaLookAndFeel() {
        return UIManager.getLookAndFeel().getID().equals("Aqua");
    }

    public void openPathChooserDialog(PathChooserPM pm) {

        Window parent = getParentWindow(pm);
        System.out.println("openPathChooserDialog: parent=" + parent);
        PathChooserDialog dlg = PathChooserDialog.create(parent);
        dlg.setPresentationModel(pm);
        dlg.pack();
        dlg.setModal(true);
        dlg.setLocationRelativeTo(dlg.getOwner());
        dlg.setVisible(true);
    }

    public void openColumnListConfigurationDialog(ColumnListConfigurationPM pm) {
        Window parent = getParentWindow(pm);
        ColumnListConfigurationDialog dlg = ColumnListConfigurationDialog.create(parent);
        dlg.setModal(true);
        dlg.setPresentationModel(pm);
        dlg.setSize(600, 400);
        dlg.setLocationRelativeTo(dlg.getOwner());
        dlg.setVisible(true);
    }

    public RootWindowLocator getRootWindowLocator(final Component comp) {
        return new RootWindowLocator() {
            public Window getRootWindow() {
                Component result = SwingUtilities.getRoot(comp);
                if (result instanceof Window) {
                    return (Window)result;
                }
                return null;
            }
        };
    }

    private Window getParentWindow(PresentationModel pm) {
        Window result;
        RootWindowLocator loc = pm.getContext().getService(RootWindowLocator.class);
        if (loc == null) {
            result = null;
        } else {
            result = loc.getRootWindow();
        }
        return result;
    }

}