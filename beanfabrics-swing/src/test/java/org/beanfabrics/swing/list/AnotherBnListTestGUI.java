/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.list;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.beanfabrics.Binder;
import org.beanfabrics.BnModelObserver;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IconTextPM;
import org.beanfabrics.model.MapPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Validation;
import org.beanfabrics.swing.BnAction;
import org.beanfabrics.swing.BnButton;

/**
 * @author Michael Karneim
 */
@SuppressWarnings("serial")
public class AnotherBnListTestGUI extends JFrame implements View<AnotherBnListTestGUI.DirectoryBrowserModel>, ModelSubscriber {
    private static JFileChooser FILE_CHOOSER;
    private final Link link = new Link(this);

    private ModelProvider localProvider;
    private BnButton btnChangeToParent;
    private JPanel buttonPanel;
    private BnAction changeDirAction;
    private BnModelObserver propertyObserver;
    private BnList bnList;
    private JScrollPane scrollPane;
    private JPanel panel;

    public static void main(String[] args)
        throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        FILE_CHOOSER = new JFileChooser();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                AnotherBnListTestGUI f = new AnotherBnListTestGUI();
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(400, 400);
                f.setLocationRelativeTo(null);

                DirectoryBrowserModel dirMdl = new DirectoryBrowserModel();
                dirMdl.setDirectory(new File(System.getProperty("user.home")));

                Binder.bind(f, dirMdl);

                f.setVisible(true);
            }
        });
    }

    public AnotherBnListTestGUI() {
        super();
        setLayout(new BorderLayout());
        getContentPane().add(getPanel(), BorderLayout.CENTER);
        setTitle("File System");

        getPropertyObserver(); // make sure that the prop. observer is
        // instantiated
        //
    }

    /** {@inheritDoc} */
    public DirectoryBrowserModel getPresentationModel() {
        return getLocalProvider().getPresentationModel();
    }

    /** {@inheritDoc} */
    public void setPresentationModel(DirectoryBrowserModel pModel) {
        getLocalProvider().setPresentationModel(pModel);
    }

    /** {@inheritDoc} */
    public IModelProvider getModelProvider() {
        return link.getModelProvider();
    }

    /** {@inheritDoc} */
    public void setModelProvider(IModelProvider provider) {
        this.link.setModelProvider(provider);
    }

    /** {@inheritDoc} */
    public Path getPath() {
        return link.getPath();
    }

    /** {@inheritDoc} */
    public void setPath(Path path) {
        this.link.setPath(path);
    }

    protected ModelProvider getLocalProvider() {
        if (localProvider == null) {
            localProvider = new ModelProvider(); // @wb:location=11,442
            localProvider.setPresentationModelType(DirectoryBrowserModel.class);
        }
        return localProvider;
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.setBorder(new EmptyBorder(12, 8, 12, 8));
            panel.setLayout(new BorderLayout());
            panel.add(getScrollPane(), BorderLayout.CENTER);
            panel.add(getButtonPanel(), BorderLayout.NORTH);
        }
        return panel;
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(getBnList());
        }
        return scrollPane;
    }

    private BnList getBnList() {
        if (bnList == null) {
            bnList = new BnList();
            bnList.addMouseListener(new MouseAdapter() {
                public void mouseClicked(final MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        getChangeDirAction().actionPerformed(null);
                    }
                }
            });
            bnList.setPath(new org.beanfabrics.Path("this.elements"));
            bnList.setModelProvider(getLocalProvider());
            //bnList.setCellConfig(new CellConfig(new Path("name")));

            bnList.setLayoutOrientation(JList.VERTICAL_WRAP);
            bnList.setVisibleRowCount(-1);
        }
        return bnList;
    }

    private BnModelObserver getPropertyObserver() {
        // observe the "title" property of the model and change this frame's
        // title accordingly
        if (propertyObserver == null) {
            propertyObserver = new BnModelObserver(); // @wb:location=116,467
            propertyObserver.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(final PropertyChangeEvent evt) {
                    TextPM titlePM = (TextPM)propertyObserver.getPresentationModel();
                    if (titlePM != null) {
                        setTitle(titlePM.getText());
                    } else {
                        setTitle("");
                    }
                }
            });
            propertyObserver.setModelProvider(getLocalProvider());
            propertyObserver.setPath(new Path("this.title"));
        }
        return propertyObserver;
    }

    protected BnAction getChangeDirAction() {
        if (changeDirAction == null) {
            changeDirAction = new BnAction(); // @wb:location=238,461
            changeDirAction.setModelProvider(getLocalProvider());
            changeDirAction.setPath(new Path("changeDir"));
        }
        return changeDirAction;
    }

    protected JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            final FlowLayout flowLayout = new FlowLayout();
            flowLayout.setAlignment(FlowLayout.LEFT);
            buttonPanel.setLayout(flowLayout);
            buttonPanel.add(getBtnChangeToParent());
        }
        return buttonPanel;
    }

    protected BnButton getBtnChangeToParent() {
        if (btnChangeToParent == null) {
            btnChangeToParent = new BnButton();
            btnChangeToParent.setPath(new org.beanfabrics.Path("this.changeToParentDir"));
            btnChangeToParent.setModelProvider(getLocalProvider());
            btnChangeToParent.setFocusable(false);
            btnChangeToParent.setText("..");
        }
        return btnChangeToParent;
    }

    /**
     * PresentationModel for a file entry.
     */
    private static class FileModel extends AbstractPM {
        private File file;
        protected final IconTextPM name = new IconTextPM();

        public FileModel() {
            PMManager.setup(this);
            name.setEditable(false);
        }

        public void setFile(File f) {
            this.file = f;
            name.setText(f.getName());
            name.setIcon(FILE_CHOOSER.getIcon(f));
            name.setDescription("<html>Last Changed: " + new Date(f.lastModified()) + "\n<br>" + "Size: " + f.length() + " Bytes</html>");
        }

        public File getFile() {
            return file;
        }
    }

    /**
     * PresentationModel for the directory browser.
     */
    static class DirectoryBrowserModel extends AbstractPM {
        private File dir;
        protected final MapPM<File, FileModel> elements = new MapPM<File, FileModel>();
        protected final TextPM title = new TextPM();
        protected final OperationPM changeDir = new OperationPM();
        protected final OperationPM changeToParentDir = new OperationPM();

        public DirectoryBrowserModel() {
            PMManager.setup(this);
        }

        public void setDirectory(File dir) {
            this.dir = dir;
            elements.clear();
            this.title.setText(dir.getAbsolutePath());
            File[] files = dir.listFiles();
            for (File f : files) {
                FileModel pModel = new FileModel();
                pModel.setFile(f);
                elements.put(f, pModel);
            }
        }

        @Validation(path = "changeDir", message = "Select single directory")
        boolean isDirectorySelected() {
            return elements.getSelection().size() == 1 && elements.getSelection().getFirst().getFile().isDirectory();
        }

        @Operation
        public void changeDir() {
            this.changeDir.check();
            File toDir = elements.getSelection().getFirst().getFile();
            this.setDirectory(toDir);
        }

        @Validation(path = "changeToParentDir", message = "No parent directory available")
        boolean hasParentDirectory() {
            return dir != null && dir.getParentFile() != null;
        }

        @Operation
        public void changeToParentDir() {
            this.changeToParentDir.check();
            this.setDirectory(dir.getParentFile());
        }
    }
}