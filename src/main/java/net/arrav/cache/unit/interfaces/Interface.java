package net.arrav.cache.unit.interfaces;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.arrav.Constants;
import net.arrav.cache.unit.AnimationFrame;
import net.arrav.cache.unit.NPCType;
import net.arrav.cache.unit.ObjectType;
import net.arrav.cache.unit.interfaces.component.dropdown.Dropdown;
import net.arrav.cache.unit.interfaces.component.dropdown.DropdownMenu;
import net.arrav.cache.unit.interfaces.component.tab.Tab;
import net.arrav.cache.unit.interfaces.component.tab.TabMenu;
import net.arrav.cache.unit.interfaces.custom.BankInterface;
import net.arrav.cache.unit.interfaces.custom.ClanInterface;
import net.arrav.world.model.Model;
import net.arrav.net.SignLink;
import net.arrav.util.io.Buffer;
import net.arrav.Client;
import net.arrav.cache.CacheArchive;
import net.arrav.graphic.font.BitmapFont;
import net.arrav.util.DataToolkit;

import java.io.IOException;

public class Interface {



	public static int currentInputFieldId;
	public static Client client;
	public static BitmapFont[] fonts;
	public static CacheArchive archive;
	public static Interface[] cache;
	private static final Int2ObjectOpenHashMap<Model> modelcache = new Int2ObjectOpenHashMap<>();


	public int tabHover = -1;
	public TabMenu tab;
	public boolean hovered = false;




	public int type;
	public int id;
	public int parent;
	public int[] children;
	public int[] childX;
	public int[] childY;
	public int width;
	public int height;
	public int scrollMax;
	public int scrollPos;
	public int offsetX;
	public int offsetY;
	public byte alpha;
	public int contentType;
	public int actionType;
	public String[] actions;
	public String tooltip;
	public String[] menuItem;
	public int[] invId;
	public int[] invAmt;
	public boolean invDrag;
	public boolean isInv;
	public boolean invUse;
	public boolean invMove;
	public int invPadX;
	public int invPadY;
	public int[] invIconX;
	public int[] invIconY;
	public int[] invIcon;
	public String text;
	public String textAlt;
	public boolean textCenter;
	public int textAlign;
	public int fontId;
	public boolean textShadow;
	public int color;
	public int colorAlt;
	public int hoverColor;
	public int hoverColorAlt;
	public boolean rectFilled;
	public int modelType;
	public int modelId;
    private int modelTypeAlt;
	private int modelIdAlt;
	public int modelZoom;
	public int modelYaw;
	public int modelRoll;
	public int modelAnim;
	public int modelAnimAlt;
	public int modelAnimLength;
	public int modelAnimDelay;
	public boolean drawsAlpha;
	public int[] requiredValues;
	public String spellName;
	public String selectedActionName;
	public int[][] valueIndexArray;
	public int hoverInterToTrigger;
	public int spellUsableOn;
	public int valueCompareType[];
	public boolean hoverTriggered;
	public int spriteID;
	public int secondarySpriteID;

	public boolean displayAsterisks;
	public boolean updatesEveryInput;
	public int characterLimit;
	public String inputRegex = "";
	public String defaultInputFieldText = "";
	public boolean isInFocus;
	private boolean updateConfig;


	public static void unpack(CacheArchive widgetArchive, BitmapFont[] fonts) {
		Interface.fonts = fonts;
		final Buffer buffer;
		//if(Constants.USER_HOME_FILE_STORE)
	//		buffer = new Buffer(widgetArchive.getFile("data"));
	//	else
			buffer = new Buffer(DataToolkit.readFile(SignLink.getCacheDir() + "util/int/data.dat"));
		final int length = buffer.getInt();
		System.out.println("[loading] widget size: " + length);
		cache = new Interface[100000];
		while(buffer.pos < buffer.data.length) {
			decode(buffer);
		}
		archive = widgetArchive;
		
		
		//Buttons (can't be packed)
		//Duel
		addHoverButton(37910, 1638, 72, 32, "Accept", -1, 37911, 1);
		addHoveredButton(37911, 1639, 72, 32, 37912);
		addHoverButton(37913, 1638, 72, 32, "Decline", -1, 37914, 1);
		addHoveredButton(37914, 1639, 72, 32, 37915);
		//Summoning
		addHoverButton(18029, 872, 38, 38, "Withdraw BoB", -1, 18030, 1);
		addHoveredButton(18030, 873, 38, 38, 18031);
		addHoverButton(18032, 884, 38, 38, "Renew familiar", -1, 18033, 1);
		addHoveredButton(18033, 874, 38, 38, 18034);
		addHoverButton(18035, 875, 38, 38, "Call follower", -1, 18036, 1);
		addHoveredButton(18036, 876, 38, 38, 18037);
		addHoverButton(18038, 877, 38, 38, "Dismiss familiar", -1, 18039, 1);
		addHoveredButton(18039, 878, 38, 38, 18040);
		//Pet
		addHoverButton(19022, 875, 38, 38, "Call follower", -1, 19023, 1);
		addHoveredButton(19023, 876, 38, 38, 19024);
		addHoverButton(19025, 877, 38, 38, "Dismiss familiar", -1, 19026, 1);
		addHoveredButton(19026, 878, 38, 38, 19027);
		//Quick prayer
		addHoverButton(261, 1881, 190, 24, "Confirm Selection", -1, 261, 1);
		addHoveredButton(262, 262, 190, 24, 17243);
		//Quest
		addText(8144, "Quest Name", 0x000000, true, false, 52, fonts, 3);
		addHoverButton(8137, 1914, 26, 23, "Close", 250, 8138, 3);
		addHoveredButton(8138, 1915, 26, 23, 8139);
		//Bolt enchant
		boltEnchantInterface(fonts);
		//Warriors
		cache[34000] = addInterface(34000);
		addText(34001, "Bronze Def.", fonts, 1, 0xffffff, true);
		addItemModel(34002, 8844, 1, 1, 450); // def
		addSprite(34003, 1641);
		setChildren(3, cache[34000]);
		cache[34000].child(2, 34001, 463, 303);
		cache[34000].child(1, 34002, 455, 280);
		cache[34000].child(0, 34003, 420, 260);
		//pack();
		Pestpanel2(fonts);
		Pestpanel(fonts);
		clanWars(fonts);
		addPestControlRewardWidget(fonts);




		//tamatea added
		ClanInterface.init(fonts);
		BankInterface.bank(fonts);

		/*try {
			BufferedWriter w = new BufferedWriter(new FileWriter(new File("./int_values.txt")));
			for(int i = 0; i < cache.length; i++) {
				if(cache[i] != null) {
					Map<String, String> values = ReflectionUtil.getValues(cache[i]);
					values.forEach(((s, s2) -> {
						try {
							w.write(s+": "+s2);
							w.newLine();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}));
					w.newLine();
				}
			}
			w.flush();
			w.close();
		} catch(IOException e) {
			e.printStackTrace();
		}*/
	}

