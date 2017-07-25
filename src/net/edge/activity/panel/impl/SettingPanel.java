package net.edge.activity.panel.impl;

import net.edge.Config;
import net.edge.Constants;
import net.edge.activity.panel.Panel;
import net.edge.cache.unit.ImageCache;
import net.edge.game.Scene;
import net.edge.media.Rasterizer2D;
import net.edge.media.img.BitmapImage;

public class SettingPanel extends Panel {
	
	public static int selectedBinding = -1;
	
	public static int[] hotkeys = {
			4,
			5,
			6,
			7,
			0,
			1,
			9,
			10,
			11,
			12,
			13,
			3
	};
	private final static int[] DEFAULTS = {
			4,
			5,
			6,
			7,
			0,
			1,
			9,
			10,
			11,
			12,
			13,
			3
	};
	
	private boolean bindings;
 
	@Override
	public boolean process() {

		int x = 0;
		int y = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			x = client.windowWidth / 2 - 380;
			y = client.windowHeight / 2 - 250;
		}
		if(!client.loggedIn) {
			x = client.windowWidth / 2 - 260;
			y = client.windowHeight / 2 - 170;
		}

		/* Initialization */
		if(client.leftClickInRegion(x + 452, y + 23, x + 500, y + 44)) {
			if(!client.loggedIn) {
				client.titleActivity.settings = null;
				Config.def.save();
				return true;
			}
			Scene.hoverX = -1;
			client.panelHandler.close();
			return true;
		}
		if(client.leftClickInRegion(x + 389, y + 306, x + 493, y + 321)) {
			if(!client.loggedIn) {
				client.titleMessage = "You can only set bindings in-game.";
				return true;
			}
			bindings = !bindings;
			return true;
		}
		if(bindings) {
			int xOff = 0;
			int yOff = 0;
			if(client.leftClickInRegion(x + 389, y + 286, x + 493, y + 301)) {
				System.arraycopy(DEFAULTS, 0, hotkeys, 0, DEFAULTS.length);
				return true;
			}
			for(int i = 0; i < hotkeys.length; i++) {
				if(client.uiRenderer.resizable != null) {
					if(client.leftClickInRegion(x + 45 + xOff, y + 69 + yOff, x + 145 + xOff, y + 106 + yOff)) {
						selectedBinding = i;
						return true;
					}
				}
				yOff += 60;
				if(i % 4 == 3) {
					xOff += 110;
					yOff = 0;
				}
			}
			return false;
		}
		
		
		if(client.leftClickInRegion(x + 230, y + 50, x + 284, y + 92)) {
			if(client.uiRenderer.getId() == 1) {
				if(client.loggedIn)
					client.pushMessage("This gameframe is only available in resizable or fullscreen mode.", 0, "System");
				else
					client.titleMessage = "This gameframe is only available in resizable or fullscreen mode.";
				return true;
			}
			client.setMode(0);
		} else if(client.leftClickInRegion(x + 330, y + 50, x + 383, y + 92))
			client.setMode(1);
		else if(client.leftClickInRegion(x + 429, y + 50, x + 484, y + 92))
			client.setMode(2);
		else if(client.leftClickInRegion(x + 150, y + 48, x + 192, y + 63))
			Config.def.setLowMem(!Config.def.lowMem());
		else if(client.leftClickInRegion(x + 150, y + 72, x + 192, y + 87))
			Config.def.tween(!Config.def.tween());
		else if(client.leftClickInRegion(x + 150, y + 96, x + 192, y + 111)) {
			Config.def.enchanceMap(!Config.def.enchanceMap());
			client.renderMinimap(client.cameraPlane);
		}else if(client.leftClickInRegion(x + 150, y + 120, x + 192, y + 135))
			Config.def.modelPrecision(!Config.def.modelPrecision());
		else if(client.leftClickInRegion(x + 150, y + 144, x + 192, y + 159)) {
			Config.def.groundDec(!Config.def.groundDec());
			if(client.loggedIn)
				client.loadRegion();
		} else if(client.leftClickInRegion(x + 150, y + 168, x + 192, y + 183))
			Config.def.groundMat(!Config.def.groundMat());
		else if(client.leftClickInRegion(x + 150, y + 192, x + 192, y + 207))
			Config.def.fog(!Config.def.fog());
		else if(client.leftClickInRegion(x + 150, y + 216, x + 192, y + 231))
			Config.def.names(!Config.def.names());
		else if(client.leftClickInRegion(x + 150, y + 240, x + 192, y + 255)) {
			Config.def.roof(Config.def.roof());
		} else if(client.leftClickInRegion(x + 150, y + 264, x + 192, y + 279))
			Config.def.orbs(!Config.def.orbs());
		else if(client.leftClickInRegion(x + 150, y + 288, x + 192, y + 303))
			Config.def.skillOrbs(!Config.def.skillOrbs());
		else if(client.leftClickInRegion(x + 150, y + 312, x + 192, y + 327))
			Config.def.hits(!Config.def.hits());
		else if(client.leftClickInRegion(x + 246, y + 163, x + 273, y + 178))
			client.uiRenderer.switchRevision(459);
		else if(client.leftClickInRegion(x + 286, y + 163, x + 314, y + 178))
			client.uiRenderer.switchRevision(525);
		else if(client.leftClickInRegion(x + 326, y + 163, x + 355, y + 178))
			client.uiRenderer.switchRevision(562);
		else if(client.leftClickInRegion(x + 366, y + 163, x + 403, y + 178))
			client.uiRenderer.switchRevision(2);
		else if(client.leftClickInRegion(x + 414, y + 163, x + 459, y + 178)) {
			if(client.uiRenderer.isResizableOrFull()) {
				client.uiRenderer.switchRevision(1);
			} else {
				if(client.loggedIn)
				client.pushMessage("This gameframe is only available in resizable or fullscreen mode.", 0, "System");
					else
				client.titleMessage = "This gameframe is only available in resizable or fullscreen mode.";
			}
		} else if(client.leftClickInRegion(x + 280, y + 185, x + 430, y + 208)) {
			Config.def.panelStyle = Config.def.panelStyle + 1;
			if(Config.def.panelStyle > 2) {
				Config.def.panelStyle = 0;
			}
		} else if(client.leftClickInRegion(x + 391, y + 279, x + 432, y + 293)) {
			if(Config.def.menu() == 6)
				Config.def.menu(1);
			else
				Config.def.menu(Config.def.menu() + 1);
		} else if(client.leftClickInRegion(x + 381, y + 253, x + 422, y + 268)) {
			if(Config.def.hitbar() == 3)
				Config.def.hitbar(0);
			else
				Config.def.hitbar(Config.def.hitbar() + 1);
		} else if(client.leftClickInRegion(x + 451, y + 273, x + 492, y + 292)) {
			if(Config.def.hitsplat() == 3)
				Config.def.hitsplat(0);
			else
				Config.def.hitsplat(Config.def.hitsplat() + 1);
		} else {
			return false;
		}
		return true;
	}

	public void update() {
		int x = 0;
		int y = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			x = client.windowWidth / 2 - 380;
			y = client.windowHeight / 2 - 250;
		}
		if(!client.loggedIn) {
			x = client.windowWidth / 2 - 260;
			y = client.windowHeight / 2 - 170;
		}

		/* Main background */
		drawMain(x + 10, y + 8, 500, 323, 0x000000, 0x63625e, bindings ? 150 : 200);
		
		if(bindings) {
			drawSection(x + 20, y + 20, 310, 480, "Hot keys and bindings");
			boldFont.drawCenteredString("To bind hot keys:", x + 435, y + 150, 0xffffff);
			smallFont.drawLeftAlignedString("1. Select the F key", x + 385, y + 190, 0xffffff);
			smallFont.drawLeftAlignedString("2. Click on the tab ->", x + 385, y + 210, 0xffffff);
			int xOff = 0;
			int yOff = 0;
			for(int i = 0; i < hotkeys.length; i++) {
				if(client.uiRenderer.resizable != null) {
					BitmapImage icon = client.uiRenderer.resizable.getSide(hotkeys[i]);
					Rasterizer2D.fillRoundedRectangle(x + 45 + xOff, y + 69 + yOff, 100, 37, 4, 0xffffff, 30);
					if(selectedBinding == i)
						Rasterizer2D.fillRoundedRectangle(x + 45 + xOff, y + 69 + yOff, 100, 37, 4, 0xffffff, 30);
					if(client.mouseInRegion(x + 45 + xOff, y + 69 + yOff, x + 145 + xOff, y + 106 + yOff))
						Rasterizer2D.fillRoundedRectangle(x + 45 + xOff, y + 69 + yOff, 100, 37, 4, 0xffffff, 30);
					if(icon != null) {
						icon.drawImage(x + 110 + xOff, y + 74 + yOff);
					}
					plainFont.drawLeftAlignedString("F"+ (1 + i) + " key", x + 60 + xOff, y + 90 + yOff, 0xffffff);
				}
				yOff += 60;
				if(i % 4 == 3) {
					xOff += 110;
					yOff = 0;
				}
			}
			drawTitleButton("Set default keys", x + 388, y + 283, 0xdb8145);
			drawTitleButton("Back to main panel", x + 388, y + 303, 0xdb8145);
			if(client.mouseInRegion(x + 452, y + 23, x + 500, y + 44))
				Rasterizer2D.fillRectangle(x + 452, y + 22, 46, 19, 0x000000, 70);
			Rasterizer2D.fillRectangle(x + 450, y + 20, 50, 23, 0xffffff, 20);
			Rasterizer2D.fillRectangle(x + 452, y + 22, 46, 19, 0x000000, 70);
			fancyFont.drawLeftAlignedEffectString("Exit", x + 460, y + 37, 0xFFFFFF, true);
			return;
		}
		
		/* Details */
		drawSection(x + 20, y + 20, 307, 180, "Details");
		for(int button = 0; button < 12; button++) {
			Rasterizer2D.drawHorizontalLine(x + 20, y + 59 + (button * 24), 130, 0xDBB047, 90);
			drawTitleButton("Switch", x + 150, y + 45 + (button * 24), 0xDBB047);
		}
		
		plainFont.drawLeftAlignedEffectString((Config.def.lowMem() ? "@gre@" : "@red@") + "Low memory mode", x + 20, y + 55, 0, true);
		plainFont.drawLeftAlignedEffectString((Config.def.tween() ? "@gre@" : "@red@") + "Animation tweening", x + 20, y + 77, 0, true);
		plainFont.drawLeftAlignedEffectString((Config.def.enchanceMap() ? "@gre@" : "@red@") + "Enhanced Minimap", x + 20, y + 99, 0, true);
		plainFont.drawLeftAlignedEffectString((Config.def.modelPrecision() ? "@gre@" : "@red@") + "Model precision", x + 20, y + 123, 0, true);

		plainFont.drawLeftAlignedEffectString((Config.def.groundDec() ? "@gre@" : "@red@") + "Ground decorations", x + 20, y + 147, 0, true);
		plainFont.drawLeftAlignedEffectString((Config.def.groundMat() ? "@gre@" : "@red@") + "Ground materials", x + 20, y + 171, 0, true);
		plainFont.drawLeftAlignedEffectString((Config.def.fog() ? "@gre@" : "@red@") + "Smooth fog", x + 20, y + 195, 0, true);
		plainFont.drawLeftAlignedEffectString((Config.def.names() ? "@gre@" : "@red@") + "Display names", x + 20, y + 219, 0, true);
		plainFont.drawLeftAlignedEffectString((!Config.def.roof() ? "@red@" : "@gre@") + "Visible roofs", x + 20, y + 243, 0, true);

		plainFont.drawLeftAlignedEffectString((Config.def.orbs() ? "@gre@" : "@red@") + "Display orbs", x + 20, y + 267, 0, true);
		plainFont.drawLeftAlignedEffectString((Config.def.skillOrbs() ? "@gre@" : "@red@") + "Display skill orbs", x + 20, y + 291, 0, true);
		plainFont.drawLeftAlignedEffectString((Config.def.hits() ? "@gre@" : "@red@") + "10x hits", x + 20, y + 315, 0, true);
	
		/* Screen Mode */
		drawSection(x + 215, y + 20, 82, 285, "Screen Mode");
		ImageCache.get(client.uiRenderer.isFixed() ? 9 : 10).drawImage(x + 230, y + 50);
		ImageCache.get(client.uiRenderer.isResizable() ? 11 : 12).drawImage(x + 330, y + 50);
		ImageCache.get(client.uiRenderer.isFullscreen() ? 13 : 14).drawImage(x + 430, y + 50);
		if(client.mouseInRegion(x + 230, y + 50, x + 284, y + 92))
			ImageCache.get(9).drawImage(x + 230, y + 50);
		if(client.mouseInRegion(x + 330, y + 50, x + 383, y + 92))
			ImageCache.get(11).drawImage(x + 330, y + 50);
		if(client.mouseInRegion(x + 429, y + 50, x + 484, y + 92))
			ImageCache.get(13).drawImage(x + 430, y + 50);
			
		/* Game-frames */
		drawSection(x + 215, y + 112, 98, 285, "User Interface");
		fancyFont.drawCenteredEffectString("Selected: " + (client.uiRenderer.id == 1 ? "Custom" : (client.uiRenderer.id == 2 ? "OSRS" : client.uiRenderer.id)), x + 355, y + 155, 0xFFFFFF, true);
		for(int id = 0; id < Constants.SELECTABLE_GAMEFRAMES.length; id++) {
			int frame = Constants.SELECTABLE_GAMEFRAMES[id];
			if(frame == 1) {
				drawTitleButton("Custom", x + 245 + (id * 42), y + 160, 0xDB6147);
			} else if (frame == 2) {
				drawTitleButton("OSRS", x + 245 + (id * 40), y + 160, 0x9a7155);
			} else {
				drawTitleButton(Constants.SELECTABLE_GAMEFRAMES[id] + "", x + 245 + (id * 40), y + 160, 0xDBB047);
			}
		}
		Rasterizer2D.fillRoundedRectangle(x + 280, y + 185, 150, 20, 4, 0xffffff, 50);
		if(client.mouseInRegion(x + 280, y + 185, x + 430, y + 208)) {
			Rasterizer2D.fillRoundedRectangle(x + 280, y + 185, 150, 20, 4, 0xffffff, 25);
		}
		boldFont.drawCenteredEffectString("Panel style: " + (Config.def.panelStyle == 0 ? "OSRS" : (Config.def.panelStyle == 1 ? "EOC" : "Custom")), x + 355, y + 200, 0xFFFFFF, true);
		
		
		/* Menus */
		drawSection(x + 215, y + 220, 107, 285, "Preferences");
		drawTitleButton("Hot keys / bindings", x + 388, y + 303, 0xdb8145);
		client.gameActivity.drawer.drawMenu(x + 217, y + 267, true);
		Rasterizer2D.drawHorizontalLine(x + 376, y + 282, 14, 0xDBB047, 90);
		drawTitleButton("Switch", x + 390, y + 275, 0xDBB047);

		int hitmark = Config.def.hitsplat();
		if(hitmark == 0) {
			ImageCache.get(1653).drawImage(x + 460, y + 245);
		} else if(hitmark == 1) {
			ImageCache.get(1654).drawImage(x + 458, y + 245);
		} else if(hitmark == 2) {
			ImageCache.get(1652).drawImage(x + 450, y + 245);
		} else {
			ImageCache.get(1664).drawImage(x + 450, y + 245);
		}
		Rasterizer2D.drawVerticalLine(x + 470, y + 265, 10, 0xDBB047, 90);
		drawTitleButton("Switch", x + 450, y + 275, 0xDBB047);

		int hitbar = Config.def.hitbar();
		if(hitbar == 0) {
			Rasterizer2D.fillRectangle(x + 320 - 15, y + 257 - 3, 20, 5, 65280);
			Rasterizer2D.fillRectangle(x + 320 + 5, y + 257 - 3, 10, 5, 0xff0000);
		} else if(hitbar == 1) {
			ImageCache.get(1655).drawImage(x + 280, y + 253);
		} else if(hitbar == 3) {
			smallFont.drawCenteredString("above heads", x + 300, y + 260, 0xffffff);
		} else {
			Rasterizer2D.fillRoundedRectangle(x + 305 - 31, y + 257 - 3, 62, 5, 2, 0x000000, 120);
			Rasterizer2D.fillRoundedRectangle(x + 305 - 30, y + 257 - 3, 40, 5, 3, 65280, 180);
		}
		drawTitleButton("Switch", x + 380, y + 250, 0xDBB047);
		Rasterizer2D.drawHorizontalLine(x + 336, y + 257, 44, 0xDBB047, 90);


		if(client.mouseInRegion(x + 452, y + 23, x + 500, y + 44))
			Rasterizer2D.fillRectangle(x + 452, y + 22, 46, 19, 0x000000, 70);
		Rasterizer2D.fillRectangle(x + 450, y + 20, 50, 23, 0xffffff, 20);
		Rasterizer2D.fillRectangle(x + 452, y + 22, 46, 19, 0x000000, 70);
		fancyFont.drawLeftAlignedEffectString("Exit", x + 460, y + 37, 0xFFFFFF, true);
		//plainFont.drawLeftAlignedEffectString("Mouse: " + client.mouseX + "," + client.mouseY, x + 5, y + 15, 0xffff00, false);
	}

	@Override
	public void initialize() {
		bindings = false;
		selectedBinding = -1;
	}

	@Override
	public void reset() {
		selectedBinding = -1;
		Config.def.save();
	}

	@Override
	public int getId() {
		return 2;
	}
	
	@Override
	public boolean blockedMove() {
		return false;
	}

}
