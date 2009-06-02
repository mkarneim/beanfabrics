package org.beanfabrics.samples.properties;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class RunProperties {
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                PropertiesPM model = new PropertiesPM();
                PropertiesPanel view = new PropertiesPanel();
                view.setPresentationModel(model);
                
                JFrame frame = new JFrame("System Properties");
                frame.add(view);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(500, 400);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
        
                // load data
                model.setProperties( System.getProperties());
            }
        });
    }
}
