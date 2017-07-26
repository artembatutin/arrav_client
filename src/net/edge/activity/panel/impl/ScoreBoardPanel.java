package net.edge.activity.panel.impl;

import net.edge.Config;
import net.edge.activity.panel.Panel;
import net.edge.cache.unit.Interface;
import net.edge.game.Scene;
import net.edge.game.model.Player;
import net.edge.media.Rasterizer2D;

public class ScoreBoardPanel extends Panel {
	
	/**
	 * Scrolling position.
	 */
	private int scrollPos = 0;
	
	/**
	 * The max scrolling position.
	 */
	private int scrollMax = 0;
	
	
	/**
	 * The condition if the scroll is being dragged.
	 */
	private boolean scrollDrag = false;
	
	/**
	 * The scrolling dragging position.
	 */
	private int scrollDragPos = 0;
	
	/**
	 * The clan flag.
	 */
	private boolean clan = false;
	
	@Override
	public boolean process() {
		/* Initialization */
		int beginX = 4;
		int beginY = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			beginX = client.windowWidth / 2 - 380;
			beginY = client.windowHeight / 2 - 250;
		}
		
		if(Interface.cache[259] == null) {
			Interface.cache[259] = Interface.addInterface(259);
		}
		
		scrollMax = Math.max(35 * 20 - 265, 0);
		
		if(Config.def.panelStyle == 2) {
			if(client.leftClickInRegion(beginX + 382, beginY + 22, beginX + 438, beginY + 47)) {
				client.panelHandler.open(new PlayerPanel(1));
				return true;
			}
		} else {
			if(client.leftClickInRegion(beginX + 382, beginY + 22, beginX + 438, beginY + 42)) {
				client.panelHandler.open(new PlayerPanel(1));
				return true;
			}
		}

