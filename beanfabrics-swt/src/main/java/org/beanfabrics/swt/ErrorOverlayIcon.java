package org.beanfabrics.swt;

import org.beanfabrics.validation.ValidationState;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;

import com.swtdesigner.SWTResourceManager;

/**
 * Overlay icon to render above a {@link Control} and to indicate an invalid
 * validation state.
 *
 * <p>
 * IMPORTANT: this class does not work correctly for all controls since it can
 * not be ensured that it paints at last.
 * </p>
 *
 * @author Michael Karneim
 */
public class ErrorOverlayIcon implements ValidationIndicator {
	private final String IMAGE_NAME = "error_overlay.gif";
	private final Control control;
	private boolean visible = false;
	private int xOffset = 10;
	private int yOffset = 0;

	public ErrorOverlayIcon(Control aControl) {
		control = aControl;
		control.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				paint(e.gc);
			}
		});
	}

	public int getXOffset() {
		return xOffset;
	}

	public void setXOffset(int offset) {
		xOffset = offset;
	}

	public int getYOffset() {
		return yOffset;
	}

	public void setYOffset(int offset) {
		yOffset = offset;
	}

	public void setVisible(boolean newValue) {
		this.visible = newValue;
		control.redraw();
	}

	private void paint(GC gc) {
		if (visible) {
			gc.drawImage(getImage(), getXOffset(), getYOffset());
		}
	}

	private Image getImage() {
		return SWTResourceManager.getImage(ErrorOverlayIcon.class, IMAGE_NAME);
	}

	public void setValidationState(ValidationState state) {
		if (state == null) {
			setVisible(false);
		} else {
			setVisible(true);
		}
	}
}
