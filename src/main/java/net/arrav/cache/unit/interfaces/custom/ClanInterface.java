package net.arrav.cache.unit.interfaces.custom;

import net.arrav.cache.unit.interfaces.Interface;
import net.arrav.graphic.font.BitmapFont;

public class ClanInterface  extends Interface {

    public static void init(BitmapFont[] tda) {
        clanChat(tda);
        clanInformation(tda);
        clanInformation2(tda);
        clanManage(tda);
        clanManageScroll(tda);
        clanMemberManage(tda);
        clanShowcase(tda);
    }

    private static void clanShowcase(BitmapFont[] TDA) {
        Interface tab = addInterface(57700);
        addSprite(57701, 702);
        addText(57702, "Clan Showcase", TDA, 2, 0xff981f, true, true);
        addHoverButton(57703, 24, 21, 21, "Close", 250, 57704, 3);
        addHoveredButton(57704, 25, 21, 21, 57705);
        addHoverButton(57706, 298, 63, 25, "Confirm", -1, 57707, 1);
        addHoveredButton(57707, 700, 63, 25, 57708);
        addHoverButton(57709, 298, 63, 25, "Delete", -1, 57710, 1);
        addHoveredButton(57710, 701, 63, 25, 57711);
        addText(57712, "Confirm", TDA, 0, 0xFFFFFF, true, true);
        addText(57713, "Delete", TDA, 0, 0xFFFFFF, true, true);
        addText(57714, "You can earn items to display in your showcase by progressing", TDA, 0, 0xd8c491, true, true);
        addText(57715, "your clan or by purchasing the mystery showcase box.", TDA, 0, 0xd8c491, true, true);
        addContainer2(57716, 0, 6, 5, 22, 21, 100, false, false, false, "Select");
        addContainer3(57717, 0, 6, 5, 22, 21, 50, false, false, false);
        addText(57718, "3/28", TDA, 0, 0xff981f, true, true);
        tab.totalChildren(15);
        tab.child(0, 57701, 85, 2);
        tab.child(1, 57702, 250, 11);
        tab.child(2, 57703, 397, 11);
        tab.child(3, 57704, 397, 11);
        tab.child(4, 57706, 353, 272);
        tab.child(5, 57707, 353, 272);
        tab.child(6, 57709, 353, 300);
        tab.child(7, 57710, 353, 300);
        tab.child(8, 57712, 391, 277);
        tab.child(9, 57713, 381, 306);
        tab.child(10, 57714, 250, 37);
        tab.child(11, 57715, 250, 47);
        tab.child(12, 57716, 100, 69);
        tab.child(13, 57717, 315, 283);
        tab.child(14, 57718, 370, 13);
    }

