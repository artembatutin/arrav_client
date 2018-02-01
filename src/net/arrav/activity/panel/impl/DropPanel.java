package net.arrav.activity.panel.impl;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.arrav.Config;
import net.arrav.activity.panel.Panel;
import net.arrav.cache.unit.ObjectType;
import net.arrav.cache.unit.NPCType;
import net.arrav.media.Rasterizer2D;
import net.arrav.media.img.BitmapImage;

import java.util.*;

public class DropPanel extends Panel {
	
	public static Chance getChance(String s) {
		for(Chance chance : Chance.values()) {
			if(chance.name.equals(s.toLowerCase())) {
				return chance;
			}
		}
		return null;
	}
	
	public enum Chance {
		ALWAYS(0xffffff, ""),
		VERY_COMMON(0x66ff66, ""),
		COMMON(0x33cc33, ""),
		UNCOMMON(0xff9933, ""),
		VERY_UNCOMMON(0xcc8600, ""),
		RARE(0xcc3300, ""),
		VERY_RARE(0x990000, ""),
		EXTREMELY_RARE(0x660000, "");
		
		public String name;
		
		public int color;
		
		Chance(int color, String rate) {
			this.color = color;
			name = this.name().replaceAll("_", " ").toLowerCase();
		}
	}
	
	/**
	 * The npc type of this drop.
	 */
	private NPCType type;
	
	/**
	 * Array of chances.
	 */
	private Chance[] chances = Chance.values();
	
	/**
	 * Scroll bar manipulated value.
	 */
	private int scrollPos, scrollMax, scrollDragPos;
	
	/**
	 * The condition if the scroll is being dragged.
	 */
	private boolean scrollDrag = false;
	
	/**
	 * The cached search to not use a loop all the time.
	 */
	private String cachedSearch;
	
	/**
	 * An array of all npcs that have drops.
	 */
	public static int[] seekable;
	
	/**
	 * An array of all found npcs in our search.
	 */
	private int[] result;
	
	@Override
	public boolean process() {
		if(type == null && client.npcInfoId != 0) {
			type = NPCType.get(client.npcInfoId);
		}
	    /* Initialization */
		int beginX = 4;
		int beginY = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			beginX = client.windowWidth / 2 - 380;
			beginY = client.windowHeight / 2 - 250;
		}
		
		int max1 = 1;
		int max2 = 0;
		if(client.panelSearch && seekable != null) {
			max1 = 32 * seekable.length;
			max2 = 0;
		} else if(!client.panelSearch) {
			max2 = 58 * ((client.npcDropsId.length + 4) / 5);
		}
		scrollMax = Math.max((max1 > max2 ? max1 : max2) - 285, 0);

        /* Scrolling */
		if(client.mouseInRegion(beginX + 5, beginY + 50, beginX + 493, beginY + 365)) {
			scrollPos += client.mouseWheelAmt * 24;
			if(scrollPos < 0) {
				scrollPos = 0;
			}
			if(scrollPos > scrollMax) {
				scrollPos = scrollMax;
			}
		}
		if(!scrollDrag) {
			int height = 268;
			if(scrollMax > 0) {
				height = 275 * 268 / (scrollMax + 275);
			}
			int pos = 0;
			if(scrollPos != 0) {
				pos = scrollPos * 268 / (scrollMax + 275) + 1;
			}
			int x = 485;
			int y = 46 + pos;
			if(client.mouseDragButton == 1 && client.mouseInRegion(x, y, x + 20, y + height)) {
				scrollDrag = true;
				scrollDragPos = scrollPos;
			}
		} else if(client.mouseDragButton != 1) {
			scrollDrag = false;
		} else {
			int d = (client.mouseY - client.clickY) * (scrollMax + 275) / 268;
			scrollPos = scrollDragPos + d;
			if(scrollPos < 0) {
				scrollPos = 0;
			}
			if(scrollPos > scrollMax) {
				scrollPos = scrollMax;
			}
		}
		

        /* Exit */
		if(processClose(beginX, beginY)) {
			return true;
		}
		
