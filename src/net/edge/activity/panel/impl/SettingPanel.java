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
			3,
			0,
			1,
			2,
			4,
			5,
			6,
			7,
			8,
			9,
			11,
			12
	};
	
	public boolean bindings;
 
	@Override
	public boolean process() {

		int x = 0;
		int y = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			x = client.windowWidth / 2 - 380;
			y = client.windowHeight / 2 - 250;
		}

		/* Initialization */
		if(client.leftClickInRegion(x + 452, y + 23, x + 500, y + 44)) {
			Scene.hoverX = -1;
			client.panelHandler.close();
		}
		if(client.leftClickInRegion(x + 389, y + 306, x + 493, y + 321)) {
			bindings = !bindings;
		}
		if(bindings) {
			int xOff = 0;
			int yOff = 0;
			for(int i = 0; i < hotkeys.length; i++) {
				if(client.uiRenderer.resizable != null) {
					if(client.leftClickInRegion(x + 45 + xOff, y + 69 + yOff, x + 145 + xOff, y + 106 + yOff))
						selectedBinding = i;
				}
				yOff += 60;
				if(i % 4 == 3) {
					xOff += 110;
					yOff = 0;
				}
			}
			return false;
		}
		
		
		if(client.leftClickInRegion(x + 230, y + 60, x + 284, y + 102)) {
			if(client.uiRenderer.getId() == 1) {
				client.pushMessage("This gameframe is only available in resizable or fullscreen mode.", 0, "System");
				return true;
			}
			client.setMode(0);
		} else if(client.leftClickInRegion(x + 330, y + 60, x + 383, y + 102))
			client.setMode(1);
		else if(client.leftClickInRegion(x + 429, y + 60, x + 484, y + 102))
			client.setMode(2);
		else if(client.leftClickInRegion(x + 150, y + 48, x + 192, y + 63))
			Config.def.setLOW_MEM(!Config.def.isLOW_MEM());
		else if(client.leftClickInRegion(x + 150, y + 72, x + 192, y + 87))
			Config.def.setTWEENING(!Config.def.isTWEENING());
		else if(client.leftClickInRegion(x + 150, y + 96, x + 192, y + 111))
			Config.def.setMAP_ANTIALIASING(!Config.def.isMAP_ANTIALIASING());
		else if(client.leftClickInRegion(x + 150, y + 120, x + 192, y + 135))
			Config.def.setRETAIN_MODEL_PRECISION(!Config.def.isRETAIN_MODEL_PRECISION());
		else if(client.leftClickInRegion(x + 150, y + 144, x + 192, y + 159)) {
			Config.def.setGROUND_DECORATION(!Config.def.isGROUND_DECORATION());
			client.loadRegion();
		} else if(client.leftClickInRegion(x + 150, y + 168, x + 192, y + 183))
			Config.def.setGROUND_MATERIALS(!Config.def.isGROUND_MATERIALS());
		else if(client.leftClickInRegion(x + 150, y + 192, x + 192, y + 207))
			Config.def.setSMOOTH_FOG(!Config.def.isSMOOTH_FOG());
		else if(client.leftClickInRegion(x + 150, y + 216, x + 192, y + 231))
			Config.def.setDISPLAY_NAMES(!Config.def.isDISPLAY_NAMES());
		else if(client.leftClickInRegion(x + 150, y + 240, x + 192, y + 255)) {
			Config.def.setROOF_OFF(!Config.def.isROOF_OFF());
		} else if(client.leftClickInRegion(x + 150, y + 264, x + 192, y + 279))
			Config.def.setDRAW_ORBS(!Config.def.isDRAW_ORBS());
		else if(client.leftClickInRegion(x + 150, y + 288, x + 192, y + 303))
			Config.def.setDRAW_SKILL_ORBS(!Config.def.isDRAW_SKILL_ORBS());
		else if(client.leftClickInRegion(x + 150, y + 312, x + 192, y + 327))
			Config.def.setTEN_X_HITS(!Config.def.isTEN_X_HITS());
		else if(client.leftClickInRegion(x + 246, y + 188, x + 273, y + 203))
			client.uiRenderer.switchRevision(459);
		else if(client.leftClickInRegion(x + 286, y + 188, x + 314, y + 203))
			client.uiRenderer.switchRevision(525);
		else if(client.leftClickInRegion(x + 326, y + 188, x + 355, y + 203))
			client.uiRenderer.switchRevision(562);
		else if(client.leftClickInRegion(x + 366, y + 188, x + 403, y + 203))
			client.uiRenderer.switchRevision(2);
		else if(client.leftClickInRegion(x + 414, y + 188, x + 459, y + 203)) {
			if(client.uiRenderer.isResizableOrFull()) {
				client.uiRenderer.switchRevision(1);
			} else {
				client.pushMessage("This gameframe is only available in resizable or fullscreen mode.", 0, "System");
			}
		} else if(client.leftClickInRegion(x + 391, y + 279, x + 432, y + 293)) {
			if(Config.def.getSELECTED_MENU() == 6)
				Config.def.setSELECTED_MENU(1);
			else
				Config.def.setSELECTED_MENU(Config.def.getSELECTED_MENU() + 1);
		} else if(client.leftClickInRegion(x + 381, y + 253, x + 422, y + 268)) {
			if(Config.def.getHITBARS() == 3)
				Config.def.setHITBARS(0);
			else
				Config.def.setHITBARS(Config.def.getHITBARS() + 1);
		} else if(client.leftClickInRegion(x + 451, y + 273, x + 492, y + 292)) {
			if(Config.def.getHITSPLATS() == 3)
				Config.def.setHITSPLATS(0);
			else
				Config.def.setHITSPLATS(Config.def.getHITSPLATS() + 1);
		} else {
			return false;
		}
		return false;
	}

	public void update() {
		int x = 0;
		int y = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			x = client.windowWidth / 2 - 380;
			y = client.windowHeight / 2 - 250;
		}

		/* Main background */
		Rasterizer2D.fillRectangle(x + 10, y + 8, 500, 328, 0x000000, bindings ? 150 : 200);
		Rasterizer2D.drawRectangle(x + 10, y + 8, 500, 328, 0x63625e);
		
		if(bindings) {
			drawSection(x + 20, y + 20, 310, 480, "Hot keys and bindings");
			boldFont.drawCenteredString("To bind hot keys:", x + 435, y + 150, 0xffffff);
			smallFont.drawLeftAlignedString("1. Select the F key", x + 385, y + 190, 0xffffff);
			smallFont.drawLeftAlignedString("2. Click on the tab", x + 385, y + 210, 0xffffff);
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
			drawTitleButton("Back to main panel", x + 388, y + 303, 0xdb8145);
			if(client.mouseInRegion(x + 452, y + 23, x + 500, y + 44))
				Rasterizer2D.fillRectangle(x + 452, y + 22, 46, 19, 0x000000, 70);
			Rasterizer2D.fillRectangle(x + 450, y + 20, 50, 23, 0xffffff, 20);
			Rasterizer2D.fillRectangle(x + 452, y + 22, 46, 19, 0x000000, 70);
			fancyFont.drawLeftAlignedEffectString("Exit", x + 460, y + 37, 0xFFFFFF, true);
			return;
		}
		
		/* Details */
		drawSection(x + 20, y + 20, 310, 180, "Details");
		for(int button = 0; button < 12; button++) {
			Rasterizer2D.drawHorizontalLine(x + 20, y + 59 + (button * 24), 130, 0xDBB047, 90);
			drawTitleButton("Switch", x + 150, y + 45 + (button * 24), 0xDBB047);
		}
		
		plainFont.drawLeftAlignedEffectString((Config.def.isLOW_MEM() ? "@gre@" : "@red@") + "Low memory mode", x + 20, y + 55, 0, true);
		plainFont.drawLeftAlignedEffectString((Config.def.isTWEENING() ? "@gre@" : "@red@") + "Animation tweening", x + 20, y + 77, 0, true);
		plainFont.drawLeftAlignedEffectString((Config.def.isMAP_ANTIALIASING() ? "@gre@" : "@red@") + "Minimap antialiasing", x + 20, y + 99, 0, true);
		plainFont.drawLeftAlignedEffectString((Config.def.isRETAIN_MODEL_PRECISION() ? "@gre@" : "@red@") + "Model precision", x + 20, y + 123, 0, true);

		plainFont.drawLeftAlignedEffectString((Config.def.isGROUND_DECORATION() ? "@gre@" : "@red@") + "Ground decorations", x + 20, y + 147, 0, true);
		plainFont.drawLeftAlignedEffectString((Config.def.isGROUND_MATERIALS() ? "@gre@" : "@red@") + "Ground materials", x + 20, y + 171, 0, true);
		plainFont.drawLeftAlignedEffectString((Config.def.isSMOOTH_FOG() ? "@gre@" : "@red@") + "Smooth fog", x + 20, y + 195, 0, true);
		plainFont.drawLeftAlignedEffectString((Config.def.isDISPLAY_NAMES() ? "@gre@" : "@red@") + "Display names", x + 20, y + 219, 0, true);
		plainFont.drawLeftAlignedEffectString((Config.def.isROOF_OFF() ? "@red@" : "@gre@") + "Visible roofs", x + 20, y + 243, 0, true);

		plainFont.drawLeftAlignedEffectString((Config.def.isDRAW_ORBS() ? "@gre@" : "@red@") + "Display orbs", x + 20, y + 267, 0, true);
		plainFont.drawLeftAlignedEffectString((Config.def.isDRAW_SKILL_ORBS() ? "@gre@" : "@red@") + "Display skill orbs", x + 20, y + 291, 0, true);
		plainFont.drawLeftAlignedEffectString((Config.def.isTEN_X_HITS() ? "@gre@" : "@red@") + "10x hits", x + 20, y + 315, 0, true);
	
		/* Screen Mode */
		drawSection(x + 215, y + 20, 100, 285, "Screen Mode");
		ImageCache.get(client.uiRenderer.isFixed() ? 9 : 10).drawAlphaImage(x + 230, y + 60);
		ImageCache.get(client.uiRenderer.isResizable() ? 11 : 12).drawAlphaImage(x + 330, y + 60);
		ImageCache.get(client.uiRenderer.isFullscreen() ? 13 : 14).drawAlphaImage(x + 430, y + 60);
		if(client.mouseInRegion(x + 230, y + 60, x + 284, y + 102))
			ImageCache.get(9).drawAlphaImage(x + 230, y + 60);
		if(client.mouseInRegion(x + 330, y + 60, x + 383, y + 102))
			ImageCache.get(11).drawAlphaImage(x + 330, y + 60);
		if(client.mouseInRegion(x + 429, y + 60, x + 484, y + 102))
			ImageCache.get(13).drawAlphaImage(x + 430, y + 60);
			
		/* Game-frames */
		drawSection(x + 215, y + 130, 80, 285, "Game-Frame");
		fancyFont.drawCenteredEffectString("Selected: " + (client.uiRenderer.id == 1 ? "Custom" : (client.uiRenderer.id == 2 ? "OSRS" : client.uiRenderer.id)), x + 355, y + 175, 0xFFFFFF, true);
		for(int id = 0; id < Constants.SELECTABLE_GAMEFRAMES.length; id++) {
			int frame = Constants.SELECTABLE_GAMEFRAMES[id];
			if(frame == 1) {
				drawTitleButton("Custom", x + 245 + (id * 42), y + 185, 0xDB6147);
			} else if (frame == 2) {
				drawTitleButton("OSRS", x + 245 + (id * 40), y + 185, 0x9a7155);
			} else {
				drawTitleButton(Constants.SELECTABLE_GAMEFRAMES[id] + "", x + 245 + (id * 40), y + 185, 0xDBB047);
			}
		}
		
		/* Menus */
		drawSection(x + 215, y + 220, 110, 285, "Preferences");
		drawTitleButton("Hot keys / bindings", x + 388, y + 303, 0xdb8145);
		client.gameActivity.drawer.drawMenu(x + 217, y + 267, true);
		Rasterizer2D.drawHorizontalLine(x + 376, y + 282, 14, 0xDBB047, 90);
		drawTitleButton("Switch", x + 390, y + 275, 0xDBB047);

		int hitmark = Config.def.getHITSPLATS();
		if(hitmark == 0) {
			ImageCache.get(1653).drawAlphaImage(x + 460, y + 245);
		} else if(hitmark == 1) {
			ImageCache.get(1654).drawAlphaImage(x + 458, y + 245);
		} else if(hitmark == 2) {
			ImageCache.get(1652).drawAlphaImage(x + 450, y + 245);
		} else {
			ImageCache.get(1664).drawAlphaImage(x + 450, y + 245);
		}
		Rasterizer2D.drawVerticalLine(x + 470, y + 265, 10, 0xDBB047, 90);
		drawTitleButton("Switch", x + 450, y + 275, 0xDBB047);

		int hitbar = Config.def.getHITBARS();
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

	private void drawSection(int x, int y, int height, int width, String name) {
		Rasterizer2D.fillRectangle(x - 5, y - 5, width + 10, height + 5, 0xFFFFFF, 30);
		Rasterizer2D.fillRectangle(x - 2, y - 2, width + 4, height, 0x000000, 90);
		Rasterizer2D.fillRectangle(x, y, width, 23, 0xDBB047, 90);
		fancyFont.drawCenteredEffectString(name, x + width / 2, y + 17, 0xFFFFFF, true);
	}

	private void drawTitleButton(String text, int x, int y, int color) {
		Rasterizer2D.fillRectangle(x, y, smallFont.getStringWidth(text) + 10, 15, color, 100);
		if(client.mouseInRegion(x + 1, y + 3, x + smallFont.getStringWidth(text) + 10, y + 18)) {
			Rasterizer2D.fillRectangle(x, y, smallFont.getStringWidth(text) + 10, 15, color, 100);
		}
		smallFont.drawLeftAlignedEffectString(text, x + 5, y + 13, 0xFFFFFF, true);
	}
	
	@Override
	public boolean blockedMove() {
		return false;
	}

}
