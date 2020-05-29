package net.arrav.activity.panel.impl;

import net.arrav.Client;
import net.arrav.activity.panel.Panel;
import net.arrav.cache.unit.Interface;
import net.arrav.graphic.Rasterizer2D;

public class ClanSettingPanel extends Panel {

	private static final String[][] CONTROLS = {{"Anyone", "Friends"}, {"Anyone", "Friends", "Recruit", "Corporal", "Sergeant", "Lieutenant", "Captain", "General", "Only me"}, {"Friends", "Recruit", "Corporal", "Sergeant", "Lieutenant", "Captain", "General", "Only me"}, {"Friends", "Recruit", "Corporal", "Sergeant", "Lieutenant", "Captain", "General", "Only me"}};
	private static final String[] RANKS = {"Demote", "Recruit", "Corporal", "Sergeant", "Lieutenant", "Captain", "General",};
	private boolean advanced;
	private int scrollPos1 = 0;
	private int scrollMax1 = 0;
	private boolean scrollDrag1 = false;
	private int scrollDragPos1 = 0;
	private int scrollPos2 = 0;
	private int scrollMax2 = 0;
	private boolean scrollDrag2 = false;
	private int scrollDragPos2 = 0;

	@Override
	public boolean process() {
	    /* Initialization */
		int beginX = 4;
		int beginY = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			beginX = client.windowWidth / 2 - 380;
			beginY = client.windowHeight / 2 - 250;
		}

        /* Exit */
		if(processClose(beginX, beginY)) {
			return true;
		}

		int offset = 45;
		if(advanced) {
			for(int i = 0; i < 4; i++) {
				int height = i < 4 ? 50 : 27;
				if(i < 4) {
					if(client.rightClickInRegion(beginX + 8, beginY + offset, beginX + 125, beginY + offset + height)) {
						if(client.menuPos != CONTROLS[i].length) {
							for(int action = CONTROLS[i].length - 1; action >= 0; action--) {
								client.menuItemName[client.menuPos] = CONTROLS[i][action];
								client.menuItemCode[client.menuPos] = 647;
								client.menuItemArg2[client.menuPos] = action;
								client.menuItemArg3[client.menuPos] = 390 + i;
								client.menuPos++;
							}
						}
					}
				}
				offset += i < 4 ? 55 : 32;
			}
		}
		if(client.leftClickInRegion(beginX + 8, beginY + 265, beginX + 158, beginY + 295)) {
			client.outBuffer.putOpcode(213);
			client.outBuffer.putInt(701);
			client.outBuffer.putInt(0);
		}
		if(client.leftClickInRegion(beginX + 8, beginY + 297, beginX + 158, beginY + 327)) {
			client.outBuffer.putOpcode(213);
			client.outBuffer.putInt(701);
			client.outBuffer.putInt(1);
		}
		beginX += 135;
		scrollMax1 = Math.max(35 * countMuted() - 285, 0);

