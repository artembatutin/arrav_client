package net.arrav.graphic;

import net.arrav.Config;
import net.arrav.Constants;
import net.arrav.world.Scene;
import net.arrav.cache.unit.MaterialType;
import net.arrav.graphic.tex.Texture;

public final class Rasterizer3D extends Rasterizer2D {
	
	public static float[] EMPTY_DEPTH_BUFFER;
	public static double[] TYPES = {
			3.8, //DEFAULT
			0.5, //PLAYER
			0, //NPC
			15, //OBJECT
			0, //FLOOR,
			120, //WALL DECOR
			1, //WALL
	};
	public static double renderType = TYPES[0];
	public static float[] depthBuffer;
	private static int textureMipmap;
	public static final int[] lightDecay;
	public static Viewport viewport;
	public static int alphaFilter;
	public static int[] angleSine;
	public static int[] angleCosine;
	public static int[] hslToRgbMap = new int[65536];
	public static boolean clippedScan;
	private static float brightness = 1.0F;
	private static int[] shadowDecay;
	private static int[][] texelArrayPool;
	private static int[][] texelCache;
	public static boolean textureMissing;
	public static boolean textured;
	
	static {
		shadowDecay = new int[512];
		lightDecay = new int[2048];
		angleSine = new int[2048];
		angleCosine = new int[2048];
		for(int i = 1; i < 512; i++) {
			shadowDecay[i] = 32768 / i;
		}
		for(int i = 1; i < 2048; i++) {
			lightDecay[i] = 65536 / i;
		}
		for(int angle = 0; angle < 2048; angle++) {
			angleSine[angle] = (int) (65536 * Math.sin(angle * 0.0030679614999999999));
			angleCosine[angle] = (int) (65536 * Math.cos(angle * 0.0030679614999999999));
		}
	}
	
	private static boolean depthRender(float z1, int offset) {
		return(z1 - renderType < depthBuffer[offset]);
	}
	
	public static void clearDepthBuffer() {
		if(depthBuffer == null || depthBuffer.length != Rasterizer2D.canvasRaster.length) {
			depthBuffer = new float[Rasterizer2D.canvasRaster.length];
			EMPTY_DEPTH_BUFFER = new float[Rasterizer2D.canvasRaster.length];
			for(int i = 0; i < depthBuffer.length; i++) {
				EMPTY_DEPTH_BUFFER[i] = Constants.CAM_FAR;
			}
		}
		System.arraycopy(EMPTY_DEPTH_BUFFER, 0, depthBuffer, 0, EMPTY_DEPTH_BUFFER.length);
	}
	
	public static void drawFog(int begin, int end, int rgb) {
		if(!Config.def.fog())
			return;
		float length = end - begin;
		for (int index = 0; index < canvasRaster.length; index++) {
			float factor = (depthBuffer[index] - begin) / length;
			canvasRaster[index] = blend(canvasRaster[index], rgb, factor);
		}
	}
	
	private static int blend(int c1, int c2, float factor) {
		if (factor >= 1f) {
			return c2;
		}
		if (factor <= 0f) {
			return c1;
		}
		int r1 = (c1 >> 16) & 0xff;
		int g1 = (c1 >> 8) & 0xff;
		int b1 = (c1) & 0xff;
		int r2 = (c2 >> 16) & 0xff;
		int g2 = (c2 >> 8) & 0xff;
		int b2 = (c2) & 0xff;
		int r3 = r2 - r1;
		int g3 = g2 - g1;
		int b3 = b2 - b1;
		int r = (int) (r1 + (r3 * factor));
		int g = (int) (g1 + (g3 * factor));
		int b = (int) (b1 + (b3 * factor));
		return (r << 16) + (g << 8) + b;
	}
	
	private static void drawGouraudTriangle317(int y1, int y2, int y3, int x1, int x2, int x3, int hsl1, int hsl2, int hsl3) {
		int dx1 = 0;
		int dhsl1 = 0;
		if(y2 != y1) {
			dx1 = (x2 - x1 << 16) / (y2 - y1);
			dhsl1 = (hsl2 - hsl1 << 15) / (y2 - y1);
		}
		int dx2 = 0;
		int dhsl2 = 0;
		if(y3 != y2) {
			dx2 = (x3 - x2 << 16) / (y3 - y2);
			dhsl2 = (hsl3 - hsl2 << 15) / (y3 - y2);
		}
		int dx3 = 0;
		int dhsl3 = 0;
		if(y3 != y1) {
			dx3 = (x1 - x3 << 16) / (y1 - y3);
			dhsl3 = (hsl1 - hsl3 << 15) / (y1 - y3);
		}
		if(y1 <= y2 && y1 <= y3) {
			if(y1 >= viewport.height) {
				return;
			}
			if(y2 > viewport.height) {
				y2 = viewport.height;
			}
			if(y3 > viewport.height) {
				y3 = viewport.height;
			}
			if(y2 < y3) {
				x3 = x1 <<= 16;
				hsl3 = hsl1 <<= 15;
				if(y1 < 0) {
					y1 -= 0;
					x3 -= dx3 * y1;
					x1 -= dx1 * y1;
					hsl3 -= dhsl3 * y1;
					hsl1 -= dhsl1 * y1;
					y1 = 0;
				}
				x2 <<= 16;
				hsl2 <<= 15;
				if(y2 < 0) {
					y2 -= 0;
					x2 -= dx2 * y2;
					hsl2 -= dhsl2 * y2;
					y2 = 0;
				}
				if(y1 != y2 && dx3 < dx1 || y1 == y2 && dx3 > dx2) {
					y3 -= y2;
					y2 -= y1;
					for(y1 = viewport.scanOffsets[y1]; --y2 >= 0; y1 += Rasterizer2D.canvasWidth) {
						drawGouraudScanline317(Rasterizer2D.canvasRaster, y1, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7);
						x3 += dx3;
						x1 += dx1;
						hsl3 += dhsl3;
						hsl1 += dhsl1;
					}
					while(--y3 >= 0) {
						drawGouraudScanline317(Rasterizer2D.canvasRaster, y1, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7);
						x3 += dx3;
						x2 += dx2;
						hsl3 += dhsl3;
						hsl2 += dhsl2;
						y1 += Rasterizer2D.canvasWidth;
					}
					return;
				}
				y3 -= y2;
				y2 -= y1;
				for(y1 = viewport.scanOffsets[y1]; --y2 >= 0; y1 += Rasterizer2D.canvasWidth) {
					drawGouraudScanline317(Rasterizer2D.canvasRaster, y1, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7);
					x3 += dx3;
					x1 += dx1;
					hsl3 += dhsl3;
					hsl1 += dhsl1;
				}
				while(--y3 >= 0) {
					drawGouraudScanline317(Rasterizer2D.canvasRaster, y1, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7);
					x3 += dx3;
					x2 += dx2;
					hsl3 += dhsl3;
					hsl2 += dhsl2;
					y1 += Rasterizer2D.canvasWidth;
				}
				return;
			}
			x2 = x1 <<= 16;
			hsl2 = hsl1 <<= 15;
			if(y1 < 0) {
				y1 -= 0;
				x2 -= dx3 * y1;
				x1 -= dx1 * y1;
				hsl2 -= dhsl3 * y1;
				hsl1 -= dhsl1 * y1;
				y1 = 0;
			}
			x3 <<= 16;
			hsl3 <<= 15;
			if(y3 < 0) {
				y3 -= 0;
				x3 -= dx2 * y3;
				hsl3 -= dhsl2 * y3;
				y3 = 0;
			}
			if(y1 != y3 && dx3 < dx1 || y1 == y3 && dx2 > dx1) {
				y2 -= y3;
				y3 -= y1;
				for(y1 = viewport.scanOffsets[y1]; --y3 >= 0; y1 += Rasterizer2D.canvasWidth) {
					drawGouraudScanline317(Rasterizer2D.canvasRaster, y1, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7);
					x2 += dx3;
					x1 += dx1;
					hsl2 += dhsl3;
					hsl1 += dhsl1;
				}
				while(--y2 >= 0) {
					drawGouraudScanline317(Rasterizer2D.canvasRaster, y1, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7);
					x3 += dx2;
					x1 += dx1;
					hsl3 += dhsl2;
					hsl1 += dhsl1;
					y1 += Rasterizer2D.canvasWidth;
				}
				return;
			}
			y2 -= y3;
			y3 -= y1;
			for(y1 = viewport.scanOffsets[y1]; --y3 >= 0; y1 += Rasterizer2D.canvasWidth) {
				drawGouraudScanline317(Rasterizer2D.canvasRaster, y1, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7);
				x2 += dx3;
				x1 += dx1;
				hsl2 += dhsl3;
				hsl1 += dhsl1;
			}
			while(--y2 >= 0) {
				drawGouraudScanline317(Rasterizer2D.canvasRaster, y1, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7);
				x3 += dx2;
				x1 += dx1;
				hsl3 += dhsl2;
				hsl1 += dhsl1;
				y1 += Rasterizer2D.canvasWidth;
			}
			return;
		}
		if(y2 <= y3) {
			if(y2 >= viewport.height) {
				return;
			}
			if(y3 > viewport.height) {
				y3 = viewport.height;
			}
			if(y1 > viewport.height) {
				y1 = viewport.height;
			}
			if(y3 < y1) {
				x1 = x2 <<= 16;
				hsl1 = hsl2 <<= 15;
				if(y2 < 0) {
					y2 -= 0;
					x1 -= dx1 * y2;
					x2 -= dx2 * y2;
					hsl1 -= dhsl1 * y2;
					hsl2 -= dhsl2 * y2;
					y2 = 0;
				}
				x3 <<= 16;
				hsl3 <<= 15;
				if(y3 < 0) {
					y3 -= 0;
					x3 -= dx3 * y3;
					hsl3 -= dhsl3 * y3;
					y3 = 0;
				}
				if(y2 != y3 && dx1 < dx2 || y2 == y3 && dx1 > dx3) {
					y1 -= y3;
					y3 -= y2;
					for(y2 = viewport.scanOffsets[y2]; --y3 >= 0; y2 += Rasterizer2D.canvasWidth) {
						drawGouraudScanline317(Rasterizer2D.canvasRaster, y2, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7);
						x1 += dx1;
						x2 += dx2;
						hsl1 += dhsl1;
						hsl2 += dhsl2;
					}
					
					while(--y1 >= 0) {
						drawGouraudScanline317(Rasterizer2D.canvasRaster, y2, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7);
						x1 += dx1;
						x3 += dx3;
						hsl1 += dhsl1;
						hsl3 += dhsl3;
						y2 += Rasterizer2D.canvasWidth;
					}
					return;
				}
				y1 -= y3;
				y3 -= y2;
				for(y2 = viewport.scanOffsets[y2]; --y3 >= 0; y2 += Rasterizer2D.canvasWidth) {
					drawGouraudScanline317(Rasterizer2D.canvasRaster, y2, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7);
					x1 += dx1;
					x2 += dx2;
					hsl1 += dhsl1;
					hsl2 += dhsl2;
				}
				
				while(--y1 >= 0) {
					drawGouraudScanline317(Rasterizer2D.canvasRaster, y2, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7);
					x1 += dx1;
					x3 += dx3;
					hsl1 += dhsl1;
					hsl3 += dhsl3;
					y2 += Rasterizer2D.canvasWidth;
				}
				return;
			}
			x3 = x2 <<= 16;
			hsl3 = hsl2 <<= 15;
			if(y2 < 0) {
				y2 -= 0;
				x3 -= dx1 * y2;
				x2 -= dx2 * y2;
				hsl3 -= dhsl1 * y2;
				hsl2 -= dhsl2 * y2;
				y2 = 0;
			}
			x1 <<= 16;
			hsl1 <<= 15;
			if(y1 < 0) {
				y1 -= 0;
				x1 -= dx3 * y1;
				hsl1 -= dhsl3 * y1;
				y1 = 0;
			}
			if(dx1 < dx2) {
				y3 -= y1;
				y1 -= y2;
				for(y2 = viewport.scanOffsets[y2]; --y1 >= 0; y2 += Rasterizer2D.canvasWidth) {
					drawGouraudScanline317(Rasterizer2D.canvasRaster, y2, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7);
					x3 += dx1;
					x2 += dx2;
					hsl3 += dhsl1;
					hsl2 += dhsl2;
				}
				while(--y3 >= 0) {
					drawGouraudScanline317(Rasterizer2D.canvasRaster, y2, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7);
					x1 += dx3;
					x2 += dx2;
					hsl1 += dhsl3;
					hsl2 += dhsl2;
					y2 += Rasterizer2D.canvasWidth;
				}
				return;
			}
			y3 -= y1;
			y1 -= y2;
			for(y2 = viewport.scanOffsets[y2]; --y1 >= 0; y2 += Rasterizer2D.canvasWidth) {
				drawGouraudScanline317(Rasterizer2D.canvasRaster, y2, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7);
				x3 += dx1;
				x2 += dx2;
				hsl3 += dhsl1;
				hsl2 += dhsl2;
			}
			
			while(--y3 >= 0) {
				drawGouraudScanline317(Rasterizer2D.canvasRaster, y2, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7);
				x1 += dx3;
				x2 += dx2;
				hsl1 += dhsl3;
				hsl2 += dhsl2;
				y2 += Rasterizer2D.canvasWidth;
			}
			return;
		}
		if(y3 >= viewport.height) {
			return;
		}
		if(y1 > viewport.height) {
			y1 = viewport.height;
		}
		if(y2 > viewport.height) {
			y2 = viewport.height;
		}
		if(y1 < y2) {
			x2 = x3 <<= 16;
			hsl2 = hsl3 <<= 15;
			if(y3 < 0) {
				y3 -= 0;
				x2 -= dx2 * y3;
				x3 -= dx3 * y3;
				hsl2 -= dhsl2 * y3;
				hsl3 -= dhsl3 * y3;
				y3 = 0;
			}
			x1 <<= 16;
			hsl1 <<= 15;
			if(y1 < 0) {
				y1 -= 0;
				x1 -= dx1 * y1;
				hsl1 -= dhsl1 * y1;
				y1 = 0;
			}
			if(dx2 < dx3) {
				y2 -= y1;
				y1 -= y3;
				for(y3 = viewport.scanOffsets[y3]; --y1 >= 0; y3 += Rasterizer2D.canvasWidth) {
					drawGouraudScanline317(Rasterizer2D.canvasRaster, y3, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7);
					x2 += dx2;
					x3 += dx3;
					hsl2 += dhsl2;
					hsl3 += dhsl3;
				}
				while(--y2 >= 0) {
					drawGouraudScanline317(Rasterizer2D.canvasRaster, y3, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7);
					x2 += dx2;
					x1 += dx1;
					hsl2 += dhsl2;
					hsl1 += dhsl1;
					y3 += Rasterizer2D.canvasWidth;
				}
				return;
			}
			y2 -= y1;
			y1 -= y3;
			for(y3 = viewport.scanOffsets[y3]; --y1 >= 0; y3 += Rasterizer2D.canvasWidth) {
				drawGouraudScanline317(Rasterizer2D.canvasRaster, y3, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7);
				x2 += dx2;
				x3 += dx3;
				hsl2 += dhsl2;
				hsl3 += dhsl3;
			}
			
			while(--y2 >= 0) {
				drawGouraudScanline317(Rasterizer2D.canvasRaster, y3, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7);
				x2 += dx2;
				x1 += dx1;
				hsl2 += dhsl2;
				hsl1 += dhsl1;
				y3 += Rasterizer2D.canvasWidth;
			}
			return;
		}
		x1 = x3 <<= 16;
		hsl1 = hsl3 <<= 15;
		if(y3 < 0) {
			y3 -= 0;
			x1 -= dx2 * y3;
			x3 -= dx3 * y3;
			hsl1 -= dhsl2 * y3;
			hsl3 -= dhsl3 * y3;
			y3 = 0;
		}
		x2 <<= 16;
		hsl2 <<= 15;
		if(y2 < 0) {
			y2 -= 0;
			x2 -= dx1 * y2;
			hsl2 -= dhsl1 * y2;
			y2 = 0;
		}
		if(dx2 < dx3) {
			y1 -= y2;
			y2 -= y3;
			for(y3 = viewport.scanOffsets[y3]; --y2 >= 0; y3 += Rasterizer2D.canvasWidth) {
				drawGouraudScanline317(Rasterizer2D.canvasRaster, y3, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7);
				x1 += dx2;
				x3 += dx3;
				hsl1 += dhsl2;
				hsl3 += dhsl3;
			}
			
			while(--y1 >= 0) {
				drawGouraudScanline317(Rasterizer2D.canvasRaster, y3, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7);
				x2 += dx1;
				x3 += dx3;
				hsl2 += dhsl1;
				hsl3 += dhsl3;
				y3 += Rasterizer2D.canvasWidth;
			}
			return;
		}
		y1 -= y2;
		y2 -= y3;
		for(y3 = viewport.scanOffsets[y3]; --y2 >= 0; y3 += Rasterizer2D.canvasWidth) {
			drawGouraudScanline317(Rasterizer2D.canvasRaster, y3, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7);
			x1 += dx2;
			x3 += dx3;
			hsl1 += dhsl2;
			hsl3 += dhsl3;
		}
		
		while(--y1 >= 0) {
			drawGouraudScanline317(Rasterizer2D.canvasRaster, y3, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7);
			x2 += dx1;
			x3 += dx3;
			hsl2 += dhsl1;
			hsl3 += dhsl3;
			y3 += Rasterizer2D.canvasWidth;
		}
	}
	
