/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;
import java.util.LinkedList;

import org.beanfabrics.Path;
import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.util.ExceptionUtil;

/**
 * The <code>BnColumnPropertyEditor</code> is a JavaBeans {@link PropertyEditor}
 * for a {@link BnColumn}.
 *
 * @author Michael Karneim
 */
public class BnColumnPropertyEditor implements PropertyEditor {

    private static final String BNCOLUMN_CLASSNAME = org.beanfabrics.swing.table.BnColumn.class.getName();
    private static final String PATH_CLASSNAME = org.beanfabrics.Path.class.getName();

    /** The columns object array that has to be set or changed. */
    private BnColumn[] columns;

    /** A supporter to handle the changes of properties. */
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Set (or change) the object that is to be edited.
     *
     * @param value The object to be edited.
     */
    public void setValue(Object value) {
//  	final BnColumn[] old = this.getColumns();
        BnColumn[] newValue = (BnColumn[])value;

        //TODO (rk) eclipse workaround, to be checked again
        if (newValue != null && newValue.length == 0) {
            newValue = null;
        }

        this.columns = newValue;
        this.support.firePropertyChange(null, null, null);
    }

    /**
     * If the property value must be one of a set of known tagged values, then
     * this method should return an array of the tags.
     *
     * @return An array with the tagged values.
     */
    public String[] getTags() {
        return null;
    }

    /**
     * Gets the property value.
     *
     * @return the property value
     */
    public Object getValue() {
        return this.columns;
    }

    /**
     * A {@link PropertyEditor} may choose to make available a full custom
     * {@link Component} that edits its property value.
     *
     * @return the custom editor component
     */
    public Component getCustomEditor() {
        return null;
    }

    /**
     * Determines whether this property model is paintable.
     *
     * @return <code>true</code> is this property model is paintable, otherwise
     *         <code>false</code>
     */
    public boolean isPaintable() {
        return false;
    }

    /**
     * Paint a representation of the value into a given area of screen real
     * estate.
     *
     * @param gfx The graphics object to be painted.
     * @param box The area to be painted to.
     */
    public void paintValue(Graphics gfx, Rectangle box) {
    }

    /**
     * Determines whether this property model supports a custom editor.
     *
     * @retun <code>true</code> if this property model supports a custom editor,
     *        otherwise <code>false</code>
     */
    public boolean supportsCustomEditor() {
        return false;
    }

    /**
     * Register a listener for the {@link PropertyChangeEvent}.
     *
     * @param listener the listener to be added
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Remove a listener for the {@link PropertyChangeEvent}.
     *
     * @param listener the listener to be removed
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    /**
     * This method is intended for use when generating Java code to set the
     * value of the property.
     *
     * @return The generated Java code, like:
     * <pre>
     *     new BnColumn[] {
     *       new BnColumn(
     *         new Path(&quot;pathString&quot;),
     *         &quot;columnName&quot;
     *         100,
     *         false
     *         ),
     *       ...
     *     }
     * </pre>
     */
    public String getJavaInitializationString() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("new " + BNCOLUMN_CLASSNAME + "[] {");
            if (this.columns == null || this.columns.length == 0) {
                return null;
            }
            for (int i = 0; i < this.columns.length; i++) {
                sb.append("\n\t      ");
                if (i > 0) {
                    sb.append(" ,");
                }
                sb.append(" new " + BNCOLUMN_CLASSNAME + "(");
                sb.append(" new " + PATH_CLASSNAME + "(\"").append(this.columns[i].getPath().getPathString() + "\")");
                sb.append(", \"").append(this.columns[i].getColumnName() + "\"");
                sb.append(", ").append(this.columns[i].getWidth());
                sb.append(", ").append(this.columns[i].isWidthFixed());
                if (this.columns[i].getOperationPath() != null) {
                    sb.append(", new " + PATH_CLASSNAME + "(\"").append(this.columns[i].getOperationPath().getPathString() + "\")");
                }
                sb.append("    )");
            }
            sb.append("}");
            return sb.toString();
        } catch (Exception ex) {
            ExceptionUtil.getInstance().handleException("Can't call getJavaInitializationString ", ex);
            return null;
        }
    }

    /**
     * Gets the property value as text.
     *
     * @return The property value as text.
     */
    public String getAsText() {
        try {
            if (this.columns == null || this.columns.length == 0) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.columns.length; ++i) {
                sb.append("[");
                sb.append(Path.getPathString(this.columns[i].getPath()));
                sb.append(",").append(this.columns[i].getColumnName());
                sb.append(",").append(this.columns[i].getWidth());
                sb.append(",").append(this.columns[i].isWidthFixed());
                sb.append(",").append(this.columns[i].getOperationPath());
                sb.append("]");
            }
            return sb.toString();
        } catch (Exception ex) {
            ExceptionUtil.getInstance().handleException("Can't call getAsText ", ex);
            return "";
        }
    }

    /**
     * Set the property value by parsing a given String.
     *
     * @param text The new value for the property.
     */
    public void setAsText(String text) {
        try {
            final BnColumn[] old = this.columns;
            final BnColumn[] def;
            if (text == null || text.trim().length() == 0) {
                def = null;
            } else {
                final LinkedList<BnColumn> found = new LinkedList<BnColumn>();
                int idx;
                do {
                    // Found a colon
                    int braceStart = text.indexOf("[");
                    int braceEnd = text.indexOf("]");
                    final BnColumn definition = scanDef(text.substring(braceStart + 1, braceEnd));
                    found.add(definition);
                    text = text.substring(braceEnd + 1);
                    idx = text.indexOf(",");
                } while (idx > -1);
                def = found.toArray(new BnColumn[found.size()]);
            }
            setValue(def);

        } catch (Exception e) {
            ExceptionUtil.getInstance().handleException("Can't call setAsText with param '" + text + "'", e);
        }
    }

    private BnColumn scanDef(String text)
        throws IllegalArgumentException {
        if (text == null || text.trim().length() == 0) {
            return null;
        } else {
            final String[] tokens = tokenize(text);
            String pathString;
            String columnName;
            int width;
            boolean widthFixed;
            String operationPathString;
            switch (tokens.length) {
                case 2:
                    pathString = tokens[0];
                    columnName = tokens[1];
                    return new BnColumn(Path.parse(pathString), columnName);
                case 4:
                    pathString = tokens[0];
                    columnName = tokens[1];
                    width = Integer.parseInt(tokens[2]);
                    widthFixed = Boolean.parseBoolean(tokens[3]);
                    return new BnColumn(Path.parse(pathString), columnName, width, widthFixed);
                case 5:
                    pathString = tokens[0];
                    columnName = tokens[1];
                    width = Integer.parseInt(tokens[2]);
                    widthFixed = Boolean.parseBoolean(tokens[3]);
                    operationPathString = tokens[4];
                    return new BnColumn(Path.parse(pathString), columnName, width, widthFixed, Path.parse(operationPathString));
            }
            return null;
        }
    }

    private String[] tokenize(String text) {
        final LinkedList<String> tokenList = new LinkedList<String>();
        int idx = text.indexOf(",");
        while (idx != -1) {
            final String token = text.substring(0, idx);
            tokenList.add(token);
            text = text.substring(idx + 1);
            idx = text.indexOf(",");
        }
        tokenList.add(text);
        return tokenList.toArray(new String[tokenList.size()]);
    }

    private BnColumn[] getColumns() {
        return this.columns;
    }
}