package net.arrav.cache.unit;

import net.arrav.cache.CacheArchive;
import net.arrav.util.io.Buffer;

public final class VariancePopulation {

	public static VariancePopulation[] cache;
	private static int anInt702;
	private static int[] anIntArray703;
	public int anInt709;
	boolean aBoolean713;

	private VariancePopulation() {
		aBoolean713 = false;
	}

	public static void unpack(CacheArchive archive) {
		final Buffer buffer = new Buffer(archive.getFile("varp.dat"));
		anInt702 = 0;
		final int length = buffer.getUShort();
		System.out.println("[loading] varp size: " + length);
		if(cache == null) {
			cache = new VariancePopulation[length];
		}
		if(anIntArray703 == null) {
			anIntArray703 = new int[length];
		}
		for(int id = 0; id < length; id++) {
			if(cache[id] == null) {
				cache[id] = new VariancePopulation();
			}
			cache[id].read(buffer, id);
		}
		if(buffer.pos != buffer.data.length) {
			System.out.println("varptype load mismatch");
		}
	}

	private void read(Buffer buffer, int id) {
		do {
			final int code = buffer.getUByte();
			if(code == 0) {
				return;
			}
			if(code == 1) {
				buffer.getUByte();
			} else if(code == 2) {
				buffer.getUByte();
			} else if(code == 3) {
				anIntArray703[anInt702++] = id;
			} else if(code == 4) {
			} else if(code == 5) {
				anInt709 = buffer.getUShort();
			} else if(code == 6) {
			} else if(code == 7) {
				buffer.getInt();
			} else if(code == 8) {
				aBoolean713 = true;
			} else if(code == 10) {
				buffer.getLine();
			} else if(code == 11) {
				aBoolean713 = true;
			} else if(code == 12) {
				buffer.getInt();
			} else if(code == 13) {
			} else {
				System.out.println("Error unrecognised config code: " + code);
			}
		} while(true);
	}
}