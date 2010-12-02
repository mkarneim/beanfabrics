package org.beanfabrics.samples.regextester;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.BnTextArea;
import org.beanfabrics.swing.BnTextField;
import org.beanfabrics.swing.table.BnColumnBuilder;
import org.beanfabrics.swing.table.BnTable;

/*
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
@SuppressWarnings("serial")
public class RegexTesterPanel extends JPanel implements View<RegexTesterPM>, ModelSubscriber {
    private BnTextArea bnTextArea;
    private JScrollPane scrollPane;
    private JLabel groupsLabel;
    private BnTable bnTable;
    private JScrollPane scrollPane_1;
    private BnButton matchBnButton;
    private BnTextField matchesTextField;
    private JLabel matchesLabel;
    private BnTextField regexTextField;
    private JLabel regexLabel;
    private JLabel inputLabel;
    private final Link link = new Link(this);
    private ModelProvider localModelProvider;

    /**
     * Constructs a new <code>RegexTesterPanel</code>.
     */
    public RegexTesterPanel() {
        super();
        final GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };

        setLayout(gridBagLayout);
        final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
        gridBagConstraints_1.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints_1.insets = new Insets(4, 4, 4, 4);
        gridBagConstraints_1.gridx = 0;
        gridBagConstraints_1.gridy = 0;
        add(getInputLabel(), gridBagConstraints_1);
        final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
        gridBagConstraints_2.anchor = GridBagConstraints.WEST;
        gridBagConstraints_2.insets = new Insets(4, 4, 4, 4);
        gridBagConstraints_2.gridy = 1;
        gridBagConstraints_2.gridx = 0;
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(4, 4, 4, 4);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridx = 1;
        add(getScrollPane(), gridBagConstraints);
        add(getRegexLabel(), gridBagConstraints_2);
        final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
        gridBagConstraints_3.gridwidth = 2;
        gridBagConstraints_3.insets = new Insets(4, 4, 4, 4);
        gridBagConstraints_3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints_3.weightx = 1;
        gridBagConstraints_3.gridy = 1;
        gridBagConstraints_3.gridx = 1;
        add(getRegexTextField(), gridBagConstraints_3);
        final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
        gridBagConstraints_4.anchor = GridBagConstraints.WEST;
        gridBagConstraints_4.insets = new Insets(4, 4, 4, 4);
        gridBagConstraints_4.gridy = 2;
        gridBagConstraints_4.gridx = 1;
        final GridBagConstraints gridBagConstraints_6 = new GridBagConstraints();
        gridBagConstraints_6.insets = new Insets(4, 4, 4, 4);
        gridBagConstraints_6.anchor = GridBagConstraints.WEST;
        gridBagConstraints_6.gridy = 2;
        gridBagConstraints_6.gridx = 0;
        add(getMatchBnButton(), gridBagConstraints_6);
        add(getMatchesLabel(), gridBagConstraints_4);
        final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
        gridBagConstraints_5.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints_5.insets = new Insets(4, 4, 4, 4);
        gridBagConstraints_5.anchor = GridBagConstraints.WEST;
        gridBagConstraints_5.gridy = 2;
        gridBagConstraints_5.gridx = 2;
        add(getMatchesTextField(), gridBagConstraints_5);
        final GridBagConstraints gridBagConstraints_7 = new GridBagConstraints();
        gridBagConstraints_7.gridwidth = 2;
        gridBagConstraints_7.insets = new Insets(8, 0, 4, 4);
        gridBagConstraints_7.fill = GridBagConstraints.BOTH;
        gridBagConstraints_7.weighty = 1;
        gridBagConstraints_7.weightx = 1;
        gridBagConstraints_7.gridy = 3;
        gridBagConstraints_7.gridx = 1;
        final GridBagConstraints gridBagConstraints_8 = new GridBagConstraints();
        gridBagConstraints_8.insets = new Insets(8, 4, 4, 4);
        gridBagConstraints_8.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints_8.gridy = 3;
        gridBagConstraints_8.gridx = 0;
        add(getGroupsLabel(), gridBagConstraints_8);
        add(getScrollPane_1(), gridBagConstraints_7);

        //
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
     * 
     * @return the local <code>ModelProvider</code>
     * @wbp.nonvisual location=12,498
     */
    protected ModelProvider getLocalModelProvider() {
        if (localModelProvider == null) {
            localModelProvider = new ModelProvider(); // @wb:location=12,498
            localModelProvider.setPresentationModelType(RegexTesterPM.class);
        }
        return localModelProvider;
    }

    /** {@inheritDoc} */
    public RegexTesterPM getPresentationModel() {
        return getLocalModelProvider().getPresentationModel();

    }

    /** {@inheritDoc} */
    public void setPresentationModel(RegexTesterPM pModel) {
        getLocalModelProvider().setPresentationModel(pModel);
    }

    /** {@inheritDoc} */
    public IModelProvider getModelProvider() {
        return this.link.getModelProvider();
    }

    /** {@inheritDoc} */
    public void setModelProvider(IModelProvider modelProvider) {
        this.link.setModelProvider(modelProvider);
    }

    /** {@inheritDoc} */
    public Path getPath() {
        return this.link.getPath();
    }

    /** {@inheritDoc} */
    public void setPath(Path path) {
        this.link.setPath(path);
    }

    /**
     * @return
     */
    private JLabel getInputLabel() {
        if (inputLabel == null) {
            inputLabel = new JLabel();
            inputLabel.setText("Input");
        }
        return inputLabel;
    }

    /**
     * @return
     */
    private JLabel getRegexLabel() {
        if (regexLabel == null) {
            regexLabel = new JLabel();
            regexLabel.setText("Regex");
        }
        return regexLabel;
    }

    /**
     * @return
     */
    private BnTextField getRegexTextField() {
        if (regexTextField == null) {
            regexTextField = new BnTextField();
            regexTextField.setPath(new org.beanfabrics.Path("this.regex"));
            regexTextField.setModelProvider(getLocalModelProvider());
        }
        return regexTextField;
    }

    /**
     * @return
     */
    private JLabel getMatchesLabel() {
        if (matchesLabel == null) {
            matchesLabel = new JLabel();
            matchesLabel.setText("Matches entire region");
        }
        return matchesLabel;
    }

    /**
     * @return
     */
    private BnTextField getMatchesTextField() {
        if (matchesTextField == null) {
            matchesTextField = new BnTextField();
            matchesTextField.setPath(new org.beanfabrics.Path("this.doesMatchEntireRegion"));
            matchesTextField.setModelProvider(getLocalModelProvider());
            matchesTextField.setColumns(8);
        }
        return matchesTextField;
    }

    /**
     * @return
     */
    private BnButton getMatchBnButton() {
        if (matchBnButton == null) {
            matchBnButton = new BnButton();
            matchBnButton.setPath(new org.beanfabrics.Path("this.match"));
            matchBnButton.setModelProvider(getLocalModelProvider());
            matchBnButton.setText("Match");
        }
        return matchBnButton;
    }

    /**
     * @return
     */
    private JScrollPane getScrollPane_1() {
        if (scrollPane_1 == null) {
            scrollPane_1 = new JScrollPane();
            scrollPane_1.setViewportView(getBnTable());
        }
        return scrollPane_1;
    }

    /**
     * @return
     */
    private BnTable getBnTable() {
        if (bnTable == null) {
            bnTable = new BnTable();
            bnTable.setPath(new org.beanfabrics.Path("this.groups"));
            bnTable.setColumns(new BnColumnBuilder().addColumn().withPath("this.findIndex").withName("FindIndex").withWidth(80).withWidthFixed(true).addColumn().withPath("this.groupIndex").withName("GroupIndex").withWidth(80).withWidthFixed(true)
                    .addColumn().withPath("this.text").withName("Text").build());
            bnTable.setModelProvider(getLocalModelProvider());
        }
        return bnTable;
    }

    /**
     * @return
     */
    private JLabel getGroupsLabel() {
        if (groupsLabel == null) {
            groupsLabel = new JLabel();
            groupsLabel.setText("Groups");
        }
        return groupsLabel;
    }

    /**
     * @return
     */
    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setMinimumSize(new Dimension(200, 90));
            scrollPane.setPreferredSize(new Dimension(200, 90));
            scrollPane.setViewportView(getBnTextArea());
        }
        return scrollPane;
    }

    /**
     * @return
     */
    private BnTextArea getBnTextArea() {
        if (bnTextArea == null) {
            bnTextArea = new BnTextArea();
            bnTextArea.setPath(new org.beanfabrics.Path("this.input"));
            bnTextArea.setModelProvider(getLocalModelProvider());
            bnTextArea.setRows(5);
        }
        return bnTextArea;
    }

}