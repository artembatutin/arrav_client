package net.edge.util.collect;

import net.edge.sign.SignLink;

public final class HashLruCache {

	private int space;
	private final int size;
	private final DoublyLinkableEntry empty = new DoublyLinkableEntry();
	private final HashMap map = new HashMap();
	private final LinkedQueue queue = new LinkedQueue();

	/**
	 * Constructs a new cache with the given size.
	 */
	public HashLruCache(int size) {
		this.size = size;
		space = size;
	}

	/**
	 * Gets an entry for a key.
	 */
	public DoublyLinkableEntry get(long key) {
		final DoublyLinkableEntry node = (DoublyLinkableEntry) map.get(key);
		if(node != null) {
			queue.addLast(node);
		}
		return node;
	}

	/**
	 * Puts a new entry at for the key.
	 */
	public void put(long key, DoublyLinkableEntry value) {
		try {
			if(space == 0) {
				final DoublyLinkableEntry n = queue.removeFirst();
				n.unlinkPrimary();
				n.unlinkSecondary();
				if(n == empty) {
					final DoublyLinkableEntry n2 = queue.removeFirst();
					n2.unlinkPrimary();
					n2.unlinkSecondary();
				}
			} else {
				space--;
			}
			map.put(key, value);
			queue.addLast(value);
			return;
		} catch(final RuntimeException runtimeexception) {
			SignLink.reportError("Cannot handle a new Node, " + value + ", " + key + ", " + (byte) 2 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	/**
	 * Clears all of the entries.
	 */
	public void clear() {
		do {
			final DoublyLinkableEntry node = queue.removeFirst();
			if(node != null) {
				node.unlinkPrimary();
				node.unlinkSecondary();
			} else {
				space = size;
				return;
			}
		} while(true);
	}
}