package org.beanfabrics.samples.timespan;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnTextField;

/*
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
@SuppressWarnings("serial")
public class TimeSpanPanel extends JPanel implements View<TimeSpanPM>, ModelSubscriber {
    private BnTextField endTextField;
    private JLabel endLabel;
    private JLabel startLabel;
    private BnTextField startTextField;
    private JPanel panel;
    private final Link link = new Link(this);
    private ModelProvider localModelProvider;

    /**
     * Constructs a new <code>TimeSpanPanel</code>.
     */
    public TimeSpanPanel() {
        super();
        setLayout(new BorderLayout());
        add(getPanel(), BorderLayout.CENTER);
        //
    }

    /**
     * Returns the local {@link ModelProvider} for this class.
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
    /**
     * @return
     */
    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.add(getStartLabel());
            panel.add(getStartTextField());
            panel.add(getEndLabel());
            panel.add(getEndTextField());
        }
        return panel;
    }
    /**
     * @return
     */
    private BnTextField getStartTextField() {
        if (startTextField == null) {
            startTextField = new BnTextField();
            startTextField.setPath(new org.beanfabrics.Path("this.start"));
            startTextField.setModelProvider(getLocalModelProvider());
            startTextField.setColumns(10);
        }
        return startTextField;
    }
    /**
     * @return
     */
    private JLabel getStartLabel() {
        if (startLabel == null) {
            startLabel = new JLabel();
            startLabel.setText("Start");
        }
        return startLabel;
    }
    /**
     * @return
     */
    private JLabel getEndLabel() {
        if (endLabel == null) {
            endLabel = new JLabel();
            endLabel.setText("End");
        }
        return endLabel;
    }
    /**
     * @return
     */
    private BnTextField getEndTextField() {
        if (endTextField == null) {
            endTextField = new BnTextField();
            endTextField.setPath(new org.beanfabrics.Path("this.end"));
            endTextField.setModelProvider(getLocalModelProvider());
            endTextField.setColumns(10);
        }
        return endTextField;
    }

}