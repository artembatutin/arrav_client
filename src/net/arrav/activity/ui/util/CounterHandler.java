package net.arrav.activity.ui.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.arrav.Client;
import net.arrav.Config;
import net.arrav.graphic.Rasterizer2D;

import java.text.NumberFormat;
import java.util.Iterator;

public class CounterHandler {

	private static final ObjectList<SkillUpdate> updates = new ObjectArrayList<>();
	private static boolean counterToggled;
	private static int gainedXPWidth = 5;
	private static int movable = 0;
	public static int gainedXP;

	public static Client client;
	private static boolean login = true;
	private static int drawingLevelUp = -1;
	private static int[] savedExp = new int[24];
	private static CounterHandler[] orbs = new CounterHandler[24];
	private static boolean[] updated = new boolean[24];
	private int levelUpTo;
	private int levelUpCycle;
	private float progress;
	private int position;
	private int lastUpdateCycle;
	private int appearCycle;

	private CounterHandler(int skill) {
		position = -1;
		appearCycle = client.loopCycle;
		progress = getProgress(skill);
		levelUpTo = -1;
	}

	public static void drawCounter() {
		Iterator<SkillUpdate> it = updates.iterator();
		int x = client.uiRenderer.isFixed() ? 513 : client.windowWidth - 223;
		int yMove = (client.uiRenderer.isResizableOrFull() ? 15 : client.uiRenderer.getId() > 500 ? -65 : -130) - 30;
		while(it.hasNext()) {
			SkillUpdate update = it.next();
			/* Moving the xp drop up or down by 1 */
			if(client.uiRenderer.getId() > 500) {
				update.move += 1;
			} else {
				update.move -= 1;
			}
			/* Increasing transparency of the xp drop */
			if(movable < -20)
				movable++;
			if(update.move > 30 && client.uiRenderer.getId() > 500) {
				update.alpha -= 10;
			} else if (update.move < -185) {
				update.alpha -= 10;
			}


			/* Removing xp drop */
			if(update.move > 50 && client.uiRenderer.getId() > 500) {
				it.remove();
				continue;
			} else if (update.move < -205) {
				it.remove();
				continue;
			}
			if(counterToggled) {
				//Client.spriteCache.get(update.skill + (client.uiRenderer.getId() > 500 ? 1957 : 2053)).drawImage(x - 25 - update.width, 60 + update.move - yMove, update.alpha);
				Client.spriteCache.get(update.skill + 1957).drawImage(x - 25 - update.width, 60 + update.move - yMove, update.alpha);
				client.smallFont.drawRightAlignedEffectString("" + update.xp, x, 80 + update.move - yMove, 0xffffff, true);
			}
		}
		if(counterToggled) {
			int y = client.uiRenderer.isResizableOrFull() ? 10 : client.uiRenderer.getId() > 500 ? 58 : 8;
			int width = gainedXPWidth + (client.uiRenderer.getId() > 500 ? 25 : 80);
			if(gainedXPWidth == 5 && gainedXP > 0) {
				if(client.uiRenderer.getId() > 500) {
					gainedXPWidth = client.smallFont.getStringWidth("" + gainedXP);
				}
			}
			if(client.uiRenderer.getId() > 500) {
				Rasterizer2D.fillRectangle(x - width - 37, y, width + 37, 16, 0x4d493e, 220); // Rectangle filled
				Rasterizer2D.drawRectangle(x - width - 37, y, width + 37, 17, 0x323130, 100); // darker inner line
				Rasterizer2D.drawRectangle(x - width - 1 - 37, y - 1, width + 2 + 37, 19, 0xad7d3f); // orange outline
				Rasterizer2D.drawRectangle(x - width - 1 - 37, y - 1, width + 2 + 37, 19, 0x91825c, 100); // yellow glowlike outline
				//client.smallFont.drawRightAlignedEffectString("XP:", x - 20, y + 12, 0xffffff, true);
				client.plainFont.drawRightAlignedEffectString("XP:      " + NumberFormat.getInstance().format(gainedXP), x - 5, y + 13, 0xffffff, true);
			} else {
				Rasterizer2D.fillRectangle(x - width, y, width, 28, client.uiRenderer.getId() == 1 ? 0x000000 : 0x413c34, 170);
				Rasterizer2D.drawRectangle(x - width, y, width, 28, 0x5b5348);
				Rasterizer2D.drawRectangle(x - width - 1, y - 1, width + 2, 30, 0x383322);
				Client.spriteCache.get(975).drawImage(x - width + 2, 10);
				client.plainFont.drawRightAlignedEffectString(""+ NumberFormat.getInstance().format(gainedXP), x - 5, y + 19, 0xffffff, true);

			}
		}
	}

