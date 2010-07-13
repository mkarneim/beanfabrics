package org.beanfabrics.swing.customizer.list;

import static junit.framework.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.Path;
import org.beanfabrics.swing.list.CellConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CellConfigPropertyEditorTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CellConfigPropertyEditorTest.class);
    }

    private CellConfigPropertyEditor editor;

    @Before
    public void createEditor() {
        editor = new CellConfigPropertyEditor();
    }

    @After
    public void removeEditor() {
        editor = null;
    }

    @Test
    public void getJavaInitializationString() {
        CellConfig value = new CellConfig(new Path("this.name"));
        editor.setValue(value);
        String expected = "new org.beanfabrics.swing.list.CellConfig( new org.beanfabrics.Path(\"this.name\") )";
        String actual = editor.getJavaInitializationString();
        //System.out.println(actual);		
        assertEquals("actual", expected, actual);

    }

}
