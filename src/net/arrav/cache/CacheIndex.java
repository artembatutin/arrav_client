package net.arrav.cache;

import java.io.IOException;
import java.nio.MappedByteBuffer;

public final class CacheIndex {

	private static final byte[] buffer = new byte[520];
	private final MappedByteBuffer dataFile;
	private final MappedByteBuffer indexFile;
	private final int index;

	/**
	 * Returns the number of files in the cache index.
	 */
	public long getFileCount() {
		try {
			if(indexFile != null) {
				return (indexFile.capacity() / 6);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	CacheIndex(MappedByteBuffer dat, MappedByteBuffer idx, int id) {
		index = id;
		dataFile = dat;
		indexFile = idx;
	}

	/**
	 * Reads a file from the cache.
	 */
	public synchronized byte[] readFile(int id) {
		try {
			seekTo(indexFile, id * 6);
			int n;
			for(int i = 0; i < 6; i += n) {
				try {
					indexFile.get(buffer, i, 6 - i);
					n = 6 - i;
				} catch(Exception e) {
					return null;
				}
			}
			int fileSize = ((buffer[0] & 0xff) << 16) + ((buffer[1] & 0xff) << 8) + (buffer[2] & 0xff);
			int fileBlock = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);
			if(fileBlock <= 0 || fileBlock > dataFile.capacity() / 520L) {
				return null;
			}
			byte[] fileBuffer = new byte[fileSize];
			int read = 0;
			for(int i = 0; read < fileSize; i++) {
				if(fileBlock == 0) {
					return null;
				}
				seekTo(dataFile, fileBlock * 520);
				int size = 0;
				int remaining = fileSize - read;
				if(remaining > 512) {
					remaining = 512;
				}
				int nbytes;
				for(; size < remaining + 8; size += nbytes) {
					try {
						dataFile.get(buffer, size, remaining + 8 - size);
						nbytes = remaining + 8 - size;
					} catch(Exception e) {
						return null;
					}
				}
				int nextFileId = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
				int currentPartId = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
				int nextBlockId = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8) + (buffer[6] & 0xff);
				int nextStoreId = buffer[7] & 0xff;
				if(nextFileId != id || currentPartId != i || nextStoreId != index) {
					return null;
				}
				if(nextBlockId < 0 || nextBlockId > dataFile.capacity() / 520L) {
					return null;
				}
				for(int k3 = 0; k3 < remaining; k3++) {
					fileBuffer[read++] = buffer[k3 + 8];
				}
				fileBlock = nextBlockId;
			}
			return fileBuffer;
		} catch(final IOException _ex) {
			_ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Writes a file to the cache.
	 */
	public synchronized boolean writeFile(int length, byte[] data, int id) {
		boolean flag = writeFile(true, id, length, data);
		if(!flag) {
			flag = writeFile(false, id, length, data);
		}
		return flag;
	}

	/**
	 * Implements the actual file writing.
	 */
	private synchronized boolean writeFile(boolean flag, int id, int length, byte[] data) {
		try {
			int l;
			if(flag) {
				seekTo(indexFile, id * 6);
				int k1;
				for(int i1 = 0; i1 < 6; i1 += k1) {
					try {
						indexFile.get(buffer, i1, 6 - i1);
						k1 = 6 - i1;
					} catch(Exception e) {
						e.printStackTrace();
						return false;
					}
				}
				l = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);
				if(l <= 0 || l > dataFile.capacity() / 520L) {
					return false;
				}
			} else {
				l = (int) ((dataFile.capacity() + 519L) / 520L);
				if(l == 0) {
					l = 1;
				}
			}
			buffer[0] = (byte) (length >> 16);
			buffer[1] = (byte) (length >> 8);
			buffer[2] = (byte) length;
			buffer[3] = (byte) (l >> 16);
			buffer[4] = (byte) (l >> 8);
			buffer[5] = (byte) l;
			seekTo(indexFile, id * 6);
			indexFile.put(buffer, 0, 6);
			int j1 = 0;
			for(int l1 = 0; j1 < length; l1++) {
				int i2 = 0;
				if(flag) {
					seekTo(dataFile, l * 520);
					int j2;
					int l2;
					for(j2 = 0; j2 < 8; j2 += l2) {
						try {
							dataFile.get(buffer, j2, 8 - j2);
							l2 = 8 - j2;
						} catch(Exception e) {
							e.printStackTrace();
							return false;
						}
					}
					if(j2 == 8) {
						final int i3 = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
						final int j3 = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
						i2 = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8) + (buffer[6] & 0xff);
						final int k3 = buffer[7] & 0xff;
						if(i3 != id || j3 != l1 || k3 != index) {
							return false;
						}
						if(i2 < 0 || i2 > dataFile.capacity() / 520L) {
							return false;
						}
					}
				}
				if(i2 == 0) {
					flag = false;
					i2 = (int) ((dataFile.capacity() + 519L) / 520L);
					if(i2 == 0) {
						i2++;
					}
					if(i2 == l) {
						i2++;
					}
				}
				if(length - j1 <= 512) {
					i2 = 0;
				}
				buffer[0] = (byte) (id >> 8);
				buffer[1] = (byte) id;
				buffer[2] = (byte) (l1 >> 8);
				buffer[3] = (byte) l1;
				buffer[4] = (byte) (i2 >> 16);
				buffer[5] = (byte) (i2 >> 8);
				buffer[6] = (byte) i2;
				buffer[7] = (byte) index;
				seekTo(dataFile, l * 520);
				dataFile.put(buffer, 0, 8);
				int k2 = length - j1;
				if(k2 > 512) {
					k2 = 512;
				}
				dataFile.put(data, j1, k2);
				j1 += k2;
				l = i2;
			}

			return true;
		} catch(final IOException _ex) {
			return false;
		}
	}

	/**
	 * Seeks to the given position.
	 */
	private synchronized void seekTo(MappedByteBuffer file, int pos) throws IOException {
		try {
			file.position(pos);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
