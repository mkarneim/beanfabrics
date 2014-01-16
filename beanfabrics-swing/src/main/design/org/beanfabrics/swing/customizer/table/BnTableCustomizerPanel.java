/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.customizer.path.PathPanel;
import org.beanfabrics.swing.customizer.util.CustomizerUtil;
import org.beanfabrics.swing.customizer.util.TitlePanel;

/**
 * The <code>BnTableCustomizerPanel</code> is a View on a {@link BnTableCustomizerPM}.
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BnTableCustomizerPanel extends JPanel implements View<BnTableCustomizerPM>, ModelSubscriber {
    private final Link link = new Link(this);
    private TitlePanel titlePanel;
    private PathPanel pathPanel;
    private JPanel contentPanel;
    private ModelProvider localProvider;
    private BnButton bnbtnConfigureColumn;
    private JLabel lblPathToPresentation;
    private JLabel lblVisibleColumns;

    public BnTableCustomizerPanel() {
        setLayout(new BorderLayout());
        add(getContentPanel(), BorderLayout.CENTER);
        add(getTitlePanel(), BorderLayout.NORTH);
        setBackground(getDefaultBackground());
        //
        CustomizerUtil.get().setupGUI(this);
    }

    @Override
    public IModelProvider getModelProvider() {
        return link.getModelProvider();
    }

    @Override
    public void setModelProvider(IModelProvider provider) {
        link.setModelProvider(provider);
    }

    @Override
    public Path getPath() {
        return link.getPath();
    }

    @Override
    public void setPath(Path path) {
        link.setPath(path);
    }

    @Override
    public BnTableCustomizerPM getPresentationModel() {
        return getLocalProvider().getPresentationModel();
    }

    @Override
    public void setPresentationModel(BnTableCustomizerPM pModel) {
        getLocalProvider().setPresentationModel(pModel);
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
     * 
     * @wbp.nonvisual location=16,357
     * @return the local <code>ModelProvider</code>
     */
    protected ModelProvider getLocalProvider() {
        if (localProvider == null) {
            localProvider = new ModelProvider(); // @wb:location=16,577
            localProvider.setPresentationModelType(BnTableCustomizerPM.class);
        }
        return localProvider;
    }

    public JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            final GridBagLayout gbl_contentPanel = new GridBagLayout();
            gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 1.0};
            gbl_contentPanel.columnWidths = new int[]{0, 0, 0};
            gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 1.0 };
            gbl_contentPanel.rowHeights = new int[] { 0, 7, 0 };
            contentPanel.setLayout(gbl_contentPanel);
            GridBagConstraints gbc_lblPathToPresentation = new GridBagConstraints();
            gbc_lblPathToPresentation.insets = new Insets(0, 0, 5, 5);
            gbc_lblPathToPresentation.gridx = 0;
            gbc_lblPathToPresentation.gridy = 0;
            contentPanel.add(getLblPathToPresentation(), gbc_lblPathToPresentation);
            contentPanel.setOpaque(false);
            GridBagConstraints gbc_pathPanel = new GridBagConstraints();
            gbc_pathPanel.gridwidth = 2;
            gbc_pathPanel.fill = GridBagConstraints.HORIZONTAL;
            gbc_pathPanel.insets = new Insets(0, 0, 5, 5);
            gbc_pathPanel.gridx = 1;
            gbc_pathPanel.gridy = 0;
            contentPanel.add(getPathPanel(), gbc_pathPanel);
            GridBagConstraints gbc_lblVisibleColumns = new GridBagConstraints();
            gbc_lblVisibleColumns.anchor = GridBagConstraints.EAST;
            gbc_lblVisibleColumns.insets = new Insets(0, 0, 5, 5);
            gbc_lblVisibleColumns.gridx = 0;
            gbc_lblVisibleColumns.gridy = 1;
            contentPanel.add(getLblVisibleColumns(), gbc_lblVisibleColumns);
            GridBagConstraints gbc_bnbtnConfigureColumn = new GridBagConstraints();
            gbc_bnbtnConfigureColumn.fill = GridBagConstraints.HORIZONTAL;
            gbc_bnbtnConfigureColumn.insets = new Insets(0, 0, 5, 5);
            gbc_bnbtnConfigureColumn.gridx = 1;
            gbc_bnbtnConfigureColumn.gridy = 1;
            contentPanel.add(getBnbtnConfigureColumn(), gbc_bnbtnConfigureColumn);
        }
        return contentPanel;
    }

    private PathPanel getPathPanel() {
        if (pathPanel == null) {
            pathPanel = new PathPanel();
            pathPanel.setPath(new org.beanfabrics.Path("this.path"));
            pathPanel.setModelProvider(getLocalProvider());
            pathPanel.setOpaque(false);
        }
        return pathPanel;
    }

    protected TitlePanel getTitlePanel() {
        if (titlePanel == null) {
            titlePanel = new TitlePanel();
            titlePanel.setPath(new org.beanfabrics.Path("this.title"));
            titlePanel.setModelProvider(getLocalProvider());
        }
        return titlePanel;
    }

    private Color getDefaultBackground() {
        if (CustomizerUtil.get().isAquaLookAndFeel()) {
            return new Color(232, 232, 232);
        } else {
            return getBackground();
        }
    }

    private BnButton getBnbtnConfigureColumn() {
        if (bnbtnConfigureColumn == null) {
            bnbtnConfigureColumn = new BnButton();
            bnbtnConfigureColumn.setPath(new Path("this.configureColumns"));
            bnbtnConfigureColumn.setText("Configure...");
            bnbtnConfigureColumn.setModelProvider(getLocalProvider());
        }
        return bnbtnConfigureColumn;
    }

    private JLabel getLblPathToPresentation() {
        if (lblPathToPresentation == null) {
            lblPathToPresentation = new JLabel("Path to Presentation Model");
        }
        return lblPathToPresentation;
    }

    private JLabel getLblVisibleColumns() {
        if (lblVisibleColumns == null) {
            lblVisibleColumns = new JLabel("Visible Columns");
        }
        return lblVisibleColumns;
    }
}