	public static void drawGouraudScanline317(int[] dest, int offset, int x1, int x2, int hsl1, int hsl2) {
		int rgb;
		int n;
		int dhsl = 0;
		if(clippedScan) {
			//l1 = (color2 - color1) / (x2 - x1);//TODO
			if(x2 > viewport.width) {
				x2 = viewport.width;
			}
			if(x1 < 0) {
				x1 -= 0;
				hsl1 -= x1 * dhsl;
				x1 = 0;
			}
		}
		if(x1 < x2) {
			offset += x1;
			hsl1 += dhsl * x1;
			//if(textured) {
			n = x2 - x1 >> 2;
			if(n > 0) {
				dhsl = (hsl2 - hsl1) * shadowDecay[n] >> 15;
			} else {
				dhsl = 0;
			}
			if(alphaFilter == 0) {
				if(n > 0) {
					do {
						rgb = hslToRgbMap[hsl1 >> 8];
						hsl1 += dhsl;
						dest[offset++] = rgb;
						dest[offset++] = rgb;
						dest[offset++] = rgb;
						dest[offset++] = rgb;
					} while(--n > 0);
				}
				n = x2 - x1 & 3;
				if(n > 0) {
					rgb = hslToRgbMap[hsl1 >> 8];
					do {
						dest[offset++] = rgb;
					} while(--n > 0);
				}
			} else {
				final int a1 = alphaFilter;
				final int a2 = 256 - alphaFilter;
				int dst;
				if(n > 0) {
					do {
						rgb = hslToRgbMap[hsl1 >> 8];
						hsl1 += dhsl;
						rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
						dst = dest[offset];
						dest[offset++] = rgb + ((dst & 0xff00ff) * a1 >> 8 & 0xff00ff) + ((dst & 0xff00) * a1 >> 8 & 0xff00);
						dst = dest[offset];
						dest[offset++] = rgb + ((dst & 0xff00ff) * a1 >> 8 & 0xff00ff) + ((dst & 0xff00) * a1 >> 8 & 0xff00);
						dst = dest[offset];
						dest[offset++] = rgb + ((dst & 0xff00ff) * a1 >> 8 & 0xff00ff) + ((dst & 0xff00) * a1 >> 8 & 0xff00);
						dst = dest[offset];
						dest[offset++] = rgb + ((dst & 0xff00ff) * a1 >> 8 & 0xff00ff) + ((dst & 0xff00) * a1 >> 8 & 0xff00);
					} while(--n > 0);
				}
				n = x2 - x1 & 3;
				if(n > 0) {
					rgb = hslToRgbMap[hsl1 >> 8];
					rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
					do {
						dst = dest[offset];
						dest[offset++] = rgb + ((dst & 0xff00ff) * a1 >> 8 & 0xff00ff) + ((dst & 0xff00) * a1 >> 8 & 0xff00);
					} while(--n > 0);
				}
			}
			/*} else {
				final int dhsl2 = (hsl2 - hsl1) / (x2 - x1);
				n = x2 - x1;
				if(alphaFilter == 0) {
					do {
						dest[offset++] = hslToRgbMap[hsl1 >> 8];
						hsl1 += dhsl2;
					} while(--n > 0);
				} else {
					final int a = alphaFilter;
					final int a2 = 256 - alphaFilter;
					do {

						rgb = hslToRgbMap[hsl1 >> 8];
						hsl1 += dhsl2;
						rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
						final int rgb2 = dest[offset];
						dest[offset++] = rgb + ((rgb2 & 0xff00ff) * a >> 8 & 0xff00ff) + ((rgb2 & 0xff00) * a >> 8 & 0xff00);
					} while(--n > 0);
				}
			}*/
		}
	}
	
	public static void drawFlatTriangle(int y1, int y2, int y3, int x1, int x2, int x3, float z1, float z2, float z3, int rgb) {
		int dx1 = 0;
		float dz1 = 0;
		if(y2 != y1) {
			final int d = (y2 - y1);
			dx1 = (x2 - x1 << 16) / d;
			dz1 = (z2 - z1) / d;
		}
		int dx2 = 0;
		float dz2 = 0;
		if(y3 != y2) {
			final int d = (y3 - y2);
			dx2 = (x3 - x2 << 16) / d;
			dz2 = (z3 - z2) / d;
		}
		int dx3 = 0;
		float dz3 = 0;
		if(y3 != y1) {
			final int d = (y1 - y3);
			dx3 = (x1 - x3 << 16) / d;
			dz3 = (z1 - z3) / d;
		}
		if(y1 <= y2 && y1 <= y3) {
			if(y1 >= viewport.height) {
				return;
			}
			if(y2 > viewport.height) {
				y2 = viewport.height;
			}
			if(y3 > viewport.height) {
				y3 = viewport.height;
			}
			if(y2 < y3) {
				x3 = x1 <<= 16;
				z3 = z1;
				if(y1 < 0) {
					x3 -= dx3 * y1;
					x1 -= dx1 * y1;
					z3 -= dz3 * y1;
					z1 -= dz1 * y1;
					y1 = 0;
				}
				x2 <<= 16;
				if(y2 < 0) {
					x2 -= dx2 * y2;
					z2 -= dz2 * y2;
					y2 = 0;
				}
				if(y1 != y2 && dx3 < dx1 || y1 == y2 && dx3 > dx2) {
					y3 -= y2;
					y2 -= y1;
					for(y1 = viewport.scanOffsets[y1]; --y2 >= 0; y1 += Rasterizer2D.canvasWidth) {
						drawFlatScanline(Rasterizer2D.canvasRaster, y1, rgb, x3 >> 16, x1 >> 16, z3, z1);
						x3 += dx3;
						x1 += dx1;
						z3 += dz3;
						z1 += dz1;
					}
					while(--y3 >= 0) {
						drawFlatScanline(Rasterizer2D.canvasRaster, y1, rgb, x3 >> 16, x2 >> 16, z3, z2);
						x3 += dx3;
						x2 += dx2;
						z3 += dz3;
						z2 += dz2;
						y1 += Rasterizer2D.canvasWidth;
					}
					return;
				}
				y3 -= y2;
				y2 -= y1;
				for(y1 = viewport.scanOffsets[y1]; --y2 >= 0; y1 += Rasterizer2D.canvasWidth) {
					drawFlatScanline(Rasterizer2D.canvasRaster, y1, rgb, x1 >> 16, x3 >> 16, z1, z3);
					x3 += dx3;
					x1 += dx1;
					z3 += dz3;
					z1 += dz1;
				}
				while(--y3 >= 0) {
					drawFlatScanline(Rasterizer2D.canvasRaster, y1, rgb, x2 >> 16, x3 >> 16, z2, z3);
					x3 += dx3;
					x2 += dx2;
					z3 += dz3;
					z2 += dz2;
					y1 += Rasterizer2D.canvasWidth;
				}
				return;
			}
			x2 = x1 <<= 16;
			z2 = z1;
			if(y1 < 0) {
				x2 -= dx3 * y1;
				x1 -= dx1 * y1;
				z2 -= dz3 * y1;
				z1 -= dz1 * y1;
				y1 = 0;
			}
			x3 <<= 16;
			if(y3 < 0) {
				x3 -= dx2 * y3;
				z3 -= dz2 * y3;
				y3 = 0;
			}
			if(y1 != y3 && dx3 < dx1 || y1 == y3 && dx2 > dx1) {
				y2 -= y3;
				y3 -= y1;
				for(y1 = viewport.scanOffsets[y1]; --y3 >= 0; y1 += Rasterizer2D.canvasWidth) {
					drawFlatScanline(Rasterizer2D.canvasRaster, y1, rgb, x2 >> 16, x1 >> 16, z2, z1);
					x2 += dx3;
					x1 += dx1;
					z2 += dz3;
					z1 += dz1;
				}
				while(--y2 >= 0) {
					drawFlatScanline(Rasterizer2D.canvasRaster, y1, rgb, x3 >> 16, x1 >> 16, z3, z1);
					x3 += dx2;
					x1 += dx1;
					z3 += dz2;
					z1 += dz1;
					y1 += Rasterizer2D.canvasWidth;
				}
				return;
			}
			y2 -= y3;
			y3 -= y1;
			for(y1 = viewport.scanOffsets[y1]; --y3 >= 0; y1 += Rasterizer2D.canvasWidth) {
				drawFlatScanline(Rasterizer2D.canvasRaster, y1, rgb, x1 >> 16, x2 >> 16, z1, z2);
				x2 += dx3;
				x1 += dx1;
				z2 += dz3;
				z1 += dz1;
			}
			while(--y2 >= 0) {
				drawFlatScanline(Rasterizer2D.canvasRaster, y1, rgb, x1 >> 16, x3 >> 16, z1, z3);
				x3 += dx2;
				x1 += dx1;
				z3 += dz2;
				z1 += dz1;
				y1 += Rasterizer2D.canvasWidth;
			}
			return;
		}
		if(y2 <= y3) {
			if(y2 >= viewport.height) {
				return;
			}
			if(y3 > viewport.height) {
				y3 = viewport.height;
			}
			if(y1 > viewport.height) {
				y1 = viewport.height;
			}
			if(y3 < y1) {
				x1 = x2 <<= 16;
				z1 = z2;
				if(y2 < 0) {
					x1 -= dx1 * y2;
					x2 -= dx2 * y2;
					z1 -= dz1 * y2;
					z2 -= dz2 * y2;
					y2 = 0;
				}
				x3 <<= 16;
				if(y3 < 0) {
					x3 -= dx3 * y3;
					z3 -= dz3 * y3;
					y3 = 0;
				}
				if(y2 != y3 && dx1 < dx2 || y2 == y3 && dx1 > dx3) {
					y1 -= y3;
					y3 -= y2;
					for(y2 = viewport.scanOffsets[y2]; --y3 >= 0; y2 += Rasterizer2D.canvasWidth) {
						drawFlatScanline(Rasterizer2D.canvasRaster, y2, rgb, x1 >> 16, x2 >> 16, z1, z2);
						x1 += dx1;
						x2 += dx2;
						z1 += dz1;
						z2 += dz2;
					}
					while(--y1 >= 0) {
						drawFlatScanline(Rasterizer2D.canvasRaster, y2, rgb, x1 >> 16, x3 >> 16, z1, z3);
						x1 += dx1;
						x3 += dx3;
						z1 += dz1;
						z3 += dz3;
						y2 += Rasterizer2D.canvasWidth;
					}
					return;
				}
				y1 -= y3;
				y3 -= y2;
				for(y2 = viewport.scanOffsets[y2]; --y3 >= 0; y2 += Rasterizer2D.canvasWidth) {
					drawFlatScanline(Rasterizer2D.canvasRaster, y2, rgb, x2 >> 16, x1 >> 16, z2, z1);
					x1 += dx1;
					x2 += dx2;
					z1 += dz1;
					z2 += dz2;
				}
				while(--y1 >= 0) {
					drawFlatScanline(Rasterizer2D.canvasRaster, y2, rgb, x3 >> 16, x1 >> 16, z3, z1);
					x1 += dx1;
					x3 += dx3;
					z1 += dz1;
					z3 += dz3;
					y2 += Rasterizer2D.canvasWidth;
				}
				return;
			}
			x3 = x2 <<= 16;
			z3 = z2;
			if(y2 < 0) {
				x3 -= dx1 * y2;
				x2 -= dx2 * y2;
				z3 -= dz1 * y2;
				z2 -= dz2 * y2;
				y2 = 0;
			}
			x1 <<= 16;
			if(y1 < 0) {
				x1 -= dx3 * y1;
				z1 -= dz3 * y1;
				y1 = 0;
			}
			if(dx1 < dx2) {
				y3 -= y1;
				y1 -= y2;
				for(y2 = viewport.scanOffsets[y2]; --y1 >= 0; y2 += Rasterizer2D.canvasWidth) {
					drawFlatScanline(Rasterizer2D.canvasRaster, y2, rgb, x3 >> 16, x2 >> 16, z3, z2);
					x3 += dx1;
					x2 += dx2;
					z3 += dz1;
					z2 += dz2;
				}
				while(--y3 >= 0) {
					drawFlatScanline(Rasterizer2D.canvasRaster, y2, rgb, x1 >> 16, x2 >> 16, z1, z2);
					x1 += dx3;
					x2 += dx2;
					z1 += dz3;
					z2 += dz2;
					y2 += Rasterizer2D.canvasWidth;
				}
				return;
			}
			y3 -= y1;
			y1 -= y2;
			for(y2 = viewport.scanOffsets[y2]; --y1 >= 0; y2 += Rasterizer2D.canvasWidth) {
				drawFlatScanline(Rasterizer2D.canvasRaster, y2, rgb, x2 >> 16, x3 >> 16, z2, z3);
				x3 += dx1;
				x2 += dx2;
				z3 += dz1;
				z2 += dz2;
			}
			while(--y3 >= 0) {
				drawFlatScanline(Rasterizer2D.canvasRaster, y2, rgb, x2 >> 16, x1 >> 16, z2, z1);
				x1 += dx3;
				x2 += dx2;
				z1 += dz3;
				z2 += dz2;
				y2 += Rasterizer2D.canvasWidth;
			}
			return;
		}
		if(y3 >= viewport.height) {
			return;
		}
		if(y1 > viewport.height) {
			y1 = viewport.height;
		}
		if(y2 > viewport.height) {
			y2 = viewport.height;
		}
		if(y1 < y2) {
			x2 = x3 <<= 16;
			z2 = z3;
			if(y3 < 0) {
				x2 -= dx2 * y3;
				x3 -= dx3 * y3;
				z2 -= dz2 * y3;
				z3 -= dz3 * y3;
				y3 = 0;
			}
			x1 <<= 16;
			if(y1 < 0) {
				x1 -= dx1 * y1;
				z1 -= dz1 * y1;
				y1 = 0;
			}
			if(dx2 < dx3) {
				y2 -= y1;
				y1 -= y3;
				for(y3 = viewport.scanOffsets[y3]; --y1 >= 0; y3 += Rasterizer2D.canvasWidth) {
					drawFlatScanline(Rasterizer2D.canvasRaster, y3, rgb, x2 >> 16, x3 >> 16, z2, z3);
					x2 += dx2;
					x3 += dx3;
					z2 += dz2;
					z3 += dz3;
				}
				while(--y2 >= 0) {
					drawFlatScanline(Rasterizer2D.canvasRaster, y3, rgb, x2 >> 16, x1 >> 16, z2, z1);
					x2 += dx2;
					x1 += dx1;
					z2 += dz2;
					z1 += dz1;
					y3 += Rasterizer2D.canvasWidth;
				}
				return;
			}
			y2 -= y1;
			y1 -= y3;
			for(y3 = viewport.scanOffsets[y3]; --y1 >= 0; y3 += Rasterizer2D.canvasWidth) {
				drawFlatScanline(Rasterizer2D.canvasRaster, y3, rgb, x3 >> 16, x2 >> 16, z3, z2);
				x2 += dx2;
				x3 += dx3;
				z2 += dz2;
				z3 += dz3;
			}
			while(--y2 >= 0) {
				drawFlatScanline(Rasterizer2D.canvasRaster, y3, rgb, x1 >> 16, x2 >> 16, z1, z2);
				x2 += dx2;
				x1 += dx1;
				z2 += dz2;
				z1 += dz1;
				y3 += Rasterizer2D.canvasWidth;
			}
			return;
		}
		x1 = x3 <<= 16;
		z1 = z3;
		if(y3 < 0) {
			x1 -= dx2 * y3;
			x3 -= dx3 * y3;
			z1 -= dz2 * y3;
			z3 -= dz3 * y3;
			y3 = 0;
		}
		x2 <<= 16;
		if(y2 < 0) {
			x2 -= dx1 * y2;
			z2 -= dz1 * y2;
			y2 = 0;
		}
		if(dx2 < dx3) {
			y1 -= y2;
			y2 -= y3;
			for(y3 = viewport.scanOffsets[y3]; --y2 >= 0; y3 += Rasterizer2D.canvasWidth) {
				drawFlatScanline(Rasterizer2D.canvasRaster, y3, rgb, x1 >> 16, x3 >> 16, z1, z3);
				x1 += dx2;
				x3 += dx3;
				z1 += dz2;
				z3 += dz3;
			}
			while(--y1 >= 0) {
				drawFlatScanline(Rasterizer2D.canvasRaster, y3, rgb, x2 >> 16, x3 >> 16, z2, z3);
				x2 += dx1;
				x3 += dx3;
				z2 += dz1;
				z3 += dz3;
				y3 += Rasterizer2D.canvasWidth;
			}
			return;
		}
		y1 -= y2;
		y2 -= y3;
		for(y3 = viewport.scanOffsets[y3]; --y2 >= 0; y3 += Rasterizer2D.canvasWidth) {
			drawFlatScanline(Rasterizer2D.canvasRaster, y3, rgb, x3 >> 16, x1 >> 16, z3, z1);
			x1 += dx2;
			x3 += dx3;
			z1 += dz2;
			z3 += dz3;
		}
		while(--y1 >= 0) {
			drawFlatScanline(Rasterizer2D.canvasRaster, y3, rgb, x3 >> 16, x2 >> 16, z3, z2);
			x2 += dx1;
			x3 += dx3;
			z2 += dz1;
			z3 += dz3;
			y3 += Rasterizer2D.canvasWidth;
		}
	}
	
