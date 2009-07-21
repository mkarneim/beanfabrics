/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
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
public class BnCheckBoxTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BnCheckBoxTest.class);
    }

    private GroupModel groupModel;
    private ModelProvider provider;
    private BnCheckBox checkBox;
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
        this.checkBox = new BnCheckBox();
        this.path = new Path("this.selected.active");
        this.groupModel.persons.getSelection().setInterval(0, 0);
    }

    /**
     * Test method for
     * {@link org.beanfabrics.swing.BnCheckBox#getModelProvider()}.
     */
    @Test
    public void testGetModelProvider() {
        this.checkBox.setModelProvider(this.provider);
        assertEquals("checkBox.getLocalProvider()", this.provider, this.checkBox.getModelProvider());
    }

    /**
     * Test method for
     * {@link org.beanfabrics.swing.BnCheckBox#setModelProvider(org.beanfabrics.IModelProvider)}
     * .
     */
    @Test
    public void testSetModelProvider() {
        Path myPath = new Path("this.ready");

        this.checkBox.setModelProvider(this.provider);
        this.checkBox.setPath(myPath);
        assertTrue("checkBox.isConnected()", this.checkBox.isConnected());
    }

    /**
     * Test method for
     * {@link org.beanfabrics.swing.BnCheckBox#setModelProvider(org.beanfabrics.IModelProvider)}
     * .
     */
    @Test
    public void testSetWritable() {
        Path myPath = new Path("this.ready");

        this.checkBox.setModelProvider(this.provider);
        this.checkBox.setPath(myPath);
        groupModel.ready.setEditable(false);
        assertEquals("checkBox.isConnected()", false, this.checkBox.isEnabled());
    }

    /**
     * Test method for
     * {@link org.beanfabrics.swing.BnCheckBox#setModelProvider(org.beanfabrics.IModelProvider)}
     * .
     */
    @Test
    public void testSetModelProvider2() {
        this.checkBox.setModelProvider(this.provider);
        this.checkBox.setPath(this.path);
        GroupModel.PersonModel selectedPerson = groupModel.selected;
        assertNotNull("selectedPerson", selectedPerson);

        assertTrue("checkBox.isConnected()", this.checkBox.isConnected());
    }

    /**
     * Test method for {@link org.beanfabrics.swing.BnCheckBox#getPath()}.
     */
    @Test
    public void testGetPath() {
        this.checkBox.setPath(this.path);
        assertEquals("checkBox.getPath()", this.path, this.checkBox.getPath());
    }

    /**
     * Test method for
     * {@link org.beanfabrics.swing.BnCheckBox#setPath(org.beanfabrics.Path)}.
     */
    @Test
    public void testSetPath() {
        this.checkBox.setModelProvider(this.provider);
        this.checkBox.setPath(this.path);
        assertTrue("checkBox.isConnected()", this.checkBox.isConnected());
    }

    @Test
    public void testGetPresentationModel() {
        final BooleanPM pm = new BooleanPM();
        pm.setBoolean(true);
        this.checkBox.setPresentationModel(pm);
        assertEquals("checkBox.getPresentationModel()", pm, this.checkBox.getPresentationModel());
    }

    @Test
    public void testSetPresentationModel() {
        final BooleanPM pm = new BooleanPM();
        pm.setBoolean(true);
        this.checkBox.setPresentationModel(pm);
        assertTrue("checkBox.isSelected()", this.checkBox.isSelected());
        pm.setBoolean(false);
        assertFalse("checkBox.isSelected()", this.checkBox.isSelected());
        this.checkBox.setSelected(true);
        assertTrue("pm.getBoolean()", pm.getBoolean());
    }

    @Test
    public void testIsConnected() {
        assertFalse("checkBox.isConnected()", this.checkBox.isConnected());
        this.checkBox.setModelProvider(this.provider);
        this.checkBox.setPath(this.path);
        assertTrue("checkBox.isConnected()", this.checkBox.isConnected());
    }
}