/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.table.celleditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.IBooleanPM;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.BnCheckBox;
import org.beanfabrics.swing.BnComboBox;
import org.beanfabrics.swing.BnTextField;
import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.swing.table.BnTable;

/**
 * The <code>BnTableCellEditor</code> is a {@link TableCellEditor} for a
 * {@link PresentationModel} object inside a cell of a {@link BnTable}.
 * <p>
 * Currently supported presentation models are: {@link ITextPM},
 * {@link IBooleanPM}
 * </p>
 * 
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class BnTableCellEditor extends AbstractCellEditor implements TableCellEditor {
    private ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
        }
    };
    private int clickCountToStart = 1;
    private MyBnTextField textField;
    private MyBnComboBox comboBox;
    private MyBnCheckBox checkBox;
    private EmptyPanel emptyPanel;

    private JComponent currentComponent;

    public BnTableCellEditor() {
        super();
    }

    /** {@inheritDoc} */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JComponent result = chooseComponentFor((PresentationModel)value);
        BnColumn bnCol = getBnColumn(table, column);

        if (bnCol.getOperationPath() != null) {
            result = createButtonDecorator(table, row, result, bnCol);
        }

        this.setCurrentComponent(result);
        return result;
    }

    protected JComponent createButtonDecorator(JTable table, int row, JComponent leftComponent, BnColumn bnCol) {
        PresentationModel rowModel = getRowModel(table, row);
        ButtonDecorator deco = new ButtonDecorator(leftComponent, rowModel, bnCol.getOperationPath());
        return deco;
    }

    protected PresentationModel getRowModel(JTable table, int row) {
        BnTable bnTable = (BnTable)table;
        PresentationModel result = bnTable.getPresentationModel().getAt(row);
        return result;
    }

    private BnColumn getBnColumn(JTable table, int column) {
        BnTable bnTable = (BnTable)table;
        BnColumn result = bnTable.getColumns()[column];
        return result;
    }

    protected JComponent chooseComponentFor(PresentationModel pModel) {
        final JComponent result;
        // choose the appropriate component for the given model
        if (pModel instanceof IBooleanPM) {
            IBooleanPM booleanModel = (IBooleanPM)pModel;
            result = getCheckBox(booleanModel);
        } else if (pModel instanceof ITextPM) {
            ITextPM textModel = (ITextPM)pModel;
            if (textModel.getOptions() != null) {
                result = getComboBox(textModel);
            } else {
                result = getTextField(textModel);
            }
        } else {
            result = getEmptyPanel();
        }
        return result;
    }

    public JComponent getCurrentComponent() {
        return currentComponent;
    }

    public void setCurrentComponent(JComponent currentComponent) {
        this.currentComponent = currentComponent;
    }

    private MyBnTextField getTextField(ITextPM textPM) {
        if (this.textField == null) {
            this.textField = new MyBnTextField();
            this.textField.addActionListener(this.actionListener);
        }
        this.textField.setPresentationModel(textPM);
        return this.textField;
    }

    private MyBnComboBox getComboBox(ITextPM textPM) {
        if (this.comboBox == null) {
            this.comboBox = new MyBnComboBox();
            this.comboBox.addActionListener(this.actionListener);
        }
        this.comboBox.setPresentationModel(textPM);

        return this.comboBox;
    }

    private MyBnCheckBox getCheckBox(IBooleanPM booleanPM) {
        if (this.checkBox == null) {
            this.checkBox = new MyBnCheckBox();
            this.checkBox.addActionListener(this.actionListener);
        }
        this.checkBox.setPresentationModel(booleanPM);
        return this.checkBox;
    }

    private EmptyPanel getEmptyPanel() {
        if (this.emptyPanel == null) {
            this.emptyPanel = new EmptyPanel();
        }
        return this.emptyPanel;
    }

    /** {@inheritDoc} */
    public Object getCellEditorValue() {
        return null; // we don't support getting the value with this method
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public void cancelCellEditing() {
        fireEditingCanceled();
        this.setCurrentComponent(null);
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        this.setCurrentComponent(null);
        return true;
    }

    private static class MyBnTextField extends BnTextField {
        public MyBnTextField() {
            this.setSelectAllOnFocusGainedEnabled(false);
            this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }
    }

    private static class MyBnComboBox extends BnComboBox {
    }

    private static class MyBnCheckBox extends BnCheckBox {
        public MyBnCheckBox() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    private static class EmptyPanel extends JPanel {
    }

    private static class ButtonDecorator extends JPanel {
        private ModelProvider modelProvider = new ModelProvider();
        private JComponent leftComponent;
        private BnButton button = new BnButton();

        public ButtonDecorator(JComponent leftComponent, PresentationModel rootModel, Path operationPath) {
            this.leftComponent = leftComponent;
            this.setLayout(new BorderLayout(0, 0));
            this.button.setMargin(new Insets(2, 2, 2, 2));
            this.button.setText("...");
            this.button.setFocusable(false);
            this.button.setPreferredSize(new Dimension(20, 0));
            this.button.setModelProvider(modelProvider);
            this.button.setPath(operationPath);
            this.add(leftComponent, BorderLayout.CENTER);
            this.add(button, BorderLayout.EAST);
            this.setOpaque(false);
        }

        public void dismiss() {
            this.modelProvider.setPresentationModel(null);
        }

        @Override
        public void requestFocus() {
            this.leftComponent.requestFocus();
        }
    }
}