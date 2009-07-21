/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.model.ListPM;
import org.junit.Test;

public class ViewClassDecoratorTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ViewClassDecoratorTest.class);
    }

    private static class MyView implements View<ListPM> {

        public ListPM getPresentationModel() {
            return null;
        }

        public void setPresentationModel(ListPM pModel) {
        }

    }

    @Test
    public void getExpectedModelType() {
        ViewClassDecorator deco = new ViewClassDecorator(MyView.class);
        Class pModelType = deco.getExpectedModelType();
        assertEquals("pModelType", ListPM.class, pModelType);
    }
}