    private static void clanManageScroll(BitmapFont[] tda) {
        Interface scrollInterface = addTabInterface(42100);
        addText(42101, "Name:", tda, 0, 0xDE8B0D, true, true);
        addInputField(42102, 10, 0xFF981F, "Enter name...", 115, 25, false, false, "[A-Za-z0-9 ]");
        addText(42103, "Tag:", tda, 0, 0xDE8B0D, true, true);
        addInputField(42104, 4, 0xFF981F, "Enter tag...", 115, 25, false, false, "[A-Za-z0-9 ]");
        addText(42105, "Slogan:", tda, 0, 0xDE8B0D, true, true);
        addInputField(42106, 24, 0xFF981F, "Enter slogan...", 115, 25, false, false, "[A-Za-z0-9 ]");
        addText(42107, "Pass:", tda, 0, 0xDE8B0D, true, true);
        addInputField(42108, 10, 0xFF981F, "Enter password...", 115, 25, false, false, "[A-Za-z0-9 ]");
        addText(42137, "Thread:", tda, 0, 0xDE8B0D, true, true);
        addInputField(42138, 25, 0xFF981F, "Enter thread url...", 115, 25, false, false, "[A-Za-z0-9 ]");
        addText(42109, "Type:", tda, 0, 0xDE8B0D, true, true);
        addDropdownMenu(42110, 115, 0, false, false, Dropdown.DEFAULT, "<icon=8> PvP", "<icon=3> PvM",
                "<icon=5> Skilling", "<icon=20> Social", "<icon=18> Ironman");
        addText(42111, "Enter:", tda, 0, 0xDE8B0D, true, true);
        addDropdownMenu(42112, 115, 0, false, false, Dropdown.DEFAULT, "Anyone", "<clan=0> Friends", "<clan=1> Recruit",
                "<clan=2> Corporeal", "<clan=3> Sergeant", "<clan=4> Lieutenant", "<clan=5> Captain",
                "<clan=6> General", "<clan=7> Leader");
        addText(42113, "Talk:", tda, 0, 0xDE8B0D, true, true);
        addDropdownMenu(42114, 115, 0, false, false, Dropdown.DEFAULT, "Anyone", "<clan=0> Friends", "<clan=1> Recruit",
                "<clan=2> Corporeal", "<clan=3> Sergeant", "<clan=4> Lieutenant", "<clan=5> Captain",
                "<clan=6> General", "<clan=7> Leader");
        addText(42115, "Manage:", tda, 0, 0xDE8B0D, true, true);
        addDropdownMenu(42116, 115, 0, false, false, Dropdown.DEFAULT, "Anyone", "<clan=0> Friends", "<clan=1> Recruit",
                "<clan=2> Corporeal", "<clan=3> Sergeant", "<clan=4> Lieutenant", "<clan=5> Captain",
                "<clan=6> General", "<clan=7> Leader");
        addHoverButton(42117, 699, 36, 36, "Change item showcase", -1, 42118, 1);
        addHoveredButton(42118, 287, 36, 36, 42119);
        addHoverButton(42120, 699, 36, 36, "Change item showcase", -1, 42121, 1);
        addHoveredButton(42121, 287, 36, 36, 42122);
        addHoverButton(42123, 699, 36, 36, "Change item showcase", -1, 42124, 1);
        addHoveredButton(42124, 287, 36, 36, 42125);
        addContainer(42126, 3, 7, 9, 8, false, false, false, null, null, null, null, null);
        addHoverText(42127, "Lock clan (no one can join)", "Toggle clan lock", 0, 0xEB981F, 0xFFFFFF, false, true, 60);
        addConfigButton(42128, 42100, 235, 234, 15, 15, "Toggle clan lock", 1, 1, 326, false);
        addText(42129, "Banned Members", tda, 0, 0xDE8B0D, false, true);
        addHoverButton(42130, 391, 118, 32, "Manage banned members", 0, 42131, 1);
        addHoveredButton(42131, 392, 118, 32, 42132);
        addText(42133, "Color:", tda, 0, 0xDE8B0D, true, true);
        addDropdownMenu(42134, 115, 0, false, false, Dropdown.DEFAULT, "<col=ffffff>White", "<col=F03737>Red",
                "<col=2ADE36>Green", "<col=2974FF>Blue", "<col=EBA226>Orange", "<col=A82D81>Purple",
                "<col=FF57CA>Pink");
        addText(42135, "To confirm an input field change", tda, 0, 0xA1D490, true, true);
        addText(42136, "press enter after filling it out", tda, 0, 0xA1D490, true, true);

        scrollInterface.totalChildren(34);
        scrollInterface.scrollPos = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 178;
        scrollInterface.height = 190;
        scrollInterface.scrollMax = 460;

        scrollInterface.child(0, 42117, 75, 8 + 30);
        scrollInterface.child(1, 42118, 75, 8 + 30);
        scrollInterface.child(2, 42120, 35, 8 + 30);
        scrollInterface.child(3, 42121, 35, 8 + 30);
        scrollInterface.child(4, 42123, 115, 8 + 30);
        scrollInterface.child(5, 42124, 115, 8 + 30);
        scrollInterface.child(6, 42126, 38, 8 + 30);
        scrollInterface.child(7, 42101, 28, 55 + 30);
        scrollInterface.child(8, 42102, 60, 50 + 30);
        scrollInterface.child(9, 42103, 28, 85 + 30);
        scrollInterface.child(10, 42104, 60, 80 + 30);
        scrollInterface.child(11, 42105, 28, 115 + 30);
        scrollInterface.child(12, 42106, 60, 110 + 30);
        scrollInterface.child(13, 42107, 28, 145 + 30);
        scrollInterface.child(14, 42108, 60, 140 + 30);
        scrollInterface.child(15, 42115, 28, 265 + 60);
        scrollInterface.child(16, 42127, 35, 295 + 90 + 5);
        scrollInterface.child(17, 42128, 15, 293 + 90 + 5);
        scrollInterface.child(18, 42130, 35, 320 + 90 + 5);
        scrollInterface.child(19, 42131, 35, 320 + 90 + 5);
        scrollInterface.child(20, 42129, 52, 328 + 90 + 5);
        scrollInterface.child(21, 42133, 30, 290 + 60);
        scrollInterface.child(22, 42134, 60, 290 + 60);
        scrollInterface.child(23, 42116, 60, 260 + 60);
        scrollInterface.child(24, 42113, 28, 235 + 60);
        scrollInterface.child(25, 42114, 60, 230 + 60);
        scrollInterface.child(26, 42111, 28, 205 + 60);
        scrollInterface.child(27, 42112, 60, 200 + 60);
        scrollInterface.child(28, 42109, 28, 175 + 60);
        scrollInterface.child(29, 42110, 60, 170 + 60);
        scrollInterface.child(30, 42135, 95, 6);
        scrollInterface.child(31, 42136, 95, 6 + 15);
        scrollInterface.child(32, 42137, 28, 145 + 60);
        scrollInterface.child(33, 42138, 60, 140 + 60);
    }

