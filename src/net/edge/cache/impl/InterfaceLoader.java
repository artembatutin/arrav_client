package net.edge.cache.impl;

import net.edge.cache.CacheLoader;
import net.edge.Client;
import net.edge.cache.CacheArchive;
import net.edge.cache.unit.Interface;
import net.edge.media.font.BitmapFont;

public class InterfaceLoader implements CacheLoader {
	
	private final CacheArchive archive;
	
	public InterfaceLoader(CacheArchive archive) {
		this.archive = archive;
	}
	
	@Override
	public String message() {
		return "Loading interfaces.";
	}
	
	public void run(Client client) {
		final BitmapFont fonts[] = {client.smallFont, client.plainFont, client.boldFont, client.fancyFont};
		// Unpacking Interfaces
		Interface.unpack(archive, fonts);
	}

}
