/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.path;

import java.net.URL;
import java.util.ResourceBundle;

import org.beanfabrics.Path;
import org.beanfabrics.meta.PathElementInfo;
import org.beanfabrics.meta.TypeInfo;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IconTextPM;
import org.beanfabrics.model.MapPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.SortOrder;
import org.beanfabrics.support.Validation;
import org.beanfabrics.util.ResourceBundleFactory;
import org.beanfabrics.validation.ValidationState;

/**
 * The <code>PathBrowserPM</code> is the presentation model of the
 * {@link PathBrowserPanel}.
 *
 * @author Michael Karneim
 */
public class PathBrowserPM extends AbstractPM {
    protected static final String KEY_MESSAGE_INVALID_PATH = "message.invalidPath";
    private final ResourceBundle resourceBundle = ResourceBundleFactory.getBundle(PathBrowserPM.class);

    private static final URL SUCCESS_ICON = PathBrowserPM.class.getResource("agt_action_success.png");
    private static final URL FAIL_ICON = PathBrowserPM.class.getResource("agt_action_fail.png");
    private static final URL WARNING_ICON = PathBrowserPM.class.getResource("messagebox_warning.png");

    TextPM currentPath = new TextPM();
    TextPM currentType = new TextPM();
    MapPM<PathElementInfo, PathInfoPM> children = new MapPM<PathElementInfo, PathInfoPM>();
    OperationPM gotoSelectedChild = new OperationPM();
    OperationPM gotoCurrentPath = new OperationPM();
    OperationPM gotoParent = new OperationPM();
    IconTextPM status = new IconTextPM();

    private PathElementInfo rootElement;
    private PathElementInfo currentElement;

    private TypeInfo requiredModelTypeInfo;

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
        rootElement = pathContext.root;
        currentPath.setText(Path.getPathString(pathContext.initialPath));
        requiredModelTypeInfo = pathContext.requiredModelTypeInfo;
        setCurrentPath(pathContext.initialPath);
    }

    private void setCurrentPath(Path path) {
        currentPath.setText(Path.getPathString(path));
        PathElementInfo pathElementInfo = rootElement == null ? null : rootElement.getPathInfo(path);
        if (pathElementInfo == null) {
            loadChildren(null);
        } else {
            loadChildren(pathElementInfo.getParent());
            selectChild(pathElementInfo);
        }
    }

    public Path getCurrentPath() {
        if (currentPath.isEmpty()) {
            return null;
        } else {
            Path path = Path.parse(currentPath.getText());
            return path;
        }
    }

    private void loadChildren(PathElementInfo aElement) {
        currentElement = aElement;
        Path currentPath = getCurrentPath();
        children.clear();
        if (aElement == null) {
            // no owner -> so we load the root object
            PathInfoPM rootCell = new PathInfoPM();
            rootCell.setPathElementInfo(rootElement);
            children.put(rootElement, rootCell);
            if (rootElement.getPath().equals(currentPath)) {
                children.getSelectedKeys().add(rootElement);
            }
        } else {
            for (PathElementInfo child : aElement.getChildren()) {
                PathInfoPM cell = new PathInfoPM();
                cell.setPathElementInfo(child);
                children.put(child, cell);
                if (child.getPath().equals(currentPath)) {
                    children.getSelectedKeys().add(child);
                }
            }
        }
    }

    public TypeInfo getCurrentModelTypeInfo() {
        Path path = getCurrentPath();
        if (path != null && rootElement != null) {
            PathElementInfo element = rootElement.getPathInfo(path);
            if (element != null) {
                return element.getTypeInfo();
            }
        }
        return null;
    }

    @OnChange(path = "children")
    void updateCurrentPath() {
        if (children.getSelection().isEmpty() == false) {
            PathInfoPM first = children.getSelection().getFirst();
            if (first != null) {
                String pathStr = Path.getPathString(first.getPathElementInfo().getPath());
                currentPath.setText(pathStr);
            }
        }
    }

    @OnChange(path = "currentPath")
    void updateStatus() {
        if (currentPath.isEmpty()) {
            String txt = "Enter a valid path here.";
            if (requiredModelTypeInfo != null) {
                txt += " A path is valid if it points to an PresentationModel of type " + requiredModelTypeInfo.getName();
            }
            status.setText("<html>" + txt + "</html>");
            status.setIconUrl(WARNING_ICON);
        } else {
            ValidationState vState = currentPath.getValidationState();
            status.setText(vState == null ? null : "<html>" + vState.getMessage() + "</html>");
            status.setIconUrl(vState == null ? SUCCESS_ICON : FAIL_ICON);
        }
    }

    @OnChange(path = "currentPath")
    void updateNodeType() {
        TypeInfo desc = getCurrentModelTypeInfo();
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
        Path path = Path.parse(currentPath.getText());
        return (rootElement != null && rootElement.getPathInfo(path) != null);
    }

    @Validation(path = "currentPath")
    @SortOrder(2)
    ValidationState matchesRequiredModelTypeInfo() {
        if (requiredModelTypeInfo != null) {
            TypeInfo currentModelTypeInfo = getCurrentModelTypeInfo();
            boolean valid = (currentModelTypeInfo != null && requiredModelTypeInfo.isAssignableFrom(currentModelTypeInfo));
            if (!valid) {
                String message = String.format(resourceBundle.getString(KEY_MESSAGE_INVALID_PATH), requiredModelTypeInfo.getName());
                return new ValidationState(message);
            }
        }
        return null;
    }

    @Validation
    public boolean isPathValid() {
        return currentPath.isValid();
    }

    @Validation(path = "gotoSelectedChild", message = "the selected model has no children")
    public boolean canGotoSelectedChild() {
        PathInfoPM first = children.getSelection().getFirst();
        if (first != null) {
            PathElementInfo nextPathElementInfo = first.getPathElementInfo();
            if (nextPathElementInfo.hasChildren() == true) {
                return true;
            }
        }
        return false;
    }

    @Operation
    public void gotoSelectedChild() {
        PathInfoPM cell = children.getSelection().getFirst();
        if (cell != null) {
            PathElementInfo nextElement = cell.getPathElementInfo();
            if (nextElement.hasChildren() == true) {
                loadChildren(nextElement);
            }
        }
    }

    @Validation(path = "gotoParent", message = "Already on top")
    public boolean canGotoParent() {
        return currentElement != null;
    }

    @Operation
    public void gotoParent() {
        if (currentElement == null) {
            return;
        }
        PathElementInfo nextSelectedChild = currentElement;
        PathElementInfo nextElement = currentElement.getParent();
        loadChildren(nextElement);
        selectChild(nextSelectedChild);
    }

    private void selectChild(PathElementInfo child) {
        if (children.containsKey(child)) {
            children.getSelectedKeys().clear();
            children.getSelectedKeys().add(child);
        }
    }

    @Operation
    public void gotoCurrentPath() {
        if (rootElement != null) {
            Path pathToChild = getCurrentPath();

            PathElementInfo childPathInfo = rootElement.getPathInfo(pathToChild);
            if (childPathInfo != null) {
                setCurrentPath(childPathInfo.getPath());
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