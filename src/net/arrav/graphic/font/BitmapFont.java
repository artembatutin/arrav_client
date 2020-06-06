package net.arrav.graphic.font;

import net.arrav.Client;
import net.arrav.cache.CacheArchive;
import net.arrav.graphic.Rasterizer2D;
import net.arrav.graphic.img.BitmapImage;
import net.arrav.util.io.Buffer;

import java.awt.*;


public final class BitmapFont extends Rasterizer2D {

	public int lineHeight = 0;
	public int[] typefaceVerticalOffset;
	public int[] typefaceHeight;
	public int[] typefaceHorizontalOffset;
	public int[] typefaceWidth;
	public byte[][] typefaceMask;
	public int[] charWidth;
	private boolean strikethrought;

	public static String startImage;
	public static String aRSString_4135;
	public static String startTransparency;
	public static String startDefaultShadow;
	public static String endShadow = "/shad";
	public static String endEffect;
	public static String aRSString_4143;
	public static String endStrikethrough = "/str";
	public static String aRSString_4147;
	public static String startColor;
	public static String lineBreak;
	public static String startStrikethrough;
	public static String endColor;
	public static String endUnderline;
	public static String defaultStrikethrough;
	public static String startShadow;
	public static String startEffect;
	public static String aRSString_4162;
	public static String aRSString_4163;
	public static String endTransparency;
	public static String aRSString_4165;
	public static String startUnderline;
	public static String startDefaultUnderline;
	public static String aRSString_4169;
	public static String[] splitTextStrings;
	public static int defaultColor;
	public static int textShadowColor;
	public static int strikethroughColor;
	public static int defaultTransparency;
	public static int anInt4175;
	public static int underlineColor;
	public static int defaultShadow;
	public static int anInt4178;
	public static int transparency;
	public static int textColor;

