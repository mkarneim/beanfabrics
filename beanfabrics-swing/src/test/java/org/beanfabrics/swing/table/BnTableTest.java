/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.table;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.event.ElementChangedEvent;
import org.beanfabrics.event.ElementsAddedEvent;
import org.beanfabrics.event.ElementsDeselectedEvent;
import org.beanfabrics.event.ElementsRemovedEvent;
import org.beanfabrics.event.ElementsReplacedEvent;
import org.beanfabrics.event.ElementsSelectedEvent;
import org.beanfabrics.event.ListEvent;
import org.beanfabrics.event.ListListener;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.DatePM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.MapPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Property;
import org.beanfabrics.support.PropertySupport;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class BnTableTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BnTableTest.class);
    }

    private ModelProvider provider;
    private BnTable table;
    private GroupModel groupModel;
    private BnColumn[] def;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp()
        throws Exception {
        this.table = new BnTable();
        this.groupModel = new GroupModel();
        this.provider = new ModelProvider();
        this.def = new BnColumn[] { new BnColumn(new Path("name"), "Name"), new BnColumn(new Path("date"), "Date") };
    }

    /**
     * Test method for
     * {@link org.beanfabrics.swing.table.BnTable#getModelProvider()}.
     */
    @Test
    public void testGetModelProvider() {
        this.table.setModelProvider(provider);
        assertEquals("table.getModelProvider()", provider, this.table.getModelProvider());
    }

    /**
     * Test method for
     * {@link BnTable#setModelProvider(org.beanfabrics.IModelProvider)}.
     */
    @Test
    public void testSetModelProvider() {
        this.table.setPath(new Path("this.persons"));
        this.table.setColumns(this.def);
        this.provider.setPresentationModel(this.groupModel);
        assertEquals("table.getRowCount()", 0, table.getRowCount());
        this.table.setModelProvider(provider);
        assertEquals("table.getColumnCount()", def.length, table.getColumnCount());
        assertEquals("table.getRowCount()", this.groupModel.persons.size(), table.getRowCount());
        assertEquals("table.getColumnCount()", this.def.length, table.getColumnCount());
    }

    /**
     * Test method for {@link BnTable#getPath()}.
     */
    @Test
    public void testGetPath() {
        final Path path = new Path("this.persons");
        this.table.setPath(path);
        assertEquals("this.table.getPath()", path, this.table.getPath());
    }

    /**
     * Test method for
     * {@link org.beanfabrics.swing.table.BnTable#setPath(org.beanfabrics.Path)}
     * .
     */
    @Test
    public void testSetPath() {
        this.table.setModelProvider(provider);
        this.table.setColumns(this.def);
        this.provider.setPresentationModel(this.groupModel);
        assertEquals("table.getRowCount()", 0, table.getRowCount());
        assertEquals("table.getColumnCount()", 0, table.getColumnCount());
        this.table.setPath(new Path("this.persons"));
        assertEquals("table.getRowCount()", this.groupModel.persons.size(), table.getRowCount());
        assertEquals("table.getColumnCount()", this.def.length, table.getColumnCount());
    }

    /**
     * Test method for
     * {@link org.beanfabrics.swing.table.BnTable#getTableColumnDefinitions()}.
     */
    @Test
    public void testgetColumns() {
        this.table.setColumns(this.def);
        assertArrayEquals("table.getColumns()", this.def, this.table.getColumns());
    }

    /**
     * Test method for
     * {@link org.beanfabrics.swing.table.BnTable#setTableColumnDefinitions(org.beanfabrics.swing.table.TableColumnDefinition[])}
     * .
     */
    @Test
    public void testSetColumns() {
        this.table.setModelProvider(provider);
        this.table.setPath(new Path("this.persons"));
        this.provider.setPresentationModel(this.groupModel);
        assertEquals("table.getRowCount()", 2, table.getRowCount());
        assertEquals("table.getColumnCount()", 0, table.getColumnCount());
        this.table.setColumns(this.def);
        assertEquals("table.getRowCount()", this.groupModel.persons.size(), table.getRowCount());
        assertEquals("table.getColumnCount()", this.def.length, table.getColumnCount());
    }

    @Test
    public void testRemoveRows() {
        setup();
        assertEquals("table.getRowCount()", this.groupModel.persons.size(), table.getRowCount());
        assertEquals("table.getColumnCount()", this.def.length, table.getColumnCount());
        this.groupModel.persons.clear();
        assertEquals("table.getRowCount()", 0, table.getRowCount());
        assertEquals("table.getColumnCount()", this.def.length, table.getColumnCount());
    }

    @Test
    public void testAddRow() {
        setup();
        assertEquals("table.getRowCount()", this.groupModel.persons.size(), table.getRowCount());
        assertEquals("table.getColumnCount()", this.def.length, table.getColumnCount());
        PersonModel pModel = new PersonModel();
        pModel.name.setText("Tester");
        pModel.date.setDate(new Date());
        this.groupModel.persons.put("pModel", pModel);
        assertEquals("table.getRowCount()", 3, table.getRowCount());
        assertEquals("table.getColumnCount()", this.def.length, table.getColumnCount());
    }

    @Test
    public void testDeleteModel() {
        setup();
        assertEquals("table.getRowCount()", this.groupModel.persons.size(), table.getRowCount());
        assertEquals("table.getColumnCount()", this.def.length, table.getColumnCount());
        this.table.setPresentationModel(null);
        assertEquals("table.getRowCount()", 0, table.getRowCount());
        assertEquals("table.getColumnCount()", 0, table.getColumnCount());
    }

    @Test
    public void testAddSelection() {
        setup();
        this.table.selectAll();
        assertTrue("addSelection", this.groupModel.persons.getSelection().containsAll(this.groupModel.persons.toCollection()));
    }

    @Test
    public void testClearSelection() {
        setup();
        this.table.selectAll();
        assertTrue("addSelection", this.groupModel.persons.getSelection().containsAll(this.groupModel.persons.toCollection()));
        this.table.clearSelection();
        assertEquals("groupModel.persons.getSelection().size()", 0, this.groupModel.persons.getSelection().size());
    }

    @Test
    public void testEditorAddSelection() {
        this.setup();
        assertEquals("table.getSelectedRowCount()", 0, this.table.getSelectedRowCount());
        this.groupModel.persons.getSelection().addInterval(0, 1);
        assertEquals("table.getSelectedRowCount()", 2, this.table.getSelectedRowCount());
    }

    @Test
    public void testEditorRemoveSelection() {
        this.setup();
        assertTrue("table.getSelectedRowCount()==0", this.table.getSelectedRowCount() == 0);
        this.groupModel.persons.getSelection().addInterval(0, 1);
        assertTrue("table.getSelectedRowCount()==0", this.table.getSelectedRowCount() == 2);
        this.groupModel.persons.getSelection().removeInterval(0, 1);
        assertTrue("table.getSelectedRowCount()==0", this.table.getSelectedRowCount() == 0);
    }

    @Test
    public void multiplePutsWithSelectionAndBnTableModel() {
        MapPM<Integer, PersonModel> map = new MapPM<Integer, PersonModel>();
        PersonModel[] elems = populate(map, 10);
        map.getSelection().addAll();

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        List<BnColumn> cols = new ArrayList<BnColumn>();
        cols.add(new BnColumn(new Path("id"), "ID"));
        BnTableModel model = new BnTableModel(map, cols, true);

        map.put(1, elems[1]);

        assertEquals("counter.elementsAdded", 0, counter.elementsAdded);
        assertEquals("counter.elementChanged", 0, counter.elementChanged);
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("counter.elementsSelected", 0, counter.elementsSelected);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
    }

    @Test
    public void multiplePutsWithSelectionAndBnTable() {
        MapPM<Integer, PersonModel> map = new MapPM<Integer, PersonModel>();
        PersonModel[] elems = populate(map, 10);
        map.getSelection().addAll();

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        List<BnColumn> cols = new ArrayList<BnColumn>();
        cols.add(new BnColumn(new Path("id"), "ID"));
        BnTable table = new BnTable();
        table.setPresentationModel(map);
        table.setColumns(cols.toArray(new BnColumn[cols.size()]));

        map.put(1, elems[1]);

        assertEquals("counter.elementsAdded", 0, counter.elementsAdded);
        assertEquals("counter.elementChanged", 0, counter.elementChanged);
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("counter.elementsSelected", 0, counter.elementsSelected);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
    }

    private PersonModel[] populate(MapPM<Integer, PersonModel> map, int number) {
        PersonModel[] elems = new PersonModel[number];
        for (int i = 0; i < elems.length; ++i) {
            elems[i] = new PersonModel();
            elems[i].name.setText("Tester");
            elems[i].date.setDate(new Date());
            map.put(i, elems[i]);
        }
        assertEquals("map.size()", number, map.size());
        return elems;
    }

    @Test
    public void addressable() {
        ListPM<AddressablePM> list = new ListPM<AddressablePM>();
        for (int i = 0; i < 5; ++i) {
            PersonPM person = new PersonPM();
            person.getName().setText("person " + i);
            person.getAddress().getStreet().setText("street of person " + i);
            list.add(person);
        }
        for (int i = 0; i < 5; ++i) {
            CompanyPM company = new CompanyPM();
            company.getName().setText("company " + i);
            company.getAddress().getStreet().setText("street of company " + i);
            list.add(company);
        }
        List<BnColumn> colDefs = new LinkedList<BnColumn>();
        colDefs.add(new BnColumn(new Path("address.street"), "Street"));
        colDefs.add(new BnColumn(new Path("address.city.name"), "City"));

        BnTableModel tableModel = new BnTableModel(list, colDefs, true);

        assertEquals("tableModel.getRowCount()", 10, tableModel.getRowCount());
    }

    @Test
    public void reconnect() {
        this.setup();
        this.groupModel.persons.getSelection().setInterval(0, 0);
        assertEquals("this.table.getSelectedRowCount()", 1, this.table.getSelectedRowCount());
        this.provider.setPresentationModel(null);
        assertEquals("this.table.getSelectedRowCount()", 0, this.table.getSelectedRowCount());
        assertEquals("this.groupEd.persons.getSelection().size()", 1, this.groupModel.persons.getSelection().size());
        this.provider.setPresentationModel(this.groupModel);
        assertEquals("this.groupEd.persons.getSelection().size()", 1, this.groupModel.persons.getSelection().size());
        assertEquals("this.table.getSelectedRowCount()", 1, this.table.getSelectedRowCount());
    }

    private void setup() {
        this.table.setModelProvider(provider);
        this.table.setPath(new Path("this.persons"));
        this.provider.setPresentationModel(this.groupModel);
        this.table.setColumns(this.def);
    }

    private static class PersonModel extends AbstractPM {
        protected final TextPM name = new TextPM();
        protected final DatePM date = new DatePM();

        public PersonModel() {
            PMManager.setup(this);
            this.name.getValidator().add(new ValidationRule() {
                public ValidationState validate() {
                    if (name.getText().length() < 4) {
                        return new ValidationState("The name has to have 4 characters at least");
                    }
                    return null;
                }
            });

        }
    }

    private static class GroupModel extends AbstractPM {
        protected final MapPM<String, PersonModel> persons = new MapPM<String, PersonModel>();

        public GroupModel() {
            PMManager.setup(this);
            this.populate();
        }

        private void populate() {
            PersonModel pModel1 = new PersonModel();
            pModel1.name.setText("Michael Karneim");
            pModel1.date.setDate(new Date(System.currentTimeMillis()));
            this.persons.put("pModel1", pModel1);
            PersonModel pModel2 = new PersonModel();
            pModel2.name.setText("Michael Karneim");
            pModel2.date.setDate(new Date(System.currentTimeMillis()));
            this.persons.put("pModel2", pModel2);
        }
    }

    private static class EventCounter implements ListListener {
        int elementChanged;
        int elementsReplaced;
        int elementsAdded;
        int elementsDeselected;
        int elementsRemoved;
        int elementsSelected;
        List<ListEvent> events = new LinkedList<ListEvent>();

        public void elementChanged(ElementChangedEvent evt) {
            events.add(evt);
            elementChanged++;
        }

        public void elementsReplaced(ElementsReplacedEvent evt) {
            events.add(evt);
            elementsReplaced++;
        }

        public void elementsAdded(ElementsAddedEvent evt) {
            events.add(evt);
            elementsAdded++;
        }

        public void elementsDeselected(ElementsDeselectedEvent evt) {
            events.add(evt);
            elementsDeselected++;
        }

        public void elementsRemoved(ElementsRemovedEvent evt) {
            events.add(evt);
            elementsRemoved++;
        }

        public void elementsSelected(ElementsSelectedEvent evt) {
            events.add(evt);
            elementsSelected++;
        }
    }

    private interface AddressablePM extends PresentationModel {
        @Property
        AddressPM getAddress();
    }

    private static class CompanyPM extends AbstractPM implements AddressablePM {
        @Property
        private TextPM name = new TextPM();
        @Property
        PersonPM owner = new PersonPM();
        @Property
        AddressPM address = new AddressPM();

        public CompanyPM() {
            PMManager.setup(this);
        }

        public AddressPM getAddress() {
            return address;
        }

        public TextPM getName() {
            return name;
        }
    }

    private static class PersonPM extends AbstractPM implements AddressablePM {
        private TextPM name = new TextPM();
        private TextPM phone = new TextPM();
        @Property
        private AddressPM address = new AddressPM();

        private TimeSpanPM timespan;
        @Property
        private IntegerPM height = new IntegerPM();

        public PersonPM() {
            PMManager.setup(this);
        }

        @Property
        public TextPM getName() {
            return name;
        }

        @Property
        public TextPM getPhone() {
            return phone;
        }

        public AddressPM getAddress() {
            return address;
        }

        @Property
        public TimeSpanPM getTimespan() {
            return timespan;
        }

        public void setTimespan(TimeSpanPM newValue) {
            if (equals(this.timespan, newValue)) {
                return;
            }
            this.timespan = newValue;
            PropertySupport.get(this).refresh();
        }
    }

    private static interface TimeSpanPM extends PresentationModel {
        @Property
        public DatePM getStart();

        @Property
        public DatePM getEnd();
    }

    private static class AddressPM extends AbstractPM {
        @Property
        private TextPM street = new TextPM();
        @Property
        CityPM city = new CityPM();

        public AddressPM() {
            PMManager.setup(this);
        }

        public TextPM getStreet() {
            return street;
        }
    }

    private static class CityPM extends AbstractPM {
        TextPM name = new TextPM();

        public CityPM() {
            PMManager.setup(this);
        }
    }
}
