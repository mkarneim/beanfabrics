package org.beanfabrics.samples.regextester;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Validation;

/**
 * The RegexPM is a presentation model for editing a regular expression. 
 */
public class RegexPM extends TextPM {
    
    public RegexPM() {
        PMManager.setup(this);
    }
    
    @Validation
    boolean containsValidRegex() {
        String text = getText();        
        try {
            Pattern pattern = Pattern.compile(text);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }
    
    public Pattern getPattern() {
        Pattern pattern = Pattern.compile(getText());
        return pattern;
    }
}
