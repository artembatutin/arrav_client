package net.arrav.activity.panel.impl;

import net.arrav.Client;
import net.arrav.activity.panel.Panel;
import net.arrav.graphic.Rasterizer2D;

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
		if(processClose(beginX, beginY)) {
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
		drawMain(beginX, beginY + 8, 500, 328, 0x000000, 0x63625e, 200);
		drawOver(beginX, beginY);
		drawClose(beginX, beginY);

		fancyFont.drawLeftAlignedEffectString("Dungeon Teleport", beginX + 20, beginY + 31, 0xFF8A1F, 0);
		smallFont.drawLeftAlignedString("You can right click \"More info\" to check drops on monsters", beginX + 150, beginY + 25, 0xFF8A1F);
		smallFont.drawLeftAlignedString("or access the Monster database from the quest tab.", beginX + 150, beginY + 36, 0xFF8A1F);
		Rasterizer2D.setClip(beginX + 5, beginY + 40, beginX + 493, beginY + 330);
		int offset = 45;
		/* Dungeons */
		for(int i = 0; i < DUNGEONS.length; i++) {
			int x = i % 6 * 81;
			if(!DISABLED[i]) {
				Client.spriteCache.get(IMAGES[i]).drawImage(beginX + 8 + x, beginY + offset);
				if(client.mouseInRegion(beginX + 8 + x, beginY + offset, beginX + 85 + x, beginY + offset + 50))
					Rasterizer2D.fillRectangle(beginX + 8 + x, beginY + offset, 77, 50, 0, 40);
			}
			Rasterizer2D.drawRectangle(beginX + 8 + x, beginY + offset, 77, 50, 0, 200);
			smallFont.drawCenteredEffectString(DUNGEONS[i], beginX + 46 + x, beginY + offset + 46, 0xFF8A1F, 0);
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
