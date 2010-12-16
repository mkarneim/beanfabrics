package org.beanfabrics.swing.customizer.table;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Validation;
import org.beanfabrics.swing.table.BnColumn;

public class ColumnListConfigurationPM extends AbstractPM {
    public interface Model {
        ColumnListContext getColumnListContext();

        void apply(BnColumn[] cols);
    }

    private Model model;

    ColumnListPM list = new ColumnListPM();
    OperationPM apply = new OperationPM();

    public ColumnListConfigurationPM(Model aModel) {
        PMManager.setup(this);
        setModel(aModel);
    }

    public void setModel(Model aModel) {
        this.model = aModel;
        list.setColumnListContext(aModel.getColumnListContext());
    }

    @Operation
    public void apply() {
        model.apply(list.getData());
    }

    @Validation(path = "apply")
    public boolean canApply() {
        return list.isValid();
    }

}
