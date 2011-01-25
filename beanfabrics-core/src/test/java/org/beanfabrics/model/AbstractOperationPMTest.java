/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertEquals;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class AbstractOperationPMTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AbstractOperationPMTest.class);
    }

    public AbstractOperationPMTest() {
    }

    @Before
    public void setUp()
        throws Exception {
        util = new Util();
    }

    private Util util;

    private static class Util {
        public AbstractOperationPM createAbstractOperation() {
            AbstractOperationPM op = new AbstractOperationPM() {
                @Override
                public void execute()
                    throws Throwable {
                }
            };
            return op;
        }
    }

    @Test
    public void create() {
        util.createAbstractOperation();
    }

    @Test
    public void execute()
        throws Throwable {
        final boolean[] didInvoke = new boolean[1];
        AbstractOperationPM op = new AbstractOperationPM() {
            public void execute() {
                didInvoke[0] = true;
            }
        };

        op.execute();
        assertEquals("didInvoke[0]", true, didInvoke[0]);
    }

    @Test
    public void validate()
        throws Throwable {
        AbstractOperationPM op = util.createAbstractOperation();
        op.getValidator().add(new ValidationRule() {
            public ValidationState validate() {
                return new ValidationState("give up");
            }
        });

        assertEquals("op.isEnabled()", false, op.isEnabled());
    }

    @Test
    public void validateInsideCell()
        throws Throwable {
        class MyModel extends AbstractPM {
            AbstractOperationPM op = util.createAbstractOperation();
            final TextPM t = new TextPM();

            public MyModel() {
                PMManager.setup(this);
            }
        }

        final MyModel model = new MyModel();

        model.op.getValidator().add(new ValidationRule() {
            public ValidationState validate() {
                if (model.t.getText().equals("open sesame")) {
                    return null;
                } else {
                    return new ValidationState("type in 'open sesame'");
                }
            }
        });

        assertEquals("op.isEnabled()", false, model.op.isEnabled());
        model.t.setText("open sesame");
        assertEquals("op.isEnabled()", true, model.op.isEnabled());
    }

    @Test
    public void getIcon_v1() {
        AbstractOperationPM op = util.createAbstractOperation();
        assertEquals("op.getIcon()", null, op.getIcon());
    }

    @Test
    public void getIcon_v2() {
        AbstractOperationPM op = util.createAbstractOperation();
        Icon icon = new ImageIcon();
        op.setIcon(icon);
        assertEquals("op.getIcon()", icon, op.getIcon());
    }
}