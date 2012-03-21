/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.model.TextPM;
import org.beanfabrics.swing.internal.BnPlainDocument;
import org.beanfabrics.swing.internal.TextPMTextField;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class TextPMTextFieldTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TextPMTextFieldTest.class);
    }

    private TextPMTextField field;
    private int refreshCounter = 0;

    @Before
    public void setUp()
        throws Exception {
        this.field = new TextPMTextField() {
            protected void refresh() {
                refreshCounter++;
                super.refresh();
            }
        };
    }

    @Test
    public void testSetDocumentDocument() {
        final BnPlainDocument doc = new BnPlainDocument();
        this.field.setDocument(doc);
        assertEquals("BnPlainDocument", doc, this.field.getDocument());
        final TextPM pM = new TextPM();
        this.field.setPresentationModel(pM);
        try {
            this.field.setDocument(new BnPlainDocument());
            fail("The model could't be changed after a property was applied.");
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testSetPresentationModel() {
        assertEquals("field.getText()", "", this.field.getText());
        final TextPM pM = new TextPM();
        pM.setText("Test");
        this.field.setPresentationModel(pM);
        assertEquals("field.getText()", "Test", this.field.getText());
    }

    @Test
    public void testGetPresentationModel() {
        final TextPM pM = new TextPM();
        this.field.setPresentationModel(pM);
        assertEquals("", pM, this.field.getPresentationModel());
    }

    @Test
    public void testRefresh() {
        assertFalse("field.isEnabled()", this.field.isEnabled());

        final TextPM pM = new TextPM();
        this.field.setPresentationModel(pM);
        assertTrue("field.isEnabled()", this.field.isEnabled());

        final String validationText = "Test error: size > 5";
        pM.getValidator().add(new ValidationRule() {
            public ValidationState validate() {
                if (pM.getText().length() > 4) {
                    return new ValidationState(validationText);
                }
                return null;
            }
        });
        pM.setText("ERROR");
        assertNotNull("pM.getValidationState()", pM.getValidationState());
        assertEquals("field.getToolTipText()", validationText, this.field.getToolTipText());

        int counter = this.refreshCounter;
        pM.setMandatory(true);
        assertEquals("refreshCounter)", ++counter, this.refreshCounter);
    }

    @Test
    public void testSetText() {
        assertEquals("field.getText()", "", this.field.getText());
        final TextPM pM = new TextPM();
        this.field.setPresentationModel(pM);
        this.field.setText("Test");
        assertEquals("pM.getText()", "Test", pM.getText());
        this.field.setText("");
        assertEquals("pM.getText()", "", pM.getText());
    }
}