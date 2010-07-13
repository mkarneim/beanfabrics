/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.TextPM;
import org.junit.Before;
import org.junit.Test;

public class BnTextFieldTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BnTextFieldTest.class);
    }

    BnTextField bnTextField;

    @Before
    public void setUp() {
        bnTextField = new BnTextField();
    }

    @Test
    public void modelToView() {
        TextPM textPM = new TextPM();
        ModelProvider ds = new ModelProvider();
        ds.setPresentationModel(textPM);

        bnTextField.setModelProvider(ds);
        bnTextField.setPath(new Path("this"));

        textPM.setText("hello");
        assertEquals("bnTextField.getText()", "hello", bnTextField.getText());

        bnTextField.setModelProvider(null);
        textPM.setText("world");
        assertEquals("bnTextField.getText()", "", bnTextField.getText());
    }

    @Test
    public void viewToModel() {
        TextPM textPM = new TextPM();
        ModelProvider ds = new ModelProvider();
        ds.setPresentationModel(textPM);

        bnTextField.setModelProvider(ds);
        bnTextField.setPath(new Path("this"));

        bnTextField.setText("hello");
        assertEquals("textCell.getText()", "hello", textPM.getText());

        bnTextField.setModelProvider(null);
        bnTextField.setText("world");
        assertEquals("textCell.getText()", "hello", textPM.getText());
    }

    @Test
    public void incorrectModel() {
        OperationPM op = new OperationPM();
        ModelProvider ds = new ModelProvider();
        ds.setPresentationModel(op);

        bnTextField.setModelProvider(ds);
        bnTextField.setPath(new Path("this"));

    }
}