package net.edge.activity.ui.util;

import net.edge.Client;
import net.edge.Config;
import net.edge.Constants;

import static net.edge.Config.TEN_X_HITS;

public class OrbHandler {

	public static Client client;

	/**
	 * Values displayed on the orbs.
	 */
	private static int healthValue = 0;
	private static int prayerValue = 0;
	private static int runValue = 0;
	private static int summoningValue = 0;
	/**
	 * Used to count the filling progress for the orbs.
	 */
	private static float healthFill = (float) .8;
	private static float prayerFill = (float) .6;
	private static float runFill = (float) .4;
	private static float summoningFill = (float) .2;
	
	/**
	 * A flag which determines if the player is poisoned.
	 */
	public static boolean poisoned;
	
	/**
	 * A flag which determines if the run orb is enabled.
	 */
	public static boolean runEnabled;
	
	/**
	 * A flag which determines if the quick prayers are enabled.
	 */
	public static boolean prayersEnabled;
	
	/**
	 * A flag which determines if the summoning is in progress.
	 */
	public static boolean summonEnabled;

	/**
	 * Updates the arguments which are used for drawing the orbs.
	 */
	public static void updateOrbs() {
		if(Config.DRAW_ORBS.isOn()) {
			healthValue = client.currentStats[3];
			healthFill = healthValue / (float) client.maxStats[3] / 10;
			if(!TEN_X_HITS.isOn()) {
				healthValue = healthValue / 10;
			}

			prayerValue = client.currentStats[5];
			prayerFill = prayerValue / (float) client.maxStats[5];

			runValue = client.energy;
			runFill = runValue / 100F;

			summoningValue = client.currentStats[22];
			summoningFill = summoningValue / (float) client.maxStats[22];
		}
	}

	/**
	 * Gets the displayed value for specified orb.
	 */
	public static String getValue(int orb) {
		if(orb == Constants.ORB_HEALTH) {
			return Integer.toString(healthValue);
		}
		if(orb == Constants.ORB_PRAYER) {
			return Integer.toString(prayerValue);
		}
		if(orb == Constants.ORB_RUN) {
			return Integer.toString(runValue);
		}
		if(orb == Constants.ORB_SUMMONING) {
			return Integer.toString(summoningValue);
		}
		return "";
	}

	/**
	 * Gets the font color for specified orb.
	 */
	public static int getColor(int orb) {
		if(orb == Constants.ORB_HEALTH) {
			return getColor(healthFill);
		}
		if(orb == Constants.ORB_PRAYER) {
			return getColor(prayerFill);
		}
		if(orb == Constants.ORB_RUN) {
			return getColor(runFill);
		}
		if(orb == Constants.ORB_SUMMONING) {
			return getColor(summoningFill);
		}
		return 0;
	}

	/**
	 * Gets the amount of fill for the specified orb for the given maximum fill
	 * amount.
	 */
	public static int getFill(int orb, int max) {
		if(orb == Constants.ORB_HEALTH) {
			return max - (int) (max * healthFill);
		}
		if(orb == Constants.ORB_PRAYER) {
			return max - (int) (max * prayerFill);
		}
		if(orb == Constants.ORB_RUN) {
			return max - (int) (max * runFill);
		}
		if(orb == Constants.ORB_SUMMONING) {
			return max - (int) (max * summoningFill);
		}
		return 0;
	}

	public static int getPercent(int orb) {
		if(orb == Constants.ORB_HEALTH) {
			return (int) (100 * healthFill);
		}
		if(orb == Constants.ORB_PRAYER) {
			return (int) (100 * prayerFill);
		}
		if(orb == Constants.ORB_RUN) {
			return (int) (100 * runFill);
		}
		if(orb == Constants.ORB_SUMMONING) {
			return (int) (100 * summoningFill);
		}
		return 0;
	}

	/**
	 * Gets the color for displayed value by the fill amount.
	 */
	private static int getColor(float fill) {
		if(fill > .75) {
			return 65280;
		} else if(fill > .5) {
			return 0xffff00;
		} else if(fill > .25) {
			return 0xff7000;
		} else {
			return 0xff0000;
		}
	}
	
	public static int getOrb(int orb) {
		int img = 56;
		if(orb == Constants.ORB_HEALTH && poisoned) {
			img = 78;
		}
		if(orb == Constants.ORB_PRAYER) {
			if(prayersEnabled)
				img = 72;
			else
				img += Constants.ORB_PRAYER;
		}
		if(orb == Constants.ORB_RUN) {
			if(runEnabled)
				img = 73;
			else
				img += Constants.ORB_RUN;
		}
		if(orb == Constants.ORB_SUMMONING) {
			if(summonEnabled)
				img = 1883;
			else
				img += Constants.ORB_SUMMONING;
		}
		return img;
	}

}
