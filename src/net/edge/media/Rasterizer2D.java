package net.edge.media;

/**
 * Handles 2D graphic components drawing.
 */
public class Rasterizer2D {

	public static int[] canvasRaster;
	public static int canvasWidth;
	public static int canvasHeight;
	public static int clipStartY;
	public static int clipEndY;
	public static int clipStartX;
	public static int clipEndX;
	public static int clipCenterX;
	public static int clipCenterY;

	/**
	 * Clears the whole area that has been initialized to black.
	 */
	public static void clearCanvas() {
		int index = 0;
		int length = canvasWidth * canvasHeight - 7;
		while(index < length) {
			canvasRaster[index++] = 0;
			canvasRaster[index++] = 0;
			canvasRaster[index++] = 0;
			canvasRaster[index++] = 0;
			canvasRaster[index++] = 0;
			canvasRaster[index++] = 0;
			canvasRaster[index++] = 0;
			canvasRaster[index++] = 0;
		}
		length += 7;
		while(index < length) {
			canvasRaster[index++] = 0;
		}
	}

	/**
	 * Draws a point with the specified color at (x, y).
	 */
	public static void drawPoint(int x, int y, int color) {
		if(x >= clipStartX && y >= clipStartY && x < clipEndX && y < clipEndY) {
			canvasRaster[x + y * canvasWidth] = color;
		}
	}

	/**
	 * Draws a point with the specified color and alpha value at (x, y).
	 */
	public static void drawPoint(int x, int y, int color, int alpha) {
		if(alpha == 0)
			return;
		if(x >= clipStartX && y >= clipStartY && x < clipEndX && y < clipEndY) {
			int pos = x + y * canvasWidth;
			int a2 = 256 - alpha;
			int r1 = (color >> 16 & 0xff) * alpha;
			int g1 = (color >> 8 & 0xff) * alpha;
			int b1 = (color & 0xff) * alpha;
			int r2 = (canvasRaster[pos] >> 16 & 0xff) * a2;
			int g2 = (canvasRaster[pos] >> 8 & 0xff) * a2;
			int b2 = (canvasRaster[pos] & 0xff) * a2;
			canvasRaster[pos] = (r1 + r2 >> 8 << 16) + (g1 + g2 >> 8 << 8) + (b1 + b2 >> 8);
		}
	}

	/**
	 * Draws a horizontal line.
	 */
	public static void drawHorizontalLine(int x, int y, int length, int color) {
		if(y < clipStartY || y >= clipEndY) {
			return;
		}
		if(x < clipStartX) {
			length -= clipStartX - x;
			x = clipStartX;
		}
		if(x + length > clipEndX) {
			length = clipEndX - x;
		}
		final int i1 = x + y * canvasWidth;
		for(int j1 = 0; j1 < length; j1++) {
			canvasRaster[i1 + j1] = color;
		}
	}

	/**
	 * Draws a horizontal line with an alpha value support.
	 */
	public static void drawHorizontalLine(int x, int y, int length, int color, int alpha) {
		if(alpha == 0)
			return;
		if(y < clipStartY || y >= clipEndY) {
			return;
		}
		if(x < clipStartX) {
			length -= clipStartX - x;
			x = clipStartX;
		}
		if(x + length > clipEndX) {
			length = clipEndX - x;
		}
		final int j1 = 256 - alpha;
		final int k1 = (color >> 16 & 0xff) * alpha;
		final int l1 = (color >> 8 & 0xff) * alpha;
		final int i2 = (color & 0xff) * alpha;
		int i3 = x + y * canvasWidth;
		for(int j3 = 0; j3 < length; j3++) {
			final int j2 = (canvasRaster[i3] >> 16 & 0xff) * j1;
			final int k2 = (canvasRaster[i3] >> 8 & 0xff) * j1;
			final int l2 = (canvasRaster[i3] & 0xff) * j1;
			final int k3 = (k1 + j2 >> 8 << 16) + (l1 + k2 >> 8 << 8) + (i2 + l2 >> 8);
			canvasRaster[i3++] = k3;
		}
	}