    private static void clanManage(BitmapFont[] tda) {
        Interface Interface = addTabInterface(42000);
        addText(42001, "Clan Management", tda, 2, 0xDE8B0D, true, true);
        addSprite(42004, 265);
        addSprite(42005, 64);

        addHoverButton(42006, 391, 118, 32, "Return to main tab", 0, 42007, 1);
        addHoveredButton(42007, 392, 118, 32, 42008);
        addText(42009, "Close Tab", tda, 3, 0xF7DC6F, true, true);
        Interface.totalChildren(9);
        Interface.child(0, 42001, 97, 5);
        Interface.child(1, 42004, -5, 25);
        Interface.child(2, 42004, -5, 75);
        Interface.child(3, 42005, 0, 25);
        Interface.child(4, 42005, 0, 217);
        Interface.child(5, 42100, -3, 27);
        Interface.child(6, 42006, 40, 224);
        Interface.child(7, 42007, 40, 224);
        Interface.child(8, 42009, 98, 230);
    }

    private static void clanChat(BitmapFont[] tda) {
        Interface Interface = addInterface(33500);
        addSprite(33501, 67);
        addText(33502, "Overload", tda, 3, 0xDE8B0D, true, true);
        addSprite(33503, 68);
        addHoverButton(33507, 834, 20, 20, "Join clan", -1, 33508, 1);
        addHoveredButton(33508, 835, 20, 20, 33509);
        addHoverButton(33510, 836, 20, 20, "Leave clan", -1, 33511, 1);
        addHoveredButton(33511, 837, 20, 20, 33512);
        addHoverButton(33513, 838, 20, 20, "View clan information", -1, 33514, 1);
        addHoveredButton(33514, 839, 20, 20, 33515);
        addHoverButton(33520, 840, 20, 20, "Manage clan", -1, 33521, 1);
        addHoveredButton(33521, 841, 20, 20, 33522);
        addConfigButton(33518, 33500, 706, 705, 24, 22, "Manage Lootshare", 0, 5, 393, false);
        addConfigButton(33525, 33500, 444, 443, 24, 12, "Sort by privilege", 0, 5, 394, false);
        addConfigButton(33526, 33500, 446, 445, 117, 12, "Sort by name", 1, 5, 394, false);
        addConfigButton(33527, 33500, 448, 447, 56, 12, "Sort by rank", 2, 5, 394, false);
        Interface.totalChildren(16);
        Interface.child(0, 33502, 97, 2);
        Interface.child(1, 33507, 5, 237);
        Interface.child(2, 33508, 5, 237);
        Interface.child(3, 33510, 25, 235);
        Interface.child(4, 33511, 25, 235);
        Interface.child(5, 33513, 45, 235);
        Interface.child(6, 33514, 45, 235);
        Interface.child(7, 33520, 65, 235);
        Interface.child(8, 33521, 65, 235);
        Interface.child(9, 33518, 155, 235);
        Interface.child(10, 33530, -3, 38);
        Interface.child(11, 33503, 0, 25);
        Interface.child(12, 33503, 0, 223);
        Interface.child(13, 33525, 0, 26);
        Interface.child(14, 33526, 20, 26);
        Interface.child(15, 33527, 135, 26);
        Interface scrollInterface = addTabInterface(33530);
        scrollInterface.scrollPos = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 178;
        scrollInterface.height = 188;
        scrollInterface.scrollMax = 189;
        int x = 3, y = 0, child = 0, sprite = 0, size = 600;
        scrollInterface.totalChildren(size);
        for (int i = 0; i < size; i += 4) {
            sprite++;
            int id = sprite % 2 == 0 ? 65 : 66;
            addSprite(33531 + i, id);
            addText((33531 + (i + 1)), "", tda, 3, 0xA4997D, false, true);
            addHoverText((33531 + (i + 2)), "", "", 3, 0xA4997D, false, true, 60);
            addText((33531 + (i + 3)), "", tda, 3, 0xF7DC6F, false, true);
            scrollInterface.child(child++, 33531 + i, x, y);
            scrollInterface.child(child++, (33531 + (i + 1)), x + 5, y + 4);
            scrollInterface.child(child++, (33531 + (i + 2)), x + 20, y + 1);
            scrollInterface.child(child++, (33531 + (i + 3)), x + 140, y + 1);
            y += 21;
        }
    }

