package net.edge.activity;

import net.edge.Constants;
import net.edge.Client;
import net.edge.Config;
import net.edge.cache.unit.ImageCache;
import net.edge.media.GraphicalComponent;
import net.edge.media.Rasterizer2D;
import net.edge.util.string.StringUtils;

public class TitleActivity extends Activity {
	
	public static class Connection {
		
		private final String name;
		private final long ip;
		private final int port;
		
		Connection(String name, long ip, int port) {
			this.name = name;
			this.ip = ip;
			this.port = port;
		}
		
		public String getName() {
			return name;
		}
		
		public long getIp() {
			return ip;
		}
		
		public int getPort() {
			return port;
		}
		
	}
	
	public final static Connection[] CONNECTIONS = {
			new Connection("Main", 757073827L, 43594),
			new Connection("Dev", 2130706433L, 43593)
	};
	
	/**
	 * The connection index selected.
	 */
	private int connection = 0;
	
	/**
	 * Scroll state condition.
	 */
	public static boolean scrollOpened;
	/**
	 * Started up flag.
	 */
	private boolean started;
	/**
	 * Scroll value.
	 */
	public static short scrollValue;
	/**
	 * Main graphical component of the title screen.
	 */
	private GraphicalComponent titleGraphics;
	/**
	 * Selected input form 1: username 2: password
	 */
	private byte selectedInputForm;
	/**
	 * Title screen's spot fading value.
	 */
	private short fadeValue;
	/**
	 * Alpha opacity for hover positions.
	 */
	private short[] alphaOpacity = new short[4];
	/**
	 * Hover positions toggles.
	 */
	private boolean[] hovered = new boolean[3];

	/**
	 * Main activity Id.
	 * @return activity Id
	 */
	@Override
	public String id() {
		return "title";
	}

	/**
	 * Main title activity data process.
	 */
	@Override
	public boolean process() {
		/* Initialization */
		checkWindow();
		final int centerX = client.uiRenderer.isFixed() ? 765 / 2 : client.windowWidth / 2;
		final int centerY = client.uiRenderer.isFixed() ? 503 / 2 : client.windowHeight / 2;
		/* Dismiss warnings */
		if(client.titleMessage.length() > 0) {
			if(client.leftClickInRegion(0, 0, client.windowWidth, client.windowHeight)) {
				client.titleMessage = "";
			}
		}
		/* Clicking */
		if(client.leftClickInRegion(centerX - 370, centerY - 245, centerX - 320, centerY - 227)) { // ip
			int con = connection + 1;
			if(con >= CONNECTIONS.length)
				con = 0;
			connection = con;
		}else if(client.leftClickInRegion(centerX - 127, centerY - 47, centerX + 126, centerY - 20)) { // User
			selectedInputForm = 1;
		} else if(client.leftClickInRegion(centerX - 127, centerY - 5, centerX + 126, centerY + 22)) { // Password
			selectedInputForm = 2;
		} else if(client.leftClickInRegion(centerX - 58, centerY + 47, centerX + 57, centerY + 80)) { // Login
			client.loginFailures = 0;
			if(client.loggedIn) {
				return true;
			}
			client.connect(client.localUsername, client.localPassword);
		}

		/* Login & keyboard key process */
		do {
			final int key = client.getKey();
			if(key == -1) {
				break;
			}
			boolean valid = false;
			for(int i2 = 0; i2 < Client.validUsernameOrPasswordChar.length(); i2++) {
				if(key != Client.validUsernameOrPasswordChar.charAt(i2)) {
					continue;
				}
				valid = true;
				break;
			}
			if(selectedInputForm == 0) {
				if(key == 9 || key == 10 || key == 13) {
					selectedInputForm = 1;
				}
			} else if(selectedInputForm == 1) {
				if(key == 8 && client.localUsername.length() > 0) {
					client.localUsername = client.localUsername.substring(0, client.localUsername.length() - 1);
				}
				if(key == 9 || key == 10 || key == 13) {
					selectedInputForm = 2;
				} else if(valid) {
					client.localUsername += (char) key;
					if(client.localUsername.length() > 15) {
						client.localUsername = client.localUsername.substring(0, 15);
					}
				}
			} else if(selectedInputForm == 2) {
				if(key == 8 && client.localPassword.length() > 0) {
					client.localPassword = client.localPassword.substring(0, client.localPassword.length() - 1);
				}
				if(key == 9 || key == 10 || key == 13) {
					if(client.titleMessage.length() > 0) {
						client.titleMessage = "";
					} else if(client.localUsername.length() == 0 || client.localPassword.length() == 0) {
						selectedInputForm = 1;
					} else {
						if(client.loggedIn) {
							return true;
						}
						client.connect(client.localUsername, client.localPassword);
					}
				} else if(valid) {
					client.localPassword += (char) key;
					if(client.localPassword.length() > 26) {
						client.localPassword = client.localPassword.substring(0, 26);
					}
				}
			}
		} while(true);
		return false;
	}

