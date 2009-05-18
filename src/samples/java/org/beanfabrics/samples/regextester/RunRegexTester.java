package org.beanfabrics.samples.regextester;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class RunRegexTester {
    
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
        EventQueue.invokeLater( new Runnable() {
        
            public void run() {
                RegexTesterPM model = new RegexTesterPM();
                model.input.setText("This text contais 1 number.");
                model.regex.setText("[A-Za-z]+");
                
                
                RegexTesterPanel panel = new RegexTesterPanel();
                panel.setPresentationModel(model);
                
                JFrame frame = new JFrame("Regex-Tester");
                frame.add(panel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(500, 400);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
        
            }
        });
    }
}
