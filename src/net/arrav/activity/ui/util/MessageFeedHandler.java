package net.arrav.activity.ui.util;

import net.arrav.Client;
import net.arrav.graphic.Rasterizer2D;
import net.arrav.graphic.img.BitmapImage;

public class MessageFeedHandler {


    private String[] message = new String[5];
    private String[] colors = new String[5];
    private BitmapImage[] feedImage = new BitmapImage[5];
    private int[] feedAlpha = new int[5];
    private int[] feedYPos = new int[5];
    private int killsDisplayed = 5;

    private Client client;

    public MessageFeedHandler(Client client) {
        this.client = client;
    }

    public void pushKill(String message, String color) {
        for (int index = killsDisplayed - 1; index > 0; index--) {
            this.message[index] = this.message[index - 1];
            feedAlpha[index] = feedAlpha[index - 1];
            feedYPos[index] = feedYPos[index - 1];
            colors[index] = colors[index - 1];
        }
        this.message[0] = message;
        this.colors[0] = color;
        feedAlpha[0] = 0;
        feedYPos[0] = 0;
    }

    public void clearKill(int index) {
        message[index] = null;
        feedAlpha[index] = -1;
        feedYPos[index] = -1;
        colors[index] = null;
    }

    public void displayKillFeed() {
        int x = 5;
        for (int index = 0; index < killsDisplayed; index++) {
            if (message[index] != null && message[index] != null) {
                if (message[index].length() > 0) {
                    if (feedYPos[index] < (index + 1) * 22) {
                        feedYPos[index] += 1;
                        if (index == 0) {
                            feedAlpha[index] += 256 / 22;
                        }
                    } else if (feedYPos[index] == (index + 1) * 22) {
                        if (feedAlpha[index] > 200) {
                            feedAlpha[index] -= 1;
                        } else if (feedAlpha[index] <= 200 && feedAlpha[index] > 0) {
                            feedAlpha[index] -= 5;
                        }
                        if (feedAlpha[index] < 0) {
                            feedAlpha[index] = 0;
                        }
                        if (feedAlpha[index] == 0) {
                            clearKill(index);
                        }
                    }
                    if (feedAlpha[index] != 0) {
                        String statement = "<col="+colors[index]+">"+message[index]+"</col>";
                        Rasterizer2D.fillRectangle(x, feedYPos[index] + 55, client.smallFont.getStringWidth(statement) + 18, 19,
                                0x29221D, feedAlpha[index]);
                        Rasterizer2D.drawRectangle(x, feedYPos[index] + 55, client.smallFont.getStringWidth(statement) + 18, 19,
                                0x070606, feedAlpha[index]);
                       // client.smallFont.drawLeftAlignedString(statement, x + 9, feedYPos[index] + 69, 0xff, feedAlpha[index]);
                        client.smallFont.drawLeftAlignedEffectString("<trans=" + feedAlpha[index] + ">" + statement, x + 9,
                                feedYPos[index] + 69, 0xffffff, 0);
                    }
                }
            }
        }
    }
}
