package net.edge.activity.panel.impl;

import net.edge.activity.panel.Panel;
import net.edge.cache.unit.ImageCache;
import net.edge.game.Scene;
import net.edge.media.Rasterizer2D;

public class MonsterPanel extends Panel {
	
	
	private final String DUNGEONS[] = {"Rock crabs", "Taverly", "Brimhaven", "Edgeville", "Elven Camp", "Slayer tower", "Fremennik S.", "Ancient cavern", "Chaos Dwarf B.", "Glacors"};
	
	private final boolean DISABLED[] = {false, false, false, false, false, false, false, false, false, false};
	
	private final int IMAGES[] = {1010, 1011, 1012, 1834, 1884, 1835, 1836, 1837, 1910, 1920};

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


		/* Clicking a monster */
		int offset = 45;
		for(int i = 0; i < DUNGEONS.length; i++) {
			int x = i % 6 * 81;
			if(client.leftClickInRegion(beginX + 8 + x, beginY + offset, beginX + 85 + x, beginY + offset + 50)) {
				if(!DISABLED[i]) {
					client.outBuffer.putOpcode(185);
					client.outBuffer.putShort(i + 70);
					return true;
				}
			}
			offset += i % 6 == 5 ? 55 : 0;
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

		fancyFont.drawLeftAlignedEffectString("Monster teleport", beginX + 20, beginY + 28, 0xF3B13F, true);

		/* Skills */
		Rasterizer2D.drawRectangle(beginX + 4, beginY + 39, 490, 292, 0xffffff, 80);
		Rasterizer2D.fillRectangle(beginX + 5, beginY + 40, 488, 290, 0xffffff, 60);
		Rasterizer2D.setClip(beginX + 5, beginY + 40, beginX + 493, beginY + 330);
		int offset = 45;
		
		/* Dungeons */
		for(int i = 0; i < DUNGEONS.length; i++) {
			int x = i % 6 * 81;
			if(!DISABLED[i]) {
				ImageCache.get(IMAGES[i]).drawAlphaImage(beginX + 8 + x, beginY + offset);
				if(client.mouseInRegion(beginX + 8 + x, beginY + offset, beginX + 85 + x, beginY + offset + 50))
					Rasterizer2D.fillRectangle(beginX + 8 + x, beginY + offset, 77, 50, 0, 40);
			}
			Rasterizer2D.drawRectangle(beginX + 8 + x, beginY + offset, 77, 50, 0, 200);
			smallFont.drawCenteredEffectString(DUNGEONS[i], beginX + 46 + x, beginY + offset + 46, 0xF3B13F, true);
			if(DISABLED[i])
				Rasterizer2D.fillRectangle(beginX + 8 + x, beginY + offset, 77, 50, 0x000000, 150);
			offset += i % 6 == 5 ? 55 : 0;
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
		return 5;
	}
	
	@Override
	public boolean blockedMove() {
		return false;
	}
	
}
