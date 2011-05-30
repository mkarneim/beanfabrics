package org.beanfabrics.swt.samples.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * The {@link RunLoginDialog} is a sample program that demonstrates the use of
 * the {@link LoginDialog}.
 * 
 * @author Michael Karneim
 */
public class RunLoginDialog {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);

        LoginPM pm = new LoginPM();
        LoginDialog dlg = new LoginDialog(shell, SWT.PRIMARY_MODAL | SWT.TITLE);
        dlg.setPresentationModel(pm);
        dlg.open();

        System.out.println("User has entered:\nusername: " + pm.username.getText() + "\npassword: " + pm.password.getText());

        display.dispose();
    }
}
