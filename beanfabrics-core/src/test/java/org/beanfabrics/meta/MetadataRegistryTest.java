/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.meta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.MapPM;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Property;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class MetadataRegistryTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(MetadataRegistryTest.class);
    }

    private MetadataRegistry metadata;

    @Before
    public void createMetadata() {
        this.metadata = new MetadataRegistry();
    }

    private static class ListOfFilePM extends ListPM<FilePM> {
        public ListOfFilePM() {
            super();
        }
    }

    private static class FilePM extends AbstractPM {
        @Property
        TextPM path = new TextPM();
    }

    private static class FlatFilePM extends FilePM {
        @Property
        IntegerPM size = new IntegerPM();
    }

    private static class DirectoryPM extends FilePM {
        @Property
        DirectoryPM parent;
        @Property
        ListPM<FilePM> files = new ListPM<FilePM>();
    }

    private static class AnotherDirectoryPM extends FilePM {
        @Property
        ListOfFilePM listOfFiles = new ListOfFilePM();
    }

    private static class ThirdDirectoryPM extends FilePM {
        @Property
        DirectoryPM parent;
        @Property
        MapPM<File, FilePM> files = new MapPM<File, FilePM>();
    }

    @Test
    public void describeTextPM() {
        Class cls = TextPM.class;
        TypeInfo info = this.metadata.getTypeInfo(cls);
        assertEquals("info.getProperties().size()", 0, info.getProperties().size());
        assertEquals("info.getJavaType()", cls, info.getJavaType());

        TypeInfo info2 = this.metadata.getTypeInfo(cls);
        assertEquals("info2", info, info2);
    }

    @Test
    public void describeListPM() {
        Class cls = ListPM.class;
        TypeInfo info = this.metadata.getTypeInfo(cls);
        assertEquals("info.getProperties().size()", 0, info.getProperties().size());
        assertEquals("info.getJavaType()", cls, info.getJavaType());

    }

    @Test
    public void describeListOfFilePM() {
        Class cls = ListOfFilePM.class;
        TypeInfo info = this.metadata.getTypeInfo(cls);
        assertEquals("info.getProperties().size()", 0, info.getProperties().size());
        assertEquals("info.getJavaType()", cls, info.getJavaType());
    }

    @Test
    public void describeFilePM() {
        Class cls = FilePM.class;
        TypeInfo info = this.metadata.getTypeInfo(cls);
        assertEquals("info.getJavaType()", cls, info.getJavaType());

        assertEquals("info.getProperties().size()", 1, info.getProperties().size());
        PropertyInfo pdesc = info.getProperties().iterator().next();
        assertEquals("pdesc.getName()", "path", pdesc.getName());
        TypeInfo typeInfo = pdesc.getTypeInfo();
        assertEquals("textCellInfo", this.metadata.getTypeInfo(TextPM.class), typeInfo);
    }

    @Test
    public void describeDirectoryPM() {
        // this test shows how to find out the actual type argument of a property of type IListPM.
        Class cls = DirectoryPM.class;
        TypeInfo info = this.metadata.getTypeInfo(cls);
        assertEquals("info.getJavaType()", cls, info.getJavaType());

        assertEquals("info.getProperties().size()", 3, info.getProperties().size());

        PropertyInfo pInfo = info.getProperty("parent");
        assertNotNull("pInfo", pInfo);
        // otherwise see test 'describeAnotherDirectoryCell'
    }

    @Test
    public void describeAnotherDirectoryPM() {
        // this test shows how to find out the actual type argument of a property of type IListPM.

        Class cls = AnotherDirectoryPM.class;
        TypeInfo info = this.metadata.getTypeInfo(cls);
        assertEquals("info.getJavaType()", cls, info.getJavaType());

        assertEquals("info.getProperties().size()", 2, info.getProperties().size());

    }

    @Test
    public void describeAnotherDirectoryPM2() {
        Class cls = AnotherDirectoryPM.class;
        TypeInfo info = this.metadata.getTypeInfo(cls);
        assertEquals("info.getJavaType()", cls, info.getJavaType());

        PropertyInfo pInfo = info.getProperty("listOfFiles");

    }

    @Test
    public void describeDirectoryPM2() {
        Class cls = DirectoryPM.class;
        TypeInfo info = this.metadata.getTypeInfo(cls);
        assertEquals("info.getJavaType()", cls, info.getJavaType());

        PropertyInfo pInfo = info.getProperty("files");

    }

    @Test
    public void describeThirdDirectoryPM() {
        Class cls = ThirdDirectoryPM.class;
        TypeInfo info = this.metadata.getTypeInfo(cls);
        assertEquals("info.getJavaType()", cls, info.getJavaType());

        PropertyInfo pInfo = info.getProperty("files");

    }
}