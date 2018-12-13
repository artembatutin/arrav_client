package net.arrav.world.emitter;

public class ParticleVector {

	public static final ParticleVector ZERO = new ParticleVector(0, 0, 0);
	
	private int x;
	private int y;
	private int z;

	public ParticleVector(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	public final int getZ() {
		return z;
	}

	public final ParticleVector subtract(ParticleVector other) {
		return new ParticleVector(this.x - other.x, this.y - other.y, this.z - other.z);
	}

	public final ParticleVector divide(float scalar) {
		return new ParticleVector((int)(this.x / scalar), (int)(this.y / scalar), (int)(this.z / scalar));
	}

	public final ParticleVector addLocal(ParticleVector other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		return this;
	}

	public final ParticleVector clone() {
		return new ParticleVector(this.x, this.y, this.z);
	}

	@Override
	public final String toString() {
		return "ParticleVector{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
	}
}