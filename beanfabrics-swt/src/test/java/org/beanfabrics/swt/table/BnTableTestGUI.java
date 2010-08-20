package org.beanfabrics.swt.table;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.DatePM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.PropertySupport;
import org.beanfabrics.swt.model.ImageTextPM;
import org.beanfabrics.swt.old.BnPushButton;
import org.beanfabrics.swt.old.BnText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

/**
 * @author Michael Karneim
 */
public class BnTableTestGUI extends Shell implements View<BnTableTestGUI.ContactNoteList> {
	private Button button;
	private static final URL EDIT = BnTableTestGUI.class.getResource("edit.gif");
	private static final URL VIEW = BnTableTestGUI.class.getResource("view.gif");

	private static Image imgEdit;
	private static Image imgView;

	private BnPushButton replaceBnButton;
	private Table table2;
	private Composite composite;
	private Table table;
	private BnText bnText_2;
	private BnText bnText_1;
	private BnText bnText;
	private Composite detailPanel;
	private ModelProvider modelProvider;
	private BnTableDecorator bnTable;
	private BnTableDecorator bnTable2;
	private Composite centerPanel;
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			loadImages();

			ContactNoteList model = new ContactNoteList();
			populate( model);
			Display display = Display.getDefault();
			BnTableTestGUI shell = new BnTableTestGUI(display, SWT.SHELL_TRIM);
			shell.setPresentationModel(model);

			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disposeImages();
		}
	}

	private static void disposeImages() {
		if (imgEdit != null) {
			imgEdit.dispose();
		}
		if (imgView != null) {
			imgView.dispose();
		}
	}

	private static void loadImages() throws IOException {
		imgEdit = getImage( EDIT.openStream());
		imgView = getImage( VIEW.openStream());
	}

	/**
	 * Create the shell
	 * @param display
	 * @param style
	 */
	public BnTableTestGUI(Display display, int style) {
		super(display, style);
		modelProvider = new ModelProvider(); // @wb:location=28,436
		modelProvider.setPresentationModelType(ContactNoteList.class);
		createContents();
		setLayout(new FillLayout());
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(500, 375);

		centerPanel = new Composite(this, SWT.NONE);
		centerPanel.setLayout(new FillLayout());

		table = new Table(centerPanel, SWT.BORDER | SWT.MULTI  | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		bnTable = new BnTableDecorator(table );
		bnTable.setViewConfig( new ViewConfigBuilder()
			.addColumn().setPath("name").setHeader("Name").setWidth(100)
			.addColumn().setPath("date").setHeader("Date").setWidth(80)
			.addColumn().setPath("note").setHeader("Note").setWidth(180)
			.buildViewConfig()
		);
		bnTable.setPath( new Path("this"));
		bnTable.setModelProvider(modelProvider);

		detailPanel = new Composite(this, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		detailPanel.setLayout(gridLayout);

		replaceBnButton = new BnPushButton(detailPanel, SWT.NONE);
		replaceBnButton.setModelProvider(modelProvider);
		replaceBnButton.getButton().setText("Replace");
		replaceBnButton.setPath( new Path("replaceSelected"));

		button = new Button(detailPanel, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (bnTable2.getModelProvider() == null) {
					bnTable2.setModelProvider(modelProvider);
				} else {
					bnTable2.setModelProvider(null);
				}
			}
		});
		button.setText("button");

		bnText = new BnText(detailPanel, SWT.BORDER);
		bnText.setModelProvider(modelProvider);
		bnText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		bnText.setPath( new Path("firstSelected.name"));

		bnText_1 = new BnText(detailPanel, SWT.BORDER);
		bnText_1.setModelProvider(modelProvider);
		bnText_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		bnText_1.setPath( new Path("firstSelected.date"));

		bnText_2 = new BnText(detailPanel, SWT.MULTI | SWT.BORDER);
		bnText_2.setModelProvider(modelProvider);
		bnText_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		bnText_2.setPath( new Path("firstSelected.note"));

		composite = new Composite(this, SWT.NONE);
		composite.setLayout(new FillLayout());

		table2 = new Table(composite, SWT.BORDER | SWT.MULTI  | SWT.FULL_SELECTION);
		table2.setLinesVisible(true);
		table2.setHeaderVisible(true);


		bnTable2 = new BnTableDecorator(table2 );
		bnTable2.setViewConfig( new ViewConfigBuilder()
			.addColumn().setPath("name").setHeader("Name").setWidth(100)
			.addColumn().setPath("date").setHeader("Date").setWidth(80)
			.addColumn().setPath("note").setHeader("Note").setWidth(180)
			.buildViewConfig()
		);
		bnTable2.setPath( new Path("this"));
		//bnTable2.setModelProvider(modelProvider);
		//
	}


	static class ContactNote extends AbstractPM {
		ImageTextPM name = new ImageTextPM();
		DatePM date = new DatePM();
		TextPM note = new TextPM();

		public ContactNote() {
			PMManager.setup(this);
			name.setImage( imgEdit);
			name.setEditable(false);
			date.setEditable(false);
			note.setEditable(true);
		}
	}

	public static class ContactNoteList extends ListPM<ContactNote> {
		ContactNote firstSelected;
		OperationPM replaceSelected = new OperationPM();

		public ContactNoteList() {
			PMManager.setup(this);
		}

		@OnChange
		void refreshFirstSelected() {

			firstSelected = getSelection().getFirst();
			PropertySupport.get(this).refresh();
		}
		@Operation
		void replaceSelected() {
			if (firstSelected != null) {
				ContactNote newElement = new ContactNote();
				this.replace(firstSelected, newElement);
			}
		}
	}

	public ContactNoteList getPresentationModel() {
		return modelProvider.getPresentationModel();
	}

	public void setPresentationModel(ContactNoteList pModel) {
		modelProvider.setPresentationModel(pModel);
	}

	static void populate( ContactNoteList list) {
		for( int i=0; i<100; ++i) {
			ContactNote c = new ContactNote();
			c.name.setText("Name "+i);
			c.date.setDate( new Date());
			c.note.setText( "Some notes");
			list.add(c);
		}
	}

	/**
     * Returns an image encoded by the specified input stream
     * @param is InputStream The input stream encoding the image data
     * @return Image The image encoded by the specified input stream
     */
    static Image getImage(InputStream is) {
        Display display = Display.getCurrent();
        ImageData data = new ImageData(is);
        if (data.transparentPixel > 0)
            return new Image(display, data, data.getTransparencyMask());
        return new Image(display, data);
    }
}
