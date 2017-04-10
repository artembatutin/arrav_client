package net.edge.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class DataToolkit {
	
	/**
	 * Can't initiate this class.
	 */
	private DataToolkit() {}

	/**
	 * Gets the checksums from the byte data.
	 */
	public static int getCRCFromData(byte[] data) {
		final CRC32 crc = new CRC32(); // CRC.
		crc.update(data); // Updates the value.
		return (int) crc.getValue(); // result.
	}

	/**
	 * GZip decompressor.
	 */
	public static byte[] decompressGZip(byte[] b) throws IOException {
		final GZIPInputStream gzi = new GZIPInputStream(new ByteArrayInputStream(b));
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final byte[] buf = new byte[1024];
		int len;
		while((len = gzi.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.close();
		return out.toByteArray();
	}

	/**
	 * GZip compressor.
	 */
	public static byte[] compressGZip(byte[] data, int off, int len) throws IOException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final GZIPOutputStream gzo = new GZIPOutputStream(bos);
		try {
			gzo.write(data, off, len);
		} finally {
			gzo.close();
			bos.close();
		}
		return bos.toByteArray();
	}

	/**
	 * File reader.
	 */
	public static byte[] readFile(String name) {
		try {
			final RandomAccessFile raf = new RandomAccessFile(name, "r");
			final ByteBuffer buf = raf.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, raf.length());
			try {
				if(buf.hasArray()) {
					return buf.array();
				} else {
					final byte[] array = new byte[buf.remaining()];
					buf.get(array);
					return array;
				}
			} finally {
				raf.close();
			}
		} catch(final Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Reads the JAG Hash.
	 */
	public static int readJAGHash(String string) {
		int id = 0;
		string = string.toUpperCase();
		for(int j = 0; j < string.length(); j++) {
			id = id * 61 + string.charAt(j) - 32;
		}
		return id;
	}

	/**
	 * Unzip the raw data.
	 */
	static byte[] decompressZip(byte[] data) throws IOException {
		InputStream in = new ByteArrayInputStream(data);
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			in = new GZIPInputStream(in);
			final byte[] buffer = new byte[65536];
			int noRead;
			while((noRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, noRead);
			}
		} finally {
			try {
				out.close();
			} catch(final Exception ignored) {
				//empty
			}
		}
		return out.toByteArray();
	}

	/**
	 * Write file.
	 */
	public static void writeFile(File f, byte[] data) throws IOException {
		try(RandomAccessFile raf = new RandomAccessFile(f, "rw")) {
			raf.write(data);
		}
	}
}