	/**
	 * Draws a line from (x1, y1) to (x2, y2) with the specified color.
	 */
	public static void drawDiagonalLine(int x1, int y1, int x2, int y2, int color) {
		x2 -= x1;
		y2 -= y1;
		if(y2 == 0) {
			if(x2 >= 0) {
				drawHorizontalLine(x1, y1, x2 + 1, color);
				return;
			} else {
				drawHorizontalLine(x1 + x2, y1, -x2 + 1, color);
				return;
			}
		}
		if(x2 == 0) {
			if(y2 >= 0) {
				drawVerticalLine(x1, y1, y2 + 1, color);
				return;
			} else {
				drawVerticalLine(x1, y1 + y2, -y2 + 1, color);
				return;
			}
		}
		if(x2 + y2 < 0) {
			x1 += x2;
			x2 = -x2;
			y1 += y2;
			y2 = -y2;
		}
		if(x2 > y2) {
			y1 <<= 16;
			y1 += 32768;
			y2 <<= 16;
			int dy = (int) Math.floor((double) y2 / (double) x2 + 0.5D);
			x2 += x1;
			if(x1 < clipStartX) {
				y1 += dy * (clipStartX - x1);
				x1 = clipStartX;
			}
			if(x2 >= clipEndX) {
				x2 = clipEndX - 1;
			}
			for(; x1 <= x2; x1++) {
				int y = y1 >> 16;
				if(y >= clipStartY && y < clipEndY) {
					canvasRaster[x1 + y * canvasWidth] = color;
				}
				y1 += dy;
			}
			return;
		}
		x1 <<= 16;
		x1 += 32768;
		x2 <<= 16;
		int dx = (int) Math.floor((double) x2 / (double) y2 + 0.5D);
		y2 += y1;
		if(y1 < clipStartY) {
			x1 += dx * (clipStartY - y1);
			y1 = clipStartY;
		}
		if(y2 >= clipEndY) {
			y2 = clipEndY - 1;
		}
		for(; y1 <= y2; y1++) {
			int x = x1 >> 16;
			if(x >= clipStartX && x < clipEndX) {
				canvasRaster[x + y1 * canvasWidth] = color;
			}
			x1 += dx;
		}
	}

	/**
	 * Draws a line from (x1, y1) to (x2, y2) with the specified color and alpha
	 * value.
	 */
	public static void drawDiagonalLine(int x1, int y1, int x2, int y2, int color, int alpha) {
		if(alpha == 0)
			return;
		x2 -= x1;
		y2 -= y1;
		if(y2 == 0) {
			if(x2 >= 0) {
				drawHorizontalLine(x1, y1, x2 + 1, color, alpha);
				return;
			} else {
				drawHorizontalLine(x1 + x2, y1, -x2 + 1, color, alpha);
				return;
			}
		}
		if(x2 == 0) {
			if(y2 >= 0) {
				drawVerticalLine(x1, y1, y2 + 1, color, alpha);
				return;
			} else {
				drawVerticalLine(x1, y1 + y2, -y2 + 1, color, alpha);
				return;
			}
		}
		if(x2 + y2 < 0) {
			x1 += x2;
			x2 = -x2;
			y1 += y2;
			y2 = -y2;
		}
		int a2 = 256 - alpha;
		int r1 = (color >> 16 & 0xff) * alpha;
		int g1 = (color >> 8 & 0xff) * alpha;
		int b1 = (color & 0xff) * alpha;
		if(x2 > y2) {
			y1 <<= 16;
			y1 += 32768;
			y2 <<= 16;
			int dy = (int) Math.floor((double) y2 / (double) x2 + 0.5D);
			x2 += x1;
			if(x1 < clipStartX) {
				y1 += dy * (clipStartX - x1);
				x1 = clipStartX;
			}
			if(x2 >= clipEndX) {
				x2 = clipEndX - 1;
			}
			for(; x1 <= x2; x1++) {
				int y = y1 >> 16;
				if(y >= clipStartY && y < clipEndY) {
					int pos = x1 + y * canvasWidth;
					int r2 = (canvasRaster[pos] >> 16 & 0xff) * a2;
					int g2 = (canvasRaster[pos] >> 8 & 0xff) * a2;
					int b2 = (canvasRaster[pos] & 0xff) * a2;
					canvasRaster[pos] = (r1 + r2 >> 8 << 16) + (g1 + g2 >> 8 << 8) + (b1 + b2 >> 8);
				}
				y1 += dy;
			}
			return;
		}
		x1 <<= 16;
		x1 += 32768;
		x2 <<= 16;
		int dx = (int) Math.floor((double) x2 / (double) y2 + 0.5D);
		y2 += y1;
		if(y1 < clipStartY) {
			x1 += dx * (clipStartY - y1);
			y1 = clipStartY;
		}
		if(y2 >= clipEndY) {
			y2 = clipEndY - 1;
		}
		for(; y1 <= y2; y1++) {
			int x = x1 >> 16;
			if(x >= clipStartX && x < clipEndX) {
				int pos = x + y1 * canvasWidth;
				int r2 = (canvasRaster[pos] >> 16 & 0xff) * a2;
				int g2 = (canvasRaster[pos] >> 8 & 0xff) * a2;
				int b2 = (canvasRaster[pos] & 0xff) * a2;
				canvasRaster[pos] = (r1 + r2 >> 8 << 16) + (g1 + g2 >> 8 << 8) + (b1 + b2 >> 8);
			}
			x1 += dx;
		}
	}

