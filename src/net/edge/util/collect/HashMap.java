package net.edge.util.collect;

import net.edge.sign.SignLink;

/**
 * @see java.util.HashMap
 */
public final class HashMap {

	private final int size;
	private final SinglyLinkableEntry[] buckets;

	/**
	 * Construct a new hash map instance.
	 */
	HashMap() {
		size = 1024; //must be a power of two
		buckets = new SinglyLinkableEntry[size];
		for(int i = 0; i < size; i++) {
			final SinglyLinkableEntry head = buckets[i] = new SinglyLinkableEntry();
			head.nextPrimary = head;
			head.prevPrimary = head;
		}
	}

	/**
	 * Gets an entry for a key.
	 */
	public SinglyLinkableEntry get(long key) {
		final SinglyLinkableEntry head = buckets[(int) (key & size - 1)];
		for(SinglyLinkableEntry node = head.nextPrimary; node != head; node = node.nextPrimary) {
			if(node.key == key) {
				return node;
			}
		}
		return null;
	}

	/**
	 * Puts a new entry for the key. In case the hash of the key is already used,
	 * the old entry will be overridden.
	 */
	public void put(long key, SinglyLinkableEntry value) {
		try {
			if(value.prevPrimary != null) {
				value.unlinkPrimary();
			}
			final SinglyLinkableEntry head = buckets[(int) (key & size - 1)];
			value.prevPrimary = head.prevPrimary;
			value.nextPrimary = head;
			value.prevPrimary.nextPrimary = value;
			value.nextPrimary.prevPrimary = value;
			value.key = key;
			return;
		} catch(final RuntimeException runtimeexception) {
			SignLink.reportError("91499, " + value + ", " + key + ", " + (byte) 7 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}
}