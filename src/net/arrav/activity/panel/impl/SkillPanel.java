package net.arrav.activity.panel.impl;

import net.arrav.activity.panel.Panel;
import net.arrav.cache.unit.ImageCache;
import net.arrav.media.Rasterizer2D;

public class SkillPanel extends Panel {
	
	public static final String[] SKILL_NAME = {"Prayer", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Farming", "Runecrafting", "Construction", "Hunter", "Summoning", "Dungeoneering"};
	public static final boolean[] SKILL_ENABLED = {
			true,//Prayer
			true,//Cooking
			true,//Woodcutting
			true,//Fletching
			true,//Fishing
			true,//Firemaking
			true,//Crafting
			true,//Smithing
			true,//Mining
			true,//Herblore
			true,//Agility
			true,//Thieving
			true,//Farming
			true,//Runecrafting
			false,//Construction
			true,//Hunter
			true,//Summoning
			false//Dungeoneering
	};

	@Override
	public boolean process() {
	    /* Initialization */
		int beginX = 4;
		int beginY = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			beginX = client.windowWidth / 2 - 380;
			beginY = client.windowHeight / 2 - 250;
		}

        /* Exit */
		if(processClose(beginX, beginY))
			return true;
		/* Clicking a skill */
		int offset = 45;
		for(int i = 0; i < SKILL_NAME.length; i++) {
			int x = i % 4 * 121;
			int skill = i >= 1 ? i > 11 ? i + 2 : i + 1 : i;
			boolean disabled = !SKILL_ENABLED[i];
			if(!disabled) {
				if(client.leftClickInRegion(beginX + 8 + x, beginY + offset, beginX + 125 + x, beginY + offset + 50)) {
					client.outBuffer.putOpcode(185);
					client.outBuffer.putShort(skill + 5);
					return true;
				}
			}
			offset += i % 4 == 3 ? 55 : 0;
		}

		return false;
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
		fancyFont.drawLeftAlignedEffectString("Skilling Teleport", beginX + 20, beginY + 31, 0xF3B13F, true);

		Rasterizer2D.setClip(beginX + 5, beginY + 40, beginX + 493, beginY + 330);
		int offset = 45;
		for(int i = 0; i < SKILL_NAME.length; i++) {
			int x = i % 4 * 121;
			int image = i >= 1 ? i > 11 ? i + 2 : i + 1 : i;
			boolean disabled = !SKILL_ENABLED[i];
			Rasterizer2D.fillRoundedRectangle(beginX + 8 + x, beginY + offset, 117, 50, 3, disabled ? 0x460000 : 0x000000, 100);
			if(!disabled && client.mouseInRegion(beginX + 8 + x, beginY + offset, beginX + 125 + x, beginY + offset + 50)) {
				Rasterizer2D.fillRectangle(beginX + 8 + x, beginY + offset, 117, 50, 0, 40);
			}
			ImageCache.get(124 + image + 5).drawImage(beginX + 49 + x, beginY + offset);
			smallFont.drawCenteredEffectString(SKILL_NAME[i], beginX + 66 + x, beginY + offset + 46, 0xF3B13F, true);
			offset += i % 4 == 3 ? 55 : 0;
		}

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
		return 4;
	}
	
	@Override
	public boolean blockedMove() {
		return false;
	}
	
}
