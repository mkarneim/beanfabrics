/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.table;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.MapPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.model.TextPM;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class ListModelTableModelTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ListModelTableModelTest.class);
    }

    private MapPM<String, MyModel> mapProperty;
    private List<BnColumn> def;

    public static class MyModel extends AbstractPM {
        protected final TextPM text = new TextPM();

        public MyModel() {
            PMManager.setup(this);
        }
    }

    @Before
    public void setUp()
        throws Exception {
        this.mapProperty = this.createMapProperty();
        this.def = this.createTableColumnDefinition();
    }

    private List<BnColumn> createTableColumnDefinition() {
        final LinkedList<BnColumn> list = new LinkedList<BnColumn>();
        list.add(new BnColumn(new Path("this.text"), "Text"));
        return list;
    }

    private MapPM<String, MyModel> createMapProperty() {
        final MapPM<String, MyModel> map = new MapPM<String, MyModel>();
        final MyModel pModel1 = new MyModel();
        pModel1.text.setText("Hello");
        final MyModel pModel2 = new MyModel();
        pModel2.text.setText("world");
        map.put("pModel1", pModel1);
        map.put("pModel2", pModel2);
        return map;
    }

    @Test
    public void testListPropertyTableModel() {
        final BnTableModel model = new BnTableModel(this.mapProperty, this.def, true);
        assertNotNull(model);
    }

    @Test
    public void testGetColumnCount() {
        final BnTableModel pModel = new BnTableModel(this.mapProperty, this.def, true);
        assertEquals("pModel.getColumnCount()", 1, pModel.getColumnCount());
    }

    @Test
    public void testGetRowCount() {
        final BnTableModel pModel = new BnTableModel(this.mapProperty, this.def, true);
        assertEquals("pModel.getRowCount()", 2, pModel.getRowCount());
    }

    @Test
    public void testGetValueAt() {
        final BnTableModel pModel = new BnTableModel(this.mapProperty, this.def, true);
        final Object o = pModel.getValueAt(0, 0);
        assertNotNull(o);
        assertTrue(o instanceof TextPM);
        final TextPM pM = (TextPM)o;
        assertEquals("pModel.getText()", "Hello", pM.getText());
    }

    @Test
    public void testAddRow() {
        final BnTableModel pModel = new BnTableModel(this.mapProperty, this.def, true);
        final MyModel pModel1 = new MyModel();
        pModel1.text.setText("!");
        this.mapProperty.put("pModel", pModel1);
        assertEquals("pModel.getRowCount()", 3, pModel.getRowCount());
    }

    @Test
    public void testDeleteRow() {
        final BnTableModel pModel = new BnTableModel(this.mapProperty, this.def, true);
        this.mapProperty.removeKey("pModel1");
        assertEquals("pModel.getRowCount()", 1, pModel.getRowCount());
    }

    @Test
    public void testReplaceRow() {
        final BnTableModel pModel = new BnTableModel(this.mapProperty, this.def, true);
        final MyModel pModel1 = new MyModel();
        pModel1.text.setText("!");
        this.mapProperty.put("pModel1", pModel1);
        assertEquals("pModel.getRowCount()", 2, pModel.getRowCount());
    }

    @Test
    public void testUpdateRow() {
        final BnTableModel model = new BnTableModel(this.mapProperty, this.def, true);
        this.mapProperty.getAt(0).text.setText("Salute");
        final TextPM pModel = (TextPM)model.getValueAt(0, 0);
        assertEquals("pModel.getText()", "Salute", pModel.getText());
    }

    @Test
    public void testGetColumnName() {
        final BnTableModel pModel = new BnTableModel(this.mapProperty, this.def, true);
        assertEquals("pModel.getColumnName( 0)", "Text", pModel.getColumnName(0));
    }

    @Test
    public void testFindColumn() {
        final BnTableModel pModel = new BnTableModel(this.mapProperty, this.def, true);
        assertEquals("pModel.findColumn(\"text\")", 0, pModel.findColumn("Text"));
    }

    @Test
    public void testGetColumnClass() {
        final BnTableModel pModel = new BnTableModel(this.mapProperty, this.def, true);
        assertEquals("pModel.getColumnClass(0)", PresentationModel.class, pModel.getColumnClass(0));
    }

    @Test
    public void testIsCellEditable() {
        final BnTableModel pModel = new BnTableModel(this.mapProperty, this.def, true);
        assertTrue("pModel.isCellEditable( 0,0)", pModel.isCellEditable(0, 0));
        final MyModel pModel1 = this.mapProperty.getAt(0);
        pModel1.text.setEditable(false);
        assertFalse("pModel.isCellEditable( 0,0)", pModel.isCellEditable(0, 0));
    }
}