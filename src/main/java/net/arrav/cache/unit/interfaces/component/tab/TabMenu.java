package net.arrav.cache.unit.interfaces.component.tab;


import net.arrav.Client;
import net.arrav.cache.unit.interfaces.Interface;
import net.arrav.graphic.font.BitmapFont;
import net.arrav.graphic.img.BitmapImage;

public class TabMenu {
	private final int tabAmount;
	private final String[] tabNames;
	private final Tab tabType;
	private int selected;
	private BitmapImage spriteBack;
	private BitmapImage spriteReg;
	private int height;

	public TabMenu(Tab tabType, int spriteBack,  String... tabNames) {
		this.tabAmount = tabNames.length;
		this.tabNames = tabNames;
		this.selected = 0;
		this.spriteBack = Client.spriteCache.get(spriteBack);
		this.spriteReg = Client.spriteCache.get(spriteBack+1);
		this.height = this.spriteBack.imageHeight;
		this.tabType = tabType;
	}

	public int getTabAmount() {
		return this.tabAmount;
	}

	public String[] getTabNames() {
		return this.tabNames;
	}


	public void setSelectedTab(int selected) {
		this.selected = selected;
	}

	public Tab getTabType() {
		return this.tabType;
	}

	public void drawTabMenu(Interface child, int x, int y) {
		BitmapFont font = Client.instance.smallFont;

		int offsetX = 0;
		for (int tab = 0; tab < tabAmount; tab++) {
			if(tab == selected) {
				spriteBack.drawImage(x+offsetX, y);

				if(tabType == Tab.STARTER) {

				} else
				font.drawCenteredString(tabNames[tab], x+offsetX+(spriteBack.imageWidth / 2), y+17, child.color, 0x0);
				offsetX +=spriteBack.imageWidth;
				continue;
			}
			spriteReg.drawImage(x+offsetX, y, child.tabHover == tab ? 100 : 255);
			if(tabType == Tab.STARTER) {

			} else
				font.drawCenteredString(tabNames[tab], x+offsetX+(spriteReg.imageWidth / 2), y+17, child.color, 0x0);
			offsetX += spriteReg.imageWidth;
		}

	}

	public void hover(Interface parent, Interface child, int hoverX, int hoverY, int xBounds, int yBounds) {
		boolean inY = hoverY >= yBounds && hoverY <= yBounds + height;
		boolean inX = hoverX >= xBounds && hoverX <= xBounds + getWidth();
		if(!inY || !inX) {
			child.tabHover = -1;
			return;
		}
		int selectX = hoverX - xBounds;
		int hovered = selectX / spriteBack.imageWidth;
		if(hovered >= tabNames.length)
			return;
		child.tabHover = hovered;
		if(child.tabHover == selected)
			return;
		Client.instance.menuItemName[Client.instance.menuPos] = "Select " + tabNames[hovered];
		Client.instance.menuItemCode[Client.instance.menuPos] = 772;
		Client.instance.menuItemArg2[Client.instance.menuPos] = child.id;
		Client.instance.menuItemArg3[Client.instance.menuPos] = child.tabHover;
		Client.instance.menuItemArg4[Client.instance.menuPos] = parent.id;
		Client.instance.menuPos++;

	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return spriteBack.imageWidth + (spriteReg.imageWidth * (tabAmount -1));
	}

}
