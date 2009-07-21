/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Property;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * @author Michael Karneim
 */
public class CalculatorPM extends AbstractPM {
    @Property
    public final TextPM operator = new TextPM();
    @Property
    public final IntegerPM result = new IntegerPM();
    @Property
    public final IntegerPM input = new IntegerPM();
    @Property
    public final IOperationPM plus = new OperationPM();
    @Property
    public final IOperationPM minus = new OperationPM();
    @Property
    public final IOperationPM showResult = new OperationPM();

    public CalculatorPM() {
        init();
    }

    private void init() {
        PMManager.setup(this);

        result.setInteger(0);
        operator.setText("+");
        input.setMandatory(true);
        plus.getValidator().add(new ValidationRule() {
            public ValidationState validate() {
                if (!input.isValid()) {
                    return new ValidationState("Enter a number");
                }
                return null;
            }
        });
        minus.getValidator().add(new ValidationRule() {
            public ValidationState validate() {
                if (!input.isValid()) {
                    return new ValidationState("Enter a number");
                }
                return null;
            }
        });
    }

    @Operation
    public void plus() {
        plus.check();
        this.evaluate();
        operator.setText("+");
        input.setInteger(result.getInteger());
    }

    @Operation
    public void minus() {
        minus.check();
        this.evaluate();
        operator.setText("-");
        input.setInteger(result.getInteger());
    }

    @Operation
    public void showResult() {
        // TODO (mk) this behaves not really like a pocket calculator
        showResult.check();
        this.evaluate();
        input.setInteger(result.getInteger());
    }

    private void evaluate() {
        if (operator.getText().equals("+")) {
            result.setInteger(result.getInteger() + input.getInteger());
        } else if (operator.getText().equals("-")) {
            result.setInteger(result.getInteger() - input.getInteger());
        }
    }
}