	private static void drawFlatScanline(int[] dest, int offset, int rgb, int x1, int x2, float z1, float z2) {
		float dz = (z2 - z1) / (x2 - x1);
		if(x1 >= x2) {
			return;
		}
		if(clippedScan) {
			if(x2 > viewport.width) {
				x2 = viewport.width;
			}
			if(x1 < 0) {
				z1 -= x1 * dz;
				x1 = 0;
			}
		}
		if(x1 >= x2) {
			return;
		}
		offset += x1;
		int n = x2 - x1;
		if(alphaFilter == 0) {
			while(--n >= 0) {
				if (z1 < depthBuffer[offset] || depthRender(z1, offset)) {
					dest[offset] = rgb;
					depthBuffer[offset] = z1;
				}
				z1 += dz;
				offset++;
			}
		} else {
			final int a1 = alphaFilter;
			final int a2 = 256 - alphaFilter;
			double alphaPercentage = (a1 / 256D);
			rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
			while(--n >= 0) {
				if (z1 < depthBuffer[offset] || depthRender(z1, offset)) {
					dest[offset] = rgb + ((dest[offset] & 0xff00ff) * a1 >> 8 & 0xff00ff) + ((dest[offset] & 0xff00) * a1 >> 8 & 0xff00);
					depthBuffer[offset] = (int) (z1 + ((depthBuffer[offset] - z1) * alphaPercentage));
				}
				z1 += dz;
				offset++;
			}
		}
	}
	
	
	public static void drawMaterializedTriangle(int y1, int y2, int y3, int x1, int x2, int x3, float z1, float z2, float z3, int hsl1, int hsl2, int hsl3, int tx1, int tx2, int tx3, int ty1, int ty2, int ty3, int tz1, int tz2, int tz3, int[] ai) {
		tx2 = tx1 - tx2;
		ty2 = ty1 - ty2;
		tz2 = tz1 - tz2;
		tx3 -= tx1;
		ty3 -= ty1;
		tz3 -= tz1;
		int l4 = ((tx3 * ty1) - (ty3 * tx1)) * Scene.focalLength << 5;
		int i5 = ((ty3 * tz1) - (tz3 * ty1)) << 8;
		int j5 = ((tz3 * tx1) - (tx3 * tz1)) << 5;
		int k5 = ((tx2 * ty1) - (ty2 * tx1)) * Scene.focalLength << 5;
		int l5 = ((ty2 * tz1) - (tz2 * ty1)) << 8;
		int i6 = ((tz2 * tx1) - (tx2 * tz1)) << 5;
		int j6 = ((ty2 * tx3) - (tx2 * ty3)) * Scene.focalLength << 5;
		int k6 = ((tz2 * ty3) - (ty2 * tz3)) << 8;
		int l6 = ((tx2 * tz3) - (tz2 * tx3)) << 5;
		int i7 = 0;
		int j7 = 0;
		float dz1 = 0;
		if(y2 != y1) {
			i7 = (x2 - x1 << 16) / (y2 - y1);
			j7 = (hsl2 - hsl1 << 15) / (y2 - y1);
			dz1 = (z2 - z1) / (y2 - y1);
		}
		int k7 = 0;
		int l7 = 0;
		float dz2 = 0;
		if(y3 != y2) {
			k7 = (x3 - x2 << 16) / (y3 - y2);
			l7 = (hsl3 - hsl2 << 15) / (y3 - y2);
			dz2 = (z3 - z2) / (y3 - y2);
		}
		int i8 = 0;
		int j8 = 0;
		float dz3 = 0;
		if(y3 != y1) {
			i8 = (x1 - x3 << 16) / (y1 - y3);
			j8 = (hsl1 - hsl3 << 15) / (y1 - y3);
			dz3 = (z1 - z3) / (y1 - y3);
		}
		if(y1 <= y2 && y1 <= y3) {
			if(y1 >= Rasterizer2D.clipEndY) {
				return;
			}
			if(y2 > Rasterizer2D.clipEndY) {
				y2 = Rasterizer2D.clipEndY;
			}
			if(y3 > Rasterizer2D.clipEndY) {
				y3 = Rasterizer2D.clipEndY;
			}
			if(y2 < y3) {
				x3 = x1 <<= 16;
				z3 = z1;
				hsl3 = hsl1 <<= 15;
				if(y1 < 0) {
					x3 -= i8 * y1;
					x1 -= i7 * y1;
					z3 -= dz3 * y1;
					z1 -= dz1 * y1;
					hsl3 -= j8 * y1;
					hsl1 -= j7 * y1;
					y1 = 0;
				}
				x2 <<= 16;
				hsl2 <<= 15;
				if(y2 < 0) {
					x2 -= k7 * y2;
					z2 -= dz2 * y2;
					hsl2 -= l7 * y2;
					y2 = 0;
				}
				int k8 = y1 - viewport.centerY;
				l4 += j5 * k8;
				k5 += i6 * k8;
				j6 += l6 * k8;
				if(y1 != y2 && i8 < i7 || y1 == y2 && i8 > k7) {
					y3 -= y2;
					y2 -= y1;
					y1 = viewport.scanOffsets[y1];
					while(--y2 >= 0) {
						drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y1, x3 >> 16, x1 >> 16, z3, z1, hsl3 >> 7, hsl1 >> 7, l4, k5, j6, i5, l5, k6);
						x3 += i8;
						x1 += i7;
						z3 += dz3;
						z1 += dz1;
						hsl3 += j8;
						hsl1 += j7;
						y1 += Rasterizer2D.canvasWidth;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					while(--y3 >= 0) {
						drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y1, x3 >> 16, x2 >> 16, z3, z2, hsl3 >> 7, hsl2 >> 7, l4, k5, j6, i5, l5, k6);
						x3 += i8;
						x2 += k7;
						z3 += dz3;
						z2 += dz2;
						//z3 += dz3;
						//z2 += dz2;
						hsl3 += j8;
						hsl2 += l7;
						y1 += Rasterizer2D.canvasWidth;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					return;
				}
				y3 -= y2;
				y2 -= y1;
				y1 = viewport.scanOffsets[y1];
				while(--y2 >= 0) {
					drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y1, x1 >> 16, x3 >> 16, z1, z3, hsl1 >> 7, hsl3 >> 7, l4, k5, j6, i5, l5, k6);
					x3 += i8;
					x1 += i7;
					z3 += dz3;
					z1 += dz1;
					hsl3 += j8;
					hsl1 += j7;
					y1 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while(--y3 >= 0) {
					drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y1, x2 >> 16, x3 >> 16, z2, z3, hsl2 >> 7, hsl3 >> 7, l4, k5, j6, i5, l5, k6);
					x3 += i8;
					x2 += k7;
					z3 += dz3;
					z2 += dz2;
					hsl3 += j8;
					hsl2 += l7;
					y1 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			x2 = x1 <<= 16;
			z2 = z1;
			hsl2 = hsl1 <<= 15;
			if(y1 < 0) {
				x2 -= i8 * y1;
				x1 -= i7 * y1;
				z2 -= dz3 * y1;
				z1 -= dz1 * y1;
				hsl2 -= j8 * y1;
				hsl1 -= j7 * y1;
				y1 = 0;
			}
			x3 <<= 16;
			hsl3 <<= 15;
			if(y3 < 0) {
				x3 -= k7 * y3;
				z3 -= dz2 * y3;
				hsl3 -= l7 * y3;
				y3 = 0;
			}
			int l8 = y1 - viewport.centerY;
			l4 += j5 * l8;
			k5 += i6 * l8;
			j6 += l6 * l8;
			if(y1 != y3 && i8 < i7 || y1 == y3 && k7 > i7) {
				y2 -= y3;
				y3 -= y1;
				y1 = viewport.scanOffsets[y1];
				while(--y3 >= 0) {
					drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y1, x2 >> 16, x1 >> 16, z2, z1, hsl2 >> 7, hsl1 >> 7, l4, k5, j6, i5, l5, k6);
					x2 += i8;
					x1 += i7;
					z2 += dz3;
					z1 += dz1;
					hsl2 += j8;
					hsl1 += j7;
					y1 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while(--y2 >= 0) {
					drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y1, x3 >> 16, x1 >> 16, z3, z1, hsl3 >> 7, hsl1 >> 7, l4, k5, j6, i5, l5, k6);
					x3 += k7;
					x1 += i7;
					z3 += dz2;
					z1 += dz1;
					hsl3 += l7;
					hsl1 += j7;
					y1 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			y2 -= y3;
			y3 -= y1;
			y1 = viewport.scanOffsets[y1];
			while(--y3 >= 0) {
				drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y1, x1 >> 16, x2 >> 16, z1, z2, hsl1 >> 7, hsl2 >> 7, l4, k5, j6, i5, l5, k6);
				x2 += i8;
				x1 += i7;
				z2 += dz3;
				z1 += dz1;
				hsl2 += j8;
				hsl1 += j7;
				y1 += Rasterizer2D.canvasWidth;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while(--y2 >= 0) {
				drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y1, x1 >> 16, x3 >> 16, z1, z3, hsl1 >> 7, hsl3 >> 7, l4, k5, j6, i5, l5, k6);
				x3 += k7;
				x1 += i7;
				z3 += dz2;
				z1 += dz1;
				hsl3 += l7;
				hsl1 += j7;
				y1 += Rasterizer2D.canvasWidth;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		if(y2 <= y3) {
			if(y2 >= Rasterizer2D.clipEndY) {
				return;
			}
			if(y3 > Rasterizer2D.clipEndY) {
				y3 = Rasterizer2D.clipEndY;
			}
			if(y1 > Rasterizer2D.clipEndY) {
				y1 = Rasterizer2D.clipEndY;
			}
			if(y3 < y1) {
				x1 = x2 <<= 16;
				z1 = z2;
				hsl1 = hsl2 <<= 15;
				if(y2 < 0) {
					x1 -= i7 * y2;
					x2 -= k7 * y2;
					z1 -= dz1 * y2;
					z2 -= dz2 * y2;
					hsl1 -= j7 * y2;
					hsl2 -= l7 * y2;
					y2 = 0;
				}
				x3 <<= 16;
				hsl3 <<= 15;
				if(y3 < 0) {
					x3 -= i8 * y3;
					z3 -= dz3 * y3;
					hsl3 -= j8 * y3;
					y3 = 0;
				}
				int i9 = y2 - viewport.centerY;
				l4 += j5 * i9;
				k5 += i6 * i9;
				j6 += l6 * i9;
				if(y2 != y3 && i7 < k7 || y2 == y3 && i7 > i8) {
					y1 -= y3;
					y3 -= y2;
					y2 = viewport.scanOffsets[y2];
					while(--y3 >= 0) {
						drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y2, x1 >> 16, x2 >> 16, z1, z2, hsl1 >> 7, hsl2 >> 7, l4, k5, j6, i5, l5, k6);
						x1 += i7;
						x2 += k7;
						z1 += dz1;
						z2 += dz2;
						hsl1 += j7;
						hsl2 += l7;
						y2 += Rasterizer2D.canvasWidth;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					while(--y1 >= 0) {
						drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y2, x1 >> 16, x3 >> 16, z1, z3, hsl1 >> 7, hsl3 >> 7, l4, k5, j6, i5, l5, k6);
						x1 += i7;
						x3 += i8;
						z1 += dz1;
						z3 += dz3;
						hsl1 += j7;
						hsl3 += j8;
						y2 += Rasterizer2D.canvasWidth;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					return;
				}
				y1 -= y3;
				y3 -= y2;
				y2 = viewport.scanOffsets[y2];
				while(--y3 >= 0) {
					drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y2, x2 >> 16, x1 >> 16, z2, z1, hsl2 >> 7, hsl1 >> 7, l4, k5, j6, i5, l5, k6);
					x1 += i7;
					x2 += k7;
					z1 += dz1;
					z2 += dz2;
					hsl1 += j7;
					hsl2 += l7;
					y2 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while(--y1 >= 0) {
					drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y2, x3 >> 16, x1 >> 16, z3, z1, hsl3 >> 7, hsl1 >> 7, l4, k5, j6, i5, l5, k6);
					x1 += i7;
					x3 += i8;
					z1 += dz1;
					z3 += dz3;
					hsl1 += j7;
					hsl3 += j8;
					y2 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			x3 = x2 <<= 16;
			z3 = z2;
			hsl3 = hsl2 <<= 15;
			if(y2 < 0) {
				x3 -= i7 * y2;
				x2 -= k7 * y2;
				z3 -= dz1 * y2;
				z2 -= dz2 * y2;
				hsl3 -= j7 * y2;
				hsl2 -= l7 * y2;
				y2 = 0;
			}
			x1 <<= 16;
			
			hsl1 <<= 15;
			if(y1 < 0) {
				x1 -= i8 * y1;
				z1 -= dz3 * y1;
				hsl1 -= j8 * y1;
				y1 = 0;
			}
			int j9 = y2 - viewport.centerY;
			l4 += j5 * j9;
			k5 += i6 * j9;
			j6 += l6 * j9;
			if(i7 < k7) {
				y3 -= y1;
				y1 -= y2;
				y2 = viewport.scanOffsets[y2];
				while(--y1 >= 0) {
					drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y2, x3 >> 16, x2 >> 16, z3, z2, hsl3 >> 7, hsl2 >> 7, l4, k5, j6, i5, l5, k6);
					x3 += i7;
					x2 += k7;
					z3 += dz1;
					z2 += dz2;
					hsl3 += j7;
					hsl2 += l7;
					y2 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while(--y3 >= 0) {
					drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y2, x1 >> 16, x2 >> 16, z1, z2, hsl1 >> 7, hsl2 >> 7, l4, k5, j6, i5, l5, k6);
					x1 += i8;
					x2 += k7;
					z1 += dz3;
					z2 += dz2;
					hsl1 += j8;
					hsl2 += l7;
					y2 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			y3 -= y1;
			y1 -= y2;
			y2 = viewport.scanOffsets[y2];
			while(--y1 >= 0) {
				drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y2, x2 >> 16, x3 >> 16, z2, z3, hsl2 >> 7, hsl3 >> 7, l4, k5, j6, i5, l5, k6);
				x3 += i7;
				x2 += k7;
				z3 += dz1;
				z2 += dz2;
				hsl3 += j7;
				hsl2 += l7;
				y2 += Rasterizer2D.canvasWidth;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while(--y3 >= 0) {
				drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y2, x2 >> 16, x1 >> 16, z2, z1, hsl2 >> 7, hsl1 >> 7, l4, k5, j6, i5, l5, k6);
				x1 += i8;
				x2 += k7;
				z1 += dz3;
				z2 += dz2;
				hsl1 += j8;
				hsl2 += l7;
				y2 += Rasterizer2D.canvasWidth;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		if(y3 >= Rasterizer2D.clipEndY) {
			return;
		}
		if(y1 > Rasterizer2D.clipEndY) {
			y1 = Rasterizer2D.clipEndY;
		}
		if(y2 > Rasterizer2D.clipEndY) {
			y2 = Rasterizer2D.clipEndY;
		}
		if(y1 < y2) {
			x2 = x3 <<= 16;
			z2 = z3;
			hsl2 = hsl3 <<= 15;
			if(y3 < 0) {
				x2 -= k7 * y3;
				x3 -= i8 * y3;
				z2 -= dz2 * y3;
				z3 -= dz3 * y3;
				hsl2 -= l7 * y3;
				hsl3 -= j8 * y3;
				y3 = 0;
			}
			x1 <<= 16;
			hsl1 <<= 15;
			if(y1 < 0) {
				x1 -= i7 * y1;
				z1 -= dz1 * y1;
				hsl1 -= j7 * y1;
				y1 = 0;
			}
			int k9 = y3 - viewport.centerY;
			l4 += j5 * k9;
			k5 += i6 * k9;
			j6 += l6 * k9;
			if(k7 < i8) {
				y2 -= y1;
				y1 -= y3;
				y3 = viewport.scanOffsets[y3];
				while(--y1 >= 0) {
					drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y3, x2 >> 16, x3 >> 16, z2, z3, hsl2 >> 7, hsl3 >> 7, l4, k5, j6, i5, l5, k6);
					x2 += k7;
					x3 += i8;
					z2 += dz2;
					z3 += dz3;
					hsl2 += l7;
					hsl3 += j8;
					y3 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while(--y2 >= 0) {
					drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y3, x2 >> 16, x1 >> 16, z2, z1, hsl2 >> 7, hsl1 >> 7, l4, k5, j6, i5, l5, k6);
					x2 += k7;
					x1 += i7;
					z2 += dz2;
					z1 += dz1;
					hsl2 += l7;
					hsl1 += j7;
					y3 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			y2 -= y1;
			y1 -= y3;
			y3 = viewport.scanOffsets[y3];
			while(--y1 >= 0) {
				drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y3, x3 >> 16, x2 >> 16, z3, z2, hsl3 >> 7, hsl2 >> 7, l4, k5, j6, i5, l5, k6);
				x2 += k7;
				x3 += i8;
				z2 += dz2;
				z3 += dz3;
				hsl2 += l7;
				hsl3 += j8;
				y3 += Rasterizer2D.canvasWidth;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while(--y2 >= 0) {
				drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y3, x1 >> 16, x2 >> 16, z1, z2, hsl1 >> 7, hsl2 >> 7, l4, k5, j6, i5, l5, k6);
				x2 += k7;
				x1 += i7;
				z2 += dz2;
				z1 += dz1;
				hsl2 += l7;
				hsl1 += j7;
				y3 += Rasterizer2D.canvasWidth;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		x1 = x3 <<= 16;
		z1 = z3;
		hsl1 = hsl3 <<= 15;
		if(y3 < 0) {
			x1 -= k7 * y3;
			x3 -= i8 * y3;
			z1 -= dz2 * y3;
			z3 -= dz3 * y3;
			hsl1 -= l7 * y3;
			hsl3 -= j8 * y3;
			y3 = 0;
		}
		x2 <<= 16;
		hsl2 <<= 15;
		if(y2 < 0) {
			x2 -= i7 * y2;
			z2 -= dz1 * y2;
			hsl2 -= j7 * y2;
			y2 = 0;
		}
		int l9 = y3 - viewport.centerY;
		l4 += j5 * l9;
		k5 += i6 * l9;
		j6 += l6 * l9;
		if(k7 < i8) {
			y1 -= y2;
			y2 -= y3;
			y3 = viewport.scanOffsets[y3];
			while(--y2 >= 0) {
				drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y3, x1 >> 16, x3 >> 16, z1, z3, hsl1 >> 7, hsl3 >> 7, l4, k5, j6, i5, l5, k6);
				x1 += k7;
				x3 += i8;
				z1 += dz2;
				z3 += dz3;
				hsl1 += l7;
				hsl3 += j8;
				y3 += Rasterizer2D.canvasWidth;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while(--y1 >= 0) {
				drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y3, x2 >> 16, x3 >> 16, z2, z3, hsl2 >> 7, hsl3 >> 7, l4, k5, j6, i5, l5, k6);
				x2 += i7;
				x3 += i8;
				z2 += dz1;
				z3 += dz3;
				hsl2 += j7;
				hsl3 += j8;
				y3 += Rasterizer2D.canvasWidth;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		y1 -= y2;
		y2 -= y3;
		y3 = viewport.scanOffsets[y3];
		while(--y2 >= 0) {
			drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y3, x3 >> 16, x1 >> 16, z3, z1, hsl3 >> 7, hsl1 >> 7, l4, k5, j6, i5, l5, k6);
			x1 += k7;
			x3 += i8;
			z1 += dz2;
			z3 += dz3;
			hsl1 += l7;
			hsl3 += j8;
			y3 += Rasterizer2D.canvasWidth;
			l4 += j5;
			k5 += i6;
			j6 += l6;
		}
		while(--y1 >= 0) {
			drawMaterializedScanline(Rasterizer2D.canvasRaster, ai, y3, x3 >> 16, x2 >> 16, z3, z2, hsl3 >> 7, hsl2 >> 7, l4, k5, j6, i5, l5, k6);
			x2 += i7;
			x3 += i8;
			z2 += dz1;
			z3 += dz3;
			hsl2 += j7;
			hsl3 += j8;
			y3 += Rasterizer2D.canvasWidth;
			l4 += j5;
			k5 += i6;
			j6 += l6;
		}
	}
	
	private static void drawMaterializedScanline(int dst[], int src[], int off, int x1, int x2, float z1, float z2, int hsl1, int hsl2, int l1, int i2, int j2, int k2, int l2, int i3) {
		int i = 0;// was parameter
		int j = 0;// was parameter
		if(x1 >= x2) {
			return;
		}
		
		int k3;
		z2 = (z2 - z1) / (x2 - x1);
		
		hsl2 = (hsl2 - hsl1) / (x2 - x1);
		if(clippedScan) {
			if(x2 > Rasterizer2D.clipEndX) {
				x2 = Rasterizer2D.clipEndX;
			}
			if(x1 < 0) {
				z1 -= x1 * z2;
				x1 = 0;
			}
			if(x1 < 0) {
				hsl1 -= x1 * hsl2;
				x1 = 0;
			}
			if(x1 >= x2) {
				return;
			}
			k3 = x2 - x1 >> 3;
		} else {
			if(x2 - x1 > 7) {
				k3 = x2 - x1 >> 3;
			} else {
				k3 = 0;
			}
		}
		off += x1;
		int j4 = 0;
		int l4 = 0;
		int l6 = x1 - viewport.centerX;
		l1 += (k2 >> 3) * l6;
		i2 += (l2 >> 3) * l6;
		j2 += (i3 >> 3) * l6;
		int l5 = j2 >> 14;
		if(l5 != 0) {
			i = l1 / l5;
			j = i2 / l5;
			if(i < 0) {
				i = 0;
			} else if(i > 16256) {
				i = 16256;
			}
		}
		l1 += k2;
		i2 += l2;
		j2 += i3;
		l5 = j2 >> 14;
		if(l5 != 0) {
			j4 = l1 / l5;
			l4 = i2 / l5;
			if(j4 < 7) {
				j4 = 7;
			} else if(j4 > 16256) {
				j4 = 16256;
			}
		}
		int j7 = j4 - i >> 3;
		int l7 = l4 - j >> 3;
		int rgb1, rgb2;
		while(k3-- > 0) {
			rgb1 = hslToRgbMap[hsl1 >> 8];
			if(texelPos((j & 0x3f80) + (i >> 7)) >= src.length)
				return;
			rgb2 = src[texelPos((j & 0x3f80) + (i >> 7))];
			if (z1 < depthBuffer[off] || depthRender(z1, off)) {
				dst[off] = (((rgb1 >> 16 & 0xff) * (rgb2 >> 17 & 0x7f) << 11) / 3 & 0xff0000) + (((rgb1 >> 8 & 0xff) * (rgb2 >> 9 & 0x7f) << 3) / 3 & 0xff00) + (((rgb1 & 0xff) * (rgb2 >> 1 & 0x7f) >> 5) / 3 & 0xff);
				depthBuffer[off] = z1;
			}
			off++;
			z1 += z2;
			i += j7;
			j += l7;
			hsl1 += hsl2;
			rgb1 = hslToRgbMap[hsl1 >> 8];
			if(texelPos((j & 0x3f80) + (i >> 7)) >= src.length)
				return;
			rgb2 = src[texelPos((j & 0x3f80) + (i >> 7))];
			if (z1 < depthBuffer[off] || depthRender(z1, off)) {
				dst[off] = (((rgb1 >> 16 & 0xff) * (rgb2 >> 17 & 0x7f) << 11) / 3 & 0xff0000) + (((rgb1 >> 8 & 0xff) * (rgb2 >> 9 & 0x7f) << 3) / 3 & 0xff00) + (((rgb1 & 0xff) * (rgb2 >> 1 & 0x7f) >> 5) / 3 & 0xff);
				depthBuffer[off] = z1;
			}
			
			off++;
			z1 += z2;
			i += j7;
			j += l7;
			hsl1 += hsl2;
			rgb1 = hslToRgbMap[hsl1 >> 8];
			if(texelPos((j & 0x3f80) + (i >> 7)) >= src.length)
				return;
			rgb2 = src[texelPos((j & 0x3f80) + (i >> 7))];
			if (z1 < depthBuffer[off] || depthRender(z1, off)) {
				dst[off] = (((rgb1 >> 16 & 0xff) * (rgb2 >> 17 & 0x7f) << 11) / 3 & 0xff0000) + (((rgb1 >> 8 & 0xff) * (rgb2 >> 9 & 0x7f) << 3) / 3 & 0xff00) + (((rgb1 & 0xff) * (rgb2 >> 1 & 0x7f) >> 5) / 3 & 0xff);
				depthBuffer[off] = z1;
			}
			
			off++;
			z1 += z2;
			i += j7;
			j += l7;
			hsl1 += hsl2;
			rgb1 = hslToRgbMap[hsl1 >> 8];
			if(texelPos((j & 0x3f80) + (i >> 7)) >= src.length)
				return;
			rgb2 = src[texelPos((j & 0x3f80) + (i >> 7))];
			if (z1 < depthBuffer[off] || depthRender(z1, off)) {
				dst[off] = (((rgb1 >> 16 & 0xff) * (rgb2 >> 17 & 0x7f) << 11) / 3 & 0xff0000) + (((rgb1 >> 8 & 0xff) * (rgb2 >> 9 & 0x7f) << 3) / 3 & 0xff00) + (((rgb1 & 0xff) * (rgb2 >> 1 & 0x7f) >> 5) / 3 & 0xff);
				depthBuffer[off] = z1;
			}
			
			off++;
			z1 += z2;
			i += j7;
			j += l7;
			hsl1 += hsl2;
			rgb1 = hslToRgbMap[hsl1 >> 8];
			if(texelPos((j & 0x3f80) + (i >> 7)) >= src.length)
				return;
			rgb2 = src[texelPos((j & 0x3f80) + (i >> 7))];
			if (z1 < depthBuffer[off] || depthRender(z1, off)) {
				dst[off] = (((rgb1 >> 16 & 0xff) * (rgb2 >> 17 & 0x7f) << 11) / 3 & 0xff0000) + (((rgb1 >> 8 & 0xff) * (rgb2 >> 9 & 0x7f) << 3) / 3 & 0xff00) + (((rgb1 & 0xff) * (rgb2 >> 1 & 0x7f) >> 5) / 3 & 0xff);
				depthBuffer[off] = z1;
			}
			
			off++;
			z1 += z2;
			i += j7;
			j += l7;
			hsl1 += hsl2;
			rgb1 = hslToRgbMap[hsl1 >> 8];
			if(texelPos((j & 0x3f80) + (i >> 7)) >= src.length)
				return;
			rgb2 = src[texelPos((j & 0x3f80) + (i >> 7))];
			if (z1 < depthBuffer[off] || depthRender(z1, off)) {
				dst[off] = (((rgb1 >> 16 & 0xff) * (rgb2 >> 17 & 0x7f) << 11) / 3 & 0xff0000) + (((rgb1 >> 8 & 0xff) * (rgb2 >> 9 & 0x7f) << 3) / 3 & 0xff00) + (((rgb1 & 0xff) * (rgb2 >> 1 & 0x7f) >> 5) / 3 & 0xff);
				depthBuffer[off] = z1;
			}
			
			off++;
			z1 += z2;
			i += j7;
			j += l7;
			hsl1 += hsl2;
			rgb1 = hslToRgbMap[hsl1 >> 8];
			if(texelPos((j & 0x3f80) + (i >> 7)) >= src.length)
				return;
			rgb2 = src[texelPos((j & 0x3f80) + (i >> 7))];
			if (z1 < depthBuffer[off] || depthRender(z1, off)) {
				dst[off] = (((rgb1 >> 16 & 0xff) * (rgb2 >> 17 & 0x7f) << 11) / 3 & 0xff0000) + (((rgb1 >> 8 & 0xff) * (rgb2 >> 9 & 0x7f) << 3) / 3 & 0xff00) + (((rgb1 & 0xff) * (rgb2 >> 1 & 0x7f) >> 5) / 3 & 0xff);
				depthBuffer[off] = z1;
			}
			
			off++;
			z1 += z2;
			i += j7;
			j += l7;
			hsl1 += hsl2;
			rgb1 = hslToRgbMap[hsl1 >> 8];
			if(texelPos((j & 0x3f80) + (i >> 7)) >= src.length)
				return;
			rgb2 = src[texelPos((j & 0x3f80) + (i >> 7))];
			if (z1 < depthBuffer[off] || depthRender(z1, off)) {
				dst[off] = (((rgb1 >> 16 & 0xff) * (rgb2 >> 17 & 0x7f) << 11) / 3 & 0xff0000) + (((rgb1 >> 8 & 0xff) * (rgb2 >> 9 & 0x7f) << 3) / 3 & 0xff00) + (((rgb1 & 0xff) * (rgb2 >> 1 & 0x7f) >> 5) / 3 & 0xff);
				depthBuffer[off] = z1;
			}
			
			off++;
			z1 += z2;
			i = j4;
			j = l4;
			hsl1 += hsl2;
			l1 += k2;
			i2 += l2;
			j2 += i3;
			int i6 = j2 >> 14;
			if(i6 != 0) {
				j4 = l1 / i6;
				l4 = i2 / i6;
				if(j4 < 7) {
					j4 = 7;
				} else if(j4 > 16256) {
					j4 = 16256;
				}
			}
			j7 = j4 - i >> 3;
			l7 = l4 - j >> 3;
		}
		for(k3 = x2 - x1 & 7; k3-- > 0; ) {
			rgb1 = hslToRgbMap[hsl1 >> 8];
			int pos = texelPos((j & 0x3f80) + (i >> 7));
			if(pos >= src.length)
				return;
			rgb2 = src[pos];
			if (z1 < depthBuffer[off] || depthRender(z1, off)) {
				dst[off] = (((rgb1 >> 16 & 0xff) * (rgb2 >> 17 & 0x7f) << 11) / 3 & 0xff0000) + (((rgb1 >> 8 & 0xff) * (rgb2 >> 9 & 0x7f) << 3) / 3 & 0xff00) + (((rgb1 & 0xff) * (rgb2 >> 1 & 0x7f) >> 5) / 3 & 0xff);
				depthBuffer[off] = z1;
			}
			
			off++;
			z1 += z2;
			i += j7;
			j += l7;
			hsl1 += hsl2;
		}
	}
	
	
	public static void drawHDTexturedTriangle(int y1, int y2, int y3, int x1, int x2, int x3, float z1, float z2, float z3, int l1, int l2, int l3, int tx1, int tx2, int tx3, int ty1, int ty2, int ty3, int tz1, int tz2, int tz3, int[] ai) {
		
		l1 &= 0x7f;
		l2 &= 0x7f;
		l3 &= 0x7f;
		//XXX: Temp fix.
		if(l1 > 70) {
			l1 -= 40;
		}
		if(l2 > 70) {
			l2 -= 40;
		}
		if(l3 > 70) {
			l3 -= 40;
		}
		l1 = 0x7f - l1 << 1;
		l2 = 0x7f - l2 << 1;
		l3 = 0x7f - l3 << 1;
		tx2 = tx1 - tx2;
		ty2 = ty1 - ty2;
		tz2 = tz1 - tz2;
		tx3 -= tx1;
		ty3 -= ty1;
		tz3 -= tz1;
		int l4 = ((tx3 * ty1) - (ty3 * tx1)) * Scene.focalLength << 5;
		int i5 = ((ty3 * tz1) - (tz3 * ty1)) << 8;
		int j5 = ((tz3 * tx1) - (tx3 * tz1)) << 5;
		int k5 = ((tx2 * ty1) - (ty2 * tx1)) * Scene.focalLength << 5;
		int l5 = ((ty2 * tz1) - (tz2 * ty1)) << 8;
		int i6 = ((tz2 * tx1) - (tx2 * tz1)) << 5;
		int j6 = ((ty2 * tx3) - (tx2 * ty3)) * Scene.focalLength << 5;
		int k6 = ((tz2 * ty3) - (ty2 * tz3)) << 8;
		int l6 = ((tx2 * tz3) - (tz2 * tx3)) << 5;
		int i7 = 0;
		int j7 = 0;
		float dz1 = 0;
		if(y2 != y1) {
			i7 = (x2 - x1 << 16) / (y2 - y1);
			j7 = (l2 - l1 << 16) / (y2 - y1);
			dz1 = (z2 - z1) / (y2 - y1);
		}
		int k7 = 0;
		int l7 = 0;
		float dz2 = 0;
		if(y3 != y2) {
			k7 = (x3 - x2 << 16) / (y3 - y2);
			l7 = (l3 - l2 << 16) / (y3 - y2);
			dz2 = (z3 - z2) / (y3 - y2);
		}
		
		int i8 = 0;
		int j8 = 0;
		float dz3 = 0;
		if(y3 != y1) {
			i8 = (x1 - x3 << 16) / (y1 - y3);
			j8 = (l1 - l3 << 16) / (y1 - y3);
			dz3 = (z1 - z3) / (y1 - y3);
		}
		if(y1 <= y2 && y1 <= y3) {
			if(y1 >= clipEndY) {
				return;
			}
			if(y2 > clipEndY) {
				y2 = clipEndY;
			}
			if(y3 > clipEndY) {
				y3 = clipEndY;
			}
			if(y2 < y3) {
				x3 = x1 <<= 16;
				l3 = l1 <<= 16;
				z3 = z1;
				if(y1 < 0) {
					x3 -= i8 * y1;
					x1 -= i7 * y1;
					z3 -= dz3 * y1;
					z1 -= dz1 * y1;
					l3 -= j8 * y1;
					l1 -= j7 * y1;
					y1 = 0;
				}
				x2 <<= 16;
				l2 <<= 16;
				if(y2 < 0) {
					x2 -= k7 * y2;
					l2 -= l7 * y2;
					z2 -= dz2 * y2;
					y2 = 0;
				}
				int k8 = y1 - viewport.centerY;
				l4 += j5 * k8;
				k5 += i6 * k8;
				j6 += l6 * k8;
				if(y1 != y2 && i8 < i7 || y1 == y2 && i8 > k7) {
					y3 -= y2;
					y2 -= y1;
					y1 = viewport.scanOffsets[y1];
					while(--y2 >= 0) {
						drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y1, x3 >> 16, x1 >> 16, z3, z1, l3, l1, l4, k5, j6, i5, l5, k6);
						x3 += i8;
						x1 += i7;
						z3 += dz3;
						z1 += dz1;
						l3 += j8;
						l1 += j7;
						y1 += Rasterizer2D.canvasWidth;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					while(--y3 >= 0) {
						drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y1, x3 >> 16, x2 >> 16, z3, z2, l3, l2, l4, k5, j6, i5, l5, k6);
						x3 += i8;
						x2 += k7;
						z3 += dz3;
						z2 += dz2;
						l3 += j8;
						l2 += l7;
						y1 += Rasterizer2D.canvasWidth;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					return;
				}
				y3 -= y2;
				y2 -= y1;
				y1 = viewport.scanOffsets[y1];
				while(--y2 >= 0) {
					drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y1, x1 >> 16, x3 >> 16, z1, z3, l1, l3, l4, k5, j6, i5, l5, k6);
					x3 += i8;
					x1 += i7;
					z3 += dz3;
					z1 += dz1;
					l3 += j8;
					l1 += j7;
					y1 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while(--y3 >= 0) {
					drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y1, x2 >> 16, x3 >> 16, z2, z3, l2, l3, l4, k5, j6, i5, l5, k6);
					x3 += i8;
					x2 += k7;
					z3 += dz3;
					z2 += dz2;
					l3 += j8;
					l2 += l7;
					y1 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			x2 = x1 <<= 16;
			l2 = l1 <<= 16;
			z2 = z1;
			if(y1 < 0) {
				x2 -= i8 * y1;
				x1 -= i7 * y1;
				z2 -= dz3 * y1;
				z1 -= dz1 * y1;
				l2 -= j8 * y1;
				l1 -= j7 * y1;
				y1 = 0;
			}
			x3 <<= 16;
			l3 <<= 16;
			if(y3 < 0) {
				x3 -= k7 * y3;
				l3 -= l7 * y3;
				z3 -= dz2 * y3;
				y3 = 0;
			}
			int l8 = y1 - viewport.centerY;
			l4 += j5 * l8;
			k5 += i6 * l8;
			j6 += l6 * l8;
			if(y1 != y3 && i8 < i7 || y1 == y3 && k7 > i7) {
				y2 -= y3;
				y3 -= y1;
				y1 = viewport.scanOffsets[y1];
				while(--y3 >= 0) {
					drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y1, x2 >> 16, x1 >> 16, z2, z1, l2, l1, l4, k5, j6, i5, l5, k6);
					x2 += i8;
					x1 += i7;
					z2 += dz3;
					z1 += dz1;
					l2 += j8;
					l1 += j7;
					y1 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while(--y2 >= 0) {
					drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y1, x3 >> 16, x1 >> 16, z3, z1, l3, l1, l4, k5, j6, i5, l5, k6);
					x3 += k7;
					x1 += i7;
					z3 += dz2;
					z1 += dz1;
					l3 += l7;
					l1 += j7;
					y1 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			y2 -= y3;
			y3 -= y1;
			y1 = viewport.scanOffsets[y1];
			while(--y3 >= 0) {
				drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y1, x1 >> 16, x2 >> 16, z1, z2, l1, l2, l4, k5, j6, i5, l5, k6);
				x2 += i8;
				x1 += i7;
				z2 += dz3;
				z1 += dz1;
				l2 += j8;
				l1 += j7;
				y1 += Rasterizer2D.canvasWidth;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while(--y2 >= 0) {
				drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y1, x1 >> 16, x3 >> 16, z1, z3, l1, l3, l4, k5, j6, i5, l5, k6);
				x3 += k7;
				x1 += i7;
				z2 += dz3;
				z1 += dz1;
				l3 += l7;
				l1 += j7;
				y1 += Rasterizer2D.canvasWidth;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		if(y2 <= y3) {
			if(y2 >= clipEndY) {
				return;
			}
			if(y3 > clipEndY) {
				y3 = clipEndY;
			}
			if(y1 > clipEndY) {
				y1 = clipEndY;
			}
			if(y3 < y1) {
				x1 = x2 <<= 16;
				l1 = l2 <<= 16;
				z1 = z2;
				
				if(y2 < 0) {
					x1 -= i7 * y2;
					x2 -= k7 * y2;
					z1 -= dz1 * y2;
					z2 -= dz2 * y2;
					l1 -= j7 * y2;
					l2 -= l7 * y2;
					y2 = 0;
				}
				x3 <<= 16;
				l3 <<= 16;
				
				if(y3 < 0) {
					x3 -= i8 * y3;
					l3 -= j8 * y3;
					z3 -= dz3 * y3;
					
					y3 = 0;
				}
				int i9 = y2 - viewport.centerY;
				l4 += j5 * i9;
				k5 += i6 * i9;
				j6 += l6 * i9;
				if(y2 != y3 && i7 < k7 || y2 == y3 && i7 > i8) {
					y1 -= y3;
					y3 -= y2;
					y2 = viewport.scanOffsets[y2];
					while(--y3 >= 0) {
						drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y2, x1 >> 16, x2 >> 16, z1, z2, l1, l2, l4, k5, j6, i5, l5, k6);
						x1 += i7;
						x2 += k7;
						z1 += dz1;
						z2 += dz2;
						l1 += j7;
						l2 += l7;
						y2 += Rasterizer2D.canvasWidth;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					while(--y1 >= 0) {
						drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y2, x1 >> 16, x3 >> 16, z1, z3, l1, l3, l4, k5, j6, i5, l5, k6);
						x1 += i7;
						x3 += i8;
						z1 += dz1;
						z3 += dz3;
						l1 += j7;
						l3 += j8;
						y2 += Rasterizer2D.canvasWidth;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					return;
				}
				y1 -= y3;
				y3 -= y2;
				y2 = viewport.scanOffsets[y2];
				while(--y3 >= 0) {
					drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y2, x2 >> 16, x1 >> 16, z2, z1, l2, l1, l4, k5, j6, i5, l5, k6);
					x1 += i7;
					x2 += k7;
					z1 += dz1;
					z2 += dz2;
					l1 += j7;
					l2 += l7;
					y2 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while(--y1 >= 0) {
					drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y2, x3 >> 16, x1 >> 16, z3, z1, l3, l1, l4, k5, j6, i5, l5, k6);
					x1 += i7;
					x3 += i8;
					z1 += dz1;
					z3 += dz3;
					l1 += j7;
					l3 += j8;
					y2 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			x3 = x2 <<= 16;
			l3 = l2 <<= 16;
			z3 = z2;
			
			if(y2 < 0) {
				x3 -= i7 * y2;
				x2 -= k7 * y2;
				z3 -= dz1 * y2;
				z2 -= dz2 * y2;
				l3 -= j7 * y2;
				l2 -= l7 * y2;
				y2 = 0;
			}
			x1 <<= 16;
			l1 <<= 16;
			if(y1 < 0) {
				x1 -= i8 * y1;
				l1 -= j8 * y1;
				z1 -= dz3 * y1;
				
				y1 = 0;
			}
			int j9 = y2 - viewport.centerY;
			l4 += j5 * j9;
			k5 += i6 * j9;
			j6 += l6 * j9;
			if(i7 < k7) {
				y3 -= y1;
				y1 -= y2;
				y2 = viewport.scanOffsets[y2];
				while(--y1 >= 0) {
					drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y2, x3 >> 16, x2 >> 16, z3, z2, l3, l2, l4, k5, j6, i5, l5, k6);
					x3 += i7;
					x2 += k7;
					z3 += dz1;
					z2 += dz2;
					l3 += j7;
					l2 += l7;
					y2 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while(--y3 >= 0) {
					drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y2, x1 >> 16, x2 >> 16, z1, z2, l1, l2, l4, k5, j6, i5, l5, k6);
					x1 += i8;
					x2 += k7;
					z1 += dz3;
					z2 += dz2;
					l1 += j8;
					l2 += l7;
					y2 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			y3 -= y1;
			y1 -= y2;
			y2 = viewport.scanOffsets[y2];
			while(--y1 >= 0) {
				drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y2, x2 >> 16, x3 >> 16, z2, z3, l2, l3, l4, k5, j6, i5, l5, k6);
				x3 += i7;
				x2 += k7;
				z3 += dz1;
				z2 += dz2;
				l3 += j7;
				l2 += l7;
				y2 += Rasterizer2D.canvasWidth;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while(--y3 >= 0) {
				drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y2, x2 >> 16, x1 >> 16, z2, z1, l2, l1, l4, k5, j6, i5, l5, k6);
				x1 += i8;
				x2 += k7;
				z1 += dz3;
				z2 += dz2;
				l1 += j8;
				l2 += l7;
				y2 += Rasterizer2D.canvasWidth;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		if(y3 >= clipEndY) {
			return;
		}
		if(y1 > clipEndY) {
			y1 = clipEndY;
		}
		if(y2 > clipEndY) {
			y2 = clipEndY;
		}
		if(y1 < y2) {
			x2 = x3 <<= 16;
			z2 = z3;
			l2 = l3 <<= 16;
			if(y3 < 0) {
				x2 -= k7 * y3;
				x3 -= i8 * y3;
				z2 -= dz2 * y3;
				z3 -= dz3 * y3;
				l2 -= l7 * y3;
				l3 -= j8 * y3;
				y3 = 0;
			}
			x1 <<= 16;
			l1 <<= 16;
			if(y1 < 0) {
				x1 -= i7 * y1;
				z1 -= dz1 * y1;
				l1 -= j7 * y1;
				y1 = 0;
			}
			int k9 = y3 - viewport.centerY;
			l4 += j5 * k9;
			k5 += i6 * k9;
			j6 += l6 * k9;
			if(k7 < i8) {
				y2 -= y1;
				y1 -= y3;
				y3 = viewport.scanOffsets[y3];
				while(--y1 >= 0) {
					drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y3, x2 >> 16, x3 >> 16, z2, z3, l2, l3, l4, k5, j6, i5, l5, k6);
					x2 += k7;
					x3 += i8;
					z2 += dz2;
					z3 += dz3;
					l2 += l7;
					l3 += j8;
					y3 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while(--y2 >= 0) {
					drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y3, x2 >> 16, x1 >> 16, z2, z1, l2, l1, l4, k5, j6, i5, l5, k6);
					x2 += k7;
					x1 += i7;
					z2 += dz2;
					z1 += dz1;
					l2 += l7;
					l1 += j7;
					y3 += Rasterizer2D.canvasWidth;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			y2 -= y1;
			y1 -= y3;
			y3 = viewport.scanOffsets[y3];
			while(--y1 >= 0) {
				drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y3, x3 >> 16, x2 >> 16, z3, z2, l3, l2, l4, k5, j6, i5, l5, k6);
				x2 += k7;
				x3 += i8;
				z2 += dz2;
				z3 += dz3;
				l2 += l7;
				l3 += j8;
				y3 += Rasterizer2D.canvasWidth;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while(--y2 >= 0) {
				drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y3, x1 >> 16, x2 >> 16, z1, z2, l1, l2, l4, k5, j6, i5, l5, k6);
				x2 += k7;
				x1 += i7;
				z2 += dz2;
				z1 += dz1;
				l2 += l7;
				l1 += j7;
				y3 += Rasterizer2D.canvasWidth;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		x1 = x3 <<= 16;
		l1 = l3 <<= 16;
		z1 = z3;
		
		if(y3 < 0) {
			x1 -= k7 * y3;
			x3 -= i8 * y3;
			z1 -= dz2 * y3;
			z3 -= dz3 * y3;
			l1 -= l7 * y3;
			l3 -= j8 * y3;
			y3 = 0;
		}
		x2 <<= 16;
		
		l2 <<= 16;
		if(y2 < 0) {
			x2 -= i7 * y2;
			z2 -= dz1 * y2;
			l2 -= j7 * y2;
			y2 = 0;
		}
		int l9 = y3 - viewport.centerY;
		l4 += j5 * l9;
		k5 += i6 * l9;
		j6 += l6 * l9;
		if(k7 < i8) {
			y1 -= y2;
			y2 -= y3;
			y3 = viewport.scanOffsets[y3];
			while(--y2 >= 0) {
				drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y3, x1 >> 16, x3 >> 16, z1, z3, l1, l3, l4, k5, j6, i5, l5, k6);
				x1 += k7;
				x3 += i8;
				z1 += dz2;
				z3 += dz3;
				l1 += l7;
				l3 += j8;
				y3 += Rasterizer2D.canvasWidth;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while(--y1 >= 0) {
				drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y3, x2 >> 16, x3 >> 16, z2, z3, l2, l3, l4, k5, j6, i5, l5, k6);
				x2 += i7;
				x3 += i8;
				z2 += dz1;
				z3 += dz3;
				l2 += j7;
				l3 += j8;
				y3 += Rasterizer2D.canvasWidth;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		y1 -= y2;
		y2 -= y3;
		y3 = viewport.scanOffsets[y3];
		while(--y2 >= 0) {
			drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y3, x3 >> 16, x1 >> 16, z3, z1, l3, l1, l4, k5, j6, i5, l5, k6);
			x1 += k7;
			x3 += i8;
			z1 += dz2;
			z3 += dz3;
			l1 += l7;
			l3 += j8;
			y3 += Rasterizer2D.canvasWidth;
			l4 += j5;
			k5 += i6;
			j6 += l6;
		}
		while(--y1 >= 0) {
			drawHDTexturedScanline(Rasterizer2D.canvasRaster, ai, y3, x3 >> 16, x2 >> 16, z3, z2, l3, l2, l4, k5, j6, i5, l5, k6);
			x2 += i7;
			x3 += i8;
			z2 += dz1;
			z3 += dz3;
			l2 += j7;
			l3 += j8;
			y3 += Rasterizer2D.canvasWidth;
			l4 += j5;
			k5 += i6;
			j6 += l6;
		}
	}
	
	private static void drawHDTexturedScanline(int ai[], int ai1[], int k, int x1, int x2, float z1, float z2, int l1, int l2, int a1, int i2, int j2, int k2, int a2, int i3) {
		int i = 0;// was parameter
		int j = 0;// was parameter
		if(x1 >= x2) {
			return;
		}
		z2 = (z2 - z1) / (x2 - x1);
		int dl = (l2 - l1) / (x2 - x1);
		int n;
		if(clippedScan) {
			if(x2 > Rasterizer2D.clipEndX) {
				x2 = Rasterizer2D.clipEndX;
			}
			if(x1 < 0) {
				z1 -= x1 * z2;
				l1 -= x1 * dl;
				x1 = 0;
			}
		}
		if(x1 >= x2) {
			return;
		}
		n = x2 - x1 >> 3;
		k += x1;
		int j4 = 0;
		int l4 = 0;
		int l6 = x1 - viewport.centerX;
		a1 += (k2 >> 3) * l6;
		i2 += (a2 >> 3) * l6;
		j2 += (i3 >> 3) * l6;
		int l5 = j2 >> 14;
		if(l5 != 0) {
			i = a1 / l5;
			j = i2 / l5;
			if(i < 0) {
				i = 0;
			} else if(i > 16256) {
				i = 16256;
			}
		}
		a1 += k2;
		i2 += a2;
		j2 += i3;
		l5 = j2 >> 14;
		if(l5 != 0) {
			j4 = a1 / l5;
			l4 = i2 / l5;
			if(j4 < 7) {
				j4 = 7;
			} else if(j4 > 16256) {
				j4 = 16256;
			}
		}
		int j7 = j4 - i >> 3;
		int l7 = l4 - j >> 3;
		while(n-- > 0) {
			int i9;
			int l;
			if((i9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
				l = l1 >> 16;
				if (z1 < depthBuffer[k] || depthRender(z1, k)) {
					ai[k] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;
					depthBuffer[k] = z1;
				}
			}
			k++;
			i += j7;
			z1 += z2;
			j += l7;
			l1 += dl;
			if((i9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
				l = l1 >> 16;
				if (z1 < depthBuffer[k] || depthRender(z1, k)) {
					ai[k] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;
					depthBuffer[k] = z1;
				}
			}
			k++;
			i += j7;
			z1 += z2;
			j += l7;
			l1 += dl;
			if((i9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
				l = l1 >> 16;
				if (z1 < depthBuffer[k] || depthRender(z1, k)) {
					ai[k] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;
					depthBuffer[k] = z1;
				}
			}
			k++;
			i += j7;
			z1 += z2;
			j += l7;
			l1 += dl;
			if((i9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
				l = l1 >> 16;
				if (z1 < depthBuffer[k] || depthRender(z1, k)) {
					ai[k] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;
					depthBuffer[k] = z1;
				}
			}
			k++;
			i += j7;
			z1 += z2;
			j += l7;
			l1 += dl;
			if((i9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
				l = l1 >> 16;
				if (z1 < depthBuffer[k] || depthRender(z1, k)) {
					ai[k] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;
					depthBuffer[k] = z1;
				}
			}
			k++;
			i += j7;
			z1 += z2;
			j += l7;
			l1 += dl;
			if((i9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
				l = l1 >> 16;
				if (z1 < depthBuffer[k] || depthRender(z1, k)) {
					ai[k] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;
					depthBuffer[k] = z1;
				}
			}
			k++;
			i += j7;
			z1 += z2;
			j += l7;
			l1 += dl;
			if((i9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
				l = l1 >> 16;
				if (z1 < depthBuffer[k] || depthRender(z1, k)) {
					ai[k] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;
					depthBuffer[k] = z1;
				}
			}
			k++;
			i += j7;
			z1 += z2;
			j += l7;
			l1 += dl;
			if((i9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
				l = l1 >> 16;
				if (z1 < depthBuffer[k] || depthRender(z1, k)) {
					ai[k] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;
					depthBuffer[k] = z1;
				}
			}
			k++;
			i += j7;
			z1 += z2;
			j += l7;
			l1 += dl;
			a1 += k2;
			i2 += a2;
			j2 += i3;
			int j6 = j2 >> 14;
			if(j6 != 0) {
				j4 = a1 / j6;
				l4 = i2 / j6;
				if(j4 < 7) {
					j4 = 7;
				} else if(j4 > 16256) {
					j4 = 16256;
				}
			}
			j7 = j4 - i >> 3;
			l7 = l4 - j >> 3;
			l1 += dl;
		}
		for(int l3 = x2 - x1 & 7; l3-- > 0; ) {
			int j9;
			int l;
			if((j9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
				l = l1 >> 16;
				if (z1 < depthBuffer[k] || depthRender(z1, k)) {
					ai[k] = ((j9 & 0xff00ff) * l & ~0xff00ff) + ((j9 & 0xff00) * l & 0xff0000) >> 8;
					depthBuffer[k] = z1;
				}
			}
			k++;
			z1 += z2;
			i += j7;
			j += l7;
			l1 += dl;
		}
		
	}
	
	public static void drawTexturedTriangle(int y1, int y2, int y3, int x1, int x2, int x3, float z1, float z2, float z3, int hsl1, int hsl2, int hsl3, int tx1, int tx2, int tx3, int ty1, int ty2, int ty3, int tz1, int tz2, int tz3, int tex, boolean force, boolean mipmap) {
		if(tex >= 0 && tex < MaterialType.textures.length) {
			
			MaterialType def = MaterialType.textures[tex];
			Texture texture = Texture.get(tex);
			
			if(def != null && !def.aBoolean1223 && !force && texture != null) {
				drawGouraudTriangle(y1, y2, y3, x1, x2, x3, z1, z2, z3, hsl1, hsl2, hsl3);
				//drawGouraudTriangle317(y1, y2, y3, x1, x2, x3, hsl1, hsl2, hsl3);
				return;
			}
			
			if(texture != null) {
				textureMissing = false;
				if(mipmap) {
					int area = (x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1) >> 1;
					if(area < 0) {
						area = -area;
					}
					double a = area / 8192d;
					a = Math.pow(a, 0.3);
					if(a >= 0.875) {
						textureMipmap = 0;
					} else if(a >= 0.625) {
						textureMipmap = 1;
					} else if(a >= 0.375) {
						textureMipmap = 2;
					} else if(a >= 0.1875) {
						textureMipmap = 3;
					} else if(a >= 0.09375) {
						textureMipmap = 4;
					} else if(a >= 0.046875) {
						textureMipmap = 5;
					} else if(a >= 0.0234375) {
						textureMipmap = 6;
					} else {
						textureMipmap = 7;
					}
				} else {
					textureMipmap = 0;
				}
				int[] texels = texture.getTexels(textureMipmap);
				
				if(force)
					drawMaterializedTriangle(y1, y2, y3, x1, x2, x3, z1, z2, z3, hsl1, hsl2, hsl3, tx1, tx2, tx3, ty1, ty2, ty3, tz1, tz2, tz3, texels);
				else //if(textured) {
					drawHDTexturedTriangle(y1, y2, y3, x1, x2, x3, z1, z2, z3, hsl1, hsl2, hsl3, tx1, tx2, tx3, ty1, ty2, ty3, tz1, tz2, tz3, texels);
				///} else {
				//	drawMaterializedTriangle(y1, y2, y3, x1, x2, x3, z1, z2, z3, hsl1, hsl2, hsl3, tx1, tx2, tx3, ty1, ty2, ty3, tz1, tz2, tz3, texels);
				//}
				return;
			}
		}
		
		textureMissing = true;
		drawGouraudTriangle(y1, y2, y3, x1, x2, x3, z1, z2, z3, hsl1, hsl2, hsl3);
	}
	
	private static int texelPos(int defaultIndex) {
		int x = defaultIndex & 127;
		int y = defaultIndex >> 7;
		x >>= textureMipmap;
		y >>= textureMipmap;
		return x + (y << (7 - textureMipmap));
	}
	
	public static void drawGouraudTriangle(int y1, int y2, int y3, int x1, int x2, int x3, float z1, float z2, float z3, int hsl1, int hsl2, int hsl3) {
		if(!textured) {
			drawGouraudTriangle317(y1, y2, y3, x1, x2, x3, hsl1, hsl2, hsl3);
			return;
		}
		final int rgb1 = hslToRgbMap[hsl1];
		final int rgb2 = hslToRgbMap[hsl2];
		final int rgb3 = hslToRgbMap[hsl3];
		int r1 = rgb1 >> 16 & 0xff;
		int g1 = rgb1 >> 8 & 0xff;
		int b1 = rgb1 & 0xff;
		int r2 = rgb2 >> 16 & 0xff;
		int g2 = rgb2 >> 8 & 0xff;
		int b2 = rgb2 & 0xff;
		int r3 = rgb3 >> 16 & 0xff;
		int g3 = rgb3 >> 8 & 0xff;
		int b3 = rgb3 & 0xff;
		int dx1 = 0;
		float dz1 = 0;
		int dr1 = 0;
		int dg1 = 0;
		int db1 = 0;
		if(y2 != y1) {
			final int d = (y2 - y1);
			dx1 = (x2 - x1 << 16) / d;
			dz1 = (z2 - z1) / d;
			dr1 = (r2 - r1 << 16) / d;
			dg1 = (g2 - g1 << 16) / d;
			db1 = (b2 - b1 << 16) / d;
		}
		int dx2 = 0;
		float dz2 = 0;
		int dr2 = 0;
		int dg2 = 0;
		int db2 = 0;
		if(y3 != y2) {
			final int d = (y3 - y2);
			dx2 = (x3 - x2 << 16) / d;
			dz2 = (z3 - z2) / d;
			dr2 = (r3 - r2 << 16) / d;
			dg2 = (g3 - g2 << 16) / d;
			db2 = (b3 - b2 << 16) / d;
		}
		int dx3 = 0;
		float dz3 = 0;
		int dr3 = 0;
		int dg3 = 0;
		int db3 = 0;
		if(y3 != y1) {
			final int d = (y1 - y3);
			dx3 = (x1 - x3 << 16) / d;
			dz3 = (z1 - z3) / d;
			dr3 = (r1 - r3 << 16) / d;
			dg3 = (g1 - g3 << 16) / d;
			db3 = (b1 - b3 << 16) / d;
		}
		if(y1 <= y2 && y1 <= y3) {
			if(y1 >= viewport.height) {
				return;
			}
			if(y2 > viewport.height) {
				y2 = viewport.height;
			}
			if(y3 > viewport.height) {
				y3 = viewport.height;
			}
			if(y2 < y3) {
				x3 = x1 <<= 16;
				z3 = z1;
				r3 = r1 <<= 16;
				g3 = g1 <<= 16;
				b3 = b1 <<= 16;
				if(y1 < 0) {
					x3 -= dx3 * y1;
					x1 -= dx1 * y1;
					z3 -= dz3 * y1;
					z1 -= dz1 * y1;
					r3 -= dr3 * y1;
					g3 -= dg3 * y1;
					b3 -= db3 * y1;
					r1 -= dr1 * y1;
					g1 -= dg1 * y1;
					b1 -= db1 * y1;
					y1 = 0;
				}
				x2 <<= 16;
				r2 <<= 16;
				g2 <<= 16;
				b2 <<= 16;
				if(y2 < 0) {
					x2 -= dx2 * y2;
					z2 -= dz2 * y2;
					r2 -= dr2 * y2;
					g2 -= dg2 * y2;
					b2 -= db2 * y2;
					y2 = 0;
				}
				if(y1 != y2 && dx3 < dx1 || y1 == y2 && dx3 > dx2) {
					y3 -= y2;
					y2 -= y1;
					for(y1 = viewport.scanOffsets[y1]; --y2 >= 0; y1 += Rasterizer2D.canvasWidth) {
						drawGouraudScanline(Rasterizer2D.canvasRaster, y1, x3 >> 16, x1 >> 16, z3, z1, r3, g3, b3, r1, g1, b1);
						x3 += dx3;
						x1 += dx1;
						z3 += dz3;
						z1 += dz1;
						r3 += dr3;
						g3 += dg3;
						b3 += db3;
						r1 += dr1;
						g1 += dg1;
						b1 += db1;
					}
					while(--y3 >= 0) {
						drawGouraudScanline(Rasterizer2D.canvasRaster, y1, x3 >> 16, x2 >> 16, z3, z2, r3, g3, b3, r2, g2, b2);
						x3 += dx3;
						x2 += dx2;
						z3 += dz3;
						z2 += dz2;
						r3 += dr3;
						g3 += dg3;
						b3 += db3;
						r2 += dr2;
						g2 += dg2;
						b2 += db2;
						y1 += Rasterizer2D.canvasWidth;
					}
					return;
				}
				y3 -= y2;
				y2 -= y1;
				for(y1 = viewport.scanOffsets[y1]; --y2 >= 0; y1 += Rasterizer2D.canvasWidth) {
					drawGouraudScanline(Rasterizer2D.canvasRaster, y1, x1 >> 16, x3 >> 16, z1, z3, r1, g1, b1, r3, g3, b3);
					x3 += dx3;
					x1 += dx1;
					z3 += dz3;
					z1 += dz1;
					r3 += dr3;
					g3 += dg3;
					b3 += db3;
					r1 += dr1;
					g1 += dg1;
					b1 += db1;
				}
				while(--y3 >= 0) {
					drawGouraudScanline(Rasterizer2D.canvasRaster, y1, x2 >> 16, x3 >> 16, z2, z3, r2, g2, b2, r3, g3, b3);
					x3 += dx3;
					x2 += dx2;
					z3 += dz3;
					z2 += dz2;
					r3 += dr3;
					g3 += dg3;
					b3 += db3;
					r2 += dr2;
					g2 += dg2;
					b2 += db2;
					y1 += Rasterizer2D.canvasWidth;
				}
				return;
			}
			x2 = x1 <<= 16;
			z2 = z1;
			r2 = r1 <<= 16;
			g2 = g1 <<= 16;
			b2 = b1 <<= 16;
			if(y1 < 0) {
				x2 -= dx3 * y1;
				x1 -= dx1 * y1;
				z2 -= dz3 * y1;
				z1 -= dz1 * y1;
				r2 -= dr3 * y1;
				g2 -= dg3 * y1;
				b2 -= db3 * y1;
				r1 -= dr1 * y1;
				g1 -= dg1 * y1;
				b1 -= db1 * y1;
				y1 = 0;
			}
			x3 <<= 16;
			r3 <<= 16;
			g3 <<= 16;
			b3 <<= 16;
			if(y3 < 0) {
				x3 -= dx2 * y3;
				z3 -= dz2 * y3;
				r3 -= dr2 * y3;
				g3 -= dg2 * y3;
				b3 -= db2 * y3;
				y3 = 0;
			}
			if(y1 != y3 && dx3 < dx1 || y1 == y3 && dx2 > dx1) {
				y2 -= y3;
				y3 -= y1;
				for(y1 = viewport.scanOffsets[y1]; --y3 >= 0; y1 += Rasterizer2D.canvasWidth) {
					drawGouraudScanline(Rasterizer2D.canvasRaster, y1, x2 >> 16, x1 >> 16, z2, z1, r2, g2, b2, r1, g1, b1);
					x2 += dx3;
					x1 += dx1;
					z2 += dz3;
					z1 += dz1;
					r2 += dr3;
					g2 += dg3;
					b2 += db3;
					r1 += dr1;
					g1 += dg1;
					b1 += db1;
				}
				while(--y2 >= 0) {
					drawGouraudScanline(Rasterizer2D.canvasRaster, y1, x3 >> 16, x1 >> 16, z3, z1, r3, g3, b3, r1, g1, b1);
					x3 += dx2;
					x1 += dx1;
					z3 += dz2;
					z1 += dz1;
					r3 += dr2;
					g3 += dg2;
					b3 += db2;
					r1 += dr1;
					g1 += dg1;
					b1 += db1;
					y1 += Rasterizer2D.canvasWidth;
				}
				return;
			}
			y2 -= y3;
			y3 -= y1;
			for(y1 = viewport.scanOffsets[y1]; --y3 >= 0; y1 += Rasterizer2D.canvasWidth) {
				drawGouraudScanline(Rasterizer2D.canvasRaster, y1, x1 >> 16, x2 >> 16, z1, z2, r1, g1, b1, r2, g2, b2);
				x2 += dx3;
				x1 += dx1;
				z2 += dz3;
				z1 += dz1;
				r2 += dr3;
				g2 += dg3;
				b2 += db3;
				r1 += dr1;
				g1 += dg1;
				b1 += db1;
			}
			while(--y2 >= 0) {
				drawGouraudScanline(Rasterizer2D.canvasRaster, y1, x1 >> 16, x3 >> 16, z1, z3, r1, g1, b1, r3, g3, b3);
				x3 += dx2;
				x1 += dx1;
				z3 += dz2;
				z1 += dz1;
				r3 += dr2;
				g3 += dg2;
				b3 += db2;
				r1 += dr1;
				g1 += dg1;
				b1 += db1;
				y1 += Rasterizer2D.canvasWidth;
			}
			return;
		}
		if(y2 <= y3) {
			if(y2 >= viewport.height) {
				return;
			}
			if(y3 > viewport.height) {
				y3 = viewport.height;
			}
			if(y1 > viewport.height) {
				y1 = viewport.height;
			}
			if(y3 < y1) {
				x1 = x2 <<= 16;
				z1 = z2;
				r1 = r2 <<= 16;
				g1 = g2 <<= 16;
				b1 = b2 <<= 16;
				if(y2 < 0) {
					x1 -= dx1 * y2;
					x2 -= dx2 * y2;
					z1 -= dz1 * y2;
					z2 -= dz2 * y2;
					r1 -= dr1 * y2;
					g1 -= dg1 * y2;
					b1 -= db1 * y2;
					r2 -= dr2 * y2;
					g2 -= dg2 * y2;
					b2 -= db2 * y2;
					y2 = 0;
				}
				x3 <<= 16;
				r3 <<= 16;
				g3 <<= 16;
				b3 <<= 16;
				if(y3 < 0) {
					x3 -= dx3 * y3;
					z3 -= dz3 * y3;
					r3 -= dr3 * y3;
					g3 -= dg3 * y3;
					b3 -= db3 * y3;
					y3 = 0;
				}
				if(y2 != y3 && dx1 < dx2 || y2 == y3 && dx1 > dx3) {
					y1 -= y3;
					y3 -= y2;
					for(y2 = viewport.scanOffsets[y2]; --y3 >= 0; y2 += Rasterizer2D.canvasWidth) {
						drawGouraudScanline(Rasterizer2D.canvasRaster, y2, x1 >> 16, x2 >> 16, z1, z2, r1, g1, b1, r2, g2, b2);
						x1 += dx1;
						x2 += dx2;
						z1 += dz1;
						z2 += dz2;
						r1 += dr1;
						g1 += dg1;
						b1 += db1;
						r2 += dr2;
						g2 += dg2;
						b2 += db2;
					}
					while(--y1 >= 0) {
						drawGouraudScanline(Rasterizer2D.canvasRaster, y2, x1 >> 16, x3 >> 16, z1, z3, r1, g1, b1, r3, g3, b3);
						x1 += dx1;
						x3 += dx3;
						z1 += dz1;
						z3 += dz3;
						r1 += dr1;
						g1 += dg1;
						b1 += db1;
						r3 += dr3;
						g3 += dg3;
						b3 += db3;
						y2 += Rasterizer2D.canvasWidth;
					}
					return;
				}
				y1 -= y3;
				y3 -= y2;
				for(y2 = viewport.scanOffsets[y2]; --y3 >= 0; y2 += Rasterizer2D.canvasWidth) {
					drawGouraudScanline(Rasterizer2D.canvasRaster, y2, x2 >> 16, x1 >> 16, z2, z1, r2, g2, b2, r1, g1, b1);
					x1 += dx1;
					x2 += dx2;
					z1 += dz1;
					z2 += dz2;
					r1 += dr1;
					g1 += dg1;
					b1 += db1;
					r2 += dr2;
					g2 += dg2;
					b2 += db2;
				}
				while(--y1 >= 0) {
					drawGouraudScanline(Rasterizer2D.canvasRaster, y2, x3 >> 16, x1 >> 16, z3, z1, r3, g3, b3, r1, g1, b1);
					x1 += dx1;
					x3 += dx3;
					z1 += dz1;
					z3 += dz3;
					r1 += dr1;
					g1 += dg1;
					b1 += db1;
					r3 += dr3;
					g3 += dg3;
					b3 += db3;
					y2 += Rasterizer2D.canvasWidth;
				}
				return;
			}
			x3 = x2 <<= 16;
			z3 = z2;
			r3 = r2 <<= 16;
			g3 = g2 <<= 16;
			b3 = b2 <<= 16;
			if(y2 < 0) {
				x3 -= dx1 * y2;
				x2 -= dx2 * y2;
				z3 -= dz1 * y2;
				z2 -= dz2 * y2;
				r3 -= dr1 * y2;
				g3 -= dg1 * y2;
				b3 -= db1 * y2;
				r2 -= dr2 * y2;
				g2 -= dg2 * y2;
				b2 -= db2 * y2;
				y2 = 0;
			}
			x1 <<= 16;
			r1 <<= 16;
			g1 <<= 16;
			b1 <<= 16;
			if(y1 < 0) {
				x1 -= dx3 * y1;
				z1 -= dz3 * y1;
				r1 -= dr3 * y1;
				g1 -= dg3 * y1;
				b1 -= db3 * y1;
				y1 = 0;
			}
			if(dx1 < dx2) {
				y3 -= y1;
				y1 -= y2;
				for(y2 = viewport.scanOffsets[y2]; --y1 >= 0; y2 += Rasterizer2D.canvasWidth) {
					drawGouraudScanline(Rasterizer2D.canvasRaster, y2, x3 >> 16, x2 >> 16, z3, z2, r3, g3, b3, r2, g2, b2);
					x3 += dx1;
					x2 += dx2;
					z3 += dz1;
					z2 += dz2;
					r3 += dr1;
					g3 += dg1;
					b3 += db1;
					r2 += dr2;
					g2 += dg2;
					b2 += db2;
				}
				while(--y3 >= 0) {
					drawGouraudScanline(Rasterizer2D.canvasRaster, y2, x1 >> 16, x2 >> 16, z1, z2, r1, g1, b1, r2, g2, b2);
					x1 += dx3;
					x2 += dx2;
					z1 += dz3;
					z2 += dz2;
					r1 += dr3;
					g1 += dg3;
					b1 += db3;
					r2 += dr2;
					g2 += dg2;
					b2 += db2;
					y2 += Rasterizer2D.canvasWidth;
				}
				return;
			}
			y3 -= y1;
			y1 -= y2;
			for(y2 = viewport.scanOffsets[y2]; --y1 >= 0; y2 += Rasterizer2D.canvasWidth) {
				drawGouraudScanline(Rasterizer2D.canvasRaster, y2, x2 >> 16, x3 >> 16, z2, z3, r2, g2, b2, r3, g3, b3);
				x3 += dx1;
				x2 += dx2;
				z3 += dz1;
				z2 += dz2;
				r3 += dr1;
				g3 += dg1;
				b3 += db1;
				r2 += dr2;
				g2 += dg2;
				b2 += db2;
			}
			while(--y3 >= 0) {
				drawGouraudScanline(Rasterizer2D.canvasRaster, y2, x2 >> 16, x1 >> 16, z2, z1, r2, g2, b2, r1, g1, b1);
				x1 += dx3;
				x2 += dx2;
				z1 += dz3;
				z2 += dz2;
				r1 += dr3;
				g1 += dg3;
				b1 += db3;
				r2 += dr2;
				g2 += dg2;
				b2 += db2;
				y2 += Rasterizer2D.canvasWidth;
			}
			return;
		}
		if(y3 >= viewport.height) {
			return;
		}
		if(y1 > viewport.height) {
			y1 = viewport.height;
		}
		if(y2 > viewport.height) {
			y2 = viewport.height;
		}
		if(y1 < y2) {
			x2 = x3 <<= 16;
			z2 = z3;
			r2 = r3 <<= 16;
			g2 = g3 <<= 16;
			b2 = b3 <<= 16;
			if(y3 < 0) {
				x2 -= dx2 * y3;
				x3 -= dx3 * y3;
				z2 -= dz2 * y3;
				z3 -= dz3 * y3;
				r2 -= dr2 * y3;
				g2 -= dg2 * y3;
				b2 -= db2 * y3;
				r3 -= dr3 * y3;
				g3 -= dg3 * y3;
				b3 -= db3 * y3;
				y3 = 0;
			}
			x1 <<= 16;
			r1 <<= 16;
			g1 <<= 16;
			b1 <<= 16;
			if(y1 < 0) {
				x1 -= dx1 * y1;
				z1 -= dz1 * y1;
				r1 -= dr1 * y1;
				g1 -= dg1 * y1;
				b1 -= db1 * y1;
				y1 = 0;
			}
			if(dx2 < dx3) {
				y2 -= y1;
				y1 -= y3;
				for(y3 = viewport.scanOffsets[y3]; --y1 >= 0; y3 += Rasterizer2D.canvasWidth) {
					drawGouraudScanline(Rasterizer2D.canvasRaster, y3, x2 >> 16, x3 >> 16, z2, z3, r2, g2, b2, r3, g3, b3);
					x2 += dx2;
					x3 += dx3;
					z2 += dz2;
					z3 += dz3;
					r2 += dr2;
					g2 += dg2;
					b2 += db2;
					r3 += dr3;
					g3 += dg3;
					b3 += db3;
				}
				while(--y2 >= 0) {
					drawGouraudScanline(Rasterizer2D.canvasRaster, y3, x2 >> 16, x1 >> 16, z2, z1, r2, g2, b2, r1, g1, b1);
					x2 += dx2;
					x1 += dx1;
					z2 += dz2;
					z1 += dz1;
					r2 += dr2;
					g2 += dg2;
					b2 += db2;
					r1 += dr1;
					g1 += dg1;
					b1 += db1;
					y3 += Rasterizer2D.canvasWidth;
				}
				return;
			}
			y2 -= y1;
			y1 -= y3;
			for(y3 = viewport.scanOffsets[y3]; --y1 >= 0; y3 += Rasterizer2D.canvasWidth) {
				drawGouraudScanline(Rasterizer2D.canvasRaster, y3, x3 >> 16, x2 >> 16, z3, z2, r3, g3, b3, r2, g2, b2);
				x2 += dx2;
				x3 += dx3;
				z2 += dz2;
				z3 += dz3;
				r2 += dr2;
				g2 += dg2;
				b2 += db2;
				r3 += dr3;
				g3 += dg3;
				b3 += db3;
			}
			while(--y2 >= 0) {
				drawGouraudScanline(Rasterizer2D.canvasRaster, y3, x1 >> 16, x2 >> 16, z1, z2, r1, g1, b1, r2, g2, b2);
				x2 += dx2;
				x1 += dx1;
				z2 += dz2;
				z1 += dz1;
				r2 += dr2;
				g2 += dg2;
				b2 += db2;
				r1 += dr1;
				g1 += dg1;
				b1 += db1;
				y3 += Rasterizer2D.canvasWidth;
			}
			return;
		}
		x1 = x3 <<= 16;
		z1 = z3;
		r1 = r3 <<= 16;
		g1 = g3 <<= 16;
		b1 = b3 <<= 16;
		if(y3 < 0) {
			x1 -= dx2 * y3;
			x3 -= dx3 * y3;
			z1 -= dz2 * y3;
			z3 -= dz3 * y3;
			r1 -= dr2 * y3;
			g1 -= dg2 * y3;
			b1 -= db2 * y3;
			r3 -= dr3 * y3;
			g3 -= dg3 * y3;
			b3 -= db3 * y3;
			y3 = 0;
		}
		x2 <<= 16;
		r2 <<= 16;
		g2 <<= 16;
		b2 <<= 16;
		if(y2 < 0) {
			x2 -= dx1 * y2;
			z2 -= dz1 * y2;
			r2 -= dr1 * y2;
			g2 -= dg1 * y2;
			b2 -= db1 * y2;
			y2 = 0;
		}
		if(dx2 < dx3) {
			y1 -= y2;
			y2 -= y3;
			for(y3 = viewport.scanOffsets[y3]; --y2 >= 0; y3 += Rasterizer2D.canvasWidth) {
				drawGouraudScanline(Rasterizer2D.canvasRaster, y3, x1 >> 16, x3 >> 16, z1, z3, r1, g1, b1, r3, g3, b3);
				x1 += dx2;
				x3 += dx3;
				z1 += dz2;
				z3 += dz3;
				r1 += dr2;
				g1 += dg2;
				b1 += db2;
				r3 += dr3;
				g3 += dg3;
				b3 += db3;
			}
			while(--y1 >= 0) {
				drawGouraudScanline(Rasterizer2D.canvasRaster, y3, x2 >> 16, x3 >> 16, z2, z3, r2, g2, b2, r3, g3, b3);
				x2 += dx1;
				x3 += dx3;
				z2 += dz1;
				z3 += dz3;
				r2 += dr1;
				g2 += dg1;
				b2 += db1;
				r3 += dr3;
				g3 += dg3;
				b3 += db3;
				y3 += Rasterizer2D.canvasWidth;
			}
			return;
		}
		y1 -= y2;
		y2 -= y3;
		for(y3 = viewport.scanOffsets[y3]; --y2 >= 0; y3 += Rasterizer2D.canvasWidth) {
			drawGouraudScanline(Rasterizer2D.canvasRaster, y3, x3 >> 16, x1 >> 16, z3, z1, r3, g3, b3, r1, g1, b1);
			x1 += dx2;
			x3 += dx3;
			z1 += dz2;
			z3 += dz3;
			r1 += dr2;
			g1 += dg2;
			b1 += db2;
			r3 += dr3;
			g3 += dg3;
			b3 += db3;
		}
		while(--y1 >= 0) {
			drawGouraudScanline(Rasterizer2D.canvasRaster, y3, x3 >> 16, x2 >> 16, z3, z2, r3, g3, b3, r2, g2, b2);
			x2 += dx1;
			x3 += dx3;
			z2 += dz1;
			z3 += dz3;
			r2 += dr1;
			g2 += dg1;
			b2 += db1;
			r3 += dr3;
			g3 += dg3;
			b3 += db3;
			y3 += Rasterizer2D.canvasWidth;
		}
	}
	
	public static void drawGouraudScanline(int[] dest, int offset, int x1, int x2, float z1, float z2, int r1, int g1, int b1, int r2, int g2, int b2) {
		int n = x2 - x1;
		if(n <= 0) {
			return;
		}
		z2 = (z2 - z1) / n;
		r2 = (r2 - r1) / n;
		g2 = (g2 - g1) / n;
		b2 = (b2 - b1) / n;
		if(clippedScan) {
			if(x2 > viewport.width) {
				n -= x2 - viewport.width;
				x2 = viewport.width;
			}
			if(x1 < 0) {
				n = x2;
				z1 -= x1 * z2;
				r1 -= x1 * r2;
				g1 -= x1 * g2;
				b1 -= x1 * b2;
				x1 = 0;
			}
		}
		if(x1 < x2) {
			offset += x1;
			if(alphaFilter == 0) {
				while(--n >= 0) {
					if (z1 < depthBuffer[offset] || depthRender(z1, offset)) {
						dest[offset] = (r1 & 0xff0000) | (g1 >> 8 & 0xff00) | (b1 >> 16 & 0xff);
						depthBuffer[offset] = z1;
					}
					z1 += z2;
					r1 += r2;
					g1 += g2;
					b1 += b2;
					offset++;
				}
			} else {
				final int a1 = alphaFilter;
				final int a2 = 256 - alphaFilter;
				double alphaPercentage = (a1 / 256D);
				int rgb;
				int dst;
				while(--n >= 0) {
					rgb = (r1 & 0xff0000) | (g1 >> 8 & 0xff00) | (b1 >> 16 & 0xff);
					rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
					if (z1 < depthBuffer[offset] || depthRender(z1, offset)) {
						dst = dest[offset];
						dest[offset] = rgb + ((dst & 0xff00ff) * a1 >> 8 & 0xff00ff) + ((dst & 0xff00) * a1 >> 8 & 0xff00);
						depthBuffer[offset] = (int) (z1 + ((depthBuffer[offset] - z1) * alphaPercentage));
					}
					z1 += z2;
					r1 += r2;
					g1 += g2;
					b1 += b2;
					offset++;
				}
			}//wanna login real quick
		}
	}
	
	private static int adjustBrightness(int color, double amt) {
		double red = (color >> 16) / 256D;
		double green = (color >> 8 & 0xff) / 256D;
		double blue = (color & 0xff) / 256D;
		red = Math.pow(red, amt);
		green = Math.pow(green, amt);
		blue = Math.pow(blue, amt);
		final int red2 = (int) (red * 256D);
		final int green2 = (int) (green * 256D);
		final int blue2 = (int) (blue * 256D);
		return (red2 << 16) + (green2 << 8) + blue2;
	}
	
	public static void reset() {
		shadowDecay = null;
		shadowDecay = null;
		angleSine = null;
		angleCosine = null;
		viewport = null;
		texelArrayPool = null;
		texelCache = null;
		hslToRgbMap = null;
		brightness = 1.0F;
	}
	
	
	public static void setBrightness(float brightness) {
		if(Constants.ANTI_BOT_ENABLED) {
			brightness += Math.random() * 0.029999999999999999D - 0.014999999999999999D;
		}
		if(Rasterizer3D.brightness == brightness) {
			return;
		}
		Texture.setBrightness(brightness);
		Rasterizer3D.brightness = brightness;
		int pos = 0;
		for(int k = 0; k < 512; k++) {
			final double d1 = k / 8 / 64D + 0.0078125D;
			final double d2 = (k & 7) / 8D + 0.0625D;
			for(int i = 0; i < 128; i++) {
				final double c = i / 128D;
				double r = c;
				double g = c;
				double b = c;
				if(d2 != 0.0D) {
					double d7;
					if(c < 0.5D) {
						d7 = c * (1.0D + d2);
					} else {
						d7 = c + d2 - c * d2;
					}
					final double d8 = 2D * c - d7;
					double d9 = d1 + 0.33333333333333331D;
					if(d9 > 1.0D) {
						d9--;
					}
					final double d10 = d1;
					double d11 = d1 - 0.33333333333333331D;
					if(d11 < 0.0D) {
						d11++;
					}
					if(6D * d9 < 1.0D) {
						r = d8 + (d7 - d8) * 6D * d9;
					} else if(2D * d9 < 1.0D) {
						r = d7;
					} else if(3D * d9 < 2D) {
						r = d8 + (d7 - d8) * (0.66666666666666663D - d9) * 6D;
					} else {
						r = d8;
					}
					if(6D * d10 < 1.0D) {
						g = d8 + (d7 - d8) * 6D * d10;
					} else if(2D * d10 < 1.0D) {
						g = d7;
					} else if(3D * d10 < 2D) {
						g = d8 + (d7 - d8) * (0.66666666666666663D - d10) * 6D;
					} else {
						g = d8;
					}
					if(6D * d11 < 1.0D) {
						b = d8 + (d7 - d8) * 6D * d11;
					} else if(2D * d11 < 1.0D) {
						b = d7;
					} else if(3D * d11 < 2D) {
						b = d8 + (d7 - d8) * (0.66666666666666663D - d11) * 6D;
					} else {
						b = d8;
					}
				}
				int color = ((int) ((float) Math.pow(r, brightness) * 256F) << 16) +
						((int) ((float) Math.pow(g, brightness) * 256F) << 8) +
						(int) ((float) Math.pow(b, brightness) * 256F);
				if(color == 0) {
					color = 1;
				}
				hslToRgbMap[pos++] = color;
			}
		}
		Texture.clear();
	}
	
	public static void resetTextures() {
		if(texelArrayPool == null) {
			int textureTexelPoolPointer = 50;
			texelArrayPool = new int[textureTexelPoolPointer][0x10000];
			for(int k = 0; k < texelCache.length; k++)
				texelCache[k] = null;
			
		}
	}
	
}