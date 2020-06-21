package net.arrav.activity.panel.impl;

import net.arrav.Config;
import net.arrav.activity.panel.Panel;
import net.arrav.cache.unit.interfaces.Interface;
import net.arrav.cache.unit.ObjectType;
import net.arrav.graphic.Rasterizer2D;
import net.arrav.Client;
import net.arrav.graphic.img.BitmapImage;

public class BankPanel extends Panel {

	/**
	 * The selected bank slot
	 * static to keep it saved.
	 */
	private static int tab;

	/**
	 * An item selection manipulator value.
	 */
	private int srcSlot = -1, srcIcon, destSlot = -1, srcTab = 0, itemPressX, itemPressY;

	/**
	 * The condition if the item is being dragged.
	 */
	private boolean itemDrag = false;

	/**
	 * Scroll bar manipulated value.
	 */
	private static int scrollPos;//static to save.
	private int scrollMax;
	private int scrollDragPos;

	/**
	 * The condition if the scroll is being dragged.
	 */
	private boolean scrollDrag = false;

	@Override
	public boolean process() {
	    /* Initialization */
		int beginX = 10;
		int beginY = 0;
		if (client.uiRenderer.isResizableOrFull()) {
			beginX = client.windowWidth / 2 - 380;
			beginY = client.windowHeight / 2 - 250;
		}

		scrollMax = Math.max(45 * ((Interface.cache[270 + tab].invId.length + 7) / 8) - 235, 0);

        /* Scrolling */
		if(client.mouseInRegion(beginX + 5, beginY + 50, beginX + 493, beginY + 365)) {
			scrollPos += client.mouseWheelAmt * 24;
			if(scrollPos < 0) {
				scrollPos = 0;
			}
			if(scrollPos > scrollMax) {
				scrollPos = scrollMax;
			}
		}
		if(!scrollDrag && srcSlot == -1) {
			int height = 228;
			if(scrollMax > 0) {
				height = 235 * 228 / (scrollMax + 235);
			}
			int pos = beginY;
			if(scrollPos != 0) {
				pos = scrollPos * 228 / (scrollMax + 235) + 1;
			}
			int x = 482 + beginX;
			int y = 60 + pos;
			if(client.mouseDragButton == 1 && client.mouseInRegion(x, y, x + 20, y + height)) {
				scrollDrag = true;
				scrollDragPos = scrollPos;
			}
		} else if(client.mouseDragButton != 1 && srcSlot == -1) {
			scrollDrag = false;
		} else if(srcSlot == -1) {
			int d = (client.mouseY - client.clickY) * (scrollMax + 235) / 228;
			scrollPos = scrollDragPos + d;
			if(scrollPos < 0) {
				scrollPos = 0;
			}
			if(scrollPos > scrollMax) {
				scrollPos = scrollMax;
			}
		}

		if(processClose(beginX, beginY)) {
			return true;
		}
		if(processSettings(beginX, beginY)) {
			return true;
		}

		/* Slots */
		for(int i = 1; i < 11; i++) {
			if(Interface.cache[270 + i - 1].invId == null)
				continue;
			int x = (i - 1) * 40;
			/* bank tabs clicking range */
			if (client.leftClickInRegion(beginX + 42 + x, beginY + 41, beginX + 81 + x, beginY + 81)) {
				tab = (i - 1);
				client.outBuffer.putOpcode(185);
				client.outBuffer.putShort((i - 1) + 100);
			}
		}

		if(itemPressX != 0 && itemPressY != 0) {
			if(client.mouseX > itemPressX + 5 || client.mouseX < itemPressX - 5 || client.mouseY > itemPressY + 5 || client.mouseY < itemPressY - 5) {
				itemDrag = true;
			}
		}

		/* Bank content */
		int offset = -scrollPos + (client.bankSearching ? 45 : 80);
		if(client.bankSearching) {
			int shift = 0;
			for(int t = 0; t < 10; t++) {
				for(int i = 0; i < Interface.cache[270 + t].invId.length; i++) {
					int icon = Interface.cache[270 + t].invId[i];
					int x = shift % 8 * 50;
					if(icon > 0) {
						String name = ObjectType.get(icon).name;
						if(client.bankSearch.length() > 1 && name != null && name.toLowerCase().contains(client.bankSearch)) {
							if(client.mouseDragButton == 0 && srcSlot != -1) {
								client.outBuffer.putOpcode(145);
								client.outBuffer.putShortPlus128(srcTab);
								client.outBuffer.putShortPlus128(srcSlot);
								client.outBuffer.putShortPlus128(srcIcon);
								srcSlot = -1;
								itemPressX = 0;
								itemPressY = 0;
								itemDrag = false;
								return true;
							}
							if(client.rightClickInRegion(beginX + 55 + x, beginY + offset, beginX + 91 + x, beginY + offset + 44)) { // right click on item pos
								int[] ids = {53, 431, 867, 78, 632};
								String[] actions = { "X", "All", "10", "5", "1"};
								client.menuPos = 0;
								for(int p = 0; p < ids.length; p++) {
									client.menuItemName[p] = "Withdraw " + actions[p] + " @ban@" + name;
									client.menuItemCode[p] = ids[p];
									client.menuItemArg3[p] = t;
									client.menuItemArg2[p] = i;
									client.menuItemArg1[p] = icon;
									client.menuPos++;
								}
								srcSlot = -1;
								itemPressX = 0;
								itemPressY = 0;
								itemDrag = false;
								client.activeInterfaceType = 5;
								return true;
							}
							if(!client.menuOpened && client.leftClickInRegion(beginX + 55 + x, beginY + offset, beginX + 91 + x, beginY + offset + 44)) { //left click item pos
								srcSlot = i;
								srcIcon = icon;
								srcTab = t;
								itemPressX = client.mouseX;
								itemPressY = client.mouseY;
								return true;
							}
							offset += shift % 8 == 7 ? 45 : 0;
							shift++;
						}
					}
				}
			}
		} else if(this.on()) {
			for(int i = 0; i < Interface.cache[270 + tab].invId.length; i++) {
				int icon = Interface.cache[270 + tab].invId[i];
				int x = i % 8 * 50; // default 57
				if(client.mouseInRegion(beginX + 55 + x, beginY + offset, beginX + 91 + x, beginY + offset + 44)) {
					destSlot = i;
				}
				if(icon <= 0) {
					offset += i % 8 == 7 ? 40 : 0;
					continue;
				}
				if(offset > 260) {
					break;
				}
				if(client.mouseDragButton == 0 && srcSlot != -1) {
					if(itemDrag) {
						if(destSlot < 0) {
							destSlot = (-destSlot) - 1;
							client.outBuffer.putOpcode(216);
							client.outBuffer.putLitEndShortPlus128(tab);
							client.outBuffer.putLitEndShortPlus128(srcSlot);
							client.outBuffer.putLitEndShortPlus128(destSlot);
						} else {
							if(client.anIntArray1045[116] == 0) {
								int src = srcSlot;
								for(int dest = destSlot; src != dest; ) {
									if(src > dest) {
										Interface.cache[270 + tab].invSwap(src, src - 1);
										src--;
									} else if(src < dest) {
										Interface.cache[270 + tab].invSwap(src, src + 1);
										src++;
									}
								}
							} else {
								Interface.cache[270 + tab].invSwap(srcSlot, destSlot);
							}
							client.outBuffer.putOpcode(214);
							client.outBuffer.putLitEndShortPlus128(tab);
							client.outBuffer.putLitEndShortPlus128(srcSlot);
							client.outBuffer.putLitEndShort(destSlot);
						}
					} else {
						client.outBuffer.putOpcode(145);
						client.outBuffer.putShortPlus128(tab);
						client.outBuffer.putShortPlus128(srcSlot);
						client.outBuffer.putShortPlus128(srcIcon);
					}
					srcSlot = -1;
					itemPressX = 0;
					itemPressY = 0;
					itemDrag = false;
					return true;
				}
				if(client.mouseY > beginY + 82) {
					if(client.rightClickInRegion(beginX + 52 + x, beginY + offset + 2, beginX + 87 + x, beginY + offset + 41)) {
						String name = ObjectType.get(icon).name;
						int[] ids = {53, 431, 867, 78, 632};
						String[] actions = {"X", "All", "10", "5", "1"};
						client.menuPos = 0;
						for(int p = 0; p < ids.length; p++) {
							client.menuItemName[p] = "Withdraw " + actions[p] + " @ban@" + name;
							client.menuItemCode[p] = ids[p];
							client.menuItemArg3[p] = tab;
							client.menuItemArg2[p] = i;
							client.menuItemArg1[p] = icon;
							client.menuPos++;
						}
						srcSlot = -1;
						itemPressX = 0;
						itemPressY = 0;
						itemDrag = false;
						client.activeInterfaceType = 5;
						return true;
					}
				}
				if(!client.menuOpened && client.leftClickInRegion(beginX + 52 + x, beginY + offset + 2, beginX + 87 + x, beginY + offset + 41)) {
					srcSlot = i;
					srcIcon = icon;
					itemPressX = client.mouseX;
					itemPressY = client.mouseY;
					return true;
				}
				offset += i % 8 == 7 ? 40 : 0;
			}
		}
		return false;
	}