        /* Scrolling */
		if(client.mouseInRegion(beginX + 2, 42 + beginY, beginX + 182, beginY + 330)) {
			scrollPos1 += client.mouseWheelAmt * 24;
			if(scrollPos1 < 0) {
				scrollPos1 = 0;
			}
			if(scrollPos1 > scrollMax1) {
				scrollPos1 = scrollMax1;
			}
		}
		if(!scrollDrag1) {
			int height = 278;
			if(scrollMax1 > 0) {
				height = 285 * 278 / (scrollMax1 + 285);
			}
			int pos = 0;
			if(scrollPos1 != 0) {
				pos = scrollPos1 * 278 / (scrollMax1 + 285) + 1;
			}
			int x = 175 + beginX;
			int y = 46 + pos + beginY;
			if(client.mouseDragButton == 1 && client.mouseInRegion(x, y, x + 10, y + height)) {
				scrollDrag1 = true;
				scrollDragPos1 = scrollPos1;
			}
		} else if(client.mouseDragButton != 1) {
			scrollDrag1 = false;
		} else {
			int d = (client.mouseY - client.clickY) * (scrollMax1 + 285) / 278;
			scrollPos1 = scrollDragPos1 + d;
			if(scrollPos1 < 0) {
				scrollPos1 = 0;
			}
			if(scrollPos1 > scrollMax1) {
				scrollPos1 = scrollMax1;
			}
		}
		int muted = countMuted();
		scrollMax2 = Math.max(35 * (muted + client.clanBansList.length) - 285, 0);
		if(client.mouseInRegion(beginX + 185, 42 + beginY, beginX + 355, 330 + beginY)) {
			scrollPos2 += client.mouseWheelAmt * 24;
			if(scrollPos2 < 0) {
				scrollPos2 = 0;
			}
			if(scrollPos2 > scrollMax2) {
				scrollPos2 = scrollMax2;
			}
		}
		if(!scrollDrag2) {
			int height = 278;
			if(scrollMax2 > 0) {
				height = 285 * 278 / (scrollMax2 + 285);
			}
			int pos = 0;
			if(scrollPos2 != 0) {
				pos = scrollPos2 * 278 / (scrollMax2 + 285) + 1;
			}
			int x = 349 + beginX;
			int y = 46 + pos + beginY;
			if(client.mouseDragButton == 1 && client.mouseInRegion(x, y, x + 10, y + height)) {
				scrollDrag2 = true;
				scrollDragPos2 = scrollPos2;
			}
		} else if(client.mouseDragButton != 1) {
			scrollDrag2 = false;
		} else {
			int d = (client.mouseY - client.clickY) * (scrollMax2 + 285) / 278;
			scrollPos2 = scrollDragPos2 + d;
			if(scrollPos2 < 0) {
				scrollPos2 = 0;
			}
			if(scrollPos2 > scrollMax2) {
				scrollPos2 = scrollMax2;
			}
		}
		offset = -scrollPos1 + 45;
		for(int i = 0; i < client.clanMatesList.length; i++) {
			ClanMember name = client.clanMatesList[i];
			if(name == null)
				continue;
			if(name.getRank() <= 1)
				continue;
			if(client.rightClickInRegion(beginX + 8, beginY + offset, beginX + 158, beginY + offset + 30)) {
				for(int action = RANKS.length - 1; action >= 0; action--) {
					client.menuItemName[client.menuPos] = RANKS[action] + " " + name.getName();
					client.menuItemCode[client.menuPos] = 647;
					client.menuItemArg2[client.menuPos] = action;
					client.menuItemArg3[client.menuPos] = 400 + i;
					client.menuPos++;
				}
				break;
			}
			if(client.leftClickInRegion(beginX + 8, beginY + offset, beginX + 158, beginY + offset + 30)) {
				client.menuItemName[client.menuPos] = RANKS[0] + " " + name.getName();
				client.menuItemCode[client.menuPos] = 647;
				client.menuItemArg2[client.menuPos] = 0;
				client.menuItemArg3[client.menuPos] = 400 + i;
				client.menuPos++;
			}

			offset += 35;
		}

		offset = -scrollPos2 + 45;
		for(int i = 0; i < client.clanMatesList.length; i++) {
			ClanMember name = client.clanMatesList[i];
			if(name == null)
				continue;
			if(!name.isMuted())
				continue;
			if(client.leftClickInRegion(beginX + 188, beginY + offset, beginX + 338, beginY + offset + 30) || client.rightClickInRegion(beginX + 188, beginY + offset, beginX + 338, beginY + offset + 30)) {
				client.menuItemName[client.menuPos] = "Unmute " + name.getName();
				client.menuItemCode[client.menuPos] = 647;
				client.menuItemArg2[client.menuPos] = 0;
				client.menuItemArg3[client.menuPos] = 600 + i;
				client.menuPos++;
				break;
			}
			offset += 35;
		}
		offset = -scrollPos2 + 45 + muted * 35;
		for(int i = 0; i < client.clanBansList.length; i++) {
			String name = client.clanBansList[i];
			if(name == null || name.length() == 0)
				continue;
			if(client.leftClickInRegion(beginX + 188, beginY + offset, beginX + 338, beginY + offset + 30) || client.rightClickInRegion(beginX + 188, beginY + offset, beginX + 338, beginY + offset + 30)) {
				client.menuItemName[client.menuPos] = "Unban " + name;
				client.menuItemCode[client.menuPos] = 647;
				client.menuItemArg2[client.menuPos] = 1;
				client.menuItemArg3[client.menuPos] = 600 + i;
				client.menuPos++;
				break;
			}
			offset += 35;
		}

