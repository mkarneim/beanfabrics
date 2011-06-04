/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractOperationPM;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Max Gensthaler
 * @author Michael Karneim
 */
public class BnMenuItemTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BnMenuItemTest.class);
    }

    private BnMenuItem menuItem;
    private IModelProvider provider;
    private TestModel testModel;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp()
        throws Exception {
        this.menuItem = new BnMenuItem();
        this.provider = new ModelProvider();
        this.testModel = new TestModel();
    }

    @Test
    public void testGetDataSource() {
        this.menuItem.setModelProvider(this.provider);
        assertEquals("button.getLocalProvider()", this.provider, this.menuItem.getModelProvider());
    }

    @Test
    public void testSetDataSource() {
        this.provider.setPresentationModel(this.testModel);
        this.menuItem.setPath(new Path("this.op"));
        this.menuItem.setModelProvider(this.provider);
        assertNotNull("button.getOperation()", this.menuItem.getPresentationModel());
    }

    @Test
    public void testGetPath() {
        Path path = new Path("this.op");
        this.menuItem.setPath(path);
        assertEquals("button.getPath()", path, this.menuItem.getPath());
    }

    @Test
    public void testSetPath() {
        this.provider.setPresentationModel(this.testModel);
        this.menuItem.setPath(new Path("this.op"));
        this.menuItem.setModelProvider(this.provider);
        assertNotNull("button.getPresentationModel()", this.menuItem.getPresentationModel());
    }

    @Test
    public void testSetPathString() {
        this.provider.setPresentationModel(this.testModel);
        this.menuItem.setPath(new Path("this.op"));
        this.menuItem.setModelProvider(this.provider);
        assertNotNull("button.getPresentationModel()", this.menuItem.getPresentationModel());
    }

    @Test
    public void testGetPathString() {
        this.menuItem.setPath(new Path("this.op"));
        assertEquals("", "this.op", this.menuItem.getPath().toString());
    }

    @Test
    public void testSetOperation() {
        final Counter counter = new Counter();
        final IOperationPM op = new AbstractOperationPM() {
            @Override
            public boolean execute()
                throws Throwable {
                counter.increase();
                return true; // success
            }
        };
        this.menuItem.setPresentationModel(op);
        this.menuItem.doClick();
        assertEquals("counter.get()", 1, counter.get());
    }

    @Test
    public void testGetPresentationModel() {
        IOperationPM op = new OperationPM();
        this.menuItem.setPresentationModel(op);
        assertEquals("", op, this.menuItem.getPresentationModel());
    }

    @Test
    public void testRefresh() {
        this.provider.setPresentationModel(this.testModel);
        this.menuItem.setPath(new Path("this.op"));
        this.menuItem.setModelProvider(this.provider);
        this.menuItem.setText("abc");
        assertFalse("button.isEnabled()", this.menuItem.isEnabled());
        assertEquals("", this.menuItem.getPresentationModel().getValidationState().getMessage(), this.menuItem.getToolTipText());
    }

    @Test
    public void testIsAutoExecute() {
        this.menuItem.setAutoExecute(false);
        assertFalse("button.isAutoExecute()", this.menuItem.isAutoExecute());
    }

    @Test
    public void testSetAutoExecute()
        throws Throwable {
        final Counter counter = new Counter();
        final IOperationPM op = new AbstractOperationPM() {
            @Override
            public boolean execute()
                throws Throwable {
                counter.increase();
                return true; // success
            }
        };
        this.menuItem.setPresentationModel(op);
        this.menuItem.setAutoExecute(false);
        this.menuItem.doClick();
        assertEquals("counter.get()", 0, counter.get());
        this.menuItem.setAutoExecute(true);
        this.menuItem.doClick();
        assertEquals("counter.get()", 1, counter.get());
    }

    @Test
    public void getIcon() {
        final IOperationPM op = new AbstractOperationPM() {
            @Override
            public boolean execute()
                throws Throwable {
            	return true; // success
            }
        };
        op.setIconUrl(BnButtonTest.class.getResource("sample.gif"));
        this.menuItem.setPresentationModel(op);
        assertNotNull("this.button.getIcon()", this.menuItem.getIcon());
    }

    private static class TestModel extends AbstractPM {
        protected final AbstractOperationPM op = new AbstractOperationPM() {
            @Override
            public boolean execute()
                throws Throwable {
                TestModel.this.op();
                return true; // success
            }
        };

        protected final TextPM pM = new TextPM();

        public TestModel() {
            PMManager.setup(this);
            this.pM.setDescription("Insert digits");
            this.pM.setMandatory(true);
            this.pM.getValidator().add(new ValidationRule() {
                public ValidationState validate() {
                    try {
                        Integer.parseInt(pM.getText());
                        return null;
                    } catch (NumberFormatException e) {
                        return new ValidationState("Error: Insert digits only");
                    }
                }
            });
            this.op.setDescription("Execute");
            this.op.getValidator().add(new ValidationRule() {
                public ValidationState validate() {
                    return pM.getValidationState();
                }
            });
        }

        public void op() {
            this.op.check();
        }
    }

    private static class Counter {
        private int count = 0;

        public int get() {
            return count;
        }

        public void increase() {
            count++;
        }
    }
}