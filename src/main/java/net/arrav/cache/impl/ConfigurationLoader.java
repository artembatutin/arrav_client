package net.arrav.cache.impl;

import net.arrav.Client;
import net.arrav.cache.CacheArchive;
import net.arrav.cache.CacheLoader;
import net.arrav.cache.CacheUnpacker;
import net.arrav.cache.unit.*;

public class ConfigurationLoader implements CacheLoader {
	
	private final CacheArchive archive;
	
	public ConfigurationLoader(CacheArchive archive) {
		this.archive = archive;
	}

	@Override
	public String message() {
		return "Loading game configurations.";
	}
	
	@Override
	public void run(Client client) {
		DeformSequence.unpack(archive);
		CacheUnpacker.progress = 20;
		LocationType.unpack(archive);
		CacheUnpacker.progress = 25;
		OverlayFloorType.unpack(archive);
		CacheUnpacker.progress = 30;
		UnderlayFloorType.unpack(archive);
		CacheUnpacker.progress = 35;
		ObjectType.unpack(archive);
		CacheUnpacker.progress = 40;
		NPCType.unpack(archive);
		CacheUnpacker.progress = 45;
		Identikit.unpack(archive);
		CacheUnpacker.progress = 50;
		SpotAnimation.unpack(archive);
		CacheUnpacker.progress = 55;
		VariancePopulation.unpack(archive);
		CacheUnpacker.progress = 60;
		VaryingBit.unpack(archive);
		CacheUnpacker.progress = 65;
		MaterialType.unpackConfig(archive);
		CacheUnpacker.progress = 70;
	}
}
