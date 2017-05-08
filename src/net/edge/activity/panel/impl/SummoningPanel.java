package net.edge.activity.panel.impl;

import net.edge.activity.panel.Panel;
import net.edge.cache.unit.ImageCache;
import net.edge.cache.unit.Interface;
import net.edge.cache.unit.ObjectType;
import net.edge.game.Scene;
import net.edge.media.Rasterizer2D;
import net.edge.media.img.BitmapImage;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class SummoningPanel extends Panel {
	
	private enum Familiar {
		SPIRIT_WOLF("Spirit wolf", 12047, "Howl", 12425, 1),
		DREADFOWL("Dreadfowl", 12043, "Dreadfowl Strike", 12445, 4),
		SPIRIT_SPIDER("Spirit spider", 12059, "Egg Spawn", 12428, 10),
		THORNY_SNAIL("Thorny snail", 12019, "Slime Spray", 12459, 13),
		GRANITE_CRAB("Granite crab", 12009, "Stony Shell", 12533, 16),
		SPIRIT_MOSQUITO("Spirit mosquito", 12778, "Pester", 12838, 17),
		DESERT_WYRM("Desert wyrm", 12049, "Electric Lash", 12460, 18),
		SPIRIT_SCORPION("Spirit scorpion", 12055, "Venom Shot", 12432, 19),
		SPIRIT_TZ_KIH("Spirit tz-kih", 12808, "Fireball Assault", 12839, 22),
		ALBINO_RAT("Albino rat", 12067, "Cheese Feast", 12430, 23),
		SPIRIT_KALPHITE("Spirit kalphite", 12063, "Sandstorm", 12446, 25),
		COMPOST_MOUND("Compost mound", 12091, "Generate Compost", 12440, 28),
		GIANT_CHINCHOMPA("Giant chinchompa", 12800, "Explode", 12834, 29),
		VAMPIRE_BAT("Vampire bat", 12053, "Vampire Touch", 12447, 31),
		HONEY_BADGER("Honey badger", 12065, "Insane Ferocity", 12433, 32),
		BEAVER("Beaver", 12021, "Multichop", 12429, 33),
		VOID_RAVAGER("Void ravager", 12818, "Call of Arms", 12443, 34),
		VOID_SPINNER("Void spinner", 12780, "Call of Arms", 12443, 34),
		VOID_TORCHER("Void torcher", 12798, "Call of Arms", 12443, 34),
		VOID_SHIFTER("Void shifter", 12814, "Call of Arms", 12443, 34),
		BRONZE_MINOTAUR("Bronze minotaur", 12073, "Bronze Bull Rush", 12461, 36),
		BULL_ANT("Bull ant", 12087, "Unburden", 12431, 40),
		MACAW("Macaw", 12071, "Herbcall", 12422, 41),
		//EVIL_TURNIP("Evil turnip", 12051, "Evil Flames", 12448, 42),
		SP_COCKATRICE("Sp. cockatrice", 12095, "Petrifying gaze", 12458, 43),
		SP_GUTHATRICE("Sp. guthatrice", 12097, "Petrifying gaze", 12458, 43),
		SP_SARATRICE("Sp. saratrice", 12099, "Petrifying gaze", 12458, 43),
		SP_ZAMATRICE("Sp. zamatrice", 12101, "Petrifying gaze", 12458, 43),
		SP_PENGATRICE("Sp. pengatrice", 12103, "Petrifying gaze", 12458, 43),
		SP_CORAXATRICE("Sp. coraxatrice", 12105, "Petrifying gaze", 12458, 43),
		SP_VULATRICE("Sp. vulatrice", 12107, "Petrifying gaze", 12458, 43),
		IRON_MINOTAUR("Iron minotaur", 12075, "Iron Bull Rush", 12462, 46);
		/*PYRELORD("Pyrelord", 12816, "Immense Heat", 12829, 46),
		MAGPIE("Magpie", 12041, "Thieving Fingers", 12426, 47),
		BLOATED_LEECH("Bloated leech", 12061, "Blood Drain", 12444, 49),
		SPIRIT_TERRORBIRD("Spirit terrorbird", 12007, "Tireless Run", 12441, 52),
		ABYSSAL_PARASITE("Abyssal parasite", 12035, "Abyssal Drain", 12454, 54),
		SPIRIT_JELLY("Spirit jelly", 12027, "Dissolve", 12453, 55),
		STEEL_MINOTAUR("Steel minotaur", 12077, "Steel Bull Rush", 12463, 56),
		IBIS("Ibis", 12531, "Fish Rain", 12424, 56),
		SPIRIT_GRAAHK("Spirit graahk", 12810, "Goad", 12835, 57),
		SPIRIT_KYATT("Spirit kyatt", 12812, "Ambush", 12836, 57),
		SPIRIT_LARUPIA("Spirit larupia", 12784, "Rending", 12840, 57),
		KARAM_OVERLORD("Karam. overlord", 12023, "Doomsphere Device", 12455, 58),
		SMOKE_DEVIL("Smoke devil", 12085, "Dust Cloud", 12468, 61),
		ABYSSAL_LURKER("Abyssal lurker", 12037, "Abyssal Stealth", 12427, 62),
		SPIRIT_COBRA("Spirit cobra", 12015, "Ophidian Incubation", 12436, 63),
		STRANGER_PLANT("Stranger plant", 12045, "Poisonous Blast", 12467, 64),
		MITHRIL_MINOTAUR("Mithril minotaur", 12079, "Mithril Bull Rush", 12464, 66),
		BARKER_TOAD("Barker toad", 12123, "Toad Bark", 12452, 66),
		WAR_TORTOISE("War tortoise", 12031, "Testudo", 12439, 67),
		BUNYIP("Bunyip", 12029, "Swallow Whole", 12438, 68),
		FRUIT_BAT("Fruit bat", 12033, "Fruitfall", 12423, 69),
		RAVENOUS_LOCUST("Ravenous locust", 12820, "Famine", 12830, 70),
		ARCTIC_BEAR("Arctic bear", 12057, "Arctic Blast", 12451, 71),
		PHOENIX("Phoenix", 14623, "Rise from the Ashes", 14622, 72),
		OBSIDIAN_GOLEM("Obsidian golem", 12792, "Volcanic Strength", 12826, 73),
		GRANITE_LOBSTER("Granite lobster", 12069, "Crushing Claw", 12449, 74),
		PRAYING_MANTIS("Praying mantis", 12011, "Mantis Strike", 12450, 75),
		ADAMANT_MINOTAUR("Adamant minotaur", 12081, "Adamant Bull Rush", 12465, 76),
		FORGE_REGENT("Forge regent", 12782, "Inferno", 12841, 76),
		TALON_BEAST("Talon beast", 12794, "Deadly Claw", 12831, 77),
		GIANT_ENT("Giant ent", 12013, "Acorn Missile", 12457, 78),
		FIRE_TITAN("Fire titan", 12802, "Titan's Consitution", 12824, 79),
		MOSS_TITAN("Moss titan", 12804, "Titan's Consitution", 12824, 79),
		ICE_TITAN("Ice titan", 12806, "Titan's Consitution", 12824, 79),
		HYDRA("Hydra", 12025, "Regrowth", 12442, 80),
		SPIRIT_DAGANNOTH("Spirit dagannoth", 12017, "Spike Shot", 12456, 83),
		LAVA_TITAN("Lava titan", 12788, "Ebon Thunder", 12837, 83),
		SWAMP_TITAN("Swamp titan", 12776, "Swamp Plague", 12832, 85),
		RUNE_MINOTAUR("Rune minotaur", 12083, "Rune Bull Rush", 12466, 86),
		UNICORN_STALLION("Unicorn stallion", 12039, "Healing Aura", 12434, 88),
		GEYSER_TITAN("Geyser titan", 12786, "Boil", 12833, 89),
		WOLPERTINGER("Wolpertinger", 12089, "Magic Focus", 12437, 92),
		ABYSSAL_TITAN("Abyssal titan", 12796, "Essence Shipment", 12827, 93),
		IRON_TITAN("Iron titan", 12822, "Iron Within", 12828, 95),
		PACK_YAK("Pack yak", 12093, "Winter Storage", 12435, 96),
		STEEL_TITAN("Steel titan", 12790, "Steel of Legends", 12825, 99);*/
		
		String pouchName, scrollName;
		int pouchId, scrollId, level;
		
		Familiar(String pouchName, int pouchId, String scrollName, int scrollId, int level) {
			this.pouchName = pouchName;
			this.pouchId = pouchId;
			this.scrollName = scrollName;
			this.scrollId = scrollId;
			this.level = level;
		}
	}
	
	/**
	 * An array of all familiars.
	 */
	private final Familiar[] familiars = Familiar.values();
	
	/**
	 * Summoning level
	 */
	private int summoningLevel = 1;

	/**
	 * Scrolling position.
	 */
	private int scrollPos = 0;

	/**
	 * The max scrolling position.
	 */
	private int scrollMax = 0;

	/**
	 * The condition if the scroll is being dragged.
	 */
	private boolean scrollDrag = false;

	/**
	 * The scrolling dragging position.
	 */
	private int scrollDragPos = 0;
	
	/**
	 * Condition if back button displayed.
	 */
	private static boolean back = false;

	@Override
	public String id() {
		return "shop";
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

		if(Interface.cache[259] == null) {
			Interface.cache[259] = Interface.addInterface(259);
		}

		scrollMax = Math.max(55 * ((client.currentShopInterfacePrices.length + 5) / 6) - 285, 0);

		/* Scrolling */
		if(client.mouseInRegion(beginX + 5, beginY + 40, beginX + 493, beginY + 365)) {
			scrollPos += client.mouseWheelAmt * 24;
			if(scrollPos < 0) {
				scrollPos = 0;
			}
			if(scrollPos > scrollMax) {
				scrollPos = scrollMax;
			}
		}
		if(!scrollDrag) {
			int height = 278;
			if(scrollMax > 0) {
				height = 285 * 278 / (scrollMax + 285);
			}
			int pos = 0;
			if(scrollPos != 0) {
				pos = scrollPos * 278 / (scrollMax + 285) + 1;
			}
			int x = 481 + beginX;
			int y = 46 + pos + beginY;
			if(client.mouseDragButton == 1 && client.mouseInRegion(x, y, x + 10, y + height)) {
				scrollDrag = true;
				scrollDragPos = scrollPos;
			}
		} else if(client.mouseDragButton != 1) {
			scrollDrag = false;
		} else {
			int d = (client.mouseY - client.clickY) * (scrollMax + 285) / 278;
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
		
		if(back) {
			if(client.leftClickInRegion(beginX + 382, beginY + 12, beginX + 438, beginY + 42)) {
				client.panelHandler.open(new CounterPanel());
			}
		}

		/* Adding item */
		if(client.mouseInRegion(beginX + 5, beginY + 40, beginX + 493, beginY + 330)) {
			int offset = -scrollPos + 45;
			for(int i = 0; i < client.currentShopInterfacePrices.length; i++) {
				int icon = Interface.cache[3900].invId[i];
				if(icon == 0)
					continue;
				int x = i % 6 * 78;
				//if(beginY + offset + 38 > 366) {
				//	break;
				//}
				
				if(client.rightClickInRegion(beginX + 8 + x, beginY + offset, beginX + 80 + x, beginY + offset + 50)) {
					client.menuOpened = false;
					String name = ObjectType.get(icon).name;
					String[] actions = {"C", "200", "50", "10", "5", "1", "0"};
					client.menuPos = 0;
					for(int p = 0; p < actions.length; p++) {
						if(actions[p].equals("C")) {
							client.menuItemName[p] = "Cancel";
							client.menuItemCode[p] = 1107;
							client.menuPos++;
						} else {
							boolean value = actions[p].equals("0");
							client.menuItemName[p] = value ? "Value @ban@" + name : "Buy " + actions[p] + " @ban@" + name;
							client.menuItemCode[p] = 1900;
							client.menuItemArg3[p] = Integer.parseInt(actions[p]);
							client.menuItemArg2[p] = i;
							client.menuItemArg1[p] = icon;
							client.menuPos++;
						}
					}
				}
				
				if(!client.menuOpened && client.leftClickInRegion(beginX + 8 + x, beginY + offset, beginX + 80 + x, beginY + offset + 50)) {
					client.menuOpened = false;
					client.outBuffer.putOpcode(145);
					client.outBuffer.putShortPlus128(3900);
					client.outBuffer.putShortPlus128(0);
					client.outBuffer.putShortPlus128(icon);
				}
				
				offset += i % 6 == 5 ? 55 : 0;
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

		fancyFont.drawCenteredString("Exit", beginX + 467, beginY + 27, 0xF3B13F);
		Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 20, 2, 0xF3B13F, 60);
		if(client.mouseInRegion(beginX + 442, beginY + 12, beginX + 498, beginY + 42)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 20, 2, 0xF3B13F, 20);
		}
		if(back) {
			fancyFont.drawCenteredString("Back", beginX + 407, beginY + 27, 0xF3B13F);
			Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 12, 54, 20, 2, 0xF3B13F, 60);
			if(client.mouseInRegion(beginX + 382, beginY + 12, beginX + 438, beginY + 42)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 12, 54, 20, 2, 0xF3B13F, 20);
			}
		}
		fancyFont.drawLeftAlignedEffectString("Summoning Pouch Creation - Level: " + summoningLevel, beginX + 20, beginY + 28, 0xF3B13F, true);

		/* Shop content */
		Rasterizer2D.drawRectangle(beginX + 4, beginY + 39, 490, 292, 0xffffff, 80);
		Rasterizer2D.fillRectangle(beginX + 5, beginY + 40, 488, 290, 0xffffff, 60);
		Rasterizer2D.setClip(beginX + 5, beginY + 40, beginX + 493, beginY + 330);
		int offset = -scrollPos + 45;
		String tooltip = "";
		for(int i = 0; i < familiars.length; i++) {
			int x = i % 6 * 78;
			Rasterizer2D.fillRoundedRectangle(beginX + 8 + x, beginY + offset, 72, 50, 3, 0x000000, 100);
			if(!client.menuOpened && client.mouseInRegion(beginX + 8 + x, beginY + offset, beginX + 80 + x, beginY + offset + 50)) {
				Rasterizer2D.fillRectangle(beginX + 8 + x, beginY + offset, 72, 50, 0, 40);
				tooltip = familiars[i].pouchName;
			}
			final BitmapImage img = ObjectType.getIcon(familiars[i].pouchId, 1, 0);
			if(img != null) {
				img.drawImage(beginX + 28 + x, beginY + offset + 2);
			}
			int reqLvl = familiars[i].level;
			smallFont.drawLeftAlignedEffectString((summoningLevel >= reqLvl ? "@or1@" : "@red@") + reqLvl, beginX + 12 + x, beginY + offset + 14, 0xF3B13F, true);
			smallFont.drawCenteredEffectString((summoningLevel >= reqLvl ? "@or1@Lvl " : "@or2@Lvl ") + reqLvl, beginX + 44 + x, beginY + offset + 46, 0xF3B13F, true);
			offset += i % 6 == 5 ? 55 : 0;
		}
		if(!tooltip.isEmpty()) {
			boolean off = (client.mouseX + smallFont.getStringWidth(tooltip)) > 490;
			Rasterizer2D.fillRoundedRectangle(client.mouseX + (off ? -(smallFont.getStringWidth(tooltip) + 18) : 8), client.mouseY - 3, smallFont.getStringWidth(tooltip) + 7, 15, 3, 0x000000, 200);
			smallFont.drawLeftAlignedEffectString(tooltip, client.mouseX + (off ? -(smallFont.getStringWidth(tooltip) + 14) : 12), client.mouseY + 9, 0xF3B13F, true);
		}
		Rasterizer2D.drawRectangle(476 + beginX, 45 + beginY, 12, 280, 0xffffff, 60);
		int height = 278;
		if(scrollMax > 0) {
			height = 285 * 278 / (scrollMax + 285);
		}
		int pos = 0;
		if(scrollPos != 0) {
			pos = scrollPos * 278 / (scrollMax + 285) + 1;
		}
		Rasterizer2D.fillRectangle(477 + beginX, 46 + pos + beginY, 10, height, 0x222222, 120);
		Rasterizer2D.removeClip();
	}

	@Override
	public void initialize() {
		summoningLevel = client.currentStats[23];
	}

	@Override
	public void reset() {
	}

	@Override
	public int getId() {
		return 3;
	}
}
