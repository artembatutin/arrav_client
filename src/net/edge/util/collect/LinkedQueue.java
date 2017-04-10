package net.edge.util.collect;

public final class LinkedQueue {

	private final DoublyLinkableEntry head;
	private DoublyLinkableEntry current;

	public LinkedQueue() {
		head = new DoublyLinkableEntry();
		head.nextSecondary = head;
		head.prevSecondary = head;
	}

	public int size() {
		int size = 0;
		for(DoublyLinkableEntry node = head.nextSecondary; node != head; node = node.nextSecondary) {
			size++;
		}
		return size;
	}

	public void addLast(DoublyLinkableEntry node) {
		if(node.prevSecondary != null) {
			node.unlinkSecondary();
		}
		node.prevSecondary = head.prevSecondary;
		node.nextSecondary = head;
		node.prevSecondary.nextSecondary = node;
		node.nextSecondary.prevSecondary = node;
	}

	DoublyLinkableEntry removeFirst() {
		final DoublyLinkableEntry node = head.nextSecondary;
		if(node == head) {
			return null;
		} else {
			node.unlinkSecondary();
			return node;
		}
	}

	public DoublyLinkableEntry getFirst() {
		final DoublyLinkableEntry node = head.nextSecondary;
		if(node == head) {
			current = null;
			return null;
		} else {
			current = node.nextSecondary;
			return node;
		}
	}

	public DoublyLinkableEntry getNext() {
		final DoublyLinkableEntry node = current;
		if(node == head) {
			current = null;
			return null;
		} else {
			current = node.nextSecondary;
			return node;
		}
	}
}
