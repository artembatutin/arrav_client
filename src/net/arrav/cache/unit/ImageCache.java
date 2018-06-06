package net.arrav.cache.unit;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.arrav.Client;
import net.arrav.graphic.img.BitmapImage;

public final class ImageCache {

	private static Client client;

	private final static Int2ObjectOpenHashMap<BitmapImage> imageCache = new Int2ObjectOpenHashMap<>();

	private final static BitmapImage nulledImage = new BitmapImage(0, 0);

	public static void init(Client client_) {
		client = client_;
	}

	public static BitmapImage get(int id) {
		return get(id, false);
	}

	public static BitmapImage get(int id, boolean urgent) {
		if(!imageCache.containsKey(id)) {
			if(client.onDemandRequester != null) {
				client.onDemandRequester.addRequest(5, id);
				if(urgent) {
					client.processOnDemandQueue();
				}
			}
			return nulledImage;
		}
		return imageCache.get(id);
	}

	public static synchronized void setImage(BitmapImage image, int id) {
		imageCache.put(id, image);
		
		// minimap initialization.
		if(id == 69) {
			for(int y = 0; y < 33; y++) {
				int k6 = 999;
				int i7 = 0;
				for(int x = 0; x < 34; x++) {
					if(image.imageRaster[x + y * image.imageWidth] == 0) {
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
					if(image.imageRaster[j8 + l6 * image.imageWidth] == 0 && (j8 > 34 || l6 > 34)) {
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
	}

	public static synchronized void setHeight(int id, int height) {
		if(imageCache.containsKey(id))
			imageCache.get(id).imageHeight = height;
	}

	public static synchronized void setWidth(int id, int width) {
		if(imageCache.containsKey(id))
			imageCache.get(id).imageWidth = width;
	}

	public static synchronized void decreaseHeight(int id, int value) {
		if(imageCache.containsKey(id))
			imageCache.get(id).imageHeight -= value;
	}

	public static synchronized void decreaseWidth(int id, int value) {
		if(imageCache.containsKey(id))
			imageCache.get(id).imageWidth -= value;
	}

	public static synchronized void increaseHeight(int id, int value) {
		if(imageCache.containsKey(id))
			imageCache.get(id).imageHeight += value;
	}

	public static synchronized void increaseWidth(int id, int value) {
		if(imageCache.containsKey(id))
			imageCache.get(id).imageWidth += value;
	}

	public static synchronized void clear() {
		imageCache.clear();
	}

}