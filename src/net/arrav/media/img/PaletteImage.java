package net.arrav.media.img;

import net.arrav.cache.CacheArchive;
import net.arrav.media.Rasterizer2D;
import net.arrav.util.io.Buffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class PaletteImage extends Rasterizer2D {

	public byte[] entryList;
	private final int[] colorMap;
	public int trueWidth;
	public int trueHeight;
	public int offsetX;
	public int offsetY;
	private int useWidth;
	private int useHeight;

	public void dump(int newId) {
		BufferedImage img = new BufferedImage(trueWidth, trueHeight, BufferedImage.TYPE_INT_RGB);
		int[] dest = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		for(int i = 0; i < entryList.length; i++) {
			int entry = entryList[i];
			if(entry != 0) {
				dest[i] = colorMap[entry & 0xff];
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
			writer2.write(newId + "-x" + offsetX + "-y" + offsetX);
			writer2.newLine();
			writer2.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public PaletteImage(CacheArchive archive, String pack, int index) {
		final Buffer datBuffer = new Buffer(archive.getFile(pack + ".dat"));
		final Buffer idxBuffer = new Buffer(archive.getFile("index.dat"));
		idxBuffer.pos = datBuffer.getUShort();
		useWidth = idxBuffer.getUShort();
		useHeight = idxBuffer.getUShort();
		final int colorCount = idxBuffer.getUByte();
		colorMap = new int[colorCount];
		for(int pixel = 0; pixel < colorCount - 1; pixel++) {
			colorMap[pixel + 1] = idxBuffer.getUMedium();
		}
		for(int l = 0; l < index; l++) {
			idxBuffer.pos += 2;
			datBuffer.pos += idxBuffer.getUShort() * idxBuffer.getUShort();
			idxBuffer.pos++;
		}
		offsetX = idxBuffer.getUByte();
		offsetY = idxBuffer.getUByte();
		trueWidth = idxBuffer.getUShort();
		trueHeight = idxBuffer.getUShort();
		final int loadingWay = idxBuffer.getUByte();
		final int pixelCount = trueWidth * trueHeight;
		entryList = new byte[pixelCount];
		if(loadingWay == 0) {
			for(int pixel = 0; pixel < pixelCount; pixel++) {
				entryList[pixel] = datBuffer.getSByte();
			}
			return;
		}
		if(loadingWay == 1) {
			for(int x = 0; x < trueWidth; x++) {
				for(int y = 0; y < trueHeight; y++) {
					entryList[x + y * trueWidth] = datBuffer.getSByte();
				}
			}
		}
	}

	public void drawImage(int x, int y) {
		x += offsetX;
		y += offsetY;
		int destPos = x + y * Rasterizer2D.canvasWidth;
		int srcPos = 0;
		int height = trueHeight;
		int width = trueWidth;
		int destOffset = Rasterizer2D.canvasWidth - width;
		int srcOffset = 0;
		if(y < Rasterizer2D.clipStartY) {
			final int offset = Rasterizer2D.clipStartY - y;
			height -= offset;
			y = Rasterizer2D.clipStartY;
			srcPos += offset * width;
			destPos += offset * Rasterizer2D.canvasWidth;
		}
		if(y + height > Rasterizer2D.clipEndY) {
			height -= y + height - Rasterizer2D.clipEndY;
		}
		if(x < Rasterizer2D.clipStartX) {
			final int offset = Rasterizer2D.clipStartX - x;
			width -= offset;
			x = Rasterizer2D.clipStartX;
			srcPos += offset;
			destPos += offset;
			srcOffset += offset;
			destOffset += offset;
		}
		if(x + width > Rasterizer2D.clipEndX) {
			final int offset = x + width - Rasterizer2D.clipEndX;
			width -= offset;
			srcOffset += offset;
			destOffset += offset;
		}
		if(width > 0 && height > 0) {
			drawRaster(srcPos, srcOffset, entryList, destPos, destOffset, Rasterizer2D.canvasRaster, width, height, colorMap);
		}
	}

	private void drawRaster(int srcPos, int srcOffset, byte[] src, int destPos, int destOffset, int dest[], int width, int height, int[] imageColorPalette) {
		final int k1 = -(width >> 2);
		width = -(width & 3);
		for(int y = -height; y < 0; y++) {
			for(int i2 = k1; i2 < 0; i2++) {
				int colIdx = src[srcPos++];
				if(colIdx != 0) {
					dest[destPos] = imageColorPalette[colIdx & 0xff];
				}
				destPos++;
				colIdx = src[srcPos++];
				if(colIdx != 0) {
					dest[destPos] = imageColorPalette[colIdx & 0xff];
				}
				destPos++;
				colIdx = src[srcPos++];
				if(colIdx != 0) {
					dest[destPos] = imageColorPalette[colIdx & 0xff];
				}
				destPos++;
				colIdx = src[srcPos++];
				if(colIdx != 0) {
					dest[destPos] = imageColorPalette[colIdx & 0xff];
				}
				destPos++;
			}
			for(int x = width; x < 0; x++) {
				final int colIdx = src[srcPos++];
				if(colIdx != 0) {
					dest[destPos] = imageColorPalette[colIdx & 0xff];
				}
				destPos++;
			}
			destPos += destOffset;
			srcPos += srcOffset;
		}
	}

	public void flipHorizontal() {
		final byte[] pixels = new byte[trueWidth * trueHeight];
		int pixel = 0;
		for(int y = 0; y < trueHeight; y++) {
			for(int x = trueWidth - 1; x >= 0; x--) {
				pixels[pixel++] = entryList[x + y * trueWidth];
			}
		}
		entryList = pixels;
		offsetX = useWidth - trueWidth - offsetX;
	}

	public void flipVertical() {
		final byte[] pixels = new byte[trueWidth * trueHeight];
		int pixel = 0;
		for(int y = trueHeight - 1; y >= 0; y--) {
			for(int x = 0; x < trueWidth; x++) {
				pixels[pixel++] = entryList[x + y * trueWidth];
			}
		}
		entryList = pixels;
		offsetY = useHeight - trueHeight - offsetY;
	}

	/**
	 * Sets the image size to half from the original library size.
	 */
	public void setHalfSize() {
		useWidth /= 2;
		useHeight /= 2;
		final byte[] entries = new byte[useWidth * useHeight];
		int pixel = 0;
		for(int y = 0; y < trueHeight; y++) {
			for(int x = 0; x < trueWidth; x++) {
				entries[(x + offsetX >> 1) + (y + offsetY >> 1) * useWidth] = entryList[pixel++];
			}
		}
		entryList = entries;
		trueWidth = useWidth;
		trueHeight = useHeight;
		offsetX = 0;
		offsetY = 0;
	}

	/**
	 * Sets the image size to same as the original library size.
	 */
	public void setTrueSize() {
		if(trueWidth == useWidth && trueHeight == useHeight) {
			return;
		}
		final byte[] entries = new byte[useWidth * useHeight];
		int colIdx = 0;
		for(int y = 0; y < trueHeight; y++) {
			for(int x = 0; x < trueWidth; x++) {
				entries[x + offsetX + (y + offsetY) * useWidth] = entryList[colIdx++];
			}
		}
		entryList = entries;
		trueWidth = useWidth;
		trueHeight = useHeight;
		offsetX = 0;
		offsetY = 0;
	}

	public void shiftColors(int red, int green, int blue) {
		for(int pixel = 0; pixel < colorMap.length; pixel++) {
			int r = colorMap[pixel] >> 16 & 0xff;
			r += red;
			int g = colorMap[pixel] >> 8 & 0xff;
			g += green;
			int b = colorMap[pixel] & 0xff;
			b += blue;
			if(r < 0) {
				r = 0;
			} else if(r > 255) {
				r = 255;
			}
			if(g < 0) {
				g = 0;
			} else if(g > 255) {
				g = 255;
			}
			if(b < 0) {
				b = 0;
			} else if(b > 255) {
				b = 255;
			}
			colorMap[pixel] = (r << 16) + (g << 8) + b;
		}
	}
}
