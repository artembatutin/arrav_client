package net.arrav.cache.unit;

import net.arrav.util.io.Buffer;
import net.arrav.Client;

public final class AnimationFrame {

	private static AnimationFrame[][] cache = new AnimationFrame[4110][];
	public static Client client;
	public SkinList skinList;
	public int skinCount;
	public int[] anIntArray639;
	public int[] skin3dDX;
	public int[] skin3dDY;
	public int[] skin3dDZ;
	int anInt636;

	public static boolean ready(int id) {
		if(id < 0)
			return false;
		int file = id >> 16;
		if(file < 0 || file >= cache.length)
			return false;
		if(cache[file] == null) {
			client.onDemandRequester.addRequest(1, file);
			return false;
		}
		return true;
	}

	public static AnimationFrame get(int id) {
		try {
			int file = id >> 16;
			if(cache[file] == null) {
				client.onDemandRequester.addRequest(1, file);
				return null;
			}
			int index = id & 0xffff;
			return cache[file][index];
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void decode(byte[] fileData, int file) {
		try {
			Buffer stream = new Buffer(fileData);
			SkinList class18 = new SkinList(stream);
			int k1 = stream.getUShort();
			cache[file] = new AnimationFrame[3 * k1];
			int ai[] = new int[500];
			int ai1[] = new int[500];
			int ai2[] = new int[500];
			int ai3[] = new int[500];
			for(int l1 = 0; l1 < k1; l1++) {
				int i2 = stream.getUShort();
				AnimationFrame class36 = cache[file][i2] = new AnimationFrame();
				class36.skinList = class18;
				int j2 = stream.getUByte();
				int l2 = 0;
				int k2 = -1;
				for(int i3 = 0; i3 < j2; i3++) {
					int j3 = stream.getUByte();
					if(j3 > 0) {
						if(class18.anIntArray342[i3] != 0) {
							for(int l3 = i3 - 1; l3 > k2; l3--) {
								if(class18.anIntArray342[l3] != 0)
									continue;
								ai[l2] = l3;
								ai1[l2] = 0;
								ai2[l2] = 0;
								ai3[l2] = 0;
								l2++;
								break;
							}
						}
						ai[l2] = i3;
						short c = 0;
						if(class18.anIntArray342[i3] == 3)
							c = (short) 128;
						if((j3 & 1) != 0)
							ai1[l2] = (short) stream.getSShort();
						else
							ai1[l2] = c;
						if((j3 & 2) != 0)
							ai2[l2] = stream.getSShort();
						else
							ai2[l2] = c;
						if((j3 & 4) != 0)
							ai3[l2] = stream.getSShort();
						else
							ai3[l2] = c;
						k2 = i3;
						l2++;
					}
				}
				class36.skinCount = l2;
				class36.anIntArray639 = new int[l2];
				class36.skin3dDX = new int[l2];
				class36.skin3dDY = new int[l2];
				class36.skin3dDZ = new int[l2];
				for(int k3 = 0; k3 < l2; k3++) {
					class36.anIntArray639[k3] = ai[k3];
					class36.skin3dDX[k3] = ai1[k3];
					class36.skin3dDY[k3] = ai2[k3];
					class36.skin3dDZ[k3] = ai3[k3];
				}
			}
		} catch(Exception ignored) {
		}
	}

	public static boolean isNull(int id) {
		return id == -1;
	}

	public static void reset() {
		cache = null;
	}

}