    private static void clanMemberManage(BitmapFont[] tda) {
        Interface menu = addTabInterface(43600);
        addSprite(43601, 709);
        addText(43602, "Member Management", tda, 2, 0xDE8B0D, true, true);
        addHoverButton(43603, 24, 16, 16, "Close", 250, 43604, 3);
        addHoveredButton(43604, 25, 16, 16, 43605);
        addText(43606, "Daniel", tda, 0, 0xDE8B0D, true, true);
        addHoverButton(43607, 288, 150, 35, "Promote member", -1, 43608, 1);
        addHoveredButton(43608, 289, 150, 35, 43609);
        addHoverButton(43610, 288, 150, 35, "Demote member", -1, 43611, 1);
        addHoveredButton(43611, 289, 150, 35, 43612);
        addHoverButton(43613, 288, 150, 35, "Kick member", -1, 43614, 1);
        addHoveredButton(43614, 289, 150, 35, 43615);
        addHoverButton(43616, 288, 150, 35, "Ban member", -1, 43617, 1);
        addHoveredButton(43617, 289, 150, 35, 43618);
        addText(43619, "Promote Member", tda, 3, 0xDE8B0D, true, true);
        addText(43620, "Demote Member", tda, 3, 0xDE8B0D, true, true);
        addText(43621, "Kick Member", tda, 3, 0xDE8B0D, true, true);
        addText(43622, "Ban Member", tda, 3, 0xDE8B0D, true, true);
        menu.totalChildren(17);
        menu.child(0, 43601, 125, 35);
        menu.child(1, 43602, 259, 43);
        menu.child(2, 43603, 365, 43);
        menu.child(3, 43604, 365, 44);
        menu.child(4, 43606, 259, 81);
        menu.child(5, 43607, 182, 108);
        menu.child(6, 43608, 182, 108);
        menu.child(7, 43610, 182, 152);
        menu.child(8, 43611, 182, 152);
        menu.child(9, 43613, 182, 196);
        menu.child(10, 43614, 182, 196);
        menu.child(11, 43616, 182, 241);
        menu.child(12, 43617, 182, 241);
        menu.child(13, 43619, 259, 114);
        menu.child(14, 43620, 259, 158);
        menu.child(15, 43621, 259, 203);
        menu.child(16, 43622, 259, 248);
    }

