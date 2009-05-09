/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.support;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class ValidationSupportTest {
	public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ValidationSupportTest.class);
    }

	@Test
	public void singleValidation() {
		class MyTextPM extends TextPM {
			public MyTextPM() {
				setMandatory(true);
				PMManager.setup(this);
			}

			@Validation(message="contains no x")
			boolean containsX() {
				return getText().indexOf('x')>=0;
			}
		}
		MyTextPM pModel = new MyTextPM();

		assertEquals("pModel.isValid()", false, pModel.isValid());

		pModel.setText("x");
		assertEquals("pModel.isValid()", true, pModel.isValid());
		pModel.setText("bla");
		assertEquals("pModel.isValid()", false, pModel.isValid());
	}

	@Test
	public void multipleValidations() {
		class MyTextPM extends TextPM {
			public MyTextPM() {
				setMandatory(true);
				PMManager.setup(this);
			}

			@Validation(message="contains no x")
			boolean containsX() {
				return getText().indexOf('x')>=0;
			}
			@Validation(message="contains no y")
			boolean containsY() {
				return getText().indexOf('y')>=0;
			}

		}
		MyTextPM pModel = new MyTextPM();

		assertEquals("pModel.isValid()", false, pModel.isValid());

		pModel.setText("x");
		assertEquals("pModel.isValid()", false, pModel.isValid());
		pModel.setText("xy");
		assertEquals("pModel.isValid()", true, pModel.isValid());
		pModel.setText("bla");
		assertEquals("pModel.isValid()", false, pModel.isValid());
	}

	@Test
	public void multipleValidationsWithSortOrder() {
		class MyTextPM extends TextPM {
			public MyTextPM() {
				setMandatory(true);
				PMManager.setup(this);
			}

			Integer getInteger() {
				return Integer.parseInt(getText());
			}

			@Validation(message="not an integer") @SortOrder(1)
			boolean isInteger() {
				try {
					getInteger();
					return true;
				} catch ( NumberFormatException ex) {
					return false;
				}
			}
			@Validation(message="number must be even") @SortOrder(2)
			boolean isEven() {
				return getInteger() % 2 == 0;
			}
		}
		MyTextPM pModel = new MyTextPM();

		assertEquals("pModel.isValid()", false, pModel.isValid());

		pModel.setText("13");
		assertEquals("pModel.isValid()", false, pModel.isValid());
		pModel.setText("12");
		assertEquals("pModel.isValid()", true, pModel.isValid());
		pModel.setText("bla");
		assertEquals("pModel.isValid()", false, pModel.isValid());
	}

	@Test
	public void compositeEditor() {
		class EmailModel extends AbstractPM {
			@Property TextPM to = new TextPM();
			@Property TextPM from = new TextPM();

			public EmailModel() {
				to.setMandatory(true);
				from.setMandatory(true);
				PMManager.setup(this);
			}
			@Validation(path="to", message="missing '@'")
			boolean toContainsAt() {
				return to.getText().indexOf('@')>=0;
			}
			@Validation(path="from", message="missing '@'")
			boolean fromContainsAt() {
				return from.getText().indexOf('@')>=0;
			}
		}
		EmailModel pModel = new EmailModel();
		assertEquals("pModel.isValid()", false, pModel.isValid());
		pModel.to.setText("hallo");
		pModel.from.setText("hallo");
		assertEquals("pModel.isValid()", false, pModel.isValid());

		pModel.to.setText("tom@example.com");
		assertEquals("pModel.to.isValid()", true, pModel.to.isValid());
		assertEquals("pModel.isValid()", false, pModel.isValid());

		pModel.from.setText("jerry@example.com");
		assertEquals("pModel.from.isValid()", true, pModel.from.isValid());
		assertEquals("pModel.isValid()", true, pModel.isValid());
	}

	@Test
	public void pathWithMoreElements() {
		class SomeModel extends AbstractPM {
			@Property TextPM title = new TextPM();

			public SomeModel() {
				title.setMandatory(true);
				PMManager.setup(this);
			}
		}

		class OwnerModel extends AbstractPM {
			@Property SomeModel some = new SomeModel();
			public OwnerModel() {
				PMManager.setup(this);
			}

			@Validation(path="some.title", message ="invalid title")
			public boolean titleContainsX() {
				return some.title.getText().indexOf('x')>=0;
			}
		}
		OwnerModel pModel = new OwnerModel();

		assertEquals("pModel.some.title.isValid()", false, pModel.some.title.isValid());
		assertEquals("pModel.some.isValid()", false, pModel.some.isValid());
		assertEquals("pModel.isValid()", false, pModel.isValid());

		pModel.some.title.setText("text");
		assertEquals("pModel.some.title.isValid()", true, pModel.some.title.isValid());
		assertEquals("pModel.some.isValid()", true, pModel.some.isValid());
		assertEquals("pModel.isValid()", true, pModel.isValid());

		// now replace TextPM for 'title'
		TextPM oldTitlePM = pModel.some.title;
		pModel.some.title = new TextPM();
		pModel.some.title.setMandatory(true);
		PropertySupport.get(pModel.some).refresh();

		assertEquals("pModel.some.title.isValid()", false, pModel.some.title.isValid());
		assertEquals("pModel.some.isValid()", false, pModel.some.isValid());
		assertEquals("pModel.isValid()", false, pModel.isValid());

		pModel.some.title.setText("text");
		assertEquals("pModel.some.title.isValid()", true, pModel.some.title.isValid());
		assertEquals("pModel.some.isValid()", true, pModel.some.isValid());
		assertEquals("pModel.isValid()", true, pModel.isValid());

		// check oldTitleEd
		oldTitlePM.setText("bla");
		assertEquals("oldTitleCell.isValid()", true, oldTitlePM.isValid()); // 'x'-validation has been removed

	}

	@Test
	public void validateOperation() throws Throwable {
		class ArticleModel extends AbstractPM {
			@Property TextPM name = new TextPM();
			@Property IntegerPM number = new IntegerPM();
			@Property OperationPM addToCart = new OperationPM();

			public ArticleModel() {
				number.setInteger(1);
				PMManager.setup(this);
			}
			@Operation
			public void addToCart() {
				number.setInteger(number.getInteger()-1);
			}
			@Validation(path="addToCart", message="no items in stock")
			public boolean hasItemsInStock() {
				return number!=null && number.getInteger()>0;
			}
		}

		ArticleModel pModel = new ArticleModel();
		assertEquals("pModel.addToCart.isEnabled()", true, pModel.addToCart.isEnabled());

		pModel.addToCart.execute();
		assertEquals("pModel.addToCart.isEnabled()", false, pModel.addToCart.isEnabled());
	}
}