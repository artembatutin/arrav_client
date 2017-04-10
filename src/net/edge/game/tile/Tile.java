package net.edge.game.tile;

import net.edge.util.collect.SinglyLinkableEntry;

public final class Tile extends SinglyLinkableEntry {

	public int plane;
	public final int x;
	public final int y;
	public final int z;
	public QuadGround quadGround;
	public ShapedGround shapedGround;
	public Wall wall;
	public WallDecoration wallDecor;
	public GroundDecoration groundDecor;
	public ObjectUnit objectUnit;
	public int entityUnitAmount;
	public final EntityUnit[] entityUnit;
	public final int[] entityUnitSize;
	public int anInt1320;
	public int anInt1321;
	public boolean needToDraw;
	public boolean aBoolean1323;
	public boolean aBoolean1324;
	public int anInt1325;
	public int anInt1326;
	public int anInt1327;
	public int anInt1328;
	public Tile tile;

	public Tile(int plane, int x, int y) {
		entityUnit = new EntityUnit[5];
		entityUnitSize = new int[5];
		z = this.plane = plane;
		this.x = x;
		this.y = y;
	}
}
