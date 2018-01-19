package net.arrav.activity.ui;

import net.arrav.Client;

public abstract class UI {

	/**
	 * Game Instance.
	 */
	public static Client client;

	/**
	 * Builds the menus of the buttons and actions of the scroll bar for the chat.
	 */
	public abstract void buildChat();

	/**
	 * Updates the graphics of the chat.
	 */
	public abstract void updateChat();

	/**
	 * Builds the menus of the content around the map.
	 */
	public abstract void buildMap();

	/**
	 * Updates the graphics of the map.
	 */
	public abstract void updateMap();

	/**
	 * Builds the menus for the inventory.
	 */
	public abstract void buildInventory();

	/**
	 * Updates the graphics of the inventory.
	 */
	public abstract void updateInventory();

	/**
	 * Processes the scene overlay.
	 */
	public abstract void buildSceneOverlay();

	/**
	 * Can the player access the the minimap walking path.
	 * Returns <code>true</code> if player can walk and click the minimap and <code>false</code> otherwise.
	 */
	//public abstract boolean allowMap();

	/**
	 * Can the player access the the scenes options.
	 * Returns <code>true</code> if player can walk and click the scene and <code>false</code> otherwise.
	 */
	public abstract boolean allowScene();
}
