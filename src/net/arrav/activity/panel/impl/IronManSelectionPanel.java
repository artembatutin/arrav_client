package net.arrav.activity.panel.impl;

import net.arrav.Config;
import net.arrav.activity.panel.Panel;
import net.arrav.cache.unit.ImageCache;
import net.arrav.cache.unit.ObjectType;
import net.arrav.graphic.Rasterizer2D;
import net.arrav.graphic.img.BitmapImage;

public class IronManSelectionPanel extends Panel {
	
	private int[] itemsReg = {995, 19000, 362, 386, 1351, 590, 1265, 1323, 1333, 841, 1379, 3105, 1153, 1115, 1067, 1191, 579, 577, 1011, 4405, 884, 554, 555, 556, 557, 558, 560, 562};
	private String[] itemsRegAm = {"5000k", "50", "400", "200", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "500", "250", "250", "250", "250", "250", "150", "250" };
	
	private int[] itemsIron = {995, 1351, 590, 1265, 946, 1755, 2347, 303, 18741};
	
	@Override
	public boolean process() {
	    /* Initialization */
		int beginX = 4;
		int beginY = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			beginX = client.windowWidth / 2 - 380;
			beginY = client.windowHeight / 2 - 250;
		}
		
		//iron man
		if(client.leftClickInRegion(beginX + 370, beginY + 120, beginX + 470, beginY + 150)) {
			client.outBuffer.putOpcode(185);
			client.outBuffer.putShort(200);
			return true;
		}
		//regular
		if(client.leftClickInRegion(beginX + 190, beginY + 270, beginX + 290, beginY + 300)) {
			client.outBuffer.putOpcode(185);
			client.outBuffer.putShort(201);
			return true;
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
		//drawOver(beginX, beginY);
		
		fancyFont.drawLeftAlignedEffectString("Arrav mode selection", beginX + 180, beginY + 31, 0xFF8A1F, true);
		Rasterizer2D.fillRoundedRectangle(beginX + 20, beginY + 40, 470, 160, 5, Config.def.panelStyle == 2 ? 0xcf9d47 : 0x000000, Config.def.panelStyle == 2 ? 30 : 100);
		if(client.mouseInRegion(beginX + 190, beginY + 230, beginX + 285, beginY + 260)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 190, beginY + 230, 100, 30, 2, Config.def.panelStyle == 2 ? 0xcf9d47 : 0x000000, Config.def.panelStyle == 2 ? 60 : 100);
			Rasterizer2D.fillRectangle(beginX + 20, beginY + 200, 310, 10, Config.def.panelStyle == 2 ? 0xcf9d47 : 0x000000, Config.def.panelStyle == 2 ? 30 : 100);
			Rasterizer2D.fillRectangle(beginX + 330, beginY + 40, 5, 160, 0x000000, 100);
			int offset = 58;
			for(int i = 0; i < itemsReg.length; i++) {
				int icon = itemsReg[i];
				int x = i % 7 * 44;
				final BitmapImage img = ObjectType.getIcon(icon, icon == 19000 ? 50 : 10000, 0);
				if(img != null) {
					img.drawImage(beginX + 28 + x, beginY + offset + 2);
				}
				if(itemsRegAm[i].length() > 0)
					smallFont.drawLeftAlignedEffectString(itemsRegAm[i], beginX + 30 + x, beginY + offset + 11, 0xFFFFFF, true);
				offset += i % 7 == 6 ? 35 : 0;
			}
		} else if(client.mouseInRegion(beginX + 370, beginY + 80, beginX + 470, beginY + 110)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 370, beginY + 80, 100, 30, 2, Config.def.panelStyle == 2 ? 0xcf9d47 : 0x000000, Config.def.panelStyle == 2 ? 30 : 100);
			int offset = 48;
			for(int i = 0; i < itemsIron.length; i++) {
				int icon = itemsIron[i];
				int x = i % 5 * 68;
				final BitmapImage img = ObjectType.getIcon(icon, icon == 19000 ? 50 : 10000, 0);
				if(img != null) {
					img.drawImage(beginX + 34 + x, beginY + offset + 2);
				}
				if(icon == 995)
					smallFont.drawLeftAlignedEffectString("200k", beginX + 32 + x, beginY + offset + 11, 0xFFFFFF, true);
				offset += i % 5 == 4 ? 45 : 0;
			}
		} else {
			ImageCache.get(1929).drawImage(beginX + 45, beginY + 70);
			boldFont.drawCenteredEffectString("Iron man", beginX + 85, beginY + 60, 0xFF8A1F, true);
			smallFont.drawLeftAlignedString("Tougher monsters statistics.", beginX + 145, beginY + 60, 0xFF8A1F);
			smallFont.drawLeftAlignedString("Lose 25% of experience on death.", beginX + 145, beginY + 75, 0xFF8A1F);
			smallFont.drawLeftAlignedString("Can only trade iron man members.", beginX + 145, beginY + 90, 0xFF8A1F);
			smallFont.drawLeftAlignedString("Can't search items in global market.", beginX + 145, beginY + 105, 0xFF8A1F);
			smallFont.drawLeftAlignedString("When maxed out:", beginX + 145, beginY + 125, 0x7ff33f);
			smallFont.drawLeftAlignedString("Unlimited run outside of wilderness.", beginX + 145, beginY + 140, 0xFF8A1F);
			smallFont.drawLeftAlignedString("Restrictions removed, no dropping skills.", beginX + 145, beginY + 155, 0xFF8A1F);
			smallFont.drawLeftAlignedString("Exclusive upgraded max out cape.", beginX + 145, beginY + 170, 0xFF8A1F);
			smallFont.drawLeftAlignedString("10% higher rare drop.", beginX + 145, beginY + 185, 0xFF8A1F);
		}
		smallFont.drawLeftAlignedString("Ironmen are marked by a", beginX + 365, beginY + 170, 0xFFFFFF);
		smallFont.drawLeftAlignedString("black dot on the minimap.", beginX + 365, beginY + 188, 0xFFFFFF);
		Rasterizer2D.fillRoundedRectangle(beginX + 370, beginY + 80, 100, 30, 2, 0xcf9d47, 60);
		Rasterizer2D.fillRoundedRectangle(beginX + 370, beginY + 120, 100, 30, 2, 0xeec557, 60);
		if(client.mouseInRegion(beginX + 370, beginY + 120, beginX + 470, beginY + 150)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 370, beginY + 120, 100, 30, 2, 0xeec557, 60);
		}
		fancyFont.drawLeftAlignedString("View starter", beginX + 380, beginY + 100, 0xFF8A1F);
		fancyFont.drawLeftAlignedString("Select", beginX + 402, beginY + 140, 0xFF8A1F);
		
		//regular
		Rasterizer2D.fillRoundedRectangle(beginX + 20, beginY + 210, 310, 110, 5, Config.def.panelStyle == 2 ? 0xcf9d47 : 0x000000, Config.def.panelStyle == 2 ? 30 : 100);
		fancyFont.drawLeftAlignedString("Regular player:", beginX + 60, beginY + 268, 0xFF8A1F);
		
		Rasterizer2D.fillRoundedRectangle(beginX + 190, beginY + 230, 100, 30, 2, 0xcf9d47, 60);
		fancyFont.drawLeftAlignedString("View starter", beginX + 200, beginY + 250, 0xFF8A1F);
		Rasterizer2D.fillRoundedRectangle(beginX + 190, beginY + 270, 100, 30, 2, 0xeec557, 60);
		fancyFont.drawLeftAlignedString("Select", beginX + 222, beginY + 290, 0xFF8A1F);
		if(client.mouseInRegion(beginX + 190, beginY + 270, beginX + 290, beginY + 300)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 190, beginY + 270, 100, 30, 2, 0xcf9d47, 60);
		}
		
		boldFont.drawLeftAlignedString("XP rates:", beginX + 390, beginY + 225, 0xFF8A1F);
		plainFont.drawLeftAlignedString("Combat: x1000", beginX + 370, beginY + 245, 0xFF8A1F);
		plainFont.drawLeftAlignedString("Prayer: x100", beginX + 379, beginY + 265, 0xFF8A1F);
		plainFont.drawLeftAlignedString("Skills: x25", beginX + 385, beginY + 285, 0xFF8A1F);
		plainFont.drawLeftAlignedString("Combat rate becomes slower", beginX + 336, beginY + 305, 0xeec557);
		plainFont.drawLeftAlignedString("after maxing skill out.", beginX + 355, beginY + 320, 0xeec557);
	}
	
	@Override
	public void initialize() {
	
	}
	
	@Override
	public void reset() {
	}
	
	@Override
	public int getId() {
		return 14;
	}
	
	@Override
	public boolean blockedMove() {
		return true;
	}
	
}