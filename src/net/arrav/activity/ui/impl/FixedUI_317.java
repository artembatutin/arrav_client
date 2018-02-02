package net.arrav.activity.ui.impl;

import net.arrav.Config;
import net.arrav.Constants;
import net.arrav.activity.panel.impl.SettingPanel;
import net.arrav.activity.ui.UIComponent;
import net.arrav.activity.ui.util.CounterHandler;
import net.arrav.activity.ui.util.OrbHandler;
import net.arrav.world.model.Player;
import net.arrav.cache.unit.ImageCache;
import net.arrav.cache.unit.Interface;
import net.arrav.cache.unit.NPCType;
import net.arrav.world.model.Model;
import net.arrav.world.model.NPC;
import net.arrav.graphic.Rasterizer2D;
import net.arrav.graphic.Rasterizer3D;
import net.arrav.graphic.img.BitmapImage;
import net.arrav.util.collect.LinkedDeque;
import net.arrav.util.string.StringUtils;

public class FixedUI_317 extends FixedUI {

	@Override
	public void buildChat() {
		final String[] modes = {"View", "On", "Friends", "Off", "Hide"};
		if(client.mouseX >= 6 && client.mouseX <= 106 && client.mouseY >= 467 && client.mouseY <= 499) {
			for(int i = 0; i <= 4; i++) {
				client.menuItemName[i + 1] = modes[4 - i] + " public";
				client.menuItemCode[i + 1] = 997 - i;
			}
			client.menuPos = 6;
			if(client.clickButton == 1) {
				client.publicChatMode = (client.publicChatMode + 1) % 4;
				client.outBuffer.putOpcode(95);
				client.outBuffer.putByte(client.publicChatMode);
				client.outBuffer.putByte(client.privateChatMode);
				client.outBuffer.putByte(client.tradeMode);
			}
		} else if(client.mouseX >= 135 && client.mouseX <= 235 && client.mouseY >= 467 && client.mouseY <= 499) {
			for(int i = 0; i <= 3; i++) {
				client.menuItemName[i + 1] = modes[3 - i] + " private";
				client.menuItemCode[i + 1] = 992 - i;
			}
			client.menuPos = 4;
			if(client.clickButton == 1) {
				client.privateChatMode = (client.privateChatMode + 1) % 3;
				client.outBuffer.putOpcode(95);
				client.outBuffer.putByte(client.publicChatMode);
				client.outBuffer.putByte(client.privateChatMode);
				client.outBuffer.putByte(client.tradeMode);
			}
		} else if(client.mouseX >= 273 && client.mouseX <= 373 && client.mouseY >= 467 && client.mouseY <= 499) {
			for(int i = 0; i <= 3; i++) {
				client.menuItemName[i + 1] = modes[3 - i] + " trade";
				client.menuItemCode[i + 1] = 987 - i;
			}
			client.menuPos = 5;
			if(client.clickButton == 1) {
				client.tradeMode = (client.tradeMode + 1) % 3;
				client.outBuffer.putOpcode(95);
				client.outBuffer.putByte(client.publicChatMode);
				client.outBuffer.putByte(client.privateChatMode);
				client.outBuffer.putByte(client.tradeMode);
			}
		}
		if(client.forcedChatWidgetId == -1) {
			client.chatWidget.scrollPos = client.chatContentHeight - client.chatScrollPos - 76;
			if(client.mouseX > 478 && client.mouseX < 580 && client.mouseY > 342) {
				client.gameActivity.processScrollbar(481, 20, client.mouseX, client.mouseY - 338, 76, client.chatContentHeight, client.chatWidget);
			}
			int pos = client.chatContentHeight - 76 - client.chatWidget.scrollPos;
			if(client.mouseWheelAmt != 0 && client.mouseWheelX >= 17 && client.mouseWheelY >= 357 &&
					client.mouseWheelX <= 495 && client.mouseWheelY <= 433) {
				pos -= 30 * client.mouseWheelAmt;
			}
			if(pos < 0) {
				pos = 0;
			}
			if(pos > client.chatContentHeight - 76) {
				pos = client.chatContentHeight - 76;
			}
			if(client.chatScrollPos != pos) {
				client.chatScrollPos = pos;
			}
		}
	}

