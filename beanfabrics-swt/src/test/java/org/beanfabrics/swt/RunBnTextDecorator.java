/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
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
import org.beanfabrics.support.OnChange;
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
public class RunBnTextDecorator {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());


        Text text = new Text(shell, SWT.SINGLE);

        Model model = new Model();
        ModelProvider modelProvider = new ModelProvider();
        modelProvider.setPresentationModel(model);

        Decorator deco = new Decorator(modelProvider);
        deco.decorateText(text, new Path("newColor"));

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
        TextPM newColor = new TextPM();

        public Model() {
            PMManager.setup(this);
        }

        @OnChange(path="newColor")
        public void log() {
          System.out.println("newColor="+newColor.getText());
          new RuntimeException().printStackTrace();
        }

    }
}
