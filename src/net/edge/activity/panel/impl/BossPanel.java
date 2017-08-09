package net.edge.activity.panel.impl;

import net.edge.activity.panel.Panel;
import net.edge.cache.unit.ImageCache;
import net.edge.media.Rasterizer2D;
import net.edge.util.string.StringUtils;

public class BossPanel extends Panel {

	private enum Boss {
		PHOENIX(235, 1709),
		SKELETAL_HORROR(320, 1710),
		SEA_TROLL_QUEEN(91, 1711),
		BORK(267, 1712),
		TORMENTED_DEMON(119, 1713),
		GIANT_MOLE(230, 1714),
		KING_BLACK_DRAG(276, 1715),
		CHAOS_ELE(305, 1716),
		KALPHITE_QUEEN(333, 1717),
		WILDYWYRM(382, 1718),
		NOMAD(699, 1719),
		JAD(702, 1720),
		CORP_BEAST(785, 1721),
		NEX(1001, 1722),
		GOD_WARS(0, 1723),
		DAGANNOTH(0, 1724);

		int level, img;
		String name;

		Boss(int level, int img) {
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
		if(processClose(beginX, beginY)) {
			return true;
		}


		/* Clicking a boss */
		int offset = 45;
		for(int i = 0; i < bosses.length; i++) {
			int x = i % 6 * 81;
			if(client.leftClickInRegion(beginX + 12 + x, beginY + offset, beginX + 82 + x, beginY + offset + 70)) {
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
		drawMain(beginX, beginY + 8, 500, 328, 0x000000, 0x63625e, 200);
		drawOver(beginX, beginY);
		drawClose(beginX, beginY);

		fancyFont.drawLeftAlignedEffectString("Bosses teleport", beginX + 20, beginY + 31, 0xF3B13F, true);

		Rasterizer2D.setClip(beginX + 5, beginY + 40, beginX + 493, beginY + 330);
		int offset = 45;
		for(int i = 0; i < bosses.length; i++) {
			int x = i % 6 * 81;
			ImageCache.get(bosses[i].img).drawImage(beginX + 12 + x, beginY + offset);
			if(client.mouseInRegion(beginX + 12 + x, beginY + offset, beginX + 82 + x, beginY + offset + 70)) {
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