	@Override
	public void updateChat() {
		client.chatGraphics.setCanvas();
		Rasterizer3D.viewport = client.chatAreaViewport;
		ImageCache.get(189).drawImage(0, 0);
		displayChannelButtons();
		if(client.messagePromptRaised) {
			client.boldFont.drawCenteredString(client.promptInputTitle, 259, 60, 0);
			client.boldFont.drawCenteredString(client.promptInput + "*", 259, 80, 128);
		} else if(client.inputDialogState == 1) {
			client.boldFont.drawCenteredString("Enter amount:", 259, 60, 0);
			client.boldFont.drawCenteredString(client.amountOrNameInput + client.outBoundInput + "*", 259, 80, 128);
		} else if(client.inputDialogState == 2) {
			client.boldFont.drawCenteredString("Enter name:", 259, 60, 0);
			client.boldFont.drawCenteredString(client.amountOrNameInput + client.outBoundInput + "*", 259, 80, 128);
		} else if(client.chatBoxStatement != null) {
			client.boldFont.drawCenteredString(client.chatBoxStatement, 259, 60, 0);
			client.boldFont.drawCenteredString("Click to continue", 259, 80, 128);
		} else if(client.forcedChatWidgetId != -1) {
			client.drawWidget(Interface.cache[client.forcedChatWidgetId], 20, 20, 0, UIComponent.CHAT);
		} else if(client.chatWidgetId != -1) {
			client.drawWidget(Interface.cache[client.chatWidgetId], 20, 20, 0, UIComponent.CHAT);
		} else if(client.bankSearching) {
			client.boldFont.drawCenteredString("What are you looking for?", 259, 60, 0);
			client.boldFont.drawCenteredString(client.bankSearch + "*", 259, 80, 128);
		} else {
			int msgpos = 0;
			Rasterizer2D.setClip(0, 20, 463, 96);
			final int basicFontColor = 0;
			final int blueFontColor = 255;
			final int redFontColor = 0x800000;
			final int purpleFontColor = 0x800080;
			final int orangeFontColor = 0x7e3200;
			final int view = client.chatTypeView;
			for(int i = 0; i < 100; i++) {
				if(client.chatMessage[i] != null) {
					int x = 19;
					final int yPos = (90 - 14 * msgpos) + client.chatScrollPos;
					if(yPos <= 0 && yPos >= 100) {
						msgpos++;
						continue;
					}
					final int type = client.chatType[i];
					String author = client.chatAuthor[i];
					final String message = client.chatMessage[i];
					int rights = client.chatPriv[i];
					if(!client.uiRenderer.canSeeMessage(type, view, rights, author)) {
						continue;
					}
					if(type == 0) {
						client.plainFont.drawLeftAlignedEffectString(message, x, yPos, basicFontColor, false);
					} else if(type == 1) {
						if(rights >= 1) {
							ImageCache.get(1984 + rights - 1).drawImage(x + 1, yPos - 12);
							x += 14;
						}
						client.plainFont.drawLeftAlignedString(author + ":", x, yPos, basicFontColor);
						x += client.plainFont.getStringWidth(author + ": ");
						client.plainFont.drawLeftAlignedString(message, x, yPos, blueFontColor);
					} else if(type == 2) {
						client.plainFont.drawLeftAlignedString("From", x, yPos, redFontColor);
						x += client.plainFont.getStringWidth("From ");
						if(rights >= 1) {
							ImageCache.get(1984 + rights - 1).drawImage(x, yPos - 12);
							x += 12;
						}
						client.plainFont.drawLeftAlignedString(author + ":", x, yPos, redFontColor);
						x += client.plainFont.getStringWidth(author) + 8;
						client.plainFont.drawLeftAlignedString(client.chatMessage[i], x, yPos, redFontColor);
					} else if(type == 4) {
						client.plainFont.drawLeftAlignedString(author + " " + message, x, yPos, purpleFontColor);
					} else if(type == 5) {
						client.plainFont.drawLeftAlignedString(message, x, yPos, redFontColor);
					} else if(type == 6) {
						client.plainFont.drawLeftAlignedString("To " + author + ": " + message, x, yPos, redFontColor);
					} else if(type == 7) {
						final int split = author.indexOf(":");
						final String clan = author.substring(0, split);
						author = author.substring(split + 1);
						client.plainFont.drawLeftAlignedString("[", x, yPos, 0);
						x += 5;
						client.plainFont.drawLeftAlignedString(clan, x, yPos, 255);
						x += client.plainFont.getStringWidth(clan);
						client.plainFont.drawLeftAlignedString("]", x, yPos, 0);
						x += 7;
						if(rights >= 1) {
							ImageCache.get(1984 + rights - 1).drawImage(x, yPos - 12);
							x += 13;
						}
						client.plainFont.drawLeftAlignedString(author + ":", x, yPos, 0);
						x += client.plainFont.getStringWidth(author) + 6;
						client.plainFont.drawLeftAlignedString(message, x, yPos, 0x800000);
					} else if(type == 8) {
						client.plainFont.drawLeftAlignedString(author + " " + message, x, yPos, orangeFontColor);
					} else if(type == 9) {
						final int split = author.indexOf(":");
						author = author.substring(split + 1);
						if(rights >= 1) {
							ImageCache.get(1984 + rights - 1).drawImage(x, yPos - 12);
							x += 13;
						}
						client.plainFont.drawLeftAlignedString(author + ":", x, yPos, 0);
						x += client.plainFont.getStringWidth(author) + 6;
						client.plainFont.drawLeftAlignedString(message, x, yPos, 0x235148);
					}
					msgpos++;
				}
			}
			Rasterizer2D.removeClip();
			client.chatContentHeight = msgpos * 14 + 7 + 5;
			if(client.chatContentHeight < 78) {
				client.chatContentHeight = 78;
			}
			client.gameActivity.drawOldScrollbar(480, 19, 77, client.chatContentHeight, client.chatContentHeight - client.chatScrollPos - 77);
			String myName;
			if(client.localPlayer != null && client.localPlayer.name != null) {
				myName = client.localPlayer.name;
			} else {
				myName = StringUtils.formatName(client.localUsername);
			}
			client.plainFont.drawLeftAlignedString(myName + ":", 19, 110, 0);
			client.plainFont.drawLeftAlignedString(client.chatInput + "*", 20 + client.plainFont.getStringWidth(myName + ": "), 110, 255);
			Rasterizer2D.drawHorizontalLine(17, 96, 479, 0x000000);
		}
		if(client.menuOpened) {
			client.gameActivity.drawer.drawMenu(0, -338, false);
		}
		client.chatGraphics.drawGraphics(0, 338, client.graphics);
		client.gameGraphics.setCanvas();
		Rasterizer3D.viewport = client.gameAreaViewport;
	}