	@Override
	public void update() {
		/* Initialization */
		int beginX = 10;
		int beginY = 0;
		if (client.uiRenderer.isResizableOrFull()) {
			beginX = client.windowWidth / 2 - 380;
			beginY = client.windowHeight / 2 - 250;
		}

		/* Main background */
		drawMain(beginX, beginY + 8, 500, 328, 0x000000, 0x63625e, 200);
		drawOver(beginX, beginY);
		drawClose(beginX, beginY);

		boldFont.drawCenteredEffectString("Bank of Arrav", 250, beginY + 31, 0xff981f, 0);

		/* Slots */
		if (client.bankSearching) {
			smallFont.drawLeftAlignedEffectString("Searching for: " + client.bankSearch, beginX + 10, beginY + 33, 0xff981f, 0);
		} else {
			for (int i = 1; i < 11; i++) {
				if (Interface.cache[270 + i - 1].invId == null)
					continue;
				int first = getFirst(Interface.cache[270 + i - 1]);
				int icon = Interface.cache[270 + i - 1].invId[first];
				int x = (i - 1) * 40;
				if (Config.def.panelStyle == 2) {
					Rasterizer2D.fillRoundedRectangle(beginX + 44 + x, beginY + 16, 44, 36, 3, 0xff981f, icon > 0 ? 50 + ((i - 1) == tab ? 100 : 0) : 25);
					if (client.mouseInRegion(beginX + 44 + x, beginY + 16, beginX + 95 + x, beginY + 52)) {
						destSlot = -i;
						Rasterizer2D.fillRoundedRectangle(beginX + 44 + x, beginY + 16, 44, 36, 3, 0xff981f, 20);
					}
				} else {
					/*
					 * Tab positions
					 */
					Client.spriteCache.get(i - 1 == tab ? 2019 : 2018).drawImage(beginX + 42 + x, beginY + 41); // bank tabs
					
					if (client.mouseInRegion(beginX + 42 + x, beginY + 41, beginX + 81 + x, beginY + 81)) {
						destSlot = -i;
						Rasterizer2D.fillRectangle(beginX + 44 + x, beginY + 43, 38, 36, Config.def.panelStyle == 1 ? 0x000000 : 0xaaaaaa, 20);
					}
					if(icon <= 0) {
						Client.spriteCache.get(2060).drawImage(beginX + 45 + x, beginY + 43); // bank tab + icon
					}
				}
				if (icon > 0) {
					final BitmapImage img = ObjectType.getIcon(icon, Interface.cache[270 + i - 1].invAmt[first], 0);
					if (img != null) {
						img.drawImage(beginX + 47 + x, beginY + 44); // bank tab model
					}
				}
			}
			//Client.spriteCache.get(2000).drawImage(beginX + 42, beginY + 41); // bank tabs
			//Client.spriteCache.get(2052).drawImage(beginX + 46, beginY + 43); // bank tab + icon
			if (client.mouseInRegion(beginX + 44, beginY + 40, beginX + 92, beginY + 88)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 44, beginY + 41, 37, 36, 3, Config.def.panelStyle == 1 ? 0x000000 : 0xaaaaaa, 20);
			}

		}
		if (Config.def.panelStyle == 2 && !client.bankSearching) {
			Rasterizer2D.drawRectangle(beginX + 4, beginY + 53, 490, 280, Config.def.panelStyle == 2 ? 0xffffff : 0x514a35, 80);
			Rasterizer2D.fillRectangle(beginX + 5, beginY + 54, 488, 278, Config.def.panelStyle == 2 ? 0xffffff : 0x514a35, 60);
		} else if(!client.bankSearching) {
			Rasterizer2D.drawHorizontalLine(beginX + 5, beginY + 80, 37, 0x000000, 55);
			Rasterizer2D.drawHorizontalLine(beginX + 440, beginY + 80, 38, 0x000000, 55);
		}
		if (Config.def.panelStyle == 1 && !client.bankSearching) {
			Rasterizer2D.fillRectangle(beginX + 5, beginY + 81, 488, 210, 0x000000, 30);
		}

