package net.arrav.activity.ui.util;

import net.arrav.Client;
import net.arrav.Config;
import net.arrav.graphic.Rasterizer2D;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class CombatOverlayHandler {


    public String entityFeedName;
    public int entityFeedHP;
    public int entityFeedMaxHP;
    public int entityFeedHP2;
    public int entityAlpha;
    private int entityTick;

    private Client client;

    public CombatOverlayHandler(Client client) {
        this.client = client;
    }

    public void pushFeed(String entityName, int HP, int maxHP) {
        entityFeedHP2 = entityFeedHP <= 0 ? entityFeedMaxHP : entityFeedHP;
        entityFeedName = entityName;
        entityFeedHP = HP;
        entityFeedMaxHP = maxHP;
        entityAlpha = 255;
        entityTick = entityName.isEmpty() ? 0 : 600;
    }

    public void displayEntityFeed() {
        if (entityFeedName == null)
            return;
        if (entityFeedHP == 0)
            return;
        if (entityTick-- <= 0)
            return;

        double percentage = entityFeedHP / (double) entityFeedMaxHP;
        double percentage2 = (entityFeedHP2 - entityFeedHP) / (double) entityFeedMaxHP;
        int width = (int) (135 * percentage);

        if (width > 132)
            width = 132;

        int xOff = 3;
        int yOff = 25;

        // background
        Rasterizer2D.fillRectangle(xOff, yOff, 141, 50, 0x4c433d, 155);
        Rasterizer2D.drawRectangle(xOff, yOff, 141, 50, 0x332f2d, 255);

        // name
        client.plainFont.drawCenteredEffectString(entityFeedName, xOff + 69, yOff + 23, 0xFDFDFD, 0);

        // Hp fill
        Rasterizer2D.fillRectangle(xOff + 7, yOff + 30, width - 4, 16, 0x66b754, 130);
        Rasterizer2D.fillRectangle(xOff + 7, yOff + 30, width - 4, 16, 0x66b754, 130);

        // Hp empty
        Rasterizer2D.fillRectangle(xOff + 4 + width, yOff + 30, 135 - width - 4, 16, 0xc43636, 130);

        if (entityAlpha > 0) {
            entityAlpha -= 5;
            Rasterizer2D.fillRectangle(xOff + 4 + width, yOff + 32, (int) (135 * percentage2) - 4, 12, 0xFFDB00,
                    (int) (130 * entityAlpha / 255.0));
        }

        Rasterizer2D.drawRectangle(xOff + 7, yOff + 30, 128, 16, 0x332f2d, 130);


        int hp =  Config.def.hits() ? (entityFeedHP * 10) : entityFeedHP;
        int maxHp =  Config.def.hits() ? (entityFeedMaxHP * 10) : entityFeedMaxHP;

        // HP text
        client.smallFont.drawCenteredString(NumberFormat.getInstance(Locale.US).format(hp) + " / "
                + NumberFormat.getInstance(Locale.US).format(maxHp), xOff + 72, yOff + 43, 0xFDFDFD, 240);
    }
    
}