	@Override
	public void buildMap() {
		if(client.mouseInRegion(540, 3, 578, 36)) {
			client.menuItemName[1] = "Face North";
			client.menuItemCode[1] = 1014;
			client.menuPos = 2;
		} else if(Config.def.orbs() && client.mouseInRegion(543, 126, 600, 158)) {
			client.menuItemName[1] = "Run";
			client.menuItemCode[1] = 1051;
			client.menuPos = 2;
		} else if(Config.def.orbs() && client.mouseInRegion(518, 91, 574, 122)) {
			client.menuItemName[client.menuPos] = "Select quick prayers";
			client.menuItemCode[client.menuPos] = 1054;
			client.menuPos++;
			client.menuItemName[client.menuPos] = "Turn quick prayers " + (OrbHandler.prayersEnabled ? "off" : "on");
			client.menuItemCode[client.menuPos] = 1053;
			client.menuPos++;
		} else if(client.mouseInRegion(519, 26, 544, 52)) {
			client.menuItemName[client.menuPos] = "Reset counter";
			client.menuItemCode[client.menuPos] = 1056;
			client.menuPos++;
			client.menuItemName[client.menuPos] = "Turn counter " + (CounterHandler.isCounterOn() ? "off" : "on");
			client.menuItemCode[client.menuPos] = 1055;
			client.menuPos++;
		}
	}