		/* Scrolling */
		if(client.mouseInRegion(beginX + 5, beginY + 40, beginX + 493, beginY + 365)) {
			scrollPos += client.mouseWheelAmt * 24;
			if(scrollPos < 0) {
				scrollPos = 0;
			}
			if(scrollPos > scrollMax) {
				scrollPos = scrollMax;
			}
		}
		if(!scrollDrag) {
			int height = 258;
			if(scrollMax > 0) {
				height = 265 * 258 / (scrollMax + 265);
			}
			int pos = 0;
			if(scrollPos != 0) {
				pos = scrollPos * 258 / (scrollMax + 265) + 1;
			}
			int x = 481 + beginX;
			int y = 66 + pos + beginY;
			if(client.mouseDragButton == 1 && client.mouseInRegion(x, y, x + 10, y + height)) {
				scrollDrag = true;
				scrollDragPos = scrollPos;
			}
		} else if(client.mouseDragButton != 1) {
			scrollDrag = false;
		} else {
			int d = (client.mouseY - client.clickY) * (scrollMax + 265) / 258;
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
		drawMain(beginX, beginY + 8, 500, 328, 0x050F00, 0x63625e, 200);
		drawOver(beginX, beginY);
		drawClose(beginX, beginY);
		
		if(Config.def.panelStyle == 2) {
			Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 12, 54, 25, 2, 0xF3B13F, 60);
			if(client.mouseInRegion(beginX + 382, beginY + 22, beginX + 438, beginY + 47)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 12, 54, 25, 2, 0xF3B13F, 20);
			}
			fancyFont.drawCenteredString("Presets", beginX + 407, beginY + 30, 0xF3B13F);
		} else {
			Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 17, 54, 20, 2, 0x000000, 60);
			if(client.mouseInRegion(beginX + 382, beginY + 22, beginX + 438, beginY + 47)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 17, 54, 20, 2, 0x000000, 60);
			}
			fancyFont.drawCenteredString("Presets", beginX + 407, beginY + 32, 0xF3B13F);
		}
		
		/*if(Config.def.panelStyle == 2) {
			Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 12, 54, 20, 2, 0xF3B13F, 60);
			if(client.mouseInRegion(beginX + 382, beginY + 12, beginX + 498, beginY + 42)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 12, 54, 20, 2, 0xF3B13F, 20);
			}
			fancyFont.drawCenteredString(clan ? "Indiv." : "Clan", beginX + 407, beginY + 27, 0xF3B13F);
		} else {
			Rasterizer2D.fillRoundedRectangle(beginX + 408, beginY + 17, 54, 20, 2, 0x000000, 60);
			if(client.mouseInRegion(beginX + 408, beginY + 17, beginX + 462, beginY + 37)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 408, beginY + 17, 54, 20, 2, 0x000000, 20);
			}
			fancyFont.drawCenteredString(clan ? "Indiv." : "Clan", beginX + 434, beginY + 33, 0xF3B13F);
		}*/
		
		fancyFont.drawLeftAlignedEffectString((clan ? "Clan " : "Individual ") + "Scoreboard", beginX + 20, beginY + 30, 0xF3B13F, true);
		smallFont.drawLeftAlignedEffectString("#", beginX + 20, beginY + 53, 0xF3B13F, true);
		smallFont.drawLeftAlignedEffectString("Player name", beginX + 50, beginY + 53, 0xF3B13F, true);
		smallFont.drawLeftAlignedEffectString("Killstreak", beginX + 180, beginY + 53, 0xF3B13F, true);
		smallFont.drawLeftAlignedEffectString("Kills", beginX + 260, beginY + 53, 0xF3B13F, true);
		smallFont.drawLeftAlignedEffectString("Deaths", beginX + 320, beginY + 53, 0xF3B13F, true);
		smallFont.drawLeftAlignedEffectString("K/D ratio", beginX + 380, beginY + 53, 0xF3B13F, true);
		
		Rasterizer2D.setClip(beginX + 5, beginY + 60, beginX + 493, beginY + 330);
		int offset = -scrollPos + 65;
		if(client.killstreak == null) {
			client.scoreNames = new String[20];
			client.killstreak = new int[20];
			client.scoreDeaths = new int[20];
			client.scoreKills = new int[20];
		}
		for(int i = 0; i < client.killstreak.length; i++) {
			if(client.scoreNames[i] == null)
				continue;
			int bg = i == 0 ? 0xFCC900 : i == 1 ? 0x9B9B9B : i == 2 ? 0x87783E : 0x050F00;
			Rasterizer2D.fillRoundedRectangle(beginX + 8, beginY + offset, 460, 30, 3, bg, 100);
			if(!client.menuOpened && client.mouseInRegion(beginX + 8, beginY + offset, beginX + 468, beginY + offset + 30)) {
				Rasterizer2D.fillRectangle(beginX + 8, beginY + offset, 460, 30, 0, 40);
			}
			smallFont.drawLeftAlignedEffectString("" + (i + 1), beginX + 22, beginY + offset + 19, 0xF3B13F, true);
			smallFont.drawLeftAlignedEffectString(client.scoreNames[i], beginX + 40, beginY + offset + 19, 0xF3B13F, true);
			
			smallFont.drawCenteredEffectString(client.killstreak[i] + "", beginX + 200, beginY + offset + 19, 0xF3B13F, true);
			smallFont.drawCenteredEffectString(client.scoreKills[i] + "", beginX + 270, beginY + offset + 19, 0xF3B13F, true);
			smallFont.drawCenteredEffectString(client.scoreDeaths[i] + "", beginX + 340, beginY + offset + 19, 0xF3B13F, true);
			double ratio = ((double) client.scoreKills[i]) / ((double) client.scoreDeaths[i]);
			smallFont.drawCenteredEffectString(String.format("%.2f", ratio) + "", beginX + 400, beginY + offset + 19, 0xF3B13F, true);
			offset += 35;
		}
		Rasterizer2D.drawRectangle(476 + beginX, 65 + beginY, 12, 260, 0xffffff, 60);
		int height = 258;
		if(scrollMax > 0) {
			height = 265 * 258 / (scrollMax + 265);
		}
		int pos = 0;
		if(scrollPos != 0) {
			pos = scrollPos * 258 / (scrollMax + 265) + 1;
		}
		Rasterizer2D.fillRectangle(477 + beginX, 66 + pos + beginY, 10, height, 0x222222, 120);
		Rasterizer2D.removeClip();
	}
	
	@Override
	public void initialize() {
		
	}
	
	@Override
	public void reset() {
	}
	
	@Override
	public int getId() {
		return 12;
	}
	
	@Override
	public boolean blockedMove() {
		return false;
	}
	
}
