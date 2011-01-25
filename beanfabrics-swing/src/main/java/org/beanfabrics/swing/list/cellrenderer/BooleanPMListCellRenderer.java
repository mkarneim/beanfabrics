/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.swing.list.cellrenderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.CellRendererPane;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.beanfabrics.model.IBooleanPM;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.ErrorIconPainter;

/**
 * @author Michael Karneim
 */
// TODO (mk) this class contains several workarounds for
//         a bug in nimbus l&f: the alternating background is painted wrong if
//         we don't extend the DefaultListCellRenderer. See mantis 0000029.
@SuppressWarnings("serial")
public class BooleanPMListCellRenderer extends JPanel implements PMListCellRenderer {
    private CellRendererPane cellRendererPane = new CellRendererPane();
    private JCheckBox cb = new JCheckBox();
    private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    private ErrorIconPainter errorIconPainter = createDefaultErrorIconPainter();

    private IBooleanPM model;
    private JComponent rendererComponent;

    public BooleanPMListCellRenderer() {
        cb.setHorizontalAlignment(SwingConstants.CENTER);
        cb.setBorderPainted(false);
        cb.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.add(this.cb, BorderLayout.CENTER);
    }

    /** {@inheritDoc} */
    public boolean supportsPresentationModel(PresentationModel pModel) {
        return pModel instanceof IBooleanPM;
    }

    /** {@inheritDoc} */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof IBooleanPM == false) {
            return null;
        }
        model = (IBooleanPM)value;
        Boolean bvalue = false;
        if (model.getBoolean() != null) {
            bvalue = model.getBoolean();
        }
        cb.setSelected(bvalue);

        if (isNimbus()) { // one more work around
            if (rendererComponent != null) {
                rendererComponent.setBackground(null);
            }
            rendererComponent = (JComponent)defaultRenderer.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
            if (isSelected == false && index % 2 != 0) {
                rendererComponent.setBackground(Color.white);
            }
        } else {
            rendererComponent = (JComponent)defaultRenderer.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
        }

        return this;
    }

    private boolean isNimbus() {
        return "Nimbus".equals(UIManager.getLookAndFeel().getID());
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (rendererComponent != null) {
            rendererComponent.setBackground(bg);
        }
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        if (rendererComponent != null) {
            rendererComponent.setForeground(fg);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        cellRendererPane.paintComponent(g, rendererComponent, this, 0, 0, getWidth(), getHeight());
    }

    private ErrorIconPainter createDefaultErrorIconPainter() {
        ErrorIconPainter result = new ErrorIconPainter();
        return result;
    }

    public ErrorIconPainter getErrorIconPainter() {
        return errorIconPainter;
    }

    public void setErrorIconPainter(ErrorIconPainter aErrorIconPainter) {
        if (aErrorIconPainter == null) {
            throw new IllegalArgumentException("aErrorIconPainter == null");
        }
        this.errorIconPainter = aErrorIconPainter;
    }

    /** {@inheritDoc} */
    @Override
    public void paintChildren(Graphics g) {
        super.paintChildren(g);
        if (shouldPaintErrorIcon()) {
            errorIconPainter.paint(g, this);
        }
    }

    private boolean shouldPaintErrorIcon() {
        if (model == null) {
            return false;
        }
        return (model.isValid() == false);
    }
}