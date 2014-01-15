/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.goodies.calendar;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * @author Michael Karneim
 * @author Max Gensthaler
 * @version 3.0
 */
@SuppressWarnings("serial")
public class MonthPanel extends JPanel {
    private final static int ROWS = 7;
    private final static int COLS = 7;
    public static final String SELECTEDDATE_PROPERTYNAME = "selectedDate";

    private Configuration config = new Configuration();

    public static class Configuration extends MonthPanelUISettings {
        public Configuration() {
            updateUI();
        }

        public void updateUI() {
            setDelegate(new MonthPanelUISettingsDefaults());
        }
    }

    // State
    private Locale locale = null;
    private Calendar month;
    private Calendar selectedDate = null;
    private DateFormat format = null;
    private DateFormatSymbols dateSymbols = null;
    private JComponent leftUpperCornerComponent = null;
    private JComponent rightUpperCornerComponent = null;

    // Implementation
    private GridLayout gridLayout1 = new GridLayout();
    private String[] dayName = null;
    private JLabel[] headerLabel = new JLabel[7];
    private JToggleButton[][] button = new JToggleButton[ROWS][COLS];
    private JPanel controlPanel;
    private ButtonGroup group;
    private JLabel dateLabel = new JLabel("September 2000");
    private final ItemListener itemListener = new MyItemListener();

