package net.arrav.util.collect;

public class DoublyLinkableEntry extends SinglyLinkableEntry {

	DoublyLinkableEntry nextSecondary;
	DoublyLinkableEntry prevSecondary;

	public final void unlinkSecondary() {
		if(prevSecondary != null) {
			prevSecondary.nextSecondary = nextSecondary;
			nextSecondary.prevSecondary = prevSecondary;
			nextSecondary = null;
			prevSecondary = null;
		}
	}
}