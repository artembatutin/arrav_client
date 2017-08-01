package net.edge.game.model;

import net.edge.Client;
import net.edge.Config;
import net.edge.cache.unit.DeformSequence;
import net.edge.cache.unit.LocationType;
import net.edge.cache.unit.VaryingBit;

public final class Location extends Entity {

	private int animFrame;
	private final int[] childs;
	private final int anInt1601;
	private final int anInt1602;
	private final int anInt1603;
	private final int anInt1604;
	private final int anInt1605;
	private final int anInt1606;
	private DeformSequence seq;
	private int anInt1608;
	public static Client client;
	private final int anInt1610;
	private final int anInt1611;
	private final int anInt1612;

	public Location(int i, int j, int k, int l, int i1, int j1, int k1, int l1, boolean flag) {
		anInt1610 = i;
		anInt1611 = k;
		anInt1612 = j;
		anInt1603 = j1;
		anInt1604 = l;
		anInt1605 = i1;
		anInt1606 = k1;
		if(l1 != -1) {
			seq = DeformSequence.cache[l1];
			animFrame = 0;
			anInt1608 = client.loopCycle;
			if(flag && seq.animCycle != -1) {
				animFrame = (int) (Math.random() * seq.length);
				anInt1608 -= (int) (Math.random() * seq.getFrame(animFrame));
			}
		}
		final LocationType class46 = LocationType.getPrecise(anInt1610);
		anInt1601 = class46.varBitId;
		anInt1602 = class46.anInt749;
		childs = class46.childIds;
	}

	@Override
	public Model getTransformedModel() {
		int currAnim = -1;
		int nextAnim = -1;
		int end = -1;
		int cycle = -1;
		if(seq != null) {
			int k = client.loopCycle - anInt1608;
			if(k > 100 && seq.animCycle > 0) {
				k = 100;
			}
			while(k > seq.getFrame(animFrame)) {
				k -= seq.getFrame(animFrame);
				animFrame++;
				if(animFrame < seq.length) {
					continue;
				}
				animFrame -= seq.animCycle;
				if(animFrame >= 0 && animFrame < seq.length) {
					continue;
				}
				seq = null;
				break;
			}
			anInt1608 = client.loopCycle - k;
			if(seq != null) {
				currAnim = seq.frameList[animFrame];
				if(Config.def.tween()) {
					int nextFrame = animFrame + 1;
					if(nextFrame < seq.length) {
						nextAnim = seq.frameList[nextFrame];
						end = seq.cycleList[animFrame];
						cycle = k;
					}
				}
			}
		}
		LocationType class46;
		if(childs != null) {
			class46 = getChildren();
		} else {
			class46 = LocationType.getPrecise(anInt1610);
		}
		if(class46 == null) {
			return null;
		} else {
			return class46.getModelAt(anInt1611, anInt1612, anInt1603, anInt1604, anInt1605, anInt1606, currAnim, nextAnim, end, cycle);
		}
	}

	private LocationType getChildren() {
		int i = -1;
		if(anInt1601 != -1) {
			final VaryingBit varBit = VaryingBit.cache[anInt1601];
			final int k = varBit.configId;
			final int l = varBit.leastSignificantBit;
			final int i1 = varBit.mostSignificantBit;
			final int j1 = Client.BIT_MASK[i1 - l];
			i = client.variousSettings[k] >> l & j1;
		} else if(anInt1602 != -1) {
			i = client.variousSettings[anInt1602];
		}
		if(i < 0 || i >= childs.length || childs[i] == -1) {
			return null;
		} else {
			return LocationType.getPrecise(childs[i]);
		}
	}
}
