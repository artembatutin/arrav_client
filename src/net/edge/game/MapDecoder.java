package net.edge.game;

import net.edge.Constants;
import net.edge.cache.unit.OverlayFloorType;
import net.edge.game.model.Location;
import net.edge.game.model.Model;
import net.edge.od.OnDemandFetcher;
import net.edge.util.io.Buffer;
import net.edge.Client;
import net.edge.Config;
import net.edge.cache.unit.LocationType;
import net.edge.cache.unit.UnderlayFloorType;
import net.edge.game.model.Entity;
import net.edge.media.Rasterizer3D;

public final class MapDecoder {

	public static Client client;
	private static int hueAdjust;
	private final int[] floorEntryHue;
	private final int[] floorEntrySaturation;
	private final int[] floorEntryLightness;
	private final int[] floorEntryBrightness;
	private final int[] floorEntryCount;
	private final int[][][] heightMap3d;
	private final byte[][][] overlayFloorId;
	public static int plane;
	private static int lightnessAdjust;
	private final byte[][][] aByteArrayArrayArray134;
	private final int[][][] anIntArrayArrayArray135;
	private final byte[][][] tileShape;
	private static final int anIntArray137[] = {1, 0, -1, 0};
	private final int[][] floorLightness;
	private static final int anIntArray140[] = {16, 32, 64, 128};
	private final byte[][][] underlayFloorId;
	private static final int anIntArray144[] = {0, -1, 0, 1};
	public static int setPlane = 99;
	private final int sizeX;
	private final int sizeY;
	private final byte[][][] tileShapeRotation;
	private final byte[][][] renderRuleFlags;
	private static final int anIntArray152[] = {1, 2, 4, 8};

	static {
		if(Constants.ANTI_BOT_ENABLED) {
			hueAdjust = (int) (Math.random() * 17D) - 8;
			lightnessAdjust = (int) (Math.random() * 33D) - 16;
		}
	}

	private int underlay_floor_map_color;
	private int underlay_floor_texture;

	private static int method170(int i, int j) {
		int k = i + j * 57;
		k = k << 13 ^ k;
		final int l = k * (k * k * 15731 + 0xc0ae5) + 0x5208dd0d & 0x7fffffff;
		return l >> 19 & 0xff;
	}

	private static int method172(int i, int j) {
		int k = method176(i + 45365, j + 0x16713, 4) - 128 + (method176(i + 10294, j + 37821, 2) - 128 >> 1) + (method176(i, j, 1) - 128 >> 2);
		k = (int) (k * 0.29999999999999999D) + 35;
		if(k < 10) {
			k = 10;
		} else if(k > 60) {
			k = 60;
		}
		return k;
	}

	public static void decode(Buffer buffer, OnDemandFetcher odf, boolean oldMap) {
		start:
		{
			int id = -1;
			do {
				final int j = buffer.getUSmart();
				if(j == 0) {
					break start;
				}
				id += j;
				final LocationType loc = LocationType.getSpecific(id, oldMap);
				loc.requestModels(odf, loc);
				do {
					final int config = buffer.getUSmart();
					if(config == 0) {
						break;
					}
					buffer.getUByte();
				} while(true);
			} while(true);
		}
	}

	private static int method176(int i, int j, int k) {
		final int l = i / k;
		final int i1 = i & k - 1;
		final int j1 = j / k;
		final int k1 = j & k - 1;
		final int l1 = method186(l, j1);
		final int i2 = method186(l + 1, j1);
		final int j2 = method186(l, j1 + 1);
		final int k2 = method186(l + 1, j1 + 1);
		final int l2 = method184(l1, i2, i1, k);
		final int i3 = method184(j2, k2, i1, k);
		return method184(l2, i3, k1, k);
	}

	public static boolean method178(int i, int j) {
		final LocationType class46 = LocationType.getRelative(i);
		if(j == 11) {
			j = 10;
		}
		if(j >= 5 && j <= 8) {
			j = 4;
		}
		return class46.modelTypeCached(j);
	}

	private static int method184(int i, int j, int k, int l) {
		final int i1 = 0x10000 - Rasterizer3D.angleCosine[k * 1024 / l] >> 1;
		return (i * (0x10000 - i1) >> 16) + (j * i1 >> 16);
	}

	private static int method186(int i, int j) {
		final int k = method170(i - 1, j - 1) + method170(i + 1, j - 1) + method170(i - 1, j + 1) + method170(i + 1, j + 1);
		final int l = method170(i - 1, j) + method170(i + 1, j) + method170(i, j - 1) + method170(i, j + 1);
		final int i1 = method170(i, j);
		return k / 16 + l / 8 + i1 / 4;
	}

	private static int adjustLightness(int hsl, int adj) {
		if(hsl == -1) {
			return 12345678;
		}
		adj = adj * (hsl & 0x7f) / 128;
		if(adj < 2) {
			adj = 2;
		} else if(adj > 126) {
			adj = 126;
		}
		return (hsl & 0xff80) + adj;
	}

