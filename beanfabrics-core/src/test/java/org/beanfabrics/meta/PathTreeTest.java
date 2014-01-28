/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.meta;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.junit.Test;

/**
 * The {@link PathTreeTest} is a JUnit test case for the
 * {@link PathTree}.
 * 
 * @author Michael Karneim
 */
public class PathTreeTest {

    private static class CompanyPM extends AbstractPM {
        AddressPM address = new AddressPM();

        public CompanyPM() {
            PMManager.setup(this);
        }
    }

    private static class AddressPM extends AbstractPM {
        CityPM city = new CityPM();
        TextPM street = new TextPM();

        public AddressPM() {
            PMManager.setup(this);
        }
    }

    private static class CityPM extends AbstractPM {
        TextPM name = new TextPM();

        public CityPM() {
            PMManager.setup(this);
        }
    }

    @Test
    public void getNameReturnsThis() {
        MetadataRegistry reg = new MetadataRegistry();
        PathTree info = reg.getPathTree(CompanyPM.class);
        assertEquals("info.getName()", "this", info.getName());
    }

    @Test
    public void getTypeInfoReturnsTypeInfoOfCorrectJavaType() {
        MetadataRegistry reg = new MetadataRegistry();
        PathTree info = reg.getPathTree(CompanyPM.class);
        assertEquals("info.getTypeInfo().getJavaType()", CompanyPM.class, info.getTypeInfo().getJavaType());
    }

    @Test
    public void hasChildrenReturnsTrue() {
        MetadataRegistry reg = new MetadataRegistry();
        PathTree info = reg.getPathTree(CompanyPM.class);
        assertEquals("info.hasChildren()", true, info.hasChildren());
    }

    @Test
    public void pathToAddressIsThisDotAddress() {
        MetadataRegistry reg = new MetadataRegistry();
        PathTree info = reg.getPathTree(CompanyPM.class);
        assertEquals("info.getChild(\"address\").getPath().getPathString()", "this.address", info.getChild("address").getPath().getPathString());
    }

    @Test
    public void pathToCityIsThisDotAddressDotCity() {
        MetadataRegistry reg = new MetadataRegistry();
        PathTree info = reg.getPathTree(CompanyPM.class);
        assertEquals("info.getChild(\"address\").getChild(\"city\").getPath().getPathString()", "this.address.city", info.getChild("address").getChild("city").getPath().getPathString());
    }

    @Test
    public void pathToStreetIsThisDotAddressDotCityDotName() {
        MetadataRegistry reg = new MetadataRegistry();
        PathTree info = reg.getPathTree(CompanyPM.class);
        assertEquals("info.getChild(\"address\").getChild(\"city\").getChild(\"name\").getPath().getPathString()", "this.address.city.name", info.getChild("address").getChild("city").getChild("name").getPath().getPathString());
    }

    @Test
    public void javaTypeOfThisDotAddressIsAddressPM() {
        MetadataRegistry reg = new MetadataRegistry();
        PathTree info = reg.getPathTree(CompanyPM.class);
        assertEquals("info.getPathInfo(new Path(\"this.address\")).getTypeInfo().getJavaType()", AddressPM.class, info.getPathInfo(new Path("this.address")).getTypeInfo().getJavaType());
    }

    @Test
    public void javaTypeOfThisDotAddressDotCityDotNameIsTextPM() {
        MetadataRegistry reg = new MetadataRegistry();
        PathTree info = reg.getPathTree(CompanyPM.class);
        assertEquals("info.getPathInfo(new Path(\"this.address.city.name\")).getTypeInfo().getJavaType()", TextPM.class, info.getPathInfo(new Path("this.address.city.name")).getTypeInfo().getJavaType());
    }
}
