package net.arrav.cache.unit;

import com.google.gson.Gson;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.arrav.Config;
import net.arrav.graphic.Viewport;
import net.arrav.cache.CacheArchive;
import net.arrav.net.SignLink;
import net.arrav.util.DataToolkit;
import net.arrav.util.string.JsonSaver;
import net.arrav.world.model.DataType;
import net.arrav.world.model.Model;
import net.arrav.graphic.Rasterizer2D;
import net.arrav.graphic.Rasterizer3D;
import net.arrav.graphic.img.BitmapImage;
import net.arrav.util.io.Buffer;
import net.arrav.world.model.TieredEntity;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public final class ObjectType {
	
	public static final boolean REPACK = false;
	
	public static Buffer data;
	public static int length;
	public static int[] index;
	public static Int2ObjectArrayMap<ObjectType> defCache = new Int2ObjectArrayMap<>();
	public static Int2ObjectOpenHashMap<Model> modelcache = new Int2ObjectOpenHashMap<>();
	public static Int2ObjectOpenHashMap<BitmapImage> iconcache = new Int2ObjectOpenHashMap<>();
	public static ObjectType nulled;
	
	public int id;
	public TieredEntity tier;
	public DataType dataType;
	public int modelId;
	public int iconZoom;
	public int iconYaw;
	public int iconRoll;
	public int iconVerticalOffset;
	public int iconHorizontalOffset;
	
	public int maleEquip;
	public int maleEquipAlt;
	public int femaleEquip;
	public int femaleEquipAlt;
	
	public int groundScaleZ;
	public int groundScaleY;
	public int groundScaleX;
	public short[] retextureDst;
	public short[] retextureSrc;
	public int[] modifiedModelColors;
	public int[] originalModelColors;
	public byte[] recolorDstPalette;
	
	public int value;
	public String name;
	public String description;
	public String[] actions;
	public String[] groundActions;
	public boolean stackable;
	public int noteId;
	public int noteTemplateId;
	public int[] stackableIds;
	public int[] stackAmounts;
	public int maleDialogueHatmodelId;
	public int femaleDialogueHatmodelId;
	public int maleDialoguemodelId;
	public int femaleDialoguemodelId;
	public int tertiaryMaleModel;
	public int tertiaryFemaleModel;
	public int diffusion;
	public int ambience;
	public int team;
	public int spriteCameraYaw;
	
	public int womanEquipOffsetZ;
	public int womanEquipOffsetY;
	public int womanEquipOffsetX;
	public int maleEquipOffsetZ;
	public int maleEquipOffsetY;
	public int maleEquipOffsetX;
	
	public int lendID;
	public int lentItemID;
	public String[] equipActions;
	
	public ObjectType() {
		id = -1;
		dataType = DataType.NEWEST;
	}
	
	public static ObjectType get(int id) {
		if(defCache.containsKey(id))
			return defCache.get(id);
		final ObjectType obj = new ObjectType();
		if(id > index.length)
			return nulled;
		data.pos = index[id];
		obj.id = id;
		obj.renew();
		obj.decode(data, id);
		
		if(obj.noteTemplateId != -1) {
			obj.toNote();
		}
		if(obj.lentItemID != -1) {
			obj.toLend();
		}

	if(obj.id == 14293) {
		obj.tier = TieredEntity.TIER1;
	}
		if(obj.tier != null)
			obj.name = "<col="+obj.tier.getColor()+">"+obj.name+(Config.def.isShowTier() ? " "+ obj.tier.getName()+"</col>" : "</col>");

		defCache.put(id, obj);
		return obj;
	}
	
	public static void unpack(CacheArchive archive) {
		final Buffer buffer;
		/*if(Constants.USER_HOME_FILE_STORE) {
			data = new Buffer(archive.getFile("obj.dat"));
			buffer = new Buffer(archive.getFile("obj.idx"));
		} else {*/
			data = new Buffer(DataToolkit.readFile(SignLink.getCacheDir() + "/util/item/custom_obj.dat"));
			buffer = new Buffer(DataToolkit.readFile(SignLink.getCacheDir() + "/util/item/custom_obj.idx"));
		//}
		
		length = buffer.getUShort();
		System.out.println("[loading] obj size: " + length);
		index = new int[length];
		int pos = 2;
		for(int i = 0; i < length; i++) {
			index[i] = pos;
			pos += buffer.getUShort();
		}
		nulled = get(0);
		//Repacking with fixes.
		if(REPACK) {
			try {
				packValues("obj2");
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void packValues(String name) throws IOException {
		//final Buffer data = new Buffer(DataToolkit.readFile(SignLink.getCacheDir() + "/util/item/obj.dat"));
		//final Buffer index = new Buffer(DataToolkit.readFile(SignLink.getCacheDir() + "/util/item/obj.idx"));

		data.pos = 2;
		DataOutputStream dat = new DataOutputStream(new FileOutputStream(SignLink.getCacheDir() + "/util/item/"+name+".dat"));
		DataOutputStream idx = new DataOutputStream(new FileOutputStream(SignLink.getCacheDir() + "/util/item/"+name+".idx"));
		idx.writeShort(length);
		dat.writeShort(length);

		JsonSaver saver = new JsonSaver();
		Gson gson = new Gson();

		for(int i = 0; i < length; i++) {
			ObjectType obj = new ObjectType();
			try {
				//System.out.println(i);
				int start = dat.size();

				obj = get(i);
				obj.decode(data, i);


				obj.encode(dat);

				int end = dat.size();
				idx.writeShort(end - start);
			} catch(Exception e) {
				e.printStackTrace();
				break;
			}
			saver.current().add(i+"", gson.toJsonTree(obj));
			saver.split();
			double progress = ((double) (i + 1) / length) * 100;
			System.out.println(String.format("%.2f%s", progress, "%"));
		}
		saver.publish("./tacking/item_definitions.json");
		dat.close();
		idx.close();
	}
	
	private void decode(Buffer buffer, int id) {
		do {
			int opcode = buffer.getUByte();
			if(opcode == 0)
				return;
			if(opcode == 1) {
				modelId = buffer.getUShort();
			} else if(opcode == 2) {
				name = buffer.getLine();
			} else if(opcode == 3) {
				dataType = DataType.ofOrdinal(buffer.getUByte());
			} else if(opcode == 4) {
				iconZoom = buffer.getUShort();
			} else if(opcode == 5) {
				iconYaw = buffer.getUShort();
			} else if(opcode == 6) {
				iconRoll = buffer.getUShort();
			} else if(opcode == 7) {
				iconHorizontalOffset = buffer.getUShort();
				if(iconHorizontalOffset > 32767)
					iconHorizontalOffset -= 65536;
			} else if(opcode == 8) {
				iconVerticalOffset = buffer.getUShort();
				if(iconVerticalOffset > 32767)
					iconVerticalOffset -= 65536;
			} else if(opcode == 9) {
				tier = TieredEntity.ofOrdinal(buffer.getUByte());
			} else if(opcode == 11) {
				stackable = true;
			} else if(opcode == 12) {
				value = buffer.getInt();
			} else if(opcode == 23) {
				maleEquip = buffer.getUShort();
			} else if(opcode == 24) {
				maleEquipAlt = buffer.getUShort();
			} else if(opcode == 25) {
				femaleEquip = buffer.getUShort();
			} else if(opcode == 26) {
				femaleEquipAlt = buffer.getUShort();
			} else if(opcode >= 30 && opcode < 35) {
				if(groundActions == null)
					groundActions = new String[10];
				groundActions[opcode - 30] = buffer.getLine();
				if(groundActions[opcode - 30].equalsIgnoreCase("hidden"))
					groundActions[opcode - 30] = null;
			} else if(opcode >= 35 && opcode < 40) {
				if(actions == null)
					actions = new String[10];
				actions[opcode - 35] = buffer.getLine();
				if(actions[opcode - 35].equalsIgnoreCase("hidden"))
					actions[opcode - 35] = null;
			} else if(opcode == 40) {
				int length = buffer.getUByte();
				originalModelColors = new int[length];
				modifiedModelColors = new int[length];
				for(int index = 0; length > index; index++) {
					originalModelColors[index] = buffer.getUShort();
					modifiedModelColors[index] = buffer.getUShort();
				}
			} else if(opcode == 41) {
				int length = buffer.getUByte();
				retextureSrc = new short[length];
				retextureDst = new short[length];
				for(int index = 0; length > index; index++) {
					retextureSrc[index] = (short) buffer.getUShort();
					retextureDst[index] = (short) buffer.getUShort();
				}
			} else if(opcode == 42) {
				int index = buffer.getUByte();
				recolorDstPalette = new byte[index];
				for(int i_35_ = 0; index > i_35_; i_35_++) {
					recolorDstPalette[i_35_] = buffer.getSByte();
				}
			} else if(opcode == 65) {
				//stockmarket = true;
			} else if(opcode == 78) {
				tertiaryMaleModel = buffer.getUShort();
			} else if(opcode == 79) {
				tertiaryFemaleModel = buffer.getUShort();
			} else if(opcode == 90) {
				maleDialoguemodelId = buffer.getUShort();
			} else if(opcode == 91) {
				femaleDialoguemodelId = buffer.getUShort();
			} else if(opcode == 92) {
				maleDialogueHatmodelId = buffer.getUShort();
			} else if(opcode == 93) {
				femaleDialogueHatmodelId = buffer.getUShort();
			} else if(opcode == 95) {
				spriteCameraYaw = buffer.getUShort();
			} else if(opcode == 97) {
				noteId = buffer.getUShort();
			} else if(opcode == 98) {
				noteTemplateId = buffer.getUShort();
			} else if(opcode >= 100 && opcode < 110) {
				if(stackableIds == null) {
					stackableIds = new int[10];
					stackAmounts = new int[10];
				}
				stackableIds[opcode - 100] = buffer.getUShort();
				stackAmounts[opcode - 100] = buffer.getUShort();
			} else if(opcode == 110) {
				groundScaleX = buffer.getUShort();
			} else if(opcode == 111) {
				groundScaleY = buffer.getUShort();
			} else if(opcode == 112) {
				groundScaleZ = buffer.getUShort();
			} else if(opcode == 113) {
				ambience = buffer.getSByte();
			} else if(opcode == 114) {
				diffusion = buffer.getSByte() * 5;
			} else if(opcode == 115) {
				team = buffer.getUByte();
			} else if(opcode == 121) {
				lendID = buffer.getUShort();
			} else if(opcode == 122) {
				lentItemID = buffer.getUShort();
			} else if(opcode == 125) {
				maleEquipOffsetX = buffer.getSByte();
				maleEquipOffsetY = buffer.getSByte();
				maleEquipOffsetZ = buffer.getSByte();
			} else if(opcode == 126) {
				womanEquipOffsetX = buffer.getSByte();
				womanEquipOffsetY = buffer.getSByte();
				womanEquipOffsetZ = buffer.getSByte();
			} else {
				System.out.println("[ObjectType] Unknown opcode: " + opcode);
				break;
			}
		} while(true);
	}
	
	public void decodeOSRS(Buffer buffer) {
		while(true) {
			int opcode = buffer.getUByte();
			if(opcode == 0)
				return;
			if(opcode == 1) {
				modelId = buffer.getUShort();
			} else if(opcode == 2) {
				name = buffer.getLine();
			} else if(opcode == 3) {
				description = buffer.getLine();
			} else if(opcode == 4) {
				iconZoom = buffer.getUShort();
			} else if(opcode == 5) {
				iconYaw = buffer.getUShort();
			} else if(opcode == 6) {
				iconRoll = buffer.getUShort();
			} else if(opcode == 7) {
				iconHorizontalOffset = buffer.getUShort();
				if(iconHorizontalOffset > 32767)
					iconHorizontalOffset -= 0x10000;
			} else if(opcode == 8) {
				iconVerticalOffset = buffer.getUShort();
				if(iconVerticalOffset > 32767)
					iconVerticalOffset -= 0x10000;
			} else if(opcode == 11) {
				stackable = true;
			} else if(opcode == 12) {
				value = buffer.getInt();
			} else if(opcode == 16) {
			} else if(opcode == 23) {
				maleEquip = buffer.getUShort();
				maleEquipOffsetY = buffer.getSByte();
			} else if(opcode == 24) {
				maleEquipAlt = buffer.getUShort();
			} else if(opcode == 25) {
				femaleEquip = buffer.getUShort();
				womanEquipOffsetY = buffer.getSByte();
			} else if(opcode == 26) {
				femaleEquipAlt = buffer.getUShort();
			} else if(opcode >= 30 && opcode < 35) {
				if(groundActions == null)
					groundActions = new String[5];
				groundActions[opcode - 30] = buffer.getLine();
				if(groundActions[opcode - 30].equalsIgnoreCase("hidden"))
					groundActions[opcode - 30] = null;
			} else if(opcode >= 35 && opcode < 40) {
				if(actions == null)
					actions = new String[5];
				actions[opcode - 35] = buffer.getLine();
			} else if(opcode == 40) {
				int colours = buffer.getUByte();
				originalModelColors = new int[colours];
				modifiedModelColors = new int[colours];
				for(int i = 0; i < colours; i++) {
					originalModelColors[i] = buffer.getUShort();
					modifiedModelColors[i] = buffer.getUShort();
				}
			} else if(opcode == 41) {
				int length = buffer.getUByte();
				retextureSrc = new short[length];
				retextureDst = new short[length];
				for(int idx = 0; idx < length; ++idx) {
					retextureSrc[idx] = (short) (buffer.getSShort() & 0xFFFF);
					retextureDst[idx] = (short) (buffer.getSShort() & 0xFFFF);
				}
			} else if(opcode == 42) {
				int anInt2173 = buffer.getUByte();
			} else if(opcode == 65) {
				//stockMarket = true;
			} else if(opcode == 78) {
				tertiaryMaleModel = buffer.getUShort();
			} else if(opcode == 79) {
				tertiaryFemaleModel = buffer.getUShort();
			} else if(opcode == 90) {
				maleDialoguemodelId = buffer.getUShort();
			} else if(opcode == 91) {
				femaleDialoguemodelId = buffer.getUShort();
			} else if(opcode == 92) {
				maleDialogueHatmodelId = buffer.getUShort();
			} else if(opcode == 93) {
				femaleDialogueHatmodelId = buffer.getUShort();
			} else if(opcode == 95) {
				spriteCameraYaw = buffer.getUShort();
			} else if(opcode == 97) {
				noteId = buffer.getUShort();
			} else if(opcode == 98) {
				noteTemplateId = buffer.getUShort();
			} else if(opcode >= 100 && opcode < 110) {
				if(stackableIds == null) {
					stackableIds = new int[10];
					stackAmounts = new int[10];
					//stackRevisions = new Revision[10];
				}
				stackableIds[opcode - 100] = buffer.getUShort();
				stackAmounts[opcode - 100] = buffer.getUShort();
				//stackRevisions[opcode - 100] = Revision.OSRS;
			} else if(opcode == 110) {
				groundScaleX = buffer.getUShort();
			} else if(opcode == 111) {
				groundScaleY = buffer.getUShort();
			} else if(opcode == 112) {
				groundScaleZ = buffer.getUShort();
			} else if(opcode == 113) {
				ambience = buffer.getSByte();
			} else if(opcode == 114) {
				diffusion = buffer.getSByte() * 5;
			} else if(opcode == 115) {
				team = buffer.getUByte();
			} else if(opcode == 139) {
				int boughtLink = buffer.getSShort() & 0xFFFF;
			} else if(opcode == 140) {
				int boughtTemplate = buffer.getSShort() & 0xFFFF;
			} else if(opcode == 148) {
				int anInt1879 = buffer.getSShort() & 0xFFFF;
			} else if(opcode == 149) {
				int anInt1833 = buffer.getSShort() & 0xFFFF;
			} else if(opcode == 249) {
				int length = buffer.getUByte();
				
				//parameters = new HashMap(nextPowerOfTwo(length));
				for(int i = 0; i < length; i++) {
					boolean isString = (buffer.getUByte()) == 1;
					int key = buffer.getUMedium();
					Object value;
					
					value = isString ? buffer.getLine() : buffer.getInt();
					
					//parameters.put(key, value);
				}
			}
		}
	}

	public void encode(DataOutputStream out) throws IOException {
		boolean actionsd = false, actionsd2 = false, actionsd3 = false;
		Set<Integer> written = new HashSet<>();
		do {
			if(modelId > 0 && !written.contains(1)) {
				out.writeByte(1);
				out.writeShort(modelId);
				written.add(1);
			} else if(name != null && !written.contains(2)) {
				out.writeByte(2);
				out.write(name.replaceAll("_", " ").getBytes());
				out.writeByte(10);
				written.add(2);
			}  else if(dataType != DataType.NEWEST && !written.contains(3)) {
				out.writeByte(3);
				out.writeByte(dataType.ordinal());
				written.add(3);
			} else if(iconZoom != 2000 && !written.contains(4)) {
				out.writeByte(4);
				out.writeShort(iconZoom);
				written.add(4);
			} else if(iconYaw != 0 && !written.contains(5)) {
				out.writeByte(5);
				out.writeShort(iconYaw);
				written.add(5);
			} else if(iconRoll != 0 && !written.contains(6)) {
				out.writeByte(6);
				out.writeShort(iconRoll);
				written.add(6);
			} else if(iconHorizontalOffset != 0 && !written.contains(7)) {
				out.writeByte(7);
				out.writeShort(iconHorizontalOffset);
				written.add(7);
			} else if(iconVerticalOffset != 0 && !written.contains(8)) {
				out.writeByte(8);
				out.writeShort(iconVerticalOffset);
				written.add(8);
			} else if(tier != null && !written.contains(9)) {
				out.writeByte(9);
				out.writeByte(tier.ordinal());
				written.add(9);
			} else if(stackable && !written.contains(11)) {
				out.writeByte(11);
				written.add(11);
			} else if(value != 1 && !written.contains(12)) {
				out.writeByte(12);
				out.writeInt(value);
				written.add(12);
			} else if(maleEquip > 0 && !written.contains(23)) {
				out.writeByte(23);
				out.writeShort(maleEquip);
				written.add(23);
			} else if(maleEquipAlt > 0 && !written.contains(24)) {
				out.writeByte(24);
				out.writeShort(maleEquipAlt);
				written.add(24);
			}   else if(femaleEquip > 0 && !written.contains(25)) {
				out.writeByte(25);
				out.writeShort(femaleEquip);
				written.add(25);
			} else if(femaleEquipAlt > 0 && !written.contains(26)) {
				out.writeByte(26);
				out.writeShort(femaleEquipAlt);
				written.add(26);
			} else if(groundActions != null && !actionsd2) {
				for(int i = 0; i < groundActions.length; i++) {
					if(groundActions[i] != null && i < 5) {
						out.writeByte(30 + i);
						out.write(groundActions[i].getBytes());
						out.writeByte(10);
					}
				}
				actionsd2 = true;
			} else if(actions != null && !actionsd) {
				for(int i = 0; i < actions.length; i++) {
					if(actions[i] != null && i < 5) {
						out.writeByte(35 + i);
						out.write(actions[i].getBytes());
						out.writeByte(10);
					}
				}
				actionsd = true;
			} else if(originalModelColors != null && modifiedModelColors != null && !written.contains(40)) {
				out.writeByte(40);
				out.writeByte(originalModelColors.length);
				for(int i = 0; i < originalModelColors.length; i++) {
					out.writeShort(originalModelColors[i]);
					out.writeShort(modifiedModelColors[i]);
				}
				written.add(40);
			} else if(retextureSrc != null && !written.contains(41)) {
				out.writeByte(41);
				out.writeByte(retextureSrc.length);
				for(int i = 0; i < retextureSrc.length; i++) {
					out.writeShort(retextureSrc[i]);
					out.writeShort(retextureDst[i]);
				}
				written.add(41);
			} else if(recolorDstPalette != null && !written.contains(42)) {
				out.writeByte(42);
				out.writeByte(recolorDstPalette.length);
				for(int i = 0; i < retextureSrc.length; i++) {
					out.writeByte(recolorDstPalette[i]);
				}
				written.add(42);
			} else if(tertiaryMaleModel > 0 && !written.contains(78)) {
				out.writeByte(78);
				out.writeShort(tertiaryMaleModel);
				written.add(78);
			} else if(tertiaryFemaleModel > 0 && !written.contains(79)) {
				out.writeByte(79);
				out.writeShort(tertiaryFemaleModel);
				written.add(79);
			} else if(maleDialoguemodelId > 0 && !written.contains(90)) {
				out.writeByte(90);
				out.writeShort(maleDialoguemodelId);
				written.add(90);
			} else if(femaleDialoguemodelId != -1 && !written.contains(91)) {
				out.writeByte(91);
				out.writeShort(femaleDialoguemodelId);
				written.add(91);
			} else if(maleDialogueHatmodelId != -1 && !written.contains(92)) {
				out.writeByte(92);
				out.writeShort(maleDialogueHatmodelId);
				written.add(92);
			} else if(femaleDialogueHatmodelId != -1 && !written.contains(93)) {
				out.writeByte(93);
				out.writeShort(femaleDialogueHatmodelId);
				written.add(93);
			} else if(spriteCameraYaw != 0 && !written.contains(95)) {
				out.writeByte(95);
				out.writeShort(spriteCameraYaw);
				written.add(95);
			} else if(noteId != -1 && !written.contains(97)) {
				out.writeByte(97);
				out.writeShort(noteId);
				written.add(97);
			} else if(noteTemplateId != -1 && !written.contains(98)) {
				out.writeByte(98);
				out.writeShort(noteTemplateId);
				written.add(98);
			} else if(stackableIds != null && !actionsd3) {
				for(int i = 0; i < stackableIds.length; i++) {
					if(stackableIds[i] > 0 && stackAmounts[i] > 0) {
						out.writeByte(100 + i);
						out.writeShort(stackableIds[i]);
						out.writeShort(stackAmounts[i]);
					}
				}
				actionsd3 = true;
			} else if(groundScaleX != 128 && !written.contains(110)) {
				out.writeByte(110);
				out.writeShort(groundScaleX);
				written.add(110);
			} else if(groundScaleY != 128 && !written.contains(111)) {
				out.writeByte(111);
				out.writeShort(groundScaleY);
				written.add(111);
			} else if(groundScaleZ != 128 && !written.contains(112)) {
				out.writeByte(112);
				out.writeShort(groundScaleZ);
				written.add(112);
			} else if(ambience != 0 && !written.contains(113)) {
				out.writeByte(113);
				out.write(ambience);
				written.add(113);
			} else if(diffusion != 0 && !written.contains(114)) {
				out.writeByte(114);
				out.write(diffusion);
				written.add(114);
			} else if(team != 0 && !written.contains(115)) {
				out.writeByte(115);
				out.write(team);
				written.add(115);
			} else if(lendID != -1 && !written.contains(121)) {
				out.writeByte(121);
				out.writeShort(lendID);
				written.add(121);
			} else if(lentItemID != -1 && !written.contains(122)) {
				out.writeByte(122);
				out.writeShort(lentItemID);
				written.add(122);
			} else if((maleEquipOffsetX != 0 || maleEquipOffsetY != 0 || maleEquipOffsetZ != 0) && !written.contains(125)) {
				out.writeByte(125);
				out.write(maleEquipOffsetX);
				out.write(maleEquipOffsetY);
				out.write(maleEquipOffsetZ);
				written.add(125);
			} else if((womanEquipOffsetX != 0 || womanEquipOffsetY != 0 || womanEquipOffsetZ != 0) && !written.contains(126)) {
				out.writeByte(126);
				out.write(womanEquipOffsetX);
				out.write(womanEquipOffsetY);
				out.write(womanEquipOffsetZ);
				written.add(126);
			} else {
				out.writeByte(0);
				break;
			}
		} while(true);
	}

	public static BitmapImage getIcon(int id, int itemAmount, int border) {
		int hash = (border << 16) + id;
		if(iconcache.containsKey(hash)) {
			BitmapImage sprite = iconcache.get(hash);
			if(sprite != null && sprite.imageOriginalHeight != itemAmount && sprite.imageOriginalHeight != -1) {
				iconcache.remove(hash);
				sprite = null;
			}
			if(sprite != null) {
				return sprite;
			}
		}
		ObjectType obj = get(id);
		double oldType = Rasterizer3D.renderType;
		Rasterizer3D.renderType = 10000;
		if(obj == null) {
			Rasterizer3D.renderType = oldType;
			return null;
		}
		if(obj.stackableIds == null) {
			itemAmount = -1;
		}
		if(itemAmount > 1) {
			int stackedId = -1;
			for(int j1 = 0; j1 < 10; j1++) {
				if(itemAmount >= obj.stackAmounts[j1] && obj.stackAmounts[j1] != 0) {
					stackedId = obj.stackableIds[j1];
				}
			}
			if(stackedId != -1) {
				obj = get(stackedId);
			}
		}
		final Model model = obj.getAmountModel(1);
		if(model == null) {
			Rasterizer3D.renderType = oldType;
			return null;
		}
		BitmapImage sprite = null;
		if(obj.noteTemplateId != -1) {
			sprite = getIcon(obj.noteId, 10, -1);
			if(sprite == null) {
				Rasterizer3D.renderType = oldType;
				return null;
			}
		}
		if(obj.lentItemID != -1) {
			try {
				sprite = getIcon(obj.lendID, 50, 0);
			} catch(Exception ignored) {
			}
			if(sprite == null) {
				Rasterizer3D.renderType = oldType;
				return null;
			}
		}
		final BitmapImage sprite2 = new BitmapImage(32, 32);
		Viewport viewport = Rasterizer3D.viewport;
		final int pixels[] = Rasterizer2D.canvasRaster;
		final int width = Rasterizer2D.canvasWidth;
		final int height = Rasterizer2D.canvasHeight;
		final int startX = Rasterizer2D.clipStartX;
		final int endX = Rasterizer2D.clipEndX;
		final int startY = Rasterizer2D.clipStartY;
		final int endY = Rasterizer2D.clipEndY;
		Rasterizer2D.setCanvas(sprite2.imageRaster, 32, 32);
		Rasterizer2D.fillRectangle(0, 0, 32, 32, 0);
		Rasterizer3D.viewport = new Viewport(0, 0, 32, 32, 32);
		int zoom = obj.iconZoom;
		if(border == -1) {
			zoom = (int) (zoom * 1.5D);
		}
		if(border > 0) {
			zoom = (int) (zoom * 1.04D);
		}
		final int l3 = Rasterizer3D.angleSine[obj.iconYaw] * zoom >> 16;
		final int i4 = Rasterizer3D.angleCosine[obj.iconYaw] * zoom >> 16;
		model.drawModel(obj.iconRoll, obj.spriteCameraYaw, obj.iconYaw, obj.iconHorizontalOffset, l3 + model.maxVerticalDistUp / 2 + obj.iconVerticalOffset, i4 + obj.iconVerticalOffset);
		for(int _x = 31; _x >= 0; _x--) {
			for(int _y = 31; _y >= 0; _y--) {
				if(sprite2.imageRaster[_x + _y * 32] == 0) {
					if(_x > 0 && sprite2.imageRaster[_x - 1 + _y * 32] > 1) {
						sprite2.imageRaster[_x + _y * 32] = 1;
					} else if(_y > 0 && sprite2.imageRaster[_x + (_y - 1) * 32] > 1) {
						sprite2.imageRaster[_x + _y * 32] = 1;
					} else if(_x < 31 && sprite2.imageRaster[_x + 1 + _y * 32] > 1) {
						sprite2.imageRaster[_x + _y * 32] = 1;
					} else if(_y < 31 && sprite2.imageRaster[_x + (_y + 1) * 32] > 1) {
						sprite2.imageRaster[_x + _y * 32] = 1;
					}
				}
			}
		}
		if(border > 0) {
			for(int _x = 31; _x >= 0; _x--) {
				for(int _y = 31; _y >= 0; _y--) {
					if(sprite2.imageRaster[_x + _y * 32] == 0) {
						if(_x > 0 && sprite2.imageRaster[_x - 1 + _y * 32] == 1) {
							sprite2.imageRaster[_x + _y * 32] = border;
						} else if(_y > 0 && sprite2.imageRaster[_x + (_y - 1) * 32] == 1) {
							sprite2.imageRaster[_x + _y * 32] = border;
						} else if(_x < 31 && sprite2.imageRaster[_x + 1 + _y * 32] == 1) {
							sprite2.imageRaster[_x + _y * 32] = border;
						} else if(_y < 31 && sprite2.imageRaster[_x + (_y + 1) * 32] == 1) {
							sprite2.imageRaster[_x + _y * 32] = border;
						}
					}
				}
			}
		} else if(border == 0) {
			for(int _x = 31; _x >= 0; _x--) {
				for(int _y = 31; _y >= 0; _y--) {
					if(sprite2.imageRaster[_x + _y * 32] == 0 && _x > 0 && _y > 0 && sprite2.imageRaster[_x - 1 + (_y - 1) * 32] > 0) {
						sprite2.imageRaster[_x + _y * 32] = 0x302020;
					}
				}
			}
		}
		if(obj.noteTemplateId != -1 && sprite != null) {
			final int maxWidth = sprite.imageOriginalWidth;
			final int maxHeight = sprite.imageOriginalHeight;
			sprite.imageOriginalWidth = 32;
			sprite.imageOriginalHeight = 32;
			sprite.drawImage(0, 0);
			sprite.imageOriginalWidth = maxWidth;
			sprite.imageOriginalHeight = maxHeight;
		}
		if(obj.lendID != -1 && sprite != null) {
			final int l5 = sprite.imageOriginalWidth;
			final int j6 = sprite.imageOriginalHeight;
			sprite.imageOriginalWidth = 32;
			sprite.imageOriginalHeight = 32;
			sprite.drawImage(0, 0);
			sprite.imageOriginalWidth = l5;
			sprite.imageOriginalHeight = j6;
		}
		if(!Rasterizer3D.textureMissing) {
			iconcache.put((border << 16) + id, sprite2);
		}
		Rasterizer2D.setCanvas(pixels, height, width);
		Rasterizer2D.setClip(startX, startY, endX, endY);
		Rasterizer3D.viewport = viewport;
		if(obj.stackable) {
			sprite2.imageOriginalWidth = 33;
		} else {
			sprite2.imageOriginalWidth = 32;
		}
		sprite2.imageOriginalHeight = itemAmount;
		Rasterizer3D.renderType = oldType;
		return sprite2;
	}
	
	public static void reset() {
		modelcache = null;
		iconcache = null;
		index = null;
		data = null;
	}
	
	public boolean isDialogueModelCached(int gender) {
		int dialoguemodelId = maleDialoguemodelId;
		int dialogueHatmodelId = maleDialogueHatmodelId;
		if(gender == 1) {
			dialoguemodelId = femaleDialoguemodelId;
			dialogueHatmodelId = femaleDialogueHatmodelId;
		}
		if(dialoguemodelId == -1) {
			return true;
		}
		boolean cached = true;
		if(!Model.isCached(dialoguemodelId, dataType)) {
			cached = false;
		}
		if(dialogueHatmodelId != -1 && !Model.isCached(dialogueHatmodelId, dataType)) {
			cached = false;
		}
		return cached;
	}
	
	public Model getDialogueModel(int gender) {
		int k = maleDialoguemodelId;
		int l = maleDialogueHatmodelId;
		if(gender == 1) {
			k = femaleDialoguemodelId;
			l = femaleDialogueHatmodelId;
		}
		if(k == -1) {
			return null;
		}
		Model model = Model.fetchModel(k, dataType);
		if(l != -1) {
			final Model model_1 = Model.fetchModel(l, dataType);
			final Model model_2[] = {model, model_1};
			model = new Model(2, model_2);
		}
		if(originalModelColors != null) {
			for(int i1 = 0; i1 < originalModelColors.length; i1++) {
				model.replaceHsl(originalModelColors[i1], modifiedModelColors[i1]);
			}
		}
		if(retextureSrc != null) {
			for(int i1 = 0; i1 < retextureSrc.length; i1++) {
				model.setTexture(retextureSrc[i1], retextureDst[i1]);
			}
		}
		return model;
	}
	
	public boolean equipModelFetched(int gender) {
		int firstModel = maleEquip;
		int secondModel = maleEquipAlt;
		int thirdModel = tertiaryMaleModel;
		if(gender == 1) {
			firstModel = femaleEquip;
			secondModel = femaleEquipAlt;
			thirdModel = tertiaryFemaleModel;
		}
		if(firstModel == -1) {
			return true;
		}
		boolean flag = true;
		if(!Model.isCached(firstModel, dataType)) {
			flag = false;
		}
		if(secondModel != -1 && !Model.isCached(secondModel, dataType)) {
			flag = false;
		}
		if(thirdModel != -1 && !Model.isCached(thirdModel, dataType)) {
			flag = false;
		}
		return flag;
	}
	
	public Model getEquipModel(int gender) {
		int j = maleEquip;
		int k = maleEquipAlt;
		int l = tertiaryMaleModel;
		if(gender == 1) {
			j = femaleEquip;
			k = femaleEquipAlt;
			l = tertiaryFemaleModel;
		}
		if(j == -1) {
			return null;
		}
		Model model = Model.fetchModel(j, dataType);
		if(k != -1) {
			if(l != -1) {
				final Model model_1 = Model.fetchModel(k, dataType);
				final Model model_3 = Model.fetchModel(l, dataType);
				final Model model_33[] = {model, model_1, model_3};
				model = new Model(3, model_33);
			} else {
				final Model model_2 = Model.fetchModel(k, dataType);
				final Model model_33[] = {model, model_2};
				model = new Model(2, model_33);
			}
		}
		
		if(gender == 0 && (maleEquipOffsetX != 0 || maleEquipOffsetY != 0 || maleEquipOffsetZ != 0))
			model.translate(maleEquipOffsetX, maleEquipOffsetY, maleEquipOffsetZ);
		
		if(gender == 1 && (womanEquipOffsetX != 0 || womanEquipOffsetY != 0 || womanEquipOffsetZ != 0))
			model.translate(womanEquipOffsetX, womanEquipOffsetY, womanEquipOffsetZ);
		if(originalModelColors != null) {
			for(int i1 = 0; i1 < originalModelColors.length; i1++) {
				model.replaceHsl(originalModelColors[i1], modifiedModelColors[i1]);
			}
		}
		if(retextureSrc != null) {
			for(int i1 = 0; i1 < retextureSrc.length; i1++) {
				model.setTexture(retextureSrc[i1], retextureDst[i1]);
			}
		}
		return model;
	}
	
	public Model getAmountModel(int amt) {
		if(stackableIds != null && amt > 1) {
			int j = -1;
			for(int k = 0; k < 10; k++) {
				if(amt >= stackAmounts[k] && stackAmounts[k] != 0) {
					j = stackableIds[k];
				}
			}
			if(j != -1) {
				return get(j).getAmountModel(1);
			}
		}
		Model model = modelcache.get(id);
		if(model != null) {
			return model;
		}
		model = Model.fetchModel(modelId, dataType);
		if(model == null) {
			return null;
		}
		if(groundScaleX != 128 || groundScaleY != 128 || groundScaleZ != 128) {
			model.scale(groundScaleX, groundScaleY, groundScaleZ);
		}
		if(originalModelColors != null) {
			for(int l = 0; l < originalModelColors.length; l++) {
				model.replaceHsl(originalModelColors[l], modifiedModelColors[l]);
			}
		}
		if(retextureSrc != null) {
			for(int i1 = 0; i1 < retextureSrc.length; i1++) {
				model.setTexture(retextureSrc[i1], retextureDst[i1]);
			}
		}
		model.calculateLighting(64 + ambience, 768 + diffusion, -50, -10, -50, true);
		model.hoverable = true;
		if(!Rasterizer3D.textureMissing)
			modelcache.put(id, model);
		return model;
	}
	
	Model getItemModel(int i) {
		if(stackableIds != null && i > 1) {
			int j = -1;
			for(int k = 0; k < 10; k++) {
				if(i >= stackAmounts[k] && stackAmounts[k] != 0) {
					j = stackableIds[k];
				}
			}
			if(j != -1) {
				return get(j).getItemModel(1);
			}
		}
		final Model model = Model.fetchModel(modelId, dataType);
		if(model == null) {
			return null;
		}
		if(originalModelColors != null) {
			for(int l = 0; l < originalModelColors.length; l++) {
				model.replaceHsl(originalModelColors[l], modifiedModelColors[l]);
			}
		}
		if(retextureSrc != null) {
			for(int i1 = 0; i1 < retextureSrc.length; i1++) {
				model.setTexture(retextureSrc[i1], retextureDst[i1]);
			}
		}
		return model;
	}
	
	private void renew() {
		modelId = 0;
		name = null;
		description = null;
		originalModelColors = null;
		modifiedModelColors = null;
		iconZoom = 2000;
		iconYaw = 0;
		iconRoll = 0;
		iconYaw = 0;
		iconRoll = 0;
		spriteCameraYaw = 0;
		iconHorizontalOffset = 0;
		iconVerticalOffset = 0;
		stackable = false;
		value = 1;
		groundActions = null;
		actions = null;
		dataType = DataType.NEWEST;
		maleEquip = -1;
		tier = null;
		maleEquipAlt = -1;
		femaleEquip = -1;
		femaleEquipAlt = -1;
		tertiaryMaleModel = -1;
		tertiaryFemaleModel = -1;
		maleDialoguemodelId = -1;
		maleDialogueHatmodelId = -1;
		femaleDialoguemodelId = -1;
		femaleDialogueHatmodelId = -1;
		stackableIds = null;
		stackAmounts = null;
		noteId = -1;
		noteTemplateId = -1;
		groundScaleX = 128;
		groundScaleY = 128;
		groundScaleZ = 128;
		ambience = 0;
		diffusion = 0;
		team = 0;
		lendID = -1;
		lentItemID = -1;
		maleEquipOffsetX = 0;
		maleEquipOffsetY = 0;
		maleEquipOffsetZ = 0;
		womanEquipOffsetX = 0;
		womanEquipOffsetY = 0;
		womanEquipOffsetZ = 0;
		equipActions = new String[5];
		// TODO: Are all of the fields cleared?
	}
	
	private void toLend() {
		final ObjectType itemDef = get(lentItemID);
		actions = new String[5];
		modelId = itemDef.modelId;
		iconHorizontalOffset = itemDef.iconHorizontalOffset;
		iconVerticalOffset = itemDef.iconVerticalOffset;
		iconZoom = itemDef.iconZoom;
		iconYaw = itemDef.iconYaw;
		iconRoll = itemDef.iconRoll;
		spriteCameraYaw = itemDef.spriteCameraYaw;
		value = 0;
		final ObjectType obj = get(lendID);
		maleDialogueHatmodelId = obj.maleDialogueHatmodelId;
		originalModelColors = itemDef.originalModelColors;
		tertiaryMaleModel = obj.tertiaryMaleModel;
		maleEquipAlt = obj.maleEquipAlt;
		femaleDialogueHatmodelId = obj.femaleDialogueHatmodelId;
		dataType = obj.dataType;
		maleDialoguemodelId = obj.maleDialoguemodelId;
		groundActions = obj.groundActions;
		maleEquip = obj.maleEquip;
		name = obj.name;
		femaleDialoguemodelId = obj.femaleDialoguemodelId;
		femaleEquip = obj.femaleEquip;
		femaleEquipAlt = obj.femaleEquipAlt;
		tertiaryFemaleModel = obj.tertiaryFemaleModel;
		modifiedModelColors = obj.modifiedModelColors;
		team = obj.team;
		if(obj.actions != null) {
			System.arraycopy(obj.actions, 0, actions, 0, 4);
		}
		actions[4] = "Discard";
	}

	public ObjectType clone() {
		final ObjectType itemDef = new ObjectType();
		itemDef.actions = new String[5];
		itemDef.id = id;
		itemDef.tier = tier;
		itemDef.dataType = dataType;
		itemDef.modelId = modelId;
		itemDef.iconZoom = iconZoom;
		itemDef.iconYaw = iconYaw;
		itemDef.iconRoll = iconRoll;
		itemDef.iconHorizontalOffset = iconHorizontalOffset;
		itemDef.iconVerticalOffset = iconVerticalOffset;
		itemDef.spriteCameraYaw = spriteCameraYaw;
		itemDef.maleEquip = maleEquip;
		itemDef.maleEquipAlt = maleEquipAlt;
		itemDef.femaleEquip = femaleEquip;
		itemDef.femaleEquipAlt = femaleEquipAlt;
		itemDef.groundScaleZ = groundScaleZ;
		itemDef.groundScaleY = groundScaleY;
		itemDef.groundScaleX = groundScaleX;
		itemDef.retextureDst = retextureDst;
		itemDef.retextureSrc = retextureSrc;
		itemDef.modifiedModelColors = modifiedModelColors;
		itemDef.originalModelColors = originalModelColors;
		itemDef.recolorDstPalette = recolorDstPalette;
		itemDef.value = 0;
		itemDef.name = name;
		itemDef.description = description;
		itemDef.stackable = stackable;
		itemDef.noteId = noteId;
		itemDef.noteTemplateId = noteTemplateId;
		itemDef.stackableIds = stackableIds;
		itemDef.stackAmounts = stackAmounts;
		itemDef.maleDialogueHatmodelId = maleDialogueHatmodelId;
		itemDef.femaleDialogueHatmodelId = femaleDialogueHatmodelId;
		itemDef.maleDialoguemodelId = maleDialoguemodelId;
		itemDef.femaleDialoguemodelId = femaleDialoguemodelId;
		itemDef.tertiaryMaleModel = tertiaryMaleModel;
		itemDef.tertiaryFemaleModel = tertiaryFemaleModel;
		itemDef.diffusion = diffusion;
		itemDef.ambience = ambience;
		itemDef.womanEquipOffsetZ = womanEquipOffsetZ;
		itemDef.womanEquipOffsetY = womanEquipOffsetY;
		itemDef.womanEquipOffsetX = womanEquipOffsetX;
		itemDef.maleEquipOffsetZ = maleEquipOffsetZ;
		itemDef.maleEquipOffsetY = maleEquipOffsetY;
		itemDef.maleEquipOffsetX = maleEquipOffsetX;
		itemDef.team = team;
		itemDef.lendID = lendID;
		itemDef.lentItemID = lentItemID;

		itemDef.equipActions = new String[5];
		itemDef.actions = new String[5];
		itemDef.groundActions = new String[5];

		if(equipActions != null) {
			System.arraycopy(equipActions, 0, itemDef.equipActions, 0, 5);
		}
		if(actions != null) {
			System.arraycopy(actions, 0, itemDef.actions, 0, 5);
		}
		if(groundActions != null) {
			System.arraycopy(groundActions, 0, itemDef.groundActions, 0, 5);
		}
		return this;
	}
	
	private void toNote() {
		final ObjectType itemDef = get(noteTemplateId);
		modelId = itemDef.modelId;
		iconZoom = itemDef.iconZoom;
		iconYaw = itemDef.iconYaw;
		iconRoll = itemDef.iconRoll;
		spriteCameraYaw = itemDef.spriteCameraYaw;
		iconHorizontalOffset = itemDef.iconHorizontalOffset;
		iconVerticalOffset = itemDef.iconVerticalOffset;
		originalModelColors = itemDef.originalModelColors;
		modifiedModelColors = itemDef.modifiedModelColors;
		final ObjectType obj = get(noteId);
		name = obj.name;
		value = obj.value;
		String s = "a";
		if(obj.name != null) {
			final char c = obj.name.charAt(0);
			if(c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
				s = "an";
			}
		}
		description = ("Swap this note at any bank for " + s + " " + obj.name + ".");
		stackable = true;
	}

}
