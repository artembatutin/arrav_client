package net.arrav.activity;

import net.arrav.Config;
import net.arrav.Constants;
import net.arrav.activity.ui.UIComponent;
import net.arrav.activity.ui.util.CombatOverlayHandler;
import net.arrav.world.Scene;
import net.arrav.world.emitter.Particle;
import net.arrav.graphic.Rasterizer2D;
import net.arrav.graphic.Rasterizer3D;
import net.arrav.net.SignLink;
import net.arrav.util.collect.LinkedDeque;
import net.arrav.Client;
import net.arrav.activity.ui.util.AssetDrawer;
import net.arrav.activity.ui.util.CounterHandler;
import net.arrav.cache.unit.*;
import net.arrav.world.model.*;
import net.arrav.graphic.GraphicalComponent;
import net.arrav.graphic.font.BitmapFont;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class GameActivity extends Activity {

	public AssetDrawer drawer = new AssetDrawer(client);

	@Override
	public boolean process() {
		if(client.systemUpdateTimer > 1) {
			client.systemUpdateTimer--;
		}
		if(client.anInt1011 > 0) {
			client.anInt1011--;
		}
		for(int i = 0; i < 10; i++) {
			if(!client.parsePacket()) {
				break;
			}
		}
		if(!client.loggedIn) {
			return false;
		}
		if(client.panelHandler.isActive()) {
			if(client.panelHandler.process()) {
				return false;
			}
		}
		synchronized(client.mouseDetection.synchronizer) {
			if(client.flagged && client.panelHandler.action()) {
				if(client.clickButton != 0 || client.mouseDetection.index >= 40) {
					final int j2 = client.outBuffer.pos;
					int j3 = 0;
					for(int j4 = 0; j4 < client.mouseDetection.index; j4++) {
						if(j2 - client.outBuffer.pos >= 240) {
							break;
						}
						j3++;
						int l4 = client.mouseDetection.mouseY[j4];
						if(l4 < 0) {
							l4 = 0;
						} else if(l4 > 502) {
							l4 = 502;
						}
						int k5 = client.mouseDetection.mouseX[j4];
						if(k5 < 0) {
							k5 = 0;
						} else if(k5 > 764) {
							k5 = 764;
						}
						int i6 = l4 * 765 + k5;
						if(client.mouseDetection.mouseY[j4] == -1 && client.mouseDetection.mouseX[j4] == -1) {
							k5 = -1;
							l4 = -1;
							i6 = 0x7ffff;
						}
						if(k5 == client.anInt1237 && l4 == client.anInt1238) {
							if(client.anInt1022 < 2047) {
								client.anInt1022++;
							}
						} else {
							int j6 = k5 - client.anInt1237;
							client.anInt1237 = k5;
							int k6 = l4 - client.anInt1238;
							client.anInt1238 = l4;
							if(client.anInt1022 < 8 && j6 >= -32 && j6 <= 31 && k6 >= -32 && k6 <= 31) {
								j6 += 32;
								k6 += 32;
								client.outBuffer.putShort((client.anInt1022 << 12) + (j6 << 6) + k6);
								client.anInt1022 = 0;
							} else if(client.anInt1022 < 8) {
								client.outBuffer.putMedium(0x800000 + (client.anInt1022 << 19) + i6);
								client.anInt1022 = 0;
							} else {
								client.outBuffer.putInt(0xc0000000 + (client.anInt1022 << 19) + i6);
								client.anInt1022 = 0;
							}
						}
					}
					client.outBuffer.putLengthAfterwards(client.outBuffer.pos - j2);
					if(j3 >= client.mouseDetection.index) {
						client.mouseDetection.index = 0;
					} else {
						client.mouseDetection.index -= j3;
						for(int i5 = 0; i5 < client.mouseDetection.index; i5++) {
							client.mouseDetection.mouseX[i5] = client.mouseDetection.mouseX[i5 + j3];
							client.mouseDetection.mouseY[i5] = client.mouseDetection.mouseY[i5 + j3];
						}

					}
				}
			} else {
				client.mouseDetection.index = 0;
			}
		}
		if(client.clickButton != 0 && client.panelHandler.action()) {
			client.aLong1220 = client.clickTime;
		}
		if(client.anInt1016 > 0) {
			client.anInt1016--;
		}
		if(client.keyPressed[1] == 1 || client.keyPressed[2] == 1 || client.keyPressed[3] == 1 || client.keyPressed[4] == 1) {
			client.cameraMoved = true;
		}
		if(client.cameraMoved && client.anInt1016 <= 0) {
			client.anInt1016 = 20;
			client.cameraMoved = false;
			client.outBuffer.putOpcode(86);
			client.outBuffer.putShort(client.mapVerticalRotation);
			client.outBuffer.putShortPlus128(client.cameraAngleX);
		}
		if(client.awtFocus && !client.aBoolean954) {
			client.aBoolean954 = true;
			client.outBuffer.putOpcode(3);
			client.outBuffer.putByte(1);
		}
		if(!client.awtFocus && client.aBoolean954) {
			client.aBoolean954 = false;
			client.outBuffer.putOpcode(3);
			client.outBuffer.putByte(0);
		}
		client.loadingStages();
		client.method115();
		client.method114();
		client.method95();
		client.method38();
		client.anInt945++;
		if(client.crossType != 0) {
			client.crossIndex += 20;
			if(client.crossIndex >= 400) {
				client.crossType = 0;
			}
		}
		if(client.atInventoryInterfaceType != 0) {
			client.atInventoryLoopCycle++;
			if(client.atInventoryLoopCycle >= 15) {
				if(client.atInventoryInterfaceType == 3) {
					client.atInventoryInterfaceType = 0;
				}
			}
		}
		if(client.activeInterfaceType != 0) {
			client.itemPressTimer++;
			if(client.mouseX > client.itemPressX + 5 || client.mouseX < client.itemPressX - 5 || client.mouseY > client.itemPressY + 5 || client.mouseY < client.itemPressY - 5) {
				client.draggingItem = true;
			}
			if(client.activeInterfaceType == 5) {
				client.determineMenuSize();
				client.activeInterfaceType = 0;
			} else if(client.mouseDragButton == 0) {
				if(client.activeInterfaceType != 5)
					client.activeInterfaceType = 0;
				if(client.draggingItem && client.itemPressTimer >= 10) {
					client.lastActiveInvInterface = -1;
					processRightClick();
					if(client.lastActiveInvInterface == client.invWidgetId && client.invDestSlot != client.invSrcSlot) {
						final Interface widget = Interface.cache[client.invWidgetId];
						int swapMode = 0;
						if(client.anInt913 == 1 && widget.contentType == 206) {
							swapMode = 1;
						}
						if(widget.invId[client.invDestSlot] <= 0) {
							swapMode = 0;
						}
						if(widget.invMove) {
							int l2 = client.invSrcSlot;
							int l3 = client.invDestSlot;
							widget.invId[l3] = widget.invId[l2];
							widget.invAmt[l3] = widget.invAmt[l2];
							widget.invId[l2] = -1;
							widget.invAmt[l2] = 0;
						} else if(swapMode == 1) {
							int src = client.invSrcSlot;
							for(int dest = client.invDestSlot; src != dest; ) {
								if(src > dest) {
									widget.invSwap(src, src - 1);
									src--;
								} else if(src < dest) {
									widget.invSwap(src, src + 1);
									src++;
								}
							}

						} else {
							widget.invSwap(client.invSrcSlot, client.invDestSlot);
						}
						client.outBuffer.putOpcode(214);
						client.outBuffer.putLitEndShortPlus128(client.invWidgetId);
						client.outBuffer.putLitEndShortPlus128(client.invSrcSlot);
						client.outBuffer.putLitEndShort(client.invDestSlot);
					}
				} else if((client.mouseButtonsToggle == 1 || client.menuHasAddFriend(client.menuPos - 1)) && client.menuPos > 2) {
					client.determineMenuSize();
				} else if(client.menuPos > 0) {
					client.executeMenuAction(client.menuPos - 1);
				}
				client.atInventoryLoopCycle = 10;
				client.clickButton = 0;
			}
		}
		if(Scene.hoverX != -1 && client.panelHandler.action()) {
			final int k = Scene.hoverX;
			final int k1 = Scene.hoverY;
			final boolean flag = client.findWalkingPath(0, 0, 0, 0, client.localPlayer.smallY[0], 0, 0, k1, client.localPlayer.smallX[0], true, k);
			Scene.hoverX = -1;
			if(flag) {
				client.crossX = client.clickX;
				client.crossY = client.clickY;
				client.crossType = 1;
				client.crossIndex = 0;
			}
		}
		if(client.clickButton == 1 && client.chatBoxStatement != null) {
			client.chatBoxStatement = null;
			client.clickButton = 0;
		}
		if(!client.menuOpened) {
			if(client.uiRenderer.isResizableOrFull()) {
				client.uiRenderer.resizable.buildInventory();
			}
			if(client.uiRenderer.isFixed()) {
				client.uiRenderer.fixed.buildInventory();
			}
		}
		if(!processMenuClick()) {
			processMainScreenClick();
		}
		if(client.mouseDragButton == 1 || client.clickButton == 1) {
			client.scrollBarChangeAmount++;
		}
		if(client.anInt1500 != 0 || client.anInt1044 != 0 || client.anInt1129 != 0) {
			if(client.anInt1501 < 50 && !client.menuOpened) {
				client.anInt1501++;
			}
		} else if(client.anInt1501 > 0) {
			client.anInt1501--;
		}
		if(client.loadingStage == 2) {
			updateCamera();
		}
		if(client.loadingStage == 2 && client.forcedCameraLocation) {
			client.calcForcedCameraPos();
		}
		for(int i1 = 0; i1 < 5; i1++) {
			client.anIntArray1030[i1]++;
		}

		client.method73();
		client.idleTime++;
		if(client.idleTime > 10000) {
			client.anInt1011 = 250;
			client.idleTime -= 500;
			client.outBuffer.putOpcode(202);
		}
		client.anInt988++;
		if(client.anInt988 > 500) {
			client.anInt988 = 0;
			final int random = (int) (Math.random() * 8D);
			if((random & 1) == 1) {
				client.cameraAdjustX += client.cameraOffsetXChange;
			}
			if((random & 2) == 2) {
				client.cameraAdjustY += client.cameraOffsetYChange;
			}
			if((random & 4) == 4) {
				client.cameraAngleAdjustX += client.cameraAngleOffsetXChange;
			}
		}
		if(client.cameraAdjustX < -50) {
			client.cameraOffsetXChange = 2;
		}
		if(client.cameraAdjustX > 50) {
			client.cameraOffsetXChange = -2;
		}
		if(client.cameraAdjustY < -55) {
			client.cameraOffsetYChange = 2;
		}
		if(client.cameraAdjustY > 55) {
			client.cameraOffsetYChange = -2;
		}
		if(client.cameraAngleAdjustX < -40) {
			client.cameraAngleOffsetXChange = 1;
		}
		if(client.cameraAngleAdjustX > 40) {
			client.cameraAngleOffsetXChange = -1;
		}
		client.anInt1254++;
		if(client.anInt1254 > 500) {
			client.anInt1254 = 0;
			final int random = (int) (Math.random() * 8D);
			if((random & 1) == 1) {
				client.minimapAngle += client.minimapAngleChange;
			}
			if((random & 2) == 2) {
				client.minimapZoom += client.minimapZoomChange;
			}
		}
		if(client.minimapAngle < -60) {
			client.minimapAngleChange = 2;
		}
		if(client.minimapAngle > 60) {
			client.minimapAngleChange = -2;
		}
		if(client.minimapZoom < -20) {
			client.minimapZoomChange = 1;
		}
		if(client.minimapZoom > 10) {
			client.minimapZoomChange = -1;
		}
		client.anInt1010++;
		if(client.anInt1010 > 50) {
			client.outBuffer.putOpcode(0);
		}
		try {
			if(client.socketStream != null && client.outBuffer.pos > 0) {
				client.socketStream.write(client.outBuffer.data, client.outBuffer.pos);
				client.outBuffer.pos = 0;
				client.anInt1010 = 0;
			}
		} catch(final IOException _ex) {
			client.dropClient();
		} catch(final Exception exception) {
			client.logOut();
		}
		return false;
	}

	@Override
	public void update() {
		if(client.fullscreenWidgetId != -1 && (client.loadingStage == 2)) {
			if(client.loadingStage == 2) {
				client.method119(client.anInt945, client.fullscreenWidgetId);
				if(client.openInterfaceID != -1) {
					client.method119(client.anInt945, client.openInterfaceID);
				}
				client.anInt945 = 0;
				Rasterizer3D.viewport = client.fixedFullScreenViewport;
				Rasterizer2D.clearCanvas();
				client.updateAllGraphics = true;
				if(client.openInterfaceID != -1) {
					final Interface widget = Interface.cache[client.openInterfaceID];
					if(widget.width == 512 && widget.height == 334 && widget.type == 0) {
						widget.width = 765;
						widget.height = 503;
					}
					client.drawWidget(widget, 0, 8, 0, UIComponent.FULL);
				}
				final Interface widget = Interface.cache[client.fullscreenWidgetId];
				if(widget.width == 512 && widget.height == 334 && widget.type == 0) {
					widget.width = 765;
					widget.height = 503;
				}
				client.drawWidget(widget, 0, 8, 0, UIComponent.FULL);
				if(!client.menuOpened) {
					processRightClick();
					drawTooltip();
				} else {
					drawer.drawMenu(0, 0, false);
				}
			}
			client.drawCycle++;
			return;
		}
		if(client.forcedChatWidgetId != -1) {
			boolean flag2 = client.method119(client.anInt945, client.forcedChatWidgetId);
			if(flag2)
				client.updateInventory = true;
		}
		if(client.invOverlayInterfaceID != -1) {
			boolean flag1 = client.method119(client.anInt945, client.invOverlayInterfaceID);
			if(flag1)
				client.updateInventory = true;
		}
		if(client.updateAllGraphics) {
			client.updateAllGraphics = false;
			client.updateInventory = true;
		}
		if(client.anInt1054 != -1) {
			client.updateInventory = true;
		}
		if(client.updateInventory) {
			if(client.anInt1054 != -1 && client.anInt1054 == client.invTab) {
				client.anInt1054 = -1;
				client.outBuffer.putOpcode(120);
				client.outBuffer.putByte(client.invTab);
			}
			client.updateInventory = false;
		}
		if(client.loadingStage == 2) {
			client.gameGraphics.setCanvas();
			drawWorld();
			drawWorldOverlay();
			if(client.uiRenderer.isFixed()) {
				client.uiRenderer.displayFrameArea();
				CounterHandler.drawOrbs();
				CounterHandler.drawCounter();
				client.taskHandler.processCompletedTasks();
				client.uiRenderer.fixed.updateMap();
				client.uiRenderer.fixed.updateInventory();
				client.uiRenderer.fixed.updateChat();
			} else {
				CounterHandler.drawOrbs();
				CounterHandler.drawCounter();
				client.taskHandler.processCompletedTasks();
				client.uiRenderer.resizable.updateMap();
				client.uiRenderer.resizable.updateInventory();
				client.uiRenderer.resizable.updateChat();
			}
			if(client.panelHandler.isActive())
				client.panelHandler.update();
			if(client.menuOpened)
				drawer.drawMenu(0, 0, false);
			if(client.onDemandRequester.requestedCount > 5) {
				Rasterizer2D.fillRectangle(0, 0 , 180, 20, 0x000000);
				Rasterizer2D.drawRectangle(0, 0, 180, 20, 0xffffff);
				smallFont.drawLeftAlignedEffectString("Loading " + client.onDemandRequester.requestedCount + " files - please wait.", 6, 16, 0xffffff, true);
			}
			client.gameGraphics.drawGraphics(0, 0, client.graphics);
		} else {
			if(client.onDemandRequester.requestedCount > 5) {
				client.updateGraphics.setCanvas();
				Rasterizer2D.fillRectangle(0, 0 , 180, 20, 0x000000);
				Rasterizer2D.drawRectangle(0, 0, 180, 20, 0xffffff);
				smallFont.drawLeftAlignedEffectString("Loading " + client.onDemandRequester.requestedCount + " files - please wait.", 6, 16, 0xffffff, true);
				client.updateGraphics.drawGraphics(1, 1, client.graphics);
			}
		}
		client.anInt945 = 0;
	}

	private void processRightClick() {
		if(client.activeInterfaceType != 0) {
			return;
		}
		client.menuItemName[0] = "Cancel";
		client.menuItemCode[0] = 1107;
		client.menuPos = 1;
		if(client.fullscreenWidgetId != -1) {
			client.anInt886 = 0;
			client.anInt1315 = 0;
			client.buildWidgetMenu(8, Interface.cache[client.fullscreenWidgetId], client.mouseX, 8, client.mouseY, 0);
			if(client.anInt886 != client.anInt1026) {
				client.anInt1026 = client.anInt886;
			}
			if(client.anInt1315 != client.anInt1129) {
				client.anInt1129 = client.anInt1315;
			}
			return;
		}
		buildSplitPrivateChatMenu();
		client.anInt886 = 0;
		client.anInt1315 = 0;
		boolean allow;
		if(client.uiRenderer.isResizableOrFull()) {
			allow = client.uiRenderer.resizable.allowScene();
		} else {
			allow = client.uiRenderer.fixed.allowScene();
		}
		if(allow) {
			if(client.openInterfaceID != -1) {
				if(client.uiRenderer.isResizableOrFull()) {
					client.buildWidgetMenu(client.uiRenderer.resizable.getOnScreenWidgetOffsets().x, Interface.cache[client.openInterfaceID], client.mouseX, client.uiRenderer.resizable.getOnScreenWidgetOffsets().y, client.mouseY, 0);
				} else {
					client.buildWidgetMenu(4, Interface.cache[client.openInterfaceID], client.mouseX, 4, client.mouseY, 0);
				}
			} else {
				if(client.panelHandler.action()) {
					if(client.mouseWheelAmt != 0 ) {
						client.cameraZoom += client.mouseWheelAmt * 20;
					}
					if(client.cameraZoom < -150) {
						client.cameraZoom = -150;
					}
					if(client.cameraZoom > 150) {
						client.cameraZoom = 150;
					}
				}
				build3dScreenMenu();
			}
		}
		if(client.anInt886 != client.anInt1026) {
			client.anInt1026 = client.anInt886;
		}
		if(client.anInt1315 != client.anInt1129) {
			client.anInt1129 = client.anInt1315;
		}
		client.anInt886 = 0;
		client.anInt1315 = 0;
		if(!client.uiRenderer.isResizableOrFull()) {
			if(client.mouseX > 548 && client.mouseY > 207 && client.mouseX < 740 && client.mouseY < 468) {
				if(client.invOverlayInterfaceID != -1) {
					client.buildWidgetMenu(548, Interface.cache[client.invOverlayInterfaceID], client.mouseX, 207, client.mouseY, 0);
				} else if((client.uiRenderer.id == 562 ? client.newerTabInterfaces : client.olderTabInterfaces)[client.invTab] != -1) {
					client.buildWidgetMenu(548, Interface.cache[(client.uiRenderer.id == 562 ? client.newerTabInterfaces : client.olderTabInterfaces)[client.invTab]], client.mouseX, 207, client.mouseY, 0);
				}
			}
		} else {
			final int yOffset = client.windowWidth < (client.uiRenderer.id == 562 ? 1000 : 980) ? 36 : 0;
			if(client.mouseX >= client.windowWidth - 225 && client.mouseY >= client.windowHeight - 340 - yOffset && client.mouseY < client.windowHeight - 36 - yOffset && client.showTab) {
				if(client.invOverlayInterfaceID != -1) {
					client.buildWidgetMenu(client.windowWidth - 195, Interface.cache[client.invOverlayInterfaceID], client.mouseX, client.windowHeight - 305 - yOffset, client.mouseY, 0);
				} else if((client.uiRenderer.id == 562 ? client.newerTabInterfaces : client.olderTabInterfaces)[client.invTab] != -1) {
					client.buildWidgetMenu(client.windowWidth - 195, Interface.cache[(client.uiRenderer.id == 562 ? client.newerTabInterfaces : client.olderTabInterfaces)[client.invTab]], client.mouseX, client.windowHeight - 305 - yOffset, client.mouseY, 0);
				}
			}
		}
		if(client.anInt886 != client.anInt1048) {
			client.updateInventory = true;
			client.anInt1048 = client.anInt886;
		}
		if(client.anInt1315 != client.anInt1044) {
			client.updateInventory = true;
			client.anInt1044 = client.anInt1315;
		}
		client.anInt886 = 0;
		client.anInt1315 = 0;
		if(!client.uiRenderer.isResizableOrFull()) {
			if(client.mouseX > 0 && client.mouseY > 338 && client.mouseX < 490 && client.mouseY < 463) {
				if(client.forcedChatWidgetId != -1) {
					client.buildWidgetMenu(20, Interface.cache[client.forcedChatWidgetId], client.mouseX, 358, client.mouseY, 0);
				} else if(client.mouseY < 463 && client.mouseX < 490) {
					buildChatAreaMenu(client.mouseY - 338);
				}
			}
		} else if(client.mouseX > 0 && client.mouseY > client.windowHeight - 165 && client.mouseX < 490 && client.mouseY < client.windowHeight - 22) {
			if(client.forcedChatWidgetId != -1) {
				client.buildWidgetMenu(20, Interface.cache[client.forcedChatWidgetId], client.mouseX, client.windowHeight - 140, client.mouseY, 0);
			} else if(client.mouseY < client.windowHeight - 22 && client.mouseX < 490 && client.showChat) {
				buildChatAreaMenu(client.mouseY - (client.windowHeight - 165));
			}
		}
		if(client.forcedChatWidgetId != -1 && client.anInt886 != client.anInt1039) {
			client.anInt1039 = client.anInt886;
		}
		if(client.forcedChatWidgetId != -1 && client.anInt1315 != client.anInt1500) {
			client.anInt1500 = client.anInt1315;
		}
		if(client.uiRenderer.isResizableOrFull()) {
			client.uiRenderer.resizable.buildChat();
			client.uiRenderer.resizable.buildMap();
		}
		if(client.uiRenderer.isFixed()) {
			client.uiRenderer.fixed.buildChat();
			client.uiRenderer.fixed.buildMap();
		}
		/**/
		boolean flag = false;
		while(!flag) {
			flag = true;
			for(int j = 0; j < client.menuPos - 1; j++) {
				if(client.menuItemCode[j] < 1000 && client.menuItemCode[j + 1] > 1000) {
					final String s = client.menuItemName[j];
					client.menuItemName[j] = client.menuItemName[j + 1];
					client.menuItemName[j + 1] = s;
					int k = client.menuItemCode[j];
					client.menuItemCode[j] = client.menuItemCode[j + 1];
					client.menuItemCode[j + 1] = k;
					long l = client.menuItemArg4[j];
					client.menuItemArg4[j] = client.menuItemArg4[j + 1];
					client.menuItemArg4[j + 1] = l;
					k = client.menuItemArg3[j];
					client.menuItemArg3[j] = client.menuItemArg3[j + 1];
					client.menuItemArg3[j + 1] = k;
					k = client.menuItemArg2[j];
					client.menuItemArg2[j] = client.menuItemArg2[j + 1];
					client.menuItemArg2[j + 1] = k;
					k = client.menuItemArg1[j];
					client.menuItemArg1[j] = client.menuItemArg1[j + 1];
					client.menuItemArg1[j + 1] = k;
					flag = false;
				}
			}
		}
	}

	private void build3dScreenMenu() {
		if(!client.panelHandler.action()) {
			return;
		}
		if(client.itemSelected == 0 && client.spellSelected == 0) {
			client.menuItemName[client.menuPos] = "Walk here";
			client.menuItemCode[client.menuPos] = 516;
			client.menuItemArg2[client.menuPos] = client.mouseX;
			client.menuItemArg3[client.menuPos] = client.mouseY;
			client.menuPos++;
		}
		long j = -1;
		for(int k = 0; k < Model.modelHoverAmt; k++) {
			final long hash = Model.modelHover[k];
			final int x = (int) (hash & 0x7f);
			final int y = (int) (hash >> 7 & 0x7f);
			final int config = (int) (hash >> 38 & 3);
			final int id = (int) (hash >> 14 & 0xFFFFFF);
			if(hash == j) {
				continue;
			}
			j = hash;
			if(config == 2 && client.scene.getWallsForMinimap(client.cameraPlane, x, y, hash) >= 0) {
				LocationType obj = LocationType.getPrecise(id);
				if(obj.childIds != null) {
					obj = obj.getChild();
				}
				if(obj == null || obj.name == null) {
					continue;
				}
				if(client.itemSelected == 1) {
					client.menuItemName[client.menuPos] = "Use " + client.selectedItemName + " with @cya@" + obj.name;
					client.menuItemCode[client.menuPos] = 62;
					client.menuItemArg2[client.menuPos] = x;
					client.menuItemArg3[client.menuPos] = y;
					client.menuItemArg4[client.menuPos] = hash;
					client.menuPos++;
				} else if(client.spellSelected == 1) {
					if((client.spellUsableOn & 4) == 4) {
						client.menuItemName[client.menuPos] = client.spellTooltip + " @cya@" + obj.name;
						client.menuItemCode[client.menuPos] = 956;
						client.menuItemArg2[client.menuPos] = x;
						client.menuItemArg3[client.menuPos] = y;
						client.menuItemArg4[client.menuPos] = hash;
						client.menuPos++;
					}
				} else {
					if(obj.actions != null) {
						for(int i2 = 4; i2 >= 0; i2--) {
							if(obj.actions[i2] != null) {
								client.menuItemName[client.menuPos] = obj.actions[i2] + " @cya@" + obj.name;
								if(i2 == 0) {
									client.menuItemCode[client.menuPos] = 502;
								}
								if(i2 == 1) {
									client.menuItemCode[client.menuPos] = 900;
								}
								if(i2 == 2) {
									client.menuItemCode[client.menuPos] = 113;
								}
								if(i2 == 3) {
									client.menuItemCode[client.menuPos] = 872;
								}
								if(i2 == 4) {
									client.menuItemCode[client.menuPos] = 1062;
								}
								client.menuItemArg2[client.menuPos] = x;
								client.menuItemArg3[client.menuPos] = y;
								client.menuItemArg4[client.menuPos] = hash;
								client.menuPos++;
							}
						}
					}
					client.menuItemName[client.menuPos] = "Examine @cya@" + obj.name;
					if(Config.def.idx()) {
						client.menuItemName[client.menuPos] += " @mag@" + obj.id + " " + Arrays.toString(obj.modelIds);
					}
					client.menuItemCode[client.menuPos] = 1226;
					client.menuItemArg1[client.menuPos] = obj.id;
					client.menuItemArg2[client.menuPos] = x;
					client.menuItemArg3[client.menuPos] = y;
					client.menuPos++;
				}
			}
			if(config == 1) {
				final NPC npc = client.npcList[id];
				if(npc.type.boundaryDimension == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64) {
					for(int j2 = 0; j2 < client.npcListSize; j2++) {
						final NPC npc2 = client.npcList[client.npcEntryList[j2]];
						if(npc2 != null && npc2 != npc && npc2.type.boundaryDimension == 1 && npc2.x == npc.x && npc2.y == npc.y) {
							buildAtNPCMenu(npc2.type, client.npcEntryList[j2], y, x);
						}
					}

					for(int l2 = 0; l2 < client.playerCount; l2++) {
						final Player player = client.playerList[client.playerEntryList[l2]];
						if(player != null && player.x == npc.x && player.y == npc.y) {
							buildAtPlayerMenu(x, client.playerEntryList[l2], player, y);
						}
					}

				}
				buildAtNPCMenu(npc.type, id, y, x);
			}
			if(config == 0) {
				final Player player = client.playerList[id];
				if((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
					for(int k2 = 0; k2 < client.npcListSize; k2++) {
						final NPC npc = client.npcList[client.npcEntryList[k2]];
						if(npc != null && npc.type.boundaryDimension == 1 && npc.x == player.x && npc.y == player.y) {
							buildAtNPCMenu(npc.type, client.npcEntryList[k2], y, x);
						}
					}

					for(int i3 = 0; i3 < client.playerCount; i3++) {
						final Player playerHover = client.playerList[client.playerEntryList[i3]];
						if(playerHover != null && playerHover != player && playerHover.x == player.x && playerHover.y == player.y) {
							buildAtPlayerMenu(x, client.playerEntryList[i3], playerHover, y);
						}
					}

				}
				buildAtPlayerMenu(x, id, player, y);
			}
			if(config == 3) {
				final LinkedDeque class19 = client.sceneItems[client.cameraPlane][x][y];
				if(class19 != null) {
					for(ObjectStack item = (ObjectStack) class19.getLast(); item != null; item = (ObjectStack) class19.getPrevious()) {
						final ObjectType itemDef = ObjectType.get(item.id);
						if(client.itemSelected == 1) {
							client.menuItemName[client.menuPos] = "Use " + client.selectedItemName + " with @lre@" + itemDef.name;
							client.menuItemCode[client.menuPos] = 511;
							client.menuItemArg1[client.menuPos] = item.id;
							client.menuItemArg2[client.menuPos] = x;
							client.menuItemArg3[client.menuPos] = y;
							client.menuPos++;
						} else if(client.spellSelected == 1) {
							if((client.spellUsableOn & 1) == 1) {
								client.menuItemName[client.menuPos] = client.spellTooltip + " @lre@" + itemDef.name;
								client.menuItemCode[client.menuPos] = 94;
								client.menuItemArg1[client.menuPos] = item.id;
								client.menuItemArg2[client.menuPos] = x;
								client.menuItemArg3[client.menuPos] = y;
								client.menuPos++;
							}
						} else {
							for(int j3 = 4; j3 >= 0; j3--) {
								if(itemDef.groundActions != null && itemDef.groundActions[j3] != null) {
									client.menuItemName[client.menuPos] = itemDef.groundActions[j3] + " @lre@" + itemDef.name;
									if(j3 == 0) {
										client.menuItemCode[client.menuPos] = 652;
									}
									if(j3 == 1) {
										client.menuItemCode[client.menuPos] = 567;
									}
									if(j3 == 2) {
										client.menuItemCode[client.menuPos] = 234;
									}
									if(j3 == 3) {
										client.menuItemCode[client.menuPos] = 244;
									}
									if(j3 == 4) {
										client.menuItemCode[client.menuPos] = 213;
									}
									client.menuItemArg1[client.menuPos] = item.id;
									client.menuItemArg2[client.menuPos] = x;
									client.menuItemArg3[client.menuPos] = y;
									client.menuPos++;
								} else if(j3 == 2) {
									client.menuItemName[client.menuPos] = "Take @lre@" + itemDef.name;
									client.menuItemCode[client.menuPos] = 234;
									client.menuItemArg1[client.menuPos] = item.id;
									client.menuItemArg2[client.menuPos] = x;
									client.menuItemArg3[client.menuPos] = y;
									client.menuPos++;
								}
							}
							client.menuItemName[client.menuPos] = "Examine @lre@" + itemDef.name;
							if(Config.def.idx()) {
								client.menuItemName[client.menuPos] += " @mag@" + itemDef.id;
							}
							client.menuItemCode[client.menuPos] = 1448;
							client.menuItemArg1[client.menuPos] = item.id;
							client.menuItemArg2[client.menuPos] = x;
							client.menuItemArg3[client.menuPos] = y;
							client.menuPos++;
						}
					}
				}
			}
		}
	}

	private static String getCombatDifferenceColor(int myCombat, int otherCombat) {
		final int difference = myCombat - otherCombat;
		if(difference < -9) {
			return "@red@";
		}
		if(difference < -6) {
			return "@or3@";
		}
		if(difference < -3) {
			return "@or2@";
		}
		if(difference < 0) {
			return "@or1@";
		}
		if(difference > 9) {
			return "@gre@";
		}
		if(difference > 6) {
			return "@gr3@";
		}
		if(difference > 3) {
			return "@gr2@";
		}
		if(difference > 0) {
			return "@gr1@";
		} else {
			return "@yel@";
		}
	}

	private void buildAtNPCMenu(NPCType entityDef, int i, int j, int k) {
		if(!client.panelHandler.action()) {
			return;
		}
		if(client.menuPos >= 400) {
			return;
		}
		if(entityDef.childrenIDs != null) {
			entityDef = entityDef.getSubNPCType();
		}
		if(entityDef == null) {
			return;
		}
		if(!entityDef.clickable) {
			return;
		}
		String s = entityDef.name;
		if(entityDef.combatLevel != 0) {
			s = s + getCombatDifferenceColor(client.localPlayer.combatLevel, entityDef.combatLevel) + " (level-" + entityDef.combatLevel + ")";
		}
		if(client.itemSelected == 1) {
			client.menuItemName[client.menuPos] = "Use " + client.selectedItemName + " with @yel@" + s;
			client.menuItemCode[client.menuPos] = 582;
			client.menuItemArg1[client.menuPos] = i;
			client.menuItemArg2[client.menuPos] = k;
			client.menuItemArg3[client.menuPos] = j;
			client.menuPos++;
			return;
		}
		if(client.spellSelected == 1) {
			if((client.spellUsableOn & 2) == 2) {
				client.menuItemName[client.menuPos] = client.spellTooltip + " @yel@" + s;
				client.menuItemCode[client.menuPos] = 413;
				client.menuItemArg1[client.menuPos] = i;
				client.menuItemArg2[client.menuPos] = k;
				client.menuItemArg3[client.menuPos] = j;
				client.menuPos++;
			}
		} else {
			if(entityDef.actions != null) {
				for(int l = 4; l >= 0; l--) {
					if(entityDef.actions[l] != null && !entityDef.actions[l].equalsIgnoreCase("attack")) {
						client.menuItemName[client.menuPos] = entityDef.actions[l] + " @yel@" + s;
						if(l == 0) {
							client.menuItemCode[client.menuPos] = 20;
						}
						if(l == 1) {
							client.menuItemCode[client.menuPos] = 412;
						}
						if(l == 2) {
							client.menuItemCode[client.menuPos] = 225;
						}
						if(l == 3) {
							client.menuItemCode[client.menuPos] = 965;
						}
						if(l == 4) {
							client.menuItemCode[client.menuPos] = 478;
						}
						client.menuItemArg1[client.menuPos] = i;
						client.menuItemArg2[client.menuPos] = k;
						client.menuItemArg3[client.menuPos] = j;
						client.menuPos++;
					}
				}
			}
			if(entityDef.actions != null) {
				for(int i1 = 4; i1 >= 0; i1--) {
					if(entityDef.actions[i1] != null && entityDef.actions[i1].equalsIgnoreCase("attack")) {
						char c = '\0';
						if(entityDef.combatLevel > client.localPlayer.combatLevel) {
							c = '\u07D0';
						}
						client.menuItemName[client.menuPos] = entityDef.actions[i1] + " @yel@" + s;
						if(i1 == 0) {
							client.menuItemCode[client.menuPos] = 20 + c;
						}
						if(i1 == 1) {
							client.menuItemCode[client.menuPos] = 412 + c;
						}
						if(i1 == 2) {
							client.menuItemCode[client.menuPos] = 225 + c;
						}
						if(i1 == 3) {
							client.menuItemCode[client.menuPos] = 965 + c;
						}
						if(i1 == 4) {
							client.menuItemCode[client.menuPos] = 478 + c;
						}
						client.menuItemArg1[client.menuPos] = i;
						client.menuItemArg2[client.menuPos] = k;
						client.menuItemArg3[client.menuPos] = j;
						client.menuPos++;
					}
				}
			}
			// menuActionName[menuActionRow] = "Examine @yel@" + s +
			// " @gre@(@whi@" + entityDef.type + "@gre@)";
			if(entityDef.combatLevel > 0) {
				client.menuItemName[client.menuPos] = "More info @yel@" + s;
				client.menuItemCode[client.menuPos] = 1026;
				client.menuItemArg1[client.menuPos] = entityDef.id;
			} else {
				client.menuItemName[client.menuPos] = "Examine @yel@" + s;
				client.menuItemCode[client.menuPos] = 1025;
				client.menuItemArg1[client.menuPos] = i;
			}
			if(Config.def.idx()) {
				client.menuItemName[client.menuPos] += " @mag@" + entityDef.id;
			}
			client.menuItemArg2[client.menuPos] = k;
			client.menuItemArg3[client.menuPos] = j;
			client.menuPos++;
		}
	}

	private void buildAtPlayerMenu(int i, int j, Player player, int k) {
		if(!client.panelHandler.action()) {
			return;
		}
		if(player == client.localPlayer) {
			return;
		}
		if(client.menuPos >= 400) {
			return;
		}
		String s = "";//player.hasTitle() ? "" : "[" + player.getTitle().getName() + "] ";
		if(player.skill == 0) {
			s = s + player.name + getCombatDifferenceColor(client.localPlayer.combatLevel, player.combatLevel) + " (level-" + player.combatLevel + ")";
		} else {
			s = s + player.name + " (skill-" + player.skill + ")";
		}
		if(client.itemSelected == 1) {
			client.menuItemName[client.menuPos] = "Use " + client.selectedItemName + " with @whi@" + s;
			client.menuItemCode[client.menuPos] = 491;
			client.menuItemArg1[client.menuPos] = j;
			client.menuItemArg2[client.menuPos] = i;
			client.menuItemArg3[client.menuPos] = k;
			client.menuPos++;
		} else if(client.spellSelected == 1) {
			if((client.spellUsableOn & 8) == 8) {
				client.menuItemName[client.menuPos] = client.spellTooltip + " @whi@" + s;
				client.menuItemCode[client.menuPos] = 365;
				client.menuItemArg1[client.menuPos] = j;
				client.menuItemArg2[client.menuPos] = i;
				client.menuItemArg3[client.menuPos] = k;
				client.menuPos++;
			}
		} else {
			for(int l = 4; l >= 0; l--) {
				if(client.atPlayerActions[l] != null) {
					client.menuItemName[client.menuPos] = client.atPlayerActions[l] + " @whi@" + s;
					char c = '\0';
					if(client.atPlayerActions[l].equalsIgnoreCase("attack")) {
						//if(player.combatLevel > client.localPlayer.combatLevel) {
						//	c = '\u07D0';
						//}
						if(client.localPlayer.team != 0 && player.team != 0) {
							if(client.localPlayer.team == player.team) {
								c = '\u07D0';
							} else {
								c = '\0';
							}
						}
					} else if(client.atPlayerArray[l]) {
						c = '\u07D0';
					}
					if(l == 0) {
						client.menuItemCode[client.menuPos] = 561 + c;
					}
					if(l == 1) {
						client.menuItemCode[client.menuPos] = 779 + c;
					}
					if(l == 2) {
						client.menuItemCode[client.menuPos] = 27 + c;
					}
					if(l == 3) {
						client.menuItemCode[client.menuPos] = 577 + c;
					}
					if(l == 4) {
						client.menuItemCode[client.menuPos] = 729 + c;
					}
					client.menuItemArg1[client.menuPos] = j;
					client.menuItemArg2[client.menuPos] = i;
					client.menuItemArg3[client.menuPos] = k;
					client.menuPos++;
				}
			}
		}
		for(int i1 = 0; i1 < client.menuPos; i1++) {
			if(client.menuItemCode[i1] == 516) {
				client.menuItemName[i1] = "Walk here @whi@" + s;
				return;
			}
		}

	}

	private void buildChatAreaMenu(int j) {
		int line = 0;
		for(int i = 0; i < 500; i++) {
			if(client.chatMessage[i] == null) {
				continue;
			}
			final int type = client.chatType[i];
			final int ypos = 121 - line * 14 + client.chatScrollPos;
			if(ypos < -23) {
				break;
			}
			String name = client.chatAuthor[i];
			if(client.chatTypeView == 1) {
				buildPublicChat(j);
				break;
			}
			if(client.chatTypeView == 2) {
				buildFriendChat(j);
				break;
			}
			if(client.chatTypeView == Constants.MSG_REQUEST || client.chatTypeView == Constants.MSG_CLAN) {
				buildDuelorTrade(j);
				break;
			}
			if(client.chatTypeView == Constants.MSG_GAME) {
				break;
			}
			if(type == 0) {
				line++;
			}
			if((type == 1 || type == 2 || type == 9) && (type == 1 || client.publicChatMode == 0 || client.publicChatMode == 1 && client.isFriendOrSelf(name))) {
				if(j > ypos - 14 && j <= ypos && !name.equals(client.localPlayer.name)) {
					if(client.localPrivilege >= 1) {
						client.menuItemName[client.menuPos] = "Report abuse @whi@" + name;
						client.menuItemCode[client.menuPos] = 606;
						client.menuPos++;
					}
					client.menuItemName[client.menuPos] = "Add ignore @whi@" + name;
					client.menuItemCode[client.menuPos] = 42;
					client.menuPos++;
					client.menuItemName[client.menuPos] = "Add friend @whi@" + name;
					client.menuItemCode[client.menuPos] = 337;
					client.menuPos++;
				}
				line++;
			}
			if((type == 3 || type == 7) && client.splitPrivateChat == 0 && (type == 7 || client.privateChatMode == 0 || client.privateChatMode == 1 && client.isFriendOrSelf(name))) {
				if(j > ypos - 14 && j <= ypos) {
					if(client.localPrivilege >= 1) {
						client.menuItemName[client.menuPos] = "Report abuse @whi@" + name;
						client.menuItemCode[client.menuPos] = 606;
						client.menuPos++;
					}
					client.menuItemName[client.menuPos] = "Add ignore @whi@" + name;
					client.menuItemCode[client.menuPos] = 42;
					client.menuPos++;
					client.menuItemName[client.menuPos] = "Add friend @whi@" + name;
					client.menuItemCode[client.menuPos] = 337;
					client.menuPos++;
				}
				line++;
			}
			if(type == 4 && (client.tradeMode == 0 || client.tradeMode == 1 && client.isFriendOrSelf(name))) {
				if(j > ypos - 14 && j <= ypos) {
					client.menuItemName[client.menuPos] = "Accept trade @whi@" + name;
					client.menuItemCode[client.menuPos] = 484;
					client.menuPos++;
				}
				line++;
			}
			if((type == 5 || type == 6) && client.splitPrivateChat == 0 && client.privateChatMode < 2) {
				line++;
			}
			if(type == 8 && (client.tradeMode == 0 || client.tradeMode == 1 && client.isFriendOrSelf(name))) {
				if(j > ypos - 14 && j <= ypos) {
					client.menuItemName[client.menuPos] = "Accept challenge @whi@" + name;
					client.menuItemCode[client.menuPos] = 6;
					client.menuPos++;
				}
				line++;
			}
		}
	}

	private void buildPublicChat(int j) {
		int l = 0;
		for(int i1 = 0; i1 < 500; i1++) {
			if(client.chatMessage[i1] == null) {
				continue;
			}
			if(client.chatTypeView != 1) {
				continue;
			}
			final int j1 = client.chatType[i1];
			String s = client.chatAuthor[i1];
			final int k1 = 70 - l * 14 + 42 + client.chatScrollPos + 9;
			if(k1 < -23) {
				break;
			}
			if((j1 == 1 || j1 == 2) && (j1 == 1 || client.publicChatMode == 0 || client.publicChatMode == 1 && client.isFriendOrSelf(s))) {
				if(j > k1 - 14 && j <= k1 && !s.equals(client.localPlayer.name)) {
					if(client.localPrivilege >= 1) {
						client.menuItemName[client.menuPos] = "Report abuse @whi@" + s;
						client.menuItemCode[client.menuPos] = 606;
						client.menuPos++;
					}
					client.menuItemName[client.menuPos] = "Add ignore @whi@" + s;
					client.menuItemCode[client.menuPos] = 42;
					client.menuPos++;
					client.menuItemName[client.menuPos] = "Add friend @whi@" + s;
					client.menuItemCode[client.menuPos] = 337;
					client.menuPos++;
				}
				l++;
			}
		}
	}

	private void buildSplitPrivateChatMenu() {
		if(!client.panelHandler.action()) {
			return;
		}
		if(client.uiRenderer.isResizableOrFull() && !client.showChat) {
			return;
		}
		if(client.splitPrivateChat == 0) {
			return;
		}
		int i = 0;
		if(client.systemUpdateTimer != 0) {
			i = 1;
		}
		for(int j = 0; j < 500; j++) {
			if(client.chatMessage[j] != null) {
				int y = 0;
				if(client.uiRenderer.isResizableOrFull()) {
					y = client.windowHeight - 496;
				}
				final int type = client.chatType[j];
				String name = client.chatAuthor[j];
				int privilege = client.chatPriv[j];
				if(type == 2 && (client.privateChatMode == 0 || client.privateChatMode == 1 && (client.isFriendOrSelf(name) || privilege != 0))) {
					final int l = 329 - i * 13;
					if(client.mouseX > 4 && client.mouseY > l - 10 + y && client.mouseY <= l + 3 + y) {
						int i1 = plainFont.getEffectStringWidth("From:  " + name + client.chatMessage[j]) + 25;
						if(i1 > 450) {
							i1 = 450;
						}
						if(client.mouseX < 4 + i1) {
							if(client.localPrivilege >= 1) {
								client.menuItemName[client.menuPos] = "Report abuse @whi@" + name;
								client.menuItemCode[client.menuPos] = 2606;
								client.menuPos++;
							}
							client.menuItemName[client.menuPos] = "Add ignore @whi@" + name;
							client.menuItemCode[client.menuPos] = 2042;
							client.menuPos++;
							client.menuItemName[client.menuPos] = "Add friend @whi@" + name;
							client.menuItemCode[client.menuPos] = 2337;
							client.menuPos++;
						}
					}
					if(++i >= 5) {
						return;
					}
				}
				if((type == 5 || type == 6) && client.privateChatMode < 2 && ++i >= 5) {
					return;
				}
			}
		}

	}

	private void buildDuelorTrade(int j) {
		int l = 0;
		for(int i1 = 0; i1 < 500; i1++) {
			if(client.chatMessage[i1] == null) {
				continue;
			}
			if(client.chatTypeView != 3 && client.chatTypeView != 4) {
				continue;
			}
			final int j1 = client.chatType[i1];
			String s = client.chatAuthor[i1];
			final int k1 = 70 - l * 14 + 42 + client.chatScrollPos + 4 + 5;
			if(k1 < -23) {
				break;
			}
			if(client.chatTypeView == 3 && j1 == 4 && (client.tradeMode == 0 || client.tradeMode == 1 && client.isFriendOrSelf(s))) {
				if(j > k1 - 14 && j <= k1) {
					client.menuItemName[client.menuPos] = "Accept trade @whi@" + s;
					client.menuItemCode[client.menuPos] = 484;
					client.menuPos++;
				}
				l++;
			}
			if(client.chatTypeView == 4 && j1 == 8 && (client.tradeMode == 0 || client.tradeMode == 1 && client.isFriendOrSelf(s))) {
				if(j > k1 - 14 && j <= k1) {
					client.menuItemName[client.menuPos] = "Accept challenge @whi@" + s;
					client.menuItemCode[client.menuPos] = 6;
					client.menuPos++;
				}
				l++;
			}
			if(j1 == 12) {
				if(j > k1 - 14 && j <= k1) {
					client.menuItemName[client.menuPos] = "Go-to @blu@" + s;
					client.menuItemCode[client.menuPos] = 915;
					client.menuPos++;
				}
				l++;
			}
		}
	}

	private void buildFriendChat(int j) {
		int l = 0;
		for(int i1 = 0; i1 < 500; i1++) {
			if(client.chatMessage[i1] == null) {
				continue;
			}
			if(client.chatTypeView != 2) {
				continue;
			}
			final int j1 = client.chatType[i1];
			String s = client.chatAuthor[i1];
			final int k1 = 70 - l * 14 + 42 + client.chatScrollPos + 4 + 5;
			if(k1 < -23) {
				break;
			}
			if((j1 == 5 || j1 == 6) && (client.splitPrivateChat == 0 || client.chatTypeView == 2) && (j1 == 6 || client.privateChatMode == 0 || client.privateChatMode == 1 && client.isFriendOrSelf(s))) {
				l++;
			}
			if((j1 == 3 || j1 == 7) && (client.splitPrivateChat == 0 || client.chatTypeView == 2) && (j1 == 7 || client.privateChatMode == 0 || client.privateChatMode == 1 && client.isFriendOrSelf(s))) {
				if(j > k1 - 14 && j <= k1) {
					if(client.localPrivilege >= 1) {
						client.menuItemName[client.menuPos] = "Report abuse @whi@" + s;
						client.menuItemCode[client.menuPos] = 606;
						client.menuPos++;
					}
					client.menuItemName[client.menuPos] = "Add ignore @whi@" + s;
					client.menuItemCode[client.menuPos] = 42;
					client.menuPos++;
					client.menuItemName[client.menuPos] = "Add friend @whi@" + s;
					client.menuItemCode[client.menuPos] = 337;
					client.menuPos++;
				}
				l++;
			}
		}
	}

	private void drawWorld() {
		client.anInt1265++;
		client.method47(true);
		client.method26(true);
		client.method47(false);
		client.method26(false);
		client.method55();
		client.method104();
		if(!client.forcedCameraLocation) {
			int i = client.mapVerticalRotation;
			if(client.anInt984 / 256 > i) {
				i = client.anInt984 / 256;
			}
			if(client.aBooleanArray876[4] && client.anIntArray1203[4] + 128 > i) {
				i = client.anIntArray1203[4] + 128;
			}
			final int k = client.cameraAngleX + client.cameraAngleAdjustX & 0x7ff;
			int camzoom = Rasterizer3D.viewport.width * 592 >> 9;
			if(camzoom > 900) {
				camzoom = 900;
			}
			camzoom += client.cameraZoom;
			client.setCameraPos(camzoom + (int) (i * (camzoom / 200f)), i, client.anInt1014, client.method42(client.cameraPlane, client.localPlayer.x, client.localPlayer.y) - 50, k, client.anInt1015);
		}
		int camPlane = 0;
		if(!client.forcedCameraLocation) {
			camPlane = client.method120();
		} else {
			camPlane = client.method121();
		}
		for(int i2 = 0; i2 < 5; i2++) {
			if(client.aBooleanArray876[i2]) {
				final int cameraMovement = (int) (Math.random() * (client.anIntArray873[i2] * 2 + 1) - client.anIntArray873[i2] + Math.sin(client.anIntArray1030[i2] * (client.anIntArray928[i2] / 100D)) * client.anIntArray1203[i2]);
				if(i2 == 0) {
					client.cameraLocationX += cameraMovement;
				}
				if(i2 == 1) {
					client.cameraLocationZ += cameraMovement;
				}
				if(i2 == 2) {
					client.cameraLocationY += cameraMovement;
				}
				if(i2 == 3) {
					client.cameraYaw = client.cameraYaw + cameraMovement & 0x7ff;
				}
				if(i2 == 4) {
					client.cameraRoll += cameraMovement;
					if(client.cameraRoll < 128) {
						client.cameraRoll = 128;
					}
					if(client.cameraRoll > 383) {
						client.cameraRoll = 383;
					}
				}
			}
		}
		final int camx = client.cameraLocationX;
		final int camz = client.cameraLocationZ;
		final int camy = client.cameraLocationY;
		final int camroll = client.cameraRoll;
		final int camyaw = client.cameraYaw;
		Model.aBoolean1684 = true;
		Model.modelHoverAmt = 0;
		if(client.uiRenderer.isFixed()) {
			Rasterizer3D.viewport = client.gameAreaViewport;
		}
		Model.hoverX = client.mouseX - Rasterizer3D.viewport.getX();
		Model.hoverY = client.mouseY - Rasterizer3D.viewport.getY();
		if(client.loggedIn) {
			int x = (client.baseX + (client.localPlayer.x - 6 >> 7)) >> 3;
			int y = (client.baseY + (client.localPlayer.y - 6 >> 7)) >> 3;
			if(!Config.def.fog()) {
				Rasterizer2D.fillRectangle(Rasterizer3D.viewport.getX(), Rasterizer3D.viewport.getY(), Rasterizer3D.viewport.width, Rasterizer3D.viewport.height, 0);
			}
			int fogRgb = (x >= 270 && x <= 465 && y >= 335 && y <= 495) ? 0xc8c0a8 : 0;
			client.scene.drawScene(client.cameraLocationX, client.cameraLocationY, client.cameraLocationZ, client.cameraYaw, client.cameraRoll, camPlane, fogRgb);
			client.scene.removeEntityUnit1s();
			drawParticles();
		}
		updateEntities();
		drawHeadIcon();
		client.combatOverlayHandler.displayEntityFeed();
		client.messageFeedHandler.displayKillFeed();
		client.method37();
		client.drawFadeTransition();
		client.cameraLocationX = camx;
		client.cameraLocationZ = camz;
		client.cameraLocationY = camy;
		client.cameraRoll = camroll;
		client.cameraYaw = camyaw;
	}

	private boolean processMenuClick() {
		if(client.activeInterfaceType != 0) {
			return false;
		}
		int click = client.clickButton;
		if(client.spellSelected == 1 && client.clickX >= 516 && client.clickY >= 160 && client.clickX <= 765 && client.clickY <= 205) {
			click = 0;
		}
		if(client.menuOpened) {
			if(click != 1) {
				int k = client.mouseX;
				int j1 = client.mouseY;
				if(k < client.menuX - 10 || k > client.menuX + client.menuWidth + 10 || j1 < client.menuY - 10 || j1 > client.menuY + client.menuHeight + 10) {
					client.menuOpened = false;
				}
			}
			if(click == 1) {
				final int x = client.menuX;
				final int y = client.menuY;
				final int i2 = client.menuWidth;
				int cx = client.clickX;
				int cY = client.clickY;
				int menu = -1;
				for(int hov = 0; hov < client.menuPos; hov++) {
					final int k3 = y + 31 + (client.menuPos - 1 - hov) * 15;
					if(cx > x && cx < x + i2 && cY > k3 - 13 && cY < k3 + 3) {
						menu = hov;
					}
				}
				
				if(menu != -1) {
					client.executeMenuAction(menu);
				}
				client.menuOpened = false;
			}
			return true;
		} else {
			if(click == 1 && client.menuPos > 0) {
				final int i1 = client.menuItemCode[client.menuPos - 1];
				if(i1 == 632 || i1 == 78 || i1 == 867 || i1 == 431 || i1 == 53 || i1 == 74 || i1 == 454 || i1 == 539 || i1 == 493 || i1 == 847 || i1 == 447 || i1 == 1125) {
					final int l1 = client.menuItemArg2[client.menuPos - 1];
					final int j2 = client.menuItemArg3[client.menuPos - 1];
					final Interface class9 = Interface.cache[j2];
					if(class9.invDrag || class9.invMove) {
						client.draggingItem = false;
						client.itemPressTimer = 0;
						client.invWidgetId = j2;
						client.invSrcSlot = l1;
						client.activeInterfaceType = 2;
						client.itemPressX = client.clickX;
						client.itemPressY = client.clickY;
						if(Interface.cache[j2].parent == client.openInterfaceID) {
							client.activeInterfaceType = 1;
						}
						if(Interface.cache[j2].parent == client.forcedChatWidgetId) {
							client.activeInterfaceType = 3;
						}
						return true;
					}
				}
			}
			if(click == 1 && (client.mouseButtonsToggle == 1 || client.menuHasAddFriend(client.menuPos - 1)) && client.menuPos > 2) {
				click = 2;
			}
			if(click == 1 && client.menuPos > 0 && !client.panelHandler.isSettingOpen()) {
				client.executeMenuAction(client.menuPos - 1);
			}
			if(click == 2 && client.menuPos > 0) {
				client.determineMenuSize();
			}
			return false;
		}
	}

	private void processMainScreenClick() {
		if(client.minimapOverlay != 0) {
			return;
		}
		if(client.clickButton == 1) {
			if(client.uiRenderer.id == 1 && client.mouseInRegion(client.windowWidth - 170, 120, client.windowWidth - 139, 151)) {
				return;
			}
			if(client.clickX > client.windowWidth - 25 && client.clickX < client.windowWidth && client.clickY < 26 && client.clickY > 0) {
				return;
			}
			if(client.uiRenderer.getId() == 1 && client.clickX > client.windowWidth - 168 && client.clickX < client.windowWidth - 140 && client.clickY < 120 && client.clickY > 147) {
				return;
			}
			int xClick = client.clickX - 25 - 545;
			int yClick = client.clickY - 5 - 4;
			if((client.uiRenderer.id == 508 || client.uiRenderer.id == 525 || client.uiRenderer.id == 562) && client.uiRenderer.isFixed()) {
				xClick += 16;
			}
			if(client.uiRenderer.isResizableOrFull()) {
				xClick = client.clickX - (client.windowWidth - 182 + 25);
				yClick = client.clickY - 4;
			}
			if(!client.panelHandler.action())
				return;
			if(xClick >= 0 && yClick >= 0 && xClick < 146 && yClick < 151) {
				xClick -= 73;
				yClick -= 75;
				final int k = client.cameraAngleX + client.minimapAngle & 0x7ff;
				int i1 = Rasterizer3D.angleSine[k];
				int j1 = Rasterizer3D.angleCosine[k];
				i1 = i1 * (client.minimapZoom + 256) >> 8;
				j1 = j1 * (client.minimapZoom + 256) >> 8;
				final int k1 = yClick * i1 + xClick * j1 >> 11;
				final int l1 = yClick * j1 - xClick * i1 >> 11;
				final int i2 = client.localPlayer.x + k1 >> 7;
				final int j2 = client.localPlayer.y - l1 >> 7;

				if(client.shiftDown) {
					String teleport = "::tele "+(client.baseX + i2)+" "+(client.baseY + j2);
					client.outBuffer.putOpcode(103);
					client.outBuffer.putByte(teleport.length() - 1);
					client.outBuffer.putLine(teleport.substring(2));
					} else {
				final boolean flag1 = client.findWalkingPath(1, 0, 0, 0, client.localPlayer.smallY[0], 0, 0, j2, client.localPlayer.smallX[0], true, i2);
				if(flag1) {
					if(client.panelHandler.action()) {
						client.panelHandler.close();
					}
					client.outBuffer.putByte(xClick);
					client.outBuffer.putByte(yClick);
					client.outBuffer.putShort(client.cameraAngleX);
					client.outBuffer.putByte(57);
					client.outBuffer.putByte(client.minimapAngle);
					client.outBuffer.putByte(client.minimapZoom);
					client.outBuffer.putByte(89);
					client.outBuffer.putShort(client.localPlayer.x);
					client.outBuffer.putShort(client.localPlayer.y);
					client.outBuffer.putByte(client.anInt1264);
					client.outBuffer.putByte(63);
				}
			}
			Client.pkt246Count++;
				if(Client.pkt246Count > 1151) {
					Client.pkt246Count = 0;
				}
			}
		}

	}

	private void updateEntities() {
		try {
			int chatsDisplayed = 0;
			for(int j = -1; j < client.playerCount + client.npcListSize; j++) {
				Object obj;
				if(j == -1) {
					obj = client.localPlayer;
				} else if(j < client.playerCount) {
					obj = client.playerList[client.playerEntryList[j]];
				} else {
					obj = client.npcList[client.npcEntryList[j - client.playerCount]];
				}
				if(obj == null || !((Mobile) obj).isVisible()) {
					continue;
				}
				Mobile mobile = (Mobile) obj;
				if(obj instanceof NPC) {
					NPCType entityDef = ((NPC) obj).type;
					if(entityDef.childrenIDs != null) {
						entityDef = entityDef.getSubNPCType();
					}
					if(entityDef == null) {
						continue;
					}
					if(Config.def.names()) {
						calcMobileRenderLoc(mobile, mobile.height + 15);
						int offset = ((NPC) obj).special == 101 ? 10 : 20;
						smallFont.drawCenteredEffectString(entityDef.name, client.spriteDrawX, client.spriteDrawY - offset, 0xffbf00, true);
					}
				}
				if(j < client.playerCount) {
					int l = 30;
					final Player player = (Player) obj;
					player.skullIcon = 0;
					if(player.headIcon >= 0) {
						calcMobileRenderLoc(mobile, mobile.height + 15);
						if(client.spriteDrawX > -1) {
							if(player.skullIcon > 0 && player.skullIcon < 6) {
								Client.spriteCache.get(1692 + player.skullIcon).drawImage(client.spriteDrawX - 12, client.spriteDrawY - l);
								l += 25;
							}
							if(player.headIcon != 255) {
								Client.spriteCache.get(1592 + player.headIcon).drawImage(client.spriteDrawX - 12, client.spriteDrawY - l);
								l += 18;
							}
						}
					}
					if(j >= 0 && client.hintType == 10 && client.anInt933 == client.playerEntryList[j]) {
						calcMobileRenderLoc(mobile, mobile.height + 15);
						if(client.spriteDrawX > -1) {
							Client.spriteCache.get(1701 + player.hintIcon).drawImage(client.spriteDrawX - 12, client.spriteDrawY - l);
						}
					}
					if(Config.def.names()) {
						calcMobileRenderLoc(mobile, mobile.height + 15);
						int col = 0x00ff00;
						//if (player.clanName == localPlayer.clanName)
						//	col = 0x00ff00;
						smallFont.drawCenteredEffectString(player.name, client.spriteDrawX, client.spriteDrawY - 10, col, true);
						//if (player.clanName != "")
						//	smallText.drawText(col, "<" + player.clanName + ">",
						//			spriteDrawY - 5, spriteDrawX);
					}
				} else {
					final NPCType entityDef_1 = ((NPC) obj).type;
					if(entityDef_1.headIcon >= 0) {
						calcMobileRenderLoc(mobile, mobile.height + 15);
						if(client.spriteDrawX > -1) {
							Client.spriteCache.get(1592 + entityDef_1.headIcon).drawImage(client.spriteDrawX - 12, client.spriteDrawY - 30);
						}
					}
					if(client.hintType == 1 && client.NPCHintID == client.npcEntryList[j - client.playerCount] && client.loopCycle % 20 < 10) {
						calcMobileRenderLoc(mobile, mobile.height + 15);
						if(client.spriteDrawX > -1) {
							Client.spriteCache.get(1701).drawImage(client.spriteDrawX - 12, client.spriteDrawY - 28);
						}
					}
				}
				if(mobile.chatSpoken != null && (j >= client.playerCount || client.publicChatMode == 0 || client.publicChatMode == 3 || client.publicChatMode == 1 && client.isFriendOrSelf(((Player) obj).name))) {
					calcMobileRenderLoc(mobile, mobile.height);
					if(client.spriteDrawX > -1 && chatsDisplayed < client.anInt975) {
						client.chatOffsetsX[chatsDisplayed] = boldFont.getStringWidth(mobile.chatSpoken) / 2;
						client.chatOffsetsY[chatsDisplayed] = boldFont.lineHeight;
						client.chatPositionsX[chatsDisplayed] = client.spriteDrawX;
						client.chatPositionsY[chatsDisplayed] = client.spriteDrawY;
						client.chatColorEffects[chatsDisplayed] = mobile.chatColorEffect;
						client.chatAnimationEffects[chatsDisplayed] = mobile.chatAnimationEffect;
						client.chatLoopCycles[chatsDisplayed] = mobile.chatLoopCycle;
						client.chatMessagesSpoken[chatsDisplayed++] = mobile.chatSpoken;
						if(client.chatEffectsToggle == 0 && mobile.chatAnimationEffect >= 1 && mobile.chatAnimationEffect <= 3) {
							client.chatOffsetsY[chatsDisplayed] += 10;
							client.chatPositionsY[chatsDisplayed] += 5;
						}
						if(client.chatEffectsToggle == 0 && mobile.chatAnimationEffect == 4) {
							client.chatOffsetsX[chatsDisplayed] = 60;
						}
						if(client.chatEffectsToggle == 0 && mobile.chatAnimationEffect == 5) {
							client.chatOffsetsY[chatsDisplayed] += 5;
						}
					}
				}
				if(mobile.loopCycleStatus > client.loopCycle) {
					try {
						calcMobileRenderLoc(mobile, mobile.height + 15);
						if(client.spriteDrawX > -1) {
							drawer.drawBar(mobile);
							if(Config.def.hitbar() == 3) {
								calcMobileRenderLoc(mobile, mobile.height + 15);
								int level = obj instanceof NPC ? ((NPC) obj).type.combatLevel : ((Player) obj).combatLevel;
								if(obj instanceof NPC && ((NPC) obj).special != 101) {
									smallFont.drawCenteredEffectString("@gre@" + ((NPC) obj).special + "/100", client.spriteDrawX, client.spriteDrawY + -8, 0xff0000, true);
								}
								smallFont.drawCenteredEffectString(getCombatDifferenceColor(client.localPlayer.combatLevel, level) + mobile.currentHealth + "/" + mobile.maxHealth, client.spriteDrawX, client.spriteDrawY + 3, 0xff0000, true);
							}
						}
					} catch(final Exception e) {
					}
				}
				for(int j1 = 0; j1 < 4; j1++) {
					if(mobile.hitsLoopCycle[j1] > client.loopCycle) {
						calcMobileRenderLoc(mobile, mobile.height / 2);
						drawer.drawHit(mobile, j1);
					}
				}
			}
			for(int i = 0; i < chatsDisplayed; i++) {
				final int xPos = client.chatPositionsX[i];
				int yPos = client.chatPositionsY[i];
				final int xOffset = client.chatOffsetsX[i];
				final int yOffset = client.chatOffsetsY[i];
				boolean flag = true;
				while(flag) {
					flag = false;
					for(int l2 = 0; l2 < i; l2++) {
						if(yPos + 2 > client.chatPositionsY[l2] - client.chatOffsetsY[l2] && yPos - yOffset < client.chatPositionsY[l2] + 2 && xPos - xOffset < client.chatPositionsX[l2] + client.chatOffsetsX[l2] && xPos + xOffset > client.chatPositionsX[l2] - client.chatOffsetsX[l2] && client.chatPositionsY[l2] - client.chatOffsetsY[l2] < yPos) {
							yPos = client.chatPositionsY[l2] - client.chatOffsetsY[l2];
							flag = true;
						}
					}

				}
				client.spriteDrawX = client.chatPositionsX[i];
				client.spriteDrawY = client.chatPositionsY[i] = yPos;
				final String message = client.chatMessagesSpoken[i];
				if(client.chatEffectsToggle == 0) {
					int color = 0xffff00;
					if(client.chatColorEffects[i] < 8) {
						color = client.chatColors[client.chatColorEffects[i]];
					}
					if(client.chatColorEffects[i] == 8) {
						color = client.anInt1265 % 20 >= 10 ? 0xffff00 : 0xff0000;
					}
					if(client.chatColorEffects[i] == 9) {
						color = client.anInt1265 % 20 >= 10 ? 65535 : 255;
					}
					if(client.chatColorEffects[i] == 10) {
						color = client.anInt1265 % 20 >= 10 ? 0x80ff80 : 45056;
					}
					if(client.chatColorEffects[i] == 11) {
						final int j3 = 150 - client.chatLoopCycles[i];
						if(j3 < 50) {
							color = 0xff0000 + 1280 * j3;
						} else if(j3 < 100) {
							color = 0xffff00 - 0x50000 * (j3 - 50);
						} else if(j3 < 150) {
							color = 65280 + 5 * (j3 - 100);
						}
					}
					if(client.chatColorEffects[i] == 12) {
						final int k3 = 150 - client.chatLoopCycles[i];
						if(k3 < 50) {
							color = 0xff0000 + 5 * k3;
						} else if(k3 < 100) {
							color = 0xff00ff - 0x50000 * (k3 - 50);
						} else if(k3 < 150) {
							color = 255 + 0x50000 * (k3 - 100) - 5 * (k3 - 100);
						}
					}
					if(client.chatColorEffects[i] == 13) {
						final int l3 = 150 - client.chatLoopCycles[i];
						if(l3 < 50) {
							color = 0xffffff - 0x50005 * l3;
						} else if(l3 < 100) {
							color = 65280 + 0x50005 * (l3 - 50);
						} else if(l3 < 150) {
							color = 0xffffff - 0x50000 * (l3 - 100);
						}
					}
					if(client.chatAnimationEffects[i] == 0) {
						boldFont.drawCenteredString(message, client.spriteDrawX + 1, client.spriteDrawY + 1, 0);
						boldFont.drawCenteredString(message, client.spriteDrawX, client.spriteDrawY, color);
					}
					if(client.chatAnimationEffects[i] == 1) {
						boldFont.drawWaveString(message, client.spriteDrawX + 1, client.spriteDrawY + 1, client.anInt1265, 0);
						boldFont.drawWaveString(message, client.spriteDrawX, client.spriteDrawY, client.anInt1265, color);
					}
					if(client.chatAnimationEffects[i] == 2) {
						boldFont.drawWave2String(message, client.spriteDrawX + 1, client.spriteDrawY + 1, client.anInt1265, 0);
						boldFont.drawWave2String(message, client.spriteDrawX, client.spriteDrawY, client.anInt1265, color);
					}
					if(client.chatAnimationEffects[i] == 3) {
						boldFont.drawShakeString(message, client.spriteDrawX + 1, client.spriteDrawY + 1, client.anInt1265, 150 - client.chatLoopCycles[i], 0);
						boldFont.drawShakeString(message, client.spriteDrawX, client.spriteDrawY, client.anInt1265, 150 - client.chatLoopCycles[i], color);
					}
					if(client.chatAnimationEffects[i] == 4) {
						final int i4 = boldFont.getStringWidth(message);
						final int k4 = (150 - client.chatLoopCycles[i]) * (i4 + 100) / 150;
						Rasterizer2D.setClip(client.spriteDrawX - 50, 0, client.spriteDrawX + 50, 334);
						boldFont.drawLeftAlignedString(message, client.spriteDrawX + 1 + 50 - k4, client.spriteDrawY + 1, 0);
						boldFont.drawLeftAlignedString(message, client.spriteDrawX + 50 - k4, client.spriteDrawY, color);
						Rasterizer2D.removeClip();
					}
					if(client.chatAnimationEffects[i] == 5) {
						final int j4 = 150 - client.chatLoopCycles[i];
						int l4 = 0;
						if(j4 < 25) {
							l4 = j4 - 25;
						} else if(j4 > 125) {
							l4 = j4 - 125;
						}
						Rasterizer2D.setClip(0, client.spriteDrawY - boldFont.lineHeight - 1, 512, client.spriteDrawY + 5);
						boldFont.drawCenteredString(message, client.spriteDrawX + 1, client.spriteDrawY + 1 + l4, 0);
						boldFont.drawCenteredString(message, client.spriteDrawX, client.spriteDrawY + l4, color);
						Rasterizer2D.removeClip();
					}
				} else {
					boldFont.drawCenteredString(message, client.spriteDrawX + 1, client.spriteDrawY + 1, 0);
					boldFont.drawCenteredString(message, client.spriteDrawX, client.spriteDrawY, 0xffff00);
				}
			}
		} catch(final Exception e) {
		}
	}

	private void drawHeadIcon() {
		if(client.hintType != 2) {
			return;
		}
		calcRenderLoc((client.anInt934 - client.baseX << 7) + client.hintRegionX, (client.anInt935 - client.baseY << 7) + client.hintRegionY, client.anInt936 * 2);
		if(client.spriteDrawX > -1 && client.loopCycle % 20 < 10) {
			Client.spriteCache.get(1701).drawImage(client.spriteDrawX - 12, client.spriteDrawY - 28);
		}
	}

	private void drawWorldOverlay() {
		int offset = 4;
		if(client.uiRenderer.isResizableOrFull()) {
			offset = 0;
		}
		drawSplitPrivateChat();
		if(client.crossType == 1) {
			int off = client.uiRenderer.isFixed() ? 4 : 8;
			client.clickCross[client.crossIndex / 100].drawImage(client.crossX - off - offset, client.crossY - off - offset);
			client.anInt1142++;
			if(client.anInt1142 > 67) {
				client.anInt1142 = 0;
			}
		}
		if(client.crossType == 2) {
			int off = client.uiRenderer.isFixed() ? 4 : 8;
			client.clickCross[4 + client.crossIndex / 100].drawImage(client.crossX - off - offset, client.crossY - off - offset);
		}
		if(client.anInt1018 != -1) {
			client.method119(client.anInt945, client.anInt1018);
			if(client.uiRenderer.isResizableOrFull()) {
				if(client.anInt1018 == 197)
					client.drawWidget(Interface.cache[client.anInt1018], client.windowWidth - 550, -90, 0, UIComponent.SCENE);
				else
					client.drawWidget(Interface.cache[client.anInt1018], client.windowWidth - 720, 0, 0, UIComponent.SCENE);
			} else {
				client.drawWidget(Interface.cache[client.anInt1018], 4, 4, 0, UIComponent.SCENE);
			}
		}
		if(client.openInterfaceID != -1) {
			client.method119(client.anInt945, client.openInterfaceID);
			if(client.uiRenderer.isResizableOrFull()) {
				client.drawWidget(Interface.cache[client.openInterfaceID], client.uiRenderer.resizable.getOnScreenWidgetOffsets().x, client.uiRenderer.resizable.getOnScreenWidgetOffsets().y, 0, UIComponent.SCENE);
			} else {
				client.drawWidget(Interface.cache[client.openInterfaceID], 4, 4, 0, UIComponent.SCENE);
			}
		}
		client.method70();
		if(client.combatMultiwayMode == 1) {
			if(client.uiRenderer.isResizableOrFull()) {
				if(client.uiRenderer.getId() < 500 && client.uiRenderer.getId() != 1)
					Client.spriteCache.get(1926).drawImage(client.windowWidth - 30, 140);
				else
					Client.spriteCache.get(15).drawImage(client.windowWidth - 35, 170);
			} else {
					Client.spriteCache.get(1926).drawImage(485, 310);
			}
		}
		if(client.systemUpdateTimer != 0) {
			int seconds = client.systemUpdateTimer / 50;
			final int minutes = seconds / 60;
			seconds %= 60;
			plainFont.drawLeftAlignedString("System update in: " + minutes + ":" + (seconds < 10 ? "0" : "") + seconds, 4, 329, 0xffff00);
			client.anInt849++;
			if(client.anInt849 > 75) {
				client.anInt849 = 0;
			}
		}
		if(!client.menuOpened) {
			processRightClick();
			drawTooltip();
		}
		final int x = client.baseX + (client.localPlayer.x - 6 >> 7);
		final int y = client.baseY + (client.localPlayer.y - 6 >> 7);
		final int modeint = client.uiRenderer.getMode();
		int line = modeint == 0 ? 5 : 1;
		int off = modeint == 0 ? 511 : client.windowWidth - 240;


		if(Config.def.fps()) {
			smallFont.drawRightAlignedString("Fps: " + client.fps, off, (line += 15), 0xffff00);
			final Runtime runtime = Runtime.getRuntime();
			final int mem = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
			smallFont.drawRightAlignedString("Mem: " + mem+"k", off, (line += 15), 0xffff00);
		}
		if(Config.def.data()) {
			final Runtime runtime = Runtime.getRuntime();
			final int usedMemory = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
			final int maxMemory = (int) (runtime.totalMemory() / 1024L);
			final int centerX = client.windowWidth / 2;
			final int centerY = client.windowHeight / 2;
			client.socketStream.updateIOPerSec();
			final int in = client.socketStream.inPerSec;
			final int out = client.socketStream.outPerSec;
			smallFont.drawRightAlignedString("IO: " + in + " - " + out, off, (line += 15), 0xffff00);
			smallFont.drawRightAlignedString("Mem: " + (usedMemory / 1024L) + " / " + (maxMemory / 1024L), off, (line += 15), 0xffff00);
			line += 5;
			smallFont.drawRightAlignedString("Cursor: " + client.mouseX + "," + client.mouseY, off, (line += 15), 0xffff00);
			smallFont.drawRightAlignedString("Center: " + (client.mouseX - centerX) + "," + (client.mouseY - centerY), off, (line += 15), 0xffff00);
			smallFont.drawRightAlignedString("Coords: " + x + "," + y, off, (line += 15), 0xffff00);
			smallFont.drawRightAlignedString("Screen: " + client.windowWidth + "x" + client.windowHeight, off, (line += 15), 0xffff00);
			//int[] flo = client.terrainDataIds;
			//smallFont.drawRightAlignedString("Map Data: " + Arrays.toString(flo), off, (line += 15), 0xffff00);
			//int[] obj = client.objectDataIds;
			//smallFont.drawRightAlignedString("Obj Data: " + Arrays.toString(obj), off, (line += 15), 0xffff00);
			}
	}

	private void drawSplitPrivateChat() {
		if(client.uiRenderer.isResizableOrFull() && !client.showChat) {
			return;
		}
		if(client.splitPrivateChat == 0) {
			return;
		}
		final BitmapFont font = plainFont;
		int i = 0;
		if(client.systemUpdateTimer != 0) {
			i = 1;
		}
		for(int j = 0; j < 500; j++) {
			if(client.chatMessage[j] != null) {
				int y = 4;
				int x = 4;
				if(client.uiRenderer.isResizableOrFull()) {
					y = client.windowHeight - 496;
					x = 0;
				}
				final int type = client.chatType[j];
				String name = client.chatAuthor[j];
				int rights = client.chatPriv[j];
				if(type == 2 && (client.privateChatMode == 0 || client.privateChatMode == 1 && (client.isFriendOrSelf(name) || rights != 0))) {
					final int l = 329 - i * 13;
					int k1 = 4 + x;
					font.drawLeftAlignedString("From", k1, y + l, 0);
					font.drawLeftAlignedString("From", k1, y + l - 1, Config.def.privateChat());
					k1 += font.getStringWidth("From ");
					if(rights >= 1) {
						Client.spriteCache.get(1984 + rights - 1).drawImage(k1, y + l - 12);
						k1 += 12;
					}
					font.drawLeftAlignedString(name + ": " + client.chatMessage[j], k1, y + l, 0);
					font.drawLeftAlignedString(name + ": " + client.chatMessage[j], k1, y + l - 1, Config.def.privateChat());
					if(++i >= 5) {
						return;
					}
				}
				if(type == 5 && client.privateChatMode < 2) {
					final int i1 = 329 - i * 13;
					font.drawLeftAlignedString(client.chatMessage[j], x + 4, y + i1, 0);
					font.drawLeftAlignedString(client.chatMessage[j], x + 4, y + i1 - 1, Config.def.privateChat());
					if(++i >= 5) {
						return;
					}
				}
				if(type == 6 && client.privateChatMode < 2) {
					final int j1 = 329 - i * 13;
					font.drawLeftAlignedString("To " + name + ": " + client.chatMessage[j], x + 4, y + j1, 0);
					font.drawLeftAlignedString("To " + name + ": " + client.chatMessage[j], x + 4, y + j1 - 1, Config.def.privateChat());
					if(++i >= 5) {
						return;
					}
				}
			}
		}
	}

	private void drawTooltip() {
		if(client.menuPos < 2 && client.itemSelected == 0 && client.spellSelected == 0) {
			return;
		}
		String s;
		if(client.itemSelected == 1 && client.menuPos < 2) {
			s = "Use " + client.selectedItemName + " with...";
		} else if(client.spellSelected == 1 && client.menuPos < 2) {
			s = client.spellTooltip + "...";
		} else {
			s = client.menuItemName[client.menuPos - 1];
		}
		if(client.menuPos > 2) {
			s = s + "@whi@ / " + (client.menuPos - 2) + " more options";
		}
		int off = client.uiRenderer.isFixed() ? 4 : 0;
		boldFont.drawTooltip(s, 4 + off, 15 + off, 0xffffff, client.loopCycle / 1000);
	}

	public void processScrollbar(int x, int y, int mX, int mY, int visibleHeight, int contentHeight, Interface widget) {
		processScrollbar(x, y, mX, mY, visibleHeight, contentHeight, widget, false);
	}

	public void processScrollbar(int x, int y, int mX, int mY, int visibleHeight, int contentHeight, Interface widget, boolean newPanel) {
		int anInt992;
		int wid = newPanel ? 32 : 16;
		if(client.aBoolean972) {
			anInt992 = 32;
		} else {
			anInt992 = 0;
		}
		client.aBoolean972 = false;
		if(mX >= x && mX < x + wid && mY >= y && mY < y + 16) {
			widget.scrollPos -= client.scrollBarChangeAmount * 4;
		} else if(mX >= x && mX < x + wid && mY >= y + visibleHeight - 16 && mY < y + visibleHeight) {
			widget.scrollPos += client.scrollBarChangeAmount * 4;
		} else if(mX >= x - anInt992 && mX < x + wid + anInt992 && mY >= y + 16 && mY < y + visibleHeight - 16 && client.scrollBarChangeAmount > 0) {
			int l1 = (visibleHeight - 32) * visibleHeight / contentHeight;
			if(l1 < 8) {
				l1 = 8;
			}
			final int i2 = mY - y - 16 - l1 / 2;
			final int j2 = visibleHeight - 32 - l1;
			if(contentHeight != visibleHeight) {
				widget.scrollPos = (contentHeight - visibleHeight) * i2 / j2;
			} else {
				widget.scrollPos = 0;
			}
			client.aBoolean972 = true;
		}
	}

	public void drawOldScrollbar(int x, int y, int visibleHeight, int contentHeight, int scrollPos) {
		//arrow icons
		Client.spriteCache.get(211).drawImage(x + 3, y + 3);
		Client.spriteCache.get(212).drawImage(x + 3, y + visibleHeight - 13);
		//middle of the top button
		Rasterizer2D.drawRectangle(x + 2, y + 2, 12, 12, 0x4d4233);
		//light arrav of the top button
		Rasterizer2D.drawVerticalLine(x, y, 16, 0x766654);
		Rasterizer2D.drawVerticalLine(x + 1, y + 1, 14, 0x766654);
		Rasterizer2D.drawHorizontalLine(x + 1, y, 15, 0x766654);
		Rasterizer2D.drawHorizontalLine(x + 2, y + 1, 13, 0x766654);
		//dark arrav of the top button
		Rasterizer2D.drawVerticalLine(x + 15, y + 1, 14, 0x332d25);
		Rasterizer2D.drawVerticalLine(x + 14, y + 2, 16, 0x332d25);
		Rasterizer2D.drawHorizontalLine(x + 1, y + 14, 13, 0x332d25);
		Rasterizer2D.drawHorizontalLine(x, y + 15, 16, 0x332d25);
		//middle of the down button
		Rasterizer2D.drawRectangle(x + 2, y + visibleHeight - 14, 12, 12, 0x4d4233);
		//light arrav of the down button
		Rasterizer2D.drawVerticalLine(x, y + visibleHeight - 16, 16, 0x766654);
		Rasterizer2D.drawVerticalLine(x + 1, y + visibleHeight - 15, 14, 0x766654);
		Rasterizer2D.drawHorizontalLine(x + 1, y + visibleHeight - 16, 15, 0x766654);
		Rasterizer2D.drawHorizontalLine(x + 2, y + visibleHeight - 15, 13, 0x766654);
		//dark arrav of the down button
		Rasterizer2D.drawVerticalLine(x + 15, y + visibleHeight - 15, 14, 0x332d25);
		Rasterizer2D.drawVerticalLine(x + 14, y + visibleHeight - 14, 16, 0x332d25);
		Rasterizer2D.drawHorizontalLine(x + 1, y + visibleHeight - 2, 13, 0x332d25);
		Rasterizer2D.drawHorizontalLine(x, y + visibleHeight - 1, 16, 0x332d25);
		int barHeight = (visibleHeight - 32) * visibleHeight / contentHeight;
		if(barHeight < 8) {
			barHeight = 8;
		}
		int barPos = 0;
		if(visibleHeight != contentHeight) {
			barPos = (visibleHeight - 32 - barHeight) * scrollPos / (contentHeight - visibleHeight);
		}
		//background
		Rasterizer2D.fillRectangle(x, y + 16, 16, barPos, 0x23201b);
		Rasterizer2D.fillRectangle(x, y + 16 + barPos + barHeight, 16, visibleHeight - barPos - barHeight - 32, 0x23201b);
		//middle of the bar
		Rasterizer2D.fillRectangle(x + 2, y + 18 + barPos, 12, barHeight - 4, 0x4d4233);
		//light arrav of the bar
		Rasterizer2D.drawVerticalLine(x, y + 16 + barPos, barHeight - 1, 0x766654);
		Rasterizer2D.drawVerticalLine(x + 1, y + 17 + barPos, barHeight - 3, 0x766654);
		Rasterizer2D.drawHorizontalLine(x + 1, y + 16 + barPos, 14, 0x766654);
		Rasterizer2D.drawHorizontalLine(x + 2, y + 17 + barPos, 12, 0x766654);
		//dark arrav of the bar
		Rasterizer2D.drawVerticalLine(x + 15, y + 16 + barPos, barHeight - 1, 0x332d25);
		Rasterizer2D.drawVerticalLine(x + 14, y + 17 + barPos, barHeight - 3, 0x332d25);
		Rasterizer2D.drawHorizontalLine(x + 1, y + 14 + barPos + barHeight, 14, 0x332d25);
		Rasterizer2D.drawHorizontalLine(x, y + 15 + barPos + barHeight, 16, 0x332d25);
	}


	public void drawScrollbar(int x, int y, int visibleHeight, int contentHeight, int scrollPos) {
		client.scrollBarTop.drawImage(x, y);
		Rasterizer2D.fillRectangle(x, y + 16, 16, visibleHeight - 32, 0x000001);
		Rasterizer2D.fillRectangle(x, y + 16, 15, visibleHeight - 32, 0x3d3426);
		Rasterizer2D.fillRectangle(x, y + 16, 13, visibleHeight - 32, 0x342d21);
		Rasterizer2D.fillRectangle(x, y + 16, 11, visibleHeight - 32, 0x2e281d);
		Rasterizer2D.fillRectangle(x, y + 16, 10, visibleHeight - 32, 0x29241b);
		Rasterizer2D.fillRectangle(x, y + 16, 9, visibleHeight - 32, 0x252019);
		Rasterizer2D.fillRectangle(x, y + 16, 1, visibleHeight - 32, 0x000001);
		int barHeight = (visibleHeight - 32) * visibleHeight / contentHeight;
		if(barHeight < 8) {
			barHeight = 8;
		}
		int barPos = 0;
		if(visibleHeight != contentHeight) {
			barPos = (visibleHeight - 32 - barHeight) * scrollPos / (contentHeight - visibleHeight);
		}
		Rasterizer2D.fillRectangle(x, y + 16 + barPos, 16, barHeight, 0x4d4233);
		Rasterizer2D.drawVerticalLine(x, y + 16 + barPos, barHeight, 0x000001);
		Rasterizer2D.drawVerticalLine(x + 1, y + 16 + barPos, barHeight, 0x817051);
		Rasterizer2D.drawVerticalLine(x + 2, y + 16 + barPos, barHeight, 0x73654a);
		Rasterizer2D.drawVerticalLine(x + 3, y + 16 + barPos, barHeight, 0x6a5c43);
		Rasterizer2D.drawVerticalLine(x + 4, y + 16 + barPos, barHeight, 0x6a5c43);
		Rasterizer2D.drawVerticalLine(x + 5, y + 16 + barPos, barHeight, 0x655841);
		Rasterizer2D.drawVerticalLine(x + 6, y + 16 + barPos, barHeight, 0x655841);
		Rasterizer2D.drawVerticalLine(x + 7, y + 16 + barPos, barHeight, 0x61553e);
		Rasterizer2D.drawVerticalLine(x + 8, y + 16 + barPos, barHeight, 0x61553e);
		Rasterizer2D.drawVerticalLine(x + 9, y + 16 + barPos, barHeight, 0x5d513c);
		Rasterizer2D.drawVerticalLine(x + 10, y + 16 + barPos, barHeight, 0x5d513c);
		Rasterizer2D.drawVerticalLine(x + 11, y + 16 + barPos, barHeight, 0x594e3a);
		Rasterizer2D.drawVerticalLine(x + 12, y + 16 + barPos, barHeight, 0x594e3a);
		Rasterizer2D.drawVerticalLine(x + 13, y + 16 + barPos, barHeight, 0x514635);
		Rasterizer2D.drawVerticalLine(x + 14, y + 16 + barPos, barHeight, 0x4b4131);
		Rasterizer2D.drawHorizontalLine(x, y + 16 + barPos, 15, 0x000001);
		Rasterizer2D.drawHorizontalLine(x, y + 17 + barPos, 15, 0x000001);
		Rasterizer2D.drawHorizontalLine(x, y + 17 + barPos, 14, 0x655841);
		Rasterizer2D.drawHorizontalLine(x, y + 17 + barPos, 13, 0x6a5c43);
		Rasterizer2D.drawHorizontalLine(x, y + 17 + barPos, 11, 0x6d5f48);
		Rasterizer2D.drawHorizontalLine(x, y + 17 + barPos, 10, 0x73654a);
		Rasterizer2D.drawHorizontalLine(x, y + 17 + barPos, 7, 0x76684b);
		Rasterizer2D.drawHorizontalLine(x, y + 17 + barPos, 5, 0x7b6a4d);
		Rasterizer2D.drawHorizontalLine(x, y + 17 + barPos, 4, 0x7e6e50);
		Rasterizer2D.drawHorizontalLine(x, y + 17 + barPos, 3, 0x817051);
		Rasterizer2D.drawHorizontalLine(x, y + 17 + barPos, 2, 0x000001);
		Rasterizer2D.drawHorizontalLine(x, y + 18 + barPos, 16, 0x000001);
		Rasterizer2D.drawHorizontalLine(x, y + 18 + barPos, 15, 0x564b38);
		Rasterizer2D.drawHorizontalLine(x, y + 18 + barPos, 14, 0x5d513c);
		Rasterizer2D.drawHorizontalLine(x, y + 18 + barPos, 11, 0x625640);
		Rasterizer2D.drawHorizontalLine(x, y + 18 + barPos, 10, 0x655841);
		Rasterizer2D.drawHorizontalLine(x, y + 18 + barPos, 7, 0x6a5c43);
		Rasterizer2D.drawHorizontalLine(x, y + 18 + barPos, 5, 0x6e6046);
		Rasterizer2D.drawHorizontalLine(x, y + 18 + barPos, 4, 0x716247);
		Rasterizer2D.drawHorizontalLine(x, y + 18 + barPos, 3, 0x7b6a4d);
		Rasterizer2D.drawHorizontalLine(x, y + 18 + barPos, 2, 0x817051);
		Rasterizer2D.drawHorizontalLine(x, y + 18 + barPos, 1, 0x000001);
		Rasterizer2D.drawHorizontalLine(x, y + 19 + barPos, 16, 0x000001);
		Rasterizer2D.drawHorizontalLine(x, y + 19 + barPos, 15, 0x514635);
		Rasterizer2D.drawHorizontalLine(x, y + 19 + barPos, 14, 0x564b38);
		Rasterizer2D.drawHorizontalLine(x, y + 19 + barPos, 11, 0x5d513c);
		Rasterizer2D.drawHorizontalLine(x, y + 19 + barPos, 9, 0x61553e);
		Rasterizer2D.drawHorizontalLine(x, y + 19 + barPos, 7, 0x655841);
		Rasterizer2D.drawHorizontalLine(x, y + 19 + barPos, 5, 0x6a5c43);
		Rasterizer2D.drawHorizontalLine(x, y + 19 + barPos, 4, 0x6e6046);
		Rasterizer2D.drawHorizontalLine(x, y + 19 + barPos, 3, 0x73654a);
		Rasterizer2D.drawHorizontalLine(x, y + 19 + barPos, 2, 0x817051);
		Rasterizer2D.drawHorizontalLine(x, y + 19 + barPos, 1, 0x000001);
		Rasterizer2D.drawHorizontalLine(x, y + 20 + barPos, 16, 0x000001);
		Rasterizer2D.drawHorizontalLine(x, y + 20 + barPos, 15, 0x4b4131);
		Rasterizer2D.drawHorizontalLine(x, y + 20 + barPos, 14, 0x544936);
		Rasterizer2D.drawHorizontalLine(x, y + 20 + barPos, 13, 0x594e3a);
		Rasterizer2D.drawHorizontalLine(x, y + 20 + barPos, 10, 0x5d513c);
		Rasterizer2D.drawHorizontalLine(x, y + 20 + barPos, 8, 0x61553e);
		Rasterizer2D.drawHorizontalLine(x, y + 20 + barPos, 6, 0x655841);
		Rasterizer2D.drawHorizontalLine(x, y + 20 + barPos, 4, 0x6a5c43);
		Rasterizer2D.drawHorizontalLine(x, y + 20 + barPos, 3, 0x73654a);
		Rasterizer2D.drawHorizontalLine(x, y + 20 + barPos, 2, 0x817051);
		Rasterizer2D.drawHorizontalLine(x, y + 20 + barPos, 1, 0x000001);
		Rasterizer2D.drawVerticalLine(x + 15, y + 16 + barPos, barHeight, 0x000001);
		Rasterizer2D.drawHorizontalLine(x, y + 15 + barPos + barHeight, 16, 0x000001);
		Rasterizer2D.drawHorizontalLine(x, y + 14 + barPos + barHeight, 15, 0x000001);
		Rasterizer2D.drawHorizontalLine(x, y + 14 + barPos + barHeight, 14, 0x3f372a);
		Rasterizer2D.drawHorizontalLine(x, y + 14 + barPos + barHeight, 10, 0x443c2d);
		Rasterizer2D.drawHorizontalLine(x, y + 14 + barPos + barHeight, 9, 0x483e2f);
		Rasterizer2D.drawHorizontalLine(x, y + 14 + barPos + barHeight, 7, 0x4a402f);
		Rasterizer2D.drawHorizontalLine(x, y + 14 + barPos + barHeight, 4, 0x4b4131);
		Rasterizer2D.drawHorizontalLine(x, y + 14 + barPos + barHeight, 3, 0x564b38);
		Rasterizer2D.drawHorizontalLine(x, y + 14 + barPos + barHeight, 2, 0x000001);
		Rasterizer2D.drawHorizontalLine(x, y + 13 + barPos + barHeight, 16, 0x000001);
		Rasterizer2D.drawHorizontalLine(x, y + 13 + barPos + barHeight, 15, 0x443c2d);
		Rasterizer2D.drawHorizontalLine(x, y + 13 + barPos + barHeight, 11, 0x4b4131);
		Rasterizer2D.drawHorizontalLine(x, y + 13 + barPos + barHeight, 9, 0x514635);
		Rasterizer2D.drawHorizontalLine(x, y + 13 + barPos + barHeight, 7, 0x544936);
		Rasterizer2D.drawHorizontalLine(x, y + 13 + barPos + barHeight, 6, 0x564b38);
		Rasterizer2D.drawHorizontalLine(x, y + 13 + barPos + barHeight, 4, 0x594e3a);
		Rasterizer2D.drawHorizontalLine(x, y + 13 + barPos + barHeight, 3, 0x625640);
		Rasterizer2D.drawHorizontalLine(x, y + 13 + barPos + barHeight, 2, 0x6a5c43);
		Rasterizer2D.drawHorizontalLine(x, y + 13 + barPos + barHeight, 1, 0x000001);
		Rasterizer2D.drawHorizontalLine(x, y + 12 + barPos + barHeight, 16, 0x000001);
		Rasterizer2D.drawHorizontalLine(x, y + 12 + barPos + barHeight, 15, 0x443c2d);
		Rasterizer2D.drawHorizontalLine(x, y + 12 + barPos + barHeight, 14, 0x4b4131);
		Rasterizer2D.drawHorizontalLine(x, y + 12 + barPos + barHeight, 12, 0x544936);
		Rasterizer2D.drawHorizontalLine(x, y + 12 + barPos + barHeight, 11, 0x564b38);
		Rasterizer2D.drawHorizontalLine(x, y + 12 + barPos + barHeight, 10, 0x594e3a);
		Rasterizer2D.drawHorizontalLine(x, y + 12 + barPos + barHeight, 7, 0x5d513c);
		Rasterizer2D.drawHorizontalLine(x, y + 12 + barPos + barHeight, 4, 0x61553e);
		Rasterizer2D.drawHorizontalLine(x, y + 12 + barPos + barHeight, 3, 0x6e6046);
		Rasterizer2D.drawHorizontalLine(x, y + 12 + barPos + barHeight, 2, 0x7b6a4d);
		Rasterizer2D.drawHorizontalLine(x, y + 12 + barPos + barHeight, 1, 0x000001);
		Rasterizer2D.drawHorizontalLine(x, y + 11 + barPos + barHeight, 16, 0x000001);
		Rasterizer2D.drawHorizontalLine(x, y + 11 + barPos + barHeight, 15, 0x4b4131);
		Rasterizer2D.drawHorizontalLine(x, y + 11 + barPos + barHeight, 14, 0x514635);
		Rasterizer2D.drawHorizontalLine(x, y + 11 + barPos + barHeight, 13, 0x564b38);
		Rasterizer2D.drawHorizontalLine(x, y + 11 + barPos + barHeight, 11, 0x594e3a);
		Rasterizer2D.drawHorizontalLine(x, y + 11 + barPos + barHeight, 9, 0x5d513c);
		Rasterizer2D.drawHorizontalLine(x, y + 11 + barPos + barHeight, 7, 0x61553e);
		Rasterizer2D.drawHorizontalLine(x, y + 11 + barPos + barHeight, 5, 0x655841);
		Rasterizer2D.drawHorizontalLine(x, y + 11 + barPos + barHeight, 4, 0x6a5c43);
		Rasterizer2D.drawHorizontalLine(x, y + 11 + barPos + barHeight, 3, 0x73654a);
		Rasterizer2D.drawHorizontalLine(x, y + 11 + barPos + barHeight, 2, 0x7b6a4d);
		Rasterizer2D.drawHorizontalLine(x, y + 11 + barPos + barHeight, 1, 0x000001);
		client.scrollBarDown.drawImage(x, y + visibleHeight - 16);
	}

	public void drawDarkScrollbar(int x, int y, int visibleHeight, int contentHeight, int viewOffY) {
		int barHeight = (visibleHeight - 32) * visibleHeight / contentHeight;
		if(barHeight < 10) {
			barHeight = 10;
		}
		int barPos = 0;
		if(contentHeight != visibleHeight) {
			barPos = (visibleHeight - 32 - barHeight) * viewOffY / (contentHeight - visibleHeight);
		}
		Rasterizer2D.fillRectangle(x + 1, y + 16 + barPos, 19, y + 21 + barPos + barHeight - 5 - (y + 16 + barPos), 0xffffff, 16);
	}
	
	public void drawPanelScroll(int x, int y, int visibleHeight, int contentHeight, int scrollPos) {
		if(Config.def.panelStyle == 2) {
			drawWhiteScrollbar(x, y, visibleHeight, contentHeight, scrollPos);
		} else if(client.uiRenderer.getId() == 317) {
			drawOldScrollbar(x, y, visibleHeight, contentHeight, scrollPos);
		} else {
			drawScrollbar(x, y, visibleHeight, contentHeight, scrollPos);
		}
	}

	public void drawWhiteScrollbar(int x, int y, int visibleHeight, int contentHeight, int viewOffY) {
		Client.spriteCache.get(86).drawImage(x, y, 64);
		Client.spriteCache.get(87).drawImage(x, y + visibleHeight - 16, 64);
		Rasterizer2D.drawVerticalLine(x, y + 16, visibleHeight - 32, 0xffffff, 64);
		Rasterizer2D.drawVerticalLine(x + 15, y + 16, visibleHeight - 32, 0xffffff, 64);
		int barHeight = (visibleHeight - 32) * visibleHeight / contentHeight;
		if(barHeight < 10) {
			barHeight = 10;
		}
		int barPos = 0;
		if(contentHeight != visibleHeight) {
			barPos = (visibleHeight - 32 - barHeight) * viewOffY / (contentHeight - visibleHeight);
		}
		Rasterizer2D.drawPoint(x + 1, y + 17 + barPos, 0xffffff, 32);
		Rasterizer2D.drawPoint(x + 14, y + 17 + barPos, 0xffffff, 32);
		Rasterizer2D.drawPoint(x + 1, y + 17 + barPos + barHeight - 3, 0xffffff, 32);
		Rasterizer2D.drawPoint(x + 14, y + 17 + barPos + barHeight - 3, 0xffffff, 32);
		Rasterizer2D.drawRectangle(x, y + 16 + barPos, 16, 5 + y + 16 + barPos + barHeight - 5 - (y + 16 + barPos), 0xffffff, 32);
		Rasterizer2D.fillRectangle(x + 1, y + 16 + barPos, 14, 5 + y + 16 + barPos + barHeight - 5 - (y + 16 + barPos), 0xffffff, 16);
	}

	private void updateCamera() {
		try {
			final int x = client.localPlayer.x + client.cameraAdjustX;
			final int y = client.localPlayer.y + client.cameraAdjustY;
			if(client.anInt1014 - x < -500 || client.anInt1014 - x > 500 || client.anInt1015 - y < -500 || client.anInt1015 - y > 500) {
				client.anInt1014 = x;
				client.anInt1015 = y;
			}
			if(client.anInt1014 != x) {
				client.anInt1014 += (x - client.anInt1014) / 16;
			}
			if(client.anInt1015 != y) {
				client.anInt1015 += (y - client.anInt1015) / 16;
			}
			if(client.keyPressed[1] == 1) {
				client.anInt1186 += (-24 - client.anInt1186) / 2;
			} else if(client.keyPressed[2] == 1) {
				client.anInt1186 += (24 - client.anInt1186) / 2;
			} else {
				client.anInt1186 /= 2;
			}
			if(client.keyPressed[3] == 1) {
				client.anInt1187 += (12 - client.anInt1187) / 2;
			} else if(client.keyPressed[4] == 1) {
				client.anInt1187 += (-12 - client.anInt1187) / 2;
			} else {
				client.anInt1187 /= 2;
			}
			if(client.keyPressed[6] == 1 || client.keyPressed[7] == 1) {
				client.cameraZoom += client.keyPressed[6] == 1 ? -15 : 15;
				if(client.cameraZoom < -120) {
					client.cameraZoom = -120;
				}
				if(client.cameraZoom > 120) {
					client.cameraZoom = 120;
				}
			}
			client.cameraAngleX = client.cameraAngleX + client.anInt1186 / 2 & 0x7ff;
			client.mapVerticalRotation += client.anInt1187 / 2;
			if(client.mapVerticalRotation < 128) {
				client.mapVerticalRotation = 128;
			}
			if(client.mapVerticalRotation > 383) {
				client.mapVerticalRotation = 383;
			}
			final int tilex = client.anInt1014 >> 7;
			final int tiley = client.anInt1015 >> 7;
			final int j1 = client.method42(client.cameraPlane, client.anInt1014, client.anInt1015);
			int k1 = 0;
			if(tilex > 3 && tiley > 3 && tilex < 100 && tiley < 100) {
				for(int px = tilex - 4; px <= tilex + 4; px++) {
					for(int py = tiley - 4; py <= tiley + 4; py++) {
						int plane = client.cameraPlane;
						if(plane < 3 && (client.tiles[1][px][py] & 2) == 2) {
							plane++;
						}
						final int i3 = j1 - client.sceneGroundZ[plane][px][py];
						if(i3 > k1) {
							k1 = i3;
						}
					}
				}
			}
			Client.pkt77Count++;
			int j2 = k1 * 192;
			if(j2 > 0x17f00) {
				j2 = 0x17f00;
			}
			if(j2 < 32768) {
				j2 = 32768;
			}
			if(j2 > client.anInt984) {
				client.anInt984 += (j2 - client.anInt984) / 24;
				return;
			}
			if(j2 < client.anInt984) {
				client.anInt984 += (j2 - client.anInt984) / 80;
			}
		} catch(final Exception _ex) {
			SignLink.reportError("glfc_ex " + client.localPlayer.x + "," + client.localPlayer.y + "," + client.anInt1014 + "," + client.anInt1015 + "," + client.regionX + "," + client.regionY + "," + client.baseX + "," + client.baseY);
			throw new RuntimeException("eek");
		}
	}
	
	private void calcRenderLoc(int x, int y, int dz) {
		Scene.focalLength = Rasterizer3D.viewport.width;
		if(x < 128 || y < 128 || x > 13056 || y > 13056) {
			client.spriteDrawX = -1;
			client.spriteDrawY = -1;
			return;
		}
		int z = client.method42(client.cameraPlane, x, y) - dz;
		x -= client.cameraLocationX;
		z -= client.cameraLocationZ;
		y -= client.cameraLocationY;
		final int sinroll = Model.angleSine[client.cameraRoll];
		final int cosroll = Model.angleCosine[client.cameraRoll];
		final int sinyaw = Model.angleSine[client.cameraYaw];
		final int cosyaw = Model.angleCosine[client.cameraYaw];
		int z1 = y * sinyaw + x * cosyaw >> 16;
		y = y * cosyaw - x * sinyaw >> 16;
		x = z1;
		z1 = z * cosroll - y * sinroll >> 16;
		y = z * sinroll + y * cosroll >> 16;
		z = z1;
		if(y >= 50) {
			client.spriteDrawX = Rasterizer3D.viewport.centerX + (x * Scene.focalLength) / y + Rasterizer3D.viewport.getX();
			client.spriteDrawY = Rasterizer3D.viewport.centerY + (z * Scene.focalLength) / y + Rasterizer3D.viewport.getY();
		} else {
			client.spriteDrawX = -1;
			client.spriteDrawY = -1;
		}
		Scene.focalLength = 512;
	}

	private void calcMobileRenderLoc(Mobile entity, int dz) {
		calcRenderLoc(entity.x, entity.y, dz);
	}

	@Override
	public void initialize() {
		client.chatGraphics = new GraphicalComponent(519, 165);
		client.mapGraphics = new GraphicalComponent(246, 168);
		client.inventoryGraphics = new GraphicalComponent(246, 335);
		client.gameGraphics = new GraphicalComponent(client.uiRenderer.isResizableOrFull() ? client.windowWidth : 519, client.uiRenderer.isResizableOrFull() ? client.windowHeight : 338);
		client.updateGraphics = new GraphicalComponent(180, 20);
		client.updateAllGraphics = true;
	}

	@Override
	public void reset() {
		client.chatGraphics = null;
		client.mapGraphics = null;
		client.inventoryGraphics = null;
		client.gameGraphics = null;
		client.updateGraphics = null;
	}
	
	public void drawParticles() {
		Iterator<Particle> iterator;
		Particle particle;
		if (true) {
			iterator = client.displayedParticles.iterator();
			while (iterator.hasNext()) {
				particle = iterator.next();
				if (particle != null) {
					particle.tick();
					if (particle.isDead()) {
						client.removeDeadParticles.add(particle);
					} else {
						int displayX = particle.getPosition().getX();
						int displayY = particle.getPosition().getY();
						int displayZ = particle.getPosition().getZ();
						int[] projection = calcParticlePos(displayX, displayY, displayZ, (int) particle.getSize());
						float size = particle.getSize();
						int alpha = (int) (particle.getAlpha() * 255.0F);
						int radius = (int) (((client.uiRenderer.isFixed() ? 4.0F : 4.5F)) * particle.getSize());
						int srcAlpha = 256 - alpha;
						int srcR = (particle.getColor() >> 16 & 255) * alpha;
						int srcG = (particle.getColor() >> 8 & 255) * alpha;
						int srcB = (particle.getColor() & 255) * alpha;
						int y1 = projection[1] - radius;
						if (y1 < 0) {
							y1 = 0;
						}
						int y2 = projection[1] + radius;
						if (y2 >= Rasterizer2D.canvasHeight) {
							y2 = Rasterizer2D.canvasHeight - 1;
						}
						for (int iy = y1; iy <= y2; ++iy) {
							int dy = iy - projection[1];
							int dist = (int) Math.sqrt(radius * radius - dy * dy);
							int x1 = projection[0] - dist;
							if (x1 < 0) {
								x1 = 0;
							}
							int x2 = projection[0] + dist;
							if (x2 >= Rasterizer2D.canvasWidth) {
								x2 = Rasterizer2D.canvasWidth - 1;
							}
							int pixel = x1 + iy * Rasterizer2D.canvasWidth;
							try {
								if (Rasterizer3D.depthBuffer != null) {
									if (Rasterizer3D.depthBuffer[pixel] >= projection[2] - size - 15 || Rasterizer3D.depthBuffer[pixel++] >= projection[2] + size + 15) {
										for (int ix = x1; ix <= x2; ++ix) {
											int dstR = (client.gameGraphics.getRaster()[pixel] >> 16 & 255) * srcAlpha;
											int dstG = (client.gameGraphics.getRaster()[pixel] >> 8 & 255) * srcAlpha;
											int dstB = (client.gameGraphics.getRaster()[pixel] & 255) * srcAlpha;
											int rgb = (srcR + dstR >> 8 << 16) + (srcG + dstG >> 8 << 8) + (srcB + dstB >> 8);
											client.gameGraphics.set(pixel++, rgb);
										}
									} else {
										particle.setAlpha(0f);
									}
								}
							} catch (Exception exception) {
								exception.printStackTrace();
							}
						}
					}
				}
			}
			if(!client.removeDeadParticles.isEmpty()) {
				client.displayedParticles.removeAll(client.removeDeadParticles);
				client.removeDeadParticles.clear();
			}
		} else {
			if(!client.displayedParticles.isEmpty()) {
				iterator = client.displayedParticles.iterator();
				while(iterator.hasNext()) {
					particle = iterator.next();
					if(particle != null) {
						particle.tick();
						if(particle.isDead()) {
							client.removeDeadParticles.add(particle);
						}
					}
				}
			}
			if(!client.removeDeadParticles.isEmpty()) {
				client.displayedParticles.removeAll(client.removeDeadParticles);
				client.removeDeadParticles.clear();
			}
		}
	}
	
	public final int[] calcParticlePos(int x, int y, int z, int size) {
		if (x < 128 || z < 128 || x > 13056 || z > 13056) {
			return new int[] { 0, 0, 0, 0, 0, 0, 0 };
		}
		int i1 = client.method42(client.cameraPlane, z, x) - y;
		x -= client.cameraLocationX;
		i1 -= client.cameraLocationZ;
		z -= client.cameraLocationY;
		int j1 = Model.angleSine[client.cameraRoll];
		int k1 = Model.angleCosine[client.cameraRoll];
		int l1 = Model.angleSine[client.cameraYaw];
		int i2 = Model.angleCosine[client.cameraYaw];
		int j2 = z * l1 + x * i2 >> 16;
		z = z * i2 - x * l1 >> 16;
		x = j2;
		j2 = i1 * k1 - z * j1 >> 16;
		z = i1 * j1 + z * k1 >> 16;
		if(z >= 50) {//can you replace those? textureInt2 = centerY yee mmk, try that out.
			return new int[] { Rasterizer3D.viewport.centerX + (x * Scene.focalLength) / z,
					Rasterizer3D.viewport.centerY + (j2 * Scene.focalLength) / z, z,
					Rasterizer3D.viewport.centerX + (x - size / 2 * Scene.focalLength) / z,
					Rasterizer3D.viewport.centerY + (j2 - size / 2 * Scene.focalLength) / z,
					Rasterizer3D.viewport.centerX + (x + size / 2 * Scene.focalLength) / z,
					Rasterizer3D.viewport.centerY + (j2 + size / 2 * Scene.focalLength) / z };
		} else {
			return new int[] { 0, 0, 0, 0, 0, 0, 0 };
		}
	}
}
