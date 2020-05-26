package net.arrav;

public final class Constants {

	private Constants() {
	}
	
	public static boolean MEMORY_MAPPED_CACHE = false;
	public static boolean JAGGRAB_ENABLED = false;
	public static boolean USER_HOME_FILE_STORE = true;

	/*
	 * Miscellaneous constants
	 */
	public static final int BUILD = 37;
	public static final boolean ANTI_BOT_ENABLED = true;
	public static final int CACHE_INDEX_COUNT = 9;

	/*
	 * Scene constants
	 */
	public static final int CAM_NEAR = 50;
	public static final int CAM_FAR = 4000; //originally 3500, = 140 * VISIBLE_DISTANCE
	private static final int CAM_FRUSTUM = CAM_FAR - CAM_NEAR;
	public static final int FOG_BEGIN = Constants.CAM_FRUSTUM - 1000;
	public static final int FOG_END = Constants.CAM_FRUSTUM - 400;

	/*
	 * UI constants
	 */
	public static final byte UI_FIXED = 0;
	public static final byte UI_RESIZABLE = 1;
	public static final byte UI_FULLSCREEN = 2;
	public static final short[] SELECTABLE_GAMEFRAMES = {317, 525, 562, 2, 1};

	/*
	 * Message constants
	 */
	public static final byte MSG_ALL = 0;
	public static final byte MSG_PUBLIC = 1;
	public static final byte MSG_PRIVATE = 2;
	public static final byte MSG_REQUEST = 3;
	public static final byte MSG_CLAN = 4;
	public static final byte MSG_GAME = 5;

	/*
	 * Skill constants
	 */
	static final byte SKILL_AMOUNT = 25;
	public static final String[] SKILL_NAMES_UNORDERED = {"Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Hunter", "Construction", "Summoning", "Dungeoneering"};
	public static final String[] SKILL_NAMES_ORDERED = {"Attack", "Hitpoints", "Mining", "Strength", "Agility", "Smithing", "Defence", "Herblore", "Fishing", "Range", "Thieving", "Cooking", "Prayer", "Crafting", "Firemaking", "Magic", "Fletching", "Woodcutting", "Rune", "Slayer", "Farming", "Construction", "Hunter", "Summoning", "Dungeoneering"};
	static final int[] MORE_DETAILS_PANEL_ID = new int[]{4040, 4076, 4112, 4046, 4082, 4118, 4052, 4088, 4124, 4058, 4094, 4130, 4064, 4100, 4136, 4070, 4106, 4142, 4160, 2832, 13917, 19005, 19006, 19007, 19008};

	/*
	 * Widget constants
	 */
	public static final byte WIDGET_MAIN = 0;
	public static final byte WIDGET_STRING_2 = 1;
	public static final byte WIDGET_INVENTORY = 2;
	public static final byte WIDGET_RECTANGLE = 3;
	public static final byte WIDGET_STRING = 4;
	public static final byte WIDGET_IMAGE = 5;
	public static final byte WIDGET_MODEL = 6;
	public static final byte WIDGET_INVENTORY_2 = 7;
	public static final byte WIDGET_TOOLTIP = 8;

	/*
	 * Packet constants
	 */
	static final byte[] PACKET_SIZE = {
			0, 0, 0, 0, 6, 0, 0, 0, 4, 0, 0,//10
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,//20
			0, 0, 0, 1, 0, 0, -1, 0, 0, -1,//30
			0, 0, 0, -2, 4, 3, 0, 0, 0, 2,//40
			0, 0, 0, 5, 0, 0, 6, 0, 0, 9,//50
			0, -1, -2, -2, -2, 0, 0, 0, 0, -2,//60
			1, 0, 0, 2, -2, 0, 0, 0, 0, 6,//70
			4, 2, 4, 0, 4, 0, 0, 0, 4, 3,//80
			-2, 8, 0, 7, 2, 0, 6, 0, 0, -2,//90
			0, 0, 0, 0, 0, 0, 2, 0, 1, -1,//100
			2, 0, 0, -1, 4, 2, 0, 0, 0, 1,//110
			1, 0, 0, 2, 0, 0, 15, 0, 0, 0,//120
			-2, 4, 0, 0, 0, -2, 4, 0, 0, -1,//130
			0, 0, 0, 9, 2, 0, 0, 0, 0, 0,//140
			0, 2, 0, 0, 0, 0, 14, 0, 0, -2,//150
			6, 0, 0, 0, 0, 3, 0, 0, 0, 4,//160
			0, 0, 0, 2, 0, 6, 0, 0, 0, 0,//170
			3, 0, 0, 0, 0, 0, 6, 0, 0, 0,//180
			0, 0, 0, 0, 2, 0, -1, 0, 0, 0,//190
			0, 0, 0, 0, 0, -1, 0, 0, 0, 4,//200
			0, 0, 0, 0, 0, 3, 0, 4, 0, -1,//210
			0, 0, 0, -2, 7, 0, -1, 2, 0, 0,//220
			1, 0, 0, 0, 0, 0, 0, 0, 0, 8,//230
			0, 0, 0, 0, 0, 0, 0, 0, 0, 2,//240
			-2, 0, 0, 0, 0, 6, 0, 4, 3, 0,//250
			0, -1, -1, 6, 0, 0};

	/*
	 * Orb constants
	 */
	public static final byte ORB_HEALTH = 0;
	public static final byte ORB_PRAYER = 1;
	public static final byte ORB_RUN = 2;
	public static final byte ORB_SUMMONING = 3;
	
	public static final boolean OSRS_OBJECTS = false;
	
	

}
