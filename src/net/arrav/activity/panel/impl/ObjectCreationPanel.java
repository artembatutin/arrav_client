package net.arrav.activity.panel.impl;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.arrav.activity.panel.Panel;
import net.arrav.cache.unit.ObjectType;
import net.arrav.game.model.Item;
import net.arrav.media.Rasterizer2D;
import net.arrav.media.img.BitmapImage;

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
		if(processClose(beginX, beginY)) {
			return true;
		}
		
		int offset = 50;
		int i = 0;
		for(ConstructionObject obj : objects) {
			int x = i % 2 * 244;
			if(client.leftClickInRegion(beginX + 6 + x, beginY + offset, beginX + 248 + x, beginY + offset + 40)) {
				client.outBuffer.putOpcode(10);
				client.outBuffer.putByte(i + 40);
				return true;
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
		drawMain(beginX, beginY + 8, 500, 328, 0x000000, 0x63625e, 200);
		drawOver(beginX, beginY);
		drawClose(beginX, beginY);
		
		fancyFont.drawLeftAlignedEffectString("Select a piece to build - Level: " + consLevel, beginX + 20, beginY + 33, 0xFF8A1F, true);
		
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
			smallFont.drawLeftAlignedEffectString(tooltip, client.mouseX + (off ? -(smallFont.getStringWidth(tooltip) + 14) : 12), client.mouseY + 9, 0xFF8A1F, true);
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