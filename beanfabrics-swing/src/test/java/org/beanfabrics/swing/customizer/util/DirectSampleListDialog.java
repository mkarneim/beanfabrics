package org.beanfabrics.swing.customizer.util;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import org.beanfabrics.swing.table.BnTable;

@SuppressWarnings("serial")
public class DirectSampleListDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private SampleListPM sampleListPM;
    private JScrollPane scrollPane;
    private BnTable bnTable;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            DirectSampleListDialog dialog = new DirectSampleListDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public DirectSampleListDialog() {
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        contentPanel.add(getScrollPane(), BorderLayout.CENTER);
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

    /**
     * @wbp.nonvisual location=76,371
     */
    private SampleListPM getSampleListPM() {
        if (sampleListPM == null) {
        	sampleListPM = new SampleListPM();
        }
        return sampleListPM;
    }
    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
        	scrollPane = new JScrollPane();
        	scrollPane.setViewportView(getBnTable());
        }
        return scrollPane;
    }
    private BnTable getBnTable() {
        if (bnTable == null) {
        	bnTable = new BnTable();
        	bnTable.setPresentationModel(getSampleListPM());
        }
        return bnTable;
    }
}
