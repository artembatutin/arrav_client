package net.arrav.cache.unit.interfaces.custom;

import net.arrav.Client;
import net.arrav.cache.unit.interfaces.Interface;
import net.arrav.cache.unit.interfaces.component.tab.Tab;
import net.arrav.graphic.font.BitmapFont;

public class BankInterface extends Interface {

    public static void bank(BitmapFont[] tda) {
        int interfaceId = 60_000;
        Interface bank = addInterface(interfaceId);
        bank.totalChildren(64);
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
        addConfigButton(interfaceId + 6, 60_000, 1359, 1360, 32, 20, "Toggle Insert/Swap", 1, 5, 116);
        addConfigButton(interfaceId + 7, 60_000, 1361, 1362, 32, 20, "Toggle Note/Un-note", 1, 5, 115);
        addHoverButton(interfaceId + 8, 1363, 35, 25, "Deposit Inventory", -1, interfaceId + 9, 1);
        addHoveredButton(interfaceId + 9, 1364, 35, 25, interfaceId + 10);
        addHoverButton(interfaceId + 11, 1365, 35, 25, "Deposit Equipment", -1, interfaceId + 12, 1);
        addHoveredButton(interfaceId + 12, 1366, 35, 25, interfaceId + 13);
        addHoverButton(interfaceId + 14, 1367, 35, 25, "Withdraw coins", -1, interfaceId + 15, 1);
        addHoveredButton(interfaceId + 15, 1368, 35, 25, interfaceId + 16);
        addText(interfaceId + 17, "%1", tda, 0, 0xFE9624, true);
        Interface.cache[interfaceId + 17].valueIndexArray = new int[][] { { 22, 5382, 0 } };
        addText(interfaceId + 18, "360", tda, 0, 0xFF981F, true, true);
        addText(interfaceId + 19, "search...", tda, 0, 0xFF981F, true, true);
        //addInputField(interfaceId + 19, 25, 0xFF981F, "Search...", 125, 22, false, true, "[A-Za-z0-9 ]");
        addHoverButton(interfaceId + 20, 298, 0, 0, "", -1, interfaceId + 21, 1);
        addHoveredButton(interfaceId + 21, 298, 0, 0, interfaceId + 22);
        addConfigButton(interfaceId + 23, 60_000, 1345, 1346, 15, 15, "Display tabs as items", 0, 5, 210);
        addHoverText(interfaceId + 24, "Item", "Display tabs as items", 0, 0xFF981F, false, true, 30);
        addConfigButton(interfaceId + 25, 60_000, 1345, 1346, 15, 15, "Display tabs as numbers", 1, 5, 210);
        addHoverText(interfaceId + 26, "Number", "Display tabs as numbers", 0, 0xFF981F, false, true, 40);
        addConfigButton(interfaceId + 27, 60_000, 1345, 1346, 15, 15, "Display tabs as roman numerals", 2, 5, 210);
        addHoverText(interfaceId + 28, "Roman", "Display tabs as roman numerals", 0, 0xFF981F, false, true, 35);
        addSprite(interfaceId + 29, 1357);
        addContainer(5382, 109, 9, 89, "Withdraw-1", "Withdraw-5", "Withdraw-10", "Withdraw-All", "Withdraw-X", null, "Withdraw-All but one");
        addConfigButton(interfaceId + 72, 60_000, 1372, 1371, 35, 25, "Release Place Holders", 0, 5, 116);
        addConfigButton(interfaceId + 73, 60_000, 1372, 1371, 35, 25, "Release Place Holders", 0, 5, 116);
        //addHoverButton(interfaceId + 73, "b", 1, 35, 25, "Toggle Place Holders", -1, interfaceId + 72, 1);
        addHoverButton(interfaceId + 73, 1372, 35, 25, "Toggle place holders", -1, interfaceId + 72, 1);
        addHoverButton(interfaceId + 74, 1369, 115, 25, "Access bank pins settings", -1, interfaceId + 75, 1);
        addHoveredButton(interfaceId + 75, 1370, 115, 25, interfaceId + 76);
        addHoveredButton(interfaceId + 77, 1370, 115, 25, interfaceId + 76);
        addHoveredButton(interfaceId + 78, 1370, 115, 25, interfaceId + 76);
        //addHoverButton(interfaceId + 77, 1, 35, 25, "Deposit coins", -1, interfaceId + 14, 1);
       // addHoverButton(interfaceId + 78, 1, 35, 25, "Check Vault", -1, interfaceId + 14, 1);
        addText(interfaceId + 79, "Bank value: 0", tda, 0, 0xFF981F, false, true);
        addText(interfaceId + 80, "Vault value: 0", tda, 0, 0xFF981F, false, true);

        addTabMenu(interfaceId+ 81, 0, 2018, Tab.STARTER, new String[9]);

        Interface.cache[5385].width += 22;
        Interface.cache[5385].height -= 18;
        Interface.cache[5385].scrollMax = 1444;
        Interface.cache[5382].contentType = 206;
        bank.child(0, interfaceId + 1, 12, 2);
        bank.child(1, interfaceId + 2, 470, 12);
        bank.child(2, interfaceId + 3, 470, 12);
        bank.child(3, interfaceId + 5, 255, 12);
        bank.child(4, interfaceId + 6, 20, 70);
        bank.child(5, interfaceId + 7, 20, 104);
        bank.child(6, interfaceId + 8, 20, 172);
        bank.child(7, interfaceId + 9, 20, 172);
        bank.child(8, interfaceId + 11, 20, 203);
        bank.child(9, interfaceId + 12, 20, 203);
        bank.child(10, interfaceId + 14, 20, 240);
        bank.child(11, interfaceId + 15, 20, 240);
        bank.child(12, interfaceId + 17, 472, 299);
        bank.child(13, interfaceId + 18, 472, 313);
        bank.child(14, interfaceId + 19, 318, 301);
        bank.child(15, interfaceId + 20, 195, 300);
        bank.child(16, interfaceId + 21, 195, 300);
        bank.child(17, interfaceId + 23, 23, 304);
        bank.child(18, interfaceId + 24, 42, 307 - 1);
        bank.child(19, interfaceId + 25, 70, 304);
        bank.child(20, interfaceId + 26, 88, 307 - 1);
        bank.child(21, interfaceId + 27, 130, 304);
        bank.child(22, interfaceId + 28, 148, 307 - 1);
        bank.child(53, interfaceId + 29, 58, 42);
        bank.child(54, 5385, 25, 66);
        bank.child(55, interfaceId + 72, 20, 138);
        bank.child(56, interfaceId + 73, 20, 138);
        bank.child(57, interfaceId + 74, 195, 300);
        bank.child(58, interfaceId + 75, 195, 300);
        bank.child(59, interfaceId + 77, 19, 240);
        bank.child(60, interfaceId + 78, 19, 240);
        bank.child(61, interfaceId + 79, 40, 14);
        bank.child(62, interfaceId + 80, 365, 14);
        bank.child(63, interfaceId + 81, 63, 25);
    }

}
