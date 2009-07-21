/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.support;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class ServiceSupportTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ServiceSupportTest.class);
    }

    public static class TopLevelModel extends AbstractPM {
        @Property
        LevelOneModel levelOneModel;
        @Property
        ListPM<RowModel> rows = new ListPM<RowModel>();

        public TopLevelModel() {
            PMManager.setup(this);
        }

        public void setLevelOne(LevelOneModel pModel) {
            this.levelOneModel = pModel;
            PropertySupport.get(this).refresh();
        }
    }

    public static class RowModel extends AbstractPM {
        MyService myService;

        public RowModel() {
            PMManager.setup(this);
        }

        @Service
        void setMyService(MyService service) {
            this.myService = service;
        }
    }

    public static class LevelOneModel extends AbstractPM {
        @Property
        LevelTwoModel levelTwo = new LevelTwoModel();

        public LevelOneModel() {
            PMManager.setup(this);
        }
    }

    public static class LevelTwoModel extends AbstractPM {
        @Property
        LevelThreeModel levelThree = new LevelThreeModel();

        public LevelTwoModel() {
            PMManager.setup(this);
        }
    }

    public static class LevelThreeModel extends AbstractPM {
        @Property
        TextPM name = new TextPM();

        MyService myService;
        MyOtherService myOtherService;

        public LevelThreeModel() {
            PMManager.setup(this);
        }

        @Service
        void setMyService(MyService service) {
            this.myService = service;
        }

        @Service
        void setMyOtherService(MyOtherService service) {
            this.myOtherService = service;
        }
    }

    public static class MyService {
    }

    public static class MyOtherService {
    }

    public static class SampleModel extends AbstractPM {
        @Service
        MyService myService;

        public SampleModel() {
            PMManager.setup(this);
        }
    }

    @Test
    public void addService() {
        LevelOneModel pModel = new LevelOneModel();
        MyService service = new MyService();
        pModel.getContext().addService(MyService.class, service);
        assertNotNull("pModel.levelTwo.levelThree.myService", pModel.levelTwo.levelThree.myService);
        pModel.getContext().removeService(MyService.class);
        assertNull("pModel.levelTwo.levelThree.myService", pModel.levelTwo.levelThree.myService);
    }

    @Test
    public void addTopLevelService() {
        TopLevelModel topLevel = new TopLevelModel();
        MyService service = new MyService();
        topLevel.getContext().addService(MyService.class, service);
        assertEquals("topLevel.rows.getContext().getServiceEntries().size()", 1, topLevel.rows.getContext().getServiceEntries().size());
        RowModel rowMdl = new RowModel();
        topLevel.rows.add(rowMdl);
        assertNotNull("rowEd.myService", rowMdl.myService);
    }

    @Test
    public void competingServices() {
        TopLevelModel topLevelMdl = new TopLevelModel();
        MyService service1 = new MyService();
        topLevelMdl.getContext().addService(MyService.class, service1);

        LevelOneModel pModel = new LevelOneModel();
        topLevelMdl.setLevelOne(pModel);

        assertEquals("topLevelMdl.levelOne.levelTwo.levelThree.myService", service1, topLevelMdl.levelOneModel.levelTwo.levelThree.myService);

        MyService service2 = new MyService();
        topLevelMdl.levelOneModel.getContext().addService(MyService.class, service2);

        assertEquals("topLevelMdl.levelOne.levelTwo.levelThree.myService", service2, topLevelMdl.levelOneModel.levelTwo.levelThree.myService);

        topLevelMdl.levelOneModel.getContext().removeService(MyService.class);
        assertEquals("topLevelMdl.levelOne.levelTwo.levelThree.myService", service1, topLevelMdl.levelOneModel.levelTwo.levelThree.myService);
    }

    @Test
    public void competingServices2() {
        TopLevelModel topLevelMdl = new TopLevelModel();

        LevelOneModel pModel = new LevelOneModel();
        topLevelMdl.setLevelOne(pModel);

        MyService service2 = new MyService();
        topLevelMdl.levelOneModel.getContext().addService(MyService.class, service2);

        assertEquals("topLevelMdl.levelOne.levelTwo.levelThree.myService", service2, topLevelMdl.levelOneModel.levelTwo.levelThree.myService);

        MyService service1 = new MyService();
        topLevelMdl.getContext().addService(MyService.class, service1);

        assertEquals("topLevelMdl.levelOne.levelTwo.levelThree.myService", service2, topLevelMdl.levelOneModel.levelTwo.levelThree.myService);

        topLevelMdl.levelOneModel.getContext().removeService(MyService.class);
        assertEquals("topLevelMdl.levelOne.levelTwo.levelThree.myService", service1, topLevelMdl.levelOneModel.levelTwo.levelThree.myService);
    }

    @Test
    public void addingAndRemoving2Services() {
        LevelOneModel pModel = new LevelOneModel();
        pModel.getContext().addService(MyService.class, new MyService());
        assertNotNull("pModel.levelTwo.levelThree.myService", pModel.levelTwo.levelThree.myService);
        pModel.getContext().addService(MyOtherService.class, new MyOtherService());
        assertNotNull("pModel.levelTwo.levelThree.myOtherService", pModel.levelTwo.levelThree.myOtherService);

        pModel.getContext().removeService(MyService.class);

        pModel.getContext().addService(MyService.class, new MyService());
        assertNotNull("pModel.levelTwo.levelThree.myService", pModel.levelTwo.levelThree.myService);

        pModel.getContext().removeService(MyService.class);
    }

    @Test
    public void settingServiceInField() {
        SampleModel pModel = new SampleModel();
        MyService myService = new MyService();

        pModel.getContext().addService(MyService.class, myService);
        assertNotNull("pModel.myService", pModel.myService);
        assertEquals("pModel.myService", myService, pModel.myService);

        pModel.getContext().removeService(MyService.class);
        assertNull("pModel.myService", pModel.myService);
    }
}