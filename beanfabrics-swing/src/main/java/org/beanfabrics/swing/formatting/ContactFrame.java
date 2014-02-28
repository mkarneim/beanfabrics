package org.beanfabrics.swing.formatting;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;

import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.Path;
import org.beanfabrics.View;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import org.beanfabrics.swing.BnButton;

import java.awt.GridBagConstraints;

import javax.swing.JButton;

import java.awt.Insets;

import javax.swing.JLabel;

import org.beanfabrics.swing.BnTextField;

import javax.swing.border.EmptyBorder;
import java.awt.Dimension;

/**
 * The {@link ContactFrame} is a {@link View} on a {@link ContactPM}.
 *
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org 
 */
@SuppressWarnings("serial")
public class ContactFrame extends JFrame implements View<ContactPM>, ModelSubscriber {
    
    public static void main(String[] args) {
        EventQueue.invokeLater( new Runnable() {
            
            @Override
            public void run() {
                ContactPM pm = new ContactPM();
                
                ContactFrame view = new ContactFrame();
                view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                view.setPresentationModel(pm);
                view.pack();
                view.setLocationRelativeTo(null);
                view.setVisible(true);
            }
        });
    }
    
    
	private final Link link = new Link(this);
	private ModelProvider localModelProvider;
	private JPanel centerPanel;
	private JLabel lblName;
	private BnTextField tfName;
	private JLabel lblPhone;
	private BnTextField tfPhone;

	/**
	 * Constructs a new <code>ContactFrame</code>.
	 */
	public ContactFrame() {
		super();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getCenterPanel(), BorderLayout.NORTH);
		//
	}
	
	/**
	 * Returns the local {@link ModelProvider} for this class.
	 * @return the local <code>ModelProvider</code>
	 * @wbp.nonvisual location=10,430
	 */
	protected ModelProvider getLocalModelProvider() {
		if (localModelProvider == null) {
			localModelProvider = new ModelProvider(); // @wb:location=10,430
			localModelProvider.setPresentationModelType(ContactPM.class);
		}
		return localModelProvider;
	}
	
	/** {@inheritDoc} */
	public ContactPM getPresentationModel() {
		return getLocalModelProvider().getPresentationModel();
	}

	/** {@inheritDoc} */
	public void setPresentationModel(ContactPM pModel) {
		getLocalModelProvider().setPresentationModel(pModel);
	}

	/** {@inheritDoc} */
	public IModelProvider getModelProvider() {
		return this.link.getModelProvider();
	}

	/** {@inheritDoc} */
	public void setModelProvider(IModelProvider modelProvider) {
		this.link.setModelProvider(modelProvider);
	}

	/** {@inheritDoc} */
	public Path getPath() {
		return this.link.getPath();
	}

	/** {@inheritDoc} */
	public void setPath(Path path) {
		this.link.setPath(path);
	}
	
    private JPanel getCenterPanel() {
        if (centerPanel == null) {
        	centerPanel = new JPanel();
        	centerPanel.setPreferredSize(new Dimension(400, 300));
        	centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        	GridBagLayout gbl_centerPanel = new GridBagLayout();
        	gbl_centerPanel.columnWidths = new int[]{89, 0, 0};
        	gbl_centerPanel.rowHeights = new int[]{0, 0, 0};
        	gbl_centerPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        	gbl_centerPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        	centerPanel.setLayout(gbl_centerPanel);
        	GridBagConstraints gbc_lblName = new GridBagConstraints();
        	gbc_lblName.insets = new Insets(0, 0, 5, 5);
        	gbc_lblName.anchor = GridBagConstraints.EAST;
        	gbc_lblName.gridx = 0;
        	gbc_lblName.gridy = 0;
        	centerPanel.add(getLblName(), gbc_lblName);
        	GridBagConstraints gbc_tfName = new GridBagConstraints();
        	gbc_tfName.insets = new Insets(0, 0, 5, 0);
        	gbc_tfName.fill = GridBagConstraints.HORIZONTAL;
        	gbc_tfName.gridx = 1;
        	gbc_tfName.gridy = 0;
        	centerPanel.add(getTfName(), gbc_tfName);
        	GridBagConstraints gbc_lblPhone = new GridBagConstraints();
        	gbc_lblPhone.anchor = GridBagConstraints.EAST;
        	gbc_lblPhone.insets = new Insets(0, 0, 0, 5);
        	gbc_lblPhone.gridx = 0;
        	gbc_lblPhone.gridy = 1;
        	centerPanel.add(getLblPhone(), gbc_lblPhone);
        	GridBagConstraints gbc_tfPhone = new GridBagConstraints();
        	gbc_tfPhone.fill = GridBagConstraints.HORIZONTAL;
        	gbc_tfPhone.gridx = 1;
        	gbc_tfPhone.gridy = 1;
        	centerPanel.add(getTfPhone(), gbc_tfPhone);
        }
        return centerPanel;
    }
    private JLabel getLblName() {
        if (lblName == null) {
        	lblName = new JLabel("Name");
        }
        return lblName;
    }
    private BnTextField getTfName() {
        if (tfName == null) {
        	tfName = new BnTextField();
        	tfName.setPath(new Path("this.name"));
        	tfName.setModelProvider(getLocalModelProvider());
        }
        return tfName;
    }
    private JLabel getLblPhone() {
        if (lblPhone == null) {
        	lblPhone = new JLabel("Phone");
        }
        return lblPhone;
    }
    private BnTextField getTfPhone() {
        if (tfPhone == null) {
        	tfPhone = new BnTextField();
        	tfPhone.setPath(new Path("this.phone"));
        	tfPhone.setModelProvider(getLocalModelProvider());
        }
        return tfPhone;
    }
}