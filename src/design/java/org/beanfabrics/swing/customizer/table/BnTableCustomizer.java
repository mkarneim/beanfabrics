/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
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
import org.beanfabrics.swing.customizer.path.PathPanel;
import org.beanfabrics.swing.customizer.util.CustomizerBasePanel;
import org.beanfabrics.swing.customizer.util.CustomizerUtil;
import org.beanfabrics.swing.customizer.util.SeparatorLabel;
import org.beanfabrics.swing.customizer.util.TitlePanel;
import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.swing.table.BnTable;

/**
 * The <code>BnTableCustomizer</code> is a JavaBeans {@link Customizer} for a
 * {@link BnTable}.
 *
 * @author Michael Karneim
 */
public class BnTableCustomizer extends CustomizerBasePanel implements Customizer {
    private SeparatorLabel label_1;
    private SeparatorLabel label;
    private TitlePanel titlePanel;
    private ColumnListPanel columnListPanel;
    private PathPanel pathPanel;
    private JPanel headerPanel;
    private JPanel centerPanel;
    private ModelProvider localProvider;
    private BnTableCustomizerPM pModel;

    public BnTableCustomizer() {

        this.pModel = new BnTableCustomizerPM();
        this.getLocalProvider().setPresentationModel(this.pModel);
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
        this.pModel.setFunctions(new BnTableCustomizerPM.Functions() {

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
        this.pModel.setBnTable(bnTable);
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
     *
     * @return the local <code>ModelProvider</code>
     */
    protected ModelProvider getLocalProvider() {
        if (localProvider == null) {
            localProvider = new ModelProvider(); // @wb:location=16,477
            localProvider.setPresentationModelType(BnTableCustomizerPM.class);
        }
        return localProvider;
    }

    private JPanel getCenterPanel() {
        if (centerPanel == null) {
            centerPanel = new JPanel();
            final GridBagLayout gridBagLayout = new GridBagLayout();
            gridBagLayout.rowHeights = new int[] { 7, 7, 0, 7 };
            centerPanel.setLayout(gridBagLayout);
            final GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 1;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.ipadx = 282;
            gridBagConstraints.insets = new Insets(0, 0, 0, 0);
            centerPanel.add(getHeaderPanel(), gridBagConstraints);
            final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
            gridBagConstraints_1.fill = GridBagConstraints.BOTH;
            gridBagConstraints_1.weighty = 1;
            gridBagConstraints_1.weightx = 1;
            gridBagConstraints_1.gridx = 0;
            gridBagConstraints_1.gridy = 5;
            gridBagConstraints_1.insets = new Insets(0, 0, 0, 0);
            final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
            gridBagConstraints_2.weightx = 1;
            gridBagConstraints_2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints_2.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_2.gridy = 3;
            gridBagConstraints_2.gridx = 0;
            centerPanel.add(getLabel_1(), gridBagConstraints_2);
            centerPanel.add(getColumnListPanel(), gridBagConstraints_1);
            centerPanel.setOpaque(false);
        }
        return centerPanel;
    }

    private JPanel getHeaderPanel() {
        if (headerPanel == null) {
            final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
            gridBagConstraints_2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints_2.weightx = 1;
            gridBagConstraints_2.gridx = 0;
            gridBagConstraints_2.gridy = 1;
            gridBagConstraints_2.insets = new Insets(4, 4, 4, 4);
            centerPanel.add(getLabel(), gridBagConstraints_2);
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
            pathPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
            pathPanel.setPath(new org.beanfabrics.Path("this.path"));
            pathPanel.setModelProvider(getLocalProvider());
            pathPanel.setOpaque(false);
        }
        return pathPanel;
    }

    private ColumnListPanel getColumnListPanel() {
        if (columnListPanel == null) {
            columnListPanel = new ColumnListPanel();
            columnListPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
            columnListPanel.setPath(new org.beanfabrics.Path("this.columns"));
            columnListPanel.setModelProvider(getLocalProvider());
            columnListPanel.setOpaque(false);
        }
        return columnListPanel;
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

    private SeparatorLabel getLabel() {
        if (label == null) {
            label = new SeparatorLabel();
            label.setText("Choose Path to Model");
        }
        return label;
    }

    private JLabel getLabel_1() {
        if (label_1 == null) {
            label_1 = new SeparatorLabel();
            label_1.setText("Choose Visible Columns");
        }
        return label_1;
    }
}