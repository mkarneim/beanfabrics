/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.path;

import org.beanfabrics.Path;
import org.beanfabrics.meta.PathInfo;
import org.beanfabrics.meta.PresentationModelInfo;
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

    private PathInfo root;
    private PresentationModelInfo requiredModelInfo;

    public PathPM() {
        PMManager.setup(this);
    }

    public void setPathContext(PathContext pathContext) {
        this.root = pathContext.root;
        this.requiredModelInfo = pathContext.requiredModelInfo;
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
        return this.root != null;
    }

    private PathContext getPathContext() {
        PathContext result = new PathContext(root, requiredModelInfo, getPath());
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
        return (this.root == null || this.root.getPathInfo(path) != null);
    }

    @Validation(message = "The object at the end of this path does not match the required type")
    @SortOrder(3)
    boolean isCorrect() {
        Path path = new Path(this.getText());
        return root == null || requiredModelInfo == null || requiredModelInfo.isAssignableFrom(this.root.getPathInfo(path).getModelInfo());
    }

}