/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.event.ModelProviderEvent;
import org.beanfabrics.event.ModelProviderListener;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.PropertySupport;
import org.beanfabrics.swing.BnTextField;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class ModelProviderTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ModelProviderTest.class);
    }

    private static class ProductPM extends AbstractPM {
        public final TextPM name = new TextPM();
        public final DimensionPM dimension = new DimensionPM();
        public ProducerPM producer;

        public ProductPM() {
            PMManager.setup(this);
        }
    }

    private static class ProducerPM extends AbstractPM {
        public final TextPM name = new TextPM();

        public ProducerPM() {
            PMManager.setup(this);
        }
    }

    private static class DimensionPM extends AbstractPM {
        public final IntegerPM height = new IntegerPM();
        public final IntegerPM width = new IntegerPM();
        public final IntegerPM length = new IntegerPM();
        public DummyPM dummy = new DummyPM();

        public DimensionPM() {
            PMManager.setup(this);
        }
    }

    private static class DummyPM extends AbstractPM {
        public final TextPM text = new TextPM();

        public DummyPM() {
            PMManager.setup(this);
            text.setText("blah");
        }
    }

    ProductPM root;

    public ModelProviderTest() {
    }

    @Before
    public void setUp()
        throws Exception {
        root = new ProductPM();
        root.name.setText("iPod");
        root.dimension.height.setText("103");
        root.dimension.width.setText("61");
        root.dimension.length.setText("10");
    }

    @Test
    public void create() {
        new ModelProvider();
    }

    @Test
    public void setRoot() {
        ModelProvider provider = new ModelProvider(ProductPM.class);
        provider.setPresentationModel(root);
        assertEquals("provider.getContent()", root, provider.getPresentationModel());
    }

    @Test
    public void setContentType() {
        ModelProvider provider = new ModelProvider(ProductPM.class);
        provider.setPresentationModelType(ProductPM.class);
        provider.setPresentationModel(root);
        Class cls = ProducerPM.class;
        try {
            provider.setPresentationModelType(cls);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void bind() {
        ModelProvider provider = new ModelProvider();
        AbstractPM cell = new AbstractPM() {
            TextPM text = new TextPM();
        };
        PMManager.setup(cell);

        Counter counter = new Counter();
        provider.addModelProviderListener(new Path("this.text"), counter);
        assertEquals("counter.targetGained", 0, counter.targetGained);
        assertNull("counter.lastEvent", counter.lastEvent);

        provider.setPresentationModel(cell); // set root late
        assertEquals("counter.targetGained", 1, counter.targetGained);
        assertNotNull("counter.lastEvent", counter.lastEvent);

        provider.setPresentationModel(null);
        assertEquals("counter.targetLost", 1, counter.targetLost);

        provider.setPresentationModel(cell);
        assertEquals("counter.targetGained", 2, counter.targetGained);

    }

    @Test
    public void bind2() {
        ModelProvider provider = new ModelProvider();
        AbstractPM cell = new AbstractPM() {
            TextPM text = new TextPM();
        };
        PMManager.setup(cell);
        provider.setPresentationModel(cell); // set content root early

        Counter counter = new Counter();
        provider.addModelProviderListener(new Path("this.text"), counter);
        assertEquals("counter.targetGained", 1, counter.targetGained);
        assertNotNull("counter.lastEvent", counter.lastEvent);
    }

    @Test
    public void bindLongPath() {
        class BCell extends AbstractPM {
            TextPM c = new TextPM();

            public BCell() {
                PMManager.setup(this);
            }
        }
        class ACell extends AbstractPM {
            BCell b = new BCell();

            public ACell() {
                PMManager.setup(this);
            }
        }
        class RootCell extends AbstractPM {
            ACell a = new ACell();

            public RootCell() {
                PMManager.setup(this);
            }
        }

        RootCell cell = new RootCell();

        ModelProvider provider = new ModelProvider();

        provider.setPresentationModel(cell);

        Path path = new Path("this.a.b.c");
        assertEquals("pModel.getProperty(path)", cell.a.b.c, PropertySupport.get(cell).getProperty(path));

        Counter counter = new Counter();
        provider.addModelProviderListener(new Path("this.a.b.c"), counter);
        assertEquals("counter.targetGained", 1, counter.targetGained);
    }

    @Test
    public void bindRemoveProperty() {
        ModelProvider provider = new ModelProvider();
        Counter counter = new Counter();
        provider.addModelProviderListener(new Path("this.dimension.dummy.text"), counter);

        assertEquals("counter.targetGained", 0, counter.targetGained);
        provider.setPresentationModel(root);
        assertEquals("counter.targetGained", 1, counter.targetGained);
        assertEquals("counter.targetLost", 0, counter.targetLost);

        DummyPM bak = root.dimension.dummy;
        root.dimension.dummy = null;
        PropertySupport.get(root.dimension).refresh();
        assertEquals("root.dimension.getProperty(\"dummy\")", null, root.dimension.dummy);
        assertEquals("counter.targetLost", 1, counter.targetLost);
        assertEquals("counter.targetGained", 1, counter.targetGained);

        root.dimension.dummy = bak;
        PropertySupport.get(root.dimension).refresh();
        assertEquals("counter.targetGained", 2, counter.targetGained);
        assertEquals("counter.targetLost", 1, counter.targetLost);
    }

    @Test
    public void setRoot2() {
        ModelProvider provider = new ModelProvider();
        BnTextField tfHeight = new BnTextField();
        tfHeight.setModelProvider(provider);
        tfHeight.setPath(new Path("this.dimension.height"));
        BnTextField tfWidth = new BnTextField();
        tfWidth.setModelProvider(provider);
        tfWidth.setPath(new Path("this.dimension.width"));
        provider.setPresentationModel(root);
        ProductPM root2 = new ProductPM();
        provider.setPresentationModel(root2);
    }

    private static class Counter implements ModelProviderListener {
        private int targetGained = 0;
        private int targetLost = 0;
        private ModelProviderEvent lastEvent;

        public void modelGained(ModelProviderEvent evt) {
            targetGained++;
            lastEvent = evt;
        }

        public void modelLost(ModelProviderEvent evt) {
            targetLost++;
            lastEvent = evt;
        }
    }
}