package net.arrav.activity.panel.impl;

import net.arrav.Config;
import net.arrav.Constants;
import net.arrav.activity.panel.Panel;
import net.arrav.activity.ui.impl.ResizableUI_CUS;
import net.arrav.cache.unit.ImageCache;
import net.arrav.cache.unit.Interface;
import net.arrav.graphic.Rasterizer2D;

public class PlayerPanel extends Panel {
	
	/**
	 * The type of the selected path
	 */
	private int type = -1;
	private String[] typeNames = {"Skill progression", "Player Killing", "Achievement Tasks", "Quests"};
	
	//achievements
	private int[] difColors = { 0xb3b4b3, 0xD9750B, 0xbe7056, 0xC41414};
	public static int[] tiers = new int[100];
	
	/**
	 * Scroll bar manipulated value.
	 */
	private int scrollPos, scrollMax, scrollDragPos;
	
	/**
	 * The condition if the scroll is being dragged.
	 */
	private boolean scrollDrag = false;
	
	public PlayerPanel() {
		type = -1;
	}
	
	public PlayerPanel(int i) {
		type = i;
	}
	
	@Override
	public boolean process() {
	    /* Initialization */
		int beginX = 4;
		int beginY = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			beginX = client.windowWidth / 2 - 380;
			beginY = client.windowHeight / 2 - 250;
		}
		
		processClose(beginX, beginY);
		
		if(Config.def.panelStyle == 2) {
			if(client.leftClickInRegion(beginX + 382, beginY + 22, beginX + 438, beginY + 47)) {
				type = -1;
				return true;
			}
		} else {
			if(client.leftClickInRegion(beginX + 382, beginY + 22, beginX + 438, beginY + 42)) {
				type = -1;
				return true;
			}
		}
		
		if(type == -1) {
			for(int i = 0; i < 4; i++) {
				if(client.leftClickInRegion(beginX + 52 + i * 115, beginY + 150, beginX + 102 + i * 115, beginY + 200)) {
					if(i == 3) {
						client.pushMessage("There is currently no quests available.", 1, "Quest manager");
						return true;
					}
					type = i;
				}
			}
			return false;
		} else if(type == 1) {
			if(client.leftClickInRegion(beginX + 305, beginY + 55, beginX + 390, beginY + 80)) {
				client.panelHandler.open(new ScoreBoardPanel());
			}
			if(client.leftClickInRegion(beginX + 400, beginY + 55, beginX + 485, beginY + 80)) {
				client.panelHandler.open(new PvPPanel());
			}
			return false;
		}
		
		int max1 = 22 * 100;
		scrollMax = Math.max(max1 - 235, 0);
        /* Scrolling */
		if(client.mouseInRegion(beginX + 5, beginY + 50, beginX + 493, beginY + 365)) {
			scrollPos += client.mouseWheelAmt * 24;
			if(scrollPos < 0) {
				scrollPos = 0;
			}
			if(scrollPos > scrollMax) {
				scrollPos = scrollMax;
			}
		}
		if(!scrollDrag) {
			int height = 218;
			if(scrollMax > 0) {
				height = 225 * 218 / (scrollMax + 225);
			}
			int pos = 0;
			if(scrollPos != 0) {
				pos = scrollPos * 218 / (scrollMax + 225) + 1;
			}
			int x = 485;
			int y = 46 + pos;
			if(client.mouseDragButton == 1 && client.mouseInRegion(x, y, x + 20, y + height)) {
				scrollDrag = true;
				scrollDragPos = scrollPos;
			}
		} else if(client.mouseDragButton != 1) {
			scrollDrag = false;
		} else {
			int d = (client.mouseY - client.clickY) * (scrollMax + 275) / 268;
			scrollPos = scrollDragPos + d;
			if(scrollPos < 0) {
				scrollPos = 0;
			}
			if(scrollPos > scrollMax) {
				scrollPos = scrollMax;
			}
		}
		
