/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.swing.text.BadLocationException;
import javax.swing.undo.UndoManager;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.model.TextPM;
import org.beanfabrics.swing.internal.BnPlainDocument;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class BnPlainDocumentTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BnPlainDocumentTest.class);
    }

    private BnPlainDocument doc = null;

    @Before
    public void setUp()
        throws Exception {
        this.doc = new BnPlainDocument();
    }

    @Test
    public void testRemove()
        throws BadLocationException {
        final TextPM pM = new TextPM();
        this.doc.setPresentationModel(pM);
        this.doc.insertString(0, "Test", null);
        assertEquals("doc.getLength()", 4, this.doc.getLength());
        this.doc.remove(0, 4);
        assertEquals("doc.getLength()", 0, this.doc.getLength());
        assertEquals("pM.getText()", "", pM.getText());
    }

    @Test
    public void testSetPresentationModel()
        throws BadLocationException {
        final TextPM pM = new TextPM();
        pM.setText("Test");
        this.doc.setPresentationModel(pM);
        assertTrue("doc.isConnected()", this.doc.isConnected());
        assertEquals("doc.getText( 0, doc.getLength())", "Test", this.doc.getText(0, doc.getLength()));
    }

    @Test
    public void testGetPresentationModel() {
        final TextPM pM = new TextPM();
        this.doc.setPresentationModel(pM);
        assertEquals("doc.getPresentationModel()", pM, this.doc.getPresentationModel());
    }

    @Test
    public void testIsConnected() {
        assertFalse("doc.isConnected()", this.doc.isConnected());
        final TextPM pM = new TextPM();
        this.doc.setPresentationModel(pM);
        assertTrue("doc.isConnected()", this.doc.isConnected());
    }

    @Test
    public void testInsertString()
        throws BadLocationException {
        final TextPM pM = new TextPM();
        this.doc.setPresentationModel(pM);
        this.doc.insertString(0, "Test", null);
        assertEquals("doc.getLength()", 4, doc.getLength());
        assertEquals("pM.getText()", "Test", pM.getText());
    }

    @Test
    public void testSetSuppressRemoveEvent()
        throws BadLocationException {
        final TextPM pM = new TextPM();
        this.doc.setPresentationModel(pM);
        pM.setText("Test");

        this.doc.setSuppressRemoveEvent(true);
        this.doc.remove(0, 1);
        assertEquals("pM.getText()", "Test", pM.getText()); // expecting that nothing was changed

        this.doc.remove(0, this.doc.getLength());
        assertEquals("doc.getText(0, this.doc.getLength())", "", this.doc.getText(0, this.doc.getLength()));
    }


    @Test
    public void testUndoManager() throws BadLocationException {
        UndoManager manager = new UndoManager();
        doc.addUndoableEditListener(manager);

        final TextPM pM = new TextPM();
        this.doc.setPresentationModel(pM);
        this.doc.insertString(0, "Test", null);
        this.doc.insertString(4, " failed!", null);

        manager.undo();

        assertEquals("doc.getLength()", 4, doc.getLength());
        assertEquals("pM.getText()", "Test", pM.getText());
    }
}
