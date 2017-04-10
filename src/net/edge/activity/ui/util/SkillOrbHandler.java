package net.edge.activity.ui.util;

import net.edge.Client;
import net.edge.Config;
import net.edge.cache.unit.ImageCache;
import net.edge.media.Rasterizer2D;

public class SkillOrbHandler {

	public static Client client;
	private static boolean login = true;
	private static int drawingLevelUp = -1;
	private static int[] savedExp = new int[24];
	private static SkillOrbHandler[] orbs = new SkillOrbHandler[24];
	private static boolean[] updated = new boolean[24];
	private int levelUpTo;
	private int levelUpCycle;
	private float progress;
	private int position;
	private int lastUpdateCycle;
	private int appearCycle;

	private SkillOrbHandler(int skill) {
		position = -1;
		appearCycle = client.loopCycle;
		progress = getProgress(skill);
		levelUpTo = -1;
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
				updated[i] = Config.DRAW_SKILL_ORBS.isOn();
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
			ImageCache.get(90).drawAlphaImage(xPosition - 44, -2, alpha);
			int alpha2 = alpha;
			if(alpha2 == 256 && client.loopCycle - orbs[skill].levelUpCycle > LEVEL_UP_DISPLAY_TIME / 2) {
				alpha2 = 8 * (client.loopCycle - orbs[skill].levelUpCycle - LEVEL_UP_DISPLAY_TIME / 2);
			}
			if(alpha2 > 256) {
				alpha2 = 256;
			}
			ImageCache.get(skill + 1885).drawAlphaImage(xPosition + 30 - ImageCache.get(skill + 91).imageWidth / 2, 63 - ImageCache.get(skill + 91).imageHeight / 2, client.loopCycle - orbs[skill].levelUpCycle > LEVEL_UP_DISPLAY_TIME / 2 ? alpha - alpha2 / 3 * 2 : alpha);
			if(client.loopCycle - orbs[skill].levelUpCycle > LEVEL_UP_DISPLAY_TIME / 2) {
				if(orbs[skill].levelUpTo < 10) {
					ImageCache.get(orbs[skill].levelUpTo + 114).drawAlphaImage(xPosition + 30 - ImageCache.get(orbs[skill].levelUpTo + 114).imageWidth / 2, 63 - ImageCache.get(orbs[skill].levelUpTo + 114).imageHeight / 2, alpha2);
				} else {
					ImageCache.get(orbs[skill].levelUpTo % 10 + 114).drawAlphaImage(xPosition + 26, 63 - ImageCache.get(orbs[skill].levelUpTo % 10 + 114).imageHeight / 2, alpha2);
					ImageCache.get((orbs[skill].levelUpTo - orbs[skill].levelUpTo % 10) / 10 + 114).drawAlphaImage(xPosition + 31 - ImageCache.get((orbs[skill].levelUpTo - orbs[skill].levelUpTo % 10) / 10 + 114).imageWidth, 63 - ImageCache.get((orbs[skill].levelUpTo - orbs[skill].levelUpTo % 10) / 10 + 114).imageHeight / 2, alpha2);
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
			orbs[skill] = new SkillOrbHandler(skill);
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
		int alpha = 256;
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
		final int yPosition = -4;
		ImageCache.get(88).drawAlphaImage(xPosition, yPosition, alpha);
		Rasterizer2D.setClip(xPosition + 7, (int) (45 - orbs[skill].progress) + 5 + yPosition, xPosition + 30, 60 + yPosition);
		ImageCache.get(89).drawAlphaImage(xPosition + 7, 7 + yPosition, alpha);
		Rasterizer2D.setClip(xPosition + 30, 7 + yPosition, xPosition + 52, (int) (orbs[skill].progress - 38) + yPosition);
		ImageCache.get(89).drawAlphaImage(xPosition + 7, 7 + yPosition, alpha);
		Rasterizer2D.removeClip();
		ImageCache.get(skill + 124).drawAlphaImage(xPosition + 30 - ImageCache.get(skill + 124).imageWidth / 2, 28 - ImageCache.get(skill + 124).imageHeight / 2 + yPosition, alpha);
	}

	public static void drawOrbs() {
		checkForUpdates();
		if(!Config.DRAW_SKILL_ORBS.isOn()) {
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
			for(final SkillOrbHandler orb : orbs) {
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
}
