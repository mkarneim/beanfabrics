/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.path;

import java.net.URL;

import org.beanfabrics.Path;
import org.beanfabrics.meta.PathInfo;
import org.beanfabrics.meta.PresentationModelInfo;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IconTextPM;
import org.beanfabrics.model.MapPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.SortOrder;
import org.beanfabrics.support.Validation;
import org.beanfabrics.validation.ValidationState;

/**
 * The <code>PathBrowserPM</code> is the presentation model of the
 * {@link PathBrowserPanel}.
 * 
 * @author Michael Karneim
 */
public class PathBrowserPM extends AbstractPM {
    private static final URL SUCCESS_ICON = PathBrowserPM.class.getResource("agt_action_success.png");
    private static final URL FAIL_ICON = PathBrowserPM.class.getResource("agt_action_fail.png");
    private static final URL WARNING_ICON = PathBrowserPM.class.getResource("messagebox_warning.png");

    TextPM currentPath = new TextPM();
    TextPM currentType = new TextPM();
    MapPM<PathInfo, PathInfoPM> children = new MapPM<PathInfo, PathInfoPM>();
    OperationPM gotoSelectedChild = new OperationPM();
    OperationPM gotoCurrentPath = new OperationPM();
    OperationPM gotoParent = new OperationPM();
    IconTextPM status = new IconTextPM();

    private PathInfo root;
    private PathInfo currentOwner;

    private PresentationModelInfo requiredModelInfo;

    public PathBrowserPM() {
        PMManager.setup(this);
        currentType.setEditable(false);
        status.setEditable(false);

        updateStatus();
    }

    public void setPathContext(PathContext pathContext) {
        if (pathContext == null) {
            throw new IllegalArgumentException("pathContext==null");
        }
        this.root = pathContext.root;
        this.currentPath.setText(Path.getPathString(pathContext.initialPath));
        this.requiredModelInfo = pathContext.requiredModelInfo;
        this.setCurrentPath(pathContext.initialPath);
    }

    private void setCurrentPath(Path path) {
        this.currentPath.setText(Path.getPathString(path));
        PathInfo pathInfo = this.root == null ? null : this.root.getPathInfo(path);
        if (pathInfo == null) {
            loadChildren(null);
        } else {
            loadChildren(pathInfo.getParent());
            selectChild(pathInfo);
        }
    }

    public Path getCurrentPath() {
        if (this.currentPath.isEmpty()) {
            return null;
        } else {
            Path path = Path.parse(this.currentPath.getText());
            return path;
        }
    }

    private void loadChildren(PathInfo aOwner) {
        this.currentOwner = aOwner;
        Path currentPath = this.getCurrentPath();
        children.clear();
        if (aOwner == null) {
            // no owner -> so we load the root object
            PathInfoPM rootCell = new PathInfoPM();
            rootCell.setPathInfo(this.root);
            children.put(this.root, rootCell);
            if (root.getPath().equals(currentPath)) {
                children.getSelectedKeys().add(root);
            }
        } else {
            for (PathInfo child : aOwner.getChildren()) {
                PathInfoPM cell = new PathInfoPM();
                cell.setPathInfo(child);
                children.put(child, cell);
                if (child.getPath().equals(currentPath)) {
                    children.getSelectedKeys().add(child);
                }
            }
        }
    }

    public PresentationModelInfo getCurrentModelInfo() {
        Path path = getCurrentPath();
        if (path != null && this.root != null) {
            PathInfo node = this.root.getPathInfo(path);
            if (node != null) {
                return node.getModelInfo();
            }
        }
        return null;
    }

    @OnChange(path = "children")
    void updateCurrentPath() {
        if (this.children.getSelection().isEmpty() == false) {
            PathInfoPM first = this.children.getSelection().getFirst();
            if (first != null) {
                String pathStr = Path.getPathString(first.getPathInfo().getPath());
                this.currentPath.setText(pathStr);
            }
        }
    }

