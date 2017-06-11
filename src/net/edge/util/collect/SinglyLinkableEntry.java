package net.edge.util.collect;

public class SinglyLinkableEntry {
	
	SinglyLinkableEntry nextPrimary;
	SinglyLinkableEntry prevPrimary;

	public final void unlinkPrimary() {
		if(prevPrimary != null) {
			prevPrimary.nextPrimary = nextPrimary;
			nextPrimary.prevPrimary = prevPrimary;
			nextPrimary = null;
			prevPrimary = null;
		}
	}
}