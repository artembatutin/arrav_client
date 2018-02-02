package net.arrav.net;

import net.arrav.activity.panel.impl.DropPanel;
import net.arrav.util.collect.LinkedQueue;
import net.arrav.Client;
import net.arrav.Constants;
import net.arrav.cache.CacheArchive;
import net.arrav.util.DataToolkit;
import net.arrav.util.collect.LinkedDeque;
import net.arrav.util.io.Buffer;

import java.io.*;
import java.net.Socket;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;

public final class OnDemandFetcher implements Runnable {
	
	public int requestedCount;
	private final int[][] fileChecksums;
	private final CRC32 crc32;
	private final LinkedDeque requested;
	public String statusString;
	private int writeLoopCycle;
	private int[] regionObjectFiles;
	private final byte[] ioBuffer;
	private Client client;
	private final LinkedDeque aClass19_1344;
	private int completedSize;
	private int expectedSize;
	private boolean[] newerMap;
	private int[] regionLandFiles;
	private boolean running;
	private OutputStream outputStream;
	private boolean waiting;
	private final LinkedDeque aClass19_1358;
	private final byte[] unzipBuffer;
	private final LinkedQueue requestList;
	private InputStream inputStream;
	private Socket socket;
	private int uncompletedCount;
	private final LinkedDeque aClass19_1368;
	private OnDemandEntry currentEntry;
	private final LinkedDeque aClass19_1370;
	private int[] regionFiles;
	private int loopCycle;

	public OnDemandFetcher() {
		crc32 = new CRC32();
		fileChecksums = new int[Constants.CACHE_INDEX_COUNT][];
		newerMap = new boolean[7000];
		requested = new LinkedDeque();
		statusString = "";
		ioBuffer = new byte[500];
		aClass19_1344 = new LinkedDeque();
		running = true;
		waiting = false;
		aClass19_1358 = new LinkedDeque();
		unzipBuffer = new byte[9999999];
		requestList = new LinkedQueue();
		aClass19_1368 = new LinkedDeque();
		aClass19_1370 = new LinkedDeque();
	}

	public void missingFile(OnDemandEntry entry) {
		String[] archives = {"Model", "Animation", "Music", "Map", "Texture"};
		System.out.println("Couldn't find file: " + entry.id + " in " + archives[entry.type - 1] + " archive.");
	}

	private void readData() {
		try {
			if(socket == null || !socket.isConnected()) {
				socket = Client.instance.openSocket(43595);
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
			}
			int j = inputStream.available();
			if(expectedSize == 0 && j >= 6) {
				waiting = true;
				for(int k = 0; k < 10; k += inputStream.read(ioBuffer, k, 10 - k));
				int type = ioBuffer[0] & 0xff;
				int id = ((ioBuffer[1] & 0xff) << 16) + ((ioBuffer[2] & 0xff) << 8) + (ioBuffer[3] & 0xff);
				int fileSize = ((ioBuffer[4] & 0xff) << 32) + ((ioBuffer[5] & 0xff) << 16) + ((ioBuffer[6] & 0xff) << 8) + (ioBuffer[7] & 0xff);
				int chunkId = ((ioBuffer[8] & 0xff) << 8) + (ioBuffer[9] & 0xff);
				currentEntry = null;
				for(OnDemandEntry entry = (OnDemandEntry) requested.getFirst(); entry != null; entry = (OnDemandEntry) requested.getNext()) {
					if(entry.type == type && entry.id == id) {
						currentEntry = entry;
					}
					if(currentEntry != null) {
						entry.cyclesSinceSend = 0;
					}
				}
				if(currentEntry != null) {
					loopCycle = 0;
					if(fileSize == 0) {
						SignLink.reportError("Rej: " + type + "," + id);
						currentEntry.data = null;
						if(currentEntry.incomplete) {
							synchronized(aClass19_1358) {
								aClass19_1358.addLast(currentEntry);
							}
						} else {
							currentEntry.unlinkPrimary();
						}
						currentEntry = null;
					} else {
						if(currentEntry.data == null && chunkId == 0) {
							currentEntry.data = new byte[fileSize];
						}
						if(currentEntry.data == null) {
							throw new IOException("missing start of file");
						}
					}
				}
				completedSize = chunkId * 500;
				expectedSize = 500;
				if(expectedSize > fileSize - chunkId * 500) {
					expectedSize = fileSize - chunkId * 500;
				}
			}
			if(expectedSize > 0 && j >= expectedSize) {
				waiting = true;
				byte[] data = ioBuffer;
				int size = 0;
				if(currentEntry != null) {
					data = currentEntry.data;
					size = completedSize;
				}
				for(int pos = 0; pos < expectedSize; pos += inputStream.read(data, pos + size, expectedSize - pos));
				if(expectedSize + completedSize >= data.length && currentEntry != null) {
					//if(client.cacheIdx[0] != null) {
					client.cacheIdx[currentEntry.type + 1].writeFile(data.length, data, currentEntry.id);
					//}
					if(!currentEntry.incomplete && currentEntry.type == 3) {
						currentEntry.incomplete = true;
						currentEntry.type = 93;
					}
					if(currentEntry.incomplete) {
						synchronized(aClass19_1358) {
							aClass19_1358.addLast(currentEntry);
						}
					} else {
						currentEntry.unlinkPrimary();
					}
				}
				expectedSize = 0;
			}
		} catch(IOException ioe) {
			try {
				socket.close();
			} catch(Exception e) {
			}
			socket = null;
			inputStream = null;
			outputStream = null;
			expectedSize = 0;
		}
	}

