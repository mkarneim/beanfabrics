/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.list;

import static org.junit.Assert.assertEquals;

import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Property;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Max Gensthaler
 */
public class BnListTest {
    private ModelProvider provider;
    private BnList list;
    private GroupModel model;

    @Before
    public void setUp()
        throws Exception {
        this.list = new BnList();
        this.model = new GroupModel();
        this.provider = new ModelProvider();
    }

    @Test
    public void testSetDataSource() {
        this.provider.setPresentationModel(this.model);
        this.list.setPath(new Path("this.things"));
        assertEquals("listCell.getVisibleRowCount()", 0, list.getModel().getSize());
        this.list.setModelProvider(provider);
        this.list.setCellConfig(new CellConfig(new Path("this")));
        assertEquals("listCell.getVisibleRowCount()", this.model.things.size(), list.getModel().getSize());
    }

    @Test
    public void testGetDataSource() {
        this.list.setModelProvider(provider);
        assertEquals("listCell.getLocalProvider()", provider, this.list.getModelProvider());
    }

    @Test
    public void testSetPath() {
        this.provider.setPresentationModel(this.model);
        this.list.setModelProvider(provider);
        assertEquals("listCell.getVisibleRowCount()", 0, list.getModel().getSize());
        this.list.setPath(new Path("this.things"));
        this.list.setCellConfig(new CellConfig(new Path("this")));
        assertEquals("listCell.getVisibleRowCount()", this.model.things.size(), list.getModel().getSize());
    }

    @Test
    public void testGetPath() {
        Path path = new Path("this.things");
        this.list.setPath(path);
        assertEquals("listCell.getPath()", path, this.list.getPath());
    }

    @Test
    public void testAddEntry() {
        setup();
        int listModelSize = list.getModel().getSize();
        // this.groupEd.things.add(new ThingModel());
        this.model.things.add(new TextPM());
        assertEquals("addEntry ModelSize", listModelSize + 1, list.getModel().getSize());
    }

    @Test
    public void testRemoveEntry() {
        setup();
        int listModelSize = list.getModel().getSize();
        this.model.things.removeAt(this.model.things.size() - 1);
        assertEquals("removeEntry ModelSize", listModelSize - 1, list.getModel().getSize());
    }

    @Test
    public void testDeleteModel() {
        setup();
        assertEquals("listCell.getVisibleRowCount()", this.model.things.size(), list.getModel().getSize());
        this.list.setModel(new DefaultListModel());
        assertEquals("listCell.getVisibleRowCount()", 0, list.getModel().getSize());
    }

    @Test
    public void testSetSelectionOnList() {
        setup();
        this.list.setSelectedIndices(new int[] {});
        assertEquals("setSelectionOnList to none", 0, this.model.things.getSelection().size());
        this.list.setSelectionInterval(0, this.list.getModel().getSize() - 1);
        assertEquals("setSelectionOnList to all", this.model.things.size(), this.model.things.getSelection().size());
        this.list.setSelectedIndices(new int[] {});
        assertEquals("setSelectionOnList to none", 0, this.model.things.getSelection().size());
    }

    @Test
    public void testSetSelectionOnModel() {
        setup();
        this.model.things.getSelection().clear();
        assertEquals("setSelectionOnModel to none", 0, this.list.getSelectedIndices().length);
        this.model.things.getSelection().setInterval(0, this.model.things.size() - 1);
        assertEquals("setSelectionOnModel to all", this.model.things.size(), this.list.getSelectedIndices().length);
        this.model.things.getSelection().clear();
        assertEquals("setSelectionOnModel to none", 0, this.list.getSelectedIndices().length);
    }

    @Test
    public void testSetSelectionOnListWithSingleSel() {
        setup();
        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.list.setSelectedIndices(new int[] {});
        assertEquals("setSelectionOnList to none", 0, this.model.things.getSelection().size());
        this.list.setSelectionInterval(0, this.list.getModel().getSize() - 1);
        assertEquals("setSelectionOnList to all selCount", 1, this.model.things.getSelection().size());
        assertEquals("setSelectionOnList to all selElem", this.model.things.getAt(this.model.things.size() - 1), this.model.things.getSelection().getFirst());
        this.list.setSelectedIndices(new int[] {});
        assertEquals("setSelectionOnList to none", 0, this.model.things.getSelection().size());
    }

