package net.arrav.cache.unit;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.arrav.game.model.Model;
import net.arrav.util.io.Buffer;
import net.arrav.cache.CacheArchive;

public final class SpotAnimation {

	public static SpotAnimation[] cache;
	private int id;
	private int modelid;
	private int seqid;
	public DeformSequence animFrameSequence;
	private int[] anIntArray408;
	private int[] anIntArray409;
	public int scaleHorizontal;
	public int scaleVertical;
	public int rotation;
	public int lightness;
	public int contrast;
	public static Int2ObjectOpenHashMap<Model> modelcache = new Int2ObjectOpenHashMap<>();

	private SpotAnimation() {
		seqid = -1;
		anIntArray408 = new int[6];
		anIntArray409 = new int[6];
		scaleHorizontal = 128;
		scaleVertical = 128;
	}

	public static void unpack(CacheArchive archive) {
		final Buffer buffer = new Buffer(archive.getFile("spotanim.dat"));
		final int length = buffer.getUShort();
		System.out.println("[loading] spotanim size: " + length);
		if(cache == null) {
			cache = new SpotAnimation[length];
		}
		for(int id = 0; id < length; id++) {
			if(cache[id] == null) {
				cache[id] = new SpotAnimation();
			}
			cache[id].id = id;
			cache[id].read(buffer);
		}
	}

	private void read(Buffer buffer) {
		do {
			int code = buffer.getUByte();
			if(code == 0) {
				return;
			}
			if(code == 1) {
				modelid = buffer.getUShort();
			} else if(code == 2) {
				seqid = buffer.getUShort();
				if(DeformSequence.cache != null) {
					animFrameSequence = DeformSequence.cache[seqid];
				}
			} else if(code == 4) {
				scaleHorizontal = buffer.getUShort();
			} else if(code == 5) {
				scaleVertical = buffer.getUShort();
			} else if(code == 6) {
				rotation = buffer.getUShort();
			} else if(code == 7) {
				lightness = buffer.getUByte();
			} else if(code == 8) {
				contrast = buffer.getUByte();
			} else if(code == 40) {
				int j = buffer.getUByte();
				for(int k = 0; k < j; k++) {
					anIntArray408[k] = buffer.getUShort();
					anIntArray409[k] = buffer.getUShort();
				}
			} else {
				System.out.println("Error unrecognised spotanim config code: " + code);
			}
		} while(true);
	}

	public Model getModel() {
		Model model = modelcache.get(id);
		if(model != null) {
			return model;
		}
		model = Model.get(modelid);
		if(model == null) {
			return null;
		}
		for(int i = 0; i < 6; i++) {
			if(anIntArray408[0] != 0) {
				model.replaceHsl(anIntArray408[i], anIntArray409[i]);
			}
		}
		modelcache.put(id, model);
		return model;
	}
}