	public static void clanWars(BitmapFont[] tda) {
		/*
		 * 0xff7000 was the original color
		 */
		Interface tab = addInterface(31000);
		Interface end = addInterface(31006);
		Interface arena = addInterface(31007);
		addSprite(31001, 2053);
		addHoveredButton(31002, 2054, 21, 21, 2055);
		//addHover(31002, 3, 250, 31003, 1, s, 21, 21, "Close Window");
		//addHovered(31003, 2, s, 21, 21, 31004);
		addText(31005, "Clan Wars Setup:", tda, 2, 0xff981f, true, true);
		addText(31008, "Game end", tda, 2, 0xff981f, true, true);
		addText(31009, "Arena", tda, 2, 0xff981f, true, true);
		addText(31010, "Miscellaneous", tda, 2, 0xff981f, true, true);
		addText(31011, "Stragglers", tda, 2, 0xff981f, true, true);
		addSprite(31012, 2056);
		addSprite(31013, 2057);
		addSprite(31014, 2058);
		addSprite(31015, 2059);
		addSprite(31016, 2060);
		addSprite(31017, 2061);
		addSprite(31018, 2062);
		addText(31019, "Melee", tda, 2, 0xff981f, true, true);
		addText(31020, "Ranging", tda, 2, 0xff981f, true, true);
		addText(31021, "Magic", tda, 2, 0xff981f, true, true);
		addText(31022, "Food", tda, 2, 0xff981f, true, true);
		addText(31023, "Drinks", tda, 2, 0xff981f, true, true);
		addText(31024, "Special Attacks", tda, 2, 0xff981f, true, true);
		addText(31025, "Prayer", tda, 2, 0xff981f, true, true);

		String[] options = {
				"Allowed", "Disabled", "Allowed", "Disabled", "Allowed",
				"No Staff of the Dead", "Disabled", "Kill 'em all", "Ignore 5", "Allowed",
				"Disabled", "Allowed", "Disabled", "All spellbooks", "Standard spells",
				"Binding only", "Disabled", "All allowed", "No overheads", "Disabled",
		};
		int[][] positions = {
				{177, 70}, {177, 87}, {177, 132}, {177, 148}, {177, 192},
				{177, 210}, {177, 227}, {176, 276}, {176, 293}, {269, 70},
				{269, 87}, {269, 131}, {269, 148}, {361, 70}, {361, 87},
				{361, 104}, {361, 121}, {361, 179}, {361, 196}, {361, 213},
		};
		for (int index = 0; index < 20; index++) {
			addToggleButton(31026 + index, 2064, 899 + index, 15, 15, "toggle");
			addText(31047 + index, options[index], tda, 1, 0xff981f, false, true);
		}
		addHoveredButton(31070, 2067, 94, 40, 2066);
//		addHover(31070, 1, 0, 31071, 175, s, 94, 40, "Select");
//		addHovered(31071, 174, s, 94, 40, 31072);
		addToggleButton(31073, 2063, 2064, 930, 15, 15, "toggle");
		addToggleButton(31074, 2063, 2064, 931, 15, 15, "toggle");
		addToggleButton(31075, 2063, 2064, 932, 15, 15, "toggle");
		addToggleButton(31076, 2063, 2064, 933, 15, 15, "toggle");
		addText(31077, "Ignore freezing", tda, 1, 0xff981f, false, true);
		addText(31078, "PJ timer", tda, 1, 0xff981f, false, true);
		addText(31079, "Single Spells", tda, 1, 0xff981f, false, true);
		addText(31080, "Keep items on death", tda, 1, 0xDF0101, false, true);
		int x = 6, y = 6;
		tab.totalChildren(74);
		tab.child(0, 31001, x, y);
		tab.child(1, 31002, 470+x, 7+y);
		tab.child(2, 31003, 470+x, 7+y);
		tab.child(3, 31005, 250+x, 10+y);
		tab.child(4, 31006, 12+x, 65+y);
		tab.child(5, 31007, 12+x, 151+y);
		tab.child(6, 31008, 90+x, 46+y);
		tab.child(7, 31009, 90+x, 132+y);
		tab.child(8, 31010, 90+x, 222+y);
		tab.child(9, 31011, 233+x, 256+y);
		tab.child(10, 31012, 177+x, 44+y);
		tab.child(11, 31013, 269+x, 46+y);
		tab.child(12, 31014, 361+x, 45+y);
		tab.child(13, 31015, 361+x, 154+y);
		tab.child(14, 31016, 177+x, 170+y);
		tab.child(15, 31017, 177+x, 107+y);
		tab.child(16, 31018, 274+x, 108+y);
		tab.child(17, 31019, 233+x, 50+y);
		tab.child(18, 31020, 325+x, 50+y);
		tab.child(19, 31021, 435+x, 50+y);
		tab.child(20, 31022, 233+x, 110+y);
		tab.child(21, 31023, 325+x, 110+y);
		tab.child(22, 31024, 276+x, 171+y);
		tab.child(23, 31025, 435+x, 158+y);
		for (int i = 0; i < 20; i++) {
			tab.child(24 + i, 31026 + i, positions[i][0] + x, positions[i][1] + y);
			tab.child(44 + i, 31047 + i, positions[i][0] + 17 + x, positions[i][1] + y);
		}
		tab.child(64, 31070, 345+x, 260+y);
		tab.child(65, 31071, 345+x, 260+y);
		tab.child(66, 31073, 14+x, 242+y);
		tab.child(67, 31074, 14+x, 259+y);
		tab.child(68, 31075, 14+x, 276+y);
		tab.child(69, 31076, 14+x, 293+y);
		tab.child(70, 31077, 31+x, 242+y);
		tab.child(71, 31078, 31+x, 259+y);
		tab.child(72, 31079, 31+x, 276+y);
		tab.child(73, 31080, 31+x, 293+y);
		end.width = 140;
		end.height = 56;
		end.scrollMax = 90;
		addToggleButton(31081, 2064, 936, 15, 15, "toggle");
		addToggleButton(31082, 2064, 937, 15, 15, "toggle");
		addToggleButton(31083, 2064, 938, 15, 15, "toggle");
		addToggleButton(31084, 2064, 939, 15, 15, "toggle");
		addToggleButton(31085, 2064, 940, 15, 15, "toggle");
		addText(31086, "Last team standing", tda, 1, 0xff981f, false, true);
		addText(31087, "25 kills", tda, 1, 0xff981f, false, true);
		addText(31088, "50 kills", tda, 1, 0xff981f, false, true);
		addText(31089, "75 kills", tda, 1, 0xff981f, false, true);
		addText(31090, "100 kills", tda, 1, 0xff981f, false, true);
		end.totalChildren(10);
		end.child(0, 31081, 2, 2);
		end.child(1, 31082, 2, 19);
		end.child(2, 31083, 2, 36);
		end.child(3, 31084, 2, 53);
		end.child(4, 31085, 2, 70);
		end.child(5, 31086, 19, 2);
		end.child(6, 31087, 19, 19);
		end.child(7, 31088, 19, 36);
		end.child(8, 31089, 19, 53);
		end.child(9, 31090, 19, 70);
		arena.width = 140;
		arena.height = 60;
		arena.scrollMax = 120;
		addToggleButton(31091, 2063, 941, 15, 15, "toggle");
		addToggleButton(31092, 2063, 942, 15, 15, "toggle");
		addToggleButton(31093, 2063, 943, 15, 15, "toggle");
		addToggleButton(31094, 2063, 944, 15, 15, "toggle");
		addToggleButton(31095, 2063, 945, 15, 15, "toggle");
		addToggleButton(31096, 2063, 946, 15, 15, "toggle");
		addText(31097, "Arena 1", tda, 1, 0xff981f, false, true);
		addText(31098, "Arena 2", tda, 1, 0xff981f, false, true);
		addText(31099, "Arena 3", tda, 1, 0xff981f, false, true);
		addText(31100, "Arena 4", tda, 1, 0xff981f, false, true);
		addText(31101, "Arena 5", tda, 1, 0xff981f, false, true);
		addText(31102, "Arena 6", tda, 1, 0xff981f, false, true);
		arena.totalChildren(12);
		arena.child(0, 31091, 2, 2);
		arena.child(1, 31092, 2, 19);
		arena.child(2, 31093, 2, 36);
		arena.child(3, 31094, 2, 53);
		arena.child(4, 31095, 2, 70);
		arena.child(5, 31096, 2, 87);
		arena.child(6, 31097, 19, 2);
		arena.child(7, 31098, 19, 19);
		arena.child(8, 31099, 19, 36);
		arena.child(9, 31100, 19, 53);
		arena.child(10, 31101, 19, 70);
		arena.child(11, 31102, 19, 87);
	}
	
	public static void addPestControlRewardWidget(BitmapFont[] tda) {
		Interface main = addInterface(37000);
		addSprite(37001, 1936);
		//addButton(37002, 1937, "Confirm");
		//addClickableText(37003, "10 points", "Confirm", tda, 1, 0xFF981F, true, true, 80);
		addHoverButton(37004, 1939, 21, 21, "Close", -1, 37005, 3);
		addHoveredButton(37005, 1940, 21, 21, 37006);
		addText(37007, "1,000 pts", tda, 2, 0xFF981F, false, true);
		
		setChildren(5, main);
		setBounds(37001, 0, 0, 0, main);
		//setBounds(37002, 181, 273, 1, main);
		//setBounds(37003, 253, 300, 2, main);
		setBounds(37004, 463, 14, 1, main);
		setBounds(37005, 463, 14, 2, main);
		setBounds(37007, 35, 19, 3, main);
		setBounds(37010, 27, 43, 4, main);
		
		Interface scroll = addInterface(37010);
		scroll.width = 442;
		scroll.height = 221;
		scroll.scrollMax = 450;
		setChildren(68, scroll);
		int x = 5;
		int y = 5;
		int imageId = 5;
		String[] names = new String[] {
				"Attack xp", "Defence xp", "Magic xp",
				"Prayer xp", "Strength xp", "Range xp",
				"Hitpoints xp"};
		int count = 0;
		for (int index = 0; index < 35; index += 5) {
			addSprite(37012 + index, 1941 + count);
			addText(37013 + index, names[index / 5], tda, 1, 0x339900, false, true);
			addClickableText(37014 + index, "(1 Pt)", "(1 Pt)", tda, 0, 0xFF981F, false, true, 40);
			addClickableText(37015 + index, "(10 Pts)", "(10 Pts)", tda, 0, 0xFF981F, false, true, 40);
			addClickableText(37016 + index, "(100 Pts)", "(100 Pts)", tda, 0, 0xFF981F, false, true, 40);
			setBounds(37012 + index, x, y, index, scroll);
			setBounds(37013 + index, x + 32, y, index + 1, scroll);
			setBounds(37014 + index, x + 32, y + 16, index + 2, scroll);
			setBounds(37015 + index, x + 70, y + 16, index + 3, scroll);
			setBounds(37016 + index, x + 120, y + 16, index + 4, scroll);
			y += 40;
			if (imageId == 8) {
				x += 210;
				y = 5;
			}
			count++;
			imageId++;
		}
		addSprite(37050, 1938);
		setBounds(37050, 53, 165, 35, scroll);
		addSprite(37051, 1938);
		setBounds(37051, 53, 265, 36, scroll);
		x = 5;
		y = 180;
		names = new String[] {
				"Herb Pack", "Seed Pack", "Mineral Pack", "Void Knight Mace", "Void Knight Robe", "Void Mage Helm",
				"Void Melee Helm", "Void Knight Top", "Void Knight Gloves", "Void Ranger Helm"
		};
		int[] items = new int[] {
				257, 5295, 449, 8841, 8840, 11663, 11665, 8839, 8842, 11664
		};
		String[] costs = new String[] {
				"(30 Pts)", "(15 Pts)", "(15 Pts)", "(250 Pts)", "(250 Pts)",
				"(200 Pts)", "(200 Pts)", "(250 Pts)", "(150 Pts)", "(200 Pts)"
		};
		for (int index = 0; index < 30; index += 3) {
			addText(37052 + index, names[index / 3], tda, 1, 0x339900, false, true);
			addClickableText(37053 + index, costs[index / 3], costs[index / 3], tda, 0, 0xFF981F, false, true, 40);
			addToItemGroup(37054 + index, 1, 1, 0, 0, false, "", "", "");
			cache[37054 + index].invId = new int[] { items[index / 3] + 1 };
			cache[37054 + index].invAmt = new int[] { items[index / 3] == 5295 ? 30 : 1 };
			setBounds(37052 + index, x + 36, y, 37 + index, scroll);
			setBounds(37053 + index, x + 36, y + 16, 37 + index + 1, scroll);
			setBounds(37054 + index, x, y, 37 + index + 2, scroll);
			y += 40;
			if (y == 220 && x == 215) {
				x = 5;
				y = 280;
			}
			if (x == 5 && y == 440) {
				x += 210;
				y = 280;
			}
			if (y == 260) {
				x += 210;
				y = 180;
			}
		}
		darken(37084, 200, 40, 0x000000, (byte) 100);
		setBounds(37084, 0, 0, 67, scroll);
	}
	
	public static void darken(int identity, int width, int height, int color, byte transparency) {
		Interface component = addInterface(identity);
		component.id = identity;
		component.type = 17;
		component.width = width;
		component.height = height;
		component.color = color;
		component.alpha = transparency;
	}
	
