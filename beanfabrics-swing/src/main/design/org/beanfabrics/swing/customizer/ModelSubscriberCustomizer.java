/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.Customizer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.swing.customizer.path.PathPanel;
import org.beanfabrics.swing.customizer.util.CustomizerBasePanel;
import org.beanfabrics.swing.customizer.util.CustomizerUtil;
import org.beanfabrics.swing.customizer.util.TitlePanel;

/**
 * The <code>ModelSubscriberCustomizer</code> is a Java Beans {@link Customizer}
 * for a {@link ModelSubscriber}.
 *
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ModelSubscriberCustomizer extends CustomizerBasePanel<ModelSubscriberCustomizerPM> implements Customizer {
    private JPanel centerPanel;
    private ModelProvider localProvider;
    private ModelSubscriberCustomizerPM modelSubscriberCustomizerPM;
    private JLabel lblPathToPresentation;
    private PathPanel pathPanel;
    private TitlePanel titlePanel;

    public ModelSubscriberCustomizer() {
        setPresentationModel(getModelSubscriberCustomizerPM());
        setLayout(new BorderLayout());
        this.add(getCenterPanel());
        setBackground(getDefaultBackground());

        CustomizerUtil.get().setupGUI(this);
        add(getTitlePanel(), BorderLayout.NORTH);
    }

    @Override
	public void setObject(Object bean) {
        if (bean == null || bean instanceof ModelSubscriber) {
            try {
                setModelSubscriber((ModelSubscriber)bean);
            } catch (Throwable t) {
                showException(t);
            }
        } else {
            showMessage("Can't customize this instance of " + bean.getClass().getName() + ". Expected instance of " + ModelSubscriber.class.getName());
        }
    }

    public void setModelSubscriber(final ModelSubscriber aSubscriber) {
        getPresentationModel().setFunctions(new ModelSubscriberCustomizerPM.Functions() {
            @Override
			public void setPath(Path path) {
                Path oldValue = (aSubscriber == null ? null : aSubscriber.getPath());
                aSubscriber.setPath(path);
                ModelSubscriberCustomizer.this.firePropertyChange("path", oldValue, path);
            }
        });
        getPresentationModel().setModelSubscriber(aSubscriber);
    }

    private JPanel getCenterPanel() {
        if (centerPanel == null) {
            centerPanel = new JPanel();
            centerPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
            centerPanel.setOpaque(false);
            GridBagLayout gbl_centerPanel = new GridBagLayout();
            gbl_centerPanel.columnWidths = new int[] { 0, 0, 0 };
            gbl_centerPanel.rowHeights = new int[] { 0, 0, 0 };
            gbl_centerPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
            gbl_centerPanel.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
            centerPanel.setLayout(gbl_centerPanel);
            GridBagConstraints gbc_lblPathToPresentation = new GridBagConstraints();
            gbc_lblPathToPresentation.insets = new Insets(0, 0, 5, 5);
            gbc_lblPathToPresentation.gridx = 0;
            gbc_lblPathToPresentation.gridy = 0;
            centerPanel.add(getLblPathToPresentation(), gbc_lblPathToPresentation);
            GridBagConstraints gbc_pathPanel = new GridBagConstraints();
            gbc_pathPanel.insets = new Insets(0, 0, 5, 0);
            gbc_pathPanel.fill = GridBagConstraints.BOTH;
            gbc_pathPanel.gridx = 1;
            gbc_pathPanel.gridy = 0;
            centerPanel.add(getPathPanel(), gbc_pathPanel);
        }
        return centerPanel;
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
     *
     * @wbp.nonvisual location=10,430
     * @return the local <code>ModelProvider</code>
     */
    protected ModelProvider getLocalProvider() {
        if (localProvider == null) {
            localProvider = new ModelProvider(); // @wb:location=16,477
            localProvider.setPresentationModelType(ModelSubscriberCustomizerPM.class);
        }
        return localProvider;
    }

    @Override
	public void setPresentationModel(ModelSubscriberCustomizerPM aPm) {
        super.setPresentationModel(aPm);
        getLocalProvider().setPresentationModel(aPm);
    }

    public ModelSubscriberCustomizerPM getPresentationModel() {
        return getLocalProvider().getPresentationModel();
    }

    private Color getDefaultBackground() {
        if (CustomizerUtil.get().isAquaLookAndFeel()) {
            return new Color(232, 232, 232);
        } else {
            return getBackground();
        }
    }

    /**
     * @wbp.nonvisual location=-4,481
     */
    private ModelSubscriberCustomizerPM getModelSubscriberCustomizerPM() {
        if (modelSubscriberCustomizerPM == null) {
            modelSubscriberCustomizerPM = new ModelSubscriberCustomizerPM();
        }
        return modelSubscriberCustomizerPM;
    }

    private JLabel getLblPathToPresentation() {
        if (lblPathToPresentation == null) {
            lblPathToPresentation = new JLabel("Path to Presentation Model");
        }
        return lblPathToPresentation;
    }

    private PathPanel getPathPanel() {
        if (pathPanel == null) {
            pathPanel = new PathPanel();
            pathPanel.setPath(new Path("this.path"));
            pathPanel.setModelProvider(getLocalProvider());
        }
        return pathPanel;
    }

    private TitlePanel getTitlePanel() {
        if (titlePanel == null) {
            titlePanel = new TitlePanel();
            titlePanel.setPath(new Path("this.title"));
            titlePanel.setModelProvider(getLocalProvider());
        }
        return titlePanel;
    }
}