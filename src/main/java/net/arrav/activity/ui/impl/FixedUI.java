package net.arrav.activity.ui.impl;

import net.arrav.activity.ui.UI;

public abstract class FixedUI extends UI {

	/**
	 * Checks the game frame walk positioning.
	 */
	@Override
	public boolean allowScene() {
		return client.mouseX > 0 && client.mouseY > 0 && client.mouseX < 516 && client.mouseY < 338;
	}

	/**
	 * Updates the frame around 3D screen.
	 */
	public abstract void updateSceneOverlay();
}
