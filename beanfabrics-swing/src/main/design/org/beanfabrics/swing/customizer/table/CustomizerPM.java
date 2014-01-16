package org.beanfabrics.swing.customizer.table;

import org.beanfabrics.model.PresentationModel;
// FIXME apply on AbstractCustomizerPM
public interface CustomizerPM extends PresentationModel {
    void setCustomizer(CustomizerBase customizer);
}
