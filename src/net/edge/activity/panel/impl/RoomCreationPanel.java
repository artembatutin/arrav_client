package net.edge.activity.panel.impl;

import net.edge.activity.panel.Panel;
import net.edge.cache.unit.ObjectType;
import net.edge.game.Scene;
import net.edge.media.Rasterizer2D;
import net.edge.media.img.BitmapImage;
import net.edge.util.string.StringUtils;

public class RoomCreationPanel extends Panel {
	
	public static final int GARDEN = 2;
	public static final int PARLOUR = 3;
	public static final int KITCHEN = 4;
	public static final int DINING_ROOM = 5;
	public static final int WORKSHOP = 6;
	public static final int BEDROOM = 7;
	public static final int SKILL_ROOM = 8;
	public static final int QUEST_HALL_DOWN = 9;
	public static final int GAMES_ROOM = 10;
	public static final int COMBAT_ROOM = 11;
	public static final int QUEST_ROOM = 12;
	public static final int MENAGERY = 13;
	public static final int STUDY = 14;
	public static final int COSTUME_ROOM = 15;
	public static final int CHAPEL = 16;
	public static final int PORTAL_ROOM = 17;
	public static final int FORMAL_GARDEN = 18;
	public static final int THRONE_ROOM = 19;
	public static final int OUBLIETTE = 20;
	public static final int PIT = 21;
	public static final int DUNGEON_STAIR_ROOM = 22;
	public static final int TREASURE_ROOM = 23;
	public static final int CORRIDOR = 24;
	public static final int JUNCTION = 25;
	public static final int SKILL_HALL_DOWN = 26;
	
	public enum Room {
		GARDEN(RoomCreationPanel.GARDEN, 1, "1k"),
		PARLOUR(RoomCreationPanel.PARLOUR, 1, "1k"),
		KITCHEN(RoomCreationPanel.KITCHEN, 5, "5k"),
		DINING_ROOM(RoomCreationPanel.DINING_ROOM, 10, "5k"),
		WORKSHOP(RoomCreationPanel.WORKSHOP, 15, "10k"),
		BEDROOM(RoomCreationPanel.BEDROOM, 20, "10k"),
		SKILL_ROOM(RoomCreationPanel.SKILL_ROOM, 25, "15k"),
		QUEST_HALL(RoomCreationPanel.QUEST_HALL_DOWN, 35, "free"),
		SKILL_HALL(RoomCreationPanel.SKILL_HALL_DOWN, 25, "free)"),
		GAMES_ROOM(RoomCreationPanel.GAMES_ROOM, 30, "25k"),
		COMBAT_ROOM(RoomCreationPanel.COMBAT_ROOM, 32, "25k"),
		QUEST_ROOM(RoomCreationPanel.QUEST_ROOM, 35, "25k"),
		MENAGERY(RoomCreationPanel.MENAGERY, 37, "30k"),
		STUDY(RoomCreationPanel.STUDY, 40, "50k"),
		CUSTOME_ROOM(RoomCreationPanel.COSTUME_ROOM, 42, "50k"),
		CHAPEL(RoomCreationPanel.CHAPEL, 45, "50k"),
		PORTAL_ROOM(RoomCreationPanel.PORTAL_ROOM, 50, "100k"),
		FORMAL_GARDEN(RoomCreationPanel.FORMAL_GARDEN, 55, "75k"),
		THRONE_ROOM(RoomCreationPanel.THRONE_ROOM, 60, "150k"),
		OUBLIETTE(RoomCreationPanel.OUBLIETTE, 65, "150k"),
		PIT_DUNGEON(RoomCreationPanel.PIT, 70, "10k"),
		DUNGEON_STAIRS(RoomCreationPanel.DUNGEON_STAIR_ROOM, 70, "7.5k"),
		CORRIDOR(RoomCreationPanel.CORRIDOR, 70, "7.5k"),
		JUNCTION(RoomCreationPanel.JUNCTION, 70, "7.5k"),
		TREASURE_ROOM(RoomCreationPanel.TREASURE_ROOM, 75, "250k");
		
