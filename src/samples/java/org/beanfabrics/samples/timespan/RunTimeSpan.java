package org.beanfabrics.samples.timespan;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class RunTimeSpan {
    public static void main(String[] args) {
        
        EventQueue.invokeLater( new Runnable() {
        
            public void run() {
                
                TimeSpanPM model = new TimeSpanPM();
                TimeSpanPanel view = new TimeSpanPanel();
                view.setPresentationModel(model);
                
                JFrame frame = new JFrame("Time span");
                frame.add(view);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(500, 400);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                
        
            }
        } );
        
    }
}
