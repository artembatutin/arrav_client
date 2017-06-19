package net.edge.activity.panel.impl;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.activity.panel.Panel;
import net.edge.cache.unit.ObjectType;
import net.edge.game.Scene;
import net.edge.game.model.Item;
import net.edge.media.Rasterizer2D;
import net.edge.media.img.BitmapImage;
import net.edge.util.string.StringUtils;

public class ObjectCreationPanel extends Panel {
	
	public static class ConstructionObject {
		
		private final Item[] items;
		private final int item;
		private final int level;
		
		public ConstructionObject(int item, int level, Item[] items) {
			this.item = item;
			this.level = level;
			this.items = items;
		}
	}
	
	private static final ObjectList<ConstructionObject> objects = new ObjectArrayList<>();
	
	/**
	 * Construction level
	 */
	private int consLevel = 1;
	
	@Override
	public boolean process() {
	    /* Initialization */
		int beginX = 4;
		int beginY = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			beginX = client.windowWidth / 2 - 380;
			beginY = client.windowHeight / 2 - 250;
		}

        /* Exit */
		if(client.leftClickInRegion(beginX + 442, beginY + 12, beginX + 498, beginY + 42)) {
			client.panelHandler.close();
			client.outBuffer.putOpcode(185);
			client.outBuffer.putShort(123);
			Scene.hoverX = -1;
			return true;
		}
		
		int offset = 50;
		int i = 0;
		for(ConstructionObject obj : objects) {
			int x = i % 2 * 244;
			if(client.leftClickInRegion(beginX + 6 + x, beginY + offset, beginX + 248 + x, beginY + offset + 40)) {
				client.outBuffer.putOpcode(10);
				client.outBuffer.putByte(i + 40);
			}
			offset += i % 2 == 1 ? 41 : 0;
			i++;
		}
		
		return false;
	}
	
	@Override
	public void update() {
		/* Initialization */
		int beginX = 8;
		int beginY = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			beginX = client.windowWidth / 2 - 380;
			beginY = client.windowHeight / 2 - 250;
		}

		/* Main background */
		Rasterizer2D.fillRectangle(beginX, beginY + 8, 500, 328, 0x000000, 200);
		Rasterizer2D.drawRectangle(beginX, beginY + 8, 500, 328, 0x63625e);
		
		fancyFont.drawLeftAlignedEffectString("Select a piece to build - Level: " + consLevel, beginX + 20, beginY + 33, 0xF3B13F, true);
		
		fancyFont.drawCenteredString("Exit", beginX + 467, beginY + 30, 0xF3B13F);
		Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 25, 2, 0xF3B13F, 60);
		if(client.mouseInRegion(beginX + 442, beginY + 12, beginX + 498, beginY + 47)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 25, 2, 0xF3B13F, 20);
		}
		
		/* content */
		Rasterizer2D.drawRectangle(beginX + 4, beginY + 49, 490, 282, 0xffffff, 80);
		Rasterizer2D.fillRectangle(beginX + 5, beginY + 50, 488, 280, 0xffffff, 60);
		int offset = 50;
		int i = 0;
		String tooltip = null;
		for(ConstructionObject obj : objects) {
			int x = i % 2 * 244;
			Rasterizer2D.fillRectangle(beginX + 6 + x, beginY + offset, 242, 40, 0x0000, consLevel >= obj.level ? 100 : 150);
			Rasterizer2D.drawRectangle(beginX + 6 + x, beginY + offset, 242, 40, 0x0000);
			if(consLevel >= obj.level && client.mouseInRegion(beginX + 6 + x, beginY + offset, beginX + 248 + x, beginY + offset + 40)) {
				Rasterizer2D.drawRectangle(beginX + 6 + x, beginY + offset, 242, 40, 0xffffff);
			}
			ObjectType object = ObjectType.get(obj.item);
			BitmapImage img = ObjectType.getIcon(obj.item, 1, 0);
			if(img != null) {
				img.drawImage(beginX + 8 + x, beginY + offset + 2);
			}
			if(object != null)
				smallFont.drawLeftAlignedString(object.name, beginX + 46 + x, beginY + offset + 24, 0xffffff);
			
			int xOff = 0;
			for(Item req : obj.items) {
				object = ObjectType.get(req.getId());
				img = ObjectType.getIcon(req.getId(), req.getAmount(), 0);
				if(img != null) {
					img.drawImage(beginX + 214 + x + xOff, beginY + offset + 2);
					smallFont.drawLeftAlignedString(req.getAmount()+"", beginX + 217 + x + xOff, beginY + offset + 13, 0xffffff);
				}
				if(object != null && client.mouseInRegion(beginX + 214 + x + xOff, beginY + offset + 2, beginX + 244 + x + xOff, beginY + offset + 32)) {
					tooltip = req.getAmount() + " x " + object.name;
				}
				xOff -= 30;
			}
			offset += i % 2 == 1 ? 41 : 0;
			i++;
		}
		if(tooltip != null) {
			boolean off = (client.mouseX - beginX + 8 + smallFont.getStringWidth(tooltip)) > 490;
			Rasterizer2D.fillRoundedRectangle(client.mouseX + (off ? -(smallFont.getStringWidth(tooltip) + 18) : 8), client.mouseY - 3, smallFont.getStringWidth(tooltip) + 7, 15, 3, 0x000000, 200);
			smallFont.drawLeftAlignedEffectString(tooltip, client.mouseX + (off ? -(smallFont.getStringWidth(tooltip) + 14) : 12), client.mouseY + 9, 0xF3B13F, true);
		}
	}
	
	@Override
	public void initialize() {
		consLevel = client.currentStats[22];
	}
	
	@Override
	public void reset() {
	
	}
	
	@Override
	public int getId() {
		return 9;
	}
	
	@Override
	public boolean blockedMove() {
		return false;
	}
	
	public static ObjectList<ConstructionObject> getObjects() {
		return objects;
	}

}