	public static void method188(Scene scene, int i, int y, int code, int plane, CollisionMap collmap, int[][][] heightMap, int x, int id, int k1) {
		int h1 = heightMap[plane][x][y];
		int h2 = heightMap[plane][x + 1][y];
		int h3 = heightMap[plane][x + 1][y + 1];
		int h4 = heightMap[plane][x][y + 1];
		final int height = h1 + h2 + h3 + h4 >> 2;
		final LocationType loc = LocationType.getPrecise(id);
		id = loc.id;
		long hash = x + (y << 7) + (id << 14) + 0x8000000000L;
		if(!loc.hasActions) {
			hash += 0x8000000000000000L;
		}
		final byte config = (byte) ((i << 6) + code);
		if(code == 22) {
			Object obj;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj = loc.getModelAt(22, i, h1, h2, h3, h4, -1, -1, -1, -1);
			} else {
				obj = new Location(id, i, 22, h2, h3, h1, h4, loc.animationId, true);
			}
			scene.setGroundDecor((Entity) obj, k1, x, y, height, hash, config);
			if(loc.solid && loc.hasActions) {
				collmap.setBlocked(x, y);
			}
			return;
		}
		if(code == 10 || code == 11) {
			Object obj1;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj1 = loc.getModelAt(10, i, h1, h2, h3, h4, -1, -1, -1, -1);
			} else {
				obj1 = new Location(id, i, 10, h2, h3, h1, h4, loc.animationId, true);
			}
			if(obj1 != null) {
				int j5 = 0;
				if(code == 11) {
					j5 += 256;
				}
				int k4;
				int i5;
				if(i == 1 || i == 3) {
					k4 = loc.sizeY;
					i5 = loc.sizeX;
				} else {
					k4 = loc.sizeX;
					i5 = loc.sizeY;
				}
				scene.method284(hash, config, height, i5, (Entity) obj1, k4, k1, j5, y, x);
			}
			if(loc.solid) {
				collmap.method212(loc.walkable, loc.sizeX, loc.sizeY, x, y, i);
			}
			return;
		}
		if(code >= 12) {
			Object obj2;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj2 = loc.getModelAt(code, i, h1, h2, h3, h4, -1, -1, -1, -1);
			} else {
				obj2 = new Location(id, i, code, h2, h3, h1, h4, loc.animationId, true);
			}
			scene.method284(hash, config, height, 1, (Entity) obj2, 1, k1, 0, y, x);
			if(loc.solid) {
				collmap.method212(loc.walkable, loc.sizeX, loc.sizeY, x, y, i);
			}
			return;
		}
		if(code == 0) {
			Object obj3;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj3 = loc.getModelAt(0, i, h1, h2, h3, h4, -1, -1, -1, -1);
			} else {
				obj3 = new Location(id, i, 0, h2, h3, h1, h4, loc.animationId, true);
			}
			scene.setWallDecor((Entity) obj3, null, k1, x, y, height, anIntArray152[i], 0, hash, config);
			if(loc.solid) {
				collmap.method211(y, i, x, code, loc.walkable);
			}
			return;
		}
		if(code == 1) {
			Object obj4;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj4 = loc.getModelAt(1, i, h1, h2, h3, h4, -1, -1, -1, -1);
			} else {
				obj4 = new Location(id, i, 1, h2, h3, h1, h4, loc.animationId, true);
			}
			scene.setWallDecor((Entity) obj4, null, k1, x, y, height, anIntArray140[i], 0, hash, config);
			if(loc.solid) {
				collmap.method211(y, i, x, code, loc.walkable);
			}
			return;
		}
		if(code == 2) {
			final int j3 = i + 1 & 3;
			Object obj11;
			Object obj12;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj11 = loc.getModelAt(2, 4 + i, h1, h2, h3, h4, -1, -1, -1, -1);
				obj12 = loc.getModelAt(2, j3, h1, h2, h3, h4, -1, -1, -1, -1);
			} else {
				obj11 = new Location(id, 4 + i, 2, h2, h3, h1, h4, loc.animationId, true);
				obj12 = new Location(id, j3, 2, h2, h3, h1, h4, loc.animationId, true);
			}
			scene.setWallDecor((Entity) obj11, (Entity) obj12, k1, x, y, height, anIntArray152[i], anIntArray152[j3], hash, config);
			if(loc.solid) {
				collmap.method211(y, i, x, code, loc.walkable);
			}
			return;
		}
		if(code == 3) {
			Object obj5;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj5 = loc.getModelAt(3, i, h1, h2, h3, h4, -1, -1, -1, -1);
			} else {
				obj5 = new Location(id, i, 3, h2, h3, h1, h4, loc.animationId, true);
			}
			scene.setWallDecor((Entity) obj5, null, k1, x, y, height, anIntArray140[i], 0, hash, config);
			if(loc.solid) {
				collmap.method211(y, i, x, code, loc.walkable);
			}
			return;
		}
		if(code == 9) {
			Object obj6;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj6 = loc.getModelAt(code, i, h1, h2, h3, h4, -1, -1, -1, -1);
			} else {
				obj6 = new Location(id, i, code, h2, h3, h1, h4, loc.animationId, true);
			}
			scene.method284(hash, config, height, 1, (Entity) obj6, 1, k1, 0, y, x);
			if(loc.solid) {
				collmap.method212(loc.walkable, loc.sizeX, loc.sizeY, x, y, i);
			}
			return;
		}
		if(loc.adjustToTerrain) {
			if(i == 1) {
				final int k3 = h4;
				h4 = h3;
				h3 = h2;
				h2 = h1;
				h1 = k3;
			} else if(i == 2) {
				int l3 = h4;
				h4 = h2;
				h2 = l3;
				l3 = h3;
				h3 = h1;
				h1 = l3;
			} else if(i == 3) {
				final int i4 = h4;
				h4 = h1;
				h1 = h2;
				h2 = h3;
				h3 = i4;
			}
		}
		if(code == 4) {
			Object obj7;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj7 = loc.getModelAt(4, 0, h1, h2, h3, h4, -1, -1, -1, -1);
			} else {
				obj7 = new Location(id, 0, 4, h2, h3, h1, h4, loc.animationId, true);
			}
			scene.setWallDecor(hash, y, i * 512, k1, 0, height, (Entity) obj7, x, config, 0, anIntArray152[i]);
			return;
		}
		if(code == 5) {
			int j4 = 16;
			long l4 = scene.getWallHash(k1, x, y);
			if(l4 > 0) {
				j4 = LocationType.getPrecise((int) (l4 >> 14 & 0xFFFFFF)).offsetAmplifier;
			}
			Object obj13;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj13 = loc.getModelAt(4, 0, h1, h2, h3, h4, -1, -1, -1, -1);
			} else {
				obj13 = new Location(id, 0, 4, h2, h3, h1, h4, loc.animationId, true);
			}
			scene.setWallDecor(hash, y, i * 512, k1, anIntArray137[i] * j4, height, (Entity) obj13, x, config, anIntArray144[i] * j4, anIntArray152[i]);
			return;
		}
		if(code == 6) {
			Object obj8;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj8 = loc.getModelAt(4, 0, h1, h2, h3, h4, -1, -1, -1, -1);
			} else {
				obj8 = new Location(id, 0, 4, h2, h3, h1, h4, loc.animationId, true);
			}
			scene.setWallDecor(hash, y, i, k1, 0, height, (Entity) obj8, x, config, 0, 256);
			return;
		}
		if(code == 7) {
			Object obj9;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj9 = loc.getModelAt(4, 0, h1, h2, h3, h4, -1, -1, -1, -1);
			} else {
				obj9 = new Location(id, 0, 4, h2, h3, h1, h4, loc.animationId, true);
			}
			scene.setWallDecor(hash, y, i, k1, 0, height, (Entity) obj9, x, config, 0, 512);
			return;
		}
		if(code == 8) {
			Object obj10;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj10 = loc.getModelAt(4, 0, h1, h2, h3, h4, -1, -1, -1, -1);
			} else {
				obj10 = new Location(id, 0, 4, h2, h3, h1, h4, loc.animationId, true);
			}
			scene.setWallDecor(hash, y, i, k1, 0, height, (Entity) obj10, x, config, 0, 768);
		}
	}

	public static boolean regionCached(int i, byte[] is, int i_250_, boolean oldMap) {
		boolean bool = true;
		final Buffer stream = new Buffer(is);
		int i_252_ = -1;
		for(; ; ) {
			final int i_253_ = stream.getUSmart();
			if(i_253_ == 0) {
				break;
			}
			i_252_ += i_253_;
			int i_254_ = 0;
			boolean bool_255_ = false;
			for(; ; ) {
				if(bool_255_) {
					final int i_256_ = stream.getUSmart();
					if(i_256_ == 0) {
						break;
					}
					stream.getUByte();
				} else {
					final int i_257_ = stream.getUSmart();
					if(i_257_ == 0) {
						break;
					}
					i_254_ += i_257_ - 1;
					final int i_258_ = i_254_ & 0x3f;
					final int i_259_ = i_254_ >> 6 & 0x3f;
					final int i_260_ = stream.getUByte() >> 2;
					final int i_261_ = i_259_ + i;
					final int i_262_ = i_258_ + i_250_;
					if(i_261_ > 0 && i_262_ > 0 && i_261_ < 103 && i_262_ < 103) {
						final LocationType class46 = LocationType.getSpecific(i_252_, oldMap);
						if(i_260_ != 22 || Config.GROUND_DECORATION.isOn() || class46.hasActions || class46.aBoolean736) {
							bool &= class46.modelCached();
							bool_255_ = true;
						}
					}
				}
			}
		}
		return bool;
	}

	public MapDecoder(byte[][][] abyte0, int[][][] ai) {
		setPlane = 99;
		sizeX = 104;
		sizeY = 104;
		heightMap3d = ai;
		renderRuleFlags = abyte0;
		underlayFloorId = new byte[4][sizeX][sizeY];
		overlayFloorId = new byte[4][sizeX][sizeY];
		tileShape = new byte[4][sizeX][sizeY];
		tileShapeRotation = new byte[4][sizeX][sizeY];
		anIntArrayArrayArray135 = new int[4][sizeX + 1][sizeY + 1];
		aByteArrayArrayArray134 = new byte[4][sizeX + 1][sizeY + 1];
		floorLightness = new int[sizeX + 1][sizeY + 1];
		floorEntryHue = new int[sizeY];
		floorEntrySaturation = new int[sizeY];
		floorEntryLightness = new int[sizeY];
		floorEntryBrightness = new int[sizeY];
		floorEntryCount = new int[sizeY];
	}

	public final void set(CollisionMap[] collmap, Scene scene) {
		for(int plane = 0; plane < 4; plane++) {
			for(int x = 0; x < 104; x++) {
				for(int y = 0; y < 104; y++) {
					if((renderRuleFlags[plane][x][y] & 1) == 1) {
						int k1 = plane;
						if((renderRuleFlags[1][x][y] & 2) == 2) {
							k1--;
						}
						if(k1 >= 0) {
							collmap[k1].setBlocked(x, y);
						}
					}
				}
			}
		}
		if(Constants.ANTI_BOT_ENABLED) {
			hueAdjust += (int) (Math.random() * 5D) - 2;
			if(hueAdjust < -8) {
				hueAdjust = -8;
			}
			if(hueAdjust > 8) {
				hueAdjust = 8;
			}
			lightnessAdjust += (int) (Math.random() * 5D) - 2;
			if(lightnessAdjust < -16) {
				lightnessAdjust = -16;
			}
			if(lightnessAdjust > 16) {
				lightnessAdjust = 16;
			}
		}
		for(int z = 0; z < 4; z++) {
			final byte abyte0[][] = aByteArrayArrayArray134[z];
			final byte lghtness = 104/*96*/;
			final char roundness = 1280/*768*/;
			final byte lightx = -50;
			final byte lighty = -10;
			final byte lightz = -50;
			final int distance = (int) Math.sqrt(lightx * lightx + lighty * lighty + lightz * lightz);
			final int contrast = roundness * distance >> 8;
			for(int y = 1; y < sizeY - 1; y++) {
				for(int x = 1; x < sizeX - 1; x++) {
					final int dx = heightMap3d[z][x + 1][y] - heightMap3d[z][x - 1][y];
					final int dy = heightMap3d[z][x][y + 1] - heightMap3d[z][x][y - 1];
					final int ddistance = (int) Math.sqrt(dx * dx + 0x10000 + dy * dy);
					final int ddx = (dx << 8) / ddistance;
					final int ddz = 0x10000 / ddistance;
					final int ddy = (dy << 8) / ddistance;
					final int j16 = lghtness + (lightx * ddx + lighty * ddz + lightz * ddy) / contrast;
					final int j17 = (abyte0[x - 1][y] >> 2) + (abyte0[x + 1][y] >> 3) + (abyte0[x][y - 1] >> 2) + (abyte0[x][y + 1] >> 3) + (abyte0[x][y] >> 1);
					floorLightness[x][y] = j16 - j17;
				}
			}
			for(int y = 0; y < sizeY; y++) {
				floorEntryHue[y] = 0;
				floorEntrySaturation[y] = 0;
				floorEntryLightness[y] = 0;
				floorEntryBrightness[y] = 0;
				floorEntryCount[y] = 0;
			}
			for(int x = -5; x < sizeX + 5; x++) {
				for(int i8 = 0; i8 < sizeY; i8++) {
					final int k9 = x + 5;
					if(k9 >= 0 && k9 < sizeX) {
						final int l12 = underlayFloorId[z][k9][i8] & 0xff;
						if(l12 > 0 && UnderlayFloorType.exists(l12 - 1)) {
							final UnderlayFloorType flo = UnderlayFloorType.cache[l12 - 1];
							floorEntryHue[i8] += flo.hueAdj;
							floorEntrySaturation[i8] += flo.saturation;
							floorEntryLightness[i8] += flo.lightness;
							floorEntryBrightness[i8] += flo.brightness;
							floorEntryCount[i8]++;
						}
					}
					final int i13 = x - 5;
					if(i13 >= 0 && i13 < sizeX) {
						final int i14 = underlayFloorId[z][i13][i8] & 0xff;
						if(i14 > 0 && UnderlayFloorType.exists(i14 - 1)) {
							final UnderlayFloorType flo_1 = UnderlayFloorType.cache[i14 - 1];
							floorEntryHue[i8] -= flo_1.hueAdj;
							floorEntrySaturation[i8] -= flo_1.saturation;
							floorEntryLightness[i8] -= flo_1.lightness;
							floorEntryBrightness[i8] -= flo_1.brightness;
							floorEntryCount[i8]--;
						}
					}
				}

				if(x >= 1 && x < sizeX - 1) {
					int zhue = 0;
					int zsaturation = 0;
					int zlightness = 0;
					int brightness = 0;
					int count = 0;
					for(int y = -5; y < sizeY + 5; y++) {
						final int j18 = y + 5;
						if(j18 >= 0 && j18 < sizeY) {
							zhue += floorEntryHue[j18];
							zsaturation += floorEntrySaturation[j18];
							zlightness += floorEntryLightness[j18];
							brightness += floorEntryBrightness[j18];
							count += floorEntryCount[j18];
						}
						final int k18 = y - 5;
						if(k18 >= 0 && k18 < sizeY) {
							zhue -= floorEntryHue[k18];
							zsaturation -= floorEntrySaturation[k18];
							zlightness -= floorEntryLightness[k18];
							brightness -= floorEntryBrightness[k18];
							count -= floorEntryCount[k18];
						}
						if(y >= 1 && y < sizeY - 1) {
							if(z < setPlane) {
								setPlane = z;
							}
							int underlayFloId = underlayFloorId[z][x][y] & 0xff;
							int overlayFloId = overlayFloorId[z][x][y] & 0xff;
							if(underlayFloId > 0 || overlayFloId > 0) {
								final int j19 = heightMap3d[z][x][y];
								final int k19 = heightMap3d[z][x + 1][y];
								final int l19 = heightMap3d[z][x + 1][y + 1];
								final int i20 = heightMap3d[z][x][y + 1];
								final int j20 = floorLightness[x][y];
								final int k20 = floorLightness[x + 1][y];
								final int l20 = floorLightness[x + 1][y + 1];
								final int i21 = floorLightness[x][y + 1];
								int hsl = -1;
								int adjhs = -1;
								if(underlayFloId > 0) {
									if(brightness == 0) {//XXX: Cheap fix.
										brightness = 1;
									}
									if(count <= 0) {
										count = 1;
									}
									int hue = zhue * 256 / brightness;
									final int saturation = zsaturation / count;
									int lightness = zlightness / count;
									hsl = getHSLHashCode(hue, saturation, lightness);
									if(Constants.ANTI_BOT_ENABLED) {
										hue = hue + hueAdjust & 0xff;
										lightness += lightnessAdjust;
									}
									if(lightness < 0) {
										lightness = 0;
									} else if(lightness > 255) {
										lightness = 255;
									}
									adjhs = getHSLHashCode(hue, saturation, lightness);
								}
								if(z > 0) {
									boolean hide_underlay = true;

									if(underlayFloId == 0 && tileShape[z][x][y] != 0) {
										hide_underlay = false;
									}
									if(overlayFloId > 0 && !OverlayFloorType.floorTypes[overlayFloId - 1].occlude) {
										hide_underlay = false;
									}
									if(hide_underlay && j19 == k19 && j19 == l19 && j19 == i20) {
										anIntArrayArrayArray135[z][x][y] |= 0x924;
									}
								}
								int rgb_bitset_randomized = 0;
								if(hsl != -1) {
									rgb_bitset_randomized = Rasterizer3D.hslToRgbMap[adjustLightness(adjhs, 96)];
								}
								if(overlayFloId == 0 && underlayFloId - 1 >= UnderlayFloorType.cache.length) {
									overlayFloId = 1;
								} else if(overlayFloId - 1 >= OverlayFloorType.floorTypes.length) {
									overlayFloId = 1;
								}
								if(overlayFloId == 0) {
									final UnderlayFloorType underlayFlo = UnderlayFloorType.cache[underlayFloId - 1];
									underlay_floor_texture = underlayFlo.texture;
									scene.setGround(z, x, y, 0, 0, underlay_floor_texture, -1, j19, k19, l19, i20, adjustLightness(hsl, j20), adjustLightness(hsl, k20), adjustLightness(hsl, l20), adjustLightness(hsl, i21), 0, 0, 0, 0, rgb_bitset_randomized, 0);
								} else {
									final int lay = tileShape[z][x][y] + 1;
									final byte k23 = tileShapeRotation[z][x][y];
									final OverlayFloorType overlayFlo = OverlayFloorType.floorTypes[overlayFloId - 1];
									int overlay_texture = overlayFlo.textureId;
									int flohsl;
									int mapColor;
									if(overlay_texture >= 0) {
										flohsl = getHSLHashCode(overlayFlo.hue, overlayFlo.saturation, overlayFlo.lightness);
										mapColor = Rasterizer3D.hslToRgbMap[method185(overlayFlo.hsl, 96)];
									} else if(overlayFlo.groundColorOverlay == 0xff00ff) {
										mapColor = 0;
										flohsl = -2;
										overlay_texture = -1;
									} else {
										flohsl = getHSLHashCode(overlayFlo.hue, overlayFlo.saturation, overlayFlo.lightness);
										mapColor = Rasterizer3D.hslToRgbMap[method185(overlayFlo.hsl, 96)];
									}
									scene.setGround(z, x, y, lay, k23, underlay_floor_texture, overlay_texture, j19, k19, l19, i20, adjustLightness(hsl, j20), adjustLightness(hsl, k20), adjustLightness(hsl, l20), adjustLightness(hsl, i21), method185(flohsl, j20), method185(flohsl, k20), method185(flohsl, l20), method185(flohsl, i21), rgb_bitset_randomized, mapColor);

								}
							}
						}
					}
				}
			}
			for(int j8 = 1; j8 < sizeY - 1; j8++) {
				for(int i10 = 1; i10 < sizeX - 1; i10++) {
					scene.method278(z, i10, j8, method182(z, i10, j8));
				}
			}
		}
		scene.shadeModels(-10, -50, -50);
		for(int j1 = 0; j1 < sizeX; j1++) {
			for(int l1 = 0; l1 < sizeY; l1++) {
				if((renderRuleFlags[1][j1][l1] & 2) == 2) {
					scene.method276(j1, l1);
				}
			}
		}

		int i2 = 1;
		int j2 = 2;
		int k2 = 4;
		for(int l2 = 0; l2 < 4; l2++) {
			if(l2 > 0) {
				i2 <<= 3;
				j2 <<= 3;
				k2 <<= 3;
			}
			for(int i3 = 0; i3 <= l2; i3++) {
				for(int k3 = 0; k3 <= sizeY; k3++) {
					for(int i4 = 0; i4 <= sizeX; i4++) {
						if((anIntArrayArrayArray135[i3][i4][k3] & i2) != 0) {
							int k4 = k3;
							int l5 = k3;
							int i7 = i3;
							int k8 = i3;
							for(; k4 > 0 && (anIntArrayArrayArray135[i3][i4][k4 - 1] & i2) != 0; k4--)
								;
							for(; l5 < sizeY && (anIntArrayArrayArray135[i3][i4][l5 + 1] & i2) != 0; l5++)
								;
							label0:
							for(; i7 > 0; i7--) {
								for(int j10 = k4; j10 <= l5; j10++) {
									if((anIntArrayArrayArray135[i7 - 1][i4][j10] & i2) == 0) {
										break label0;
									}
								}
							}
							label1:
							for(; k8 < l2; k8++) {
								for(int k10 = k4; k10 <= l5; k10++) {
									if((anIntArrayArrayArray135[k8 + 1][i4][k10] & i2) == 0) {
										break label1;
									}
								}
							}
							final int l10 = (k8 + 1 - i7) * (l5 - k4 + 1);
							if(l10 >= 8) {
								final char c1 = '\360';
								final int k14 = heightMap3d[k8][i4][k4] - c1;
								final int l15 = heightMap3d[i7][i4][k4];
								Scene.method277(l2, i4 * 128, l15, i4 * 128, l5 * 128 + 128, k14, k4 * 128, 1);
								for(int l16 = i7; l16 <= k8; l16++) {
									for(int l17 = k4; l17 <= l5; l17++) {
										anIntArrayArrayArray135[l16][i4][l17] &= ~i2;
									}
								}
							}
						}
						if((anIntArrayArrayArray135[i3][i4][k3] & j2) != 0) {
							int l4 = i4;
							int i6 = i4;
							int j7 = i3;
							int l8 = i3;
							for(; l4 > 0 && (anIntArrayArrayArray135[i3][l4 - 1][k3] & j2) != 0; l4--)
								;
							for(; i6 < sizeX && (anIntArrayArrayArray135[i3][i6 + 1][k3] & j2) != 0; i6++)
								;
							label2:
							for(; j7 > 0; j7--) {
								for(int i11 = l4; i11 <= i6; i11++) {
									if((anIntArrayArrayArray135[j7 - 1][i11][k3] & j2) == 0) {
										break label2;
									}
								}
							}
							label3:
							for(; l8 < l2; l8++) {
								for(int j11 = l4; j11 <= i6; j11++) {
									if((anIntArrayArrayArray135[l8 + 1][j11][k3] & j2) == 0) {
										break label3;
									}
								}
							}
							final int k11 = (l8 + 1 - j7) * (i6 - l4 + 1);
							if(k11 >= 8) {
								final char c2 = '\360';
								final int l14 = heightMap3d[l8][l4][k3] - c2;
								final int i16 = heightMap3d[j7][l4][k3];
								Scene.method277(l2, l4 * 128, i16, i6 * 128 + 128, k3 * 128, l14, k3 * 128, 2);
								for(int i17 = j7; i17 <= l8; i17++) {
									for(int i18 = l4; i18 <= i6; i18++) {
										anIntArrayArrayArray135[i17][i18][k3] &= ~j2;
									}
								}
							}
						}
						if((anIntArrayArrayArray135[i3][i4][k3] & k2) != 0) {
							int i5 = i4;
							int j6 = i4;
							int k7 = k3;
							int i9 = k3;
							for(; k7 > 0 && (anIntArrayArrayArray135[i3][i4][k7 - 1] & k2) != 0; k7--)
								;
							for(; i9 < sizeY && (anIntArrayArrayArray135[i3][i4][i9 + 1] & k2) != 0; i9++)
								;
							label4:
							for(; i5 > 0; i5--) {
								for(int l11 = k7; l11 <= i9; l11++) {
									if((anIntArrayArrayArray135[i3][i5 - 1][l11] & k2) == 0) {
										break label4;
									}
								}
							}
							label5:
							for(; j6 < sizeX; j6++) {
								for(int i12 = k7; i12 <= i9; i12++) {
									if((anIntArrayArrayArray135[i3][j6 + 1][i12] & k2) == 0) {
										break label5;
									}
								}
							}
							if((j6 - i5 + 1) * (i9 - k7 + 1) >= 4) {
								final int j12 = heightMap3d[i3][i5][k7];
								Scene.method277(l2, i5 * 128, j12, j6 * 128 + 128, i9 * 128 + 128, j12, k7 * 128, 4);
								for(int k13 = i5; k13 <= j6; k13++) {
									for(int i15 = k7; i15 <= i9; i15++) {
										anIntArrayArrayArray135[i3][k13][i15] &= ~k2;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public final void method174(int i, int j, int l, int i1) {
		for(int j1 = i; j1 <= i + j; j1++) {
			for(int k1 = i1; k1 <= i1 + l; k1++) {
				if(k1 >= 0 && k1 < sizeX && j1 >= 0 && j1 < sizeY) {
					aByteArrayArrayArray134[0][k1][j1] = 127;
					if(k1 == i1 && k1 > 0) {
						heightMap3d[0][k1][j1] = heightMap3d[0][k1 - 1][j1];
					}
					if(k1 == i1 + l && k1 < sizeX - 1) {
						heightMap3d[0][k1][j1] = heightMap3d[0][k1 + 1][j1];
					}
					if(j1 == i && j1 > 0) {
						heightMap3d[0][k1][j1] = heightMap3d[0][k1][j1 - 1];
					}
					if(j1 == i + j && j1 < sizeY - 1) {
						heightMap3d[0][k1][j1] = heightMap3d[0][k1][j1 + 1];
					}
				}
			}
		}
	}

	private void method175(int y, Scene worldController, CollisionMap class11, int j, int p, int x, int id, int j1, boolean oldMap) {
		/*if(!Config.VISIBLE_LEVELS.isOn() && (renderRuleFlags[0][x][y] & 2) == 0) {
			if((renderRuleFlags[p][x][y] & 0x10) != 0) {
				return;
			}
			if(method182(p, x, y) != plane) {
				return;
			}
		}*/
		if(p < setPlane) {
			setPlane = p;
		}
		if(p >= 4) {
			p = 0;
		}
		int k1 = heightMap3d[p][x][y];
		int l1 = heightMap3d[p][x + 1][y];
		int i2 = heightMap3d[p][x + 1][y + 1];
		int j2 = heightMap3d[p][x][y + 1];
		final int k2 = k1 + l1 + i2 + j2 >> 2;
		final LocationType loc = LocationType.getSpecific(id, oldMap);
		id = loc.id;
		if(id == 38453 || id == 38447)
			return;
		long hash = x + (y << 7) + (id << 14) + 0x8000000000L;
		if(!loc.hasActions) {
			hash += 0x8000000000000000L;
		}
		final byte byte0 = (byte) ((j1 << 6) + j);
		if(j == 22) {
			if(!Config.GROUND_DECORATION.isOn() && !loc.hasActions && !loc.aBoolean736) {
				return;
			}
			Object obj;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj = loc.getModelAt(22, j1, k1, l1, i2, j2, -1, -1, -1, -1);
			} else {
				obj = new Location(id, j1, 22, l1, i2, k1, j2, loc.animationId, true);
			}
			worldController.setGroundDecor((Entity) obj, p, x, y, k2, hash, byte0);
			if(loc.solid && loc.hasActions && class11 != null) {
				class11.setBlocked(x, y);
			}
			return;
		}
		if(j == 10 || j == 11) {
			Object obj1;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj1 = loc.getModelAt(10, j1, k1, l1, i2, j2, -1, -1, -1, -1);
			} else {
				obj1 = new Location(id, j1, 10, l1, i2, k1, j2, loc.animationId, true);
			}
			if(obj1 != null) {
				int i5 = 0;
				if(j == 11) {
					i5 += 256;
				}
				int j4;
				int l4;
				if(j1 == 1 || j1 == 3) {
					j4 = loc.sizeY;
					l4 = loc.sizeX;
				} else {
					j4 = loc.sizeX;
					l4 = loc.sizeY;
				}
				if(worldController.method284(hash, byte0, k2, l4, (Entity) obj1, j4, p, i5, y, x) && loc.castsShadow) {
					Model model;
					if(obj1 instanceof Model) {
						model = (Model) obj1;
					} else {
						model = loc.getModelAt(10, j1, k1, l1, i2, j2, -1, -1, -1, -1);
					}
					if(model != null) {
						for(int j5 = 0; j5 <= j4; j5++) {
							for(int k5 = 0; k5 <= l4; k5++) {
								int l5 = model.maxHorizontalDist / 4;
								if(l5 > 30) {
									l5 = 30;
								}
								if(l5 > aByteArrayArrayArray134[p][x + j5][y + k5]) {
									aByteArrayArrayArray134[p][x + j5][y + k5] = (byte) l5;
								}
							}
						}
					}
				}
			}
			if(loc.solid && class11 != null) {
				class11.method212(loc.walkable, loc.sizeX, loc.sizeY, x, y, j1);
			}
			return;
		}
		if(j >= 12) {
			Object obj2;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj2 = loc.getModelAt(j, j1, k1, l1, i2, j2, -1, -1, -1, -1);
			} else {
				obj2 = new Location(id, j1, j, l1, i2, k1, j2, loc.animationId, true);
			}
			worldController.method284(hash, byte0, k2, 1, (Entity) obj2, 1, p, 0, y, x);
			if(j >= 12 && j <= 17 && j != 13 && p > 0) {
				anIntArrayArrayArray135[p][x][y] |= 0x924;
			}
			if(loc.solid && class11 != null) {
				class11.method212(loc.walkable, loc.sizeX, loc.sizeY, x, y, j1);
			}
			return;
		}
		if(j == 0) {
			Object obj3;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj3 = loc.getModelAt(0, j1, k1, l1, i2, j2, -1, -1, -1, -1);
			} else {
				obj3 = new Location(id, j1, 0, l1, i2, k1, j2, loc.animationId, true);
			}
			worldController.setWallDecor((Entity) obj3, null, p, x, y, k2, anIntArray152[j1], 0, hash, byte0);
			if(j1 == 0) {
				if(loc.castsShadow) {
					aByteArrayArrayArray134[p][x][y] = 50;
					aByteArrayArrayArray134[p][x][y + 1] = 50;
				}
				if(loc.wall) {
					anIntArrayArrayArray135[p][x][y] |= 0x249;
				}
			} else if(j1 == 1) {
				if(loc.castsShadow) {
					aByteArrayArrayArray134[p][x][y + 1] = 50;
					aByteArrayArrayArray134[p][x + 1][y + 1] = 50;
				}
				if(loc.wall) {
					anIntArrayArrayArray135[p][x][y + 1] |= 0x492;
				}
			} else if(j1 == 2) {
				if(loc.castsShadow) {
					aByteArrayArrayArray134[p][x + 1][y] = 50;
					aByteArrayArrayArray134[p][x + 1][y + 1] = 50;
				}
				if(loc.wall) {
					anIntArrayArrayArray135[p][x + 1][y] |= 0x249;
				}
			} else if(j1 == 3) {
				if(loc.castsShadow) {
					aByteArrayArrayArray134[p][x][y] = 50;
					aByteArrayArrayArray134[p][x + 1][y] = 50;
				}
				if(loc.wall) {
					anIntArrayArrayArray135[p][x][y] |= 0x492;
				}
			}
			if(loc.solid && class11 != null) {
				class11.method211(y, j1, x, j, loc.walkable);
			}
			if(loc.offsetAmplifier != 16) {
				worldController.method290(y, loc.offsetAmplifier, x, p);
			}
			return;
		}
		if(j == 1) {
			Object obj4;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj4 = loc.getModelAt(1, j1, k1, l1, i2, j2, -1, -1, -1, -1);
			} else {
				obj4 = new Location(id, j1, 1, l1, i2, k1, j2, loc.animationId, true);
			}
			worldController.setWallDecor((Entity) obj4, null, p, x, y, k2, anIntArray140[j1], 0, hash, byte0);
			if(loc.castsShadow) {
				if(j1 == 0) {
					aByteArrayArrayArray134[p][x][y + 1] = 50;
				} else if(j1 == 1) {
					aByteArrayArrayArray134[p][x + 1][y + 1] = 50;
				} else if(j1 == 2) {
					aByteArrayArrayArray134[p][x + 1][y] = 50;
				} else if(j1 == 3) {
					aByteArrayArrayArray134[p][x][y] = 50;
				}
			}
			if(loc.solid && class11 != null) {
				class11.method211(y, j1, x, j, loc.walkable);
			}
			return;
		}
		if(j == 2) {
			final int i3 = j1 + 1 & 3;
			Object obj11;
			Object obj12;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj11 = loc.getModelAt(2, 4 + j1, k1, l1, i2, j2, -1, -1, -1, -1);
				obj12 = loc.getModelAt(2, i3, k1, l1, i2, j2, -1, -1, -1, -1);
			} else {
				obj11 = new Location(id, 4 + j1, 2, l1, i2, k1, j2, loc.animationId, true);
				obj12 = new Location(id, i3, 2, l1, i2, k1, j2, loc.animationId, true);
			}
			worldController.setWallDecor((Entity) obj11, (Entity) obj12, p, x, y, k2, anIntArray152[j1], anIntArray152[i3], hash, byte0);
			if(loc.wall) {
				if(j1 == 0) {
					anIntArrayArrayArray135[p][x][y] |= 0x249;
					anIntArrayArrayArray135[p][x][y + 1] |= 0x492;
				} else if(j1 == 1) {
					anIntArrayArrayArray135[p][x][y + 1] |= 0x492;
					anIntArrayArrayArray135[p][x + 1][y] |= 0x249;
				} else if(j1 == 2) {
					anIntArrayArrayArray135[p][x + 1][y] |= 0x249;
					anIntArrayArrayArray135[p][x][y] |= 0x492;
				} else if(j1 == 3) {
					anIntArrayArrayArray135[p][x][y] |= 0x492;
					anIntArrayArrayArray135[p][x][y] |= 0x249;
				}
			}
			if(loc.solid && class11 != null) {
				class11.method211(y, j1, x, j, loc.walkable);
			}
			if(loc.offsetAmplifier != 16) {
				worldController.method290(y, loc.offsetAmplifier, x, p);
			}
			return;
		}
		if(j == 3) {
			Object obj5;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj5 = loc.getModelAt(3, j1, k1, l1, i2, j2, -1, -1, -1, -1);
			} else {
				obj5 = new Location(id, j1, 3, l1, i2, k1, j2, loc.animationId, true);
			}
			worldController.setWallDecor((Entity) obj5, null, p, x, y, k2, anIntArray140[j1], 0, hash, byte0);
			if(loc.castsShadow) {
				if(j1 == 0) {
					aByteArrayArrayArray134[p][x][y + 1] = 50;
				} else if(j1 == 1) {
					aByteArrayArrayArray134[p][x + 1][y + 1] = 50;
				} else if(j1 == 2) {
					aByteArrayArrayArray134[p][x + 1][y] = 50;
				} else if(j1 == 3) {
					aByteArrayArrayArray134[p][x][y] = 50;
				}
			}
			if(loc.solid && class11 != null) {
				class11.method211(y, j1, x, j, loc.walkable);
			}
			return;
		}
		if(j == 9) {
			Object obj6;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj6 = loc.getModelAt(j, j1, k1, l1, i2, j2, -1, -1, -1, -1);
			} else {
				obj6 = new Location(id, j1, j, l1, i2, k1, j2, loc.animationId, true);
			}
			worldController.method284(hash, byte0, k2, 1, (Entity) obj6, 1, p, 0, y, x);
			if(loc.solid && class11 != null) {
				class11.method212(loc.walkable, loc.sizeX, loc.sizeY, x, y, j1);
			}
			return;
		}
		if(loc.adjustToTerrain) {
			if(j1 == 1) {
				final int j3 = j2;
				j2 = i2;
				i2 = l1;
				l1 = k1;
				k1 = j3;
			} else if(j1 == 2) {
				int k3 = j2;
				j2 = l1;
				l1 = k3;
				k3 = i2;
				i2 = k1;
				k1 = k3;
			} else if(j1 == 3) {
				final int l3 = j2;
				j2 = k1;
				k1 = l1;
				l1 = i2;
				i2 = l3;
			}
		}
		if(j == 4) {
			Object obj7;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj7 = loc.getModelAt(4, 0, k1, l1, i2, j2, -1, -1, -1, -1);
			} else {
				obj7 = new Location(id, 0, 4, l1, i2, k1, j2, loc.animationId, true);
			}
			worldController.setWallDecor(hash, y, j1 * 512, p, 0, k2, (Entity) obj7, x, byte0, 0, anIntArray152[j1]);
			return;
		}
		if(j == 5) {
			int i4 = 16;
			long k4 = worldController.getWallHash(p, x, y);
			if(k4 > 0) {
				i4 = LocationType.getPrecise((int) (k4 >> 14 & 0xFFFFFF)).offsetAmplifier;
			}
			Object obj13;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj13 = loc.getModelAt(4, 0, k1, l1, i2, j2, -1, -1, -1, -1);
			} else {
				obj13 = new Location(id, 0, 4, l1, i2, k1, j2, loc.animationId, true);
			}
			worldController.setWallDecor(hash, y, j1 * 512, p, anIntArray137[j1] * i4, k2, (Entity) obj13, x, byte0, anIntArray144[j1] * i4, anIntArray152[j1]);
			return;
		}
		if(j == 6) {
			Object obj8;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj8 = loc.getModelAt(4, 0, k1, l1, i2, j2, -1, -1, -1, -1);
			} else {
				obj8 = new Location(id, 0, 4, l1, i2, k1, j2, loc.animationId, true);
			}
			worldController.setWallDecor(hash, y, j1, p, 0, k2, (Entity) obj8, x, byte0, 0, 256);
			return;
		}
		if(j == 7) {
			Object obj9;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj9 = loc.getModelAt(4, 0, k1, l1, i2, j2, -1, -1, -1, -1);
			} else {
				obj9 = new Location(id, 0, 4, l1, i2, k1, j2, loc.animationId, true);
			}
			worldController.setWallDecor(hash, y, j1, p, 0, k2, (Entity) obj9, x, byte0, 0, 512);
			return;
		}
		if(j == 8) {
			Object obj10;
			if(loc.animationId == -1 && loc.childIds == null) {
				obj10 = loc.getModelAt(4, 0, k1, l1, i2, j2, -1, -1, -1, -1);
			} else {
				obj10 = new Location(id, 0, 4, l1, i2, k1, j2, loc.animationId, true);
			}
			worldController.setWallDecor(hash, y, j1, p, 0, k2, (Entity) obj10, x, byte0, 0, 768);
		}
		// }
	}

	private int getHSLHashCode(int h, int s, int l) {
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

	public final void method179(int i, int j, CollisionMap aclass11[], int l, int i1, byte abyte0[], int j1, int k1, int l1) {
		for(int i2 = 0; i2 < 8; i2++) {
			for(int j2 = 0; j2 < 8; j2++) {
				if(l + i2 > 0 && l + i2 < 103 && l1 + j2 > 0 && l1 + j2 < 103) {
					aclass11[k1].flagMap[l + i2][l1 + j2] &= 0xfeffffff;
				}
			}
		}
		final Buffer buffer = new Buffer(abyte0);
		for(int l2 = 0; l2 < 4; l2++) {
			for(int i3 = 0; i3 < 64; i3++) {
				for(int j3 = 0; j3 < 64; j3++) {
					if(l2 == i && i3 >= i1 && i3 < i1 + 8 && j3 >= j1 && j3 < j1 + 8) {
						method181(l1 + TileUtils.method156(j3 & 7, j, i3 & 7), 0, buffer, l + TileUtils.method155(j, j3 & 7, i3 & 7), k1, j, 0);
					} else {
						method181(-1, 0, buffer, -1, 0, 0, 0);
					}
				}
			}
		}

	}

	public final void method180(byte[] data, int y, int x, int k, int l, CollisionMap[] collmaps) {
		for(int plane = 0; plane < 4; plane++) {
			for(int x1 = 0; x1 < 64; x1++) {
				for(int y1 = 0; y1 < 64; y1++) {
					if(x + x1 > 0 && x + x1 < 103 && y + y1 > 0 && y + y1 < 103) {
						collmaps[plane].flagMap[x + x1][y + y1] &= 0xfeffffff;
					}
				}
			}
		}
		final Buffer buffer = new Buffer(data);
		for(int plane = 0; plane < 4; plane++) {
			for(int x1 = 0; x1 < 64; x1++) {
				for(int y1 = 0; y1 < 64; y1++) {
					method181(y1 + y, l, buffer, x1 + x, plane, 0, k);
				}
			}
		}
	}

	private void method181(int y, int j, Buffer buffer, int x, int plane, int i1, int k1) {
		if(x >= 0 && x < 104 && y >= 0 && y < 104) {
			renderRuleFlags[plane][x][y] = 0;
			do {
				final int code = buffer.getUByte();
				if(code == 0) {
					if(plane == 0) {
						heightMap3d[0][x][y] = -method172(0xe3b7b + x + k1, 0x87cce + y + j) << 3;
						return;
					} else {
						heightMap3d[plane][x][y] = heightMap3d[plane - 1][x][y] - 240;
						return;
					}
				}
				if(code == 1) {
					int temp = buffer.getUByte();
					if(temp == 1) {
						temp = 0;
					}
					if(plane == 0) {
						heightMap3d[0][x][y] = -temp << 3;
						return;
					} else {
						heightMap3d[plane][x][y] = heightMap3d[plane - 1][x][y] - (temp << 3);
						return;
					}
				}
				if(code <= 49) {
					overlayFloorId[plane][x][y] = buffer.getSByte();
					tileShape[plane][x][y] = (byte) ((code - 2) >> 2);
					tileShapeRotation[plane][x][y] = (byte) (code - 2 + i1 & 3);
				} else if(code <= 81) {
					renderRuleFlags[plane][x][y] = (byte) (code - 49);
				} else {
					underlayFloorId[plane][x][y] = (byte) (code - 81);
				}
			} while(true);
		}
		do {
			final int code = buffer.getUByte();
			if(code == 0) {
				break;
			}
			if(code == 1) {
				buffer.getUByte();
				return;
			}
			if(code <= 49) {
				buffer.getUByte();
			}
		} while(true);
	}

	private int method182(int plane, int x, int y) {
		if((renderRuleFlags[plane][x][y] & 8) != 0) {
			return 0;
		}
		if(plane > 0 && (renderRuleFlags[1][x][y] & 2) != 0) {
			return plane - 1;
		} else {
			return plane;
		}
	}

	public final void method183(CollisionMap[] collmaps, Scene scene, int currentHeight, int j, int k, int l, byte[] data, int i1, int j1, int k1, boolean oldMap) {
		label0:
		{
			final Buffer buffer = new Buffer(data);
			int objectId = -1;
			do {
				final int objectIdIncrement = buffer.getUSmart();
				if(objectIdIncrement == 0) {
					break label0;
				}
				objectId += objectIdIncrement;
				int hash = 0;
				do {
					final int deltaPos = buffer.getUSmart();
					if(deltaPos == 0) {
						break;
					}
					hash += deltaPos - 1;
					final int localY = hash & 0x3f;
					final int localX = hash >> 6 & 0x3f;
					final int height = hash >> 12;
					final int attributeHashCode = buffer.getUByte();
					final int type = attributeHashCode >> 2;
					final int i4 = attributeHashCode & 3;
					if(height == currentHeight && localX >= i1 && localX < i1 + 8 && localY >= k && localY < k + 8) {
						final LocationType loc = LocationType.getSpecific(objectId, oldMap);
						final int x = j + TileUtils.method157(j1, loc.sizeY, localX & 7, localY & 7, loc.sizeX);
						final int y = k1 + TileUtils.method158(localY & 7, loc.sizeY, j1, loc.sizeX, localX & 7);


						if(x > 0 && y > 0 && x < 103 && y < 103) {
							int markingPlane = height;
							if((renderRuleFlags[1][x][y] & 2) == 2) {
								markingPlane--;
							}
							CollisionMap collmap = null;
							if(markingPlane >= 0) {
								collmap = collmaps[markingPlane];
							}
							method175(y, scene, collmap, type, l, x, objectId, i4 + j1 & 3, oldMap);
						}
					}
				} while(true);
			} while(true);
		}
	}

	private int method185(int i, int j) {
		if(i == -2) {
			return 0xbc614e;
		}
		if(i == -1) {
			if(j < 0) {
				j = 0;
			} else if(j > 127) {
				j = 127;
			}
			j = 127 - j;
			return j;
		}
		j = j * (i & 0x7f) / 128;
		if(j < 2) {
			j = 2;
		} else if(j > 126) {
			j = 126;
		}
		return (i & 0xff80) + j;
	}

	public final void method190(int i, CollisionMap[] collmaps, int j, Scene scene, byte[] data, boolean oldMap) {
		label0:
		{
			final Buffer buffer = new Buffer(data);
			int l = -1;
			do {
				int i1 = buffer.getUSmart();
				if(i1 == 0) {
					break label0;
				}
				l += i1;
				int j1 = 0;
				do {
					final int k1 = buffer.getUSmart();
					if(k1 == 0) {
						break;
					}
					j1 += k1 - 1;
					final int l1 = j1 & 0x3f;
					final int i2 = j1 >> 6 & 0x3f;
					final int j2 = j1 >> 12;
					final int k2 = buffer.getUByte();
					final int l2 = k2 >> 2;
					final int i3 = k2 & 3;
					final int j3 = i2 + i;
					final int k3 = l1 + j;
					
					if(j3 > 0 && k3 > 0 && j3 < 103 && k3 < 103) {
						int plane = j2;
						if((renderRuleFlags[1][j3][k3] & 2) == 2) {
							plane--;
						}
						CollisionMap collmap = null;
						if(plane >= 0 && plane < collmaps.length) {
							collmap = collmaps[plane];
						}
						method175(k3, scene, collmap, l2, j2, j3, l, i3, oldMap);
					}
				} while(true);
			} while(true);
		}
	}
}