    private static void clanInformation2(BitmapFont[] tda) {
        Interface Interface = addTabInterface(43500);
        addSprite(45001, 435);
        Interface.totalChildren(14);

        Interface.child(0, 45001, 13, 13);
        Interface.child(1, 43002, 473, 22);
        Interface.child(2, 43003, 473, 22);
        Interface.child(3, 43005, 258, 21);
        Interface.child(4, 43006, 95, 46);
        Interface.child(5, 43009, 275, 88);
        Interface.child(6, 43010, 275, 105);
        Interface.child(7, 43011, 351, 85);
        Interface.child(8, 43013, 180, 48);
        Interface.child(9, 43014, 340, 48);
        Interface.child(10, 43007, 258, 46);
        Interface.child(11, 43008, 417, 46);
        Interface.child(12, 45010, 189, 140);
        Interface.child(13, 45050, 18, 74);

        Interface clanMembersList = addTabInterface(45050);
        clanMembersList.scrollPos = 0;
        clanMembersList.contentType = 0;
        clanMembersList.width = 138;
        clanMembersList.height = 240;
        clanMembersList.scrollMax = 385;
        clanMembersList.totalChildren(200);
        int y = 1, count = 0;
        for (int i = 0, child = 0; i < 200; i += 2) {
            addSprite(45051 + i, count % 2 == 0 ? 453 : 454);
            addHoverText((45051 + (i + 1)), "", "", 0, 0xFF9900, 0xFFFFFF, false, true, 168);
            clanMembersList.child(child++, 45051 + i, 2, y + 1);
            clanMembersList.child(child++, 45051 + (1 + i), 7, y + 4);
            y += 22;
            count++;
        }

        Interface clanMemberInfo = addTabInterface(45010);
        clanMemberInfo.scrollPos = 0;
        clanMemberInfo.contentType = 0;
        clanMemberInfo.width = 276;
        clanMemberInfo.height = 160;
        clanMemberInfo.scrollMax = 385;
        clanMemberInfo.totalChildren(20);
        y = 1;
        count = 0;
        for (int i = 0, child = 0; i < 20; i += 2) {
            addSprite(45011 + i, count % 2 == 0 ? 453 : 454);
            addText(45011 + (1 + i), "2", tda, 0, 0xDE8B0D, false, true);
            clanMemberInfo.child(child++, 45011 + i, 2, y + 1);
            clanMemberInfo.child(child++, 45011 + (1 + i), 7, y + 4);
            y += 22;
            count++;
        }

    }