		int offset = -scrollPos + (Config.def.panelStyle == 2 ? 52 : 42);
		if(!client.panelSearch) {
			if(client.leftClickInRegion(beginX + 280, beginY + 12, beginX + 337, beginY + 37)) {
				client.panelSearch = true;
				client.npcSug = false;
				client.promptInput = "";
				client.panelSearchInput = "";
				client.promptInputTitle = "What monster are you searching for?";
				client.messagePromptRaised = true;
				type = null;
			}
		}
		if(!client.panelSearch && type != null) {
			if(client.leftClickInRegion(beginX + 340, beginY + 12, beginX + 437, beginY + 47)) {
				client.npcSug = true;
				client.panelSearch = false;
				client.promptInputTitle = "What would be the name of the item?";
				client.messagePromptRaised = true;
			}
		} else if(client.panelSearch) {
			if(client.panelSearchInput != null) {
				if(!Objects.equals(cachedSearch, client.panelSearchInput)) {
					cachedSearch = client.panelSearchInput;
					IntArrayList ids = new IntArrayList();
					for(int i = 0; i < seekable.length; i++) {
						NPCType n = NPCType.get(seekable[i]);
						if(n == null)
							continue;
						if(n.name == null)
							continue;
						if(Objects.equals(n.name, "null"))
							continue;
						if(n.name.toLowerCase().contains(cachedSearch.toLowerCase()))
							ids.add(i);
					}
					result = ids.toIntArray();
				}
			}
			if(result != null && result.length > 0) {
				for(int i : result) {
					if(client.leftClickInRegion(beginX + 6, beginY + offset, beginX + 468, beginY + 30 + offset)) {
						client.outBuffer.putOpcode(134);
						client.outBuffer.putShort(NPCType.get(seekable[i]).id);
						client.panelSearchInput = null;
						return true;
					}
					offset += 31;
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
		drawMain(beginX, beginY + 8, 500, 328, 0x000000, 0x63625e, 200);
		drawOver(beginX, beginY);
		drawClose(beginX, beginY);
		
		if(client.panelSearch) {
			fancyFont.drawLeftAlignedEffectString("Searching: " + client.panelSearchInput, beginX + 20, beginY + 32, 0xFF8A1F, true);
		} else {
			fancyFont.drawLeftAlignedEffectString(type.name + " - lvl " + type.combatLevel, beginX + 20, beginY + 32, 0xFF8A1F, true);
			Rasterizer2D.fillRoundedRectangle(beginX + 340, beginY + 12, 97, 25, 2, Config.def.panelStyle == 2 ? 0xFF8A1F : 0x000000, 60);
			if(client.mouseInRegion(beginX + 340, beginY + 12, beginX + 437, beginY + 47)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 340, beginY + 12, 97, 25, 2, 0xFF8A1F, 20);
			}
			fancyFont.drawCenteredString("Suggest drop", beginX + 390, beginY + 30, 0xFF8A1F);
			Rasterizer2D.fillRoundedRectangle(beginX + 280, beginY + 12, 57, 25, 2, Config.def.panelStyle == 2 ? 0xFF8A1F : 0x000000, 60);
			if(client.mouseInRegion(beginX + 280, beginY + 12, beginX + 337, beginY + 37)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 280, beginY + 12, 57, 25, 2, 0xFF8A1F, 20);
			}
			fancyFont.drawCenteredString("Search", beginX + 309, beginY + 30, 0xFF8A1F);
		}
		
		if(Config.def.panelStyle == 2)
			Rasterizer2D.setClip(beginX + 5, beginY + 50, beginX + 493, beginY + 330);
		else
			Rasterizer2D.setClip(beginX + 5, beginY + 42, beginX + 493, Config.def.panelStyle == 0 ? beginY + 328 : beginY + 325);
		int offset = -scrollPos + (Config.def.panelStyle == 2 ? 52 : 42);
		
		if(client.panelSearch) {
			if(result != null && result.length > 0) {
				for(int i : result) {
					NPCType npc = NPCType.get(seekable[i]);
					if(npc == null)
						continue;
					Rasterizer2D.fillRectangle(beginX + 6, beginY + offset, 300, 30, 0x0000, 100);
					Rasterizer2D.fillRectangle(beginX + 307, beginY + offset, 90, 30, 0x0000, 100);
					Rasterizer2D.fillRectangle(beginX + 398, beginY + offset, 70, 30, 0x0000, 100);
					if(client.mouseInRegion(beginX + 6, beginY + offset, beginX + 468, beginY + 30 + offset)) {
						Rasterizer2D.drawRectangle(beginX + 6, beginY + offset, 460, 31, 0xffffff);
					}
					plainFont.drawLeftAlignedString(npc.name, beginX + 15, beginY + offset + 17, 0xffffff);
					plainFont.drawLeftAlignedString("Combat: " +npc.combatLevel, beginX + 320, beginY + offset + 17, 0xffffff);
					smallFont.drawLeftAlignedString("Id: " +npc.id, beginX + 410, beginY + offset + 17, 0xffffff);
					offset += 31;
				}
			}
		} else {
			if(type == null)
				return;
			offset = -scrollPos + 52;
			String tooltip = null;
			for(int u = 0; u < client.npcDropsId.length; u++) {
				int id = client.npcDropsId[u];
				int min = client.npcDropsMin[u];
				int max = client.npcDropsMax[u];
				Chance ch = chances[client.npcDropsChance[u]];
				ObjectType obj = ObjectType.get(id);
				if(obj == null)
					continue;
				int x = u % 5 * 93;
				Rasterizer2D.fillRoundedRectangle(beginX + 12 + x, beginY + offset, 82, 50, 3, ch.color, 150);
				Rasterizer2D.fillRoundedRectangle(beginX + 12 + x, beginY + offset, 82, 50, 3, 0x000000, 50);
				if(client.mouseInRegion(beginX + 12 + x, beginY + offset, beginX + 100 + x, beginY + offset + 50)) {
					Rasterizer2D.fillRoundedRectangle(beginX + 12 + x, beginY + offset, 82, 50, 3, 0x000000, 50);
					tooltip = obj.name;
				}
				final BitmapImage img = ObjectType.getIcon(id, min, 0);
				if(img != null) {
					img.drawImage(beginX + 36 + x, beginY + offset + 3);
				}
				//plainFont.drawLeftAlignedString(obj.name, beginX + 190, beginY + offset + 14, 0xffffff);
				///smallFont.drawLeftAlignedString(ch.name, beginX + 190, beginY + offset + 26, ch.color);
				if(max > 1 && min != max) {
					smallFont.drawLeftAlignedEffectString(min + "-" + max, beginX + 15 + x, beginY + offset + 14, 0xFF8A1F, true);
				}
				smallFont.drawCenteredEffectString(ch.name, beginX + 52 + x, beginY + offset + 46, ch.color, true);
				offset += u % 5 == 4 ? 55 : 0;
			}
			if(tooltip != null) {
				boolean off = (client.mouseX - beginX + 8 + smallFont.getStringWidth(tooltip)) > 490;
				Rasterizer2D.fillRoundedRectangle(client.mouseX + (off ? -(smallFont.getStringWidth(tooltip) + 18) : 8), client.mouseY - 3, smallFont.getStringWidth(tooltip) + 7, 15, 3, 0x000000, 200);
				smallFont.drawLeftAlignedEffectString(tooltip, client.mouseX + (off ? -(smallFont.getStringWidth(tooltip) + 14) : 12), client.mouseY + 9, 0xFF8A1F, true);
			}
		}

		/* Scroll bar */
		Rasterizer2D.drawRectangle(476 + beginX, 55 + beginY, 12, 270, 0xffffff, 60);
		int height = 268;
		if(scrollMax > 0) {
			height = 275 * 268 / (scrollMax + 275);
		}
		int pos = 0;
		if(scrollPos != 0) {
			pos = scrollPos * 268 / (scrollMax + 275) + 1;
		}
		Rasterizer2D.fillRectangle(477 + beginX, 56 + pos + beginY, 10, height, 0x222222, 120);
		Rasterizer2D.removeClip();
		
	}
	
	@Override
	public void initialize() {
		client.npcSug = false;
		client.panelSearchInput = "";
	}
	
	@Override
	public void reset() {
		client.npcSug = false;
		client.panelSearchInput = "";
		client.promptInput = "";
		client.promptInputTitle = "";
		client.messagePromptRaised = false;
		type = null;
	}
	
	@Override
	public int getId() {
		return 10;
	}
	
	@Override
	public boolean blockedMove() {
		return false;
	}
	
}