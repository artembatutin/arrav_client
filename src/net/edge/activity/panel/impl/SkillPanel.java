package net.edge.activity.panel.impl;

import net.edge.activity.panel.Panel;
import net.edge.cache.unit.ImageCache;
import net.edge.game.Scene;
import net.edge.media.Rasterizer2D;

public class SkillPanel extends Panel {
	
	public static final String[] SKILL_NAME = {"Prayer", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Farming", "Runecrafting", "Construction", "Hunter", "Summoning", "Dungeoneering"};
	public static final boolean[] SKILL_ENABLED = {
			false,//Prayer
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
			false,//Farming
			true,//Runecrafting
			false,//Hunter
			true,//Construction
			false,//Summoning
			false//Dungeoneering
	};

	@Override
	public String id() {
		return "skill";
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

        /* Exit */
		if(client.leftClickInRegion(beginX + 442, beginY + 12, beginX + 498, beginY + 42)) {
			client.panelHandler.close();
			Scene.hoverX = -1;
			return true;
		}


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
		Rasterizer2D.fillRoundedRectangle(beginX, beginY + 8, 500, 328, 4, 0x000000, 200);

		fancyFont.drawCenteredString("Exit", beginX + 467, beginY + 27, 0xF3B13F);
		Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 20, 2, 0xF3B13F, 60);
		if(client.mouseInRegion(beginX + 442, beginY + 12, beginX + 498, beginY + 42)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 20, 2, 0xF3B13F, 20);
		}

		fancyFont.drawLeftAlignedEffectString("Skilling teleport", beginX + 20, beginY + 28, 0xF3B13F, true);

		/* Skills */
		Rasterizer2D.drawRectangle(beginX + 4, beginY + 39, 490, 292, 0xffffff, 80);
		Rasterizer2D.fillRectangle(beginX + 5, beginY + 40, 488, 290, 0xffffff, 60);
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
			ImageCache.get(124 + image + 5).drawAlphaImage(beginX + 49 + x, beginY + offset);
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
}