	public void start(CacheArchive archive, Client client) {

		String as1[] = {"model_crc", "anim_crc", "midi_crc", "map_crc", "textures_crc", "images_crc", "object_crc"};
		for(int index = 0; index < as1.length; index++) {
			byte abyte1[] = archive.getFile(as1[index]);
			System.out.println(as1[index] + " - " + abyte1.length);
			int fileAmount = abyte1.length / 4;
			Buffer buffer = new Buffer(abyte1);
			fileChecksums[index] = new int[fileAmount];
			for(int fileId = 0; fileId < fileAmount; fileId++) {
				fileChecksums[index][fileId] = buffer.getInt();
			}
		}
		
		byte abyte1[] = archive.getFile("mob_drops");
		System.out.println("mob_drops - " + abyte1.length);
		int fileAmount = abyte1.length / 2;
		Buffer mobs = new Buffer(abyte1);
		DropPanel.seekable = new int[fileAmount];
		for(int i = 0; i< fileAmount; i++) {
			DropPanel.seekable[i] = mobs.getSShort();
		}
		

		Buffer buffer = new Buffer(Constants.JAGGRAB_ENABLED ? archive.getFile("map_index") : DataToolkit.readFile(SignLink.getCacheDir() + "map_index"));
		int length = buffer.getUShort();
		regionFiles = new int[length];
		regionLandFiles = new int[length];
		regionObjectFiles = new int[length];
		for(int i = 0; i < length; i++) {
			regionFiles[i] = buffer.getUShort();
			regionLandFiles[i] = buffer.getUShort();
			regionObjectFiles[i] = buffer.getUShort();
			if(buffer.getBoolean()) {
				newerMap[regionLandFiles[i]] = true;
				newerMap[regionObjectFiles[i]] = true;
			}
		}
		System.out.println("map_index size: " + length);

		this.client = client;
		running = true;
		client.startThread(this, 2);
	}


