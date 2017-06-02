package net.edge.activity.panel.impl;

import net.edge.activity.panel.Panel;
import net.edge.cache.unit.ObjectType;
import net.edge.cache.unit.NPCType;
import net.edge.game.Scene;
import net.edge.media.Rasterizer2D;
import net.edge.media.img.BitmapImage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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
		ALWAYS(0xffffff),
		COMMON(0x66ff66),
		UNCOMMON(0x33cc33),
		VERY_UNCOMMON(0xff9933),
		RARE(0xcc3300),
		VERY_RARE(0x990000),
		EXTREMELY_RARE(0x660000);
		
		public String name;
		
		public int color;
		
		Chance(int color) {
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
	private NPCType[] seekable;
	
	/**
	 * An array of all found npcs in our search.
	 */
	private int[] result;
	
	@Override
	public String id() {
		return "npc";
	}
	
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
		scrollMax = Math.max(51 * ((client.npcDropsId.length + 2) / 3) - 285, 0);

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
		if(client.leftClickInRegion(beginX + 442, beginY + 12, beginX + 498, beginY + 42)) {
			client.panelHandler.close();
			client.outBuffer.putOpcode(185);
			client.outBuffer.putShort(123);
			Scene.hoverX = -1;
			return true;
		}
		
		int offset = -scrollPos + 52;
		
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
					List<Integer> ids = new ArrayList<>();
					for(int i = 0; i < seekable.length; i++) {
						NPCType n = seekable[i];
						if(n == null)
							continue;
						if(n.name == null)
							continue;
						if(Objects.equals(n.name, "null"))
							continue;
						if(n.name.toLowerCase().contains(cachedSearch.toLowerCase()))
							ids.add(i);
					}
					result = new int[ids.size()];
					for(int i = 0; i < ids.size(); i++) {
						result[i] = ids.get(i);
					}
				}
			}
			if(result != null && result.length > 0) {
				for(int i : result) {
					if(client.leftClickInRegion(beginX + 6, beginY + offset, beginX + 468, beginY + 30 + offset)) {
						client.outBuffer.putOpcode(134);
						client.outBuffer.putShort(seekable[i].id);
						client.panelSearchInput = null;
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
		Rasterizer2D.fillRoundedRectangle(beginX, beginY + 8, 500, 328, 4, 0x000000, 200);
		
		fancyFont.drawCenteredString("Exit", beginX + 467, beginY + 30, 0xF3B13F);
		Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 25, 2, 0xF3B13F, 60);
		if(client.mouseInRegion(beginX + 442, beginY + 12, beginX + 498, beginY + 47)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 25, 2, 0xF3B13F, 20);
		}
		if(client.panelSearch) {
			fancyFont.drawLeftAlignedEffectString("Searching: " + client.panelSearchInput, beginX + 20, beginY + 35, 0xF3B13F, true);
		} else {
			fancyFont.drawLeftAlignedEffectString(type.name + " - lvl " + type.combatLevel, beginX + 20, beginY + 35, 0xF3B13F, true);
			fancyFont.drawCenteredString("Suggest drop", beginX + 390, beginY + 30, 0xF3B13F);
			Rasterizer2D.fillRoundedRectangle(beginX + 340, beginY + 12, 97, 25, 2, 0xF3B13F, 60);
			if(client.mouseInRegion(beginX + 340, beginY + 12, beginX + 437, beginY + 47)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 340, beginY + 12, 97, 25, 2, 0xF3B13F, 20);
			}
			fancyFont.drawCenteredString("Search", beginX + 309, beginY + 30, 0xF3B13F);
			Rasterizer2D.fillRoundedRectangle(beginX + 280, beginY + 12, 57, 25, 2, 0xF3B13F, 60);
			if(client.mouseInRegion(beginX + 280, beginY + 12, beginX + 337, beginY + 37)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 280, beginY + 12, 57, 25, 2, 0xF3B13F, 20);
			}
		}
		
		/* content */
		Rasterizer2D.drawRectangle(beginX + 4, beginY + 49, 490, 282, 0xffffff, 80);
		Rasterizer2D.fillRectangle(beginX + 5, beginY + 50, 488, 280, 0xffffff, 60);
		Rasterizer2D.setClip(beginX + 5, beginY + 50, beginX + 493, beginY + 330);
		int offset = -scrollPos + 52;
		
		if(client.panelSearch) {
			if(result != null && result.length > 0) {
				for(int i : result) {
					NPCType npc = seekable[i];
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
			for(int u = 0; u < client.npcDropsId.length; u++) {
				int id = client.npcDropsId[u];
				int min = client.npcDropsMin[u];
				int max = client.npcDropsMax[u];
				Chance ch = chances[client.npcDropsChance[u]];
				ObjectType obj = ObjectType.get(id);
				if(obj == null)
					continue;
				int x = u % 3 * 158;
				BitmapImage image = ObjectType.getIcon(id, max, 0);
				Rasterizer2D.fillRoundedRectangle(beginX + 10 + x, beginY + offset, 155, 47, 3, 0x000000, 50);
				if(image != null)
					image.drawImage(beginX + 15 + x, beginY + offset + 2);
				plainFont.drawLeftAlignedString(obj.name, beginX + 55 + x, beginY + offset + 14, 0xffffff);
				smallFont.drawLeftAlignedString("From " + min + " to " + max, beginX + 55 + x, beginY + offset + 29, 0xe6c9a5);
				smallFont.drawLeftAlignedString(ch.name, beginX + 55 + x, beginY + offset + 40, ch.color);
				offset += u % 3 == 2 ? 50 : 0;
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
		URL url;
		List<Integer> ids = new ArrayList<>();
		try {
			url = new URL("http://edgeville.net/game/drops.txt");
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			br.lines().forEach(line -> {
				String[] l = line.split("-");
				for(String num : l) {
					int id = Integer.parseInt(num);
					if(id > 0)
						ids.add(id);
				}
			});
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		seekable = new NPCType[ids.size()];
		for(int i = 0; i < ids.size(); i++) {
			int id = ids.get(i);
			if(id > 0 && id < NPCType.size())
				seekable[i] = NPCType.get(id);
		}
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
		return 6;
	}
}