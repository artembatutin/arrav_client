package net.arrav.graphic;

import net.arrav.graphic.img.BitmapImage;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.Raster;

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

	public static void draw_arc(int x, int y, int width, int height, int stroke, int start, int sweep, int color,
								int alpha, int closure, boolean fill) {
		Graphics2D graphics = BitmapImage.createGraphics(canvasRaster, canvasWidth, canvasHeight);
		graphics.setColor(new Color((color >> 16 & 0xff), (color >> 8 & 0xff), (color & 0xff),
				((alpha >= 256 || alpha < 0) ? 255 : alpha)));

		RenderingHints render = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		render.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);// fix the 'jittering'

		graphics.setRenderingHints(render);
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		if (!fill) {
			graphics.setStroke(new BasicStroke((Math.max(stroke, 1))));
		}
		// Closure types - OPEN(0), CHORD(1), PIE(2)
		Arc2D.Double arc = new Arc2D.Double(x + stroke, y + stroke, width, height, start, sweep, closure);
		if (fill) {
			graphics.fill(arc);
		} else {
			graphics.draw(arc);
		}
	}

	public static void drawInnerShadow(int x, int y, int width, int height, int color, int alpha, int weight) {
		int fade_rate = alpha / weight;
		for (int index = 0; index < weight; index++) {
			drawHorizontalLine(x + index, y + index, width - (index * 2), color, alpha - (index * fade_rate));
			drawHorizontalLine(x + index, y + height - index, width - (index * 2), color, alpha - (index * fade_rate));
			drawVerticalLine(x + index, y + index + 1, height - (index * 2) - 1, color, alpha - (index * fade_rate));
			drawVerticalLine(x + width - index - 1, y + index + 1, height - (index * 2) - 1, color, alpha - (index * fade_rate));
		}
	}


	public static void drawDropShadow(int x, int y, int width, int height, int color, int alpha, int weight) {
		int fade_rate = alpha / weight;
		for (int index = 0; index < weight; index++) {
			drawHorizontalLine(x - index, y - index, width + (index * 2), color, alpha - (index * fade_rate));
			drawHorizontalLine(x - index, y + height + index, width + (index * 2), color, alpha - (index * fade_rate));
			drawVerticalLine(x - index, y - index + 1, height + (index * 2) - 1, color, alpha - (index * fade_rate));
			drawVerticalLine(x + width + index - 1, y - index + 1, height + (index * 2) - 1, color, alpha - (index * fade_rate));
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



	public static void fillGradient(int drawX, int drawY, int width, int height, int startColor, int endColor, int alpha) {
		int offsetY = 0;
		int step = 0x10000 / height;

		if (drawX < clipStartX) {
			width -= clipStartX - drawX;
			drawX = clipStartX;
		}

		if (drawY < clipStartY) {
			offsetY += (clipStartY - drawY) * step;
			height -= clipStartY - drawY;
			drawY = clipStartY;
		}

		if (drawX + width > clipEndX)
			width = clipEndX - drawX;

		if (drawY + height > clipEndY)
			height = clipEndY - drawY;

		int dx = canvasWidth - width;
		int reverseAlpha = 256 - alpha;
		int pixel = drawX + drawY * canvasWidth;
		for (int x = -height; x < 0; x++) {
			int start = 0x10000 - offsetY >> 8;
			int end = offsetY >> 8;
			int gradient = ((startColor & 0xff00ff) * start + (endColor & 0xff00ff) * end & 0xff00ff00) + ((startColor & 0xff00) * start + (endColor & 0xff00) * end & 0xff0000) >>> 8;
			int colour = ((gradient & 0xff00ff) * alpha >> 8 & 0xff00ff) + ((gradient & 0xff00) * alpha >> 8 & 0xff00);
			for (int y = -width; y < 0; y++) {
				int pixels1 = canvasRaster[pixel];
				pixels1 = ((pixels1 & 0xff00ff) * reverseAlpha >> 8 & 0xff00ff) + ((pixels1 & 0xff00) * reverseAlpha >> 8 & 0xff00);
				canvasRaster[pixel++] = colour + pixels1;
			}
			pixel += dx;
			offsetY += step;
		}
	}

	public static void drawAlphaGradient(int x, int y, int gradientWidth, int gradientHeight, int startColor,
										 int endColor, int alpha) {
		int k1 = 0;
		int l1 = 0x10000 / gradientHeight;
		if (x < clipStartX) {
			gradientWidth -= clipStartX - x;
			x = clipStartX;
		}
		if (y < clipStartY) {
			k1 += (clipStartY - y) * l1;
			gradientHeight -= clipStartY - y;
			y = clipStartY;
		}
		if (x + gradientWidth > clipEndX)
			gradientWidth = clipEndX - x;
		if (y + gradientHeight > clipEndY)
			gradientHeight = clipEndY - y;
		int i2 = canvasWidth - gradientWidth;
		int result_alpha = 256 - alpha;
		int total_pixels = x + y * canvasWidth;
		for (int k2 = -gradientHeight; k2 < 0; k2++) {
			int gradient1 = 0x10000 - k1 >> 8;
			int gradient2 = k1 >> 8;
			int gradient_color = ((startColor & 0xff00ff) * gradient1 + (endColor & 0xff00ff) * gradient2 & 0xff00ff00)
					+ ((startColor & 0xff00) * gradient1 + (endColor & 0xff00) * gradient2 & 0xff0000) >>> 8;
			int color = ((gradient_color & 0xff00ff) * alpha >> 8 & 0xff00ff)
					+ ((gradient_color & 0xff00) * alpha >> 8 & 0xff00);
			for (int k3 = -gradientWidth; k3 < 0; k3++) {
				int colored_pixel = canvasRaster[total_pixels];
				colored_pixel = ((colored_pixel & 0xff00ff) * result_alpha >> 8 & 0xff00ff)
						+ ((colored_pixel & 0xff00) * result_alpha >> 8 & 0xff00);
				canvasRaster[total_pixels++] = color + colored_pixel;
			}
			total_pixels += i2;
			k1 += l1;
		}
	}

	public static void drawOutlinedRectangle(int x, int y, int w, int h, int outline, int fill, int alpha) {
		fillRectangle(x,y,w,h,fill, alpha);
		drawRectangle(x,y,w,h,outline);
	}

	public static void drawOutlinedGradientRectangle(int x, int y, int w, int h, int outline, int start, int finish, int alpha) {
		fillGradient(x,y,w,h,start,finish,alpha);
		drawRectangle(x,y,w,h,outline);
	}
	public static void drawOutlinedRoundedGradientRectangle(int x, int y, int w, int h, int outline, int start, int finish, int alpha) {
		fillRoundedGradientRectangle(x,y,w,h,start,finish,alpha,true,false);
		/*drawRoundedRectangle(x,y,w,h,outline,255,false,false);
        drawRoundedRectangle(x-1,y-1,w+2,h+2,outline,255, false,false);*/
		drawRoundedGlowBorder(x,y,w,h,outline,255,3,true);

	}

	public static void drawGlowBorder(int x, int y, int w, int h, int color, int maxOpacity, int thickness, boolean inset) {
		for(int i = 0; i < thickness; i++) {
			method338(y+i, h-i*2, (int)((float)maxOpacity/thickness*(!inset ? i : thickness-i)), color, w-i*2, x+i);
		}
	}
	public static void drawRoundedGlowBorder(int x, int y, int w, int h, int color, int maxOpacity, int thickness, boolean inset) {
		for(int i = 0; i < thickness; i++) {
			drawRoundedRectangle(x+i, y+i, w-i*2, h-i*2, color, (int)((float)maxOpacity/thickness*(!inset ? i : thickness-i)), false, false);
		}
	}


	public static void fillRoundedGradientRectangle(int x, int y, int width, int height, int color, int color2, int alpha, boolean filled, boolean shadowed) {
		if (width <= 0) {
			return;
		}
		if (shadowed) {
			drawRoundedRectangle(x + 1, y + 1, width, height, 0, alpha, filled, false);
		}
		if (alpha == -1) {
			if (filled) {

				drawHorizontalLine(x + 2, y + 1, width - 4, color);
				drawHorizontalLine(x + 2, y + height - 2, width - 4, color2);
				fillGradient(x + 1, y + 2, width - 2, height - 4, color, color2, alpha);
			}
			// drawHorizontal(worldX + 2, worldY, width - 4, color);
			// drawHorizontal(worldX + 2, worldY + height - 1, width - 4, color2);

			drawVerticalLine(y+2, color, height-4, x);

			drawVerticalLine(y+ width -1, color, height -4, x+width -1);

			drawHorizontalGradient(x, y+2, 1,height-4, color, color2);
			drawHorizontalGradient(x+width-1, y+width-1, 1,height-4, color, color2);



			fillRectangle(x + 1, y + 1, 1, 1, color);
			fillRectangle(x + width - 2, y + 1, 1, 1, color);
			fillRectangle(x + width - 2, y + height - 2, 1, 1, color);
			fillRectangle(x + 1, y + height - 2, 1, 1, color);
		} else {
			if (filled) {
				drawHorizontalLine(x + 2, y + 1, width - 4, color, alpha);
				drawHorizontalLine(x + 2, y + height - 2, width - 4, color2, alpha);
				fillGradient(x + 1, y + 2, width - 2, height - 4, color, color2, alpha);
			}
			drawHorizontalLine(x + 2, y, width - 4, color, alpha);
			drawHorizontalLine(x + 2, y + height - 1, width - 4, color2, alpha);

			drawVerticalLine(color, x, alpha, y+2, height -4);
			drawVerticalLine(color, x + width -1, alpha, y+2, height -4);

			// Raster.drawHorizontalGradient(worldX, worldY+2, 1,height-4, color, color2);
			//  Raster.drawHorizontalGradient(worldX+width-1, worldY+2, 1,height-4, color, color2);


			fillRectangle(x + 1, y + 1, 1, 1, color, alpha);
			fillRectangle(x + width - 2, y + 1, 1, 1, color, alpha);
			fillRectangle(x + width - 2, y + height - 2, 1, 1, color, alpha);
			fillRectangle(x + 1, y + height - 2, 1, 1, color, alpha);
		}
	}

	public static void drawOutlinedRoundedRectangle(int x, int y, int w, int h, int outline, int fill, int alpha) {
		drawRoundedRectangle(x,y,w,h,fill,alpha,true,false);
		drawRoundedRectangle(x,y,w,h,outline,255, false,false);
	}

	public static void drawPixels(int i, int j, int k, int l, int i1) {
		if (k < clipStartX) {
			i1 -= clipStartX - k;
			k = clipStartX;
		}
		if (j < clipStartY) {
			i -= clipStartY - j;
			j = clipStartY;
		}
		if (k + i1 > clipEndX)
			i1 = clipEndX - k;
		if (j + i > clipEndY)
			i = clipEndY - j;
		int k1 = canvasWidth - i1;
		int l1 = k + j * canvasWidth;
		for (int i2 = -i; i2 < 0; i2++) {
			for (int j2 = -i1; j2 < 0; j2++)
				canvasRaster[l1++] = l;

			l1 += k1;
		}

	}


	public static void drawRoundedRectangle(int x, int y, int width, int height, int color, int alpha, boolean filled,
											boolean shadowed) {
		if (shadowed) {
			drawRoundedRectangle(x + 1, y + 1, width, height, 0, alpha, filled, false);
		}
		if (alpha == -1) {
			if (filled) {
				method339(y + 1, color, width - 4, x + 2);
				method339(y + height - 2, color, width - 4, x + 2);
				drawPixels(height - 4, y + 2, x + 1, color, width - 2);
			}
			method339(y, color, width - 4, x + 2);
			method339(y + height - 1, color, width - 4, x + 2);
			method341(y + 2, color, height - 4, x);
			method341(y + 2, color, height - 4, x + width - 1);
			drawPixels(1, y + 1, x + 1, color, 1);
			drawPixels(1, y + 1, x + width - 2, color, 1);
			drawPixels(1, y + height - 2, x + width - 2, color, 1);
			drawPixels(1, y + height - 2, x + 1, color, 1);
		} else if (alpha != -1) {
			if (filled) {
				method340(color, width - 4, y + 1, alpha, x + 2);
				method340(color, width - 4, y + height - 2, alpha, x + 2);
				method335(color, y + 2, width - 2, height - 4, alpha, x + 1);
			}
			method340(color, width - 4, y, alpha, x + 2);
			method340(color, width - 4, y + height - 1, alpha, x + 2);
			method342(color, x, alpha, y + 2, height - 4);
			method342(color, x + width - 1, alpha, y + 2, height - 4);
			method335(color, y + 1, 1, 1, alpha, x + 1);
			method335(color, y + 1, 1, 1, alpha, x + width - 2);
			method335(color, y + height - 2, 1, 1, alpha, x + 1);
			method335(color, y + height - 2, 1, 1, alpha, x + width - 2);
		}
	}

	public static void method335(int i, int j, int k, int l, int i1, int k1) {
		if (k1 < clipStartX) {
			k -= clipStartX - k1;
			k1 = clipStartX;
		}
		if (j < clipStartY) {
			l -= clipStartY - j;
			j = clipStartY;
		}
		if (k1 + k > clipEndX)
			k = clipEndX - k1;
		if (j + l > clipEndY)
			l = clipEndY - j;
		int l1 = 256 - i1;
		int i2 = (i >> 16 & 0xff) * i1;
		int j2 = (i >> 8 & 0xff) * i1;
		int k2 = (i & 0xff) * i1;
		int k3 = canvasWidth - k;
		int l3 = k1 + j * canvasWidth;
		for (int i4 = 0; i4 < l; i4++) {
			for (int j4 = -k; j4 < 0; j4++) {
				int l2 = (canvasRaster[l3] >> 16 & 0xff) * l1;
				int i3 = (canvasRaster[l3] >> 8 & 0xff) * l1;
				int j3 = (canvasRaster[l3] & 0xff) * l1;
				int k4 = ((i2 + l2 >> 8) << 16) + ((j2 + i3 >> 8) << 8) + (k2 + j3 >> 8);
				canvasRaster[l3++] = k4;
			}

			l3 += k3;
		}
	}

	public static void method336(int i, int j, int k, int l, int i1) {
		if (k < clipStartX) {
			i1 -= clipStartX - k;
			k = clipStartX;
		}
		if (j < clipStartY) {
			i -= clipStartY - j;
			j = clipStartY;
		}
		if (k + i1 > clipEndX)
			i1 = clipEndX - k;
		if (j + i > clipEndY)
			i = clipEndY - j;
		int k1 = canvasWidth - i1;
		int l1 = k + j * canvasWidth;
		for (int i2 = -i; i2 < 0; i2++) {
			for (int j2 = -i1; j2 < 0; j2++)
				canvasRaster[l1++] = l;

			l1 += k1;
		}

	}

	public static void method338(int i, int j, int k, int l, int i1, int j1) {
		method340(l, i1, i, k, j1);
		method340(l, i1, (i + j) - 1, k, j1);
		if (j >= 3) {
			method342(l, j1, k, i + 1, j - 2);
			method342(l, (j1 + i1) - 1, k, i + 1, j - 2);
		}
	}

	public static void method339(int i, int j, int k, int l) {
		if (i < clipStartY || i >= clipEndY)
			return;
		if (l < clipStartX) {
			k -= clipStartX - l;
			l = clipStartX;
		}
		if (l + k > clipEndX)
			k = clipEndX - l;
		int i1 = l + i * canvasWidth;
		for (int j1 = 0; j1 < k; j1++)
			canvasRaster[i1 + j1] = j;

	}

	private static void method340(int i, int j, int k, int l, int i1) {
		if (k < clipStartY || k >= clipEndY)
			return;
		if (i1 < clipStartX) {
			j -= clipStartX - i1;
			i1 = clipStartX;
		}
		if (i1 + j > clipEndX)
			j = clipEndX - i1;
		int j1 = 256 - l;
		int k1 = (i >> 16 & 0xff) * l;
		int l1 = (i >> 8 & 0xff) * l;
		int i2 = (i & 0xff) * l;
		int i3 = i1 + k * canvasWidth;
		for (int j3 = 0; j3 < j; j3++) {
			int j2 = (canvasRaster[i3] >> 16 & 0xff) * j1;
			int k2 = (canvasRaster[i3] >> 8 & 0xff) * j1;
			int l2 = (canvasRaster[i3] & 0xff) * j1;
			int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
			canvasRaster[i3++] = k3;
		}

	}

	public static void method341(int i, int j, int k, int l) {
		if (l < clipStartX || l >= clipEndX)
			return;
		if (i < clipStartY) {
			k -= clipStartY - i;
			i = clipStartY;
		}
		if (i + k > clipEndY)
			k = clipEndY - i;
		int j1 = l + i * canvasWidth;
		for (int k1 = 0; k1 < k; k1++)
			canvasRaster[j1 + k1 * canvasWidth] = j;

	}

	private static void method342(int i, int j, int k, int l, int i1) {
		if (j < clipStartX || j >= clipEndX)
			return;
		if (l < clipStartY) {
			i1 -= clipStartY - l;
			l = clipStartY;
		}
		if (l + i1 > clipEndY)
			i1 = clipEndY - l;
		int j1 = 256 - k;
		int k1 = (i >> 16 & 0xff) * k;
		int l1 = (i >> 8 & 0xff) * k;
		int i2 = (i & 0xff) * k;
		int i3 = j + l * canvasWidth;
		for (int j3 = 0; j3 < i1; j3++) {
			int j2 = (canvasRaster[i3] >> 16 & 0xff) * j1;
			int k2 = (canvasRaster[i3] >> 8 & 0xff) * j1;
			int l2 = (canvasRaster[i3] & 0xff) * j1;
			int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
			canvasRaster[i3] = k3;
			i3 += canvasWidth;
		}
	}

	public static void drawBubble(int x, int y, int radius, int colour, int initialAlpha) {
		fillCircleAlpha(x, y, radius, colour, initialAlpha);
		fillCircleAlpha(x, y, radius + 2, colour, 8);
		fillCircleAlpha(x, y, radius + 4, colour, 6);
		fillCircleAlpha(x, y, radius + 6, colour, 4);
		fillCircleAlpha(x, y, radius + 8, colour, 2);
	}

	public static void fillCircleAlpha(int posX, int posY, int radius, int colour, int alpha) {
		int dest_intensity = 256 - alpha;
		int src_red = (colour >> 16 & 0xff) * alpha;
		int src_green = (colour >> 8 & 0xff) * alpha;
		int src_blue = (colour & 0xff) * alpha;
		int i3 = posY - radius;
		if (i3 < 0)
			i3 = 0;
		int j3 = posY + radius;
		if (j3 >= canvasWidth)
			j3 = canvasHeight - 1;
		for (int y = i3; y <= j3; y++) {
			int l3 = y - posY;
			int i4 = (int) Math.sqrt(radius * radius - l3 * l3);
			int x = posX - i4;
			if (x < 0)
				x = 0;
			int k4 = posX + i4;
			if (k4 >= canvasWidth)
				k4 = canvasWidth - 1;
			int pixel_offset = x + y * canvasWidth;
			for (int i5 = x; i5 <= k4; i5++) {
				int dest_red = (canvasRaster[pixel_offset] >> 16 & 0xff) * dest_intensity;
				int dest_green = (canvasRaster[pixel_offset] >> 8 & 0xff) * dest_intensity;
				int dest_blue = (canvasRaster[pixel_offset] & 0xff) * dest_intensity;
				int result_rgb = ((src_red + dest_red >> 8) << 16) + ((src_green + dest_green >> 8) << 8)
						+ (src_blue + dest_blue >> 8);
				canvasRaster[pixel_offset++] = result_rgb;
			}
		}
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
