/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.util;

import java.awt.Component;
import java.beans.Customizer;

import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.beanfabrics.swing.customizer.path.PathChooserDialog;
import org.beanfabrics.swing.customizer.path.PathChooserPM;

/**
 * <p>
 * ATTENTION: This class is experimental and currently not used.
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

    public static void openPathChooserDialog(PathChooserPM chooserMdl) {
        PathChooserDialog dlg = new PathChooserDialog();
        dlg.setPresentationModel(chooserMdl);
        dlg.pack();
        dlg.setModal(true);
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }

}