/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swt.table;

import java.util.ArrayList;
import java.util.List;

import org.beanfabrics.Path;

/**
 * @author Michael Karneim
 */
public class ViewConfigBuilder {
    private List<Column> columns;

    public ViewConfigBuilder() {
        columns = new ArrayList<Column>();
    }

    public ViewConfigBuilder addColumn(Path path, String header, int width) {
        Column newCol = new Column();
        newCol.path = path;
        newCol.header = header;
        newCol.width = width;
        columns.add(newCol);
        return this;
    }

    public ViewConfigBuilder addColumn(Path path) {
        Column newCol = new Column();
        newCol.path = path;
        columns.add(newCol);
        return this;
    }

    public ViewConfigBuilder addColumn() {
        Column newCol = new Column();
        columns.add(newCol);
        return this;
    }

    public ViewConfigBuilder setPath(Path path) {
        Column col = columns.get(columns.size() - 1);
        col.path = path;
        return this;
    }

    public ViewConfigBuilder setPath(String pathStr) {
        Column col = columns.get(columns.size() - 1);
        col.path = new Path(pathStr);
        return this;
    }

    public ViewConfigBuilder setHeader(String header) {
        Column col = columns.get(columns.size() - 1);
        col.header = header;
        return this;
    }

    public ViewConfigBuilder setWidth(int width) {
        Column col = columns.get(columns.size() - 1);
        col.width = width;
        return this;
    }

    public ViewConfig buildViewConfig() {
        List<ViewConfig.Column> cols = buildViewConfigColumns();
        ViewConfig result = new ViewConfig(cols);
        return result;
    }

    private List<ViewConfig.Column> buildViewConfigColumns() {
        List<ViewConfig.Column> result = new ArrayList<ViewConfig.Column>(columns.size());
        for (Column col : columns) {
            ViewConfig.Column resEntry = new ViewConfig.Column(col.path, col.header, col.width);
            result.add(resEntry);
        }
        return result;
    }

    private class Column {
        Path path;
        String header;
        int width;
    }
}
