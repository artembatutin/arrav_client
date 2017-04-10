package net.edge.od;

import net.edge.util.collect.DoublyLinkableEntry;

public final class OnDemandEntry extends DoublyLinkableEntry {

	public int type;
	public byte[] data;
	public int id;
	public boolean incomplete = true;
	public int cyclesSinceSend;
}