    private class MyItemListener implements ItemListener, Serializable {
        public void itemStateChanged(ItemEvent e) {
            if (pendingShowMonth) {
                return;
            } else {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String day = ((JToggleButton) e.getSource()).getText();
                    try {
                        int d = Integer.parseInt(day);
                        MonthPanel.this.setSelectedDay(d);
                    } catch (NumberFormatException ex) {
                        // ignore
                    }
                } else {
                    setSelectedDate((Date) null, false);
                }
            }
        }
    };

    private final ActionListener dayActionListener = new MyActionListener();

    private class MyActionListener implements ActionListener, Serializable {
        public void actionPerformed(ActionEvent evt) {
            fireActionPerformed(new ActionEvent(MonthPanel.this, ActionEvent.ACTION_PERFORMED, null));
        }
    };

    private Vector<ActionListener> actionListeners;

    /**
     * Creates a new instance of MonthPanel displaying the current month using the default locale.
     */
    public MonthPanel() {
        this(new Date(), Locale.getDefault(), new ButtonGroup());
    }

    /**
     * Creates a new instance of MonthPanel displaying the given month and using the given locale.
     * 
     * @param date
     *            the date that initially should be selected
     * @param locale
     *            the locale that should be used for the calendar
     */
    public MonthPanel(Date date, Locale locale) {
        this(date, locale, new ButtonGroup());
    }

    /**
     * Creates a new instance of MonthPanel displaying the current month using the default locale.
     */
    public MonthPanel(ButtonGroup buttonGroup) {
        this(new Date(), Locale.getDefault(), buttonGroup);
    }

    /**
     * Creates a new instance of MonthPanel displaying the given month and using the given locale.
     * 
     * @param date
     *            the date that initially should be selected
     * @param locale
     *            the locale that should be used for the calendar
     * @param buttonGroup
     *            ButtonGroup
     */
    public MonthPanel(Date date, Locale locale, ButtonGroup buttonGroup) {
        this.locale = locale;
        this.group = buttonGroup;
        Calendar month = Calendar.getInstance(locale);
        month.setTime(date);
        this.format = new SimpleDateFormat("MMMM yyyy", this.locale);
        this.dateSymbols = new DateFormatSymbols(this.locale);

        this.setLayout(new BorderLayout(1, 1));
        this.setOpaque(false);
        this.initGui();
        this.setMonth(month);
    }

    /**
     * Returns the button group.
     * 
     * @return ButtonGroup
     */
    public ButtonGroup getButtonGroup() {
        return this.group;
    }

    /**
     * Adds all 'day' buttons to the given button group.
     * 
     * @param group
     *            ButtonGroup
     */
    public void setButtonGroup(ButtonGroup group) {
        if (this.group != null) {
            for (int y = 1; y < ROWS; y++) {
                for (int x = 0; x < COLS; x++) {
                    if (button[y][x] != null) {
                        this.group.remove(button[y][x]);
                    }
                }
            }
        }
        this.group = group;
        if (this.group != null) {
            for (int y = 1; y < ROWS; y++) {
                for (int x = 0; x < COLS; x++) {
                    if (button[y][x] != null) {
                        group.add(button[y][x]);
                    }
                }
            }
        }
        this.rebuildGui();
    }

    /**
     * Sets the graphical configuration of this panel
     * 
     * @param config
     *            Configuration
     */
    public void setConfiguration(Configuration config) {
        this.config = config;
        this.rebuildGui();
    }

    /**
     * Sets the component to be displayed in the left upper corner of this panel
     * 
     * @param comp
     *            JComponent
     */
    public void setLeftUpperCornerComponent(JComponent comp) {
        if (this.controlPanel != null && this.leftUpperCornerComponent != null) {
            this.controlPanel.remove(this.leftUpperCornerComponent);
        }
        this.leftUpperCornerComponent = comp;
        if (this.controlPanel != null && this.leftUpperCornerComponent != null) {
            this.controlPanel.add("West", this.leftUpperCornerComponent);
        }
    }

    /**
     * Sets the component to be displayed in the right upper corner of this panel
     * 
     * @param comp
     *            JComponent
     */
    public void setRightUpperCornerComponent(JComponent comp) {
        if (this.controlPanel != null && this.rightUpperCornerComponent != null) {
            this.controlPanel.remove(this.rightUpperCornerComponent);
        }
        this.rightUpperCornerComponent = comp;
        if (this.controlPanel != null && this.rightUpperCornerComponent != null) {
            this.controlPanel.add("East", this.rightUpperCornerComponent);
        }
    }

    /**
     * Builds or Rebuilds the graphical user interface of the calendar. This method is called by the constructor or
     * whenever properties like font colors or font sizes are changed.
     */
    public void initGui() {
        // Remove all components from this panel in order to begin creating the
        // gui
        // from scratch.
        this.removeAll();

        // Create all toggle buttons
        for (int y = 1; y < ROWS; y++) {
            for (int x = 0; x < COLS; x++) {
                if (button[y][x] == null) {
                    button[y][x] = new JToggleButton("  ") {
                        @Override
                        public void paintComponent(Graphics g) {
                            // paint background
                            if (this.isSelected()) {

                                g.setColor(config.getSelectedBackgroundColor());

                            } else {
                                g.setColor(this.getBackground());
                            }
                            g.fillRect(0, 0, this.getWidth(), this.getHeight());
                            super.paintComponent(g);
                        }
                    };
                    button[y][x].setContentAreaFilled(false);
                    button[y][x].setBorderPainted(false);
                    button[y][x].setBorder(null);
                    button[y][x].setFocusPainted(false);
                    button[y][x].addItemListener(itemListener);
                    button[y][x].addActionListener(dayActionListener);
                    if (group != null) {
                        group.add(button[y][x]);
                    }
                }
            }
        }

        // Create the control panel on the top of the calendar
        // containing the roll forward and roll backward buttons
        // and the title label for the calendar, which displays the current
        // visible
        // month name and the year.
        EmptyBorder emptyBorder = new EmptyBorder(0, 0, 0, 0);
        if (this.controlPanel == null) {
            this.controlPanel = new JPanel(new BorderLayout());
            this.controlPanel.setOpaque(false);
            this.controlPanel.add("Center", dateLabel);
            if (this.leftUpperCornerComponent != null) {
                this.controlPanel.add("West", this.leftUpperCornerComponent);
            }
            if (this.rightUpperCornerComponent != null) {
                this.controlPanel.add("East", this.rightUpperCornerComponent);
            }
        }
        this.add("North", controlPanel);

        dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dateLabel.setFont(this.config.getDateFont());
        dateLabel.setForeground(this.config.getHeaderForegroundColor());
        dateLabel.setOpaque(false);

        // Create the calandar panel which contains all the toggle buttons
        // for the days of the currently dispayed month, and the header with
        // the names of the days.
        JPanel calendarPanel = new JPanel(gridLayout1);
        calendarPanel.setOpaque(false);
        this.add("Center", calendarPanel);
        gridLayout1.setColumns(COLS);
        gridLayout1.setRows(ROWS);

        // Add the headers
        for (int x = 0; x < COLS; x++) {
            // headerLabel[x] = new JLabel( getDayNames()[ x]);
            if (headerLabel[x] == null) {
                headerLabel[x] = new JLabel("XX");
                headerLabel[x].setOpaque(false);
            }
            headerLabel[x].setHorizontalAlignment(SwingConstants.CENTER);
            headerLabel[x].setFont(this.config.getHeaderFont());
            headerLabel[x].setForeground(this.config.getHeaderForegroundColor());
            calendarPanel.add(headerLabel[x]);
        }

        // Add the toggle buttons for the days
        for (int y = 1; y < ROWS; y++) {
            for (int x = 0; x < COLS; x++) {
                button[y][x].setFont(this.config.getDayFont());
                button[y][x].setMargin(this.config.getDayMargin());
                button[y][x].setBorder(emptyBorder);
                button[y][x].setBackground(this.config.getBackgroundColor());
                calendarPanel.add(button[y][x]);
            }
        }

        // force revalidation of the layout manager
        this.validate();
    }

    /**
     * Rolls the calendar one month forward and displays the new month.
     */
    public void rollOneMonthForward() {
        this.month.add(Calendar.MONTH, 1);
        this.refresh();
    }

    /**
     * Rolls the calendar one month backwards and displays the new month.
     */
    public void rollOneMonthBack() {
        this.month.add(Calendar.MONTH, -1);
        this.refresh();
    }

    /**
     * Sets the selected day of the visible month.
     * 
     * @param dayInMonth
     *            int
     */
    public void setSelectedDay(int dayInMonth) {
        Calendar cal = (Calendar) this.month.clone();
        cal.set(Calendar.DATE, dayInMonth);
        this.setSelectedDate(cal.getTime());
    }

    /**
     * Selects the given date and displays the month containing that date.
     * 
     * @param date
     *            the date to select
     */
    public void setSelectedDate(Date date) {
        this.setSelectedDate(date, true);
    }

    /**
     * Sets the displayed month and the selected date.
     * 
     * @param month
     *            the month to show
     * @param selectedDate
     *            the date to select
     */
    public void setDates(Date month, Date selectedDate) {
        this.setMonth(month);
        this.setSelectedDate(selectedDate, false);
    }

    /**
     * Selects the given date and displays the month containing that date.
     * 
     * @param date
     *            the date to select
     * @param rollToMonth
     *            boolean if true the panel scrolls to the respective month
     */
    public void setSelectedDate(Date date, boolean rollToMonth) {
        if (date == null) {
            this.selectedDate = null;
            this.refresh();
            if (rollToMonth) {
                setMonth(new Date());
            }
        } else {
            if (this.selectedDate == null) {
                this.selectedDate = (Calendar) this.month.clone();
            }
            this.selectedDate.setTime(date);
            if (rollToMonth) {
                this.setMonth(date);
            } else {
                this.refresh();
            }
        }
    }

    /**
     * Returns the selected date as an Date instance.
     * 
     * @return the selected date
     */
    public Date getSelectedDate() {
        if (this.selectedDate == null)
            return null;
        return this.selectedDate.getTime();
    }

    /**
     * Returns the visible month's date.
     * 
     * @return Date
     */
    public Date getMonth() {
        return this.month.getTime();
    }

    /**
     * Sets the given month to display on the panel.
     * 
     * @param month
     *            Date
     */
    public void setMonth(Date month) {
        if (month == null)
            throw new NullPointerException("Illeagal null argument for month.");
        this.month.setTime(month);
        this.refresh();
    }

    /**
     * Sets the given month to display on the panel.
     * 
     * @param month
     *            the month to display
     */
    public void setMonth(Calendar month) {
        if (month == null)
            throw new NullPointerException("Illeagal null argument for month.");
        this.month = (Calendar) month.clone();
        this.format.setCalendar((Calendar) this.month.clone());
        this.refresh();
    }

    boolean pendingShowMonth = false;

    /**
     * Displays the currently set month on the calendar panel.
     */
    protected void refresh() {
        if (this.pendingShowMonth) {
            return;
        }
        this.pendingShowMonth = true;
        try {
            int selectedDay = -1; // NONE selected
            if (selectedDate != null && (this.selectedDate.get(Calendar.YEAR) == this.month.get(Calendar.YEAR))
                    && (this.selectedDate.get(Calendar.MONTH) == this.month.get(Calendar.MONTH))) {
                selectedDay = this.selectedDate.get(Calendar.DATE);
            }
            if (group != null) {
                group.setSelected(null, true);
            }

            int todayDate = -1;
            Calendar today = Calendar.getInstance();
            if ((today.get(Calendar.YEAR) == this.month.get(Calendar.YEAR))
                    && (today.get(Calendar.MONTH) == this.month.get(Calendar.MONTH))) {
                todayDate = today.get(Calendar.DATE);
            }

            for (int x = 0; x < COLS; x++) {
                this.headerLabel[x].setText(this.getDayNames()[x]);
            }

            int[][] dates = getDatesForButtons(month);
            for (int y = 0; y < 6; y++) {
                for (int x = 0; x < 7; x++) {
                    int dayOfMonth = dates[y][x];
                    button[y + 1][x].setSelected(false);
                    if (dayOfMonth != 0) {
                        button[y + 1][x].setText("" + dayOfMonth);
                        button[y + 1][x].setEnabled(true);
                        configure(button[y + 1][x], dayOfMonth, selectedDay, todayDate);
                        if (dayOfMonth == selectedDay) {
                            button[y + 1][x].setSelected(true);
                        } else {
                            this.deselectButton(button[y + 1][x]);
                        }

                    } else {
                        button[y + 1][x].setText("");
                        button[y + 1][x].setEnabled(false);
                        this.deselectButton(button[y + 1][x]);
                    }
                }
            }
            this.dateLabel.setText(this.formatDate(this.month.getTime()));
        } finally {
            this.pendingShowMonth = false;
        }
    }

    private void deselectButton(JToggleButton btn) {
        if (group != null) {
            group.remove(btn);
        }
        btn.setSelected(false);
        if (group != null) {
            group.add(btn);
        }
    }

    /**
     * Configures the given button.
     * 
     * @param btn
     *            JToggleButton
     * @param dayOfMonth
     *            int
     * @param selectedDay
     *            int
     * @param todayDate
     *            int
     */
    private void configure(JToggleButton btn, int dayOfMonth, int selectedDay, int todayDate) {
        if (dayOfMonth == selectedDay) {
            btn.setForeground(this.config.getSelectedColor());
            if (this.config.getSelectedBackgroundColor() != null) {
                btn.setBackground(this.config.getSelectedBackgroundColor());
            }
        } else {
            if (dayOfMonth == todayDate) {
                btn.setForeground(this.config.getTodayForegroundColor());
            } else if (this.isWeekend(dayOfMonth)) {
                btn.setForeground(this.config.getWeekendForegroundColor());
            } else {
                btn.setForeground(this.config.getWorkdayForegroundColor());
            }
            btn.setBackground(this.config.getBackgroundColor());
        }
    }

    /**
     * Returns the human readable shortages for the week days beginning with the weeks first day at index 0.
     * 
     * @return an array of String containing the short names of the days of the week
     */
    protected String[] getDayNames() {
        // return this.dateSymbols.getShortWeekdays();
        if (this.dayName == null) {
            String[] shortWeekdays = this.dateSymbols.getShortWeekdays();
            int firstDayOfWeek = this.month.getFirstDayOfWeek();
            int delta;
            switch (firstDayOfWeek) {
            case Calendar.SUNDAY:
                delta = 0;
                break;
            case Calendar.MONDAY:
                delta = 1;
                break;
            case Calendar.TUESDAY:
                delta = 2;
                break;
            case Calendar.WEDNESDAY:
                delta = 3;
                break;
            case Calendar.THURSDAY:
                delta = 4;
                break;
            case Calendar.FRIDAY:
                delta = 5;
                break;
            case Calendar.SATURDAY:
                delta = 6;
                break;
            default:
                throw new Error("Unexpected Error. First day of month is non of Sun, Mon, Tue, Wed, Thu, Fri, Sat");
            }

            this.dayName = new String[7];
            for (int i = 0; i < 7; i++) {
                this.dayName[i] = shortWeekdays[((i + delta) % 7) + 1];
            }
        }

        return this.dayName;
    }

    protected boolean isWeekend(int dayOfMonth) {
        Calendar temp = (Calendar) this.month.clone();
        temp.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        int dayName = temp.get(Calendar.DAY_OF_WEEK);
        if (dayName == Calendar.SATURDAY || dayName == Calendar.SUNDAY)
            return true;
        else
            return false;
    }

    /**
     * Rebuilds the GUI.
     */
    public void rebuildGui() {
        this.initGui();
        this.refresh();
    }

    // These members are for optimization purpose only
    private Date tempDate = new Date();
    private Calendar tempCal = null;

    /**
     * Returns an two dimensional array of int, each containing the date of the given months day as they will appear on
     * the toggle buttons on the calendar panel.
     * 
     * @param month
     *            the month the array should be calculated for
     * @return an array of the dates of the days of the given month
     */
    protected/* static */int[][] getDatesForButtons(Calendar month) {
        int[][] result = new int[6][7];

        this.tempDate.setTime(month.getTime().getTime());
        if (this.tempCal == null)
            this.tempCal = (Calendar) month.clone();
        this.tempCal.setTime(tempDate);
        Calendar day = tempCal;

        // Calendar day = (Calendar) month.clone();
        day.set(Calendar.DAY_OF_MONTH, 1);
        // int firstWeekDayOfMonth = day.get(Calendar.DAY_OF_WEEK);

        // find the first day of the month in the first week
        int x = 0;
        while (day.get(Calendar.DAY_OF_WEEK) != day.getFirstDayOfWeek()) {
            day.roll(Calendar.DAY_OF_WEEK, false);
            x = x + 1;
        }
        day = (Calendar) month.clone();
        day.set(Calendar.DAY_OF_MONTH, 1);

        for (int y = 0; y < 6; y++) {
            for (; x < 7; x++) {
                result[y][x] = day.get(Calendar.DATE);
                day.roll(Calendar.DATE, true);
                if (day.get(Calendar.DATE) == 1)
                    return result;
            }
            x = 0;
        }
        throw new Error("Unexpected Error. Month has more than 31 days!");
    }

    /**
     * Returns the formatted string of the given date, using the internal DateFormat as it was passed to the constructor
     * of this class. This method is called by the routine that displays the title of the currently displayed month.
     * 
     * @param date
     *            the dat to format
     * @return the fromatted string
     */
    protected String formatDate(Date date) {
        return this.format.format(date);
    }

    /**
     * Removes the given action listener from this component.
     */
    public synchronized void removeActionListener(ActionListener l) {
        if (actionListeners != null && actionListeners.contains(l)) {
            Vector<ActionListener> v = (Vector<ActionListener>) actionListeners.clone();
            v.removeElement(l);
            actionListeners = v;
        }
    }

    /**
     * Adds the given action listnener to this component. All action listeners will be informed about changes that are
     * made to the selected date of this calendar.
     * 
     * @param l
     *            the listener to add to this component
     */
    public synchronized void addActionListener(ActionListener l) {
        Vector<ActionListener> v = (actionListeners == null ? new Vector<ActionListener>(2)
                : (Vector<ActionListener>) actionListeners.clone());
        if (!v.contains(l)) {
            v.addElement(l);
            actionListeners = v;
        }
    }

    /**
     * Fires the given action event to all listeners that are added to this component.
     * 
     * @param e
     *            the event to fire
     */
    protected void fireActionPerformed(ActionEvent e) {
        if (actionListeners != null) {
            Vector<ActionListener> listeners = actionListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                listeners.elementAt(i).actionPerformed(e);
            }
        }
    }
}
