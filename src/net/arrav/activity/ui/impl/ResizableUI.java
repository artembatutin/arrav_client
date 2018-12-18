package net.arrav.activity.ui.impl;

import net.arrav.activity.ui.UI;
import net.arrav.graphic.img.BitmapImage;

import java.awt.*;

public abstract class ResizableUI extends UI {

	/**
	 * Gets the offset for the main interface on the resizable mode.
	 */
	public Point getOnScreenWidgetOffsets() {
		int x = client.windowWidth / 2 - 256 - 204 / 2;
		int y = client.windowHeight / 2 - 167 - 159 / 2;
		if(client.windowWidth >= 1100) {
			x = client.windowWidth / 2 - 256;
		}
		if(client.windowHeight >= 800) {
			y = client.windowHeight / 2 - 167;
		}
		return new Point(x, y);
	}
	
	/**
	 * Gets the side image as an index.
	 * @param index index
	 * @return the side icon image.
	 */
	public abstract BitmapImage getSide(int index);
}
