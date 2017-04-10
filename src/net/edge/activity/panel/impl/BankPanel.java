package net.edge.activity.panel.impl;

import net.edge.activity.panel.Panel;
import net.edge.cache.unit.ImageCache;
import net.edge.cache.unit.Interface;
import net.edge.cache.unit.ObjectType;
import net.edge.game.Scene;
import net.edge.media.Rasterizer2D;
import net.edge.Client;
import net.edge.media.img.BitmapImage;

public class BankPanel extends Panel {

	/**
	 * The selected bank slot
	 */
	private int tab;

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
	private int scrollPos, scrollMax, scrollDragPos;

	/**
	 * The condition if the scroll is being dragged.
	 */
	private boolean scrollDrag = false;

	@Override
	public String id() {
		return "bank";
	}

	@Override
	public boolean process() {
	    /* Initialization */
		int beginX = 4;
		int beginY = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			beginX = client.windowWidth / 2 - 380;
			beginY = client.windowHeight / 2 - 250;
		}

		scrollMax = Math.max(50 * ((Interface.cache[270 + tab].invId.length + 6) / 7) - 275, 0);

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
			int height = 268;
			if(scrollMax > 0) {
				height = 275 * 268 / (scrollMax + 275);
			}
			int pos = 0;
			if(scrollPos != 0) {
				pos = scrollPos * 268 / (scrollMax + 275) + 1;
			}
			int x = 485;
			int y = 46 + pos;
			if(client.mouseDragButton == 1 && client.mouseInRegion(x, y, x + 20, y + height)) {
				scrollDrag = true;
				scrollDragPos = scrollPos;
			}
		} else if(client.mouseDragButton != 1 && srcSlot == -1) {
			scrollDrag = false;
		} else if(srcSlot == -1) {
			int d = (client.mouseY - client.clickY) * (scrollMax + 275) / 268;
			scrollPos = scrollDragPos + d;
			if(scrollPos < 0) {
				scrollPos = 0;
			}
			if(scrollPos > scrollMax) {
				scrollPos = scrollMax;
			}
		}
		
		//Settings
		if(!client.menuOpened) {
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
		}

        /* Exit */
		if(client.leftClickInRegion(beginX + 442, beginY + 12, beginX + 498, beginY + 42)) {
			client.panelHandler.close();
			client.outBuffer.putOpcode(185);
			client.outBuffer.putShort(123);
			Scene.hoverX = -1;
			return true;
		}

		/* Slots */
		for(int i = 1; i < 10; i++) {
			if(Interface.cache[270 + i - 1].invId == null)
				continue;
			int x = (i - 1) * 45;
			if(client.leftClickInRegion(beginX + 14 + x, beginY + 11, beginX + 50 + x, beginY + 47)) {
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
		int offset = -scrollPos + 52;
		if(client.bankSearching) {
			int shift = 0;
			for(int t = 0; t < 9; t++) {
				for(int i = 0; i < Interface.cache[270 + t].invId.length; i++) {
					int icon = Interface.cache[270 + t].invId[i];
					int x = shift % 7 * 65;
					if(icon != 0) {
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
							if(client.rightClickInRegion(beginX + 14 + x, beginY + offset, beginX + 74 + x, beginY + offset + 44)) {
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
							if(!client.menuOpened && client.leftClickInRegion(beginX + 14 + x, beginY + offset, beginX + 74 + x, beginY + offset + 44)) {
								srcSlot = i;
								srcIcon = icon;
								srcTab = t;
								itemPressX = client.mouseX;
								itemPressY = client.mouseY;
								return true;
							}
							offset += shift % 7 == 6 ? 50 : 0;
							shift++;
						}
					}
				}
			}
		} else {
			for(int i = 0; i < Interface.cache[270 + tab].invId.length; i++) {
				int icon = Interface.cache[270 + tab].invId[i];
				int x = i % 7 * 65;
				if(client.mouseInRegion(beginX + 14 + x, beginY + offset, beginX + 74 + x, beginY + offset + 44)) {
					destSlot = i;
				}
				if(icon == 0) {
					offset += i % 7 == 6 ? 50 : 0;
					continue;
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
				if(client.rightClickInRegion(beginX + 14 + x, beginY + offset, beginX + 74 + x, beginY + offset + 44)) {
					String name = ObjectType.get(icon).name;
					int[] ids = {53, 431, 867, 78, 632};
					String[] actions = { "X", "All", "10", "5", "1"};
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
				if(!client.menuOpened && client.leftClickInRegion(beginX + 14 + x, beginY + offset, beginX + 74 + x, beginY + offset + 44)) {
					srcSlot = i;
					srcIcon = icon;
					itemPressX = client.mouseX;
					itemPressY = client.mouseY;
					return true;
				}
				offset += i % 7 == 6 ? 50 : 0;
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
		Rasterizer2D.fillRoundedRectangle(beginX, beginY + 8, 500, 328, 4, 0x000000, 200);

		fancyFont.drawCenteredString("Exit", beginX + 467, beginY + 27, 0xF3B13F);
		Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 20, 2, 0xF3B13F, 60);
		if(client.mouseInRegion(beginX + 442, beginY + 12, beginX + 498, beginY + 42)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 440, beginY + 12, 54, 20, 2, 0xF3B13F, 20);
		}


		/* Slots */
		if(client.bankSearching) {
			fancyFont.drawLeftAlignedString("Looking for: " + client.bankSearch, beginX + 10, beginY + 33, 0xffffff);
		} else {
			for(int i = 1; i < 10; i++) {
				if(Interface.cache[270 + i - 1].invId == null)
					continue;
				int first = getFirst(Interface.cache[270 + i - 1]);
				int icon = Interface.cache[270 + i - 1].invId[first];
				int x = (i - 1) * 45;
				Rasterizer2D.fillRoundedRectangle(beginX + 14 + x, beginY + 11, 36, 36, 3, 0xF3B13F, icon == 0 ? 25 : 50 + ((i - 1) == tab ? 100 : 0));
				if(client.mouseInRegion(beginX + 14 + x, beginY + 11, beginX + 50 + x, beginY + 47)) {
					destSlot = -i;
					Rasterizer2D.fillRoundedRectangle(beginX + 14 + x, beginY + 11, 36, 36, 2, 0xF3B13F, 20);
				}
				if(icon != 0) {
					final BitmapImage img = ObjectType.getIcon(icon, Interface.cache[270 + i - 1].invAmt[first], 0);
					if(img != null) {
						img.drawImage(beginX + 16 + x, beginY + 13);
					}
				}
			}
		}


		/* Bank content */
		Rasterizer2D.drawRectangle(beginX + 4, beginY + 49, 490, 282, 0xffffff, 80);
		Rasterizer2D.fillRectangle(beginX + 5, beginY + 50, 488, 280, 0xffffff, 60);
		Rasterizer2D.setClip(beginX + 5, beginY + 50, beginX + 493, beginY + 330);
		int offset = -scrollPos + 52;
		int xSelected = 0;
		int ySelected = 0;
		String tooltip = "";

		if(client.bankSearching) {
			int shift = 0;
			for(int t = 0; t < 9; t++) {
				for(int i = 0; i < Interface.cache[270 + t].invId.length; i++) {
					int icon = Interface.cache[270 + t].invId[i];
					int x = shift % 7 * 65;
					if(icon != 0) {
						String name = ObjectType.get(icon).name;
						if(client.bankSearch.length() > 1 && name != null && name.toLowerCase().contains(client.bankSearch)) {
							Rasterizer2D.fillRoundedRectangle(beginX + 14 + x, beginY + offset, 60, 44, 3, 0x000000, 60);
							if(client.mouseInRegion(beginX + 14 + x, beginY + offset, beginX + 74 + x, beginY + offset + 44)) {
								Rasterizer2D.fillRectangle(beginX + 14 + x, beginY + offset, 60, 44, 0, 40);
								tooltip = ObjectType.get(icon).name;
							}
							final BitmapImage img = ObjectType.getIcon(icon, Interface.cache[270 + t].invAmt[i], 0);
							if(img != null) {
								img.drawImage(beginX + 28 + x, beginY + offset + 5);
								/* Amount numbers */
								if(Interface.cache[270 + t].invAmt[i] > 1) {
									String amt = Client.valueToKOrM(Interface.cache[270 + t].invAmt[i]);
									int color = 0xffff00;
									if(amt.endsWith("M")) {
										color = 0x00ff80;
									} else if(amt.endsWith("K")) {
										color = 0xffffff;
									}
									smallFont.drawLeftAlignedEffectString(amt, beginX + 18 + x, beginY + offset + 14, color, true);
								}
								offset += shift % 7 == 6 ? 50 : 0;
								shift++;
							}
						}
					}
				}
			}
		} else {
			for(int i = 0; i < Interface.cache[270 + tab].invId.length; i++) {
				int x = i % 7 * 65;
				int icon = Interface.cache[270 + tab].invId[i];
				Rasterizer2D.fillRoundedRectangle(beginX + 14 + x, beginY + offset, 60, 44, 3, 0x000000, 80);
				if(icon == 0) {
					offset += i % 7 == 6 ? 50 : 0;
					continue;
				}
				Rasterizer2D.fillRoundedRectangle(beginX + 14 + x, beginY + offset, 60, 44, 3, 0x000000, 60);
				if(client.mouseInRegion(beginX + 14 + x, beginY + offset, beginX + 74 + x, beginY + offset + 44)) {
					Rasterizer2D.fillRectangle(beginX + 14 + x, beginY + offset, 60, 44, 0, 40);
					tooltip = ObjectType.get(icon).name;
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
						xSelected = beginX + 28 + x + mouseDragOffsetX;
						ySelected = beginY + offset + 5 + mouseDragOffsetY;
						if(beginY + offset + 5 + mouseDragOffsetY < Rasterizer2D.clipStartY && scrollPos > 0) {
							int i10 = client.anInt945 * (Rasterizer2D.clipStartY - (beginY + offset + 5) - mouseDragOffsetY) / 3;
							if(i10 > client.anInt945 * 10) {
								i10 = client.anInt945 * 10;
							}
							if(i10 > scrollPos) {
								i10 = scrollPos;
							}
							scrollPos -= i10;
							itemPressY += i10;
						}
						if(beginY + offset + 5 + mouseDragOffsetY + 32 > Rasterizer2D.clipEndY && scrollPos < scrollMax - 268) {
							int j10 = client.anInt945 * (beginY + offset + 5 + mouseDragOffsetY + 32 - Rasterizer2D.clipEndY) / 3;
							if(j10 > client.anInt945 * 10) {
								j10 = client.anInt945 * 10;
							}
							if(j10 > scrollMax - 268 - scrollPos) {
								j10 = scrollMax - 268 - scrollPos;
							}
							scrollPos += j10;
							itemPressY -= j10;
						}
					} else {
						img.drawImage(beginX + 28 + x, beginY + offset + 5);
						/* Amount numbers */
						if(Interface.cache[270 + tab].invAmt[i] > 1) {
							String amt = Client.valueToKOrM(Interface.cache[270 + tab].invAmt[i]);
							int color = 0xffff00;
							if(amt.endsWith("M")) {
								color = 0x00ff80;
							} else if(amt.endsWith("K")) {
								color = 0xffffff;
							}
							smallFont.drawLeftAlignedEffectString(amt, beginX + 18 + x, beginY + offset + 14, color, true);
						}
					}
				}
				offset += i % 7 == 6 ? 50 : 0;
			}
		}

		/* Tooltip */
		if(!client.menuOpened && client.mouseDragButton == 0 && tooltip != null && !tooltip.isEmpty()) {
			boolean off = (client.mouseX + smallFont.getStringWidth(tooltip)) > 490;
			Rasterizer2D.fillRoundedRectangle(client.mouseX + (off ? -(smallFont.getStringWidth(tooltip) + 14) : 8), client.mouseY - 3, smallFont.getStringWidth(tooltip) + 7, 15, 3, 0x000000, 200);
			smallFont.drawLeftAlignedEffectString(tooltip, client.mouseX + (off ? -(smallFont.getStringWidth(tooltip) + 10) : 12), client.mouseY + 9, 0xF3B13F, true);
		}

		/* Scroll bar */
		Rasterizer2D.drawRectangle(476 + beginX, 55 + beginY, 12, 270, 0xffffff, 60);
		int height = 268;
		if(scrollMax > 0) {
			height = 275 * 268 / (scrollMax + 275);
		}
		int pos = 0;
		if(scrollPos != 0) {
			pos = scrollPos * 268 / (scrollMax + 275) + 1;
		}
		Rasterizer2D.fillRectangle(477 + beginX, 56 + pos + beginY, 10, height, 0x222222, 120);
		Rasterizer2D.removeClip();

		/* Dragging */
		if(xSelected != 0 || ySelected != 0 && srcSlot != -1) {
			int icon = Interface.cache[270 + tab].invId[srcSlot];
			if(icon != 0) {
				ObjectType.getIcon(icon, Interface.cache[270 + tab].invAmt[srcSlot], 0).drawImage(xSelected, ySelected, 128);
				/* Amount numbers */
				if(Interface.cache[270 + tab].invAmt[srcSlot] > 1) {
					String amt = Client.valueToKOrM(Interface.cache[270 + tab].invAmt[srcSlot]);
					int color = 0xffff00;
					if(amt.endsWith("M")) {
						color = 0x00ff80;
					} else if(amt.endsWith("K")) {
						color = 0xffffff;
					}
					smallFont.drawLeftAlignedEffectString(amt, xSelected - 10, ySelected + 9, color, true);
				}
			}
		}

		/* Settings */
		Rasterizer2D.fillRectangle(beginX + 135, beginY + 295, 215, 36, 0x000000, 120);
		for(int i = 6; i < 12; i++) {
			int x = i * 35;
			Rasterizer2D.fillRectangle(beginX - 70 + x, beginY + 298, 30, 30, 0xb35f52, 100);
			if(client.mouseInRegion(beginX - 70 + x, beginY + 298, beginX - 40 + x, beginY + 328)) {
				Rasterizer2D.fillRectangle(beginX - 70 + x, beginY + 298, 30, 30, 0xF3B13F, 20);
			}
			if(i == 6)
				ImageCache.get(client.anIntArray1045[116] == 0 ? 729 : 726).drawAlphaImage(beginX - 66 + x, beginY + 303);
			if(i == 7) {
				if(client.anIntArray1045[115] != 0)
					Rasterizer2D.fillRectangle(beginX - 70 + x, beginY + 298, 30, 30, 0xF3B13F, 60);
				ImageCache.get(732).drawAlphaImage(beginX - 65 + x, beginY + 304);
			}
			if(i == 8) {
				if(client.bankSearching)
					Rasterizer2D.fillRectangle(beginX - 70 + x, beginY + 298, 30, 30, 0xF3B13F, 60);
				ImageCache.get(733).drawAlphaImage(beginX - 65 + x, beginY + 304);
			}
			if(i == 9)
				ImageCache.get(731).drawAlphaImage(beginX - 68 + x, beginY + 304);
			if(i == 10)
				ImageCache.get(730).drawAlphaImage(beginX - 68 + x, beginY + 304);
			if(i == 11)
				ImageCache.get(734).drawAlphaImage(beginX - 68 + x, beginY + 304);
		}
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
}
