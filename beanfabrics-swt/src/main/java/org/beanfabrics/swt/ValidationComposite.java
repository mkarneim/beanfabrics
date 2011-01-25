/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swt;

import org.beanfabrics.validation.ValidationState;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

import com.swtdesigner.SWTResourceManager;

/**
 * @author Michael Karneim
 */
public class ValidationComposite extends Composite implements ValidationIndicator {
    private Label icon;
    private final String IMAGE_NAME = "error_overlay.gif";

    /**
     * Create the composite
     * 
     * @param parent
     * @param style
     */
    public ValidationComposite(Composite parent, int style) {
        super(parent, style);
        MyLayout myLayout = new MyLayout();
        setLayout(myLayout);
        {
            icon = new Label(this, SWT.NONE);
            icon.setImage(getImage());
        }
        //
        myLayout.setLeftCtrl(icon);
        layout();
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    private Image getImage() {
        return SWTResourceManager.getImage(ValidationComposite.class, IMAGE_NAME);
    }

    public void setValidationState(ValidationState state) {
        if (state == null) {
            icon.setVisible(false);
            icon.setToolTipText(null);
        } else {
            icon.setVisible(true);
            icon.setToolTipText(state.getMessage());
        }
    }

    private boolean trace;

    public boolean isTrace() {
        return trace;
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    private class MyLayout extends Layout {
        private Control leftCtrl;

        public MyLayout() {
            super();
        }

        public Control getLeftCtrl() {
            return leftCtrl;
        }

        public void setLeftCtrl(Control leftCtrl) {
            this.leftCtrl = leftCtrl;
        }

        @Override
        protected Point computeSize(Composite composite, int wHint, int hHint, boolean flag) {
            if (trace) {
                System.out.println("MyLayout.computeSize() wHint=" + wHint + ",hHint=" + hHint);
            }
            Point result;
            if (leftCtrl == null) {
                result = new Point(0, 0);
            } else {
                result = leftCtrl.computeSize(wHint, hHint);
            }

            // add other size
            Control[] children = composite.getChildren();
            for (Control c : children) {
                if (c != leftCtrl) {
                    Point otherSize = c.computeSize(wHint, hHint);
                    if (trace) {
                        System.out.println("MyLayout.computeSize() c=" + c);
                        System.out.println("MyLayout.computeSize() otherSize=" + otherSize);
                    }
                    result.x = result.x + otherSize.x;
                    result.y = Math.max(result.y, otherSize.y);
                    break;
                }
            }

            if (wHint != SWT.DEFAULT)
                result.x = wHint;
            if (hHint != SWT.DEFAULT)
                result.y = hHint;
            if (trace) {
                System.out.println("MyLayout.computeSize() result=" + result);
            }
            return result;
        }

        @Override
        protected void layout(Composite composite, boolean flag) {
            doLayoutLeftControl();
            Control[] children = composite.getChildren();
            for (Control c : children) {
                if (c != leftCtrl) {
                    // only layout first child that is not an icon
                    doLayout(composite, c);
                    break;
                }
            }
        }

        private void doLayoutLeftControl() {
            if (leftCtrl != null) {
                leftCtrl.pack();
            }
        }

        private void doLayout(Composite composite, Control child) {
            Rectangle parentBounds = composite.getBounds();
            Rectangle leftCtrlBounds;
            if (leftCtrl == null) {
                leftCtrlBounds = new Rectangle(0, 0, 0, 0);
            } else {
                leftCtrlBounds = leftCtrl.getBounds();
            }
            int borderWidth = composite.getBorderWidth();
            int x = leftCtrlBounds.x + leftCtrlBounds.width;
            int y = 0;
            int width = parentBounds.width - leftCtrlBounds.width - borderWidth;
            int height = parentBounds.height - borderWidth;
            child.setBounds(x, y, width, height);
        }
    }
}
