package net.arrav.activity;

import net.arrav.Constants;
import net.arrav.Client;
import net.arrav.Config;
import net.arrav.activity.panel.impl.SettingPanel;
import net.arrav.graphic.GraphicalComponent;
import net.arrav.graphic.Rasterizer2D;
import net.arrav.graphic.img.BitmapImage;
import net.arrav.util.string.StringUtils;

import java.util.concurrent.ThreadLocalRandom;


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
			new Connection("Main", 3227739482L, 43594),
			new Connection("Dev", 2130706433L, 43594)
	};

	/**
	 * The connection index selected.
	 */
	public static int connection = 1;

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
	 * The background clouds
	 */
	private Cloud[] clouds = new Cloud[10];

	/**
	 * the y offset for components
	 */
	private int yOffset = 200;


	/**
	 * The formatted username.
	 */
	private String formatted = "";

	/**
	 * Astericks password entry.
	 */
	private String astericks = "";

	/**
	 * Setting panel instance if enabled.
	 */
	public SettingPanel settings = null;

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
		} else if(client.leftClickInRegion(client.windowWidth - 100, client.windowHeight - 20, client.windowWidth, client.windowHeight)) { // ip
			int con = connection + 1;
			if(con >= CONNECTIONS.length)
				con = 0;
			connection = con;
		} else if(client.leftClickInRegion(client.windowWidth - 22, 3, client.windowWidth - 3, 20)) {
			client.exit();
		} else if(client.leftClickInRegion(client.windowWidth - 40, 3, client.windowWidth - 21, 20)) {
			if(settings == null) {
				settings = new SettingPanel();
				scrollOpened = true;
				scrollValue = 110;
				started = true;
			} else {
				settings = null;
			}
		}

		if(settings == null) {
			/* Clicking */
			if(client.leftClickInRegion(centerX - (client.spriteCache.get(860).imageWidth / 2), centerY + 20 ,//user
					centerX - (client.spriteCache.get(860).imageWidth / 2) + 292,centerY + 20 + 44)) { // User
				selectedInputForm = 2;
			} else if(client.leftClickInRegion(centerX - (client.spriteCache.get(860).imageWidth / 2), centerY - 36 ,//pass
					centerX - (client.spriteCache.get(860).imageWidth / 2) + 292,centerY - 36 + 44)) { // Password
				selectedInputForm = 1;
			} else if(client.leftClickInRegion(centerX - 3, centerY + 70, centerX - 3+218, centerY + 70+71)) { // Login
				client.loginFailures = 0;
				if(client.loggedIn) {
					return true;
				}
				client.connect(client.localUsername, client.localPassword);

			} else if(client.leftClickInRegion(centerX - 55, centerY + 87 - scrollValue + yOffset, centerX - 5, centerY + 105 - scrollValue + yOffset)) {
				Config.def.clouds = !Config.def.clouds;
			} else if(client.leftClickInRegion(centerX + 5, centerY + 87 - scrollValue +yOffset, centerX + 50, centerY + 105 - scrollValue + yOffset)) {
				Config.def.oldModels = !Config.def.oldModels;
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
						formatted = StringUtils.formatName(client.localUsername);
					}
					if(key == 9 || key == 10 || key == 13) {
						selectedInputForm = 2;
					} else if(valid) {
						client.localUsername += (char) key;
						if(client.localUsername.length() > 13) {
							client.localUsername = client.localUsername.substring(0, 15);
						}
						formatted = StringUtils.formatName(client.localUsername);
					}
				} else if(selectedInputForm == 2) {
					if(key == 8 && client.localPassword.length() > 0) {
						client.localPassword = client.localPassword.substring(0, client.localPassword.length() - 1);
						astericks = StringUtils.toAsterisks(client.localPassword);
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
						astericks = StringUtils.toAsterisks(client.localPassword);
					}
				}
			} while(true);
		} else {
			settings.process();
		}
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
		Rasterizer2D.fillRectangle(0, 0, client.windowWidth, client.windowHeight, 0x392822);
		BitmapImage bg = client.spriteCache.get(859);
		bg.drawAlphaImage(centerX - (bg.imageWidth / 2),centerY - (bg.imageHeight / 2));//background

		//clouds drawing
		if(Config.def.clouds) {
			for(int i = 0; i < clouds.length; i++) {
				if(clouds[i] == null)
					clouds[i] = new Cloud();
				clouds[i].draw();
				if(clouds[i].dead) {
					clouds[i] = null;
				}
			}
		}
		client.spriteCache.get(2059).drawImage(centerX - (client.spriteCache.get(2059).imageWidth / 2), centerY - 260);//logo

		if(settings == null) {

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

			BitmapImage loginbox = Client.spriteCache.get(2061);
			loginbox.drawAlphaImage(centerX - (loginbox.imageWidth / 2),centerY - (loginbox.imageHeight / 2) + 30);//login box
			fadeValue = 80;
			client.spriteCache.get(860).drawImage(centerX - (client.spriteCache.get(860).imageWidth / 2), centerY - 36, fadeValue * 2 + alphaOpacity[0]);//pass
			client.spriteCache.get(860).drawImage(centerX - (client.spriteCache.get(860).imageWidth / 2), centerY + 20, fadeValue * 2 + alphaOpacity[1]);//user


			client.spriteCache.get(0).drawImage(centerX - 3, centerY + 70, fadeValue * 2 + alphaOpacity[2]);//login

			Rasterizer2D.fillRoundedRectangle(centerX - 55, centerY + 87 - scrollValue + yOffset, 50, 18, 3, 0x000000, 100);

			if(Config.def.clouds || client.mouseInRegion(centerX - 55, centerY + 87 - scrollValue + yOffset, centerX - 5, centerY + 105 - scrollValue + yOffset))
				Rasterizer2D.fillRoundedRectangle(centerX - 55, centerY + 87 - scrollValue + yOffset, 50, 18, 3, 0x000000, 40);

			smallFont.drawCenteredString("Clouds", centerX - 30, centerY + 101 - scrollValue + yOffset, 0xffffff);

			Rasterizer2D.fillRoundedRectangle(centerX + 5, centerY + 87 - scrollValue + yOffset, 50, 18, 3, 0x000000, 100);

			if(client.mouseInRegion(centerX + 5, centerY + 87 - scrollValue + yOffset, centerX + 50, centerY + 105 - scrollValue + yOffset))
				Rasterizer2D.fillRoundedRectangle(centerX + 5, centerY + 87 - scrollValue + yOffset, 50, 18, 3, 0x000000, 40);

			smallFont.drawCenteredString(Config.def.oldModels ? "OSRS" : "HD", centerX + 30, centerY + 101- scrollValue + yOffset, 0xffffff);

			/* Text */
			boldFont.drawLeftAlignedEffectString(formatted + (selectedInputForm == 1 & client.loopCycle % 40 < 20 ? "|" : ""), centerX - (client.spriteCache.get(860).imageWidth / 2) + 5, centerY - 13, 0xFFFFFF, 0);
			boldFont.drawLeftAlignedEffectString(astericks + (selectedInputForm == 2 & client.loopCycle % 40 < 20 ? "|" : ""), centerX - (client.spriteCache.get(860).imageWidth / 2) + 5, centerY + 20 + 25, 0xFFFFFF, 0);
		} else {
			settings.update();
		}

		/* Error & offline warning // effect */
		Rasterizer2D.fillRectangle(0, 0, client.windowWidth, client.windowHeight, 0x000000, alphaOpacity[3]);
		if(client.titleMessage.length() > 0) {
			if(scrollOpened) {
				alphaOpacity[3] += (alphaOpacity[3] > 220 ? 0 : 8);
				String[] msgs = client.titleMessage.split("\n");
				int y = (client.windowHeight >> 1) - 7 * (msgs.length >> 1);
				for(String msg : msgs) {
					boldFont.drawCenteredEffectString(msg, centerX, y += 15, 0xFF8A1F, 0);
					smallFont.drawLeftAlignedString("Click anywhere on the screen to remove this error message.", 10, client.windowHeight - 10, 0xFFFFFF);
				}
			}
		} else {
			alphaOpacity[3] -= (alphaOpacity[3] > 5 ? 8 : 0);
		}

		Rasterizer2D.drawRectangle(client.windowWidth - 59, 2, 56, 16, 0x000000);
		client.spriteCache.get(client.mouseInRegion(client.windowWidth - 20, 3, client.windowWidth - 3, 20) ? 2042 : 2037).drawImage(client.windowWidth - 22, 3);
		client.spriteCache.get(client.mouseInRegion(client.windowWidth - 40, 3, client.windowWidth - 21, 20) ? 2041 : 2036).drawImage(client.windowWidth - 40, 3);
		client.spriteCache.get(2035).drawImage(client.windowWidth - 58, 3);

		/* Debugging information */
		if(Config.def.data()) {
			final Runtime runtime = Runtime.getRuntime();
			final int usedMemory = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
			plainFont.drawLeftAlignedEffectString("mouse: " + client.mouseX + "," + client.mouseY, 5, 15, 0xffff00, -1);
			plainFont.drawLeftAlignedEffectString("dMouse: " + -(centerX - client.mouseX) + "," + -(centerY - client.mouseY), 5, 30, 0xffff00, -1);
			plainFont.drawLeftAlignedEffectString("memory: " + usedMemory + "k (" + usedMemory / 1024L + "M)", 5, 45, 0xffff00, -1);
			plainFont.drawLeftAlignedEffectString("fps: " + client.fps, 5, 60, 0xffff00, -1);
		}

		smallFont.drawRightAlignedString("Build: " + Constants.BUILD + " - " + longToIp(CONNECTIONS[connection].getIp())+" "+CONNECTIONS[connection].getPort(), client.windowWidth - 120, client.windowHeight - 10, 0xffffff);
		titleGraphics.drawGraphics(0, 0, client.graphics);
	}

	private String longToIp(long ip) {
		return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + (ip & 0xFF);
	}

	/**
	 * Initializing all title activity data.
	 */
	@Override
	public void initialize() {
		started = false;
		titleGraphics = new GraphicalComponent(client.windowWidth, client.windowHeight);
		if(Client.firstRun) {
			settings = new SettingPanel();
			client.titleMessage = "Welcome to Fantasy, choose your preferences.";
			Client.firstRun = false;
		}
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
		if(client.frame == null) {
			if(client.windowWidth != client.getWidth() || client.windowHeight != client.getHeight()) {
				client.windowWidth = client.getWidth();
				client.windowHeight = client.getHeight();
				titleGraphics = new GraphicalComponent(client.windowWidth, client.windowHeight);
			}
		} else if (client.windowWidth != client.frame.getContentWidth() || client.windowHeight != client.frame.getContentHeight()) {
			client.windowWidth = client.frame.getContentWidth();
			client.windowHeight = client.frame.getContentHeight();
			titleGraphics = new GraphicalComponent(client.windowWidth, client.windowHeight);
		}
	}

	/**
	 * Processing all hover spots.
	 * @param x hover x position
	 * @param y hover y position
	 */
	private void processHover(int x, int y) {
		if(scrollOpened) {
			hovered[0] = selectedInputForm == 2 || client.mouseInRegion(x - (client.spriteCache.get(860).imageWidth / 2), y - 30 ,//pass
					x - (client.spriteCache.get(860).imageWidth / 2) + 292,y - 30 + 44);//pass
			hovered[1] = selectedInputForm == 1 || client.mouseInRegion(x - (client.spriteCache.get(860).imageWidth / 2), y + 20 ,//user
					x - (client.spriteCache.get(860).imageWidth / 2) + 292,y + 20 + 44);//user
			hovered[2] = client.mouseInRegion(x - 110, y + 75, x - 110+218, y + 75+71);
		} else {
			hovered[0] = false;
			hovered[1] = false;
			hovered[2] = false;
		}
	}

	public class Cloud {

		int x, y, alpha, life;
		boolean left;
		boolean dead;

		public Cloud() {
			left = ThreadLocalRandom.current().nextBoolean();
			x = ThreadLocalRandom.current().nextInt(0, client.windowWidth / 4);
			if(left)
				x = client.windowWidth - x;
			y = ThreadLocalRandom.current().nextInt(0, client.windowHeight) - 300;
			life = ThreadLocalRandom.current().nextInt(40, 250);
			dead = false;
		}

		public void draw() {
			if(alpha <= 250)
				alpha+=1;
			x += left ? -1 : 1;
			Client.spriteCache.get(2).drawImage(x, y, alpha);
			if(x < 0 || x > client.windowWidth - 100 || life <= 0)
				alpha-=1;
			if(x < -512 || x > client.windowWidth)
				dead = true;
			if(y < -512 || y > client.windowHeight)
				dead = true;
			if(alpha <= 0)
				dead = true;
			life--;
		}
	}

}
