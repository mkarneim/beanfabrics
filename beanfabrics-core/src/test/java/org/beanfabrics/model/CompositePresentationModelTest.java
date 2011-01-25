/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertNotNull;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.Path;
import org.beanfabrics.PathEvaluation;
import org.beanfabrics.support.Property;
import org.beanfabrics.support.PropertySupport;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class CompositePresentationModelTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CompositePresentationModelTest.class);
    }

    // TODO (mk) 1, Visitor und Filter im Helper anbieten
    // TODO (mk) 1, PropertiesUIModelHelper sollte der PropertiesCell selbst sein.

    @Test
    public void address() {
        AddressPM pModel = new AddressPM();

        //TextPM street = (TextPM) Helper.get(pModel).getProperty( new Path("this.street"));
        TextPM street = (TextPM)PathEvaluation.evaluateOrNull(pModel, new Path("this.street"));
        assertNotNull("street", street);

        //TextPM cityName = (TextPM) Helper.get(pModel).getProperty( new Path("this.city.name"));
        TextPM cityName = (TextPM)PathEvaluation.evaluateOrNull(pModel, new Path("this.city.name"));

        assertNotNull("cityName", cityName);
    }

    @Test
    public void person() {
        PersonPM pModel = new PersonPM();
        pModel.getValidator().validate();

        //TextPM name = (TextPM) Helper.get(pModel).getProperty( "name");
        TextPM name = (TextPM)PathEvaluation.evaluateOrNull(pModel, new Path("name"));
        assertNotNull("name", name);

        //TextPM street = (TextPM) Helper.get(pModel).getProperty( new Path("this.address.street"));
        TextPM street = (TextPM)PathEvaluation.evaluateOrNull(pModel, new Path("this.address.street"));
        assertNotNull("street", street);

        //TextPM cityName = (TextPM) Helper.get(pModel).getProperty( new Path("this.address.city.name"));
        TextPM cityName = (TextPM)PathEvaluation.evaluateOrNull(pModel, new Path("this.address.city.name"));

        assertNotNull("cityName", cityName);
    }

    @Test
    public void project() {
        ProjectPM pModel = new ProjectPM();
        //TextPM name = (TextPM) Helper.get(pModel).getProperty( "name");
        TextPM name = (TextPM)PathEvaluation.evaluateOrNull(pModel, new Path("name"));
        assertNotNull("name", name);

        //TextPM street = (TextPM) Helper.get(pModel).getProperty( new Path("this.leader.address.street"));
        TextPM street = (TextPM)PathEvaluation.evaluateOrNull(pModel, new Path("this.leader.address.street"));
        assertNotNull("street", street);

        //TextPM cityName = (TextPM) Helper.get(pModel).getProperty( new Path("this.leader.address.city.name"));
        TextPM cityName = (TextPM)PathEvaluation.evaluateOrNull(pModel, new Path("this.leader.address.city.name"));
        assertNotNull("cityName", cityName);

        //TextPM companyName = (TextPM) Helper.get(pModel).getProperty( new Path("this.company.name"));
        TextPM companyName = (TextPM)PathEvaluation.evaluateOrNull(pModel, new Path("this.company.name"));
        assertNotNull("companyName", companyName);
    }

    @Test
    public void company() {
        CompanyPM pModel = new CompanyPM();
        //TextPM name = (TextPM) Helper.get(pModel).getProperty( "name");
        TextPM name = (TextPM)PathEvaluation.evaluateOrNull(pModel, new Path("name"));
        assertNotNull("name", name);

        //TextPM street = (TextPM) Helper.get(pModel).getProperty( new Path("this.address.street"));
        TextPM street = (TextPM)PathEvaluation.evaluateOrNull(pModel, new Path("this.address.street"));
        assertNotNull("street", street);

        //TextPM cityName = (TextPM) Helper.get(pModel).getProperty( new Path("this.address.city.name"));
        TextPM cityName = (TextPM)PathEvaluation.evaluateOrNull(pModel, new Path("this.address.city.name"));
        assertNotNull("cityName", cityName);
    }

    public static class ProjectPM extends AbstractPM {
        @Property
        TextPM name = new TextPM();
        @Property
        PersonPM leader = new PersonPM();
        @Property
        CompanyPM company = new CompanyPM();

        public ProjectPM() {
            PMManager.setup(this);
            name.setText("untitled project");
        }

        public void setLeader(PersonPM newLeader) {
            if (equals(this.leader, newLeader)) {
                return;
            } else {
                this.leader = newLeader;
                PropertySupport.get(this).refresh();
            }
        }
    }

    public interface AddressablePM extends PresentationModel {
        @Property
        AddressPM getAddress();
    }

    public static class CompanyPM extends AbstractPM implements AddressablePM {

        private TextPM name = new TextPM();
        @Property
        PersonPM owner = new PersonPM();

        AddressPM address = new AddressPM();

        public CompanyPM() {
            PMManager.setup(this);
        }

        @Property
        public AddressPM getAddress() {
            return address;
        }

        @Property
        public TextPM getName() {
            return name;
        }
    }

    public static class PersonPM extends AbstractPM implements AddressablePM {
        private TextPM name = new TextPM();
        private TextPM phone = new TextPM();
        @Property
        private AddressPM address = new AddressPM();

        private TimeSpanPM timespan;
        @Property
        private IntegerPM height = new IntegerPM();

        public PersonPM() {
            PMManager.setup(this);
        }

        @Property
        public TextPM getName() {
            return name;
        }

        @Property
        public TextPM getPhone() {
            return phone;
        }

        public AddressPM getAddress() {
            return address;
        }

        @Property
        public TimeSpanPM getTimespan() {
            return timespan;
        }

        public void setTimespan(TimeSpanPM newValue) {
            if (equals(this.timespan, newValue)) {
                return;
            }
            this.timespan = newValue;
            PropertySupport.get(this).refresh();
        }
    }

    public static interface TimeSpanPM extends PresentationModel {
        @Property
        public DatePM getStart();

        @Property
        public DatePM getEnd();
    }

    public static class AddressPM extends AbstractPM {
        @Property
        private TextPM street = new TextPM();
        @Property
        CityPM city = new CityPM();

        public AddressPM() {
            PMManager.setup(this);
        }

        public TextPM getStreet() {
            return street;
        }
    }

    public static class CityPM extends AbstractPM {
        TextPM name = new TextPM();

        public CityPM() {
            PMManager.setup(this);
        }
    }
}