		return processClose(beginX, beginY);
	}
	
	@Override
	public void update() {
		/* Initialization */
		int beginX = 8;
		int beginY = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			beginX = client.windowWidth / 2 - 380;
			beginY = client.windowHeight / 2 - 250;
		}

		/* Main background */
		drawMain(beginX, beginY + 8, 500, 328, 0x000000, 0x63625e, 200);
		drawOver(beginX, beginY);
		drawClose(beginX, beginY);
		if(type == -1)
			plainFont.drawLeftAlignedEffectString(client.localPlayer.name, beginX + 20, beginY + 31, 0xFF8A1F, true);
		else {
			if(Config.def.panelStyle == 2) {
				Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 12, 54, 25, 2, 0xFF8A1F, 60);
				if(client.mouseInRegion(beginX + 382, beginY + 22, beginX + 438, beginY + 47)) {
					Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 12, 54, 25, 2, 0xFF8A1F, 20);
				}
				plainFont.drawCenteredString("Back", beginX + 407, beginY + 30, 0xFF8A1F);
			} else {
				Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 17, 54, 20, 2, 0x000000, 60);
				if(client.mouseInRegion(beginX + 382, beginY + 22, beginX + 438, beginY + 47)) {
					Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 17, 54, 20, 2, 0x000000, 60);
				}
				plainFont.drawCenteredString("Back", beginX + 407, beginY + 32, 0xFF8A1F);
			}
			plainFont.drawLeftAlignedEffectString(client.localPlayer.name + " - " + typeNames[type], beginX + 20, beginY + 31, 0xFF8A1F, true);
		}
		if(type == -1) {
			ResizableUI_CUS.drawFace(beginX + 235, beginY + 75);
			for(int i = 0; i < 4; i++) {
				ImageCache.get(2047 + i).drawImage(beginX + 52 + i * 115, beginY + 150);
				if(client.mouseInRegion(beginX + 52 + i * 115, beginY + 150, beginX + 102 + i * 115, beginY + 200)) {
					Rasterizer2D.fillRectangle(beginX + 52 + i * 115, beginY + 150, 50, 50, 0x000000, 40);
				}
				smallFont.drawLeftAlignedEffectString(typeNames[i], beginX + 38 + i * 120, beginY + 220, 0xffffff, true);
			}
		} else if(type == 0) {
			ResizableUI_CUS.drawFace(beginX + 38, beginY + 75);
			int offset = 52;
			int x = 0;
			int complet = 0;
			int total = 0;
			for(int i = 0; i < Constants.SKILL_NAMES_UNORDERED.length; i++) {
				int max = 99;
				int cur = client.maxStats[i];
				if(i == 24)
					max = 120;
				complet += max;
				total += cur;
				Rasterizer2D.fillRectangle(beginX + 110 + x, beginY + offset - 10, 100, 25, 0x9e2b18, 200);
				Rasterizer2D.fillRectangle(beginX + 110 + x, beginY + offset - 10, (int) (100 * (cur / (max * 1f))), 25, 0x3a9e18, 200);
				ImageCache.get(1957 + i).drawImage(beginX + 180 + x, beginY + offset - 10);
				Rasterizer2D.drawRectangle(beginX + 110 + x, beginY + offset - 10, 100, 25, 0x000000);
				smallFont.drawLeftAlignedString(Constants.SKILL_NAMES_UNORDERED[i] + ": " + cur, beginX + 113 + x, beginY + offset + 8, 0xffffff);
				offset += 32;
				if(i == 7 || i == 15) {
					x += 130;
					offset = 52;
				}
			}
			plainFont.drawCenteredEffectString((int) (((float) total / complet) * 100) + "%", beginX + 55, beginY + 171, 0xFF8A1F, true);
			plainFont.drawCenteredEffectString(total + " / " + complet, beginX + 55, beginY + 201, 0xFF8A1F, true);
		} else if(type == 1) {
			ResizableUI_CUS.drawFace(beginX + 38, beginY + 75);
			Rasterizer2D.fillRectangle(beginX + 100, beginY + 41, 190, 285, 0xffffff, 20);
			Rasterizer2D.fillRoundedRectangle(beginX + 305, beginY + 55, 85, 25, 4, 0x000000, 80);
			if(client.mouseInRegion(beginX + 305, beginY + 55, beginX + 390, beginY + 80)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 305, beginY + 55, 85, 25, 4, 0x000000, 20);
			}
			plainFont.drawLeftAlignedString("Scoreboard", beginX + 310, beginY + 71, 0xffffff);
			Rasterizer2D.fillRoundedRectangle(beginX + 400, beginY + 55, 85, 25, 4, 0x000000, 80);
			if(client.mouseInRegion(beginX + 400, beginY + 55, beginX + 485, beginY + 80)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 400, beginY + 55, 85, 25, 4, 0x000000, 20);
			}
			plainFont.drawLeftAlignedString("Wilderness", beginX + 405, beginY + 71, 0xffffff);
			/*plainFont.drawLeftAlignedString("Presets", beginX + 370, beginY + 101, 0xffffff);
			Rasterizer2D.fillRoundedRectangle(beginX + 305, beginY + 110, 180, 185, 4, 0x000000, 80);
			drawTitleButton("Set current", beginX + 310, beginY + 305, 0xdb8145);
			drawTitleButton("I", beginX + 384, beginY + 305, 0xdb8145);
			drawTitleButton("II", beginX + 402, beginY + 305, 0xdb8145);
			drawTitleButton("II", beginX + 423, beginY + 305, 0xdb8145);
			drawTitleButton("III", beginX + 443, beginY + 305, 0xdb8145);
			drawTitleButton("VI", beginX + 467, beginY + 305, 0xdb8145);
			
			drawTitleButton("Grab from bank", beginX + 310, beginY + 115, 0xdb8145);*/
			int offset = 60;
			for(int i = 16051; i < 16065; i++) {
				plainFont.drawLeftAlignedEffectString(Interface.cache[i].text, beginX + 120, beginY + offset + 5, 0xffffff, true);
				offset += 15;
			}
		} else if(type == 2) {
			//achievements
			ResizableUI_CUS.drawFace(beginX + 48, beginY + 75);
			Rasterizer2D.drawHorizontalLine(beginX + 5, beginY + 295, 490, 0x000000);
			Rasterizer2D.drawHorizontalLine(beginX + 5, beginY + 296, 490, 0x000000, 100);
			Rasterizer2D.setClip(beginX + 5, beginY + 40, beginX + 493, beginY + 295);
			int offset = -scrollPos + 42;
			String task = null;
			int complet = 0;
			for(int u = 0; u < 100; u++) {
				String tex = Interface.cache[26000 + u].text;
				Rasterizer2D.fillRectangle(beginX + 126, beginY + offset, 345, 20, 0x0000, 100);
				if(client.mouseInRegion(beginX + 126, beginY + offset, beginX + 471, beginY + offset + 20)) {
					Rasterizer2D.fillRectangle(beginX + 126, beginY + offset, 345, 20, 0x0000, 100);
					task = Interface.cache[26100 + u].text;
				}
				if(tiers[u] != -1) {
					ImageCache.get(2043 + tiers[u]).drawImage(beginX + 132, beginY + offset + 3);
					if(tex.length() > 0) {
						smallFont.drawLeftAlignedEffectString(tex, beginX + 152, beginY + offset + 14, difColors[tiers[u]], true);
						if(tex.charAt(0) == '@') {
							complet += 1;
						}
					}
				}
				offset += 22;
			}
			
			boldFont.drawCenteredEffectString("Completed: " + complet, beginX + 65, beginY + 181, 0xFF8A1F, true);
			ImageCache.get(2043).drawImage(beginX + 18, beginY + 210);
			smallFont.drawLeftAlignedString("Easy - 75k", beginX + 38, beginY + 221, 0xb3b4b3);
			ImageCache.get(2044).drawImage(beginX + 18, beginY + 230);
			smallFont.drawLeftAlignedString("Medium - 225k", beginX + 38, beginY + 241, 0xD9750B);
			ImageCache.get(2045).drawImage(beginX + 18, beginY + 250);
			smallFont.drawLeftAlignedString("Hard - 750k", beginX + 38, beginY + 261, 0xbe7056);
			ImageCache.get(2046).drawImage(beginX + 18, beginY + 270);
			smallFont.drawLeftAlignedString("Elite - 1.5m", beginX + 38, beginY + 281, 0xC41414);
			/* Scroll bar */
			Rasterizer2D.drawRectangle(476 + beginX, 55 + beginY, 12, 220, 0xffffff, 60);
			int height = 218;
			if(scrollMax > 0) {
				height = 225 * 218 / (scrollMax + 225);
			}
			int pos = 0;
			if(scrollPos != 0) {
				pos = scrollPos * 218 / (scrollMax + 225) + 1;
			}
			Rasterizer2D.fillRectangle(477 + beginX, 56 + pos + beginY, 10, height, 0x222222, 120);
			Rasterizer2D.removeClip();
			if(task != null) {
				plainFont.drawLeftAlignedEffectString(task, beginX + 20, beginY + 318, 0xFF8A1F, true);
			}
		}
		
	}
	
	@Override
	public void initialize() {

	}
	
	@Override
	public void reset() {

	}
	
	@Override
	public int getId() {
		return 15;
	}
	
	@Override
	public boolean blockedMove() {
		return false;
	}
	
	
	
}