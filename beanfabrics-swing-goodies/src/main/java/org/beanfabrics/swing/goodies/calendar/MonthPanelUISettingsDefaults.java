/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.goodies.calendar;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * @author Michael Karneim
 */
public class MonthPanelUISettingsDefaults extends MonthPanelUISettings {
    private Font baseFont;
    private Color selectionForeground;
    private Color selectionBackground;
    private Color dayBackground;

    public MonthPanelUISettingsDefaults() {
        UIDefaults def = UIManager.getLookAndFeelDefaults();
        if (isNimbus()) {
            baseFont = def.getFont("Label.font");
            selectionForeground = copy(def.getColor("nimbusSelectedText"));
            selectionBackground = copy(def.getColor("nimbusSelectionBackground"));
            dayBackground = copy(def.getColor("nimbusLightBackground"));
        } else {
            baseFont = def.getFont("Label.font");
            selectionForeground = copy(def.getColor("List.selectionForeground"));
            selectionBackground = copy(def.getColor("List.selectionBackground"));
            dayBackground = copy(def.getColor("List.background"));
        }

        if (isMotif()) {
            setWeekendForegroundColor(new Color(119, 66, 8));
            setTodayForegroundColor(new Color(204, 51, 51));
        } else {
            setWeekendForegroundColor(new Color(238, 129, 0));
            setTodayForegroundColor(Color.RED);
        }

        setWorkdayForegroundColor(new Color(21, 55, 93));

        setSelectedColor(selectionForeground);
        setSelectedBackgroundColor(selectionBackground);
        setBackgroundColor(dayBackground);
        setHeaderFont(baseFont.deriveFont(Font.PLAIN));
        setHeaderForegroundColor(Color.BLACK);
        setDayFont(baseFont.deriveFont(Font.PLAIN).deriveFont(baseFont.getSize2D() * 0.8f));
        setDateFont(baseFont);
        setDayMargin(new Insets(1, 1, 1, 1));
    }

    private static Color copy(Color color) {
        return new Color(color.getRGB());
    }

    private boolean isNimbus() {
        return "Nimbus".equals(UIManager.getLookAndFeel().getID());
    }

    private boolean isMotif() {
        return "Motif".equals(UIManager.getLookAndFeel().getID());
    }
}
