package org.beanfabrics.samples.login;

import java.awt.EventQueue;

/**
 * This sample applications runs the LoginDialog.
 */
public class RunLogin {
	
	public static void main(String[] args) {
		EventQueue.invokeLater( new Runnable() {
			public void run() {
				
				LoginPM model = new LoginPM();
				LoginDialog dlg = new LoginDialog();
				
				dlg.setPresentationModel(model);
				dlg.setVisible(true);		

			}
		});
	}
}
