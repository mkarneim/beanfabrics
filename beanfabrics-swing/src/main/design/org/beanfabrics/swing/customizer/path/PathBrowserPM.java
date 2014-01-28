/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.path;

import java.net.URL;
import java.util.ResourceBundle;

import org.beanfabrics.Path;
import org.beanfabrics.meta.PathNode;
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
 * The {@link PathBrowserPM} is the presentation model of the {@link PathBrowserPanel}.
 * 
 * @author Michael Karneim
 */
public class PathBrowserPM extends AbstractPM {
    protected static final String KEY_MESSAGE_INVALID_PATH = "message.invalidPath";
    private final ResourceBundle resourceBundle = ResourceBundleFactory.getBundle(PathBrowserPM.class);

    private static final URL SUCCESS_ICON = PathBrowserPM.class.getResource("agt_action_success.png");
    private static final URL FAIL_ICON = PathBrowserPM.class.getResource("agt_action_fail.png");
    private static final URL WARNING_ICON = PathBrowserPM.class.getResource("messagebox_warning.png");

    TextPM currentSelectedPath = new TextPM();
    TextPM currentSelectedType = new TextPM();
    MapPM<PathNode, PathNodePM> children = new MapPM<PathNode, PathNodePM>();
    OperationPM gotoSelectedChild = new OperationPM();
    OperationPM gotoCurrentPath = new OperationPM();
    OperationPM gotoParent = new OperationPM();
    IconTextPM status = new IconTextPM();

    private PathNode rootElement;
    private PathNode currentElement;

    private TypeInfo requiredModelTypeInfo;

    public PathBrowserPM() {
        PMManager.setup(this);
        currentSelectedType.setEditable(false);
        status.setEditable(false);
        updateStatus();
    }

    public void setPathContext(PathContext pathContext) {
        if (pathContext == null) {
            throw new IllegalArgumentException("pathContext==null");
        }
        rootElement = pathContext.root;
        currentSelectedPath.setText(Path.getPathString(pathContext.initialPath));
        requiredModelTypeInfo = pathContext.requiredModelTypeInfo;
        setCurrentPath(pathContext.initialPath);
    }

    private void setCurrentPath(Path path) {
        currentSelectedPath.setText(Path.getPathString(path));
        PathNode pathElementInfo = rootElement == null ? null : rootElement.getNode(path);
        if (pathElementInfo == null) {
            loadChildren(null);
        } else {
            loadChildren(pathElementInfo.getParent());
            selectChild(pathElementInfo);
        }
    }

    public Path getCurrentPath() {
        if (currentSelectedPath.isEmpty()) {
            return null;
        } else {
            Path path = Path.parse(currentSelectedPath.getText());
            return path;
        }
    }

    private void loadChildren(PathNode aElement) {
        currentElement = aElement;
        Path currentPath = getCurrentPath();
        children.clear();
        if (aElement == null) {
            gotoParent.setDescription("");
            // no owner -> so we load the root object
            PathNodePM rootCell = new PathNodePM();
            rootCell.setData(rootElement);
            children.put(rootElement, rootCell);
            if (rootElement.getPath().equals(currentPath)) {
                children.getSelectedKeys().add(rootElement);
            }
        } else {
            gotoParent.setDescription("Up to "+currentElement.getTypeInfo().getJavaType().getSimpleName());
            for (PathNode child : aElement.getChildren()) {
                PathNodePM cell = new PathNodePM();
                cell.setData(child);
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
            PathNode element = rootElement.getNode(path);
            if (element != null) {
                return element.getTypeInfo();
            }
        }
        return null;
    }

    @OnChange(path = "children")
    void updateCurrentPath() {
        if (children.getSelection().isEmpty() == false) {
            PathNodePM first = children.getSelection().getFirst();
            if (first != null) {
                String pathStr = Path.getPathString(first.getData().getPath());
                currentSelectedPath.setText(pathStr);
            }
        }
    }

    @OnChange(path = "currentSelectedPath")
    void updateStatus() {
        if (currentSelectedPath.isEmpty()) {
            String txt = "Enter a valid path here.";
            if (requiredModelTypeInfo != null) {
                txt += " A path is valid if it points to an PresentationModel of type "
                        + requiredModelTypeInfo.getName();
            }
            status.setText("<html>" + txt + "</html>");
            status.setIconUrl(WARNING_ICON);
        } else {
            ValidationState vState = currentSelectedPath.getValidationState();
            status.setText(vState == null ? null : "<html>" + vState.getMessage() + "</html>");
            status.setIconUrl(vState == null ? SUCCESS_ICON : FAIL_ICON);
        }
    }

    @OnChange(path = "currentSelectedPath")
    void updateNodeType() {
        TypeInfo desc = getCurrentModelTypeInfo();
        if (desc == null) {
            currentSelectedType.setText(null);
            currentSelectedType.setDescription("Unknown type");
        } else {
            String classname = desc.getName();
            currentSelectedType.setText(getBasename(classname));
            currentSelectedType.setDescription(classname);
        }
    }

    @Validation(path = "currentSelectedPath", message = "This path can not be resolved")
    @SortOrder(1)
    boolean canPathBeResolved() {
        Path path = Path.parse(currentSelectedPath.getText());
        return (rootElement != null && rootElement.getNode(path) != null);
    }

    @Validation(path = "currentSelectedPath")
    @SortOrder(2)
    ValidationState matchesRequiredModelTypeInfo() {
        if (requiredModelTypeInfo != null) {
            TypeInfo currentModelTypeInfo = getCurrentModelTypeInfo();
            boolean valid = (currentModelTypeInfo != null && requiredModelTypeInfo
                    .isAssignableFrom(currentModelTypeInfo));
            if (!valid) {
                String message = String.format(resourceBundle.getString(KEY_MESSAGE_INVALID_PATH),
                        requiredModelTypeInfo.getName());
                return new ValidationState(message);
            }
        }
        return null;
    }

    @Validation
    public boolean isPathValid() {
        return currentSelectedPath.isValid();
    }

    @Validation(path = "gotoSelectedChild", message = "the selected model has no children")
    public boolean canGotoSelectedChild() {
        PathNodePM first = children.getSelection().getFirst();
        if (first != null) {
            PathNode nextPathNode = first.getData();
            if (nextPathNode.hasChildren() == true) {
                return true;
            }
        }
        return false;
    }

    @Operation
    public void gotoSelectedChild() {
        PathNodePM cell = children.getSelection().getFirst();
        if (cell != null) {
            PathNode nextElement = cell.getData();
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
        PathNode nextSelectedChild = currentElement;
        PathNode nextElement = currentElement.getParent();
        loadChildren(nextElement);
        selectChild(nextSelectedChild);
    }

    private void selectChild(PathNode child) {
        if (children.containsKey(child)) {
            children.getSelectedKeys().clear();
            children.getSelectedKeys().add(child);
        }
    }

    @Operation
    public void gotoCurrentPath() {
        if (rootElement != null) {
            Path pathToChild = getCurrentPath();

            PathNode childPathInfo = rootElement.getNode(pathToChild);
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