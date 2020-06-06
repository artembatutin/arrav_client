package net.arrav.activity.ui.util;

import net.arrav.Client;
import net.arrav.Constants;


public class ExpOrbHandler {

	/**
	 * Initializes orbs and their sprites.
	 */
	public ExpOrbHandler() {
		for (int i = 0; i < Constants.SKILL_NAMES_UNORDERED.length; i++) {
			orbs[i] = new ExpOrb(i, Client.spriteCache.get(791 + i));
		}
	}

	/**
	 * The array containing all skill orbs. Each skill orb per available skill.
	 */
	public static final ExpOrb[] orbs = new ExpOrb[Constants.SKILL_NAMES_UNORDERED.length];

	/**
	 * Processes all orbs.
	 */
	public void process() {
		int totalOrbs = 0;

		for (ExpOrb orb : orbs) {
			if (draw(orb)) {
				totalOrbs++;
			}
		}

		// Is the bounty hunter interface open? Then the orbs may need to be
		// re-positioned.
		final boolean blockingInterfaceOpen = /* Client.instance.openWalkableInterface == 23300 */false;
		boolean hpOverlay = /* Client.instance.shouldDrawCombatBox() */ false;

		int y = -2;
		int x = (int) (Client.instance.getWidth() / 3.1) - (totalOrbs * 30);

		if (x < 5) {
			x = 5;
		}

		ExpOrb hover = null;

		for (ExpOrb orb : orbs) {
			if (draw(orb)) {
				if (orb.getShowTimer().finished()) {
					orb.decrementAlpha();
				}
				orb.draw(x, y);
				if (Client.instance.hover(x, y, Client.spriteCache.get(743))) {
					hover = orb;
				}
				x += 62;
				if (x > 460) {
					break;
				}
			}
		}

		if (hover != null) {
			hover.drawTooltip();
		}
	}

	/**
	 * Should a skillorb be drawn?
	 */
	private boolean draw(ExpOrb orb) {
		return !orb.getShowTimer().finished() || orb.getAlpha() > 0;
	}
}
