/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

import org.beanfabrics.Path;
import org.beanfabrics.meta.PathInfo;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Validation;
import org.beanfabrics.swing.customizer.path.PathChooserPM;
import org.beanfabrics.swing.customizer.path.PathContext;
import org.beanfabrics.swing.customizer.util.CustomizerUtil;
import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.validation.ValidationState;

/**
 * The <code>ColumnListPM</code> is a {@link ListPM} for {@link ColumnPM}
 * elements.
 * 
 * @author Michael Karneim
 */
public class ColumnListPM extends ListPM<ColumnPM> {
    protected final OperationPM addColumn = new OperationPM();
    protected final OperationPM removeColumns = new OperationPM();
    protected final OperationPM moveUp = new OperationPM();
    protected final OperationPM moveDown = new OperationPM();

    private PathInfo elementRoot;

    public ColumnListPM() {
        super();
        PMManager.setup(this);
    }

    public void setColumnListContext(ColumnListContext clContext) {
        this.elementRoot = clContext.elementRoot;
        this.clear();
        if (clContext.initialColumns != null) {
            for (BnColumn col : clContext.initialColumns) {
                ColumnPM cell = new ColumnPM();
                ColumnContext colContext = new ColumnContext(this.elementRoot, col);
                cell.setColumnContext(colContext);
                this.add(cell);
            }
        }
        revalidateProperties();
    }

    public BnColumn[] getData() {
        BnColumn[] result = new BnColumn[this.size()];
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
            public void apply(Path path) {
                addColumun(path);
            }
        });
        chooserMdl.setPathContext(new PathContext(this.elementRoot, null, new Path()));
        chooserMdl.getContext().addParent(this.getContext());
        CustomizerUtil.get().openPathChooserDialog(chooserMdl);

    }

    @Validation(path = "addColumn", message = "Unknown element type")
    boolean canAddColumn() {
        return this.elementRoot != null;
    }

    private void addColumun(Path path) {
        ColumnPM newCell = new ColumnPM();
        String header = createHeader(path);
        ColumnContext colContext = new ColumnContext(this.elementRoot, new BnColumn(path, header));
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
        this.removeAll(this.getSelection());

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
            return new ValidationState("Select single entry to move up");
        }
        int index = getSelection().getMinIndex();
        if (index == 0) {
            return new ValidationState("Already at top");
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
            return new ValidationState("Select single entry to move down");
        }
        int index = getSelection().getMinIndex();
        if (index == size() - 1) {
            return new ValidationState("Already at bottom");
        }
        return null;
    }

}