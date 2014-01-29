/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.path;

import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Validation;
import org.beanfabrics.validation.ValidationState;

/**
 * The <code>PathChooserPM</code> is the presentation model of the
 * {@link PathChooserDialog}.
 * 
 * @author Michael Karneim
 */
public class PathChooserPM extends AbstractPM {
    public interface OnApplyHandler {
        void apply();
    }

    private OnApplyHandler onApplyHandler;

    protected final PathBrowserPM pathBrowser = new PathBrowserPM();
    protected final OperationPM apply = new OperationPM();

    public PathChooserPM() {
        PMManager.setup(this);
    }

    public void onApply(OnApplyHandler handler) {
        this.onApplyHandler = handler;
        revalidateProperties();
    }

    public void setPathContext(PathContext pathContext) {
        this.pathBrowser.setPathContext(pathContext);
    }

    public Path getData() {
        return pathBrowser.getCurrentPath();
    }
    
    public void setData(Path data) {
        pathBrowser.setCurrentPath(data);
    }
    
    @Operation
    public void apply() {
        this.onApplyHandler.apply();
    }

    @Validation(path = "apply",message="Missing callback functions")
    boolean isApplicable() {
        return onApplyHandler != null;
    }
    
    @Validation(path = "apply")
    ValidationState getPathBrowserValidationState() {
        return pathBrowser.getValidationState();
    }

   

}