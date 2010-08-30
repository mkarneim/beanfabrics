/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swt;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.Options;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Operation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Michael Karneim
 */
public class RunBnComboDecorator {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());

        Combo combo = new Combo(shell, SWT.READ_ONLY);
        Text text = new Text(shell, SWT.SINGLE);
        Button setButton = new Button(shell, SWT.PUSH);
        setButton.setText("set");
        Button addButton = new Button(shell, SWT.PUSH);
        addButton.setText("add");

        Model model = new Model();
        ModelProvider modelProvider = new ModelProvider();
        modelProvider.setPresentationModel(model);

        Decorator deco = new Decorator(modelProvider);
        deco.decorateCombo(combo, new Path("colors"));
        deco.decorateText(text, new Path("newColor"));
        deco.decoratePushButton(setButton, new Path("setNewColor"));
        deco.decoratePushButton(addButton, new Path("addNewColor"));

        shell.pack();
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        display.dispose();
    }

    private static class Model extends AbstractPM {
        TextPM colors = new TextPM();
        TextPM newColor = new TextPM();
        OperationPM setNewColor = new OperationPM();
        OperationPM addNewColor = new OperationPM();

        public Model() {
            PMManager.setup(this);
            colors.setOptions(Options.create("green", "blue", "yellow"));
        }

        @Operation
        void setNewColor() {
            colors.setText(newColor.getText());
        }

        @Operation
        void addNewColor() {
            colors.getOptions().put(newColor.getText(), newColor.getText());
        }
    }
}