	@Override
	public void updateMap() {
		client.mapGraphics.setCanvas();
		if(client.minimapOverlay == 2) {
			Rasterizer2D.fillRectangle(0, 0, 246, 168, 0);
			ImageCache.get(70).drawImage(0, 0);
			ImageCache.get(69).drawImage(26, 4);
			ImageCache.get(40).drawImage(0, 4);
			ImageCache.get(1700).drawAffineTransformedImage(26, 0, 33, 33, 25, 25, client.compassClipStarts, client.compassLineLengths, client.cameraAngleX, 256);
			if(Config.def.orbs()) {
				displayOrb(-2, 42, Constants.ORB_HEALTH, false);
				displayOrb(-2, 87, Constants.ORB_PRAYER, true);
				displayOrb(24, 122, Constants.ORB_RUN, true);
				displaySpecialOrb(178, 117, Constants.ORB_SUMMONING, true);
			}
			ImageCache.get(1955).drawImage(-2, 22);
			if(client.mouseInRegion(519, 26, 544, 52)) {
				ImageCache.get(1956).drawImage(-2, 22);
			}
			client.mapGraphics.drawGraphics(519, 0, client.graphics);
			client.gameGraphics.setCanvas();
			return;
		}
		final int rotation = client.cameraAngleX + client.minimapAngle & 0x7ff;
		final int middleX = 48 + client.localPlayer.x / 32;
		final int middleY = 464 - client.localPlayer.y / 32;
		client.minimapImage.drawAffineTransformedImage(51, 9, 146, 151, middleX, middleY, client.minimapLineStarts, client.minimapLineLengths, rotation, 256 + client.minimapZoom);
		ImageCache.get(192).drawAffineTransformedImage(26, 4, 33, 33, 25, 25, client.compassClipStarts, client.compassLineLengths, client.cameraAngleX, 256);
		for(int j5 = 0; j5 < client.mapFunctionCount; j5++) {
			final int x = client.mapFunctionX[j5] * 4 + 2 - client.localPlayer.x / 32;
			final int y = client.mapFunctionY[j5] * 4 + 2 - client.localPlayer.y / 32;
			markMinimap(client.mapFunctionImage[j5], x, y);
		}
		for(int k5 = 0; k5 < 104; k5++) {
			for(int l5 = 0; l5 < 104; l5++) {
				final LinkedDeque class19 = client.sceneItems[client.cameraPlane][k5][l5];
				if(class19 != null) {
					final int x = k5 * 4 + 2 - client.localPlayer.x / 32;
					final int y = l5 * 4 + 2 - client.localPlayer.y / 32;
					markMinimap(client.mapDotItem, x, y);
				}
			}
		}
		for(int i = 0; i < client.npcListSize; i++) {
			final NPC npc = client.npcList[client.npcEntryList[i]];
			if(npc != null && npc.isVisible()) {
				NPCType entityDef = npc.type;
				if(entityDef.childrenIDs != null) {
					entityDef = entityDef.getSubNPCType();
				}
				if(entityDef != null && entityDef.visibleMinimap && entityDef.clickable) {
					final int x = npc.x / 32 - client.localPlayer.x / 32;
					final int y = npc.y / 32 - client.localPlayer.y / 32;
					markMinimap(client.mapDotNPC, x, y);
				}
			}
		}
		for(int i = 0; i < client.playerCount; i++) {
			final Player player = client.playerList[client.playerEntryList[i]];
			if(player != null && player.isVisible()) {
				final int x = player.x / 32 - client.localPlayer.x / 32;
				final int y = player.y / 32 - client.localPlayer.y / 32;
				boolean friend = false;
				final long l6 = StringUtils.encryptName(player.name);
				for(int k6 = 0; k6 < client.friendsCount; k6++) {
					if(l6 != client.friendsListAsLongs[k6] || client.friendsNodeIDs[k6] == 0) {
						continue;
					}
					friend = true;
					break;
				}
				boolean team = false;
				if(client.localPlayer.team != 0 && player.team != 0 && client.localPlayer.team == player.team) {
					team = true;
				}
				if(friend) {
					markMinimap(client.mapDotFriend, x, y);
				} else if(team) {
					markMinimap(client.mapDotTeam, x, y);
				} else if(player.iron) {
					markMinimap(client.mapDotIronman, x, y);
				} else {
					markMinimap(client.mapDotPlayer, x, y);
				}
			}
		}
		if(client.hintType != 0 && client.loopCycle % 20 < 10) {
			if(client.hintType == 1 && client.NPCHintID >= 0 && client.NPCHintID < client.npcList.length) {
				final NPC npc = client.npcList[client.NPCHintID];
				if(npc != null) {
					final int x = npc.x / 32 - client.localPlayer.x / 32;
					final int y = npc.y / 32 - client.localPlayer.y / 32;
					method81(client.mapArrow, x, y);
				}
			}
			if(client.hintType == 2) {
				final int x = (client.anInt934 - client.baseX) * 4 + 2 - client.localPlayer.x / 32;
				final int y = (client.anInt935 - client.baseY) * 4 + 2 - client.localPlayer.y / 32;
				method81(client.mapArrow, x, y);
			}
			if(client.hintType == 10 && client.anInt933 >= 0 && client.anInt933 < client.playerList.length) {
				final Player player = client.playerList[client.anInt933];
				if(player != null) {
					final int x = player.x / 32 - client.localPlayer.x / 32;
					final int y = player.y / 32 - client.localPlayer.y / 32;
					method81(client.mapArrow, x, y);
				}
			}
		}
		if(client.walkX != 0) {
			final int x = client.walkX * 4 + 2 - client.localPlayer.x / 32;
			final int y = client.walkY * 4 + 2 - client.localPlayer.y / 32;
			markMinimap(client.mapFlag, x, y);
		}
		Rasterizer2D.removeClip();
		Rasterizer2D.fillRectangle(123, 78, 3, 3, 0xffffff);
		ImageCache.get(70).drawImage(0, 0);
		ImageCache.get(69).drawImage(26, 4);
		ImageCache.get(40).drawImage(0, 4);
		if(Config.def.orbs()) {
			displayOrb(-2, 42, Constants.ORB_HEALTH, false);
			displayOrb(-2, 87, Constants.ORB_PRAYER, true);
			displayOrb(24, 122, Constants.ORB_RUN, true);
			displaySpecialOrb(178, 117, Constants.ORB_SUMMONING, true);
		}
		ImageCache.get(1955).drawImage(-2, 22);
		if(client.mouseInRegion(519, 26, 544, 52)) {
			ImageCache.get(1956).drawImage(-2, 22);
		}
		if(client.menuOpened) {
			client.gameActivity.drawer.drawMenu(-519, 0, false);
		}
		client.mapGraphics.drawGraphics(519, 0, client.graphics);
		client.gameGraphics.setCanvas();
	}

