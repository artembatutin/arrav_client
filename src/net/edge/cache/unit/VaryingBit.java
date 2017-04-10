package net.edge.cache.unit;

import net.edge.util.FileToolkit;
import net.edge.util.io.Buffer;
import net.edge.cache.CacheArchive;

public final class VaryingBit {

	public static VaryingBit[] cache;
	public int configId;
	public int leastSignificantBit;
	public int mostSignificantBit;
	private boolean aBoolean651;

	private VaryingBit() {
		aBoolean651 = false;
	}

	public static void unpack(CacheArchive archive) {
		Buffer buffer = new Buffer(archive.getFile("varbit.dat"));
		int length = buffer.getUShort();
		System.out.println("[loading] varbit size: " + length);
		if(cache == null) {
			cache = new VaryingBit[length];
		}
		for(int i = 0; i < length; i++) {
			if(cache[i] == null)
				cache[i] = new VaryingBit();
			cache[i].readValues(buffer);
			if(cache[i].aBoolean651)
				VariancePopulation.cache[cache[i].configId].aBoolean713 = true;
		}
	}


	private void readValues(Buffer buffer) {
		configId = buffer.getUShort();
		leastSignificantBit = buffer.getUByte();
		mostSignificantBit = buffer.getUByte();
	}

	public static void dump() {
		int size = cache.length * 4 + 2;
		Buffer buffer = new Buffer(new byte[size]);
		buffer.putShort(cache.length);
		for(VaryingBit b : cache) {
			buffer.putShort(b.configId);
			buffer.putByte(b.leastSignificantBit);
			buffer.putByte(b.mostSignificantBit);
		}
		FileToolkit.writeFile("./varbit.dat", buffer.data);
	}


}
