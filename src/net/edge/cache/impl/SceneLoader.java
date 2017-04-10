package net.edge.cache.impl;

import net.edge.Client;
import net.edge.cache.CacheLoader;
import net.edge.cache.unit.AnimationFrame;
import net.edge.cache.unit.LocationType;
import net.edge.cache.unit.NPCType;
import net.edge.game.CollisionMap;
import net.edge.game.Scene;
import net.edge.game.model.Location;
import net.edge.media.Rasterizer3D;
import net.edge.media.Viewport;
import net.edge.media.tex.Texture;

public class SceneLoader implements CacheLoader {
	
	@Override
	public String message() {
		return "Preparing game components.";
	}
	
	public void run(Client client) {
		Texture.reset();
		client.tiles = new byte[4][104][104];
		client.sceneGroundZ = new int[4][105][105];
		client.scene = new Scene(client.sceneGroundZ);
		for(int j = 0; j < 4; j++) {
			client.collisionMaps[j] = new CollisionMap();
		}
		final int i5 = (int) (Math.random() * 21D) - 10;
		final int j5 = (int) (Math.random() * 21D) - 10;
		final int k5 = (int) (Math.random() * 21D) - 10;
		final int l5 = (int) (Math.random() * 41D) - 20;
		for(int i6 = 0; i6 < 100; i6++) {
			if(client.mapFunctions[i6] != null) {
				client.mapFunctions[i6].method344(i5 + l5, j5 + l5, k5 + l5);
			}
			if(client.mapScenes[i6] != null) {
				client.mapScenes[i6].shiftColors(i5 + l5, j5 + l5, k5 + l5);
			}
		}
		client.fixedFullScreenViewport = new Viewport(0, 0, 765, 503, 765);
		client.chatAreaViewport = new Viewport(0, 0, 519, 165, 519);
		client.tabAreaViewport = new Viewport(0, 0, 246, 335, 246);
		client.gameAreaViewport = new Viewport(0, 0, 519, 338, 519);
		/*if(Constants.WEB) {
			int width = client.getWidth();
			int height = client.getHeight();
			client.windowWidth = width;
			client.windowHeight = height;
		} else {
			final Dimension frameSize = client.frame.getContentSize();
			client.windowWidth = frameSize.width - 1;
			client.windowHeight = frameSize.height - 1;
		}*/
		final int[] ai = new int[9];
		for(int i8 = 0; i8 < 9; i8++) {
			final int k8 = 128 + i8 * 32 + 15;
			final int l8 = client.cameraZoom + k8 * 3;
			final int i9 = Rasterizer3D.angleSine[k8];
			ai[i8] = l8 * i9 >> 16;
		}
		Scene.setViewport(500, 800, 512, 334, ai);
		client.startThread(client.mouseDetection, 10);
		Location.client = client;
		LocationType.client = client;
		NPCType.client = client;
		AnimationFrame.client = client;
	}

}
