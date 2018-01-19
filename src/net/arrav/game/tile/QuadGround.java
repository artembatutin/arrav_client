package net.arrav.game.tile;

public final class QuadGround {

	public final int color1;
	public final int color2;
	public final int color3;
	public final int color4;
	public final int texture;
	public final int mapcolor;
	public boolean solid;

	public QuadGround(int color1, int color2, int color3, int color4, int texture, int mapcolor, boolean solid) {
		this.color1 = color1;
		this.color2 = color2;
		this.color3 = color3;
		this.color4 = color4;
		this.texture = texture;
		this.mapcolor = mapcolor;
		this.solid = solid;
	}
}
