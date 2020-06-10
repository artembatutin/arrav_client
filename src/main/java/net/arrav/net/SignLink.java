package net.arrav.net;

import net.arrav.Constants;
import net.arrav.Client;
import net.arrav.cache.CacheIndex;
import net.arrav.cache.CacheIndexDefault;
import net.arrav.cache.CacheIndexMapped;

import java.applet.Applet;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public final class SignLink implements Runnable {

	public static int storeId = 32;
	public static boolean sunJava;
	public static Applet mainApp = null;
	public static String dns = null;
	public static boolean reportError = true;
	public static String errorName = "";
	
	private static RandomAccessFile cacheDatFile = null;
	private static MappedByteBuffer cacheDat = null;
	private static final RandomAccessFile[] cacheIdxFile = new RandomAccessFile[Constants.CACHE_INDEX_COUNT];
	private static final MappedByteBuffer[] cacheIdx = new MappedByteBuffer[Constants.CACHE_INDEX_COUNT];
	private static boolean active;
	private static int threadLiveId;
	private static InetAddress socketIp;
	private static int socketReq;
	private static Socket socket = null;
	private static int threadReqPri = 1;
	private static Runnable threadReq = null;
	private static String dnsReq = null;
	private static String urlReq = null;
	private static DataInputStream urlStream = null;

	/**
	 * Can't initiate this class.
	 */
	private SignLink() {}

	/**
	 * DNS lookup.
	 */
	public static synchronized void dnsLookUp(String s) {
		dns = s;
		dnsReq = s;
	}

	/**
	 * Gets the set file path for the cache directory.
	 */
	public static String getCacheDir() {
		/*if(Constants.USER_HOME_FILE_STORE) {
			final String home = System.getProperty("user.home");
			final String separator = System.getProperty("file.separator");
			StringBuilder sb = new StringBuilder();
			sb.append(home);
			if(!home.endsWith(separator)) {
				sb.append(separator);
			}
			sb.append(".arrav");
			sb.append(separator);
			String dir = sb.toString();
			File file = new File(dir);
			if(!file.exists()) {
				Client.firstRun = true;
				file.mkdir();
			}
			return dir;
		} else {
			return "./cache/";
		}*/
		return "./cache/";
	}

	/**
	 * Returns the error message.
	 */
	public static void reportError(String message) {
		System.out.println("Error: " + message);
	}

	/**
	 * Opens the socket.
	 */
	public static synchronized Socket openSocket(int socketPort) throws IOException {
		for(socketReq = socketPort; socketReq != 0; ) {
			try {
				Thread.sleep(50L);
			} catch(final Exception ignored) {
				//empty
			}
		}
		if(socket == null) {
			throw new IOException("could not open socket");
		} else {
			return socket;
		}
	}

	/**
	 * Opens the url.
	 */
	public static synchronized DataInputStream openUrl(String url) throws IOException {
		for(urlReq = url; urlReq != null; ) {
			try {
				Thread.sleep(50L);
			} catch(final Exception ignored) {
				//empty
			}
		}
		if(urlStream == null) {
			throw new IOException("could not open: " + url);
		} else {
			return urlStream;
		}
	}

	/**
	 * Start the protocol.
	 */
	public static void startPriv(InetAddress inetaddress) {
		threadLiveId = (int) (Math.random() * 99999999D);
		if(active) {
			try {
				Thread.sleep(500L);
			} catch(final Exception ignored) {
				//empty
			}
			active = false;
		}
		socketReq = 0;
		threadReq = null;
		dnsReq = null;
		urlReq = null;
		socketIp = inetaddress;
		final Thread thread = new Thread(new SignLink());
		thread.setDaemon(true);
		thread.start();
		while(!active) {
			try {
				Thread.sleep(50L);
			} catch(final Exception ignored) {
				//empty
			}
		}
	}

	/**
	 * Start the thread.
	 */
	public static synchronized void startThread(Runnable runnable, int i) {
		threadReqPri = i;
		threadReq = runnable;
	}

	/**
	 * Initialises the cache.
	 */
	@Override
	public void run() {
		active = true;
		final String cacheDir = getCacheDir();
		try {
			cacheDatFile = new RandomAccessFile(cacheDir + "main_file_cache.dat", "rw");
			if(Constants.MEMORY_MAPPED_CACHE)
				cacheDat = cacheDatFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, cacheDatFile.length());
			for(int j = 0; j < Constants.CACHE_INDEX_COUNT; j++) {
				cacheIdxFile[j] = new RandomAccessFile(cacheDir + "main_file_cache.idx" + j, "rw");
				if(Constants.MEMORY_MAPPED_CACHE)
					cacheIdx[j] = cacheIdxFile[j].getChannel().map(FileChannel.MapMode.READ_WRITE, 0, cacheIdxFile[j].length());
			}
			
			if(Constants.MEMORY_MAPPED_CACHE) {
				boolean loaded = false;
				while(!loaded) {
					loaded = cacheDat.isLoaded();
					for(int j = 0; j < Constants.CACHE_INDEX_COUNT; j++) {
						if(!cacheIdx[j].isLoaded())
							loaded = false;
					}
					Thread.sleep(100l);
				}
			}
		} catch(final Exception e) {
			e.printStackTrace();
		}
		for(final int i = threadLiveId; threadLiveId == i; ) {
			if(socketReq != 0) {
				try {
					socket = new Socket(socketIp, socketReq);
				} catch(final Exception _ex) {
					socket = null;
				}
				socketReq = 0;
			} else if(threadReq != null) {
				final Thread thread = new Thread(threadReq);
				thread.setDaemon(true);
				thread.start();
				thread.setPriority(threadReqPri);
				threadReq = null;
			} else if(dnsReq != null) {
				try {
					dns = InetAddress.getByName(dnsReq).getHostName();
				} catch(final Exception _ex) {
					dns = "unknown";
				}
				dnsReq = null;
			} else if(urlReq != null) {
				try {
					System.out.println("urlstream");
					urlStream = new DataInputStream(new URL(mainApp.getCodeBase(), urlReq).openStream());
				} catch(final Exception _ex) {
					urlStream = null;
				}
				urlReq = null;
			}
			try {
				Thread.sleep(50L);
			} catch(final Exception ignored) {
				//empty
			}
		}
	}
	
	public static CacheIndex getIndex(Client client, int i) {
		if(Constants.MEMORY_MAPPED_CACHE)
			return client.cacheIdx[i] = new CacheIndexMapped(cacheDat, cacheIdx[i], i + 1);
		else
			return client.cacheIdx[i] = new CacheIndexDefault(cacheDatFile, cacheIdxFile[i], i + 1);
	}

}
