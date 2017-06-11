package net.edge.activity.panel.impl;

import net.edge.activity.panel.Panel;
import net.edge.cache.unit.ImageCache;
import net.edge.game.Scene;
import net.edge.media.Rasterizer2D;
import net.edge.util.string.StringUtils;

public class MinigamePanel extends Panel {

	private enum Minigames {
		BARB_ASSAULT(false, 1683),
		BARROWS(true, 1684),
		CASTLE_WARS(false, 1685),
		CLAN_WARS(false, 1686),
		DUEL_ARENA(true, 1687),
		FIST_OF_GUTHIX(false, 1688),
		MAGE_ARENA(false, 1689),
		PEST_CONTROL(false, 1690),
		SOUL_WARS(false, 1691),
		FIGHT_PIT(false, 1692),
		WARRIORS_GUILD(true, 1911);

		int img;
		boolean enabled;
		String name;

		Minigames(boolean enabled, int img) {
			this.enabled = enabled;
			this.img = img;
			this.name = StringUtils.formatName(name().toLowerCase());
		}
	}

	private Minigames[] mgs = Minigames.values();

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


		/* Clicking a minigame */
		int offset = 45;
		for(int i = 0; i < mgs.length; i++) {
			int x = i % 6 * 81;
			if(mgs[i].enabled && client.leftClickInRegion(beginX + 12 + x, beginY + offset, beginX + 82 + x, beginY + offset + 70)) {
				client.outBuffer.putOpcode(185);
				client.outBuffer.putShort(i + 30);
			}
			offset += i % 6 == 5 ? 95 : 0;
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

		fancyFont.drawLeftAlignedEffectString("Minigame teleport", beginX + 20, beginY + 28, 0xF3B13F, true);

		/* Minigames */
		Rasterizer2D.drawRectangle(beginX + 4, beginY + 39, 490, 292, 0xffffff, 80);
		Rasterizer2D.fillRectangle(beginX + 5, beginY + 40, 488, 290, 0xffffff, 60);
		Rasterizer2D.setClip(beginX + 5, beginY + 40, beginX + 493, beginY + 330);
		int offset = 45;
		for(int i = 0; i < mgs.length; i++) {
			int x = i % 6 * 81;
			ImageCache.get(mgs[i].img).drawImage(beginX + 12 + x, beginY + offset);
			if(!mgs[i].enabled) {
				Rasterizer2D.fillRectangle(beginX + 12 + x, beginY + offset, 70, 70, 0, 200);
			} else if(client.mouseInRegion(beginX + 12 + x, beginY + offset, beginX + 82 + x, beginY + offset + 70)) {
				Rasterizer2D.fillRectangle(beginX + 12 + x, beginY + offset, 70, 70, 0, 40);
			}
			smallFont.drawCenteredEffectString(mgs[i].name, beginX + 46 + x, beginY + offset + 82, 0xF3B13F, true);
			offset += i % 6 == 5 ? 95 : 0;
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
		return 7;
	}
	
	@Override
	public boolean blockedMove() {
		return false;
	}
	
}
