package net.arrav.cache.unit;

import net.arrav.Config;
import net.arrav.Constants;
import net.arrav.cache.CacheArchive;
import net.arrav.net.SignLink;
import net.arrav.util.DataToolkit;
import net.arrav.util.io.Buffer;
import net.arrav.world.model.Model;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public final class Identikit {
	
	public static int length;
	public static Identikit[] cache;
	public static Identikit[] cacheOSRS;
	
	public int partId;
	private int[] modelIds;
	public boolean widgetDisplayed;
	private int[] recolorSrc;
	private int[] recolorDst;
	private int[] recolorSrcOSRS;
	private int[] recolorDstOSRS;
	private short[] retextureSrc;
	private short[] retextureDst;
	private short[] retextureSrcOSRS;
	private short[] retextureDstOSRS;
	private int[] headModelIds = {-1, -1, -1, -1, -1};
	private int[] headModelIdsOSRS = {-1, -1, -1, -1, -1};
	
	private boolean canOsrs;
	
	private Identikit() {
		partId = -1;
		widgetDisplayed = false;
	}
	
	public static void unpack(CacheArchive archive) {
		final Buffer buffer;
		if(Constants.USER_HOME_FILE_STORE) {
			buffer = new Buffer(archive.getFile("idk.dat"));
		} else {
			buffer = new Buffer(DataToolkit.readFile(SignLink.getCacheDir() + "/util/idk/idk2.dat"));
		}
		length = buffer.getUShort();
		System.out.println("[loading] idk size: " + length);
		if(cache == null) {
			cache = new Identikit[length + 65];
		}
		for(int i = 0; i < length; i++) {
			if(cache[i] == null) {
				cache[i] = new Identikit();
			}
			cache[i].read(buffer, false);
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
			
			/*System.out.print("[id]:"+i+"[part]:"+cache[i].partId);
			System.out.print("[models]:");
			for(int k = 0; k < cache[i].modelIds.length; k++) {
				System.out.print("(" + k + "," + cache[i].modelIds[k] + ")");
			}
			System.out.print("[head]:");
			System.out.print("("+cache[i].headModelIds[0] + ",");
			System.out.print(cache[i].headModelIds[1] + ",");
			System.out.print(cache[i].headModelIds[2] + ",");
			System.out.print(cache[i].headModelIds[3] + ",");
			System.out.print(cache[i].headModelIds[4] + ")");
			if(cache[i].widgetDisplayed)
				System.out.print("-true");
			System.out.println();*/
		}
		
		/*unpackOSRS();
		
		for(Identikit okit : cacheOSRS) {
			if(okit == null)
				continue;
			
			for(Identikit kit : cache) {
				if(kit == null)
					continue;
				
				boolean same = true;
				
				for(int omodel : okit.modelIds) {
					boolean modelFound = false;
					for(int model : kit.modelIds) {
						if(omodel == model) {
							modelFound = true;
							break;
						}
					}
					
					if(!modelFound) {
						same = false;
						break;
					}
				}
				
				if(same) {
					kit.recolorDstOSRS = okit.recolorDst;
					kit.recolorSrcOSRS = okit.recolorSrc;
					kit.retextureDstOSRS = okit.retextureDst;
					kit.retextureSrcOSRS = okit.retextureSrc;
					kit.headModelIdsOSRS = okit.headModelIds;
					kit.canOsrs = true;
					System.out.println("same found");
				}
			}
		}
		
		try {
			Identikit.dump();
		} catch(IOException e) {
			e.printStackTrace();
		}*/
	}
	
	public static void unpackOSRS() {
		final Buffer buffer = new Buffer(DataToolkit.readFile(SignLink.getCacheDir() + "/util/idk/idk_osrs.dat"));
		length = buffer.getUShort();
		System.out.println("[loading] idk size: " + length);
		if(cacheOSRS == null) {
			cacheOSRS = new Identikit[length + 65];
		}
		for(int i = 0; i < length; i++) {
			if(cacheOSRS[i] == null) {
				cacheOSRS[i] = new Identikit();
			}
			cacheOSRS[i].read(buffer, true);
			if(cacheOSRS[i].recolorSrc != null)
				cacheOSRS[i].recolorSrc[0] = (short) 55232;
			else {
				cacheOSRS[i].recolorSrc = new int[1];
				cacheOSRS[i].recolorSrc[0] = 55232;
				
			}
			if(cacheOSRS[i].recolorDst != null)
				cacheOSRS[i].recolorDst[0] = 6798;
			else {
				cacheOSRS[i].recolorDst = new int[1];
				cacheOSRS[i].recolorDst[0] = 6798;
				
			}
		}
		
		//try {
		//	Identikit.dump();
		//} catch(IOException e) {
		//	e.printStackTrace();
		//}
	}
	
	private void read(Buffer buffer, boolean osrs) {
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
					modelIds[k] = osrs ? buffer.getUShort() : buffer.getInt();
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
			} else if(opcode == 42) {
				int numColors = buffer.getUByte();
				recolorSrcOSRS = new int[numColors];
				recolorDstOSRS = new int[numColors];
				for(int i = 0; i < numColors; i++) {
					recolorSrcOSRS[i] = (short) buffer.getUShort();
					recolorDstOSRS[i] = (short) buffer.getUShort();
				}
			} else if(opcode == 43) {
				int numTextures = buffer.getUByte();
				retextureSrcOSRS = new short[numTextures];
				retextureDstOSRS = new short[numTextures];
				for(int i = 0; i < numTextures; i++) {
					retextureSrcOSRS[i] = (short) buffer.getUShort();
					retextureDstOSRS[i] = (short) buffer.getUShort();
				}
			} else if(opcode >= 60 && opcode < 70) {
				headModelIds[opcode - 60] = osrs ? buffer.getUShort() : buffer.getInt();
			} else if(opcode >= 70 && opcode < 80) {
				headModelIdsOSRS[opcode - 70] = osrs ? buffer.getUShort() : buffer.getInt();
			} else
				System.out.println("Error unrecognised idk config code: " + opcode);
		}
	}
	
	private static void dump() throws IOException {
		int id = 0;
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(SignLink.getCacheDir() + "/util/idk/idk2.dat"));
		dos.writeShort(127);
		for(Identikit def : cache) {
			if(def == null)
				continue;
			if(!def.canOsrs)
				continue;
			
			if (def.partId != -1) {
				dos.writeByte(1);
				dos.writeByte(def.partId);
			}
			if (def.modelIds != null) {
				dos.writeByte(2);
				dos.writeByte(def.modelIds.length);
				for (int modelId : def.modelIds) {
					dos.writeInt(modelId);
				}
			}
			if (def.widgetDisplayed) {
				dos.writeByte(3);
			}
			if (def.recolorSrc != null && def.recolorDst != null) {
				dos.writeByte(40);
				dos.writeByte(def.recolorSrc.length);
				for (int j = 0; j < def.recolorSrc.length; ++j) {
					dos.writeShort(def.recolorSrc[j]);
					dos.writeShort(def.recolorDst[j]);
				}
			}
			if (def.retextureSrc != null && def.retextureDst != null) {
				dos.writeByte(41);
				dos.writeByte(def.retextureSrc.length);
				for (int j = 0; j < def.retextureSrc.length; ++j) {
					dos.writeShort(def.retextureSrc[j]);
					dos.writeShort(def.retextureDst[j]);
				}
			}
			if (def.recolorSrcOSRS != null && def.recolorDstOSRS != null) {
				dos.writeByte(42);
				dos.writeByte(def.recolorSrcOSRS.length);
				for (int j = 0; j < def.recolorSrcOSRS.length; ++j) {
					dos.writeShort(def.recolorSrcOSRS[j]);
					dos.writeShort(def.recolorDstOSRS[j]);
				}
			}
			if (def.retextureSrcOSRS != null && def.retextureDstOSRS != null) {
				dos.writeByte(43);
				dos.writeByte(def.retextureSrcOSRS.length);
				for (int j = 0; j < def.retextureSrcOSRS.length; ++j) {
					dos.writeShort(def.retextureSrcOSRS[j]);
					dos.writeShort(def.retextureDstOSRS[j]);
				}
			}
			if (def.headModelIds != null) {
				for (int j = 0; j < def.headModelIds.length; ++j) {
					if(def.headModelIds[j] != -1) {
						dos.writeByte(60 + j);
						dos.writeInt(def.headModelIds[j]);
					}
				}
			}
			if (def.headModelIdsOSRS != null) {
				for (int j = 0; j < def.headModelIdsOSRS.length; ++j) {
					if(def.headModelIdsOSRS[j] != -1) {
						dos.writeByte(70 + j);
						dos.writeInt(def.headModelIdsOSRS[j]);
					}
				}
			}
			dos.writeByte(0);
			id++;
		}
		System.out.println("packed: " + id);
	}
	
	public boolean bodyModelCached() {
		if(modelIds == null)
			return true;
		boolean cached = true;
		for(int modelId : modelIds) {
			if(!Model.isCached(modelId, Config.def.oldModels ? 7 : 0))
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
			aclass30_sub2_sub4_sub6s[i] = Model.get(modelIds[i], Config.def.oldModels ? 7 : 0);
		}
		
		Model model;
		if(aclass30_sub2_sub4_sub6s.length == 1) {
			model = aclass30_sub2_sub4_sub6s[0];
		} else {
			model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);
		}
		
		if(Config.def.oldModels) {
			if(retextureSrcOSRS != null && model != null) {
				for(int i_53_ = 0; i_53_ < retextureSrcOSRS.length; i_53_++) {
					model.setTexture(retextureSrcOSRS[i_53_], retextureDstOSRS[i_53_]);
				}
			}
			if(recolorSrcOSRS != null) {
				for(int k2 = 0; k2 < recolorSrcOSRS.length; k2++) {
					model.replaceHsl(recolorSrcOSRS[k2], recolorDstOSRS[k2]);
				}
			}
		} else {
			if(retextureSrc != null && model != null) {
				for(int i_53_ = 0; i_53_ < retextureSrc.length; i_53_++) {
					model.setTexture(retextureSrc[i_53_], retextureDst[i_53_]);
				}
			}
			if(recolorSrc != null) {
				for(int k2 = 0; k2 < recolorSrc.length; k2++) {
					model.replaceHsl(recolorSrc[k2], recolorDst[k2]);
				}
			}
		}
		return model;
	}
	
	public boolean headModelCached() {
		boolean cached = true;
		if(Config.def.oldModels) {
			for(int i = 0; i < 5; i++) {
				if(headModelIdsOSRS[i] != -1 && !Model.isCached(headModelIds[i], 7))
					cached = false;
			}
		} else {
			for(int i = 0; i < 5; i++) {
				if(headModelIds[i] != -1 && !Model.isCached(headModelIds[i], 0))
					cached = false;
			}
		}
		return cached;
	}
	
	public Model getHeadModel() {
		final Model aclass30_sub2_sub4_sub6s[] = new Model[5];
		int j = 0;
		if(Config.def.oldModels) {
			for(int k = 0; k < 5; k++) {
				if(headModelIds[k] != -1) {
					aclass30_sub2_sub4_sub6s[j++] = Model.get(headModelIdsOSRS[k], 7);
				}
			}
		} else {
			for(int k = 0; k < 5; k++) {
				if(headModelIds[k] != -1) {
					aclass30_sub2_sub4_sub6s[j++] = Model.get(headModelIds[k], 0);
				}
			}
		}
		
		final Model model = new Model(j, aclass30_sub2_sub4_sub6s);
		if(Config.def.oldModels) {
			if(retextureSrcOSRS != null) {
				for(int i_53_ = 0; i_53_ < retextureSrcOSRS.length; i_53_++) {
					model.setTexture(retextureSrcOSRS[i_53_], retextureDstOSRS[i_53_]);
				}
			}
			if(recolorSrcOSRS != null) {
				for(int k2 = 0; k2 < recolorSrcOSRS.length; k2++) {
					model.replaceHsl(recolorSrcOSRS[k2], recolorDstOSRS[k2]);
				}
			}
		} else {
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
		}
		
		
		return model;
	}
}