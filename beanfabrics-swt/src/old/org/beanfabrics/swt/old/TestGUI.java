package org.beanfabrics.swt.old;

import java.util.Date;

import org.beanfabrics.Binder;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.DatePM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.Options;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Validation;
import org.beanfabrics.swt.ValidationComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Michael Karneim
 */
public class TestGUI extends Shell implements View<TestGUI.ContactCell>, ModelSubscriber {
	private BnPushButton bnPushButton_1;
	private BnButton openBnButton;
	private ValidationComposite validationComposite_12;
	private BnButton closedBnButton_1;
	private ValidationComposite validationComposite_11;
	private BnText bnText_4;
	private ValidationComposite validationComposite_10;
	private BnButton bnButton;
	private ValidationComposite validationComposite_9;
	private ValidationComposite validationComposite_8;
	private BnButton closedBnButton;
	private BnCombo bnCombo_2;
	private ValidationComposite validationComposite;
	private BnPushButton bnPushButton;
	private BnText bnText_3;
	private ValidationComposite validationComposite_2;
	private ValidationComposite validationComposite_6;
	private BnText bnText_2;
	private ValidationComposite validationComposite_5;
	private BnCombo bnCombo_1;
	private BnText bnText_1;
	private ValidationComposite validationComposite_7;
	private ValidationComposite validationComposite_4;
	private ValidationComposite validationComposite_3;
	private ValidationComposite validationComposite_1;
	private BnCombo bnCombo;
	private Label priorityLabel;
	private Label label_3;
	private BnText bnText;
	private Label label_2;
	private BnText txName;
	private Label label_1;
	private Label label;

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			ContactCell model = new ContactCell();
			model.date.setDate( new Date());
			model.note.setText("abc test");

			Display display = Display.getDefault();
			TestGUI shell = new TestGUI(display, SWT.SHELL_TRIM);

			Binder.bind(shell, model);

			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final Link link = new Link(this);
	private final IModelProvider localModelProvider = new ModelProvider();// @wb:location=28,436

	/**
	 * Create the shell
	 * @param display
	 * @param style
	 */
	public TestGUI(Display display, int style) {
		super(display, style);
		localModelProvider.setPresentationModelType(ContactCell.class);
		createContents();

	}

	public ContactCell getPresentationModel() {
		return getLocalModelProvider().getPresentationModel();
	}

	public void setPresentationModel(ContactCell pModel) {
		getLocalModelProvider().setPresentationModel(pModel);
	}

	private IModelProvider getLocalModelProvider() {
		return localModelProvider;
	}

	/** {@inheritDoc} */
	public void setModelProvider(IModelProvider dataSource) {
		this.link.setModelProvider(dataSource);
	}

	/** {@inheritDoc} */
	public IModelProvider getModelProvider() {
		return link.getModelProvider();
	}

	/** {@inheritDoc} */
	public void setPath(Path path) {
		this.link.setPath(path);
	}

	/** {@inheritDoc} */
	public Path getPath() {
		return link.getPath();
	}

	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(500, 408);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		setLayout(gridLayout);

		label = new Label(this, SWT.NONE);
		label.setLayoutData(new GridData());
		label.setText("Name");
		validationComposite_1 = new ValidationComposite(this, SWT.NONE);
		validationComposite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		txName = new BnText(validationComposite_1,SWT.BORDER);
		txName.setModelProvider(getLocalModelProvider());
		txName.setPath( new Path("name"));
		new Label(this, SWT.NONE);

		validationComposite_8 = new ValidationComposite(this, SWT.NONE);

		closedBnButton = new BnButton(validationComposite_8, SWT.CHECK);
		closedBnButton.setModelProvider(localModelProvider);
		closedBnButton.setPath( new Path("closed"));
		closedBnButton.setText("Closed");

		validationComposite_9 = new ValidationComposite(this, SWT.NONE);

		bnButton = new BnButton(validationComposite_9, SWT.RADIO);
		bnButton.setModelProvider(localModelProvider);
		bnButton.setPath( new Path("closed"));
		bnButton.setText("Closed");

		validationComposite_10 = new ValidationComposite(this, SWT.NONE);

		bnText_4 = new BnText(validationComposite_10, SWT.BORDER);
		bnText_4.setModelProvider(localModelProvider);
		bnText_4.setPath( new Path("closed"));

		validationComposite_11 = new ValidationComposite(this, SWT.NONE);

		closedBnButton_1 = new BnButton(validationComposite_11, SWT.TOGGLE);
		closedBnButton_1.setModelProvider(localModelProvider);
		closedBnButton_1.setPath( new Path("closed"));
		closedBnButton_1.setText("Closed");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		validationComposite_12 = new ValidationComposite(this, SWT.NONE);

		openBnButton = new BnButton(validationComposite_12, SWT.RADIO);
		openBnButton.setModelProvider(localModelProvider);
		openBnButton.setPath( new Path("open"));
		openBnButton.setText("Open");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		label_1 = new Label(this, SWT.NONE);
		label_1.setLayoutData(new GridData());
		label_1.setText("Date");

