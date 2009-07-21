/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer;

import java.net.URL;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.ViewClassDecorator;
import org.beanfabrics.meta.MetadataRegistry;
import org.beanfabrics.meta.PathInfo;
import org.beanfabrics.meta.PresentationModelInfo;
import org.beanfabrics.model.IconTextPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.swing.customizer.path.PathBrowserPM;
import org.beanfabrics.swing.customizer.path.PathContext;
import org.beanfabrics.swing.customizer.util.AbstractCustomizerPM;

/**
 * The <code>ModelSubscriberCustomizerPM</code> is the presentation model for
 * the {@link ModelSubscriberCustomizer}.
 * 
 * @author Michael Karneim
 */
public class ModelSubscriberCustomizerPM extends AbstractCustomizerPM {
    private static final URL ICON_BEANFABRICS = ModelSubscriberCustomizerPM.class.getResource("/org/beanfabrics/swing/beanfabrics48c.png");
    PathBrowserPM pathBrowser = new PathBrowserPM();
    IconTextPM status = new IconTextPM();

    public interface Functions {
        public void setPath(Path path);
    }

    private Functions functions;
    private ModelSubscriber subscriber;
    private final MetadataRegistry metadata = PMManager.getInstance().getMetadata();

    public ModelSubscriberCustomizerPM() {
        PMManager.setup(this);
        status.setEditable(false);
        status.setIconUrl(ICON_BEANFABRICS);
    }

    public void setFunctions(Functions functions) {
        this.functions = functions;
    }

    public void setModelSubscriber(ModelSubscriber aSubscriber) {
        this.subscriber = aSubscriber;

        PresentationModelInfo requiredModelInfo;
        if (aSubscriber instanceof View) {
            ViewClassDecorator deco = new ViewClassDecorator(((View)aSubscriber).getClass());
            Class c = deco.getExpectedModelType();
            requiredModelInfo = this.metadata.getPresentationModelInfo(c);
        } else {
            requiredModelInfo = this.metadata.getPresentationModelInfo(PresentationModel.class);
        }

        IModelProvider provider = aSubscriber.getModelProvider();
        if (provider == null) {
            throw new IllegalStateException("Can't customize ModelSubscriber bean since no ModelProvider is assigned.");
        }
        Class<? extends PresentationModel> rootPresentationModelType = provider.getPresentationModelType();
        PathInfo rootPathInfo = this.metadata.getPathInfo(rootPresentationModelType);

        Path path = this.subscriber.getPath();

        this.pathBrowser.setPathContext(new PathContext(rootPathInfo, requiredModelInfo, path));

        String classname = this.subscriber.getClass().getName();
        String statusText = "This is the Beanfabrics JavaBeans customizer for the selected " + getBasename(classname) + " bean." + " \nThis bean can subscribe to an instance of <b>" + getBasename(requiredModelInfo.getJavaType().getName()) + "</b> "
                + "provided by the assigned " + getBasename(ModelProvider.class.getName()) + " and specified by a " + getBasename(Path.class.getName()) + "." + " \nPlease specify that " + getBasename(Path.class.getName()) + " here.";
        this.status.setText("<html><font face=\"arial,sansserif\">" + statusText + "</font></html>");
    }

    @OnChange(path = "pathBrowser.currentPath")
    void updateJavaBean() {
        if (pathBrowser != null && functions != null && pathBrowser.isPathValid()) {
            functions.setPath(pathBrowser.getCurrentPath());
        }
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