	/**
	 * Main title activity graphical updater.
	 */
	@Override
	public void update() {
		/* Initializing */
		if(titleGraphics == null || client.updateAllGraphics) {
			titleGraphics = new GraphicalComponent(client.windowWidth, client.windowHeight);
		}
		titleGraphics.setCanvas();
		final int centerX = client.windowWidth / 2;
		final int centerY = client.windowHeight / 2;
		
		/* Background */
		Rasterizer2D.fillRectangle(0, 0, client.windowWidth, client.windowHeight, 0x070505);
		ImageCache.get(859).drawImage(centerX - 433, centerY - 305);
		ImageCache.get(860).drawImage(centerX, centerY - 305);
		ImageCache.get(861).drawImage(centerX - 433, centerY);
		ImageCache.get(862).drawImage(centerX, centerY);

		/* Scroll */
		if(ImageCache.get(6).imageWidth > 0) {
			if(!started) {
				started = true;
				ImageCache.setHeight(6, 20);
			}
			ImageCache.get(6).drawAlphaImage(centerX - 152, centerY - 120 + scrollValue);
		}
		ImageCache.get(8).drawAlphaImage(centerX - 166, centerY - 133 + scrollValue);
		ImageCache.get(8).drawAlphaImage(centerX - 166, centerY + 107 - scrollValue);
		if(ImageCache.get(6).imageWidth > 0 && scrollOpened && started) {
			if(scrollValue != 0) {
				scrollValue -= 5;
				ImageCache.increaseHeight(6, 10);
			} else {
				if(fadeValue != 80)
					fadeValue += 2;
			}
		} else if(ImageCache.get(6).imageWidth > 0 && started) {
			if(fadeValue != 0) {
				fadeValue -= 2;
			} else if(scrollValue != 110) {
				scrollValue += 5;
				ImageCache.decreaseHeight(6, 10);
			}
		}
		
		/* Hovers */
		processHover(centerX, centerY);
		for(int hover = 0; hover < hovered.length; hover++) {
			if(hovered[hover]) {
				alphaOpacity[hover] += (alphaOpacity[hover] > 90 ? 0 : 3);
			} else {
				alphaOpacity[hover] -= (alphaOpacity[hover] > 0 ? 3 : 0);
			}
		}
		
		/* Main spots */
		if(scrollValue < 10) {
			Rasterizer2D.drawRectangle(centerX - 127, centerY - 5, 254, 28, 0x000000, fadeValue);
			Rasterizer2D.drawRectangle(centerX - 127, centerY - 47, 254, 28, 0x000000, fadeValue);

			Rasterizer2D.fillRectangle(centerX - 127, centerY - 5, 254, 28, 0x000000, fadeValue + alphaOpacity[0]);
			Rasterizer2D.fillRectangle(centerX - 127, centerY - 47, 254, 28, 0x000000, fadeValue + alphaOpacity[1]);
			ImageCache.get(0).drawAlphaImage(centerX - 59, centerY + 45, fadeValue * 2 + alphaOpacity[2]);
		}

		/* Text */
		if(scrollValue < 10) {
			fancyFont.drawLeftAlignedEffectString(StringUtils.formatName(client.localUsername) + (selectedInputForm == 1 & client.loopCycle % 40 < 20 ? "|" : ""), centerX - 125, centerY - 28, 0xFFFFFF, true);
			fancyFont.drawLeftAlignedEffectString(StringUtils.toAsterisks(client.localPassword) + (selectedInputForm == 2 & client.loopCycle % 40 < 20 ? "|" : ""), centerX - 125, centerY + 14, 0xFFFFFF, true);
		}

		/* Error & offline warning // effect */
		Rasterizer2D.fillRectangle(0, 0, client.windowWidth, client.windowHeight, 0x000000, alphaOpacity[3]);
		if(client.titleMessage.length() > 0) {
			if(scrollOpened) {
				alphaOpacity[3] += (alphaOpacity[3] > 220 ? 0 : 8);
				String[] msgs = client.titleMessage.split("\n");
				int y = (client.windowHeight >> 1) - 7 * (msgs.length >> 1);
				for(String msg : msgs) {
					fancyFont.drawCenteredEffectString(msg, centerX, y += 15, 0xF3B13F, true);
					smallFont.drawLeftAlignedString("Click anywhere on the screen to remove this error message.", 10, client.windowHeight - 10, 0xFFFFFF);
				}
			}
		} else {
			alphaOpacity[3] -= (alphaOpacity[3] > 5 ? 8 : 0);
		}
		
		/* Debugging information */
		if(Config.DEBUG_DATA.isOn()) {
			final Runtime runtime = Runtime.getRuntime();
			final int usedMemory = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
			plainFont.drawLeftAlignedEffectString("Mouse: " + client.mouseX + "," + client.mouseY, 5, 15, 0xffff00, false);
			plainFont.drawLeftAlignedEffectString("DMouse: " + -(centerX - client.mouseX) + "," + -(centerY - client.mouseY), 5, 30, 0xffff00, false);
			plainFont.drawLeftAlignedEffectString("Memory: " + usedMemory + "k (" + usedMemory / 1024L + "M)", 5, 45, 0xffff00, false);
			plainFont.drawLeftAlignedEffectString("Fps: " + client.fps, 5, 60, 0xffff00, false);
		}
		Rasterizer2D.fillRoundedRectangle(centerX - 370, centerY - 245, 50, 18, 3, 0x000000, 100);
		smallFont.drawCenteredString(CONNECTIONS[connection].getName(), centerX - 345, centerY - 232, 0xffffff);
		smallFont.drawRightAlignedString("Build: " + Constants.BUILD, client.windowWidth - 20, client.windowHeight - 10, 0xffffff);
		titleGraphics.drawGraphics(0, 0, client.graphics);
	}