	/**
	 * Draws a non-filled rectangle.
	 */
	public static void drawRectangle(int x, int y, int width, int height, int color) {
		drawHorizontalLine(x, y, width, color);
		drawHorizontalLine(x, y + height - 1, width, color);
		drawVerticalLine(x, y, height, color);
		drawVerticalLine(x + width - 1, y, height, color);
	}

	/**
	 * Draws a non-filled rectangle with an alpha value support.
	 */
	public static void drawRectangle(int x, int y, int width, int height, int color, int alpha) {
		if(alpha == 0)
			return;
		drawHorizontalLine(x, y, width, color, alpha);
		drawHorizontalLine(x, y + height - 1, width, color, alpha);
		if(height >= 3) {
			drawVerticalLine(x, y + 1, height - 2, color, alpha);
			drawVerticalLine(x + width - 1, y + 1, height - 2, color, alpha);
		}
	}

	/**
	 * Draws a vertical line.
	 */
	public static void drawVerticalLine(int x, int y, int length, int color) {
		if(x < clipStartX || x >= clipEndX) {
			return;
		}
		if(y < clipStartY) {
			length -= clipStartY - y;
			y = clipStartY;
		}
		if(y + length > clipEndY) {
			length = clipEndY - y;
		}
		final int j1 = x + y * canvasWidth;
		for(int k1 = 0; k1 < length; k1++) {
			canvasRaster[j1 + k1 * canvasWidth] = color;
		}
	}

	/**
	 * Draws a vertical line with an alpha value support.
	 */
	public static void drawVerticalLine(int x, int y, int length, int color, int alpha) {
		if(alpha == 0)
			return;
		if(x < clipStartX || x >= clipEndX)
			return;
		if(y < clipStartY) {
			length -= clipStartY - y;
			y = clipStartY;
		}
		if(y + length > clipEndY) {
			length = clipEndY - y;
		}
		final int j1 = 256 - alpha;
		final int k1 = (color >> 16 & 0xff) * alpha;
		final int l1 = (color >> 8 & 0xff) * alpha;
		final int i2 = (color & 0xff) * alpha;
		int i3 = x + y * canvasWidth;
		for(int j3 = 0; j3 < length; j3++) {
			final int j2 = (canvasRaster[i3] >> 16 & 0xff) * j1;
			final int k2 = (canvasRaster[i3] >> 8 & 0xff) * j1;
			final int l2 = (canvasRaster[i3] & 0xff) * j1;
			final int k3 = (k1 + j2 >> 8 << 16) + (l1 + k2 >> 8 << 8) + (i2 + l2 >> 8);
			canvasRaster[i3] = k3;
			i3 += canvasWidth;
		}
	}

