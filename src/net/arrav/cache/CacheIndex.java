package net.arrav.cache;

public interface CacheIndex {

	/**
	 * Returns the number of files in the cache index.
	 */
	long getFileCount();

	/**
	 * Reads a file from the cache.
	 */
	byte[] readFile(int id);

	/**
	 * Writes a file to the cache.
	 */
	boolean writeFile(int length, byte[] data, int id);

	/**
	 * Implements the actual file writing.
	 */
	boolean writeFile(boolean flag, int id, int length, byte[] data);

}
