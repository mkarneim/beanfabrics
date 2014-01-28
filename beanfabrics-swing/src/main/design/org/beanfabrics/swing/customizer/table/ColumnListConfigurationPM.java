package org.beanfabrics.swing.customizer.table;

import org.beanfabrics.meta.PathNode;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Validation;
import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.validation.ValidationState;

public class ColumnListConfigurationPM extends AbstractPM {
    public interface OnApplyHandler {
        public void apply();
    }

    ColumnListPM list = new ColumnListPM();
    OperationPM apply = new OperationPM();
    OnApplyHandler onApplyHandler;

    public ColumnListConfigurationPM() {
        PMManager.setup(this);
    }

    public void onApply(OnApplyHandler handler) {
        onApplyHandler = handler;
        revalidateProperties();
    }

    public void setData(BnColumn[] columns) {
        list.setData(columns);
    }

    public BnColumn[] getData() {
        return list.getData();
    }

    public void setRootPathInfo(PathNode rootPathInfo) {
        list.setRootPathInfo(rootPathInfo);
    }

    @Operation
    public void apply() {
        onApplyHandler.apply();
    }
    
    @Validation(path = "apply",message="Missing callback functions")
    boolean isApplicable() {
        return onApplyHandler != null;
    }
    
    @Validation(path = "apply")
    ValidationState getPathBrowserValidationState() {
        return list.getValidationState();
    }

}
