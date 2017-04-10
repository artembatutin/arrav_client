package net.edge.cache;

import net.edge.cache.bzip2.BZip2Compressor;
import net.edge.util.io.Buffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public final class CacheArchive {

	private final int index;
	private final String name;

	private final byte[] data;
	private final int fileCount;
	private final int[] fileHash;
	private final int[] fileSize;
	private final int[] comprSize;
	private final int[] filePos;
	private final boolean decompressed;

	public CacheArchive(int index, String name, byte[] data) {
		this.name = name;
		this.index = index;
		Buffer buffer = new Buffer(data);
		final int len = buffer.getUMedium();
		final int clen = buffer.getUMedium();
		if(clen != len) {
			final byte[] temp = new byte[len];
			BZip2Compressor.decompress(data, 6, temp, clen, len);
			this.data = temp;
			buffer = new Buffer(this.data);
			decompressed = true;
		} else {
			this.data = data;
			decompressed = false;
		}
		fileCount = buffer.getUShort();
		fileHash = new int[fileCount];
		fileSize = new int[fileCount];
		comprSize = new int[fileCount];
		filePos = new int[fileCount];
		int pos = buffer.pos + fileCount * 10;
		for(int n = 0; n < fileCount; n++) {
			fileHash[n] = buffer.getInt();
			fileSize[n] = buffer.getUMedium();
			comprSize[n] = buffer.getUMedium();
			filePos[n] = pos;
			pos += comprSize[n];
		}
	}

	public byte[] getFile(String name) {
		byte[] temp = null;
		int hash = 0;
		name = name.toUpperCase();
		for(int i = 0; i < name.length(); i++) {
			hash = hash * 61 + name.charAt(i) - 32;
		}
		for(int index = 0; index < fileCount; index++) {
			if(fileHash[index] == hash) {
				if(temp == null) {
					temp = new byte[fileSize[index]];
				}
				if(!decompressed) {
					BZip2Compressor.decompress(data, filePos[index], temp, comprSize[index], fileSize[index]);
				} else {
					System.arraycopy(data, filePos[index], temp, 0, fileSize[index]);
				}
				return temp;
			}
		}
		return null;
	}

	/*public boolean writeFile(Client client, String name, byte[] pack) {
		if(pack == null) {
			throw new RuntimeException("nulled data");
		}
		int hash = 0;
		name = name.toUpperCase();
		for(int i = 0; i < name.length(); i++) {
			hash = hash * 61 + name.charAt(i) - 32;
		}
		for(int index = 0; index < fileCount; index++) {
			if(fileHash[index] == hash) {
				pack = gzip(pack);
				//if(client.cacheIdx[0] != null) {
				//	buffer = client.cacheIdx[0].readFile(index);
				//}
				client.cacheIdx[0].writeFile(pack.length, pack, index);
				return true;
			}
		}
		return false;
	}*/

	/**
	 * Gzips the data.
	 * @param data data being gzipped.
	 * @return the gzipped data.
	 */
	private byte[] gzip(byte[] data) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream gzos = new GZIPOutputStream(baos);
			gzos.write(data);
			gzos.close();
			return baos.toByteArray();
		} catch(IOException e) {
			throw new RuntimeException("error zipping");
		}
	}

}
