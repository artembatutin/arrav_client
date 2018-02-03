package net.arrav.graphic.tex;

import net.arrav.net.OnDemandFetcher;
import net.arrav.util.io.Buffer;

public final class Texture {

	private static int TEX_SIZE = 1500;
	private static Texture[] cache = new Texture[TEX_SIZE];
	private static OnDemandFetcher resourceManager;
	private static float brightness = 1;

	private final int width;
	private final int height;
	private final int[] texels;
	private int[][] mipmaps;

	private Texture(int width, int height, int[] texels) {
		this.width = width;
		this.height = height;
		this.texels = texels;
	}

	public static Texture get(int index) {
		if(cache[index] == null) {
			resourceManager.addRequest(4, index);
			return null;
		}
		return cache[index];
	}

	public static void clear() {
		cache = new Texture[TEX_SIZE];
	}

	public static void init(OnDemandFetcher updateManager_) {
		resourceManager = updateManager_;
	}

	public static void decode(byte[] data, int index) {
		if(data != null && data.length >= 5) {
			Buffer buf = new Buffer(data);
			int type = buf.getUByte();
			int width = buf.getUShort();
			int height = buf.getUShort();
			int[] texels = new int[width * height];
			if(type == 0) {
				int[] palette = new int[buf.getUByte() + 1];
				for(int i = 0, n = palette.length - 1; i != n; i++) {
					palette[i + 1] = buf.getUMedium();
				}
				for(int i = 0, n = width * height; i != n; i++) {
					int indx = buf.getUByte();
					if(indx != 0) {
						texels[i] = palette[indx];
					}
				}
			} else if(type == 1) {
				for(int i = 0, n = width * height; i != n; i++) {
					texels[i] = buf.getUMedium();
				}
			} else if(type == 2) {
				int[] palette = new int[buf.getUByte() + 1];
				for(int i = 0, n = palette.length - 1; i != n; i++) {
					palette[i + 1] = buf.getUMedium();
				}
				int n = width * height;
				byte[] indices = new byte[n];
				for(int i = 0; i != n; i++) {
					indices[i] = buf.getSByte();
				}
				for(int i = 0; i != n; i++) {
					int alpha = buf.getUByte();
					if(alpha != 0 && indices[i] != 0) {
						texels[i] = (alpha << 24) | palette[indices[i] & 0xff];
					}
				}
			} else if(type == 3) {
				for(int i = 0, n = width * height; i != n; i++) {
					int argb = buf.getInt();
					if((argb & 0xff000000) == 0) {
						argb = 0;
					}
					texels[i] = argb;
				}
			}
			cache[index] = new Texture(width, height, texels);
		}
	}

	public static void setBrightness(float brightness) {
		Texture.brightness = brightness;
	}

	private static int adjustBrightness(int rgb, float value) {
		double red = (rgb >> 16) / 256D;
		double green = (rgb >> 8 & 0xff) / 256D;
		double blue = (rgb & 0xff) / 256D;
		red = Math.pow(red, value);
		green = Math.pow(green, value);
		blue = Math.pow(blue, value);
		final int red2 = (int) (red * 256D);
		final int green2 = (int) (green * 256D);
		final int blue2 = (int) (blue * 256D);
		return (red2 << 16) + (green2 << 8) + blue2;
	}

	private int[][] generate() {
		int shift = 0;
		if(width <= 1 && height <= 1) {
			shift = 7;
		} else if(width <= 2 && height <= 2) {
			shift = 6;
		} else if(width <= 4 && height <= 4) {
			shift = 5;
		} else if(width <= 8 && height <= 8) {
			shift = 4;
		} else if(width <= 16 && height <= 16) {
			shift = 3;
		} else if(width <= 32 && height <= 32) {
			shift = 2;
		} else if(width <= 64 && height <= 64) {
			shift = 1;
		}
		int[][] mipmaps = new int[8][];
		int[] size128 = mipmaps[0] = new int[16384];
		for(int x = 0; x < 128; x++) {
			for(int y = 0; y < 128; y++) {
				int tx = x >> shift;
				int ty = y >> shift;
				if(tx < width && ty < height) {
					int rgb = texels[tx + ty * width];
					/*if(rgb == 0) {
						scattered = true;
					}*/
					size128[x + (y << 7)] = adjustBrightness(rgb, Texture.brightness) & 0xf8f8ff;
				}// else if(!scattered) {
				//scattered = true;
				//}
			}
		}
		for(int level = 1, size = 64; level < 8; level++) {
			int[] src = mipmaps[level - 1];
			int[] dst = mipmaps[level] = new int[size * size];
			for(int x = 0; x < size; x++) {
				for(int y = 0; y < size; y++) {
					double r = 0, g = 0, b = 0;
					int count = 0;
					for(int rgb : new int[]{src[x + (y * size << 1) << 1], src[(x + (y * size << 1) << 1) + 1], src[(x + (y * size << 1) << 1) + (size << 1)], src[(x + (y * size << 1) << 1) + (size << 1) + 1]}) {
						if(rgb != 0) {
							double dr = (rgb >> 16 & 0xff) / 255d;
							double dg = (rgb >> 8 & 0xff) / 255d;
							double db = (rgb & 0xff) / 255d;
							r += dr * dr;
							g += dg * dg;
							b += db * db;
							count++;
						}
					}
					if(count != 0) {
						int ri = Math.round(255 * (float) Math.sqrt(r / count));
						int gi = Math.round(255 * (float) Math.sqrt(g / count));
						int bi = Math.round(255 * (float) Math.sqrt(b / count));
						dst[x + y * size] = ri << 16 | gi << 8 | bi;
					}
				}
			}
			size >>= 1;
		}
		return mipmaps;
	}

	public int[] getTexels(int level) {
		if(level == 0)
			return texels;
		else if(mipmaps == null) {
			mipmaps = generate();
		}
		return mipmaps[level];
	}

	public String toString() {
		return "width: " + width + " - height: " + height;
	}

}
