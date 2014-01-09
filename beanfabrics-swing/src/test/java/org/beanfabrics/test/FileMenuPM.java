package org.beanfabrics.test;

import java.util.ArrayList;
import java.util.List;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.Operation;

public class FileMenuPM extends AbstractPM {
    private final List<String> calledOperationNames = new ArrayList<String>();
    OperationPM newFile = new OperationPM();
    OperationPM saveFile = new OperationPM();
    OperationPM quit = new OperationPM();
    BooleanPM autoSaveOnQuit = new BooleanPM();

    public FileMenuPM() {
        PMManager.setup(this);
    }

    @Operation
    public void newFile() {
        calledOperationNames.add("newFile");
    }

    @Operation
    public void saveFile() {
        calledOperationNames.add("saveFile");
    }

    @Operation
    public void quit() {
        calledOperationNames.add("quit");
    }
    
    public List<String> getCalledOperationNames() {
        return calledOperationNames;
    }
}
