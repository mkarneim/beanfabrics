package org.beanfabrics.swt.samples.login;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swt.Decorator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
/**
 * The {@link LoginDialog} is a dialog for entering user credentials.
 *
 * @author Michael Karneim
 */
public class LoginDialog extends Dialog implements View<LoginPM> {
    private final ModelProvider modelProvider = new ModelProvider();
    protected Object result;
    protected Shell shlLogin;
    private Text tfUsername;
    private Text tfPassword;
    private Button btnLogin;
    private Button btnCancel;

    /**
     * Create the dialog.
     * @param parent
     * @param style
     */
    public LoginDialog(Shell parent, int style) {
        super(parent, style);
        setText("SWT Dialog");
    }

    /**
     * Open the dialog.
     * @return the result
     */
    public Object open() {
        createContents();
        bindContents();
        shlLogin.open();
        shlLogin.layout();
        Display display = getParent().getDisplay();
        while (!shlLogin.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        shlLogin = new Shell(getParent(), getStyle());
        shlLogin.setSize(319, 166);
        shlLogin.setText("Login");

        tfUsername = new Text(shlLogin, SWT.BORDER);
        tfUsername.setBounds(65, 13, 150, 19);

        Label lblUsername = new Label(shlLogin, SWT.NONE);
        lblUsername.setBounds(10, 16, 49, 13);
        lblUsername.setText("Username");

        Label lblPassword = new Label(shlLogin, SWT.NONE);
        lblPassword.setBounds(10, 41, 49, 13);
        lblPassword.setText("Password");

        tfPassword = new Text(shlLogin, SWT.BORDER | SWT.PASSWORD);
        tfPassword.setBounds(65, 38, 150, 19);

        btnLogin = new Button(shlLogin, SWT.NONE);
        btnLogin.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                getParent().close();
            }
        });
        btnLogin.setBounds(10, 108, 68, 23);
        btnLogin.setText("Login");

        btnCancel = new Button(shlLogin, SWT.NONE);
        btnCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                getParent().close();
            }
        });
        btnCancel.setBounds(235, 108, 68, 23);
        btnCancel.setText("Cancel");
    }

    private void bindContents() {
        Decorator deco = new Decorator(modelProvider);
        deco.decorateText(tfUsername, new Path("username"));
        deco.decorateText(tfPassword, new Path("password"));
        deco.decoratePushButton(btnLogin, new Path("login"));
    }

    public LoginPM getPresentationModel() {
        return modelProvider.getPresentationModel();
    }

    public void setPresentationModel(LoginPM pModel) {
        modelProvider.setPresentationModel(pModel);
    }
}
