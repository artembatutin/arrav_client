package net.arrav.util.string;

import java.awt.*;

public class ColorConstants {
    public static final int GREEN_TEXT = 0x33AC25;
    public static final int RED_TEXT = 0xDF1F11;
    public final static int YELLOW = 0xffff01;
    public final static int BLUE = 0x005aff;
    public final static int GOLD = 0xffc600;
    public final static int SKILL_TAB_YELLOW = 0xf2eb5e;
    public final static int WHITE = 0xffffff;
    public final static int ORANGE = 0xff981f;
    public final static int BLACK = 0x000000;
    public final static int PALE_ORANGE = 0xc8aa64;
    public final static int RED = 0xfe3200;
    public final static int BURGUNDY = 0x800000;
    Color color = new Color(8388608);
    public final static int DARK_BLUE = 0x000080;
    public final static int GREEN = 0xFF00;
    public final static int PALE_GREEN = 0x46b556;
    public final static int LOADIN_1 = 0xFF403434;
    public final static int LOADIN_2 = 0xFF2F2929;
    public final static Color red       = new Color(PALE_GREEN).brighter();

    public final static FadingColor TEST_FADE = new FadingColor(PALE_GREEN);


    public static int lighten(int color) {
        Color c = new Color(color);
       return c.brighter().brighter().getRGB();
    }

    public static int fade(int color, int step) {
        int r, g, b, a;


        a = (color >> 24) * 0xff;
        r = (color >> 16) & 0xFF;
        g = (color >> 8) & 0xFF;
        b = (color) & 0xFF;

        r+= step & 0xff;
        g+= step & 0xff;
        b+= step & 0xff;

        return 0xff000000 | (a << 24) | (r << 16) | (g << 8) | (b);
    }

    public static void processFading() {
        TEST_FADE.fade();
        TEST_FADE.fade();
    }

    public static class FadingColor {

        int color;
        boolean up;

        public FadingColor(int color) {
            this.color = color;
        }

        public void fade() {
            int r, g, b;
            r = (color >> 16) & 0xFF;
            g = (color >> 8) & 0xFF;
            b = (color) & 0xFF;
            if(g == 255)
                up = false;
            if(g == 0)
                up = true;

            if(up) {
                g++;
            } else {
                g--;
            }
            color = 0xff000000  | (r << 16) | (g << 8) | (b);
        }

        public int getColor() {
            return color;
        }
    }


}
