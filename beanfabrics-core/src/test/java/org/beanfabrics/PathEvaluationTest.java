/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics;

/**
 * @author Michael Karneim
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.PropertySupport;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class PathEvaluationTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PathEvaluationTest.class);
    }

    private static class ProductPM extends AbstractPM {
        public final TextPM name = new TextPM();
        public ProducerPM producer;
        public final DimensionPM dimension = new DimensionPM();
        public IntegerPM price;

        public ProductPM() {
            PMManager.setup(this);
        }
    }

    private static class ProducerPM extends AbstractPM {
        public final TextPM name = new TextPM();
        public final AddressPM address = new AddressPM();

        public ProducerPM() {
            PMManager.setup(this);
        }
    }

    private static class AddressPM extends AbstractPM {
        public final TextPM street = new TextPM();

        public AddressPM() {
            PMManager.setup(this);
        }
    }

    private static class DimensionPM extends AbstractPM {
        public final IntegerPM height = new IntegerPM();
        public final IntegerPM width = new IntegerPM();
        public final IntegerPM length = new IntegerPM();

        public DimensionPM() {
            PMManager.setup(this);
        }
    }

    private ProductPM root;

    public PathEvaluationTest() {
    }

    @Before
    public void setUp()
        throws Exception {
        root = new ProductPM();
        root.name.setText("iPod");
        root.producer = new ProducerPM();
        PropertySupport.get(root).refresh();
        root.producer.name.setText("Apple Inc.");
        root.producer.address.street.setText("Infinite Loop");
        root.dimension.height.setText("103");
        root.dimension.width.setText("61");
        root.dimension.length.setText("10");
    }

    @AfterClass
    public static void tearDownClass()
        throws Exception {
    }

    @Test
    public void isCompletelyResolved() {
        Path path = new Path("this");
        PathEvaluation eval = new PathEvaluation(root, path);
        assertEquals("eval.isCompletelyResolved()", true, eval.isCompletelyResolved());
        assertEquals("eval.getResolvedLength()", 0, eval.getResolvedLength());
        assertEquals("eval.getResolvedPath()", path, eval.getResolvedPath());
        assertNotNull("eval.getLastEntry()", eval.getLastEntry());
        assertEquals("eval.getRoot()", root, eval.getRoot());
        assertEquals("eval.getLastEntry().getProperty()", root, eval.getLastEntry().getValue());
    }

    @Test
    public void isCompletelyResolved2() {
        Path path = new Path("this.name");
        PathEvaluation eval = new PathEvaluation(root, path);
        assertEquals("eval.isCompletelyResolved()", true, eval.isCompletelyResolved());
        assertEquals("eval.getResult().getName()", "name", eval.getResult().getName());
    }

    @Test
    public void isCompletelyResolved3() {
        Path path = new Path("name");
        PathEvaluation eval = new PathEvaluation(root, path);
        assertEquals("eval.isCompletelyResolved()", true, eval.isCompletelyResolved());
        assertEquals("eval.getResult().getName()", "name", eval.getResult().getName());
    }

    @Test
    public void isCompletelyResolved4() {
        Path path = new Path("this.dimension.height");
        PathEvaluation eval = new PathEvaluation(root, path);
        assertEquals("eval.isCompletelyResolved()", true, eval.isCompletelyResolved());
        assertEquals("eval.getResult().getName()", "height", eval.getResult().getName());
    }

    @Test
    public void evaluateThisPath() {
        Path path = new Path("this");
        PathEvaluation eval = new PathEvaluation(root, path);
        assertEquals("eval.isCompletelyResolved()", true, eval.isCompletelyResolved());
        assertNotNull("eval.getResult()", eval.getResult());
        assertEquals("eval.getRoot()", root, eval.getRoot());
        assertEquals("eval.getLastEntry().getProperty()", root, eval.getResult().getValue());
    }

    @Test
    public void evaluateShortPathToTextProperty() {
        Path path = new Path("this.name");
        PathEvaluation eval = new PathEvaluation(root, path);
        assertEquals("eval.isCompletelyResolved()", true, eval.isCompletelyResolved());
        assertNotNull("eval.getResult()", eval.getResult());
        assertEquals("eval.getResult().getProperty()", root.name, eval.getResult().getValue());
    }

    @Test
    public void evaluateShortPathToNullableProperty() {
        Path path = new Path("this.producer");
        PathEvaluation eval = new PathEvaluation(root, path);
        assertNotNull("eval", eval);
        assertEquals("eval.isCompletelyResolved()", true, eval.isCompletelyResolved());
        assertEquals("eval.getResult().getName()", "producer", eval.getResult().getName());
        assertNotNull("eval.getResult()", eval.getResult());
        assertEquals("eval.getResult().getProperty()", root.producer, eval.getResult().getValue());
        assertEquals("eval.getResult().getProperty() instanceof ProducerPM", true, eval.getResult().getValue() instanceof ProducerPM);
    }

    @Test
    public void evaluateShortPathToNullProperty() {
        Path path = new Path("this.price");
        PathEvaluation eval = new PathEvaluation(root, path);
        assertNotNull("eval", eval);
        assertEquals("eval.isCompletelyResolved()", true, eval.isCompletelyResolved());
        assertEquals("eval.getResult().getName()", "price", eval.getResult().getName());
        assertNotNull("eval.getResult()", eval.getResult());
        assertEquals("eval.getResult().getProperty()", null, eval.getResult().getValue());
    }

    @Test
    public void evaluateShortPathToNullModel() {
        root.producer = null;
        PropertySupport.get(root).refresh();
        Path path = new Path("this.producer");
        PathEvaluation eval = new PathEvaluation(root, path);
        assertNotNull("eval", eval);
        assertEquals("eval.isCompletelyResolved()", true, eval.isCompletelyResolved());
        assertNotNull("eval.getResult()", eval.getResult());
        assertNull("eval.getResult().getProperty()", eval.getResult().getValue());
        assertEquals("eval.getResult().getProperty()", root.producer, eval.getResult().getValue());
    }

    @Test
    public void evaluateLongPath() {
        Path path = new Path("this.producer.address.street");
        PathEvaluation eval = new PathEvaluation(root, path);
        assertNotNull("eval", eval);
        assertEquals("eval.isCompletelyResolved()", true, eval.isCompletelyResolved());
        assertEquals("eval.getResult().getName()", "street", eval.getResult().getName());
        assertNotNull("eval.getResult()", eval.getResult());
        assertEquals("eval.getResult().property", root.producer.address.street, eval.getResult().getValue());
    }

    @Test
    public void evaluateLongInvalidPath() {
        Path path = new Path("this.producer.error.street");
        PathEvaluation eval = new PathEvaluation(root, path);
        assertNotNull("eval", eval);
        assertEquals("eval.isCompletelyResolved()", false, eval.isCompletelyResolved());
        assertEquals("eval.getResolvedLength()", 1, eval.getResolvedLength());
        try {
            eval.getResult();
            fail("expected IllegalStateException");
        } catch (IllegalStateException ex) {
            // ok.
        }
    }

    @Test
    public void evaluateLongInvalidPath2() {
        Path path = new Path("this.producer.address.street.nr.a.x");
        PathEvaluation eval = new PathEvaluation(root, path);
        assertNotNull("eval", eval);
        assertEquals("eval.isCompletelyResolved()", false, eval.isCompletelyResolved());
        assertEquals("eval.getResolvedLength()", 3, eval.getResolvedLength());
    }

    @Test
    public void evaluateEmbeddedEditorPath() {
        Path path = new Path("this.dimension.height");
        PathEvaluation eval = new PathEvaluation(root, path);
        assertNotNull("eval", eval);
        assertEquals("eval.isCompletelyResolved()", true, eval.isCompletelyResolved());
        PresentationModel pModel = eval.getResult().getValue();
        assertEquals("pM", root.dimension.height, pModel);
    }

    @Test
    public void evaluate()
        throws EvaluationException {
        Path path = new Path("this.producer.name");
        PresentationModel result = PathEvaluation.evaluate(root, path);
        assertEquals("result", root.producer.name, result);
    }
}