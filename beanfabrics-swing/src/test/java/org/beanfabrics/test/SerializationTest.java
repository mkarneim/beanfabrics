package org.beanfabrics.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;

import org.beanfabrics.Path;
import org.beanfabrics.model.IconPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.Options;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.swing.BnAction;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.BnCheckBox;
import org.beanfabrics.swing.BnCheckBoxMenuItem;
import org.beanfabrics.swing.BnComboBox;
import org.beanfabrics.swing.BnIconLabel;
import org.beanfabrics.swing.BnLabel;
import org.beanfabrics.swing.BnLabelTest;
import org.beanfabrics.swing.BnMenuItem;
import org.beanfabrics.swing.BnPasswordField;
import org.beanfabrics.swing.BnProgressBar;
import org.beanfabrics.swing.BnRadioButton;
import org.beanfabrics.swing.BnTextArea;
import org.beanfabrics.swing.BnTextField;
import org.beanfabrics.swing.BnToggleButton;
import org.beanfabrics.swing.list.BnList;
import org.beanfabrics.swing.list.CellConfig;
import org.beanfabrics.swing.table.BnColumnBuilder;
import org.beanfabrics.swing.table.BnTable;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

public class SerializationTest {

    @Test
    public void testBnProgressBar() throws Exception {
        // Given:
        BnProgressBar gui = new BnProgressBar();
        IntegerPM pm = new IntegerPM();
        byte[] buf = serialize(gui);

        // When:
        BnProgressBar act = deserialize(buf);
        act.setPresentationModel(pm);
        pm.setInteger(10);

        // Then:
        // show(act);
        assertThat(act.getValue(), is(10));
    }

    @Test
    public void testBnLabel() throws Exception {
        // Given:
        BnLabel gui = new BnLabel();
        TextPM pm = new TextPM();
        byte[] buf = serialize(gui);

        // When:
        BnLabel act = deserialize(buf);
        act.setPresentationModel(pm);
        pm.setText("hello");

        // Then:
        // show(act);
        assertThat(act.getText(), is("hello"));
    }

    @Test
    public void testBnIconLabel() throws Exception {
        // Given:
        BnIconLabel gui = new BnIconLabel();
        IconPM pm = new IconPM();
        byte[] buf = serialize(gui);

        // When:
        BnIconLabel act = deserialize(buf);
        act.setPresentationModel(pm);
        pm.setIconUrl(BnLabelTest.class.getResource("sample.gif"));

        // Then:
        // show(act);
        assertThat(act.getIcon(), is(pm.getIcon()));
    }

    @Test
    public void testBnTable() throws Exception {
        // Given:
        BnTable gui = new BnTable();
        gui.setColumns(new BnColumnBuilder().addColumn().withName("Color").withPath("this").build());
        ListPM<TextPM> pm = new ListPM<TextPM>();
        populate(pm, "green", "blue", "yellow", "black");

        byte[] buf = serialize(gui);

        // When:
        BnTable act = deserialize(buf);
        act.setPresentationModel(pm);
        pm.getSelection().add(pm.getAt(2));

        // Then:
        // show(act);
        assertThat(act.getSelectedRow(), is(2));
    }

    @Test
    public void testBnList() throws Exception {
        // Given:
        BnList gui = new BnList();
        gui.setCellConfig(new CellConfig(new Path("this")));
        ListPM<TextPM> pm = new ListPM<TextPM>();
        populate(pm, "green", "blue", "yellow", "black");

        byte[] buf = serialize(gui);

        // When:
        BnList act = deserialize(buf);
        act.setPresentationModel(pm);
        pm.getSelection().add(pm.getAt(2));

        // Then:
        // show(act);
        assertThat(((TextPM) act.getSelectedValue()).getText(), is("yellow"));
    }

    @Test
    public void testBnTextArea() throws Exception {
        // Given:
        BnTextArea gui = new BnTextArea();
        TextPM pm = new TextPM();

        byte[] buf = serialize(gui);

        // When:
        BnTextArea act = deserialize(buf);
        act.setPresentationModel(pm);
        pm.setText("dummy");

        // Then:
        assertThat(act.getText(), is("dummy"));
    }

    @Test
    public void testBnComboBox() throws Exception {
        // Given:
        BnComboBox gui = new BnComboBox();
        TextPM pm = new TextPM();
        pm.setOptions(Options.create("green", "yellow", "blue"));
        byte[] buf = serialize(gui);

        // When:
        BnComboBox act = deserialize(buf);
        act.setPresentationModel(pm);
        pm.setText("yellow");

        // Then:
        // show(act);
        assertThat((String) act.getSelectedItem(), is("yellow"));
    }

    @Test
    public void testBnAction() throws Exception {
        // Given:
        BnAction gui = new BnAction();
        FileMenuPM pm = new FileMenuPM();
        byte[] buf = serialize(gui);

        // When:
        BnAction act = deserialize(buf);
        act.setPresentationModel(pm.newFile);
        pm.newFile.setTitle("New File");
        act.actionPerformed(new ActionEvent(this, 0, "dummy"));

        // Then:
        assertThat((String) act.getValue(Action.NAME), is("New File"));
        assertThat(pm.getCalledOperationNames(), containsExactly("newFile"));
    }

