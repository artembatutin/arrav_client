package net.arrav.util.collect;

/**
 * @see java.util.LinkedList
 */
public final class LinkedDeque {

	private final SinglyLinkableEntry head;
	private SinglyLinkableEntry current;

	/**
	 * Construct a new linked list instance.
	 */
	public LinkedDeque() {
		head = new SinglyLinkableEntry();
		head.nextPrimary = head;
		head.prevPrimary = head;
	}

	/**
	 * Adds as last entry.
	 */
	public void addLast(SinglyLinkableEntry node) {
		if(node.prevPrimary != null) {
			node.unlinkPrimary();
		}
		node.prevPrimary = head.prevPrimary;
		node.nextPrimary = head;
		node.prevPrimary.nextPrimary = node;
		node.nextPrimary.prevPrimary = node;
	}

	/**
	 * Adds as first entry.
	 */
	public void addFirst(SinglyLinkableEntry node) {
		if(node.prevPrimary != null) {
			node.unlinkPrimary();
		}
		node.prevPrimary = head;
		node.nextPrimary = head.nextPrimary;
		node.prevPrimary.nextPrimary = node;
		node.nextPrimary.prevPrimary = node;
	}

	/**
	 * Removes and returns the first entry.
	 */
	public SinglyLinkableEntry removeFirst() {
		final SinglyLinkableEntry node = head.nextPrimary;
		if(node == head) {
			return null;
		} else {
			node.unlinkPrimary();
			return node;
		}
	}

	/**
	 * Clears all of the entries.
	 */
	public void clear() {
		if(head.nextPrimary == head) {
			return;
		}
		do {
			final SinglyLinkableEntry node = head.nextPrimary;
			if(node == head) {
				return;
			}
			node.unlinkPrimary();
		} while(true);
	}

	/**
	 * Gets the first entry.
	 */
	public SinglyLinkableEntry getFirst() {
		final SinglyLinkableEntry node = head.nextPrimary;
		if(node == head) {
			current = null;
			return null;
		} else {
			current = node.nextPrimary;
			return node;
		}
	}

	/**
	 * Gets the last entry.
	 */
	public SinglyLinkableEntry getLast() {
		final SinglyLinkableEntry node = head.prevPrimary;
		if(node == head) {
			current = null;
			return null;
		} else {
			current = node.prevPrimary;
			return node;
		}
	}

	/**
	 * Gets the previous entry.
	 */
	public SinglyLinkableEntry getPrevious() {
		final SinglyLinkableEntry node = current;
		if(node == head) {
			current = null;
			return null;
		}
		current = node.prevPrimary;
		return node;
	}

	/**
	 * Gets the next entry.
	 */
	public SinglyLinkableEntry getNext() {
		final SinglyLinkableEntry node = current;
		if(node == head) {
			current = null;
			return null;
		} else {
			current = node.nextPrimary;
			return node;
		}
	}
}