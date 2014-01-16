/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.table;

import java.lang.reflect.Type;

import javax.swing.text.DefaultEditorKit.CutAction;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.ViewClassDecorator;
import org.beanfabrics.meta.PathElementInfo;
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
import org.beanfabrics.util.GenericType;

/**
 * The <code>BnTableCustomizerPM</code> is the presentation model for the {@link BnTableCustomizer}.
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

    private BnTable bnTable;
    private Class<? extends PresentationModel> rootModelType;
    private Class<? extends PresentationModel> requiredModelType;

    private CustomizerBase customizer;

    public BnTableCustomizerPM() {
        PMManager.setup(this);
    }

    public void setCustomizer(CustomizerBase customizer) {
        this.customizer = customizer;
        setBnTable((BnTable) customizer.getObject());
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
            PathElementInfo rootPathElementInfo = PMManager.getInstance().getMetadata()
                    .getPathElementInfo(elementModelType);
            columnListContext = new ColumnListContext(rootPathElementInfo, bnTable.getColumns());
        }

        ColumnListConfigurationPM pm = new ColumnListConfigurationPM(new ColumnListConfigurationPM.Model() {

            public ColumnListContext getColumnListContext() {
                return columnListContext;
            }

            public void apply(BnColumn[] cols) {
                BnColumn[] oldValue = (bnTable == null ? null : bnTable.getColumns());
                bnTable.setColumns(cols);
                customizer.firePropertyChange("columns", oldValue, cols);
            }
        });
        pm.getContext().addParent(this.getContext());

        CustomizerUtil.get().openColumnListConfigurationDialog(pm);

    }

    private Class getElementModelType() {
        PathElementInfo listPathElementInfo = getPathElementInfoOfList();
        if (listPathElementInfo == null) {
            return null;
        } else {
            GenericType gt = listPathElementInfo.getGenericType();
            GenericType typeParam = gt.getTypeParameter(IListPM.class.getTypeParameters()[0]);
            Type tArg = typeParam.narrow(typeParam.getType(), PresentationModel.class);
            if (tArg instanceof Class) {
                return (Class) tArg;
            } else {
                throw new IllegalStateException("Unexpected type: " + tArg.getClass().getName());
            }
        }
    }

    private PathElementInfo getPathElementInfoOfList() {
        IModelProvider ds = this.bnTable.getModelProvider();
        if (ds != null) {
            Path path = this.bnTable.getPath();
            if (path != null) {
                Class cls = ds.getPresentationModelType();
                if (cls != null) {
                    PathElementInfo root = PMManager.getInstance().getMetadata().getPathElementInfo(cls);
                    return root.getPathInfo(path);
                }
            }
        }
        IListPM listPM = this.bnTable.getPresentationModel();
        if (listPM != null) {
            PathElementInfo result = PMManager.getInstance().getMetadata().getPathElementInfo(listPM.getClass());
            return result;
        } else {
            return null;
        }
    }

    private PathContext getPathContext() {
        PathContext result = new PathContext(PMManager.getInstance().getMetadata()
                .getPathElementInfo(this.rootModelType), PMManager.getInstance().getMetadata()
                .getTypeInfo(this.requiredModelType), this.bnTable.getPath());
        return result;
    }

    @OnChange(path = "path")
    void applyPath() {
        if (path.isValid() && bnTable != null && customizer != null) {
            Path oldValue = bnTable.getPath();
            Path newValue = path.getPath();
            bnTable.setPath(newValue);
            customizer.firePropertyChange("path", oldValue, newValue);
        }
    }

}