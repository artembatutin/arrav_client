package net.edge.activity.panel.impl;

import net.edge.Constants;
import net.edge.activity.panel.Panel;
import net.edge.activity.ui.impl.ResizableUI_CUS;
import net.edge.cache.unit.ImageCache;
import net.edge.cache.unit.Interface;
import net.edge.cache.unit.NPCType;
import net.edge.game.Scene;
import net.edge.media.Rasterizer2D;

public class PlayerPanel extends Panel {
	
	/**
	 * The npc type of this drop.
	 */
	private NPCType type;
	
	/**
	 * Scroll bar manipulated value.
	 */
	private int scrollPos, scrollMax, scrollDragPos;
	
	/**
	 * The condition if the scroll is being dragged.
	 */
	private boolean scrollDrag = false;
	
	@Override
	public boolean process() {
		if(type == null && client.npcInfoId != 0) {
			type = NPCType.get(client.npcInfoId);
		}
	    /* Initialization */
		int beginX = 4;
		int beginY = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			beginX = client.windowWidth / 2 - 380;
			beginY = client.windowHeight / 2 - 250;
		}
		
		int max1 = 43 * 5;
		int max2 = (32 * 5);
		scrollMax = Math.max(max1 - 285, 0);

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
			int height = 268;
			if(scrollMax > 0) {
				height = 275 * 268 / (scrollMax + 275);
			}
			int pos = 0;
			if(scrollPos != 0) {
				pos = scrollPos * 268 / (scrollMax + 275) + 1;
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
		
		fancyFont.drawLeftAlignedEffectString(client.localPlayer.name, beginX + 20, beginY + 31, 0xF3B13F, true);
		
		Rasterizer2D.setClip(beginX + 5, beginY + 40, beginX + 493, beginY + 340);
		ResizableUI_CUS.drawFace(45, 75);
		
		int offset = 140;
		int[] texts = {
				16045,
				16048,
				16049,
		};
		for(int i = 0; i < texts.length; i++) {
			smallFont.drawLeftAlignedEffectString(Interface.cache[texts[i]].text, beginX + 2, beginY + offset + 5, 0xffffff, true);
			offset += 10;
		}
		
		//skills
		Rasterizer2D.fillRectangle(beginX + 105, beginY + 40, 215, 290, 0xffffff, 30);
		offset = 52;
		int x = 0;
		for(int i = 0; i < Constants.SKILL_NAMES_UNORDERED.length; i++) {
			int max = 99;
			int cur = client.maxStats[i];
			if(i == 24)
				max = 120;
			Rasterizer2D.setClip(beginX + 110 + x, beginY + offset - 10, beginX + 210 + x, beginY + offset + 9);
			Rasterizer2D.fillRectangle(beginX + 110 + x, beginY + offset - 10, 100, 19, 0x9e2b18, 200);
			Rasterizer2D.fillRectangle(beginX + 110 + x, beginY + offset - 10, (int) (100 * (cur / (max * 1f))), 19, 0x3a9e18, 200);
			ImageCache.get(1957 + i).drawImage(beginX + 180 + x, beginY + offset - 12);
			Rasterizer2D.drawRectangle(beginX + 110 + x, beginY + offset - 10, 100, 19, 0x000000);
			smallFont.drawLeftAlignedString(Constants.SKILL_NAMES_UNORDERED[i] + ": " + cur, beginX + 113 + x, beginY + offset + 5, 0xffffff);
			offset += 22;
			if(i == 12) {
				x += 105;
				offset = 52;
			}
		}
		Rasterizer2D.removeClip();
		
		
		//achievements
		Rasterizer2D.fillRectangle(beginX + 320, beginY + 40, 175, 290, 0x000000, 50);
		offset = -scrollPos + 52;
		for(int u = 0; u < 10; u++) {
			Rasterizer2D.fillRectangle(beginX + 326, beginY + offset, 145, 20, 0x0000, 100);
			plainFont.drawLeftAlignedString("task name", beginX + 332, beginY + offset + 14, 0xffffff);
			offset += 22;
		}

		/* Scroll bar */
		Rasterizer2D.drawRectangle(476 + beginX, 55 + beginY, 12, 270, 0xffffff, 60);
		int height = 268;
		if(scrollMax > 0) {
			height = 275 * 268 / (scrollMax + 275);
		}
		int pos = 0;
		if(scrollPos != 0) {
			pos = scrollPos * 268 / (scrollMax + 275) + 1;
		}
		Rasterizer2D.fillRectangle(477 + beginX, 56 + pos + beginY, 10, height, 0x222222, 120);
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
		return 15;
	}
	
	@Override
	public boolean blockedMove() {
		return false;
	}
	
}