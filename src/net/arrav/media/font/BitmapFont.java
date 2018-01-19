package net.arrav.media.font;

import net.arrav.Constants;
import net.arrav.cache.CacheArchive;
import net.arrav.media.Rasterizer2D;
import net.arrav.util.io.Buffer;

import java.util.Random;

public final class BitmapFont extends Rasterizer2D {

	private byte[][] typefaceMask;
	private int[] typefaceWidth;
	private int[] typefaceHeight;
	private int[] typefaceHorizontalOffset;
	private int[] typefaceVerticalOffset;
	private int[] charWidth;
	public int lineHeight;
	private Random tooltipRandom;
	private boolean strikethrought;

	public BitmapFont(CacheArchive archive, String filename, boolean largeSpaces) {
		try {
			int length = (filename.equals("hit_full") || filename.equals("critical_full")) ? 58 : 256;
			typefaceMask = new byte[length][];
			typefaceWidth = new int[length];
			typefaceHeight = new int[length];
			typefaceHorizontalOffset = new int[length];
			typefaceVerticalOffset = new int[length];
			charWidth = new int[length];
			tooltipRandom = new Random();
			strikethrought = false;
			final Buffer dat = new Buffer(archive.getFile(filename + ".dat"));
			final Buffer idx = new Buffer(archive.getFile("index.dat"));
			idx.pos = dat.getUShort() + 4;
			final int k = idx.getUByte();
			if(k > 0) {
				idx.pos += 3 * (k - 1);
			}
			for(int index = 0; index < length; index++) {
				typefaceHorizontalOffset[index] = idx.getUByte();
				typefaceVerticalOffset[index] = idx.getUByte();
				final int width = typefaceWidth[index] = idx.getUShort();
				final int height = typefaceHeight[index] = idx.getUShort();
				final int k1 = idx.getUByte();
				final int pixelCount = width * height;
				typefaceMask[index] = new byte[pixelCount];
				if(k1 == 0) {
					for(int i2 = 0; i2 < pixelCount; i2++) {
						typefaceMask[index][i2] = dat.getSByte();
					}
				} else if(k1 == 1) {
					for(int j2 = 0; j2 < width; j2++) {
						for(int l2 = 0; l2 < height; l2++) {
							typefaceMask[index][j2 + l2 * width] = dat.getSByte();
						}
					}
				}
				if(height > lineHeight && index < 128) {
					lineHeight = height;
				}
				typefaceHorizontalOffset[index] = 1;
				charWidth[index] = width + 2;
				int k2 = 0;
				for(int i3 = height / 7; i3 < height; i3++) {
					k2 += typefaceMask[index][i3 * width];
				}
				if(k2 <= height / 7) {
					charWidth[index]--;
					typefaceHorizontalOffset[index] = 0;
				}
				k2 = 0;
				for(int j3 = height / 7; j3 < height; j3++) {
					k2 += typefaceMask[index][width - 1 + j3 * width];
				}
				if(k2 <= height / 7) {
					charWidth[index]--;
				}
			}
			if(largeSpaces) {
				charWidth[' '] = charWidth['I'];
			} else {
				charWidth[' '] = charWidth['i'];
			}
		} catch(Exception ignored) {
		}
	}

	public void drawLeftAlignedString(String s, int x, int y, int color) {
		if(s == null) {
			return;
		}
		y -= lineHeight;
		for(int i1 = 0; i1 < s.length(); i1++) {
			final char c = s.charAt(i1);
			if(c > typefaceMask.length && c > typefaceHeight.length)
				continue;
			if(c != ' ') {
				drawTypeface(typefaceMask[c], x + typefaceHorizontalOffset[c], y + typefaceVerticalOffset[c], typefaceWidth[c], typefaceHeight[c], color);
			}
			x += charWidth[c];
		}
	}

	public void drawCenteredString(String s, int x, int y, int color) {
		drawLeftAlignedString(s, x - getStringWidth(s) / 2, y, color);
	}

	public void drawRightAlignedString(String s, int x, int y, int color) {
		drawLeftAlignedString(s, x - getStringWidth(s), y, color);
	}

