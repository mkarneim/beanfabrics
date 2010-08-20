/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Property;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class ContextTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ContextTest.class);
    }

    public static class TopLevelPM extends AbstractPM {
        @Property
        LevelOnePM levelOne;

        public TopLevelPM() {
            PMManager.setup(this);
        }

        public void setLevelOne(LevelOnePM pModel) {
            this.levelOne = pModel;
            PMManager.setup(this);
        }
    }

    public static class LevelOnePM extends AbstractPM {
        @Property
        LevelTwoPM levelTwo = new LevelTwoPM();

        public LevelOnePM() {
            PMManager.setup(this);
        }
    }

    public static class LevelTwoPM extends AbstractPM {
        @Property
        LevelThreePM levelThree = new LevelThreePM();

        public LevelTwoPM() {
            PMManager.setup(this);
        }

    }

    public static class LevelThreePM extends AbstractPM {
        @Property
        TextPM name = new TextPM();
        int countServiceAdded = 0;
        int countServiceRemoved = 0;
        Object service;

        public LevelThreePM() {
            PMManager.setup(this);
            this.getContext().addContextListener(new ContextListener() {

                public void serviceRemoved(ServiceRemovedEvent evt) {
                    countServiceRemoved++;
                    service = null;
                }

                public void serviceAdded(ServiceAddedEvent evt) {
                    countServiceAdded++;
                    service = evt.getServiceEntry().getService();
                }

                public void parentRemoved(ParentRemovedEvent evt) {

                }

                public void parentAdded(ParentAddedEvent evt) {

                }
            });
        }
    }

    @Test
    public void addService() {
        LevelOnePM pModel = new LevelOnePM();
        Object service = new Object();
        pModel.getContext().addService(Object.class, service);
        assertEquals("pModel.levelTwo.levelThree.countServiceAdded", 1, pModel.levelTwo.levelThree.countServiceAdded);
        assertEquals("pModel.levelTwo.levelThree.service", service, pModel.levelTwo.levelThree.service);
    }

    @Test
    public void removeService() {
        LevelOnePM pModel = new LevelOnePM();
        pModel.getContext().addService(Object.class, new Object());
        assertEquals("pModel.levelTwo.levelThree.countServiceAdded", 1, pModel.levelTwo.levelThree.countServiceAdded);
        pModel.getContext().removeService(Object.class);
        assertEquals("pModel.levelTwo.levelThree.countServiceRemoved", 1, pModel.levelTwo.levelThree.countServiceRemoved);
        assertNull("pModel.levelTwo.levelThree.service", pModel.levelTwo.levelThree.service);
    }

    @Test
    public void addTopLevelService() {
        TopLevelPM topLevelMdl = new TopLevelPM();
        Object service = new Object();
        topLevelMdl.getContext().addService(Object.class, service);

        LevelOnePM pModel = new LevelOnePM();
        topLevelMdl.setLevelOne(pModel);

        assertEquals("pModel.levelTwo.levelThree.countServiceAdded", 1, pModel.levelTwo.levelThree.countServiceAdded);
        assertEquals("pModel.levelTwo.levelThree.service", service, pModel.levelTwo.levelThree.service);

        topLevelMdl.setLevelOne(null);

        assertEquals("pModel.levelTwo.levelThree.countServiceRemoved", 1, pModel.levelTwo.levelThree.countServiceRemoved);
        assertNull("pModel.levelTwo.levelThree.service", pModel.levelTwo.levelThree.service);
    }
    
    @Test
    public void getService() {
    	TopLevelPM topLevelMdl = new TopLevelPM();
        Object service = new Object();
        topLevelMdl.getContext().addService(Object.class, service);
        topLevelMdl.setLevelOne( new LevelOnePM());
        
        LevelThreePM levelThree = topLevelMdl.levelOne.levelTwo.levelThree;
        Object foundService = levelThree.getContext().getService(Object.class);
        assertEquals("foundService", service, foundService);
    }
}