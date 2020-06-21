package net.arrav.graphic.img;

import net.arrav.Config;
import net.arrav.cache.CacheArchive;
import net.arrav.graphic.Rasterizer2D;
import net.arrav.net.SignLink;
import net.arrav.util.FileToolkit;
import net.arrav.util.io.Buffer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class BitmapImage extends Rasterizer2D {

	private static final ColorModel COLOR_MODEL = new DirectColorModel(32, 0xff0000, 0xff00, 0xff);

	public static Graphics2D createGraphics(int[] pixels, int width, int height) {
		return new java.awt.image.BufferedImage(COLOR_MODEL,
				java.awt.image.Raster.createWritableRaster(COLOR_MODEL.createCompatibleSampleModel(width, height),
						new java.awt.image.DataBufferInt(pixels, width * height), null),
				false, new java.util.Hashtable<Object, Object>()).createGraphics();
	}

	public boolean alpha;
	public int imageRaster[];
	public int imageWidth;
	public int imageHeight;
	public int xOffset;
	public int yOffset;
	public int imageOriginalWidth;
	public int imageOriginalHeight;
	private String location = SignLink.getCacheDir() + "images/";

	public BitmapImage(int width, int height, int offsetX, int offsetY, int[] pixels) {
		this.imageWidth = width;
		this.imageHeight = height;
		this.xOffset = offsetX;
		this.yOffset = offsetY;
		this.alpha = true;
		this.imageRaster = pixels;
		setTransparency(255, 0, 255);
	}
	public void dump(int newId) {
		BufferedImage img = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		int[] dest = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		for(int i = 0; i < imageRaster.length; i++) {
			if(imageRaster[i] != 0) {
				dest[i] = imageRaster[i];
			} else {
				dest[i] = 0xff00ff;
			}
		}
		File dir = new File("./imagedump/");
		if(!dir.exists()) {
			dir.mkdir();
		}
		try {
			ImageIO.write(img, "png", new File(dir, newId + ".png"));
			BufferedWriter writer2 = new BufferedWriter(new FileWriter(dir.getPath() + "/offsets.txt", true));
			writer2.write(newId + "-x" + xOffset + "-y" + yOffset);
			writer2.newLine();
			writer2.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public BitmapImage(byte data[]) {
		try {
			Buffer buf = new Buffer(data);
			imageWidth = buf.getUShort();
			imageHeight = buf.getUShort();
			imageOriginalWidth = imageWidth;
			imageOriginalHeight = imageHeight;
			xOffset = buf.getUShort();
			yOffset = buf.getUShort();
			imageRaster = new int[imageWidth * imageHeight];
			alpha = buf.getUByte() == 1;
			int pixelByteCount = buf.getInt();
			int pixelCount = 0;
			if(alpha) {
				for(int pixel = 13; pixel < pixelByteCount + 13; pixel += 4) {
					int argb = 0;
					argb += ((buf.getUByte() & 0xff) << 24); // alpha
					argb += (buf.getUByte() & 0xff); // blue
					argb += ((buf.getUByte() & 0xff) << 8); // green
					argb += ((buf.getUByte() & 0xff) << 16); // red
					imageRaster[pixelCount] = argb;
					pixelCount += 1;
				}
			} else {
				for(int pixel = 13; pixel < pixelByteCount + 13; pixel += 3) {
					int argb = 0;
					argb += -16777216; // 255 alpha
					argb += (buf.getUByte() & 0xff); // blue
					argb += ((buf.getUByte() & 0xff) << 8); // green
					argb += ((buf.getUByte() & 0xff) << 16); // red
					imageRaster[pixelCount] = argb;
					pixelCount += 1;
				}
			}
			setTransparency(255, 0, 255);
		} catch(Exception _ex) {
			_ex.printStackTrace();
			System.out.println("Error loading image.");
		}
	}

	public BitmapImage(byte abyte0[], Component component) {
		try {
			final Image image = Toolkit.getDefaultToolkit().createImage(abyte0);
			final MediaTracker mediatracker = new MediaTracker(component);
			mediatracker.addImage(image, 0);
			mediatracker.waitForAll();
			imageWidth = image.getWidth(component);
			imageHeight = image.getHeight(component);
			imageOriginalWidth = imageWidth;
			imageOriginalHeight = imageHeight;
			xOffset = 0;
			yOffset = 0;
			imageRaster = new int[imageWidth * imageHeight];
			final PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, imageWidth, imageHeight, imageRaster, 0, imageWidth);
			pixelgrabber.grabPixels();
			setTransparency(255, 0, 255);
		} catch(final Exception _ex) {
			System.out.println("Error converting jpg");
		}
	}

	public BitmapImage(int width, int height) {
		imageRaster = new int[width * height];
		imageWidth = imageOriginalWidth = width;
		imageHeight = imageOriginalHeight = height;
		xOffset = yOffset = 0;
	}

	public BitmapImage(CacheArchive archive, String s, int i) {
		final Buffer stream = new Buffer(archive.getFile(s + ".dat"));
		final Buffer stream_1 = new Buffer(archive.getFile("index.dat"));
		stream_1.pos = stream.getUShort();
		imageOriginalWidth = stream_1.getUShort();
		imageOriginalHeight = stream_1.getUShort();
		final int j = stream_1.getUByte();
		final int ai[] = new int[j];
		for(int k = 0; k < j - 1; k++) {
			ai[k + 1] = stream_1.getUMedium();
			if(ai[k + 1] == 0) {
				ai[k + 1] = 1;
			}
		}
		for(int l = 0; l < i; l++) {
			stream_1.pos += 2;
			stream.pos += stream_1.getUShort() * stream_1.getUShort();
			stream_1.pos++;
		}
		xOffset = stream_1.getUByte();
		yOffset = stream_1.getUByte();
		imageWidth = stream_1.getUShort();
		imageHeight = stream_1.getUShort();
		final int i1 = stream_1.getUByte();
		final int j1 = imageWidth * imageHeight;
		imageRaster = new int[j1];
		if(i1 == 0) {
			for(int k1 = 0; k1 < j1; k1++) {
				imageRaster[k1] = ai[stream.getUByte()];
			}
			setTransparency(255, 0, 255);
			return;
		}
		if(i1 == 1) {
			for(int l1 = 0; l1 < imageWidth; l1++) {
				for(int i2 = 0; i2 < imageHeight; i2++) {
					imageRaster[l1 + i2 * imageWidth] = ai[stream.getUByte()];
				}
			}
			setTransparency(255, 0, 255);
		}
	}

	public BitmapImage(String img) {
		try {
			Image image = Toolkit.getDefaultToolkit().getImage(location + img + ".png");
			final ImageIcon sprite = new ImageIcon(image);
			imageWidth = sprite.getIconWidth();
			imageHeight = sprite.getIconHeight();
			imageOriginalWidth = imageWidth;
			imageOriginalHeight = imageHeight;
			xOffset = 0;
			yOffset = 0;
			imageRaster = new int[imageWidth * imageHeight];
			final PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, imageWidth, imageHeight, imageRaster, 0, imageWidth);
			pixelgrabber.grabPixels();
			setTransparency(255, 0, 255);
		} catch(final Exception _ex) {
			_ex.printStackTrace();
		}
	}

	public BitmapImage(String img, int width, int height) {
		try {
			Image image = Toolkit.getDefaultToolkit().createImage(FileToolkit.readFile(img));
			imageWidth = width;
			imageHeight = height;
			imageOriginalWidth = imageWidth;
			imageOriginalHeight = imageHeight;
			xOffset = 0;
			yOffset = 0;
			imageRaster = new int[imageWidth * imageHeight];
			final PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, imageWidth, imageHeight, imageRaster, 0, imageWidth);
			pixelgrabber.grabPixels();
			setTransparency(255, 0, 255);
		} catch(final Exception _ex) {
			_ex.printStackTrace();
		}
	}

	public void drawAlphaImage(int x, int y) {
		drawImage(x, y, 256);
	}
	
	private void drawAlphaImage(int x, int y, int alpha) {
		x += xOffset;
		y += yOffset;
		int destOffset = x + y * Rasterizer2D.canvasWidth;
		int srcOffset = 0;
		int height = imageHeight;
		int width = imageWidth;
		int destStep = Rasterizer2D.canvasWidth - width;
		int srcStep = 0;
		if(y < Rasterizer2D.clipStartY) {
			final int trimHeight = Rasterizer2D.clipStartY - y;
			height -= trimHeight;
			y = Rasterizer2D.clipStartY;
			srcOffset += trimHeight * width;
			destOffset += trimHeight * Rasterizer2D.canvasWidth;
		}
		if(y + height > Rasterizer2D.clipEndY) {
			height -= y + height - Rasterizer2D.clipEndY;
		}
		if(x < Rasterizer2D.clipStartX) {
			final int trimLeft = Rasterizer2D.clipStartX - x;
			width -= trimLeft;
			x = Rasterizer2D.clipStartX;
			srcOffset += trimLeft;
			destOffset += trimLeft;
			srcStep += trimLeft;
			destStep += trimLeft;
		}
		if(x + width > Rasterizer2D.clipEndX) {
			final int trimRight = x + width - Rasterizer2D.clipEndX;
			width -= trimRight;
			srcStep += trimRight;
			destStep += trimRight;
		}
		if(!(width <= 0 || height <= 0)) {
			setAlphaPixels(width, height, Rasterizer2D.canvasRaster, imageRaster, alpha, destOffset, srcOffset, destStep, srcStep);
		}
	}

	public void drawImage(int x, int y) {
		if(alpha) {
			drawAlphaImage(x, y);
			return;
		}
		x += xOffset;
		y += yOffset;
		int l = x + y * Rasterizer2D.canvasWidth;
		int i1 = 0;
		int height = imageHeight;
		int width = imageWidth;
		int l1 = Rasterizer2D.canvasWidth - width;
		int i2 = 0;
		if(y < Rasterizer2D.clipStartY) {
			final int j2 = Rasterizer2D.clipStartY - y;
			height -= j2;
			y = Rasterizer2D.clipStartY;
			i1 += j2 * width;
			l += j2 * Rasterizer2D.canvasWidth;
		}
		if(y + height > Rasterizer2D.clipEndY) {
			height -= y + height - Rasterizer2D.clipEndY;
		}
		if(x < Rasterizer2D.clipStartX) {
			final int k2 = Rasterizer2D.clipStartX - x;
			width -= k2;
			x = Rasterizer2D.clipStartX;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if(x + width > Rasterizer2D.clipEndX) {
			final int l2 = x + width - Rasterizer2D.clipEndX;
			width -= l2;
			i2 += l2;
			l1 += l2;
		}
		if(!(width <= 0 || height <= 0)) {
			drawPixels(Rasterizer2D.canvasRaster, imageRaster, i1, l, width, height, l1, i2);
		}
	}

	public void drawOutline(int i, int k, int color) {
		int tempWidth = imageWidth + 2;
		int tempHeight = imageHeight + 2;
		int[] tempArray = new int[tempWidth * tempHeight];
		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				if (imageRaster[x + y * imageWidth] != 0)
					tempArray[(x + 1) + (y + 1) * tempWidth] = imageRaster[x + y * imageWidth];
			}
		}
		for (int x = 0; x < tempWidth; x++) {
			for (int y = 0; y < tempHeight; y++) {
				if (tempArray[(x) + (y) * tempWidth] == 0) {
					if (x < tempWidth - 1 && tempArray[(x + 1) + ((y) * tempWidth)] > 0 && tempArray[(x + 1) + ((y) * tempWidth)] != 0xffffff) {
						tempArray[(x) + (y) * tempWidth] = color;
					}
					if (x > 0 && tempArray[(x - 1) + ((y) * tempWidth)] > 0 && tempArray[(x - 1) + ((y) * tempWidth)] != 0xffffff) {
						tempArray[(x) + (y) * tempWidth] = color;
					}
					if (y < tempHeight - 1 && tempArray[(x) + ((y + 1) * tempWidth)] > 0 && tempArray[(x) + ((y + 1) * tempWidth)] != 0xffffff) {
						tempArray[(x) + (y) * tempWidth] = color;
					}
					if (y > 0 && tempArray[(x) + ((y - 1) * tempWidth)] > 0 && tempArray[(x) + ((y - 1) * tempWidth)] != 0xffffff) {
						tempArray[(x) + (y) * tempWidth] = color;
					}
				}
			}
		}
		i--;
		k--;
		i += xOffset;
		k += yOffset;
		int l = i + k * Rasterizer2D.canvasWidth;
		int i1 = 0;
		int j1 = tempHeight;
		int k1 = tempWidth;
		int l1 = Rasterizer2D.canvasWidth - k1;
		int i2 = 0;
		if (k < Rasterizer2D.clipStartY) {
			int j2 = Rasterizer2D.clipStartY - k;
			j1 -= j2;
			k = Rasterizer2D.clipStartY;
			i1 += j2 * k1;
			l += j2 * Rasterizer2D.canvasWidth;
		}
		if (k + j1 > Rasterizer2D.clipEndY) {
			j1 -= (k + j1) - Rasterizer2D.clipEndY;
		}
		if (i < Rasterizer2D.clipStartX) {
			int k2 = Rasterizer2D.clipStartX - i;
			k1 -= k2;
			i = Rasterizer2D.clipStartX;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if (i + k1 > Rasterizer2D.clipEndX) {
			int l2 = (i + k1) - Rasterizer2D.clipEndX;
			k1 -= l2;
			i2 += l2;
			l1 += l2;
		}
		if (!(k1 <= 0 || j1 <= 0)) {
			drawPixels(Rasterizer2D.canvasRaster, tempArray, i1, l, k1, j1, l1, i2);
		}
	}

	public void drawSprite1(int i, int j, int k, boolean one) {
		i += xOffset;
		j += yOffset;
		int i1 = i + j * Rasterizer2D.canvasWidth;
		int j1 = 0;
		int k1 = imageHeight;
		int l1 = imageHeight;
		int i2 = Rasterizer2D.canvasWidth - l1;
		int j2 = 0;
		if (!(one && j > 0) && j < clipStartY) {
			int k2 = clipStartY - j;
			k1 -= k2;
			j = clipStartY;
			j1 += k2 * l1;
			i1 += k2 * Rasterizer2D.canvasWidth;
		}
		if (j + k1 > clipEndY)
			k1 -= (j + k1) - clipEndY;
		if (!(one && j > 0) && i < clipStartX) {
			int l2 = clipStartX - i;
			l1 -= l2;
			i = clipStartX;
			j1 += l2;
			i1 += l2;
			j2 += l2;
			i2 += l2;
		}
		if (i + l1 > clipEndX) {
			int i3 = (i + l1) - clipEndX;
			l1 -= i3;
			j2 += i3;
			i2 += i3;
		}
		if (!(l1 <= 0 || k1 <= 0)) {
			drawPixels(j1, l1, Rasterizer2D.canvasRaster, imageRaster, j2, k1, i2, k, i1);
		}
	}


	public void drawImage(int x, int y, int alpha) {
		if(this.alpha) {
			drawAlphaImage(x, y, alpha);
			return;
		}
		x += xOffset;
		y += yOffset;
		int i1 = x + y * Rasterizer2D.canvasWidth;
		int j1 = 0;
		int k1 = imageHeight;
		int l1 = imageWidth;
		int i2 = Rasterizer2D.canvasWidth - l1;
		int j2 = 0;
		if(y < Rasterizer2D.clipStartY) {
			final int k2 = Rasterizer2D.clipStartY - y;
			k1 -= k2;
			y = Rasterizer2D.clipStartY;
			j1 += k2 * l1;
			i1 += k2 * Rasterizer2D.canvasWidth;
		}
		if(y + k1 > Rasterizer2D.clipEndY) {
			k1 -= y + k1 - Rasterizer2D.clipEndY;
		}
		if(x < Rasterizer2D.clipStartX) {
			final int l2 = Rasterizer2D.clipStartX - x;
			l1 -= l2;
			x = Rasterizer2D.clipStartX;
			j1 += l2;
			i1 += l2;
			j2 += l2;
			i2 += l2;
		}
		if(x + l1 > Rasterizer2D.clipEndX) {
			final int i3 = x + l1 - Rasterizer2D.clipEndX;
			l1 -= i3;
			j2 += i3;
			i2 += i3;
		}
		if(l1 > 0 && k1 > 0) {
			drawPixels(j1, l1, Rasterizer2D.canvasRaster, imageRaster, j2, k1, i2, alpha, i1);
		}
	}

	private void drawPixels(int areaPixels[], int imagePixels[], int imagePixel, int areaPixel, int l, int i1, int areaWidth, int imageWidth) {
		int imagePixelColor;
		final int l1 = -(l >> 2);
		l = -(l & 3);
		for(int i2 = -i1; i2 < 0; i2++) {
			for(int j2 = l1; j2 < 0; j2++) {
				imagePixelColor = imagePixels[imagePixel++];
				if(imagePixelColor != 0 && imagePixelColor != -1) {
					areaPixels[areaPixel] = imagePixelColor;
				}
				areaPixel++;
				imagePixelColor = imagePixels[imagePixel++];
				if(imagePixelColor != 0 && imagePixelColor != -1) {
					areaPixels[areaPixel] = imagePixelColor;
				}
				areaPixel++;
				imagePixelColor = imagePixels[imagePixel++];
				if(imagePixelColor != 0 && imagePixelColor != -1) {
					areaPixels[areaPixel] = imagePixelColor;
				}
				areaPixel++;
				imagePixelColor = imagePixels[imagePixel++];
				if(imagePixelColor != 0 && imagePixelColor != -1) {
					areaPixels[areaPixel] = imagePixelColor;
				}
				areaPixel++;
			}
			for(int k2 = l; k2 < 0; k2++) {
				imagePixelColor = imagePixels[imagePixel++];
				if(imagePixelColor != 0 && imagePixelColor != -1) {
					areaPixels[areaPixel] = imagePixelColor;
				}
				areaPixel++;
			}
			areaPixel += areaWidth;
			imagePixel += imageWidth;
		}
	}

	private void drawPixels(int imagePixel, int width, int areaPixels[], int imagePixels[], int imageWidth, int height, int areaWidth, int alpha, int areaPixel) {
		final int a = 256 - alpha;
		for(int k2 = -height; k2 < 0; k2++) {
			for(int l2 = -width; l2 < 0; l2++) {
				final int imagePixelColor = imagePixels[imagePixel++];
				if(imagePixelColor != 0 && imagePixelColor != -1) {
					final int areaPixelColor = areaPixels[areaPixel];
					areaPixels[areaPixel] = ((imagePixelColor & 0xff00ff) * alpha + (areaPixelColor & 0xff00ff) * a & 0xff00ff00) + ((imagePixelColor & 0xff00) * alpha + (areaPixelColor & 0xff00) * a & 0xff0000) >> 8;
				}
				areaPixel++;
			}
			areaPixel += areaWidth;
			imagePixel += imageWidth;
		}
	}

	/**
	 * Draws rotated image which has alpha channel.
	 */
	public void drawAffineTransformedAlphaImage(int x, int y, int width, int height, int centerX, int centerY, int lineStarts[], int lineLengths[], int rotation, int zoom) {
		drawAffineTransformedAlphaImage(x, y, width, height, centerX, centerY, lineStarts, lineLengths, rotation, zoom, 256);
	}

	/**
	 * Draws rotated image which has alpha channel with alpha value support.
	 */
	private void drawAffineTransformedAlphaImage(int x, int y, int width, int height, int centerX, int centerY, int lineStarts[], int lineLengths[], int rotation, int zoom, int alpha) {
		try {
			final int imageCenterPointX = -width / 2;
			final int imageCenterPointY = -height / 2;
			int imagePixelStepY = (int) (Math.sin(rotation / 326.11000000000001D) * 65536D);
			int imagePixelStepX = (int) (Math.cos(rotation / 326.11000000000001D) * 65536D);
			imagePixelStepY = imagePixelStepY * zoom >> 8;
			imagePixelStepX = imagePixelStepX * zoom >> 8;
			int rotationCenterPointX = (centerX << 16) + imageCenterPointY * imagePixelStepY + imageCenterPointX * imagePixelStepX;
			int rotationCenterPointY = (centerY << 16) + imageCenterPointY * imagePixelStepX - imageCenterPointX * imagePixelStepY;
			int pixel = x + y * Rasterizer2D.canvasWidth;
			for(y = 0; y < height; y++) {
				final int lineStart = lineStarts[y];
				int areaPixel = pixel + lineStart;
				int imagePixelX = rotationCenterPointX + imagePixelStepX * lineStart;
				int imagePixelY = rotationCenterPointY - imagePixelStepY * lineStart;
				for(x = -lineLengths[y]; x < 0; x++) {
					// TODO antialiasing
					int imageAlpha = alpha - (255 - (imageRaster[(imagePixelX >> 16) + (imagePixelY >> 16) * imageWidth] >> 24 & 255));
					if(imageAlpha < 0) {
						imageAlpha = 0;
					}
					final int areaAlpha = 256 - imageAlpha;
					final int imageColor = imageRaster[(imagePixelX >> 16) + (imagePixelY >> 16) * imageWidth];
					if(imageColor != 0 && imageColor != 0xffffff) {
						final int destColor = canvasRaster[areaPixel];
						Rasterizer2D.canvasRaster[areaPixel] = ((imageColor & 0xff00ff) * imageAlpha + (destColor & 0xff00ff) * areaAlpha & 0xff00ff00) + ((imageColor & 0xff00) * imageAlpha + (destColor & 0xff00) * areaAlpha & 0xff0000) >> 8;
					}
					areaPixel++;
					imagePixelX += imagePixelStepX;
					imagePixelY -= imagePixelStepY;
				}
				rotationCenterPointX += imagePixelStepY;
				rotationCenterPointY += imagePixelStepX;
				pixel += Rasterizer2D.canvasWidth;
			}
		} catch(final Exception e) {
			//empty
		}
	}

	/**
	 * Draws a image with rotation.
	 * @param rotation a value between 0 and 2047
	 * @param zoom     1:1 = 256 - to zoom in, make smaller
	 */
	public void drawAffineTransformedImage(int x, int y, int width, int height, int centerX, int centerY, int lineStarts[], int lineLengths[], int rotation, int zoom) {
		try {
			final int j2 = -width / 2;
			final int k2 = -height / 2;
			int l2 = (int) (Math.sin(rotation / 326.11000000000001D) * 65536D);
			int i3 = (int) (Math.cos(rotation / 326.11000000000001D) * 65536D);
			l2 = l2 * zoom >> 8;
			i3 = i3 * zoom >> 8;
			int j3 = (centerX << 16) + k2 * l2 + j2 * i3;
			int k3 = (centerY << 16) + k2 * i3 - j2 * l2;
			int pixel = x + y * Rasterizer2D.canvasWidth;
			for(y = 0; y < height; y++) {
				final int lineStart = lineStarts[y];
				int areaPixel = pixel + lineStart;
				int k4 = j3 + i3 * lineStart;
				int l4 = k3 - l2 * lineStart;
				for(x = -lineLengths[y]; x < 0; x++) {
					if(Config.def.enchanceMap()) {
						int x1 = k4 >> 16;
						int y1 = l4 >> 16;
						int x2 = x1 + 1;
						int y2 = y1 + 1;
						int c1 = imageRaster[x1 + y1 * imageWidth];
						int c2 = imageRaster[x2 + y1 * imageWidth];
						int c3 = imageRaster[x1 + y2 * imageWidth];
						int c4 = imageRaster[x2 + y2 * imageWidth];
						int u1 = (k4 >> 8) - (x1 << 8);
						int v1 = (l4 >> 8) - (y1 << 8);
						int u2 = (x2 << 8) - (k4 >> 8);
						int v2 = (y2 << 8) - (l4 >> 8);
						int a1 = u2 * v2;
						int a2 = u1 * v2;
						int a3 = u2 * v1;
						int a4 = u1 * v1;
						int r = (c1 >> 16 & 0xff) * a1 + (c2 >> 16 & 0xff) * a2 +
								(c3 >> 16 & 0xff) * a3 + (c4 >> 16 & 0xff) * a4 & 0xff0000;
						int g = (c1 >> 8 & 0xff) * a1 + (c2 >> 8 & 0xff) * a2 +
								(c3 >> 8 & 0xff) * a3 + (c4 >> 8 & 0xff) * a4 >> 8 & 0xff00;
						int b = (c1 & 0xff) * a1 + (c2 & 0xff) * a2 +
								(c3 & 0xff) * a3 + (c4 & 0xff) * a4 >> 16;
						Rasterizer2D.canvasRaster[areaPixel++] = r | g | b;
					} else {
						Rasterizer2D.canvasRaster[areaPixel++] = imageRaster[(k4 >> 16) + (l4 >> 16) * imageWidth];
					}
					k4 += i3;
					l4 -= l2;
				}
				j3 += l2;
				k3 += i3;
				pixel += Rasterizer2D.canvasWidth;
			}
		} catch(final Exception e) {
			//empty
		}
	}

	public void method344(int i, int j, int k) {
		for(int i1 = 0; i1 < imageRaster.length; i1++) {
			final int j1 = imageRaster[i1];
			if(j1 != 0) {
				int k1 = j1 >> 16 & 0xff;
				k1 += i;
				if(k1 < 1) {
					k1 = 1;
				} else if(k1 > 255) {
					k1 = 255;
				}
				int l1 = j1 >> 8 & 0xff;
				l1 += j;
				if(l1 < 1) {
					l1 = 1;
				} else if(l1 > 255) {
					l1 = 255;
				}
				int i2 = j1 & 0xff;
				i2 += k;
				if(i2 < 1) {
					i2 = 1;
				} else if(i2 > 255) {
					i2 = 255;
				}
				imageRaster[i1] = (k1 << 16) + (l1 << 8) + i2;
			}
		}

	}

	public void method345() {
		final int ai[] = new int[imageOriginalWidth * imageOriginalHeight];
		for(int j = 0; j < imageHeight; j++) {
			System.arraycopy(imageRaster, j * imageWidth, ai, j + yOffset * imageOriginalWidth + xOffset, imageWidth);
		}
		imageRaster = ai;
		imageWidth = imageOriginalWidth;
		imageHeight = imageOriginalHeight;
		xOffset = 0;
		yOffset = 0;
	}

	public void method346(int i, int j) {
		i += xOffset;
		j += yOffset;
		int l = i + j * Rasterizer2D.canvasWidth;
		int i1 = 0;
		int j1 = imageHeight;
		int k1 = imageWidth;
		int l1 = Rasterizer2D.canvasWidth - k1;
		int i2 = 0;
		if(j < Rasterizer2D.clipStartY) {
			final int j2 = Rasterizer2D.clipStartY - j;
			j1 -= j2;
			j = Rasterizer2D.clipStartY;
			i1 += j2 * k1;
			l += j2 * Rasterizer2D.canvasWidth;
		}
		if(j + j1 > Rasterizer2D.clipEndY) {
			j1 -= j + j1 - Rasterizer2D.clipEndY;
		}
		if(i < Rasterizer2D.clipStartX) {
			final int k2 = Rasterizer2D.clipStartX - i;
			k1 -= k2;
			i = Rasterizer2D.clipStartX;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if(i + k1 > Rasterizer2D.clipEndX) {
			final int l2 = i + k1 - Rasterizer2D.clipEndX;
			k1 -= l2;
			i2 += l2;
			l1 += l2;
		}
		if(k1 <= 0 || j1 <= 0) {
		} else {
			method347(l, k1, j1, i2, i1, l1, imageRaster, Rasterizer2D.canvasRaster);
		}
	}

	private void method347(int areaPixel, int j, int k, int imageWidth, int imagePixel, int areaWidth, int imagePixels[], int areaPixels[]) {
		final int l1 = -(j >> 2);
		j = -(j & 3);
		for(int i2 = -k; i2 < 0; i2++) {
			for(int j2 = l1; j2 < 0; j2++) {
				areaPixels[areaPixel++] = imagePixels[imagePixel++];
				areaPixels[areaPixel++] = imagePixels[imagePixel++];
				areaPixels[areaPixel++] = imagePixels[imagePixel++];
				areaPixels[areaPixel++] = imagePixels[imagePixel++];
			}
			for(int k2 = j; k2 < 0; k2++) {
				areaPixels[areaPixel++] = imagePixels[imagePixel++];
			}
			areaPixel += areaWidth;
			imagePixel += imageWidth;
		}
	}

	public void method353(int x, int y, double rotation) {
		final int centerY = 15;
		final int width = 20;
		final int centerX = 15;
		final int zoom = 256;
		final int height = 20;
		try {
			final int i2 = -width / 2;
			final int j2 = -height / 2;
			int k2 = (int) (Math.sin(rotation) * 65536D);
			int l2 = (int) (Math.cos(rotation) * 65536D);
			k2 = k2 * zoom >> 8;
			l2 = l2 * zoom >> 8;
			int i3 = (centerX << 16) + j2 * k2 + i2 * l2;
			int j3 = (centerY << 16) + j2 * l2 - i2 * k2;
			int k3 = x + y * Rasterizer2D.canvasWidth;
			for(y = 0; y < height; y++) {
				int areaPixel = k3;
				int i4 = i3;
				int j4 = j3;
				for(x = -width; x < 0; x++) {
					// TODO antialiasing
					final int imagePixelColor = imageRaster[(i4 >> 16) + (j4 >> 16) * imageWidth];
					if(imagePixelColor != 0) {
						Rasterizer2D.canvasRaster[areaPixel] = imagePixelColor;
					}
					areaPixel++;
					i4 += l2;
					j4 -= k2;
				}
				i3 += k2;
				j3 += l2;
				k3 += Rasterizer2D.canvasWidth;
			}
		} catch(final Exception _ex) {
			_ex.printStackTrace();
		}
	}

	public void method354(PaletteImage background, int i, int j) {
		j += xOffset;
		i += yOffset;
		int k = j + i * Rasterizer2D.canvasWidth;
		int l = 0;
		int i1 = imageHeight;
		int j1 = imageWidth;
		int k1 = Rasterizer2D.canvasWidth - j1;
		int l1 = 0;
		if(i < Rasterizer2D.clipStartY) {
			final int i2 = Rasterizer2D.clipStartY - i;
			i1 -= i2;
			i = Rasterizer2D.clipStartY;
			l += i2 * j1;
			k += i2 * Rasterizer2D.canvasWidth;
		}
		if(i + i1 > Rasterizer2D.clipEndY) {
			i1 -= i + i1 - Rasterizer2D.clipEndY;
		}
		if(j < Rasterizer2D.clipStartX) {
			final int j2 = Rasterizer2D.clipStartX - j;
			j1 -= j2;
			j = Rasterizer2D.clipStartX;
			l += j2;
			k += j2;
			l1 += j2;
			k1 += j2;
		}
		if(j + j1 > Rasterizer2D.clipEndX) {
			final int k2 = j + j1 - Rasterizer2D.clipEndX;
			j1 -= k2;
			l1 += k2;
			k1 += k2;
		}
		if(!(j1 <= 0 || i1 <= 0)) {
			method355(imageRaster, j1, background.entryList, i1, Rasterizer2D.canvasRaster, 0, k1, k, l1, l);
		}
	}

	private void method355(int imagePixels[], int i, byte abyte0[], int j, int areaPixels[], int imagePixelColor, int areaWidth, int areaPixel, int imageWidth, int imagePixel) {
		final int l1 = -(i >> 2);
		i = -(i & 3);
		for(int j2 = -j; j2 < 0; j2++) {
			for(int k2 = l1; k2 < 0; k2++) {
				imagePixelColor = imagePixels[imagePixel++];
				if(imagePixelColor != 0 && abyte0[areaPixel] == 0) {
					areaPixels[areaPixel] = imagePixelColor;
				}
				areaPixel++;
				imagePixelColor = imagePixels[imagePixel++];
				if(imagePixelColor != 0 && abyte0[areaPixel] == 0) {
					areaPixels[areaPixel] = imagePixelColor;
				}
				areaPixel++;
				imagePixelColor = imagePixels[imagePixel++];
				if(imagePixelColor != 0 && abyte0[areaPixel] == 0) {
					areaPixels[areaPixel] = imagePixelColor;
				}
				areaPixel++;
				imagePixelColor = imagePixels[imagePixel++];
				if(imagePixelColor != 0 && abyte0[areaPixel] == 0) {
					areaPixels[areaPixel] = imagePixelColor;
				}
				areaPixel++;
			}
			for(int l2 = i; l2 < 0; l2++) {
				imagePixelColor = imagePixels[imagePixel++];
				if(imagePixelColor != 0 && abyte0[areaPixel] == 0) {
					areaPixels[areaPixel] = imagePixelColor;
				}
				areaPixel++;
			}
			areaPixel += areaWidth;
			imagePixel += imageWidth;
		}
	}

	private void setAlphaPixels(int width, int height, int destPixels[], int srcPixels[], int alpha, int destOffset, int srcOffset, int destStep, int srcStep) {
		int srcColor;
		int destAlpha;
		for(int loop = -height; loop < 0; loop++) {
			for(int loop2 = -width; loop2 < 0; loop2++) {
				int srcAlpha = alpha - (255 - (imageRaster[srcOffset] >> 24 & 255));
				if(srcAlpha < 0) {
					srcAlpha = 0;
				}
				destAlpha = 256 - srcAlpha;
				srcColor = srcPixels[srcOffset++];
				if(srcColor != 0 && srcColor != 0xffffff) {
					final int destColor = destPixels[destOffset];
					destPixels[destOffset] = ((srcColor & 0xff00ff) * srcAlpha + (destColor & 0xff00ff) * destAlpha & 0xff00ff00) + ((srcColor & 0xff00) * srcAlpha + (destColor & 0xff00) * destAlpha & 0xff0000) >> 8;
				}
				destOffset++;
			}
			destOffset += destStep;
			srcOffset += srcStep;
		}
	}

	public void setCanvas() {
		Rasterizer2D.setCanvas(imageRaster, imageHeight, imageWidth);
	}

	private void setTransparency(int red, int green, int blue) {
		for(int index = 0; index < imageRaster.length; index++) {
			if((imageRaster[index] >> 16 & 255) == red && (imageRaster[index] >> 8 & 255) == green && (imageRaster[index] & 255) == blue) {
				imageRaster[index] = 0;
			}
		}
	}
}
