package org.beanfabrics.swing.formatting;

import java.util.regex.Pattern;

import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Validation;

public class PhoneNumberPM extends TextPM {
    private Pattern pattern = Pattern.compile("[0-9]{3}-[0-9]{7}"); // e.g. 605-8889999 
    
    public PhoneNumberPM() {
        PMManager.setup(this);
    }
    
    @Validation(message="Not a valid phone number! Expecting format xxx-xxxxxxx")
    public boolean isPhoneNumber() {
        return pattern.matcher(getText()).matches();
    }
    
    /** {@inheritDoc} */
    @Override
    public void reformat() {
        String text = getText().replaceAll("[^0-9-]", "");
        if ( !text.contains("-") && text.length()>3) {
            text = text.substring(0,3).concat("-").concat( text.substring(3));
        }
        setText(text);
    }
}