	public void drawLeftAlignedString(String s, int x, int y, int color, int alpha) {
		if(s == null) {
			return;
		}
		y -= lineHeight;
		for(int i1 = 0; i1 < s.length(); i1++) {
			char c = s.charAt(i1);
			if(c != ' ') {
				drawTypeface(typefaceMask[c], x + typefaceHorizontalOffset[c], y + typefaceVerticalOffset[c], typefaceWidth[c], typefaceHeight[c], color, alpha);
			}
			x += charWidth[c];
		}
	}

	public void drawCenteredString(String s, int x, int y, int color, int alpha) {
		drawLeftAlignedString(s, x - getStringWidth(s) / 2, y, color, alpha);
	}

	public void drawRightAlignedString(String s, int x, int y, int color, int alpha) {
		drawLeftAlignedString(s, x - getStringWidth(s), y, color, alpha);
	}

	public void drawLeftAlignedEffectString(String s, int x, int y, int color, boolean shadow) {
		strikethrought = false;
		final int l = x;
		if(s == null) {
			return;
		}
		y -= lineHeight;
		for(int i1 = 0; i1 < s.length(); i1++) {
			if(s.charAt(i1) == '@' && i1 + 4 < s.length() && s.charAt(i1 + 4) == '@') {
				final int j1 = getEffect(s.substring(i1 + 1, i1 + 4));
				if(j1 != -1) {
					color = j1;
				}
				i1 += 4;
			} else {
				final char c = s.charAt(i1);
				if(c != ' ') {
					if(shadow) {
						drawTypeface(typefaceMask[c], x + typefaceHorizontalOffset[c] + 1, y + typefaceVerticalOffset[c] + 1, typefaceWidth[c], typefaceHeight[c], 0);
					}
					drawTypeface(typefaceMask[c], x + typefaceHorizontalOffset[c], y + typefaceVerticalOffset[c], typefaceWidth[c], typefaceHeight[c], color);
				}
				x += charWidth[c];
			}
		}
		if(strikethrought) {
			Rasterizer2D.drawHorizontalLine(l, y + (int) (lineHeight * 0.69999999999999996D), x - l, 0x800000);
		}
	}

	public void drawCenteredEffectString(String s, int x, int y, int color, boolean shadow) {
		drawLeftAlignedEffectString(s, x - getEffectStringWidth(s) / 2, y, color, shadow);
	}

	public void drawRightAlignedEffectString(String s, int x, int y, int color, boolean shadow) {
		drawLeftAlignedEffectString(s, x - getEffectStringWidth(s), y, color, shadow);
	}

	public void drawWaveString(String s, int x, int y, int frame, int color) {
		if(s == null) {
			return;
		}
		x -= getStringWidth(s) / 2;
		y -= lineHeight;
		for(int i1 = 0; i1 < s.length(); i1++) {
			final char c = s.charAt(i1);
			if(c != ' ') {
				drawTypeface(typefaceMask[c], x + typefaceHorizontalOffset[c], y + typefaceVerticalOffset[c] + (int) (Math.sin(i1 / 2D + frame / 5D) * 5D), typefaceWidth[c], typefaceHeight[c], color);
			}
			x += charWidth[c];
		}
	}

	public void drawWave2String(String s, int x, int y, int frame, int color) {
		if(s == null) {
			return;
		}
		x -= getStringWidth(s) / 2;
		y -= lineHeight;
		for(int i1 = 0; i1 < s.length(); i1++) {
			final char c = s.charAt(i1);
			if(c != ' ') {
				drawTypeface(typefaceMask[c], x + typefaceHorizontalOffset[c] + (int) (Math.sin(i1 / 5D + frame / 5D) * 5D), y + typefaceVerticalOffset[c] + (int) (Math.sin(i1 / 3D + frame / 5D) * 5D), typefaceWidth[c], typefaceHeight[c], color);
			}
			x += charWidth[c];
		}
	}

	public void drawShakeString(String s, int x, int y, int frame, int pos, int color) {
		if(s == null) {
			return;
		}
		double d = 7D - pos / 8D;
		if(d < 0.0D) {
			d = 0.0D;
		}
		x -= getStringWidth(s) / 2;
		y -= lineHeight;
		for(int k1 = 0; k1 < s.length(); k1++) {
			final char c = s.charAt(k1);
			if(c != ' ') {
				drawTypeface(typefaceMask[c], x + typefaceHorizontalOffset[c], y + typefaceVerticalOffset[c] + (int) (Math.sin(k1 / 1.5D + frame) * d), typefaceWidth[c], typefaceHeight[c], color);
			}
			x += charWidth[c];
		}
	}

