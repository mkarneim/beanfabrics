package org.beanfabrics.swt.samples.login;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Validation;

/**
 * The {@link LoginPM} is the presentation model of the {@link LoginDialog}.
 * 
 * @author Michael Karneim
 */
public class LoginPM extends AbstractPM {
    TextPM username = new TextPM();
    TextPM password = new TextPM();
    OperationPM login = new OperationPM();

    public LoginPM() {
        username.setMandatory(true);
        password.setMandatory(true);
        PMManager.setup(this);
    }

    @Validation(path = "login")
    public boolean canLogin() {
        return isValid();
    }

    @Operation
    public void login() {
        login.check();
        // TODO insert login code here
        System.out.println("login for user " + username.getText() + " with password " + password.getText());
    }
}
