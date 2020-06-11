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
    public final static Color red       = new Color(243, 234, 234, 255).brighter();


    public static int lighten(int color) {
        Color c = new Color(color);
       return c.brighter().brighter().getRGB();
    }


}