	/**
	 * Draws a filled rectangle.
	 */
	public static void fillRectangle(int x, int y, int width, int height, int color) {
		if(x < clipStartX) {
			width -= clipStartX - x;
			x = clipStartX;
		}
		if(y < clipStartY) {
			height -= clipStartY - y;
			y = clipStartY;
		}
		if(x + width > clipEndX) {
			width = clipEndX - x;
		}
		if(y + height > clipEndY) {
			height = clipEndY - y;
		}
		final int k1 = canvasWidth - width;
		int l1 = x + y * canvasWidth;
		for(int i2 = -height; i2 < 0; i2++) {
			for(int j2 = -width; j2 < 0; j2++) {
				canvasRaster[l1++] = color;
			}
			l1 += k1;
		}

	}

	/**
	 * Draws a filled rectangle with an alpha value support.
	 */
	public static void fillRectangle(int x, int y, int width, int height, int color, int alpha) {
		if(alpha == 0)
			return;
		if(x < clipStartX) {
			width -= clipStartX - x;
			x = clipStartX;
		}
		if(y < clipStartY) {
			height -= clipStartY - y;
			y = clipStartY;
		}
		if(x + width > clipEndX) {
			width = clipEndX - x;
		}
		if(y + height > clipEndY) {
			height = clipEndY - y;
		}
		final int l1 = 256 - alpha;
		final int i2 = (color >> 16 & 0xff) * alpha;
		final int j2 = (color >> 8 & 0xff) * alpha;
		final int k2 = (color & 0xff) * alpha;
		final int k3 = canvasWidth - width;
		int l3 = x + y * canvasWidth;
		for(int i4 = 0; i4 < height; i4++) {
			for(int j4 = -width; j4 < 0; j4++) {
				final int l2 = (canvasRaster[l3] >> 16 & 0xff) * l1;
				final int i3 = (canvasRaster[l3] >> 8 & 0xff) * l1;
				final int j3 = (canvasRaster[l3] & 0xff) * l1;
				final int k4 = (i2 + l2 >> 8 << 16) + (j2 + i3 >> 8 << 8) + (k2 + j3 >> 8);
				canvasRaster[l3++] = k4;
			}
			l3 += k3;
		}
	}

	public static void fillCircle(int x, int y, int radius, int color) {
		int y1 = y - radius;
		if(y1 < 0) {
			y1 = 0;
		}
		int y2 = y + radius;
		if(y2 >= canvasHeight) {
			y2 = canvasHeight - 1;
		}
		for(int iy = y1; iy <= y2; iy++) {
			int dy = iy - y;
			int dist = (int) Math.sqrt(radius * radius - dy * dy);
			int x1 = x - dist;
			if(x1 < 0) {
				x1 = 0;
			}
			int x2 = x + dist;
			if(x2 >= canvasWidth) {
				x2 = canvasWidth - 1;
			}
			int pos = x1 + iy * canvasWidth;
			for(int ix = x1; ix <= x2; ix++) {
				canvasRaster[pos++] = color;
			}
		}
	}

	public static void fillCircle(int x, int y, int radius, int color, int alpha) {
		if(alpha == 0)
			return;
		int a2 = 256 - alpha;
		int r1 = (color >> 16 & 0xff) * alpha;
		int g1 = (color >> 8 & 0xff) * alpha;
		int b1 = (color & 0xff) * alpha;
		int y1 = y - radius;
		if(y1 < 0) {
			y1 = 0;
		}
		int y2 = y + radius;
		if(y2 >= canvasHeight) {
			y2 = canvasHeight - 1;
		}
		for(int iy = y1; iy <= y2; iy++) {
			int dy = iy - y;
			int dist = (int) Math.sqrt(radius * radius - dy * dy);
			int x1 = x - dist;
			if(x1 < 0) {
				x1 = 0;
			}
			int x2 = x + dist;
			if(x2 >= canvasWidth) {
				x2 = canvasWidth - 1;
			}
			int pos = x1 + iy * canvasWidth;
			for(int ix = x1; ix <= x2; ix++) {
				int r2 = (canvasRaster[pos] >> 16 & 0xff) * a2;
				int g2 = (canvasRaster[pos] >> 8 & 0xff) * a2;
				int b2 = (canvasRaster[pos] & 0xff) * a2;
				canvasRaster[pos++] = ((r1 + r2 >> 8) << 16) + ((g1 + g2 >> 8) << 8) + (b1 + b2 >> 8);
			}
		}
	}

