/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.ViewClassDecorator;
import org.beanfabrics.meta.PathInfo;
import org.beanfabrics.model.IListPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Validation;
import org.beanfabrics.swing.customizer.path.PathContext;
import org.beanfabrics.swing.customizer.path.PathPM;
import org.beanfabrics.swing.customizer.util.AbstractCustomizerPM;
import org.beanfabrics.swing.customizer.util.CustomizerUtil;
import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.swing.table.BnTable;

/**
 * The <code>BnTableCustomizerPM</code> is the presentation model for the
 * {@link BnTableCustomizer}.
 * 
 * @author Michael Karneim
 */
public class BnTableCustomizerPM extends AbstractCustomizerPM {
    protected final PathPM path = new PathPM();

    protected final OperationPM configureColumns = new OperationPM();

    public interface Functions {
        void setPath(Path path);

        void setBnColumns(BnColumn[] cols);
    }

    private Functions functions;
    private BnTable bnTable;
    private Class<? extends PresentationModel> rootModelType;
    private Class<? extends PresentationModel> requiredModelType;

    public BnTableCustomizerPM() {
        PMManager.setup(this);
    }

    public void setFunctions(Functions functions) {
        this.functions = functions;
    }

    public void setBnTable(BnTable bnTable) {
        this.bnTable = bnTable;
        IModelProvider ds = this.bnTable.getModelProvider();
        if (ds != null) {
            this.rootModelType = ds.getPresentationModelType();
        } else {
            IListPM list = this.bnTable.getPresentationModel();
            if (list != null) {
                this.rootModelType = list.getClass();
            }
        }

        ViewClassDecorator viewDeco = new ViewClassDecorator(bnTable.getClass());
        this.requiredModelType = viewDeco.getExpectedModelType();

        configurePath();
        revalidateProperties();
    }

    private void configurePath() {
        this.path.setPathContext(this.getPathContext());
    }

    @Validation(path = "configureColumns")
    public boolean canConfigureColumns() {
        return path.isValid();
    }

    @Operation
    public void configureColumns() {
        configureColumns.check();

        final ColumnListContext columnListContext;
        Class elementModelType = getElementModelType();
        if (elementModelType == null) {
            columnListContext = new ColumnListContext(null, bnTable.getColumns());
        } else {
            PathInfo elementRootPathInfo = PMManager.getInstance().getMetadata().getPathInfo(elementModelType);
            columnListContext = new ColumnListContext(elementRootPathInfo, bnTable.getColumns());
        }

        ColumnListConfigurationPM pm = new ColumnListConfigurationPM(new ColumnListConfigurationPM.Model() {

            public ColumnListContext getColumnListContext() {
                return columnListContext;
            }

            public void apply(BnColumn[] cols) {
                functions.setBnColumns(cols);
            }
        });
        pm.getContext().addParent(this.getContext());

        CustomizerUtil.get().openColumnListConfigurationDialog(pm);

    }

    private Class getElementModelType() {
        PathInfo listPathInfo = getPathInfoOfList();
        if (listPathInfo == null) {
            return null;
        } else {
            Type tArg = listPathInfo.getTypeArguments(IListPM.class)[0];
            if (tArg instanceof Class) {
                return (Class)tArg;
            } else if (tArg instanceof TypeVariable) {
                // if no actual argument can be found, we just return the first bound.
                return (Class)((TypeVariable)tArg).getBounds()[0];
            } else {
                throw new IllegalStateException("Unexpected type: " + tArg.getClass().getName());
            }
        }
    }

    private PathInfo getPathInfoOfList() {
        IModelProvider ds = this.bnTable.getModelProvider();
        if (ds != null) {
            Path path = this.bnTable.getPath();
            if (path != null) {
                Class cls = ds.getPresentationModelType();
                if (cls != null) {
                    PathInfo root = PMManager.getInstance().getMetadata().getPathInfo(cls);
                    return root.getPathInfo(path);
                }
            }
        }
        IListPM listPM = this.bnTable.getPresentationModel();
        if (listPM != null) {
            PathInfo result = PMManager.getInstance().getMetadata().getPathInfo(listPM.getClass());
            return result;
        } else {
            return null;
        }
    }

    private PathContext getPathContext() {
        PathContext result = new PathContext(PMManager.getInstance().getMetadata().getPathInfo(this.rootModelType), PMManager.getInstance().getMetadata().getPresentationModelInfo(this.requiredModelType), this.bnTable.getPath());
        return result;
    }

    @OnChange(path = "path")
    void applyPath() {
        if (functions != null && path.isValid()) {
            functions.setPath(path.getPath());
        }
    }
}