		validationComposite_2 = new ValidationComposite(this, SWT.NONE);
		validationComposite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));

		bnText_3 = new BnText(validationComposite_2, SWT.BORDER);
		bnText_3.setModelProvider(localModelProvider);
		bnText_3.setPath(new Path("date"));
		new Label(this, SWT.NONE);
		priorityLabel = new Label(this, SWT.NONE);
		priorityLabel.setText("Priority");
		validationComposite_4 = new ValidationComposite(this, SWT.NONE);
		validationComposite_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		bnCombo = new BnCombo(validationComposite_4, SWT.NONE);
		bnCombo.setBounds(0, 0, 162, 25);
		bnCombo.setValidationIndicator(validationComposite_4);
		bnCombo.setModelProvider(localModelProvider);
		bnCombo.setPath(new Path("priority"));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		validationComposite = new ValidationComposite(this, SWT.NONE);
		validationComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		bnCombo_2 = new BnCombo(validationComposite, SWT.READ_ONLY);
		bnCombo_2.setModelProvider(localModelProvider);
		bnCombo_2.setPath( new Path("priorityEditable"));
		validationComposite_5 = new ValidationComposite(this, SWT.NONE);
		validationComposite_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		bnCombo_1 = new BnCombo(validationComposite_5, SWT.READ_ONLY);
		bnCombo_1.setBounds(0, 0, 147, 25);
		bnCombo_1.setPath(new Path("priority"));
		bnCombo_1.setModelProvider(localModelProvider);
		bnCombo_1.setValidationIndicator(validationComposite_5);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		validationComposite_6 = new ValidationComposite(this, SWT.NONE);
		validationComposite_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		bnText_2 = new BnText(validationComposite_6, SWT.BORDER);
		bnText_2.setBounds(0, 0, 139, 27);
		bnText_2.setPath(new Path("priority"));
		bnText_2.setModelProvider(localModelProvider);
		bnText_2.setValidationIndicator(validationComposite_6);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		label_3 = new Label(this, SWT.NONE);
		label_3.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
		label_3.setText("Note");
		validationComposite_3 = new ValidationComposite(this, SWT.NONE);
		validationComposite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 4, 1));

		bnText = new BnText(validationComposite_3, SWT.MULTI | SWT.BORDER);
		bnText.setBounds(0, 0, 384, 187);
		bnText.setValidationIndicator(validationComposite_3);
		bnText.setModelProvider(localModelProvider);
		bnText.setPath( new Path("note"));
		new Label(this, SWT.NONE);

		bnPushButton = new BnPushButton(this, SWT.NONE);
		bnPushButton.setModelProvider(localModelProvider);
		bnPushButton.getButton().setText("Clear");
		bnPushButton.setPath( new Path("clear"));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		label_2 = new Label(this, SWT.NONE);
		label_2.setLayoutData(new GridData());
		label_2.setText("Text Length");
		validationComposite_7 = new ValidationComposite(this, SWT.NONE);
		validationComposite_7.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 2, 1));
		validationComposite_7.setTrace(true);
		bnText_1 = new BnText(validationComposite_7, SWT.BORDER | SWT.SINGLE);
		bnText_1.setModelProvider(localModelProvider);
		bnText_1.setPath(new Path("noteLength"));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		bnPushButton_1 = new BnPushButton(this, SWT.NONE);
		bnPushButton_1.setModelProvider(localModelProvider);
		bnPushButton_1.setPath( new Path("save"));
		bnPushButton_1.getButton().setText("Save");

		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		//
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}


	public static class ContactCell extends AbstractPM {
		TextPM name = new TextPM();
		DatePM date = new DatePM();
		TextPM note = new TextPM();
		IntegerPM noteLength = new IntegerPM();
		TextPM priority = new TextPM();
		OperationPM clear = new OperationPM();
		BooleanPM priorityEditable = new BooleanPM();
		BooleanPM closed = new BooleanPM();
		BooleanPM open = new BooleanPM();
		OperationPM save = new OperationPM();

		public ContactCell() {
			PMManager.setup(this);
			name.setMandatory(true);
			priority.setOptions(Options.create(1,2,3,4,5));
			priority.setText("1");
			updatePriority();
			updateOpen();
		}
		@OnChange(path="closed")
		void updateOpen() {
			if (closed.isValid()) {
				open.setBoolean(!closed.getBoolean());
			}
		}
		@OnChange(path="open")
		void updateClosed() {
			if (open.isValid()) {
				closed.setBoolean(!open.getBoolean());
			}
		}
		@OnChange(path="note")
		void updateNameLength() {
			noteLength.setInteger(note.getText().length());
		}
		@Validation(path="note",message="too much characters",validWhen=false)
		boolean hasTooMuchCharacters() {
			return note.getText().length()>20;
		}
		@Operation
		public void clear() {
			note.setText(null);
		}
		@Validation(path="clear")
		boolean canClear() {
			return note.getText().length()>0;
		}
		@OnChange(path="priorityEditable")
		void updatePriority() {
			priority.setEditable(priorityEditable.getBoolean());
		}
		@Operation
		void save() {
			System.out.println("Saving ContactNote");
		}
		@Validation(path="save")
		boolean canSave() {
			return isValid();
		}
	}
}
