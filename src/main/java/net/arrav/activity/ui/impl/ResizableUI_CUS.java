package net.arrav.activity.ui.impl;

import net.arrav.Client;
import net.arrav.Constants;
import net.arrav.activity.ui.util.CounterHandler;
import net.arrav.activity.ui.util.OrbHandler;
import net.arrav.cache.unit.DeformSequence;
import net.arrav.world.model.Player;
import net.arrav.Config;
import net.arrav.activity.panel.impl.SettingPanel;
import net.arrav.activity.ui.UIComponent;
import net.arrav.cache.unit.interfaces.Interface;
import net.arrav.cache.unit.NPCType;
import net.arrav.world.model.Model;
import net.arrav.world.model.NPC;
import net.arrav.graphic.Rasterizer2D;
import net.arrav.graphic.Rasterizer3D;
import net.arrav.graphic.font.BitmapFont;
import net.arrav.graphic.img.BitmapImage;
import net.arrav.util.collect.LinkedDeque;
import net.arrav.util.string.StringUtils;

import java.awt.*;

public class ResizableUI_CUS extends ResizableUI {

	/**
	 * Alpha/Opacity Value declared for chatbox fading.
	 */
	private int alpha = 0;

	private int chatboxHeight = 230;
	private int chatboxResizeClickY = -1;
	private int chatboxResizeFromY = -1;


	@Override
	public void buildChat() {
		final String[] modes = {"View", "On", "Friends", "Off", "Hide"};
		if(client.mouseX >= 57 && client.mouseX <= 113 && client.mouseY >= client.windowHeight - 21 && client.mouseY <= client.windowHeight) {
			client.menuItemName[1] = "View All";
			client.menuItemCode[1] = 999;
			client.menuPos = 2;
			client.hoveredChannelButton = 0;
		} else if(client.mouseX >= 114 && client.mouseX <= 170 && client.mouseY >= client.windowHeight - 21 && client.mouseY <= client.windowHeight) {
			client.menuItemName[1] = "View Game";
			client.menuItemCode[1] = 998;
			client.menuPos = 2;
			client.hoveredChannelButton = 1;
		} else if(client.mouseX >= 171 && client.mouseX <= 227 && client.mouseY >= client.windowHeight - 21 && client.mouseY <= client.windowHeight) {
			for(int i = 0; i <= 4; i++) {
				client.menuItemName[i + 1] = modes[4 - i] + " public";
				client.menuItemCode[i + 1] = 997 - i;
			}
			client.menuPos = 6;
			client.hoveredChannelButton = 2;
		} else if(client.mouseX >= 228 && client.mouseX <= 284 && client.mouseY >= client.windowHeight - 21 && client.mouseY <= client.windowHeight) {
			for(int i = 0; i <= 3; i++) {
				client.menuItemName[i + 1] = modes[3 - i] + " private";
				client.menuItemCode[i + 1] = 992 - i;
			}
			client.menuPos = 5;
			client.hoveredChannelButton = 3;
		} else if(client.mouseX >= 285 && client.mouseX <= 341 && client.mouseY >= client.windowHeight - 21 && client.mouseY <= client.windowHeight) {
			for(int i = 0; i <= 3; i++) {
				client.menuItemName[i + 1] = modes[3 - i] + " clan chat";
				client.menuItemCode[i + 1] = 1003 - i;
			}
			client.menuPos = 5;
			client.hoveredChannelButton = 4;
		} else if(client.mouseX >= 342 && client.mouseX <= 396 && client.mouseY >= client.windowHeight - 21 && client.mouseY <= client.windowHeight) {
			for(int i = 0; i <= 3; i++) {
				client.menuItemName[i + 1] = modes[3 - i] + " trade";
				client.menuItemCode[i + 1] = 987 - i;
			}
			client.menuPos = 5;
			client.hoveredChannelButton = 5;
		} else if(client.hoveredChannelButton != -1) {
			client.hoveredChannelButton = -1;
		}
		if(client.forcedChatWidgetId == -1) {
			client.chatWidget.scrollPos = client.chatContentHeight - client.chatScrollPos - chatboxHeight;
			if(client.mouseX > 373 && client.mouseX < 399 && client.mouseY > client.windowHeight - chatboxHeight - 38) {
				client.gameActivity.processScrollbar(373, client.windowHeight - chatboxHeight - 38, client.mouseX, client.mouseY, chatboxHeight + 40, client.chatContentHeight + 40, client.chatWidget, true);
			}
			if(client.leftClickInRegion(372, client.windowHeight - chatboxHeight - 38, 401, client.windowHeight - chatboxHeight)) {
				client.chatScrollPos += 4;
			}
			int i = client.chatContentHeight - chatboxHeight - client.chatWidget.scrollPos;
			if(i < 0) {
				i = 0;
			}
			if(i > client.chatContentHeight - chatboxHeight) {
				i = client.chatContentHeight - chatboxHeight;
			}
			if(client.chatScrollPos != i) {
				client.chatScrollPos = i;
			}
		}
		if(client.mouseWheelX < 400 && client.mouseWheelY > client.windowHeight - chatboxHeight) {
			int pos = client.chatScrollPos;
			pos -= client.mouseWheelAmt * 30;
			if(pos < 0) {
				pos = 0;
			}
			if(pos > client.chatContentHeight - chatboxHeight) {
				pos = client.chatContentHeight - chatboxHeight;
			}
			if(client.chatScrollPos != pos) {
				client.chatScrollPos = pos;
			}
		}
	}

