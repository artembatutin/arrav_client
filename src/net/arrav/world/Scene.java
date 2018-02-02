package net.arrav.world;

import net.arrav.Constants;
import net.arrav.world.model.Model;
import net.arrav.Config;
import net.arrav.world.model.Entity;
import net.arrav.world.tile.*;
import net.arrav.graphic.Rasterizer3D;
import net.arrav.util.collect.LinkedDeque;

public final class Scene {
	
	private static final byte DRAW_DISTANCE_TILES = 30;
	public static int focalLength = 512;
	private final int planesDisplayed;
	private final int tilesDisplayedX;
	private final int tilesDisplayedY;
	private final int[][][] heightMap3d;
	private final Tile[][][] tileMap;
	private int currentPlane;
	private int entity1Count;
	private EntityUnit[] entities1;
	private final int[][][] heightMap2d;
	private static int anInt446;
	private static int cameraPlane;
	private static int drawCycle;
	private static int firstTileX;
	private static int lastTileX;
	private static int firstTileY;
	private static int lastTileY;
	private static int cameraTileX;
	private static int cameraTileY;
	private static int cameraPixelX;
	private static int cameraPixelY;
	private static int cameraPixelZ;
	private static int rollSine;
	private static int rollCosine;
	private static int yawSine;
	private static int yawCosine;
	private static EntityUnit[] entities2 = new EntityUnit[100];
	private static final int[] anIntArray463 = {53, -53, -53, 53};
	private static final int[] anIntArray464 = {-53, -53, 53, 53};
	private static final int[] anIntArray465 = {-45, 45, 45, -45};
	private static final int[] anIntArray466 = {45, 45, -45, -45};
	private static boolean hasClicked;
	private static int clickX;
	private static int clickY;
	public static int hoverX = -1;
	public static int hoverY = -1;
	private static final int planeCount;
	private static int[] perspectiveCount;
	private static Perspective[][] perspectives;
	private static int anInt475;
	private static final Perspective[] aClass47Array476 = new Perspective[500];
	private static LinkedDeque tilelist = new LinkedDeque();
	private static final int[] anIntArray478 = {19, 55, 38, 155, 255, 110, 137, 205, 76};
	private static final int[] anIntArray479 = {160, 192, 80, 96, 0, 144, 80, 48, 160};
	private static final int[] anIntArray480 = {76, 8, 137, 4, 0, 1, 38, 2, 19};
	private static final int[] anIntArray481 = {0, 0, 2, 0, 0, 2, 1, 1, 0};
	private static final int[] anIntArray482 = {2, 0, 0, 2, 0, 0, 0, 4, 4};
	private static final int[] anIntArray483 = {0, 4, 4, 8, 0, 0, 8, 0, 0};
	private static final int[] anIntArray484 = {1, 1, 0, 0, 0, 8, 0, 0, 8};
	private final int[] anIntArray486;
	private final int[] anIntArray487;
	private int anInt488;
	private final int[][] firstFloorHalfShapes = {new int[16], {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1}, {1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0}, {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1}, {0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0}, {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1}, {1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1}};
	private final int[][] secondFloorHalfShapes = {{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, {12, 8, 4, 0, 13, 9, 5, 1, 14, 10, 6, 2, 15, 11, 7, 3}, {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0}, {3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13, 0, 4, 8, 12}};
	private static boolean[][][][] aBooleanArrayArrayArrayArray491 = new boolean[8][32][(DRAW_DISTANCE_TILES << 1) | 1][(DRAW_DISTANCE_TILES << 1) | 1];
	private static boolean[][] aBooleanArrayArray492;
	private static int viewportMidX;
	private static int viewportMidY;
	private static int viewportLeft;
	private static int viewportTop;
	private static int viewportRight;
	private static int viewportBottom;
	
	static {
		planeCount = 4;
		perspectiveCount = new int[planeCount];
		perspectives = new Perspective[planeCount][500];
	}
	
	public Scene(int[][][] grndz) {
		final int tilesY = 104;
		final int tilesX = 104;
		final int planes = 4;
		entities1 = new EntityUnit[5000];
		anIntArray486 = new int[10000];
		anIntArray487 = new int[10000];
		planesDisplayed = planes;
		tilesDisplayedX = tilesX;
		tilesDisplayedY = tilesY;
		tileMap = new Tile[planes][tilesX][tilesY];
		heightMap2d = new int[planes][tilesX + 1][tilesY + 1];
		heightMap3d = grndz;
		clear();
	}
	
	static void method277(int i, int j, int k, int l, int i1, int j1, int l1, int i2) {
		final Perspective class47 = new Perspective();
		class47.tileX1 = j >> 7;
		class47.tileX2 = l >> 7;
		class47.tileY1 = l1 >> 7;
		class47.tileY2 = i1 >> 7;
		class47.direction = i2;
		class47.pixelX1 = j;
		class47.pixelX2 = l;
		class47.pixelY1 = l1;
		class47.pixelY2 = i1;
		class47.pixelZ1 = j1;
		class47.pixelZ2 = k;
		perspectives[i][perspectiveCount[i]++] = class47;
	}
	
	public static void setViewport(int minZ, int maxZ, int width, int height, int ai[]) {
		viewportLeft = 0;
		viewportTop = 0;
		viewportRight = width;
		viewportBottom = height;
		viewportMidX = width / 2;
		viewportMidY = height / 2;
		final boolean aflag[][][][] = new boolean[9][32][(DRAW_DISTANCE_TILES << 1) + 3][(DRAW_DISTANCE_TILES << 1) + 3];
		for(int roll = 128; roll <= 384; roll += 32) {
			for(int yaw = 0; yaw < 2048; yaw += 64) {
				rollSine = Model.angleSine[roll];
				rollCosine = Model.angleCosine[roll];
				yawSine = Model.angleSine[yaw];
				yawCosine = Model.angleCosine[yaw];
				final int l1 = (roll - 128) >> 5;
				final int j2 = yaw >> 6;
				for(int l2 = -DRAW_DISTANCE_TILES - 1; l2 <= DRAW_DISTANCE_TILES + 1; l2++) {
					for(int j3 = -DRAW_DISTANCE_TILES - 1; j3 <= DRAW_DISTANCE_TILES + 1; j3++) {
						final int k3 = l2 << 7;
						final int i4 = j3 << 7;
						boolean flag2 = false;
						for(int k4 = -minZ; k4 <= maxZ; k4 += 128) {
							if(!tileIsVisible(ai[l1] + k4, i4, k3)) {
								continue;
							}
							flag2 = true;
							break;
						}
						aflag[l1][j2][l2 + DRAW_DISTANCE_TILES + 1][j3 + DRAW_DISTANCE_TILES + 1] = flag2;
					}
				}
			}
		}
		for(int k1 = 0; k1 < 8; k1++) {
			for(int i2 = 0; i2 < 32; i2++) {
				for(int k2 = -DRAW_DISTANCE_TILES; k2 < DRAW_DISTANCE_TILES; k2++) {
					for(int i3 = -DRAW_DISTANCE_TILES; i3 < DRAW_DISTANCE_TILES; i3++) {
						boolean flag1 = false;
						label0:
						for(int l3 = -1; l3 <= 1; l3++) {
							for(int j4 = -1; j4 <= 1; j4++) {
								if(aflag[k1][i2][k2 + l3 + DRAW_DISTANCE_TILES + 1][i3 + j4 + DRAW_DISTANCE_TILES + 1]) {
									flag1 = true;
								} else if(aflag[k1][(i2 + 1) % 31][k2 + l3 + DRAW_DISTANCE_TILES + 1][i3 + j4 + DRAW_DISTANCE_TILES + 1]) {
									flag1 = true;
								} else if(aflag[k1 + 1][i2][k2 + l3 + DRAW_DISTANCE_TILES + 1][i3 + j4 + DRAW_DISTANCE_TILES + 1]) {
									flag1 = true;
								} else {
									if(!aflag[k1 + 1][(i2 + 1) % 31][k2 + l3 + DRAW_DISTANCE_TILES + 1][i3 + j4 + DRAW_DISTANCE_TILES + 1]) {
										continue;
									}
									flag1 = true;
								}
								break label0;
							}
						}
						aBooleanArrayArrayArrayArray491[k1][i2][k2 + DRAW_DISTANCE_TILES][i3 + DRAW_DISTANCE_TILES] = flag1;
					}
				}
			}
		}
	}
	
	private static boolean tileIsVisible(int z, int y, int x) {
		final int l = y * yawSine + x * yawCosine >> 16;
		final int i1 = y * yawCosine - x * yawSine >> 16;
		final int j1 = z * rollSine + i1 * rollCosine >> 16;
		final int k1 = z * rollCosine - i1 * rollSine >> 16;
		//TODO
		if(j1 < Constants.CAM_NEAR || j1 > Constants.CAM_FAR) {
			return false;
		}
		final int px = viewportMidX + (l * focalLength) / j1;
		final int py = viewportMidY + (k1 * focalLength) / j1;
		return px >= viewportLeft && px <= viewportRight && py >= viewportTop && py <= viewportBottom;
	}
	
	public static void reset() {
		entities2 = null;
		perspectiveCount = null;
		perspectives = null;
		tilelist = null;
		aBooleanArrayArrayArrayArray491 = null;
		aBooleanArrayArray492 = null;
	}
	
	private void drawComplexGround(ShapedGround ground, int x, int y, int rollsin, int rollcos, int yawsin, int yawcos) {
		int length = ground.vertexX.length;
		for(int i = 0; i < length; i++) {
			int tvx = ground.vertexX[i] - cameraPixelX;
			int tvy = ground.vertexZ[i] - cameraPixelY;
			int tvz = ground.vertexY[i] - cameraPixelZ;
			int k3 = tvz * yawsin + tvx * yawcos >> 16;
			tvz = tvz * yawcos - tvx * yawsin >> 16;
			tvx = k3;
			k3 = tvy * rollcos - tvz * rollsin >> 16;
			tvz = tvy * rollsin + tvz * rollcos >> 16;
			tvy = k3;
			if(tvz < Constants.CAM_NEAR) {
				return;
			}
			if(ground.texId != null || Config.def.groundMat) {
				ShapedGround.texVertexX[i] = tvx;
				ShapedGround.texVertexY[i] = tvy;
				ShapedGround.texVertexZ[i] = tvz;
			}
			ShapedGround.vertex2dX[i] = Rasterizer3D.viewport.centerX + (tvx * focalLength) / tvz;
			ShapedGround.vertex2dY[i] = Rasterizer3D.viewport.centerY + (tvy * focalLength) / tvz;
			ShapedGround.vertex2dZ[i] = tvz;
		}
		
		Rasterizer3D.alphaFilter = 0;
		length = ground.tex1.length;
		for(int i = 0; i < length; i++) {
			final int l2 = ground.tex1[i];
			final int j3 = ground.tex2[i];
			final int l3 = ground.tex3[i];
			final int x1 = ShapedGround.vertex2dX[l2];
			final int x2 = ShapedGround.vertex2dX[j3];
			final int x3 = ShapedGround.vertex2dX[l3];
			final int y1 = ShapedGround.vertex2dY[l2];
			final int y2 = ShapedGround.vertex2dY[j3];
			final int y3 = ShapedGround.vertex2dY[l3];
			if((x1 - x2) * (y3 - y2) - (y1 - y2) * (x3 - x2) > 0) {
				final int z1 = ShapedGround.vertex2dZ[l2];
				final int z2 = ShapedGround.vertex2dZ[j3];
				final int z3 = ShapedGround.vertex2dZ[l3];
				Rasterizer3D.clippedScan = x1 < 0 || x2 < 0 || x3 < 0 || x1 > Rasterizer3D.viewport.width || x2 > Rasterizer3D.viewport.width || x3 > Rasterizer3D.viewport.width;
				if(hasClicked && triangleContains(clickX, clickY, y1, y2, y3, x1, x2, x3)) {
					hoverX = x;
					hoverY = y;
				}
				if((ground.texId == null || ground.texId[i] == -1) || !Config.def.groundMat) {
					if(ground.color1[i] != 12345678) {
						Rasterizer3D.drawGouraudTriangle(y1, y2, y3, x1, x2, x3, z1, z2, z3, ground.color1[i], ground.color2[i], ground.color3[i]);
					}
				} else {
					if(ground.color1[i] != 12345678) {
						if(ground.messed) {
							Rasterizer3D.drawTexturedTriangle(y1, y2, y3, x1, x2, x3, z1, z2, z3, ground.color1[i], ground.color2[i], ground.color3[i], ShapedGround.texVertexX[0], ShapedGround.texVertexX[1], ShapedGround.texVertexX[3], ShapedGround.texVertexY[0], ShapedGround.texVertexY[1], ShapedGround.texVertexY[3], ShapedGround.texVertexZ[0], ShapedGround.texVertexZ[1], ShapedGround.texVertexZ[3], ground.texId[i], Config.def.groundMat, true);
						} else {
							Rasterizer3D.drawTexturedTriangle(y1, y2, y3, x1, x2, x3, z1, z2, z3, ground.color1[i], ground.color2[i], ground.color3[i], ShapedGround.texVertexX[l2], ShapedGround.texVertexX[j3], ShapedGround.texVertexX[l3], ShapedGround.texVertexY[l2], ShapedGround.texVertexY[j3], ShapedGround.texVertexY[l3], ShapedGround.texVertexZ[l2], ShapedGround.texVertexZ[j3], ShapedGround.texVertexZ[l3], ground.texId[i], Config.def.groundMat, true);
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * Draws the scene landscape and spawns by given position and rotation
	 * values.
	 */
	public void drawScene(int camX, int camY, int camZ, int camYaw, int camRoll, int camPlane, int fogRgb) {
		Rasterizer3D.clearDepthBuffer();
		focalLength = Rasterizer3D.viewport.width;
		if(camX < 0) {
			camX = 0;
		} else if(camX >= tilesDisplayedX << 7) {
			camX = (tilesDisplayedX << 7) - 1;
		}
		if(camY < 0) {
			camY = 0;
		} else if(camY >= tilesDisplayedY << 7) {
			camY = (tilesDisplayedY << 7) - 1;
		}
		drawCycle++;
		rollSine = Model.angleSine[camRoll];
		rollCosine = Model.angleCosine[camRoll];
		yawSine = Model.angleSine[camYaw];
		yawCosine = Model.angleCosine[camYaw];
		aBooleanArrayArray492 = aBooleanArrayArrayArrayArray491[(camRoll - 128) / 32][camYaw / 64];
		cameraPixelX = camX;
		cameraPixelY = camZ;
		cameraPixelZ = camY;
		cameraTileX = camX >> 7;
		cameraTileY = camY >> 7;
		cameraPlane = camPlane;
		firstTileX = cameraTileX - DRAW_DISTANCE_TILES;
		if(firstTileX < 0) {
			firstTileX = 0;
		}
		firstTileY = cameraTileY - DRAW_DISTANCE_TILES;
		if(firstTileY < 0) {
			firstTileY = 0;
		}
		lastTileX = cameraTileX + DRAW_DISTANCE_TILES;
		if(lastTileX > tilesDisplayedX) {
			lastTileX = tilesDisplayedX;
		}
		lastTileY = cameraTileY + DRAW_DISTANCE_TILES;
		if(lastTileY > tilesDisplayedY) {
			lastTileY = tilesDisplayedY;
		}
		method319();
		anInt446 = 0;
		for(int plane = currentPlane; plane < planesDisplayed; plane++) {
			final Tile[][] tiles = tileMap[plane];
			for(int x = firstTileX; x < lastTileX; x++) {
				for(int y = firstTileY; y < lastTileY; y++) {
					final Tile tile = tiles[x][y];
					if(tile != null) {
						if(tile.anInt1321 > camPlane || !aBooleanArrayArray492[x - cameraTileX + DRAW_DISTANCE_TILES][y - cameraTileY + DRAW_DISTANCE_TILES] && heightMap3d[plane][x][y] - camZ < 2000) {//XXX 2000 = original
							tile.needToDraw = false;
							tile.aBoolean1323 = false;
							tile.anInt1325 = 0;
						} else {
							tile.needToDraw = true;
							tile.aBoolean1323 = true;
							tile.aBoolean1324 = tile.entityUnitAmount > 0;
							anInt446++;
						}
					}
				}
			}
		}
		for(int plane = currentPlane; plane < planesDisplayed; plane++) {
			final Tile[][] tiles = tileMap[plane];
			for(int tileX = -DRAW_DISTANCE_TILES; tileX <= 0; tileX++) {
				final int startX = cameraTileX + tileX;
				final int endX = cameraTileX - tileX;
				if(startX >= firstTileX || endX < lastTileX) {
					for(int tileY = -DRAW_DISTANCE_TILES; tileY <= 0; tileY++) {
						final int startY = cameraTileY + tileY;
						final int endY = cameraTileY - tileY;
						if(startX >= firstTileX) {
							if(startY >= firstTileY) {
								final Tile tile = tiles[startX][startY];
								if(tile != null && tile.needToDraw) {
									drawTile(tile, true);
								}
							}
							if(endY < lastTileY) {
								final Tile tile = tiles[startX][endY];
								if(tile != null && tile.needToDraw) {
									drawTile(tile, true);
								}
							}
						}
						if(endX < lastTileX) {
							if(startY >= firstTileY) {
								final Tile tile = tiles[endX][startY];
								if(tile != null && tile.needToDraw) {
									drawTile(tile, true);
								}
							}
							if(endY < lastTileY) {
								final Tile tile = tiles[endX][endY];
								if(tile != null && tile.needToDraw) {
									drawTile(tile, true);
								}
							}
						}
						if(anInt446 == 0) {
							hasClicked = false;
							focalLength = 512;
							Rasterizer3D.drawFog(Constants.FOG_BEGIN, Constants.FOG_END, fogRgb);
							return;
						}
					}
				}
			}
		}
		for(int plane = currentPlane; plane < planesDisplayed; plane++) {
			final Tile[][] tiles = tileMap[plane];
			for(int tileX = -DRAW_DISTANCE_TILES; tileX <= 0; tileX++) {
				final int x1 = cameraTileX + tileX;
				final int x2 = cameraTileX - tileX;
				if(x1 >= firstTileX || x2 < lastTileX) {
					for(int tileY = -DRAW_DISTANCE_TILES; tileY <= 0; tileY++) {
						final int y1 = cameraTileY + tileY;
						final int y2 = cameraTileY - tileY;
						if(x1 >= firstTileX) {
							if(y1 >= firstTileY) {
								final Tile tile = tiles[x1][y1];
								if(tile != null && tile.needToDraw) {
									drawTile(tile, false);
								}
							}
							if(y2 < lastTileY) {
								final Tile tile = tiles[x1][y2];
								if(tile != null && tile.needToDraw) {
									drawTile(tile, false);
								}
							}
						}
						if(x2 < lastTileX) {
							if(y1 >= firstTileY) {
								final Tile tile = tiles[x2][y1];
								if(tile != null && tile.needToDraw) {
									drawTile(tile, false);
								}
							}
							if(y2 < lastTileY) {
								final Tile tile = tiles[x2][y2];
								if(tile != null && tile.needToDraw) {
									drawTile(tile, false);
								}
							}
						}
						if(anInt446 == 0) {
							hasClicked = false;
							focalLength = 512;
							Rasterizer3D.drawFog(Constants.FOG_BEGIN, Constants.FOG_END, fogRgb);
							return;
						}
					}
				}
			}
		}
		hasClicked = false;
		focalLength = 512;
		Rasterizer3D.drawFog(Constants.FOG_BEGIN, Constants.FOG_END, fogRgb);
	}
	
	private void drawTile(Tile tile, boolean flag) {
		tilelist.addLast(tile);
		do {
			Tile tile1;
			do {
				tile1 = (Tile) tilelist.removeFirst();
				if(tile1 == null) {
					return;
				}
			} while(!tile1.aBoolean1323);
			final int x = tile1.x;
			final int y = tile1.y;
			final int plane = tile1.plane;
			final int z = tile1.z;
			final Tile[][] tiles = tileMap[plane];
			if(tile1.needToDraw) {
				if(flag) {
					if(plane > 0) {
						final Tile tile_2 = tileMap[plane - 1][x][y];
						if(tile_2 != null && tile_2.aBoolean1323) {
							continue;
						}
					}
					if(x <= cameraTileX && x > firstTileX) {
						final Tile tile_3 = tiles[x - 1][y];
						if(tile_3 != null && tile_3.aBoolean1323 && (tile_3.needToDraw || (tile1.anInt1320 & 1) == 0)) {
							continue;
						}
					}
					if(x >= cameraTileX && x < lastTileX - 1) {
						final Tile tile_4 = tiles[x + 1][y];
						if(tile_4 != null && tile_4.aBoolean1323 && (tile_4.needToDraw || (tile1.anInt1320 & 4) == 0)) {
							continue;
						}
					}
					if(y <= cameraTileY && y > firstTileY) {
						final Tile tile_5 = tiles[x][y - 1];
						if(tile_5 != null && tile_5.aBoolean1323 && (tile_5.needToDraw || (tile1.anInt1320 & 8) == 0)) {
							continue;
						}
					}
					if(y >= cameraTileY && y < lastTileY - 1) {
						final Tile tile_6 = tiles[x][y + 1];
						if(tile_6 != null && tile_6.aBoolean1323 && (tile_6.needToDraw || (tile1.anInt1320 & 2) == 0)) {
							continue;
						}
					}
				} else {
					flag = true;
				}
				tile1.needToDraw = false;
				if(tile1.tile != null) {
					final Tile tile_2 = tile1.tile;
					if(tile_2.quadGround != null) {
						if(!groundHidden(0, x, y)) {
							drawGenericGround(tile_2.quadGround, 0, x, y, rollSine, rollCosine, yawSine, yawCosine);
						}
					} else if(tile_2.shapedGround != null && !groundHidden(0, x, y)) {
						drawComplexGround(tile_2.shapedGround, x, y, rollSine, rollCosine, yawSine, yawCosine);
					}
					final Wall wall = tile_2.wall;
					if(wall != null) {
						wall.model1.drawModel(0, rollSine, rollCosine, yawSine, yawCosine, wall.x - cameraPixelX, wall.y - cameraPixelY, wall.z - cameraPixelZ, wall.hash, Rasterizer3D.TYPES[6]);
					}
					for(int i2 = 0; i2 < tile_2.entityUnitAmount; i2++) {
						final EntityUnit entity = tile_2.entityUnit[i2];
						if(entity != null) {
							entity.model.drawModel(entity.yaw, rollSine, rollCosine, yawSine, yawCosine, entity.x - cameraPixelX, entity.y - cameraPixelY, entity.z - cameraPixelZ, entity.hash, entity.model.getType());
						}
					}
				}
				boolean groundExists = false;
				if(tile1.quadGround != null) {
					if(!groundHidden(z, x, y)) {
						groundExists = true;
						drawGenericGround(tile1.quadGround, z, x, y, rollSine, rollCosine, yawSine, yawCosine);
					}
				} else if(tile1.shapedGround != null && !groundHidden(z, x, y)) {
					groundExists = true;
					drawComplexGround(tile1.shapedGround, x, y, rollSine, rollCosine, yawSine, yawCosine);
				}
				int j1 = 0;
				int j2 = 0;
				final Wall wall = tile1.wall;
				final WallDecoration walldecor = tile1.wallDecor;
				if(wall != null || walldecor != null) {
					if(cameraTileX == x) {
						j1++;
					} else if(cameraTileX < x) {
						j1 += 2;
					}
					if(cameraTileY == y) {
						j1 += 3;
					} else if(cameraTileY > y) {
						j1 += 6;
					}
					j2 = anIntArray478[j1];
					tile1.anInt1328 = anIntArray480[j1];
				}
				if(wall != null) {
					if((wall.face1 & anIntArray479[j1]) != 0) {
						if(wall.face1 == 16) {
							tile1.anInt1325 = 3;
							tile1.anInt1326 = anIntArray481[j1];
							tile1.anInt1327 = 3 - tile1.anInt1326;
						} else if(wall.face1 == 32) {
							tile1.anInt1325 = 6;
							tile1.anInt1326 = anIntArray482[j1];
							tile1.anInt1327 = 6 - tile1.anInt1326;
						} else if(wall.face1 == 64) {
							tile1.anInt1325 = 12;
							tile1.anInt1326 = anIntArray483[j1];
							tile1.anInt1327 = 12 - tile1.anInt1326;
						} else {
							tile1.anInt1325 = 9;
							tile1.anInt1326 = anIntArray484[j1];
							tile1.anInt1327 = 9 - tile1.anInt1326;
						}
					} else {
						tile1.anInt1325 = 0;
					}
					if((wall.face1 & j2) != 0 && !method321(z, x, y, wall.face1)) {
						wall.model1.drawModel(0, rollSine, rollCosine, yawSine, yawCosine, wall.x - cameraPixelX, wall.y - cameraPixelY, wall.z - cameraPixelZ, wall.hash, Rasterizer3D.TYPES[6]);
					}
					if((wall.face2 & j2) != 0 && !method321(z, x, y, wall.face2)) {
						wall.model2.drawModel(0, rollSine, rollCosine, yawSine, yawCosine, wall.x - cameraPixelX, wall.y - cameraPixelY, wall.z - cameraPixelZ, wall.hash, Rasterizer3D.TYPES[6]);
					}
				}
				if(walldecor != null && !method322(z, x, y, walldecor.model.maxVerticalDistUp)) {
					if((walldecor.face & j2) != 0) {
						walldecor.model.drawModel(walldecor.anInt503, rollSine, rollCosine, yawSine, yawCosine, walldecor.x - cameraPixelX, walldecor.y - cameraPixelY, walldecor.z - cameraPixelZ, walldecor.hash, Rasterizer3D.TYPES[5]);
					} else if((walldecor.face & 0x300) != 0) {
						final int j4 = walldecor.x - cameraPixelX;
						final int l5 = walldecor.y - cameraPixelY;
						final int k6 = walldecor.z - cameraPixelZ;
						final int i8 = walldecor.anInt503;
						int k9;
						if(i8 == 1 || i8 == 2) {
							k9 = -j4;
						} else {
							k9 = j4;
						}
						int k10;
						if(i8 == 2 || i8 == 3) {
							k10 = -k6;
						} else {
							k10 = k6;
						}
						if((walldecor.face & 0x100) != 0 && k10 < k9) {
							final int i11 = j4 + anIntArray463[i8];
							final int k11 = k6 + anIntArray464[i8];
							walldecor.model.drawModel(i8 * 512 + 256, rollSine, rollCosine, yawSine, yawCosine, i11, l5, k11, walldecor.hash, Rasterizer3D.TYPES[5]);
						}
						if((walldecor.face & 0x200) != 0 && k10 > k9) {
							final int j11 = j4 + anIntArray465[i8];
							final int l11 = k6 + anIntArray466[i8];
							walldecor.model.drawModel(i8 * 512 + 1280 & 0x7ff, rollSine, rollCosine, yawSine, yawCosine, j11, l5, l11, walldecor.hash, Rasterizer3D.TYPES[5]);
						}
					}
				}
				if(groundExists) {
					final GroundDecoration grounddecor = tile1.groundDecor;
					if(grounddecor != null) {
						grounddecor.model.drawModel(0, rollSine, rollCosine, yawSine, yawCosine, grounddecor.x - cameraPixelX, grounddecor.y - cameraPixelY, grounddecor.z - cameraPixelZ, grounddecor.hash, Rasterizer3D.TYPES[4]);
					}
					final ObjectUnit object = tile1.objectUnit;
					if(object != null && object.anInt52 == 0) {
						if(object.model2 != null) {
							object.model2.drawModel(0, rollSine, rollCosine, yawSine, yawCosine, object.x - cameraPixelX, object.y - cameraPixelY, object.z - cameraPixelZ, object.hash, Rasterizer3D.TYPES[0]);
						}
						if(object.model3 != null) {
							object.model3.drawModel(0, rollSine, rollCosine, yawSine, yawCosine, object.x - cameraPixelX, object.y - cameraPixelY, object.z - cameraPixelZ, object.hash, Rasterizer3D.TYPES[0]);
						}
						if(object.model1 != null) {
							object.model1.drawModel(0, rollSine, rollCosine, yawSine, yawCosine, object.x - cameraPixelX, object.y - cameraPixelY, object.z - cameraPixelZ, object.hash, Rasterizer3D.TYPES[0]);
						}
					}
				}
				final int k4 = tile1.anInt1320;
				if(k4 != 0) {
					if(x < cameraTileX && (k4 & 4) != 0) {
						final Tile tile_17 = tiles[x + 1][y];
						if(tile_17 != null && tile_17.aBoolean1323) {
							tilelist.addLast(tile_17);
						}
					}
					if(y < cameraTileY && (k4 & 2) != 0) {
						final Tile tile_18 = tiles[x][y + 1];
						if(tile_18 != null && tile_18.aBoolean1323) {
							tilelist.addLast(tile_18);
						}
					}
					if(x > cameraTileX && (k4 & 1) != 0) {
						final Tile tile_19 = tiles[x - 1][y];
						if(tile_19 != null && tile_19.aBoolean1323) {
							tilelist.addLast(tile_19);
						}
					}
					if(y > cameraTileY && (k4 & 8) != 0) {
						final Tile tile_20 = tiles[x][y - 1];
						if(tile_20 != null && tile_20.aBoolean1323) {
							tilelist.addLast(tile_20);
						}
					}
				}
			}
			if(tile1.anInt1325 != 0) {
				boolean flag2 = true;
				for(int k1 = 0; k1 < tile1.entityUnitAmount; k1++) {
					if(tile1.entityUnit[k1].anInt528 == drawCycle || (tile1.entityUnitSize[k1] & tile1.anInt1325) != tile1.anInt1326) {
						continue;
					}
					flag2 = false;
					break;
				}
				
				if(flag2) {
					final Wall wall = tile1.wall;
					if(!method321(z, x, y, wall.face1)) {
						wall.model1.drawModel(0, rollSine, rollCosine, yawSine, yawCosine, wall.x - cameraPixelX, wall.y - cameraPixelY, wall.z - cameraPixelZ, wall.hash, Rasterizer3D.TYPES[6]);
					}
					tile1.anInt1325 = 0;
				}
			}
			if(tile1.aBoolean1324) {
				try {
					final int i1 = tile1.entityUnitAmount;
					tile1.aBoolean1324 = false;
					int l1 = 0;
					label0:
					for(int k2 = 0; k2 < i1; k2++) {
						final EntityUnit npcspw = tile1.entityUnit[k2];
						if(npcspw.anInt528 == drawCycle) {
							continue;
						}
						for(int k3 = npcspw.tileX; k3 <= npcspw.sizeX; k3++) {
							for(int l4 = npcspw.tileY; l4 <= npcspw.sizeY; l4++) {
								final Tile class30_sub3_21 = tiles[k3][l4];
								if(class30_sub3_21.needToDraw) {
									tile1.aBoolean1324 = true;
								} else {
									if(class30_sub3_21.anInt1325 == 0) {
										continue;
									}
									int l6 = 0;
									if(k3 > npcspw.tileX) {
										l6++;
									}
									if(k3 < npcspw.sizeX) {
										l6 += 4;
									}
									if(l4 > npcspw.tileY) {
										l6 += 8;
									}
									if(l4 < npcspw.sizeY) {
										l6 += 2;
									}
									if((l6 & class30_sub3_21.anInt1325) != tile1.anInt1327) {
										continue;
									}
									tile1.aBoolean1324 = true;
								}
								continue label0;
							}
						}
						
						entities2[l1++] = npcspw;
						int i5 = cameraTileX - npcspw.tileX;
						final int i6 = npcspw.sizeX - cameraTileX;
						if(i6 > i5) {
							i5 = i6;
						}
						final int i7 = cameraTileY - npcspw.tileY;
						final int j8 = npcspw.sizeY - cameraTileY;
						if(j8 > i7) {
							npcspw.anInt527 = i5 + j8;
						} else {
							npcspw.anInt527 = i5 + i7;
						}
					}
					
					while(l1 > 0) {
						int i3 = -50;
						int l3 = -1;
						for(int j5 = 0; j5 < l1; j5++) {
							final EntityUnit npcspw = entities2[j5];
							if(npcspw.anInt528 != drawCycle) {
								if(npcspw.anInt527 > i3) {
									i3 = npcspw.anInt527;
									l3 = j5;
								} else if(npcspw.anInt527 == i3) {
									final int j7 = npcspw.x - cameraPixelX;
									final int k8 = npcspw.z - cameraPixelZ;
									final int l9 = entities2[l3].x - cameraPixelX;
									final int l10 = entities2[l3].z - cameraPixelZ;
									if(j7 * j7 + k8 * k8 > l9 * l9 + l10 * l10) {
										l3 = j5;
									}
								}
							}
						}
						if(l3 == -1) {
							break;
						}
						final EntityUnit npcspw = entities2[l3];
						npcspw.anInt528 = drawCycle;
						if(!method323(z, npcspw.tileX, npcspw.sizeX, npcspw.tileY, npcspw.sizeY, npcspw.model.maxVerticalDistUp)) {
							npcspw.model.drawModel(npcspw.yaw, rollSine, rollCosine, yawSine, yawCosine, npcspw.x - cameraPixelX, npcspw.y - cameraPixelY, npcspw.z - cameraPixelZ, npcspw.hash, npcspw.model.getType());
						}
						for(int k7 = npcspw.tileX; k7 <= npcspw.sizeX; k7++) {
							for(int l8 = npcspw.tileY; l8 <= npcspw.sizeY; l8++) {
								final Tile class30_sub3_22 = tiles[k7][l8];
								if(class30_sub3_22.anInt1325 != 0) {
									tilelist.addLast(class30_sub3_22);
								} else if((k7 != x || l8 != y) && class30_sub3_22.aBoolean1323) {
									tilelist.addLast(class30_sub3_22);
								}
							}
						}
						
					}
					if(tile1.aBoolean1324) {
						continue;
					}
				} catch(final Exception _ex) {
					_ex.printStackTrace();
					tile1.aBoolean1324 = false;
				}
			}
			if(!tile1.aBoolean1323 || tile1.anInt1325 != 0) {
				continue;
			}
			if(x <= cameraTileX && x > firstTileX) {
				final Tile class30_sub3_8 = tiles[x - 1][y];
				if(class30_sub3_8 != null && class30_sub3_8.aBoolean1323) {
					continue;
				}
			}
			if(x >= cameraTileX && x < lastTileX - 1) {
				final Tile class30_sub3_9 = tiles[x + 1][y];
				if(class30_sub3_9 != null && class30_sub3_9.aBoolean1323) {
					continue;
				}
			}
			if(y <= cameraTileY && y > firstTileY) {
				final Tile class30_sub3_10 = tiles[x][y - 1];
				if(class30_sub3_10 != null && class30_sub3_10.aBoolean1323) {
					continue;
				}
			}
			if(y >= cameraTileY && y < lastTileY - 1) {
				final Tile class30_sub3_11 = tiles[x][y + 1];
				if(class30_sub3_11 != null && class30_sub3_11.aBoolean1323) {
					continue;
				}
			}
			tile1.aBoolean1323 = false;
			anInt446--;
			final ObjectUnit itemspw = tile1.objectUnit;
			if(itemspw != null && itemspw.anInt52 != 0) {
				if(itemspw.model2 != null) {
					itemspw.model2.drawModel(0, rollSine, rollCosine, yawSine, yawCosine, itemspw.x - cameraPixelX, itemspw.y - cameraPixelY - itemspw.anInt52, itemspw.z - cameraPixelZ, itemspw.hash, Rasterizer3D.TYPES[0]);
				}
				if(itemspw.model3 != null) {
					itemspw.model3.drawModel(0, rollSine, rollCosine, yawSine, yawCosine, itemspw.x - cameraPixelX, itemspw.y - cameraPixelY - itemspw.anInt52, itemspw.z - cameraPixelZ, itemspw.hash, Rasterizer3D.TYPES[0]);
				}
				if(itemspw.model1 != null) {
					itemspw.model1.drawModel(0, rollSine, rollCosine, yawSine, yawCosine, itemspw.x - cameraPixelX, itemspw.y - cameraPixelY - itemspw.anInt52, itemspw.z - cameraPixelZ, itemspw.hash, Rasterizer3D.TYPES[0]);
				}
			}
			if(tile1.anInt1328 != 0) {
				final WallDecoration walldec = tile1.wallDecor;
				if(walldec != null && !method322(z, x, y, walldec.model.maxVerticalDistUp)) {
					if((walldec.face & tile1.anInt1328) != 0) {
						walldec.model.drawModel(walldec.anInt503, rollSine, rollCosine, yawSine, yawCosine, walldec.x - cameraPixelX, walldec.y - cameraPixelY, walldec.z - cameraPixelZ, walldec.hash, Rasterizer3D.TYPES[5]);
					} else if((walldec.face & 0x300) != 0) {
						final int xoff = walldec.x - cameraPixelX;
						final int zoff = walldec.y - cameraPixelY;
						final int yoff = walldec.z - cameraPixelZ;
						final int k5 = walldec.anInt503;
						int j6;
						if(k5 == 1 || k5 == 2) {
							j6 = -xoff;
						} else {
							j6 = xoff;
						}
						int l7;
						if(k5 == 2 || k5 == 3) {
							l7 = -yoff;
						} else {
							l7 = yoff;
						}
						if((walldec.face & 0x100) != 0 && l7 >= j6) {
							final int i9 = xoff + anIntArray463[k5];
							final int i10 = yoff + anIntArray464[k5];
							walldec.model.drawModel(k5 * 512 + 256, rollSine, rollCosine, yawSine, yawCosine, i9, zoff, i10, walldec.hash, Rasterizer3D.TYPES[5]);
						}
						if((walldec.face & 0x200) != 0 && l7 <= j6) {
							final int j9 = xoff + anIntArray465[k5];
							final int j10 = yoff + anIntArray466[k5];
							walldec.model.drawModel(k5 * 512 + 1280 & 0x7ff, rollSine, rollCosine, yawSine, yawCosine, j9, zoff, j10, walldec.hash, Rasterizer3D.TYPES[5]);
						}
					}
				}
				final Wall wall = tile1.wall;
				if(wall != null) {
					if((wall.face2 & tile1.anInt1328) != 0 && !method321(z, x, y, wall.face2)) {
						wall.model2.drawModel(0, rollSine, rollCosine, yawSine, yawCosine, wall.x - cameraPixelX, wall.y - cameraPixelY, wall.z - cameraPixelZ, wall.hash, Rasterizer3D.TYPES[6]);
					}
					if((wall.face1 & tile1.anInt1328) != 0 && !method321(z, x, y, wall.face1)) {
						wall.model1.drawModel(0, rollSine, rollCosine, yawSine, yawCosine, wall.x - cameraPixelX, wall.y - cameraPixelY, wall.z - cameraPixelZ, wall.hash, Rasterizer3D.TYPES[6]);
					}
				}
			}
			if(plane < planesDisplayed - 1) {
				final Tile class30_sub3_12 = tileMap[plane + 1][x][y];
				if(class30_sub3_12 != null && class30_sub3_12.aBoolean1323) {
					tilelist.addLast(class30_sub3_12);
				}
			}
			if(x < cameraTileX) {
				final Tile class30_sub3_13 = tiles[x + 1][y];
				if(class30_sub3_13 != null && class30_sub3_13.aBoolean1323) {
					tilelist.addLast(class30_sub3_13);
				}
			}
			if(y < cameraTileY) {
				final Tile class30_sub3_14 = tiles[x][y + 1];
				if(class30_sub3_14 != null && class30_sub3_14.aBoolean1323) {
					tilelist.addLast(class30_sub3_14);
				}
			}
			if(x > cameraTileX) {
				final Tile class30_sub3_15 = tiles[x - 1][y];
				if(class30_sub3_15 != null && class30_sub3_15.aBoolean1323) {
					tilelist.addLast(class30_sub3_15);
				}
			}
			if(y > cameraTileY) {
				final Tile class30_sub3_16 = tiles[x][y - 1];
				if(class30_sub3_16 != null && class30_sub3_16.aBoolean1323) {
					tilelist.addLast(class30_sub3_16);
				}
			}
		} while(true);
	}
	
	private void drawGenericGround(QuadGround ground, int plane, int x, int y, int rollsin, int rollcos, int yawsin, int yawcos) {
		if(ground.texture == 5) {
			return;
		}
		int x3d2;
		int x3d1 = x3d2 = (x << 7) - cameraPixelX;
		int y3d2;
		int y3d1 = y3d2 = (y << 7) - cameraPixelZ;
		int x3d4;
		int x3d3 = x3d4 = x3d1 + 128;
		int y3d4;
		int y3d3 = y3d4 = y3d1 + 128;
		int z1 = heightMap3d[plane][x][y] - cameraPixelY;
		int z2 = heightMap3d[plane][x + 1][y] - cameraPixelY;
		int z3 = heightMap3d[plane][x + 1][y + 1] - cameraPixelY;
		int z4 = heightMap3d[plane][x][y + 1] - cameraPixelY;
		int l4 = y3d1 * yawsin + x3d1 * yawcos >> 16;
		y3d1 = y3d1 * yawcos - x3d1 * yawsin >> 16;
		x3d1 = l4;
		l4 = z1 * rollcos - y3d1 * rollsin >> 16;
		y3d1 = z1 * rollsin + y3d1 * rollcos >> 16;
		z1 = l4;
		if(y3d1 < Constants.CAM_NEAR) {
			return;
		}
		l4 = y3d2 * yawsin + x3d3 * yawcos >> 16;
		y3d2 = y3d2 * yawcos - x3d3 * yawsin >> 16;
		x3d3 = l4;
		l4 = z2 * rollcos - y3d2 * rollsin >> 16;
		y3d2 = z2 * rollsin + y3d2 * rollcos >> 16;
		z2 = l4;
		if(y3d2 < Constants.CAM_NEAR) {
			return;
		}
		l4 = y3d3 * yawsin + x3d4 * yawcos >> 16;
		y3d3 = y3d3 * yawcos - x3d4 * yawsin >> 16;
		x3d4 = l4;
		l4 = z3 * rollcos - y3d3 * rollsin >> 16;
		y3d3 = z3 * rollsin + y3d3 * rollcos >> 16;
		z3 = l4;
		if(y3d3 < Constants.CAM_NEAR) {
			return;
		}
		l4 = y3d4 * yawsin + x3d2 * yawcos >> 16;
		y3d4 = y3d4 * yawcos - x3d2 * yawsin >> 16;
		x3d2 = l4;
		l4 = z4 * rollcos - y3d4 * rollsin >> 16;
		y3d4 = z4 * rollsin + y3d4 * rollcos >> 16;
		z4 = l4;
		if(y3d4 < Constants.CAM_NEAR) {
			return;
		}
		int x1 = Rasterizer3D.viewport.centerX + (x3d1 * focalLength) / y3d1;
		int y1 = Rasterizer3D.viewport.centerY + (z1 * focalLength) / y3d1;
		int x2 = Rasterizer3D.viewport.centerX + (x3d3 * focalLength) / y3d2;
		int y2 = Rasterizer3D.viewport.centerY + (z2 * focalLength) / y3d2;
		int x3 = Rasterizer3D.viewport.centerX + (x3d4 * focalLength) / y3d3;
		int y3 = Rasterizer3D.viewport.centerY + (z3 * focalLength) / y3d3;
		int x4 = Rasterizer3D.viewport.centerX + (x3d2 * focalLength) / y3d4;
		int y4 = Rasterizer3D.viewport.centerY + (z4 * focalLength) / y3d4;
		Rasterizer3D.alphaFilter = 0;
		if((x3 - x4) * (y2 - y4) - (y3 - y4) * (x2 - x4) > 0) {
			Rasterizer3D.clippedScan = x3 < 0 || x4 < 0 || x2 < 0 || x3 > Rasterizer3D.viewport.width || x4 > Rasterizer3D.viewport.width || x2 > Rasterizer3D.viewport.width;
			if(hasClicked && triangleContains(clickX, clickY, y3, y4, y2, x3, x4, x2)) {
				hoverX = x;
				hoverY = y;
			}
			if(ground.texture != -1 && Config.def.groundMat) {
				if(ground.color3 != 12345678) {
					if(ground.solid) {
						Rasterizer3D.drawTexturedTriangle(y3, y4, y2, x3, x4, x2, y3d3, y3d4, y3d2, ground.color3, ground.color4, ground.color2, x3d1, x3d3, x3d2, z1, z2, z4, y3d1, y3d2, y3d4, ground.texture, Config.def.groundMat, true);
					} else {
						Rasterizer3D.drawTexturedTriangle(y3, y4, y2, x3, x4, x2, y3d3, y3d4, y3d2, ground.color3, ground.color4, ground.color2, x3d4, x3d2, x3d3, z3, z4, z2, y3d3, y3d4, y3d2, ground.texture, Config.def.groundMat, true);
					}
				}
			} else if(ground.color3 != 12345678) {
				Rasterizer3D.drawGouraudTriangle(y3, y4, y2, x3, x4, x2, y3d3, y3d4, y3d2, ground.color3, ground.color4, ground.color2);
			}
		}
		if((x1 - x2) * (y4 - y2) - (y1 - y2) * (x4 - x2) > 0) {
			Rasterizer3D.clippedScan = x1 < 0 || x2 < 0 || x4 < 0 || x1 > Rasterizer3D.viewport.width || x2 > Rasterizer3D.viewport.width || x4 > Rasterizer3D.viewport.width;
			if(hasClicked && triangleContains(clickX, clickY, y1, y2, y4, x1, x2, x4)) {
				hoverX = x;
				hoverY = y;
			}
			if(!Config.def.groundMat || ground.texture == -1) {
				if(ground.color1 != 12345678) {
					Rasterizer3D.drawGouraudTriangle(y1, y2, y4, x1, x2, x4, y3d1, y3d2, y3d4, ground.color1, ground.color2, ground.color4);
				}
			} else {
				if(ground.color1 != 12345678) {
					Rasterizer3D.drawTexturedTriangle(y1, y2, y4, x1, x2, x4, y3d1, y3d2, y3d4, ground.color1, ground.color2, ground.color4, x3d1, x3d3, x3d2, z1, z2, z4, y3d1, y3d2, y3d4, ground.texture, Config.def.groundMat, true);
				}
			}
		}
	}
	
	public EntityUnit getEntityUnit(int plane, int x, int y) {
		final Tile tile = tileMap[plane][x][y];
		if(tile == null) {
			return null;
		}
		for(int l = 0; l < tile.entityUnitAmount; l++) {
			final EntityUnit sceneSpawnRequest = tile.entityUnit[l];
			if((sceneSpawnRequest.hash >> 38 & 3) == 2 && sceneSpawnRequest.tileX == x && sceneSpawnRequest.tileY == y) {
				return sceneSpawnRequest;
			}
		}
		return null;
	}
	
	public long getEntityUnitHash(int plane, int x, int y) {
		final Tile tile = tileMap[plane][x][y];
		if(tile == null) {
			return 0;
		}
		for(int l = 0; l < tile.entityUnitAmount; l++) {
			final EntityUnit sceneSpawnRequest = tile.entityUnit[l];
			if((sceneSpawnRequest.hash >> 38 & 3) == 2 && sceneSpawnRequest.tileX == x && sceneSpawnRequest.tileY == y) {
				return sceneSpawnRequest.hash;
			}
		}
		return 0;
	}
	
	public Wall getWall(int plane, int x, int y) {
		final Tile tile = tileMap[plane][x][y];
		if(tile == null) {
			return null;
		} else {
			return tile.wall;
		}
	}
	
	public long getWallHash(int plane, int x, int y) {
		final Tile tile = tileMap[plane][x][y];
		if(tile == null || tile.wall == null) {
			return 0;
		} else {
			return tile.wall.hash;
		}
	}
	
	public WallDecoration getWallDecor(int plane, int x, int y) {
		final Tile tile = tileMap[plane][x][y];
		if(tile == null) {
			return null;
		} else {
			return tile.wallDecor;
		}
	}
	
	public long getWallDecorHash(int plane, int x, int y) {
		final Tile tile = tileMap[plane][x][y];
		if(tile == null || tile.wallDecor == null) {
			return 0;
		} else {
			return tile.wallDecor.hash;
		}
	}
	
	public int getWallsForMinimap(int plane, int x, int y, long hash) {
		final Tile tile = tileMap[plane][x][y];
		if(tile == null) {
			return -1;
		}
		if(tile.wall != null && tile.wall.hash == hash) {
			return tile.wall.config & 0xff;
		}
		if(tile.wallDecor != null && tile.wallDecor.hash == hash) {
			return tile.wallDecor.config & 0xff;
		}
		if(tile.groundDecor != null && tile.groundDecor.hash == hash) {
			return tile.groundDecor.config & 0xff;
		}
		for(int i1 = 0; i1 < tile.entityUnitAmount; i1++) {
			if(tile.entityUnit[i1].hash == hash) {
				return tile.entityUnit[i1].config & 0xff;
			}
		}
		return -1;
	}
	
	public void clear() {
		for(int plane = 0; plane < planesDisplayed; plane++) {
			for(int x = 0; x < tilesDisplayedX; x++) {
				for(int y = 0; y < tilesDisplayedY; y++) {
					tileMap[plane][x][y] = null;
				}
			}
		}
		for(int plane = 0; plane < planeCount; plane++) {
			for(int j1 = 0; j1 < perspectiveCount[plane]; j1++) {
				perspectives[plane][j1] = null;
			}
			perspectiveCount[plane] = 0;
		}
		for(int k1 = 0; k1 < entity1Count; k1++) {
			entities1[k1] = null;
		}
		entity1Count = 0;
		for(int l1 = 0; l1 < entities2.length; l1++) {
			entities2[l1] = null;
		}
	}
	
	void method276(int x, int y) {
		final Tile tile = tileMap[0][x][y];
		for(int plane = 0; plane < 3; plane++) {
			final Tile tile_1 = tileMap[plane][x][y] = tileMap[plane + 1][x][y];
			if(tile_1 != null) {
				tile_1.plane--;
				for(int j1 = 0; j1 < tile_1.entityUnitAmount; j1++) {
					final EntityUnit sceneSpawnRequest = tile_1.entityUnit[j1];
					if((sceneSpawnRequest.hash >> 38 & 3) == 2 && sceneSpawnRequest.tileX == x && sceneSpawnRequest.tileY == y) {
						sceneSpawnRequest.plane--;
					}
				}
			}
		}
		if(tileMap[0][x][y] == null) {
			tileMap[0][x][y] = new Tile(0, x, y);
		}
		tileMap[0][x][y].tile = tile;
		tileMap[3][x][y] = null;
	}
	
	void method278(int i, int j, int k, int l) {
		final Tile class30_sub3 = tileMap[i][j][k];
		if(class30_sub3 != null) {
			tileMap[i][j][k].anInt1321 = l;
		}
	}
	
	void setWallDecor(long hash, int y, int k, int plane, int xoff, int height, Entity entity, int x, byte config, int yoff, int face) {
		if(entity == null) {
			return;
		}
		WallDecoration wallDecor = new WallDecoration();
		wallDecor.hash = hash;
		wallDecor.config = config;
		wallDecor.x = x * 128 + 64 + xoff;
		wallDecor.z = y * 128 + 64 + yoff;
		wallDecor.y = height;
		wallDecor.model = entity;
		wallDecor.face = face;
		wallDecor.anInt503 = k;
		for(int i = plane; i >= 0; i--) {
			if(tileMap[i][x][y] == null) {
				tileMap[i][x][y] = new Tile(i, x, y);
			}
		}
		tileMap[plane][x][y].wallDecor = wallDecor;
	}
	
	boolean method284(long hash, byte byte0, int j, int sizeY, Entity entity, int sizeX, int plane, int j1, int y, int x) {
		if(entity == null) {
			return true;
		} else {
			final int i2 = x * 128 + 64 * sizeX;
			final int j2 = y * 128 + 64 * sizeY;
			return addEntityUnit(plane, x, y, sizeX, sizeY, i2, j2, j, entity, j1, false, hash, byte0);
		}
	}
	
	public void removeEntityUnit1s() {
		for(int i = 0; i < entity1Count; i++) {
			final EntityUnit entity = entities1[i];
			removeEntityUnit(entity);
			entities1[i] = null;
		}
		entity1Count = 0;
	}
	
	public boolean addEntity(int z, int j, int k, long hash, int y, int j1, int x, Entity entity, boolean flag) {
		if(entity == null) {
			return true;
		}
		int l1 = x - j1;
		int i2 = y - j1;
		int j2 = x + j1;
		int k2 = y + j1;
		if(flag) {
			if(j > 640 && j < 1408) {
				k2 += 128;
			}
			if(j > 1152 && j < 1920) {
				j2 += 128;
			}
			if(j > 1664 || j < 384) {
				i2 -= 128;
			}
			if(j > 128 && j < 896) {
				l1 -= 128;
			}
		}
		l1 /= 128;
		i2 /= 128;
		j2 /= 128;
		k2 /= 128;
		return addEntityUnit(z, l1, i2, j2 - l1 + 1, k2 - i2 + 1, x, y, k, entity, j, true, hash, (byte) 0);
	}
	
	public boolean addPlayer(int j, int y, Entity entity, int yaw, int i1, int x, int k1, int l1, int i2, long hash, int k2) {
		return entity == null || addEntityUnit(j, l1, k2, i2 - l1 + 1, i1 - k2 + 1, x, y, k1, entity, yaw, true, hash, (byte) 0);
	}
	
	void method290(int y, int k, int x, int plane) {
		final Tile tile = tileMap[plane][x][y];
		if(tile == null) {
			return;
		}
		final WallDecoration walldec = tile.wallDecor;
		if(walldec != null) {
			final int x1 = x * 128 + 64;
			final int y1 = y * 128 + 64;
			walldec.x = x1 + (walldec.x - x1) * k / 16;
			walldec.z = y1 + (walldec.z - y1) * k / 16;
		}
	}
	
	protected void shadeModels(int lighty, int lightx, int lightz) {
		final int lightness = 104;
		final int roundness = 1480;//1280;
		final int distance = (int) Math.sqrt(lightx * lightx + lighty * lighty + lightz * lightz);
		final int contrast = roundness * distance >> 8;
		for(int plane = 0; plane < planesDisplayed; plane++) {
			for(int x = 0; x < tilesDisplayedX; x++) {
				for(int y = 0; y < tilesDisplayedY; y++) {
					final Tile tile = tileMap[plane][x][y];
					if(tile != null) {
						final Wall wall = tile.wall;
						if(wall != null && wall.model1 != null && wall.model1.vectorX != null) {
							method307((Model) wall.model1, plane, x, y, 1, 1);
							if(wall.model2 != null && wall.model2.vectorX != null) {
								method307((Model) wall.model2, plane, x, y, 1, 1);
								method308((Model) wall.model1, (Model) wall.model2, 0, 0, 0, false);
								((Model) wall.model2).doShading(lightness, contrast, lightx, lighty, lightz);
							}
							((Model) wall.model1).doShading(lightness, contrast, lightx, lighty, lightz);
						}
						for(int k2 = 0; k2 < tile.entityUnitAmount; k2++) {
							final EntityUnit npcspw = tile.entityUnit[k2];
							if(npcspw != null && npcspw.model != null && npcspw.model.vectorX != null) {
								method307((Model) npcspw.model, plane, x, y, npcspw.sizeX - npcspw.tileX + 1, npcspw.sizeY - npcspw.tileY + 1);
								((Model) npcspw.model).doShading(lightness, contrast, lightx, lighty, lightz);
							}
						}
						final GroundDecoration grounddec = tile.groundDecor;
						if(grounddec != null && grounddec.model.vectorX != null) {
							method306((Model) grounddec.model, plane, x, y);
							((Model) grounddec.model).doShading(lightness, contrast, lightx, lighty, lightz);
						}
					}
				}
			}
		}
	}
	
	private void method306(Model model, int plane, int x, int y) {
		if(x < tilesDisplayedX) {
			final Tile tile = tileMap[plane][x + 1][y];
			if(tile != null && tile.groundDecor != null && tile.groundDecor.model.vectorX != null) {
				method308(model, (Model) tile.groundDecor.model, 128, 0, 0, true);
			}
		}
		if(y < tilesDisplayedX) {
			final Tile tile_1 = tileMap[plane][x][y + 1];
			if(tile_1 != null && tile_1.groundDecor != null && tile_1.groundDecor.model.vectorX != null) {
				method308(model, (Model) tile_1.groundDecor.model, 0, 128, 0, true);
			}
		}
		if(x < tilesDisplayedX && y < tilesDisplayedY) {
			final Tile tile_2 = tileMap[plane][x + 1][y + 1];
			if(tile_2 != null && tile_2.groundDecor != null && tile_2.groundDecor.model.vectorX != null) {
				method308(model, (Model) tile_2.groundDecor.model, 128, 128, 0, true);
			}
		}
		if(x < tilesDisplayedX && y > 0) {
			final Tile tile_3 = tileMap[plane][x + 1][y - 1];
			if(tile_3 != null && tile_3.groundDecor != null && tile_3.groundDecor.model.vectorX != null) {
				method308(model, (Model) tile_3.groundDecor.model, 128, -128, 0, true);
			}
		}
	}
	
	//fade models together
	private void method307(Model model, int plane, int x, int y, int xSize, int ySize) {
		boolean flag = true;
		int x1 = x;
		final int x2 = x + xSize;
		final int y1 = y - 1;
		final int y2 = y + ySize;
		for(int tilePlane = plane; tilePlane <= plane + 1; tilePlane++) {
			if(tilePlane != planesDisplayed) {
				for(int dx = x1; dx <= x2; dx++) {
					if(dx >= 0 && dx < tilesDisplayedX) {
						for(int dy = y1; dy <= y2; dy++) {
							if(dy >= 0 && dy < tilesDisplayedY && (!flag || dx >= x2 || dy >= y2 || dy < y && dx != x)) {
								final Tile tile = tileMap[tilePlane][dx][dy];
								if(tile != null) {
									final int dz = (heightMap3d[tilePlane][dx][dy] + heightMap3d[tilePlane][dx + 1][dy] + heightMap3d[tilePlane][dx][dy + 1] + heightMap3d[tilePlane][dx + 1][dy + 1]) / 4 - (heightMap3d[plane][x][y] + heightMap3d[plane][x + 1][y] + heightMap3d[plane][x][y + 1] + heightMap3d[plane][x + 1][y + 1]) / 4;
									final Wall wall = tile.wall;
									if(wall != null && wall.model1 != null && wall.model1.vectorX != null) {
										method308(model, (Model) wall.model1, (dx - x) * 128 + (1 - xSize) * 64, (dy - y) * 128 + (1 - ySize) * 64, dz, flag);
									}
									if(wall != null && wall.model2 != null && wall.model2.vectorX != null) {
										method308(model, (Model) wall.model2, (dx - x) * 128 + (1 - xSize) * 64, (dy - y) * 128 + (1 - ySize) * 64, dz, flag);
									}
									for(int j3 = 0; j3 < tile.entityUnitAmount; j3++) {
										final EntityUnit spawn = tile.entityUnit[j3];
										if(spawn != null && spawn.model != null && spawn.model.vectorX != null) {
											final int endX = spawn.sizeX - spawn.tileX + 1;
											final int endY = spawn.sizeY - spawn.tileY + 1;
											method308(model, (Model) spawn.model, (spawn.tileX - x) * 128 + (endX - xSize) * 64, (spawn.tileY - y) * 128 + (endY - ySize) * 64, dz, flag);
										}
									}
								}
							}
						}
					}
				}
				x1--;
				flag = false;
			}
		}
	}
	
	// TODO causes problems if model and model_1 have different scaling?
	private void method308(Model model, Model model_1, int x, int y, int unknown, boolean flag) {
		anInt488++;
		int l = 0;
		final int ai[] = model_1.vertexX;
		final int i1 = model_1.vertexAmt;
		for(int j1 = 0; j1 < model.vertexAmt; j1++) {
			if(model.vectorNormalMagnitude[j1] != 0) {
				final int i2 = (model.vertexY[j1] >> (model.upscaled ? 2 : 0)) - unknown;
				if(i2 <= model_1.maxVerticalDistDown) {
					final int j2 = (model.vertexX[j1] >> (model.upscaled ? 2 : 0)) - x;
					if(j2 >= model_1.minVertexX && j2 <= model_1.maxVertexX) {
						final int k2 = (model.vertexZ[j1] >> (model.upscaled ? 2 : 0)) - y;
						if(k2 >= model_1.minVertexZ && k2 <= model_1.maxVertexZ) {
							for(int l2 = 0; l2 < i1; l2++) {
								if(j2 == (ai[l2] >> (model_1.upscaled ? 2 : 0)) && k2 == (model_1.vertexZ[l2] >> (model_1.upscaled ? 2 : 0)) && i2 == (model_1.vertexY[l2] >> (model_1.upscaled ? 2 : 0)) && model_1.vectorNormalMagnitude[l2] != 0) {
									model.vectorX[j1] += model_1.vectorNormalX[l2];
									model.vectorY[j1] += model_1.vectorNormalY[l2];
									model.vectorZ[j1] += model_1.vectorNormalZ[l2];
									model.vectorMagnitude[j1] += model_1.vectorNormalMagnitude[l2];
									model_1.vectorX[l2] += model.vectorNormalX[j1];
									model_1.vectorY[l2] += model.vectorNormalY[j1];
									model_1.vectorZ[l2] += model.vectorNormalZ[j1];
									model_1.vectorMagnitude[l2] += model.vectorNormalMagnitude[j1];
									l++;
									anIntArray486[j1] = anInt488;
									anIntArray487[l2] = anInt488;
								}
							}
						}
					}
				}
			}
		}
		if(l < 3 || !flag) {
			return;
		}
		for(int k1 = 0; k1 < model.triAmt; k1++) {
			if(anIntArray486[model.vertexIndex3d1[k1]] == anInt488 && anIntArray486[model.vertexIndex3d2[k1]] == anInt488 && anIntArray486[model.vertexIndex3d3[k1]] == anInt488) {
				model.triType[k1] = -1;
			}
		}
		for(int l1 = 0; l1 < model_1.triAmt; l1++) {
			if(anIntArray487[model_1.vertexIndex3d1[l1]] == anInt488 && anIntArray487[model_1.vertexIndex3d2[l1]] == anInt488 && anIntArray487[model_1.vertexIndex3d3[l1]] == anInt488) {
				model_1.triType[l1] = -1;
			}
		}
		
	}
	
	public void click(int x, int y) {
		hasClicked = true;
		clickX = x;
		clickY = y;
		hoverX = -1;
		hoverY = -1;
	}
	
	private int adjustLightness(int hsl, int adj) {
		adj = 127 - adj;
		adj = adj * (hsl & 0x7f) / 160;
		if(adj < 2) {
			adj = 2;
		} else if(adj > 126) {
			adj = 126;
		}
		return (hsl & 0xff80) + adj;
	}
	
	private boolean triangleContains(int pointx, int pointy, int y1, int y2, int y3, int x1, int x2, int x3) {
		if(pointy < y1 && pointy < y2 && pointy < y3) {
			return false;
		}
		if(pointy > y1 && pointy > y2 && pointy > y3) {
			return false;
		}
		if(pointx < x1 && pointx < x2 && pointx < x3) {
			return false;
		}
		if(pointx > x1 && pointx > x2 && pointx > x3) {
			return false;
		}
		final int i2 = (pointy - y1) * (x2 - x1) - (pointx - x1) * (y2 - y1);
		final int j2 = (pointy - y3) * (x1 - x3) - (pointx - x3) * (y1 - y3);
		final int k2 = (pointy - y2) * (x3 - x2) - (pointx - x2) * (y3 - y2);
		return i2 * k2 > 0 && k2 * j2 > 0;
	}
	
	private void method319() {
		final int j = perspectiveCount[cameraPlane];
		final Perspective[] aclass47 = perspectives[cameraPlane];
		anInt475 = 0;
		for(int k = 0; k < j; k++) {
			final Perspective perspective = aclass47[k];
			if(perspective.direction == 1) {
				final int l = perspective.tileX1 - cameraTileX + DRAW_DISTANCE_TILES;
				if(l < 0 || l > (DRAW_DISTANCE_TILES << 1)) {
					continue;
				}
				int k1 = perspective.tileY1 - cameraTileY + DRAW_DISTANCE_TILES;
				if(k1 < 0) {
					k1 = 0;
				}
				int j2 = perspective.tileY2 - cameraTileY + DRAW_DISTANCE_TILES;
				if(j2 > (DRAW_DISTANCE_TILES << 1)) {
					j2 = (DRAW_DISTANCE_TILES << 1);
				}
				boolean flag = false;
				while(k1 <= j2) {
					if(aBooleanArrayArray492[l][k1++]) {
						flag = true;
						break;
					}
				}
				if(!flag) {
					continue;
				}
				int dx = cameraPixelX - perspective.pixelX1;
				if(dx > 32) {
					perspective.renderZ = 1;
				} else {
					if(dx >= -32) {
						continue;
					}
					perspective.renderZ = 2;
					dx = -dx;
				}
				perspective.renderX1 = (perspective.pixelY1 - cameraPixelZ << 8) / dx;
				perspective.renderX2 = (perspective.pixelY2 - cameraPixelZ << 8) / dx;
				perspective.renderY1 = (perspective.pixelZ1 - cameraPixelY << 8) / dx;
				perspective.renderY2 = (perspective.pixelZ2 - cameraPixelY << 8) / dx;
				aClass47Array476[anInt475++] = perspective;
				continue;
			}
			if(perspective.direction == 2) {
				final int i1 = perspective.tileY1 - cameraTileY + DRAW_DISTANCE_TILES;
				if(i1 < 0 || i1 > (DRAW_DISTANCE_TILES << 1)) {
					continue;
				}
				int l1 = perspective.tileX1 - cameraTileX + DRAW_DISTANCE_TILES;
				if(l1 < 0) {
					l1 = 0;
				}
				int k2 = perspective.tileX2 - cameraTileX + DRAW_DISTANCE_TILES;
				if(k2 > (DRAW_DISTANCE_TILES << 1)) {
					k2 = (DRAW_DISTANCE_TILES << 1);
				}
				boolean flag1 = false;
				while(l1 <= k2) {
					if(aBooleanArrayArray492[l1++][i1]) {
						flag1 = true;
						break;
					}
				}
				if(!flag1) {
					continue;
				}
				int k3 = cameraPixelZ - perspective.pixelY1;
				if(k3 > 32) {
					perspective.renderZ = 3;
				} else {
					if(k3 >= -32) {
						continue;
					}
					perspective.renderZ = 4;
					k3 = -k3;
				}
				perspective.anInt799 = (perspective.pixelX1 - cameraPixelX << 8) / k3;
				perspective.anInt800 = (perspective.pixelX2 - cameraPixelX << 8) / k3;
				perspective.renderY1 = (perspective.pixelZ1 - cameraPixelY << 8) / k3;
				perspective.renderY2 = (perspective.pixelZ2 - cameraPixelY << 8) / k3;
				aClass47Array476[anInt475++] = perspective;
			} else if(perspective.direction == 4) {
				final int j1 = perspective.pixelZ1 - cameraPixelY;
				if(j1 > 128) {
					int i2 = perspective.tileY1 - cameraTileY + DRAW_DISTANCE_TILES;
					if(i2 < 0) {
						i2 = 0;
					}
					int l2 = perspective.tileY2 - cameraTileY + DRAW_DISTANCE_TILES;
					if(l2 > (DRAW_DISTANCE_TILES << 1)) {
						l2 = (DRAW_DISTANCE_TILES << 1);
					}
					if(i2 <= l2) {
						int i3 = perspective.tileX1 - cameraTileX + DRAW_DISTANCE_TILES;
						if(i3 < 0) {
							i3 = 0;
						}
						int l3 = perspective.tileX2 - cameraTileX + DRAW_DISTANCE_TILES;
						if(l3 > (DRAW_DISTANCE_TILES << 1)) {
							l3 = (DRAW_DISTANCE_TILES << 1);
						}
						boolean flag2 = false;
						label0:
						for(int i4 = i3; i4 <= l3; i4++) {
							for(int j4 = i2; j4 <= l2; j4++) {
								if(!aBooleanArrayArray492[i4][j4]) {
									continue;
								}
								flag2 = true;
								break label0;
							}
						}
						
						if(flag2) {
							perspective.renderZ = 5;
							perspective.anInt799 = (perspective.pixelX1 - cameraPixelX << 8) / j1;
							perspective.anInt800 = (perspective.pixelX2 - cameraPixelX << 8) / j1;
							perspective.renderX1 = (perspective.pixelY1 - cameraPixelZ << 8) / j1;
							perspective.renderX2 = (perspective.pixelY2 - cameraPixelZ << 8) / j1;
							aClass47Array476[anInt475++] = perspective;
						}
					}
				}
			}
		}
	}
	
	private boolean groundHidden(int plane, int x, int y) {
		final int l = heightMap2d[plane][x][y];
		if(l == -drawCycle) {
			return false;
		}
		if(l == drawCycle) {
			return true;
		}
		final int pxVrtxX = x << 7;
		final int pxVrtxY = y << 7;
		if(method324(pxVrtxX + 1, pxVrtxY + 1, heightMap3d[plane][x][y]) && method324(pxVrtxX + 128 - 1, pxVrtxY + 1, heightMap3d[plane][x + 1][y]) && method324(pxVrtxX + 128 - 1, pxVrtxY + 128 - 1, heightMap3d[plane][x + 1][y + 1]) && method324(pxVrtxX + 1, pxVrtxY + 128 - 1, heightMap3d[plane][x][y + 1])) {
			heightMap2d[plane][x][y] = drawCycle;
			return true;
		} else {
			heightMap2d[plane][x][y] = -drawCycle;
			return false;
		}
	}
	
	private boolean method321(int i, int j, int k, int type) {
		if(!groundHidden(i, j, k)) {
			return false;
		}
		final int i1 = j << 7;
		final int j1 = k << 7;
		final int k1 = heightMap3d[i][j][k] - 1;
		final int l1 = k1 - 120;
		final int i2 = k1 - 230;
		final int j2 = k1 - 238;
		if(type < 16) {
			if(type == 1) {
				if(i1 > cameraPixelX) {
					if(!method324(i1, j1, k1)) {
						return false;
					}
					if(!method324(i1, j1 + 128, k1)) {
						return false;
					}
				}
				if(i > 0) {
					if(!method324(i1, j1, l1)) {
						return false;
					}
					if(!method324(i1, j1 + 128, l1)) {
						return false;
					}
				}
				return method324(i1, j1, i2) && method324(i1, j1 + 128, i2);
			}
			if(type == 2) {
				if(j1 < cameraPixelZ) {
					if(!method324(i1, j1 + 128, k1)) {
						return false;
					}
					if(!method324(i1 + 128, j1 + 128, k1)) {
						return false;
					}
				}
				if(i > 0) {
					if(!method324(i1, j1 + 128, l1)) {
						return false;
					}
					if(!method324(i1 + 128, j1 + 128, l1)) {
						return false;
					}
				}
				return method324(i1, j1 + 128, i2) && method324(i1 + 128, j1 + 128, i2);
			}
			if(type == 4) {
				if(i1 < cameraPixelX) {
					if(!method324(i1 + 128, j1, k1)) {
						return false;
					}
					if(!method324(i1 + 128, j1 + 128, k1)) {
						return false;
					}
				}
				if(i > 0) {
					if(!method324(i1 + 128, j1, l1)) {
						return false;
					}
					if(!method324(i1 + 128, j1 + 128, l1)) {
						return false;
					}
				}
				return method324(i1 + 128, j1, i2) && method324(i1 + 128, j1 + 128, i2);
			}
			if(type == 8) {
				if(j1 > cameraPixelZ) {
					if(!method324(i1, j1, k1)) {
						return false;
					}
					if(!method324(i1 + 128, j1, k1)) {
						return false;
					}
				}
				if(i > 0) {
					if(!method324(i1, j1, l1)) {
						return false;
					}
					if(!method324(i1 + 128, j1, l1)) {
						return false;
					}
				}
				return method324(i1, j1, i2) && method324(i1 + 128, j1, i2);
			}
		}
		if(!method324(i1 + 64, j1 + 64, j2)) {
			return false;
		}
		if(type == 16) {
			return method324(i1, j1 + 128, i2);
		}
		if(type == 32) {
			return method324(i1 + 128, j1 + 128, i2);
		}
		if(type == 64) {
			return method324(i1 + 128, j1, i2);
		}
		if(type == 128) {
			return method324(i1, j1, i2);
		} else {
			System.out.println("Warning unsupported wall type");
			return true;
		}
	}
	
	private boolean method322(int plane, int x, int y, int z) {
		if(!groundHidden(plane, x, y)) {
			return false;
		}
		final int pxX = x << 7;
		final int pxY = y << 7;
		return method324(pxX + 1, pxY + 1, heightMap3d[plane][x][y] - z) && method324(pxX + 128 - 1, pxY + 1, heightMap3d[plane][x + 1][y] - z) && method324(pxX + 128 - 1, pxY + 128 - 1, heightMap3d[plane][x + 1][y + 1] - z) && method324(pxX + 1, pxY + 128 - 1, heightMap3d[plane][x][y + 1] - z);
	}
	
	private boolean method323(int plane, int startX, int endX, int startY, int endY, int j1) {
		if(startX == endX && startY == endY) {
			if(!groundHidden(plane, startX, startY)) {
				return false;
			}
			final int k1 = startX << 7;
			final int i2 = startY << 7;
			return method324(k1 + 1, i2 + 1, heightMap3d[plane][startX][startY] - j1) && method324(k1 + 128 - 1, i2 + 1, heightMap3d[plane][startX + 1][startY] - j1) && method324(k1 + 128 - 1, i2 + 128 - 1, heightMap3d[plane][startX + 1][startY + 1] - j1) && method324(k1 + 1, i2 + 128 - 1, heightMap3d[plane][startX][startY + 1] - j1);
		}
		for(int l1 = startX; l1 <= endX; l1++) {
			for(int j2 = startY; j2 <= endY; j2++) {
				if(heightMap2d[plane][l1][j2] == -drawCycle) {
					return false;
				}
			}
		}
		final int k2 = (startX << 7) + 1;
		final int l2 = (startY << 7) + 2;
		final int i3 = heightMap3d[plane][startX][startY] - j1;
		if(!method324(k2, l2, i3)) {
			return false;
		}
		final int j3 = (endX << 7) - 1;
		if(!method324(j3, l2, i3)) {
			return false;
		}
		final int k3 = (endY << 7) - 1;
		return method324(k2, k3, i3) && method324(j3, k3, i3);
	}
	
	private boolean method324(int x, int y, int z) {
		for(int l = 0; l < anInt475; l++) {
			final Perspective class47 = aClass47Array476[l];
			if(class47.renderZ == 1) {
				final int i1 = class47.pixelX1 - x;
				if(i1 > 0) {
					final int j2 = class47.pixelY1 + (class47.renderX1 * i1 >> 8);
					final int k3 = class47.pixelY2 + (class47.renderX2 * i1 >> 8);
					final int l4 = class47.pixelZ1 + (class47.renderY1 * i1 >> 8);
					final int i6 = class47.pixelZ2 + (class47.renderY2 * i1 >> 8);
					if(y >= j2 && y <= k3 && z >= l4 && z <= i6) {
						return true;
					}
				}
			} else if(class47.renderZ == 2) {
				final int j1 = x - class47.pixelX1;
				if(j1 > 0) {
					final int k2 = class47.pixelY1 + (class47.renderX1 * j1 >> 8);
					final int l3 = class47.pixelY2 + (class47.renderX2 * j1 >> 8);
					final int i5 = class47.pixelZ1 + (class47.renderY1 * j1 >> 8);
					final int j6 = class47.pixelZ2 + (class47.renderY2 * j1 >> 8);
					if(y >= k2 && y <= l3 && z >= i5 && z <= j6) {
						return true;
					}
				}
			} else if(class47.renderZ == 3) {
				final int k1 = class47.pixelY1 - y;
				if(k1 > 0) {
					final int l2 = class47.pixelX1 + (class47.anInt799 * k1 >> 8);
					final int i4 = class47.pixelX2 + (class47.anInt800 * k1 >> 8);
					final int j5 = class47.pixelZ1 + (class47.renderY1 * k1 >> 8);
					final int k6 = class47.pixelZ2 + (class47.renderY2 * k1 >> 8);
					if(x >= l2 && x <= i4 && z >= j5 && z <= k6) {
						return true;
					}
				}
			} else if(class47.renderZ == 4) {
				final int l1 = y - class47.pixelY1;
				if(l1 > 0) {
					final int i3 = class47.pixelX1 + (class47.anInt799 * l1 >> 8);
					final int j4 = class47.pixelX2 + (class47.anInt800 * l1 >> 8);
					final int k5 = class47.pixelZ1 + (class47.renderY1 * l1 >> 8);
					final int l6 = class47.pixelZ2 + (class47.renderY2 * l1 >> 8);
					if(x >= i3 && x <= j4 && z >= k5 && z <= l6) {
						return true;
					}
				}
			} else if(class47.renderZ == 5) {
				final int i2 = z - class47.pixelZ1;
				if(i2 > 0) {
					final int j3 = class47.pixelX1 + (class47.anInt799 * i2 >> 8);
					final int k4 = class47.pixelX2 + (class47.anInt800 * i2 >> 8);
					final int l5 = class47.pixelY1 + (class47.renderX1 * i2 >> 8);
					final int i7 = class47.pixelY2 + (class47.renderX2 * i2 >> 8);
					if(x >= j3 && x <= k4 && y >= l5 && y <= i7) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public void removeEntityUnit(EntityUnit entity) {
		for(int x = entity.tileX; x <= entity.sizeX; x++) {
			for(int y = entity.tileY; y <= entity.sizeY; y++) {
				final Tile tile = tileMap[entity.plane][x][y];
				if(tile != null) {
					for(int i = 0; i < tile.entityUnitAmount; i++) {
						if(tile.entityUnit[i] != entity) {
							continue;
						}
						tile.entityUnitAmount--;
						for(int i1 = i; i1 < tile.entityUnitAmount; i1++) {
							tile.entityUnit[i1] = tile.entityUnit[i1 + 1];
							tile.entityUnitSize[i1] = tile.entityUnitSize[i1 + 1];
						}
						tile.entityUnit[tile.entityUnitAmount] = null;
						break;
					}
					tile.anInt1320 = 0;
					for(int j1 = 0; j1 < tile.entityUnitAmount; j1++) {
						tile.anInt1320 |= tile.entityUnitSize[j1];
					}
				}
			}
		}
	}
	
	public void removeObjectUnit(int plane, int x, int y) {
		final Tile tile = tileMap[plane][x][y];
		if(tile != null) {
			tile.objectUnit = null;
		}
	}
	
	public void removeGroundDecor(int plane, int x, int y) {
		final Tile tile = tileMap[plane][x][y];
		if(tile != null) {
			tile.groundDecor = null;
		}
	}
	
	public void removeEntityUnits(int plane, int x, int y) {
		final Tile tile = tileMap[plane][x][y];
		if(tile == null) {
			return;
		}
		for(int i = 0; i < tile.entityUnitAmount; i++) {
			final EntityUnit entity = tile.entityUnit[i];
			if((entity.hash >> 38 & 3) == 2 && entity.tileX == x && entity.tileY == y) {
				removeEntityUnit(entity);
				return;
			}
		}
	}
	
	public void removeWall(int x, int plane, int y) {
		final Tile tile = tileMap[plane][x][y];
		if(tile != null) {
			tile.wall = null;
		}
	}
	
	public void removeWallDecor(int plane, int x, int y) {
		final Tile tile = tileMap[plane][x][y];
		if(tile != null) {
			tile.wallDecor = null;
		}
	}
	
	private boolean addEntityUnit(int plane, int x, int y, int sizeX, int sizeY, int baseX, int baseY, int l1, Entity entity, int yaw, boolean flag, long hash, byte config) {
		for(int xPos = x; xPos < x + sizeX; xPos++) {
			for(int yPos = y; yPos < y + sizeY; yPos++) {
				if(xPos < 0 || yPos < 0 || xPos >= tilesDisplayedX || yPos >= tilesDisplayedY) {
					return false;
				}
				final Tile tile = tileMap[plane][xPos][yPos];
				if(tile != null && tile.entityUnitAmount >= 5) {
					return false;
				}
			}
		}
		final EntityUnit spawn = new EntityUnit();
		spawn.hash = hash;
		spawn.config = config;
		spawn.plane = plane;
		spawn.x = baseX;
		spawn.z = baseY;
		spawn.y = l1;
		spawn.model = entity;
		spawn.yaw = yaw;
		spawn.tileX = x;
		spawn.tileY = y;
		spawn.sizeX = x + sizeX - 1;
		spawn.sizeY = y + sizeY - 1;
		for(int xPos = x; xPos < x + sizeX; xPos++) {
			for(int yPos = y; yPos < y + sizeY; yPos++) {
				int count = 0;
				if(xPos > x) {
					count++;
				}
				if(xPos < x + sizeX - 1) {
					count += 4;
				}
				if(yPos > y) {
					count += 8;
				}
				if(yPos < y + sizeY - 1) {
					count += 2;
				}
				for(int planePos = plane; planePos >= 0; planePos--) {
					if(tileMap[planePos][xPos][yPos] == null) {
						tileMap[planePos][xPos][yPos] = new Tile(planePos, xPos, yPos);
					}
				}
				
				final Tile tile_1 = tileMap[plane][xPos][yPos];
				tile_1.entityUnit[tile_1.entityUnitAmount] = spawn;
				tile_1.entityUnitSize[tile_1.entityUnitAmount] = count;
				tile_1.anInt1320 |= count;
				tile_1.entityUnitAmount++;
			}
		}
		if(flag) {
			entities1[entity1Count++] = spawn;
		}
		return true;
	}
	
	public void initPlane(int plane) {
		currentPlane = plane;
		for(int x = 0; x < tilesDisplayedX; x++) {
			for(int y = 0; y < tilesDisplayedY; y++) {
				if(tileMap[plane][x][y] == null) {
					tileMap[plane][x][y] = new Tile(plane, x, y);
				}
			}
		}
	}
	
	void setGroundDecor(Entity entity, int plane, int x, int y, int height, long hash, byte config) {
		if(entity == null) {
			return;
		}
		GroundDecoration groundDecor = new GroundDecoration();
		groundDecor.model = entity;
		groundDecor.x = x * 128 + 64;
		groundDecor.z = y * 128 + 64;
		groundDecor.y = height;
		groundDecor.hash = hash;
		groundDecor.config = config;
		if(tileMap[plane][x][y] == null) {
			tileMap[plane][x][y] = new Tile(plane, x, y);
		}
		tileMap[plane][x][y].groundDecor = groundDecor;
	}
	
	public GroundDecoration getGroundDecor(int plane, int x, int y) {
		Tile tile = tileMap[plane][x][y];
		if(tile == null || tile.groundDecor == null) {
			return null;
		} else {
			return tile.groundDecor;
		}
	}
	
	public long getGroundDecorHash(int plane, int x, int y) {
		Tile tile = tileMap[plane][x][y];
		if(tile == null || tile.groundDecor == null) {
			return 0;
		} else {
			return tile.groundDecor.hash;
		}
	}
	
	public void setObjectUnit(Entity model1, Entity model2, Entity model3, int plane, int x, int y, int height, long hash) {
		final ObjectUnit obj = new ObjectUnit();
		obj.model1 = model1;
		obj.x = x * 128 + 64;
		obj.z = y * 128 + 64;
		obj.y = height;
		obj.hash = hash;
		obj.model2 = model2;
		obj.model3 = model3;
		int j1 = 0;
		final Tile tile = tileMap[plane][x][y];
		if(tile != null) {
			for(int k1 = 0; k1 < tile.entityUnitAmount; k1++) {
				if(tile.entityUnit[k1].model instanceof Model) {
					final int l1 = ((Model) tile.entityUnit[k1].model).anInt1654;
					if(l1 > j1) {
						j1 = l1;
					}
				}
			}
		}
		obj.anInt52 = j1;
		if(tileMap[plane][x][y] == null) {
			tileMap[plane][x][y] = new Tile(plane, x, y);
		}
		tileMap[plane][x][y].objectUnit = obj;
	}
	
	void setGround(int plane, int x, int y, int lay, int orientation, int tex1, int tex2, int k1, int l1, int i2, int j2, int color1, int color2, int color3, int color4, int color5, int color6, int color7, int color8, int k4, int mapColor) {
		if(lay == 0) {
			final QuadGround ground = new QuadGround(color1, color2, color3, color4, tex1, k4, false);
			for(int i5 = plane; i5 >= 0; i5--) {
				if(tileMap[i5][x][y] == null) {
					tileMap[i5][x][y] = new Tile(i5, x, y);
				}
			}
			tileMap[plane][x][y].quadGround = ground;
			return;
		}
		if(lay == 1) {
			final QuadGround ground = new QuadGround(color5, color6, color7, color8, tex2, mapColor, k1 == l1 && k1 == i2 && k1 == j2);
			for(int j5 = plane; j5 >= 0; j5--) {
				if(tileMap[j5][x][y] == null) {
					tileMap[j5][x][y] = new Tile(j5, x, y);
				}
			}
			tileMap[plane][x][y].quadGround = ground;
			return;
		}
		final ShapedGround ground = new ShapedGround(y, color5, color4, i2, tex1, tex2, color7, orientation, color1, k4, color3, j2, l1, k1, lay, color8, color6, color2, x, mapColor);
		for(int k5 = plane; k5 >= 0; k5--) {
			if(tileMap[k5][x][y] == null) {
				tileMap[k5][x][y] = new Tile(k5, x, y);
			}
		}
		tileMap[plane][x][y].shapedGround = ground;
	}
	
	
	
	public void setMinimapFloorColor(int raster[], int index, int plane, int x, int y) {
		if (Config.def.enchanceMap()) {
			final Tile tile = tileMap[plane][x][y];
			if(tile == null) {
				return;
			}
			final QuadGround class43 = tile.quadGround;
			if(class43 != null) {
				if (class43.color1 != 12345678 && class43.color3 != 12345678) {
					if (class43.mapcolor == 0) {
						return;
					}
					int hs = class43.color1 & ~0x7f;
					int l1 = class43.color4 & 0x7f;
					int l2 = class43.color3 & 0x7f;
					int l3 = (class43.color1 & 0x7f) - l1;
					int l4 = (class43.color2 & 0x7f) - l2;
					l1 <<= 2;
					l2 <<= 2;
					for(int k1 = 0; k1 < 4; k1++) {
						if (!class43.solid) {
							raster[index] = Rasterizer3D.hslToRgbMap[hs | (l1 >> 2)];
							raster[index + 1] = Rasterizer3D.hslToRgbMap[hs | (l1 * 3 + l2 >> 4)];
							raster[index + 2] = Rasterizer3D.hslToRgbMap[hs | (l1 + l2 >> 3)];
							raster[index + 3] = Rasterizer3D.hslToRgbMap[hs | (l1 + l2 * 3 >> 4)];
						} else {
							int j1 = class43.mapcolor;
							int lig = 0xff - ((l1 >> 1) * (l1 >> 1) >> 8);
							raster[index] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
							lig = 0xff - ((l1 * 3 + l2 >> 3) * (l1 * 3 + l2 >> 3) >> 8);
							raster[index + 1] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
							lig = 0xff - ((l1 + l2 >> 2) * (l1 + l2 >> 2) >> 8);
							raster[index + 2] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
							lig = 0xff - ((l1 + l2 * 3 >> 3) * (l1 + l2 * 3 >> 3) >> 8);
							raster[index + 3] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
						}
						l1 += l3;
						l2 += l4;
						index += 512;
					}
					return;
				}
				int mapColor = class43.mapcolor;
				if(mapColor == 0) {
					return;
				}
				for(int k1 = 0; k1 < 4; k1++) {
					raster[index] = mapColor;
					raster[index + 1] = mapColor;
					raster[index + 2] = mapColor;
					raster[index + 3] = mapColor;
					index += 512;
				}
				return;
			}
			ShapedGround class40 = tile.shapedGround;
			if(class40 == null) {
				return;
			}
			int l1 = class40.firstHalfShape;
			int i2 = class40.secondHalfShape;
			int j2 = class40.firstMinimapColor;
			int k2 = class40.secondMinimapColor;
			int ai1[] = firstFloorHalfShapes[l1];
			int ai2[] = secondFloorHalfShapes[i2];
			int l2 = 0;
			if (class40.color62 != 12345678) {
				int hs1 = class40.color62 & ~0x7f;
				int l11 = class40.color92 & 0x7f;
				int l21 = class40.color82 & 0x7f;
				int l31 = (class40.color62 & 0x7f) - l11;
				int l41 = (class40.color72 & 0x7f) - l21;
				l11 <<= 2;
				l21 <<= 2;
				for(int k1 = 0; k1 < 4; k1++) {
					if (!class40.textured) {
						if(ai1[ai2[l2++]] != 0) {
							raster[index] = Rasterizer3D.hslToRgbMap[hs1 | (l11 >> 2)];
						}
						if(ai1[ai2[l2++]] != 0) {
							raster[index + 1] = Rasterizer3D.hslToRgbMap[hs1 | (l11 * 3 + l21 >> 4)];
						}
						if(ai1[ai2[l2++]] != 0) {
							raster[index + 2] = Rasterizer3D.hslToRgbMap[hs1 | (l11 + l21 >> 3)];
						}
						if(ai1[ai2[l2++]] != 0) {
							raster[index + 3] = Rasterizer3D.hslToRgbMap[hs1 | (l11 + l21 * 3 >> 4)];
						}
					} else {
						int j1 = k2;
						if(ai1[ai2[l2++]] != 0) {
							int lig = 0xff - ((l11 >> 1) * (l11 >> 1) >> 8);
							raster[index] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
						}
						if(ai1[ai2[l2++]] != 0) {
							int lig = 0xff - ((l11 * 3 + l21 >> 3) * (l11 * 3 + l21 >> 3) >> 8);
							raster[index + 1] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
						}
						if(ai1[ai2[l2++]] != 0) {
							int lig = 0xff - ((l11 + l21 >> 2) * (l11 + l21 >> 2) >> 8);
							raster[index + 2] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
						}
						if(ai1[ai2[l2++]] != 0) {
							int lig = 0xff - ((l11 + l21 * 3 >> 3) * (l11 + l21 * 3 >> 3) >> 8);
							raster[index + 3] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
						}
					}
					l11 += l31;
					l21 += l41;
					index += 512;
				}
				if (j2 != 0 && class40.color61 != 12345678) {
					index -= 512 << 2;
					l2 -= 16;
					hs1 = class40.color61 & ~0x7f;
					l11 = class40.color91 & 0x7f;
					l21 = class40.color81 & 0x7f;
					l31 = (class40.color61 & 0x7f) - l11;
					l41 = (class40.color71 & 0x7f) - l21;
					l11 <<= 2;
					l21 <<= 2;
					for(int k1 = 0; k1 < 4; k1++) {
						if(ai1[ai2[l2++]] == 0) {
							raster[index] = Rasterizer3D.hslToRgbMap[hs1 | (l11 >> 2)];
						}
						if(ai1[ai2[l2++]] == 0) {
							raster[index + 1] = Rasterizer3D.hslToRgbMap[hs1 | (l11 * 3 + l21 >> 4)];
						}
						if(ai1[ai2[l2++]] == 0) {
							raster[index + 2] = Rasterizer3D.hslToRgbMap[hs1 | (l11 + l21 >> 3)];
						}
						if(ai1[ai2[l2++]] == 0) {
							raster[index + 3] = Rasterizer3D.hslToRgbMap[hs1 | (l11 + l21 * 3 >> 4)];
						}
						l11 += l31;
						l21 += l41;
						index += 512;
					}
				}
				return;
			}
			if(j2 != 0) {
				for(int i3 = 0; i3 < 4; i3++) {
					raster[index] = ai1[ai2[l2++]] != 0 ? k2 : j2;
					raster[index + 1] = ai1[ai2[l2++]] != 0 ? k2 : j2;
					raster[index + 2] = ai1[ai2[l2++]] != 0 ? k2 : j2;
					raster[index + 3] = ai1[ai2[l2++]] != 0 ? k2 : j2;
					index += 512;
				}
				return;
			}
			for(int j3 = 0; j3 < 4; j3++) {
				if(ai1[ai2[l2++]] != 0) {
					raster[index] = k2;
				}
				if(ai1[ai2[l2++]] != 0) {
					raster[index + 1] = k2;
				}
				if(ai1[ai2[l2++]] != 0) {
					raster[index + 2] = k2;
				}
				if(ai1[ai2[l2++]] != 0) {
					raster[index + 3] = k2;
				}
				index += 512;
			}
		} else {
			final int offset = 512;
			final Tile tile = tileMap[plane][x][y];
			if(tile == null) {
				return;
			}
			final QuadGround floor = tile.quadGround;
			if(floor != null) {
				final int color = floor.mapcolor;
				if(color == 0) {
					return;
				}
				for(int row = 0; row < 4; row++) {
					raster[index] = color;
					raster[index + 1] = color;
					raster[index + 2] = color;
					raster[index + 3] = color;
					index += offset;
				}
				return;
			}
			final ShapedGround floorAdvanced = tile.shapedGround;
			if(floorAdvanced == null) {
				return;
			}
			final int firstHalfShape = floorAdvanced.firstHalfShape;
			final int secondHalfShape = floorAdvanced.secondHalfShape;
			final int color_2 = floorAdvanced.firstMinimapColor;
			final int color = floorAdvanced.secondMinimapColor;
			final int[] shape = firstFloorHalfShapes[firstHalfShape];
			final int[] shape_2 = secondFloorHalfShapes[secondHalfShape];
			int shapePixel = 0;
			if(color_2 != 0) {
				for(int row = 0; row < 4; row++) {
					raster[index] = shape[shape_2[shapePixel++]] != 0 ? color : color_2;
					raster[index + 1] = shape[shape_2[shapePixel++]] != 0 ? color : color_2;
					raster[index + 2] = shape[shape_2[shapePixel++]] != 0 ? color : color_2;
					raster[index + 3] = shape[shape_2[shapePixel++]] != 0 ? color : color_2;
					index += offset;
				}
				
				return;
			}
			for(int j3 = 0; j3 < 4; j3++) {
				if(shape[shape_2[shapePixel++]] != 0) {
					raster[index] = color;
				}
				if(shape[shape_2[shapePixel++]] != 0) {
					raster[index + 1] = color;
				}
				if(shape[shape_2[shapePixel++]] != 0) {
					raster[index + 2] = color;
				}
				if(shape[shape_2[shapePixel++]] != 0) {
					raster[index + 3] = color;
				}
				index += offset;
			}
		}
	}
	
	void setWallDecor(Entity model1, Entity model2, int plane, int x, int y, int height, int face1, int face2, long hash, byte config) {
		if(model1 == null && model2 == null) {
			return;
		}
		Wall wall = new Wall();
		wall.hash = hash;
		wall.config = config;
		wall.x = x * 128 + 64;
		wall.z = y * 128 + 64;
		wall.y = height;
		wall.model1 = model1;
		wall.model2 = model2;
		wall.face1 = face1;
		wall.face2 = face2;
		for(int l1 = plane; l1 >= 0; l1--) {
			if(tileMap[l1][x][y] == null) {
				tileMap[l1][x][y] = new Tile(l1, x, y);
			}
		}
		tileMap[plane][x][y].wall = wall;
	}
	
}