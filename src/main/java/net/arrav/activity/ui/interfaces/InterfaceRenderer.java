package net.arrav.activity.ui.interfaces;

import net.arrav.Client;
import net.arrav.Config;
import net.arrav.Constants;
import net.arrav.activity.ui.UIComponent;
import net.arrav.cache.unit.DeformSequence;
import net.arrav.cache.unit.interfaces.Interface;
import net.arrav.cache.unit.ObjectType;
import net.arrav.graphic.Rasterizer2D;
import net.arrav.graphic.Rasterizer3D;
import net.arrav.graphic.font.BitmapFont;
import net.arrav.graphic.img.BitmapImage;
import net.arrav.world.model.Model;

public class InterfaceRenderer {

    private static final Client client = Client.instance;


    public static void drawWidget(Interface widget, int xPosition, int yPosition, int activeScroll, UIComponent component) {
        if(widget.type != 0 || widget.children == null) {
            return;
        }
        if(widget.hoverTriggered && client.anInt1026 != widget.id && client.anInt1048 != widget.id && client.anInt1039 != widget.id) {
            return;
        }
        final int[] saveClip = Rasterizer2D.getClip();
        Rasterizer2D.setClip(xPosition, yPosition, xPosition + widget.width, yPosition + widget.height);
        for(int child = 0; child < widget.children.length; child++) {
            int xPos = widget.childX[child] + xPosition;
            int yPos = widget.childY[child] + yPosition - activeScroll;
            final Interface childWidget = Interface.cache[widget.children[child]];
            xPos += childWidget.offsetX;
            yPos += childWidget.offsetY;
            if(childWidget.contentType > 0) {
                client.drawFriendsListOrWelcomeScreen(childWidget);
            }

            if(childWidget.type == Constants.TYPE_CONTAINER) {
                if(childWidget.scrollPos > childWidget.scrollMax - childWidget.height) {
                    childWidget.scrollPos = childWidget.scrollMax - childWidget.height;
                }
                if(childWidget.scrollPos < 0) {
                    childWidget.scrollPos = 0;
                }
                drawWidget(childWidget, xPos, yPos, childWidget.scrollPos, component);
                if(childWidget.scrollMax > childWidget.height) {
                    if(client.uiRenderer.id == 459) {
                        client.gameActivity.drawOldScrollbar(xPos + childWidget.width, yPos, childWidget.height, childWidget.scrollMax, childWidget.scrollPos);
                    } else {
                        client.gameActivity.drawScrollbar(xPos + childWidget.width, yPos, childWidget.height, childWidget.scrollMax, childWidget.scrollPos);
                    }
                }
            }
            else if(childWidget.type != Constants.WIDGET_MODEL_LIST) {
                if(childWidget.type == Constants.WIDGET_INVENTORY) {
                    int item = 0;
                    for(int i = 0; i < childWidget.height; i++) {
                        for(int i2 = 0; i2 < childWidget.width; i2++) {
                            int x = xPos + i2 * (32 + childWidget.invPadX);
                            int y = yPos + i * (32 + childWidget.invPadY);
                            if(item < 20) {
                                x += childWidget.invIconX[item];
                                y += childWidget.invIconY[item];
                            }
                            if(item < childWidget.invId.length && childWidget.invId[item] > 0) {
                                int mouseDragOffsetX = 0;
                                int mouseDragOffsetY = 0;
                                final int itemID = childWidget.invId[item] - 1;
                                if(x > Rasterizer2D.clipStartX - 32 && x < Rasterizer2D.clipEndX && y > Rasterizer2D.clipStartY - 32 && y < Rasterizer2D.clipEndY || client.activeInterfaceType != 0 && client.invSrcSlot == item) {
                                    int borderColor = 0;
                                    if(client.itemSelected == 1 && client.anInt1283 == item && client.anInt1284 == childWidget.id) {
                                        borderColor = 0xffffff;
                                    }
                                    final BitmapImage itemImage = ObjectType.getIcon(itemID, childWidget.invAmt[item], borderColor);
                                    if(itemImage != null) {
                                        if(client.activeInterfaceType != 0 && client.invSrcSlot == item && client.invWidgetId == childWidget.id) {
                                            mouseDragOffsetX = client.mouseX - client.itemPressX;
                                            mouseDragOffsetY = client.mouseY - client.itemPressY;
                                            if(mouseDragOffsetX < 5 && mouseDragOffsetX > -5) {
                                                mouseDragOffsetX = 0;
                                            }
                                            if(mouseDragOffsetY < 5 && mouseDragOffsetY > -5) {
                                                mouseDragOffsetY = 0;
                                            }
                                            if(client.itemPressTimer < 10) {
                                                mouseDragOffsetX = 0;
                                                mouseDragOffsetY = 0;
                                            }
                                            itemImage.drawImage(x + mouseDragOffsetX, y + mouseDragOffsetY, 128);
                                            if(y + mouseDragOffsetY < Rasterizer2D.clipStartY && widget.scrollPos > 0) {
                                                int i10 = client.anInt945 * (Rasterizer2D.clipStartY - y - mouseDragOffsetY) / 3;
                                                if(i10 > client.anInt945 * 10) {
                                                    i10 = client.anInt945 * 10;
                                                }
                                                if(i10 > widget.scrollPos) {
                                                    i10 = widget.scrollPos;
                                                }
                                                widget.scrollPos -= i10;
                                                client.itemPressY += i10;
                                            }
                                            if(y + mouseDragOffsetY + 32 > Rasterizer2D.clipEndY && widget.scrollPos < widget.scrollMax - widget.height) {
                                                int j10 = client.anInt945 * (y + mouseDragOffsetY + 32 - Rasterizer2D.clipEndY) / 3;
                                                if(j10 > client.anInt945 * 10) {
                                                    j10 = client.anInt945 * 10;
                                                }
                                                if(j10 > widget.scrollMax - widget.height - widget.scrollPos) {
                                                    j10 = widget.scrollMax - widget.height - widget.scrollPos;
                                                }
                                                widget.scrollPos += j10;
                                                client.itemPressY -= j10;
                                            }
                                        } else if(client.atInventoryInterfaceType != 0 && client.atInventoryIndex == item && client.atInventoryInterface == childWidget.id) {
                                            itemImage.drawImage(x, y);
                                        } else {
                                            itemImage.drawImage(x, y);
                                        }
                                        if(itemImage.imageOriginalWidth == 33 || childWidget.invAmt[item] != 1) {
                                            final int amount = childWidget.invAmt[item];
                                            String amt = client.valueToKOrM(amount);
                                            int color = 0xffff00;
                                            if(amt.endsWith("M")) {
                                                color = 0x00ff80;
                                            } else if(amt.endsWith("K")) {
                                                color = 0xffffff;
                                            }
                                            client.smallFont.drawLeftAlignedString(amt, x + 1 + mouseDragOffsetX, y + 10 + mouseDragOffsetY, 0);
                                            client.smallFont.drawLeftAlignedString(amt, x + mouseDragOffsetX, y + 9 + mouseDragOffsetY, color);
                                        }
                                    }
                                }
                            } else if(childWidget.invIcon != null && item < 20) {
                                if(childWidget.invIcon[item] != 0 && childWidget.invIcon[item] != 0) {
                                    final BitmapImage itemImage = client.spriteCache.get(childWidget.invIcon[item]);
                                    if(itemImage != null) {
                                        itemImage.drawImage(x, y);
                                    }
                                }
                            }
                            item++;
                        }
                    }
                }
                else if(childWidget.type == Constants.WIDGET_RECTANGLE) {
                    boolean hover = false;
                    if(client.anInt1039 == childWidget.id || client.anInt1048 == childWidget.id || client.anInt1026 == childWidget.id) {
                        hover = true;
                    }
                    int color;
                    if(client.interfaceIsSelected(childWidget)) {
                        color = childWidget.colorAlt;
                        if(hover && childWidget.hoverColorAlt != 0) {
                            color = childWidget.hoverColorAlt;
                        }
                    } else {
                        color = childWidget.color;
                        if(hover && childWidget.hoverColor != 0) {
                            color = childWidget.hoverColor;
                        }
                    }
                    if(childWidget.alpha == 0) {
                        if(childWidget.rectFilled) {
                            Rasterizer2D.fillRectangle(xPos, yPos, childWidget.width, childWidget.height, color);
                        } else {
                            Rasterizer2D.drawRectangle(xPos, yPos, childWidget.width, childWidget.height, color);
                        }
                    } else if(childWidget.rectFilled) {
                        Rasterizer2D.fillRectangle(xPos, yPos, childWidget.width, childWidget.height, color, 256 - (childWidget.alpha & 0xff));
                    } else {
                        Rasterizer2D.drawRectangle(xPos, yPos, childWidget.width, childWidget.height, color, 256 - (childWidget.alpha & 0xff));
                    }
                }
                else if(childWidget.type == Constants.WIDGET_STRING) {
                    final BitmapFont font = Interface.fonts[childWidget.fontId];
                    String message = childWidget.text;
                    boolean hover = false;
                    if(client.anInt1039 == childWidget.id || client.anInt1048 == childWidget.id || client.anInt1026 == childWidget.id) {
                        hover = true;
                    }
                    int color;
                    if(childWidget.id >= 16026 && childWidget.id <= 17026) {
                        Rasterizer2D.fillRectangle(xPos - 20, yPos - 1, 175, 13, 0x000000, 70);
                        if(client.questLine == childWidget.id) {
                            Rasterizer2D.fillRectangle(xPos - 20, yPos - 1, 175, 13, 0x000000, 70);
                            client.questLine = 0;
                        }
                    }
                    if(client.interfaceIsSelected(childWidget)) {
                        color = childWidget.colorAlt;
                        if(hover && childWidget.hoverColorAlt != 0) {
                            color = childWidget.hoverColorAlt;
                        }
                        if(childWidget.textAlt.length() > 0) {
                            message = childWidget.textAlt;
                        }
                    } else {
                        color = childWidget.color;
                        if(hover && childWidget.hoverColor != 0) {
                            color = childWidget.hoverColor;
                        }
                    }
                    if(childWidget.actionType == 6 && client.aBoolean1149) {
                        message = "Please wait...";
                        color = childWidget.color;
                    }
                    if(component == UIComponent.CHAT) {
                        if(color == 0xffff00)
                            color = 255;
                        if(color == 49152)
                            color = 0xffffff;
                        if(client.uiRenderer.getId() == 1) {
                            if(color == 0x000000)
                                color = 0xecdec6;
                            if(color == 0xffff00)
                                color = 0x000000;
                            if(color == 0x800000)
                                color = 0xc5a44c;
                            if(color == 0x0000ff)
                                color = 0xff9100;
                        }
                    }
                    for(int l6 = yPos + font.lineHeight; message.length() > 0; l6 += font.lineHeight) {
                        if(message.contains("%")) {
                            do {
                                final int k7 = message.indexOf("%1");
                                if(k7 == -1) {
                                    break;
                                }
                                message = message.substring(0, k7) + client.interfaceIntToString(client.extractInterfaceValues(childWidget, 0)) + message.substring(k7 + 2);
                            } while(true);
                            do {
                                final int l7 = message.indexOf("%2");
                                if(l7 == -1) {
                                    break;
                                }
                                message = message.substring(0, l7) + client.interfaceIntToString(client.extractInterfaceValues(childWidget, 1)) + message.substring(l7 + 2);
                            } while(true);
                            do {
                                final int i8 = message.indexOf("%3");
                                if(i8 == -1) {
                                    break;
                                }
                                message = message.substring(0, i8) + client.interfaceIntToString(client.extractInterfaceValues(childWidget, 2)) + message.substring(i8 + 2);
                            } while(true);
                            do {
                                final int j8 = message.indexOf("%4");
                                if(j8 == -1) {
                                    break;
                                }
                                message = message.substring(0, j8) + client.interfaceIntToString(client.extractInterfaceValues(childWidget, 3)) + message.substring(j8 + 2);
                            } while(true);
                            do {
                                final int k8 = message.indexOf("%5");
                                if(k8 == -1) {
                                    break;
                                }
                                message = message.substring(0, k8) + client.interfaceIntToString(client.extractInterfaceValues(childWidget, 4)) + message.substring(k8 + 2);
                            } while(true);
                            do {
                                int index = message.indexOf("%6");

                                if(index == -1) {
                                    break;
                                }

                                message = message.substring(0, index) + client.interfaceIntToString(client.extractInterfaceValues(widget, 5)) + message.substring(index + 2);
                            } while(true);
                        }
                        int rank = 0;
                        if(message.contains("@ra")) {
                            int spot = message.contains("@red@") ? 8 : 3;
                            rank = Byte.parseByte(Character.toString(message.charAt(spot)));
                            message = message.replaceAll("@ra" + rank +"@", "");
                        }
                        final int l8 = message.indexOf("\\n");
                        String s1;
                        if(l8 != -1) {
                            s1 = message.substring(0, l8);
                            message = message.substring(l8 + 2);
                        } else {
                            s1 = message;
                            message = "";
                        }
                        if(Config.def.idx()) {
                            s1 = childWidget.id + "";
                        }
                        if(rank >= 1) {
                            Client.spriteCache.get(1626 + rank).drawImage(xPos + childWidget.width / 2, yPos + font.lineHeight / 2 - 3);
                            xPos += 11;
                        }
                        if(childWidget.textCenter) {
                            font.drawCenteredEffectString(s1, xPos + childWidget.width / 2, l6, color, childWidget.textShadow ? 0 : -1);
                        } else {
                            switch (childWidget.textAlign) {
                                case 0:
                                    font.drawLeftAlignedEffectString(s1, xPos, l6, color, childWidget.textShadow ? 0 : -1);
                                    break;
                                case 1:
                                    font.drawCenteredEffectString(s1, xPos + (childWidget.width / 2), l6, color, childWidget.textShadow ? 0 : -1);
                                    break;
                                case 2:
                                    font.drawRightAlignedEffectString(s1, xPos, l6, color, childWidget.textShadow ? 0 : -1);
                                    break;
                                case 3:
                                    font.drawCenteredEffectString(s1, xPos, l6, color, childWidget.textShadow ? 0 : -1);
                                    break;
                                default:
                                    font.drawLeftAlignedEffectString(s1, xPos, l6, color, childWidget.textShadow ? 0 : -1);
                                    break;
                            }
                        }
                    }
                }
                else if(childWidget.type == Constants.WIDGET_SPRITE) {
                    Interface.cache[640].text = "Arrav.net";
                    BitmapImage image = null;
                    if(childWidget.spriteID < -1) {
                        image = ObjectType.getIcon(-childWidget.spriteID, 0, 0);
                    } else {
                        if(client.interfaceIsSelected(childWidget) || (childWidget.secondarySpriteID != childWidget.spriteID && childWidget.hoverTriggered && client.mouseInRegion(xPos, yPos, xPos + childWidget.width, yPos + childWidget.height))) {
                            if(childWidget.secondarySpriteID != 0)
                                image = Client.spriteCache.get(childWidget.secondarySpriteID);
                        } else {
                            if(childWidget.spriteID != -1)
                                image = Client.spriteCache.get(childWidget.spriteID);
                        }
                    }


                    if(image == null) {
                        System.out.println("null image id:"+childWidget.id+" parent:"+childWidget.parent+" sprite:"+childWidget.spriteID+" sprite:"+childWidget.secondarySpriteID);
                        continue;
                    }

                    if(Config.def.sprite()) {
                        Rasterizer2D.drawRectangle(xPos, yPos, image.imageWidth, image.imageHeight, 0x000000);
                        client.smallFont.drawLeftAlignedString(childWidget.id + " "+childWidget.spriteID, xPos, yPos + client.smallFont.lineHeight, 0x000000);
                    } else {
                            if (childWidget.id == client.autocastId && childWidget.id == client.spellId && client.anIntArray1045[108] != 0)
                                Client.spriteCache.get(2029).drawImage(xPos - 3, yPos - 3);
                            if (client.spellSelected == 1 && childWidget.id == client.spellId && client.spellId != 0 && image != null) {
                                image.drawImage(xPos, yPos, 50);
                            } else if (childWidget.drawsAlpha)
                                image.drawImage(xPos, yPos, 100 + childWidget.alpha);
                            else
                                image.drawImage(xPos, yPos);
                    }
                }
                else if(childWidget.type == Constants.WIDGET_MODEL) {
                    final int centerX = Rasterizer3D.viewport.centerX;
                    final int centerY = Rasterizer3D.viewport.centerY;
                    Rasterizer3D.viewport.centerX = xPos + childWidget.width / 2;
                    Rasterizer3D.viewport.centerY = yPos + childWidget.height / 2;
                    final int i5 = Rasterizer3D.angleSine[childWidget.modelYaw] * childWidget.modelZoom >> 16;
                    final int l5 = Rasterizer3D.angleCosine[childWidget.modelYaw] * childWidget.modelZoom >> 16;
                    final boolean isSelected = client.interfaceIsSelected(childWidget);
                    int animationID;
                    Rasterizer3D.textured = false;
                    if(isSelected) {
                        animationID = childWidget.modelAnimAlt;
                    } else {
                        animationID = childWidget.modelAnim;
                    }
                    Model model;
                    if(animationID == -1) {
                        model = childWidget.getModel(-1, -1, isSelected);
                    } else {
                        if(animationID > DeformSequence.cache.length) {
                            Rasterizer3D.textured = true;
                            return;
                        }
                        final DeformSequence animation = DeformSequence.cache[animationID];
                        if(animation == null) {
                            Rasterizer3D.textured = true;
                            return;
                        }
                        model = childWidget.getModel(animation.anIntArray354[childWidget.modelAnimLength], animation.frameList[childWidget.modelAnimLength], isSelected);
                    }
                    if(widget.id == client.forcedChatWidgetId) {
                        Rasterizer2D.setClip(25, 25, 505, 116);
                    }
                    if(childWidget.id == 15125) {
                        if(client.leftClickInRegion(xPos, yPos - 180, xPos + childWidget.width, yPos - 80 + childWidget.height)) {
                            client.rollCharacterInInterface = !client.rollCharacterInInterface;
                        }
                    }
                    if(Config.def.idx()) {
                        client.boldFont.drawCenteredEffectString(childWidget.id + "", xPos + childWidget.width / 2, yPos, 0x000000, childWidget.textShadow ? 0 : -1);
                    }
                    if(model != null) {
                        model.drawModel(childWidget.modelRoll, 0, childWidget.modelYaw, 0, i5, l5);
                    }
                    Rasterizer2D.removeClip();
                    Rasterizer3D.viewport.centerX = centerX;
                    Rasterizer3D.viewport.centerY = centerY;
                    Rasterizer3D.textured = true;
                }
                else if(childWidget.type == Constants.WIDGET_ITEM_LIST) {
                    final BitmapFont font = Interface.fonts[childWidget.fontId];
                    int item = 0;
                    for(int yColumn = 0; yColumn < childWidget.height; yColumn++) {
                        for(int xColumn = 0; xColumn < childWidget.width; xColumn++) {
                            if(childWidget.invId[item] > 0) {
                                final ObjectType itemDef = ObjectType.get(childWidget.invId[item] - 1);
                                String itemData = itemDef.name;
                                if(itemDef.stackable || childWidget.invAmt[item] != 1) {
                                    itemData = itemData + " x" + Client.valueToKOrMLong(childWidget.invAmt[item]);
                                }
                                final int x = xPos + xColumn * (115 + childWidget.invPadX);
                                final int y = yPos + yColumn * (12 + childWidget.invPadY);
                                if(childWidget.textCenter) {
                                    font.drawCenteredEffectString(itemData, x + childWidget.width / 2, y, childWidget.color, childWidget.textShadow ? 0 : -1);
                                } else {
                                    font.drawLeftAlignedEffectString(itemData, x, y, childWidget.color, childWidget.textShadow ? 0 : -1);
                                }
                            }
                            item++;
                        }
                    }
                }
                else if(childWidget.type == Constants.WIDGET_TOOLTIP && (client.anInt1500 == childWidget.id || client.anInt1044 == childWidget.id || client.anInt1129 == childWidget.id) && client.anInt1501 == 50 && !client.menuOpened) {
                    int boxWidth = 0;
                    int boxHeight = 0;
                    final BitmapFont font = client.plainFont;
                    for(String message = childWidget.text; message.length() > 0; ) {
                        if(message.contains("%")) {
                            do {
                                final int k7 = message.indexOf("%1");
                                if(k7 == -1) {
                                    break;
                                }
                                message = message.substring(0, k7) + client.interfaceIntToString(client.extractInterfaceValues(childWidget, 0)) + message.substring(k7 + 2);
                            } while(true);
                            do {
                                final int l7 = message.indexOf("%2");
                                if(l7 == -1) {
                                    break;
                                }
                                message = message.substring(0, l7) + client.interfaceIntToString(client.extractInterfaceValues(childWidget, 1)) + message.substring(l7 + 2);
                            } while(true);
                            do {
                                final int i8 = message.indexOf("%3");
                                if(i8 == -1) {
                                    break;
                                }
                                message = message.substring(0, i8) + client.interfaceIntToString(client.extractInterfaceValues(childWidget, 2)) + message.substring(i8 + 2);
                            } while(true);
                            do {
                                final int j8 = message.indexOf("%4");
                                if(j8 == -1) {
                                    break;
                                }
                                message = message.substring(0, j8) + client.interfaceIntToString(client.extractInterfaceValues(childWidget, 3)) + message.substring(j8 + 2);
                            } while(true);
                            do {
                                final int k8 = message.indexOf("%5");
                                if(k8 == -1) {
                                    break;
                                }
                                message = message.substring(0, k8) + client.interfaceIntToString(client.extractInterfaceValues(childWidget, 4)) + message.substring(k8 + 2);
                            } while(true);
                        }
                        final int lineSplit = message.indexOf("\\n");
                        String splitMessage;
                        if(lineSplit != -1) {
                            splitMessage = message.substring(0, lineSplit);
                            message = message.substring(lineSplit + 2);
                        } else {
                            splitMessage = message;
                            message = "";
                        }
                        final int lineWidth = font.getEffectStringWidth(splitMessage);
                        if(lineWidth > boxWidth) {
                            boxWidth = lineWidth;
                        }
                        boxHeight += font.lineHeight + 1;
                    }

                    int skillId = -1;
                    int[] skillIds = {0, 3, 14, 2, 16, 13, 1, 15, 10, 4, 17, 7, 5, 12, 11, 6, 9, 8, 20, 18, 19, 21, 22, 23, 24};
                    for(int i = 0; i < 25; i++) {
                        if(Constants.MORE_DETAILS_PANEL_ID[i] == childWidget.id) {
                            if(client.currentStatGoals[skillIds[i]] == 0) {
                                break;
                            }
                            boolean maxLevel = false;
                            if(client.maxStats[skillIds[i]] >= 99) {
                                if(skillIds[i] != 24) {
                                    client.maxStats[skillIds[i]] = 99;
                                    maxLevel = true;
                                } else if(client.maxStats[skillIds[i]] >= 120) {
                                    client.maxStats[skillIds[i]] = 120;
                                    maxLevel = true;
                                }
                            }
                            if(!maxLevel)
                                skillId = i;
                            break;
                        }
                    }
                    if(skillId != -1) {
                        boxHeight += 15;
                    }

                    boxWidth += 6;
                    boxHeight += 7;
                    int x = xPos + childWidget.width - 5 - boxWidth;
                    int y = yPos + childWidget.height + 5;
                    if(x < xPos + 5) {
                        x = xPos + 5;
                    }
                    if(x + boxWidth > xPosition + widget.width) {
                        x = xPosition + widget.width - boxWidth;
                    }
                    if(y + boxHeight > yPosition + widget.height) {
                        y = yPos - boxHeight;
                    }
                    if(component == UIComponent.INVENTORY) {//Boundaries
                        if(widget.id == 3917) {//fixing skill menus
                            if(widget.childX[child] + boxWidth > 205) {
                                x -= (widget.childX[child] + boxWidth) - 190;
                            }
                            if(widget.childY[child] + boxHeight > 220) {
                                y -= (boxHeight + widget.childY[child]) - 160;
                            }
                        }
                    }

                    Rasterizer2D.fillRectangle(x, y, boxWidth, boxHeight, 0xFFFFA0);
                    Rasterizer2D.drawRectangle(x, y, boxWidth, boxHeight, 0);
                    if(skillId != -1) {
                        double percentage = (double) (client.currentStatGoals[skillIds[skillId]] != 0 ? (client.currentExp[skillIds[skillId]] * 100) / client.getXPForLevel(client.currentStatGoals[skillIds[skillId]]) : (client.currentExp[skillIds[skillId]] * 100) / client.getXPForLevel(client.currentStats[skillIds[skillId]] + 1));
                        Rasterizer2D.fillRectangle(x + 5, (y + boxHeight) - 15, boxWidth - 10, 12, 0xE62E00);
                        Rasterizer2D.fillRectangle(x + 5, (y + boxHeight) - 15, (int) ((percentage / 100) * (boxWidth - 10)), 12, 0x00B800);
                        Rasterizer2D.drawRectangle(x + 5, (y + boxHeight) - 15, boxWidth - 10, 12, 0);
                        client.smallFont.drawCenteredString(client.currentStatGoals[skillIds[skillId]] != 0 ? "To level " + client.currentStatGoals[skillIds[skillId]] : "Next level", x + (boxWidth / 2), (y + boxHeight) - 4, 0x000);
                    }
                    String message = childWidget.text;
                    for(int j11 = y + font.lineHeight + 2; message.length() > 0; j11 += font.lineHeight + 1) {
                        if(message.contains("%")) {
                            do {
                                final int k7 = message.indexOf("%1");
                                if(k7 == -1) {
                                    break;
                                }
                                message = message.substring(0, k7) + client.interfaceIntToString(client.extractInterfaceValues(childWidget, 0)) + message.substring(k7 + 2);
                            } while(true);
                            do {
                                final int l7 = message.indexOf("%2");
                                if(l7 == -1) {
                                    break;
                                }
                                message = message.substring(0, l7) + client.interfaceIntToString(client.extractInterfaceValues(childWidget, 1)) + message.substring(l7 + 2);
                            } while(true);
                            do {
                                final int i8 = message.indexOf("%3");
                                if(i8 == -1) {
                                    break;
                                }
                                message = message.substring(0, i8) + client.interfaceIntToString(client.extractInterfaceValues(childWidget, 2)) + message.substring(i8 + 2);
                            } while(true);
                            do {
                                final int j8 = message.indexOf("%4");
                                if(j8 == -1) {
                                    break;
                                }
                                message = message.substring(0, j8) + client.interfaceIntToString(client.extractInterfaceValues(childWidget, 3)) + message.substring(j8 + 2);
                            } while(true);
                            do {
                                final int k8 = message.indexOf("%5");
                                if(k8 == -1) {
                                    break;
                                }
                                message = message.substring(0, k8) + client.interfaceIntToString(client.extractInterfaceValues(childWidget, 4)) + message.substring(k8 + 2);
                            } while(true);
                        }
                        final int lineSplit = message.indexOf("\\n");
                        String splitMessage;
                        if(lineSplit != -1) {
                            splitMessage = message.substring(0, lineSplit);
                            message = message.substring(lineSplit + 2);
                        } else {
                            splitMessage = message;
                            message = "";
                        }
                        if(childWidget.textCenter) {
                            font.drawCenteredEffectString(splitMessage, x + childWidget.width / 2, j11, y, -1);
                        } else if(splitMessage.contains("\\r")) {
                            final String text = splitMessage.substring(0, splitMessage.indexOf("\\r"));
                            final String text2 = splitMessage.substring(splitMessage.indexOf("\\r") + 2);
                            font.drawLeftAlignedEffectString(text, x + 3, j11, 0, -1);
                            final int rightX = boxWidth + x - font.getEffectStringWidth(text2) - 2;
                            font.drawLeftAlignedEffectString(text2, rightX, j11, 0, -1);
                        } else {
                            font.drawLeftAlignedEffectString(splitMessage, x + 3, j11, 0, -1);
                        }
                    }
                }
            }
        }
        Rasterizer2D.setClip(saveClip);
    }



    }
