package net.edge.activity.panel;

import net.edge.Config;
import net.edge.activity.Activity;
import net.edge.cache.unit.ImageCache;
import net.edge.game.Scene;
import net.edge.media.Rasterizer2D;

public abstract class Panel extends Activity {

	public abstract int getId();
	
	public abstract boolean blockedMove();
	
	public boolean on() {
		int x = 8;
		int y = 8;
		if(client.uiRenderer.isResizableOrFull()) {
			x = client.windowWidth / 2 - 380;
			y = client.windowHeight / 2 - 250;
		}
		return client.mouseInRegion(x, y, x + 500, y + 336);
	}
	
	protected boolean processClose(int beginX, int beginY) {
		if(Config.def.panelStyle == 2) {
			if(client.leftClickInRegion(beginX + 442, beginY + 12, beginX + 498, beginY + 42)) {
				client.panelHandler.close();
				client.outBuffer.putOpcode(185);
				client.outBuffer.putShort(123);
				Scene.hoverX = -1;
				return true;
			}
		} else {
			if(client.leftClickInRegion(beginX + 467, beginY + 16, beginX + 488, beginY + 37)) {
				client.panelHandler.close();
				client.outBuffer.putOpcode(185);
				client.outBuffer.putShort(123);
				Scene.hoverX = -1;
				return true;
			}
		}
		return false;
	}
	
	protected void drawClose(int beginX, int beginY) {
		if(Config.def.panelStyle == 2) {
			fancyFont.drawCenteredString("Exit", beginX + 467, beginY + 27, 0xF3B13F);
			Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 20, 2, 0xF3B13F, 60);
			if(client.mouseInRegion(beginX + 442, beginY + 12, beginX + 498, beginY + 42)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 20, 2, 0xF3B13F, 20);
			}
		} else {
			ImageCache.get(1998).drawImage(beginX + 467, beginY + 16);
			if(client.mouseInRegion(beginX + 467, beginY + 16, beginX + 488, beginY + 37)) {
				ImageCache.get(1999).drawImage(beginX + 467, beginY + 16);
			}
		}
	}
	
	protected void drawMain(int x, int y, int width, int height, int color1, int color2, int alpha) {
		if(Config.def.panelStyle == 2) {
			Rasterizer2D.fillRectangle(x, y, width, height, color1, alpha);
			Rasterizer2D.drawRectangle(x, y, width, height, color2);
		} else if(Config.def.panelStyle == 0) {
			ImageCache.get(1996).drawImage(x, y);
		} else if(Config.def.panelStyle == 1) {
			ImageCache.get(1997).drawImage(x, y);
		}
	}
	
	protected void drawOver(int beginX, int beginY) {
		if(Config.def.panelStyle == 2) {
			Rasterizer2D.drawRectangle(beginX + 4, beginY + 39, 490, 292, Config.def.panelStyle == 2 ? 0xffffff : 0x000000, 80);
			Rasterizer2D.fillRectangle(beginX + 5, beginY + 40, 488, 290, Config.def.panelStyle == 2 ? 0xffffff : 0x000000, 60);
		} else {
			Rasterizer2D.fillRectangle(beginX + 6, beginY + 13, 489, 28, 0x000000, 60);
			Rasterizer2D.drawHorizontalLine(beginX + 6, beginY + 39, 488, 0x000000);
		}
	}
	
	protected void drawSection(int x, int y, int height, int width, String name) {
		if(Config.def.panelStyle == 2)
			Rasterizer2D.fillRectangle(x - 5, y - 5, width + 10, height + 5, 0xffffff, 30);
		Rasterizer2D.fillRectangle(x - 2, y - 2, width + 4, height, 0x000000, 90);
		int color = Config.def.panelStyle == 2 ? Config.def.panelStyle == 1 ? 0xb3afa8 : 0xDBB047 : 0x7f7365;
		Rasterizer2D.fillRectangle(x, y, width, 23, color, 90);
		if(Config.def.panelStyle != 2) {
			Rasterizer2D.drawRectangle(x - 2, y - 2, width + 4, height, 0x000000);
		}
		fancyFont.drawCenteredEffectString(name, x + width / 2, y + 17, 0xFFFFFF, true);
	}
	
	protected void drawTitleButton(String text, int x, int y, int color) {
		if(Config.def.panelStyle == 0) {
			color -= 200;
		}
		Rasterizer2D.fillRectangle(x, y, smallFont.getStringWidth(text) + 10, 15, color, 100);
		if(client.mouseInRegion(x + 1, y + 3, x + smallFont.getStringWidth(text) + 10, y + 18)) {
			Rasterizer2D.fillRectangle(x, y, smallFont.getStringWidth(text) + 10, 15, color, 100);
		}
		smallFont.drawLeftAlignedEffectString(text, x + 5, y + 13, 0xFFFFFF, true);
	}

}
