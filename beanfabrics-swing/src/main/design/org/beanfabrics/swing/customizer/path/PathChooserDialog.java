/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.path;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.customizer.util.CustomizerUtil;

/**
 * The <code>PathChooserDialog</code> is a view on a {@link PathChooserPM} and
 * allows choosing a valid Path inside a presentation model.
 * 
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
@SuppressWarnings("serial")
public class PathChooserDialog extends JDialog implements View<PathChooserPM>, ModelSubscriber {

    public static PathChooserDialog create(Window parent) {
        if (parent instanceof Dialog) {
            return new PathChooserDialog((Dialog)parent);
        } else {
            return new PathChooserDialog((Frame)parent);
        }
    }

    private JButton cancelButton;
    private BnButton okBnButton;
    private JPanel buttonPanel;
    private PathBrowserPanel pathChooserPanel;
    private JPanel centerPanel;
    private final Link link = new Link(this);
    private ModelProvider localProvider;

    private PathChooserDialog(Dialog dialog) {
        super(dialog);
        init();
    }

    private PathChooserDialog(Frame frame) {
        super(frame);
        init();
    }

    private void init() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getCenterPanel(), BorderLayout.CENTER);
        this.setBackground(this.getDefaultBackground());
        //
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
     * 
     * @wbp.nonvisual location=10,430
     * @return the local <code>ModelProvider</code>
     */
    protected ModelProvider getLocalProvider() {
        if (localProvider == null) {
            localProvider = new ModelProvider(); // @wb:location=10,430
            localProvider.setPresentationModelType(PathChooserPM.class);
        }
        return localProvider;
    }

    /** {@inheritDoc} */
    public PathChooserPM getPresentationModel() {
        return getLocalProvider().getPresentationModel();
    }

    /** {@inheritDoc} */
    public void setPresentationModel(PathChooserPM pModel) {
        getLocalProvider().setPresentationModel(pModel);
    }

    /** {@inheritDoc} */
    public IModelProvider getModelProvider() {
        return this.link.getModelProvider();
    }

    /** {@inheritDoc} */
    public void setModelProvider(IModelProvider provider) {
        this.link.setModelProvider(provider);
    }

    /** {@inheritDoc} */
    public Path getPath() {
        return this.link.getPath();
    }

    /** {@inheritDoc} */
    public void setPath(Path path) {
        this.link.setPath(path);
    }

    private JPanel getCenterPanel() {
        if (centerPanel == null) {
            centerPanel = new JPanel();
            centerPanel.setLayout(new BorderLayout());
            centerPanel.add(getPathChooserPanel(), BorderLayout.CENTER);
            centerPanel.add(getButtonPanel(), BorderLayout.SOUTH);
        }
        return centerPanel;
    }

    private PathBrowserPanel getPathChooserPanel() {
        if (pathChooserPanel == null) {
            pathChooserPanel = new PathBrowserPanel();
            pathChooserPanel.setPath(new org.beanfabrics.Path("this.pathBrowser"));
            pathChooserPanel.setModelProvider(getLocalProvider());
        }
        return pathChooserPanel;
    }

    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            final GridBagLayout gridBagLayout = new GridBagLayout();
            gridBagLayout.columnWidths = new int[] { 0, 7 };
            buttonPanel.setLayout(gridBagLayout);
            final GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new Insets(4, 4, 4, 5);
            buttonPanel.add(getOkBnButton(), gridBagConstraints);
            final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
            gridBagConstraints_1.anchor = GridBagConstraints.EAST;
            gridBagConstraints_1.weightx = 1;
            gridBagConstraints_1.weighty = 0;
            gridBagConstraints_1.insets = new Insets(4, 4, 4, 0);
            gridBagConstraints_1.gridy = 0;
            gridBagConstraints_1.gridx = 1;
            buttonPanel.add(getCancelButton(), gridBagConstraints_1);
        }
        return buttonPanel;
    }

    private BnButton getOkBnButton() {
        if (okBnButton == null) {
            okBnButton = new BnButton();
            okBnButton.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    dispose();
                }
            });
            okBnButton.setPath(new org.beanfabrics.Path("this.apply"));
            okBnButton.setModelProvider(getLocalProvider());
            okBnButton.setText("Ok");
        }
        return okBnButton;
    }

    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    dispose();
                }
            });
            cancelButton.setText("Cancel");
        }
        return cancelButton;
    }

    private Color getDefaultBackground() {
        if (CustomizerUtil.get().isAquaLookAndFeel()) {
            return new Color(232, 232, 232);
        } else {
            return getBackground();
        }
    }
}