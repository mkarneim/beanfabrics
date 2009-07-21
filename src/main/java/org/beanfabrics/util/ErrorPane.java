/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.UIManager;

/**
 * The <code>ErrorPane</code> is a panel that shows a message beside an error
 * icon and - when checking the checkbox - the stack trace of a
 * {@link Throwable}.
 * 
 * @author Max Gensthaler
 */
@SuppressWarnings("serial")
public class ErrorPane extends JPanel {
    private static JDialog dialog;
    private JLabel messageLabel;
    private JCheckBox stackTraceCheckBox;
    private JScrollPane stackTraceScrollPane;
    private JTextArea stackTraceTextArea;
    private final Object message;
    private final Throwable cause;
    private JLabel errorIconLabel;
    private JPanel stackTracePanel;

    /**
     * Main method to test this class.
     */
    public static void main(String[] args) {
        try {
            recursiveMethod(0);
        } catch (Exception e) {
            showErrorDialog("This is a test.", e);
        }
    }

    private static void recursiveMethod(int counter) {
        if (counter >= 20) {
            throw new RuntimeException("This is a test exception.");
        }
        recursiveMethod(counter + 1);
    }

    /**
     * Constructs a new instance of this class with no message and no
     * {@link Throwable}.
     */
    public ErrorPane() {
        this(null, null);
    }

    /**
     * Constructs a new instance of this class with the given message and no
     * {@link Throwable}.
     * 
     * @param message message to display
     */
    public ErrorPane(Object message) {
        this(message, null);
    }

