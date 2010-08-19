package org.beanfabrics.swt.model;

import org.beanfabrics.model.TextPM;
import org.eclipse.swt.graphics.Image;

/**
 * @author Michael Karneim
 */
public class ImageTextPM extends TextPM implements IImagePM {
	private Image image;

	public void setImage(Image newImage) {
		if (equals(newImage, image)) {
			return;
		}
		Image old = this.image;
		this.image = newImage;
		this.getPropertyChangeSupport().firePropertyChange("image", old, newImage);
	}

	public Image getImage() {
		return image;
	}

	@Override
	public boolean isEmpty() {
		return image == null && super.isEmpty();
	}
}
