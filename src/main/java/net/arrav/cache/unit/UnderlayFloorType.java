package net.arrav.cache.unit;

import net.arrav.Constants;
import net.arrav.util.io.Buffer;
import net.arrav.cache.CacheArchive;

import java.util.Arrays;

public final class UnderlayFloorType {

	public static UnderlayFloorType[] cache;

	private int rgb;
	public int texture;
	private boolean occlude;
	private int hue;
	public int saturation;
	public int lightness;
	public int hueAdj;
	public int brightness;
	private int hsl;

	public static int[] FLOOR_TEXTURE = {
			-1, 600, 301, 301, -1, -1, -1, 505, -1, 441,//0-9
			-1, -1, -1, 512, -1, 154, -1, -1, -1, -1,//10-19
			-1, 154, -1, -1, -1, 512, -1, -1, -1, -1,//20-29
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1,//30-39
			-1, -1, -1, -1, -1, -1, -1, 154, 154, 154,//40-49
			154, 154, 154, 154, 154, 154, 154, 598, 598, -1,//50-59
			154, 154, 154, 154, 154, 154, 154, 154, -1, -1,//60-69
			-1, 154, -1, -1, -1, -1, 526, 512, -1, 598,//70-79
			512, -1, 512, -1, 616, -1, -1, 512, 512, 333,//80-89
			-1, -1, 154, 492, -1, 154, -1, 492, -1, -1,//90-99
			-1, 596, 593, -1, -1, -1, -1, -1, -1, -1,//100-109
			-1, 1, -1, -1, -1, -1, 597, -1, 595, -1,//110-119
			-1, -1, 154, -1, -1, -1, -1, -1, -1, -1,//120-129
			-1, -1, -1, -1, -1, -1, 608, -1, -1, -1,//130-139
			-1, -1, -1, -1, 154, 154, 154, 154, 154, 154,//140-149
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1,//150-159
			-1, -1, 595, -1, -1, -1, -1, -1, -1, -1,//160-169
			505, 512, 512, -1//170-173
	};

	private UnderlayFloorType() {
		texture = -1;
		occlude = true;
	}

	public static void unpack(CacheArchive archive) {
		final Buffer buffer = new Buffer(archive.getFile("flo.dat"));
		final int length = buffer.getUShort();
		System.out.println("[loading] flo size: " + length);
		if(cache == null) {
			cache = new UnderlayFloorType[length];
		}
		for(int i = 0; i < length; i++) {
			if(cache[i] == null) {
				cache[i] = new UnderlayFloorType();
			}
			cache[i].read(buffer);
			Arrays.fill(FLOOR_TEXTURE, 512);
			if(FLOOR_TEXTURE[i] != -1) {
				cache[i].texture = FLOOR_TEXTURE[i];
			}
			/*if(i == 152) {
				cache[i].texture = 9;
			} else if(i == 41) {
				cache[i].texture = 58;
			} else if(i == 94) {
				cache[i].texture = 529;
			}*/
		}
		FLOOR_TEXTURE = null;
	}

	private void read(Buffer buffer) {
		do {
			final int code = buffer.getUByte();
			if(code == 0) {
				return;
			} else if(code == 1) {
				rgb = buffer.getUMedium();
				calculateValues(rgb);
			} else if(code == 2) {
				texture =
						buffer.getUShort();
			} else if(code == 3) {
				buffer.getUShort();
			} else if(code == 4) {
				//something to do with shadows lol
			} else if(code == 5) {
				occlude = false;
			} else if(code == 6) {
				buffer.getLine();
			} else if(code == 7) {
				final int h = hue;
				final int s = saturation;
				final int l = lightness;
				final int i1 = hueAdj;
				final int c = buffer.getUMedium();
				calculateValues(c);
				hue = h;
				saturation = s;
				lightness = l;
				hueAdj = i1;
				brightness = i1;
			} else {
				System.out.println("Error unrecognised flo config code: " + code);
			}
		} while(true);
	}

	private void calculateValues(int color) {
		final double red = (color >> 16 & 0xff) / 256D;
		final double green = (color >> 8 & 0xff) / 256D;
		final double blue = (color & 0xff) / 256D;
		double min = red;
		if(green < min) {
			min = green;
		}
		if(blue < min) {
			min = blue;
		}
		double max = red;
		if(green > max) {
			max = green;
		}
		if(blue > max) {
			max = blue;
		}
		double h = 0.0D;
		double s = 0.0D;
		final double l = (min + max) / 2D;
		if(min != max) {
			if(l < 0.5D) {
				s = (max - min) / (max + min);
			}
			if(l >= 0.5D) {
				s = (max - min) / (2D - max - min);
			}
			if(red == max) {
				h = (green - blue) / (max - min);
			} else if(green == max) {
				h = 2D + (blue - red) / (max - min);
			} else if(blue == max) {
				h = 4D + (red - green) / (max - min);
			}
		}
		h /= 6D;
		hue = (int) (h * 256D);
		saturation = (int) (s * 256D);
		lightness = (int) (l * 256D);
		if(saturation < 0) {
			saturation = 0;
		} else if(saturation > 255) {
			saturation = 255;
		}
		if(lightness < 0) {
			lightness = 0;
		} else if(lightness > 255) {
			lightness = 255;
		}
		if(l > 0.5D) {
			brightness = (int) ((1.0D - l) * s * 512D);
		} else {
			brightness = (int) (l * s * 512D);
		}
		if(brightness < 1) {
			brightness = 1;
		}
		hueAdj = (int) (h * brightness);
		int hr = hue;
		if(Constants.ANTI_BOT_ENABLED) {
			hr += (int) (Math.random() * 16D) - 8;
		}
		if(hr < 0) {
			hr = 0;
		} else if(hr > 255) {
			hr = 255;
		}
		int sr = saturation;
		if(Constants.ANTI_BOT_ENABLED) {
			sr += (int) (Math.random() * 48D) - 24;
		}
		if(sr < 0) {
			sr = 0;
		} else if(sr > 255) {
			sr = 255;
		}
		int lr = lightness;
		if(Constants.ANTI_BOT_ENABLED) {
			lr += (int) (Math.random() * 48D) - 24;
		}
		if(lr < 0) {
			lr = 0;
		} else if(lr > 255) {
			lr = 255;
		}
		hsl = hslCode(hr, sr, lr);
	}

	public static int hslCode(int h, int s, int l) {
		if(l > 179) {
			s /= 2;
		}
		if(l > 192) {
			s /= 2;
		}
		if(l > 217) {
			s /= 2;
		}
		if(l > 243) {
			s /= 2;
		}
		return (h / 4 << 10) + (s / 32 << 7) + l / 2;
	}
	
	public static boolean exists(int id) {
		return !(id < 0 || id >= cache.length) && !(cache[id] == null);
	}
}