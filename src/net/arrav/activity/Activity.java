package net.arrav.activity;

import net.arrav.Client;
import net.arrav.graphic.font.BitmapFont;

/**
 * An <i>Activity</i> is a handler for a stage of the client.
 * @author Rob Bubletan
 * @see TitleActivity
 * @see GameActivity
 */
public abstract class Activity {

	/**
	 * Instance to access the client.
	 */
	public static Client client;

	/*
	 * Bit map fonts.
	 */
	protected static BitmapFont smallFont;
	protected static BitmapFont plainFont;
	protected static BitmapFont boldFont;
	protected static BitmapFont fancyFont;

	/**
	 * This method will create the access to the fields of the client.
	 */
	public static void init() {
		smallFont = client.smallFont;
		plainFont = client.plainFont;
		boldFont = client.boldFont;
		fancyFont = client.fancyFont;
	}

	/**
	 * This will process the actions e.g. clicking and writing.
	 * @return {@code true} if process has been made, {@code false} otherwise.
	 */
	public abstract boolean process();

	/**
	 * This method will process the updating of the graphics.
	 */
	public abstract void update();

	/**
	 * This initializes the activity.
	 */
	public abstract void initialize();

	/**
	 * This method cleans all the data that is no longer needed.
	 * {@link #initialize() initialize} will undo the changes.
	 */
	public abstract void reset();
}
