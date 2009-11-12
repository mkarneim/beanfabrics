/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
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
import org.beanfabrics.swing.ErrorImagePainter;

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
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        cellRendererPane.paintComponent(g, rendererComponent, this, 0, 0, getWidth(), getHeight());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // TODO shouldn't this be moved to the method paintComponent()?
        this.paintErrorIcon(g);
    }

    /**
     * Paints an error icon on top of the given {@link Graphics} if this
     * component is connected to an {@link PresentationModel} and this
     * <code>PresentationModel</code> has an invalid validation state.
     *
     * @param g the <code>Graphics</code> to paint the error icon to
     */
    protected void paintErrorIcon(Graphics g) {
        if (model != null && model.isValid() == false) {
            boolean isRightAligned = cb.getHorizontalAlignment() == SwingConstants.RIGHT || cb.getHorizontalAlignment() == SwingConstants.TRAILING;
            ErrorImagePainter.getInstance().paintTrailingErrorImage(g, this, isRightAligned);
        }
    }
}