/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IIconPM;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.IValuePM;
import org.beanfabrics.model.PresentationModel;

/**
 * The <code>BnIconLabel</code> is a {@link JLabel} that can subscribe to an
 * {@link IIconPM}.
 * 
 * @author Michael Karneim
 * @author Marcel Eyke
 * @beaninfo
 */
@SuppressWarnings("serial")
public class BnIconLabel extends JLabel implements View<IIconPM>, ModelSubscriber {
    private final Link link = new Link(this);

    private final PropertyChangeListener listener = new WeakPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            refresh();
        }
    };
    private IIconPM pModel;

    /**
     * Constructs a <code>BnIconLabel</code>.
     */
    public BnIconLabel() {
    }

    /**
     * Constructs a <code>BnIconLabel</code> and binds it to the specified
     * model.
     * 
     * @param pModel the model
     */
    public BnIconLabel(IIconPM pModel) {
        this();
        setPresentationModel(pModel);
    }

    /**
     * Constructs a <code>BnIconLabel</code> and subscribes it for the model at
     * the specified Path provided by the given provider.
     * 
     * @param provider
     * @param path
     */
    public BnIconLabel(ModelProvider provider, Path path) {
        this.setModelProvider(provider);
        this.setPath(path);
    }

    /**
     * Constructs a <code>BnIconLabel</code> and subscribes it for the model at
     * the root level provided by the given provider.
     * 
     * @param provider
     */
    public BnIconLabel(ModelProvider provider) {
        this.setModelProvider(provider);
        this.setPath(new Path());
    }

    /**
     * Returns whether this component is connected to the target
     * {@link AbstractPM} to synchronize with.
     * 
     * @return <code>true</code> when this component is connected, else
     *         <code>false</code>
     */
    boolean isConnected() {
        return this.pModel != null;
    }

    /**
     * Configures this component depending on the target {@link AbstractPM} s
     * attributes.
     */
    private void refresh() {
        refreshText();
        refreshIcon();
        refreshToolTip();
    }

    /**
     * Configures the tool tip of this component on the depending on the target
     * {@link IValuePM}s attributes.
     */
    private void refreshToolTip() {
        if (this.pModel == null) {
            this.setToolTipText(null);
        } else {
            this.setToolTipText(pModel.isValid() == false ? pModel.getValidationState().getMessage() : pModel.getDescription());
        }
    }

    /**
     * Configures the tool tip of this component on the depending on the target
     * {@link IIconPM}s attributes.
     */
    private void refreshIcon() {
        if (this.pModel != null) {
            this.setIcon(this.pModel.getIcon());
        } else {
            this.setIcon(null);
        }
    }

    /**
     * Configures the text of this component on the depending on the target
     * {@link ITextPM}s attributes.
     */
    private void refreshText() {
        if (this.pModel instanceof ITextPM) {
            ITextPM textUiModel = (ITextPM)this.pModel;
            this.setText(textUiModel.getText());
        } else {
            this.setText("");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.paintErrorIcon(g);
    }

    /**
     * Paints an error icon on top of the given {@link Graphics} if this
     * component is connected to an {@link PresentationModel} and this
     * <code>PresentationModel</code> has an invalid validation state.
     * 
     * @param g the <code>Graphics</code> to paint the error icon to
     */
    protected void paintErrorIcon(Graphics g) {
        if (pModel != null && pModel.isValid() == false) {
            boolean isRightAligned = this.getHorizontalAlignment() == SwingConstants.RIGHT || this.getHorizontalAlignment() == SwingConstants.TRAILING;
            ErrorImagePainter.getInstance().paintTrailingErrorImage(g, this, isRightAligned);
        }
    }

    /** {@inheritDoc} */
    public IIconPM getPresentationModel() {
        return pModel;
    }

    /** {@inheritDoc} */
    public void setPresentationModel(IIconPM pModel) {
        if (pModel != null && pModel instanceof ITextPM == false && pModel instanceof IIconPM == false) {
            throw new IllegalArgumentException("pModel must be an instance of ITextPM or IIconPM");
        }
        if (this.pModel != null) {
            this.pModel.removePropertyChangeListener(listener);
        }
        this.pModel = pModel;
        if (this.pModel != null) {
            this.pModel.addPropertyChangeListener(listener);
        }
        refresh();
    }

    /** {@inheritDoc} */
    public void setModelProvider(IModelProvider provider) {
        this.link.setModelProvider(provider);
    }

    /** {@inheritDoc} */
    public IModelProvider getModelProvider() {
        return link.getModelProvider();
    }

    /** {@inheritDoc} */
    public void setPath(Path path) {
        this.link.setPath(path);
    }

    /** {@inheritDoc} */
    public Path getPath() {
        return link.getPath();
    }
}