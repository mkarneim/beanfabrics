/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swt;

import static org.junit.Assert.assertEquals;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.Operation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The {@link BnPushButtonDecoratorTest} is a unit test for the
 * {@link BnPushButtonDecorator}.
 * 
 * @author Michael Karneim
 */
public class BnPushButtonDecoratorTest {

    private Button button;
    private BnPushButtonDecorator decorator;
    private ModelProvider modelProvider;
    private SamplePM samplePM;

    private class SamplePM extends AbstractPM {

        OperationPM doSomething = new OperationPM();
        int counter = 0;

        public SamplePM() {
            PMManager.setup(this);
        }

        @Operation
        public void doSomething() {
            counter++;
        }
    }

    @Before
    public void setUp()
        throws Exception {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        button = new Button(shell, SWT.PUSH);
        decorator = new BnPushButtonDecorator(button);
        samplePM = new SamplePM();
        modelProvider = new ModelProvider();
        modelProvider.setPresentationModel(samplePM);
    }

    @After
    public void tearDown()
        throws Exception {
    }

    @Test
    public void notifyListeners() {
        decorator.setModelProvider(modelProvider);
        decorator.setPath(new Path("doSomething"));
        button.notifyListeners(SWT.Selection, new Event());

        assertEquals("samplePM.counter", 1, samplePM.counter);
    }
}
