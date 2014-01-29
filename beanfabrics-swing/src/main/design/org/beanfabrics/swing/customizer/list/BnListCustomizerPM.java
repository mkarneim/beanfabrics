/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.list;

import static org.beanfabrics.swing.customizer.util.CustomizerUtil.getPathContextToCustomizeModelSubscriber;

import org.beanfabrics.Path;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.swing.customizer.AbstractCustomizerPM;
import org.beanfabrics.swing.customizer.CustomizerBase;
import org.beanfabrics.swing.customizer.path.PathContext;
import org.beanfabrics.swing.customizer.path.PathPM;
import org.beanfabrics.swing.customizer.util.CustomizerUtil;
import org.beanfabrics.swing.list.BnList;
import org.beanfabrics.swing.list.CellConfig;

/**
 * The <code>BnListCustomizerPM</code> is the presentation model for the {@link BnListCustomizer}.
 * 
 * @author Michael Karneim
 */
public class BnListCustomizerPM extends AbstractCustomizerPM {

    private CustomizerBase customizer;
    private BnList bnList;

    protected final PathPM pathToList = new PathPM();
    protected final PathPM cellConfigPath = new PathPM();

    public BnListCustomizerPM() {
        // this.title.setText("This is the Beanfabrics customizer for the "+BnList.class.getName()+" component.");
        PMManager.setup(this);
    }

    public void setCustomizer(CustomizerBase customizer) {
        this.customizer = customizer;
        setBnList((BnList) customizer.getObject());
    }

    public void setBnList(BnList bnList) {
        this.bnList = bnList;
        // Attention: order is relevant
        this.pathToList.setData(bnList.getPath()); // 1
        this.pathToList.setPathContext(getPathContextToCustomizeModelSubscriber(bnList)); // 2

        revalidateProperties();
        configureCellConfigPath();
    }

    @OnChange(path = "pathToList")
    void applyPathToList() {
        if (pathToList.isValid() && bnList != null && customizer != null) {
            Path oldValue = bnList.getPath();
            Path newValue = pathToList.getData();
            bnList.setPath(newValue);
            customizer.firePropertyChange("path", oldValue, newValue);
        }
        configureCellConfigPath();
    }

    @OnChange(path = "cellConfigPath")
    void applyPathToRowPm() {
        if (cellConfigPath.isValid() && bnList != null && customizer != null) {
            CellConfig oldValue = bnList.getCellConfig();
            CellConfig newValue;
            if (!cellConfigPath.isEmpty()) {
                newValue = new CellConfig(cellConfigPath.getData());
            } else {
                newValue = null;
            }
            bnList.setCellConfig(newValue);
            customizer.firePropertyChange("cellConfig", oldValue, newValue);
        }
    }

    private void configureCellConfigPath() {
        if (bnList != null) {
            // Attention: order is relevant
            Path initialPath = getCellConfigPath(this.bnList.getCellConfig());
            this.cellConfigPath.setData(initialPath); // 1
            this.cellConfigPath.setPathContext(new PathContext(CustomizerUtil.asRootNode(CustomizerUtil
                    .getElementTypeOfSubscribedOrActualIListPM(bnList)), null)); // 2
        } else {
            this.cellConfigPath.setData(null);
        }
    }

    private Path getCellConfigPath(CellConfig cellConfig) {
        if (cellConfig == null) {
            return null;
        } else {
            return cellConfig.getPath();
        }
    }

}