    @Test
    public void testSetSelectionOnModelWithSingleSel() {
        setup();
        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.model.things.getSelection().clear();
        assertEquals("setSelectionOnModel to none", 0, this.list.getSelectedIndices().length);
        this.model.things.getSelection().setInterval(0, this.model.things.size() - 1);
        // More then one entries were selected in model -> more then one have to
        // be displayed as selected in the listCell => override single selection on
        // listCell
        assertEquals("setSelectionOnModel to all selCount", 10, this.list.getSelectedIndices().length);
        this.model.things.getSelection().clear();
        assertEquals("setSelectionOnModel to none", 0, this.list.getSelectedIndices().length);
    }

    @Test
    public void testAddSelectionOnList() {
        setup();
        this.list.setSelectedIndices(new int[] {});
        assertEquals("setSelectionOnList to none", 0, this.model.things.getSelection().size());
        assertEquals("listSize", 10, this.list.getModel().getSize());
        this.list.addSelectionInterval(2, 4);
        assertEquals("addSelectionOnList from 2 to 4", 3, this.model.things.getSelection().size());
        this.list.addSelectionInterval(3, 7);
        assertEquals("addSelectionOnList from 3 to 7", 6, this.model.things.getSelection().size());
        this.list.addSelectionInterval(1, 3);
        assertEquals("addSelectionOnList from 1 to 3", 7, this.model.things.getSelection().size());
    }

    @Test
    public void testAddSelectionOnModel() {
        setup();
        this.model.things.getSelection().clear();
        assertEquals("setSelectionOnModel to none", 0, this.list.getSelectedIndices().length);
        assertEquals("modelSize", 10, this.model.things.size());
        this.model.things.getSelection().addInterval(2, 4);
        assertEquals("setSelectionOnModel from 2 to 4", 3, this.list.getSelectedIndices().length);
        this.model.things.getSelection().addInterval(3, 7);
        assertEquals("setSelectionOnModel from 3 to 7", 6, this.list.getSelectedIndices().length);
        this.model.things.getSelection().addInterval(1, 3);
        assertEquals("setSelectionOnModel from 1 to 3", 7, this.list.getSelectedIndices().length);
    }

    @Test
    public void testAddSelectionOnListWithSingleInterval() {
        setup();
        this.list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.list.setSelectedIndices(new int[] {});
        assertEquals("setSelectionOnList to none", 0, this.model.things.getSelection().size());
        assertEquals("listSize", 10, this.list.getModel().getSize());
        this.list.addSelectionInterval(4, 7);
        assertEquals("addSelectionOnList from 4 to 7", 4, this.model.things.getSelection().size());
        this.list.addSelectionInterval(1, 3);
        assertEquals("addSelectionOnList from 1 to 3", 3, this.model.things.getSelection().size());
    }

    @Test
    public void testAddSelectionOnModelWithSingleInterval() {
        setup();
        this.list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.model.things.getSelection().clear();
        assertEquals("setSelectionOnModel to none", 0, this.list.getSelectedIndices().length);
        assertEquals("modelSize", 10, this.model.things.size());
        this.model.things.getSelection().addInterval(4, 7);
        assertEquals("setSelectionOnModel from 4 to 7", 4, this.list.getSelectedIndices().length);
        this.model.things.getSelection().addInterval(1, 3);
        // More then one entries were selected in model -> more then one have to
        // be displayed as selected in the listCell => override single selection on
        // listCell
        assertEquals("setSelectionOnModel from 1 to 3", 7, this.list.getSelectedIndices().length);
    }

    @Test
    public void testRemoveSelectionOnList() {
        setup();
        assertEquals("listSize", 10, this.list.getModel().getSize());
        this.list.setSelectionInterval(0, this.list.getModel().getSize() - 1);
        assertEquals("setSelectionOnList to all", this.model.things.size(), this.model.things.getSelection().size());
        this.list.removeSelectionInterval(2, 5);
        assertEquals("removeSelectionOnList from 2 to 5", 6, this.model.things.getSelection().size());
    }

