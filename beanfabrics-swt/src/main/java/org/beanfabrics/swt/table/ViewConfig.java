/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swt.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.beanfabrics.Path;

/**
 * @author Michael Karneim
 */
public class ViewConfig {
	private List<Column> columns;

	public ViewConfig(Column[] columns) {
		this.columns = Collections.unmodifiableList(Arrays.asList(columns));
	}

	public ViewConfig(List<Column> columns) {
		this.columns = Collections.unmodifiableList(new ArrayList<Column>(columns));
	}

	public List<Column> getColumns() {
		return this.columns;
	}

	public static class Column {
		public final Path path;
		public final String header;
		public final int width;

		public Column(Path path, String header, int with) {
			super();
			this.path = path;
			this.header = header;
			this.width = with;
		}
	}
}
