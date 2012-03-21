/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.path;

import org.beanfabrics.Path;
import org.beanfabrics.meta.PathElementInfo;
import org.beanfabrics.meta.TypeInfo;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.SortOrder;
import org.beanfabrics.support.Validation;
import org.beanfabrics.swing.customizer.util.CustomizerUtil;

/**
 * The <code>PathPM</code> is a presentation model presenting a {@link Path}
 * object.
 * 
 * @author Michael Karneim
 */
public class PathPM extends TextPM {
    OperationPM choosePath = new OperationPM();

    private PathElementInfo rootElementInfo;
    private TypeInfo requiredModelTypeInfo;

    public PathPM() {
        PMManager.setup(this);
    }

    public void setPathContext(PathContext pathContext) {
        this.rootElementInfo = pathContext.root;
        this.requiredModelTypeInfo = pathContext.requiredModelTypeInfo;
        this.setText(Path.getPathString(pathContext.initialPath));
        this.revalidateProperties();
    }

    public Path getPath() {
        return Path.parse(this.getText());
    }

    private void setPath(Path path) {
        this.setText(Path.getPathString(path));
    }

    @Operation
    void choosePath() {
        PathChooserPM chooserMdl = new PathChooserPM();
        chooserMdl.setFunctions(new PathChooserPM.Functions() {
            public void apply(Path path) {
                setPath(path);
            }
        });
        chooserMdl.setPathContext(this.getPathContext());
        chooserMdl.getContext().addParent(this.getContext());
        CustomizerUtil.get().openPathChooserDialog(chooserMdl);
    }

    @Validation(path = "choosePath")
    boolean canChoosePath() {
        return this.rootElementInfo != null;
    }

    private PathContext getPathContext() {
        PathContext result = new PathContext(rootElementInfo, requiredModelTypeInfo, getPath());
        return result;
    }

    @Validation
    @SortOrder(1)
    boolean isSyntacticallyCorrect() {
        try {
            new Path(this.getText());
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    @Validation(message = "This path points into the void")
    @SortOrder(2)
    boolean isComplete() {
        Path path = new Path(this.getText());
        return (this.rootElementInfo == null || this.rootElementInfo.getPathInfo(path) != null);
    }

    @Validation(message = "The object at the end of this path does not match the required type")
    @SortOrder(3)
    boolean isCorrect() {
        Path path = new Path(this.getText());
        return rootElementInfo == null || requiredModelTypeInfo == null || requiredModelTypeInfo.isAssignableFrom(this.rootElementInfo.getPathInfo(path).getTypeInfo());
    }

}