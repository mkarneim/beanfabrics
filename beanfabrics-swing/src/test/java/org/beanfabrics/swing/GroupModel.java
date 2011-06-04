/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import java.util.Date;

import org.beanfabrics.model.AbstractOperationPM;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.DatePM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.MapPM;
import org.beanfabrics.model.Options;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.PropertySupport;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * @author Michael Karneim
 */
public class GroupModel extends AbstractPM {
    protected final BooleanPM ready = new BooleanPM();
    protected final TextPM name = new TextPM();
    protected final MapPM<String, PersonModel> persons = new MapPM<String, PersonModel>();
    protected PersonModel selected;

    protected final IOperationPM remove = new AbstractOperationPM() {
        public boolean execute()
            throws Throwable {
            GroupModel.this.remove();
            return true; // success
        }
    };

    public GroupModel() {
        PMManager.setup(this);
        this.populate();
    }

    @OnChange(path = "persons")
    private void onElementsChanged() {
        this.selected = persons.getSelection().getFirst();
        PropertySupport.get(this).refresh();
    }

    private void populate() {
        {
            PersonModel pModel = new PersonModel();
            pModel.name.setText("John Doe");
            pModel.birthday.setDate(new Date(System.currentTimeMillis()));
            pModel.address.street.setText("infinite loop 5");
            pModel.address.city.setText("munich");
            pModel.active.setBoolean(true);
            this.persons.put("pModel", pModel);
        }
        {
            PersonModel pModel = new PersonModel();
            pModel.name.setText("Jenny Doe");
            pModel.birthday.setDate(new Date(System.currentTimeMillis()));
            pModel.address.street.setText("infinite loop 4");
            pModel.address.city.setText("munich");
            pModel.active.setBoolean(true);
            this.persons.put("pModel", pModel);
        }
    }

    public void remove() {
        this.remove.check();

        final String[] keys = this.persons.getSelectedKeys().toArray(new String[this.persons.getSelectedKeys().size()]);
        for (int i = 0; i < keys.length; i++) {
            this.persons.removeKey(keys[i]);
        }
    }

    public static class PersonModel extends AbstractPM {
        protected final TextPM name = new TextPM();
        protected final DatePM birthday = new DatePM();
        protected final AddressModel address = new AddressModel();
        protected final BooleanPM active = new BooleanPM();

        public PersonModel() {
            PMManager.setup(this);
            this.name.getValidator().add(new ValidationRule() {
                public ValidationState validate() {
                    if (name.getText().length() < 4) {
                        return new ValidationState("The name has to have 4 characters at least");
                    }
                    return null;
                }
            });

        }
    }

    public static class AddressModel extends AbstractPM {
        protected final TextPM street = new TextPM();
        protected final TextPM city = new TextPM();
        protected final TextPM country = new TextPM();

        public AddressModel() {
            PMManager.setup(this);
            final Options<String> countries = new Options<String>();
            countries.put("Austria", "Austria");
            countries.put("Germany", "Germany");
            countries.put("Swiss", "Swiss");
            countries.put("Unknown", "Unknown");
            country.setOptions(countries);
            this.country.getValidator().add(new ValidationRule() {
                public ValidationState validate() {
                    if ("Unknown".equals(country.getText())) {
                        return new ValidationState("The country is unknown");
                    }
                    return null;
                }
            });
        }
    }
}