	public static void fillRoundedRectangle(int x, int y, int width, int height, int radius, int color) {
		if(x >= clipEndX || y >= clipEndY || x + width < clipStartX || y + height < clipStartY) {
			return;
		}
		if(width == height) {
			if(radius > width >> 1) {
				fillCircle(x + radius, y + radius, radius, color);
				return;
			}
		} else if(width < height) {
			if(radius > width >> 1) {
				radius = width >> 1;
			}
		} else {
			if(radius > height >> 1) {
				radius = height >> 1;
			}
		}
		for(int i = 0; i < radius; i++) {
			int dist = (int) Math.sqrt(radius * radius - i * i);
			if(y + (radius - i) - 1 >= clipStartY) {
				for(int n = dist; n > 0; n--) {
					if(x + radius - n >= clipStartX) {
						canvasRaster[(x + radius - n) + (y + (radius - i) - 1) * canvasWidth] = color;
					}
					if(x + width - (radius - n) - 1 < clipEndX) {
						canvasRaster[(x + width - (radius - n) - 1) + (y + (radius - i) - 1) * canvasWidth] = color;
					}
				}
			}
			if(y + height - (radius - i) < clipEndY) {
				for(int n = dist; n > 0; n--) {
					if(x + (radius - n) >= clipStartX) {
						canvasRaster[(x + (radius - n)) + (y + height - (radius - i)) * canvasWidth] = color;
					}
					if(x + width - (radius - n) - 1 < clipEndX) {
						canvasRaster[(x + width - (radius - n) - 1) + (y + height - (radius - i)) * canvasWidth] = color;
					}
				}
			}
		}
		Rasterizer2D.fillRectangle(x + radius, y, width - (radius << 1), radius, color);
		Rasterizer2D.fillRectangle(x, y + radius, radius, height - (radius << 1), color);
		Rasterizer2D.fillRectangle(x + radius, y + radius, width - (radius << 1), height - (radius << 1), color);
		Rasterizer2D.fillRectangle(x + width - radius, y + radius, radius, height - (radius << 1), color);
		Rasterizer2D.fillRectangle(x + radius, y + height - radius, width - (radius << 1), radius, color);
	}

