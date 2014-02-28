package org.beanfabrics.swing.formatting;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;

public class ContactPM extends AbstractPM {
    TextPM name = new TextPM();
    PhoneNumberPM phone = new PhoneNumberPM();
    
    public ContactPM() {
        PMManager.setup(this);
    }
    
}
