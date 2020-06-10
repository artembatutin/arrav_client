package net.arrav.cache;

import net.arrav.Constants;
import net.arrav.graphic.img.BitmapImage;
import net.arrav.graphic.img.PaletteImage;
import net.arrav.Client;
import net.arrav.Config;
import net.arrav.net.Updater;
import net.arrav.activity.Activity;
import net.arrav.activity.TitleActivity;
import net.arrav.cache.impl.*;
import net.arrav.graphic.GraphicalComponent;
import net.arrav.graphic.Rasterizer2D;
import net.arrav.graphic.font.BitmapFont;
import net.arrav.net.SignLink;
import net.arrav.util.ThreadUtils;
import net.arrav.util.io.Buffer;
import net.arrav.util.string.ColorConstants;

import java.awt.*;
import java.awt.image.Raster;
import java.io.*;
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
	 * Client access instance.
	 */
	private final Client client;

	/**
	 * Loading progress information.
	 */
	private volatile String message = "";
	private volatile String sideMessage;
	public static int progress;
	private static int lastProgress = 0;
	private int LP;

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
		progress = 5;
		load(new ProtocolLoader(getCacheArchive(5, "versionlist", CacheUnpacker.EXPECTED_CRC[5])));
		load(new MediaLoader(getCacheArchive(4, "media", CacheUnpacker.EXPECTED_CRC[4])));
		progress = 15;
		load(new ConfigurationLoader(getCacheArchive(2, "config", CacheUnpacker.EXPECTED_CRC[2])));
		load(new InterfaceLoader(getCacheArchive(3, "interface", CacheUnpacker.EXPECTED_CRC[3])));
		progress = 80;
		load(new SceneLoader());
		progress = 100;
		finished = true;
		client.titleActivity.initialize();
		TitleActivity.scrollOpened = true;
		TitleActivity.scrollValue = 110;
		// Resetting when finished
		message = null;
		System.gc();
	}
	
	private void load(CacheLoader loader) {
		message = loader.message();
		loader.run(client);
	}

	/**
	 * Main Initializer usage: Unpacks the start up archive. And all important startup files.
	 */
	private void init() {
		if(SignLink.sunJava) {
			client.minDelay = 5;
		}
		for(int i = 0; i < Constants.CACHE_INDEX_COUNT; i++) {
			client.cacheIdx[i] = SignLink.getIndex(client, i);
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
		Client.spriteCache.init(new File(SignLink.getCacheDir()+"main_file_sprites.dat"), new File(SignLink.getCacheDir()+"main_file_sprites.idx"));
		Client.modelVault.init(new File(SignLink.getCacheDir()+"models.dat"), new File(SignLink.getCacheDir()+"models.idx"));
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
		try {
			if(client.cacheIdx[0] != null) {
				buffer = client.cacheIdx[0].readFile(index);
			}
		} catch(Exception _ex) {
			_ex.printStackTrace();
		}
		if(buffer != null) {
			if(Constants.JAGGRAB_ENABLED) {
				client.getCrc().reset();
				client.getCrc().update(buffer);
				int crcValue = (int) client.getCrc().getValue();
				if(crcValue != crc) {
					buffer = null;
				}
			}
		}
		if(buffer != null) {
			return new CacheArchive(index, fileName, buffer);
		}

		while(buffer == null) {
			sideMessage = "Requesting " + fileName;
			try(DataInputStream in = openJagGrabInputStream(fileName)) {
				int size = in.readInt();
				System.out.println("size found: " + size + " - " + fileName + " tru: " + in.available());
				if(size <= 0) {
					buffer = null;
				} else {
					buffer = new byte[size];
					in.readFully(buffer, 0, size);
					in.close();
				}
				if(buffer != null) {
					if(Constants.JAGGRAB_ENABLED) {
						client.getCrc().reset();
						client.getCrc().update(buffer);
						int crcValue = (int) client.getCrc().getValue();
						if(crcValue != crc) {
							buffer = null;
						}
					}
				}
				
				try {
					if(client.cacheIdx[0] != null) {
						client.cacheIdx[0].writeFile(buffer.length, buffer, index);
					}
				} catch(Exception _ex) {
					_ex.printStackTrace();
					client.cacheIdx[0] = null;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				buffer = null;
			}
			if(buffer == null) {
				//retry.
				sideMessage = "Retrying to load " + fileName + ".";
				System.out.println("he");
				return getCacheArchive(index, fileName, crc);
			}
		}
		return new CacheArchive(index, fileName, buffer);
	}

	private void compareCrcValues() {
		int secondsToWait = 5;
		EXPECTED_CRC[8] = 0;
		int checksumCount = 0;

		while(EXPECTED_CRC[8] == 0) {
			message = "Connecting to the web server...";

			try {
				DataInputStream in = openJagGrabInputStream("crc");
				Buffer buffer = new Buffer(new byte[44]);
				in.readFully(buffer.data, 0, 44);
				in.close();
				buffer.getInt();
				for(int index = 0; index < 9; index++) {
					EXPECTED_CRC[index] = buffer.getInt();
				}

				int checksumValue = buffer.getInt();
				int expectedValue = 1234;
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
		if(!Constants.JAGGRAB_ENABLED) {
			return new DataInputStream(new ByteArrayInputStream(file.getBytes()));
		}
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
		outputstream.write((file).getBytes());
		return new DataInputStream(inputstream);
	}

	/**
	 * Finalize stage.
	 */
	public void reset() {
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
				if(lastProgress != progress) {
					for (int i = lastProgress; i < progress; i++) {
						updateLoading(i, x, y);
					}
				} else {
					updateLoading(progress, x, y);
				}

				lastProgress = progress;
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
		private synchronized void updateLoading(int completion, int centerX, int centerY) {
			if(client.onDemandRequester == null)
				return;
			if(component == null || component.getWidth() != client.windowWidth || component.getHeight() != client.windowHeight) {
				component = new GraphicalComponent(client.windowWidth, client.windowHeight);
				component.setCanvas();
			} else {
				component.setCanvas();
				Rasterizer2D.clearCanvas();
			}
			Rasterizer2D.fillRectangle(0, 0, client.windowWidth, client.windowHeight, 0x070505);
			BitmapImage bg = Client.spriteCache.get(859);
			BitmapImage icon = Client.spriteCache.get(2059);
			bg.drawImage(centerX - (bg.imageWidth / 2), centerY - (bg.imageHeight / 2));
			icon.drawImage(centerX - (icon.imageWidth / 2), centerY - (bg.imageHeight / 2));

			int percentage = completion;

			if (percentage > 100)
				percentage = 100;

			if (percentage >= 0 && percentage <= 19) {
				fadingToColor = 0xD4222E;
			} else if (percentage >= 20 && percentage <= 39) {
				fadingToColor = 0xF5793B;
			} else if (percentage >= 59 && percentage <= 79) {
				fadingToColor = 0x53D462;
			} else {
				fadingToColor = 0x0FD426;
			}

			if (!switchColor) {
				if (percentageColor != fadingToColor)
					switchColor = true;
			}

			if (switchColor) {
				steps++;
				if (steps >= 100) {
					steps = 1;
					switchColor = false;
					percentageColor = fadingToColor;
				} else {
					percentageColor = fadeColors(new Color(percentageColor), new Color(fadingToColor), steps);
				}
			}

			int loadingWidth = 490;
			int loadingHeight = 40;
			int loadingX = centerX - (loadingWidth /  2);
			int loadingY = centerY - (loadingHeight /  2);


			Rasterizer2D.drawOutlinedRoundedGradientRectangle(loadingX, loadingY, loadingWidth, loadingHeight, 0x0f0f0e, 0x191919, 0x191919, 255);
			Rasterizer2D.drawRoundedGlowBorder(loadingX, loadingY, loadingWidth, loadingHeight, ColorConstants.BLACK, 40, 7, true);

			int fillWidth = (loadingWidth * (completion)) / 100;
			Rasterizer2D.fillRoundedGradientRectangle(loadingX + 5, loadingY + 5, fillWidth, loadingHeight - 10, percentageColor, ColorConstants.lighten(percentageColor), 100, true, false);

			client.fancyFont.drawCenteredEffectString((completion) + "%", centerX, centerY - 10, 0xF0BB3C, 0);
			client.boldFont.drawLeftAlignedEffectString(sideMessage, 4, 15, 0xFFFFFF, 0);
			client.smallFont.drawCenteredEffectString(client.onDemandRequester.statusString, centerX, centerY + 60, 0xFFFFFF, 0);

			sendText(message, centerX, centerY - 60);
		}
		
		private void sendText(String text, int x, int y) {
			if(client.fancyFont == null)
				return;
			client.fancyFont.drawCenteredEffectString(text, x, y, 0xDBB047, 0);
			component.drawGraphics(0, 0, client.graphics);
		}
	}

	public static int percentageColor;
	public static int steps = 1;
	public static int fadingToColor;
	public static boolean switchColor = false;

	public static int fadeColors(Color color1, Color color2, float step) {
		float ratio = step / 100;
		int r = (int) (color2.getRed() * ratio + color1.getRed() * (1 - ratio));
		int g = (int) (color2.getGreen() * ratio + color1.getGreen() * (1 - ratio));
		int b = (int) (color2.getBlue() * ratio + color1.getBlue() * (1 - ratio));
		return new Color(r, g, b).getRGB();
	}
}