    /**
     * Constructs a new instance of this class with the given message and the
     * given {@link Throwable}.
     * 
     * @param message message to display
     * @param cause <code>Throwable</code> to display the stack trace of
     */
    public ErrorPane(Object message, Throwable cause) {
        this.message = message;
        this.cause = cause;
        this.setLayout(new GridBagLayout());
        this.add(getErrorIconLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
        this.add(getMessageLabel(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 0, 10, 10), 0, 0));
        this.add(getStackTracePanel(), new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 0, 10), 0, 0));
    }

    private JPanel getStackTracePanel() {
        if (stackTracePanel == null) {
            stackTracePanel = new JPanel();
            stackTracePanel.setLayout(new BorderLayout());
            if (cause != null) {
                stackTracePanel.add(getStackTraceCheckBox(), BorderLayout.NORTH);
                stackTracePanel.add(getStackTraceScrollPane(), BorderLayout.CENTER);
            }
        }
        return stackTracePanel;
    }

    private JLabel getErrorIconLabel() {
        if (errorIconLabel == null) {
            Icon errorIcon = UIManager.getIcon("OptionPane.errorIcon");
            errorIconLabel = new JLabel(errorIcon);
        }
        return errorIconLabel;
    }

    private JLabel getMessageLabel() {
        if (messageLabel == null) {
            messageLabel = new JLabel();
            messageLabel.setText(message.toString());
        }
        return messageLabel;
    }

    private JCheckBox getStackTraceCheckBox() {
        if (stackTraceCheckBox == null) {
            Action action = new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {
                    Object source = evt.getSource();
                    if (source instanceof JCheckBox) {
                        JCheckBox checkBox = (JCheckBox)source;
                        JScrollPane scrollPane = ErrorPane.this.getStackTraceScrollPane();
                        scrollPane.setVisible(checkBox.isSelected());
                        if (checkBox.isSelected()) {
                            scrollPane.requestFocus();
                        }
                        if (dialog != null) {
                            dialog.pack();
                        }
                        getStackTracePanel().revalidate();
                    }
                }
            };
            stackTraceCheckBox = new JCheckBox(action);
            stackTraceCheckBox.setText("show details");
        }
        return stackTraceCheckBox;
    }

    private JScrollPane getStackTraceScrollPane() {
        if (stackTraceScrollPane == null) {
            stackTraceScrollPane = new JScrollPane();
            stackTraceScrollPane.setViewportView(getStackTraceTextArea());
            stackTraceScrollPane.setPreferredSize(new Dimension(500, 200));
            stackTraceScrollPane.setVisible(getStackTraceCheckBox().isSelected());
        }
        return stackTraceScrollPane;
    }

    private JTextArea getStackTraceTextArea() {
        if (stackTraceTextArea == null) {
            stackTraceTextArea = new JTextArea();
            stackTraceTextArea.setEditable(false);
            if (cause != null) {
                Writer writer = new StringWriter();
                cause.printStackTrace(new PrintWriter(writer));
                stackTraceTextArea.setText(writer.toString());
            }
        }
        return stackTraceTextArea;
    }

    /**
     * Show a {@link JDialog} with the given message.
     * 
     * @param message message to display
     */
    public static void showErrorDialog(Object message) {
        showErrorDialog(null, "Error", message, null);
    }

    /**
     * Show a {@link JDialog} with the given message and the given
     * {@link Throwable}.
     * 
     * @param message message to display, may be <code>null</code>
     * @param cause <code>Throwable</code> to display the stack trace of, may be
     *            <code>null</code>
     */
    public static void showErrorDialog(Object message, Throwable cause) {
        showErrorDialog(null, "Error", message, cause);
    }

    /**
     * Show a {@link JDialog} which has the given parent component and the given
     * title, showing the given message and the given {@link Throwable}.
     * 
     * @param parentComponent parent component of the dialog
     * @param title title of the dialog
     * @param message message to show in the dialog, may be <code>null</code>
     * @param cause Throwable to show the stack trace of in the dialog, may be
     *            <code>null</code>
     */
    public static void showErrorDialog(Component parentComponent, String title, Object message, Throwable cause) {
        showErrorDialog(parentComponent, false, title, message, cause);
    }

    /**
     * Show a {@link JDialog} which has the given parent component, the given
     * modality and the given title, showing the given message and the given
     * {@link Throwable}.
     * 
     * @param parentComponent parent component of the dialog
     * @param modal specifies whether the dialog should be modal
     * @param title title of the dialog
     * @param message message to show in the dialog, may be <code>null</code>
     * @param cause Throwable to show the stack trace of in the dialog, may be
     *            <code>null</code>
     */
    public static void showErrorDialog(Component parentComponent, boolean modal, String title, Object message, Throwable cause) {
        dialog = createDialog(parentComponent, modal, title, message, cause);
        dialog.pack();
        dialog.setLocationRelativeTo(parentComponent);
        dialog.setVisible(true);
    }

    private static JDialog createDialog(Component owner, boolean modal, String title, Object message, Throwable cause) {
        Window window = getWindowForComponent(owner);
        if (window instanceof Frame) {
            dialog = new ErrorPaneDialog((Frame)window, title, message, cause);
        } else if (window instanceof Dialog) {
            dialog = new ErrorPaneDialog((Dialog)window, title, message, cause);
        } else {
            // window is null. Use a dummy frame as parent.
            dialog = new ErrorPaneDialog(new Frame(), title, message, cause);
        }
        dialog.setModal(modal);
        return dialog;
    }

    private static Window getWindowForComponent(Component component) {
        if (component != null) {
            for (Component parent; (parent = component.getParent()) != null;) {
                if (parent instanceof Window) {
                    return (Window)parent;
                }
            }
        }
        return null;
    }

    static class ErrorPaneDialog extends JDialog {
        private JPanel contentPane;
        private JPanel buttonPanel;
        private JButton okButton;

        public ErrorPaneDialog(Frame owner, String title, Object message, Throwable cause) {
            super(owner, title, true);
            init(message, cause);
            registerCloseOnEsc();
        }

        public ErrorPaneDialog(Dialog owner, String title, Object message, Throwable cause) {
            super(owner, title, true);
            init(message, cause);
            registerCloseOnEsc();
        }

        private void registerCloseOnEsc() {
            KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
            getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
            getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
        }

        private void init(Object message, Throwable cause) {
            this.setContentPane(getDialogContentPane(message, cause));
            this.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy() {
                @Override
                public Component getInitialComponent(Window window) {
                    return ErrorPaneDialog.this.getOkButton();
                }
            });
        }

        private JPanel getDialogContentPane(Object message, Throwable cause) {
            if (contentPane == null) {
                contentPane = new JPanel();
                contentPane.setLayout(new BorderLayout());
                contentPane.add(new ErrorPane(message, cause), BorderLayout.CENTER);
                contentPane.add(getButtonPanel(), BorderLayout.SOUTH);
            }
            return contentPane;
        }

        private JPanel getButtonPanel() {
            if (buttonPanel == null) {
                buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.add(getOkButton());
            }
            return buttonPanel;
        }

        private JButton getOkButton() {
            if (okButton == null) {
                okButton = new JButton();
                okButton.setText("OK");
                okButton.setMnemonic('O');
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ErrorPane.dialog.setVisible(false);
                        ErrorPane.dialog.dispose();
                    }
                });
            }
            return okButton;
        }
    }
}