	public static void fillRoundedRectangle(int x, int y, int width, int height, int radius, int color, int alpha) {
		if(alpha == 0)
			return;
		if(x >= clipEndX || y >= clipEndY || x + width < clipStartX || y + height < clipStartY)
			return;
		if(width == height) {
			if(radius > width >> 1) {
				fillCircle(x + radius, y + radius, radius, color);
				return;
			}
		} else if(width < height) {
			if(radius > width >> 1) {
				radius = width >> 1;
			}
		} else {
			if(radius > height >> 1) {
				radius = height >> 1;
			}
		}
		int a2 = 256 - alpha;
		int r1 = (color >> 16 & 0xff) * alpha;
		int g1 = (color >> 8 & 0xff) * alpha;
		int b1 = (color & 0xff) * alpha;
		int pos;
		for(int i = 0; i < radius; i++) {
			int dist = (int) Math.sqrt(radius * radius - i * i);
			if(y + (radius - i) - 1 >= clipStartY) {
				for(int n = dist; n > 0; n--) {
					if(x + radius - n >= clipStartX) {
						pos = (x + radius - n) + (y + (radius - i) - 1) * canvasWidth;
						int r2 = (canvasRaster[pos] >> 16 & 0xff) * a2;
						int g2 = (canvasRaster[pos] >> 8 & 0xff) * a2;
						int b2 = (canvasRaster[pos] & 0xff) * a2;
						canvasRaster[pos] = ((r1 + r2 >> 8) << 16) + ((g1 + g2 >> 8) << 8) + (b1 + b2 >> 8);
					}
					if(x + width - (radius - n) - 1 < clipEndX) {
						pos = (x + width - (radius - n) - 1) + (y + (radius - i) - 1) * canvasWidth;
						int r2 = (canvasRaster[pos] >> 16 & 0xff) * a2;
						int g2 = (canvasRaster[pos] >> 8 & 0xff) * a2;
						int b2 = (canvasRaster[pos] & 0xff) * a2;
						canvasRaster[pos] = ((r1 + r2 >> 8) << 16) + ((g1 + g2 >> 8) << 8) + (b1 + b2 >> 8);
					}
				}
			}
			if(y + height - (radius - i) < clipEndY) {
				for(int n = dist; n > 0; n--) {
					if(x + (radius - n) >= clipStartX) {
						pos = (x + (radius - n)) + (y + height - (radius - i)) * canvasWidth;
						int r2 = (canvasRaster[pos] >> 16 & 0xff) * a2;
						int g2 = (canvasRaster[pos] >> 8 & 0xff) * a2;
						int b2 = (canvasRaster[pos] & 0xff) * a2;
						canvasRaster[pos] = ((r1 + r2 >> 8) << 16) + ((g1 + g2 >> 8) << 8) + (b1 + b2 >> 8);
					}
					if(x + width - (radius - n) - 1 < clipEndX) {
						pos = (x + width - (radius - n) - 1) + (y + height - (radius - i)) * canvasWidth;
						int r2 = (canvasRaster[pos] >> 16 & 0xff) * a2;
						int g2 = (canvasRaster[pos] >> 8 & 0xff) * a2;
						int b2 = (canvasRaster[pos] & 0xff) * a2;
						canvasRaster[pos] = ((r1 + r2 >> 8) << 16) + ((g1 + g2 >> 8) << 8) + (b1 + b2 >> 8);
					}
				}
			}
		}
		Rasterizer2D.fillRectangle(x + radius, y, width - (radius << 1), radius, color, alpha);
		Rasterizer2D.fillRectangle(x, y + radius, radius, height - (radius << 1), color, alpha);
		Rasterizer2D.fillRectangle(x + radius, y + radius, width - (radius << 1), height - (radius << 1), color, alpha);
		Rasterizer2D.fillRectangle(x + width - radius, y + radius, radius, height - (radius << 1), color, alpha);
		Rasterizer2D.fillRectangle(x + radius, y + height - radius, width - (radius << 1), radius, color, alpha);
	}

	/**
	 * Initializes the maximum size to the drawing area.
	 */
	public static void setCanvas(int[] raster, int height, int width) {
		canvasRaster = raster;
		canvasWidth = width;
		canvasHeight = height;
		setClip(0, 0, width, height);
	}

	/**
	 * Sets the clipping to its defaults.
	 */
	public static void removeClip() {
		setClip(0, 0, canvasWidth, canvasHeight);
	}

	/**
	 * Sets clipping by a rectangle.
	 */
	public static void setClip(int x1, int y1, int x2, int y2) {
		if(x1 < 0) {
			x1 = 0;
		}
		if(y1 < 0) {
			y1 = 0;
		}
		if(x2 > canvasWidth) {
			x2 = canvasWidth;
		}
		if(y2 > canvasHeight) {
			y2 = canvasHeight;
		}
		clipStartX = x1;
		clipStartY = y1;
		clipEndX = x2;
		clipEndY = y2;
		clipCenterX = clipEndX / 2;
		clipCenterY = clipEndY / 2;
	}

	/**
	 * Sets clipping by a rectangle which is clipped by the old clipping.
	 */
	public static void setInheritClip(int x1, int y1, int x2, int y2) {
		if(clipStartX < x1) {
			clipStartX = x1;
		}
		if(clipStartY < y1) {
			clipStartY = y1;
		}
		if(clipEndX > x2) {
			clipEndX = x2;
		}
		if(clipEndY > y2) {
			clipEndY = y2;
		}
	}