	public BitmapFont(CacheArchive archive, String filename, boolean largeSpaces) {
		try {
			int length = (filename.equals("hit_full") || filename.equals("critical_full")) ? 58 : 256;
			typefaceMask = new byte[length][];
			typefaceWidth = new int[length];
			typefaceHeight = new int[length];
			typefaceHorizontalOffset = new int[length];
			typefaceVerticalOffset = new int[length];
			charWidth = new int[length];
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
				drawTypeface(c, x + typefaceHorizontalOffset[c], y + typefaceVerticalOffset[c], typefaceWidth[c], typefaceHeight[c], color);
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
				drawTypefaceTrans(c, x + typefaceHorizontalOffset[c], y + typefaceVerticalOffset[c], typefaceWidth[c], typefaceHeight[c], color, alpha);
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

	public void drawLeftAlignedEffectString(String string, int drawX, int drawY, int color, boolean shadow) {
		if(string == null)
			return;
		setColorAndShadow(color, defaultShadow);
		try {
			drawY -= lineHeight;
			for (int currentCharacter = 0; currentCharacter < string.length(); currentCharacter++) {
				int character = string.charAt(currentCharacter);
				if (character > 255) {
					character = 32;
				}

				if (character == '<') {
					int end = string.indexOf(">", currentCharacter);
					if (end != currentCharacter + 1) {
						if (end > -1 && end < string.length()) {
							String effect = string.substring(currentCharacter + 1, end);

							if (effect.startsWith(startImage)) {
								int idx = 0;
								for (int index = effect.length() - 1; index >= 4; index--) {
									int num = effect.charAt(index) - 48;
									int pow = 10 * (effect.length() - 1 - index);
									if (pow > 0)
										idx += num * pow;
									else
										idx += num;
								}
								try {
									BitmapImage icon = Client.spriteCache.get(idx);
									icon.drawImage(drawX, drawY);
									drawX += icon.imageWidth+ icon.xOffset;
								}catch (Exception e) {
									e.printStackTrace();
									System.out.println("Error getting image for text String "+string);
								}
								} else if (!setTextEffects(effect)) {
								end = -1;
							}
						}
						if (end > -1) {
							currentCharacter = end;
							continue;
						}
					}
				}

				if (character == '@' && currentCharacter + 4 < string.length()
						&& string.charAt(currentCharacter + 4) == '@') {
					textColor = getEffect(string.substring(currentCharacter + 1, currentCharacter + 4));
					currentCharacter += 4;
					continue;
				}

				int width = typefaceWidth[character];
				int height = typefaceHeight[character];
				if (character != 32) {
					if (transparency == 256) {
						if (textShadowColor != -1) {
							drawTypeFaceNew(character, drawX + typefaceHorizontalOffset[character] + 1,
									drawY + typefaceVerticalOffset[character] + 1, width, height, textShadowColor, true);
						}
						drawTypeFaceNew(character, drawX + typefaceHorizontalOffset[character],
								drawY + typefaceVerticalOffset[character], width, height, textColor, false);
					} else {
						if (textShadowColor != -1) {
							drawTypefaceTrans(character, drawX + typefaceHorizontalOffset[character] + 1,
									drawY + typefaceVerticalOffset[character] + 1, width, height, textShadowColor,
									transparency);
						}
						drawTypefaceTrans(character, drawX + typefaceHorizontalOffset[character],
								drawY + typefaceVerticalOffset[character], width, height, textColor, transparency);
					}
				} else if (anInt4178 > 0) {
					anInt4175 += anInt4178;
					drawX += anInt4175 >> 8;
					anInt4175 &= 0xff;
				}
				int lineWidth = charWidth[character];
				if (strikethroughColor != -1) {
					drawHorizontalLine(drawX, strikethroughColor, lineWidth,
							drawY + (int) ((double) lineHeight * 0.69999999999999996D));
				}
				if (underlineColor != -1) {
					drawHorizontalLine(drawX, underlineColor, lineWidth, drawY + lineHeight + 3);
				}
				drawX += lineWidth;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		/*strikethrought = false;
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
						drawTypeface(c, x + typefaceHorizontalOffset[c] + 1, y + typefaceVerticalOffset[c] + 1, typefaceWidth[c], typefaceHeight[c], 0);
					}
					drawTypeface(c, x + typefaceHorizontalOffset[c], y + typefaceVerticalOffset[c], typefaceWidth[c], typefaceHeight[c], color);
				}
				x += charWidth[c];
			}
		}
		if(strikethrought) {
			Rasterizer2D.drawHorizontalLine(l, y + (int) (lineHeight * 0.69999999999999996D), x - l, 0x800000);
		}*/
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
				drawTypeface(c, x + typefaceHorizontalOffset[c], y + typefaceVerticalOffset[c] + (int) (Math.sin(i1 / 2D + frame / 5D) * 5D), typefaceWidth[c], typefaceHeight[c], color);
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
				drawTypeface(c, x + typefaceHorizontalOffset[c] + (int) (Math.sin(i1 / 5D + frame / 5D) * 5D), y + typefaceVerticalOffset[c] + (int) (Math.sin(i1 / 3D + frame / 5D) * 5D), typefaceWidth[c], typefaceHeight[c], color);
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
				drawTypeface(c, x + typefaceHorizontalOffset[c], y + typefaceVerticalOffset[c] + (int) (Math.sin(k1 / 1.5D + frame) * d), typefaceWidth[c], typefaceHeight[c], color);
			}
			x += charWidth[c];
		}
	}

	public void drawBaseStringMoveXY(String string, int drawX, int drawY, int[] xModifier, int[] yModifier) {
		drawY -= lineHeight;
		int modifierOffset = 0;
		for (int currentCharacter = 0; currentCharacter < string.length(); currentCharacter++) {
			int character = string.charAt(currentCharacter);
			int width = typefaceWidth[character];
			int height = typefaceHeight[character];
			int xOff;
			if (xModifier != null) {
				xOff = xModifier[modifierOffset];
			} else {
				xOff = 0;
			}
			int yOff;
			if (yModifier != null) {
				yOff = yModifier[modifierOffset];
			} else {
				yOff = 0;
			}
			modifierOffset++;

			if (character != 32) {
				if (transparency == 256) {
					if (textShadowColor != -1) {
						drawTypeFaceNew(character, (drawX + typefaceHorizontalOffset[character] + 1 + xOff),
								(drawY + typefaceVerticalOffset[character] + 1 + yOff), width, height, textShadowColor,
								true);
					}
					drawTypeFaceNew(character, drawX + typefaceHorizontalOffset[character] + xOff,
							drawY + typefaceVerticalOffset[character] + yOff, width, height, textColor, false);
				} else {
					if (textShadowColor != -1) {
						drawTypefaceTrans(character, (drawX + typefaceHorizontalOffset[character] + 1 + xOff),
								(drawY + typefaceVerticalOffset[character] + 1 + yOff), width, height, textShadowColor,
								transparency);
					}
					drawTypefaceTrans(character, drawX + typefaceHorizontalOffset[character] + xOff,
							drawY + typefaceVerticalOffset[character] + yOff, width, height, textColor, transparency);
				}
			} else if (anInt4178 > 0) {
				anInt4175 += anInt4178;
				drawX += anInt4175 >> 8;
				anInt4175 &= 0xff;
			}
			int i_109_ = charWidth[character];
			if (strikethroughColor != -1) {
				drawHorizontalLine(drawX, strikethroughColor, i_109_,
						drawY + (int) ((double) lineHeight * 0.7));
			}
			if (underlineColor != -1) {
				drawHorizontalLine(drawX, underlineColor, i_109_, drawY + lineHeight);
			}
			drawX += i_109_;
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
		if(code.equals("lye")) {
			return 0xFFFF64;
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

	public int getEffectStringWidth(String string) {
		if (string == null) {
			return 0;
		}
		int finalWidth = 0;
		for (int currentCharacter = 0; currentCharacter < string.length(); currentCharacter++) {
			int character = string.charAt(currentCharacter);
			if (character > 255) {
				character = 32;
			}

			if (character == '<') {
				int end = string.indexOf(">", currentCharacter);
				if (end != currentCharacter + 1) {
					if (end > -1 && end != currentCharacter + 1 && end < string.length()) {
						String effect = string.substring(currentCharacter + 1, end);

						if (effect.startsWith(startImage)) {
							finalWidth += 11;
						}

						currentCharacter = end;
						continue;
					}
				}
			}

			if (character == '@' && currentCharacter + 4 < string.length()
					&& string.charAt(currentCharacter + 4) == '@') {
				textColor = getEffect(string.substring(currentCharacter + 1, currentCharacter + 4));
				currentCharacter += 4;
				continue;
			}

			finalWidth += charWidth[character];
		}
		return finalWidth;
	}


	/*public int getEffectStringWidth(String s) {
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
	}*/
	public int getCharacterWidth(int i) {
		return charWidth[i & 0xff];
	}


	private void drawTypeface(int character, int x, int y, int width, int height, int color) {
		int destPos = x + y * Rasterizer2D.canvasWidth;
		int destOffset = Rasterizer2D.canvasWidth - width;
		int maskOffset = 0;
		int maskPos = 0;
		if(y < Rasterizer2D.clipStartY) {
			final int offsetY = Rasterizer2D.clipStartY - y;
			height -= offsetY;
			y = Rasterizer2D.clipStartY;
			maskPos += offsetY * width;
			destPos += offsetY * Rasterizer2D.canvasWidth;
		}
		if(y + height >= Rasterizer2D.clipEndY) {
			height -= y + height - Rasterizer2D.clipEndY;
		}
		if(x < Rasterizer2D.clipStartX) {
			final int offsetX = Rasterizer2D.clipStartX - x;
			width -= offsetX;
			x = Rasterizer2D.clipStartX;
			maskPos += offsetX;
			destPos += offsetX;
			maskOffset += offsetX;
			destOffset += offsetX;
		}
		if(x + width >= Rasterizer2D.clipEndX) {
			final int d = x + width - Rasterizer2D.clipEndX;
			width -= d;
			maskOffset += d;
			destOffset += d;
		}
		if(width > 0 && height > 0) {
			copyRaster(typefaceMask[character], canvasRaster, maskPos, destPos, maskOffset, destOffset, width, height, color);
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

	private void drawTypefaceTrans(int character, int x, int y, int width, int height, int color, int alpha) {
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
			copyRasterTrans(typefaceMask[character], Rasterizer2D.canvasRaster, maskPos, destPos, maskOffset, destOffset, width, height, color, alpha);
		}
	}

	private void copyRasterTrans(byte[] mask, int[] dest, int maskPos, int destPos,
								 int maskOffset, int destOffset, int width, int height, int color, int alpha) {
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

	public static void copyRasterNew(int[] is, byte[] is_24_, int i, int i_25_, int i_26_, int i_27_, int i_28_,
									 int i_29_, int i_30_) {
		int i_31_ = -(i_27_ >> 2);
		i_27_ = -(i_27_ & 0x3);
		for (int i_32_ = -i_28_; i_32_ < 0; i_32_++) {
			for (int i_33_ = i_31_; i_33_ < 0; i_33_++) {
				if (is_24_[i_25_++] != 0) {
					is[i_26_++] = i;
				} else {
					i_26_++;
				}
				if (is_24_[i_25_++] != 0) {
					is[i_26_++] = i;
				} else {
					i_26_++;
				}
				if (is_24_[i_25_++] != 0) {
					is[i_26_++] = i;
				} else {
					i_26_++;
				}
				if (is_24_[i_25_++] != 0) {
					is[i_26_++] = i;
				} else {
					i_26_++;
				}
			}
			for (int i_34_ = i_27_; i_34_ < 0; i_34_++) {
				if (is_24_[i_25_++] != 0) {
					is[i_26_++] = i;
				} else {
					i_26_++;
				}
			}
			i_26_ += i_29_;
			i_25_ += i_30_;
		}
	}

	public void drawTypeFaceNew(int character, int x, int y, int width, int height, int shadowColor, boolean bool) {
		int i_40_ = x + y * canvasWidth;
		int i_41_ = canvasWidth - width;
		int i_42_ = 0;
		int i_43_ = 0;
		if (y < clipStartY) {
			int i_44_ = clipStartY - y;
			height -= i_44_;
			y = clipStartY;
			i_43_ += i_44_ * width;
			i_40_ += i_44_ * canvasWidth;
		}
		if (y + height > clipEndY) {
			height -= y + height - clipEndY;
		}
		if (x < clipStartX) {
			int i_45_ = clipStartX - x;
			width -= i_45_;
			x = clipStartX;
			i_43_ += i_45_;
			i_40_ += i_45_;
			i_42_ += i_45_;
			i_41_ += i_45_;
		}
		if (x + width > clipEndX) {
			int i_46_ = x + width - clipEndX;
			width -= i_46_;
			i_42_ += i_46_;
			i_41_ += i_46_;
		}
		if (width > 0 && height > 0) {
			copyRasterNew(canvasRaster, typefaceMask[character], shadowColor, i_43_, i_40_, width, height, i_41_,
					i_42_);
		}
	}


	public void drawTooltip(String s, int x, int y, int color, int seed) {
		if(s == null) {
			return;
		}
		int alpha = 192;
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
					if(c >= typefaceMask.length)
						return;
					if(c >= typefaceHorizontalOffset.length)
						return;
					if(c >= typefaceVerticalOffset.length)
						return;
					if(c >= typefaceWidth.length)
						return;
					if(c >= typefaceHeight.length)
						return;
					drawTypefaceTrans(c, x + typefaceHorizontalOffset[c] + 1, y + typefaceVerticalOffset[c] + 1, typefaceWidth[c], typefaceHeight[c], 0, 192);
					drawTypefaceTrans(c, x + typefaceHorizontalOffset[c], y + typefaceVerticalOffset[c], typefaceWidth[c], typefaceHeight[c], color, alpha);
				}
				x += charWidth[c];
			}
		}
	}

	public void setColorAndShadow(int color, int shadow) {
		strikethroughColor = -1;
		underlineColor = -1;
		textShadowColor = defaultShadow = shadow;
		textColor = defaultColor = color;
		transparency = defaultTransparency = 256;
		anInt4178 = 0;
		anInt4175 = 0;
	}

	public boolean setTextEffects(String string) {
		try {
			if (string.startsWith(startColor)) {
				String color = string.substring(4);
				textColor = color.length() < 6 ? Color.decode(color).getRGB() : Integer.parseInt(color, 16);
				return true;
			} else if (string.equals(endColor)) {
				textColor = defaultColor;
				return true;
			} else if (string.startsWith(startTransparency)) {
				transparency = Integer.valueOf(string.substring(6));
				return true;
			} else if (string.equals(endTransparency)) {
				transparency = defaultTransparency;
				return true;
			} else if (string.startsWith(startStrikethrough)) {
				strikethroughColor = Integer.valueOf(string.substring(4));
				return true;
			} else if (string.equals(defaultStrikethrough)) {
				strikethroughColor = 8388608;
				return true;
			} else if (string.equals(endStrikethrough)) {
				strikethroughColor = -1;
				return true;
			} else if (string.startsWith(startUnderline)) {
				underlineColor = Integer.valueOf(string.substring(2));
				return true;
			} else if (string.equals(startDefaultUnderline)) {
				underlineColor = 0;
				return true;
			} else if (string.equals(endUnderline)) {
				underlineColor = -1;
				return true;
			} else if (string.startsWith(startShadow)) {
				textShadowColor = Integer.valueOf(string.substring(5));
				return true;
			} else if (string.equals(startDefaultShadow)) {
				textShadowColor = 0;
				return true;
			} else if (string.equals(endShadow)) {
				textShadowColor = defaultShadow;
				return true;
			} else if (string.equals(lineBreak)) {
				setDefaultTextEffectValues(defaultColor, defaultShadow, defaultTransparency);
				return true;
			}
		}catch (Exception e) {
			//e.printStackTrace();
			//System.out.println("ERROR: "+string);
		}
		return false;
	}

	public void setTrans(int shadow, int color, int trans) {
		textShadowColor = defaultShadow = shadow;
		textColor = defaultColor = color;
		transparency = defaultTransparency = trans;
	}

	public void setDefaultTextEffectValues(int color, int shadow, int trans) {
		strikethroughColor = -1;
		underlineColor = -1;
		textShadowColor = defaultShadow = shadow;
		textColor = defaultColor = color;
		transparency = defaultTransparency = trans;
		anInt4178 = 0;
		anInt4175 = 0;
	}

	static {
		startTransparency = "trans=";
		startStrikethrough = "str=";
		startDefaultShadow = "shad";
		startColor = "col=";
		lineBreak = "br";
		defaultStrikethrough = "str";
		endUnderline = "/u";
		startImage = "img=";
		startShadow = "shad=";
		startUnderline = "u=";
		endColor = "/col";
		startDefaultUnderline = "u";
		endTransparency = "/trans";
		aRSString_4143 = Integer.toString(100);
		aRSString_4135 = "nbsp";
		aRSString_4169 = "reg";
		aRSString_4165 = "times";
		aRSString_4162 = "shy";
		aRSString_4163 = "copy";
		endEffect = "gt";
		aRSString_4147 = "euro";
		startEffect = "lt";
		defaultTransparency = 256;
		defaultShadow = -1;
		anInt4175 = 0;
		textShadowColor = -1;
		textColor = 0;
		defaultColor = 0;
		strikethroughColor = -1;
		splitTextStrings = new String[100];
		underlineColor = -1;
		anInt4178 = 0;
		transparency = 256;
	}
}
