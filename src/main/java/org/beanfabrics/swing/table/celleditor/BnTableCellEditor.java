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
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
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
import org.beanfabrics.swing.KeyBindingProcessor;
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
public class BnTableCellEditor implements TableCellEditor {

    private int clickCountToStart = 1;
    private EmptyPanel emptyPanel;

    private JComponent currentComponent;
    private TableCellEditor currentCellEditor;

    private final List<TableCellEditor> installedEditors = new ArrayList<TableCellEditor>();

    public BnTableCellEditor() {
        super();
        installDefaultEditors();
    }

    private void installDefaultEditors() {
        installedEditors.add(new MyBnCheckBoxCellEditor());
        installedEditors.add(new MyBnComboBoxCellEditor());
        installedEditors.add(new MyBnTextFieldCellEditor());
    }

    public List<TableCellEditor> getInstalledEditors() {
        return installedEditors;
    }

    /** {@inheritDoc} */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JComponent result = null;
        TableCellEditor editor = null;
        for (TableCellEditor aEd : installedEditors) {
            Component aComp = aEd.getTableCellEditorComponent(table, value, isSelected, row, column);
            if (aComp != null && aComp instanceof JComponent) {
                result = (JComponent)aComp;
                editor = aEd;
                break;
            }
        }
        if (result == null) {
            result = getEmptyPanel();
        }

        BnColumn bnCol = getBnColumn(table, column);

        if (bnCol.getOperationPath() != null) {
            result = createButtonDecorator(table, row, result, bnCol);
        }

        this.setCurrentComponent(result);
        this.setCurrentCellEditor(editor);
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

    public JComponent getCurrentComponent() {
        return currentComponent;
    }

    public void setCurrentComponent(JComponent currentComponent) {
        this.currentComponent = currentComponent;
    }

    public TableCellEditor getCurrentCellEditor() {
        return currentCellEditor;
    }

    public void setCurrentCellEditor(TableCellEditor currentCellEditor) {
        this.currentCellEditor = currentCellEditor;
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

    /** {@inheritDoc} */
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }

    /** {@inheritDoc} */
    public boolean shouldSelectCell(EventObject anEvent) {
        return currentCellEditor.shouldSelectCell(anEvent);
    }

    /** {@inheritDoc} */
    public void cancelCellEditing() {
        currentCellEditor.cancelCellEditing();
        this.setCurrentComponent(null);
        this.setCurrentCellEditor(null);
    }

    /** {@inheritDoc} */
    public boolean stopCellEditing() {
        boolean result = currentCellEditor.stopCellEditing();
        this.setCurrentComponent(null);
        this.setCurrentCellEditor(null);
        return result;
    }

    /** {@inheritDoc} */
    public void addCellEditorListener(CellEditorListener l) {
        currentCellEditor.addCellEditorListener(l);
    }

    /** {@inheritDoc} */
    public void removeCellEditorListener(CellEditorListener l) {
        currentCellEditor.removeCellEditorListener(l);
    }

    private static class MyBnTextFieldCellEditor extends AbstractCellEditor implements TableCellEditor {
        
        private ActionListener stopAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        };

        public MyBnTextFieldCellEditor() {            
        }
        
        private BnTextField createBnTextField() {
            BnTextField textField = new BnTextField();
            textField.setSelectAllOnFocusGainedEnabled(false);
            //textField.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            textField.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0));
            textField.addActionListener(this.stopAction);
            return textField;
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof ITextPM) {
                BnTextField textField = createBnTextField();
                textField.setPresentationModel((ITextPM)value);
                textField.setSelectAllOnFocusGainedEnabled(!isSelected);
                textField.selectAll();
                return textField;
            }
            return null;
        }

        public Object getCellEditorValue() {
            // in Beanfabrics we don't need to return a value
            return null;
        }

    }

    private static class MyBnComboBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
            
        private ActionListener stopAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        };
        private PopupMenuListener popupListener = new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                // do nothing special
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                fireEditingStopped();
            }

            public void popupMenuCanceled(PopupMenuEvent e) {
                fireEditingCanceled();
            }
        };

        public MyBnComboBoxCellEditor() {
            super();
        }

        private BnComboBox createBnComboBox() {
            BnComboBox comboBox = new BnComboBox();
            comboBox.addPopupMenuListener(popupListener);
            comboBox.registerKeyboardAction(stopAction, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);
            return comboBox;
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof ITextPM) {
                if (((ITextPM)value).getOptions() != null) {
                    BnComboBox comboBox = createBnComboBox();
                    comboBox.setPresentationModel((ITextPM)value);
                    return comboBox;
                }
            }
            return null;
        }

        public Object getCellEditorValue() {
            // in Beanfabrics we don't need to return a value
            return null;
        }
    }

    private static class MyBnCheckBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
        
        private ActionListener stopAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        };

        public MyBnCheckBoxCellEditor() {
            
        }
        
        private BnCheckBox createBnCheckBox() {
            BnCheckBox checkBox = new BnCheckBox();
            checkBox.setHorizontalAlignment(SwingConstants.CENTER);
            checkBox.addActionListener(stopAction);
            return checkBox;
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof IBooleanPM) {
                BnCheckBox checkBox = createBnCheckBox();
                checkBox.setPresentationModel((IBooleanPM)value);
                return checkBox;
            }
            return null;
        }

        public Object getCellEditorValue() {
            // in Beanfabrics we don't need to return a value
            return null;
        }

        public boolean shouldSelectCell(EventObject anEvent) {
            return false;
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
            this.modelProvider.setPresentationModel(rootModel);
        }

        public void dismiss() {
            this.modelProvider.setPresentationModel(null);
        }

        @Override
        public void requestFocus() {
            this.leftComponent.requestFocus();
        }

        protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
            boolean result = super.processKeyBinding(ks, e, condition, pressed);
            if (result == false) {
                if (leftComponent instanceof KeyBindingProcessor) {
                    ((KeyBindingProcessor)leftComponent).processKeyBinding(ks, e, condition, pressed);
                }

                //                final KeyEvent newEvt = new KeyEvent(leftComponent, e.getID(), e.getWhen(), e.getModifiers(), e.getKeyCode(), e.getKeyChar(), e.getKeyLocation());
                //                EventQueue.invokeLater( new Runnable() {
                //                    public void run() {
                //                        leftComponent.dispatchEvent( newEvt);         
                //                    }                    
                //                });                       
            }
            return result;
        }
    }

}