    @Test
    public void testBnMenuItem() throws Exception {
        // Given:
        BnMenuItem gui = new BnMenuItem();
        FileMenuPM pm = new FileMenuPM();
        byte[] buf = serialize(gui);

        // When:
        BnMenuItem act = deserialize(buf);
        act.setPresentationModel(pm.newFile);
        pm.newFile.setTitle("New File");
        act.doClick();

        // Then:
        // show(act);
        assertThat(pm.getCalledOperationNames(), containsExactly("newFile"));
    }

    @Test
    public void testBnButton() throws Exception {
        // Given:
        BnButton gui = new BnButton();
        FileMenuPM pm = new FileMenuPM();
        byte[] buf = serialize(gui);

        // When:
        BnButton act = deserialize(buf);
        act.setPresentationModel(pm.newFile);
        pm.newFile.setTitle("New File");
        act.doClick();

        // Then:
        // show(act);
        assertThat(act.getText(), is("New File"));
        assertThat(pm.getCalledOperationNames(), containsExactly("newFile"));
    }

    @Test
    public void testBnCheckBoxMenuItem() throws Exception {
        // Given:
        BnCheckBoxMenuItem gui = new BnCheckBoxMenuItem();
        FileMenuPM pm = new FileMenuPM();
        byte[] buf = serialize(gui);

        // When:
        BnCheckBoxMenuItem act = deserialize(buf);
        act.setPresentationModel(pm.autoSaveOnQuit);
        act.doClick();

        // Then:
        // show(act);
        assertThat(pm.autoSaveOnQuit.getBoolean(), is(true));
    }

    @Test
    public void testBnRadioButton() throws Exception {
        // Given:
        BnRadioButton gui = new BnRadioButton();
        ContactPM pm = new ContactPM();
        byte[] buf = serialize(gui);

        // When:
        BnRadioButton act = deserialize(buf);
        act.setPresentationModel(pm.isMarried);
        pm.isMarried.setBoolean(true);

        // Then:
        // show(act);
        assertThat(act.isSelected(), is(true));
    }

    @Test
    public void testBnCheckBox() throws Exception {
        // Given:
        BnCheckBox gui = new BnCheckBox();
        ContactPM pm = new ContactPM();
        byte[] buf = serialize(gui);

        // When:
        BnCheckBox act = deserialize(buf);
        act.setPresentationModel(pm.isMarried);
        pm.isMarried.setBoolean(true);

        // Then:
        // show(act);
        assertThat(act.isSelected(), is(true));
    }

    @Test
    public void testBnToggleButton() throws Exception {
        // Given:
        BnToggleButton gui = new BnToggleButton();
        ContactPM pm = new ContactPM();
        byte[] buf = serialize(gui);

        // When:
        BnToggleButton act = deserialize(buf);
        act.setPresentationModel(pm.isMarried);
        pm.isMarried.setBoolean(true);

        // Then:
        // show(act);
        assertThat("act.isSelected()", act.isSelected(), is(true));
    }

    @Test
    public void testBnTextField() throws Exception {
        // Given:
        BnTextField gui = new BnTextField();
        CalculatorPM pm = new CalculatorPM();
        byte[] buf = serialize(gui);

        // When:
        BnTextField act = deserialize(buf);
        act.setPresentationModel(pm.input);
        pm.input.setInteger(123);

        // Then:
        // show(act);
        assertThat(act.getText(), is("123"));
    }

    @Test
    public void testBnPasswordField() throws Exception {
        // Given:
        BnPasswordField gui = new BnPasswordField();
        TextPM pm = new TextPM();
        byte[] buf = serialize(gui);

        // When:
        BnPasswordField act = deserialize(buf);
        act.setPresentationModel(pm);
        pm.setText("very secret");

        // Then:
        // show(act);
        assertThat("act.getText()", act.getPassword(), is("very secret".toCharArray()));
    }

    @Test
    public void testBnTextFieldInsidePanel() throws Exception {
        // Given:
        AddressPanel panel = new AddressPanel();
        AddressPM pm = new AddressPM();
        byte[] buf = serialize(panel);

        // When:
        AddressPanel act = deserialize(buf);
        act.setPresentationModel(pm);
        pm.city.setText("Boston");

        // Then:
        // show(act);
        assertThat(act.getTfCity().getText(), is("Boston"));
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

    private void populate(ListPM<TextPM> pm, String... strings) {
        for (String string : strings) {
            pm.add(new TextPM(string));
        }
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

    private <T> Matcher<List<T>> containsExactly(final T... expected) {
        Matcher<List<T>> result = new BaseMatcher<List<T>>() {
            protected T[] theExpected = expected;

            @Override
            public boolean matches(Object actual) {
                List<T> list = (List<T>) actual;
                if (list.size() != theExpected.length) {
                    return false;
                }
                return list.containsAll(Arrays.asList(theExpected));
            }

            @Override
            public void describeTo(Description desc) {
                desc.appendText(Arrays.toString(theExpected));
            }
        };
        return result;
    }
}
