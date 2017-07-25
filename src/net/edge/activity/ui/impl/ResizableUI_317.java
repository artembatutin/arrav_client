package net.edge.activity.ui.impl;

import net.edge.Config;
import net.edge.Constants;
import net.edge.activity.panel.impl.SettingPanel;
import net.edge.activity.ui.UIComponent;
import net.edge.activity.ui.util.CounterHandler;
import net.edge.activity.ui.util.OrbHandler;
import net.edge.game.model.Model;
import net.edge.game.model.Player;
import net.edge.media.Rasterizer2D;
import net.edge.cache.unit.ImageCache;
import net.edge.cache.unit.Interface;
import net.edge.cache.unit.NPCType;
import net.edge.game.model.NPC;
import net.edge.media.img.BitmapImage;
import net.edge.util.collect.LinkedDeque;
import net.edge.util.string.StringUtils;

import java.awt.*;

public class ResizableUI_317 extends ResizableUI {

	@Override
	public void buildChat() {
		final String[] modes = {"View", "On", "Friends", "Off", "Hide"};
		if(client.mouseX >= 5 && client.mouseX <= 61 && client.mouseY >= client.windowHeight - 21 && client.mouseY <= client.windowHeight) {
			client.menuItemName[1] = "View All";
			client.menuItemCode[1] = 999;
			client.menuPos = 2;
			client.hoveredChannelButton = 0;
		} else if(client.mouseX >= 62 && client.mouseX <= 118 && client.mouseY >= client.windowHeight - 21 && client.mouseY <= client.windowHeight) {
			client.menuItemName[1] = "View Game";
			client.menuItemCode[1] = 998;
			client.menuPos = 2;
			client.hoveredChannelButton = 1;
		} else if(client.mouseX >= 119 && client.mouseX <= 175 && client.mouseY >= client.windowHeight - 21 && client.mouseY <= client.windowHeight) {
			for(int i = 0; i <= 4; i++) {
				client.menuItemName[i + 1] = modes[4 - i] + " public";
				client.menuItemCode[i + 1] = 997 - i;
			}
			client.menuPos = 6;
			client.hoveredChannelButton = 2;
		} else if(client.mouseX >= 176 && client.mouseX <= 232 && client.mouseY >= client.windowHeight - 21 && client.mouseY <= client.windowHeight) {
			for(int i = 0; i <= 3; i++) {
				client.menuItemName[i + 1] = modes[3 - i] + " private";
				client.menuItemCode[i + 1] = 992 - i;
			}
			client.menuPos = 5;
			client.hoveredChannelButton = 3;
		} else if(client.mouseX >= 233 && client.mouseX <= 289 && client.mouseY >= client.windowHeight - 21 && client.mouseY <= client.windowHeight) {
			for(int i = 0; i <= 3; i++) {
				client.menuItemName[i + 1] = modes[3 - i] + " clan chat";
				client.menuItemCode[i + 1] = 1003 - i;
			}
			client.menuPos = 5;
			client.hoveredChannelButton = 4;
		} else if(client.mouseX >= 290 && client.mouseX <= 344 && client.mouseY >= client.windowHeight - 21 && client.mouseY <= client.windowHeight) {
			for(int i = 0; i <= 3; i++) {
				client.menuItemName[i + 1] = modes[3 - i] + " trade";
				client.menuItemCode[i + 1] = 987 - i;
			}
			client.menuPos = 5;
			client.hoveredChannelButton = 5;
		} else if(client.mouseX >= 345 && client.mouseX <= 402 && client.mouseY >= client.windowHeight - 21 && client.mouseY <= client.windowHeight) {
			for(int i = 0; i <= 3; i++) {
				client.menuItemName[i + 1] = modes[3 - i] + " yell";
				client.menuItemCode[i + 1] = 887 - i;
			}
			client.menuPos = 5;
			client.hoveredChannelButton = 6;
		} else if(client.mouseX >= 404 && client.mouseX <= 514 && client.mouseY >= client.windowHeight - 21 && client.mouseY <= client.windowHeight) {
			client.hoveredChannelButton = 7;
		} else if(client.hoveredChannelButton != -1) {
			client.hoveredChannelButton = -1;
		}
		if(client.forcedChatWidgetId == -1 && client.showChat) {
			client.chatWidget.scrollPos = client.chatContentHeight - client.chatScrollPos - 114;
			if(client.mouseX > 478 && client.mouseX < 580 && client.mouseY > client.windowHeight - 165) {
				client.gameActivity.processScrollbar(494, 0, client.mouseX, client.mouseY - (client.windowHeight - 159), 114, client.chatContentHeight, client.chatWidget);
			}
			int pos = client.chatContentHeight - 114 - client.chatWidget.scrollPos;
			if(client.mouseWheelAmt != 0 && client.mouseWheelX < 520 &&
					client.mouseWheelY > client.windowHeight - 160) {
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
		int yoff = client.windowHeight - 165;
		if(client.messagePromptRaised || client.bankSearching || client.inputDialogState > 0 || client.chatBoxStatement != null || client.forcedChatWidgetId != -1 || client.chatWidgetId != -1) {
			ImageCache.get(67).drawImage(0, yoff - 1);
			Rasterizer2D.fillRectangle(7, yoff + 6, 506, 129, 0xccbb9a, 150);
		} else {
			if(client.showChat) {
				for(int i = 0; i < 114; i++) {
					int alpha = (int) (64 * i / 114f);
					Rasterizer2D.drawHorizontalLine(7, yoff + 7 + i, 505, 0, alpha);
				}
			}
		}
		displayChannelButtons();
		if(client.messagePromptRaised) {
			client.boldFont.drawCenteredString(client.promptInputTitle, 259, yoff + 60, 0);
			client.boldFont.drawCenteredString(client.promptInput + "*", 259, yoff + 80, 128);
		} else if(client.inputDialogState == 1) {
			client.boldFont.drawCenteredString("Enter amount:", 259, yoff + 60, 0);
			client.boldFont.drawCenteredString(client.amountOrNameInput + client.outBoundInput + "*", 259, yoff + 80, 128);
		} else if(client.inputDialogState == 2) {
			client.boldFont.drawCenteredString("Enter name:", 259, yoff + 60, 0);
			client.boldFont.drawCenteredString(client.amountOrNameInput + client.outBoundInput + "*", 259, yoff + 80, 128);
		} else if(client.chatBoxStatement != null) {
			client.boldFont.drawCenteredString(client.chatBoxStatement, 259, yoff + 60, 0);
			client.boldFont.drawCenteredString("Click to continue", 259, yoff + 80, 128);
		} else if(client.forcedChatWidgetId != -1) {
			client.drawWidget(Interface.cache[client.forcedChatWidgetId], 20, yoff + 20, 0, UIComponent.CHAT);
		} else if(client.chatWidgetId != -1) {
			client.drawWidget(Interface.cache[client.chatWidgetId], 20, yoff + 20, 0, UIComponent.CHAT);
		} else if(client.bankSearching) {
			client.boldFont.drawCenteredString("What are you looking for?", 259, yoff + 60, 0);
			client.boldFont.drawCenteredString(client.bankSearch + "*", 259, yoff + 80, 128);
		} else if(client.showChat) {
			int totalMessages = 0;
			Rasterizer2D.setClip(8, yoff + 7, 496, yoff + 122);
			int basicFontColor = 0xffffff;
			int blueFontColor = 0x7fa9ff;
			int redFontColor = 0xff5256;
			int purpleFontColor = 0xff78d9;
			int orangeFontColor = 0xff8c38;
			int view = client.chatTypeView;
			for(int i = 0; i < 500; i++) {
				if(client.chatMessage[i] != null) {
					int x = 11;
					int y = (115 - 14 * totalMessages) + client.chatScrollPos + yoff;
					boolean inArea = y > yoff && y < yoff + 210;
					if(!inArea) {
						totalMessages++;
						continue;
					}
					int type = client.chatType[i];
					String author = client.chatAuthor[i];
					String msg = client.chatMessage[i];
					int rights = client.chatPriv[i];
					if(!client.uiRenderer.canSeeMessage(type, view, rights, author)) {
						continue;
					}
					if(type == 0) {
						client.plainFont.drawLeftAlignedEffectString(msg, x, y, basicFontColor, true);
					} else if(type == 1) {
						if(rights >= 1) {
							ImageCache.get(1984 + rights - 1).drawImage(x + 1, y - 12);
							x += 14;
						}
						client.plainFont.drawLeftAlignedString(author + ":", x + 1, y + 1, 0);
						client.plainFont.drawLeftAlignedString(author + ":", x, y, basicFontColor);
						x += client.plainFont.getStringWidth(author + ": ");
						client.plainFont.drawLeftAlignedString(msg, x + 1, y + 1, 0);
						client.plainFont.drawLeftAlignedString(msg, x, y, blueFontColor);
					} else if(type == 2) {
						client.plainFont.drawLeftAlignedString("From", x + 1, y + 1, 0);
						client.plainFont.drawLeftAlignedString("From", x, y, redFontColor);
						x += client.plainFont.getStringWidth("From ") + 12;
						if(rights >= 1 && rights <= 3) {
							ImageCache.get(1984 + rights - 1).drawImage(x, y - 12);
						} else {
							x -= 12;
						}
						client.plainFont.drawLeftAlignedString(author + ":  " + client.chatMessage[i], x + 1, y + 1, 0);
						client.plainFont.drawLeftAlignedString(author + ":  " + client.chatMessage[i], x, y, redFontColor);
					} else if(type == 4) {
						client.plainFont.drawLeftAlignedString(author + " " + msg, x + 1, y + 1, 0);
						client.plainFont.drawLeftAlignedString(author + " " + msg, x, y, purpleFontColor);
					} else if(type == 5) {
						client.plainFont.drawLeftAlignedString(msg, x + 1, y + 1, 0);
						client.plainFont.drawLeftAlignedString(msg, x, y, redFontColor);
					} else if(type == 6) {
						client.plainFont.drawLeftAlignedString("To " + author + ": " + msg, x + 1, y + 1, 0);
						client.plainFont.drawLeftAlignedString("To " + author + ": " + msg, x, y, redFontColor);
					} else if(type == 7) {
						final int split = author.indexOf(":");
						final String clan = author.substring(0, split);
						author = author.substring(split + 1);
						client.plainFont.drawLeftAlignedString("[", x + 1, y + 1, 0);
						client.plainFont.drawLeftAlignedString("[", x, y, 0xffffff);
						x += 5;
						client.plainFont.drawLeftAlignedString(clan, x + 1, y + 1, 0);
						client.plainFont.drawLeftAlignedString(clan, x, y, 0x7fa9ff);
						x += client.plainFont.getStringWidth(clan);
						client.plainFont.drawLeftAlignedString("]", x + 1, y + 1, 0);
						client.plainFont.drawLeftAlignedString("]", x, y, 0xffffff);
						x += 7;
						if(rights >= 1) {
							ImageCache.get(1984 + rights - 1).drawImage(x, y - 12);
							x += 13;
						}
						client.plainFont.drawLeftAlignedString(author + ":", x + 1, y + 1, 0);
						client.plainFont.drawLeftAlignedString(author + ":", x, y, 0xffffff);
						x += client.plainFont.getStringWidth(author) + 6;
						client.plainFont.drawLeftAlignedString(msg, x + 1, y + 1, 0);
						client.plainFont.drawLeftAlignedString(msg, x, y, 0xff5256);
					} else if(type == 8) {
						client.plainFont.drawLeftAlignedString(author + " " + msg, x + 1, y + 1, 0);
						client.plainFont.drawLeftAlignedString(author + " " + msg, x, y, orangeFontColor);
					} else if(type == 9) {
						final int split = author.indexOf(":");
						author = author.substring(split + 1);
						if(rights >= 1) {
							ImageCache.get(1984 + rights - 1).drawImage(x, y - 12);
							x += 13;
						}
						client.plainFont.drawLeftAlignedString(author + ":", x + 1, y + 1, 0);
						client.plainFont.drawLeftAlignedString(author + ":", x, y, 0xffffff);
						x += client.plainFont.getStringWidth(author) + 6;
						client.plainFont.drawLeftAlignedString(msg, x + 1, y + 1, 0);
						client.plainFont.drawLeftAlignedString(msg, x, y, 0x5cac9c);
					}
					totalMessages++;
				}
			}
			Rasterizer2D.removeClip();
			client.chatContentHeight = totalMessages * 14 + 7;
			if(client.chatContentHeight < 114) {
				client.chatContentHeight = 114;
			}
			client.gameActivity.drawWhiteScrollbar(496, yoff + 7, 114, client.chatContentHeight, client.chatContentHeight - client.chatScrollPos - 113);
			String myName;
			if(client.localPlayer != null && client.localPlayer.name != null) {
				myName = client.localPlayer.name;
			} else {
				myName = StringUtils.formatName(client.localUsername);
			}
			client.plainFont.drawLeftAlignedString(myName + ":", 12, yoff + 134, 0);
			client.plainFont.drawLeftAlignedString(myName + ":", 11, yoff + 133, 0xffffff);
			client.plainFont.drawLeftAlignedString(client.chatInput + "*", 13 + client.plainFont.getStringWidth(myName + ": "), yoff + 134, 0);
			client.plainFont.drawLeftAlignedString(client.chatInput + "*", 12 + client.plainFont.getStringWidth(myName + ": "), yoff + 133, 0x7fa9ff);
			for(int i = 0; i < 505; i++) {
				int opacity = 100 - (int) (i / 5.05);
				Rasterizer2D.drawHorizontalLine(7 + i, yoff + 6, 1, 0xffffff, opacity);
				Rasterizer2D.drawHorizontalLine(7 + i, yoff + 121, 1, 0xffffff, opacity);
			}
		}
		client.gameGraphics.setCanvas();
	}

	@Override
	public void buildMap() {
		if(client.mouseInRegion(client.windowWidth - 180, 3, client.windowWidth - 145, 38)) {
			client.menuItemName[1] = "Face North";
			client.menuItemCode[1] = 1014;
			client.menuPos = 2;
		} else if(Config.def.orbs() && client.mouseX > client.windowWidth - 202 && client.mouseX < client.windowWidth - 142 && client.mouseY > 106 && client.mouseY < 139) {
			client.menuItemName[1] = "Run";
			client.menuItemCode[1] = 1051;
			client.menuPos = 2;
		} else if(Config.def.orbs() && client.mouseInRegion(client.windowWidth - 212, 72, client.windowWidth - 155, 105)) {
			client.menuPos = 0;
			client.menuItemName[client.menuPos] = "Select quick prayers";
			client.menuItemCode[client.menuPos] = 1054;
			client.menuPos++;
			client.menuItemName[client.menuPos] = "Turn quick prayers " + (OrbHandler.prayersEnabled ? "off" : "on");
			client.menuItemCode[client.menuPos] = 1053;
			client.menuPos++;
		} else if(client.mouseInRegion(client.windowWidth - 217, 10, client.windowWidth - 188, 37)) {
			client.menuPos = 0;
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
			int amt = (int) (Math.sqrt(Math.pow(77, 2) - Math.pow(75 - i, 2)));
			client.minimapLineLengths[i] = 2 * amt + 2;
			client.minimapLineLengths[150 - i] = 2 * amt + 2;
			client.minimapLineStarts[i] = -amt + 73;
			client.minimapLineStarts[150 - i] = -amt + 73;
		}
		int xOffset = client.windowWidth - 182;
		if(client.minimapOverlay == 2) {
			ImageCache.get(1925).drawImage(xOffset, -2);
			ImageCache.get(1700).drawAffineTransformedImage(xOffset + 5, 3, 33, 33, 25, 25, client.compassClipStarts, client.compassLineLengths, client.cameraAngleX, 256);
			if(Config.def.orbs()) {
				displayOrb(client.windowWidth - 209, 38, Constants.ORB_HEALTH, false);
				displayOrb(client.windowWidth - 212, 72, Constants.ORB_PRAYER, true);
				displayOrb(client.windowWidth - 200, 106, Constants.ORB_RUN, true);
				displayOrb(client.windowWidth - 177, 140, Constants.ORB_SUMMONING, true);
			}
			return;
		}
		int rotation = client.cameraAngleX + client.minimapAngle & 0x7ff;
		int middleX = 48 + client.localPlayer.x / 32;
		int middleY = 464 - client.localPlayer.y / 32;
		client.minimapImage.drawAffineTransformedImage(xOffset + 25, 5, 146, 151, middleX, middleY, client.minimapLineStarts, client.minimapLineLengths, rotation, 256 + client.minimapZoom);
		for(int j5 = 0; j5 < client.mapFunctionCount; j5++) {
			int k = (client.mapFunctionX[j5] * 4 + 2) - client.localPlayer.x / 32;
			int i3 = (client.mapFunctionY[j5] * 4 + 2) - client.localPlayer.y / 32;
			markMinimap(client.mapFunctionImage[j5], k, i3);
		}
		for(int k5 = 0; k5 < 104; k5++) {
			for(int l5 = 0; l5 < 104; l5++) {
				LinkedDeque class19 = client.sceneItems[client.cameraPlane][k5][l5];
				if(class19 != null) {
					int l = (k5 * 4 + 2) - client.localPlayer.x / 32;
					int j3 = (l5 * 4 + 2) - client.localPlayer.y / 32;
					markMinimap(client.mapDotItem, l, j3);
				}
			}
		}
		for(int i6 = 0; i6 < client.npcListSize; i6++) {
			NPC npc = client.npcList[client.npcEntryList[i6]];
			if(npc != null && npc.isVisible()) {
				NPCType entityDef = npc.type;
				if(entityDef.childrenIDs != null) {
					entityDef = entityDef.getSubNPCType();
				}
				if(entityDef != null && entityDef.visibleMinimap && entityDef.clickable) {
					int i1 = npc.x / 32 - client.localPlayer.x / 32;
					int k3 = npc.y / 32 - client.localPlayer.y / 32;
					markMinimap(client.mapDotNPC, i1, k3);
				}
			}
		}
		for(int j6 = 0; j6 < client.playerCount; j6++) {
			Player player = client.playerList[client.playerEntryList[j6]];
			if(player != null && player.isVisible()) {
				int x = player.x / 32 - client.localPlayer.x / 32;
				int y = player.y / 32 - client.localPlayer.y / 32;
				boolean flag1 = false;
				long l6 = StringUtils.encryptName(player.name);
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
				NPC npc = client.npcList[client.NPCHintID];
				if(npc != null) {
					int k1 = npc.x / 32 - client.localPlayer.x / 32;
					int i4 = npc.y / 32 - client.localPlayer.y / 32;
					method81(client.mapArrow, k1, i4);
				}
			}
			if(client.hintType == 2) {
				int l1 = ((client.anInt934 - client.baseX) * 4 + 2) - client.localPlayer.x / 32;
				int j4 = ((client.anInt935 - client.baseY) * 4 + 2) - client.localPlayer.y / 32;
				method81(client.mapArrow, l1, j4);
			}
			if(client.hintType == 10 && client.anInt933 >= 0 && client.anInt933 < client.playerList.length) {
				Player player = client.playerList[client.anInt933];
				if(player != null) {
					int i2 = player.x / 32 - client.localPlayer.x / 32;
					int k4 = player.y / 32 - client.localPlayer.y / 32;
					method81(client.mapArrow, i2, k4);
				}
			}
		}
		if(client.walkX != 0) {
			int j2 = (client.walkX * 4 + 2) - client.localPlayer.x / 32;
			int l4 = (client.walkY * 4 + 2) - client.localPlayer.y / 32;
			markMinimap(client.mapFlag, j2, l4);
		}
		Rasterizer2D.removeClip();
		ImageCache.get(1925).drawImage(xOffset, -2);
		ImageCache.get(1700).drawAffineTransformedImage(xOffset + 5, 3, 33, 33, 25, 25, client.compassClipStarts, client.compassLineLengths, client.cameraAngleX, 256);
		if(Config.def.orbs()) {
			displayOrb(client.windowWidth - 209, 38, Constants.ORB_HEALTH, false);
			displayOrb(client.windowWidth - 212, 72, Constants.ORB_PRAYER, true);
			displayOrb(client.windowWidth - 200, 106, Constants.ORB_RUN, true);
			displayOrb(client.windowWidth - 177, 140, Constants.ORB_SUMMONING, true);
		}
		ImageCache.get(1955).drawImage(client.windowWidth - 217, 10);
		if(client.mouseInRegion(client.windowWidth - 217, 10, client.windowWidth - 188, 37)) {
			ImageCache.get(1956).drawImage(client.windowWidth - 217, 10);
		}
		Rasterizer2D.fillRectangle(xOffset + 97, 78, 3, 3, 0xffffff);
		client.gameGraphics.setCanvas();
	}

	@Override
	public void buildInventory() {
		if(client.mouseWheelAmt != 0 && client.olderTabInterfaces[client.invTab] != -1) {
			Interface tab = Interface.cache[client.olderTabInterfaces[client.invTab]];
			if(tab.subId != null) {
				int posX = client.windowWidth - 197;
				int posY = client.windowHeight - 303;
				if(client.windowWidth < 980) {
					posY -= 36;
				}
				Interface widget = null;
				for(int index = 0; index < tab.subId.length; index++) {
					if(Interface.cache[tab.subId[index]].scrollMax > 0) {
						posX += tab.subX[index];
						posY += tab.subY[index];
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
		if(client.windowWidth < 980) {
			if(client.clickX > client.windowWidth - 231 && client.clickY > client.windowHeight - 72) {
				int tabClick = (client.clickX - (client.windowWidth - 231)) / 33;
				if(client.clickY > client.windowHeight - 36) {
					tabClick += 7;
				}
				if(SettingPanel.selectedBinding != -1) {
					SettingPanel.hotkeys[SettingPanel.selectedBinding] = tabClick;
					SettingPanel.selectedBinding = -1;
					return;
				}
				if(tabClick == client.invTab && client.showTab) {
					client.showTab = false;
				} else if(client.olderTabInterfaces[tabClick] != -1) {
					client.showTab = true;
					client.invTab = tabClick;
				}
			}
		} else if(client.clickX > client.windowWidth - 462 && client.clickY > client.windowHeight - 36) {
			final int tabClick = (client.clickX - (client.windowWidth - 462)) / 33;
			if(SettingPanel.selectedBinding != -1) {
				SettingPanel.hotkeys[SettingPanel.selectedBinding] = tabClick;
				SettingPanel.selectedBinding = -1;
				return;
			}
			if(tabClick == client.invTab && client.showTab) {
				client.showTab = false;
			} else if(client.olderTabInterfaces[tabClick] != -1) {
				client.showTab = true;
				client.invTab = tabClick;
			}
		}
	}

	@Override
	public void updateInventory() {
		int xOffset = client.windowWidth - 197;
		int yOffset = client.windowHeight - 303;
		if(client.windowWidth < 980) {
			ImageCache.get(46).drawImage(xOffset - 34, yOffset + 231);
			ImageCache.get(46).drawImage(xOffset - 34, yOffset + 266);
		} else {
			ImageCache.get(46).drawImage(xOffset - 34, yOffset + 267);
			ImageCache.get(46).drawImage(xOffset - 265, yOffset + 267);
		}
		if(client.invOverlayInterfaceID == -1) {
			displaySelectedTabHighlight();
			displaySideIcons();
		}
		if(client.showTab) {
			if(client.windowWidth < 980) {
				Rasterizer2D.fillRectangle(xOffset, yOffset - 36, 190, 269, 0x50463C, 100);
				ImageCache.get(47).drawImage(xOffset - 7, yOffset - 43);
			} else {
				Rasterizer2D.fillRectangle(xOffset, yOffset, 190, 269, 0x50463C, 100);
				ImageCache.get(47).drawImage(xOffset - 7, yOffset - 7);
			}
			if(client.invOverlayInterfaceID != -1) {
				client.drawWidget(Interface.cache[client.invOverlayInterfaceID], xOffset, yOffset - (client.windowWidth < 980 ? 36 : 0), 0, UIComponent.INVENTORY);
			} else if(client.olderTabInterfaces[client.invTab] != -1) {
				client.drawWidget(Interface.cache[client.olderTabInterfaces[client.invTab]], xOffset, yOffset - (client.windowWidth < 980 ? 36 : 0), 0, UIComponent.INVENTORY);
			}
		}
	}

	@Override
	public Point getOnScreenWidgetOffsets() {
		return super.getOnScreenWidgetOffsets();
	}
	
	@Override
	public BitmapImage getSide(int index) {
		return index < 0 || index >= client.sideIcons.length ? null : client.sideIcons[index];
	}
	
	@Override
	public boolean allowScene() {
		if(client.mouseX >= (client.windowWidth < 980 ? client.windowWidth - 231 : client.windowWidth - 462) && client.mouseY >= (client.windowWidth < 980 ? client.windowHeight - 72 : client.windowHeight - 36)) {
			return false;
		}
		if(client.mouseX >= client.windowWidth - 204 && client.mouseY >= (client.windowWidth < 980 ? client.windowHeight - 346 : client.windowHeight - 310) && client.showTab) {
			return false;
		}
		if(client.mouseX >= 0 && client.mouseX <= 518 & client.mouseY >= client.windowHeight - 22) {
			return false;
		}
		if(client.mouseX >= 0 && client.mouseX <= 518 & client.mouseY >= client.windowHeight - 165 && (client.showChat || client.messagePromptRaised || client.inputDialogState > 0 || client.chatBoxStatement != null || client.forcedChatWidgetId != -1 || client.chatWidgetId != -1)) {
			return false;
		}
		if(client.mouseY >= 0 && client.mouseY <= 165 & client.mouseX >= client.windowWidth - 182) {
			return false;
		}
		return client.mouseX >= 0 && client.mouseY >= 0;
	}

	@Override
	public void buildSceneOverlay() {
	}

	public void method81(BitmapImage icon, int x, int y) {
		final int l = x * x + y * y;
		if(l > 4225 && l < 0x15f90) {
			final int i1 = client.cameraAngleX + client.minimapAngle & 0x7ff;
			int j1 = Model.angleSine[i1];
			int k1 = Model.angleCosine[i1];
			j1 = (j1 << 8) / (client.minimapZoom + 256);
			k1 = (k1 << 8) / (client.minimapZoom + 256);
			final int l1 = y * j1 + x * k1 >> 16;
			final int i2 = y * k1 - x * j1 >> 16;
			final double d = Math.atan2(l1, i2);
			final int j2 = (int) (Math.sin(d) * 63D);
			final int k2 = (int) (Math.cos(d) * 57D);
			ImageCache.get(85).method353(94 + j2 + 4, 83 - k2 - 20, d);
		} else {
			int xOffset = client.windowWidth - 182;
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
		int xOffset = client.windowWidth - 182;
		int yOffset = -4;
		icon.drawImage(94 + fx - icon.imageOriginalWidth / 2 + xOffset + 4, 83 - fy - icon.imageOriginalHeight / 2 - 8 + 8 + yOffset);
	}

	/**
	 * Displays the channel buttons.
	 */
	private void displayChannelButtons() {
		int y = client.windowHeight - 165;
		ImageCache.get(65).drawImage(5, y + 142);
		String text[] = {"On", "Friends", "Off", "Hide"};
		int textColor[] = {65280, 0xffff00, 0xff0000, 65535};
		for(int i = 0; i <= 6; i++) {
			if(client.selectedChannelButton == i && (client.showChat)) {
				client.chatButtons[1].drawImage(5 + 57 * i, y + 142);
			}
			if(i == client.hoveredChannelButton) {
				if(client.hoveredChannelButton == client.selectedChannelButton && (client.showChat)) {
					client.chatButtons[2].drawImage(5 + 57 * i, y + 142);
				} else {
					client.chatButtons[0].drawImage(5 + 57 * i, y + 142);
				}
			}
		}
		if(client.hoveredChannelButton == 7) {
			client.chatButtons[3].drawImage(404, y + 143);
		}
		client.smallFont.drawLeftAlignedEffectString("Report Abuse", 425, y + 157, 0xffffff, true);
		client.smallFont.drawLeftAlignedEffectString("All", 26, y + 157, 0xffffff, true);
		client.smallFont.drawLeftAlignedEffectString("Game", 78, y + 152, 0xffffff, true);
		client.smallFont.drawLeftAlignedEffectString("Public", 132, y + 152, 0xffffff, true);
		client.smallFont.drawLeftAlignedEffectString("Private", 187, y + 152, 0xffffff, true);
		client.smallFont.drawLeftAlignedEffectString("Clan", 249, y + 152, 0xffffff, true);
		client.smallFont.drawLeftAlignedEffectString("Trade", 304, y + 152, 0xffffff, true);
		client.smallFont.drawCenteredEffectString("Yell", 374, y + 152, 0xffffff, true);
		client.smallFont.drawCenteredEffectString("On", 90, y + 163, 65280, true);
		client.smallFont.drawCenteredEffectString(text[client.yellChatMode], 374, y + 163, textColor[client.yellChatMode], true);
		client.smallFont.drawCenteredEffectString(text[client.publicChatMode], 147, y + 163, textColor[client.publicChatMode], true);
		client.smallFont.drawCenteredEffectString(text[client.privateChatMode], 205, y + 163, textColor[client.privateChatMode], true);
		client.smallFont.drawCenteredEffectString(text[client.clanChatMode], 260, y + 163, textColor[client.clanChatMode], true);
		client.smallFont.drawCenteredEffectString(text[client.tradeMode], 318, y + 163, textColor[client.tradeMode], true);
	}
	
	/**
	 * Displays the orb.
	 */
	private void displayOrb(int x, int y, int orb, boolean hover) {
		ImageCache.get(hover && client.mouseInRegion(x, y, x + 57, y + 33) ? 1922 : 1921).drawImage(x, y);
		client.smallFont.drawCenteredEffectString(OrbHandler.getValue(orb), x + 15, y + 26, OrbHandler.getColor(orb), true);
		ImageCache.get(OrbHandler.getOrb(orb)).drawImage(x + 27, y + 3);
		Rasterizer2D.setClip(x + 27, y + 3, x + 58, y + 3 + OrbHandler.getFill(orb, 27));
		ImageCache.get(60).drawImage(x + 27, y + 3);
		Rasterizer2D.removeClip();
		if(orb != Constants.ORB_HEALTH || OrbHandler.getPercent(orb) > 20 || OrbHandler.getPercent(orb) < 1 || client.loopCycle % 20 > 10) {
			ImageCache.get(orb == Constants.ORB_RUN && OrbHandler.runEnabled ? 74 : 61 + orb).drawImage(x + 41 - ImageCache.get(orb == Constants.ORB_RUN && OrbHandler.runEnabled ? 74 : 61 + orb).imageWidth / 2, y + 17 - ImageCache.get(orb == Constants.ORB_RUN && OrbHandler.runEnabled ? 74 : 61 + orb).imageHeight / 2);
		}
	}

	/**
	 * Displays the selected tab.
	 */
	private void displaySelectedTabHighlight() {
		if(client.olderTabInterfaces[client.invTab] == -1) {
			return;
		}
		for(int i = 0; i < 14; i++) {
			if(client.invTab == i && client.showTab) {
				if(client.windowWidth < 980) {
					if(i < 7) {
						ImageCache.get(45).drawImage((client.windowWidth - 231) + 33 * i, client.windowHeight - 72);
					} else {
						ImageCache.get(45).drawImage((client.windowWidth - 231) + 33 * (i - 7), client.windowHeight - 36);
					}
				} else {
					ImageCache.get(45).drawImage((client.windowWidth - 462) + 33 * i, client.windowHeight - 36);
				}
			}
		}
	}

	/**
	 * Displays the side icons.
	 */
	private void displaySideIcons() {
		if(client.windowWidth < 980) {
			for(int i = 0; i < 14; i++) {
				if(client.olderTabInterfaces[i] != -1 || SettingPanel.selectedBinding != -1) {
					int xOffset = 16 - client.sideIcons[i].imageWidth / 2;
					int yOffset = 18 - client.sideIcons[i].imageHeight / 2;
					if(i < 7) {
						client.sideIcons[i].drawImage(xOffset + client.windowWidth - 231 + 33 * i, yOffset + client.windowHeight - 72);
					} else {
						client.sideIcons[i].drawImage(xOffset + client.windowWidth - 231 + 33 * (i - 7), yOffset + client.windowHeight - 36);
					}
				}
			}
		} else {
			for(int i = 0; i < 14; i++) {
				int xOffset = 16 - client.sideIcons[i].imageWidth / 2;
				int yOffset = 18 - client.sideIcons[i].imageHeight / 2;
				if(client.olderTabInterfaces[i] != -1 || SettingPanel.selectedBinding != -1) {
					client.sideIcons[i].drawImage(xOffset + client.windowWidth - 462 + 33 * i, yOffset + client.windowHeight - 36);
				}
			}
		}
	}
}
