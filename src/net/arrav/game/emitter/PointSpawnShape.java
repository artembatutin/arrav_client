package net.arrav.game.emitter;

import java.util.Random;

public class PointSpawnShape implements SpawnShape {

	private ParticleVector vector;

	public PointSpawnShape(ParticleVector vector) {
		this.vector = vector;
	}

	public final ParticleVector divide(Random random) {
		return vector.clone();
	}
}