	@Override
	public void buildInventory() {
		if(client.mouseWheelAmt != 0 && client.olderTabInterfaces[client.invTab] != -1) {
			Interface tab = Interface.cache[client.olderTabInterfaces[client.invTab]];
			if(tab.subId != null) {
				int posX = 0;
				int posY = 0;
				Interface widget = null;
				for(int index = 0; index < tab.subId.length; index++) {
					if(Interface.cache[tab.subId[index]].scrollMax > 0) {
						posX = tab.subX[index] + 553;
						posY = tab.subY[index] + 205;
						widget = Interface.cache[tab.subId[index]];
						break;
					}
				}
				if(widget != null && client.mouseWheelX > posX && client.mouseWheelX < posX + widget.width + 16 &&
						client.mouseWheelY > posY && client.mouseWheelY < posY + widget.height) {
					widget.scrollPos += client.mouseWheelAmt * 30;
					if(widget.scrollPos < 0) {
						widget.scrollPos = 0;
					} else if(widget.scrollPos > widget.scrollMax) {
						widget.scrollPos = widget.scrollMax;
					}
				}
			}
		}
		if(client.clickButton != 1) {
			return;
		}
		final short[][] tabClickPositions317 = {{539, 570, 171, 205}, {571, 598, 171, 205}, {599, 626, 171, 205}, {627, 670, 171, 203}, {671, 697, 171, 205}, {698, 725, 171, 205}, {726, 756, 171, 205}, {537, 569, 460, 213}, {570, 598, 464, 503}, {599, 625, 464, 503}, {626, 669, 467, 503}, {670, 698, 464, 503}, {699, 725, 464, 503}, {726, 755, 461, 503}};
		for(int i = 0; i < 14; i++) {
			if(client.clickX >= tabClickPositions317[i][0] && client.clickX <= tabClickPositions317[i][1] && client.clickY >= tabClickPositions317[i][2] && client.clickY < tabClickPositions317[i][3] && (client.olderTabInterfaces[i] != -1 || SettingPanel.selectedBinding != -1)) {
				if(SettingPanel.selectedBinding != -1) {
					SettingPanel.hotkeys[SettingPanel.selectedBinding] = i;
					SettingPanel.selectedBinding = -1;
					return;
				}
				client.invTab = i;
				client.updateInventory = true;
			}
		}
	}