		Rasterizer2D.setClip(beginX + 5, !client.bankSearching ? beginY + 81 : 43, beginX + 493, beginY + (Config.def.panelStyle == 2 ? 298 : 288));

		int offset = -scrollPos + (client.bankSearching ? 45 : 80);
		int xSelected = 0;
		int ySelected = 0;
		String tooltip = "";
		int itemsCount = 0;
		if(client.bankSearching) {
			int shift = 0;
			for(int t = 0; t < 10; t++) {
				for(int i = 0; i < Interface.cache[270 + t].invId.length; i++) {
					int icon = Interface.cache[270 + t].invId[i];
					int x = shift % 8 * 50; //default 57
					if(icon > 0) {
						String name = ObjectType.get(icon).name;
						if (client.bankSearch.length() == 0) {
							plainFont.drawCenteredEffectString("No search term entered!", 250, 164, 0x757575, 0);
						}
						if(client.bankSearch.length() > 0 && name != null && name.toLowerCase().contains(client.bankSearch)) {
							if(client.mouseInRegion(beginX + 55 + x, beginY + offset, beginX + 91 + x, beginY + offset + 44)) {
								tooltip = ObjectType.get(icon).name;
							}
							final BitmapImage img = ObjectType.getIcon(icon, Interface.cache[270 + t].invAmt[i], 0);
							if(img != null) {
								img.drawImage(beginX + 53 + x, beginY + offset + 5);
								/* Amount numbers */
								if(Interface.cache[270 + t].invAmt[i] > 1) {
									String amt = Client.valueToKOrM(Interface.cache[270 + t].invAmt[i]);
									int color = 0xffff00;
									if(amt.endsWith("M")) {
										color = 0x00ff80;
									} else if(amt.endsWith("K")) {
										color = 0xffffff;
									}
									smallFont.drawLeftAlignedEffectString(amt, beginX + 54 + x, beginY + offset + 14, color, 0);
								}
								offset += shift % 7 == 6 ? 45 : 0;
								shift++;
							}
						}
					}
				}
			}
		} else {
			for(int i = 0; i < Interface.cache[270 + tab].invId.length; i++) {
				int x = i % 8 * 50; // 57 is default, space between item models
				int icon = Interface.cache[270 + tab].invId[i];
				if(icon <= 0) {
					offset += i % 8 == 7 ? 40 : 0;
					continue;
				}
				itemsCount++;
				/* Slots item tooltip */
				if(client.mouseY > beginY + 82) {
					if(client.mouseInRegion(beginX + 52 + x, beginY + offset + 2, beginX + 87 + x, beginY + offset + 41)) {
						tooltip = ObjectType.get(icon).name;
					}
				}
				/* Icons */
				final BitmapImage img = ObjectType.getIcon(icon, Interface.cache[270 + tab].invAmt[i], 0);
				if(img != null) {
					if(srcSlot == i) {
						int mouseDragOffsetX = client.mouseX - itemPressX;
						int mouseDragOffsetY = client.mouseY - itemPressY;
						if(mouseDragOffsetX < 5 && mouseDragOffsetX > -5) {
							mouseDragOffsetX = 0;
						}
						if(mouseDragOffsetY < 5 && mouseDragOffsetY > -5) {
							mouseDragOffsetY = 0;
						}
						xSelected = beginX + 53 + x + mouseDragOffsetX;
						ySelected = beginY + offset + 5 + mouseDragOffsetY;
						if(beginY + offset + 5 + mouseDragOffsetY < Rasterizer2D.clipStartY && scrollPos > 0) {
							int i10 = client.tickDelta * (Rasterizer2D.clipStartY - (beginY + offset + 5) - mouseDragOffsetY) / 3;
							if(i10 > client.tickDelta * 10) {
								i10 = client.tickDelta * 10;
							}
							if(i10 > scrollPos) {
								i10 = scrollPos;
							}
							scrollPos -= i10;
							itemPressY += i10;
						}
						if(beginY + offset + 5 + mouseDragOffsetY + 32 > Rasterizer2D.clipEndY && scrollPos < scrollMax - 268) {
							int j10 = client.tickDelta * (beginY + offset + 5 + mouseDragOffsetY + 32 - Rasterizer2D.clipEndY) / 3;
							if(j10 > client.tickDelta * 10) {
								j10 = client.tickDelta * 10;
							}
							if(j10 > scrollMax - 268 - scrollPos) {
								j10 = scrollMax - 268 - scrollPos;
							}
							scrollPos += j10;
							itemPressY -= j10;
						}
					} else {
						img.drawImage(beginX + 53 + x, beginY + offset + 5); // item model beginX + 28 default
						/* Amount numbers */
						if(Interface.cache[270 + tab].invAmt[i] > 1) {
							String amt = Client.valueToKOrM(Interface.cache[270 + tab].invAmt[i]);
							int color = 0xffff00;
							if(amt.endsWith("M")) {
								color = 0x00ff80;
							} else if(amt.endsWith("K")) {
								color = 0xffffff;
							}
							smallFont.drawLeftAlignedEffectString(amt, beginX + 54 + x, beginY + offset + 14, color, 0); // amount text, beginx+29 = default
						}
					}
				}
				offset += i % 8 == 7 ? 40 : 0;
			}
		}

