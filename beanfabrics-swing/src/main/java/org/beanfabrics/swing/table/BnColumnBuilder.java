/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;

import org.beanfabrics.Path;

/**
 * DSL-sytle Builder for BnColumn objects.
 * <p>
 * Use it like this:
 * <p>
 * <code>
 * BnColumn[] columns = new BnColumnBuilder()
 * 		.addColumn().withPath("this.name").withName("Name")
 * 		.addColumn().withPath("this.phone").withName("Phone").withWidth(100).withWidthFixed(true).build();
 * </code>
 * 
 * @author Michael Karneim
 */
public class BnColumnBuilder {
    
    public static final int ALIGNMENT_LEFT = SwingConstants.LEFT;
    public static final int ALIGNMENT_RIGHT = SwingConstants.RIGHT;
    public static final int ALIGNMENT_TRAILING = SwingConstants.TRAILING;
    public static final int ALIGNMENT_LEADING = SwingConstants.LEADING;
    public static final int ALIGNMENT_CENTER = SwingConstants.CENTER;
    
    public static void main(String[] args) {
        new BnColumnBuilder().addColumn().withPath("this.name").withName("Name").addColumn().withPath("this.phone").withName("Phone").withWidth(100).withWidthFixed(true).build();
    }

    private static class Column {
        private String columnName;
        private Path path;
        private int width = BnColumn.DEFAULT_WIDTH;
        private boolean fixed = BnColumn.DEFAULT_WIDTH_FIXED;
        private Path operationPath;
        private Integer alignment = BnColumn.DEFAULT_ALIGNEMNT;

        BnColumn toBnColumn() {
            return new BnColumn(this.path, this.columnName, this.width, this.fixed, this.operationPath, this.alignment);
        }
    }

    private List<Column> columns = new ArrayList<Column>();

    private Column currentColumn;

    public BnColumnBuilder() {
        //
    }

    public BnColumnBuilder addColumn() {
        this.currentColumn = new Column();
        this.columns.add(this.currentColumn);
        return this;
    }

    public BnColumnBuilder withPath(String pathStr) {
        checkCurrentColumn();
        this.currentColumn.path = Path.parse(pathStr);
        return this;
    }

    public BnColumnBuilder withName(String name) {
        checkCurrentColumn();
        this.currentColumn.columnName = name;
        return this;
    }

    public BnColumnBuilder withWidth(int width) {
        checkCurrentColumn();
        this.currentColumn.width = width;
        return this;
    }

    public BnColumnBuilder withWidthFixed(boolean fixed) {
        checkCurrentColumn();
        this.currentColumn.fixed = fixed;
        return this;
    }

    public BnColumnBuilder withOperationPath(String pathStr) {
        checkCurrentColumn();
        this.currentColumn.operationPath = Path.parse(pathStr);
        return this;
    }

    public BnColumnBuilder withAlignment(Integer alignment) {
        checkCurrentColumn();
        this.currentColumn.alignment = alignment;
        return this;
    }

    public BnColumn[] build() {
        BnColumn[] result = new BnColumn[columns.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = columns.get(i).toBnColumn();
        }
        return result;
    }

    private void checkCurrentColumn() {
        if (this.currentColumn == null) {
            throw new IllegalStateException("No current column. Call 'addColumn' first.");
        }
    }
}
