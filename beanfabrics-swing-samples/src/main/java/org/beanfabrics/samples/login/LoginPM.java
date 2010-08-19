package org.beanfabrics.samples.login;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Validation;

public class LoginPM extends AbstractPM {

    TextPM username = new TextPM();
    TextPM password = new TextPM();

    OperationPM login = new OperationPM();

    public LoginPM() {
        username.setMandatory(true);
        password.setMandatory(true);
        PMManager.setup(this);
    }

    @Operation
    public void login() {
        System.out.println("sending username=" + username.getText() + ", password=" + password.getText());
    }

    @Validation(path = "login")
    public boolean isLoginValid() {
        return username.isValid() && password.isValid();
    }

}
