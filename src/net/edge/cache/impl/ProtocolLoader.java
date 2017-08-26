package net.edge.cache.impl;

import net.edge.Client;
import net.edge.Constants;
import net.edge.cache.CacheArchive;
import net.edge.cache.CacheLoader;
import net.edge.game.model.Model;
import net.edge.media.tex.Texture;
import net.edge.od.OnDemandFetcher;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class ProtocolLoader implements CacheLoader {
	
	private final CacheArchive archive;
	
	public ProtocolLoader(CacheArchive archive) {
		this.archive = archive;
	}
	
	@Override
	public String message() {
		return "Preparing net protocols.";
	}
	
	@Override
	public void run(Client client) {
		client.onDemandRequester = new OnDemandFetcher();
		client.onDemandRequester.start(archive, client);
		Model.method459(client.onDemandRequester.getModelCount(), client.onDemandRequester);
		Texture.init(client.onDemandRequester);
		
		//MAC address
		try {
			InetAddress inet = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(inet);
			Client.mac = ByteBuffer.wrap(network.getHardwareAddress()).getInt();
		} catch(UnknownHostException | SocketException e) {
			e.printStackTrace();
		}
		
		//client.onDemandRequester.writeChecksumList(3);
		//for(int i = 0; i < Constants.CACHE_INDEX_COUNT - 1; i++) {
		//	client.onDemandRequester.writeChecksumList(i);
		//	client.onDemandRequester.writeVersionList(i);
		//}
		//client.onDemandRequester.writeChecksumList(3);
		//Music files unpacking.
		/*if(!Constants.DETAIL.LOW_MEM.getToggle()) {
			client.nextSong = 0;
			try {
				client.nextSong = Integer.parseInt(client.getParameter("music"));
			} catch(Exception ignored) { }
			client.songChanging = true;
			client.onDemandRequester.method558(2, client.nextSong);
			while(client.onDemandRequester.getNodeCount() > 0) {
				client.processOnDemandQueue();
				try {
					Thread.sleep(100L);
				} catch(Exception ignored) { }
				if(client.onDemandRequester.anInt1349 > 3) {
					loadError();
					return;
				}
			}
		}
		//Index unpacking.
		int versionCount = client.onDemandRequester.getVersionCount(1);
		for(int index = 0; index < versionCount; index++) {
			client.onDemandRequester.method558(1, index);
		}
		//Index files preload.
		while(client.onDemandRequester.getNodeCount() > 0) {
			client.processOnDemandQueue();
			try {
				Thread.sleep(100L);
			} catch(Exception ignored) { }
			if(client.onDemandRequester.anInt1349 > 3) {
				loadError();
				return;
			}
		}
		//Models.
		versionCount = client.onDemandRequester.getVersionCount(0);
		for(int index = 0; index < versionCount; index++) {
			int modelIndex = client.onDemandRequester.getModelIndex(index);
			if((modelIndex & 1) != 0) {
				client.onDemandRequester.method558(0, index);
			}
		}
		//Maps.
		versionCount = client.onDemandRequester.getNodeCount();
		while(client.onDemandRequester.getNodeCount() > 0) {
			client.processOnDemandQueue();
			try {
				Thread.sleep(100L);
			} catch(Exception ignored) { }
		}
		if(client.cache_idx[0] != null) {
			client.onDemandRequester.method558(3, client.onDemandRequester.getMapId(0, 48, 47));
			client.onDemandRequester.method558(3, client.onDemandRequester.getMapId(1, 48, 47));
			client.onDemandRequester.method558(3, client.onDemandRequester.getMapId(0, 48, 48));
			client.onDemandRequester.method558(3, client.onDemandRequester.getMapId(1, 48, 48));
			client.onDemandRequester.method558(3, client.onDemandRequester.getMapId(0, 48, 49));
			client.onDemandRequester.method558(3, client.onDemandRequester.getMapId(1, 48, 49));
			client.onDemandRequester.method558(3, client.onDemandRequester.getMapId(0, 47, 47));
			client.onDemandRequester.method558(3, client.onDemandRequester.getMapId(1, 47, 47));
			client.onDemandRequester.method558(3, client.onDemandRequester.getMapId(0, 47, 48));
			client.onDemandRequester.method558(3, client.onDemandRequester.getMapId(1, 47, 48));
			client.onDemandRequester.method558(3, client.onDemandRequester.getMapId(0, 148, 48));
			client.onDemandRequester.method558(3, client.onDemandRequester.getMapId(1, 148, 48));
			versionCount = client.onDemandRequester.getNodeCount();
			while(client.onDemandRequester.getNodeCount() > 0) {
				client.processOnDemandQueue();
				try {
					Thread.sleep(100L);
				} catch(Exception ignored) { }
			}
		}
		//ConfigurationLoader.
		versionCount = client.onDemandRequester.getVersionCount(0);
		for(int index = 0; index < versionCount; index++) {
			int modelIndex = client.onDemandRequester.getModelIndex(index);
			byte byte0 = 0;
			if((modelIndex & 8) != 0) {
				byte0 = 10;
			} else if((modelIndex & 0x20) != 0) {
				byte0 = 9;
			} else if((modelIndex & 0x10) != 0) {
				byte0 = 8;
			} else if((modelIndex & 0x40) != 0) {
				byte0 = 7;
			} else if((modelIndex & 0x80) != 0) {
				byte0 = 6;
			} else if((modelIndex & 2) != 0) {
				byte0 = 5;
			} else if((modelIndex & 4) != 0) {
				byte0 = 4;
			}
			if((modelIndex & 1) != 0) {
				byte0 = 3;
			}
			if(byte0 != 0) {
				client.onDemandRequester.method563(byte0, 0, index);
			}
		}
		//Toggles.
		client.onDemandRequester.method554();
		if(!Constants.DETAIL.LOW_MEM.getToggle()) {
			int version = client.onDemandRequester.getVersionCount(2);
			for(int index = 1; index < version; index++) {
				if(client.onDemandRequester.method569(index)) {
					client.onDemandRequester.method563((byte) 1, 2, index);
				}
			}

		}*/
	}

}