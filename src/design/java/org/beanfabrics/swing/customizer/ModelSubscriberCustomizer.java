/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.Customizer;

import javax.swing.JPanel;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.swing.BnLabel;
import org.beanfabrics.swing.customizer.path.PathBrowserPanel;
import org.beanfabrics.swing.customizer.util.CustomizerBasePanel;
import org.beanfabrics.swing.customizer.util.CustomizerUtil;
/**
 * The <code>ModelSubscriberCustomizer</code> is a Java Beans {@link Customizer} for a {@link ModelSubscriber}.
 *
 * @author Michael Karneim
 */
public class ModelSubscriberCustomizer extends CustomizerBasePanel implements Customizer {

	private JPanel panel_1;
	private BnLabel bnLabel;
	private JPanel headerPanel;
	private PathBrowserPanel pathChooserPanel;
	private JPanel centerPanel;
	private ModelProvider localProvider;
	private ModelSubscriberCustomizerPM pModel;

	public ModelSubscriberCustomizer() {
		this.pModel = new ModelSubscriberCustomizerPM();
		this.getLocalProvider().setPresentationModel(this.pModel);
		this.setLayout( new BorderLayout());
		this.add( this.getCenterPanel());
		setBackground( this.getDefaultBackground());

		CustomizerUtil.get().setupGUI(this);
	}

	public void setObject(Object bean) {
		if (bean == null || bean instanceof ModelSubscriber) {
			try {
				setModelSubscriber((ModelSubscriber)bean);
			} catch ( Throwable t) {
				showException(t);
			}
		} else {
			showMessage("Can't customize this instance of " + bean.getClass().getName()
					+". Expected instance of "+ModelSubscriber.class.getName());
		}
	}

	public void setModelSubscriber( final ModelSubscriber aSubscriber) {
		this.pModel.setFunctions(new ModelSubscriberCustomizerPM.Functions() {
			public void setPath(Path path) {
				Path oldValue = aSubscriber.getPath();
				aSubscriber.setPath(path);
				ModelSubscriberCustomizer.this.firePropertyChange("path",oldValue, path);
			}
		});
		this.pModel.setModelSubscriber(aSubscriber);
	}

	private JPanel getCenterPanel() {
		if (centerPanel == null) {
			centerPanel = new JPanel();
			centerPanel.setLayout(new BorderLayout());
			centerPanel.add(getPathChooserPanel(), BorderLayout.CENTER);
			centerPanel.add(getHeaderPanel(), BorderLayout.NORTH);
			centerPanel.setOpaque(false);
		}
		return centerPanel;
	}

	private PathBrowserPanel getPathChooserPanel() {
		if (pathChooserPanel == null) {
			pathChooserPanel = new PathBrowserPanel();
			pathChooserPanel.setPath(new org.beanfabrics.Path("this.pathBrowser"));
			pathChooserPanel.setModelProvider(getLocalProvider());
			pathChooserPanel.setOpaque(false);
		}
		return pathChooserPanel;
	}

	/**
	 * Returns the local {@link ModelProvider} for this class.
	 * @return the local <code>ModelProvider</code>
	 */
	protected ModelProvider getLocalProvider() {
		if (localProvider == null) {
			localProvider = new ModelProvider(); // @wb:location=16,477
			localProvider.setPresentationModelType(ModelSubscriberCustomizerPM.class);
		}
		return localProvider;
	}

	private JPanel getHeaderPanel() {
		if (headerPanel == null) {
			headerPanel = new JPanel();
			final GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.rowHeights = new int[] {7,7};
			gridBagLayout.columnWidths = new int[] {7};
			headerPanel.setLayout(gridBagLayout);
			headerPanel.setBackground(Color.WHITE);
			final GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridx = 1;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.insets = new Insets(4, 4, 4, 4);
			final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
			gridBagConstraints_1.ipady = 64;
			gridBagConstraints_1.insets = new Insets(4, 4, 4, 4);
			gridBagConstraints_1.gridy = 0;
			gridBagConstraints_1.gridx = 0;
			headerPanel.add(getPanel_1(), gridBagConstraints_1);
			headerPanel.add(getBnLabel(), gridBagConstraints);
		}
		return headerPanel;
	}

	private BnLabel getBnLabel() {
		if (bnLabel == null) {
			bnLabel = new BnLabel();
			bnLabel.setPreferredSize(new Dimension(500, 0));
			bnLabel.setPath(new org.beanfabrics.Path("this.status"));
			bnLabel.setModelProvider(getLocalProvider());
			bnLabel.setText("New BnLabel");
		}
		return bnLabel;
	}

	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.setOpaque(false);
		}
		return panel_1;
	}

	private Color getDefaultBackground() {
		if ( CustomizerUtil.get().isAquaLookAndFeel()) {
			return new Color( 232,232,232);
		} else {
			return getBackground();
		}
	}
}