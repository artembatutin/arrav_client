package net.arrav.cache.unit;

import net.arrav.util.io.Buffer;

public final class SkinList {

	public final int[] anIntArray342;
	public final int[][] anIntArrayArray343;
	public final int length;

	SkinList(Buffer buffer) {
		length = buffer.getUShort();
		anIntArray342 = new int[length];
		anIntArrayArray343 = new int[length][];
		for(int j = 0; j < length; j++) {
			anIntArray342[j] = buffer.getUShort();
		}
		for(int j = 0; j < length; j++) {
			anIntArrayArray343[j] = new int[buffer.getUShort()];
		}
		for(int j = 0; j < length; j++) {
			for(int l = 0; l < anIntArrayArray343[j].length; l++) {
				anIntArrayArray343[j][l] = buffer.getUShort();
			}
		}
	}
}