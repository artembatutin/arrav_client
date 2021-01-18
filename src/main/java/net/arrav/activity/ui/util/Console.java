package net.arrav.activity.ui.util;


import net.arrav.Client;
import net.arrav.Config;
import net.arrav.cache.CacheUnpacker;
import net.arrav.cache.impl.InterfaceLoader;
import net.arrav.cache.unit.UnderlayFloorType;
import net.arrav.graphic.Rasterizer2D;
import net.arrav.util.DateTimeUtil;
import net.arrav.util.string.StringUtils;

import java.util.Arrays;

/**
 * Handles the developer terminal console.
 *
 * @author Daniel
 * @author Zion
 */
public class Console {

	/**
	 * The index of the last command that was executed.
	 */
	private int commandIndex;

	/**
	 * The console being opened flag.
	 */
	public boolean openConsole;

	/**
	 * The renderable time stamp flag.
	 */
	public boolean displayTime;

	/**
	 * The console input string.
	 */
	public String consoleInput;

	/**
	 * The console messages.
	 */
	public final String[] terminal;

	/**
	 * The terminal command prefix.
	 */
	private String TERMINAL_COMMAND_PREFIX = (displayTime ? "[" + DateTimeUtil.getTime() + "] " : "") + "-> <col=0xffffff>";

	/**
	 * Constructs a new developer terminal console.
	 */
	public Console() {
		this.openConsole = false;
		this.displayTime = false;
		this.terminal = new String[17];
		clear();
	}

	/**
	 * Handles the clearing of the developer terminal console.
	 */
	public final void clear() {
		commandIndex = 0;
		consoleInput = "";
		for (int index = 0; index < terminal.length; index++) {
			terminal[index] = null;
		}
		terminal[4] = "Welcome to the developer terminal.";
		terminal[3] = " - Type \"clear\" to clear the developer terminal.";
		terminal[2] = " - Press \"page up\" or \"page down\" to navigate through the terminal commands.";
		terminal[1] = " - Type \"time\" to renderable the time stamp of each executed command.";
	}

	/**
	 * Draws the developer terminal console.
	 *
	 * @param width The width of the console.
	 */
	public void drawConsole(int width) {
		if (openConsole) {
			Rasterizer2D.fillRectangle(0, 0, width, 334, 0x406AA1, 185);
			Rasterizer2D.drawPixels(1, 334 - 21, 0, 0xffffff, width);
			String title = StringUtils.formatName(Client.instance.localUsername) + "@Paragon:";
			Client.instance.boldFont.drawLeftAlignedEffectString(title + " ", 5, 334 - 6, 0xACB0B5, 0);
			Client.instance.boldFont.drawLeftAlignedEffectString(
					"<col=0xffffff>" + consoleInput + (Client.instance.loopCycle % 20 < 10 ? "|" : ""),
					Client.instance.boldFont.getEffectStringWidth(title) + 8, 334 - 5, 0xffffff, 0);
			for (int index = 0, messageY = 308; index < 17; index++, messageY -= 18) {
				String msg = terminal[index];
				if (msg != null && msg.length() > 0) {
					Client.instance.plainFont.drawLeftAlignedEffectString(msg, 5, messageY, 0xACB0B5, 0);
				}
			}
		}
	}

