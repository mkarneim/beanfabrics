/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

import org.beanfabrics.meta.PathElementInfo;
import org.beanfabrics.meta.TypeInfo;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.swing.customizer.path.PathContext;
import org.beanfabrics.swing.customizer.path.PathPM;
import org.beanfabrics.swing.table.BnColumn;

/**
 * The <code>ColumnPM</code> is a presentation model presenting a
 * {@link BnColumn}.
 * 
 * @author Michael Karneim
 */
public class ColumnPM extends AbstractPM {
    protected final PathPM path = new PathPM();
    protected final TextPM columnName = new TextPM();
    protected final IntegerPM width = new IntegerPM();
    protected final BooleanPM fixedWidth = new BooleanPM();
    protected final PathPM operationPath = new PathPM();
    protected final HorizontalAlignmentPM alignment = new HorizontalAlignmentPM();

    public PathElementInfo rootPathElementInfo; 

    public ColumnPM(PathElementInfo rootPathElementInfo) {
        this.rootPathElementInfo = rootPathElementInfo;
        PMManager.setup(this);
        path.setMandatory(true);
        columnName.setMandatory(true);
        width.setMandatory(true);
        fixedWidth.setMandatory(true);
        operationPath.setMandatory(false);
        alignment.setMandatory(false);
    }

    public void setData(BnColumn col) {
        this.path.setPathContext(new PathContext(rootPathElementInfo, null, col.getPath()));
        this.columnName.setText(col.getColumnName());
        this.width.setInteger(col.getWidth());
        this.fixedWidth.setBoolean(col.isWidthFixed());
        TypeInfo opModelTypeInfo = PMManager.getInstance().getMetadata().getTypeInfo(IOperationPM.class);
        this.operationPath.setPathContext(new PathContext(rootPathElementInfo, opModelTypeInfo, col.getOperationPath()));
        this.alignment.setText(this.alignment.getOptions().get(col.getAlignment()));
    }

    public BnColumn getData() {
        BnColumn result = new BnColumn(this.path.getPath(), this.columnName.getText(), this.width.getInteger(), this.fixedWidth.getBoolean(), this.operationPath.getPath(), (Integer)this.alignment.getOptions().getKey(this.alignment.getText()));
        return result;
    }

}