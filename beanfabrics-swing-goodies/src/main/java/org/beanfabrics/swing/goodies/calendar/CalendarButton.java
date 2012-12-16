/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.goodies.calendar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

/**
 * CalendarButton provides a simple mechanism for the user to bind a date
 * chooser popup to a text component. Whenever the button is pressed a popup
 * appears below a specified text component, and displays a CalenderBean
 * control.
 * <P>
 * example:<BR>
 * 
 * <PRE>
 * JTextField dateField = new JTextField(&quot;14.01.1971   &quot;);
 * CalendarButton dateButton = new CalendarButton();
 * dateButton.setText(&quot;...&quot;);
 * dateButton.setTextComponent(dateField);
 * dateButton.setPreferredSize(new Dimension(20, dateField.getPreferredSize().height));
 * JFrame frame = new JFrame();
 * frame.getContentPane().setLayout(new FlowLayout(0));
 * frame.getContentPane().add(dateField);
 * frame.getContentPane().add(dateButton);
 * frame.pack();
 * frame.setVisible(true);
 * </PRE>
 * 
 * @author Michael Karneim
 */
public class CalendarButton extends JButton implements ActionListener {
    private JTextComponent textComponent;
    private JPopupMenu popup = null;
    private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    private CalendarChooser calendarBean;
    private int numberOfPreviousVisibleMonths = 0;
    private int numberOfSubsequentVisibleMonths = 0;

    /**
     * Creates an instance of CalenderBean
     */
    public CalendarButton() {
        this.addActionListener(this);
        this.setEnabled(this.textComponent != null ? this.textComponent.isEditable() : false);
    }

    /**
     * Implements the ActionListener interface. This method is called whenever
     * the button is pressed.
     */
    public void actionPerformed(ActionEvent e) {
        if (textComponent == null)
            return;
        if (e.getSource() == this) {
            JPopupMenu p = this.getPopupMenu();
            String dateString = this.textComponent.getText();
            try {
                Date date = CalendarButton.format.parse(dateString);
                this.getCalendarBean().setSelectedDate(date);
            } catch (ParseException ex) {
                this.getCalendarBean().setMonth(new Date());
                this.getCalendarBean().setSelectedDate((Date)null);
            }
            p.show(textComponent, 0, textComponent.getSize().height);
        } else if (e.getSource() == this.getCalendarBean()) {
            JPopupMenu p = this.getPopupMenu();
            p.setVisible(false);
            Date date = this.getCalendarBean().getSelectedDate();
            if (date == null) {
                this.textComponent.setText("");
            } else {
                String dateString = format.format(date);
                this.textComponent.setText(dateString);
            }
        }
    }

    private transient PropertyChangeListener enableStateListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            if (!(evt.getSource() == textComponent))
                return;
            boolean newValue = ((Boolean)evt.getNewValue()).booleanValue();
            setEnabled(newValue);
        }
    };

    /**
     * Sets the reference to the text component, the calender button should work
     * with. From this text component the calendar button will get the date
     * string to put into the date chooser popup on button click. Below this
     * text component the date chooser popup will be displayed. Into this text
     * component the selected date will be inserted from the date chooser
     * component.
     * 
     * @param newTextComponent the text component the button will work with
     */
    public void setTextComponent(javax.swing.text.JTextComponent newTextComponent) {
        javax.swing.text.JTextComponent oldTextComponent = textComponent;
        if (this.textComponent != null)
            this.textComponent.removePropertyChangeListener("editable", enableStateListener);
        textComponent = newTextComponent;
        if (this.textComponent != null) {
            this.textComponent.addPropertyChangeListener("editable", enableStateListener);
            this.setEnabled(this.textComponent.isEditable());
        }
        firePropertyChange("textComponent", oldTextComponent, newTextComponent);
    }

    /**
     * Returns the text component that is bound to this button for displaying
     * the date.
     * 
     * @return the text component that is bound to this button for displaying
     *         the date
     */
    public JTextComponent getTextComponent() {
        return textComponent;
    }

    /**
     * Returns the popup menu with the date chooser control.
     * 
     * @return the popup menu with the date chooser control
     */
    protected JPopupMenu getPopupMenu() {
        if (this.popup == null) {
            this.popup = new JPopupMenu();
            this.popup.add(this.getCalendarBean());
        }
        return this.popup;
    }

    /**
     * Defines the pattern that has to be used to transform a String to a Date
     * instance and vice versa. The default is "dd.mm.yyyy".
     * 
     * @param pattern the format pattern
     */
    public void setDatePattern(String pattern) {
        String oldPattern = CalendarButton.format.toPattern();
        CalendarButton.format.applyPattern(pattern);
        firePropertyChange("datePattern", oldPattern, pattern);
    }

    /**
     * Returns the pattern that has to be used to transform a String to a Date
     * instance and vice versa.
     * 
     * @return the pattern that has to be used to transform a String to a Date
     *         instance and vice versa
     */
    public String getDatePattern() {
        return CalendarButton.format.toPattern();
    }

    /**
     * Sets the CalendarBean instance that has to be used in the popup.
     * <P>
     * Note:<BR>
     * This method offers an easy way to customize the look and feel of the date
     * chooser popup. Be carefull not to use one single calendar bean in more
     * calendar buttons.
     * 
     * @param newCalendarBean the CalendarBean instance that has to be used in
     *            the popup
     */
    public void setCalendarBean(CalendarChooser newCalendarBean) {
        CalendarChooser oldCalendarBean = calendarBean;
        if (oldCalendarBean != null) {
            oldCalendarBean.removeActionListener(this);
        }
        this.calendarBean = newCalendarBean;
        this.calendarBean.addActionListener(this);
        firePropertyChange("calendarBean", oldCalendarBean, newCalendarBean);
    }

    /**
     * Returns the CalendarBean instance that is used in the popup.
     * 
     * @return the CalendarBean instance that is used in the popup
     */
    public CalendarChooser getCalendarBean() {
        if (this.calendarBean == null) {
            this.calendarBean = new CalendarChooser();
            this.calendarBean.setNumberOfPreviousVisibleMonths(numberOfPreviousVisibleMonths);
            this.calendarBean.setNumberOfSubsequentVisibleMonths(numberOfSubsequentVisibleMonths);
            this.calendarBean.addActionListener(this);
        }
        return this.calendarBean;
    }

    public int getNumberOfPreviousVisibleMonths() {
        return numberOfPreviousVisibleMonths;
    }

    public void setNumberOfPreviousVisibleMonths(int numberOfPreviousVisibleMonths) {
        this.numberOfPreviousVisibleMonths = numberOfPreviousVisibleMonths;
    }

    public int getNumberOfSubsequentVisibleMonths() {
        return numberOfSubsequentVisibleMonths;
    }

    public void setNumberOfSubsequentVisibleMonths(int numberOfSubsequentVisibleMonths) {
        this.numberOfSubsequentVisibleMonths = numberOfSubsequentVisibleMonths;
    }
}
