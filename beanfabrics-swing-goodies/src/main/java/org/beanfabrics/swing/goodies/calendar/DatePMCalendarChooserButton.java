/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.goodies.calendar;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.ConversionException;
import org.beanfabrics.model.DatePM;
import org.beanfabrics.model.IDatePM;
import org.beanfabrics.model.PresentationModel;

/**
 * @author Max Gensthaler
 */
@SuppressWarnings("serial")
public class DatePMCalendarChooserButton extends JButton implements View<IDatePM> {
    private static final URL ICON_URL = DatePMCalendarChooserButton.class.getResource("bn_calendarchooser_obj16.png");
    private static final ImageIcon ICON = new ImageIcon(ICON_URL);

    private final WeakPropertyChangeListener listener = new MyWeakPropertyChangeListener();

    private class MyWeakPropertyChangeListener implements WeakPropertyChangeListener, Serializable {
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            refresh();
        }
    };

    private IDatePM pModel;
    private CalendarChooser calendarChooser;
    private JPopupMenu popup;
    private boolean dateSelected;
    private final ActionListener myActionListener = new MyActionListener();

    private class MyActionListener implements ActionListener, Serializable {
        public void actionPerformed(ActionEvent e) {

            JPopupMenu popup = createPopup();
            int x = DatePMCalendarChooserButton.this.getWidth() - popup.getPreferredSize().width;
            int y = DatePMCalendarChooserButton.this.getHeight();
            
            dateSelected = false;
            popup.show(DatePMCalendarChooserButton.this, x, y);
        }
    }

    private final PropertyChangeListener calendarChooserListener = new MyPropertyChangeListener();

    private class MyPropertyChangeListener implements PropertyChangeListener, Serializable {
        /**
         * Listens for a "date" property change or a "selectedDate" property change event from the CalendarChooser.
         * Updates the {@link DatePM} and closes the popup.
         * 
         * @param evt
         *            the event
         */
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(CalendarChooser.SELECTEDDATE_PROPERTYNAME)) {
                if (popup != null && popup.isVisible()) {
                    dateSelected = true;
                    popup.setVisible(false);
                    Date selectedDate = getCalendarChooser().getSelectedDate();

                    setDate(selectedDate);

                }
            }
        }
    };

    public DatePMCalendarChooserButton() {
        this(0, 0);
    }

    public DatePMCalendarChooserButton(int prevVisMonths, int subsVisMonths) {
        super(ICON);
        this.setMargin(new Insets(0, 0, 0, 0));
        this.setContentAreaFilled(false);
        this.addActionListener(myActionListener);
        CalendarChooser cc = getCalendarChooser();
        cc.setNumberOfPreviousVisibleMonths(prevVisMonths);
        cc.setNumberOfSubsequentVisibleMonths(subsVisMonths);
        refresh();
    }

    /** {@inheritDoc} */
    public IDatePM getPresentationModel() {
        return pModel;
    }

    /** {@inheritDoc} */
    public void setPresentationModel(IDatePM newModel) {
        IDatePM oldModel = this.pModel;
        if (this.pModel != null) {
            this.pModel.removePropertyChangeListener(listener);
        }
        this.pModel = newModel;
        if (this.pModel != null) {
            this.pModel.addPropertyChangeListener(listener);
        }
        refresh();
        this.firePropertyChange("presentationModel", oldModel, newModel);
    }

    /**
     * Returns whether this component is connected to the target {@link PresentationModel} to synchronize with. This is
     * a convenience method.
     * 
     * @return <code>true</code> when this component is connected, else <code>false</code>
     */
    boolean isConnected() {
        return this.pModel != null;
    }

    private void refresh() {
        if (isConnected()) {
            try {
                Date date = getPresentationModel().getDate();
                calendarChooser.setSelectedDate(date);
            } catch (ConversionException ce) {
                // the date is invalid
                // -> clear the selected date from the calendar chooser component
                calendarChooser.setSelectedDate(null);
            }
            
            final String tooltip;
            if (pModel.isValid()) {
                tooltip = pModel.getDescription();
                this.setBackground(null);
            } else {
                tooltip = pModel.getValidationState().getMessage();
                this.setBackground(Color.RED);
            }
            this.setToolTipText(tooltip);
            boolean editable = pModel.isEditable();
            this.setEnabled(editable);
        } else {
            this.setToolTipText(null);
            this.setEnabled(false);
        }
    }

    /**
     * Set date to the model.
     * 
     * @param date
     *            date to set
     */
    private void setDate(Date date) {
        if (pModel != null) {
            pModel.setDate(date);
        }
    }

    public JPopupMenu createPopup() {
        if (this.popup == null) {
            this.popup = new JPopupMenu() {
                @Override
                public void setVisible(boolean b) {
                    Boolean isCanceled = (Boolean) getClientProperty("JPopupMenu.firePopupMenuCanceled");
                    if (b || (!b && dateSelected) || ((isCanceled != null) && !b && isCanceled.booleanValue())) {
                        super.setVisible(b);
                    }
                }
            };
            popup.setLightWeightPopupEnabled(true);
        }
        popup.removeAll();
        popup.add(getCalendarChooser());
        return popup;
    }

    public CalendarChooser getCalendarChooser() {
        if (calendarChooser == null) {
            CalendarChooser chooser = new CalendarChooser();
            // chooser.setBackground(new Color(230, 142, 138));
            // chooser.setHeaderForegroundColor(Color.WHITE);
            // chooser.setDateFont(chooser.getDateFont().deriveFont(Font.BOLD));
            chooser.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            chooser.setOpaque(false);
            setCalendarChooser(chooser);
        }
        return calendarChooser;
    }

    public void setCalendarChooser(CalendarChooser aCalendarChooser) {
        if (this.calendarChooser != null) {
            this.calendarChooser.removePropertyChangeListener(CalendarChooser.SELECTEDDATE_PROPERTYNAME,
                    this.calendarChooserListener);
        }
        this.calendarChooser = aCalendarChooser;
        if (this.calendarChooser != null) {
            this.calendarChooser.addPropertyChangeListener(CalendarChooser.SELECTEDDATE_PROPERTYNAME,
                    this.calendarChooserListener);
        }
    }

    /**
     * Updates the UI of itself and the popup.
     */
    @Override
    public void updateUI() {
        super.updateUI();
        setEnabled(isEnabled()); // TODO hmm, why?
        if (calendarChooser != null) {
            SwingUtilities.updateComponentTreeUI(calendarChooser);
        }
        if (popup != null) {
            SwingUtilities.updateComponentTreeUI(popup);
        }
    }
}
