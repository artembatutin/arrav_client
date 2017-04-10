package net.edge.cache.unit;

import net.edge.cache.CacheArchive;
import net.edge.util.io.Buffer;

public final class MaterialType {

	public static MaterialType[] textures;
	public boolean aBoolean1223;
	public boolean aBoolean1204;
	boolean aBoolean1205;
	byte brightness;
	byte shadowFactor;
	byte aByte1214;
	byte aByte1213;
	short aShort1221;
	byte aByte1211;
	byte aByte1203;
	boolean aBoolean1222;
	boolean aBoolean1216;
	byte aByte1207;
	boolean aBoolean1212;
	boolean aBoolean1210;
	boolean aBoolean1215;
	int anInt1202;
	int anInt1206;
	public int anInt1226;

	public static void unpackConfig(CacheArchive streamLoader) {
		byte[] data = streamLoader.getFile("texture.dat");
		Buffer buffer = new Buffer(data);
		int count = buffer.getUShort();
		textures = new MaterialType[count];
		for(int i = 0; i != count; ++i)
			if(buffer.getUByte() == 1)
				textures[i] = new MaterialType();

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].aBoolean1223 = buffer.getUByte() == 1;

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].aBoolean1204 = buffer.getUByte() == 1;

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].aBoolean1205 = buffer.getUByte() == 1;

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].brightness = buffer.getSByte();

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].shadowFactor = buffer.getSByte();

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].aByte1214 = buffer.getSByte();

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].aByte1213 = buffer.getSByte();

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].aShort1221 = (short) buffer.getUShort();

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].aByte1211 = buffer.getSByte();

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].aByte1203 = buffer.getSByte();

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].aBoolean1222 = buffer.getUByte() == 1;

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].aBoolean1216 = buffer.getUByte() == 1;

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].aByte1207 = buffer.getSByte();

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].aBoolean1212 = buffer.getUByte() == 1;

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].aBoolean1210 = buffer.getUByte() == 1;

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].aBoolean1215 = buffer.getUByte() == 1;

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].anInt1202 = buffer.getUByte();

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].anInt1206 = buffer.getInt();

		for(int i = 0; i != count; ++i)
			if(textures[i] != null)
				textures[i].anInt1226 = buffer.getUByte();

	}

	public static void reset() {
		textures = null;
	}

}
