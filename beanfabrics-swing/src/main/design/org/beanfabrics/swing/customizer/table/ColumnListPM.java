/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

import java.util.ResourceBundle;

import org.beanfabrics.Path;
import org.beanfabrics.meta.PathElementInfo;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Validation;
import org.beanfabrics.swing.customizer.path.PathChooserPM;
import org.beanfabrics.swing.customizer.path.PathContext;
import org.beanfabrics.swing.customizer.util.CustomizerUtil;
import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.util.ResourceBundleFactory;
import org.beanfabrics.validation.ValidationState;

/**
 * The <code>ColumnListPM</code> is a {@link ListPM} for {@link ColumnPM}
 * elements.
 *
 * @author Michael Karneim
 */
public class ColumnListPM extends ListPM<ColumnPM> {
    protected static final String KEY_MESSAGE_SELECT_TO_MOVE_UP = "message.selectToMoveUp";
    protected static final String KEY_MESSAGE_SELECT_TO_MOVE_DOWN = "message.selectToMoveDown";
    protected static final String KEY_MESSAGE_ALREADY_AT_TOP = "message.alreadyAtTop";
    protected static final String KEY_MESSAGE_ALREADY_AT_BOTTOM = "message.alreadyAtBottom";
    private final ResourceBundle resourceBundle = ResourceBundleFactory.getBundle(ColumnListPM.class);

    protected final OperationPM addColumn = new OperationPM();
    protected final OperationPM removeColumns = new OperationPM();
    protected final OperationPM moveUp = new OperationPM();
    protected final OperationPM moveDown = new OperationPM();

    private PathElementInfo rootPathElementInfo;

    public ColumnListPM() {
        super();
        PMManager.setup(this);
    }

    public void setColumnListContext(ColumnListContext clContext) {
        rootPathElementInfo = clContext.rootPathElementInfo;
        clear();
        if (clContext.initialColumns != null) {
            for (BnColumn col : clContext.initialColumns) {
                ColumnPM cell = new ColumnPM();
                ColumnContext colContext = new ColumnContext(rootPathElementInfo, col);
                cell.setColumnContext(colContext);
                this.add(cell);
            }
        }
        revalidateProperties();
    }

    public BnColumn[] getData() {
        BnColumn[] result = new BnColumn[size()];
        int i = 0;
        for (ColumnPM cell : this) {
            result[i] = cell.getBnColumn();
            i++;
        }
        return result;
    }

    @Operation
    public void addColumn() {
        PathChooserPM chooserMdl = new PathChooserPM();
        chooserMdl.setFunctions(new PathChooserPM.Functions() {
            @Override
			public void apply(Path path) {
                addColumun(path);
            }
        });
        chooserMdl.setPathContext(new PathContext(rootPathElementInfo, null, new Path()));
        chooserMdl.getContext().addParent(getContext());
        CustomizerUtil.get().openPathChooserDialog(chooserMdl);

    }

    @Validation(path = "addColumn", message = "Unknown element type")
    boolean canAddColumn() {
        return rootPathElementInfo != null;
    }

    private void addColumun(Path path) {
        ColumnPM newCell = new ColumnPM();
        String header = createHeader(path);
        ColumnContext colContext = new ColumnContext(rootPathElementInfo, new BnColumn(path, header));
        newCell.setColumnContext(colContext);
        this.add(newCell);
    }

    private String createHeader(Path path) {
        if (path == null) {
            return "new header";
        }
        String lastElement = path.getLastElement();
        if (lastElement == null) {
            return "new header";
        } else if (lastElement.length() > 1) {
            String result = Character.toUpperCase(lastElement.charAt(0)) + lastElement.substring(1);
            return result;
        } else {
            return lastElement;
        }
    }

    @Operation
    public void removeColumns() {
        removeAll(getSelection());

    }

    @Operation
    public void moveUp() {
        moveUp.check();
        int index = getSelection().getMinIndex();
        swap(index, index - 1);
    }

    @Validation(path = "moveUp")
    ValidationState validateMoveUp() {
        if (getSelection().size() != 1) {
            String message = resourceBundle.getString(KEY_MESSAGE_SELECT_TO_MOVE_UP);
            return new ValidationState(message);
        }
        int index = getSelection().getMinIndex();
        if (index == 0) {
            String message = resourceBundle.getString(KEY_MESSAGE_ALREADY_AT_TOP);
            return new ValidationState(message);
        }
        return null;
    }

    @Operation
    public void moveDown() {
        moveDown.check();
        int index = getSelection().getMinIndex();
        swap(index, index + 1);
    }

    @Validation(path = "moveDown")
    public ValidationState validateMoveDown() {
        if (getSelection().size() != 1) {
            String message = resourceBundle.getString(KEY_MESSAGE_SELECT_TO_MOVE_DOWN);
            return new ValidationState(message);
        }
        int index = getSelection().getMinIndex();
        if (index == size() - 1) {
            String message = resourceBundle.getString(KEY_MESSAGE_ALREADY_AT_BOTTOM);
            return new ValidationState(message);
        }
        return null;
    }

}