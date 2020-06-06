package net.arrav;

import net.arrav.activity.panel.impl.SettingPanel;
import net.arrav.activity.ui.util.CounterHandler;
import net.arrav.graphic.Rasterizer3D;
import net.arrav.net.SignLink;
import net.arrav.util.FileToolkit;
import net.arrav.util.io.Buffer;

public class Config {
	
	public static Config def = new Config();


	/**
	 * The login clouds
	 */
	public boolean clouds = false;
	
	/**
	 * The brightness to be saved.
	 */
	public int brightness = 3;
	
	/**
	 * The panel visual style
	 */
	public int panelStyle;

	/**
	 * Selected Menus
	 * OLD: 1
	 * OLD ALPHA: 2
	 * MIDDLE: 3
	 * NEW: 4
	 * NEW ALPHA: 5
	 * CUSTOM: 6
	 */
	private int menu = 4;

	/**
	 * Hitsplats
	 * OLD-317: 0
	 * NEW-562: 1
	 * NEWEST-660: 2
	 */
	private int hitsplat = 2;

	/**
	 * Hitbars
	 * OLD-317: 0
	 * NEW-562: 1
	 */
	private int hitbar = 1;

	/**
	 * The 10x hitpoints toggle.
	 */
	private boolean hits = true;

	/**
	 * The game-frame revision id.
	 */
	private int gameframe = 562;
	
	/**
	 * Condition if older models will be used.
	 */
	public boolean oldModels = false;

	/*
	 * Debug configurations
	 */
	private boolean data = false;
	private boolean fps = false;
	private boolean idx = true;
	private boolean sprite = false;

	/*
	 * Detail configurations
	 */
	private boolean lowMem = false;
	private boolean groundDec = true;
	public boolean groundMat = false;
	private boolean fog = false;
	private boolean tween = false;
	private boolean modelPrecision = true;
	private boolean enchanceMap = true;

	/*
	 * View toggle configurations
	 */
	private boolean orbs = true;
	private boolean skillOrbs = false;

	/*
	 * Style configurations
	 */
	private boolean names = false;
	private int privateChat = 0xffff;
	private boolean charPrev = true;
	/** Roofs being off all the time. */
	private boolean roofOff = true;

	/**
	 * Saves the configurations.
	 */
	public void save() {
		Buffer data = new Buffer(new byte[38]);
		data.putInt(CounterHandler.gainedXP);
		data.putShort(gameframe);
		data.putByte(menu);
		data.putByte(hitsplat);
		data.putByte(hitbar);
		data.putByte(hits ? 1 : 0);
		data.putByte(fps ? 1 : 0);
		data.putByte(lowMem ? 1 : 0);
		data.putByte(groundDec ? 1 : 0);
		data.putByte(groundMat ? 1 : 0);
		data.putByte(fog ? 1 : 0);
		data.putByte(tween ? 1 : 0);
		data.putByte(modelPrecision ? 1 : 0);
		data.putByte(enchanceMap ? 1 : 0);
		data.putByte(orbs ? 1 : 0);
		data.putByte(skillOrbs ? 1 : 0);
		data.putByte(names ? 1 : 0);
		data.putByte(charPrev ? 1 : 0);
		data.putByte(roofOff ? 1 : 0);
		data.putByte(brightness);
		data.putByte(panelStyle);
		data.putByte(clouds ? 1 : 0);
		for(int key : SettingPanel.hotkeys) {
			data.putByte(key);
		}
		FileToolkit.writeFile(SignLink.getCacheDir() + "settings", data.data);
	}

