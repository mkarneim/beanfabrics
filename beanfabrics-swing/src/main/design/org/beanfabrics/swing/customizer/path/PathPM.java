/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.path;

import org.beanfabrics.Path;
import org.beanfabrics.meta.PathNode;
import org.beanfabrics.meta.TypeInfo;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.SortOrder;
import org.beanfabrics.support.Validation;
import org.beanfabrics.swing.customizer.util.CustomizerUtil;

/**
 * The <code>PathPM</code> is a presentation model presenting a {@link Path} object.
 * 
 * @author Michael Karneim
 */
public class PathPM extends TextPM {
    OperationPM choosePath = new OperationPM();

    private PathNode rootElementInfo;
    private TypeInfo requiredModelTypeInfo;

    public PathPM() {
        PMManager.setup(this);
    }

    public void setPathContext(PathContext pathContext) {
        this.rootElementInfo = pathContext.root;
        this.requiredModelTypeInfo = pathContext.requiredModelTypeInfo;
        this.setData(pathContext.initialPath);
        this.revalidateProperties();
    }

    public Path getData() {
        return Path.parse(this.getText());
    }

    public void setData(Path path) {
        this.setText(Path.getPathString(path));
    }

    @Operation
    void choosePath() {
        final PathChooserController ctrl = CustomizerUtil.createPathChooser(getContext(), this.getPathContext());
        ctrl.getPresentationModel().onApply(new PathChooserPM.OnApplyHandler() {
            @Override
            public void apply() {
                setData(ctrl.getPresentationModel().getData());
            }
        });
        ctrl.getView().setVisible(true);
    }

    @Validation(path = "choosePath")
    boolean canChoosePath() {
        return this.rootElementInfo != null;
    }

    private PathContext getPathContext() {
        PathContext result = new PathContext(rootElementInfo, requiredModelTypeInfo, getData());
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
        return (this.rootElementInfo == null || this.rootElementInfo.getNode(path) != null);
    }

    @Validation(message = "The object at the end of this path does not match the required type")
    @SortOrder(3)
    boolean isCorrect() {
        Path path = new Path(this.getText());
        return rootElementInfo == null || requiredModelTypeInfo == null
                || requiredModelTypeInfo.isAssignableFrom(this.rootElementInfo.getNode(path).getTypeInfo());
    }

}