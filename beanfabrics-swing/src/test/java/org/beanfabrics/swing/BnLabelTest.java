/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IconTextPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 * @author Marcel Eyke
 */
public class BnLabelTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BnLabelTest.class);
    }

    BnLabel lbl;

    private static class RootModel extends AbstractPM {
        AModel a = new AModel();

        public RootModel() {
            PMManager.setup(this);
        }
    }

    private static class AModel extends AbstractPM {
        BModel b = new BModel();

        public AModel() {
            PMManager.setup(this);
        }
    }

    private static class BModel extends AbstractPM {
        TextPM c = new TextPM();

        public BModel() {
            PMManager.setup(this);
        }
    }

    RootModel root;
    AModel a;
    BModel b;
    TextPM c;

    public BnLabelTest() {
    }

    @Before
    public void setUp()
        throws Exception {
        lbl = new BnLabel();
        root = new RootModel();
        a = root.a;
        b = a.b;
        c = b.c;
    }

    @AfterClass
    public static void tearDownClass()
        throws Exception {
    }

    @Test
    public void create() {
        assertNotNull(lbl);
    }

    @Test
    public void bind() {
        ModelProvider provider = new ModelProvider();
        provider.setPresentationModel(root);
        lbl.setPath(new Path("this.a.b.c"));
        lbl.setModelProvider(provider);

        assertEquals("lbl.isConnected()", true, lbl.isConnected());
        assertEquals("lbl.getText()", "", lbl.getText());

        c.setText("hello, world!");
        assertEquals("lbl.getText()", "hello, world!", lbl.getText());
    }

    @Test
    public void bind2() {
        ModelProvider provider = new ModelProvider();
        lbl.setPath(new Path("this.a.b.c"));
        lbl.setModelProvider(provider);

        provider.setPresentationModel(root);

        assertEquals("lbl.isConnected()", true, lbl.isConnected());
        assertEquals("lbl.getText()", "", lbl.getText());

        c.setText("hello, world!");
        assertEquals("lbl.getText()", "hello, world!", lbl.getText());
    }

    @Test
    public void bind3() {
        ModelProvider provider = new ModelProvider();
        lbl.setModelProvider(provider);
        provider.setPresentationModel(root);
        lbl.setPath(new Path("this.a.b.c"));

        assertEquals("lbl.isConnected()", true, lbl.isConnected());
        assertEquals("lbl.getText()", "", lbl.getText());

        c.setText("hello, world!");
        assertEquals("lbl.getText()", "hello, world!", lbl.getText());
    }

    @Test
    public void bind4() {
        ModelProvider provider = new ModelProvider();
        provider.setPresentationModel(root);
        lbl.setPath(new Path("this.a.b.c"));
        lbl.setModelProvider(provider);

        assertEquals("lbl.isConnected()", true, lbl.isConnected());
        assertEquals("lbl.getText()", "", lbl.getText());

        c.setText("hello, world!");
        assertEquals("lbl.getText()", "hello, world!", lbl.getText());
    }

    @Test
    public void bind5() {
        ModelProvider provider = new ModelProvider();
        lbl.setPath(new Path("this.a.b.c"));
        lbl.setModelProvider(provider);
        provider.setPresentationModel(root);

        assertEquals("lbl.isConnected()", true, lbl.isConnected());
        assertEquals("lbl.getText()", "", lbl.getText());

        c.setText("hello, world!");
        assertEquals("lbl.getText()", "hello, world!", lbl.getText());
    }

    @Test
    public void setPrsentationModel() {
        BnLabel lbl = new BnLabel();
        TextPM prop = new TextPM();
        prop.setText("hello, world!");
        assertEquals("lbl.isConnected()", false, lbl.isConnected());
        assertEquals("lbl.getText()", "", lbl.getText());

        lbl.setPresentationModel(prop);
        assertEquals("lbl.isConnected()", true, lbl.isConnected());
        assertEquals("lbl.getText()", "hello, world!", lbl.getText());

        // connect with icon and text model
        IconTextPM iconText = new IconTextPM();
        iconText.setIconUrl(BnLabelTest.class.getResource("sample.gif"));
        iconText.setText("hello, world!");
        lbl.setPresentationModel(iconText);
        assertEquals("lbl.isConnected()", true, lbl.isConnected());
        assertEquals("lbl.getText()", "hello, world!", lbl.getText());
        assertNotNull("lbl.getIcon()", lbl.getIcon());

        // disconnect
        lbl.setPresentationModel(null);
        assertEquals("lbl.isConnected()", false, lbl.isConnected());
        assertEquals("lbl.getText()", "", lbl.getText());

    }
}