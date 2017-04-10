package net.edge.activity.ui;

import net.edge.Client;
import net.edge.Constants;
import net.edge.activity.ui.fixed.FixedUI;
import net.edge.activity.ui.resize.ResizableUI;

public class UIRenderer {

	/**
	 * Game Instance.
	 */
	private final Client client;

	/**
	 * Fixed screen Instance.
	 */
	public FixedUI fixed;

	/**
	 * Resizable screen Instance.
	 */
	public ResizableUI resizable;

	/**
	 * The game mode. 1 = Fixed 2 = Resizable 3 = Fullscreen
	 */
	private byte gameFrameMode = 0;

	/**
	 * ID of the chosen gameframe.
	 */
	public int id;

	/**
	 * FrameRenderer
	 */
	public UIRenderer(Client game, int id) {
		this.client = game;
		this.id = id;
		fixed = game.gameframes.getFixed(id);
		resizable = game.gameframes.getResizable(id);
	}

	/**
	 * ChatBox message parameters check.
	 */
	public boolean canSeeMessage(int type, int view, int rights, String name) {
		switch(type) {
			case 0:
				return view == Constants.MSG_GAME || view == Constants.MSG_ALL;
			case 1:
				return (client.publicChatMode == 0 || client.publicChatMode == 1 && (client.isFriendOrSelf(name) || rights != 0)) && (view == Constants.MSG_PUBLIC || view == Constants.MSG_ALL);
			case 2:
				return (client.privateChatMode == 0 || client.privateChatMode == 1 && (client.isFriendOrSelf(name) || rights != 0)) && (view == Constants.MSG_PRIVATE || client.splitPrivateChat == 0 && view == Constants.MSG_ALL);
			case 5:
			case 6:
				return client.privateChatMode < 2 && (view == Constants.MSG_PRIVATE || client.splitPrivateChat == 0 && view == Constants.MSG_ALL);
			case 4:
				return (client.tradeMode == 0 || client.tradeMode == 1 && client.isFriendOrSelf(name)) && (view == Constants.MSG_REQUEST || view == Constants.MSG_ALL);
			case 7:
				return (client.clanChatMode == 0 || client.clanChatMode == 1 && client.isFriendOrSelf(name)) && (view == Constants.MSG_CLAN || view == Constants.MSG_ALL);
			case 8:
				return (client.tradeMode == 0 || client.tradeMode == 1 && client.isFriendOrSelf(name)) && (view == Constants.MSG_REQUEST || view == Constants.MSG_ALL);
			case 9:
				return false;
			default:
				return false;
		}
	}

	/**
	 * Gets the game frame mode.
	 */
	public byte getMode() {
		return gameFrameMode;
	}

	/**
	 * Gets the game frame revision id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Is the game mode fixed.
	 */
	public boolean isFixed() {
		return gameFrameMode == Constants.UI_FIXED;
	}

	/**
	 * is the game mode fullscreen
	 */
	public boolean isFullscreen() {
		return gameFrameMode == Constants.UI_FULLSCREEN;
	}

	/**
	 * is the game mode resizable
	 */
	public boolean isResizable() {
		return gameFrameMode == Constants.UI_RESIZABLE;
	}

	/**
	 * is the game mode resizable or fullscreen?
	 */
	public boolean isResizableOrFull() {
		return gameFrameMode != Constants.UI_FIXED;
	}

	/**
	 * Displays the scene frame.
	 */
	public void displayFrameArea() {
		if(isFixed()) {
			fixed.updateSceneOverlay();
		}
	}

	/**
	 * Set the game frame mode.
	 */
	public void setMode(int mode) {
		if(id == 1 && mode == Constants.UI_FIXED) {
			client.pushMessage("This gameframe is only available in resizable or fullscreen mode.", 0, "System");
			return;
		}
		gameFrameMode = (byte) mode;
	}

	/**
	 * Switch game frame revision.
	 */
	public void switchRevision(int id) {
		this.id = id;
		fixed = client.gameframes.getFixed(id);
		resizable = client.gameframes.getResizable(id);
	}

}
