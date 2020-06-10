package net.arrav.activity.panel.impl;

import net.arrav.Client;
import net.arrav.Config;
import net.arrav.activity.panel.Panel;
import net.arrav.cache.unit.Interface;
import net.arrav.cache.unit.ObjectType;
import net.arrav.graphic.Rasterizer2D;
import net.arrav.graphic.img.BitmapImage;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ShopPanel extends Panel {
	
	public enum Currency {
		SLAYER_POINTS(1919),
		COINS(1916),
		TOKKUL(-1),
		CASTLE_WARS_TICKETS(-1),
		AGILITY_ARENA_TICKETS(-1),
		BLOOD_MONEY(1917),
		EDGE_TOKENS(1918),
		VOTE_POINTS(1983),
		PEST_POINTS(-1),
		STAR_DUST(1982);
		
		/**
		 * The image that is represented by this element.
		 */
		private final int image;
		
		/**
		 * Creates a new {@link Currency}.
		 * @param image the currency image that is represented by this element.
		 */
		Currency(int image) {
			this.image = image;
		}
		
		/**
		 * Gets the image that is represented by this element.
		 * @return the image that is represented.
		 */
		public final int getImage() {
			return image;
		}
	}
	
	/**
	 * The currency image.
	 */
	private Currency currency = null;

	/**
	 * Scrolling position.
	 */
	private int scrollPos = 0;

	/**
	 * The max scrolling position.
	 */
	private int scrollMax = 0;

	/**
	 * The condition if the scroll is being dragged.
	 */
	private boolean scrollDrag = false;

	/**
	 * The scrolling dragging position.
	 */
	private int scrollDragPos = 0;

	/**
	 * The number formatter for prices.
	 */
	private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#####.##");
	
	/**
	 * Condition if back button displayed.
	 */
	private final boolean back;

	public ShopPanel(boolean back) {
		this.back = back;
	}
	
	@Override
	public boolean process() {
		if(currency == null && Interface.cache[259].text != null && Interface.cache[259].text.length() > 0) {
			int cur = Integer.parseInt(Interface.cache[259].text);
			if(cur >= 0 && cur < Currency.values().length)
				currency = Currency.values()[cur];
			else
				Interface.cache[259].text = null;
		}
		/* Initialization */
		int beginX = 4;
		int beginY = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			beginX = client.windowWidth / 2 - 380;
			beginY = client.windowHeight / 2 - 250;
		}

		scrollMax = Math.max(55 * ((client.currentShopInterfacePrices.length + 5) / 6) - 285, 0);

		/* Scrolling */
		if(client.mouseInRegion(beginX + 5, beginY + 40, beginX + 493, beginY + 365)) {
			scrollPos += client.mouseWheelAmt * 24;
			if(scrollPos < 0) {
				scrollPos = 0;
			}
			if(scrollPos > scrollMax) {
				scrollPos = scrollMax;
			}
		}
		if(!scrollDrag) {
			int height = 278;
			if(scrollMax > 0) {
				height = 285 * 278 / (scrollMax + 285);
			}
			int pos = 0;
			if(scrollPos != 0) {
				pos = scrollPos * 278 / (scrollMax + 285) + 1;
			}
			int x = 481 + beginX;
			int y = 46 + pos + beginY;
			if(client.mouseDragButton == 1 && client.mouseInRegion(x, y, x + 10, y + height)) {
				scrollDrag = true;
				scrollDragPos = scrollPos;
			}
		} else if(client.mouseDragButton != 1) {
			scrollDrag = false;
		} else {
			int d = (client.mouseY - client.clickY) * (scrollMax + 285) / 278;
			scrollPos = scrollDragPos + d;
			if(scrollPos < 0) {
				scrollPos = 0;
			}
			if(scrollPos > scrollMax) {
				scrollPos = scrollMax;
			}
		}

		/* Exit */
		if(processClose(beginX, beginY))
			return true;
		if(client.localPrivilege == 11) {
			if(client.leftClickInRegion(beginX + 325, beginY + 12, beginX + 381, beginY + 42)) {
				client.outBuffer.putOpcode(185);
				client.outBuffer.putShort(124);
				return true;
			}
		}
		if(back) {
			if(Config.def.panelStyle == 2) {
				if(client.leftClickInRegion(beginX + 382, beginY + 17, beginX + 438, beginY + 47)) {
					client.panelHandler.open(new CounterPanel());
					return true;
				}
			} else {
				if(client.leftClickInRegion(beginX + 382, beginY + 22, beginX + 438, beginY + 42)) {
					client.panelHandler.open(new CounterPanel());
					return true;
				}
			}
		}

		/* Adding item */
		if(client.mouseInRegion(beginX + 5, beginY + 40, beginX + 493, beginY + 330)) {
			int offset = -scrollPos + 45;
			for(int i = 0; i < client.currentShopInterfacePrices.length; i++) {
				int icon = Interface.cache[3900].invId[i];
				if(icon == 0)
					continue;
				int x = i % 6 * 78;
				//if(beginY + offset + 38 > 366) {
				//	break;
				//}
				
				if(client.rightClickInRegion(beginX + 8 + x, beginY + offset, beginX + 80 + x, beginY + offset + 50)) {
					client.menuOpened = false;
					String name = ObjectType.get(icon).name;
					String[] actions = {"C", "200", "50", "10", "5", "1", "0"};
					client.menuPos = 0;
					for(int p = 0; p < actions.length; p++) {
						if(actions[p].equals("C")) {
							client.menuItemName[p] = "Cancel";
							client.menuItemCode[p] = 1107;
							client.menuPos++;
						} else {
							boolean value = actions[p].equals("0");
							client.menuItemName[p] = value ? "Value @ban@" + name : "Buy " + actions[p] + " @ban@" + name;
							client.menuItemCode[p] = 1900;
							client.menuItemArg3[p] = Integer.parseInt(actions[p]);
							client.menuItemArg2[p] = i;
							client.menuItemArg1[p] = icon;
							client.menuPos++;
						}
					}
				}
				
				if(!client.menuOpened && client.leftClickInRegion(beginX + 8 + x, beginY + offset, beginX + 80 + x, beginY + offset + 50)) {
					client.menuOpened = false;
					client.outBuffer.putOpcode(145);
					client.outBuffer.putShortPlus128(3900);
					client.outBuffer.putShortPlus128(0);
					client.outBuffer.putShortPlus128(icon);
					return true;
				}
				offset += i % 6 == 5 ? 55 : 0;
			}
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
		if(client.localPrivilege == 11) {
			plainFont.drawCenteredString("Edit", beginX + 350, beginY + 32, 0xd2c6a9);
			Rasterizer2D.fillRoundedRectangle(beginX + 323, beginY + 17, 54, 20, 2, 0x847653, 60);
			if(client.mouseInRegion(beginX + 325, beginY + 22, beginX + 381, beginY + 47)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 323, beginY + 17, 54, 20, 2, 0x847653, 20);
			}
		}
		if(back) {
			if(Config.def.panelStyle == 2) {
				plainFont.drawCenteredString("Back", beginX + 407, beginY + 27, 0xFF8A1F);
				Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 12, 54, 20, 2, 0xFF8A1F, 60);
				if(client.mouseInRegion(beginX + 382, beginY + 22, beginX + 438, beginY + 42)) {
					Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 12, 54, 20, 2, 0xFF8A1F, 20);
				}
			} else {
				plainFont.drawCenteredString("Back", beginX + 407, beginY + 32, 0xd2c6a9);
				Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 17, 54, 20, 2, 0x847653, 60);
				if(client.mouseInRegion(beginX + 382, beginY + 22, beginX + 438, beginY + 47)) {
					Rasterizer2D.fillRoundedRectangle(beginX + 380, beginY + 17, 54, 20, 2, 0x847653, 20);
				}
			}
		}
		boldFont.drawLeftAlignedEffectString(Interface.cache[3901].text, beginX + 20, beginY + 31, 0xFF8A1F, 0);
		
		Rasterizer2D.setClip(beginX + 5, beginY + 40, beginX + 493, beginY + 327);
		int offset = -scrollPos + 45;
		String tooltip = null;
		for(int i = 0; i < client.currentShopInterfacePrices.length; i++) {
			int icon = Interface.cache[3900].invId[i];
			if(icon == 0)
				continue;
			int x = i % 6 * 78;
			Rasterizer2D.fillRoundedRectangle(beginX + 8 + x, beginY + offset, 72, 50, 3, 0x000000, 45);
			if(!client.menuOpened && client.mouseInRegion(beginX + 8 + x, beginY + offset, beginX + 80 + x, beginY + offset + 50)) {
				Rasterizer2D.fillRectangle(beginX + 8 + x, beginY + offset, 72, 50, 0, 40);
				tooltip = ObjectType.get(icon).name;
			}
			final BitmapImage img = ObjectType.getIcon(icon, icon, 0);
			if(img != null) {
				img.drawImage(beginX + 28 + x, beginY + offset + 2);
			}
			if(currency != null) {
				Client.spriteCache.get(currency.getImage()).drawImage(beginX + 10 + x, beginY + offset + 32);
			}
			int am = Interface.cache[3900].invAmt[i];
			if(am == 999999999) {
				Client.spriteCache.get(2030).drawImage(beginX + 12 + x, beginY + offset + 6);
			} else {
				smallFont.drawLeftAlignedEffectString(am + "", beginX + 12 + x, beginY + offset + 14, 0xFF8A1F, 0);
			}
			smallFont.drawCenteredString(client.currentShopInterfacePrices[i] == 0 ? "free" : formatPrice(client.currentShopInterfacePrices[i]), beginX + 44 + x, beginY + offset + 46, 0xFF8A1F);
			offset += i % 6 == 5 ? 55 : 0;
		}
		if(tooltip != null) {
			boolean off = (client.mouseX - beginX + 8 + smallFont.getStringWidth(tooltip)) > 490;
			Rasterizer2D.fillRoundedRectangle(client.mouseX + (off ? -(smallFont.getStringWidth(tooltip) + 18) : 8), client.mouseY - 3, smallFont.getStringWidth(tooltip) + 7, 15, 3, 0x000000, 200);
			smallFont.drawLeftAlignedEffectString(tooltip, client.mouseX + (off ? -(smallFont.getStringWidth(tooltip) + 14) : 12), client.mouseY + 9, 0xFF8A1F, 0);
		}
		Rasterizer2D.drawRectangle(476 + beginX, 45 + beginY, 12, 280, 0xffffff, 60);
		int height = 278;
		if(scrollMax > 0) {
			height = 285 * 278 / (scrollMax + 285);
		}
		int pos = 0;
		if(scrollPos != 0) {
			pos = scrollPos * 278 / (scrollMax + 285) + 1;
		}
		Rasterizer2D.fillRectangle(477 + beginX, 46 + pos + beginY, 10, height, 0x222222, 120);
		Rasterizer2D.removeClip();
	}

	private static String formatPrice(int price) {
		if(price == 0)
			return "Free";
		if(price < 10000) {
			return NUMBER_FORMAT.format(price);
		}
		if(price < 10000000) {
			return NUMBER_FORMAT.format(price / 1000.D) + "K";
		} else {
			return NUMBER_FORMAT.format(price / 1000000.D) + "M";
		}
	}

	@Override
	public void initialize() {
	
	}

	@Override
	public void reset() {
	
	}

	@Override
	public int getId() {
		return 3;
	}
	
	@Override
	public boolean blockedMove() {
		return false;
	}
	
}
