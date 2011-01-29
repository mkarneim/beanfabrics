/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.Path;
import org.beanfabrics.PathObservation;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Property;
import org.beanfabrics.support.PropertySupport;
import org.junit.Before;
import org.junit.Test;

public class PathObservationTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PathObservationTest.class);
    }

    public PathObservationTest() {
    }

    static class GroupPM extends AbstractPM {
        @Property
        ListPM<ContactPM> contacts = new ListPM<ContactPM>();
        @Property
        ContactPM selectedContact;

        public GroupPM() {
            PMManager.setup(this);
        }

        @OnChange(path = "contacts")
        private void updateSelectedContact() {
            selectedContact = contacts.getSelection().getFirst();
            PropertySupport.get(this).refresh();
        }

        public void populate() {
            for (int i = 0; i < 10; ++i) {
                ContactPM pModel = new ContactPM();
                pModel.firstname.setText("Firstname " + i);
                pModel.lastname.setText("Lastname " + i);
                pModel.address.city.setText("City " + i);
                pModel.address.street.setText("Street " + i);
                contacts.add(pModel);
            }
            contacts.getSelection().addInterval(0, 0); // select first row
        }
    }

    static class ContactPM extends AbstractPM {
        @Property
        TextPM firstname = new TextPM();
        @Property
        TextPM lastname = new TextPM();
        @Property
        AddressPM address = new AddressPM();

        public ContactPM() {
            PMManager.setup(this);
        }
    }

    static class AddressPM extends AbstractPM {
        @Property
        TextPM street = new TextPM();
        @Property
        TextPM city = new TextPM();
        @Property
        TextPM country;

        public AddressPM() {
            PMManager.setup(this);
        }
    }

    GroupPM root;

    @Before
    public void setUp()
        throws Exception {
        root = new GroupPM();
        root.populate();
    }

    @Test
    public void create() {
        Path path = new Path("this");
        new PathObservation(root, path);
    }

    @Test
    public void getTarget() {
        Path path = new Path("this");
        PathObservation o = new PathObservation(root, path);
        assertEquals("o.hasTarget()", true, o.hasTarget());
        assertEquals("o.getTarget()", root, o.getTarget());
    }

    @Test
    public void getTarget2() {
        Path path = new Path("this.selectedContact");
        PathObservation o = new PathObservation(root, path);
        assertEquals("o.hasTarget()", true, o.hasTarget());
        assertEquals("o.getTarget()", root.selectedContact, o.getTarget());
    }

    @Test
    public void getTarget3() {
        Path path = new Path("this.selectedContact.firstname");
        PathObservation o = new PathObservation(root, path);
        assertEquals("o.hasTarget()", true, o.hasTarget());
        assertEquals("o.getTarget()", root.selectedContact.firstname, o.getTarget());
    }

    @Test
    public void getTarget4() {
        Path path = new Path("this.selectedContact.address");
        PathObservation o = new PathObservation(root, path);
        assertEquals("o.hasTarget()", true, o.hasTarget());
        assertEquals("o.getTarget()", root.selectedContact.address, o.getTarget());
    }

    @Test
    public void getTarget5() {
        Path path = new Path("this.selectedContact.address.street");
        PathObservation o = new PathObservation(root, path);
        assertEquals("o.hasTarget()", true, o.hasTarget());
        assertEquals("o.getTarget()", root.selectedContact.address.street, o.getTarget());
    }

    @Test
    public void getTarget6() {
        Path path = new Path("this.selectedContact.address.illegal");
        PathObservation o = new PathObservation(root, path);
        assertEquals("o.hasTarget()", false, o.hasTarget());
        assertEquals("o.getTarget()", null, o.getTarget());
    }

    @Test
    public void getTarget7() {
        Path path = new Path("this.selectedContact.address.country");
        PathObservation o = new PathObservation(root, path);
        assertEquals("o.hasTarget()", false, o.hasTarget());
        assertEquals("o.getTarget()", null, o.getTarget());
    }

    @Test
    public void getTarget8() {
        Path path = new Path("this");
        PresentationModel newRoot = new TextPM();
        PathObservation o = new PathObservation(newRoot, path);
        assertEquals("o.hasTarget()", true, o.hasTarget());
        assertEquals("o.getTarget()", newRoot, o.getTarget());
    }

    @Test
    public void getTarget9() {
        Path path = new Path("this.selectedContact.address.country");
        PathObservation o = new PathObservation(root, path);
        assertEquals("o.hasTarget()", false, o.hasTarget());
        assertEquals("o.getTarget()", null, o.getTarget());
        TextPM country = new TextPM();
        root.selectedContact.address.country = country;
        PropertySupport.get(root.selectedContact.address).refresh();
        assertEquals("o.hasTarget()", true, o.hasTarget());
        assertEquals("o.getTarget()", country, o.getTarget());
    }

    @Test
    public void getTarget10() {
        Path path = new Path("this.selectedContact");
        PathObservation o = new PathObservation(root, path);
        assertEquals("o.hasTarget()", true, o.hasTarget());
        assertEquals("o.getTarget()", root.selectedContact, o.getTarget());
        root.contacts.getSelection().clear();
        assertEquals("o.hasTarget()", false, o.hasTarget());
        assertEquals("o.getTarget()", null, o.getTarget());
        root.contacts.getSelection().addInterval(1, 1);
        assertEquals("o.hasTarget()", true, o.hasTarget());
        assertEquals("o.getTarget()", root.contacts.getAt(1), o.getTarget());
    }

    @Test
    public void getTarget11() {
        Path path = new Path("this.selectedContact.lastname");
        PathObservation o = new PathObservation(root, path);
        assertEquals("o.hasTarget()", true, o.hasTarget());
        assertEquals("o.getTarget()", root.selectedContact.lastname, o.getTarget());
        root.contacts.getSelection().clear();
        assertEquals("o.hasTarget()", false, o.hasTarget());
        assertEquals("o.getTarget()", null, o.getTarget());
        root.contacts.getSelection().addInterval(1, 1);
        assertEquals("o.hasTarget()", true, o.hasTarget());
        assertEquals("o.getTarget()", root.contacts.getAt(1).lastname, o.getTarget());
    }
}