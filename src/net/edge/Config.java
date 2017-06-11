package net.edge;

import net.edge.sign.SignLink;

import java.io.*;

/**
 * [Config.java] In communications or computer systems, a configuration is an
 * arrangement of functional units according to their nature, number, and chief
 * characteristics. Often, configuration pertains to the choice of hardware,
 * software, firmware, and documentation. The configuration affects system function
 * and performance.
 */
public enum Config {

	/**
	 * Selected Menus
	 * OLD: 1
	 * OLD ALPHA: 2
	 * MIDDLE: 3
	 * NEW: 4
	 * NEW ALPHA: 5
	 * CUSTOM: 6
	 */
	SELECTED_MENU(4, true),

	/**
	 * Hitsplats
	 * OLD-317: 0
	 * NEW-562: 1
	 * NEWEST-660: 2
	 */
	HITSPLATS(2, true),

	/**
	 * Hitbars
	 * OLD-317: 0
	 * NEW-562: 1
	 */
	HITBARS(1, true),

	/**
	 * The 10x hitpoints toggle.
	 */
	TEN_X_HITS(1, true),

	/**
	 * The game-frame revision id.
	 */
	GAME_FRAME(562, true),

	/*
	 * Miscellaneous configurations
	 */
	DEBUG_DATA(0, false),
	FPS_ON(0, true),
	DEBUG_INDEXES(0, false),
	ORTHO_VIEW(0, false),

	/*
	 * Detail configurations
	 */
	LOW_MEM(0, true),
	GROUND_DECORATION(1, true),
	GROUND_MATERIALS(0, true),
	SMOOTH_FOG(0, true),
	TWEENING(0, true),
	RETAIN_MODEL_PRECISION(1, true),
	MAP_ANTIALIASING(1, true),

	/*
	 * View toggle configurations
	 */
	DRAW_ORBS(1, true),
	DRAW_SKILL_ORBS(0, true),

	/*
	 * Style configurations
	 */
	DISPLAY_NAMES(0, true),
	SPLIT_PRIVATE_CHAT_COLOR(0xffff, true),
	CHARACTER_PREVIEW(1, true),
	/** Roofs being off all the time. */
	ROOF_OFF(1, true);

	/**
	 * The current setting id of the {@link Config}.
	 */
	private int setting;

	/**
	 * The condition if the setting must be saved.
	 */
	private final boolean save;

	Config(int setting, boolean save) {
		this.setting = setting;
		this.save = save;
	}

	/**
	 * Gets the setting id.
	 * @return the settig id.
	 */
	public int get() {
		return setting;
	}

	/**
	 * Gets the setting as a boolean depending if it's set to 1.
	 * @return the setting id as a boolean.
	 */
	public boolean isOn() {
		return setting == 1;
	}

	/**
	 * Toggle the config on or off.
	 */
	public void toggle() {
		this.setting = setting == 1 ? 0 : 1;
		save();
	}

	/**
	 * Sets the new {@link #setting} to the {@link Config}.
	 * @param setting the new value to set.
	 */
	public void set(int setting) {
		set(setting, true);
	}

	/**
	 * Sets the new {@link #setting} to the {@link Config}.
	 * @param setting the new value to set.
	 * @param save    the condition if the config will be saved afterwards.
	 */
	public void set(int setting, boolean save) {
		this.setting = setting;
		if(save)
			save();
	}

	/**
	 * Saves the configurations.
	 */
	public void save() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(SignLink.getCacheDir() + "config"));
			for(Config con : Config.values()) {
				if(con.save) {
					out.write(con.toString() + ":" + con.get());
					out.newLine();
				}
			}
			out.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the configurations on startup.
	 */
	public static void load() {
		try {
			File file = new File(SignLink.getCacheDir() + "config");
			if(file.exists()) {
				FileReader in = new FileReader(file);
				BufferedReader br = new BufferedReader(in);
				String line;
				while((line = br.readLine()) != null) {
					String[] con = line.split(":");
					try {
						Config config = Config.valueOf(con[0]);
						config.set(Integer.parseInt(con[1]), false);
					} catch(Exception ex) {

					}
				}
				in.close();
				br.close();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
