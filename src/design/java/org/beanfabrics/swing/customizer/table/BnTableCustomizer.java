/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

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
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.customizer.path.PathPanel;
import org.beanfabrics.swing.customizer.util.CustomizerBasePanel;
import org.beanfabrics.swing.customizer.util.CustomizerUtil;
import org.beanfabrics.swing.customizer.util.TitlePanel;
import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.swing.table.BnTable;

/**
 * The <code>BnTableCustomizer</code> is a JavaBeans {@link Customizer} for a
 * {@link BnTable}.
 * 
 * @author Michael Karneim
 */
public class BnTableCustomizer extends CustomizerBasePanel<BnTableCustomizerPM> implements Customizer {
    private TitlePanel titlePanel;
    private PathPanel pathPanel;
    private JPanel headerPanel;
    private JPanel centerPanel;
    private ModelProvider localProvider;
    private BnButton bnbtnConfigureColumn;
    private BnTableCustomizerPM bnTableCustomizerPM;
    private JLabel lblPathToPresentation;
    private JLabel lblVisibleColumns;

    public BnTableCustomizer() {
        setPresentationModel(getBnTableCustomizerPM());
        this.setLayout(new BorderLayout());
        add(getCenterPanel(), BorderLayout.CENTER);
        add(getTitlePanel(), BorderLayout.NORTH);
        setBackground(this.getDefaultBackground());
        //
        CustomizerUtil.get().setupGUI(this);
    }

    public void setObject(Object bean) {
        if (bean == null || bean instanceof BnTable) {
            try {
                setBnTable((BnTable)bean);
            } catch (Throwable t) {
                showException(t);
            }
        } else {
            showMessage("Can't customize this instance of " + bean.getClass().getName() + ". Expected instance of " + ModelSubscriber.class.getName());
        }
    }

    public void setBnTable(final BnTable bnTable) {
        this.getBnTableCustomizerPM().setFunctions(new BnTableCustomizerPM.Functions() {

            public void setPath(Path newValue) {
                Path oldValue = bnTable.getPath();
                bnTable.setPath(newValue);
                BnTableCustomizer.this.firePropertyChange("path", oldValue, newValue);
            }

            public void setBnColumns(BnColumn[] newValue) {
                BnColumn[] oldValue = bnTable.getColumns();
                bnTable.setColumns(newValue);
                BnTableCustomizer.this.firePropertyChange("columns", oldValue, newValue);
            }
        });
        this.getBnTableCustomizerPM().setBnTable(bnTable);
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
            localProvider.setPresentationModel(getBnTableCustomizerPM());
            localProvider.setPresentationModelType(BnTableCustomizerPM.class);
        }
        return localProvider;
    }

    private JPanel getCenterPanel() {
        if (centerPanel == null) {
            centerPanel = new JPanel();
            centerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            final GridBagLayout gridBagLayout = new GridBagLayout();
            gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0 };
            gridBagLayout.rowHeights = new int[] { 0, 7, 0 };
            centerPanel.setLayout(gridBagLayout);
            GridBagConstraints gbc_lblPathToPresentation = new GridBagConstraints();
            gbc_lblPathToPresentation.insets = new Insets(0, 0, 5, 5);
            gbc_lblPathToPresentation.gridx = 0;
            gbc_lblPathToPresentation.gridy = 0;
            centerPanel.add(getLblPathToPresentation(), gbc_lblPathToPresentation);
            final GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 1;
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.ipadx = 282;
            gridBagConstraints.insets = new Insets(0, 0, 5, 0);
            centerPanel.add(getHeaderPanel(), gridBagConstraints);
            centerPanel.setOpaque(false);
            GridBagConstraints gbc_lblVisibleColumns = new GridBagConstraints();
            gbc_lblVisibleColumns.anchor = GridBagConstraints.EAST;
            gbc_lblVisibleColumns.insets = new Insets(0, 0, 5, 5);
            gbc_lblVisibleColumns.gridx = 0;
            gbc_lblVisibleColumns.gridy = 1;
            centerPanel.add(getLblVisibleColumns(), gbc_lblVisibleColumns);
            GridBagConstraints gbc_bnbtnConfigureColumn = new GridBagConstraints();
            gbc_bnbtnConfigureColumn.insets = new Insets(0, 0, 5, 0);
            gbc_bnbtnConfigureColumn.anchor = GridBagConstraints.WEST;
            gbc_bnbtnConfigureColumn.gridx = 1;
            gbc_bnbtnConfigureColumn.gridy = 1;
            centerPanel.add(getBnbtnConfigureColumn(), gbc_bnbtnConfigureColumn);
        }
        return centerPanel;
    }

    private JPanel getHeaderPanel() {
        if (headerPanel == null) {
            headerPanel = new JPanel();
            headerPanel.setLayout(new BorderLayout());
            headerPanel.add(getPathPanel());
            headerPanel.setOpaque(false);
        }
        return headerPanel;
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

    /**
     * @wbp.nonvisual location=16,411
     */
    private BnTableCustomizerPM getBnTableCustomizerPM() {
        if (bnTableCustomizerPM == null) {
            bnTableCustomizerPM = new BnTableCustomizerPM();
        }
        return bnTableCustomizerPM;
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