	@Override
	public void updateInventory() {
		client.inventoryGraphics.setCanvas();
		Rasterizer3D.viewport = client.tabAreaViewport;
		ImageCache.get(190).drawImage(0, 0);
		if(client.invOverlayInterfaceID == -1) {
			displaySelectedTab();
			displaySideIcons();
		}
		if(client.invOverlayInterfaceID != -1) {
			client.drawWidget(Interface.cache[client.invOverlayInterfaceID], 34, 37, 0, UIComponent.INVENTORY);
		} else if(client.olderTabInterfaces[client.invTab] != -1) {
			client.drawWidget(Interface.cache[client.olderTabInterfaces[client.invTab]], 34, 37, 0, UIComponent.INVENTORY);
		}
		if(client.menuOpened) {
			client.gameActivity.drawer.drawMenu(-519, -168, false);
		}
		client.inventoryGraphics.drawGraphics(519, 168, client.graphics);
		client.gameGraphics.setCanvas();
		Rasterizer3D.viewport = client.gameAreaViewport;
	}

	@Override
	public void buildSceneOverlay() {
	}

	@Override
	public void updateSceneOverlay() {
		ImageCache.get(55).drawImage(0, 0);
	}

	public void markMinimap(BitmapImage icon, int x, int y) {
		if(icon == null)
			return;
		final int rotation = client.cameraAngleX + client.minimapAngle & 0x7ff;
		final int z = x * x + y * y;
		if(z > 6400) {
			return;
		}
		int sin = Model.angleSine[rotation];
		int cos = Model.angleCosine[rotation];
		sin = (sin << 8) / (client.minimapZoom + 256);
		cos = (cos << 8) / (client.minimapZoom + 256);
		final int fx = y * sin + x * cos >> 16;
		final int fy = y * cos - x * sin >> 16;
		icon.drawImage(94 + fx - icon.imageOriginalWidth / 2 + 30, 83 - fy - icon.imageOriginalHeight / 2 - 8 + 8);
	}

	public void method81(BitmapImage icon, int x, int y) {
		int xOffset = 26;
		markMinimap(icon, xOffset + x, y);
	}

	/**
	 * Displays the chat channel buttons.
	 */
	private void displayChannelButtons() {
		final String text[] = {"On", "Friends", "Off", "Hide"};
		final int textColor[] = {65280, 0xffff00, 0xff0000, 65535};
		client.plainFont.drawLeftAlignedEffectString("Report Abuse", 422, 147, 0xffffff, true);
		client.plainFont.drawLeftAlignedEffectString("Public chat", 25, 142, 0xffffff, true);
		client.plainFont.drawLeftAlignedEffectString("Private chat", 150, 142, 0xffffff, true);
		client.plainFont.drawLeftAlignedEffectString("Trade/compete", 285, 142, 0xffffff, true);
		client.plainFont.drawCenteredEffectString(text[client.publicChatMode], 55, 153, textColor[client.publicChatMode], true);
		client.plainFont.drawCenteredEffectString(text[client.privateChatMode], 180, 153, textColor[client.privateChatMode], true);
		client.plainFont.drawCenteredEffectString(text[client.tradeMode], 325, 153, textColor[client.tradeMode], true);
	}
	
