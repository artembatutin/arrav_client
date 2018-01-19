package net.arrav.cache;

import net.arrav.Client;

/**
 * Loads a portion of the cache on startup.
 */
public interface CacheLoader {
	
	/**
	 * The loading message while running this loader.
	 * @return loading message.
	 */
	String message();
	
	/**
	 * The running instance of this loader.
	 * @param client client
	 */
	void run(Client client);
}
