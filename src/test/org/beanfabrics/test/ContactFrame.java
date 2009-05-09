/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import java.awt.BorderLayout;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class ContactFrame extends JFrame implements View<ContactPM>, ModelSubscriber {
	private final Link link = new Link(this);
	private ModelProvider localProvider; //@jve:decl-index=0:visual-constraint="464,32"
	private JPanel jContentPane;
	private JSplitPane jSplitPane;
	private ContactPanel contactPanel;
	private AddressPanel addressPanel;

	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		ContactFrame f = new ContactFrame();
		ContactPM pModel = new ContactPM();
		pModel.firstname.setText("Charly");
		pModel.lastname.setText("Chaplin");
		pModel.birthday.setText("16.4.1889");
		pModel.address.country.setText("USA");

		ModelProvider provider = new ModelProvider();
		provider.setPresentationModel(pModel);
		f.setModelProvider(provider);
		f.setPath(new Path("this"));

		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	/**
	 * This is the default constructor.
	 */
	public ContactFrame() {
		initialize();
	}

	/** {@inheritDoc} */
	public ContactPM getPresentationModel() {
		return getLocalProvider().getPresentationModel();
	}

	/** {@inheritDoc} */
	public void setPresentationModel(ContactPM pModel) {
		getLocalProvider().setPresentationModel(pModel);
	}

	/** {@inheritDoc} */
	public IModelProvider getModelProvider() {
		return link.getModelProvider();
	}

	/** {@inheritDoc} */
	public void setModelProvider(IModelProvider provider) {
		this.link.setModelProvider(provider);
	}

	/** {@inheritDoc} */
	public Path getPath() {
		return link.getPath();
	}

	/** {@inheritDoc} */
	public void setPath(Path path) {
		this.link.setPath(path);
	}

	/**
	 * This method initializes this.
	 */
	private void initialize() {
		this.setSize(417, 269);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane.
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJSplitPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes <code>localProvider</code>.
	 */
	private ModelProvider getLocalProvider() {
		if (localProvider == null) {
			localProvider = new ModelProvider();
			localProvider.setPresentationModelType(ContactPM.class);
		}
		return localProvider;
	}

	/**
	 * This method initializes jSplitPane.
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setDividerLocation(200);
			jSplitPane.setLeftComponent(getContactPanel());
			jSplitPane.setRightComponent(getAddressPanel());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes contactPanel.
	 */
	private ContactPanel getContactPanel() {
		if (contactPanel == null) {
			contactPanel = new ContactPanel();
			contactPanel.setPath(new Path("this"));
			contactPanel.setModelProvider(getLocalProvider());
		}
		return contactPanel;
	}

	/**
	 * This method initializes addressPanel.
	 */
	private AddressPanel getAddressPanel() {
		if (addressPanel == null) {
			addressPanel = new AddressPanel();
			addressPanel.setPath(new Path("address"));
			addressPanel.setModelProvider(getLocalProvider());
		}
		return addressPanel;
	}
}