		return false;
	}

	@Override
	public void update() {
		/* Initialization */
		int beginX = 8;
		int beginY = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			beginX = client.windowWidth / 2 - 380;
			beginY = client.windowHeight / 2 - 250;
		}

		/* Main background */
		drawMain(beginX, beginY + 8, 500, 328, 0x000000, 0x63625e, 200);
		drawOver(beginX, beginY);
		drawClose(beginX, beginY);
		boldFont.drawLeftAlignedEffectString("Manage chat '" +Interface.cache[50306].text+ "'", beginX + 20, beginY + 31, 0xFF8A1F, true);
		smallFont.drawLeftAlignedEffectString("Ranked members", beginX + 180, beginY + 31, 0xFF8A1F, false);
		smallFont.drawLeftAlignedEffectString("Banned/Muted", beginX + 360, beginY + 31, 0xFF8A1F, false);
		Rasterizer2D.setClip(beginX + 5, beginY + 40, beginX + 493, beginY + 330);
		int offset = 45;
		if(advanced) {
			int[] info = {50308, 50311, 50314, 50317, 50320, 50321};
			for(int i = 0; i < 6; i++) {
				int height = i < 4 ? 50 : 27;
				Rasterizer2D.fillRoundedRectangle(beginX + 8, beginY + offset, 117, height, 3, 0x000000, 100);
				if(client.mouseInRegion(beginX + 8, beginY + offset, beginX + 125, beginY + offset + height)) {
					Rasterizer2D.fillRectangle(beginX + 8, beginY + offset, 117, height, 0, 40);
				}
				if(i < 4) {
					smallFont.drawCenteredEffectString(Interface.cache[info[i]].text, beginX + 66, beginY + offset + 16, 0xFF8A1F, true);
					boldFont.drawCenteredEffectString(Interface.cache[info[i] + 1].text, beginX + 66, beginY + offset + 34, 0xFFFFFF, true);
				} else {
					smallFont.drawCenteredEffectString(info[i] == 50320 ? "Rename" : "Delete", beginX + 68, beginY + offset + 18, info[i] == 50320 ? 0xFF8A1F : 0xFF0000, true);
				}
				offset += i < 4 ? 55 : 32;
			}
			Rasterizer2D.drawVerticalLine(beginX + 130, beginY + 30, 310, 0x000000);
		} else {
			smallFont.drawCenteredEffectString("Only the owner can", beginX + 66, beginY + offset + 106, 0xFF8A1F, true);
			smallFont.drawCenteredEffectString("change the clan chat", beginX + 66, beginY + offset + 121, 0xFF8A1F, true);
			smallFont.drawCenteredEffectString("settings here.", beginX + 66, beginY + offset + 136, 0xFF8A1F, true);
		}

		offset = -scrollPos1 + 45;
		beginX += 135;
		Rasterizer2D.fillRectangle(beginX + 2, 42 + beginY, 180, 288, 0x222222, 80);
		Rasterizer2D.drawRectangle(beginX + 2, 42 + beginY, 180, 288, 0x222222);
		Rasterizer2D.setClip(beginX + 3, 43 + beginY, beginX + 180, beginY + 328);
		for(int i = 0; i < client.clanMatesList.length; i++) {
			ClanMember name = client.clanMatesList[i];
			if(name == null)
				continue;
			if(name.getRank() <= 1)
				continue;
			Rasterizer2D.fillRoundedRectangle(beginX + 8, beginY + offset, 150, 30, 3, 0x000000, 100);
			if(client.mouseInRegion(beginX + 8, beginY + offset, beginX + 158, beginY + offset + 30)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 8, beginY + offset, 150, 30, 3, 0x000000, 40);
			}
			Client.spriteCache.get(1626 + name.getRank()).drawImage(beginX + 18, beginY + offset + 11);
			Rasterizer2D.fillRoundedRectangle(beginX + 8, beginY + offset, 30, 30, 3, 0xFF8A1F, 20);
			if(plainFont.getEffectStringWidth(name.getName()) < 130) {
				plainFont.drawCenteredEffectString(name.getName(), beginX + 90, beginY + offset + 19, 0xFFFFFF, true);
			} else {
				plainFont.drawLeftAlignedEffectString(name.getName(), beginX + 40, beginY + offset + 19, 0xFFFFFF, true);
			}

			offset += 35;
		}
		Rasterizer2D.removeClip();

		offset = -scrollPos2 + 45;
		Rasterizer2D.fillRectangle(beginX + 185, 42 + beginY, 170, 288, 0x222222, 80);
		Rasterizer2D.drawRectangle(beginX + 185, 42 + beginY, 170, 288, 0x222222);
		Rasterizer2D.setClip(beginX + 185, 42 + beginY, beginX + 355, 330 + beginY);

		for(int i = 0; i < client.clanMatesList.length; i++) {
			ClanMember name = client.clanMatesList[i];
			if(name == null)
				continue;
			if(!name.isMuted())
				continue;
			Rasterizer2D.fillRoundedRectangle(beginX + 188, beginY + offset, 150, 30, 3, 0x376884, 100);
			if(client.mouseInRegion(beginX + 188, beginY + offset, beginX + 338, beginY + offset + 30)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 188, beginY + offset, 150, 30, 3, 0x000000, 40);
			}
			boldFont.drawCenteredEffectString(name.getName().replace("@mut@", ""), beginX + 265, beginY + offset + 19, 0xFFFFFF, true);
			offset += 35;
		}
		for(int i = 0; i < client.clanBansList.length; i++) {
			String name = client.clanBansList[i];
			if(name == null || name.length() == 0)
				continue;
			Rasterizer2D.fillRoundedRectangle(beginX + 188, beginY + offset, 150, 30, 3, 0x460000, 100);
			if(client.mouseInRegion(beginX + 188, beginY + offset, beginX + 338, beginY + offset + 30)) {
				Rasterizer2D.fillRoundedRectangle(beginX + 188, beginY + offset, 150, 30, 3, 0x000000, 40);
			}
			boldFont.drawCenteredEffectString(name, beginX + 265, beginY + offset + 19, 0xFFFFFF, true);
			offset += 35;
		}
		Rasterizer2D.removeClip();

		Rasterizer2D.drawRectangle(166 + beginX, 45 + beginY, 12, 280, 0xffffff, 60);
		int height = 278;
		if(scrollMax1 > 0) {
			height = 285 * 278 / (scrollMax1 + 285);
		}
		int pos = 0;
		if(scrollPos1 != 0) {
			pos = scrollPos1 * 278 / (scrollMax1 + 285) + 1;
		}
		Rasterizer2D.fillRectangle(167 + beginX, 46 + pos + beginY, 10, height, 0x222222, 120);

		Rasterizer2D.drawRectangle(339 + beginX, 45 + beginY, 12, 280, 0xffffff, 60);
		int height2 = 278;
		if(scrollMax2 > 0) {
			height2 = 285 * 278 / (scrollMax2 + 285);
		}
		int pos2 = 0;
		if(scrollPos2 != 0) {
			pos2 = scrollPos2 * 278 / (scrollMax2 + 285) + 1;
		}
		Rasterizer2D.fillRectangle(340 + beginX, 46 + pos2 + beginY, 10, height2, 0x222222, 120);

		Rasterizer2D.removeClip();
	}
	
	public int countMuted() {
		int i = 0;
		for(ClanMember m : client.clanMatesList) {
			if(m == null)
				continue;
			if(m.isMuted())
				i++;
		}
		return i;
	}

	@Override
	public void initialize() {
		advanced = false;
		for(ClanMember m : client.clanMatesList) {
			if(m == null)
				continue;
			if(m.getRank() == 8) {
				if(m.getName().equalsIgnoreCase(client.localUsername)) {
					advanced = true;
				}
			}
		}
	}
	
	public static void prepare() {
		for(int fresh = 0; fresh < 100; fresh++) {
			if(Interface.cache[50144 + fresh] != null)
				Interface.cache[50144 + fresh].actions = new String[]{"Promote", "Mute", "Ban"};
		}
	}

	@Override
	public void reset() {
	}

	@Override
	public int getId() {
		return 1;
	}
	
	@Override
	public boolean blockedMove() {
		return false;
	}

	public static class ClanMember {

		private final String name;

		private final int rank;

		private boolean muted;

		public ClanMember(String name, int rank, boolean muted) {
			this.name = name;
			this.rank = rank;
			this.muted = muted;
		}

		public String getName() {
			return name;
		}

		int getRank() {
			return rank;
		}

		boolean isMuted() {
			return muted;
		}
	}
}
