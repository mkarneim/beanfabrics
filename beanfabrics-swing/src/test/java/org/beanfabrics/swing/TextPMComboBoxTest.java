/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.Options;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.swing.internal.TextPMComboBox;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;
import org.junit.Before;
import org.junit.Test;

public class TextPMComboBoxTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TextPMComboBoxTest.class);
    }

    private TextPMComboBox combo;
    private MyModel myModel;
    private int refreshCounter = 0;

    @Before
    public void setUp()
        throws Exception {
        this.combo = new TextPMComboBox() {
            protected void refresh() {
                refreshCounter++;
                super.refresh();
            }
        };
        this.myModel = new MyModel();
    }

    @Test
    public void testGetTextCell() {
        this.combo.setPresentationModel(myModel.text);
        assertEquals("combo.getPresentationModel()", myModel.text, this.combo.getPresentationModel());
    }

    @Test
    public void testSetTextCell() {
        assertNull("combo.getSelectedItem()", this.combo.getSelectedItem());
        assertEquals("combo.getItemCount()", 0, this.combo.getItemCount());
        this.combo.setPresentationModel(myModel.text);
        assertEquals("combo.getItemCount()", 2, this.combo.getItemCount());
        assertEquals("combo.getSelectedItem()", "", this.combo.getSelectedItem());
        myModel.text.setText("Second");
        assertEquals("combo.getSelectedItem()", "Second", this.combo.getSelectedItem());
        combo.setSelectedIndex(0);
        assertEquals("myModel.text.getText()", "First", myModel.text.getText());
    }

    @Test
    public void testIsConnected() {
        assertFalse("combo.isConnected()", this.combo.isConnected());
        this.combo.setPresentationModel(myModel.text);
        assertTrue("combo.isConnected()", this.combo.isConnected());
    }

    @Test
    public void testRefresh() {
        assertFalse("combo.isEnabled()", this.combo.isEnabled());

        final TextPM pM = new TextPM();
        this.combo.setPresentationModel(pM);
        assertTrue("combo.isEnabled()", this.combo.isEnabled());

        pM.getValidator().add(new ValidationRule() {
            public ValidationState validate() {
                if (pM.getText().length() > 4)
                    return new ValidationState("Test error: size > 4");
                return null;
            }
        });
        pM.setText("ERROR");
        assertNotNull("pM.getValidationState()" + pM.getValidationState());
        assertEquals("this.combo.getToolTipText()", "Test error: size > 4", this.combo.getToolTipText());

        int counter = this.refreshCounter;
        pM.setMandatory(true);
        assertEquals("refreshCounter", ++counter, this.refreshCounter);
    }

    @Test
    public void testChangeOptions() {
        this.combo.setPresentationModel(this.myModel.text);
        assertEquals("combo.getItemCount()", 2, this.combo.getItemCount());
        this.myModel.text.getOptions().put("Third", "Thrid");
        assertEquals("combo.getItemCount()", 3, this.combo.getItemCount());
        this.myModel.text.getOptions().remove("Second");
        assertEquals("combo.getItemCount()", 2, this.combo.getItemCount());
    }

    private static class MyModel extends AbstractPM {
        private TextPM text = new TextPM();

        public MyModel() {
            PMManager.setup(this);
            final Options options = new Options();
            options.put("First", "First");
            options.put("Second", "Second");
            text.setOptions(options);
        }
    }
}