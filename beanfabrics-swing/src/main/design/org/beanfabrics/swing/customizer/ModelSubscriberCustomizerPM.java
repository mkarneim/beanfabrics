/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.ViewClassDecorator;
import org.beanfabrics.meta.MetadataRegistry;
import org.beanfabrics.meta.PathElementInfo;
import org.beanfabrics.meta.TypeInfo;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.swing.customizer.path.PathContext;
import org.beanfabrics.swing.customizer.path.PathPM;
import org.beanfabrics.swing.customizer.util.AbstractCustomizerPM;

/**
 * The <code>ModelSubscriberCustomizerPM</code> is the presentation model for
 * the {@link ModelSubscriberCustomizer}.
 * 
 * @author Michael Karneim
 */
public class ModelSubscriberCustomizerPM extends AbstractCustomizerPM {

    protected final PathPM path = new PathPM();

    public interface Functions {
        public void setPath(Path path);
    }

    private Functions functions;
    private ModelSubscriber subscriber;
    private final MetadataRegistry metadata = PMManager.getInstance().getMetadata();

    public ModelSubscriberCustomizerPM() {
        PMManager.setup(this);
    }

    public void setFunctions(Functions functions) {
        this.functions = functions;
    }

    public void setModelSubscriber(ModelSubscriber aSubscriber) {
        this.subscriber = aSubscriber;

        TypeInfo requiredModelTypeInfo;
        if (aSubscriber instanceof View) {
            ViewClassDecorator deco = new ViewClassDecorator(((View)aSubscriber).getClass());
            Class c = deco.getExpectedModelType();
            requiredModelTypeInfo = this.metadata.getTypeInfo(c);
        } else {
            requiredModelTypeInfo = this.metadata.getTypeInfo(PresentationModel.class);
        }

        IModelProvider provider = aSubscriber.getModelProvider();
        if (provider == null) {
            throw new IllegalStateException("Can't customize ModelSubscriber bean since no ModelProvider is assigned.");
        }
        Class<? extends PresentationModel> rootPresentationModelType = provider.getPresentationModelType();
        PathElementInfo rootPathElementInfo = this.metadata.getPathElementInfo(rootPresentationModelType);

        Path path = this.subscriber.getPath();

        this.path.setPathContext(new PathContext(rootPathElementInfo, requiredModelTypeInfo, path));
    }

    @OnChange(path = "path")
    void applyPath() {
        if (functions != null && path.isValid()) {
            functions.setPath(path.getPath());
        }
    }

}