	/**
	 * Gets clip bounds as an array.
	 */
	public static int[] getClip() {
		return new int[]{clipStartX, clipStartY, clipEndX, clipEndY};
	}

	/**
	 * Sets clip by an array.
	 */
	public static void setClip(int[] bounds) {
		clipStartX = bounds[0];
		clipStartY = bounds[1];
		clipEndX = bounds[2];
		clipEndY = bounds[3];
	}

	/**
	 * Draws a vertical gradient fading from <code>topColor</code> to <code>bottomColor</code>.
	 */
	public static void drawVerticalGradient(int x, int y, int width, int height, int topColor, int bottomColor) {
		int i_75_ = 0;
		int i_76_ = 65536 / height;
		if(x < clipStartX) {
			width -= clipStartX - x;
			x = clipStartX;
		}
		if(y < clipStartY) {
			i_75_ += (clipStartY - y) * i_76_;
			height -= clipStartY - y;
			y = clipStartY;
		}
		if(x + width > clipEndX) {
			width = clipEndX - x;
		}
		if(y + height > clipEndY) {
			height = clipEndY - y;
		}
		int offset = canvasWidth - width;
		int pixel = x + y * canvasWidth;
		for(int i_79_ = -height; i_79_ < 0; i_79_++) {
			int i_80_ = 65536 - i_75_ >> 8;
			int i_81_ = i_75_ >> 8;
			int color = ((topColor & 0xff00ff) * i_80_ + (bottomColor & 0xff00ff) * i_81_ & ~0xff00ff) + ((topColor & 0xff00) * i_80_ + (bottomColor & 0xff00) * i_81_ & 0xff0000) >>> 8;
			for(int i_83_ = -width; i_83_ < 0; i_83_++) {
				canvasRaster[pixel++] = color;
			}
			pixel += offset;
			i_75_ += i_76_;
		}
	}

	/**
	 * Draws a horizontal gradient fading from <code>leftColor</code> to <code>rightColor</code>.
	 */
	public static void drawHorizontalGradient(int x, int y, int width, int height, int leftColor, int rightColor) {
		int i_75_ = 0;
		int i_76_ = 65536 / width;
		if(x < clipStartX) {
			i_75_ += (clipStartX - x) * i_76_;
			width -= clipStartX - x;
			x = clipStartX;
		}
		if(y < clipStartY) {
			height -= clipStartY - y;
			y = clipStartY;
		}
		if(x + width > clipEndX) {
			width = clipEndX - x;
		}
		if(y + height > clipEndY) {
			height = clipEndY - y;
		}
		int index = x + y * canvasWidth;
		for(int i_79_ = -width; i_79_ < 0; i_79_++) {
			int i_80_ = 65536 - i_75_ >> 8;
			int i_81_ = i_75_ >> 8;
			int color = ((leftColor & 0xff00ff) * i_80_ + (rightColor & 0xff00ff) * i_81_ & ~0xff00ff) + ((leftColor & 0xff00) * i_80_ + (rightColor & 0xff00) * i_81_ & 0xff0000) >>> 8;
			for(int line = 0; line < height; line++) {
				canvasRaster[index + line * canvasWidth] = color;
			}
			index++;
			i_75_ += i_76_;
		}
	}

