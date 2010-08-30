/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.goodies.calendar;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

/**
 * @author Michael Karneim
 */
public class MonthPanelUISettings {
    private MonthPanelUISettings delegate = null;

    private Color weekendForegroundColor = null;
    private Color workdayForegroundColor = null;
    private Color todayForegroundColor = null;
    private Color selectedColor = null;
    private Color selectedBackgroundColor = null;
    private Color backgroundColor = null;
    private Font headerFont = null;
    private Color headerForegroundColor = null;
    private Font dayFont = null;
    private Font dateFont = null;
    private Insets dayMargin = null;

    public MonthPanelUISettings() {
    }

    public MonthPanelUISettings getDelegate() {
        return delegate;
    }

    public void setDelegate(MonthPanelUISettings delegate) {
        this.delegate = delegate;
    }

    public Color getWeekendForegroundColor() {
        if (weekendForegroundColor == null && delegate != null) {
            return delegate.weekendForegroundColor;
        }
        return weekendForegroundColor;
    }

    public void setWeekendForegroundColor(Color weekendForegroundColor) {
        this.weekendForegroundColor = weekendForegroundColor;
    }

    public Color getWorkdayForegroundColor() {
        if (workdayForegroundColor == null && delegate != null) {
            return delegate.workdayForegroundColor;
        }
        return workdayForegroundColor;
    }

    public void setWorkdayForegroundColor(Color workdayForegroundColor) {
        this.workdayForegroundColor = workdayForegroundColor;
    }

    public Color getTodayForegroundColor() {
        if (todayForegroundColor == null && delegate != null) {
            return delegate.todayForegroundColor;
        }
        return todayForegroundColor;
    }

    public void setTodayForegroundColor(Color todayForegroundColor) {
        this.todayForegroundColor = todayForegroundColor;
    }

    public Color getSelectedColor() {
        if (selectedColor == null && delegate != null) {
            return delegate.selectedColor;
        }
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public Color getSelectedBackgroundColor() {
        if (selectedBackgroundColor == null && delegate != null) {
            return delegate.selectedBackgroundColor;
        }
        return selectedBackgroundColor;
    }

    public void setSelectedBackgroundColor(Color selectedBackgroundColor) {
        this.selectedBackgroundColor = selectedBackgroundColor;
    }

    public Color getBackgroundColor() {
        if (backgroundColor == null && delegate != null) {
            return delegate.backgroundColor;
        }
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Font getHeaderFont() {
        if (headerFont == null && delegate != null) {
            return delegate.headerFont;
        }
        return headerFont;
    }

    public void setHeaderFont(Font headerFont) {
        this.headerFont = headerFont;
    }

    public Color getHeaderForegroundColor() {
        if (headerForegroundColor == null && delegate != null) {
            return delegate.headerForegroundColor;
        }
        return headerForegroundColor;
    }

    public void setHeaderForegroundColor(Color headerForegroundColor) {
        this.headerForegroundColor = headerForegroundColor;
    }

    public Font getDayFont() {
        if (dayFont == null && delegate != null) {
            return delegate.dayFont;
        }
        return dayFont;
    }

    public void setDayFont(Font dayFont) {
        this.dayFont = dayFont;
    }

    public Font getDateFont() {
        if (dateFont == null && delegate != null) {
            return delegate.dateFont;
        }
        return dateFont;
    }

    public void setDateFont(Font dateFont) {
        this.dateFont = dateFont;
    }

    public Insets getDayMargin() {
        if (dayMargin == null && delegate != null) {
            return delegate.dayMargin;
        }
        return dayMargin;
    }

    public void setDayMargin(Insets dayMargin) {
        this.dayMargin = dayMargin;
    }
}