	private int getEffect(String code) {
		if(code.equals("mut")) {
			return 0x376884;
		}
		if(code.equals("369")) {
			return 0x336699;
		}
		if(code.equals("mon")) {
			return 0x00ff80;
		}
		if(code.equals("red")) {
			return 0xff0000;
		}
		if(code.equals("ban")) {
			return 0xcb7a1a;
		}
		if(code.equals("gre")) {
			return 65280;
		}
		if(code.equals("blu")) {
			return 255;
		}
		if(code.equals("yel")) {
			return 0xffff00;
		}
		if(code.equals("cya")) {
			return 65535;
		}
		if(code.equals("mag")) {
			return 0xff00ff;
		}
		if(code.equals("whi")) {
			return 0xffffff;
		}
		if(code.equals("bla")) {
			return 0;
		}
		if(code.equals("lre")) {
			return 0xff9040;
		}
		if(code.equals("dre")) {
			return 0x800000;
		}
		if(code.equals("dbl")) {
			return 128;
		}
		if(code.equals("or1")) {
			return 0xffb000;
		}
		if(code.equals("or2")) {
			return 0xff7000;
		}
		if(code.equals("or3")) {
			return 0xff3000;
		}
		if(code.equals("gr1")) {
			return 0xc0ff00;
		}
		if(code.equals("gr2")) {
			return 0x80ff00;
		}
		if(code.equals("gr3")) {
			return 0x40ff00;
		}
		if(code.equals("str")) {
			strikethrought = true;
		}
		if(code.equals("end")) {
			strikethrought = false;
		}
		return -1;
	}

	public int getStringWidth(String s) {
		if(s == null) {
			return 0;
		}
		int width = 0;
		for(int i = 0; i < s.length(); i++) {
			width += charWidth[s.charAt(i)];
		}
		return width;
	}

