package net.edge.activity.panel;

/**
 * Handles the interaction of panels.
 */
public class PanelHandler {

	private Panel currentPanel;

	public void update() {
		if(currentPanel != null)
			currentPanel.update();
	}

	public boolean process() {
		return currentPanel != null && currentPanel.process();
	}

	/**
	 * Opens a chosen activity panel.
	 */
	public void open(Panel panel) {
		if(currentPanel != null)
			currentPanel.reset();
		currentPanel = panel;
		currentPanel.initialize();
	}

	/**
	 * Closes the current panel.
	 */
	public void close() {
		if(currentPanel != null)
			currentPanel.reset();
		currentPanel = null;
	}

	/**
	 * Determines if any panel is active.
	 */
	public boolean isActive() {
		return currentPanel != null && currentPanel.getId() != 0;
	}

	/**
	 * Determines if the clan panel is open.
	 */
	public boolean isClanOpen() {
		return currentPanel != null && currentPanel.getId() == 1;
	}

	/**
	 * Determines if the setting panel is open.
	 */
	public boolean isSettingOpen() {
		return currentPanel != null && currentPanel.getId() == 2;
	}

	/**
	 * Determines if the shop panel is open.
	 */
	public boolean isShopOpen() {
		return currentPanel != null && currentPanel.getId() == 3;
	}

	/**
	 * Determines if the train panel is open.
	 */
	public boolean isTrainOpen() {
		return currentPanel != null && currentPanel.getId() == 4;
	}

	/**
	 * Determines if the dungeon panel is open.
	 */
	public boolean isMonsterOpen() {
		return currentPanel != null && currentPanel.getId() == 5;
	}

	/**
	 * Determines if the bank panel is open.
	 */
	public boolean isBankpen() {
		return currentPanel != null && currentPanel.getId() == 6;
	}

	/**
	 * Determines if the minigame panel is open.
	 */
	public boolean isMinigameOpen() {
		return currentPanel != null && currentPanel.getId() == 7;
	}

	/**
	 * Determines if the boss panel is open.
	 */
	public boolean isBossOpen() {
		return currentPanel != null && currentPanel.getId() == 8;
	}
	
	/**
	 * Determines if the title panel is open in blocked instance.
	 */
	public boolean isTitleBlockedOpen() {
		return currentPanel != null && currentPanel.getId() == 9;
	}
	
	/**
	 * Determines if the title panel is open.
	 */
	public boolean isTitleOpen() {
		return currentPanel != null && currentPanel.getId() == 10;
	}
	
	/**
	 * Determines if the slayer panel is open.
	 */
	public boolean isSlayerOpen() {
		return currentPanel != null && currentPanel.getId() == 11;
	}

}