	/**
	 * Initializing all title activity data.
	 */
	@Override
	public void initialize() {
		started = false;
		titleGraphics = new GraphicalComponent(client.windowWidth, client.windowHeight);
	}

	/**
	 * Finalizing / resetting all title activity data.
	 */
	@Override
	public void reset() {
		titleGraphics = null;
	}

	/**
	 * Window listener to see whenever the window has changed positions.
	 */
	private void checkWindow() {
		if(client.frame != null) {
			if(client.windowWidth != client.frame.getContentWidth() || client.windowHeight != client.frame.getContentHeight()) {
				client.windowWidth = client.frame.getContentWidth();
				client.windowHeight = client.frame.getContentHeight();
				titleGraphics = new GraphicalComponent(client.windowWidth, client.windowHeight);
			}
		} else {
			if(client.windowWidth != client.getWidth() || client.windowHeight != client.getHeight()) {
				client.windowWidth = client.getWidth();
				client.windowHeight = client.getHeight();
				titleGraphics = new GraphicalComponent(client.windowWidth, client.windowHeight);
			}
		}
	}

	/**
	 * Processing all hover spots.
	 * @param x hover x position
	 * @param y hover y position
	 */
	private void processHover(int x, int y) {
		if(scrollOpened) {
			hovered[0] = selectedInputForm == 2 || client.mouseInRegion(x - 127, y - 5, x + 126, y + 22);
			hovered[1] = selectedInputForm == 1 || client.mouseInRegion(x - 127, y - 47, x + 126, y - 20);
			hovered[2] = client.mouseInRegion(x - 58, y + 47, x + 57, y + 80);
		} else {
			hovered[0] = false;
			hovered[1] = false;
			hovered[2] = false;
		}
	}
	
	public int getConnection() {
		return connection;
	}
}
