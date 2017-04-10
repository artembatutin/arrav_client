package net.edge.util;

import java.io.*;

public final class FileToolkit {
	
	/**
	 * Can't initiate this class.
	 */
	private FileToolkit() {}

	/**
	 * Checks if the file exists.
	 * @return true/false
	 */
	public static boolean fileExists(String path) {
		final File file = new File(path);
		return file.exists();
	}

	/**
	 * File reader.
	 */
	public static byte[] readFile(String path) {
		try {
			final File file = new File(path);
			final int size = (int) file.length();
			final byte[] buffer = new byte[size];
			try {
				DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
				dis.readFully(buffer, 0, size);
				dis.close();
			} catch(final Exception ignored) {
				//empty
			}
			return buffer;
		} catch(final Exception ignored) {
			//empty
		}
		return null;
	}

	/**
	 * Writes the file.
	 */
	public static void writeFile(String path, byte[] data) {
		try {
			new File(new File(path).getParent()).mkdirs();
			try {
				FileOutputStream fos = new FileOutputStream(path);
				fos.write(data, 0, data.length);
				fos.close();
			} catch(final Exception ignored) {
				//empty
			}
		} catch(final Throwable throwable) {
			System.out.println("Write Error: " + path);
		}
	}
}
