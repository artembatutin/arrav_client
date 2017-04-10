package net.edge.cache.unit;

import net.edge.Client;
import net.edge.media.img.BitmapImage;

import java.util.HashMap;

public final class ImageCache {

	private static Client client;

	private final static HashMap<Integer, BitmapImage> imageCache = new HashMap<>();

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

	public static void setImage(BitmapImage image, int id) {
		imageCache.put(id, image);
	}

	public static void setHeight(int id, int height) {
		if(imageCache.containsKey(id))
			imageCache.get(id).imageHeight = height;
	}

	public static void setWidth(int id, int width) {
		if(imageCache.containsKey(id))
			imageCache.get(id).imageWidth = width;
	}

	public static void decreaseHeight(int id, int value) {
		if(imageCache.containsKey(id))
			imageCache.get(id).imageHeight -= value;
	}

	public static void decreaseWidth(int id, int value) {
		if(imageCache.containsKey(id))
			imageCache.get(id).imageWidth -= value;
	}

	public static void increaseHeight(int id, int value) {
		if(imageCache.containsKey(id))
			imageCache.get(id).imageHeight += value;
	}

	public static void increaseWidth(int id, int value) {
		if(imageCache.containsKey(id))
			imageCache.get(id).imageWidth += value;
	}

	public static void reset() {
		imageCache.clear();
	}

}