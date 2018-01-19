package net.arrav.cache.unit;

import net.arrav.cache.CacheArchive;
import net.arrav.game.model.Model;
import net.arrav.util.io.Buffer;

public final class Identikit {

	public static int length;
	public static Identikit[] cache;

	public int partId;
	private int[] modelIds;
	public boolean widgetDisplayed;
	private int[] recolorSrc;
	private int[] recolorDst;
	private short[] retextureSrc;
	private short[] retextureDst;
	private final int[] headModelIds = {-1, -1, -1, -1, -1};

	private Identikit() {
		partId = -1;
		widgetDisplayed = false;
	}

	public static void unpack(CacheArchive archive) {
		final Buffer buffer = new Buffer(archive.getFile("idk.dat"));
		length = buffer.getUShort();
		System.out.println("[loading] idk size: " + length);
		if(cache == null) {
			cache = new Identikit[length];
		}
		for(int i = 0; i < length; i++) {
			if(cache[i] == null) {
				cache[i] = new Identikit();
			}
			cache[i].read(buffer);

			if(cache[i].recolorSrc != null)
				cache[i].recolorSrc[0] = 55232;
			else {
				cache[i].recolorSrc = new int[1];
				cache[i].recolorSrc[0] = 55232;

			}

			if(cache[i].recolorDst != null)
				cache[i].recolorDst[0] = 6798;
			else {
				cache[i].recolorDst = new int[1];
				cache[i].recolorDst[0] = 6798;

			}

		}
	}

	private void read(Buffer buffer) {
		do {
			final int opcode = buffer.getUByte();
			if(opcode == 0) {
				return;
			}
			if(opcode == 1) {
				partId = buffer.getUByte();
			} else if(opcode == 2) {
				final int modelCount = buffer.getUByte();
				modelIds = new int[modelCount];
				for(int k = 0; k < modelCount; k++) {
					modelIds[k] = buffer.getSmartInt();
				}

			} else if(opcode == 3) {
				widgetDisplayed = true;
			} else if(opcode == 40) {
				int numColors = buffer.getUByte();
				recolorSrc = new int[numColors];
				recolorDst = new int[numColors];
				for(int i = 0; i < numColors; i++) {
					recolorSrc[i] = buffer.getUShort();
					recolorDst[i] = buffer.getUShort();
				}
			} else if(opcode == 41) {
				int numTextures = buffer.getUByte();
				retextureSrc = new short[numTextures];
				retextureDst = new short[numTextures];
				for(int i = 0; i < numTextures; i++) {
					retextureSrc[i] = (short) buffer.getUShort();
					retextureDst[i] = (short) buffer.getUShort();
				}
			} else if(opcode >= 60 && opcode < 70)
				headModelIds[opcode - 60] = buffer.getSmartInt();
			else
				System.out.println("Error unrecognised idk config code: " + opcode);
		} while(true);
	}

	public boolean bodyModelCached() {
		if(modelIds == null)
			return true;
		boolean cached = true;
		for(int modelId : modelIds) {
			if(!Model.isCached(modelId))
				cached = false;
		}
		return cached;
	}

	public Model getBodyModel() {
		if(modelIds == null) {
			return null;
		}
		final Model aclass30_sub2_sub4_sub6s[] = new Model[modelIds.length];
		for(int i = 0; i < modelIds.length; i++) {
			aclass30_sub2_sub4_sub6s[i] = Model.get(modelIds[i]);
		}

		Model model;
		if(aclass30_sub2_sub4_sub6s.length == 1) {
			model = aclass30_sub2_sub4_sub6s[0];
		} else {
			model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);
		}

		if(retextureSrc != null) {
			for(int i_53_ = 0; i_53_ < retextureSrc.length; i_53_++) {
				model.setTexture(retextureSrc[i_53_], retextureDst[i_53_]);
			}
		}

		if(recolorSrc != null) {
			for(int k2 = 0; k2 < recolorSrc.length; k2++) {
				model.replaceHsl(recolorSrc[k2], recolorDst[k2]);
			}
		}
		return model;
	}

	public boolean headModelCached() {
		boolean cached = true;
		for(int i = 0; i < 5; i++) {
			if(headModelIds[i] != -1 && !Model.isCached(headModelIds[i]))
				cached = false;
		}
		return cached;
	}

	public Model getHeadModel() {
		final Model aclass30_sub2_sub4_sub6s[] = new Model[5];
		int j = 0;
		for(int k = 0; k < 5; k++) {
			if(headModelIds[k] != -1) {
				aclass30_sub2_sub4_sub6s[j++] = Model.get(headModelIds[k]);
			}
		}

		final Model model = new Model(j, aclass30_sub2_sub4_sub6s);
		if(retextureSrc != null) {
			for(int i_53_ = 0; i_53_ < retextureSrc.length; i_53_++) {
				model.setTexture(retextureSrc[i_53_], retextureDst[i_53_]);
			}
		}

		if(recolorSrc != null) {
			for(int k2 = 0; k2 < recolorSrc.length; k2++) {
				model.replaceHsl(recolorSrc[k2], recolorDst[k2]);
			}
		}

		return model;
	}
}