package net.edge.media;

public class Viewport {

	int[] scanOffsets;
	private int canvasWidth;
	public int width;
	public int height;
	public int centerX;
	public int centerY;

	public Viewport(int x1, int y1, int x2, int y2, int w) {
		canvasWidth = w;
		width = x2 - x1;
		height = y2 - y1;
		scanOffsets = new int[height];
		int offset = x1 + y1 * canvasWidth;
		for(int l = 0; l < height; l++) {
			scanOffsets[l] = offset;
			offset += canvasWidth;
		}
		centerX = width / 2;
		centerY = height / 2;
	}

	public int getX() {
		return scanOffsets[0] % canvasWidth;
	}

	public int getY() {
		return scanOffsets[0] / canvasWidth;
	}
}