    @Test
    public void testRemoveSelectionOnModel() {
        setup();
        assertEquals("modelSize", 10, this.model.things.size());
        this.model.things.getSelection().setInterval(0, this.model.things.size() - 1);
        assertEquals("setSelectionOnModel to all", this.model.things.size(), this.list.getSelectedIndices().length);
        this.model.things.getSelection().removeInterval(2, 5);
        assertEquals("removeSelectionOnModel from 2 to 5", 6, this.list.getSelectedIndices().length);
    }

    @Test
    public void testClearSelectionOnList() {
        setup();
        this.list.setSelectionInterval(0, this.list.getModel().getSize() - 1);
        assertEquals("setSelectionOnList to all", this.model.things.size(), this.model.things.getSelection().size());
        this.list.clearSelection();
        assertEquals("clearSelectionOnList", 0, this.model.things.getSelection().size());
    }

    @Test
    public void testClearSelectionOnModel() {
        setup();
        this.model.things.getSelection().setInterval(0, this.model.things.size() - 1);
        assertEquals("setSelectionOnModel to all", this.model.things.size(), this.list.getSelectedIndices().length);
        this.model.things.getSelection().clear();
        assertEquals("clearSelectionOnModel", 0, this.list.getSelectedIndices().length);
    }

    @Test
    public void testSelectionOnReconnect() {
        setup();
        assertEquals("modelSize", 10, this.model.things.size());
        this.model.things.getSelection().setInterval(2, 4);
        assertEquals("model.things.getSelection().size()", 3, this.model.things.getSelection().size());
        assertEquals("listCell.getSelectedIndices().length", 3, this.list.getSelectedIndices().length);
        this.provider.setPresentationModel(null);
        assertEquals("model.things.getSelection().size()", 3, this.model.things.getSelection().size());
        assertEquals("listCell.getSelectedIndices().length", 0, this.list.getSelectedIndices().length);
        this.provider.setPresentationModel(this.model);
        assertEquals("model.things.getSelection().size()", 3, this.model.things.getSelection().size());
        assertEquals("listCell.getSelectedIndices().length", 3, this.list.getSelectedIndices().length);
    }

    @Test
    public void testIsSelectedIndex() {
        setup();
        assertEquals("modelSize", 10, this.model.things.size());
        this.model.things.getSelection().setInterval(2, 4);
        assertEquals("listCell.isSelectedIndex(int)", false, this.list.isSelectedIndex(0));
        assertEquals("listCell.isSelectedIndex(int)", false, this.list.isSelectedIndex(1));
        assertEquals("listCell.isSelectedIndex(int)", true, this.list.isSelectedIndex(2));
        assertEquals("listCell.isSelectedIndex(int)", true, this.list.isSelectedIndex(3));
        assertEquals("listCell.isSelectedIndex(int)", true, this.list.isSelectedIndex(4));
        assertEquals("listCell.isSelectedIndex(int)", false, this.list.isSelectedIndex(5));
        assertEquals("listCell.isSelectedIndex(int)", false, this.list.isSelectedIndex(6));
        assertEquals("listCell.isSelectedIndex(int)", false, this.list.isSelectedIndex(7));
        assertEquals("listCell.isSelectedIndex(int)", false, this.list.isSelectedIndex(8));
        assertEquals("listCell.isSelectedIndex(int)", false, this.list.isSelectedIndex(9));
    }

    @Test
    public void testIsSelectionEmpty() {
        setup();
        assertEquals("modelSize", 10, this.model.things.size());
        assertEquals("listCell.isSelectionEmpty()", true, this.list.isSelectionEmpty());
        this.model.things.getSelection().setInterval(2, 4);
        assertEquals("listCell.isSelectionEmpty()", false, this.list.isSelectionEmpty());
    }

    private void setup() {
        this.list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.provider.setPresentationModel(this.model);
        this.list.setModelProvider(provider);
        this.list.setPath(new Path("this.things"));
        this.list.setCellConfig(new CellConfig(new Path("this")));
    }

    private static class GroupModel extends AbstractPM {
        @Property
        private final ListPM<TextPM> things = new ListPM<TextPM>();

        public GroupModel() {
            PMManager.setup(this);
            this.initThings();
        }

        private void initThings() {
            for (int i = 0; i < 10; i++) {
                TextPM thing = new TextPM();
                thing.setText("Thing " + i);
                this.things.add(thing);
            }
        }
    }
}