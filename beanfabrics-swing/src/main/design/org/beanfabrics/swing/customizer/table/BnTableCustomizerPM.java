/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

import org.beanfabrics.Path;
import org.beanfabrics.meta.PathElementInfo;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Validation;
import org.beanfabrics.swing.customizer.AbstractCustomizerPM;
import org.beanfabrics.swing.customizer.CustomizerBase;
import org.beanfabrics.swing.customizer.path.PathPM;
import org.beanfabrics.swing.customizer.util.CustomizerUtil;
import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.swing.table.BnTable;

/**
 * The <code>BnTableCustomizerPM</code> is the presentation model for the {@link BnTableCustomizer}.
 * 
 * @author Michael Karneim
 */
public class BnTableCustomizerPM extends AbstractCustomizerPM {
    private BnTable bnTable;
    
    protected final PathPM path = new PathPM();
    protected final OperationPM configureColumns = new OperationPM();

    public BnTableCustomizerPM() {
        PMManager.setup(this);
    }

    @Override
    public void setCustomizer(CustomizerBase customizer) {
        super.setCustomizer(customizer);
        setBnTable((BnTable) customizer.getObject());
    }

    private void setBnTable(BnTable bnTable) {
        this.bnTable = bnTable;
        this.path.setPathContext(CustomizerUtil.getPathContextFromBnComponent(bnTable));
        revalidateProperties();
    }

    @Validation(path = "configureColumns")
    public boolean canConfigureColumns() {
        return !path.isEmpty() && path.isValid();
    }

    @Operation
    public void configureColumns() {
        configureColumns.check();
        PathElementInfo rowPMRootPathInfo = CustomizerUtil.getPathInfo(CustomizerUtil.getRowPmType(bnTable));
        final ColumnListConfigurationConstroller ctrl = new ColumnListConfigurationConstroller( getContext(), rowPMRootPathInfo);
        
        ctrl.getPresentationModel().setData(bnTable.getColumns());
        ctrl.getPresentationModel().onApply(new ColumnListConfigurationPM.OnApplyHandler() {
            public void apply() {
                BnColumn[] oldValue = bnTable.getColumns();
                BnColumn[] newValue = ctrl.getPresentationModel().getData();
                bnTable.setColumns(newValue);
                getCustomizer().firePropertyChange("columns", oldValue, newValue);
            }
        });
        ctrl.getView().setModal(true);
        ctrl.getView().setVisible(true);
    }

    @OnChange(path = "path")
    void applyPath() {
        if (path.isValid() && bnTable != null && getCustomizer() != null) {
            Path oldValue = bnTable.getPath();
            Path newValue = path.getPath();
            bnTable.setPath(newValue);
            getCustomizer().firePropertyChange("path", oldValue, newValue);
        }
    }

}