	/**
	 * Handles the console commands.
	 *
	 * @param consoleCommand The console command being executed.
	 */
	public void sendConsoleCommands(String consoleCommand) {

		switch (consoleCommand){

			case "clear":
			case "cls":
				clear();
				return;

			case "time":
				displayTime = !displayTime;
				return;

			case "top":
				sendConsoleMessage("top:"+Client.instance.alwaysOntop());
				return;

			case "roll":
				Client.instance.rollCharacterInInterface = !Client.instance.rollCharacterInInterface;
				return;

			case "fog":
				Config.def.fog(!Config.def.fog());
				sendConsoleMessage("--> fog " + (Config.def.fog() ? "on" : "off"));
				return;

			case "tween":
				Config.def.tween(!Config.def.tween());
				sendConsoleMessage("--> tween " + (Config.def.tween() ? "on" : "off"));
				return;

			case "mat":
				Config.def.groundMat(!Config.def.groundMat());
				sendConsoleMessage("--> mat " + (Config.def.groundMat() ? "on" : "off"));
				return;

			case "hitmark":
				try {
					final String[] args = Client.instance.chatInput.split(" ");
					Config.def.hitsplat(Integer.parseInt(args[1]));
				} catch(final Exception e) {
					sendConsoleMessage("Interface Failed to load");
				}
				return;
			case "reloadi":
			case "reint":
				new InterfaceLoader(Client.instance.cacheHandler.getCacheArchive(3, "interface", CacheUnpacker.EXPECTED_CRC[3])).run(Client.instance);
				return;

			case "task":
				Client.instance.taskHandler.completeTask("Dragon Slayer\nKill 100 dragons.");
				return;

			case "task2":
				Client.instance.taskHandler.completeTask("Dragon Slayer\nKill 100 dragons.");
				Client.instance.taskHandler.completeTask("Another task!\nUnknown.\n\nSpecial information\nhere.");
				return;

			case "data":
				Config.def.data(!Config.def.data());
				sendConsoleMessage("--> data debug " + (Config.def.data() ? "on" : "off"));
				return;

			case "debug":
				Config.def.idx(!Config.def.idx());
				sendConsoleMessage("--> index debug " + (Config.def.idx() ? "on" : "off"));
				return;

			case"texture":
				try {
					String[] args = consoleCommand.split(" ");
					Arrays.fill(UnderlayFloorType.FLOOR_TEXTURE, (Integer.parseInt(args[1])));
					for(int i23 = 0; i23 < UnderlayFloorType.cache.length; i23++) {
						if (UnderlayFloorType.FLOOR_TEXTURE[i23] != -1) {
							UnderlayFloorType.cache[i23].texture = UnderlayFloorType.FLOOR_TEXTURE[i23];
						}
					}
				} catch(final Exception e) {
					sendConsoleMessage("Error changin texture");
				}
				return;

			case "sprite":
			case "sprites":
				Config.def.setSprite(!Config.def.sprite());
				sendConsoleMessage("--> sprite debug " + (Config.def.sprite() ? "on" : "off"));
				return;

			case "noclip":
				Client.instance.noclip = !Client.instance.noclip;
				sendConsoleMessage("--> noclip " + (Client.instance.noclip ? "on" : "off"));
				return;

			case "logout":
				Client.instance.logOut();;
				return;

			case "relog":
				Client.instance.logOut();
				for(int i = 0; i < 5; i++) {
					Client.instance.connect(Client.instance.localUsername, Client.instance.localPassword);
					if(Client.instance.loggedIn) {
						break;
					}
					try {
						Thread.sleep(1000L);
					} catch(InterruptedException e) {
					}
				}
				return;

			case "newmaps":
				for(int f : Client.instance.terrainDataIds) {
					Client.instance.onDemandRequester.setNew(f);
				}
				for(int o : Client.instance.objectDataIds) {
					Client.instance.onDemandRequester.setNew(o);
				}
				Client.instance.onDemandRequester.packMapIndex();
				Client.instance.loadRegion();
				Client.instance.pushMessage("set new", 0, "client");
				return;

			case "oldmaps":
				for(int f : Client.instance.terrainDataIds) {
					Client.instance.onDemandRequester.setOld(f);
				}
				for(int o : Client.instance.objectDataIds) {
					Client.instance.onDemandRequester.setOld(o);
				}
				Client.instance.onDemandRequester.packMapIndex();
				Client.instance.loadRegion();
				Client.instance.pushMessage("set old", 0, "client");
				return;

			case "fps":
				Config.def.fps(!Config.def.fps());
				sendConsoleMessage("--> fps " + (Config.def.fps() ? "on" : "off"));
				return;

			case "gc":
				System.gc();
				final Runtime runtime = Runtime.getRuntime();
				final int mem = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
				sendConsoleMessage("--> mem: " + mem + "k");
				return;
		}


		Client.instance.outBuffer.putOpcode(103);
		Client.instance.outBuffer.putByte(consoleCommand.length() + 1);
		Client.instance.outBuffer.putLine(consoleCommand);
		commandIndex = -1;
	}

	/**
	 * Handles choosing the developer command.
	 *
	 * @param previous The previous flag.
	 */
	public void chooseCommand(boolean previous) {
		String next = null;

		if (previous) {
			do {
				if (commandIndex + 1 < terminal.length) {
					next = terminal[++commandIndex];
					if (next == null) {
						commandIndex--;
						break;
					}
				} else
					break;
			} while (!next.startsWith(TERMINAL_COMMAND_PREFIX));
		} else {
			do {
				if (commandIndex - 1 >= 0) {
					next = terminal[--commandIndex];
					if (next == null) {
						commandIndex++;
						break;
					}
				} else
					break;
			} while (!next.startsWith(TERMINAL_COMMAND_PREFIX));
		}

		if (next != null) {
			consoleInput = next.substring(TERMINAL_COMMAND_PREFIX.length());
		}
	}

	/**
	 * Sends a console message.
	 *
	 * @param message The message to send.
	 */
	public void sendConsoleMessage(String message) {
		//if (Client.instance.backDialogueId == -1)
		//	Client.redrawDialogueBox = true;
		System.arraycopy(terminal, 0, terminal, 1, 16);
		message = TERMINAL_COMMAND_PREFIX + message;
		terminal[0] = message;
	}
}
