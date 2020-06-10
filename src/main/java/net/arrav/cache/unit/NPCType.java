package net.arrav.cache.unit;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.arrav.Config;
import net.arrav.Constants;
import net.arrav.cache.CacheArchive;
import net.arrav.world.model.Model;
import net.arrav.net.SignLink;
import net.arrav.util.io.Buffer;
import net.arrav.Client;
import net.arrav.util.DataToolkit;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public final class NPCType {

	private static final boolean REPACK = false;
	private static NPCType dummy;
	public final static Int2ObjectArrayMap<NPCType> defCache = new Int2ObjectArrayMap<>();
	public static Int2ObjectOpenHashMap<Model> modelcache = new Int2ObjectOpenHashMap<>();
	public static Client client;
	
	private static Buffer data;
	private static int[] index;
	
	public int id;
	public String name;
	public String description;
	public String actions[];
	public int combatLevel;
	public int walkAnimationId;
	public int standAnimationId;
	public int turnRightAnimationId;
	public int turnAroundAnimationId;
	public int turnLeftAnimationId;
	public int degreesToTurn;
	public byte boundaryDimension;
	private int[] modifiedModelColors;
	private int[] originalModelColors;
	public boolean visibleMinimap;
	public boolean clickable;
	public boolean visible;
	public int headIcon;
	private int settingId;
	private int varBitId;
	private int brightness;
	private int contrast;
	private int scaleZ;
	private int scaleXY;
	
	private int[] modelId;
	private int[] modelIdOSRS;
	private int[] dialogueModels;
	public int childrenIDs[];
	private boolean osrs;
	private boolean nonTextured;
	private boolean fixPriority;

	private NPCType() {
		turnLeftAnimationId = -1;
		varBitId = -1;
		turnAroundAnimationId = -1;
		settingId = -1;
		combatLevel = -1;
		walkAnimationId = -1;
		boundaryDimension = 1;
		headIcon = -1;
		standAnimationId = -1;
		id = -1;
		degreesToTurn = 32;
		turnRightAnimationId = -1;
		clickable = true;
		scaleZ = 128;
		visibleMinimap = true;
		scaleXY = 128;
		visible = false;
	}
	
	public static NPCType get(int id) {
		if(defCache.containsKey(id))
			return defCache.get(id);
		NPCType npc = new NPCType();
		if(id >= index.length || id < 0) {
			return dummy;
		}
		data.pos = index[id];
		npc.id = id;
		npc.decode(data);
		npc.osrs();
		
		if(id == 8331) {
			npc.name = "Guardian";
		}
		//pets
		if(id == 3167)
			npc.pet(1615, "Abyssal orphan", 48);
		if(id == 3168)
			npc.pet(2745, "Jadiku", 28);
		if(id == 3169)
			npc.pet(8349, "Toram", 48);
		if(id == 3170)
			npc.pet(3334, "Wyrmy", 48);
		if(id == 3177)
			npc.pet(6222, "Kraa", 28);//armadyl
		if(id == 3178)
			npc.pet(6260, "Grary", 38);//bandos
		if(id == 3179)
			npc.pet(6203, "Tsutsy", 48);//zamorak
		if(id == 3180)
			npc.pet(6247, "Zilzy", 68);//saradomin
		if(Config.def.idx())
			System.out.println(npc.id + " - " + npc.standAnimationId + " - " + npc.walkAnimationId);
		defCache.put(id, npc);
		return npc;
	}
	
	private void pet(int copy, String name, int scale) {
		NPCType copied = get(copy);
		this.modelId = copied.modelId;
		this.modifiedModelColors = copied.modifiedModelColors;
		this.originalModelColors = copied.originalModelColors;
		this.dialogueModels = copied.dialogueModels;
		this.turnAroundAnimationId = copied.turnAroundAnimationId;
		this.turnLeftAnimationId = copied.turnLeftAnimationId;
		this.turnRightAnimationId = copied.turnRightAnimationId;
		this.walkAnimationId = copied.walkAnimationId;
		this.standAnimationId = copied.standAnimationId;
		this.name = name;
		this.scaleXY = scale;
		this.scaleZ = scale;
	}

	public static void reset() {
		modelcache = null;
		index = null;
		data = null;
	}

	public static void unpack(CacheArchive archive) {
		final Buffer buffer;
		if(Constants.USER_HOME_FILE_STORE) {
			data = new Buffer(archive.getFile("npc.dat"));
			buffer = new Buffer(archive.getFile("npc.idx"));
		} else {
			data = new Buffer(DataToolkit.readFile(SignLink.getCacheDir() + "/util/npc/npc.dat"));
			buffer = new Buffer(DataToolkit.readFile(SignLink.getCacheDir() + "/util/npc/npc.idx"));
		}
		final int length = buffer.getUShort();
		System.out.println("[loading] npc size: " + length);
		index = new int[length];
		int pos = 2;
		for(int i = 0; i < length; i++) {
			index[i] = pos;
			pos += buffer.getUShort();
		}
		dummy = get(1);
		//Repacking with fixes.
		if(REPACK) {
			try {
				repackOSRS();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static int size() {
		return index.length;
	}

	private void decode(Buffer stream) {
		do {
			int i = stream.getUByte();
			if(i == 0)
				return;
			if(i == 1) {
				int j = stream.getUByte();
				modelId = new int[j];
				for(int j1 = 0; j1 < j; j1++)
					modelId[j1] = stream.getUShort();
			} else if(i == 2)
				name = stream.getLine();
			else if(i == 3)
				description = stream.getLine();
			else if(i == 4)
				boundaryDimension = (byte) stream.getUByte();
			else if(i == 5)
				standAnimationId = stream.getUShort();
			else if(i == 6)
				walkAnimationId = stream.getUShort();
			else if(i == 7) {
				walkAnimationId = stream.getUShort();
				turnAroundAnimationId = stream.getUShort();
				turnRightAnimationId = stream.getUShort();
				turnLeftAnimationId = stream.getUShort();
				if(walkAnimationId == 65535)
					walkAnimationId = -1;
				if(turnAroundAnimationId == 65535)
					turnAroundAnimationId = -1;
				if(turnRightAnimationId == 65535)
					turnRightAnimationId = -1;
				if(turnLeftAnimationId == 65535)
					turnLeftAnimationId = -1;
			} else if(i >= 20 && i < 30) {
				if(actions == null)
					actions = new String[10];
				actions[i - 20] = stream.getLine();
				if(actions[i - 20].equalsIgnoreCase("hidden"))
					actions[i - 20] = null;
			} else if(i == 8) {
				int k = stream.getUByte();
				originalModelColors = new int[k];
				modifiedModelColors = new int[k];
				for(int k1 = 0; k1 < k; k1++) {
					originalModelColors[k1] = stream.getUShort();
					modifiedModelColors[k1] = stream.getUShort();
				}
			} else if(i == 9) {
				int l = stream.getUByte();
				dialogueModels = new int[l];
				for(int l1 = 0; l1 < l; l1++)
					dialogueModels[l1] = stream.getUShort();
			} else if(i == 10)
				visibleMinimap = false;
			else if(i == 11)
				combatLevel = stream.getUShort();
			else if(i == 12)
				scaleXY = stream.getUShort();
			else if(i == 13)
				scaleZ = stream.getUShort();
			else if(i == 14)
				visible = true;
			else if(i == 15)
				brightness = stream.getUShort();
			else if(i == 16)
				contrast = stream.getUByte() * 5;
			else if(i == 17)
				headIcon = stream.getUShort();
			else if(i == 18)
				degreesToTurn = stream.getUShort();
			else if(i == 19) {
				varBitId = stream.getUShort();
				if(varBitId == 65535)
					varBitId = -1;
				settingId = stream.getUShort();
				if(settingId == 65535)
					settingId = -1;
				int childCount = stream.getUByte();
				childrenIDs = new int[childCount];
				for(int i2 = 0; i2 < childCount; i2++) {
					childrenIDs[i2] = stream.getUShort();
					if(childrenIDs[i2] == 65535)
						childrenIDs[i2] = -1;
				}
			} else if(i == 31) {
				int j = stream.getUByte();
				modelIdOSRS = new int[j];
				for(int j1 = 0; j1 < j; j1++)
					modelIdOSRS[j1] = stream.getUShort();
			} else if(i == 30)
				clickable = false;
			else
				System.out.println("wrong opcode: " + i);
		} while(true);
	}
	
	
	private static void repack() throws IOException {
		final Buffer data634 = new Buffer(DataToolkit.readFile(SignLink.getCacheDir() + "/util/npc/npc634.dat"));
		final Buffer idx634 = new Buffer(DataToolkit.readFile(SignLink.getCacheDir() + "/util/npc/npc634.idx"));
		final int length = idx634.getUShort();
		System.out.println("for 634: " + length);
		NPCType[] npcs = new NPCType[length];
		data634.pos = 2;
		int size = index.length;
		DataOutputStream dat = new DataOutputStream(new FileOutputStream(SignLink.getCacheDir() + "/util/npc/npc2.dat"));
		DataOutputStream idx = new DataOutputStream(new FileOutputStream(SignLink.getCacheDir() + "/util/npc/npc2.idx"));
		idx.writeShort(size);
		dat.writeShort(size);
		for(int i = 0; i < size; i++) {
			NPCType obj;
			obj = get(i);
			if(i < npcs.length) {
				npcs[i] = new NPCType();
				npcs[i].decode(data634);
				//condition and change.
				if(i == 10702) {
					obj = npcs[i];
				}
			}
			int offset1 = dat.size();
			obj.encode(dat);
			int offset2 = dat.size();
			int writeOffset = offset2 - offset1;
			idx.writeShort(writeOffset);
			System.out.println("writted: " + i + " - offset: " + writeOffset);
		}
		dat.close();
		idx.close();
	}
	
	public static void repackOSRS() throws IOException {
		final Buffer osrs_data = new Buffer(DataToolkit.readFile(SignLink.getCacheDir() + "/util/npc/npc_osrs.dat"));
		final Buffer osrs_idx = new Buffer(DataToolkit.readFile(SignLink.getCacheDir() + "/util/npc/npc_osrs.idx"));
		final int length = osrs_idx.getUShort();
		System.out.println("for osrs 154: " + length);
		NPCType[] npcs = new NPCType[length];
		osrs_data.pos = 2;
		int size = index.length;
		DataOutputStream dat = new DataOutputStream(new FileOutputStream(SignLink.getCacheDir() + "/util/npc/npc2.dat"));
		DataOutputStream idx = new DataOutputStream(new FileOutputStream(SignLink.getCacheDir() + "/util/npc/npc2.idx"));
		idx.writeShort(size);
		dat.writeShort(size);
		for(int i = 0; i < size; i++) {
			NPCType obj;
			obj = get(i);
			if(i < npcs.length) {
				NPCType osrs = new NPCType();
				osrs.decodeOSRS(osrs_data);
				if(i == 1265) {
					System.out.println("rock crabs: " + obj.name + " - " + osrs.name);
				}
				if(obj.name != null && osrs.name != null) {
					if(obj.name.equalsIgnoreCase(osrs.name)) {
						System.out.println("set osrs for " + obj.name + " - " + obj.id);
						obj.modelIdOSRS = osrs.modelId;//setting model ids.
					}
				}
			}
			int offset1 = dat.size();
			obj.encode(dat);
			int offset2 = dat.size();
			int writeOffset = offset2 - offset1;
			idx.writeShort(writeOffset);
			//System.out.println("writted: " + i + " - offset: " + writeOffset);
		}
		dat.close();
		idx.close();
	}
	
	public void decodeOSRS(Buffer buffer) {
		while(true) {
			int opcode = buffer.getUByte();
			if(opcode == 0)
				return;
			if(opcode == 1) {
				int count = buffer.getUByte();
				modelId = new int[count];
				for(int j1 = 0; j1 < count; j1++)
					modelId[j1] = buffer.getUShort();
			} else if(opcode == 2) {
				name = buffer.getLine();
			} else if(opcode == 3) {
				description = buffer.getLine();
			} else if(opcode == 12) {
				boundaryDimension = buffer.getSByte();
			} else if(opcode == 13) {
				standAnimationId = buffer.getUShort();
			} else if(opcode == 14) {
				walkAnimationId = buffer.getUShort();
			} else if(opcode == 15) {
				buffer.getUShort();
			} else if(opcode == 16) {
				buffer.getUShort();
			} else if(opcode == 17) {
				walkAnimationId = buffer.getUShort();
				turnAroundAnimationId = buffer.getUShort();
				turnRightAnimationId = buffer.getUShort();
				turnLeftAnimationId = buffer.getUShort();
			} else if(opcode >= 30 && opcode < 40) {
				if(actions == null)
					actions = new String[10];
				actions[opcode - 30] = buffer.getLine();
				if(actions[opcode - 30].equalsIgnoreCase("hidden"))
					actions[opcode - 30] = null;
			} else if(opcode == 40) {
				int colours = buffer.getUByte();
				originalModelColors = new int[colours];
				modifiedModelColors = new int[colours];
				for(int k1 = 0; k1 < colours; k1++) {
					originalModelColors[k1] = buffer.getUShort();
					modifiedModelColors[k1] = buffer.getUShort();
				}
			} else if(opcode == 41) {
				int colours = buffer.getUByte();
				for(int k1 = 0; k1 < colours; k1++) {
					buffer.getUShort();
					buffer.getUShort();
				}
			}else if(opcode == 60) {
				int additionalModelLen = buffer.getUByte();
				dialogueModels = new int[additionalModelLen];
				for(int l1 = 0; l1 < additionalModelLen; l1++)
					dialogueModels[l1] = buffer.getUShort();
			} else if(opcode == 93) {
				visibleMinimap = false;
			} else if(opcode == 95) {
				combatLevel = buffer.getUShort();
			} else if(opcode == 97) {
				scaleXY = buffer.getUShort();
			} else if(opcode == 98) {
				scaleZ = buffer.getUShort();
			} else if(opcode == 99) {
				visible = true;
			} else if(opcode == 100) {
				brightness = buffer.getSByte();
			} else if(opcode == 101) {
				contrast = buffer.getSByte();
			} else if(opcode == 102) {
				headIcon = buffer.getUShort();
			} else if(opcode == 103) {
				degreesToTurn = buffer.getUShort();
			} else if(opcode == 106) {
				varBitId = buffer.getUShort();
				if(varBitId == 65535)
					varBitId = -1;
				settingId = buffer.getUShort();
				if(settingId == 65535)
					settingId = -1;
				int childCount = buffer.getUByte();
				childrenIDs = new int[childCount + 1];
				for(int i2 = 0; i2 <= childCount; i2++) {
					childrenIDs[i2] = buffer.getUShort();
					if(childrenIDs[i2] == 65535)
						childrenIDs[i2] = -1;
				}
			} else if(opcode == 107) {
				clickable = false;
			}
		}
	}

	private void encode(DataOutputStream out) throws IOException {
		boolean actionsd = false;
		Set<Integer> written = new HashSet<>();
		do {
			if(modelId != null && !written.contains(1)) {
				out.writeByte(1);
				out.writeByte(modelId.length);
				if(modelId.length > 0) {
					for(int aModelId : modelId) {
						out.writeShort(aModelId);
					}
				}
				written.add(1);
			} else if(name != null && !written.contains(2)) {
				out.writeByte(2);
				out.write(name.replaceAll("_", " ").getBytes());
				out.writeByte(10);
				written.add(2);
			} else if(description != null && !written.contains(3)) {
				out.writeByte(3);
				out.write(description.getBytes());
				out.writeByte(10);
				written.add(3);
			} else if(boundaryDimension != 1 && !written.contains(4)) {
				out.writeByte(4);
				out.writeByte(boundaryDimension);
				written.add(4);
			} else if(standAnimationId != 1 && !written.contains(5)) {
				out.writeByte(5);
				out.writeShort(standAnimationId);
				written.add(5);
			} else if(walkAnimationId != -1 && !written.contains(6)) {
				out.writeByte(6);
				out.writeShort(walkAnimationId);
				written.add(6);
			} else if((turnAroundAnimationId != -1 || turnLeftAnimationId != -1) && !written.contains(7)) {
				out.writeByte(7);
				if(walkAnimationId == -1)
					walkAnimationId = 65535;
				if(turnAroundAnimationId == -1)
					turnAroundAnimationId = 65535;
				if(turnRightAnimationId == -1)
					turnRightAnimationId = 65535;
				if(turnLeftAnimationId == -1)
					turnLeftAnimationId = 65535;
				out.writeShort(walkAnimationId);
				out.writeShort(turnAroundAnimationId);
				out.writeShort(turnRightAnimationId);
				out.writeShort(turnLeftAnimationId);
				written.add(7);
			} else if(originalModelColors != null && !written.contains(8)) {
				out.writeByte(8);
				out.writeByte(originalModelColors.length);
				for(int i = 0; i < originalModelColors.length; i++) {
					out.writeShort(originalModelColors[i]);
					out.writeShort(modifiedModelColors[i]);
				}
				written.add(8);
			} else if(dialogueModels != null && !written.contains(9)) {
				out.writeByte(9);
				out.writeByte(dialogueModels.length);
				for(int i : dialogueModels) {
					out.writeShort(i);
				}
				written.add(9);
			} else if(!visibleMinimap && !written.contains(10)) {
				out.writeByte(10);
				written.add(10);
			} else if(combatLevel != -1 && !written.contains(11)) {
				out.writeByte(11);
				out.writeShort(combatLevel);
				written.add(11);
			} else if(scaleXY != 128 && !written.contains(12)) {
				out.writeByte(12);
				out.writeShort(scaleXY);
				written.add(12);
			} else if(scaleZ != 128 && !written.contains(13)) {
				out.writeByte(13);
				out.writeShort(scaleZ);
				written.add(13);
			} else if(visible && !written.contains(14)) {
				out.writeByte(14);
				written.add(14);
			} else if(brightness != 0 && !written.contains(15)) {
				out.writeByte(15);
				out.writeShort(brightness);
				written.add(15);
			} else if(contrast != 0 && !written.contains(16)) {
				out.writeByte(16);
				out.writeByte(contrast / 5);
				written.add(16);
			} else if(headIcon != -1 && !written.contains(17)) {
				out.writeByte(17);
				out.writeShort(headIcon);
				written.add(17);
			} else if(degreesToTurn != 32 && !written.contains(18)) {
				out.writeByte(18);
				out.writeShort(degreesToTurn);
				written.add(18);
			} else if(childrenIDs != null && !written.contains(19)) {
				out.writeByte(19);
				if(varBitId == -1) {
					varBitId = 65535;
				}
				out.writeShort(varBitId);
				if(settingId == -1) {
					settingId = 65535;
				}
				out.writeShort(settingId);
				out.writeByte(childrenIDs.length);
				for(int c : childrenIDs) {
					out.writeShort(c);
				}
				written.add(19);
			} else if(modelIdOSRS != null && !written.contains(31)) {
				out.writeByte(31);
				out.writeByte(modelIdOSRS.length);
				if(modelIdOSRS.length > 0) {
					for(int aModelId : modelIdOSRS) {
						out.writeShort(aModelId);
					}
				}
				written.add(31);
			} else if(actions != null && !actionsd) {
				for(int i = 0; i < actions.length; i++) {
					if(actions[i] != null) {
						out.writeByte(20 + i);
						out.write(actions[i].getBytes());
						out.writeByte(10);
					}
				}
				actionsd = true;
			} else if(!clickable && !written.contains(30)) {
				out.writeByte(30);
				written.add(30);
			} else {
				out.writeByte(0);
				break;
			}
		} while(true);
	}

	public Model method160() {
		if(childrenIDs != null) {
			final NPCType sub = getSubNPCType();
			if(sub == null) {
				return null;
			} else {
				return sub.method160();
			}
		}
		if(dialogueModels == null) {
			return null;
		}
		boolean flag1 = false;
		for(int anAdditionalModelCount : dialogueModels) {
			if(!Model.isCached(anAdditionalModelCount, osrs ? 7 : 0)) {
				flag1 = true;
			}
		}
		if(flag1) {
			return null;
		}
		final Model[] parts = new Model[dialogueModels.length];
		for(int j = 0; j < dialogueModels.length; j++) {
			parts[j] = Model.get(dialogueModels[j], osrs ? 7 : 0);
		}

		Model model;
		if(parts.length == 1) {
			model = parts[0];
		} else {
			model = new Model(parts.length, parts);
		}
		if(originalModelColors != null) {
			for(int k = 0; k < originalModelColors.length; k++) {
				model.replaceHsl(originalModelColors[k], modifiedModelColors[k]);
			}
		}
		if(nonTextured)
			model.triTex = null;
		if(fixPriority) {
			if(model.triPri != null) {
				for(int p = 0; p < model.triPri.length; p++)
					model.triPri[p] = 10;
			}
		}
		return model;
	}

	public NPCType getSubNPCType() {
		int child = -1;
		if(varBitId != -1) {
			final VaryingBit varBit = VaryingBit.cache[varBitId];
			final int configId = varBit.configId;
			final int lsb = varBit.leastSignificantBit;
			final int msb = varBit.mostSignificantBit;
			final int bit = Client.BIT_MASK[msb - lsb];
			child = client.variousSettings[configId] >> lsb & bit;
		} else if(settingId != -1) {
			child = client.variousSettings[settingId];
		}
		if(child < 0 || child >= childrenIDs.length || childrenIDs[child] == -1) {
			return null;
		} else {
			return get(childrenIDs[child]);
		}
	}

	private Model getModel(int idleAnim, int currAnim, int ai[]) {
		if(childrenIDs != null) {
			final NPCType type = getSubNPCType();
			if(type == null) {
				return null;
			} else {
				return type.getModel(idleAnim, currAnim, ai);
			}
		}
		Model model = modelcache.get(id);
		if(model == null) {
			boolean flag = false;
			for(int aModelId : modelId) {
				if(!Model.isCached(aModelId, osrs ? 7 : 0)) {
					flag = true;
				}
			}
			if(flag) {
				return null;
			}
			final Model[] parts = new Model[modelId.length];
			if(parts.length == 1) {
				model = parts[0];
			} else {
				model = new Model(parts.length, parts);
			}
			if(originalModelColors != null) {
				for(int k1 = 0; k1 < originalModelColors.length; k1++) {
					model.replaceHsl(originalModelColors[k1], modifiedModelColors[k1]);
				}
			}
			model.applyEffects();
			model.calculateLighting(84/*64*/ + brightness, 1106/*850*/ + contrast, -30, -50, -30, true);
			if(nonTextured)
				model.triTex = null;
			modelcache.put(id, model);
		}
		final Model model_1 = Model.model;
		model_1.method464(model, AnimationFrame.isNull(currAnim) & AnimationFrame.isNull(idleAnim));
		if(currAnim != -1 && idleAnim != -1) {
			model_1.method471(ai, idleAnim, currAnim);
		} else if(currAnim != -1) {
			model_1.applyAnimation(currAnim);
		}
		if(scaleXY != 128 || scaleZ != 128) {
			model_1.scale(scaleXY, scaleZ, scaleXY);
		}
		model_1.computeBoundsDist();
		model_1.triangleSkin = null;
		model_1.anIntArrayArray1657 = null;
		if(boundaryDimension == 1) {
			model_1.hoverable = true;
		}
		if(nonTextured)
			model_1.triTex = null;
		if(fixPriority) {
			if(model.triPri != null) {
				for(int p = 0; p < model.triPri.length; p++)
					model.triPri[p] = 10;
			}
		}
		return model_1;
	}

	public Model getModel(int j, int currAnim, int nextAnim, int currCycle, int nextCycle, int ai[]) {
		if(childrenIDs != null) {
			final NPCType type = getSubNPCType();
			if(type == null) {
				return null;
			} else {
				return type.getModel(j, currAnim, ai);
			}
		}
		Model cachedModel = modelcache.get(id);
		if(cachedModel == null) {
			boolean flag = false;
			if(modelId != null) {
				for(int aModelId : modelId) {
					if(!Model.isCached(aModelId, osrs ? 7 : 0)) {
						flag = true;
					}
				}
			} else {
				return null;
			}
			if(flag) {
				return null;
			}
			final Model[] parts = new Model[modelId.length];
			for(int j1 = 0; j1 < modelId.length; j1++) {
				parts[j1] = Model.get(modelId[j1], osrs ? 7 : 0);
			}
			if(parts.length == 1) {
				cachedModel = parts[0];
			} else {
				cachedModel = new Model(parts.length, parts);
			}
			if(originalModelColors != null) {
				for(int k1 = 0; k1 < originalModelColors.length; k1++) {
					cachedModel.replaceHsl(originalModelColors[k1], modifiedModelColors[k1]);
				}
			}
			cachedModel.applyEffects();
			cachedModel.calculateLighting(84/*64*/ + brightness, 1106/*850*/ + contrast, -30, -50, -30, true);
			if(nonTextured)
				cachedModel.triTex = null;
			modelcache.put(id, cachedModel);
		}
		final Model model = Model.model;
		model.method464(cachedModel, AnimationFrame.isNull(currAnim) & AnimationFrame.isNull(j));
		if(currAnim != -1 && j != -1) {
			model.method471(ai, j, currAnim);
		} else if(currAnim != -1) {
			if(Config.def.tween()) {
				model.applyAnimation(currAnim, nextAnim, nextCycle, currCycle);
			} else {
				model.applyAnimation(currAnim);
			}
		}
		if(scaleXY != 128 || scaleZ != 128) {
			model.scale(scaleXY, scaleZ, scaleXY);
		}
		model.computeBoundsDist();
		model.triangleSkin = null;
		model.anIntArrayArray1657 = null;
		if(boundaryDimension == 1) {
			model.hoverable = true;
		}
		if(nonTextured)
			model.triTex = null;
		if(fixPriority) {
			if(model.triPri != null) {
				for(int p = 0; p < model.triPri.length; p++)
					model.triPri[p] = 10;
			}
		}
		return model;
	}
	
	private void osrs() {
		if(modelId == null && modelIdOSRS != null) {
			modelId = modelIdOSRS;
			osrs = true;
		} else if(Config.def.oldModels && modelIdOSRS != null) {
			modelId = modelIdOSRS;
			osrs = true;
		}
	}
	
}
