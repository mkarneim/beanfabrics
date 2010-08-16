package org.beanfabrics.swing.customizer.table;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import java.awt.GridBagLayout;
import org.beanfabrics.swing.BnButton;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * The ColumnListConfigurationDialog is a {@link View} on a
 * {@link ColumnListConfigurationPM}.
 * 
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
@SuppressWarnings("serial")
public class ColumnListConfigurationDialog extends JDialog implements View<ColumnListConfigurationPM>, ModelSubscriber {

    public static ColumnListConfigurationDialog create(Window owner) {
        if (owner instanceof Dialog) {
            return new ColumnListConfigurationDialog((Dialog)owner);
        } else {
            return new ColumnListConfigurationDialog((Frame)owner);
        }
    }

    private final Link link = new Link(this);
    private ModelProvider localModelProvider;
    private JPanel buttonPanel;
    private ColumnListPanel columnListPanel;
    private BnButton bnbtnOk;
    private JButton btnCancel;

    /**
     * Constructs a new <code>ColumnListConfigurationDialog</code>.
     * 
     * @param dialog
     * @wbp.parser.constructor
     */
    private ColumnListConfigurationDialog(Dialog dialog) {
        super(dialog);
        init();
    }

    /**
     * Constructs a new <code>ColumnListConfigurationDialog</code>.
     * 
     * @param frame
     */
    private ColumnListConfigurationDialog(Frame frame) {
        super(frame);
        init();
    }

    public void init() {
        setTitle("Configure Visible Columns");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
        getContentPane().add(getColumnListPanel(), BorderLayout.CENTER);
        //
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
     * 
     * @return the local <code>ModelProvider</code>
     * @wbp.nonvisual location=10,430
     */
    protected ModelProvider getLocalModelProvider() {
        if (localModelProvider == null) {
            localModelProvider = new ModelProvider(); // @wb:location=10,430
            localModelProvider.setPresentationModelType(ColumnListConfigurationPM.class);
        }
        return localModelProvider;
    }

    /** {@inheritDoc} */
    public ColumnListConfigurationPM getPresentationModel() {
        return getLocalModelProvider().getPresentationModel();
    }

    /** {@inheritDoc} */
    public void setPresentationModel(ColumnListConfigurationPM pModel) {
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

    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            GridBagLayout gbl_buttonPanel = new GridBagLayout();
            gbl_buttonPanel.columnWidths = new int[]{0, 0, 0};
            gbl_buttonPanel.rowHeights = new int[]{0, 0};
            gbl_buttonPanel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
            gbl_buttonPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
            buttonPanel.setLayout(gbl_buttonPanel);
            GridBagConstraints gbc_bnbtnOk = new GridBagConstraints();
            gbc_bnbtnOk.anchor = GridBagConstraints.WEST;
            gbc_bnbtnOk.insets = new Insets(0, 0, 0, 5);
            gbc_bnbtnOk.gridx = 0;
            gbc_bnbtnOk.gridy = 0;
            buttonPanel.add(getBnbtnOk(), gbc_bnbtnOk);
            GridBagConstraints gbc_btnCancel = new GridBagConstraints();
            gbc_btnCancel.anchor = GridBagConstraints.EAST;
            gbc_btnCancel.gridx = 1;
            gbc_btnCancel.gridy = 0;
            buttonPanel.add(getBtnCancel(), gbc_btnCancel);
        }
        return buttonPanel;
    }

    private ColumnListPanel getColumnListPanel() {
        if (columnListPanel == null) {
            columnListPanel = new ColumnListPanel();
            columnListPanel.setPath(new Path("this.list"));
            columnListPanel.setModelProvider(getLocalModelProvider());
        }
        return columnListPanel;
    }
    private BnButton getBnbtnOk() {
        if (bnbtnOk == null) {
        	bnbtnOk = new BnButton();
        	bnbtnOk.addActionListener(new ActionListener() {
        	    public void actionPerformed(ActionEvent e) {
        	        dispose();
        	    }
        	});
        	bnbtnOk.setPath(new Path("this.apply"));
        	bnbtnOk.setText("Ok");
        	bnbtnOk.setModelProvider(getLocalModelProvider());
        }
        return bnbtnOk;
    }
    private JButton getBtnCancel() {
        if (btnCancel == null) {
        	btnCancel = new JButton("Cancel");
        	btnCancel.addActionListener(new ActionListener() {
        	    public void actionPerformed(ActionEvent e) {
        	        dispose();
        	    }
        	});
        }
        return btnCancel;
    }
}