		private int id, level;
		private final String name, cost;
		
		Room(int id, int levelToBuild, String cost) {
			this.id = id;
			this.level = levelToBuild;
			this.cost = cost;
			name = StringUtils.formatName(name().toLowerCase().replaceAll("_", " "));
		}
	}
	
	private final Room[] rooms = Room.values();
	
	/**
	 * Construction level
	 */
	private int consLevel = 1;
	
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
			client.outBuffer.putOpcode(185);
			client.outBuffer.putShort(123);
			Scene.hoverX = -1;
			return true;
		}
		
		int offset = 50;
		int i = 0;
		for(Room room : rooms) {
			int x = i % 3 * 165;
			if(client.leftClickInRegion(beginX + 6 + x, beginY + offset, beginX + 164 + x, beginY + offset + 30)) {
				client.outBuffer.putOpcode(10);
				client.outBuffer.putByte(i);
				return true;
			}
			offset += i % 3 == 2 ? 31 : 0;
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
		Rasterizer2D.fillRectangle(beginX, beginY + 8, 500, 328, 0x000000, 200);
		Rasterizer2D.drawRectangle(beginX, beginY + 8, 500, 328, 0x63625e);
		
		fancyFont.drawLeftAlignedEffectString("Build a room - Level: " + consLevel, beginX + 20, beginY + 33, 0xF3B13F, true);
		
		fancyFont.drawCenteredString("Exit", beginX + 467, beginY + 30, 0xF3B13F);
		Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 25, 2, 0xF3B13F, 60);
		if(client.mouseInRegion(beginX + 442, beginY + 12, beginX + 498, beginY + 47)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 25, 2, 0xF3B13F, 20);
		}
		
		/* content */
		Rasterizer2D.drawRectangle(beginX + 4, beginY + 49, 490, 282, 0xffffff, 80);
		Rasterizer2D.fillRectangle(beginX + 5, beginY + 50, 488, 280, 0xffffff, 60);
		int offset = 50;
		int i = 0;
		for(Room room : rooms) {
			int x = i % 3 * 165;
			Rasterizer2D.fillRectangle(beginX + 6 + x, beginY + offset, 158, 30, 0x0000, consLevel >= room.level ? 100 : 150);
			Rasterizer2D.drawRectangle(beginX + 6 + x, beginY + offset, 158, 30, 0x0000);
			if(consLevel >= room.level && client.mouseInRegion(beginX + 6 + x, beginY + offset, beginX + 164 + x, beginY + offset + 30)) {
				Rasterizer2D.drawRectangle(beginX + 6 + x, beginY + offset, 158, 31, 0xffffff);
			}
			String strike = consLevel >= room.level ? "" : "@str@";
			plainFont.drawLeftAlignedEffectString(room.level+"", beginX + 12 + x, beginY + offset + 23, 0xffffff, false);
			fancyFont.drawLeftAlignedEffectString(strike+room.name, beginX + 38 + x, beginY + offset + 20, 0xffffff, false);
			smallFont.drawRightAlignedEffectString(room.cost+"", beginX + 155 + x, beginY + offset + 20, 0xffffff, false);
			Rasterizer2D.drawVerticalLine(beginX + 32 + x, beginY + offset, 30, 0xffffff, 50);
			//plainFont.drawLeftAlignedString("Combat: " + npc.combatLevel, beginX + 320, beginY + offset + 17, 0xffffff);
			//smallFont.drawLeftAlignedString("Id: " + npc.id, beginX + 410, beginY + offset + 17, 0xffffff);
			offset += i % 3 == 2 ? 31 : 0;
			i++;
		}
		
	}
	
	@Override
	public void initialize() {
		consLevel = client.currentStats[22];
	}
	
	@Override
	public void reset() {
	
	}
	
	@Override
	public int getId() {
		return 9;
	}
	
	@Override
	public boolean blockedMove() {
		return false;
	}

}