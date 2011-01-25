/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.BooleanPM;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class BnToggleButtonTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BnToggleButtonTest.class);
    }

    private ModelProvider provider;
    private GroupModel groupModel;
    private BnToggleButton toggleButton;
    private Path path;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp()
        throws Exception {
        this.groupModel = new GroupModel();
        this.provider = new ModelProvider();
        this.provider.setPresentationModel(this.groupModel);
        this.toggleButton = new BnToggleButton();
        this.path = new Path("this.selected.active");
        this.groupModel.persons.getSelection().setInterval(0, 0);
    }

    /**
     * Test method for
     * {@link org.beanfabrics.swing.BnToggleButton#getModelProvider()}.
     */
    @Test
    public void testGetDataSource() {
        this.toggleButton.setModelProvider(this.provider);
        assertEquals("ToggleButton.getLocalProvider()", this.provider, this.toggleButton.getModelProvider());
    }

    /**
     * Test method for
     * {@link org.beanfabrics.swing.BnToggleButton#setModelProvider(org.beanfabrics.IModelProvider)}
     * .
     */
    @Test
    public void testSetDataSource() {
        Path myPath = new Path("this.ready");

        this.toggleButton.setModelProvider(this.provider);
        this.toggleButton.setPath(myPath);
        assertTrue("ToggleButton.isConnected()", this.toggleButton.isConnected());
    }

    /**
     * Test method for
     * {@link org.beanfabrics.swing.BnToggleButton#setModelProvider(org.beanfabrics.IModelProvider)}
     * .
     */
    @Test
    public void testSetDataSource2() {
        this.toggleButton.setModelProvider(this.provider);
        this.toggleButton.setPath(this.path);
        GroupModel.PersonModel selectedPerson = groupModel.selected;
        assertNotNull("selectedPerson", selectedPerson);

        assertTrue("ToggleButton.isConnected()", this.toggleButton.isConnected());
    }

    /**
     * Test method for {@link org.beanfabrics.swing.BnToggleButton#getPath()}.
     */
    @Test
    public void testGetPath() {
        this.toggleButton.setPath(this.path);
        assertEquals("ToggleButton.getPath()", this.path, this.toggleButton.getPath());
    }

    /**
     * Test method for
     * {@link org.beanfabrics.swing.BnToggleButton#setPath(org.beanfabrics.Path)}
     * .
     */
    @Test
    public void testSetPath() {
        this.toggleButton.setModelProvider(this.provider);
        this.toggleButton.setPath(this.path);
        assertTrue("ToggleButton.isConnected()", this.toggleButton.isConnected());
    }

    /**
     * Test method for
     * {@link org.beanfabrics.swing.internal.BooleanPMToggleButton#getPresentationModel()}
     * .
     */
    @Test
    public void testGetBooleanCell() {
        final BooleanPM cell = new BooleanPM();
        cell.setBoolean(true);
        this.toggleButton.setPresentationModel(cell);
        assertEquals("ToggleButton.getBooleanCell()", cell, this.toggleButton.getPresentationModel());
    }

    /**
     * Test method for
     * {@link org.beanfabrics.swing.internal.BooleanPMToggleButton#setPresentationModel(org.beanfabrics.IBooleanEditor)}
     * .
     */
    @Test
    public void testSetBooleanCell() {
        final BooleanPM cell = new BooleanPM();
        cell.setBoolean(true);
        this.toggleButton.setPresentationModel(cell);
        assertTrue("ToggleButton.isSelected()", this.toggleButton.isSelected());
        cell.setBoolean(false);
        assertFalse("ToggleButton.isSelected()", this.toggleButton.isSelected());
        this.toggleButton.setSelected(true);
        assertTrue("editor.getBoolean()", cell.getBoolean());
    }

    /**
     * Test method for
     * {@link org.beanfabrics.swing.internal.BooleanPMToggleButton#isConnected()}
     * .
     */
    @Test
    public void testIsConnected() {
        assertFalse("ToggleButton.isConnected()", this.toggleButton.isConnected());
        this.toggleButton.setModelProvider(this.provider);
        this.toggleButton.setPath(this.path);
        assertTrue("ToggleButton.isConnected()", this.toggleButton.isConnected());
    }
}