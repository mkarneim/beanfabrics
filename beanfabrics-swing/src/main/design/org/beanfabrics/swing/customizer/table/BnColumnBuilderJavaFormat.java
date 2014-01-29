/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

import javax.swing.SwingConstants;

import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.swing.table.BnColumnBuilder;

public class BnColumnBuilderJavaFormat implements ObjectToSourceCodeFormatter<BnColumn[]> {
    private static final String BUILDER_CLASSNAME = BnColumnBuilder.class.getName();

    private String builderClassname;
    private String columnDelimiter = "\n\t      ";

    public BnColumnBuilderJavaFormat() {
        this(BUILDER_CLASSNAME);
    }

    protected BnColumnBuilderJavaFormat(String builderClassname) {
        this.builderClassname = builderClassname;
    }

    public String getColumnDelimiter() {
        return columnDelimiter;
    }

    public void setColumnDelimiter(String columnDelimiter) {
        this.columnDelimiter = columnDelimiter;
    }

    @Override
    public String format(BnColumn[] columns) {
        StringBuilder sb = new StringBuilder();
        if (columns == null || columns.length == 0) {
            return null;
        }
        sb.append("new " + builderClassname + "()");
        for (int i = 0; i < columns.length; i++) {
            BnColumn c = columns[i];
            sb.append(columnDelimiter);
            if (c != null) {
                sb.append(".addColumn()");
                appendProperties(sb, c);
            }
        }
        sb.append(columnDelimiter);
        sb.append(".build()");
        return sb.toString();
    }

    protected void appendProperties(StringBuilder sb, BnColumn column) {
        if (column.getPath() != null) {
            sb.append(".withPath(\"" + column.getPath().getPathString() + "\")");
        }
        if (column.getColumnName() != null) {
            sb.append(".withName(\"" + column.getColumnName() + "\")");
        }
        if (column.getWidth() != BnColumn.DEFAULT_WIDTH) {
            sb.append(".withWidth(" + column.getWidth() + ")");
        }
        if (column.isWidthFixed() != BnColumn.DEFAULT_WIDTH_FIXED) {
            sb.append(".withWidthFixed(" + column.isWidthFixed() + ")");
        }
        if (column.getOperationPath() != null) {
            sb.append(".withOperationPath(\"" + column.getOperationPath().getPathString() + "\")");
        }
        if (!equals(column.getAlignment(), BnColumn.DEFAULT_ALIGNEMNT)) {
            sb.append(".withAlignment(" + getAlignmentConstantString(column.getAlignment()) + ")");
        }
    }

    private static boolean equals(Integer a, Integer b) {
        if (a == null) {
            return b == null;
        } else {
            return a.equals(b);
        }
    }

    private String getAlignmentConstantString(Integer alignment) {
        if (alignment == null) {
            return "null";
        }
        String code;
        switch (alignment.intValue()) {
        case SwingConstants.LEADING:
            code = builderClassname + ".ALIGNMENT_LEADING";
            break;
        case SwingConstants.LEFT:
            code = builderClassname + ".ALIGNMENT_LEFT";
            break;
        case SwingConstants.TRAILING:
            code = builderClassname + ".ALIGNMENT_TRAILING";
            break;
        case SwingConstants.RIGHT:
            code = builderClassname + ".ALIGNMENT_RIGHT";
            break;
        case SwingConstants.CENTER:
            code = builderClassname + ".ALIGNMENT_CENTER";
            break;
        default:
            code = "null";
        }
        return code;
    }

}
