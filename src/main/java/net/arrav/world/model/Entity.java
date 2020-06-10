package net.arrav.world.model;

import net.arrav.graphic.Rasterizer3D;
import net.arrav.util.collect.DoublyLinkableEntry;
import net.arrav.world.tile.EntityUnit;

public class Entity extends DoublyLinkableEntry {
	
	public int[] vectorX = null;
	public int[] vectorY = null;
	public int[] vectorZ = null;
	public int[] vectorMagnitude = null;
	public int maxVerticalDistUp;
	public EntityUnit unit;

	public Entity() {
		maxVerticalDistUp = 1000;
	}

	public Model getTransformedModel() {
		return null;
	}

	public void drawModel(int modelYaw, int rollSin, int rollCos, int yawSin, int yawCos, int camX, int camY, int camZ, long hash, double type) {
		final Model model = getTransformedModel();
		if(model != null) {
			maxVerticalDistUp = model.maxVerticalDistUp;
			model.drawModel(modelYaw, rollSin, rollCos, yawSin, yawCos, camX, camY, camZ, hash, type);
		}
	}
	
	public double getType() {
		return Rasterizer3D.TYPES[0];
	}
}