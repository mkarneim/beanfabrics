/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.table;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.swing.table.DefaultTableModel;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.DatePM;
import org.beanfabrics.model.MapPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
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
	public void setUp() throws Exception {
		this.table = new BnTable();
		this.groupModel = new GroupModel();
		this.provider = new ModelProvider();
		this.def =  new BnColumn[] {
						new BnColumn(new Path("name"), "Name")
						, new BnColumn(new Path("date"), "Date")};
	}

	/**
	 * Test method for {@link org.beanfabrics.swing.table.BnTable#getModelProvider()}.
	 */
	@Test
	public void testGetModelProvider() {
		this.table.setModelProvider( provider);
		assertEquals( "table.getModelProvider()", provider, this.table.getModelProvider());
	}

	/**
	 * Test method for {@link BnTable#setModelProvider(org.beanfabrics.IModelProvider)}.
	 */
	@Test
	public void testSetModelProvider() {
		this.table.setPath(new Path("this.persons"));
		this.table.setColumns( this.def);
		this.provider.setPresentationModel( this.groupModel);
		assertEquals( "table.getRowCount()", 0, table.getRowCount());
		assertEquals( "table.getColumnCount()", def.length, table.getColumnCount());
		this.table.setModelProvider( provider);
		assertEquals( "table.getRowCount()", this.groupModel.persons.size(), table.getRowCount());
		assertEquals( "table.getColumnCount()", this.def.length , table.getColumnCount());
	}

	/**
	 * Test method for {@link BnTable#getPath()}.
	 */
	@Test
	public void testGetPath() {
		final Path path = new Path( "this.persons");
		this.table.setPath( path);
		assertEquals( "this.table.getPath()", path, this.table.getPath());
	}

	/**
	 * Test method for {@link org.beanfabrics.swing.table.BnTable#setPath(org.beanfabrics.Path)}.
	 */
	@Test
	public void testSetPath() {
		this.table.setModelProvider( provider);
		this.table.setColumns( this.def);
		this.provider.setPresentationModel( this.groupModel);
		assertEquals( "table.getRowCount()", 0, table.getRowCount());
		assertEquals( "table.getColumnCount()", this.def.length, table.getColumnCount());
		this.table.setPath(new Path("this.persons"));
		assertEquals( "table.getRowCount()", this.groupModel.persons.size(), table.getRowCount());
		assertEquals( "table.getColumnCount()", this.def.length , table.getColumnCount());
	}


	/**
	 * Test method for {@link org.beanfabrics.swing.table.BnTable#getTableColumnDefinitions()}.
	 */
	@Test
	public void testgetColumns() {
		this.table.setColumns( this.def);
		assertEquals( "table.getColumns()", this.def, this.table.getColumns());
	}

	/**
	 * Test method for {@link org.beanfabrics.swing.table.BnTable#setTableColumnDefinitions(org.beanfabrics.swing.table.TableColumnDefinition[])}.
	 */
	@Test
	public void testSetColumns() {
		this.table.setModelProvider( provider);
		this.table.setPath(new Path("this.persons"));
		this.provider.setPresentationModel( this.groupModel);
		assertEquals( "table.getRowCount()", 2, table.getRowCount());
		assertEquals( "table.getColumnCount()", 0, table.getColumnCount());
		this.table.setColumns( this.def);
		assertEquals( "table.getRowCount()", this.groupModel.persons.size(), table.getRowCount());
		assertEquals( "table.getColumnCount()", this.def.length , table.getColumnCount());
	}

	@Test
	public void testRemoveRows() {
		setup();
		assertEquals( "table.getRowCount()", this.groupModel.persons.size(), table.getRowCount());
		assertEquals( "table.getColumnCount()", this.def.length , table.getColumnCount());
		this.groupModel.persons.clear();
		assertEquals( "table.getRowCount()", 0, table.getRowCount());
		assertEquals( "table.getColumnCount()", this.def.length , table.getColumnCount());
	}

	@Test
	public void testAddRow() {
		setup();
		assertEquals( "table.getRowCount()", this.groupModel.persons.size(), table.getRowCount());
		assertEquals( "table.getColumnCount()", this.def.length , table.getColumnCount());
		PersonModel pModel = new PersonModel();
		pModel.name.setText("Tester");
		pModel.date.setDate( new Date( System.currentTimeMillis()));
		this.groupModel.persons.put("pModel", pModel);
		assertEquals( "table.getRowCount()", 3, table.getRowCount());
		assertEquals( "table.getColumnCount()", this.def.length , table.getColumnCount());
	}

	@Test
	public void testDeleteModel() {
		setup();
		assertEquals( "table.getRowCount()", this.groupModel.persons.size(), table.getRowCount());
		assertEquals( "table.getColumnCount()", this.def.length , table.getColumnCount());
		this.table.setModel( new DefaultTableModel());
		assertEquals( "table.getRowCount()", 0, table.getRowCount());
		assertEquals( "table.getColumnCount()", 0 , table.getColumnCount());
	}

	@Test
	public void testAddSelection() {
		setup();
		this.table.selectAll();
		assertTrue("addSelection", this.groupModel.persons.getSelection().containsAll(
				this.groupModel.persons.toCollection()));
	}

	@Test
	public void testClearSelection() {
		setup();
		this.table.selectAll();
		assertTrue("addSelection", this.groupModel.persons.getSelection().containsAll(
				this.groupModel.persons.toCollection()));
		this.table.clearSelection();
		assertEquals("groupModel.persons.getSelection().size()", 0, this.groupModel.persons.getSelection().size());
	}

	@Test
	public void testEditorAddSelection() {
		this.setup();
		assertEquals("table.getSelectedRowCount()", 0,this.table.getSelectedRowCount());
		this.groupModel.persons.getSelection().addInterval( 0, 1);
		assertEquals("table.getSelectedRowCount()", 2, this.table.getSelectedRowCount());
	}

	@Test
	public void testEditorRemoveSelection() {
		this.setup();
		assertTrue("table.getSelectedRowCount()==0", this.table.getSelectedRowCount()==0);
		this.groupModel.persons.getSelection().addInterval( 0, 1);
		assertTrue("table.getSelectedRowCount()==0", this.table.getSelectedRowCount()==2);
		this.groupModel.persons.getSelection().removeInterval(0, 1);
		assertTrue("table.getSelectedRowCount()==0", this.table.getSelectedRowCount()==0);
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
		this.table.setModelProvider( provider);
		this.table.setPath(new Path("this.persons"));
		this.provider.setPresentationModel( this.groupModel);
		this.table.setColumns( this.def);
	}

	private static class PersonModel extends AbstractPM {
		protected final TextPM name = new TextPM();
		protected final DatePM date = new DatePM();
		public PersonModel() {
			PMManager.setup(this);
			this.name.getValidator().add( new ValidationRule() {
				public ValidationState validate() {
					if ( name.getText().length() < 4) {
						return new ValidationState("The name has to have 4 characters at least");
					}
					return null;
				}
			});

		}
	}

	private static class GroupModel extends AbstractPM {
		protected final MapPM<String, PersonModel> persons = new MapPM();
		public GroupModel() {
			PMManager.setup(this);
			this.populate();
		}
		private void populate() {
			PersonModel pModel1 = new PersonModel();
			pModel1.name.setText("Michael Karneim");
			pModel1.date.setDate( new Date( System.currentTimeMillis()));
			this.persons.put( "pModel1", pModel1);
			PersonModel pModel2 = new PersonModel();
			pModel2.name.setText("Michael Karneim");
			pModel2.date.setDate( new Date( System.currentTimeMillis()));
			this.persons.put( "pModel2", pModel2);
		}
	}
}