	public static void toggleCounter() {
		counterToggled = !counterToggled;
	}

	public static void resetCounter() {
		gainedXP = 0;
		gainedXPWidth = 5;
	}

	public static boolean isCounterOn() {
		return counterToggled;
	}

	private static void checkForUpdates() {
		for(int i = 0; i < orbs.length; i++) {
			if(savedExp[i] != client.currentExp[i]) {
				if(orbs[i] != null) {
					if(getLevelForExp(client.currentExp[i]) > getLevelForExp(savedExp[i])) {
						orbs[i].levelUp(getLevelForExp(client.currentExp[i]));
					}
				}
				savedExp[i] = client.currentExp[i];
				updated[i] = Config.def.skillOrbs();
			}
			if(orbs[i] != null) {
				if((int) getProgress(i) > (int) orbs[i].progress) {
					orbs[i].progress++;
				} else {
					orbs[i].progress = getProgress(i);
				}
			}
		}
	}

	private static void drawLevelUpOrb(int skill) {
		if(drawingLevelUp != -1 && drawingLevelUp != skill) {
			if(orbs[skill].levelUpTo > 0) {
				orbs[skill].levelUpCycle = client.loopCycle;
			}
			return;
		}
		if(orbs[skill].levelUpTo > 0) {
			drawingLevelUp = skill;
			final int xPosition = Rasterizer2D.clipCenterX - 30;
			int alpha = 256;
			if(client.loopCycle - orbs[skill].levelUpCycle < 32) {
				alpha = 8 * (client.loopCycle - orbs[skill].levelUpCycle);
			}
			int LEVEL_UP_DISPLAY_TIME = 340;
			if(client.loopCycle - orbs[skill].levelUpCycle > LEVEL_UP_DISPLAY_TIME - 32) {
				alpha = 256 - 8 * (client.loopCycle - (orbs[skill].levelUpCycle + LEVEL_UP_DISPLAY_TIME - 32));
			}
			Client.spriteCache.get(90).drawImage(xPosition - 44, -2, alpha);
			int alpha2 = alpha;
			if(alpha2 == 256 && client.loopCycle - orbs[skill].levelUpCycle > LEVEL_UP_DISPLAY_TIME / 2) {
				alpha2 = 8 * (client.loopCycle - orbs[skill].levelUpCycle - LEVEL_UP_DISPLAY_TIME / 2);
			}
			if(alpha2 > 256) {
				alpha2 = 256;
			}
			Client.spriteCache.get(skill + 1885).drawImage(xPosition + 30 - Client.spriteCache.get(skill + 91).imageWidth / 2, 63 - Client.spriteCache.get(skill + 91).imageHeight / 2, client.loopCycle - orbs[skill].levelUpCycle > LEVEL_UP_DISPLAY_TIME / 2 ? alpha - alpha2 / 3 * 2 : alpha);
			if(client.loopCycle - orbs[skill].levelUpCycle > LEVEL_UP_DISPLAY_TIME / 2) {
				if(orbs[skill].levelUpTo < 10) {
					Client.spriteCache.get(orbs[skill].levelUpTo + 114).drawImage(xPosition + 30 - Client.spriteCache.get(orbs[skill].levelUpTo + 114).imageWidth / 2, 63 - Client.spriteCache.get(orbs[skill].levelUpTo + 114).imageHeight / 2, alpha2);
				} else {
					Client.spriteCache.get(orbs[skill].levelUpTo % 10 + 114).drawImage(xPosition + 26, 63 - Client.spriteCache.get(orbs[skill].levelUpTo % 10 + 114).imageHeight / 2, alpha2);
					Client.spriteCache.get((orbs[skill].levelUpTo - orbs[skill].levelUpTo % 10) / 10 + 114).drawImage(xPosition + 31 - Client.spriteCache.get((orbs[skill].levelUpTo - orbs[skill].levelUpTo % 10) / 10 + 114).imageWidth, 63 - Client.spriteCache.get((orbs[skill].levelUpTo - orbs[skill].levelUpTo % 10) / 10 + 114).imageHeight / 2, alpha2);
				}
			}
			if(client.loopCycle - orbs[skill].levelUpCycle > LEVEL_UP_DISPLAY_TIME) {
				orbs[skill].levelUpTo = -1;
				drawingLevelUp = -1;
			}
		}
	}

