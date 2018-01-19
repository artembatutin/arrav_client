
package net.arrav.activity.ui.util;

import net.arrav.media.Rasterizer2D;
import net.arrav.Client;

public class TaskHandler {

	private long cycle;
	private final Client client;
	private final String[] messages;
	private String current;

	public TaskHandler(Client client) {
		this.client = client;
		messages = new String[5];
		for(int i = 0; i < messages.length; i++) {
			messages[i] = "";
		}
		cycle = -1;
	}

	public void completeTask(String message) {
		for(int i = 0; i < messages.length; i++) {
			if(messages[i].equals("")) {
				messages[i] = message;
				return;
			}
		}
	}

	private void drawTask(String s) {
		if(cycle < 0) {
			cycle = client.loopCycle;
		}
		if(client.loopCycle - cycle > (s.startsWith("Intro:") ? 800 : 600)) {
			current = null;
			cycle = -1;
			return;
		}
		final int centerX = Rasterizer2D.clipCenterX;
		final int centerY = Rasterizer2D.clipCenterY;
		int progress = 100;
		if(client.loopCycle - cycle < 10) {
			progress = 10 * (int) (client.loopCycle - cycle);
		}
		if(client.loopCycle - cycle > 140) {
			progress = 10 * (150 - (int) (client.loopCycle - cycle));
		}
		final String[] message = s.split("\n");
		final int x = centerX - 125 * progress / 100;
		int y = centerY - 75 + progress / 2 - 50;
		final int width = 2 * (125 * progress / 100);
		int height = 175 - progress;
		final int alpha = 2 * progress;
		if(message.length > 1) {
			y -= (message.length - 1) * 16;
			height += (message.length - 1) * 16;
		}
		Rasterizer2D.fillRectangle(x, y, width, height, 0x494034, alpha);
		Rasterizer2D.drawRectangle(x + 2, y + 2, width - 2, height - 2, 0x2a2416, alpha);
		Rasterizer2D.drawRectangle(x + 1, y + 1, width - 2, height - 2, 0x4f4937, alpha);
		Rasterizer2D.drawRectangle(x, y, width - 2, height - 2, 0x756b58, alpha);
		if(progress == 100) {
			//progress = (int) (client.loopCycle - cycle);
			if(s.startsWith("*")) {
				client.boldFont.drawCenteredEffectString("Arrav Tutorial.", centerX, y + 30, 0xe1981f, true);
			} else
				client.boldFont.drawCenteredEffectString("Achievements completed!", centerX, y + 30, 0xe1981f, true);
			for(int i = 0; i < message.length; i++) {
				if(message[i].length() > 0) {
					client.plainFont.drawCenteredEffectString(message[i], centerX, y + 50 + i * 16, 0xbf751d, true);
				}
			}
		}
	}

	public void processCompletedTasks() {
		if(current == null) {
			for(int i = 0; i < messages.length; i++) {
				if(!messages[i].equals("")) {
					current = messages[i];
					messages[i] = "";
					break;
				}
			}
		}
		if(current != null) {
			drawTask(current);
		}
	}
}
