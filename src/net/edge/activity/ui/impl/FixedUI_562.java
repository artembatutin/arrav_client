package net.edge.activity.ui.impl;

import net.edge.Constants;
import net.edge.activity.panel.impl.SettingPanel;
import net.edge.activity.ui.UIComponent;
import net.edge.activity.ui.util.OrbHandler;
import net.edge.activity.ui.util.CounterHandler;
import net.edge.game.model.Model;
import net.edge.game.model.Player;
import net.edge.media.Rasterizer2D;
import net.edge.Config;
import net.edge.cache.unit.ImageCache;
import net.edge.cache.unit.Interface;
import net.edge.cache.unit.NPCType;
import net.edge.game.model.NPC;
import net.edge.media.Rasterizer3D;
import net.edge.media.img.BitmapImage;
import net.edge.util.collect.LinkedDeque;
import net.edge.util.string.StringUtils;

public class FixedUI_562 extends FixedUI {

	@Override
	public void buildChat() {
		final String[] modes = {"View", "On", "Friends", "Off", "Hide"};
		if(client.mouseX >= 5 && client.mouseX <= 61 && client.mouseY >= 482 && client.mouseY <= 503) {
			client.menuItemName[1] = "View All";
			client.menuItemCode[1] = 999;
			client.menuPos = 2;
			client.hoveredChannelButton = 0;
		} else if(client.mouseX >= 62 && client.mouseX <= 118 && client.mouseY >= 482 && client.mouseY <= 503) {
			client.menuItemName[1] = "View Game";
			client.menuItemCode[1] = 998;
			client.menuPos = 2;
			client.hoveredChannelButton = 1;
		} else if(client.mouseX >= 119 && client.mouseX <= 175 && client.mouseY >= 482 && client.mouseY <= 503) {
			for(int i = 0; i <= 4; i++) {
				client.menuItemName[i + 1] = modes[4 - i] + " public";
				client.menuItemCode[i + 1] = 997 - i;
			}
			client.menuPos = 6;
			client.hoveredChannelButton = 2;
		} else if(client.mouseX >= 176 && client.mouseX <= 232 && client.mouseY >= 482 && client.mouseY <= 503) {
			for(int i = 0; i <= 3; i++) {
				client.menuItemName[i + 1] = modes[3 - i] + " private";
				client.menuItemCode[i + 1] = 992 - i;
			}
			client.menuPos = 5;
			client.hoveredChannelButton = 3;
		} else if(client.mouseX >= 233 && client.mouseX <= 289 && client.mouseY >= 482 && client.mouseY <= 503) {
			for(int i = 0; i <= 3; i++) {
				client.menuItemName[i + 1] = modes[3 - i] + " clan chat";
				client.menuItemCode[i + 1] = 1003 - i;
			}
			client.menuPos = 5;
			client.hoveredChannelButton = 4;
		} else if(client.mouseX >= 290 && client.mouseX <= 344 && client.mouseY >= 482 && client.mouseY <= 503) {
			for(int i = 0; i <= 3; i++) {
				client.menuItemName[i + 1] = modes[3 - i] + " trade";
				client.menuItemCode[i + 1] = 987 - i;
			}
			client.menuPos = 5;
			client.hoveredChannelButton = 5;
		} else if(client.mouseX >= 347 && client.mouseX <= 402 && client.mouseY >= 482 && client.mouseY <= 503) {
			for(int i = 0; i <= 3; i++) {
				client.menuItemName[i + 1] = modes[3 - i] + " yell";
				client.menuItemCode[i + 1] = 887 - i;
			}
			client.menuPos = 5;
			client.hoveredChannelButton = 6;
		} else if(client.mouseX >= 404 && client.mouseX <= 514 && client.mouseY >= 482 && client.mouseY <= 503) {
			client.hoveredChannelButton = 7;
		} else if(client.hoveredChannelButton != -1) {
			client.hoveredChannelButton = -1;
		}
		if(client.forcedChatWidgetId == -1) {
			client.chatWidget.scrollPos = client.chatContentHeight - client.chatScrollPos - 114;
			if(client.mouseX > 478 && client.mouseX < 580 && client.mouseY > 342) {
				client.gameActivity.processScrollbar(494, 10, client.mouseX, client.mouseY - 338, 114, client.chatContentHeight, client.chatWidget);
			}
			int pos = client.chatContentHeight - 114 - client.chatWidget.scrollPos;
			if(client.mouseWheelAmt != 0 && client.mouseWheelX >= 7 && client.mouseWheelX <= 495 &&
					client.mouseWheelY >= 345 && client.mouseWheelY <= 458) {
				pos -= 30 * client.mouseWheelAmt;
			}
			if(pos < 0) {
				pos = 0;
			}
			if(pos > client.chatContentHeight - 114) {
				pos = client.chatContentHeight - 114;
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
		ImageCache.get(17).drawImage(0, 0);
		displayChannelButtons();
		if(client.messagePromptRaised) {
			client.boldFont.drawCenteredString(client.promptInputTitle, 259, 60, 0);
			client.boldFont.drawCenteredString(client.promptInput + "*", 259, 80, 128);
		} else if(client.inputDialogState == 1) {
			client.boldFont.drawCenteredString(client.promptInputTitle, 259, 60, 0);
			client.boldFont.drawCenteredString(client.amountOrNameInput + client.outBoundInput + "*", 259, 80, 128);
		} else if(client.inputDialogState == 2) {
			client.boldFont.drawCenteredString(client.promptInputTitle, 259, 60, 0);
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
			int line = 0;
			Rasterizer2D.setClip(8, 7, 497, 122);
			final int basicFontColor = 0;
			final int blueFontColor = 255;
			final int redFontColor = 0x800000;
			final int purpleFontColor = 0x800080;
			final int orangeFontColor = 0x7e3200;
			final int view = client.chatTypeView;
			for(int i = 0; i < 500; i++) {
				if(client.chatMessage[i] != null) {
					int x = 9;
					final int y = (115 - 14 * line) + client.chatScrollPos;
					if(y <= 0 && y >= 210) {
						line++;
						continue;
					}
					final int type = client.chatType[i];
					String author = client.chatAuthor[i];
					final String msg = client.chatMessage[i];
					int rights = client.chatPriv[i];
					if(!client.uiRenderer.canSeeMessage(type, view, rights, author)) {
						continue;
					}
					if(type == 0) {
						client.plainFont.drawLeftAlignedEffectString(msg, x, y, basicFontColor, false);
					} else if(type == 1) {
						if(rights >= 1) {
							ImageCache.get(1984 + rights - 1).drawImage(x + 1, y - 12);
							x += 14;
						}
						client.plainFont.drawLeftAlignedString(author + ":", x, y, basicFontColor);
						x += client.plainFont.getStringWidth(author + ": ");
						client.plainFont.drawLeftAlignedString(msg, x, y, blueFontColor);
					} else if(type == 2) {
						client.plainFont.drawLeftAlignedString("From", x, y, redFontColor);
						x += client.plainFont.getStringWidth("From ");
						if(rights >= 1) {
							ImageCache.get(1984 + rights - 1).drawImage(x, y - 12);
							x += 12;
						}
						client.plainFont.drawLeftAlignedString(author + ":", x, y, redFontColor);
						x += client.plainFont.getStringWidth(author) + 8;
						client.plainFont.drawLeftAlignedString(client.chatMessage[i], x, y, redFontColor);
					} else if(type == 4) {
						client.plainFont.drawLeftAlignedString(author + " " + msg, x, y, purpleFontColor);
					} else if(type == 5) {
						client.plainFont.drawLeftAlignedString(msg, x, y, redFontColor);
					} else if(type == 6) {
						client.plainFont.drawLeftAlignedString("To " + author + ": " + msg, x, y, redFontColor);
					} else if(type == 7) {
						final int split = author.indexOf(":");
						final String clan = author.substring(0, split);
						author = author.substring(split + 1);
						client.plainFont.drawLeftAlignedString("[", x, y, 0);
						x += 5;
						client.plainFont.drawLeftAlignedString(clan, x, y, 255);
						x += client.plainFont.getStringWidth(clan);
						client.plainFont.drawLeftAlignedString("]", x, y, 0);
						x += 7;
						if(rights >= 1) {
							ImageCache.get(1984 + rights - 1).drawImage(x, y - 12);
							x += 13;
						}
						client.plainFont.drawLeftAlignedString(author + ":", x, y, 0);
						x += client.plainFont.getStringWidth(author) + 6;
						client.plainFont.drawLeftAlignedString(msg, x, y, 0x800000);
					} else if(type == 8) {
						client.plainFont.drawLeftAlignedString(author + " " + msg, x, y, orangeFontColor);
					} else if(type == 9) {
						final int split = author.indexOf(":");
						author = author.substring(split + 1);
						if(rights >= 1) {
							ImageCache.get(1984 + rights - 1).drawImage(x, y - 12);
							x += 13;
						}
						client.plainFont.drawLeftAlignedString(author + ":", x, y, 0);
						x += client.plainFont.getStringWidth(author) + 6;
						client.plainFont.drawLeftAlignedString(msg, x, y, 0x235148);
					}
					line++;
				}
			}
			Rasterizer2D.removeClip();
			client.chatContentHeight = line * 14 + 7;
			if(client.chatContentHeight < 114) {
				client.chatContentHeight = 114;
			}
			client.gameActivity.drawScrollbar(496, 7, 114, client.chatContentHeight, client.chatContentHeight - client.chatScrollPos - 114);
			String myName;
			if(client.localPlayer != null && client.localPlayer.name != null) {
				myName = client.localPlayer.name;
			} else {
				myName = StringUtils.formatName(client.localUsername);
			}
			client.plainFont.drawLeftAlignedString(myName + ":", 9, 133, 0);
			client.plainFont.drawLeftAlignedString(client.chatInput + "*", 10 + client.plainFont.getStringWidth(myName + ": "), 133, 255);
			Rasterizer2D.drawHorizontalLine(7, 121, 506, 0x807660);
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
		if(client.mouseX >= 531 && client.mouseX <= 557 && client.mouseY >= 7 && client.mouseY <= 40) {
			client.menuItemName[1] = "Face North";
			client.menuItemCode[1] = 1014;
			client.menuPos = 2;
		} else if(Config.def.isDRAW_ORBS() && client.mouseX > 705 && client.mouseX < 761 && client.mouseY > 90 && client.mouseY < 123) {
			client.menuItemName[1] = "Run";
			client.menuItemCode[1] = 1051;
			client.menuPos = 2;
		} else if(client.mouseX > client.windowWidth - 21 && client.mouseX < client.windowWidth && client.mouseY > 0 && client.mouseY < 21) {
			client.menuItemName[1] = "Logout";
			client.menuItemCode[1] = 1052;
			client.menuPos = 2;
		} else if(Config.def.isDRAW_ORBS() && client.mouseX > 704 && client.mouseX < 761 && client.mouseY > 51 && client.mouseY < 84) {
			client.menuItemName[client.menuPos] = "Select quick prayers";
			client.menuItemCode[client.menuPos] = 1054;
			client.menuPos++;
			client.menuItemName[client.menuPos] = "Turn quick prayers " + (OrbHandler.prayersEnabled ? "off" : "on");
			client.menuItemCode[client.menuPos] = 1053;
			client.menuPos++;
		} else if(client.mouseInRegion(526, 127, 562, 159)) {
			client.menuItemName[1] = "Check wilderness";
			client.menuItemCode[1] = 1050;
			client.menuPos = 2;
		} else if(client.mouseInRegion(521, 54, 550, 81)) {
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
		for(int i = 0; i < 76; i++) {
			final int amt = (int) Math.sqrt(Math.pow(77, 2) - Math.pow(75 - i, 2));
			client.minimapLineLengths[i] = 2 * amt + 2;
			client.minimapLineLengths[150 - i] = 2 * amt + 2;
			client.minimapLineStarts[i] = -amt + 73;
			client.minimapLineStarts[150 - i] = -amt + 73;
		}
		client.mapGraphics.setCanvas();
		if(client.minimapOverlay == 2) {
			Rasterizer2D.fillRectangle(0, 0, 246, 168, 0);
			ImageCache.get(18).drawImage(0, 0);
			ImageCache.get(1700).drawAffineTransformedImage(8, 8, 33, 33, 25, 25, client.compassClipStarts, client.compassLineLengths, client.cameraAngleX, 256);
			if(Config.def.isDRAW_ORBS()) {
				displayOrb(164, 9, Constants.ORB_HEALTH, false);
				displayOrb(185, 47, Constants.ORB_PRAYER, true);
				displayOrb(185, 86, Constants.ORB_RUN, true);
				displayOrb(170, 125, Constants.ORB_SUMMONING, true);
			}
			ImageCache.get(1950).drawImage(-2, 47);
			if(client.mouseInRegion(521, 54, 550, 81)) {
				ImageCache.get(1951).drawImage(-2, 47);
			}
			displayLogout();
			client.mapGraphics.drawGraphics(519, 0, client.graphics);
			client.gameGraphics.setCanvas();
			return;
		}
		final int rotation = client.cameraAngleX + client.minimapAngle & 0x7ff;
		final int middleX = 48 + client.localPlayer.x / 32;
		final int middleY = 464 - client.localPlayer.y / 32;
		client.minimapImage.drawAffineTransformedImage(35, 9, 146, 151, middleX, middleY, client.minimapLineStarts, client.minimapLineLengths, rotation, 256 + client.minimapZoom);
		for(int j5 = 0; j5 < client.mapFunctionCount; j5++) {
			final int k = client.mapFunctionX[j5] * 4 + 2 - client.localPlayer.x / 32;
			final int i3 = client.mapFunctionY[j5] * 4 + 2 - client.localPlayer.y / 32;
			markMinimap(client.mapFunctionImage[j5], k, i3);
		}
		for(int k5 = 0; k5 < 104; k5++) {
			for(int l5 = 0; l5 < 104; l5++) {
				final LinkedDeque class19 = client.sceneItems[client.cameraPlane][k5][l5];
				if(class19 != null) {
					final int l = k5 * 4 + 2 - client.localPlayer.x / 32;
					final int j3 = l5 * 4 + 2 - client.localPlayer.y / 32;
					markMinimap(client.mapDotItem, l, j3);
				}
			}
		}
		for(int i6 = 0; i6 < client.npcListSize; i6++) {
			final NPC npc = client.npcList[client.npcEntryList[i6]];
			if(npc != null && npc.isVisible()) {
				NPCType entityDef = npc.type;
				if(entityDef.childrenIDs != null) {
					entityDef = entityDef.getSubNPCType();
				}
				if(entityDef != null && entityDef.visibleMinimap && entityDef.clickable) {
					final int i1 = npc.x / 32 - client.localPlayer.x / 32;
					final int k3 = npc.y / 32 - client.localPlayer.y / 32;
					markMinimap(client.mapDotNPC, i1, k3);
				}
			}
		}
		for(int j6 = 0; j6 < client.playerCount; j6++) {
			final Player player = client.playerList[client.playerEntryList[j6]];
			if(player != null && player.isVisible()) {
				final int x = player.x / 32 - client.localPlayer.x / 32;
				final int y = player.y / 32 - client.localPlayer.y / 32;
				boolean flag1 = false;
				final long l6 = StringUtils.encryptName(player.name);
				for(int k6 = 0; k6 < client.friendsCount; k6++) {
					if(l6 != client.friendsListAsLongs[k6] || client.friendsNodeIDs[k6] == 0) {
						continue;
					}
					flag1 = true;
					break;
				}
				boolean flag2 = false;
				if(client.localPlayer.team != 0 && player.team != 0 && client.localPlayer.team == player.team) {
					flag2 = true;
				}
				if(flag1) {
					markMinimap(client.mapDotFriend, x, y);
				} else if(flag2) {
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
					final int k1 = npc.x / 32 - client.localPlayer.x / 32;
					final int i4 = npc.y / 32 - client.localPlayer.y / 32;
					method81(client.mapArrow, k1, i4);
				}
			}
			if(client.hintType == 2) {
				final int l1 = (client.anInt934 - client.baseX) * 4 + 2 - client.localPlayer.x / 32;
				final int j4 = (client.anInt935 - client.baseY) * 4 + 2 - client.localPlayer.y / 32;
				method81(client.mapArrow, l1, j4);
			}
			if(client.hintType == 10 && client.anInt933 >= 0 && client.anInt933 < client.playerList.length) {
				final Player player = client.playerList[client.anInt933];
				if(player != null) {
					final int i2 = player.x / 32 - client.localPlayer.x / 32;
					final int k4 = player.y / 32 - client.localPlayer.y / 32;
					method81(client.mapArrow, i2, k4);
				}
			}
		}
		if(client.walkX != 0) {
			final int x = client.walkX * 4 + 2 - client.localPlayer.x / 32;
			final int y = client.walkY * 4 + 2 - client.localPlayer.y / 32;
			markMinimap(client.mapFlag, x, y);
		}
		Rasterizer2D.removeClip();
		Rasterizer2D.fillRectangle(106, 79, 3, 3, 0xffffff);
		ImageCache.get(18).drawImage(0, 0);
		ImageCache.get(1700).drawAffineTransformedImage(8, 8, 33, 33, 25, 25, client.compassClipStarts, client.compassLineLengths, client.cameraAngleX, 256);
		ImageCache.get(1950).drawImage(-2, 47);
		if(client.mouseInRegion(521, 54, 550, 81)) {
			ImageCache.get(1951).drawImage(-2, 47);
		}
		if(Config.def.isDRAW_ORBS()) {
			displayOrb(164, 9, Constants.ORB_HEALTH, false);
			displayOrb(185, 47, Constants.ORB_PRAYER, true);
			displayOrb(185, 86, Constants.ORB_RUN, true);
			displayOrb(170, 125, Constants.ORB_SUMMONING, true);
		}
		if(client.mouseInRegion(526, 127, 562, 159)) {
			ImageCache.get(239).drawImage(8, 123);
		}
		if(client.menuOpened) {
			client.gameActivity.drawer.drawMenu(-519, 0, false);
		}
		displayLogout();
		client.mapGraphics.drawGraphics(519, 0, client.graphics);
		client.gameGraphics.setCanvas();
	}

	@Override
	public void buildInventory() {
		if(client.mouseWheelAmt != 0 && client.newerTabInterfaces[client.invTab] != -1) {
			Interface tab = Interface.cache[client.newerTabInterfaces[client.invTab]];
			if(tab.subId != null) {
				int posX = 0;
				int posY = 0;
				Interface widget = null;
				for(int index = 0; index < tab.subId.length; index++) {
					if(Interface.cache[tab.subId[index]].scrollMax > 0) {
						posX = tab.subX[index] + 547;
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
		final short[][] tabClickPositions562 = {{521, 550, 168, 205}, {552, 581, 168, 205}, {582, 611, 168, 205}, {612, 641, 168, 205}, {642, 671, 168, 205}, {672, 701, 168, 205}, {702, 731, 168, 205}, {732, 761, 169, 205}, {522, 551, 466, 503}, {552, 581, 466, 503}, {582, 611, 466, 503}, {612, 641, 466, 503}, {642, 671, 466, 503}, {672, 701, 466, 503}, {702, 731, 466, 503}, {732, 761, 466, 503},};
		for(int i = 0; i < 16; i++) {
			if(client.clickX >= tabClickPositions562[i][0] && client.clickX <= tabClickPositions562[i][1] && client.clickY >= tabClickPositions562[i][2] && client.clickY < tabClickPositions562[i][3] && (client.newerTabInterfaces[i] != -1 || SettingPanel.selectedBinding != -1)) {
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
		ImageCache.get(16).drawImage(0, 0);
		if(client.invOverlayInterfaceID == -1) {
			displayHoveredTab();
			displaySelectedTabHighlight();
			displaySideIcons();
		}
		if(client.invOverlayInterfaceID != -1) {
			client.drawWidget(Interface.cache[client.invOverlayInterfaceID], 28, 37, 0, UIComponent.INVENTORY);
		} else if(client.newerTabInterfaces[client.invTab] != -1) {
			client.drawWidget(Interface.cache[client.newerTabInterfaces[client.invTab]], 28, 37, 0, UIComponent.INVENTORY);
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

	public void method81(BitmapImage icon, int x, int y) {
		final int l = x * x + y * y;
		if(l > 4225 && l < 0x15f90) {
			final int i1 = client.cameraAngleX + client.minimapAngle & 0x7ff;
			int j1 = Model.angleSine[i1];
			int k1 = Model.angleCosine[i1];
			j1 = j1 * 256 / (client.minimapZoom + 256);
			k1 = k1 * 256 / (client.minimapZoom + 256);
			final int l1 = y * j1 + x * k1 >> 16;
			final int i2 = y * k1 - x * j1 >> 16;
			final double d = Math.atan2(l1, i2);
			final int j2 = (int) (Math.sin(d) * 63D);
			final int k2 = (int) (Math.cos(d) * 57D);
			if(client.uiRenderer.isResizableOrFull()) {
				ImageCache.get(85).method353(94 + j2 + 4, 83 - k2 - 20, d);
			}
		} else {
			int xOffset = 10;
			markMinimap(icon, xOffset + x, y);
		}
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
		icon.drawImage(94 + fx - icon.imageOriginalWidth / 2 + 14, 83 - fy - icon.imageOriginalHeight / 2);
	}

	/**
	 * Displays the chat channel buttons.
	 */
	private void displayChannelButtons() {
		final String text[] = {"On", "Friends", "Off", "Hide"};
		final int textColor[] = {65280, 0xffff00, 0xff0000, 65535};
		for(int i = 0; i <= 6; i++) {
			if(client.selectedChannelButton == i) {
				client.chatButtons[1].drawImage(5 + 57 * i, 143);
			}
			if(i == client.hoveredChannelButton) {
				if(client.hoveredChannelButton == client.selectedChannelButton) {
					client.chatButtons[2].drawImage(5 + 57 * i, 143);
				} else {
					client.chatButtons[0].drawImage(5 + 57 * i, 143);
				}
			}
		}
		if(client.hoveredChannelButton == 7) {
			client.chatButtons[3].drawImage(404, 142);
		}
		client.smallFont.drawLeftAlignedEffectString("Report Abuse", 425, 157, 0xffffff, true);
		client.smallFont.drawLeftAlignedEffectString("All", 26, 157, 0xffffff, true);
		client.smallFont.drawLeftAlignedEffectString("Game", 78, 154, 0xffffff, true);
		client.smallFont.drawLeftAlignedEffectString("Public", 132, 154, 0xffffff, true);
		client.smallFont.drawLeftAlignedEffectString("Private", 187, 154, 0xffffff, true);
		client.smallFont.drawLeftAlignedEffectString("Clan", 249, 154, 0xffffff, true);
		client.smallFont.drawLeftAlignedEffectString("Trade", 304, 154, 0xffffff, true);
		client.smallFont.drawCenteredEffectString("Yell", 374, 154, 0xffffff, true);
		client.smallFont.drawCenteredEffectString("On", 90, 164, 65280, true);
		client.smallFont.drawCenteredEffectString(text[client.yellChatMode], 374, 164, textColor[client.yellChatMode], true);
		client.smallFont.drawCenteredEffectString(text[client.publicChatMode], 147, 165, textColor[client.publicChatMode], true);
		client.smallFont.drawCenteredEffectString(text[client.privateChatMode], 205, 165, textColor[client.privateChatMode], true);
		client.smallFont.drawCenteredEffectString(text[client.clanChatMode], 260, 165, textColor[client.clanChatMode], true);
		client.smallFont.drawCenteredEffectString(text[client.tradeMode], 318, 165, textColor[client.tradeMode], true);
	}

	/**
	 * Displays orbs components.
	 */
	private void displayOrb(int x, int y, int orb, boolean hover) {
		ImageCache.get(hover && client.mouseInRegion(x + 519, y + 4, x + 576, y + 37) ? 49 : 48).drawImage(x, y);
		client.smallFont.drawCenteredEffectString(OrbHandler.getValue(orb), x + 43, y + 26, OrbHandler.getColor(orb), true);
		ImageCache.get(OrbHandler.getOrb(orb)).drawImage(x + 3, y + 3);
		Rasterizer2D.setClip(x + 3, y + 3, x + 30, y + 3 + OrbHandler.getFill(orb, 27));
		ImageCache.get(60).drawImage(x + 3, y + 3);
		Rasterizer2D.removeClip();
		if(orb != Constants.ORB_HEALTH || OrbHandler.getPercent(orb) > 20 || OrbHandler.getPercent(orb) < 1 || client.loopCycle % 20 > 10) {
			ImageCache.get(orb == Constants.ORB_RUN && OrbHandler.runEnabled ? 74 : 61 + orb).drawImage(x + 17 - ImageCache.get(61 + orb).imageWidth / 2, y + 17 - ImageCache.get(61 + orb).imageHeight / 2);
		}
	}

	/**
	 * Displays the hovered tab.
	 */
	private void displayHoveredTab() {
		if(client.mouseX < 522 || client.mouseX > 760 || client.mouseY < 170 || client.mouseY > 204 && client.mouseY < 468) {
			return;
		}
		for(int i = 8; i >= 0; i--) {
			if(client.mouseX - 522 > i * 30) {
				if(client.newerTabInterfaces[i + (client.mouseY < 205 ? 0 : 8)] != -1) {
					ImageCache.get(19).drawImage(i * 30 + 3, client.mouseY < 205 ? 0 : 298);
				}
				return;
			}
		}
	}

	private void displayLogout() {
		if(client.invTab == 16)
			ImageCache.get(1009).drawImage(225, 2);
		else if(client.mouseInRegion(744, 2, 765, 23))
			ImageCache.get(1008).drawImage(225, 2);
		else
			ImageCache.get(1007).drawImage(225, 2);
	}

	/**
	 * Displays the selected tab.
	 */
	private void displaySelectedTabHighlight() {
		if(client.newerTabInterfaces[client.invTab] == -1 || client.invTab == 16) {
			return;
		}
		final short[][] data = {{-2, 1}, {28, 1}, {58, 1}, {88, 1}, {118, 1}, {149, 1}, {178, 1}, {208, 1}, {-2, 299}, {28, 299}, {58, 299}, {88, 299}, {118, 299}, {149, 299}, {178, 299}, {208, 299}};
		ImageCache.get(21).drawImage(data[client.invTab][0], data[client.invTab][1]);
	}

	/**
	 * Displays the side icons.
	 */
	private void displaySideIcons() {
		for(int i = 0; i < 16; i++) {
			if(client.newerTabInterfaces[i] != -1 || SettingPanel.selectedBinding != -1) {
				final int xOffset = 18 - ImageCache.get(i + 22).imageWidth / 2;
				final int yOffset = 20 - ImageCache.get(i + 22).imageHeight / 2;
				if(i < 8) {
					ImageCache.get(i + 22).drawImage(xOffset + 30 * i, yOffset);
				} else {
					ImageCache.get(i + 22).drawImage(xOffset + 30 * (i - 8), yOffset + 298);
				}
			}
		}
	}
}