	/**
	 * Fills a triangle with given color.
	 * NOTE: Can't be used when {@link Rasterizer3D}'s viewport is not set with the dimensions of canvas.
	 */
	public static void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int color) {
		Rasterizer3D.clippedScan = x1 < 0 || x2 < 0 || x3 < 0 || x1 > Rasterizer3D.viewport.width ||
				x2 > Rasterizer3D.viewport.width || x3 > Rasterizer3D.viewport.width;
		Rasterizer3D.drawFlatTriangle(y1, y2, y3, x1, x2, x3, 0, 0, 0, color);
	}

	/**
	 * Draws a diagonal line from (x1, y1) to (x2, y2) with the marker of the given width and color.
	 * NOTE: Can't be used when {@link Rasterizer3D}'s viewport is not set with the dimensions of canvas.
	 */
	public static void drawBoldDiagonalLine(int x1, int y1, int x2, int y2, int color, int width) {
		int dx = x2 - x1;
		int dy = y2 - y1;
		int dxabs = dx >= 0 ? dx : -dx;
		int dyabs = dy >= 0 ? dy : -dy;
		int dmax = dxabs;
		if(dmax < dyabs) {
			dmax = dyabs;
		}
		if(dmax != 0) {
			int xoff = (dx << 16) / dmax;
			int yoff = (dy << 16) / dmax;
			if(yoff <= xoff) {
				xoff = -xoff;
			} else {
				yoff = -yoff;
			}
			int xAdd = width * yoff >> 17;
			int xRem = width * yoff + 1 >> 17;
			int yAdd = width * xoff >> 17;
			int yRem = width * xoff + 1 >> 17;
			x1 -= Rasterizer3D.viewport.getX();
			y1 -= Rasterizer3D.viewport.getY();
			int px1 = x1 + xAdd;
			int px2 = x1 - xRem;
			int px3 = x1 + dx - xRem;
			int px4 = x1 + dx + xAdd;
			int py1 = y1 + yAdd;
			int py2 = y1 - yRem;
			int py3 = y1 + dy - yRem;
			int py4 = y1 + dy + yAdd;
			Rasterizer3D.clippedScan = px1 < 0 || px2 < 0 || px3 < 0 || px1 > Rasterizer3D.viewport.width ||
					px2 > Rasterizer3D.viewport.width || px3 > Rasterizer3D.viewport.width;
			Rasterizer3D.drawFlatTriangle(py1, py2, py3, px1, px2, px3, 0, 0, 0, color);
			Rasterizer3D.clippedScan = px1 < 0 || px3 < 0 || px4 < 0 || px1 > Rasterizer3D.viewport.width ||
					px3 > Rasterizer3D.viewport.width || px4 > Rasterizer3D.viewport.width;
			Rasterizer3D.drawFlatTriangle(py1, py3, py4, px1, px3, px4, 0, 0, 0, color);
		}
	}

	/**
	 * Filters grayscale effect inside the given bounds.
	 * @param amount Determines the amount of the effect, 0 = no effect, 1 = full grayscale
	 */
	public static void filterGrayscale(int x, int y, int width, int height, double amount) {
		if(amount <= 0) {
			return;
		}
		if(x < clipStartX) {
			width -= clipStartX - x;
			x = clipStartX;
		}
		if(y < clipStartY) {
			height -= clipStartY - y;
			y = clipStartY;
		}
		if(x + width > clipEndX) {
			width = clipEndX - x;
		}
		if(y + height > clipEndY) {
			height = clipEndY - y;
		}
		if(width <= 0 || height <= 0) {
			return;
		}
		int pos = x + y * canvasWidth;
		int offset = canvasWidth - width;
		if(amount >= 1) {
			while(height-- > 0) {
				for(int i = 0; i < width; i++) {
					final int red = canvasRaster[pos] >> 16 & 0xff;
					final int green = canvasRaster[pos] >> 8 & 0xff;
					final int blue = canvasRaster[pos] & 0xff;
					final int lightness = (red + green + blue) / 3;
					final int color = lightness << 16 | lightness << 8 | lightness;
					canvasRaster[pos++] = color;
				}
				pos += offset;
			}
		} else {
			final double divider = 2 * amount + 1;
			while(height-- > 0) {
				for(int i = 0; i < width; i++) {
					int red = canvasRaster[pos] >> 16 & 0xff;
					int green = canvasRaster[pos] >> 8 & 0xff;
					int blue = canvasRaster[pos] & 0xff;
					final int red2 = (int) (red * amount);
					final int green2 = (int) (green * amount);
					final int blue2 = (int) (blue * amount);
					red = (int) ((red + green2 + blue2) / divider);
					green = (int) ((red2 + green + blue2) / divider);
					blue = (int) ((red2 + green2 + blue) / divider);
					final int color = red << 16 | green << 8 | blue;
					canvasRaster[pos++] = color;
				}
				pos += offset;
			}
		}
	}
}
