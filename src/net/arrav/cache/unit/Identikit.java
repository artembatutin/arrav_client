package net.arrav.cache.unit;

import net.arrav.Config;
import net.arrav.cache.CacheArchive;
import net.arrav.util.io.Buffer;
import net.arrav.world.model.Model;

public final class Identikit {
	
	public static final int[] OSRS_MODELS = {230, 63, 210, 49, 214, 52, 217, 55, 223, 59, 215, 53, 235, 67, 206, 46, 203, 45, 28321, 28386, 249, 81, 250, 82, 251, 83, 247, 79, 246, 248, 80, 253, 85, 252, 84, 292, 322, 326, 320, 327, 324, 310, 297, 151, 167, 170, 162, 163, 158, 28342, 176, 16460, 16448, 16457, 16470, 28351, 28406, 28355, 16439, 28353, 28416, 16454, 16442, 28356, 28408, 28354, 28418, 28352, 28415, 28325, 28387, 28324, 28380, 28326, 28384, 28323, 28377, 28320, 28382, 28322, 28379, 28362, 28360, 28361, 28357, 28363, 28359, 32217, 14394, 14390, 14392, 32837, 32846, 32834, 32843, 32832, 32840, 32836, 32844, 32835, 32841, 32831, 32845, 32833, 32839};
	public static int length;
	public static Identikit[] cache;
	
	public int partId;
	private int[] modelIds;
	public boolean widgetDisplayed;
	public boolean bodyOSRS;
	public boolean headOSRS;
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
			cache = new Identikit[length + 65];
		}
		for(int i = 0; i < length; i++) {
			if(cache[i] == null) {
				cache[i] = new Identikit();
			}
			cache[i].read(buffer);
			cache[i].verifyOS();
			//System.out.println("eoc " + i + " - " + cache[i].bodyOSRS + " | " + cache[i].headOSRS);
			if(cache[i].recolorSrc != null)
				cache[i].recolorSrc[0] = (short) 55232;
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
		while(true) {
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
					recolorSrc[i] = (short) buffer.getUShort();
					recolorDst[i] = (short) buffer.getUShort();
				}
			} else if(opcode == 41) {
				int numTextures = buffer.getUByte();
				retextureSrc = new short[numTextures];
				retextureDst = new short[numTextures];
				for(int i = 0; i < numTextures; i++) {
					retextureSrc[i] = (short) buffer.getUShort();
					retextureDst[i] = (short) buffer.getUShort();
				}
			} else if(opcode >= 60 && opcode < 70) {
				headModelIds[opcode - 60] = buffer.getSmartInt();
			} else
				System.out.println("Error unrecognised idk config code: " + opcode);
		}
	}
	
	private void verifyOS() {
		boolean canOS = true;
		if(modelIds != null) {
			for(int m : modelIds) {
				boolean found = false;
				//System.out.println("seek for model " + m);
				for(int o : OSRS_MODELS) {
					if(m == o) {
						//System.out.println("found osrs equiv " + o);
						found = true;
						break;
					}
				}
				if(!found) {
					canOS = false;
					break;
				}
			}
		}
		bodyOSRS = canOS;
		canOS = true;
		if(headModelIds != null) {
			for(int m : headModelIds) {
				boolean found = false;
				for(int o : OSRS_MODELS) {
					if(m == o) {
						found = true;
						break;
					}
				}
				if(!found) {
					canOS = false;
					break;
				}
			}
		}
		headOSRS = canOS;
	}
	
	public boolean bodyModelCached() {
		if(modelIds == null)
			return true;
		boolean cached = true;
		for(int modelId : modelIds) {
			if(!Model.isCached(modelId, bodyOS() ? 7 : 0))
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
			aclass30_sub2_sub4_sub6s[i] = Model.get(modelIds[i], bodyOS() ? 7 : 0);
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
			if(headModelIds[i] != -1 && !Model.isCached(headModelIds[i], headOS() ? 7 : 0))
				cached = false;
		}
		return cached;
	}
	
	public Model getHeadModel() {
		final Model aclass30_sub2_sub4_sub6s[] = new Model[5];
		int j = 0;
		for(int k = 0; k < 5; k++) {
			if(headModelIds[k] != -1) {
				aclass30_sub2_sub4_sub6s[j++] = Model.get(headModelIds[k], headOS() ? 7 : 0);
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
	
	public boolean bodyOS() {
		return bodyOSRS && Config.def.oldModels;
	}
	
	public boolean headOS() {
		return headOSRS && Config.def.oldModels;
	}
}