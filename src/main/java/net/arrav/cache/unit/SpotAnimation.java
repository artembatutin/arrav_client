package net.arrav.cache.unit;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.arrav.world.model.Model;
import net.arrav.util.io.Buffer;
import net.arrav.cache.CacheArchive;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;

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


	private void pack(DataOutputStream dao) throws IOException {
		HashSet<Integer> written = new HashSet<>();

		do {
		if(modelid != -1 && !written.contains(1)) {
			dao.writeByte(1);
			dao.writeShort(modelid);
			written.add(1);
		} else if(seqid != -1 && !written.contains(2)) {
			dao.writeByte(2);
			dao.writeShort(seqid);
			written.add(2);
		} else if(scaleHorizontal != -1 && !written.contains(4)) {
			dao.writeByte(4);
			dao.writeShort(scaleHorizontal);
			written.add(4);
		} else if(scaleVertical != -1 && !written.contains(5)) {
			dao.writeByte(5);
			dao.writeShort(scaleVertical);
			written.add(5);
		}else if(rotation != -1 && !written.contains(6)) {
			dao.writeByte(6);
			dao.writeShort(rotation);
			written.add(6);
		}else if(lightness != -1 && !written.contains(7)) {
			dao.writeByte(7);
			dao.writeByte(lightness);
			written.add(7);
		}else if(contrast != -1 && !written.contains(8)) {
			dao.writeByte(8);
			dao.writeByte(contrast);
			written.add(8);
		}else if(anIntArray408 != null && !written.contains(40)) {
			dao.writeByte(40);
			dao.writeByte(anIntArray408.length);
			for (int i = 0; i < anIntArray408.length; i++) {
				dao.writeShort(anIntArray408[i]);
				dao.writeShort(anIntArray409[i]);
			}
			written.add(40);
		} else {
			dao.writeByte(0);
			break;
		}
		} while (true);
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
				if(DeformSequence.animations != null) {
					animFrameSequence = DeformSequence.animations[seqid];
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
		model = Model.fetchModel(modelid);
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