    private static void clanInformation(BitmapFont[] tda) {
        Interface Interface = addTabInterface(43000);
        addSprite(43001, 833);
        addHoverButton(43002, 24, 16, 16, "Close", -1, 43003, 1);
        addHoveredButton(43003, 25, 16, 16, 43004);
        addText(43005, "Clan Viewer", tda, 2, 0xFF981F, true, true);
        addText(43006, "Top Clans:", tda, 3, 0xEB981F, true, true);
        addText(43007, "Clan Overview", tda, 3, 0xEB981F, true, true);
        addText(43008, "Clan Members", tda, 3, 0xEB981F, true, true);
        addText(43009, "Teh Beasts", tda, 1, 0xF7DC6F, true, true);
        addText(43010, "Unleash your inner beast", tda, 0, 0xC47423, true, true);
        addContainer(43011, 3, 7, 9, 8, false, false, false, null, null, null, null, null);
        addInputField(43012, 15, 0xFF981F, "Search for clan...", 156, 24, false, false, "[A-Za-z0-9 ]");
        addConfigButton(43013, 43000, 433, 434, 159, 19, "View clan overview", 0, 5, 531, false);
        addConfigButton(43014, 43000, 433, 434, 159, 19, "View clan members", 1, 5, 531, false);
        addHoverButton(43015, 391, 118, 32, "Join clan channel", -1, 43016, 1);
        addHoveredButton(43016, 392, 118, 32, 43017);
        addText(43018, "Join a Clan", tda, 3, 0xFF981F, true, true);
        addDropdownMenu(43019, 159, 0, false, true, Dropdown.DEFAULT, "All Time", "Top PvP", "Top PvM", "Top Skilling",
                "Top Ironman");

//        addHoverButton(43020, 391, 118, 32, "Join clan channel", -1, 43021, 1);
//        addHoveredButton(43021, 392, 118, 32, 43022);
//        addText(43018, "Join Clan", tda, 3, 0xFF981F, true, true);

        Interface.totalChildren(19);
        Interface.child(0, 43001, 13, 13);
        Interface.child(1, 43002, 473, 22);
        Interface.child(2, 43003, 473, 22);
        Interface.child(3, 43005, 258, 21);
        Interface.child(4, 43006, 95, 46);
        Interface.child(5, 43009, 275, 88);
        Interface.child(6, 43010, 275, 105);
        Interface.child(7, 43011, 351, 85);
        Interface.child(8, 43100, 189, 140);
        Interface.child(9, 43200, 18, 74);
        Interface.child(10, 43012, 18, 291);
        Interface.child(11, 43013, 180, 48);
        Interface.child(12, 43014, 340, 48);
        Interface.child(13, 43007, 258, 46);
        Interface.child(14, 43008, 417, 46);
        Interface.child(15, 43015, 280, 270);
        Interface.child(16, 43016, 280, 270);
        Interface.child(17, 43018, 335, 275);
        Interface.child(18, 43019, 18, 18);
        Interface clanDetailScroll = addTabInterface(43100);
        clanDetailScroll.scrollPos = 0;
        clanDetailScroll.contentType = 0;
        clanDetailScroll.width = 276;
        clanDetailScroll.height = 128;
        clanDetailScroll.scrollMax = 385;
        clanDetailScroll.totalChildren(40);
        int y = 1, count = 0;
        for (int i = 0, child = 0; i < 40; i += 2) {
            addSprite(43101 + i, count % 2 == 0 ? 453 : 454);
            addText(43101 + (1 + i), "", tda, 0, 0xDE8B0D, false, true);
            clanDetailScroll.child(child++, 43101 + i, 2, y + 1);
            clanDetailScroll.child(child++, 43101 + (1 + i), 7, y + 4);
            y += 22;
            count++;
        }
        Interface clanListScroll = addTabInterface(43200);
        clanListScroll.scrollPos = 0;
        clanListScroll.contentType = 0;
        clanListScroll.width = 140;
        clanListScroll.height = 216;
        clanListScroll.scrollMax = 385;
        clanListScroll.totalChildren(40);
        y = 1;
        count = 0;
        for (int i = 0, child = 0; i < 40; i += 2) {
            int id;
            int color;
            int x = 0;
            if (i == 0) {
                id = 843;
                color = 0xFF9900;
                x += 2;
            } else if (i == 2) {
                id = 844;
                color = 0xFF9900;
                x += 2;
            } else if (i == 4) {
                id = 845;
                color = 0xFF9900;
                x += 2;
            } else {
                id = count % 2 == 0 ? 453 : 454;
                color = 0xFF9900;
            }

            addSprite(43201 + i, id);
            addHoverText((43201 + (i + 1)), "", "", 0, color, 0xFFFFFF, false, true, 168);
            clanListScroll.child(child++, 43201 + i, x, y + 1);
            clanListScroll.child(child++, 43201 + (1 + i), 7, y + 4);
            y += 22;
            count++;
        }
    }
}
