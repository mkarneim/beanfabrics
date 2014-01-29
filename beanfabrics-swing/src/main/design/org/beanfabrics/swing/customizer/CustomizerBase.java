package org.beanfabrics.swing.customizer;

import java.beans.Customizer;

public interface CustomizerBase extends Customizer {
    void firePropertyChange(String propertyName, Object oldValue, Object newValue);

    Object getObject();
    
    void showMessage(String message);
    
    void showException(Throwable t);
}
