/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.list;

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
    protected final PathPM pathToRowCell = new PathPM();

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
        this.pathToList.setPathContext(CustomizerUtil.getPathContextFromBnComponent(bnList));

        revalidateProperties();
        configurePathToRowCell();
    }

    @OnChange(path = "pathToList")
    void applyPathToList() {
        if (pathToList.isValid() && bnList != null && customizer != null) {
            Path oldValue = bnList.getPath();
            Path newValue = pathToList.getPath();
            bnList.setPath(newValue);
            customizer.firePropertyChange("path", oldValue, newValue);
        }
        configurePathToRowCell();
    }

    @OnChange(path = "pathToRowCell")
    void applyCellConfig() {
        if (pathToRowCell.isValid() && bnList != null && customizer != null) {
            CellConfig oldValue = bnList.getCellConfig();
            CellConfig newValue;
            if (!pathToRowCell.isEmpty()) {
                newValue = new CellConfig(pathToRowCell.getPath());
            } else {
                newValue = null;
            }
            bnList.setCellConfig(newValue);
            customizer.firePropertyChange("cellConfig", oldValue, newValue);
        }
    }

    private void configurePathToRowCell() {
        if (bnList != null) {
            Path initialPath = getCellConfigPath(this.bnList.getCellConfig());
            this.pathToRowCell.setPathContext(new PathContext(CustomizerUtil.getPathInfo(CustomizerUtil
                    .getRowPmType(bnList)), null, initialPath));
        } else {
            this.pathToRowCell.setPath(null);
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