/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.path;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;

import org.beanfabrics.meta.PathElementInfo;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.BooleanPM;
import org.beanfabrics.model.DatePM;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;

/**
 * @author Michael Karneim
 */
public class PathBrowserPanelTestGUI {
    public static void main(String[] args) {
        PathElementInfo pathElementInfo = PMManager.getInstance().getMetadata().getPathElementInfo(ProjectModel.class);

        final PathBrowserPM model = new PathBrowserPM();
        model.setPathContext(new PathContext(pathElementInfo, PMManager.getInstance().getMetadata().getTypeInfo(ITextPM.class), null));

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                PathBrowserPanel panel = new PathBrowserPanel();
                panel.setPresentationModel(model);

                JFrame frame = new JFrame("Path Browser");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(panel, BorderLayout.CENTER);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    private static class ProjectModel extends AbstractPM {
        TextPM name = new TextPM();
        DatePM dueDate = new DatePM();
        ContactModel manager = new ContactModel();
        ListPM<ContactModel> members = new ListPM<ContactModel>();

        public ProjectModel() {
            PMManager.setup(this);
        }
    }

    private static class ContactModel extends AbstractPM {
        TextPM firstname = new TextPM();
        TextPM lastname = new TextPM();
        DatePM dateOfBirth = new DatePM();
        ListPM<EmailAddressModel> emailAddresses = new ListPM<EmailAddressModel>();

        public ContactModel() {
            PMManager.setup(this);
        }
    }

    private static class EmailAddressModel extends TextPM {
        BooleanPM isMain = new BooleanPM();

        public EmailAddressModel() {
            PMManager.setup(this);
        }
    }
}