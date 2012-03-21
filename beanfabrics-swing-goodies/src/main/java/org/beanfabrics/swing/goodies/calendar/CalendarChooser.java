/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.goodies.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * CalendarChooser is a date chooser control for a single date.
 * <P>
 * <B>example:</B><BR>
 * 
 * <pre>
 * Locale locale = Locale.US;
 * final CalendarChooser cal = new CalendarChooser(new Date(), locale);
 * JFrame frame = new JFrame();
 * frame.getContentPane().setLayout(new BorderLayout());
 * frame.getContentPane().add(&quot;Center&quot;, cal);
 * cal.addActionListener(new ActionListener() {
 *     public void actionPerformed(ActionEvent evt) {
 *         System.out.println(cal.getSelectedDate());
 *     }
 * });
 * frame.pack();
 * frame.setVisible(true);
 * </pre>
 * 
 * @author Michael Karneim
 * @author Max Gensthaler
 * @version 4.0
 */
public class CalendarChooser extends JPanel {
    private final static ArrowIcon LEFT_ICON = new ArrowIcon(ArrowIcon.LEFT);
    private final static ArrowIcon RIGHT_ICON = new ArrowIcon(ArrowIcon.RIGHT);
    public static final String SELECTEDDATE_PROPERTYNAME = MonthPanel.SELECTEDDATE_PROPERTYNAME;

    // Configuration
    private MonthPanel.Configuration config = new MonthPanel.Configuration();
    private Dimension rollButtonSize = new Dimension(10, 10);
    private int numberOfPreviousVisibleMonths = 0;
    private int numberOfSubsequentVisibleMonths = 0;
    private Locale locale;
    private Date selectedDate = null;

    // Implementation
    private GridLayout gridLayout = new GridLayout();
    private final ButtonGroup group = new ButtonGroup();
    private MonthPanel centerMonthPanel;
    private MonthPanel[] preMonthPanel = new MonthPanel[] {};
    private MonthPanel[] postMonthPanel = new MonthPanel[] {};

    private JButton oneMonthBack = new JButton(LEFT_ICON);
    private JButton oneMonthForward = new JButton(RIGHT_ICON);

    private transient ActionListener rollBackListener = null;
    private transient ActionListener rollForwardListener = null;
    private transient final ActionListener monthActionListener;
    private transient ArrayList<ActionListener> actionListeners;

    /**
     * Creates a new instance of CalendarBean displaying the current month using
     * the default locale.
     */
    public CalendarChooser() {
        this(new Date(), Locale.getDefault());
    }

