package net.edge.media;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public final class GraphicalComponent {

	private final int[] raster;
	public final int width;
	public final int height;
	private final BufferedImage image;

	public GraphicalComponent(int width, int height) {
		this.width = width;
		this.height = height;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		raster = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		setCanvas();
	}

	public void drawGraphics(int x, int y, Graphics g) {
		g.drawImage(image, x, y, null);
	}

	public void setCanvas() {
		Rasterizer2D.setCanvas(raster, height, width);
	}
}
