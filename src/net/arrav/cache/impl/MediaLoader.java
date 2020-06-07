package net.arrav.cache.impl;

import net.arrav.Config;
import net.arrav.cache.CacheLoader;
import net.arrav.cache.CacheUnpacker;
import net.arrav.graphic.img.PaletteImage;
import net.arrav.Client;
import net.arrav.activity.ui.UIRenderer;
import net.arrav.cache.CacheArchive;
import net.arrav.graphic.Rasterizer3D;
import net.arrav.graphic.img.BitmapImage;

public class MediaLoader implements CacheLoader {
	
	private final CacheArchive archive;
	
	public MediaLoader(CacheArchive archive) {
		this.archive = archive;
	}
	
	@Override
	public String message() {
		return "Loading media files.";
	}
	
	public void run(Client client) {
		client.mapback = Client.spriteCache.get(69);
		client.initMapBack(client.mapback);
		client.uiRenderer = new UIRenderer(client, Config.def.gameframe());
		CacheUnpacker.progress = 12;
		if(Config.def.gameframe() == 1)
			client.setMode(1);
		for(int k4 = 0; k4 < 8; k4++) {
			client.clickCross[k4] = new BitmapImage(archive, "cross", k4);
		}
		for(int c1 = 0; c1 <= 3; c1++) {
			client.chatButtons[c1] = new BitmapImage(archive, "chatbuttons", c1);
		}
		for(int j3 = 0; j3 <= 14; j3++) {
			client.sideIcons[j3] = new BitmapImage(archive, "sideicons", j3);
		}
		for(int k3 = 0; k3 < 92; k3++) {
			client.mapScenes[k3] = new PaletteImage(archive, "mapscene", k3);
		}
		for(int l3 = 0; l3 < 94; l3++) {
			client.mapFunctions[l3] = new BitmapImage(archive, "mapfunction", l3);
		}
		for(int i4 = 0; i4 < 5; i4++) {
			client.hitMarks[i4] = new BitmapImage(archive, "hitmarks", i4);
		}
		CacheUnpacker.progress = 13;
		client.mapFlag = new BitmapImage(archive, "mapmarker", 0);
		client.mapArrow = new BitmapImage(archive, "mapmarker", 1);
		client.mapDotItem = new BitmapImage(archive, "mapdots", 0);
		client.mapDotNPC = new BitmapImage(archive, "mapdots", 1);
		client.mapDotPlayer = new BitmapImage(archive, "mapdots", 2);
		client.mapDotIronman = new BitmapImage(archive, "mapdots", 5);
		client.mapDotFriend = new BitmapImage(archive, "mapdots", 3);
		client.mapDotTeam = new BitmapImage(archive, "mapdots", 4);
		client.scrollBarTop = new BitmapImage(archive, "scrollbar", 0);
		client.scrollBarDown = new BitmapImage(archive, "scrollbar", 1);
		client.minimapImage = new BitmapImage(512, 512);
		Rasterizer3D.setBrightness(0.80000000000000004F);
		CacheUnpacker.progress = 14;

	}
}
