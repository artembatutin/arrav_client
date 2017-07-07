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
	
	public boolean isBlocked() {
		return currentPanel != null && currentPanel.getId() != 0 && currentPanel.blockedMove();
	}
	
	public boolean on() {
		return currentPanel != null && currentPanel.on();
	}

	public boolean isActive() {
		if(currentPanel == null)
			return false;
		if(currentPanel.getId() == 0)
			return false;
		if(currentPanel.blockedMove())
			return true;
		return true;
	}
	
	public boolean action() {
		if(currentPanel == null)
			return true;
		if(currentPanel.getId() == 0)
			return true;
		if(currentPanel.blockedMove())
			return false;
		if(currentPanel.on())
			return false;
		return true;
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
	 * Determines if the bank panel is open.
	 */
	public boolean isBankpen() {
		return currentPanel != null && currentPanel.getId() == 6;
	}

}