	/**
	 * Displays orbs components.
	 */
	private void displayOrb(int x, int y, int orb, boolean hover) {
		ImageCache.get(hover && client.mouseInRegion(x + 519, y + 4, x + 576, y + 37) ? 1922 : 1921).drawImage(x, y);
		client.smallFont.drawCenteredEffectString(OrbHandler.getValue(orb), x + 15, y + 26, OrbHandler.getColor(orb), true);
		ImageCache.get(OrbHandler.getOrb(orb)).drawImage(x + 27, y + 3);
		Rasterizer2D.setClip(x + 27, y + 3, x + 54, y + 3 + OrbHandler.getFill(orb, 27));
		ImageCache.get(60).drawImage(x + 27, y + 3);
		Rasterizer2D.removeClip();
		if(orb != Constants.ORB_HEALTH || OrbHandler.getPercent(orb) > 20 || OrbHandler.getPercent(orb) < 1 || client.loopCycle % 20 > 10) {
			ImageCache.get(orb == Constants.ORB_RUN && OrbHandler.runEnabled ? 74 : 61 + orb).drawImage(x + 41 - ImageCache.get(orb == Constants.ORB_RUN && OrbHandler.runEnabled ? 74 : 61 + orb).imageWidth / 2, y + 17 - ImageCache.get(orb == Constants.ORB_RUN && OrbHandler.runEnabled ? 74 : 61 + orb).imageHeight / 2);
		}
	}
	
	/**
	 * Displays special orb component.
	 */
	private void displaySpecialOrb(int x, int y, int orb, boolean hover) {
		ImageCache.get(hover && client.mouseInRegion(x + 519, y + 4, x + 576, y + 37) ? 1924 : 1923).drawImage(x, y);
		client.smallFont.drawCenteredEffectString(OrbHandler.getValue(orb), x + 43, y + 26, OrbHandler.getColor(orb), true);
		ImageCache.get(orb == Constants.ORB_RUN && OrbHandler.runEnabled ? 73 : orb == Constants.ORB_HEALTH && OrbHandler.poisoned ? 78 : 56 + orb).drawImage(x + 4, y + 4);
		Rasterizer2D.setClip(x + 3, y + 3, x + 30, y + 3 + OrbHandler.getFill(orb, 27));
		ImageCache.get(60).drawImage(x + 3, y + 3);
		Rasterizer2D.removeClip();
		if(orb != Constants.ORB_HEALTH || OrbHandler.getPercent(orb) > 20 || OrbHandler.getPercent(orb) < 1 || client.loopCycle % 20 > 10) {
			ImageCache.get(orb == Constants.ORB_RUN && OrbHandler.runEnabled ? 74 : 61 + orb).drawImage(x + 17 - ImageCache.get(orb == Constants.ORB_RUN && OrbHandler.runEnabled ? 74 : 61 + orb).imageWidth / 2, y + 17 - ImageCache.get(orb == Constants.ORB_RUN && OrbHandler.runEnabled ? 74 : 61 + orb).imageHeight / 2);
		}
	}

	/**
	 * Displays the selected tab.
	 */
	private void displaySelectedTab() {
		if(client.olderTabInterfaces[client.invTab] == -1) {
			return;
		}
		if(client.invTab < 0 && client.invTab > 13) {
			return;
		}
		if(client.invTab == 13) // Removed music redstone.
			return;
		final short[][] data = {{0, 20, 1}, {1, 51, 1}, {1, 78, 0}, {2, 107, 0}, {1, 148, 1}, {1, 176, 1}, {3, 206, 1}, {2, 3, 298}, {1, 50, 297}, {1, 78, 298}, {2, 107, 300}, {1, 149, 297}, {1, 176, 298}, {3, 206, 298}};
		ImageCache.get(206 + data[client.invTab][0]).drawImage(data[client.invTab][1], data[client.invTab][2]);
	}

	/**
	 * Displays the side icons.
	 */
	private void displaySideIcons() {
		ImageCache.get(193).drawImage(30, 11);
		ImageCache.get(194).drawImage(54, 7);
		ImageCache.get(195).drawImage(83, 7);
		ImageCache.get(196).drawImage(113, 5);
		ImageCache.get(197).drawImage(153, 6);
		ImageCache.get(198).drawImage(181, 6);
		ImageCache.get(199).drawImage(208, 10);
		ImageCache.get(200).drawImage(54, 304);
		ImageCache.get(201).drawImage(82, 304);
		ImageCache.get(202).drawImage(116, 306);
		ImageCache.get(203).drawImage(153, 304);
		ImageCache.get(204).drawImage(185, 304);
		ImageCache.get(205).drawImage(210, 304);
	}
}
