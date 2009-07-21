package org.beanfabrics.samples.login;

import javax.swing.JDialog;
import javax.swing.JLabel;
import org.beanfabrics.ModelProvider;

import org.beanfabrics.View;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.BnPasswordField;
import org.beanfabrics.swing.BnTextField;

@SuppressWarnings("serial")
public class LoginDialog extends JDialog implements View<LoginPM> {
    private ModelProvider localModelProvider = new ModelProvider(); // @wb:location=8,202

    private BnTextField tfUsername = new BnTextField();
    private BnPasswordField tfPassword = new BnPasswordField();
    private BnButton btnLogin = new BnButton();
    private JLabel passwordLabel = new JLabel("Password");
    private JLabel usernameLabel = new JLabel("Username");

    /**
     * Create the dialog
     */
    public LoginDialog() {
        super();
        localModelProvider.setPresentationModelType(LoginPM.class);
        getContentPane().setLayout(null);
        setBounds(100, 100, 252, 174);
        usernameLabel.setBounds(36, 23, 68, 15);
        passwordLabel.setBounds(36, 49, 68, 15);

        tfUsername.setBounds(110, 21, 100, 20);
        tfUsername.setModelProvider(localModelProvider);
        tfUsername.setPath(new org.beanfabrics.Path("this.username"));

        tfPassword.setBounds(110, 47, 100, 20);
        tfPassword.setModelProvider(localModelProvider);
        tfPassword.setPath(new org.beanfabrics.Path("this.password"));

        btnLogin.setBounds(110, 91, 100, 25);
        btnLogin.setModelProvider(localModelProvider);
        btnLogin.setPath(new org.beanfabrics.Path("this.login"));
        btnLogin.setText("Login");

        getContentPane().add(usernameLabel);
        getContentPane().add(passwordLabel);
        getContentPane().add(tfUsername);
        getContentPane().add(tfPassword);
        getContentPane().add(btnLogin);

        getRootPane().setDefaultButton(btnLogin);
    }

    public LoginPM getPresentationModel() {
        return localModelProvider.getPresentationModel();
    }

    public void setPresentationModel(LoginPM model) {
        localModelProvider.setPresentationModel(model);
    }

}
