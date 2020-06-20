package net.arrav.cache.impl;

import net.arrav.cache.CacheLoader;
import net.arrav.Client;
import net.arrav.cache.CacheArchive;
import net.arrav.cache.unit.interfaces.Interface;
import net.arrav.graphic.font.BitmapFont;

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
