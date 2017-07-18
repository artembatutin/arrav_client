package net.edge.cache;

import net.edge.Constants;
import net.edge.media.img.PaletteImage;
import net.edge.Client;
import net.edge.Config;
import net.edge.Updater;
import net.edge.activity.Activity;
import net.edge.activity.TitleActivity;
import net.edge.cache.impl.*;
import net.edge.cache.unit.ImageCache;
import net.edge.media.GraphicalComponent;
import net.edge.media.Rasterizer2D;
import net.edge.media.font.BitmapFont;
import net.edge.sign.SignLink;
import net.edge.util.ThreadUtils;
import net.edge.util.io.Buffer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class CacheUnpacker {

	/**
	 * Expected Checksum checks.
	 */
	public final static int[] EXPECTED_CRC = new int[9];

	/**
	 * Success fully loaded toggle.
	 */
	public static boolean successfullyLoaded = false;

	/**
	 * Loading socket (on-demand protocol).
	 */
	private Socket loadingSocket;

	/**
	 * Loading circle data.
	 */
	private int[] loadingCircleStarts;
	private int[] loadingCircleLengths;
	private int circleAngle = 0;

	/**
	 * Client access instance.
	 */
	private final Client client;

	/**
	 * Loading progress information.
	 */
	private volatile String message = "";
	private volatile String sideMessage;
	private volatile int progress;

	/**
	 * Is loading finished?
	 */
	private volatile boolean finished;

	/**
	 * The updater that updates the client.
	 */
	private Updater updater;

	/**
	 * Constructor.
	 */
	public CacheUnpacker(Client client) {
		this.client = client;
	}

	/**
	 * Main runnable method.
	 */
	public void load() {
		final int centerX = client.windowWidth / 2;
		final int centerY = client.windowHeight / 2;
		if(Constants.JAGGRAB_ENABLED) {
			updater = new Updater(client);
			updater.run();
		}

		// Initializing
		this.init();
		Activity.init();
		Config.def.load();
		message = "Preparing packing modules.";
		client.startThread(new LoaderScreen(centerX, centerY), 8);
		if(!Constants.USER_HOME_FILE_STORE) {
			client.repackCacheIndex(1);
			client.repackCacheIndex(2);
			client.repackCacheIndex(3);
			client.repackCacheIndex(4);
			client.repackCacheIndex(5);
			client.repackCacheIndex(6);
			client.repackCacheIndex(7);
			client.repackCacheIndex(8);
		}
		load(new ProtocolLoader(getCacheArchive(5, "versionlist", CacheUnpacker.EXPECTED_CRC[5])));
		load(new MediaLoader(getCacheArchive(4, "media", CacheUnpacker.EXPECTED_CRC[4])));
		load(new ConfigurationLoader(getCacheArchive(2, "config", CacheUnpacker.EXPECTED_CRC[2])));
		load(new InterfaceLoader(getCacheArchive(3, "interface", CacheUnpacker.EXPECTED_CRC[3])));
		load(new SceneLoader());
		finished = true;
		client.titleActivity.initialize();
		TitleActivity.scrollOpened = true;
		TitleActivity.scrollValue = 110;
		// Resetting when finished
		loadingCircleStarts = null;
		loadingCircleLengths = null;
		message = null;
		System.gc();
	}
	
	private void load(CacheLoader loader) {
		message = loader.message();
		loader.run(client);
		progress++;
	}

	/**
	 * Main Initializer usage: Unpacks the start up archive. And all important startup files.
	 */
	private void init() {
		if(SignLink.sunJava) {
			client.minDelay = 5;
		}
		if(SignLink.cacheDat != null) {
			for(int i = 0; i < Constants.CACHE_INDEX_COUNT; i++) {
				client.cacheIdx[i] = new CacheIndex(SignLink.cacheDat, SignLink.cacheIdx[i], i + 1);
			}
		}
		if(Constants.JAGGRAB_ENABLED) {
			// Initializing executor
			final ExecutorService executor = ThreadUtils.createThread("Connector");
			executor.execute(this::compareCrcValues);
			do {
				updater.draw(message);
			} while(!finished);
			finished = false;
		}
		// Unpacking startup title components.
		client.titleArchive = getCacheArchive(1, "title", CacheUnpacker.EXPECTED_CRC[1]);
		// Unpacking Font
		client.smallFont = new BitmapFont(client.titleArchive, "p11_full", false);
		client.plainFont = new BitmapFont(client.titleArchive, "p12_full", false);
		client.boldFont = new BitmapFont(client.titleArchive, "b12_full", false);
		client.fancyFont = new BitmapFont(client.titleArchive, "q8_full", true);
		client.smallHitFont = new BitmapFont(client.titleArchive, "hit_full", false);
		client.bigHitFont = new BitmapFont(client.titleArchive, "critical_full", true);
		loadTitleScreen();
		//Unpacking images.
		client.titleActivity = new TitleActivity();
		ImageCache.init(client);

	}

	private void loadTitleScreen() {
		client.runes = new PaletteImage[12];
		int j = 0;
		try {
			j = Integer.parseInt(client.getParameter("fl_icon"));
		} catch(final Exception _ex) {
		}
		if(j == 0) {
			for(int k = 0; k < 12; k++) {
				client.runes[k] = new PaletteImage(client.titleArchive, "runes", k);
			}
		} else {
			for(int l = 0; l < 12; l++) {
				client.runes[l] = new PaletteImage(client.titleArchive, "runes", 12 + (l & 3));
			}
		}
		client.anIntArray851 = new int[256];
		for(int k1 = 0; k1 < 64; k1++) {
			client.anIntArray851[k1] = k1 * 0x40000;
		}
		for(int l1 = 0; l1 < 64; l1++) {
			client.anIntArray851[l1 + 64] = 0xff0000 + 1024 * l1;
		}
		for(int i2 = 0; i2 < 64; i2++) {
			client.anIntArray851[i2 + 128] = 0xffff00 + 4 * i2;
		}
		for(int j2 = 0; j2 < 64; j2++) {
			client.anIntArray851[j2 + 192] = 0xffffff;
		}
		client.anIntArray852 = new int[256];
		for(int k2 = 0; k2 < 64; k2++) {
			client.anIntArray852[k2] = k2 * 1024;
		}
		for(int l2 = 0; l2 < 64; l2++) {
			client.anIntArray852[l2 + 64] = 65280 + 4 * l2;
		}
		for(int i3 = 0; i3 < 64; i3++) {
			client.anIntArray852[i3 + 128] = 65535 + 0x40000 * i3;
		}
		for(int j3 = 0; j3 < 64; j3++) {
			client.anIntArray852[j3 + 192] = 0xffffff;
		}
		client.anIntArray853 = new int[256];
		for(int k3 = 0; k3 < 64; k3++) {
			client.anIntArray853[k3] = k3 * 4;
		}
		for(int l3 = 0; l3 < 64; l3++) {
			client.anIntArray853[l3 + 64] = 255 + 0x40000 * l3;
		}
		for(int i4 = 0; i4 < 64; i4++) {
			client.anIntArray853[i4 + 128] = 0xff00ff + 1024 * i4;
		}
		for(int j4 = 0; j4 < 64; j4++) {
			client.anIntArray853[j4 + 192] = 0xffffff;
		}
		client.anIntArray1190 = new int[32768];
		client.anIntArray1191 = new int[32768];
		client.randomizeBackground();
		if(!successfullyLoaded) {
			successfullyLoaded = true;
			client.startThread(client, 2);
		}
	}


	public CacheArchive getCacheArchive(int index, String fileName, int crc) {
		byte[] buffer = null;
		int timeToWait = 5;

		try {
			if(client.cacheIdx[0] != null) {
				buffer = client.cacheIdx[0].readFile(index);
			}
		} catch(Exception _ex) {
			_ex.printStackTrace();
		}

		if(buffer != null) {
			if(Constants.JAGGRAB_ENABLED) {
				System.out.println("new jaggrab " + fileName);
				client.getCrc().reset();
				client.getCrc().update(buffer);
				int crcValue = (int) client.getCrc().getValue();

				if(crcValue != crc) {
					buffer = null;
				}
			}
		}

		if(buffer != null) {
			System.out.println("getting jaggrab " + fileName);
			CacheArchive streamLoader = new CacheArchive(index, fileName, buffer);
			return streamLoader;
		}

		int errorCount = 0;

		while(buffer == null) {

			String error = "Unknown error";
			sideMessage = "Requesting " + fileName;

			try {
				DataInputStream datainputstream = openJagGrabInputStream(fileName + crc);
				byte temp[] = new byte[6];
				datainputstream.readFully(temp, 0, 6);
				Buffer stream = new Buffer(temp);
				stream.pos = 3;
				int totalLength = stream.getUMedium() + 6;
				int currentLength = 6;
				buffer = new byte[totalLength];
				System.arraycopy(temp, 0, buffer, 0, 6);

				while(currentLength < totalLength) {
					int remainingAmount = totalLength - currentLength;

					if(remainingAmount > 1000) {
						remainingAmount = 1000;
					}

					int remaining = datainputstream.read(buffer, currentLength, remainingAmount);

					if(remaining < 0) {
						error = "Length error: " + currentLength + "/" + totalLength;
						throw new IOException("EOF");
					}

					currentLength += remaining;
					int percentage = currentLength * 100 / totalLength;

					sideMessage = "Loading " + fileName + " - " + percentage + "%";
				}

				datainputstream.close();

				try {
					if(client.cacheIdx[0] != null) {
						client.cacheIdx[0].writeFile(buffer.length, buffer, index);
					}
				} catch(Exception _ex) {
					_ex.printStackTrace();
					client.cacheIdx[0] = null;
				}

				if(Constants.JAGGRAB_ENABLED) {
					client.getCrc().reset();
					client.getCrc().update(buffer);
					int currentCrc = (int) client.getCrc().getValue();

					if(currentCrc != crc) {
						buffer = null;
						errorCount++;
						error = "Checksum error: " + currentCrc;
					}
				}
			} catch(IOException ioexception) {
				if(error.equals("Unknown error")) {
					error = "Connection error";
				}

				ioexception.printStackTrace();
				buffer = null;
			} catch(NullPointerException _ex) {
				error = "Null error";
				buffer = null;

				_ex.printStackTrace();
				if(!SignLink.reportError) {
					return null;
				}
			} catch(ArrayIndexOutOfBoundsException _ex) {
				error = "Bounds error";
				buffer = null;

				_ex.printStackTrace();
				if(!SignLink.reportError) {
					return null;
				}
			} catch(Exception _ex) {
				error = "Unexpected error";
				buffer = null;

				_ex.printStackTrace();
				if(!SignLink.reportError) {
					return null;
				}
			}
			if(buffer == null) {
				for(int seconds = timeToWait; seconds > 0; seconds--) {
					if(errorCount >= 3) {
						sideMessage = "Game updated - please reload page";
						seconds = 10;
					} else {
						throw new RuntimeException("Unable to find archive: " + fileName);
					}

					try {
						Thread.sleep(1000L);
					} catch(Exception _ex) {
						_ex.printStackTrace();
					}
				}

				timeToWait *= 2;

				if(timeToWait > 60) {
					timeToWait = 60;
				}

				//httpFallback = !httpFallback;
			}
		}

		CacheArchive archive = new CacheArchive(index, fileName, buffer);
		return archive;
	}

	private void compareCrcValues() {
		int secondsToWait = 5;
		EXPECTED_CRC[8] = 0;
		int checksumCount = 0;

		while(EXPECTED_CRC[8] == 0) {
			message = "Connecting to the web server...";

			try {
				DataInputStream in = openJagGrabInputStream("crc" + (int) (Math.random() * 99999999D) + "-" + 317);
				Buffer buffer = new Buffer(new byte[40]);
				in.readFully(buffer.data, 0, 40);
				in.close();

				for(int index = 0; index < 9; index++) {
					EXPECTED_CRC[index] = buffer.getInt();
				}

				int checksumValue = buffer.getInt();
				int expectedValue = 1235;

				for(int index = 0; index < 9; index++) {
					expectedValue = (expectedValue << 1) + EXPECTED_CRC[index];
				}

				if(checksumValue != expectedValue) {
					EXPECTED_CRC[8] = 0;
				}
			} catch(IOException _ex) {
				_ex.printStackTrace();
				EXPECTED_CRC[8] = 0;
			} catch(Exception _ex) {
				_ex.printStackTrace();
				EXPECTED_CRC[8] = 0;

				if(!SignLink.reportError) {
					return;
				}
			}
			try {
				Thread.sleep(1000L);
			} catch(Exception _ex) {
				_ex.printStackTrace();
			}
			if(EXPECTED_CRC[8] == 0) {
				checksumCount++;

				for(int seconds = secondsToWait; seconds > 0; seconds--) {
					if(checksumCount >= 10) {
						message = "Game updated - please reload.";
						seconds = 10;
					} else {
						message = "Error - Will retry in " + seconds + " secs.";
					}

					try {
						Thread.sleep(1000L);
					} catch(Exception _ex) {
					}
				}

				secondsToWait *= 2;

				if(secondsToWait > 60) {
					secondsToWait = 60;
				}
				compareCrcValues();
			}
			finished = true;
		}
	}

	/**
	 * Enables jag grab protocol for on-demand file server.
	 * @param file fileName
	 * @return file input stream
	 * @throws IOException
	 */
	private DataInputStream openJagGrabInputStream(String file) throws IOException {
		if(loadingSocket != null) {
			try {
				loadingSocket.close();
			} catch(final Exception ignored) {
			}
			loadingSocket = null;
		}
		loadingSocket = client.openSocket(43596);
		loadingSocket.setSoTimeout(10000);
		final java.io.InputStream inputstream = loadingSocket.getInputStream();
		final OutputStream outputstream = loadingSocket.getOutputStream();
		outputstream.write(("JAGGRAB /" + file + "\n\n").getBytes());
		return new DataInputStream(inputstream);
	}

	/**
	 * Finalize stage.
	 */
	public void reset() {
		successfullyLoaded = false;
		while(successfullyLoaded) {
			successfullyLoaded = false;
			try {
				Thread.sleep(50L);
			} catch(final Exception ignored) {
			}
		}
		successfullyLoaded = true;
		client.runes = null;
		client.anIntArray851 = null;
		client.anIntArray852 = null;
		client.anIntArray853 = null;
		client.anIntArray1190 = null;
		client.anIntArray1191 = null;
	}
	
	public class LoaderScreen implements Runnable {
		
		/**
		 * Main graphical component.
		 */
		private GraphicalComponent component;
		
		private final int x, y;
		
		public LoaderScreen(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public void run() {
			// Drawing the loading screen while the loading process is going
			long t1 = System.currentTimeMillis();
			do {
				updateLoading(x, y);
				long t2 = System.currentTimeMillis();
				if(t2 - t1 < 10) {
					try {
						Thread.sleep(10 - (t2 - t1));
					} catch(InterruptedException ignored) {
					}
				}
				t1 = t2;
			} while(!finished);
			component = null;
		}
		
		/**
		 * Loading screen draw loop.
		 */
		private void updateLoading(int centerX, int centerY) {
			if(client.onDemandRequester == null)
				return;
			if(loadingCircleStarts == null || loadingCircleLengths == null) {
				loadingCircleStarts = new int[74];
				loadingCircleLengths = new int[74];
				for(int i = 37; i >= 0; i--) {
					final int amt = (int) Math.sqrt(37 * 37 - (37 - i) * (37 - i));
					loadingCircleLengths[i] = 2 * amt;
					loadingCircleLengths[73 - i] = 2 * amt;
					loadingCircleStarts[i] = -amt + 37;
					loadingCircleStarts[73 - i] = -amt + 37;
				}
			}
			if(component == null || component.getWidth() != client.windowWidth || component.getHeight() != client.windowHeight) {
				component = new GraphicalComponent(client.windowWidth, client.windowHeight);
				component.setCanvas();
			} else {
				component.setCanvas();
				Rasterizer2D.clearCanvas();
			}
			circleAngle = (circleAngle + 20) & 2047;
			Rasterizer2D.fillRectangle(0, 0, client.windowWidth, client.windowHeight, 0x070505);
			ImageCache.get(859).drawImage(centerX - 433, centerY - 305);
			ImageCache.get(860).drawImage(centerX, centerY - 305);
			ImageCache.get(861).drawImage(centerX - 433, centerY);
			ImageCache.get(862).drawImage(centerX, centerY);
			Rasterizer2D.fillRoundedRectangle(centerX - 154, centerY - 84, 308, 113, 20, 0x000000, 100);
			Rasterizer2D.fillRoundedRectangle(centerX - 150, centerY - 80, 300, 105, 17, 0x000000, 225);
			if(ImageCache.get(7, true) != null)
				ImageCache.get(7, true).drawAffineTransformedAlphaImage(centerX - 37, centerY - 55, 74, 74, 37, 37, loadingCircleStarts, loadingCircleLengths, circleAngle, 256);
			client.fancyFont.drawCenteredEffectString(progress + "/5", centerX, centerY - 10, 0xF0BB3C, true);
			client.boldFont.drawLeftAlignedEffectString(sideMessage, 4, 15, 0xFFFFFF, true);
			client.smallFont.drawCenteredEffectString(client.onDemandRequester.statusString, centerX, centerY + 60, 0xFFFFFF, true);
			sendText(message, centerX, centerY - 60);
		}
		
		private void sendText(String text, int x, int y) {
			if(client.fancyFont == null)
				return;
			client.fancyFont.drawCenteredEffectString(text, x, y, 0xDBB047, true);
			component.drawGraphics(0, 0, client.graphics);
		}
	}
}

