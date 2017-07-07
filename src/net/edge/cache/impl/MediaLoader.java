package net.edge.cache.impl;

import net.edge.Config;
import net.edge.cache.CacheLoader;
import net.edge.cache.unit.ImageCache;
import net.edge.media.img.PaletteImage;
import net.edge.Client;
import net.edge.activity.ui.UIRenderer;
import net.edge.cache.CacheArchive;
import net.edge.media.Rasterizer3D;
import net.edge.media.img.BitmapImage;

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
		ImageCache.get(69, true);//Requesting before it's too late.
		client.uiRenderer = new UIRenderer(client, Config.def.getGAME_FRAME());
		if(Config.def.getGAME_FRAME() == 1)
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
		boolean loaded = false;
		while(!loaded) {
			if(ImageCache.get(69, true).imageHeight > 3) {
				loaded = true;
			} else {
				continue;
			}
			for(int y = 0; y < 33; y++) {
				int k6 = 999;
				int i7 = 0;
				for(int x = 0; x < 34; x++) {
					if(ImageCache.get(69, true).imageRaster[x + y * ImageCache.get(69, true).imageWidth] == 0) {
						if(k6 == 999) {
							k6 = x;
						}
						continue;
					}
					if(k6 == 999) {
						continue;
					}
					i7 = x;
					break;
				}
				client.compassClipStarts[y] = k6;
				client.compassLineLengths[y] = i7 - k6;
			}
			for(int l6 = 5; l6 < 156; l6++) {
				int j7 = 999;
				int l7 = 0;
				for(int j8 = 25; j8 < 172; j8++) {
					if(ImageCache.get(69, true).imageRaster[j8 + l6 * ImageCache.get(69, true).imageWidth] == 0 && (j8 > 34 || l6 > 34)) {
						if(j7 == 999) {
							j7 = j8;
						}
						continue;
					}
					if(j7 == 999) {
						continue;
					}
					l7 = j8;
					break;
				}
				client.minimapLineStarts[l6 - 5] = j7 - 25;
				client.minimapLineLengths[l6 - 5] = l7 - j7;
			}
		}
		Rasterizer3D.setBrightness(0.80000000000000004F);
	}
}
