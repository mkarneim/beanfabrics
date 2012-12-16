package org.beanfabrics.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.Path;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class BnProgressBarTestGUI extends JFrame {

	public static class SimplePM extends AbstractPM {
		
		IntegerPM value = new IntegerPM();
		
		public SimplePM() {
			PMManager.setup(this);
			value.setDescription("Hello");
		}
	}
	
	private JPanel contentPane;
	/**
	 * @wbp.nonvisual location=49,361
	 */
	private final ModelProvider localModelProvider = new ModelProvider();
	/**
	 * @wbp.nonvisual location=21,431
	 */
	private final SimplePM simplePM = new SimplePM();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BnProgressBarTestGUI frame = new BnProgressBarTestGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public BnProgressBarTestGUI() {
		localModelProvider.setPresentationModelType(SimplePM.class);
		localModelProvider.setPresentationModel(simplePM);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel lowerPanel = new JPanel();
		contentPane.add(lowerPanel, BorderLayout.SOUTH);
		lowerPanel.setLayout(new BorderLayout(0, 0));
		
		BnProgressBar bnProgressBar = new BnProgressBar();
		bnProgressBar.setPath(new Path("this.value"));
		bnProgressBar.setModelProvider(localModelProvider);
		lowerPanel.add(bnProgressBar, BorderLayout.NORTH);
		
		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		GridBagLayout gbl_centerPanel = new GridBagLayout();
		gbl_centerPanel.columnWidths = new int[]{0, 0, 0};
		gbl_centerPanel.rowHeights = new int[]{0, 0, 0};
		gbl_centerPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_centerPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		centerPanel.setLayout(gbl_centerPanel);
		
		BnTextField bnTextField = new BnTextField();
		bnTextField.setPath(new Path("this.value"));
		bnTextField.setModelProvider(localModelProvider);
		GridBagConstraints gbc_bnTextField = new GridBagConstraints();
		gbc_bnTextField.insets = new Insets(0, 0, 5, 0);
		gbc_bnTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_bnTextField.gridx = 1;
		gbc_bnTextField.gridy = 0;
		centerPanel.add(bnTextField, gbc_bnTextField);
	}
	
	

}