    /**
     * Creates a new instance of CalendarBean displaying the given month and
     * using the given locale.
     * 
     * @param date the date that initially should be selected
     * @param locale the locale that should be used for the calendar
     */
    public CalendarChooser(Date date, Locale locale) {
        this.locale = locale;
        this.centerMonthPanel = new MonthPanel(date, locale, this.group);
        this.centerMonthPanel.setConfiguration(this.config);
        this.monthActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MonthPanel monthPanel = (MonthPanel)e.getSource();
                Date selDate = monthPanel.getSelectedDate();
                CalendarChooser.this.setSelectedDate(selDate, false);
                CalendarChooser.this.fireActionPerformed(new ActionEvent(CalendarChooser.this, e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers()));
            }
        };
        this.centerMonthPanel.addActionListener(this.monthActionListener);

        this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        this.gridLayout.setHgap(2);
        this.gridLayout.setVgap(2);
        this.setLayout(new BorderLayout(1, 1));

        this.initGui();
        this.setDates(date, date);
    }

    /**
     * Builds or Rebuilds the graphical user interface of the calendar. This
     * method is called by the constructor or whenever properties like font
     * colors or font sizes are changed.
     */
    protected void initGui() {
        // Remove all components from this panel in order to begin creating the
        // gui from scratch.
        this.removeAll();

        // Prepare the action listeners for the
        // roll forward and backward buttons.
        if (this.rollBackListener == null) {
            this.rollBackListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    CalendarChooser.this.rollOneMonthBack();
                }
            };
            oneMonthBack.addActionListener(this.rollBackListener);
        }
        if (this.rollForwardListener == null) {
            this.rollForwardListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    CalendarChooser.this.rollOneMonthForward();
                }
            };
            oneMonthForward.addActionListener(this.rollForwardListener);
        }

        // Create the calandar panel which contains all the toggle buttons
        // for the days of the currently dispayed month, and the header with
        // the names of the days.
        JPanel calendarPanel = new JPanel(gridLayout);
        calendarPanel.setOpaque(false);
        this.add(calendarPanel, BorderLayout.CENTER);
        gridLayout.setRows(1);
        gridLayout.setColumns(this.numberOfPreviousVisibleMonths + 1 + this.numberOfSubsequentVisibleMonths);

        for (int i = 0; i < this.preMonthPanel.length; i++) {
            this.preMonthPanel[i].initGui();
            calendarPanel.add(this.preMonthPanel[i]);
        }

        this.centerMonthPanel.initGui();
        calendarPanel.add(this.centerMonthPanel);

        for (int i = 0; i < this.postMonthPanel.length; i++) {
            this.postMonthPanel[i].initGui();
            calendarPanel.add(this.postMonthPanel[i]);
        }

        this.addRollButtons();

        // force revalidation of the layout manager
        this.validate();
    }

    private void addRollButtons() {
        EmptyBorder emptyBorder = new EmptyBorder(0, 0, 0, 0);
        LEFT_ICON.setPreferredSize(this.rollButtonSize);
        LEFT_ICON.setColor(config.getHeaderForegroundColor());
        RIGHT_ICON.setPreferredSize(this.rollButtonSize);
        RIGHT_ICON.setColor(config.getHeaderForegroundColor());
        oneMonthBack.setMargin(new Insets(1, 1, 1, 1));
        oneMonthBack.setBorder(emptyBorder);
        oneMonthBack.setBorderPainted(false);
        oneMonthBack.setContentAreaFilled(false);
        oneMonthBack.setOpaque(false);
        oneMonthForward.setMargin(new Insets(1, 1, 1, 1));
        oneMonthForward.setBorder(emptyBorder);
        oneMonthForward.setBorderPainted(false);
        oneMonthForward.setContentAreaFilled(false);
        oneMonthForward.setOpaque(false);

        if (this.preMonthPanel.length > 0) {
            this.preMonthPanel[0].setLeftUpperCornerComponent(this.oneMonthBack);
        } else {
            this.centerMonthPanel.setLeftUpperCornerComponent(this.oneMonthBack);
        }
        if (this.postMonthPanel.length > 0) {
            this.postMonthPanel[this.postMonthPanel.length - 1].setRightUpperCornerComponent(this.oneMonthForward);
        } else {
            this.centerMonthPanel.setRightUpperCornerComponent(this.oneMonthForward);
        }
    }

    /**
     * Rolls the calendar one month forward and displays the new month.
     */
    public void rollOneMonthForward() {
        this.centerMonthPanel.rollOneMonthForward();
        this.refresh();
    }

    /**
     * Rolls the calendar one month backwards and displays the new month.
     */
    public void rollOneMonthBack() {
        this.centerMonthPanel.rollOneMonthBack();
        this.refresh();
    }

    /**
     * Sets the displayed month and the selected date.
     * 
     * @param month the month to show
     * @param selectedDate the date to select
     */
    public void setDates(Date month, Date selectedDate) {
        this.setMonth(month);
        this.setSelectedDate(selectedDate, false);
    }

    /**
     * Selects the given date and displays the month containing that date.
     * 
     * @param date the date to select
     */
    public void setSelectedDate(Date date) {
        this.setSelectedDate(date, true);
    }

    /**
     * Selects the given date and displays the month containing that date.
     * 
     * @param date the date to select
     * @param rollToMonth boolean if true the panel scrolls to the respective
     *            month
     */
    public void setSelectedDate(Date date, boolean rollToMonth) {
        Date oldSelectedDate = this.selectedDate == null ? null : (Date)this.selectedDate.clone();
        this.centerMonthPanel.setSelectedDate(date, rollToMonth);
        this.selectedDate = date;
        this.refresh();
        this.firePropertyChange(SELECTEDDATE_PROPERTYNAME, oldSelectedDate, selectedDate);
    }

    /**
     * Refreshes the pre- and post-MonthPanels according to the month of the
     * centerMonthPanel.
     */
    private void refresh() {
        Date month = this.centerMonthPanel.getMonth();
        for (int i = 0; i < this.preMonthPanel.length; i++) {
            int months = this.preMonthPanel.length - i;
            Date showMonth = CalendarChooser.addMonths(month, -months);
            this.preMonthPanel[i].setMonth(showMonth);
            this.preMonthPanel[i].setSelectedDate(selectedDate, false);
        }
        for (int i = 0; i < this.postMonthPanel.length; i++) {
            int months = i + 1;
            Date showMonth = CalendarChooser.addMonths(month, months);
            this.postMonthPanel[i].setMonth(showMonth);
            this.postMonthPanel[i].setSelectedDate(selectedDate, false);
        }
    }

    /**
     * Returns a date that is the result of the summation of the given date and
     * the given number of months.
     * 
     * @param date Date
     * @param months int
     * @return Date
     */
    private static Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    /**
     * Returns the selected date as an Date instance.
     * 
     * @return the selected date
     */
    public Date getSelectedDate() {
        return this.selectedDate;
    }

    /**
     * Returns the visible month.
     * 
     * @return Date
     */
    public Date getMonth() {
        return this.centerMonthPanel.getMonth();
    }

    /**
     * Sets the given month to display on the calndar panel.
     * 
     * @param month Date
     */
    public void setMonth(Date month) {
        this.centerMonthPanel.setMonth(month);
        this.refresh();
    }

    /**
     * Removes the given action listener from this component.
     */
    public synchronized void removeActionListener(ActionListener l) {
        if (actionListeners != null && actionListeners.contains(l)) {
            ArrayList<ActionListener> als = new ArrayList<ActionListener>(actionListeners);
            als.remove(l);
            actionListeners = als;
        }
    }

    /**
     * Adds the given action listnener to this component. All action listeners
     * will be informed about changes that are made to the selected date of this
     * calendar.
     * 
     * @param l the listener to add to this component
     */
    public synchronized void addActionListener(ActionListener l) {
        ArrayList<ActionListener> als = actionListeners == null ? new ArrayList<ActionListener>(2) : new ArrayList<ActionListener>(actionListeners);
        if (!als.contains(l)) {
            als.add(l);
            actionListeners = als;
        }
    }

    /**
     * Fires the given action event to all listeners that are added to this
     * component.
     * 
     * @param e the event to fire
     */
    protected void fireActionPerformed(ActionEvent e) {
        if (actionListeners != null) {
            for (ActionListener l : actionListeners) {
                l.actionPerformed(e);
            }
        }
    }

    /**
     * Changes the color of the weekend day font.
     * 
     * @param weekendColor the new color for the weekend day font
     */
    public void setWeekendColor(Color weekendColor) {
        this.config.setWeekendForegroundColor(weekendColor);
        this.rebuildGui();
    }

    /**
     * Returns the currently used font color for weekend days.
     * 
     * @return the font color for the weekend days
     */
    public Color getWeekendColor() {
        return this.config.getWeekendForegroundColor();
    }

    /**
     * Changes the color of the workday font.
     * 
     * @param workdayColor the new color for the workday font
     */
    public void setWorkdayColor(Color workdayColor) {
        this.config.setWorkdayForegroundColor(workdayColor);
        this.rebuildGui();
    }

    /**
     * Returns the currently used font color for workday days.
     * 
     * @return the font color for the workday days
     */
    public Color getWorkdayColor() {
        return this.config.getWorkdayForegroundColor();
    }

    /**
     * Changes the color of the selected day font.
     * 
     * @param selectedColor the new color for the selected day font
     */
    public void setSelectedColor(Color selectedColor) {
        this.config.setSelectedColor(selectedColor);
        this.rebuildGui();
    }

    /**
     * Returns the currently used font color for selected day.
     * 
     * @return the font color for the selected day
     */
    public Color getSelectedColor() {
        return this.config.getSelectedColor();
    }

    /**
     * Changes the color of the selected day background.
     * 
     * @param selectedBackgroundColor the new color for the selected day
     *            background
     */
    public void setSelectedBackgroundColor(Color selectedBackgroundColor) {
        this.config.setSelectedBackgroundColor(selectedBackgroundColor);
        this.rebuildGui();
    }

    /**
     * Returns the currently used day background color for selected day.
     * 
     * @return the day background color for the selected day
     */
    public Color getSelectedBackgroundColor() {
        return this.config.getSelectedBackgroundColor();
    }

    /**
     * Changes the color of the background of the days.
     * 
     * @param dayBackground the new color for the background ofvthe days
     */
    public void setDayBackground(Color dayBackground) {
        this.config.setBackgroundColor(dayBackground);
        this.rebuildGui();
    }

    /**
     * Returns the currently used color of the background of the days.
     * 
     * @return the currently used color of the background of the days
     */
    public Color getDayBackground() {
        return this.config.getBackgroundColor();
    }

    /**
     * Changes the font of the header of the days.
     * 
     * @param headerFont the font of the header of the days
     */
    public void setHeaderFont(Font headerFont) {
        this.config.setHeaderFont(headerFont);
        this.rebuildGui();
    }

    /**
     * Returns the currently used font of the header of the days.
     * 
     * @return the currently used font of the header of the days
     */
    public Font getHeaderFont() {
        return this.config.getHeaderFont();
    }

    /**
     * Changes the currently used foreground color of the header.
     * 
     * @param c the foreground color of the header
     */
    public void setHeaderForegroundColor(Color c) {
        this.config.setHeaderForegroundColor(c);
        this.rebuildGui();
    }

    /**
     * Returns the currently used foreground color of the header.
     * 
     * @return the currently used foreground color of the header
     */
    public Color getHeaderForegroundColor() {
        return this.config.getHeaderForegroundColor();
    }

    /**
     * Changes the font of the days displayed on the calendar panel.
     * 
     * @param dayFont the font of the days displayed on the calendar panel
     */
    public void setDayFont(Font dayFont) {
        this.config.setDayFont(dayFont);
        this.rebuildGui();
    }

    /**
     * Returns the currently used font of the days displayed on the calendar
     * panel.
     * 
     * @return the currently used font of the days displayed on the calendar
     *         panel
     */
    public Font getDayFont() {
        return this.config.getDayFont();
    }

    /**
     * Changes the font of the title of the calendar panel, which displays the
     * currently displayed month's name and the year.
     * 
     * @param dateFont the new font for the title
     */
    public void setDateFont(Font dateFont) {
        this.config.setDateFont(dateFont);
        this.rebuildGui();
    }

    /**
     * Returns the currently used font of the title of the calendar panel, which
     * displays the currently displayed month's name and the year.
     * 
     * @return the currently used font for the title
     */
    public Font getDateFont() {
        return this.config.getDateFont();
    }

    /**
     * Changes the margin of each days button.
     * 
     * @param dayMargin the margin of each days button
     */
    public void setDayMargin(Insets dayMargin) {
        this.config.setDayMargin(dayMargin);
        this.rebuildGui();
    }

    /**
     * Returns the currently used margin of each days button.
     * 
     * @return the currently used margin of each days button
     */
    public Insets getDayMargin() {
        return this.config.getDayMargin();
    }

    /**
     * Changes the margin of each days button.
     * 
     * @param dayMargin the margin of each days button
     */
    public void setRollButtonSize(Dimension newSize) {
        this.rollButtonSize = newSize;
        this.rebuildGui();
    }

    /**
     * Returns the currently used margin of each days button.
     * 
     * @return the currently used margin of each days button
     */
    public Dimension getRollButtonSize() {
        return this.rollButtonSize;
    }

    /**
     * Sets the number of months that are visible ahead the current month.
     * 
     * @param number number of previous visible months
     */
    public void setNumberOfPreviousVisibleMonths(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("number must be >= 0");
        }
        this.numberOfPreviousVisibleMonths = number;
        MonthPanel[] panels = createMonthPanels(this.numberOfPreviousVisibleMonths);
        this.setPreMonthPanels(panels);
    }

    /**
     * Returns the number of months that are visible ahead the current month.
     * 
     * @return number of previous visible months
     */
    public int getNumberOfPreviousVisibleMonths() {
        return this.numberOfPreviousVisibleMonths;
    }

    /**
     * Sets the number of months that are visible behind the current month.
     * 
     * @param number number of subsequent visible months
     */
    public void setNumberOfSubsequentVisibleMonths(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("number must be >= 0");
        }
        this.numberOfSubsequentVisibleMonths = number;
        MonthPanel[] panels = createMonthPanels(this.numberOfSubsequentVisibleMonths);
        this.setPostMonthPanels(panels);
    }

    /**
     * Returns the number of months that are visible begind the current month.
     * 
     * @return number of subsequent visible months
     */
    public int getNumberOfSubsequentVisibleMonths() {
        return this.numberOfSubsequentVisibleMonths;
    }

    private MonthPanel[] createMonthPanels(int number) {
        MonthPanel[] panels = new MonthPanel[number];
        for (int i = 0; i < panels.length; i++) {
            panels[i] = new MonthPanel(this.centerMonthPanel.getMonth(), this.locale);
        }
        return panels;
    }

    private void onAddMonthPanel(MonthPanel panel) {
        panel.addActionListener(this.monthActionListener);
        // panel.addPropertyChangeListener(SELECTEDDATE_PROPERTYNAME,
        // this.monthPropertyChangeListener);
        panel.setButtonGroup(this.group);
        panel.setConfiguration(this.config);
    }

    private void onRemoveMonthPanel(MonthPanel panel) {
        panel.removeActionListener(this.monthActionListener);
        // panel.removePropertyChangeListener(SELECTEDDATE_PROPERTYNAME,
        // this.monthPropertyChangeListener);
        panel.setButtonGroup(null);
    }

    private void setPreMonthPanels(MonthPanel[] panels) {
        if (this.preMonthPanel != null) {
            for (int i = 0; i < this.preMonthPanel.length; i++) {
                this.onRemoveMonthPanel(this.preMonthPanel[i]);
            }
        }
        this.preMonthPanel = panels;
        if (this.preMonthPanel != null) {
            for (int i = 0; i < this.preMonthPanel.length; i++) {
                this.onAddMonthPanel(this.preMonthPanel[i]);
            }
        }
        this.rebuildGui();

    }

    private void setPostMonthPanels(MonthPanel[] panels) {
        if (this.postMonthPanel != null) {
            for (int i = 0; i < this.postMonthPanel.length; i++) {
                this.onRemoveMonthPanel(this.postMonthPanel[i]);
            }
        }
        this.postMonthPanel = panels;
        if (this.postMonthPanel != null) {
            for (int i = 0; i < this.postMonthPanel.length; i++) {
                this.onAddMonthPanel(this.postMonthPanel[i]);
            }
        }
        this.rebuildGui();
    }

    private void rebuildGui() {
        this.initGui();
        this.refresh();
    }

    public void updateUI() {
        if (config != null) {
            config.updateUI();
            rebuildGui();
        }
        super.updateUI();
    }
}
