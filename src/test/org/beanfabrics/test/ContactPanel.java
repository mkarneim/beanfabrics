/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.BnCheckBox;
import org.beanfabrics.swing.BnTextField;

@SuppressWarnings("serial")
public class ContactPanel extends JPanel implements View<ContactPM>, ModelSubscriber {
    private BnButton bnButton;
    private final Link link = new Link(this);
    private ModelProvider localProvider = null; //@jve:decl-index=0:visual-constraint="376,115"  @wb:location=38,265
    private JLabel notesLabel;
    private BnTextField notesTextField;
    private BnCheckBox hasWifeCheckBox;
    private BnTextField hasWifeTextField;
    private JPanel contentPanel = null;
    private JLabel firstnameLabel = null;
    private JLabel lastnameLabel = null;
    private JLabel birthdayLabel = null;
    private BnTextField firstnameTextField = null;
    private BnTextField lastnameTextField = null;
    private BnTextField birthdayTextField = null;

    public ContactPanel() {
        super();
        initialize();
    }

    /** {@inheritDoc} */
    public ContactPM getPresentationModel() {
        return getLocalProvider().getPresentationModel();
    }

    /** {@inheritDoc} */
    public void setPresentationModel(ContactPM pModel) {
        getLocalProvider().setPresentationModel(pModel);
    }

    /** {@inheritDoc} */
    public IModelProvider getModelProvider() {
        return link.getModelProvider();
    }

    /** {@inheritDoc} */
    public void setModelProvider(IModelProvider provider) {
        this.link.setModelProvider(provider);
    }

    /** {@inheritDoc} */
    public Path getPath() {
        return link.getPath();
    }

    /** {@inheritDoc} */
    public void setPath(Path path) {
        this.link.setPath(path);
    }

    private void initialize() {
        this.setSize(300, 200);
        this.setLayout(new BorderLayout());
        this.add(getContentPanel(), BorderLayout.CENTER);
    }

    private ModelProvider getLocalProvider() {
        if (localProvider == null) {
            localProvider = new ModelProvider(); // @wb:location=15,360
            localProvider.setPresentationModelType(ContactPM.class);
        }
        return localProvider;
    }

    private JPanel getContentPanel() {
        if (contentPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.gridy = 2;
            gridBagConstraints5.weightx = 1.0;
            gridBagConstraints5.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints5.gridx = 1;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.gridy = 1;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints4.gridx = 1;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints3.gridx = 1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.weightx = 0.0;
            gridBagConstraints2.anchor = GridBagConstraints.EAST;
            gridBagConstraints2.gridy = 2;
            birthdayLabel = new JLabel();
            birthdayLabel.setText("Birthday");
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.weightx = 0.0;
            gridBagConstraints1.anchor = GridBagConstraints.EAST;
            gridBagConstraints1.gridy = 1;
            lastnameLabel = new JLabel();
            lastnameLabel.setText("Lastname");
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 0.0;
            gridBagConstraints.anchor = GridBagConstraints.EAST;
            gridBagConstraints.gridy = 0;
            firstnameLabel = new JLabel();
            firstnameLabel.setText("Firstname");
            contentPanel = new JPanel();
            final GridBagLayout gridBagLayout = new GridBagLayout();
            gridBagLayout.rowHeights = new int[] { 0, 0, 0, 7, 7, 7, 7 };
            contentPanel.setLayout(gridBagLayout);
            contentPanel.add(firstnameLabel, gridBagConstraints);
            contentPanel.add(lastnameLabel, gridBagConstraints1);
            contentPanel.add(birthdayLabel, gridBagConstraints2);
            contentPanel.add(getFirstnameTextField(), gridBagConstraints3);
            contentPanel.add(getLastnameTextField(), gridBagConstraints4);
            contentPanel.add(getBirthdayTextField(), gridBagConstraints5);
            final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
            gridBagConstraints_1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints_1.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_1.gridy = 3;
            gridBagConstraints_1.gridx = 1;
            contentPanel.add(getHasWifeTextField(), gridBagConstraints_1);
            final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
            gridBagConstraints_2.anchor = GridBagConstraints.WEST;
            gridBagConstraints_2.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_2.gridy = 4;
            gridBagConstraints_2.gridx = 1;
            contentPanel.add(getHasWifeCheckBox(), gridBagConstraints_2);
            final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
            gridBagConstraints_3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints_3.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_3.gridy = 5;
            gridBagConstraints_3.gridx = 1;
            final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
            gridBagConstraints_4.anchor = GridBagConstraints.EAST;
            gridBagConstraints_4.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_4.gridy = 5;
            gridBagConstraints_4.gridx = 0;
            contentPanel.add(getNotesLabel(), gridBagConstraints_4);
            contentPanel.add(getNotesTextField(), gridBagConstraints_3);
            final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
            gridBagConstraints_5.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints_5.gridy = 6;
            gridBagConstraints_5.gridx = 1;
            contentPanel.add(getBnButton(), gridBagConstraints_5);
        }
        return contentPanel;
    }

    private BnTextField getFirstnameTextField() {
        if (firstnameTextField == null) {
            firstnameTextField = new BnTextField();
            firstnameTextField.setPath(new Path("firstname"));
            firstnameTextField.setModelProvider(getLocalProvider());
        }
        return firstnameTextField;
    }

    private BnTextField getLastnameTextField() {
        if (lastnameTextField == null) {
            lastnameTextField = new BnTextField();
            lastnameTextField.setPath(new Path("this.lastname"));
            lastnameTextField.setModelProvider(getLocalProvider());
        }
        return lastnameTextField;
    }

    private BnTextField getBirthdayTextField() {
        if (birthdayTextField == null) {
            birthdayTextField = new BnTextField();
            birthdayTextField.setPath(new Path("this.birthday"));
            birthdayTextField.setModelProvider(getLocalProvider());
        }
        return birthdayTextField;
    }

    protected BnTextField getHasWifeTextField() {
        if (hasWifeTextField == null) {
            hasWifeTextField = new BnTextField();
            hasWifeTextField.setPath(new org.beanfabrics.Path("this.hasWife"));
            hasWifeTextField.setModelProvider(getLocalProvider());
        }
        return hasWifeTextField;
    }

    protected BnCheckBox getHasWifeCheckBox() {
        if (hasWifeCheckBox == null) {
            hasWifeCheckBox = new BnCheckBox();
            hasWifeCheckBox.setPath(new Path("this.hasWife"));
            hasWifeCheckBox.setModelProvider(getLocalProvider());
            hasWifeCheckBox.setText("has wife");
        }
        return hasWifeCheckBox;
    }

    protected BnTextField getNotesTextField() {
        if (notesTextField == null) {
            notesTextField = new BnTextField();
            notesTextField.setPath(new Path("this.notes.content"));
            notesTextField.setModelProvider(getLocalProvider());
        }
        return notesTextField;
    }

    protected JLabel getNotesLabel() {
        if (notesLabel == null) {
            notesLabel = new JLabel();
            notesLabel.setText("Notes");
        }
        return notesLabel;
    }

    private BnButton getBnButton() {
        if (bnButton == null) {
            bnButton = new BnButton();
            bnButton.setModelProvider(getLocalProvider());
            bnButton.setText("New BnButton");
        }
        return bnButton;
    }
}