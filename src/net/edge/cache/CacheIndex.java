package net.edge.cache;

import java.io.IOException;
import java.io.RandomAccessFile;

public final class CacheIndex {

	private static final byte[] buffer = new byte[520];
	private final RandomAccessFile dataFile;
	private final RandomAccessFile indexFile;
	private final int index;

	/**
	 * Returns the number of files in the cache index.
	 */
	public long getFileCount() {
		try {
			if(indexFile != null) {
				return (indexFile.length() / 6);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	CacheIndex(RandomAccessFile dat, RandomAccessFile idx, int id) {
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
				n = indexFile.read(buffer, i, 6 - i);
				if(n == -1) {
					return null;
				}
			}
			int len = ((buffer[0] & 0xff) << 16) + ((buffer[1] & 0xff) << 8) + (buffer[2] & 0xff);
			int page = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);
			if(page <= 0 || page > dataFile.length() / 520L) {
				return null;
			}
			byte[] obuf = new byte[len];
			int opos = 0;
			for(int i = 0; opos < len; i++) {
				if(page == 0) {
					return null;
				}
				seekTo(dataFile, page * 520);
				int ipos = 0;
				int end = len - opos;
				if(end > 512) {
					end = 512;
				}
				int nbytes;
				for(; ipos < end + 8; ipos += nbytes) {
					nbytes = dataFile.read(buffer, ipos, end + 8 - ipos);
					if(nbytes == -1) {
						return null;
					}
				}
				int k2 = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
				int l2 = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
				int page_ = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8) + (buffer[6] & 0xff);
				int j3 = buffer[7] & 0xff;
				if(k2 != id || l2 != i || j3 != index) {
					return null;
				}
				if(page_ < 0 || page_ > dataFile.length() / 520L) {
					return null;
				}
				for(int k3 = 0; k3 < end; k3++) {
					obuf[opos++] = buffer[k3 + 8];
				}
				page = page_;
			}
			return obuf;
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
					k1 = indexFile.read(buffer, i1, 6 - i1);
					if(k1 == -1) {
						return false;
					}
				}
				l = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);
				if(l <= 0 || l > dataFile.length() / 520L) {
					return false;
				}
			} else {
				l = (int) ((dataFile.length() + 519L) / 520L);
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
			indexFile.write(buffer, 0, 6);
			int j1 = 0;
			for(int l1 = 0; j1 < length; l1++) {
				int i2 = 0;
				if(flag) {
					seekTo(dataFile, l * 520);
					int j2;
					int l2;
					for(j2 = 0; j2 < 8; j2 += l2) {
						l2 = dataFile.read(buffer, j2, 8 - j2);
						if(l2 == -1) {
							break;
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
						if(i2 < 0 || i2 > dataFile.length() / 520L) {
							return false;
						}
					}
				}
				if(i2 == 0) {
					flag = false;
					i2 = (int) ((dataFile.length() + 519L) / 520L);
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
				dataFile.write(buffer, 0, 8);
				int k2 = length - j1;
				if(k2 > 512) {
					k2 = 512;
				}
				dataFile.write(data, j1, k2);
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
	private synchronized void seekTo(RandomAccessFile file, int pos) throws IOException {
		try {
			file.seek(pos);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
