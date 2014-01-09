package org.beanfabrics.swing.goodies.calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.awt.BorderLayout;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JDialog;

import org.beanfabrics.model.DatePM;
import org.junit.Test;

public class SerializationTest {

    @Test
    public void testBnCalendarChooserButton() throws Exception {
        // Given:
        BnCalendarChooserButton gui = new BnCalendarChooserButton();
        DatePM pm = new DatePM();
        Date newDate = newDate("2014-05-31");
        byte[] buf = serialize(gui);

        // When:
        BnCalendarChooserButton act = deserialize(buf);
        act.setPresentationModel(pm);
        pm.setDate(newDate);
        // Then:
        // show(act);
        assertThat(act.getCalendarChooser().getSelectedDate(), is(newDate));
    }

    @Test
    public void testBnCalendarChooser() throws Exception {
        // Given:
        BnCalendarChooser gui = new BnCalendarChooser();
        gui.setNumberOfPreviousVisibleMonths(3);
        DatePM pm = new DatePM();
        Date newDate = newDate("2014-05-31");
        byte[] buf = serialize(gui);

        // When:
        BnCalendarChooser act = deserialize(buf);
        act.setPresentationModel(pm);
        pm.setDate(newDate);
        // Then:
        // show(act);
        assertThat(act.getSelectedDate(), is(newDate));
    }

    private Date newDate(String source) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(source);
    }

    // /

    private void show(final JComponent comp) {
        System.out.println("Showing component");
        JDialog dlg = new JDialog();
        dlg.setModal(true);
        dlg.getContentPane().add(comp, BorderLayout.CENTER);
        dlg.pack();
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }

    private <T> byte[] serialize(T p) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        try {
            out.writeObject(p);
            out.close();
            bout.close();
            return bout.toByteArray();
        } finally {
            bout.close();
            out.close();
        }
    }

    private <T> T deserialize(byte[] buff) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bin = new ByteArrayInputStream(buff);
        ObjectInputStream in = new ObjectInputStream(bin);
        try {
            @SuppressWarnings("unchecked")
            T result = (T) in.readObject();
            return result;
        } finally {
            bin.close();
            in.close();
        }
    }

}