    @OnChange(path = "currentPath")
    void updateStatus() {
        if (this.currentPath.isEmpty()) {
            String txt = "Enter a valid path here.";
            if (this.requiredModelInfo != null) {
                txt += " A path is valid if it points to an PresentationModel of type " + requiredModelInfo.getName();
            }
            this.status.setText("<html>" + txt + "</html>");
            this.status.setIconUrl(WARNING_ICON);
        } else {
            ValidationState vState = this.currentPath.getValidationState();
            this.status.setText(vState == null ? null : "<html>" + vState.getMessage() + "</html>");
            this.status.setIconUrl(vState == null ? SUCCESS_ICON : FAIL_ICON);
        }
    }

    @OnChange(path = "currentPath")
    void updateNodeType() {
        PresentationModelInfo desc = this.getCurrentModelInfo();
        if (desc == null) {
            currentType.setText(null);
            currentType.setDescription("Unknown type");
        } else {
            String classname = desc.getName();
            currentType.setText(getBasename(classname));
            currentType.setDescription(classname);
        }
    }

    @Validation(path = "currentPath", message = "This path can not be resolved")
    @SortOrder(1)
    boolean canPathBeResolved() {
        Path path = Path.parse(this.currentPath.getText());
        return (root != null && this.root.getPathInfo(path) != null);
    }

    @Validation(path = "currentPath")
    @SortOrder(2)
    ValidationState matchesRequiredModelInfo() {
        if (this.requiredModelInfo == null) {
            return null;
        } else {
            PresentationModelInfo currentModelInfo = getCurrentModelInfo();
            boolean valid = (currentModelInfo != null && this.requiredModelInfo.isAssignableFrom(currentModelInfo));
            return valid ? null : new ValidationState("This path is invalid because it points to an invalid PresentationModel type. " + "\nRequired is " + this.requiredModelInfo.getName());
        }
    }

    @Validation
    public boolean isPathValid() {
        return this.currentPath.isValid();
    }

    @Validation(path = "gotoSelectedChild", message = "the selected model has no children")
    public boolean canGotoSelectedChild() {
        PathInfoPM first = children.getSelection().getFirst();
        if (first != null) {
            PathInfo nextDesc = first.getPathInfo();
            if (nextDesc.hasChildren() == true) {
                return true;
            }
        }
        return false;
    }

    @Operation
    public void gotoSelectedChild() {
        PathInfoPM cell = children.getSelection().getFirst();
        if (cell != null) {
            PathInfo nextOwner = cell.getPathInfo();
            if (nextOwner.hasChildren() == true) {
                this.loadChildren(nextOwner);
            }
        }
    }

    @Validation(path = "gotoParent", message = "Already on top")
    public boolean canGotoParent() {
        return this.currentOwner != null;
    }

    @Operation
    public void gotoParent() {
        if (this.currentOwner == null) {
            return;
        }
        PathInfo nextSelectedChild = this.currentOwner;
        PathInfo nextOwner = this.currentOwner.getParent();
        this.loadChildren(nextOwner);
        this.selectChild(nextSelectedChild);
    }

    private void selectChild(PathInfo child) {
        if (children.containsKey(child)) {
            children.getSelectedKeys().clear();
            children.getSelectedKeys().add(child);
        }
    }

    @Operation
    public void gotoCurrentPath() {
        if (this.root != null) {
            Path pathToChild = getCurrentPath();

            PathInfo childPathInfo = this.root.getPathInfo(pathToChild);
            if (childPathInfo != null) {
                this.setCurrentPath(childPathInfo.getPath());
            } else {
                // TODO (mk) we could provide some auto-completion here...
            }
        } // else ignore
    }

    private static String getBasename(String classname) {
        int idx = classname.lastIndexOf('$');
        if (idx == -1) {
            idx = classname.lastIndexOf('.');
        }
        if (idx == -1) {
            return classname;
        } else {
            return classname.substring(idx + 1);
        }
    }
}