		/* Tooltip */
		if(!client.menuOpened && client.mouseDragButton == 0 && tooltip != null && !tooltip.isEmpty()) {
			boolean off = (client.mouseX + smallFont.getStringWidth(tooltip)) > 490;
			Rasterizer2D.fillRoundedRectangle(client.mouseX + (off ? -(smallFont.getStringWidth(tooltip) + 14) : 8), client.mouseY - 3, smallFont.getStringWidth(tooltip) + 7, 15, 3, 0x000000, 200);
			smallFont.drawLeftAlignedEffectString(tooltip, client.mouseX + (off ? -(smallFont.getStringWidth(tooltip) + 10) : 12), client.mouseY + 9, 0xff981f, 0);
		}
		Rasterizer2D.removeClip();

		/* Scroll bar */
		int pos = 0;
		if(scrollPos != 0) {
			pos = scrollPos * 228 / (scrollMax + 225);
		}
		int height = 228;
		if(scrollMax > 0) {
			height = 235 * 228 / (scrollMax + 225);
		}
		if(Config.def.panelStyle == 2) {
			Rasterizer2D.drawRectangle(476 + beginX, 55 + beginY, 12, 230, 0xffffff, 60);
			Rasterizer2D.fillRectangle(477 + beginX, 56 + pos + beginY, 10, height - 1, 0x222222, 120);
		} else {
			drawScroll(477 + beginX, client.bankSearching ? 43 : 78  + beginY, 215 + (client.bankSearching ? 35 : 0), scrollMax + 215, scrollPos);
		}
		/* Dragging */
		if(xSelected != 0 || ySelected != 0 && srcSlot != -1) {
			int icon = Interface.cache[270 + tab].invId[srcSlot];
			if(icon > 0) {
				BitmapImage img = ObjectType.getIcon(icon, Interface.cache[270 + tab].invAmt[srcSlot], 0);
				if(img != null) {
					img.drawImage(xSelected, ySelected, 128);
				}
				/* Amount numbers */
				if(Interface.cache[270 + tab].invAmt[srcSlot] > 1) {
					String amt = Client.valueToKOrM(Interface.cache[270 + tab].invAmt[srcSlot]);
					int color = 0xffff00;
					if(amt.endsWith("M")) {
						color = 0x00ff80;
					} else if(amt.endsWith("K")) {
						color = 0xffffff;
					}
					smallFont.drawLeftAlignedEffectString(amt, xSelected + 1, ySelected + 9, color, 0);
				}
			}
		}
		drawSettings(beginX, beginY, itemsCount, Interface.cache[270 + tab].invId.length);
	}

	private int getFirst(Interface inter) {
		for(int i = 0; i < inter.invId.length; i++) {
			if(inter.invId[i] != 0) {
				return i;
			}
		}
		return 0;
	}


	@Override
	public void initialize() {
		client.bankSearching = false;
		client.bankSearch = "";
	}

	@Override
	public void reset() {
		client.bankSearching = false;
		client.bankSearch = "";
	}

	@Override
	public int getId() {
		return 6;
	}

	@Override
	public boolean blockedMove() {
		return false;
	}

	private void drawSettings(int beginX, int beginY, int itemCount, int max) {
		/* Settings */
		if(Config.def.panelStyle == 2) {
			Rasterizer2D.fillRectangle(beginX + 5, beginY + 296, 488, 36, 0x000000, 120);
			for(int i = 6; i < 12; i++) {
				int x = i * 35;
				Rasterizer2D.fillRectangle(beginX - 70 + x, beginY + 298, 30, 30, 0xb35f52, 100);
				if(client.mouseInRegion(beginX - 70 + x, beginY + 298, beginX - 40 + x, beginY + 328)) {
					Rasterizer2D.fillRectangle(beginX - 70 + x, beginY + 298, 30, 30, 0xff981f, 20);
				}
				if(i == 6)
					Client.spriteCache.get(client.anIntArray1045[116] == 0 ? 729 : 726).drawImage(beginX - 66 + x, beginY + 303);
				if(i == 7) {
					if(client.anIntArray1045[115] != 0)
						Rasterizer2D.fillRectangle(beginX - 70 + x, beginY + 298, 30, 30, 0xff981f, 60);
					Client.spriteCache.get(732).drawImage(beginX - 65 + x, beginY + 304);
				}
				if(i == 8) {
					if(client.bankSearching)
						Rasterizer2D.fillRectangle(beginX - 70 + x, beginY + 298, 30, 30, 0xff981f, 60);
					Client.spriteCache.get(733).drawImage(beginX - 65 + x, beginY + 304);
				}
				if(i == 9)
					Client.spriteCache.get(731).drawImage(beginX - 68 + x, beginY + 304);
				if(i == 10)
					Client.spriteCache.get(730).drawImage(beginX - 68 + x, beginY + 304);
				if(i == 11)
					Client.spriteCache.get(734).drawImage(beginX - 68 + x, beginY + 304);
			}
		} else {
			if(Config.def.panelStyle == 1)
				Rasterizer2D.fillRectangle(beginX + 6, beginY + 290, 488, 36, 0x000000, 120);
			Client.spriteCache.get(Config.def.panelStyle == 0 ? 2010 : 2025).drawImage(beginX + 442, beginY + 293);
			Client.spriteCache.get(Config.def.panelStyle == 0 ? 2008 : 2023).drawImage(beginX + 409, beginY + 293);
			Client.spriteCache.get(Config.def.panelStyle == 0 ? 2014 : 2027).drawImage(beginX + 376, beginY + 293);
			Client.spriteCache.get(Config.def.panelStyle == 0 ? 2006 : 2021).drawImage(beginX + 343, beginY + 293);
			if(client.bankSearching) {
				Client.spriteCache.get(Config.def.panelStyle == 0 ? 2007 : 2022).drawImage(beginX + 343, beginY + 293);
			}
			if(Config.def.panelStyle == 0) {
				plainFont.drawCenteredEffectString("Rearrange mode:", beginX + 93, beginY + 300, 0xff981f, 0);
				Client.spriteCache.get(2016).drawImage(beginX + 4, beginY + 304);
				if(client.anIntArray1045[116] == 0) {
					Client.spriteCache.get(2017).drawImage(beginX + 4, beginY + 304);
				}
				plainFont.drawCenteredEffectString("Swap", beginX + 45, beginY + 319, 0xff981f, 0);
				Client.spriteCache.get(2016).drawImage(beginX + 86, beginY + 304);
				if(client.anIntArray1045[116] != 0) {
					Client.spriteCache.get(2017).drawImage(beginX + 86, beginY + 304);
				}
				plainFont.drawCenteredEffectString("Insert", beginX + 127, beginY + 319, 0xff981f, 0);
				plainFont.drawCenteredEffectString("Withdraw as:", beginX + 263, beginY + 300, 0xff981f, 0);
				Client.spriteCache.get(2016).drawImage(beginX + 173, beginY + 304);
				if(client.anIntArray1045[115] == 0) {
					Client.spriteCache.get(2017).drawImage(beginX + 173, beginY + 304);
				}
				plainFont.drawCenteredEffectString("Item", beginX + 214, beginY + 319, 0xff981f, 0);
				Client.spriteCache.get(2016).drawImage(beginX + 258, beginY + 304);
				if(client.anIntArray1045[115] != 0) {
					Client.spriteCache.get(2017).drawImage(beginX + 258, beginY + 304);
				}
				plainFont.drawCenteredEffectString("Noted", beginX + 299, beginY + 319, 0xff981f, 0);
			} else {
				Client.spriteCache.get(client.anIntArray1045[115] == 0 ? 2004 : 2005).drawImage(beginX + 104, beginY + 296);
				if(client.mouseInRegion(beginX + 104, beginY + 296, beginX + 139, beginY + 321)) {
					Rasterizer2D.fillRectangle(beginX + 104, beginY + 296, 35, 25, 0x000000, 30);
				}
				Client.spriteCache.get(client.anIntArray1045[116] != 0 ? 2012 : 2013).drawImage(beginX + 64, beginY + 296);
				if(client.mouseInRegion(beginX + 64, beginY + 296, beginX + 99, beginY + 321)) {
					Rasterizer2D.fillRectangle(beginX + 64, beginY + 296, 35, 25, 0x000000, 30);
				}
				Client.spriteCache.get(2020).drawImage(beginX + 9, beginY + 293);
				smallFont.drawCenteredEffectString("" + itemCount, beginX + 29, beginY + 305, 0xff981f, 0);
				smallFont.drawCenteredEffectString("" + max, beginX + 29, beginY + 320, 0xff981f, 0);
			}
		}
	}

	private boolean processSettings(int beginX, int beginY) {
		if(!client.menuOpened) {
			if(Config.def.panelStyle == 2) {
				for(int i = 6; i < 12; i++) {
					int x = i * 35;
					if(client.leftClickInRegion(beginX - 70 + x, beginY + 294, beginX - 40 + x, beginY + 324)) {
						if(i == 6) {
							client.outBuffer.putOpcode(185);
							client.outBuffer.putShort(59173);
						}
						if(i == 7) {
							client.outBuffer.putOpcode(185);
							client.outBuffer.putShort(59177);
						}
						if(i == 8) {
							client.bankSearching = !client.bankSearching;
							client.bankSearch = "";
						}
						if(i == 9) {
							client.outBuffer.putOpcode(185);
							client.outBuffer.putShort(21010);
						}
						if(i == 10) {
							client.outBuffer.putOpcode(185);
							client.outBuffer.putShort(59183);
						}
						if(i == 11) {
							client.outBuffer.putOpcode(185);
							client.outBuffer.putShort(59179);
						}
						return true;
					}
				}
			} else {
				if(client.leftClickInRegion(beginX + 442, beginY + 293, beginX + 474, beginY + 326)) {
					client.outBuffer.putOpcode(185);
					client.outBuffer.putShort(59183);
				}
				if(client.leftClickInRegion(beginX + 409, beginY + 293, beginX + 442, beginY + 326)) {
					client.outBuffer.putOpcode(185);
					client.outBuffer.putShort(21010);
				}
				if(client.leftClickInRegion(beginX + 376, beginY + 293, beginX + 408, beginY + 326)) {
					client.outBuffer.putOpcode(185);
					client.outBuffer.putShort(59179);
				}
				if(client.leftClickInRegion(beginX + 343, beginY + 293, beginX + 376, beginY + 326)) {
					client.bankSearching = !client.bankSearching;
					client.bankSearch = "";
				}
				if(Config.def.panelStyle == 0) {
					if(client.leftClickInRegion(beginX + 4, beginY + 304, beginX + 89, beginY + 326)) {
						if(client.anIntArray1045[116] != 0) {
							client.outBuffer.putOpcode(185);
							client.outBuffer.putShort(59173);
						}
					}
					if(client.leftClickInRegion(beginX + 86, beginY + 304, beginX + 171, beginY + 326)) {
						if(client.anIntArray1045[116] == 0) {
							client.outBuffer.putOpcode(185);
							client.outBuffer.putShort(59173);
						}
					}
					if(client.leftClickInRegion(beginX + 173, beginY + 304, beginX + 258, beginY + 326)) {
						if(client.anIntArray1045[115] != 0) {
							client.outBuffer.putOpcode(185);
							client.outBuffer.putShort(59177);
						}
					}
					if(client.leftClickInRegion(beginX + 258, beginY + 304, beginX + 343, beginY + 326)) {
						if(client.anIntArray1045[115] == 0) {
							client.outBuffer.putOpcode(185);
							client.outBuffer.putShort(59177);
						}
					}
				} else {
					if(client.leftClickInRegion(beginX + 104, beginY + 296, beginX + 139, beginY + 321)) {
						client.outBuffer.putOpcode(185);
						client.outBuffer.putShort(59177);
					}
					if(client.leftClickInRegion(beginX + 64, beginY + 296, beginX + 99, beginY + 321)) {
						client.outBuffer.putOpcode(185);
						client.outBuffer.putShort(59173);
					}
				}
			}
		}
		return false;
	}

}
