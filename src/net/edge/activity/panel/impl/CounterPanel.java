package net.edge.activity.panel.impl;

import net.edge.activity.panel.Panel;
import net.edge.cache.unit.ObjectType;
import net.edge.game.Scene;
import net.edge.media.Rasterizer2D;
import net.edge.media.img.BitmapImage;

public class CounterPanel extends Panel {
	
	/**
	 * The shops array
	 */
	final static Shop[] shops = {
			new Shop("General", 12, 1935, 1931, 7156),
			new Shop("Ranged Store", 13, 9143, 9183, 1135),
			new Shop("Magic Store", 14, 564, 560, 565),
			new Shop("Weaponry", 15, 4587, 3101, 1305),
			new Shop("Defence Store", 16, 1201, 1127, 4131),
			new Shop("Supply Shop", 8, 15272, 385, 2442),
			new Shop("Blood Money Store", 0, 14484, 15241, 13744),
			new Shop("Edge Tokens Store", 1, 19010, 19012, 691),
			new Shop("Voting shop", 27, 10551, 6737, 20072),
			new Shop("Strongman's shop", 17, 1434, 3204, 1377)
	};
	
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
	    /* Initialization */
		int beginX = 4;
		int beginY = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			beginX = client.windowWidth / 2 - 380;
			beginY = client.windowHeight / 2 - 250;
		}
		
		int max1 = 43 * shops.length;
		int max2 = (32 * shops.length);
		scrollMax = Math.max((max1 > max2 ? max1 : max2) - 185, 0);

        /* Scrolling */
		if(client.mouseInRegion(beginX + 5, beginY + 150, beginX + 493, beginY + 365)) {
			scrollPos += client.mouseWheelAmt * 24;
			if(scrollPos < 0) {
				scrollPos = 0;
			}
			if(scrollPos > scrollMax) {
				scrollPos = scrollMax;
			}
		}
		if(!scrollDrag) {
			int height = 168;
			if(scrollMax > 0) {
				height = 175 * 168 / (scrollMax + 175);
			}
			int pos = 0;
			if(scrollPos != 0) {
				pos = scrollPos * 168 / (scrollMax + 175) + 1;
			}
			int x = 485;
			int y = 146 + pos;
			if(client.mouseDragButton == 1 && client.mouseInRegion(x, y, x + 20, y + height)) {
				scrollDrag = true;
				scrollDragPos = scrollPos;
			}
		} else if(client.mouseDragButton != 1) {
			scrollDrag = false;
		} else {
			int d = (client.mouseY - client.clickY) * (scrollMax + 175) / 168;
			scrollPos = scrollDragPos + d;
			if(scrollPos < 0) {
				scrollPos = 0;
			}
			if(scrollPos > scrollMax) {
				scrollPos = scrollMax;
			}
		}
		

        /* Exit */
		if(client.leftClickInRegion(beginX + 442, beginY + 12, beginX + 498, beginY + 42)) {
			client.panelHandler.close();
			client.outBuffer.putOpcode(185);
			client.outBuffer.putShort(123);
			Scene.hoverX = -1;
			return true;
		}
		
		/* Item search
		if(client.leftClickInRegion(beginX + 10, beginY + 50, beginX + 494, beginY + 80)) {
			client.marketSearch = true;
			client.panelSearch = true;
			client.npcSug = false;
			client.promptInput = "";
			client.panelSearchInput = "";
			client.promptInputTitle = "What item are you looking for?";
			client.messagePromptRaised = true;
		} else {*/
			int offset = -scrollPos + 115;
			int i = 0;
			for(Shop s : shops) {
				int x = i % 2 * 235;
				if(client.leftClickInRegion(beginX + 8 + x, beginY + offset, beginX + 232 + x, beginY + 30 + offset)) {
					client.outBuffer.putOpcode(20);
					client.outBuffer.putShort(s.getId());
					return true;
				}
				offset += i % 2 == 1 ? 41 : 0;
				i++;
			}
		//}
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
		
		fancyFont.drawLeftAlignedEffectString("Shop Counter", beginX + 20, beginY + 33, 0xF3B13F, true);
		boldFont.drawCenteredEffectString("Quick shops:", beginX + 240, beginY + 100, 0xF3B13F, true);
		
		fancyFont.drawCenteredString("Exit", beginX + 467, beginY + 30, 0xF3B13F);
		Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 25, 2, 0xF3B13F, 60);
		if(client.mouseInRegion(beginX + 442, beginY + 12, beginX + 498, beginY + 47)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 25, 2, 0xF3B13F, 30);
		}
		
		if(true) {
			fancyFont.drawCenteredEffectString("Player owned shops coming soon", beginX + 240, beginY + 70, 0xF3B13F, true);
		} else {
			
			fancyFont.drawCenteredEffectString("Search item", beginX + 80, beginY + 70, 0xF3B13F, true);
			Rasterizer2D.fillRoundedRectangle(beginX + 10, beginY + 50, 130, 30, 2, 0xcf9d47, 60);
			if(client.mouseInRegion(beginX + 10, beginY + 50, beginX + 140, beginY + 80)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 10, beginY + 50, 130, 30, 2, 0xcf9d47, 30);
			}
			
			fancyFont.drawCenteredEffectString("Search player shop", beginX + 240, beginY + 70, 0xF3B13F, true);
			Rasterizer2D.fillRoundedRectangle(beginX + 150, beginY + 50, 180, 30, 2, 0xcf9d47, 60);
			if(client.mouseInRegion(beginX + 150, beginY + 50, beginX + 330, beginY + 80)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 150, beginY + 50, 180, 30, 2, 0xcf9d47, 30);
			}
			
			fancyFont.drawCenteredEffectString("Edit your shop", beginX + 417, beginY + 70, 0xF3B13F, true);
			Rasterizer2D.fillRoundedRectangle(beginX + 340, beginY + 50, 150, 30, 2, 0xcf9d47, 60);
			if(client.mouseInRegion(beginX + 340, beginY + 50, beginX + 490, beginY + 80)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 340, beginY + 50, 150, 30, 2, 0xcf9d47, 30);
			}
		}
		
		/* content */
		Rasterizer2D.drawRectangle(beginX + 4, beginY + 109, 490, 222, 0xffffff, 80);
		Rasterizer2D.fillRectangle(beginX + 5, beginY + 110, 488, 220, 0xffffff, 60);
		Rasterizer2D.setClip(beginX + 5, beginY + 110, beginX + 493, beginY + 370);
		int offset = -scrollPos + 115;
		
		int i = 0;
		for(Shop s : shops) {
			int x = i % 2 * 235;
			Rasterizer2D.fillRectangle(beginX + 8 + x, beginY + offset, 230, 40, 0x0000, 100);
			if(client.mouseInRegion(beginX + 8 + x, beginY + offset, beginX + 232 + x, beginY + 30 + offset)) {
				Rasterizer2D.drawRectangle(beginX + 8 + x, beginY + offset, 230, 41, 0xffffff);
			}
			if(s.getIcons()[0] != 1) {
				final BitmapImage img1 = ObjectType.getIcon(s.getIcons()[0], 500, 0);
				if(img1 != null)
					img1.drawImage(beginX + 10 + x, beginY + offset + 2);
			}
			if(s.getIcons()[2] != 1) {
				final BitmapImage img2 = ObjectType.getIcon(s.getIcons()[2], 500, 0);
				if(img2 != null)
					img2.drawImage(beginX + 55 + x, beginY + offset + 2);
			}
			if(s.getIcons()[1] != 1) {
				final BitmapImage img3 = ObjectType.getIcon(s.getIcons()[1], 500, 0);
				if(img3 != null)
					img3.drawImage(beginX + 35 + x, beginY + offset + 8);
			}
			fancyFont.drawLeftAlignedString(s.getName(), beginX + 90 + x, beginY + offset + 24, 0xffffff);
			//plainFont.drawLeftAlignedString("Combat: " + npc.combatLevel, beginX + 320, beginY + offset + 17, 0xffffff);
			//smallFont.drawLeftAlignedString("Id: " + npc.id, beginX + 410, beginY + offset + 17, 0xffffff);
			offset += i % 2 == 1 ? 41 : 0;
			i++;
		}

		/* Scroll bar */
		Rasterizer2D.drawRectangle(476 + beginX, 115 + beginY, 12, 210, 0xffffff, 60);
		int height = 208;
		if(scrollMax > 0) {
			height = 215 * 208 / (scrollMax + 215);
		}
		int pos = 0;
		if(scrollPos != 0) {
			pos = scrollPos * 208 / (scrollMax + 215) + 1;
		}
		Rasterizer2D.fillRectangle(477 + beginX, 116 + pos + beginY, 10, height, 0x222222, 120);
		Rasterizer2D.removeClip();
		
	}
	
	@Override
	public void initialize() {
		client.marketSearch = false;
	}
	
	@Override
	public void reset() {
		client.marketSearch = false;
	}
	
	@Override
	public int getId() {
		return 9;
	}
	
	@Override
	public boolean blockedMove() {
		return false;
	}
	/**
	 * Represents a shop on this panel.
	 */
	public static class Shop {
		
		private final String name;
		
		private final int id;
		
		private final int[] icons;
		
		Shop(String name, int id, int... icons) {
			this.name = name;
			this.id = id;
			this.icons = icons;
		}
		
		public String getName() {
			return name;
		}
		
		public int getId() {
			return id;
		}
		
		public int[] getIcons() {
			return icons;
		}
	}
}