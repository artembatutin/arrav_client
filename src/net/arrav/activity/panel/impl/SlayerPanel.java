package net.arrav.activity.panel.impl;

import net.arrav.Client;
import net.arrav.activity.panel.Panel;
import net.arrav.cache.unit.Interface;
import net.arrav.cache.unit.ObjectType;
import net.arrav.media.Rasterizer2D;
import net.arrav.media.img.BitmapImage;

import java.util.Objects;

public class SlayerPanel extends Panel {
	
	@Override
	public boolean process() {
	    /* Initialization */
		int beginX = 4;
		int beginY = 0;
		if(client.uiRenderer.isResizableOrFull()) {
			beginX = client.windowWidth / 2 - 380;
			beginY = client.windowHeight / 2 - 250;
		}
		
		if(client.leftClickInRegion(beginX + 180, beginY + 65, beginX + 234, beginY + 85)) {
			client.outBuffer.putOpcode(185);
			client.outBuffer.putShort(112);
			return true;
		}
		if(client.leftClickInRegion(beginX + 120, beginY + 65, beginX + 174, beginY + 85)) {
			client.outBuffer.putOpcode(185);
			client.outBuffer.putShort(111);
			return true;
		}
		if(client.leftClickInRegion(beginX + 50, beginY + 65, beginX + 114, beginY + 85)) {
			client.outBuffer.putOpcode(185);
			client.outBuffer.putShort(110);
			return true;
		}
		for(int i = 0; i < 5; i++) {
			int off = i * 30;
			if(!Objects.equals(Interface.cache[254 + i].text, "empty")) {
				if(client.leftClickInRegion(beginX + 422, beginY + 177 + off, beginX + 476, beginY + 192 + off)) {
					client.outBuffer.putOpcode(185);
					client.outBuffer.putShort(113 + i);
					return true;
				}
			}
		}

        /* Exit */
		return processClose(beginX, beginY);
		
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
		
		fancyFont.drawLeftAlignedEffectString("Slayer - Points: " + Interface.cache[252].text, beginX + 20, beginY + 31, 0xFF8A1F, true);
		
		fancyFont.drawCenteredEffectString("Current assignment: " + Interface.cache[253].text, beginX + 250, beginY + 55, 0xFFFFFF, true);
		
		smallFont.drawLeftAlignedString("You may spend points to cancel or block your current task.", beginX + 15, beginY + 105, 0xffffff);
		smallFont.drawLeftAlignedString("Skip: you may be assigned that target again in the future. (30p)", beginX + 15, beginY + 120, 0xffffff);
		smallFont.drawLeftAlignedString("Block: you will never get the assignment again. (100p)", beginX + 15, beginY + 135, 0xffffff);
		//smallFont.drawLeftAlignedString("Neither option will reset your current tally of completed Slayer tasks.", beginX + 15, beginY + 145, 0xffffff);
		
		Rasterizer2D.drawHorizontalLine(beginX + 250, beginY + 75, 12, 0xFFFFFF);
		Rasterizer2D.fillRoundedRectangle(beginX + 280, beginY + 65, 62, 20, 2, 0xf3763f, 60);
		fancyFont.drawCenteredString("Reward", beginX + 310, beginY + 80, 0xFFFFFF);
		if(client.mouseInRegion(beginX + 280, beginY + 65, beginX + 342, beginY + 85)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 280, beginY + 65, 62, 20, 2, 0xf3763f, 20);
			Rasterizer2D.drawRectangle(beginX + 347, beginY + 60, 145, 70, 0x000000, 60);
			Rasterizer2D.fillRectangle(beginX + 347, beginY + 60, 145, 70, 0x000000, 40);
			boldFont.drawCenteredString("Rewards:", beginX + 420, beginY + 80, 0xffffff);
			for(int i = 0; i < 4; i++) {
				int item = Interface.cache[260].invIcon[i];
				int amt = Interface.cache[260].invAmt[i];
				if(item != 0) {
					BitmapImage img = ObjectType.getIcon(item, amt, 0);
					if(img != null) {
						img.drawImage(beginX + 353 + (i * 35), beginY + 98);
						smallFont.drawLeftAlignedEffectString(Client.valueToKOrM(amt), beginX + 353 + (i * 35), beginY + 104, 0xffffff, true);
					}
				}
			}
		}
		Rasterizer2D.fillRoundedRectangle(beginX + 180, beginY + 65, 54, 20, 2, 0xFF8A1F, 60);
		fancyFont.drawCenteredString("Block", beginX + 205, beginY + 80, 0xFFFFFF);
		if(client.mouseInRegion(beginX + 180, beginY + 65, beginX + 234, beginY + 85)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 180, beginY + 65, 54, 20, 2, 0xFF8A1F, 20);
		}
		Rasterizer2D.fillRoundedRectangle(beginX + 120, beginY + 65, 54, 20, 2, 0xFF8A1F, 60);
		fancyFont.drawCenteredString("Skip", beginX + 145, beginY + 80, 0xFFFFFF);
		if(client.mouseInRegion(beginX + 120, beginY + 65, beginX + 174, beginY + 85)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 120, beginY + 65, 54, 20, 2, 0xFF8A1F, 20);
		}
		Rasterizer2D.fillRoundedRectangle(beginX + 40, beginY + 65, 74, 20, 2, 0xf3763f, 60);
		fancyFont.drawCenteredString("Teleport", beginX + 75, beginY + 80, 0xFFFFFF);
		if(client.mouseInRegion(beginX + 50, beginY + 65, beginX + 114, beginY + 85)) {
			Rasterizer2D.fillRoundedRectangle(beginX + 40, beginY + 65, 74, 20, 2, 0xf3763f, 20);
		}
		
		Rasterizer2D.drawRectangle(beginX + 15, beginY + 150, 470, 170, 0x000000, 60);
		Rasterizer2D.fillRectangle(beginX + 15, beginY + 150, 470, 170, 0x000000, 40);
		boldFont.drawLeftAlignedEffectString("Blocked tasks:", beginX + 22, beginY + 165, 0xFFFFFF, true);
		for(int i = 0; i < 5; i++) {
			int off = i * 30;
			smallFont.drawLeftAlignedString("Slot " + (i + 1), beginX + 22, beginY + 189 + off, 0xFFFFFF);
			
			smallFont.drawCenteredString(Interface.cache[254 + i].text, beginX + 252, beginY + 189 + off, 0xFFFFFF);
			
			if(!Objects.equals(Interface.cache[254 + i].text, "empty")) {
				Rasterizer2D.fillRoundedRectangle(beginX + 422, beginY + 177 + off, 54, 15, 2, 0xFF8A1F, 60);
				if(client.mouseInRegion(beginX + 422, beginY + 177 + off, beginX + 476, beginY + 192 + off)) {
					Rasterizer2D.fillRoundedRectangle(beginX + 422, beginY + 177 + off, 54, 15, 2, 0xFF8A1F, 20);
				}
				smallFont.drawCenteredString("unblock", beginX + 447, beginY + 189 + off, 0xFFFFFF);
			}
			Rasterizer2D.drawHorizontalLine(beginX + 15, beginY + 170 + off, 470, 0x000000);
		}
	}
	
	@Override
	public void initialize() {
		
	}
	
	@Override
	public void reset() {
	}
	
	@Override
	public int getId() {
		return 11;
	}
	
	@Override
	public boolean blockedMove() {
		return false;
	}
	
}
