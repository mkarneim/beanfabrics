package org.beanfabrics.swt.samples;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.DateFormat;
import java.util.Date;

import org.beanfabrics.BnModelObserver;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.DatePM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.MapPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Service;
import org.beanfabrics.swt.model.ImageTextPM;
import org.beanfabrics.swt.table.BnTableDecorator;
import org.beanfabrics.swt.table.ViewConfigBuilder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

/**
 * This sample contains a file browser implemented with SWT and Beanfabrics. 
 * 
 * @author Michael Karneim
 */
public class FileBrowser {

	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			shell.setLayout( new FillLayout());
			
			FileBrowser browser = new FileBrowser(shell);
			browser.setDirectory( new File( System.getProperty("user.home")));

			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			browser.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0); // Necessary because the FileInfoRegistry uses some Swing code 
	}
	
	
	
	private FileBrowserModel model;
	private FileBrowserView view;
	/**
	 * The FileBrowser is a kind of facade that
	 * shields FileBrowserModel and FileBrowserView
	 * for convenience.
	 */
	public FileBrowser(Shell shell) {
		this.model = new FileBrowserModel();
		this.view = new FileBrowserView(shell, SWT.NONE);
		this.view.setPresentationModel( this.model);
	}
	
	public void setDirectory(File file) {
		model.setDirectory(file);
	}
	
	public void dispose() {
		model.dispose();
	}
	

	
	
	/**
	 * This is the model for a file entry used as row model in the table of files.
	 */
	private static class FileEntry extends AbstractPM {
		File file;
		FileInfoRegistry fileInfoRegistry;
		boolean useFileInfoName = true;
		ImageTextPM name = new ImageTextPM();
		DatePM lastModified = new DatePM();
		IntegerPM size = new IntegerPM();
		
		public FileEntry(File file) {
			PMManager.setup(this);
			this.file = file;
			name.setText(file.getName());
			DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
			lastModified.setFormat( format);
			lastModified.setDate(new Date(file.lastModified()));
			if (file.isDirectory()) {
				size.setText(null);
			} else {
				size.setLong(file.length());
			}
			updateIcon();
		}

		public FileEntry(File file, String displayName) {
			this(file);
			name.setText(displayName);
			useFileInfoName = false;
		}
		
		@Service
		public void setFileInfoRegistry( FileInfoRegistry reg) {
			this.fileInfoRegistry = reg;
			updateIcon();
		}
		
		private void updateIcon() {
			// if the FileInfoRegistry is available we can load the file icon and name
			if (this.fileInfoRegistry != null && this.file != null) {
				name.setImage( fileInfoRegistry.getImage(file));
				if (useFileInfoName) {
					name.setText(this.fileInfoRegistry.getName(file));
				}
			} else {
				name.setImage(null);
			}
		}
	}
	
	/**
	 * The model for the file browser.
	 */
	private static class FileBrowserModel extends AbstractPM {
		FileInfoRegistry fileInfoRegistry;
		File directory;
		TextPM directoryPath = new TextPM();
		MapPM<File, FileEntry> files = new MapPM<File, FileEntry>();

		public FileBrowserModel() {
			PMManager.setup(this);
			this.fileInfoRegistry = new FileInfoRegistry();
			// adding the FileInfoRegistry to the context makes it accessible for all child nodes
			this.getContext().addService(FileInfoRegistry.class, this.fileInfoRegistry);
		}

		public void setDirectory(File dir) {
			this.directory = dir;
			refresh();
		}

		private void refresh() {
			directoryPath.setText(directory.getAbsolutePath());
			// clear rows
			files.clear(); 
			fileInfoRegistry.clear(); // disposes unused images
			// add a new row for parent directory
			File parent = directory.getParentFile();
			if (parent != null) {
				files.put(parent, new FileEntry(parent, ".."));
			}
			// add new rows for all files in this directory 
			for (File file : directory.listFiles()) {
				files.put(file, new FileEntry(file));
			}
		}
		
		public void dispose() {
			fileInfoRegistry.clear();
		}

		public void enterSelectedDirectory() {
			File oldDir = this.directory;
			File nextDir = files.getSelectedKeys().getFirst();
			if (nextDir.isDirectory()) {
				setDirectory( nextDir);
				// select old directory in list
				if (files.containsKey(oldDir)) {
					files.getSelectedKeys().add(oldDir);
				}
			}
		}
	}
	
	/**
	 * The view on the file browser implemented as an SWT Composite.
	 */
	private static class FileBrowserView extends Composite implements View<FileBrowserModel> {
		private Table table;
		private BnTableDecorator bnTable;
		private ModelProvider localModelProvider;
		private BnModelObserver titleObserver;

		public FileBrowserView(Composite parent, int style) {
			super(parent, style);
			//
			setLayout( new FillLayout());
			// this is the table that shows the files
			table = new Table(this, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
			table.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(final KeyEvent e) {
					if (e.keyCode==13) { // Enter-Key pressed
						getPresentationModel().enterSelectedDirectory();
					}
				}
			});
			table.addMouseListener( new MouseAdapter() {
				@Override
				public void mouseDoubleClick(MouseEvent mouseevent) {
					getPresentationModel().enterSelectedDirectory();
				}
			});
			//table.setLinesVisible(true);
			table.setHeaderVisible(true);
			
			// this BnTable decorates the table above and does the Beanfabrics model binding
			bnTable = new BnTableDecorator(table);
			// define the provider of the model
			bnTable.setModelProvider(getLocalModelProvider());
			// define the path to the model
			bnTable.setPath( new Path("files"));
			// define the columns 
			bnTable.setViewConfig( new ViewConfigBuilder()
				.addColumn().setPath("name").setHeader("File").setWidth(180)
				.addColumn().setPath("lastModified").setHeader("Last Modified").setWidth(160)
				.addColumn().setPath("size").setHeader("Size").setWidth(80)
				.buildViewConfig()
			);
			
			// this observer is responsible for sync'ing the window title
			titleObserver = new BnModelObserver();
			titleObserver.setModelProvider(getLocalModelProvider());
			titleObserver.setPath( new Path("directoryPath"));
			titleObserver.addPropertyChangeListener( new PropertyChangeListener() {
				// whenever 'directoryPath' has changed this method is called
				public void propertyChange(PropertyChangeEvent evt) {
					TextPM directoryPath = (TextPM)titleObserver.getPresentationModel();
					if (directoryPath != null) {
						getShell().setText( directoryPath.getText());
					}
				}
			});
		}
		
		@Override
		protected void checkSubclass() {
			// Disable the check that prevents subclassing of SWT components
		}

		public FileBrowserModel getPresentationModel() {
			return getLocalModelProvider().getPresentationModel();
		}

		public void setPresentationModel(FileBrowserModel pModel) {
			getLocalModelProvider().setPresentationModel(pModel);
		}

		private ModelProvider getLocalModelProvider() {
			if (localModelProvider == null) {
				localModelProvider = new ModelProvider(); // @wb:location=42,439
				localModelProvider.setPresentationModelType(FileBrowserModel.class);
			}
			return localModelProvider;
		}
	}
	

}
