package net.edge.activity.panel.impl;

import net.edge.activity.panel.Panel;
import net.edge.cache.unit.ObjectType;
import net.edge.game.Scene;
import net.edge.media.Rasterizer2D;
import net.edge.media.img.BitmapImage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CounterPanel extends Panel {
	
	/**
	 * The shops array
	 */
	public static Shop[] shops;
	
	/**
	 * Scroll bar manipulated value.
	 */
	private int scrollPos, scrollMax, scrollDragPos;
	
	/**
	 * The condition if the scroll is being dragged.
	 */
	private boolean scrollDrag = false;
	
	@Override
	public String id() {
		return "npc";
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
		
		/* Item search */
		if(client.leftClickInRegion(beginX + 10, beginY + 50, beginX + 494, beginY + 80)) {
			client.marketSearch = true;
			client.panelSearch = true;
			client.npcSug = false;
			client.promptInput = "";
			client.panelSearchInput = "";
			client.promptInputTitle = "What item are you looking for?";
			client.messagePromptRaised = true;
		} else {
			int offset = -scrollPos + 152;
			if(shops != null && shops.length > 0) {
				for(int i = 0; i < shops.length; i++) {
					int x = i % 2 * 235;
					Shop s = shops[i];
					if(s == null)
						continue;
					if(client.leftClickInRegion(beginX + 6 + x, beginY + offset, beginX + 230 + x, beginY + 30 + offset)) {
						client.outBuffer.putOpcode(20);
						client.outBuffer.putShort(s.getId());
					}
					offset += i % 2 == 1 ? 41 : 0;
				}
			}
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
		
		fancyFont.drawLeftAlignedEffectString("Shop Counter", beginX + 20, beginY + 33, 0xF3B13F, true);
		boldFont.drawCenteredEffectString("Quick shops:", beginX + 240, beginY + 143, 0xF3B13F, true);
		
		fancyFont.drawCenteredString("Exit", beginX + 467, beginY + 30, 0xF3B13F);
		Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 25, 2, 0xF3B13F, 60);
		if(client.mouseInRegion(beginX + 442, beginY + 12, beginX + 498, beginY + 47)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 25, 2, 0xF3B13F, 20);
		}
		
		fancyFont.drawCenteredEffectString("Search item", beginX + 240, beginY + 70, 0xF3B13F, true);
		Rasterizer2D.fillRoundedRectangle(beginX + 10, beginY + 50, 484, 30, 2, 0xcf9d47, 60);
		if(client.mouseInRegion(beginX + 10, beginY + 50, beginX + 494, beginY + 80)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 10, beginY + 50, 484, 30, 2, 0xcf9d47, 60);
		}
		
		fancyFont.drawCenteredEffectString("Search player shop", beginX + 240, beginY + 110, 0xF3B13F, true);
		Rasterizer2D.fillRoundedRectangle(beginX + 10, beginY + 90, 484, 30, 2, 0xcf9d47, 20);
		//if(client.mouseInRegion(beginX + 10, beginY + 90, beginX + 494, beginY + 120)) {
		//	Rasterizer2D.fillRoundedRectangle(beginX + 10, beginY + 90, 484, 30, 2, 0xcf9d47, 60);
		//}
		
		/* content */
		Rasterizer2D.drawRectangle(beginX + 4, beginY + 149, 490, 182, 0xffffff, 80);
		Rasterizer2D.fillRectangle(beginX + 5, beginY + 150, 488, 180, 0xffffff, 60);
		Rasterizer2D.setClip(beginX + 5, beginY + 150, beginX + 493, beginY + 330);
		int offset = -scrollPos + 152;
		
		if(shops != null && shops.length > 0) {
			for(int i = 0; i < shops.length; i++) {
				int x = i % 2 * 235;
				Shop s = shops[i];
				if(s == null)
					continue;
				Rasterizer2D.fillRectangle(beginX + 6 + x, beginY + offset, 230, 40, 0x0000, 100);
				if(client.mouseInRegion(beginX + 6 + x, beginY + offset, beginX + 230 + x, beginY + 30 + offset)) {
					Rasterizer2D.drawRectangle(beginX + 6 + x, beginY + offset, 230, 41, 0xffffff);
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
				fancyFont.drawLeftAlignedString(s.getName(), beginX + 110 + x, beginY + offset + 24, 0xffffff);
				//plainFont.drawLeftAlignedString("Combat: " + npc.combatLevel, beginX + 320, beginY + offset + 17, 0xffffff);
				//smallFont.drawLeftAlignedString("Id: " + npc.id, beginX + 410, beginY + offset + 17, 0xffffff);
				offset += i % 2 == 1 ? 41 : 0;
			}
		}

		/* Scroll bar */
		Rasterizer2D.drawRectangle(476 + beginX, 155 + beginY, 12, 170, 0xffffff, 60);
		int height = 168;
		if(scrollMax > 0) {
			height = 175 * 168 / (scrollMax + 175);
		}
		int pos = 0;
		if(scrollPos != 0) {
			pos = scrollPos * 168 / (scrollMax + 175) + 1;
		}
		Rasterizer2D.fillRectangle(477 + beginX, 156 + pos + beginY, 10, height, 0x222222, 120);
		Rasterizer2D.removeClip();
		
	}
	
	@Override
	public void initialize() {
		client.marketSearch = false;
		if(shops == null) {
			URL url;
			List<Shop> shops = new ArrayList<>();
			try {
				url = new URL("http://edgeville.net/game/shops.txt");
				URLConnection conn = url.openConnection();
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				br.lines().forEachOrdered(line -> {
					String[] shop = line.split("-");
					String name = shop[0];
					int id = Integer.parseInt(shop[1]);
					int icons[] = new int[3];
					icons[0] = Integer.parseInt(shop[2]);
					icons[1] = Integer.parseInt(shop[3]);
					icons[2] = Integer.parseInt(shop[4]);
					shops.add(new Shop(name, id, icons));
				});
				CounterPanel.shops = shops.toArray(new Shop[shops.size()]);
				br.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void reset() {
		client.marketSearch = false;
	}
	
	@Override
	public int getId() {
		return 9;
	}
	
	/**
	 * Represents a shop on this panel.
	 */
	public static class Shop {
		
		private final String name;
		
		private final int id;
		
		private final int[] icons;
		
		Shop(String name, int id, int[] icons) {
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