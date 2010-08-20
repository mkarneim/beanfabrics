/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.path;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnAction;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.BnLabel;
import org.beanfabrics.swing.BnTextField;
import org.beanfabrics.swing.customizer.util.CustomizerUtil;
import org.beanfabrics.swing.customizer.util.SeparatorLabel;
import org.beanfabrics.swing.customizer.util.ToolbarButton;
import org.beanfabrics.swing.table.BnTable;
import org.beanfabrics.swing.table.BnColumnBuilder;

/**
 * The <code>PathBrowserPanel</code> is the view on a {@link PathBrowserPM}.
 * 
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
@SuppressWarnings("serial")
public class PathBrowserPanel extends JPanel implements View<PathBrowserPM>, ModelSubscriber {

    private BnAction gotoParentAction;
    private BnButton folderUpButton;
    private SeparatorLabel chooseModelSeparatorLabel;
    private JPanel panel_1;
    private JPanel panel;
    private BnLabel lbHeader;
    private BnAction gotoCurrentPathAction;
    private BnTextField tfType;
    private JLabel typeLabel;
    private JPanel headerPanel;
    private JLabel pathLabel;
    private BnTextField tfPath;
    private JPanel chooserPanel;
    private BnAction gotoSelectedChildAction;
    private BnTable bnTable;
    private JScrollPane scrollPane;
    private JPanel centerPanel;
    private Action transferFocusToTfPath;
    private Action transferFocusToBnTable;

    private final Link link = new Link(this);
    private ModelProvider localProvider;

    /**
     * Constructs a new <code>PathBrowserPanel</code>.
     */
    public PathBrowserPanel() {
        super();
        setLayout(new BorderLayout());
        add(getCenterPanel(), BorderLayout.CENTER);
        add(getHeaderPanel(), BorderLayout.NORTH);
        //
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
     * 
     * @return the local <code>ModelProvider</code>
     * @wbp.nonvisual location=16,477
     */
    protected ModelProvider getLocalProvider() {
        if (localProvider == null) {
            localProvider = new ModelProvider(); // @wb:location=16,477
            localProvider.setPresentationModelType(PathBrowserPM.class);
        }
        return localProvider;
    }

    /** {@inheritDoc} */
    public PathBrowserPM getPresentationModel() {
        return getLocalProvider().getPresentationModel();
    }

    /** {@inheritDoc} */
    public void setPresentationModel(PathBrowserPM pModel) {
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
            final GridBagLayout gridBagLayout = new GridBagLayout();
            gridBagLayout.rowHeights = new int[] { 7, 7 };
            centerPanel.setLayout(gridBagLayout);
            final GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.weighty = 1;
            gridBagConstraints.weightx = 1;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.ipadx = 256;
            gridBagConstraints.ipady = 21;
            final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
            gridBagConstraints_2.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_2.anchor = GridBagConstraints.WEST;
            gridBagConstraints_2.gridy = 1;
            gridBagConstraints_2.gridx = 0;
            centerPanel.add(getFolderUpButton(), gridBagConstraints_2);
            centerPanel.add(getChooserPanel(), gridBagConstraints);
            final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
            gridBagConstraints_1.insets = new Insets(4, 8, 4, 4);
            gridBagConstraints_1.weightx = 1;
            gridBagConstraints_1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints_1.gridx = 0;
            gridBagConstraints_1.gridy = 0;
            centerPanel.add(getChooseModelSeparatorLabel(), gridBagConstraints_1);
            centerPanel.setOpaque(false);
        }
        return centerPanel;
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setPreferredSize(new Dimension(200, 200));
            scrollPane.setViewportView(getBnTable());
            scrollPane.getViewport().setBackground(getBnTable().getBackground());
        }
        return scrollPane;
    }

    private BnTable getBnTable() {
        if (bnTable == null) {
            bnTable = new BnTable();
            bnTable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(final MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        getGotoSelectedChildAction().actionPerformed(null);
                    }
                }
            });
            bnTable.setPath(new org.beanfabrics.Path("this.children"));
            bnTable.setColumns(new BnColumnBuilder()
            	      .addColumn().withPath("this.name").withName("Name").withWidth(140).withWidthFixed(true)
            	      .addColumn().withPath("this.type").withName("Type").withWidth(200)
            	      .build());
            bnTable.setModelProvider(getLocalProvider());
            bnTable.setGridColor(bnTable.getBackground());
            bnTable.setIntercellSpacing(new Dimension(0, 0));
            bnTable.setRowHeight(bnTable.getRowHeight() + 2);
            bnTable.getActionMap().put("gotoSelectedChild", getGotoSelectedChildAction());
            bnTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "gotoSelectedChild");
            bnTable.getActionMap().put("transferFocus", getTransferFocusToTfPath());
            bnTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "transferFocus");
            bnTable.getActionMap().put("gotoParent", getGotoParentAction());
            bnTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "gotoParent");
        }
        return bnTable;
    }

    private BnAction getGotoSelectedChildAction() {
        if (gotoSelectedChildAction == null) {
            gotoSelectedChildAction = new BnAction(); // @wb:location=50,509
            gotoSelectedChildAction.setModelProvider(getLocalProvider());
            gotoSelectedChildAction.setPath(new Path("gotoSelectedChild"));
        }
        return gotoSelectedChildAction;
    }

    private JPanel getChooserPanel() {
        if (chooserPanel == null) {
            chooserPanel = new JPanel();
            chooserPanel.setOpaque(false);
            final GridBagLayout gridBagLayout = new GridBagLayout();
            gridBagLayout.columnWidths = new int[] { 7, 7 };
            chooserPanel.setLayout(gridBagLayout);
            final GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridx = 1;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 0.5;
            gridBagConstraints.insets = new Insets(4, 4, 4, 4);
            final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
            gridBagConstraints_2.anchor = GridBagConstraints.WEST;
            gridBagConstraints_2.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_2.gridy = 1;
            gridBagConstraints_2.gridx = 0;
            chooserPanel.add(getPathLabel(), gridBagConstraints_2);
            chooserPanel.add(getTfPath(), gridBagConstraints);
            final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
            gridBagConstraints_3.gridy = 2;
            gridBagConstraints_3.gridx = 0;
            gridBagConstraints_3.insets = new Insets(4, 4, 4, 4);
            chooserPanel.add(getTypeLabel(), gridBagConstraints_3);
            final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
            gridBagConstraints_4.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints_4.anchor = GridBagConstraints.WEST;
            gridBagConstraints_4.gridy = 2;
            gridBagConstraints_4.gridx = 1;
            gridBagConstraints_4.insets = new Insets(4, 4, 4, 4);
            chooserPanel.add(getTfType(), gridBagConstraints_4);
            final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
            gridBagConstraints_1.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_1.fill = GridBagConstraints.BOTH;
            gridBagConstraints_1.weighty = 1;
            gridBagConstraints_1.weightx = 1;
            gridBagConstraints_1.gridwidth = 3;
            gridBagConstraints_1.gridy = 0;
            gridBagConstraints_1.gridx = 0;
            chooserPanel.add(getPanel_1(), gridBagConstraints_1);
        }
        return chooserPanel;
    }

    private BnTextField getTfPath() {
        if (tfPath == null) {
            tfPath = new BnTextField();
            tfPath.setPath(new org.beanfabrics.Path("this.currentPath"));
            tfPath.setModelProvider(getLocalProvider());
            tfPath.setColumns(10);
            tfPath.getActionMap().put("gotoCurrentPath", getGotoCurrentPathAction());
            tfPath.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "gotoCurrentPath");
            tfPath.getActionMap().put("transferFocus", getTransferFocusToBnTable());
            tfPath.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "transferFocus");

        }
        return tfPath;
    }

    private JLabel getPathLabel() {
        if (pathLabel == null) {
            pathLabel = new JLabel();
            pathLabel.setText("Path");
        }
        return pathLabel;
    }

    private JPanel getHeaderPanel() {
        if (headerPanel == null) {
            headerPanel = new JPanel();
            headerPanel.setBackground(Color.WHITE);
            final GridBagLayout gridBagLayout = new GridBagLayout();
            gridBagLayout.columnWidths = new int[] { 0, 7 };
            headerPanel.setLayout(gridBagLayout);
            final GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridx = 1;
            gridBagConstraints.weightx = 1;
            gridBagConstraints.weighty = 0;
            gridBagConstraints.insets = new Insets(0, 0, 0, 0);
            headerPanel.add(getLbHeader(), gridBagConstraints);
            final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
            gridBagConstraints_1.ipady = 40;
            gridBagConstraints_1.gridy = 0;
            gridBagConstraints_1.gridx = 0;
            gridBagConstraints_1.insets = new Insets(4, 4, 4, 4);
            headerPanel.add(getPanel(), gridBagConstraints_1);
        }
        return headerPanel;
    }

    private JLabel getTypeLabel() {
        if (typeLabel == null) {
            typeLabel = new JLabel();
            typeLabel.setText("Type");
        }
        return typeLabel;
    }

    private BnTextField getTfType() {
        if (tfType == null) {
            tfType = new BnTextField();
            tfType.setOpaque(false);
            tfType.setFocusable(false);
            tfType.setPath(new org.beanfabrics.Path("this.currentType"));
            tfType.setModelProvider(getLocalProvider());
            tfType.setColumns(15);
        }
        return tfType;
    }

    private BnAction getGotoCurrentPathAction() {
        if (gotoCurrentPathAction == null) {
            gotoCurrentPathAction = new BnAction(); // @wb:location=70,570
            gotoCurrentPathAction.setModelProvider(getLocalProvider());
            gotoCurrentPathAction.setPath(new Path("gotoCurrentPath"));
        }
        return gotoCurrentPathAction;
    }

    private Action getTransferFocusToTfPath() {
        if (transferFocusToTfPath == null) {
            transferFocusToTfPath = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    getTfPath().requestFocusInWindow();
                }
            };
        }
        return this.transferFocusToTfPath;
    }

    private Action getTransferFocusToBnTable() {
        if (transferFocusToBnTable == null) {
            transferFocusToBnTable = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    getBnTable().requestFocusInWindow();
                }
            };
        }
        return this.transferFocusToBnTable;
    }

    private BnLabel getLbHeader() {
        if (lbHeader == null) {
            lbHeader = new BnLabel();
            lbHeader.setPath(new org.beanfabrics.Path("this.status"));
            lbHeader.setModelProvider(getLocalProvider());
        }
        return lbHeader;
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.setOpaque(false);
        }
        return panel;
    }

    protected JPanel getPanel_1() {
        if (panel_1 == null) {
            panel_1 = new JPanel();
            panel_1.setLayout(new BorderLayout());
            panel_1.add(getScrollPane());
        }
        return panel_1;
    }

    protected SeparatorLabel getChooseModelSeparatorLabel() {
        if (chooseModelSeparatorLabel == null) {
            chooseModelSeparatorLabel = new SeparatorLabel();
            chooseModelSeparatorLabel.setText("Choose Path to Model");
        }
        return chooseModelSeparatorLabel;
    }

    protected BnButton getFolderUpButton() {
        if (folderUpButton == null) {
            folderUpButton = createFolderUpButton();
            folderUpButton.setMargin(new Insets(2, 2, 2, 2));
            folderUpButton.setModelProvider(getLocalProvider());
            folderUpButton.setPath(new Path("this.gotoParent"));
            folderUpButton.setFocusable(false);
            folderUpButton.setIcon(new ImageIcon(getFolderUpIconURL()));
        }
        return folderUpButton;
    }

    protected BnAction getGotoParentAction() {
        if (gotoParentAction == null) {
            gotoParentAction = new BnAction(); // @wb:location=140,490
            gotoParentAction.setModelProvider(getLocalProvider());
            gotoParentAction.setPath(new Path("this.gotoParent"));
        }
        return gotoParentAction;
    }

    private BnButton createFolderUpButton() {
        if (CustomizerUtil.get().isWindows()) {
            BnButton result = new ToolbarButton();
            result.setBorderPainted(false);
            return result;
        } else {
            BnButton result = new BnButton();
            setTexturedButtonType(result);
            return result;
        }
    }

    private URL getFolderUpIconURL() {
        if (CustomizerUtil.get().isWindows()) {
            return getClass().getResource("upFolder-win.png");
        } else if (CustomizerUtil.get().isAquaLookAndFeel()) {
            return getClass().getResource("symbolArrowUp.png");
        } else {
            return getClass().getResource("upFolder-win.png");
        }
    }

    private static void setTexturedButtonType(AbstractButton btn) {
        // see http://developer.apple.com/technotes/tn2007/tn2196.html
        btn.putClientProperty("JButton.buttonType", "textured");
    }
}