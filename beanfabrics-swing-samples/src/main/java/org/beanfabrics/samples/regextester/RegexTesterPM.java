package org.beanfabrics.samples.regextester;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Validation;

/**
 * The RegexTesterPM is the presentation model for the RegexTesterPanel.
 */
public class RegexTesterPM extends AbstractPM {
    // Input fields: these properties contain the values of the input fields
    RegexPM regex = new RegexPM();
    TextPM input = new TextPM();

    // Operation: this operation performs the pattern matching    
    OperationPM match = new OperationPM();

    // Output fields: these properties contain the result of the pattern matching
    BooleanPM doesMatchEntireRegion = new BooleanPM();
    ListPM<GroupPM> groups = new ListPM<GroupPM>();

    public RegexTesterPM() {
        doesMatchEntireRegion.setEditable(false);
        PMManager.setup(this);
    }

    /**
     * Performs the pattern matching.
     */
    @Operation
    public void match() {
        Pattern p = regex.getPattern();
        Matcher matcher = p.matcher(input.getText());

        doesMatchEntireRegion.setBoolean(matcher.matches());
        matcher.reset();

        groups.clear();
        final int grpCount = matcher.groupCount();
        int findCount = 0;
        while (matcher.find()) {
            findCount++;
            for (int i = 0; i < grpCount + 1; ++i) {
                String groupText = matcher.group(i);
                GroupPM group = new GroupPM(findCount, i, groupText);
                groups.add(group);
            }
        }
    }

    /**
     * This validation rule checks whether the match operation can be executed.
     * 
     * @return true, if the regex is valid
     */
    @Validation(path = "match")
    public boolean isMatchEnabled() {
        return regex.isValid();
    }

}
