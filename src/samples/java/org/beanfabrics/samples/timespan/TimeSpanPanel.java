package org.beanfabrics.samples.timespan;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnTextField;

/*
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
@SuppressWarnings("serial")
public class TimeSpanPanel extends JPanel implements View<TimeSpanPM>, ModelSubscriber {
	private final Link link = new Link(this);
	private ModelProvider localModelProvider;
	private JPanel panel;
	private JLabel startLabel;
	private BnTextField startTextField;
	private JLabel endLabel;
	private BnTextField endTextField;
	private JLabel daysLabel;
	private BnTextField daysTextField;

	/**
	 * Constructs a new <code>TimeSpanPanel</code>.
	 */
	public TimeSpanPanel() {
		super();
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(8, 8, 8, 8));
		add(getPanel(), BorderLayout.CENTER);
	}

	/**
	 * Returns the local {@link ModelProvider} for this class.
	 *
	 * @return the local <code>ModelProvider</code>
	 */
	protected ModelProvider getLocalModelProvider() {
		if (localModelProvider == null) {
			localModelProvider = new ModelProvider(); // @wb:location=10,430
			localModelProvider.setPresentationModelType(TimeSpanPM.class);
		}
		return localModelProvider;
	}

	/** {@inheritDoc} */
	public TimeSpanPM getPresentationModel() {
		return getLocalModelProvider().getPresentationModel();
	}

	/** {@inheritDoc} */
	public void setPresentationModel(TimeSpanPM pModel) {
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

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel(new GridBagLayout());
			panel.add(getStartLabel(), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			panel.add(getStartTextField(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			panel.add(getEndLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			panel.add(getEndTextField(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			panel.add(new JSeparator(), new GridBagConstraints(0, 2, 2, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			panel.add(getDaysLabel(), new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			panel.add(getDaysTextField(), new GridBagConstraints(1, 3, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			panel.add(new JPanel(), new GridBagConstraints(0, 4, 2, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return panel;
	}

	private JLabel getStartLabel() {
    	if (startLabel == null) {
    		startLabel = new JLabel();
    		startLabel.setText("Start:");
    	}
    	return startLabel;
    }

	private BnTextField getStartTextField() {
		if (startTextField == null) {
			startTextField = new BnTextField();
			startTextField.setPath(new org.beanfabrics.Path("this.start"));
			startTextField.setModelProvider(getLocalModelProvider());
			startTextField.setColumns(10);
		}
		return startTextField;
	}

	private JLabel getEndLabel() {
		if (endLabel == null) {
			endLabel = new JLabel();
			endLabel.setText("End:");
		}
		return endLabel;
	}

	private BnTextField getEndTextField() {
		if (endTextField == null) {
			endTextField = new BnTextField();
			endTextField.setPath(new org.beanfabrics.Path("this.end"));
			endTextField.setModelProvider(getLocalModelProvider());
			endTextField.setColumns(10);
		}
		return endTextField;
	}

	private JLabel getDaysLabel() {
		if (daysLabel == null) {
	        daysLabel = new JLabel();
	        daysLabel.setText("Days:");
        }
        return daysLabel;
	}

	private BnTextField getDaysTextField() {
		if (daysTextField == null) {
	        daysTextField = new BnTextField();
	        daysTextField.setPath(new Path("this.days"));
	        daysTextField.setModelProvider(getLocalModelProvider());
        }
        return daysTextField;
	}
}