	public static void Pestpanel(BitmapFont[] tda) {
		Interface RSinterface = addInterface(21119);
		addText(21120, "Next Departure: ", 0x999999, false, true, 52, tda, 1);
		addText(21121, "Players Ready: ", 0x33cc00, false, true, 52, tda, 1);
		addText(21122, "(Need minimum 5 players)", 0xFFcc33, false, true, 52, tda, 1);
		addText(21123, "Points", 0x33ccff, false, true, 52, tda, 1);
		int last = 4;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21120, 15, 12, 0, RSinterface);
		setBounds(21121, 15, 30, 1, RSinterface);
		setBounds(21122, 15, 48, 2, RSinterface);
		setBounds(21123, 15, 66, 3, RSinterface);
	}
	
	public static void Pestpanel2(BitmapFont tda[]) {
		Interface RSinterface = addInterface(21100);
		addSprite(21101, 1930);
		addSprite(21102, 1931);
		addSprite(21103, 1932);
		addSprite(21104, 1933);
		addSprite(21105, 1934);
		addSprite(21106, 1935);
		addText(21107, "", 0xcc00cc, false, true, 52, tda, 1);
		addText(21108, "", 255, false, true, 52, tda, 1);
		addText(21109, "", 0xffff44, false, true, 52, tda, 1);
		addText(21110, "", 0xcc0000, false, true, 52, tda, 1);
		addText(21111, "250", 0x99ff33, false, true, 52, tda, 1);
		addText(21112, "250", 0x99ff33, false, true, 52, tda, 1);
		addText(21113, "250", 0x99ff33, false, true, 52, tda, 1);
		addText(21114, "250", 0x99ff33, false, true, 52, tda, 1);
		addText(21115, "200", 0x99ff33, false, true, 52, tda, 1);
		addText(21116, "0", 0x99ff33, false, true, 52, tda, 1);
		addText(21117, "Time Remaining:", 0xffffff, false, true, 52, tda, 0);
		addText(21118, "", 0xffffff, false, true, 52, tda, 0);
		int last = 18;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21101, 361, 26, 0, RSinterface);
		setBounds(21102, 396, 26, 1, RSinterface);
		setBounds(21103, 436, 26, 2, RSinterface);
		setBounds(21104, 474, 26, 3, RSinterface);
		setBounds(21105, 3, 21, 4, RSinterface);
		setBounds(21106, 3, 50, 5, RSinterface);
		setBounds(21107, 371, 60, 6, RSinterface);
		setBounds(21108, 409, 60, 7, RSinterface);
		setBounds(21109, 443, 60, 8, RSinterface);
		setBounds(21110, 479, 60, 9, RSinterface);
		setBounds(21111, 362, 10, 10, RSinterface);
		setBounds(21112, 398, 10, 11, RSinterface);
		setBounds(21113, 436, 10, 12, RSinterface);
		setBounds(21114, 475, 10, 13, RSinterface);
		setBounds(21115, 32, 32, 14, RSinterface);
		setBounds(21116, 32, 62, 15, RSinterface);
		setBounds(21117, 8, 88, 16, RSinterface);
		setBounds(21118, 87, 88, 17, RSinterface);
	}
	
	public static void boltEnchantInterface(BitmapFont[] tda) {
		addHoverButton(49002, 478, 17, 17, "Close Window", 250, 49003, 3);
		addHoveredButton(49003, 461, 17, 17, 49004);
		Interface tab = cache[49000];
		// Background
		addSprite(49001, 484);
		addHoverButton(49002, 478, 17, 17, "Close Window", 250, 49003, 3);
		addHoveredButton(49003, 461, 17, 17, 49004);
		addText(49086, "Enchant Crossbow Bolts - 10 per click", 0xff9933, true, true, -1, tda, 2); // Title
		addHoverButton(49006, -1, 65, 110, "Enchant Opal Bolts", -1, 49007, 1);
		addText(49008, "Opal", tda, 1, 0xff9040, true, true);
		addText(49009, "@gre@Magic 4", tda, 1, 0xff9040, true, true);
		addItemModel(49005, 564, 1, 1, 850); // Cosmic rune
		addItemModel(49010, 9236, 1, 1, 450); // bolts
		addItemModel(49011, 556, 1, 1, 850); // Air rune
		addText(49012, "@or1@100", 0xff9b00, false, true, 52, fonts, 0); // Cosmic text
		addText(49013, "@or1@200", 0xff9b00, false, true, 52, fonts, 0); // Air text
		addHoverButton(49015, -1, 65, 110, "Enchant Sapphire Bolts", -1, 49007, 1);
		addText(49016, "Sapphire", tda, 1, 0xff9040, true, true);
		addText(49017, "@gre@Magic 7", tda, 1, 0xff9040, true, true);
		addItemModel(49014, 564, 1, 1, 850); // Cosmic rune
		addItemModel(49018, 9240, 1, 1, 450); // bolts
		addItemModel(49019, 555, 1, 1, 850); // Water rune
		addItemModel(49087, 558, 1, 1, 850); // body rune
		addText(49088, "@or1@100", 0xff9b00, false, true, 52, fonts, 0); // Cosmic text
		addText(49020, "@or1@100", 0xff9b00, false, true, 52, fonts, 0); // Cosmic text
		addText(49021, "@or1@100", 0xff9b00, false, true, 52, fonts, 0); // Water text
		addItemModel(49022, 564, 1, 1, 850); // Cosmic rune
		addHoverButton(49023, -1, 65, 110, "Enchant Jade Bolts", -1, 49007, 1);
		addText(49024, "Jade", tda, 1, 0xff9040, true, true);
		addText(49025, "@gre@Magic 14", tda, 1, 0xff9040, true, true);
		addItemModel(49026, 9237, 1, 1, 450); // bolts
		addItemModel(49027, 557, 1, 1, 850); // Earth rune
		addText(49028, "@or1@100", 0xff9b00, false, true, 52, fonts, 0); // Cosmic text
		addText(49029, "@or1@200", 0xff9b00, false, true, 52, fonts, 0); // Earth text
		addItemModel(49030, 564, 1, 1, 850); // Cosmic rune
		addHoverButton(49031, -1, 65, 110, "Enchant Pearl Bolts", -1, 49007, 1);
		addText(49032, "Pearl", tda, 1, 0xff9040, true, true);
		addText(49033, "@gre@Magic 24", tda, 1, 0xff9040, true, true);
		addItemModel(49034, 9238, 1, 1, 450); // bolts
		addItemModel(49035, 555, 1, 1, 850); // Water rune
		addText(49036, "@or1@100", 0xff9b00, false, true, 52, fonts, 0); // Cosmic text
		addText(49037, "@or1@200", 0xff9b00, false, true, 52, fonts, 0); // Water text
		addItemModel(49038, 564, 1, 1, 850); // Cosmic rune
		addHoverButton(49039, -1, 65, 110, "Enchant Emerald Bolts", -1, 49007, 1);
		addText(49040, "Emerald", tda, 1, 0xff9040, true, true);
		addText(49041, "@gre@Magic 27", tda, 1, 0xff9040, true, true);
		addItemModel(49042, 9241, 1, 1, 450); // bolts
		addItemModel(49043, 561, 1, 1, 850); // Nature rune
		addText(49089, "@or1@300", 0xff9b00, false, true, 52, fonts, 0); // cosmic text
		addText(49044, "@or1@100", 0xff9b00, false, true, 52, fonts, 0); // air text
		addText(49045, "@or1@100", 0xff9b00, false, true, 52, fonts, 0); // Nature text
		addItemModel(49046, 564, 1, 1, 850); // Cosmic rune
		addHoverButton(49047, -1, 65, 110, "Enchant Red Topaz Bolts", -1, 49007, 1);
		addText(49048, "Red Topaz", tda, 1, 0xff9040, true, true);
		addText(49049, "@gre@Magic 29", tda, 1, 0xff9040, true, true);
		addItemModel(49050, 9239, 1, 1, 450); // bolts
		addItemModel(49051, 554, 1, 1, 850); // Fire rune
		addText(49052, "@or1@100", 0xff9b00, false, true, 52, fonts, 0); // Cosmic text
		addText(49053, "@or1@200", 0xff9b00, false, true, 52, fonts, 0); // Fire text
		addItemModel(49054, 554, 1, 1, 850); // fire rune
		addHoverButton(49055, -1, 65, 110, "Enchant Ruby Bolts", -1, 49007, 1);
		addText(49056, "Ruby", tda, 1, 0xff9040, true, true);
		addText(49057, "@gre@Magic 49", tda, 1, 0xff9040, true, true);
		addItemModel(49058, 9242, 1, 1, 450); // bolts
		addItemModel(49059, 565, 1, 1, 850); // Astral rune
		addText(49090, "@or1@100", 0xff9b00, false, true, 52, fonts, 0); // cos text
		addText(49060, "@or1@500", 0xff9b00, false, true, 52, fonts, 0); // fire text
		addText(49061, "@or1@100", 0xff9b00, false, true, 52, fonts, 0); // blood text
		addItemModel(49062, 557, 1, 1, 850); // Law rune
		addHoverButton(49063, -1, 65, 110, "Enchant Diamond Bolts", -1, 49007, 1);
		addText(49064, "Diamond", tda, 1, 0xff9040, true, true);
		addText(49065, "@gre@Magic 57", tda, 1, 0xff9040, true, true);
		addItemModel(49066, 9243, 1, 1, 450); // bolts
		addItemModel(49067, 563, 1, 1, 850); // law rune
		addText(49068, "@or1@1k", 0xff9b00, false, true, 52, fonts, 0); // earth text
		addText(49069, "@or1@200", 0xff9b00, false, true, 52, fonts, 0); // law text
		addItemModel(49070, 557, 1, 1, 850); // Soul rune
		addHoverButton(49071, -1, 65, 110, "Enchant Dragonstone Bolts", -1, 49007, 1);
		addText(49072, "Dragonstone", tda, 1, 0xff9040, true, true);
		addText(49073, "@gre@Magic 68", tda, 1, 0xff9040, true, true);
		addItemModel(49074, 9244, 1, 1, 450); // bolts
		addItemModel(49075, 566, 1, 1, 850); // soul rune
		addText(49076, "@or1@2k", 0xff9b00, false, true, 52, fonts, 0); // earth text
		addText(49077, "@or1@100", 0xff9b00, false, true, 52, fonts, 0); // soul text
		addItemModel(49078, 554, 1, 1, 850); // fire rune
		addHoverButton(49079, -1, 65, 110, "Enchant Onyx Bolts", -1, 49007, 1);
		addText(49080, "Onyx", tda, 1, 0xff9040, true, true);
		addText(49081, "@gre@Magic 87", tda, 1, 0xff9040, true, true);
		addItemModel(49082, 9245, 1, 1, 450); // bolts
		addItemModel(49083, 560, 1, 1, 850); // Astral rune
		addText(49084, "@or1@2k", 0xff9b00, false, true, 52, fonts, 0); // fire text
		addText(49085, "@or1@200", 0xff9b00, false, true, 52, fonts, 0); // death text
		tab.totalChildren(96);
		tab.child(0, 49001, 12, 15); // Background
		tab.child(1, 49002, 470, 24); // Close button
		tab.child(2, 49003, 470, 24); // Close button
		tab.child(3, 49005, 40, 155); // Cosmic rune
		tab.child(4, 49006, 30, 60); // options
		tab.child(5, 49008, 62, 53); // title
		tab.child(6, 49009, 62, 67); // magic lvl
		tab.child(7, 49010, 60, 110); // bolts
		tab.child(8, 49011, 70, 155); // Air rune
		tab.child(9, 49012, 32, 170); // Cosmic text
		tab.child(10, 49013, 62, 170); // Air text
		tab.child(11, 49014, 130, 155); // Cosmic rune
		tab.child(12, 49015, 130, 60); // options
		tab.child(13, 49016, 162, 53); // title
		tab.child(14, 49017, 162, 67); // magic lvl
		tab.child(15, 49018, 160, 110); // bolts
		tab.child(16, 49019, 160, 155); // Air rune
		tab.child(17, 49020, 120, 170); // Cosmic text
		tab.child(18, 49021, 152, 170); // Air text
		tab.child(84, 49087, 190, 155); // Air rune
		tab.child(85, 49088, 182, 170); // Air text
		tab.child(19, 49022, 240, 155); // Cosmic rune
		tab.child(20, 49023, 230, 60); // options
		tab.child(21, 49024, 262, 53); // title
		tab.child(22, 49025, 262, 67); // magic lvl
		tab.child(23, 49026, 260, 110); // bolts
		tab.child(24, 49027, 270, 155); // Air rune
		tab.child(25, 49028, 232, 170); // Cosmic text
		tab.child(26, 49029, 262, 170); // Air text
		tab.child(27, 49030, 340, 155); // Cosmic rune
		tab.child(28, 49031, 330, 60); // options
		tab.child(29, 49032, 362, 53); // title
		tab.child(30, 49033, 362, 67); // magic lvl
		tab.child(31, 49034, 360, 110); // bolts
		tab.child(32, 49035, 370, 155); // Air rune
		tab.child(33, 49036, 332, 170); // Cosmic text
		tab.child(34, 49037, 362, 170); // Air text
		tab.child(35, 49038, 420, 155); // Cosmic rune
		tab.child(36, 49039, 430, 60); // options
		tab.child(37, 49040, 462, 53); // title
		tab.child(38, 49041, 462, 67); // magic lvl
		tab.child(39, 49042, 460, 110); // bolts
		tab.child(40, 49043, 475, 155); // Air rune
		tab.child(41, 49044, 412, 170); // Cosmic text
		tab.child(42, 49045, 467, 170); // Air text
		tab.child(86, 49011, 448, 155); // rune
		tab.child(87, 49089, 440, 170); // text
		tab.child(43, 49046, 40, 286); // Cosmic rune
		tab.child(44, 49047, 30, 191); // options
		tab.child(45, 49048, 62, 184); // title
		tab.child(46, 49049, 62, 198); // magic lvl
		tab.child(47, 49050, 60, 241); // bolts
		tab.child(48, 49051, 70, 286); // Air rune
		tab.child(49, 49052, 32, 301); // Cosmic text
		tab.child(50, 49053, 62, 301); // Air text
		tab.child(51, 49054, 150, 286); // fire rune
		tab.child(52, 49055, 130, 191); // options
		tab.child(53, 49056, 162, 184); // title
		tab.child(54, 49057, 162, 198); // magic lvl
		tab.child(55, 49058, 160, 241); // bolts
		tab.child(56, 49059, 180, 286); // Air rune
		tab.child(57, 49060, 142, 301); // fire text
		tab.child(58, 49061, 172, 301); // Air text
		tab.child(88, 49030, 120, 286); // cos rune
		tab.child(89, 49090, 112, 301); // cos text
		tab.child(59, 49062, 252, 286); // Cosmic rune
		tab.child(60, 49063, 230, 191); // options
		tab.child(61, 49064, 262, 184); // title
		tab.child(62, 49065, 262, 198); // magic lvl
		tab.child(63, 49066, 260, 241); // bolts
		tab.child(64, 49067, 280, 286); // Air rune
		tab.child(65, 49068, 247, 301); // Cosmic text
		tab.child(66, 49069, 272, 301); // Air text
		tab.child(90, 49030, 224, 286); // cos rune
		tab.child(91, 49090, 214, 301); // cos text
		tab.child(67, 49070, 349, 286); // Cosmic rune
		tab.child(68, 49071, 330, 191); // options
		tab.child(69, 49072, 362, 184); // title
		tab.child(70, 49073, 362, 198); // magic lvl
		tab.child(71, 49074, 360, 241); // bolts
		tab.child(72, 49075, 378, 286); // Air rune
		tab.child(73, 49076, 345, 301); // Cosmic text
		tab.child(74, 49077, 370, 301); // Air text
		tab.child(92, 49030, 323, 286); // cos rune
		tab.child(93, 49090, 315, 301); // cos text
		tab.child(75, 49078, 444, 286); // Cosmic rune
		tab.child(76, 49079, 430, 191); // options
		tab.child(77, 49080, 462, 184); // title
		tab.child(78, 49081, 462, 198); // magic lvl
		tab.child(79, 49082, 460, 241); // bolts
		tab.child(80, 49083, 475, 286); // Air rune
		tab.child(81, 49084, 439, 301); // Cosmic text
		tab.child(82, 49085, 467, 301); // Air text
		tab.child(94, 49030, 417, 286); // cos rune
		tab.child(95, 49090, 409, 301); // cos text
		tab.child(83, 49086, 262, 25); // Title (Enchant Crossbow Bolts)
	}
	
	public static void decode(Buffer buffer) {
		int parent = -1;
		if(buffer.getBoolean()) {
			parent = buffer.getInt();
		}
		final int id = buffer.getInt();


		final Interface inter = cache[id] = new Interface();

		inter.id = id;
		inter.parent = parent;
		inter.type = buffer.getUByte();
		inter.actionType = buffer.getUByte();
		inter.contentType = buffer.getUShort();
		inter.width = buffer.getUShort();
		inter.height = buffer.getUShort();
		inter.alpha = (byte) buffer.getUByte();
		boolean hover = buffer.getBoolean();
		inter.hoverInterToTrigger = hover ? buffer.getInt() : -1;
		inter.hoverTriggered = buffer.getUByte() == 1;

		final int operators = buffer.getUByte();
		if(operators > 0) {
			inter.valueCompareType = new int[operators];
			inter.requiredValues = new int[operators];
			for(int scriptIndex = 0; scriptIndex < operators; scriptIndex++) {
				inter.valueCompareType[scriptIndex] = buffer.getUByte();
				inter.requiredValues[scriptIndex] = buffer.getUShort();
			}
		}
		final int scripts = buffer.getUByte();
		if(scripts > 0) {
			inter.valueIndexArray = new int[scripts][];
			for(int script = 0; script < scripts; script++) {
				final int length = buffer.getUShort();
				inter.valueIndexArray[script] = new int[length];
				for(int instruction = 0; instruction < length; instruction++) {
					inter.valueIndexArray[script][instruction] = buffer.getUShort();
				}
			}
		}
		if(inter.type == Constants.TYPE_CONTAINER) {
			inter.drawsAlpha = false;
			inter.scrollMax = buffer.getUShort();
			final int count = buffer.getUShort();
			inter.children = new int[count];
			inter.childX = new int[count];
			inter.childY = new int[count];
			for(int j3 = 0; j3 < count; j3++) {
				inter.children[j3] = buffer.getUShort();
				inter.childX[j3] = buffer.getSShort();
				inter.childY[j3] = buffer.getSShort();
			}

		}
		if(inter.type == Constants.WIDGET_MODEL_LIST) {
			buffer.getUShort();
			buffer.getUByte();
		}
		if(inter.type == Constants.WIDGET_INVENTORY) {
			inter.invId = new int[inter.width * inter.height];
			inter.invAmt = new int[inter.width * inter.height];
			inter.invDrag = buffer.getUByte() == 1;
			inter.isInv = buffer.getUByte() == 1;
			inter.invUse = buffer.getUByte() == 1;
			inter.invMove = buffer.getUByte() == 1;
			inter.invPadX = buffer.getUByte();
			inter.invPadY = buffer.getUByte();
			inter.invIconX = new int[20];
			inter.invIconY = new int[20];
			inter.invIcon = new int[20];
			for(int j2 = 0; j2 < 20; j2++) {
				final int k3 = buffer.getUByte();
				if(k3 == 1) {
					inter.invIconX[j2] = buffer.getSShort();
					inter.invIconY[j2] = buffer.getSShort();
					inter.invIcon[j2] = buffer.getSShort();
				}
			}
			inter.menuItem = new String[5];
			for(int l3 = 0; l3 < 5; l3++) {
				inter.menuItem[l3] = buffer.getLine();
				if(inter.menuItem[l3].length() == 0) {
					inter.menuItem[l3] = null;
				}
				if(inter.parent == 1644) {
					inter.menuItem[2] = "Operate";
				}
				if(inter.parent == 3822) {
					inter.menuItem[4] = "Sell X";
				}
				if(inter.parent == 3824) {
					inter.menuItem[4] = "Buy X";
				}
			}
		}
		if(inter.type == Constants.WIDGET_RECTANGLE) {
			inter.rectFilled = buffer.getUByte() == 1;
		}
		if(inter.type == Constants.WIDGET_STRING || inter.type == Constants.WIDGET_MODEL_LIST) {
			inter.textCenter = buffer.getUByte() == 1;
			inter.fontId = buffer.getUByte();
			inter.textShadow = buffer.getUByte() == 1;
			inter.textAlign = buffer.getUByte();
		}
		if(inter.type == Constants.WIDGET_STRING) {
			inter.text = buffer.getLine().replace("Edgeville", "Arrav");
			inter.textAlt = buffer.getLine().replace("Edgeville", "Arrav");
			inter.textAlign = buffer.getUByte();
		}
		if(inter.type == Constants.WIDGET_MODEL_LIST || inter.type == Constants.WIDGET_RECTANGLE || inter.type == Constants.WIDGET_STRING) {
			inter.color = buffer.getInt();
		}
		if(inter.type == Constants.WIDGET_RECTANGLE || inter.type == Constants.WIDGET_STRING) {
			inter.colorAlt = buffer.getInt();
			inter.hoverColor = buffer.getInt();
			inter.hoverColorAlt = buffer.getInt();
		}
		if(inter.type == Constants.WIDGET_SPRITE) {
			inter.drawsAlpha = buffer.getUByte() == 1;
			inter.spriteID = buffer.getSShort();
			inter.secondarySpriteID = buffer.getSShort();
		}
		if(inter.type == Constants.WIDGET_MODEL) {
			int media = buffer.getUByte();
			if(media != 0) {
				inter.modelType = 1;
				inter.modelId = (media - 1 << 8) + buffer.getUByte();
			}
			media = buffer.getUByte();
			if(media != 0) {
				inter.modelTypeAlt = 1;
				inter.modelIdAlt = (media - 1 << 8) + buffer.getUByte();
			}
			media = buffer.getUByte();
			inter.modelAnim = media != 0 ? (media - 1 << 8) + buffer.getUByte() : -1;
			media = buffer.getUByte();
			inter.modelAnimAlt = media != 0 ? (media - 1 << 8) + buffer.getUByte() : -1;
			inter.modelZoom = buffer.getUShort();
			inter.modelYaw = buffer.getUShort();
			inter.modelRoll = buffer.getUShort();
		}
		if(inter.type == Constants.WIDGET_ITEM_LIST) {
			inter.invId = new int[inter.width * inter.height];
			inter.invAmt = new int[inter.width * inter.height];
			inter.textCenter = buffer.getUByte() == 1;
			inter.fontId = buffer.getUByte();
			inter.textShadow = buffer.getUByte() == 1;
			inter.color = buffer.getInt();
			inter.invPadX = buffer.getSShort();
			inter.invPadY = buffer.getSShort();
			inter.isInv = buffer.getUByte() == 1;
			inter.menuItem = new String[5];
			for(int k4 = 0; k4 < 5; k4++) {
				inter.menuItem[k4] = buffer.getLine();
				if(inter.menuItem[k4].length() == 0) {
					inter.menuItem[k4] = null;
				}
			}

		}

		if(inter.actionType == 2 || inter.type == Constants.WIDGET_INVENTORY) {
			inter.selectedActionName = buffer.getLine();
			inter.spellName = buffer.getLine();
			inter.spellUsableOn = buffer.getUShort();
		}
		if(inter.type == Constants.WIDGET_TOOLTIP) {
			inter.text = buffer.getLine();
		}

		if(inter.actionType == 1 || inter.actionType == 4 || inter.actionType == 5 || inter.actionType == 6) {
			inter.tooltip = buffer.getLine();
			if(inter.tooltip.length() == 0) {
				if(inter.actionType == 1) {
					inter.tooltip = "Ok";
				}
				if(inter.actionType == 4) {
					inter.tooltip = "Select";
				}
				if(inter.actionType == 5) {
					inter.tooltip = "Select";
				}
				if(inter.actionType == 6) {
					inter.tooltip = "Continue";
				}
			}
		}
		int actions = buffer.getUShort();
		if(actions != 65535) {
			inter.actions = new String[actions];
			for(int i = 0; i < actions; i++) {
				inter.actions[i] = buffer.getLine();
			}
		}
		
	}

	public static void pack() {
		try {

			for(int i = 16026; i < 17000; i++) {
				cache[i].text = "";
			}
			cache[14777].text = "Soft cowhide";
			cache[14778].text = "Hard cowhide";
			
			final Buffer byteV = new Buffer(new byte[999999999]);
			byteV.putInt(cache.length);
			for(final Interface screen : cache) {
				if(screen == null) {
					continue;
				}
				final int parent = screen.parent;
				byteV.putBoolean(parent != -1);
				if(screen.parent != -1) {
					byteV.putInt(parent);
				}
				byteV.putInt(screen.id);
				byteV.putByte(screen.type);
				byteV.putByte(screen.actionType);
				byteV.putShort(screen.contentType);
				byteV.putShort(screen.width);
				byteV.putShort(screen.height);
				byteV.putByte(screen.alpha);
				if(screen.hoverInterToTrigger != -1) {
					byteV.putByte(1);
					byteV.putInt(screen.hoverInterToTrigger);
				} else {
					byteV.putByte(0);
				}
				byteV.putByte(screen.hoverTriggered ? 1 : 0);
				int array245Count = 0;
				if(screen.valueCompareType != null) {
					array245Count = screen.valueCompareType.length;
				}
				byteV.putByte(array245Count);
				if(array245Count > 0) {
					for(int i = 0; i < array245Count; i++) {
						byteV.putByte(screen.valueCompareType[i]);
						byteV.putShort(screen.requiredValues[i]);
					}
				}
				int valueLength = 0;
				if(screen.valueIndexArray != null) {
					valueLength = screen.valueIndexArray.length;
				}
				byteV.putByte(valueLength);
				if(valueLength > 0) {
					for(int l1 = 0; l1 < valueLength; l1++) {
						final int i3 = screen.valueIndexArray[l1].length;
						byteV.putShort(i3);
						for(int l4 = 0; l4 < i3; l4++) {
							byteV.putShort(screen.valueIndexArray[l1][l4]);
						}
					}
				}
				if(screen.type == 0) {
					byteV.putShort(screen.scrollMax);
					byteV.putShort(screen.children.length);
					for(int i = 0; i < screen.children.length; i++) {
						byteV.putShort(screen.children[i]);
						byteV.putShort(screen.childX[i]);
						byteV.putShort(screen.childY[i]);
					}
				} else if(screen.type == 1) {
					byteV.putShort(0);
					byteV.putByte(0);
				} else if(screen.type == 2) {
					byteV.putByte(screen.invDrag ? 1 : 0);
					byteV.putByte(screen.isInv ? 1 : 0);
					byteV.putByte(screen.invUse ? 1 : 0);
					byteV.putByte(screen.invMove ? 1 : 0);
					byteV.putByte(screen.invPadX);
					byteV.putByte(screen.invPadY);
					for(int i = 0; i < 20; i++) {
						byteV.putByte(screen.invIcon[i] == -1 ? 0 : 1);
						if(screen.invIcon[i] != -1) {
							byteV.putShort(screen.invIconX[i]);
							byteV.putShort(screen.invIconY[i]);
							byteV.putShort(screen.invIcon[i]);
						}
					}
					for(int i = 0; i < 5; i++) {
						if(screen.menuItem != null && screen.menuItem[i] != null) {
							byteV.putLine(screen.menuItem[i]);
						} else {
							byteV.putLine("");
						}
					}
				} else if(screen.type == 3) {
					byteV.putByte(screen.rectFilled ? 1 : 0);
				}
				if(screen.type == 4 || screen.type == 1) {
					byteV.putByte(screen.textCenter ? 1 : 0);
					byteV.putByte(screen.fontId);
					byteV.putByte(screen.textShadow ? 1 : 0);
					byteV.putByte(screen.textAlign);
				}
				if(screen.type == 4) {
					byteV.putLine(screen.text);
					byteV.putLine(screen.textAlt == null ? screen.text : screen.textAlt);
					byteV.putByte(screen.textAlign);
				}
				if(screen.type == 1 || screen.type == 3 || screen.type == 4) {
					byteV.putInt(screen.color);
				}
				if(screen.type == 3 || screen.type == 4) {
					byteV.putInt(screen.colorAlt);
					byteV.putInt(screen.hoverColor);
					byteV.putInt(screen.hoverColorAlt);
				}
				if(screen.type == 5) {
					byteV.putByte(screen.drawsAlpha ? 1 : 0);
					byteV.putShort(screen.spriteID);
					byteV.putShort(screen.secondarySpriteID);
				} else if(screen.type == 6) {
					if(screen.modelType != -1 && screen.modelId > 0) {
						byteV.putShortSpaceSaver(screen.modelId);
					} else {
						byteV.putByte(0);
					}
					if(screen.modelIdAlt > 0) {
						byteV.putShortSpaceSaver(screen.modelIdAlt);
					} else {
						byteV.putByte(0);
					}
					if(screen.modelAnim > 0) {
						byteV.putShortSpaceSaver(screen.modelAnim);
					} else {
						byteV.putByte(0);
					}
					if(screen.modelAnimAlt > 0) {
						byteV.putShortSpaceSaver(screen.modelAnimAlt);
					} else {
						byteV.putByte(0);
					}
					byteV.putShort(screen.modelZoom);
					byteV.putShort(screen.modelYaw);
					byteV.putShort(screen.modelRoll);
				} else if(screen.type == 7) {
					byteV.putByte(screen.textCenter ? 1 : 0);
					byteV.putByte(screen.fontId);
					byteV.putByte(screen.textShadow ? 1 : 0);
					byteV.putInt(screen.color);
					byteV.putShort(screen.invPadX);
					byteV.putShort(screen.invPadY);
					byteV.putByte(screen.isInv ? 1 : 0);
					for(int i = 0; i < 5; i++) {
						if(screen.menuItem[i] != null) {
							byteV.putLine(screen.menuItem[i]);
						} else {
							byteV.putLine("");
						}
					}
				}

				if(screen.actionType == 2 || screen.type == 2) {
					byteV.putLine(screen.selectedActionName == null ? "" : screen.selectedActionName);
					byteV.putLine(screen.spellName == null ? "" : screen.spellName);
					byteV.putShort(screen.spellUsableOn);
				}

				if(screen.type == 8) {
					byteV.putLine(screen.text);
				}
				if(screen.actionType == 1 || screen.actionType == 4 || screen.actionType == 5 || screen.actionType == 6) {
					if(screen.tooltip == null)
						byteV.putLine("");
					else {
						byteV.putLine(screen.tooltip);
					}
				}
				if(screen.actions != null) {
					byteV.putShort(screen.actions.length);
					for(String action : screen.actions) {
						byteV.putLine(action == null ? "" : action);
					}
				} else {
					byteV.putShort(-1);
				}
			}
			final java.io.DataOutputStream dos = new java.io.DataOutputStream(new java.io.FileOutputStream(SignLink.getCacheDir() + "/util/int/data2.dat"));
			dos.write(byteV.data, 0, byteV.pos);
			dos.close();
			System.out.println("Interfaces saved.");
		} catch(final IOException e) {
			System.out.println("Failed to save interfaces.");
			e.printStackTrace();
		}
	}


	public static void addActionButton(int id, int sprite, int sprite2, int width, int height, String s) {
		final Interface rsi = cache[id] = new Interface();
		rsi.spriteID = sprite;
		if(sprite2 == sprite) {
			rsi.secondarySpriteID = sprite;
		} else {
			rsi.secondarySpriteID = sprite2;
		}
		rsi.tooltip = s;
		rsi.contentType = 0;
		rsi.actionType = 1;
		rsi.width = width;
		rsi.hoverInterToTrigger = 52;
		rsi.parent = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
	}

	public static void addButton(int id, int spriteId, String tooltip) {
		final Interface rsi = cache[id] = new Interface();
		rsi.id = id;
		rsi.parent = id;
		rsi.type = 5;
		rsi.actionType = 1;
		rsi.contentType = 0;
		rsi.alpha = (byte) 0;
		rsi.hoverInterToTrigger = 52;
		rsi.spriteID = spriteId;
		rsi.secondarySpriteID = spriteId;
		//rsi.width = rsi.image.imageWidth;
		//rsi.height = rsi.imageAlt.imageHeight;
		rsi.tooltip = tooltip;
	}

	public static void addCacheSprite(int id, int sprite1, int sprite2) {
		final Interface rsi = cache[id] = new Interface();
		rsi.spriteID = sprite1;
		rsi.secondarySpriteID = sprite2;
		rsi.parent = id;
		rsi.id = id;
		rsi.type = 5;
	}

	public static Interface addHead(int id, int width, int height, int zoom) {
		final Interface Widget = addInterface(id);
		Widget.type = 6;
		Widget.modelZoom = zoom;
		Widget.modelRoll = 40;
		Widget.modelYaw = 1900;
		Widget.width = width;
		Widget.height = height;
		return Widget;
	}
	
	public static void addItemModel(int interfaceId, int itemId, int w, int h, int zoom) {
		Interface rsinterface = cache[interfaceId] = new Interface();
		ObjectType itemDef = ObjectType.get(itemId);
		rsinterface.modelRoll = itemDef.iconRoll;
		rsinterface.modelYaw = itemDef.iconYaw;
		rsinterface.type = 6;
		rsinterface.modelType = 4;
		rsinterface.modelId = itemId;
		rsinterface.modelZoom = zoom;
		rsinterface.height = h;
		rsinterface.width = w;
	}

	public static void addHover(int i, int aT, int cT, int hoverid, int sId, int W, int H, String tip) {
		final Interface hover = addInterface(i);
		hover.id = i;
		hover.parent = i;
		hover.type = 5;
		hover.actionType = aT;
		hover.contentType = cT;
		hover.hoverInterToTrigger = hoverid;
		hover.spriteID = sId;
		hover.secondarySpriteID = sId;
		hover.width = W;
		hover.height = H;
		hover.tooltip = tip;
	}

	public static void addHoverBox(int id, int parent, String text, String text2, int configId, int configFrame) {
		final Interface rsi = addInterface(id);
		rsi.id = id;
		rsi.parent = parent;
		rsi.type = 8;
		rsi.textAlt = text;
		rsi.text = text2;
		rsi.valueCompareType = new int[1];
		rsi.requiredValues = new int[1];
		rsi.valueCompareType[0] = 1;
		rsi.requiredValues[0] = configId;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = configFrame;
		rsi.valueIndexArray[0][2] = 0;
	}

	public static void addHoverButton(int id, int imgId, int width, int height, String tooltipText, int contentType, int hoverOver, int actionType) {
		final Interface tab = addInterface(id);
		tab.id = id;
		tab.parent = id;
		tab.type = 5;
		tab.actionType = actionType;
		tab.contentType = contentType;
		tab.alpha = 0;
		tab.hoverInterToTrigger = hoverOver;
		tab.spriteID = imgId;
		tab.secondarySpriteID = imgId;
		tab.width = width;
		tab.height = height;
		tab.tooltip = tooltipText;
	}

	public static void addHovered(int i, int j, int w, int h, int IMAGEID) {
		final Interface hover = addInterface(i);
		hover.parent = i;
		hover.id = i;
		hover.type = 0;
		hover.actionType = 0;
		hover.width = w;
		hover.height = h;
		hover.hoverTriggered = true;
		hover.hoverInterToTrigger = -1;
		addSprite(IMAGEID, j);
		setChildren(1, hover);
		setBounds(IMAGEID, 0, 0, 0, hover);
	}

	public static void addHoveredButton(int id, int imageId, int width, int height, int IMAGEID) {
		final Interface tab = addInterface(id);
		tab.parent = id;
		tab.id = id;
		tab.type = 0;
		tab.actionType = 0;
		tab.width = width;
		tab.height = height;
		tab.hoverTriggered = true;
		tab.alpha = 0;
		tab.hoverInterToTrigger = -1;
		tab.scrollMax = 0;
		addHoverImage(IMAGEID, imageId, imageId);
		tab.totalChildren(1);
		tab.child(0, IMAGEID, 0, 0);
	}

	public static void addHoverImage(int id, int sprite1, int sprite2) {
		final Interface tab = addInterface(id);
		tab.id = id;
		tab.parent = id;
		tab.type = 5;
		tab.actionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.alpha = 0;
		tab.hoverInterToTrigger = 52;
		tab.spriteID = sprite1;
		tab.secondarySpriteID = sprite2;
	}

	public static Interface addTabInterface(int id) {
		Interface tab = createInterface(id);
		tab.id = id;
		tab.parent = id;
		tab.type = Constants.TYPE_CONTAINER;
		tab.actionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 700;
		tab.alpha = (byte) 0;
		tab.hoverInterToTrigger = -1;
		return tab;
	}

	private static Interface createInterface(int id) {
		if (cache[id] != null) {
			System.out.println("overwritten interface: " + id);
		}
		Interface rsi = new Interface();
		cache[id] = rsi;
		return rsi;
	}


	public static Interface addInterface(int id) {
		final Interface rsi = cache[id] = new Interface();
		rsi.id = id;
		rsi.parent = id;
		rsi.type = 0;
		rsi.actionType = 0;
		rsi.contentType = 0;
		rsi.width = 765;
		rsi.height = 503;
		rsi.alpha = (byte) 0;
		rsi.hoverInterToTrigger = -1;
		return rsi;
	}

	public static void addPrayer(int i, int configId, int configFrame, int anIntArray212, String prayerName, int spriteID) {
		final Interface tab = addInterface(i);
		tab.id = i;
		tab.parent = 5608;
		tab.type = 5;
		tab.actionType = 4;
		tab.contentType = 0;
		tab.alpha = 0;
		tab.hoverInterToTrigger = -1;
		tab.spriteID = -1;
		tab.secondarySpriteID = 282;
		tab.width = 34;
		tab.height = 34;
		tab.valueCompareType = new int[1];
		tab.requiredValues = new int[1];
		tab.valueCompareType[0] = 1;
		tab.requiredValues[0] = configId;
		tab.valueIndexArray = new int[1][3];
		tab.valueIndexArray[0][0] = 5;
		tab.valueIndexArray[0][1] = configFrame;
		tab.valueIndexArray[0][2] = 0;
		tab.tooltip = "Activate@or2@ " + prayerName;
		// tab.tooltip = "Select";
		final Interface tab2 = addInterface(i + 1);
		tab2.id = i + 1;
		tab2.parent = 5608;
		tab2.type = 5;
		tab2.actionType = 0;
		tab2.contentType = 0;
		tab2.alpha = 0;
		tab2.hoverInterToTrigger = -1;
		tab2.spriteID = spriteID;
		tab2.secondarySpriteID = spriteID + 1;
		tab2.width = 34;
		tab2.height = 34;
		tab2.valueCompareType = new int[1];
		tab2.requiredValues = new int[1];
		tab2.valueCompareType[0] = 2;
		tab2.requiredValues[0] = anIntArray212 + 1;
		tab2.valueIndexArray = new int[1][3];
		tab2.valueIndexArray[0][0] = 2;
		tab2.valueIndexArray[0][1] = 5;
		tab2.valueIndexArray[0][2] = 0;
		// Widget tab3 = addInterface(i + 50);
	}

	public static void addPrayer(int i, int configId, int configFrame, int requiredValues, int prayerSpriteID, String PrayerName, int Hover) {
		Interface Interface = addInterface(i);
		Interface.id = i;
		Interface.parent = 22500;
		Interface.type = 5;
		Interface.actionType = 4;
		Interface.contentType = 0;
		Interface.alpha = 0;
		Interface.hoverInterToTrigger = Hover;
		Interface.spriteID = 282;
		Interface.secondarySpriteID = -1;
		Interface.width = 34;
		Interface.height = 34;
		Interface.valueCompareType = new int[1];
		Interface.requiredValues = new int[1];
		Interface.valueCompareType[0] = 1;
		Interface.requiredValues[0] = configId;
		Interface.valueIndexArray = new int[1][3];
		Interface.valueIndexArray[0][0] = 5;
		Interface.valueIndexArray[0][1] = configFrame;
		Interface.valueIndexArray[0][2] = 0;
		Interface.tooltip = "Activate@lre@ " + PrayerName;
		Interface = addInterface(i + 1);
		Interface.id = i + 1;
		Interface.parent = 22500;
		Interface.type = 5;
		Interface.actionType = 0;
		Interface.contentType = 0;
		Interface.alpha = 0;
		Interface.spriteID = prayerSpriteID;
		Interface.secondarySpriteID = prayerSpriteID + 1;
		Interface.width = 34;
		Interface.height = 34;
		Interface.valueCompareType = new int[1];
		Interface.requiredValues = new int[1];
		Interface.valueCompareType[0] = 2;
		Interface.requiredValues[0] = requiredValues + 1;
		Interface.valueIndexArray = new int[1][3];
		Interface.valueIndexArray[0][0] = 2;
		Interface.valueIndexArray[0][1] = 5;
		Interface.valueIndexArray[0][2] = 0;
	}

	public static void addRuneText(int ID, int runeAmount, int RuneID, BitmapFont[] font) {
		final Interface Widget = addInterface(ID);
		Widget.id = ID;
		Widget.parent = 1151;
		Widget.type = 4;
		Widget.actionType = 0;
		Widget.contentType = 0;
		Widget.width = 0;
		Widget.height = 14;
		Widget.alpha = 0;
		Widget.hoverInterToTrigger = -1;
		Widget.valueCompareType = new int[1];
		Widget.requiredValues = new int[1];
		Widget.valueCompareType[0] = 3;
		Widget.requiredValues[0] = runeAmount;
		Widget.valueIndexArray = new int[1][4];
		Widget.valueIndexArray[0][0] = 4;
		Widget.valueIndexArray[0][1] = 3214;
		Widget.valueIndexArray[0][2] = RuneID;
		Widget.valueIndexArray[0][3] = 0;
		Widget.textCenter = true;
		Widget.fontId = 0;
		Widget.textShadow = true;
		Widget.text = "%1/" + runeAmount + "";
		Widget.textAlt = "";
		Widget.color = 12582912;
		Widget.colorAlt = 49152;
	}

	public static void addSprite(int ID, int i, int i2, int configId, int configFrame) {
		final Interface Tab = addInterface(ID);
		Tab.id = ID;
		Tab.parent = ID;
		Tab.type = 5;
		Tab.actionType = 0;
		Tab.contentType = 0;
		Tab.width = 512;
		Tab.height = 334;
		Tab.alpha = (byte) 0;
		Tab.hoverInterToTrigger = -1;
		Tab.valueCompareType = new int[1];
		Tab.requiredValues = new int[1];
		Tab.valueCompareType[0] = 1;
		Tab.requiredValues[0] = configId;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		Tab.spriteID = i;
		Tab.secondarySpriteID = i2;
	}

	public static void addSprite(int id, int spriteId) {
		final Interface tab = cache[id] = new Interface();
		tab.id = id;
		tab.parent = id;
		tab.type = 5;
		tab.actionType = 0;
		tab.contentType = 0;
		tab.alpha = (byte) 0;
		tab.hoverInterToTrigger = 52;
		tab.spriteID = spriteId;
		tab.secondarySpriteID = spriteId;
		tab.width = 512;
		tab.height = 334;
	}

	public static Interface addText(int i, String s, int k, boolean l, boolean m, int a, int j) {
		final Interface Widget = addInterface(i);
		Widget.parent = i;
		Widget.id = i;
		Widget.type = 4;
		Widget.actionType = 0;
		Widget.width = 0;
		Widget.height = 0;
		Widget.contentType = 0;
		Widget.alpha = 0;
		Widget.hoverInterToTrigger = a;
		Widget.textCenter = l;
		Widget.textShadow = m;
		Widget.fontId = j;
		Widget.text = s;
		Widget.color = k;
		return Widget;
	}

	public static void addClickableText(int id, String text, String tooltip, BitmapFont tda[], int idx, int color, int hoverColor, int width, int height) {
		Interface Tab = addInterface(id);
		Tab.parent = id;
		Tab.id = id;
		Tab.type = 4;
		Tab.actionType = 1;
		Tab.width = width;
		Tab.height = height;
		Tab.contentType = 0;
		Tab.alpha = 0;
		Tab.hoverInterToTrigger = -1;
		Tab.textCenter = false;
		Tab.textShadow = true;
		Tab.fontId = idx;
		Tab.text = text;
		Tab.tooltip = tooltip;
		Tab.textAlt = "";
		Tab.color = color;
		Tab.colorAlt = hoverColor;
	}

	public static void addText(int i, String s, int k, boolean l, boolean m, int a, BitmapFont[] TDA, int j) {
		final Interface Widget = addInterface(i);
		Widget.parent = i;
		Widget.id = i;
		Widget.type = 4;
		Widget.actionType = 0;
		Widget.width = 0;
		Widget.height = 0;
		Widget.contentType = 0;
		Widget.alpha = 0;
		Widget.hoverInterToTrigger = a;
		Widget.textCenter = l;
		Widget.textShadow = m;
		Widget.fontId = j;
		Widget.text = s;
		Widget.textAlt = "";
		Widget.color = k;
	}

	public static void addText(int id, String text, BitmapFont tda[], int idx, int color, boolean centered) {
		final Interface rsi = cache[id] = new Interface();
		if(centered) {
			rsi.textCenter = true;
		}
		rsi.textShadow = true;
		rsi.fontId = idx;
		rsi.text = text;
		rsi.color = color;
		rsi.id = id;
		rsi.type = 4;
	}

	public static void addText(int id, String text, BitmapFont tda[], int idx, int color, boolean center, boolean shadow) {
		final Interface tab = addInterface(id);
		tab.parent = id;
		tab.id = id;
		tab.type = 4;
		tab.actionType = 0;
		tab.width = 0;
		tab.height = 11;
		tab.contentType = 0;
		tab.alpha = 0;
		tab.hoverInterToTrigger = -1;
		tab.textCenter = center;
		tab.textShadow = shadow;
		tab.fontId = idx;
		tab.text = text;
		tab.textAlt = "";
		tab.color = color;
		tab.colorAlt = 0;
		tab.hoverColor = 0;
		tab.hoverColorAlt = 0;
	}

	public static void addToggleButton(int id, int sprite, int toggledsprite, int setconfig, int width, int height, String s) {
		final Interface rsi = addInterface(id);
		rsi.spriteID = sprite;
		rsi.secondarySpriteID = toggledsprite;
		rsi.requiredValues = new int[1];
		rsi.requiredValues[0] = 1;
		rsi.valueCompareType = new int[1];
		rsi.valueCompareType[0] = 1;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = setconfig;
		rsi.valueIndexArray[0][2] = 0;
		rsi.actionType = 4;
		rsi.width = width;
		rsi.hoverInterToTrigger = -1;
		rsi.parent = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
		rsi.tooltip = s;
	}

	public static void addToggleButton(int id, int sprite, int setconfig, int width, int height, String s) {
		final Interface rsi = addInterface(id);
		rsi.spriteID = sprite;
		rsi.secondarySpriteID = sprite + 1;
		rsi.requiredValues = new int[1];
		rsi.requiredValues[0] = 1;
		rsi.valueCompareType = new int[1];
		rsi.valueCompareType[0] = 1;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = setconfig;
		rsi.valueIndexArray[0][2] = 0;
		rsi.actionType = 4;
		rsi.width = width;
		rsi.hoverInterToTrigger = -1;
		rsi.parent = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
		rsi.tooltip = s;
	}

	public static void addTransparentSprite(int id, int spriteId, int transparency) {
		final Interface tab = cache[id] = new Interface();
		tab.id = id;
		tab.parent = id;
		tab.type = 5;
		tab.actionType = 0;
		tab.contentType = 0;
		tab.alpha = (byte) transparency;
		tab.hoverInterToTrigger = 52;
		tab.spriteID = spriteId;
		tab.secondarySpriteID = spriteId;
		tab.width = 512;
		tab.height = 334;
		tab.drawsAlpha = true;
	}

	public static void drawTooltip(int id, String text) {
		final Interface rsi = addInterface(id);
		rsi.id = id;
		rsi.parent = id;
		rsi.type = 8;
		rsi.text = text;
		rsi.hoverTriggered = true;
		rsi.hoverInterToTrigger = -1;
	}

	public static void method208(boolean flag, Model model) {
		if(flag) {
			return;
		}
		modelcache.clear();
		if(model != null) {
			modelcache.put(5 << 16, model);
		}
	}

	public static void removeSomething(int id) {
		cache[id] = new Interface();
	}

	public static void setBounds(int ID, int X, int Y, int frame, Interface widget) {
		widget.children[frame] = ID;
		widget.childX[frame] = X;
		widget.childY[frame] = Y;
	}

	public static void setChildren(int total, Interface i) {
		i.children = new int[total];
		i.childX = new int[total];
		i.childY = new int[total];
	}

	public static void color(int id, int color) {
		final Interface rsi = cache[id];
		rsi.color = color;
	}

	public static void textSize(int id, BitmapFont tda[], int idx) {
		final Interface rsi = cache[id];
		rsi.fontId = idx;
	}
	
	public static void addToItemGroup(int id, int w, int h, int x, int y, boolean actions, String action1, String action2, String action3) {
		addToItemGroup(addInterface(id), w, h, x, y, actions, action1, action2, action3);
	}
	
	public static void addToItemGroup(Interface rsi, int w, int h, int x, int y, boolean actions, String action1, String action2, String action3) {
		rsi.width = w;
		rsi.height = h;
		rsi.invId = new int[w * h];
		rsi.invAmt = new int[w * h];
		rsi.invUse = false;
		rsi.isInv = false;
		rsi.invPadX = x;
		rsi.invPadY = y;
		rsi.invIconX = new int[20];
		rsi.invIconY = new int[20];
		rsi.invIcon = new int[20];
		rsi.actions = new String[5];
		if (actions) {
			rsi.actions[0] = action1;
			rsi.actions[1] = action2;
			rsi.actions[2] = action3;
		}
		rsi.type = 2;
	}

	public static Interface addContainer(int id) {
		Interface rsi = addInterface(id);
		rsi.width = 4;
		rsi.height = 1;
		rsi.invId = new int[4];
		rsi.invAmt = new int[4];
		rsi.invUse = false;
		rsi.isInv = false;
		rsi.invPadX = 16;
		rsi.invPadY = 16;
		rsi.invIconX = new int[20];
		rsi.invIconY = new int[20];
		rsi.invIcon = new int[20];
		rsi.actions = new String[5];
		rsi.actions[0] = "Withdraw 1";
		rsi.actions[1] = "Withdraw 5";
		rsi.actions[2] = "Withdraw 10";
		rsi.actions[3] = "Withdraw All";
		rsi.actions[4] = "Withdraw X";
		rsi.menuItem = new String[5];
		rsi.menuItem[0] = "Withdraw 1";
		rsi.menuItem[1] = "Withdraw 5";
		rsi.menuItem[2] = "Withdraw 10";
		rsi.menuItem[3] = "Withdraw All";
		rsi.menuItem[4] = "Withdraw X";
		rsi.selectedActionName = "";
		rsi.spellName = "";
		rsi.spellUsableOn = 0;
		rsi.type = 2;
		return rsi;
	}

	/* Add Container */
	static Interface addContainer(int id, int contentType, int width, int height, int xPad, int yPad, int opacity,
									boolean move, boolean displayAmount, boolean displayExamine, String... actions) {
		Interface container = addTabInterface(id);
		container.parent = id;
		container.type = Constants.WIDGET_INVENTORY;
		container.contentType = contentType;
		container.width = width;
		container.height = height;
		container.invIcon = new int[20];
		container.invIconX = new int[20];
		container.invIconY = new int[20];
		container.invPadX = xPad;
		container.invPadY = yPad;
		container.invId = new int[5000];
		container.invAmt = new int[5000];
		container.menuItem = actions;
		container.invDrag = move;
		container.alpha = (byte) opacity;
		//container.displayAmount = displayAmount;
		//container.displayExamine = displayExamine;
		return container;
	}

	/* Add Container */
	static Interface addContainer(int id, int width, int height, int xPad, int yPad, boolean move,
									boolean displayAmount, boolean displayExamine, String... actions) {
		return addContainer(id, 0, width, height, xPad, yPad, 0, move, displayAmount, displayExamine, actions);
	}

	/* Add Container */
	static Interface addContainer(int id, int width, int height, int xPad, int yPad, boolean move,
									String... actions) {
		return addContainer(id, 0, width, height, xPad, yPad, 0, move, true, true, actions);
	}

	/* Add Text */
	static void addText(int id, String text, int idx, int color, boolean centered) {
		Interface rsi = createInterface(id);
		rsi.textCenter = centered;
		rsi.textShadow = true;
		rsi.fontId = idx;
		rsi.text = text;
		rsi.color = color;
		rsi.id = id;
		rsi.type = Constants.WIDGET_STRING;
	}

	/**
	 * Adds an item container layer.
	 */
	public static Interface addContainer(int id, int contentType, int width, int height, String... actions) {
		Interface container = addInterface(id);
		container.parent = id;
		container.type = Constants.WIDGET_INVENTORY;
		container.contentType = contentType;
		container.width = width;
		container.height = height;
		container.invIcon = new int[20];
		container.invIconX = new int[20];
		container.invIconY = new int[20];
		container.invPadX = 14;
		container.invPadY = 4;
		container.invId = new int[5000];
		container.invAmt = new int[5000];
		container.invDrag = true;
		container.menuItem = actions;
		return container;
	}

	/* Add Container */
	public static Interface addContainer2(int id, int contentType, int width, int height, int xPad, int yPad, int opacity,
										  boolean move, boolean displayAmount, boolean displayExamine, String... actions) {
		Interface container = addTabInterface(id);
		container.parent = id;
		container.type = Constants.WIDGET_INVENTORY;
		container.contentType = contentType;
		container.width = width;
		container.height = height;
		container.invIcon = new int[20];
		container.invIconX = new int[20];
		container.invIconY = new int[20];
		container.invPadX = xPad;
		container.invPadY = yPad;
		container.invId = new int[5000];
		container.invAmt = new int[5000];
		container.menuItem = actions;
		container.invDrag = move;
		container.alpha = (byte) opacity;
		//container.displayAmount = displayAmount;
		//container.displayExamine = displayExamine;
		return container;
	}

	public static Interface addContainer3(int id, int contentType, int width, int height, int xPad, int yPad, int opacity,
										  boolean move, boolean displayAmount, boolean displayExamine, String... actions) {
		Interface container = addTabInterface(id);
		container.parent = id;
		container.type = Constants.WIDGET_INVENTORY;
		container.contentType = contentType;
		container.width = width;
		container.height = height;
		container.invIcon = new int[20];
		container.invIconX = new int[20];
		container.invIconY = new int[20];
		container.invPadX = xPad;
		container.invPadY = yPad;
		container.invId = new int[5000];
		container.invAmt = new int[5000];
		container.menuItem = actions;
		container.invDrag = move;
		container.alpha = (byte) opacity;
		//container.displayAmount = displayAmount;
		//container.displayExamine = displayExamine;
		return container;
	}

	public static void addButton(int i, int j, int W, int H, String S, int AT) {
		Interface Interface = addInterface(i);
		Interface.id = i;
		Interface.parent = i;
		Interface.type = 5;
		Interface.actionType = AT;
		Interface.contentType = 0;
		Interface.alpha = 0;
		Interface.hoverInterToTrigger = 52;
		Interface.spriteID = j;
		Interface.secondarySpriteID = j;
		Interface.width = W;
		Interface.height = H;
		Interface.tooltip = S;
	}

	public static void addConfigButton(int ID, int pID, int bID, int bID2, int width, int height, String tT, int configID, int aT, int configFrame) {
		Interface Tab = addInterface(ID);
		Tab.parent = pID;
		Tab.id = ID;
		Tab.type = 5;
		Tab.actionType = aT;
		Tab.contentType = 0;
		Tab.width = width;
		Tab.height = height;
		Tab.alpha = 0;
		Tab.hoverInterToTrigger = -1;
		Tab.valueCompareType = new int[1];
		Tab.requiredValues = new int[1];
		Tab.valueCompareType[0] = 1;
		Tab.requiredValues[0] = configID;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		Tab.spriteID = bID;
		Tab.secondarySpriteID = bID2;
		Tab.tooltip = tT;
	}
	public static void addConfigButton(int ID, int pID, int bID, int bID2, int width, int height, String tT, int configID, int aT, int configFrame, boolean updateConfig) {
		Interface Tab = addInterface(ID);
		Tab.parent = pID;
		Tab.id = ID;
		Tab.type = 5;
		Tab.actionType = aT;
		Tab.contentType = 0;
		Tab.width = width;
		Tab.height = height;
		Tab.alpha = 0;
		Tab.hoverInterToTrigger = -1;
		Tab.valueCompareType = new int[1];
		Tab.requiredValues = new int[1];
		Tab.valueCompareType[0] = 1;
		Tab.requiredValues[0] = configID;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		Tab.spriteID = bID;
		Tab.secondarySpriteID = bID2;
		Tab.tooltip = tT;
		Tab.updateConfig = updateConfig;
	}

	/* Adds Input Field */
	protected static void addInputField(int identity, int characterLimit, int color, String text, int width, int height,
										boolean asterisks, boolean updatesEveryInput, String regex) {
		Interface field = addInterface(identity);
		field.id = identity;
		field.type = Constants.WIDGET_INPUT_FIELD;
		field.actionType = 8;
		field.text = text;
		field.width = width;
		field.height = height;
		field.characterLimit = characterLimit;
		field.color = color;
		field.displayAsterisks = asterisks;
		field.tooltip = text;
		field.defaultInputFieldText = text;
		field.updatesEveryInput = updatesEveryInput;
		field.inputRegex = regex;
	}

	public static void addInputField(int identity, int characterLimit, int color, String text, int width, int height,
									 boolean asterisks, boolean updatesEveryInput) {
		Interface field = addInterface(identity);
		field.id = identity;
		field.type = Constants.WIDGET_INPUT_FIELD;
		field.actionType = 8;
		field.text = text;
		field.width = width;
		field.height = height;
		field.characterLimit = characterLimit;
		field.color = color;
		field.displayAsterisks = asterisks;
		field.defaultInputFieldText = text;
		field.updatesEveryInput = updatesEveryInput;
	}

	public static void addInputField(int identity, int characterLimit, int color, String text, int width, int height,
									 boolean asterisks) {
		Interface field = addInterface(identity);
		field.id = identity;
		field.type = Constants.WIDGET_INPUT_FIELD;
		field.actionType = 8;
		field.text = text;
		field.width = width;
		field.height = height;
		field.characterLimit = characterLimit;
		field.color = color;
		field.displayAsterisks = asterisks;
		field.defaultInputFieldText = text;
	}

	public static void addClickableText(int id, String text, String tooltip, BitmapFont tda[], int idx, int color, boolean center, boolean shadow, int width) {
		Interface tab = addInterface(id);
		tab.parent = id;
		tab.id = id;
		tab.type = 4;
		tab.actionType = 1;
		tab.width = width;
		tab.height = 11;
		tab.contentType = 0;
		tab.alpha = 0;
		tab.hoverInterToTrigger = -1;
		tab.textCenter = center;
		tab.textShadow = shadow;
		tab.fontId = idx;
		tab.text = text;
		tab.textAlt = "";
		tab.color = color;
		tab.hoverColor = 0xffffff;
		tab.hoverColorAlt = 0;
		tab.tooltip = tooltip;
	}

	public void child(int id, int interID, int x, int y) {
		children[id] = interID;
		childX[id] = x;
		childY[id] = y;
	}

	public Model getModel(int anim1, int anim2, boolean selected) {
		Model model;
		if(selected) {
			model = method206(modelTypeAlt, modelIdAlt);
		} else {
			model = method206(modelType, modelId);
		}
		if(model == null) {
			return null;
		}
		if(anim2 == -1 && anim1 == -1 && model.triFill == null) {
			return model;
		}
		final Model model_1 = new Model(true, AnimationFrame.isNull(anim2) & AnimationFrame.isNull(anim1), false, model);
		if(anim2 != -1 || anim1 != -1) {
			model_1.applyEffects();
		}
		if(anim2 != -1) {
			model_1.applyAnimation(anim2);
		}
		if(anim1 != -1) {
			model_1.applyAnimation(anim1);
		}
		model_1.calculateLighting(74, 850, -30, -50, -30, true);
		return model_1;
	}

	public Model method206(int i, int j) {
		Model model = (Model) modelcache.get((i << 16) + j);
		if(model != null) {
			return model;
		}
		if(i == 1) {
			model = Model.fetchModel(j);
		}
		if(i == 2) {
			model = NPCType.get(j).method160();
		}
		if(i == 3) {
			model = client.localPlayer.getModel();
		}
		if(i == 4) {
			model = ObjectType.get(j).getItemModel(50);
		}
		if(i == 5) {
			model = null;
		}
		if(model != null) {
			modelcache.put((i << 16) + j, model);
		}
		return model;
	}

	public void invSwap(int i1, int i2) {
		int temp = invId[i1];
		invId[i1] = invId[i2];
		invId[i2] = temp;
		temp = invAmt[i1];
		invAmt[i1] = invAmt[i2];
		invAmt[i2] = temp;
	}

	public void totalChildren(int t) {
		children = new int[t];
		childX = new int[t];
		childY = new int[t];
	}

	public static void addItemIcon(int id, int icon) {
		final Interface tab = addInterface(id);
		tab.parent = id;
		tab.id = id;
		tab.type = 5;
		tab.actionType = 0;
		tab.width = 32;
		tab.height = 32;
		tab.spriteID = -icon;
		tab.alpha = 0;
		tab.hoverInterToTrigger = -1;
		tab.textAlt = "";
		tab.colorAlt = 0;
		tab.hoverColor = 0;
		tab.hoverColorAlt = 0;
	}

	public static void addHoverText(int id, String text, String tooltip, int idx, int color,
									boolean centerText, boolean textShadowed, int width) {
		Interface rsinterface = addInterface(id);
		rsinterface.id = id;
		rsinterface.parent = id;
		rsinterface.type = Constants.WIDGET_STRING;
		rsinterface.actionType = 1;
		rsinterface.width = width;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.alpha = 0;
		rsinterface.hoverInterToTrigger = -1;
		rsinterface.textCenter = centerText;
		rsinterface.textShadow = textShadowed;
		rsinterface.fontId = idx;
		rsinterface.text = text;
		if (text.contains("\\n")) {
			rsinterface.height += 11;
		}
		rsinterface.textAlt = "";
		rsinterface.tooltip = tooltip;
		rsinterface.color = color;
		rsinterface.colorAlt = 0;
		rsinterface.hoverColor = 0xFFFFFF;
		rsinterface.hoverColorAlt = 0;
	}

	public static void addHoverText(int id, String text, String tooltip, int idx, int color,
									int hoverColor, boolean centerText, boolean textShadowed, int width) {
		Interface rsinterface = addInterface(id);
		rsinterface.id = id;
		rsinterface.parent = id;
		rsinterface.type = Constants.WIDGET_STRING;
		rsinterface.actionType = 1;
		rsinterface.width = width;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.alpha = 0;
		rsinterface.hoverInterToTrigger = -1;
		rsinterface.textCenter = centerText;
		rsinterface.textShadow = textShadowed;
		rsinterface.fontId = idx;
		rsinterface.text = text;
		rsinterface.textAlt = "";
		rsinterface.tooltip = tooltip;
		rsinterface.color = color;
		rsinterface.colorAlt = 0;
		rsinterface.hoverColor = hoverColor;
		rsinterface.hoverColorAlt = 0;
	}

	/**
	 * Adds a button.
	 */
	public static void addButton(int id, int enabled, int disabled, String tooltip, int w, int h) {
		Interface tab = createInterface(id);
		tab.id = id;
		tab.parent = id;
		tab.type = Constants.WIDGET_SPRITE;
		tab.actionType = 1;
		tab.contentType = 0;
		tab.alpha = (byte) 0;
		tab.hoverInterToTrigger = 52;
		tab.spriteID = enabled;
		tab.secondarySpriteID = disabled;
		tab.width = w;
		tab.height = h;
		tab.tooltip = tooltip;
	}

	/**
	 * Adds a config button hover layer.
	 */
	public static void addHoverConfigButton(int id, int hoverOver, int disabledID, int enabledID, int width, int height,
											String tooltip, int[] anIntArray245, int[] anIntArray212, int[][] valueIndexArray) {
		Interface rsint = addTabInterface(id);
		rsint.parent = id;
		rsint.id = id;
		rsint.type = Constants.WIDGET_SPRITE;
		rsint.actionType = 5;
		rsint.contentType = 206;
		rsint.width = width;
		rsint.height = height;
		rsint.alpha = 0;
		rsint.hoverInterToTrigger = hoverOver;
		rsint.valueCompareType = anIntArray245;
		rsint.requiredValues = anIntArray212;
		rsint.valueIndexArray = valueIndexArray;
		rsint.spriteID = enabledID;
		rsint.secondarySpriteID = disabledID;
		rsint.tooltip = tooltip;
	}

	/**
	 * Adds a config button hover layer.
	 */
	public static void addHoveredConfigButton(Interface original, int ID, int IMAGEID, int disabledID, int enabledID) {
		Interface rsint = addTabInterface(ID);
		rsint.parent = original.id;
		rsint.id = ID;
		rsint.type = 0;
		rsint.actionType = 0;
		rsint.contentType = 0;
		rsint.width = original.width;
		rsint.height = original.height;
		rsint.alpha = 0;
		rsint.hoverInterToTrigger = -1;
		Interface hover = addInterface(IMAGEID);
		hover.type = Constants.WIDGET_SPRITE;
		hover.width = original.width;
		hover.height = original.height;
		hover.valueCompareType = original.valueCompareType;
		hover.requiredValues = original.requiredValues;
		hover.valueIndexArray = original.valueIndexArray;
		hover.spriteID = enabledID;
		hover.secondarySpriteID = disabledID;
		rsint.totalChildren(1);
		setBounds(IMAGEID, 0, 0, 0, rsint);
		rsint.tooltip = original.tooltip;
		rsint.hoverTriggered = true;
	}

	public static void addTabMenu(int id, int textColor, int spriteBack, Tab tab, String... options) {
		Interface dropdownMenu = addInterface(id);
		dropdownMenu.type = Constants.WIDGET_TAB;
		dropdownMenu.actionType = Constants.WIDGET_TAB;
		dropdownMenu.color = textColor;
		dropdownMenu.tab = new TabMenu(tab, spriteBack, options);
	}

	public DropdownMenu dropDown;
	public Interface dropDownOpen;
	public int dropDownHover = -1;

	public static void addDropdownMenu(int identification, int width, int defaultOption, boolean split, boolean center,
									   Dropdown dropdown, String... options) {
		addDropdownMenu(identification, width, defaultOption, 0xFD961E, split, center, dropdown, options);
	}

	public static void addDropdownMenu(int identification, int width, int defaultOption, int textColor, boolean split,
									   boolean center, Dropdown dropdown, String... options) {
		Interface dropdownMenu = addInterface(identification);
		dropdownMenu.type = Constants.WIDGET_DROP_DOWN;
		dropdownMenu.actionType = 69;
		dropdownMenu.textCenter = center;
		dropdownMenu.color = textColor;
		dropdownMenu.text = "";
		dropdownMenu.dropDown = new DropdownMenu(width, defaultOption, dropdown, options);
	}

}