	/**
	 * Loads the configurations on startup.
	 */
	public void load() {
		try {
			byte[] data = FileToolkit.readFile(SignLink.getCacheDir() + "settings");
			if(data != null && data.length > 0) {
				Buffer buf = new Buffer(data);
				CounterHandler.gainedXP = buf.getInt();
				gameframe = buf.getUShort();
				menu = buf.getUByte();
				hitsplat = buf.getUByte();
				hitbar = buf.getUByte();
				hits = buf.getBoolean();
				fps = buf.getBoolean();
				lowMem = buf.getBoolean();
				groundDec = buf.getBoolean();
				groundMat = buf.getBoolean();
				fog = buf.getBoolean();
				tween = buf.getBoolean();
				modelPrecision = buf.getBoolean();
				enchanceMap = buf.getBoolean();
				orbs = buf.getBoolean();
				skillOrbs = buf.getBoolean();
				names = buf.getBoolean();
				charPrev = buf.getBoolean();
				roofOff = buf.getBoolean();
				brightness = buf.getUByte();
				panelStyle = buf.getUByte();
				clouds = buf.getBoolean();
				for(int i = 0; i < SettingPanel.hotkeys.length; i++) {
					SettingPanel.hotkeys[i] = buf.getUByte();
				}
			}
		} catch(Exception e) {
		e.printStackTrace();
		}
	}
	
	public int menu() {
		return menu;
	}
	
	public void menu(int menu) {
		this.menu = menu;
	}
	
	public int hitsplat() {
		return hitsplat;
	}
	
	public void hitsplat(int hitsplat) {
		this.hitsplat = hitsplat;
	}
	
	public int hitbar() {
		return hitbar;
	}
	
	public void hitbar(int hitbar) {
		this.hitbar = hitbar;
	}
	
	public boolean hits() {
		return hits;
	}
	
	public void hits(boolean hits) {
		this.hits = hits;
	}
	
	public int gameframe() {
		return gameframe;
	}
	
	public void gameframe(int gameframe) {
		this.gameframe = gameframe;
	}
	
	public boolean data() {
		return data;
	}
	
	public void data(boolean data) {
		this.data = data;
	}
	
	public boolean fps() {
		return fps;
	}
	
	public void fps(boolean FPS_ON) {
		this.fps = FPS_ON;
	}
	
	public boolean idx() {
		return idx;
	}

	public boolean sprite() {
		return sprite;
	}

	public void setSprite(boolean sprite) {
		this.sprite = sprite;
	}

	public void idx(boolean DEBUG_INDEXES) {
		this.idx = DEBUG_INDEXES;
	}
	
	public boolean lowMem() {
		return lowMem;
	}
	
	public void setLowMem(boolean lowMem) {
		this.lowMem = lowMem;
	}
	
	public boolean groundDec() {
		return groundDec;
	}
	
	public void groundDec(boolean groundDec) {
		this.groundDec = groundDec;
	}
	
	public boolean groundMat() {
		return groundMat;
	}
	
	public void groundMat(boolean groundMat) {
		this.groundMat = groundMat;
	}
	
	public boolean fog() {
		return fog;
	}
	
	public void fog(boolean fog) {
		this.fog = fog;
	}
	
	public boolean tween() {
		return tween;
	}
	
	public void tween(boolean tween) {
		this.tween = tween;
	}
	
	public boolean modelPrecision() {
		return modelPrecision;
	}
	
	public void modelPrecision(boolean modelPrecision) {
		this.modelPrecision = modelPrecision;
	}
	
	public boolean enchanceMap() {
		return enchanceMap;
	}
	
	public void enchanceMap(boolean enchanceMap) {
		this.enchanceMap = enchanceMap;
	}
	
	public boolean orbs() {
		return orbs;
	}
	
	public void orbs(boolean orbs) {
		this.orbs = orbs;
	}
	
	public boolean skillOrbs() {
		return skillOrbs;
	}
	
	public void skillOrbs(boolean skillOrbs) {
		this.skillOrbs = skillOrbs;
	}
	
	public boolean names() {
		return names;
	}
	
	public void names(boolean names) {
		this.names = names;
	}
	
	public int privateChat() {
		return privateChat;
	}
	
	public void privateChat(int privateChat) {
		this.privateChat = privateChat;
	}
	
	public boolean charPrev() {
		return charPrev;
	}
	
	public void charPrev(boolean charPrev) {
		this.charPrev = charPrev;
	}
	
	public boolean roof() {
		return !roofOff;
	}
	
	public void roof(boolean roofOff) {
		this.roofOff = roofOff;
	}
	
}
