/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.table.celleditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.IBooleanPM;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.BnButton;
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
        installedEditors.add(new BnCheckBoxCellEditor());
        installedEditors.add(new BnComboBoxCellEditor());
        installedEditors.add(new BnTextFieldCellEditor());
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