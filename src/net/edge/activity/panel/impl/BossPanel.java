package net.edge.activity.panel.impl;

import net.edge.activity.panel.Panel;
import net.edge.cache.unit.ImageCache;
import net.edge.game.Scene;
import net.edge.media.Rasterizer2D;
import net.edge.util.string.StringUtils;

public class BossPanel extends Panel {

	private enum Boss {
		PHOENIX(false, 84, 1709),
		SKELETAL_HORROR(false, 91, 1710),
		SEA_TROLL_QUEEN(false, 91, 1711),
		BORK(false, 107, 1712),
		TORMENTED_DEMON(true, 119, 1713),
		GIANT_MOLE(false, 230, 1714),
		KING_BLACK_DRAG(true, 276, 1715),
		CHOAS_ELE(false, 305, 1716),
		KALPHITE_QUEEN(true, 333, 1717),
		WILDYWYRM(true, 382, 1718),
		NOMAD(true, 699, 1719),
		JAD(true, 702, 1720),
		CORP_BEAST(true, 785, 1721),
		NEX(false, 1001, 1722),
		GOD_WARS(true, 0, 1723),
		DAGANNOTH(true, 0, 1724);

		int level, img;
		boolean enabled;
		String name;

		Boss(boolean enabled, int level, int img) {
			this.enabled = enabled;
			this.img = img;
			this.level = level;
			this.name = StringUtils.formatName(name().toLowerCase());
		}
	}

	private Boss[] bosses = Boss.values();

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


		/* Clicking a boss */
		int offset = 45;
		for(int i = 0; i < bosses.length; i++) {
			int x = i % 6 * 81;
			if(bosses[i].enabled && client.leftClickInRegion(beginX + 12 + x, beginY + offset, beginX + 82 + x, beginY + offset + 70)) {
				client.outBuffer.putOpcode(185);
				client.outBuffer.putShort(i + 50);
				return true;
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
		Rasterizer2D.fillRectangle(beginX, beginY + 8, 500, 328, 0x000000, 200);
		Rasterizer2D.drawRectangle(beginX, beginY + 8, 500, 328, 0x63625e);

		fancyFont.drawCenteredString("Exit", beginX + 467, beginY + 27, 0xF3B13F);
		Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 20, 2, 0xF3B13F, 60);
		if(client.mouseInRegion(beginX + 442, beginY + 12, beginX + 498, beginY + 42)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 20, 2, 0xF3B13F, 20);
		}

		fancyFont.drawLeftAlignedEffectString("Bosses teleport", beginX + 20, beginY + 28, 0xF3B13F, true);

		/* Bosses */
		Rasterizer2D.drawRectangle(beginX + 4, beginY + 39, 490, 292, 0xffffff, 80);
		Rasterizer2D.fillRectangle(beginX + 5, beginY + 40, 488, 290, 0xffffff, 60);
		Rasterizer2D.setClip(beginX + 5, beginY + 40, beginX + 493, beginY + 330);
		int offset = 45;
		for(int i = 0; i < bosses.length; i++) {
			int x = i % 6 * 81;
			ImageCache.get(bosses[i].img).drawImage(beginX + 12 + x, beginY + offset);
			if(!bosses[i].enabled) {
				Rasterizer2D.fillRectangle(beginX + 12 + x, beginY + offset, 70, 70, 0, 200);
			} else if(client.mouseInRegion(beginX + 12 + x, beginY + offset, beginX + 82 + x, beginY + offset + 70)) {
				Rasterizer2D.fillRectangle(beginX + 12 + x, beginY + offset, 70, 70, 0, 40);
			}
			smallFont.drawCenteredEffectString(bosses[i].name, beginX + 46 + x, beginY + offset + 82, 0xF3B13F, true);
			smallFont.drawCenteredEffectString(bosses[i].level == 0 ? "" : "lvl " + bosses[i].level, beginX + 46 + x, beginY + offset + 92, 0xf95454, true);
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
		return 8;
	}
	
	@Override
	public boolean blockedMove() {
		return false;
	}
}
