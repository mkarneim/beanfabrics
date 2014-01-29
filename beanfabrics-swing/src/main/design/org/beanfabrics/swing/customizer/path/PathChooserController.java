package org.beanfabrics.swing.customizer.path;

import java.awt.Window;

import org.beanfabrics.context.Context;
import org.beanfabrics.swing.customizer.util.CustomizerUtil;

public class PathChooserController {
    private PathChooserPM presentationModel;
    private PathChooserDialog view;
    private Context context;
    private PathContext rootPathContext;

    public PathChooserController(Context context, PathContext rootPathContext) {
        this.context = context;
        this.rootPathContext = rootPathContext;
    }

    public PathChooserPM getPresentationModel() {
        if (presentationModel == null) {
            presentationModel = new PathChooserPM();
            presentationModel.getContext().addParent(context);
            presentationModel.setPathContext(rootPathContext);
        }
        return presentationModel;
    }

    public PathChooserDialog getView() {
        if (view == null) {
            view = PathChooserDialog.create(getRootWindow());
            view.setPresentationModel(getPresentationModel());
            view.pack();
            view.setLocationRelativeTo(view.getOwner());
        }
        return view;
    }

    private Window getRootWindow() {
        return CustomizerUtil.locateRootWindow(context);
    }
}