	@Override
	public void updateChat() {
		if(Config.def.orbs())
			drawFace(30, 44);
		//Initializing
		int y = client.windowHeight - 185;
		//Closing/opening
		if(client.leftClickInRegion(0, client.windowHeight - 45, 52, client.windowHeight)) {
			client.showChat = !client.showChat;
		}
		//Drawing
		if(client.messagePromptRaised || client.bankSearching || client.inputDialogState > 0 || client.chatBoxStatement != null || client.forcedChatWidgetId != -1 || client.chatWidgetId != -1) {
			alpha += (alpha > 250 ? 0 : 10);
			Client.spriteCache.get(1665).drawImage(13, client.windowHeight - 195, alpha);
		} else {
			alpha -= (alpha > 1 ? 10 : 0);
			if(client.showChat) {
				displayChannelButtons();
				Client.spriteCache.get(215).drawImage(2, client.windowHeight - chatboxHeight - 60);
				Client.spriteCache.get(5).drawImage(0, client.windowHeight - chatboxHeight - 85);
				Rasterizer2D.setClip(0, client.windowHeight - chatboxHeight - 25, 406, client.windowHeight - 64);
				for(int i = client.windowHeight - chatboxHeight - 25; i < client.windowHeight - 64; i += 7) {
					Client.spriteCache.get(228).drawImage(0, i);
				}
				Rasterizer2D.removeClip();
				Client.spriteCache.get(229).drawImage(0, client.windowHeight - 64);
				Rasterizer2D.fillRectangle(27, client.windowHeight - chatboxHeight - 55, 334, 193, 0xccbb9a, alpha);
			}
		}
		if(client.messagePromptRaised) {
			client.boldFont.drawCenteredString(client.promptInputTitle, 270, y + 60, 0xFFBF00);
			client.boldFont.drawCenteredString(client.promptInput + "*", 270, y + 90, 0xffffff);
		} else if(client.inputDialogState == 1) {
			client.boldFont.drawCenteredString("Enter amount:", 270, y + 60, 0xFFBF00);
			client.boldFont.drawCenteredString(client.amountOrNameInput + client.outBoundInput + "*", 270, y + 90, 0xffffff);
		} else if(client.inputDialogState == 2) {
			client.boldFont.drawCenteredString("Enter name:", 270, y + 60, 0xFFBF00);
			client.boldFont.drawCenteredString(client.amountOrNameInput + client.outBoundInput + "*", 270, y + 90, 0xffffff);
		} else if(client.chatBoxStatement != null) {
			client.boldFont.drawCenteredString(client.chatBoxStatement, 270, y + 60, 0xFFBF00);
			client.boldFont.drawCenteredString("Click to continue", 270, y + 90, 0xffffff);
		} else if(client.forcedChatWidgetId != -1) {
			client.drawWidget(Interface.cache[client.forcedChatWidgetId], 20, y + 40, 0, UIComponent.CHAT);
		} else if(client.chatWidgetId != -1) {
			client.drawWidget(Interface.cache[client.chatWidgetId], 20, y + 40, 0, UIComponent.CHAT);
		} else if(client.bankSearching) {
			client.boldFont.drawCenteredString("What are you looking for?", 259, y + 60, 0);
			client.boldFont.drawCenteredString(client.bankSearch + "*", 259, y + 80, 128);
		} else if(client.showChat) {
			int totalMessages = 0;
			Rasterizer2D.setClip(30, client.windowHeight - chatboxHeight - 54, 363, client.windowHeight - 63);
			int basicFontColor = 0xffffff;
			int blueFontColor = 0x7fa9ff;
			int redFontColor = 0xff5256;
			int purpleFontColor = 0xff78d9;
			int orangeFontColor = 0xff8c38;
			int view = client.chatTypeView;
			for(int i = 0; i < 500; i++) {
				if(client.chatMessage[i] != null) {
					int xPos = 33;
					int yPos = (chatboxHeight - 31 * totalMessages) + client.chatScrollPos + client.windowHeight - chatboxHeight - 80;
					if(yPos < client.windowHeight - chatboxHeight - 139 || yPos > client.windowHeight - 52) {
						totalMessages++;
						continue;
					}
					int type = client.chatType[i];
					String name = client.chatAuthor[i];
					int rights = client.chatPriv[i];
					if(!client.uiRenderer.canSeeMessage(type, view, rights, name)) {
						continue;
					}
					String[] chatMessage = splitString(client.smallFont, name + ": ", client.chatMessage[i], 336, rights != 0, type);
					if(chatMessage.length > 1) {
						if(type == 0) {
							Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 330, 30, 0x000000, 100);
							client.smallFont.drawLeftAlignedEffectString(chatMessage[0], xPos, yPos, basicFontColor, 0);
							client.smallFont.drawLeftAlignedEffectString(chatMessage[1], xPos, yPos + 13, basicFontColor, 0);
						}
						if(type == 1) {
							Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 330, 30, 0x000000, 100);
							if(rights != 0) {
								Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 18, 30, 0x000000, 100);
								Client.spriteCache.get(1984 + rights - 1).drawImage(xPos, yPos - 5);
								xPos += 18;
							}
							client.smallFont.drawLeftAlignedEffectString(name + ": " + chatMessage[0], xPos, yPos, blueFontColor, 0);
							client.smallFont.drawLeftAlignedEffectString(chatMessage[1], xPos, yPos + 13, blueFontColor, 0);
						}
						if(type == 2) {
							Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 330, 30, 0x3C1414, 100);
							if(rights != 0) {
								Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 18, 30, 0x000000, 100);
								Client.spriteCache.get(1984 + rights - 1).drawImage(xPos, yPos - 5);
								xPos += 18;
							}
							client.smallFont.drawLeftAlignedEffectString("From: " + name + ":  " + chatMessage[0], xPos, yPos, redFontColor, 0);
							client.smallFont.drawLeftAlignedEffectString(chatMessage[1], xPos, yPos + 13, redFontColor, 0);
						}
						if(type == 4) {
							Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 330, 30, 0x000000, 100);
							client.smallFont.drawLeftAlignedEffectString(name + " " + chatMessage[0], xPos, yPos, purpleFontColor, 0);
							client.smallFont.drawLeftAlignedEffectString(chatMessage[1], xPos, yPos + 13, purpleFontColor, 0);
						}
						if(type == 5) {
							Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 330, 30, 0x000000, 100);
							client.smallFont.drawLeftAlignedEffectString(chatMessage[0], xPos, yPos, redFontColor, 0);
							client.smallFont.drawLeftAlignedEffectString(chatMessage[1], xPos, yPos + 13, redFontColor, 0);
						}
						if(type == 6) {
							if(rights != 0) {
								Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 18, 30, 0x000000, 100);
								Client.spriteCache.get(1984 + rights - 1).drawImage(xPos, yPos - 5);
								xPos += 18;
							}
							Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 330, 30, 0x3C1414, 100);
							client.smallFont.drawLeftAlignedEffectString("To " + name + ": " + chatMessage[0], xPos, yPos, redFontColor, 0);
							client.smallFont.drawLeftAlignedEffectString(chatMessage[1], xPos, yPos + 13, redFontColor, 0);
						}
						if(type == 8) {
							if(rights != 0) {
								Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 18, 30, 0x000000, 100);
								Client.spriteCache.get(1984 + rights - 1).drawImage(xPos, yPos - 5);
								xPos += 18;
							}
							Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 330, 30, 0x3C1414, 100);
							client.smallFont.drawLeftAlignedEffectString(name + ": " + chatMessage[0], xPos, yPos, orangeFontColor, 0);
							client.smallFont.drawLeftAlignedEffectString(chatMessage[1], xPos, yPos + 13, orangeFontColor, 0);
						}
						
					} else {
						if(type == 0) {
							Rasterizer2D.fillRectangle(xPos - 2, yPos - 13, 329, 30, 0x000000, 100);
							client.smallFont.drawLeftAlignedEffectString(chatMessage[0], xPos, yPos + 7, basicFontColor, 0);
						}
						if(type == 1) {
							Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 330, 30, 0x000000, 100);
							if(rights != 0) {
								Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 18, 30, 0x000000, 100);
								Client.spriteCache.get(1984 + rights - 1).drawImage(xPos, yPos - 5);
								xPos += 18;
							}
							client.smallFont.drawLeftAlignedEffectString(name + ": " + chatMessage[0], xPos, yPos + 7, blueFontColor, 0);
						}
						if(type == 2) {
							Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 330, 30, 0x3C1414, 100);
							if(rights != 0) {
								Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 18, 30, 0x000000, 100);
								Client.spriteCache.get(1984 + rights - 1).drawImage(xPos, yPos - 5);
								xPos += 18;
							}
							client.smallFont.drawLeftAlignedEffectString("From " + name + ":  " + chatMessage[0], xPos, yPos + 7, redFontColor, 0);
						}
						if(type == 4) {
							Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 330, 30, 0x000000, 100);
							client.smallFont.drawLeftAlignedEffectString(name + " " + chatMessage[0], xPos, yPos + 7, purpleFontColor, 0);
						}
						if(type == 6) {
							if(rights != 0) {
								Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 18, 30, 0x000000, 100);
								Client.spriteCache.get(1984 + rights - 1).drawImage(xPos, yPos - 5);
								xPos += 18;
							}
							Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 330, 30, 0x3C1414, 100);
							client.smallFont.drawLeftAlignedEffectString("To " + name + ": " + chatMessage[0], xPos, yPos + 7, redFontColor, 0);
						}
						if(type == 8) {
							if(rights != 0) {
								Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 18, 30, 0x000000, 100);
								Client.spriteCache.get(1984 + rights - 1).drawImage(xPos, yPos - 5);
								xPos += 18;
							}
							Rasterizer2D.fillRectangle(xPos - 3, yPos - 13, 330, 30, 0x3C1414, 100);
							client.smallFont.drawLeftAlignedEffectString(name + ": " + chatMessage[0], xPos, yPos + 7, orangeFontColor, 0);
						}
					}
					totalMessages++;
				}
			}
			Rasterizer2D.removeClip();
			client.chatContentHeight = totalMessages * 31 + 7 + 5;
			if(client.chatContentHeight < chatboxHeight) {
				client.chatContentHeight = chatboxHeight;
			}
			client.gameActivity.drawDarkScrollbar(374, client.windowHeight - chatboxHeight - 43, chatboxHeight - 2, client.chatContentHeight, client.chatContentHeight - client.chatScrollPos - chatboxHeight - 2);
			String myName;
			if(client.localPlayer != null && client.localPlayer.name != null) {
				myName = client.localPlayer.name;
			} else {
				myName = StringUtils.formatName(client.localUsername);
			}
			String[] message = splitString(client.smallFont, myName + ": ", client.chatInput, 325, false, 0);
			if(message.length > 1) {
				client.smallFont.drawLeftAlignedEffectString(myName + ": " + message[0], 36, y + 136, 0xffffff, 0);
				client.smallFont.drawLeftAlignedEffectString(message[1], 46, y + 148, 0xffffff, 0);
			} else {
				client.smallFont.drawLeftAlignedEffectString(myName + ": " + message[0], 36, y + 136, 0xffffff, 0);
			}
		}
		Client.spriteCache.get((client.mouseInRegion(0, client.windowHeight - 45, 52, client.windowHeight)) ? 3 : 4).drawImage(0, client.windowHeight - 50);
		client.gameGraphics.setCanvas();
	}

	@Override
	public void buildMap() {
		if(client.mouseInRegion(client.windowWidth - 180, 3, client.windowWidth - 145, 38)) {
			client.menuItemName[1] = "Face North";
			client.menuItemCode[1] = 1014;
			client.menuPos = 2;
		} else if(!client.panelHandler.isActive() && client.mouseInRegion(149, 57, 177, 86)) {
			client.menuItemName[1] = "Run";
			client.menuItemCode[1] = 1051;
			client.menuPos = 2;
		} else if(client.leftClickInRegion(client.windowWidth - 170, 120, client.windowWidth - 139, 151)) {
			if(client.panelHandler.isSettingOpen())
				client.panelHandler.close();
			else
				client.panelHandler.open(new SettingPanel());
		} else if(client.mouseX > client.windowWidth - 25 && client.mouseX < client.windowWidth && client.mouseY > 0 && client.mouseY < 25) {
			client.menuItemName[1] = "Logout";
			client.menuItemCode[1] = 1052;
			client.menuPos = 2;
		} else if(client.mouseInRegion(83, 56, 113, 88)) {
			client.menuPos = 0;
			client.menuItemName[client.menuPos] = "Select quick prayers";
			client.menuItemCode[client.menuPos] = 1054;
			client.menuPos++;
			client.menuItemName[client.menuPos] = "Turn quick prayers " + (OrbHandler.prayersEnabled ? "off" : "on");
			client.menuItemCode[client.menuPos] = 1053;
			client.menuPos++;
		} else if(client.mouseInRegion(client.windowWidth - 212, 5, client.windowWidth - 183, 32)) {
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
			return;
		}
		Client.spriteCache.get(1666).drawImage(client.windowWidth - 29, 0);
		int rotation = client.cameraAngleX + client.minimapAngle & 0x7ff;
		int middleX = 48 + client.localPlayer.x / 32;
		int middleY = 464 - client.localPlayer.y / 32;
		client.minimapImage.drawAffineTransformedImage(xOffset + 25, 5, 146, 151, middleX, middleY, client.minimapLineStarts, client.minimapLineLengths, rotation, 256 + client.minimapZoom);
		for(int i = 0; i < client.mapFunctionCount; i++) {
			int x = (client.mapFunctionX[i] * 4 + 2) - client.localPlayer.x / 32;
			int y = (client.mapFunctionY[i] * 4 + 2) - client.localPlayer.y / 32;
			markMinimap(client.mapFunctionImage[i], x, y);
		}
		for(int tileX = 0; tileX < 104; tileX++) {
			for(int tileY = 0; tileY < 104; tileY++) {
				LinkedDeque sceneItem = client.sceneItems[client.cameraPlane][tileX][tileY];
				if(sceneItem != null) {
					int x = (tileX * 4 + 2) - client.localPlayer.x / 32;
					int y = (tileY * 4 + 2) - client.localPlayer.y / 32;
					markMinimap(client.mapDotItem, x, y);
				}
			}
		}
		for(int i6 = 0; i6 < client.npcListSize; i6++) {
			NPC npc = client.npcList[client.npcEntryList[i6]];
			if(npc != null && npc.isVisible()) {
				NPCType npcDefinition = npc.type;
				if(npcDefinition.childrenIDs != null) {
					npcDefinition = npcDefinition.getSubNPCType();
				}
				if(npcDefinition != null && npcDefinition.visibleMinimap && npcDefinition.clickable) {
					int x = npc.x / 32 - client.localPlayer.x / 32;
					int y = npc.y / 32 - client.localPlayer.y / 32;
					markMinimap(client.mapDotNPC, x, y);
				}
			}
		}
		for(int j6 = 0; j6 < client.playerCount; j6++) {
			Player player = client.playerList[client.playerEntryList[j6]];
			if(player != null && player.isVisible()) {
				int x = player.x / 32 - client.localPlayer.x / 32;
				int y = player.y / 32 - client.localPlayer.y / 32;
				boolean friend = false;
				long l6 = StringUtils.encryptName(player.name);
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
				NPC npc = client.npcList[client.NPCHintID];
				if(npc != null) {
					int x = npc.x / 32 - client.localPlayer.x / 32;
					int y = npc.y / 32 - client.localPlayer.y / 32;
					method81(client.mapArrow, x, y);
				}
			}
			if(client.hintType == 2) {
				int x = ((client.anInt934 - client.baseX) * 4 + 2) - client.localPlayer.x / 32;
				int y = ((client.anInt935 - client.baseY) * 4 + 2) - client.localPlayer.y / 32;
				method81(client.mapArrow, x, y);
			}
			if(client.hintType == 10 && client.anInt933 >= 0 && client.anInt933 < client.playerList.length) {
				Player player = client.playerList[client.anInt933];
				if(player != null) {
					int x = player.x / 32 - client.localPlayer.x / 32;
					int y = player.y / 32 - client.localPlayer.y / 32;
					method81(client.mapArrow, x, y);
				}
			}
		}
		if(client.walkX != 0) {
			int x = (client.walkX * 4 + 2) - client.localPlayer.x / 32;
			int y = (client.walkY * 4 + 2) - client.localPlayer.y / 32;
			markMinimap(client.mapFlag, x, y);
		}
		Rasterizer2D.removeClip();
		Client.spriteCache.get(1643).drawImage(xOffset + 10, -2);
		Client.spriteCache.get(1644).drawImage(xOffset + 7, -5);
		Client.spriteCache.get(1651).drawImage(xOffset + 7, 110);
		Client.spriteCache.get(1700).drawAffineTransformedImage(xOffset + 18, 5, 33, 33, 25, 25, client.compassClipStarts, client.compassLineLengths, client.cameraAngleX, 256);
		Rasterizer2D.fillRectangle(xOffset + 97, 78, 3, 3, 0xffffff);
		if(Config.def.orbs()) {
			displayOrb(client.windowWidth - 209, 38, Constants.ORB_HEALTH, false);
			displayOrb(client.windowWidth - 212, 72, Constants.ORB_PRAYER, true);
			displayOrb(client.windowWidth - 200, 106, Constants.ORB_RUN, true);
			displayOrb(client.windowWidth - 177, 140, Constants.ORB_SUMMONING, true);
		}
		client.gameGraphics.setCanvas();
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
			Client.spriteCache.get(85).method353(94 + j2 + 4, 83 - k2 - 20, d);
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

	@Override
	public void buildInventory() {
		if(client.clickButton != 1) {
			return;
		}
		if(client.windowWidth < 1000) {
			if(client.clickX > client.windowWidth - 231 && client.clickY > client.windowHeight - 79) {
				int tabClick = (client.clickX - (client.windowWidth - 231)) / 38;
				if(client.clickY > client.windowHeight - 42) {
					tabClick += 6;
					if(client.mouseX > client.windowWidth - 76) {
						tabClick += 1;
					}
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
		} else if(client.clickX > client.windowWidth - 458 && client.clickY > client.windowHeight - 42) {
			final int tabClick = (client.clickX - (client.windowWidth - 458)) / 38 + (client.mouseX > client.windowWidth - 78 ? 1 : 0);
			if(SettingPanel.selectedBinding != -1) {
				SettingPanel.hotkeys[SettingPanel.selectedBinding] = tabClick;
				SettingPanel.selectedBinding = -1;
				return;
			}
			if(tabClick == client.invTab && client.showTab) {
				client.showTab = false;
			} else if(client.olderTabInterfaces[tabClick] != -1) {
				client.showTab = true;
				client.invTab = tabClick == 10 ? 11 : tabClick;
			}
		}
	}

	@Override
	public void updateInventory() {
		int xOffset = client.windowWidth - 197;
		int yOffset = client.windowHeight - 303;
		if(client.windowWidth < 1000) {
			Client.spriteCache.get(82).drawImage(xOffset - 39, yOffset + 218);
		} else {
			Client.spriteCache.get(81).drawImage(xOffset - 267, yOffset + 255);
		}
		if(client.invOverlayInterfaceID == -1) {
			displaySideIcons();
			displayHoveredTab();
			displaySelectedTabHighlight();
		}
		if(client.showTab) {
			if(client.windowWidth < 1000) {
				Rasterizer2D.fillRectangle(xOffset - 3, yOffset - 53, 196, 275, 0, 25);
				Rasterizer2D.fillRectangle(xOffset, yOffset - 50, 190, 269, 0, 100);
			} else {
				Rasterizer2D.fillRectangle(xOffset - 3, yOffset - 18, 196, 275, 0, 25);
				Rasterizer2D.fillRectangle(xOffset, yOffset - 15, 190, 269, 0, 100);
			}
			if(client.invOverlayInterfaceID != -1) {
				client.drawWidget(Interface.cache[client.invOverlayInterfaceID], xOffset, yOffset - (client.windowWidth < 1000 ? 42 : 0), 8, UIComponent.INVENTORY);
			} else if(client.olderTabInterfaces[client.invTab] != -1) {
				client.drawWidget(Interface.cache[client.olderTabInterfaces[client.invTab]], xOffset, yOffset - (client.windowWidth < 1000 ? 42 : 8), 0, UIComponent.INVENTORY);
			}
		}
	}

	@Override
	public Point getOnScreenWidgetOffsets() {
		return super.getOnScreenWidgetOffsets();
	}
	
	@Override
	public BitmapImage getSide(int index) {
		return index < 0 || index >= 12 ? null : Client.spriteCache.get(index + 216);
	}
	
	/**
	 * Displays the chat channel buttons.
	 */
	private void displayChannelButtons() {
		int y = client.windowHeight - 165;
		String text[] = {"On", "Friends", "Off", "Hide"};
		int textColor[] = {65280, 0xffff00, 0xff0000, 65535};
		for(int i = 0; i <= 5; i++) {
			Client.spriteCache.get(1642).drawImage(57 + 57 * i, y + 132);
			if(client.selectedChannelButton == i && (client.showChat)) {
				Rasterizer2D.fillRectangle(66 + 57 * i, y + 140, 48, 20, 0xffffff, 50);
				//Client.spriteCache.get(1642).drawImage(5 + 57 * i, y + 142);
			}
			if(i == client.hoveredChannelButton) {
				Rasterizer2D.fillRectangle(66 + 57 * i, y + 140, 48, 20, 0xffffff, 50);
			}
		}
		client.smallFont.drawLeftAlignedEffectString("All", 84, y + 155, 0xffffff, 0);
		client.smallFont.drawLeftAlignedEffectString("Game", 133, y + 155, 0xffffff, 0);
		client.smallFont.drawLeftAlignedEffectString("Public", 188, y + 150, 0xffffff, 0);
		client.smallFont.drawLeftAlignedEffectString("Private", 242, y + 150, 0xffffff, 0);
		client.smallFont.drawLeftAlignedEffectString("Clan", 307, y + 150, 0xffffff, 0);
		client.smallFont.drawLeftAlignedEffectString("Trade", 360, y + 150, 0xffffff, 0);
		client.smallFont.drawCenteredEffectString(text[client.publicChatMode], 203, y + 161, textColor[client.publicChatMode], 0);
		client.smallFont.drawCenteredEffectString(text[client.privateChatMode], 261, y + 161, textColor[client.privateChatMode], 0);
		client.smallFont.drawCenteredEffectString(text[client.clanChatMode], 316, y + 161, textColor[client.clanChatMode], 0);
		client.smallFont.drawCenteredEffectString(text[client.tradeMode], 374, y + 161, textColor[client.tradeMode], 0);
		//game.smallFont.drawCenteredString(374, y + 163, text[game.duelMode], tetColor[game.duelMode], 0);
	}

	/**
	 * Displays the hovered tab.
	 */
	private void displayHoveredTab() {
		if(client.windowWidth < 1000) {
			if(client.mouseY < client.windowHeight - 46 && client.mouseY > client.windowHeight - 79) {
				for(int i = 6; i >= 1; i--) {
					if(client.windowWidth - 265 < client.mouseX - (i * 38)) {
						if(client.olderTabInterfaces[i - 1] != -1) {
							Rasterizer2D.fillRectangle((client.windowWidth - 266) + (38 * i) - (i > 4 ? 1 : 0), client.windowHeight - 77, 31, 31, 0xFFFFFF, 80);
						}
						return;
					}
				}
			} else if(client.mouseY > client.windowHeight - 37) {
				for(int i = 6; i >= 1; i--) {
					if(client.windowWidth - 265 < client.mouseX - (i * 38)) {
						if(client.olderTabInterfaces[i + 5] != -1) {
							Rasterizer2D.fillRectangle((client.windowWidth - 266) + (38 * i) - (i > 4 ? 1 : 0), client.windowHeight - 40, 31, 31, 0xFFFFFF, 80);
						}
						return;
					}
				}
			}
		} else {
			if(client.mouseY > client.windowHeight - 42) {
				for(int i = 12; i >= 1; i--) {
					if(client.windowWidth - 495 < client.mouseX - (i * 38)) {
						if(client.olderTabInterfaces[i - 1] != -1) {
							Rasterizer2D.fillRectangle((client.windowWidth - 495) + (38 * i), client.windowHeight - 40, 31, 31, 0xFFFFFF, 80);
						}
						return;
					}
				}
			}
		}
	}

	/**
	 * Displays the selected tab.
	 */
	private void displaySelectedTabHighlight() {
		if(client.olderTabInterfaces[client.invTab] == -1) {
			return;
		}
		int i = client.invTab;

		if(client.showTab) {
			if(i > 10) {
				i -= 1;
			}
			if(client.windowWidth < 1000) {
				if(i < 6) {
					Rasterizer2D.fillRectangle((client.windowWidth - 228) + 38 * i, client.windowHeight - 77, 31, 31, 0xFFFFFF, 80);
				} else {
					Rasterizer2D.fillRectangle((client.windowWidth - 228) + 38 * (i - 6), client.windowHeight - 40, 31, 31, 0xFFFFFF, 80);
				}
			} else {
				Rasterizer2D.fillRectangle((client.windowWidth - 456) + 38 * i, client.windowHeight - 40, 31, 31, 0xFFFFFF, 80);
			}
		}
	}

	/**
	 * Displays the side icons.
	 */
	private void displaySideIcons() {
		if(client.windowWidth < 1000) {
			for(int i = 0; i < 12; i++) {
				if(client.olderTabInterfaces[i] != -1 || SettingPanel.selectedBinding != -1) {
					int xOffset = 16 - Client.spriteCache.get(i + 22).imageWidth / 2;
					int yOffset = 18 - Client.spriteCache.get(i + 22).imageHeight / 2;
					if(i <= 5) {
						Client.spriteCache.get(216 + i).drawImage((i == 5 ? -3 : 0) + xOffset + client.windowWidth - 235 + 38 * i, yOffset + client.windowHeight - 85);
					} else {
						Client.spriteCache.get(216 + i).drawImage(xOffset + client.windowWidth - 235 + 38 * (i - 6), yOffset + client.windowHeight - 48);
					}
				}
			}
		} else {
			for(int i = 0; i < 12; i++) {
				int xOffset = 16 - Client.spriteCache.get(i + 22).imageWidth / 2;
				int yOffset = 18 - Client.spriteCache.get(i + 22).imageHeight / 2;
				if(client.olderTabInterfaces[i] != -1 || SettingPanel.selectedBinding != -1) {
					Client.spriteCache.get(216 + i).drawImage(xOffset + client.windowWidth - 463 + 38 * i, yOffset + client.windowHeight - 48);
				}
			}
		}
	}

	/**
	 * Displays the orb.
	 */
	private void displayOrb(int x, int y, int orb, boolean hover) {
		if(orb == Constants.ORB_HEALTH) {
			Client.spriteCache.get(1647).drawImage(50, 17);
			Client.spriteCache.get(1650).drawImage(50, 47);
			if(OrbHandler.getPercent(orb) > 20 || OrbHandler.getPercent(orb) < 1 || client.loopCycle % 20 > 10) {
				Rasterizer2D.setClip(60, 27, 60 - OrbHandler.getFill(orb, 192) + 192, 39);
				Client.spriteCache.get(OrbHandler.poisoned ? 1875 : 1648).drawImage(60, 27);
				Rasterizer2D.removeClip();
				Client.spriteCache.get(OrbHandler.poisoned ? 1874 : 1649).drawImage(208 - OrbHandler.getFill(orb, 192), 23);
			}
			client.smallFont.drawLeftAlignedEffectString(client.currentStats[3] + " / " + client.maxStats[3] * 10, 90, 38, 0xffffff, 0);
			return;
		}
		int move = orb == Constants.ORB_RUN ? orb * 68 : orb == Constants.ORB_SUMMONING ? orb * 68 : orb * 70;
		Client.spriteCache.get(1645).drawImage(move, 42);
		Client.spriteCache.get(OrbHandler.getOrb(orb)).drawImage(12 + move, 54);
		Rasterizer2D.setClip(12 + move, 54, 43 + move, 54 + OrbHandler.getFill(orb, 27));
		Client.spriteCache.get(60).drawImage(12 + move, 54);
		Rasterizer2D.removeClip();
		Client.spriteCache.get(61 + orb).drawImage(25 + move - Client.spriteCache.get(61 + orb).imageWidth / 2, 67 - Client.spriteCache.get(61 + orb).imageHeight / 2);
		//Client.spriteCache.get(hover && client.mouseInRegion(x, y, x + 57, y + 33) ? 51 : 1645).drawImage(x - 6, y - 5);
		client.smallFont.drawLeftAlignedEffectString(OrbHandler.getValue(orb), 50 + move, 69, OrbHandler.getColor(orb), 0);

	}

	public static void drawFace(int xPos, int yPos) {
		Client.spriteCache.get(1646).drawImage(xPos - 31, yPos - 31);
		Interface childWidget = Interface.cache[250];
		if(client.forcedChatWidgetId == -1) {
			if(childWidget.modelAnim != 9805) {
				childWidget.modelAnimLength = 0;
				childWidget.modelAnimDelay = 0;
			}
			childWidget.modelAnim = 9805;
		}
		childWidget.modelType = 3;
		final int centerX = Rasterizer3D.viewport.centerX;
		final int centerY = Rasterizer3D.viewport.centerY;
		Rasterizer3D.viewport.centerX = xPos + childWidget.width / 2;
		Rasterizer3D.viewport.centerY = yPos + childWidget.height / 2;
		final int i5 = Rasterizer3D.angleSine[childWidget.modelYaw] * childWidget.modelZoom >> 16;
		final int l5 = Rasterizer3D.angleCosine[childWidget.modelYaw] * childWidget.modelZoom >> 16;
		final boolean isSelected = false;
		int animationID = childWidget.modelAnim;
		Model model;
		if(animationID == -1) {
			model = childWidget.getModel(-1, -1, isSelected);
		} else {
			final DeformSequence animation = DeformSequence.animations[animationID];
			if(childWidget.modelAnimLength > animation.anIntArray354.length || childWidget.modelAnimLength > animation.frameList.length) {
				return;
			}
			model = childWidget.getModel(animation.anIntArray354[childWidget.modelAnimLength], animation.frameList[childWidget.modelAnimLength], isSelected);
		}
		if(model != null) {
			model.drawModel(childWidget.modelRoll, 0, childWidget.modelYaw, 0, i5, l5);
		}
		Rasterizer2D.removeClip();
		Rasterizer3D.viewport.centerX = centerX;
		Rasterizer3D.viewport.centerY = centerY;
		final DeformSequence animation = DeformSequence.animations[childWidget.modelAnim];
		for(childWidget.modelAnimDelay += client.tickDelta; childWidget.modelAnimDelay > animation.getFrame(childWidget.modelAnimLength); ) {
			childWidget.modelAnimDelay -= animation.getFrame(childWidget.modelAnimLength) + 1;
			childWidget.modelAnimLength++;
			if(childWidget.modelAnimLength >= animation.length) {
				childWidget.modelAnimLength -= animation.animCycle;
				if(childWidget.modelAnimLength < 0 || childWidget.modelAnimLength >= animation.length) {
					childWidget.modelAnimLength = 0;
				}
			}
		}
	}

	/**
	 * Cuts a string into more than one line if it exceeds the specified max width.
	 * @param font
	 * @param string
	 * @param maxWidth
	 * @return
	 */
	public static String[] splitString(BitmapFont font, String prefix, String string, int maxWidth, boolean ranked, int type) {
		int w = 0;
		if(type == 2)
			w = 35;
		maxWidth -= font.getStringWidth(prefix) + (ranked ? 30 : 0) + w;
		if(font.getStringWidth(prefix + string) + (ranked ? 30 : 0) <= maxWidth) {
			return new String[]{string};
		}
		String line = "";
		String[] cut = new String[2];
		boolean split = false;
		char[] characters = string.toCharArray();
		int space = -1;
		for(int index = 0; index < characters.length; index++) {
			char c = characters[index];
			line += c;
			if(c == ' ') {
				space = index;
			}
			if(!split) {
				if(font.getStringWidth(line) + 10 > maxWidth) {
					if(space != -1 && characters[index - 1] != ' ') {
						cut[0] = line.substring(0, space);
						line = line.substring(space);
					} else {
						cut[0] = line;
						line = "";
					}
					split = true;
				}
			}
		}
		if(line.length() > 0) {
			cut[1] = line;
		}
		return cut;
	}

	@Override
	public boolean allowScene() {
		if(client.showChat && !(client.messagePromptRaised || client.bankSearching || client.inputDialogState > 0 || client.chatBoxStatement != null || client.forcedChatWidgetId != -1 || client.chatWidgetId != -1)) {
			if(client.mouseDragButton != 1) {
				chatboxResizeClickY = -1;
				if(chatboxHeight > client.windowHeight - 170) {
					chatboxHeight = client.windowHeight - 170;
				}
			} else if(chatboxResizeClickY == -1 && client.clickX >= 2 && client.clickY >= client.windowHeight - chatboxHeight - 60 &&
					client.clickX <= 19 && client.clickY <= client.windowHeight - chatboxHeight - 48) {
				chatboxResizeFromY = chatboxHeight;
				chatboxResizeClickY = client.clickY;
			}
			if(chatboxResizeClickY != -1) {
				chatboxHeight = chatboxResizeFromY + (chatboxResizeClickY - client.mouseY);
				if(chatboxHeight < 84) {
					chatboxHeight = 84;
				}
				return false;
			}
		}
		if(client.mouseX >= (client.windowWidth < 980 ? client.windowWidth - 235 : client.windowWidth - 466) && client.mouseY >= (client.windowWidth < 980 ? client.windowHeight - 82 : client.windowHeight - 45)) {
			return false;
		}
		if(client.mouseX >= client.windowWidth - 204 && client.mouseY >= (client.windowWidth < 980 ? client.windowHeight - 352 : client.windowHeight - 320) && client.showTab) {
			return false;
		}
		boolean chat = (client.messagePromptRaised || client.bankSearching || client.inputDialogState > 0 || client.chatBoxStatement != null || client.forcedChatWidgetId != -1 || client.chatWidgetId != -1);
		if((client.showChat || chat) && client.mouseX >= 0 && client.mouseX <= (chat ? 515 : 405) && client.mouseY >= client.windowHeight - (chat ? 190 : (chatboxHeight + 65))) {
			return false;
		}
		if(!(client.showChat || chat) && client.mouseX >= 0 && client.mouseX <= 55 && client.mouseY >= client.windowHeight - 65) {
			return false;
		}
		return !(client.mouseY >= 0 && client.mouseY <= 165 & client.mouseX >= client.windowWidth - 182) && client.mouseX >= 0 && client.mouseY >= 0;
	}

	@Override
	public void buildSceneOverlay() {
	}
}