	public void packMapIndex() {
		try {
			Buffer buf = new Buffer(new byte[2 + regionFiles.length * 7]);
			buf.putShort(regionFiles.length);
			for(int i = 0; i < regionFiles.length; i++) {
				buf.putShort(regionFiles[i]);
				buf.putShort(regionLandFiles[i]);
				buf.putShort(regionObjectFiles[i]);
				buf.putByte(newerMap[regionLandFiles[i]] || newerMap[regionObjectFiles[i]] ? 1 : 0);
			}
			DataToolkit.writeFile(new File(SignLink.getCacheDir() + "map_index"), buf.data);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public int getRequestCount() {
		synchronized(requestList) {
			return requestList.size();
		}
	}

	public int getModelCount() {
		return 65929;
	}

	public void disable() {
		running = false;
	}

	private void closeRequest(OnDemandEntry entry) {
		try {
			if(socket == null || !socket.isConnected()) {
				socket = Client.instance.openSocket(43595);
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				loopCycle = 0;
			}
			ioBuffer[0] = (byte) entry.type;
			ioBuffer[1] = (byte) (entry.id >> 24);
			ioBuffer[2] = (byte) (entry.id >> 16);
			ioBuffer[3] = (byte) (entry.id >> 8);
			ioBuffer[4] = (byte) entry.id;
			outputStream.write(ioBuffer, 0, 5);
			writeLoopCycle = 0;
			return;
		} catch(IOException ioe) {
		}
		try {
			socket.close();
		} catch(Exception e) {
		}
		socket = null;
		inputStream = null;
		outputStream = null;
		expectedSize = 0;
	}

	public void addRequest(int type, int id) {
		if(type < 0 || id < 0)
			return;
		synchronized(requestList) {
			for(OnDemandEntry entry = (OnDemandEntry) requestList.getFirst(); entry != null; entry = (OnDemandEntry) requestList.getNext()) {
				if(entry.type == type && entry.id == id) {
					return;
				}
			}
			final OnDemandEntry entry = new OnDemandEntry();
			entry.type = type;
			entry.id = id;
			entry.incomplete = true;
			synchronized(aClass19_1370) {
				aClass19_1370.addLast(entry);
			}
			requestedCount += 1;
			requestList.addLast(entry);
		}
	}

	public void run() {
		try {
			while(running) {
				int t = 20;
				if(client.cacheIdx[0] != null) {
					t = 50;
				}
				try {
					Thread.sleep(t);
				} catch(Exception e) {
				}
				waiting = true;
				for(int i = 0; i < 100; i++) {
					if(!waiting) {
						break;
					}
					waiting = false;
					checkReceived();
					handleFailed();
					if(uncompletedCount == 0 && i >= 5) {
						break;
					}
					if(inputStream != null) {
						readData();
					}
				}
				boolean flag = false;
				for(OnDemandEntry entry = (OnDemandEntry) requested.getFirst(); entry != null; entry = (OnDemandEntry) requested.getNext()) {
					if(entry.incomplete) {
						flag = true;
						entry.cyclesSinceSend++;
						if(entry.cyclesSinceSend > 50) {
							entry.cyclesSinceSend = 0;
							closeRequest(entry);
						}
					}
				}
				if(!flag) {
					for(OnDemandEntry onDemandData_1 = (OnDemandEntry) requested.getFirst(); onDemandData_1 != null; onDemandData_1 = (OnDemandEntry) requested.getNext()) {
						flag = true;
						onDemandData_1.cyclesSinceSend++;
						if(onDemandData_1.cyclesSinceSend > 50) {
							onDemandData_1.cyclesSinceSend = 0;
							closeRequest(onDemandData_1);
						}
					}
				}
				if(flag) {
					loopCycle++;
					if(loopCycle > 750) {
						try {
							socket.close();
						} catch(Exception e) {
						}
						socket = null;
						inputStream = null;
						outputStream = null;
						expectedSize = 0;
					}
				} else {
					loopCycle = 0;
					statusString = "";
				}
				if(client.loggedIn && socket != null && outputStream != null && (client.cacheIdx[0] == null)) {
					writeLoopCycle++;
					if(writeLoopCycle > 500) {
						writeLoopCycle = 0;
						ioBuffer[0] = 0;
						ioBuffer[1] = 0;
						ioBuffer[2] = 0;
						ioBuffer[3] = 0;
						ioBuffer[4] = 0;
						ioBuffer[5] = 10;
						try {
							outputStream.write(ioBuffer, 0, 6);
						} catch(IOException e) {
							loopCycle = 5000;
						}
					}
				}
			}
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}

	public void passiveRequest(int id, int type) {
		if(client.cacheIdx[0] == null)
			return;
		final OnDemandEntry entry = new OnDemandEntry();
		entry.type = type;
		entry.id = id;
		entry.incomplete = false;
		synchronized(aClass19_1344) {
			aClass19_1344.addLast(entry);
		}
	}

	public OnDemandEntry nextRequest() {
		OnDemandEntry entry;
		synchronized(aClass19_1358) {
			entry = (OnDemandEntry) aClass19_1358.removeFirst();
		}
		if(entry == null) {
			return null;
		}
		synchronized(requestList) {
			entry.unlinkSecondary();
		}
		requestedCount -= 1;
		if(entry.data == null) {
			return entry;
		}
		int len = 0;
		try {
			final GZIPInputStream gzipis = new GZIPInputStream(new ByteArrayInputStream(entry.data));
			do {
				if(len == unzipBuffer.length) {
					System.out.println("File: " + entry.id + " too large");
					break;
				}
				int dlen = -1;
				try {
					dlen = gzipis.read(unzipBuffer, len, unzipBuffer.length - len);
				} catch(Exception e) {
					e.printStackTrace();
				}
				if(dlen == -1) {
					break;
				}
				len += dlen;
			} while(true);
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		entry.data = new byte[len];
		System.arraycopy(unzipBuffer, 0, entry.data, 0, len);
		return entry;
	}

	public int getMapId(int type, int mapX, int mapY) {
		int coordinates = (mapY << 8) + mapX;
		for(int region = 0; region < regionFiles.length; region++) {
			if(regionFiles[region] == coordinates) {
				if(type == 0) {
					return regionLandFiles[region];
				} else {
					return regionObjectFiles[region];
				}
			}
		}
		return -1;
	}
	
	public boolean mapOld(int map) {
		if(map < 0 || map > newerMap.length) {
			return true;
		}
		//newerMap[map] = true;
		return !newerMap[map];
	}
	
	public void setOld(int map) {
		if(map < 0 || map > newerMap.length) {
			return;
		}
		newerMap[map] = false;
	}
	
	public void setNew(int map) {
		if(map < 0 || map > newerMap.length) {
			return;
		}
		newerMap[map] = true;
	}
	
	
	public boolean mapCached(int map) {
		for(int region = 0; region < regionFiles.length; region++) {
			if(regionObjectFiles[region] == map) {
				return true;
			}
		}
		return false;
	}

	private void handleFailed() {
		uncompletedCount = 0;
		for(OnDemandEntry entry = (OnDemandEntry) requested.getFirst(); entry != null; entry = (OnDemandEntry) requested.getNext()) {
			if(entry.incomplete) {
				uncompletedCount++;
				//System.out.println("Error: model is incomplete or missing  [ type = " + entry.type + "]  [nodeId = " + entry.id + "]");
			}
		}
		while(uncompletedCount < 10) {
			try {
				final OnDemandEntry entry = (OnDemandEntry) aClass19_1368.removeFirst();
				if(entry == null)
					break;
				requested.addLast(entry);
				//missingFile(entry);
				uncompletedCount++;
				closeRequest(entry);
				waiting = true;
			} catch(Exception e) {
				final OnDemandEntry entry = (OnDemandEntry) aClass19_1368.removeFirst();
				e.printStackTrace();
				//System.out.println("missing: type: "+entry.type+" ID"+entry.id);
			}
		}
	}

	public void method566() {
		synchronized(aClass19_1344) {
			aClass19_1344.clear();
		}
	}

	private void checkReceived() {
		OnDemandEntry entry;
		synchronized(aClass19_1370) {
			entry = (OnDemandEntry) aClass19_1370.removeFirst();
		}
		while(entry != null) {
			waiting = true;
			byte[] data = null;
			if(client.cacheIdx[0] != null) {
				if(entry.id != -1)
					data = client.cacheIdx[entry.type + 1].readFile(entry.id);
			}
			if(Constants.JAGGRAB_ENABLED) {
				if(entry.type <= fileChecksums.length && entry.id <= fileChecksums[entry.type].length) {
					if(!crcMatches(fileChecksums[entry.type][entry.id], data)) {
						data = null;
					}
				}
			}
			synchronized(aClass19_1370) {
				if(data == null) {
					aClass19_1368.addLast(entry);
				} else {
					entry.data = data;
					synchronized(aClass19_1358) {
						aClass19_1358.addLast(entry);
					}
				}
				entry = (OnDemandEntry) aClass19_1370.removeFirst();
			}
		}
	}

	private boolean crcMatches(int j, byte abyte0[]) {
		if(abyte0 == null || abyte0.length < 2) {
			return false;
		}
		int k = abyte0.length - 2;
		crc32.reset();
		crc32.update(abyte0, 0, k);
		int i1 = (int) crc32.getValue();
		return i1 == j;
	}

	/**
	 * Grabs the checksum of a file from the cache.
	 * @param type The type of file (0 = model, 1 = anim, 2 = midi, 3 = map).
	 * @param id   The id of the file.
	 * @return
	 */
	public int getChecksum(int type, int id) {
		int crc = 0;
		byte[] data = client.cacheIdx[type + 1].readFile(id);
		if(data != null) {
			int length = data.length - 2;
			crc32.reset();
			crc32.update(data, 0, length);
			crc = (int) crc32.getValue();
		}
		return crc;
	}

	/**
	 * Grabs the version of a file from the cache.
	 * @param type The type of file (0 = model, 1 = anim, 2 = midi, 3 = map).
	 * @param id   The id of the file.
	 * @return
	 */
	public int getVersion(int type, int id) {
		int version = -1;
		byte[] data = client.cacheIdx[type + 1].readFile(id);
		if(data != null) {
			int length = data.length - 2;
			version = ((data[length] & 0xff) << 8) + (data[length + 1] & 0xff);
		}
		return version;
	}

	/**
	 * Writes the checksum list for the specified archive type and length.
	 * @param type The type of archive (0 = model, 1 = anim, 2 = midi, 3 = map).
	 */
	public void writeChecksumList(int type) {
		String name = null;
		switch(type) {
			case 0:
				name = "model";
				break;
			case 1:
				name = "anim";
				break;
			case 2:
				name = "midi";
				break;
			case 3:
				name = "map";
				break;
			case 4:
				name = "textures";
				break;
			case 5:
				name = "images";
				break;
			case 6:
				name = "objects";
				break;
			case 7:
				name = "osrs_model";
				break;
		}
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(SignLink.getCacheDir() + "/jaggrab/" + name + "_crc.dat"));
			for(int index = 0; index < client.cacheIdx[type + 1].getFileCount(); index++) {
				out.writeInt(getChecksum(type, index));
			}
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		/*try {
			FileWriter outFile = new FileWriter(SignLink.getCacheDir() + "/jaggrab/" + name + "_crc.txt");
			PrintWriter out = new PrintWriter(outFile);
			for(int index = 0; index < client.cacheIdx[type + 1].getFileCount(); index++) {
				out.println("Type: " + type + ", id: " + index + ", crc: " + getChecksum(type, index));
			}
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}*/
	}

	/**
	 * Writes the version list for the specified archive type and length.
	 * @param type The type of archive (0 = model, 1 = anim, 2 = midi, 3 = map).
	 */
	public void writeVersionList(int type) {
		String name = null;
		switch(type) {
			case 0:
				name = "model";
				break;
			case 1:
				name = "anim";
				break;
			case 2:
				name = "midi";
				break;
			case 3:
				name = "map";
				break;
			case 4:
				name = "textures";
				break;
			case 5:
				name = "images";
				break;
			case 6:
				name = "objects";
				break;
			case 7:
				name = "osrs_model";
				break;
		}
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(SignLink.getCacheDir() + "/jaggrab/" + name + "_version.dat"));
			for(int index = 0; index < client.cacheIdx[type + 1].getFileCount(); index++) {
				out.writeShort(getVersion(type, index));
			}
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			FileWriter outFile = new FileWriter(SignLink.getCacheDir() + "/jaggrab/" + name + "_version.txt");
			PrintWriter out = new PrintWriter(outFile);
			for(int index = 0; index < client.cacheIdx[type + 1].getFileCount(); index++) {
				out.println("Type: " + type + ", id: " + index + ", version: " + getVersion(type, index));
			}
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}