	public int getEffectStringWidth(String s) {
		if(s == null) {
			return 0;
		}
		int width = 0;
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) == '@' && i + 4 < s.length() && s.charAt(i + 4) == '@') {
				i += 4;
			} else {
				width += charWidth[s.charAt(i)];
			}
		}
		return width;
	}

	private void drawTypeface(byte[] mask, int x, int y, int width, int height, int color) {
		int destPos = x + y * Rasterizer2D.canvasWidth;
		int destOffset = Rasterizer2D.canvasWidth - width;
		int maskOffset = 0;
		int maskPos = 0;
		if(y < Rasterizer2D.clipStartY) {
			final int d = Rasterizer2D.clipStartY - y;
			height -= d;
			y = Rasterizer2D.clipStartY;
			maskPos += d * width;
			destPos += d * Rasterizer2D.canvasWidth;
		}
		if(y + height >= Rasterizer2D.clipEndY) {
			height -= y + height - Rasterizer2D.clipEndY;
		}
		if(x < Rasterizer2D.clipStartX) {
			final int d = Rasterizer2D.clipStartX - x;
			width -= d;
			x = Rasterizer2D.clipStartX;
			maskPos += d;
			destPos += d;
			maskOffset += d;
			destOffset += d;
		}
		if(x + width >= Rasterizer2D.clipEndX) {
			final int d = x + width - Rasterizer2D.clipEndX;
			width -= d;
			maskOffset += d;
			destOffset += d;
		}
		if(width > 0 && height > 0) {
			copyRaster(mask, Rasterizer2D.canvasRaster, maskPos, destPos, maskOffset, destOffset, width, height, color);
		}
	}

	private void copyRaster(byte[] mask, int[] dest, int maskPos, int destPos, int maskOffset, int destOffset, int width, int height, int color) {
		final int l1 = width >> 2;
		width &= 3;
		for(int i2 = 0; i2 < height; i2++) {
			for(int j2 = 0; j2 < l1; j2++) {
				if(mask[maskPos++] != 0) {
					dest[destPos] = color;
				}
				destPos++;
				if(mask[maskPos++] != 0) {
					dest[destPos] = color;
				}
				destPos++;
				if(mask[maskPos++] != 0) {
					dest[destPos] = color;
				}
				destPos++;
				if(mask[maskPos++] != 0) {
					dest[destPos] = color;
				}
				destPos++;
			}
			for(int k2 = 0; k2 < width; k2++) {
				if(mask[maskPos++] != 0) {
					dest[destPos] = color;
				}
				destPos++;
			}
			destPos += destOffset;
			maskPos += maskOffset;
		}
	}

	private void drawTypeface(byte[] mask, int x, int y, int width, int height, int color, int alpha) {
		int destPos = x + y * Rasterizer2D.canvasWidth;
		int destOffset = Rasterizer2D.canvasWidth - width;
		int maskOffset = 0;
		int maskPos = 0;
		if(y < Rasterizer2D.clipStartY) {
			int d = Rasterizer2D.clipStartY - y;
			height -= d;
			y = Rasterizer2D.clipStartY;
			maskPos += d * canvasWidth;
			destPos += d * Rasterizer2D.canvasWidth;
		}
		if(y + height >= Rasterizer2D.clipEndY) {
			height -= ((y + height) - Rasterizer2D.clipEndY);
		}
		if(x < Rasterizer2D.clipStartX) {
			int d = Rasterizer2D.clipStartX - x;
			width -= d;
			x = Rasterizer2D.clipStartX;
			maskPos += d;
			destPos += d;
			maskOffset += d;
			destOffset += d;
		}
		if(x + width >= Rasterizer2D.clipEndX) {
			int d = ((x + width) - Rasterizer2D.clipEndX);
			width -= d;
			maskOffset += d;
			destOffset += d;
		}
		if(width > 0 && height > 0) {
			copyRaster(mask, Rasterizer2D.canvasRaster, maskPos, destPos, maskOffset, destOffset, width, height, color, alpha);
		}
	}

	private void copyRaster(byte[] mask, int[] dest, int maskPos, int destPos, int maskOffset, int destOffset, int width, int height, int color, int alpha) {
		color = ((color & 0xff00ff) * alpha & 0xff00ff00) + ((color & 0xff00) * alpha & 0xff0000) >> 8;
		alpha = 256 - alpha;
		for(int i2 = 0; i2 < height; i2++) {
			for(int k2 = 0; k2 < width; k2++) {
				if(mask[maskPos++] != 0) {
					final int areaPixelColor = dest[destPos];
					dest[destPos] = (((areaPixelColor & 0xff00ff) * alpha & 0xff00ff00) + ((areaPixelColor & 0xff00) * alpha & 0xff0000) >> 8) + color;
				}
				destPos++;
			}
			destPos += destOffset;
			maskPos += maskOffset;
		}
	}

	public void drawTooltip(String s, int x, int y, int color, int seed) {
		if(s == null) {
			return;
		}
		tooltipRandom.setSeed(seed);
		int alpha = 192;
		if(Constants.ANTI_BOT_ENABLED) {
			alpha += (tooltipRandom.nextInt() & 0x1f);
		}
		y -= lineHeight;
		for(int index = 0; index < s.length(); index++) {
			if(s.charAt(index) == '@' && index + 4 < s.length() && s.charAt(index + 4) == '@') {
				final int l1 = getEffect(s.substring(index + 1, index + 4));
				if(l1 != -1) {
					color = l1;
				}
				index += 4;
			} else {
				final char c = s.charAt(index);
				if(c != ' ') {
					drawTypeface(typefaceMask[c], x + typefaceHorizontalOffset[c] + 1, y + typefaceVerticalOffset[c] + 1, typefaceWidth[c], typefaceHeight[c], 0, 192);
					drawTypeface(typefaceMask[c], x + typefaceHorizontalOffset[c], y + typefaceVerticalOffset[c], typefaceWidth[c], typefaceHeight[c], color, alpha);
				}
				x += charWidth[c];
				if(Constants.ANTI_BOT_ENABLED) {
					if((tooltipRandom.nextInt() & 3) == 0) {
						x++;
					}
				}
			}
		}
	}
}