	private static void drawOrb(int skill) {
		if(getLevelForExp(client.currentExp[skill]) >= 99) {
			return;
		}
		if(orbs[skill] == null) {
			orbs[skill] = new CounterHandler(skill);
		}
		if(updated[skill]) {
			orbs[skill].lastUpdateCycle = client.loopCycle;
			updated[skill] = false;
		}
		int ORB_DISPLAY_TIME = 384;
		if(client.loopCycle - orbs[skill].lastUpdateCycle > ORB_DISPLAY_TIME) {
			orbs[skill] = null;
			return;
		}
		if(orbs[skill].levelUpTo > 0 && orbs[skill].levelUpCycle > 32) {
			for(int i = 0; i < orbs.length; i++) {
				if(orbs[i] != null) {
					orbs[i].appearCycle = client.loopCycle;
					updated[i] = true;
				}
			}
		}
		int alpha = 200;
		if(client.loopCycle - orbs[skill].appearCycle < 32) {
			alpha = 8 * (client.loopCycle - orbs[skill].appearCycle);
		}
		if(client.loopCycle - orbs[skill].lastUpdateCycle > ORB_DISPLAY_TIME - 32) {
			alpha = 256 - 8 * (client.loopCycle - (orbs[skill].lastUpdateCycle + ORB_DISPLAY_TIME - 32));
		}
		if(orbs[skill].position == -1) {
			orbs[skill].position = getAvailablePosition();
		}
		int xPosition = (Rasterizer2D.clipEndX - Rasterizer2D.clipStartX) / 2 - 27;
		if(orbs[skill].position > 0) {
			if(orbs[skill].position % 2 != 0) {
				xPosition += orbs[skill].position / 2 * 54;
			} else {
				xPosition -= orbs[skill].position / 2 * 54;
			}
		}
		final int yPosition = 10;
		Client.spriteCache.get(88).drawImage(xPosition, yPosition, alpha);
		Rasterizer2D.setClip(xPosition + 8, (int) (45 - orbs[skill].progress) + 5 + yPosition, xPosition + 30, 60 + yPosition);
		Client.spriteCache.get(89).drawImage(xPosition + 8, 7 + yPosition, alpha);
		Rasterizer2D.setClip(xPosition + 30, 8 + yPosition, xPosition + 52, (int) (orbs[skill].progress - 38) + yPosition);
		Client.spriteCache.get(89).drawImage(xPosition + 8, 7 + yPosition, alpha);
		Rasterizer2D.removeClip();
		Client.spriteCache.get(skill + 1957).drawImage(xPosition + 31 - Client.spriteCache.get(skill + 1957).imageWidth / 2, 28 - Client.spriteCache.get(skill + 1957).imageHeight / 2 + yPosition + 2, alpha);
	}

	public static void drawOrbs() {
		checkForUpdates();
		if(!Config.def.skillOrbs()) {
			return;
		}
		if(login) {
			login = false;
			for(int i = 0; i < updated.length; i++) {
				updated[i] = false;
			}
			return;
		}
		for(int i = 0; i < orbs.length; i++) {
			if(orbs[i] != null || updated[i]) {
				drawOrb(i);
			}
		}
		for(int i = 0; i < orbs.length; i++) {
			if(orbs[i] != null) {
				drawLevelUpOrb(i);
			}
		}
	}

	private static int getAvailablePosition() {
		for(int i = 1; i < orbs.length + 1; i++) {
			boolean used = false;
			for(final CounterHandler orb : orbs) {
				if(orb == null) {
					continue;
				}
				if(orb.position == i) {
					used = true;
				}
			}
			if(!used) {
				return i;
			}
		}
		return 0;
	}

	private static int getExpForLevel(int level) {
		int points = 0;
		int output = 0;
		for(int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if(lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	private static int getLevelForExp(int exp) {
		int points = 0;
		int output;
		if(exp > 13034430) {
			return 99;
		}
		for(int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if(output >= exp) {
				return lvl;
			}
		}
		return 0;
	}

	private static float getProgress(int i) {
		final int currentLevel = getLevelForExp(savedExp[i]);
		final float expDifference = getExpForLevel(currentLevel + 1) - getExpForLevel(currentLevel);
		final float expEarnedAlready = savedExp[i] - getExpForLevel(currentLevel);
		return expEarnedAlready / expDifference * 90;
	}

	private void levelUp(int level) {
		levelUpCycle = client.loopCycle;
		levelUpTo = level;
	}

	public static void add(SkillUpdate update) {
		gainedXP += update.xp;
		gainedXPWidth = client.smallFont.getStringWidth("" + gainedXP);
		updates.add(update);
	}

	public static class SkillUpdate {

		private final int skill;
		private final int xp;
		private final int width;
		private int move = 0;
		private int alpha = 250;

		public SkillUpdate(int skill, int xp) {
			this.skill = skill;
			this.xp = xp;
			this.width = client.smallFont.getStringWidth("" + xp);
			movable -= 25;
			this.move = movable;
		}

	}
}
