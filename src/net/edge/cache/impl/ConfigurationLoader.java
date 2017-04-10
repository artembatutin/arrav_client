package net.edge.cache.impl;

import net.edge.Client;
import net.edge.cache.CacheArchive;
import net.edge.cache.CacheLoader;
import net.edge.cache.unit.*;

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
		LocationType.unpack(archive);
		OverlayFloorType.unpack(archive);
		UnderlayFloorType.unpack(archive);
		ObjectType.unpack(archive);
		NPCType.unpack(archive);
		Identikit.unpack(archive);
		SpotAnimation.unpack(archive);
		VariancePopulation.unpack(archive);
		VaryingBit.unpack(archive);
		MaterialType.unpackConfig(archive);
	}
}
