package net.arrav.cache.unit.interfaces.custom;

import net.arrav.cache.unit.interfaces.Interface;
import net.arrav.cache.unit.interfaces.component.tab.Tab;
import net.arrav.graphic.font.BitmapFont;

import java.util.Arrays;

public class BankInterface extends Interface {

    public static void bank(BitmapFont[] tda) {
        int interfaceId = 60_000;
        Interface bank = addInterface(interfaceId);
        bank.totalChildren(82);
        int child = 23;
        //for (int tab = 0; tab < 40; tab += 4) {
        //    addButton(interfaceId + 31 + tab, 1355, 1356, "Collapse " + (tab == 0 ? "@or2@all tabs" : "tab @or2@" + (tab / 4)), 39, 40);
        //    int[] array = { 21, (tab / 4), 0 };
        //    if (tab / 4 == 0) {
        //        array = new int[] { 5, 211, 0 };
        //    }
        //    addHoverConfigButton(interfaceId + 32 + tab, interfaceId + 33 + tab, 1353, 1355, 39, 40, tab == 0 ? "View all" : "New tab", new int[] { 1, tab / 4 == 0 ? 1 : 3 }, new int[] { (tab / 4), 0 }, new int[][] { { 5, 211, 0 }, array });
        //    addHoveredConfigButton(Interface.cache[interfaceId + 32 + tab], interfaceId + 33 + tab, interfaceId + 34 + tab, 1354, 1355);
//
        //    Interface.cache[interfaceId + 32 + tab].parent = interfaceId;
        //    Interface.cache[interfaceId + 33 + tab].parent = interfaceId;
        //    Interface.cache[interfaceId + 34 + tab].parent = interfaceId;
//
        //    bank.child(child++, interfaceId + 31 + tab, 55 + 40 * (tab / 4), 37);
        //    bank.child(child++, interfaceId + 32 + tab, 55 + 40 * (tab / 4), 37);
        //    bank.child(child++, interfaceId + 33 + tab, 55 + 40 * (tab / 4), 37);
        //}
        addSprite(interfaceId + 1, 1344);
        addHoverButton(interfaceId + 2, 478, 15, 15, "Close", 250, interfaceId + 3, 3);
        addHoveredButton(interfaceId + 3, 461, 15, 15, interfaceId + 4);
        addText(interfaceId + 5, "The Bank of Rageps", tda, 2, 0xFF981F, true, true);
        addConfigButton(interfaceId + 6, 60_000, 2012, 2013, 32, 32, "Toggle Insert/Swap", 1, 5, 116);
        addConfigButton(interfaceId + 7, 60_000, 2004, 2005, 32, 32, "Toggle Note/Un-note", 1, 5, 115);
        addHoverButton(interfaceId + 8, 2023, 32, 32, "Deposit Inventory", -1, interfaceId + 9, 1);
        addHoveredButton(interfaceId + 9, 2024, 32, 32, interfaceId + 10);
        addHoverButton(interfaceId + 11, 2025, 32, 32, "Deposit Equipment", -1, interfaceId + 12, 1);
        addHoveredButton(interfaceId + 12, 2026, 32, 32, interfaceId + 13);
        addHoverButton(interfaceId + 14, 2027, 32, 32, "Deposit Familiar", -1, interfaceId + 15, 1);
        addHoveredButton(interfaceId + 15, 2028, 32, 32, interfaceId + 16);
        addText(interfaceId + 17, "%1", tda, 0, 0xFFFFFF, true);
        Interface.cache[interfaceId + 17].valueIndexArray = new int[][] { { 22, 270, 0 } };
        addText(interfaceId + 18, "360", tda, 0, 0xFFFFFF, true, true);
        addText(interfaceId + 19, "", tda, 0, 0xFE9624, true, true);
        addContainer(270, 109, 9, 89, "Withdraw-1", "Withdraw-5", "Withdraw-10", "Withdraw-All", "Withdraw-X", null, "Withdraw-All but one");
        addConfigButton(interfaceId + 72, 60_000, 1371, 1372, 35, 25, "Toggle place holders", 0, 5, 117);
        //addConfigButton(interfaceId + 73, 60_000, 1372, 1371, 35, 25, "Release Place Holders", 0, 5, 116);
        //addHoverButton(interfaceId + 73, 1372, 35, 25, "Toggle place holders", -1, interfaceId + 72, 1);
        //addHoverButton(interfaceId + 74, 1369, 115, 25, "Access bank pins settings", -1, interfaceId + 75, 1);
        addConfigButton(interfaceId + 74, 60_000, 2021, 2022, 35, 27, "Toggle Note/Un-note", 1, 5, 115);
        addHoveredButton(interfaceId + 75, 2005, 115, 25, interfaceId + 76);
        addText(interfaceId + 79, "Bank value: 0", tda, 0, 0xFF981F, false, true);
        addText(interfaceId + 80, "Vault value: 0", tda, 0, 0xFF981F, false, true);

        addTabMenu(interfaceId+ 81, 0, 2018, Tab.BANK, new String[10]);
        System.out.println("tab:"+(interfaceId + 81));

        addHoverButton(interfaceId + 82, 2010, 35, 35, "Edit presets", -1, interfaceId + 83, 1);
        addHoveredButton(interfaceId + 83, 2011, 35, 35, interfaceId + 84);

        int c = 90;
        for(int i = 0; i < 6; i++) {
            addHoverButton(interfaceId + (c++), 2014, 35, 35, "Loadout "+(i+1), -1, interfaceId + (c + 1), 1);
            addHoveredButton(interfaceId + (c++), 2015, 35, 35, interfaceId + (c + 1));
            c++;
            addText(interfaceId + (c++), (i+1)+"", tda, 0, 0xFF981F, false, true);
        }


        Interface.cache[5385].width += 22;
        Interface.cache[5385].height -= 12;
        Interface.cache[5385].scrollMax = 1444;


        Interface.cache[270].contentType = 206;
        Interface.cache[5385].children[0] = 270;
        bank.child(0, interfaceId + 1, 5, 2);
        bank.child(1, interfaceId + 2, 470, 6);
        bank.child(2, interfaceId + 3, 470, 6);
        bank.child(3, interfaceId + 5, 255, 7);
        bank.child(4, interfaceId + 6, 22, 293);
        bank.child(5, interfaceId + 7, 62, 293);
        bank.child(6, interfaceId + 8, 379+40, 293);
        bank.child(7, interfaceId + 9, 379+40, 293);
        bank.child(8, interfaceId + 11, 379+80, 293);
        bank.child(9, interfaceId + 12, 379+80, 293);
        bank.child(10, interfaceId + 14, 379, 293);
        bank.child(11, interfaceId + 15, 379, 293);
        bank.child(12, interfaceId + 17, 270, 297);
        bank.child(13, interfaceId + 18, 289, 305);
        bank.child(14, interfaceId + 19, 318, 301);
        bank.child(53, 5385, -5, 69);
        bank.child(54, interfaceId + 72, 102, 293);
        bank.child(55, interfaceId + 74, 379-40, 293);
        bank.child(56, interfaceId + 75, 61, 298);
        bank.child(59, interfaceId + 79, 40, 9);
        bank.child(60, interfaceId + 80, 365, 9);
        bank.child(61, interfaceId + 81, 19, 28);

        bank.child(62, interfaceId + 82, 462, 28);
        bank.child(63, interfaceId + 83, 462, 28);//+35

        int y = 28 + 37;
        c = 63;
        int c2 = 90;
        //System.out.println("v");
        for (int i = 0; i < 6; i++) {
            bank.child(c++, interfaceId + (c2++), 462, y);
            bank.child(c++, interfaceId + (c2++), 462, y);//+35
            c2++;
            bank.child(c++, interfaceId + (c2++), 462 + 23, y+4);//+35
            y += 37;
           // System.out.println(c);
            }
    }

}
