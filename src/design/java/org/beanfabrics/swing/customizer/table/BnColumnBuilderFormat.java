/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

import javax.swing.SwingConstants;

import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.swing.table.BnColumnBuilder;

public class BnColumnBuilderFormat {
    private static final String BUILDER_CLASSNAME = BnColumnBuilder.class.getName();
    private static final String BUILDER_BASENAME = getBasename(BnColumnBuilder.class.getName());
    private String columnDelimiter = "\n\t      ";

    public BnColumnBuilderFormat() {
        super();
    }

    public String getColumnDelimiter() {
        return columnDelimiter;
    }

    public void setColumnDelimiter(String columnDelimiter) {
        this.columnDelimiter = columnDelimiter;
    }

    public String format(BnColumn[] columns) {
        StringBuilder sb = new StringBuilder();
        if (columns == null || columns.length == 0) {
            return null;
        }
        sb.append("new " + BUILDER_CLASSNAME + "()");
        for (int i = 0; i < columns.length; i++) {
            BnColumn c = columns[i];
            sb.append(columnDelimiter);
            sb.append(".addColumn()");
            if (c.getPath() != null) {
                sb.append(".withPath(\"" + c.getPath().getPathString() + "\")");
            }
            if (c.getColumnName() != null) {
                sb.append(".withName(\"" + c.getColumnName() + "\")");
            }
            if (c.getWidth() != BnColumn.DEFAULT_WIDTH) {
                sb.append(".withWidth(" + c.getWidth() + ")");
            }
            if (c.isWidthFixed() != BnColumn.DEFAULT_WIDTH_FIXED) {
                sb.append(".withWidthFixed(" + c.isWidthFixed() + ")");
            }
            if (c.getOperationPath() != null) {
                sb.append(".withOperationPath(\"" + c.getOperationPath().getPathString() + "\")");
            }
            if (!equals(c.getAlignment(), BnColumn.DEFAULT_ALIGNEMNT)) {
                sb.append(".withAlignment(" + getAlignmentConstantString(c.getAlignment()) + ")");
            }
        }
        sb.append(columnDelimiter);
        sb.append(".build()");
        return sb.toString();
    }

    private static boolean equals(Integer a, Integer b) {
        if (a == null) {
            return b == null;
        } else {
            return a.equals(b);
        }
    }

    private static String getAlignmentConstantString(Integer alignment) {
        if (alignment == null) {
            return "null";
        }
        String code;
        switch (alignment.intValue()) {
            case SwingConstants.LEADING:
                code = BUILDER_CLASSNAME + ".ALIGNMENT_LEADING";
                break;
            case SwingConstants.LEFT:
                code = BUILDER_CLASSNAME + ".ALIGNMENT_LEFT";
                break;
            case SwingConstants.TRAILING:
                code = BUILDER_CLASSNAME + ".ALIGNMENT_TRAILING";
                break;
            case SwingConstants.RIGHT:
                code = BUILDER_CLASSNAME + ".ALIGNMENT_RIGHT";
                break;
            case SwingConstants.CENTER:
                code = BUILDER_CLASSNAME + ".ALIGNMENT_CENTER";
                break;
            default:
                code = "null";
        }
        return code;
    }

    private static String getBasename(String classname) {
        int idx = classname.lastIndexOf('.');
        if (idx == -1) {
            return classname;
        } else {
            return classname.substring(idx + 1);
        }
    }
}
