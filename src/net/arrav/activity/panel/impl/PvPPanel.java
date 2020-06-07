package net.arrav.activity.panel.impl;

import net.arrav.Client;
import net.arrav.Config;
import net.arrav.activity.panel.Panel;
import net.arrav.cache.unit.Interface;
import net.arrav.graphic.Rasterizer2D;

public class PvPPanel extends Panel {
	
	private enum Portal {
		
		FORTRESS("Fortress", 40, 225),
		OBELISK("Obelisk 50", 236, 38),
		TEMPLE("Temple", 192, 238),
		CEMETRY("Cemetry", 30, 145),
		OBELISK2("Obelisk 44", 18, 77),
		WILDYWYRM("Wildywyrm", 151, 70),
		;
		
		private final int y;
		private final int x;
		private final String name;
		
		Portal(String name, int x, int y) {
			this.name = name;
			this.x = x;
			this.y = y;
		}
	}
	
	private final Portal[] portals = Portal.values();
	
	public static int[] xCoords;
	public static int[] yCoords;
	
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


		/* Clicking a teleport */
		int i = 0;
		for(Portal p : portals) {
			if(client.leftClickInRegion(beginX + 190 + p.x, beginY + 27 + p.y, beginX + 210 + p.x, beginY + 57 + p.y)) {
				client.outBuffer.putOpcode(185);
				client.outBuffer.putShort(i + 420);
				return true;
			}
			i++;
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

		fancyFont.drawLeftAlignedEffectString("Wilderness Activity", beginX + 20, beginY + 31, 0xFF8A1F, 0);
		
		if(Config.def.panelStyle == 2) {
			Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 12, 54, 25, 2, 0xFF8A1F, 60);
			if(client.mouseInRegion(beginX + 382, beginY + 22, beginX + 438, beginY + 47)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 12, 54, 25, 2, 0xFF8A1F, 20);
			}
			fancyFont.drawCenteredString("Presets", beginX + 407, beginY + 30, 0xFF8A1F);
		} else {
			Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 17, 54, 20, 2, 0x000000, 60);
			if(client.mouseInRegion(beginX + 382, beginY + 22, beginX + 438, beginY + 47)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 17, 54, 20, 2, 0x000000, 60);
			}
			fancyFont.drawCenteredString("Stats", beginX + 407, beginY + 32, 0xFF8A1F);
		}
		smallFont.drawLeftAlignedString("Don't get fooled by certain hot spots.", beginX + 170, beginY + 33, 0xffffff);
		plainFont.drawLeftAlignedEffectString(Interface.cache[16038].text, beginX + 10, beginY + 60, 0xffffff, 0);
		
		Rasterizer2D.drawRectangle(beginX + 10, beginY + 70, 170, 170, 0x000000);
		Rasterizer2D.drawHorizontalLine(beginX + 10, beginY + 90, 170, 0x000000);
		Rasterizer2D.fillRectangle(beginX + 10, beginY + 70, 170, 170, 0x000000, 70);
		plainFont.drawLeftAlignedString("Top Bounties:", beginX + 57, beginY + 84, 0xffffff);
		
		smallFont.drawLeftAlignedString("Purple parts of the map represent", beginX + 10, beginY + 255, 0xffffff);
		smallFont.drawLeftAlignedString("player activity. Donators can view", beginX + 10, beginY + 270, 0xffffff);
		smallFont.drawLeftAlignedString("more precise locations", beginX + 10, beginY + 285, 0xffffff);
		
		Rasterizer2D.fillCircle(beginX + 17, beginY + 301, 7, 0xbea99a);
		smallFont.drawLeftAlignedString("Teleport to position", beginX + 30, beginY + 307, 0xffffff);
		Rasterizer2D.fillRectangle(beginX + 10, beginY + 312, 15, 13, 0x871915);
		smallFont.drawLeftAlignedString("Multi area combat", beginX + 30, beginY + 325, 0xffffff);
		Rasterizer2D.setClip(beginX + 5, beginY + 40, beginX + 493, beginY + 330);
		
		Client.spriteCache.get(1949).drawImage(beginX + 200, beginY + 37);
		if(xCoords != null) {
			for(int i = 0; i < xCoords.length; i++) {
				for(int x = 0; x < 12; x++) {
					for(int y = 0; y < 12; y++) {
						Rasterizer2D.drawPoint(beginX + 200 + (xCoords[i] * 2) + y, beginY + 37 - (yCoords[i] * 2) + x + 295, 0x9600ff, (12 - x) * (12 - y));
						Rasterizer2D.drawPoint(beginX + 199 + (xCoords[i] * 2) - y, beginY + 37 - (yCoords[i] * 2) + x + 295, 0x9600ff, (12 - x) * (12 - y));
						Rasterizer2D.drawPoint(beginX + 200 + (xCoords[i] * 2) + y, beginY + 36 - (yCoords[i] * 2) - x + 295, 0x9600ff, (12 - x) * (12 - y));
						Rasterizer2D.drawPoint(beginX + 199 + (xCoords[i] * 2) - y, beginY + 36 - (yCoords[i] * 2) - x + 295, 0x9600ff, (12 - x) * (12 - y));
					}
				}
				//Rasterizer2D.fillCircle(beginX + 200 + xCoords[i], beginY + 37 + yCoords[i], 6, 0xffcc00, 200);
			}
		}
		for(Portal p : portals) {
			Rasterizer2D.fillCircle(beginX + 200 + p.x, beginY + 37 + p.y, 6, 0xffffff, 100);
			if(client.mouseInRegion(beginX + 190 + p.x, beginY + 27 + p.y, beginX + 210 + p.x, beginY + 57 + p.y)) {
				Rasterizer2D.fillCircle(beginX + 200 + p.x, beginY + 37 + p.y, 6, 0xffffff, 100);
			}
			smallFont.drawCenteredString(p.name, beginX + 200 + p.x, beginY + 54 + p.y, 0xffffff);
		}
		
		Rasterizer2D.drawHorizontalLine(beginX + 202, beginY + 77, 270, 0xffffff, 80);
		smallFont.drawLeftAlignedString("" + 50, beginX + 477, beginY + 82, 0x7e7e7e);
		Rasterizer2D.drawHorizontalLine(beginX + 202, beginY + 127, 270, 0xffffff, 80);
		smallFont.drawLeftAlignedString("" + 40, beginX + 477, beginY + 132, 0x7e7e7e);
		Rasterizer2D.drawHorizontalLine(beginX + 202, beginY + 177, 270, 0xffffff, 80);
		smallFont.drawLeftAlignedString("" + 30, beginX + 477, beginY + 182, 0x7e7e7e);
		Rasterizer2D.drawHorizontalLine(beginX + 202, beginY + 235, 270, 0xf1736f, 140);
		smallFont.drawLeftAlignedString("" + 20, beginX + 478, beginY + 240, 0xf1736f);
		Rasterizer2D.drawHorizontalLine(beginX + 202, beginY + 285, 270, 0xffffff, 80);
		smallFont.drawLeftAlignedString("" + 10, beginX + 478, beginY + 290, 0x7e7e7e);
		smallFont.drawLeftAlignedString("Arrav", beginX + 265, beginY + 327, 0xffffff);
		Rasterizer2D.removeClip();
		
		if(client.killstreak == null) {
			client.scoreNames = new String[20];
			client.killstreak = new int[20];
			client.scoreDeaths = new int[20];
			client.scoreKills = new int[20];
		}
		int offset = 67;
		for(int i = 0; i < client.killstreak.length - 10; i++) {
			offset += 15;
			smallFont.drawLeftAlignedEffectString("" + (i + 1), beginX + 22, beginY + offset + 19, 0xFF8A1F, 0);
			if(client.scoreNames[i] != null) {
				smallFont.drawLeftAlignedEffectString(client.scoreNames[i], beginX + 50, beginY + offset + 19, 0xFF8A1F, 0);
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
		return 5;
	}
	
	@Override
	public boolean blockedMove() {
		return false;
	}
	
}
