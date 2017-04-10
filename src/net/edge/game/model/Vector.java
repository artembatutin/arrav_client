package net.edge.game.model;

public final class Vector {


	public int x;
	public int y;
	public int z;
	public int magnitude;

	Vector(int x, int y, int z, int magnitude) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.magnitude = magnitude;
	}
	
	Vector() {
	
	}
}
