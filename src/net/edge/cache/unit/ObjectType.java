package net.edge.cache.unit;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.edge.media.Viewport;
import net.edge.cache.CacheArchive;
import net.edge.game.model.Model;
import net.edge.media.Rasterizer2D;
import net.edge.media.Rasterizer3D;
import net.edge.media.img.BitmapImage;
import net.edge.util.io.Buffer;

public final class ObjectType {
	
	public static Buffer data;
	public static int[] index;
	public static Int2ObjectArrayMap<ObjectType> defCache = new Int2ObjectArrayMap<>();
	public static Int2ObjectOpenHashMap<BitmapImage> iconcache = new Int2ObjectOpenHashMap<>();
	public static Int2ObjectOpenHashMap<Model> modelcache = new Int2ObjectOpenHashMap<>();
	
	public int id;
	public int value;
	private byte femaleEquipOffset;
	private int[] modifiedModelColors;
	private int[] originalModelColors;
	private boolean membersObject;
	private int anInt162;
	private int noteTemplateId;
	private int femaleEquipAlt;
	private int maleEquip;
	private int maleDialogueHatmodelId;
	private int anInt167;
	public String groundActions[];
	private int iconHorizontalOffset;
	public String name;
	private int femaleDialogueHatmodelId;
	private int modelId;
	private int maleDialoguemodelId;
	public boolean stackable;
	public String description;
	private int noteId;
	public int iconZoom;
	private int anInt184;
	private int anInt185;
	private int maleEquipAlt;
	public String actions[];
	public int iconYaw;
	private int anInt191;
	private int anInt192;
	private int[] stackableIds;
	private int iconVerticalOffset;
	private int anInt196;
	private int femaleDialoguemodelId;
	public int iconRoll;
	private int femaleEquip;
	private int[] stackAmounts;
	public int team;
	public static int length;
	private int anInt204;
	private byte maleEquipOffset;
	private int lendID;
	private int lentItemID;
	private byte[] recolorDstPalette;
	private short[] retextureDst;
	private short[] retextureSrc;
	private int womanEquipOffsetZ;
	private int womanEquipOffsetY;
	private int womanEquipOffsetX;
	private int maleEquipOffsetZ;
	private int maleEquipOffsetY;
	private int maleEquipOffsetX;
	private int[] campaigns;
	private boolean fixPriority;
	public String[] equipActions;
	
	public ObjectType() {
		id = -1;
	}
	
	public static ObjectType get(int id) {
		if(defCache.containsKey(id))
			return defCache.get(id);
		final ObjectType obj = new ObjectType();
		if(id > index.length)
			return null;
		data.pos = index[id];
		obj.id = id;
		obj.renew();
		obj.read(data);
		
		obj.transform();

		switch(id) {
			case 2552:
			case 2554:
			case 2556:
			case 2558:
			case 2560:
			case 2562:
			case 2564:
			case 2566: //Ring of duelling
				obj.equipActions[3] = "Duel Arena";
				obj.equipActions[2] = "Castle Wars";
				obj.equipActions[1] = "Clan wars";
				break;
			case 11283:
				obj.equipActions[1] = "Operate";
				break;
			case 1706:
			case 1708:
			case 1710:
			case 1712:
            case 10362:
                obj.equipActions[4] = "Edgeville";
                obj.equipActions[3] = "Karamja";
                obj.equipActions[2] = "Draynor Village";
                obj.equipActions[1] = "Al-Kharid";
                break;

		}
		if(obj.noteTemplateId != -1) {
			obj.toNote();
		}
		if(obj.lentItemID != -1) {
			obj.toLend();
		}
		
		if(obj.id == 692) {
			obj.name = "Donator certificate";
			obj.actions = new String[]{"Claim", null, null, null, null};
		}
		if(obj.id == 18741) {
			obj.name = "Ironmen cape";
		}
		if(obj.id == 18740) {
			obj.name = "Ironmen master cape";
			System.out.println(obj.maleEquip);
		}
		if(obj.id == 693) {
			obj.name = "Super donator certificate";
			obj.actions = new String[]{"Claim", null, null, null, null};
		}
		if(obj.id == 691) {
			obj.name = "Extreme donator certificate";
			obj.actions = new String[]{"Claim", null, null, null, null};
		}
		if(obj.id == 6829) {
			obj.name = "Vote box";
			obj.actions = new String[]{"Open", null, null, null, null};
		}
		
		if(obj.id == 21432) {
			obj.name = "Book of diplomacy";
			obj.actions = new String[]{"Open", null, null, null, "Drop"};
		}
		
		if(id == 3904)
			obj.pet("Trapped abyssal orphan");
		if(id == 3906)
			obj.pet("Trapped Jadiku");
		if(id == 3908)
			obj.pet("Trapped Toram");
		if(id == 3910)
			obj.pet("Trapped Wyrmy");
		
		if(id == 3912)
			obj.pet("Trapped Kraa");//armadyl
		if(id == 3914)
			obj.pet("Trapped Grary");//bandos
		if(id == 3916)
			obj.pet("Trapped Tsutsy");//zamorak
		if(id == 3918)
			obj.pet("Trapped Zilzy");//saradomin
		
		defCache.put(id, obj);
		return obj;
	}
	
	private void pet(String name) {
		ObjectType copied = get(10025);
		this.modelId = copied.modelId;
		this.modifiedModelColors = copied.modifiedModelColors;
		this.originalModelColors = copied.originalModelColors;
		this.iconRoll = copied.iconRoll;
		this.iconYaw = copied.iconYaw;
		this.iconZoom = copied.iconZoom;
		this.stackable = false;
		this.noteId = -1;
		this.name = name;
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
		if(obj == null) {
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
			return null;
		}
		BitmapImage sprite = null;
		if(obj.noteTemplateId != -1) {
			sprite = getIcon(obj.noteId, 10, -1);
			if(sprite == null) {
				return null;
			}
		}
		if(obj.lentItemID != -1) {
			try {
				sprite = getIcon(obj.lendID, 50, 0);
			} catch(Exception ignored) {
			}
			if(sprite == null) {
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
		model.drawModel(obj.iconRoll, obj.anInt204, obj.iconYaw, obj.iconHorizontalOffset, l3 + model.maxVerticalDistUp / 2 + obj.iconVerticalOffset, i4 + obj.iconVerticalOffset);
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
		if(border == 0 && !Rasterizer3D.textureMissing) {
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
		return sprite2;
	}
	
	public static void reset() {
		modelcache = null;
		iconcache = null;
		index = null;
		data = null;
	}
	
	public static void unpack(CacheArchive archive) {
		data = new Buffer(archive.getFile("obj.dat"));
		final Buffer bufferidx = new Buffer(archive.getFile("obj.idx"));
		length = bufferidx.getUShort();
		System.out.println("[loading] obj size: " + length);
		index = new int[length];
		int pos = 0;
		for(int i = 0; i < length; i++) {
			index[i] = pos;
			pos += bufferidx.getUShort();
		}
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
		if(!Model.isCached(dialoguemodelId)) {
			cached = false;
		}
		if(dialogueHatmodelId != -1 && !Model.isCached(dialogueHatmodelId)) {
			cached = false;
		}
		return cached;
	}
	
	public Model method194(int j) {
		int k = maleDialoguemodelId;
		int l = maleDialogueHatmodelId;
		if(j == 1) {
			k = femaleDialoguemodelId;
			l = femaleDialogueHatmodelId;
		}
		if(k == -1) {
			return null;
		}
		Model model = Model.get(k);
		if(l != -1) {
			final Model model_1 = Model.get(l);
			final Model aclass30_sub2_sub4_sub6s[] = {model, model_1};
			model = new Model(2, aclass30_sub2_sub4_sub6s);
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
	
	public boolean method195(int j) {
		int k = maleEquip;
		int l = maleEquipAlt;
		int i1 = anInt185;
		if(j == 1) {
			k = femaleEquip;
			l = femaleEquipAlt;
			i1 = anInt162;
		}
		if(k == -1) {
			return true;
		}
		boolean flag = true;
		if(!Model.isCached(k)) {
			flag = false;
		}
		if(l != -1 && !Model.isCached(l)) {
			flag = false;
		}
		if(i1 != -1 && !Model.isCached(i1)) {
			flag = false;
		}
		return flag;
	}
	
	public Model method196(int i) {
		int j = maleEquip;
		int k = maleEquipAlt;
		int l = anInt185;
		if(i == 1) {
			j = femaleEquip;
			k = femaleEquipAlt;
			l = anInt162;
		}
		if(j == -1) {
			return null;
		}
		Model model = Model.get(j);
		if(k != -1) {
			if(l != -1) {
				final Model model_1 = Model.get(k);
				final Model model_3 = Model.get(l);
				final Model aclass30_sub2_sub4_sub6_1s[] = {model, model_1, model_3};
				model = new Model(3, aclass30_sub2_sub4_sub6_1s);
			} else {
				final Model model_2 = Model.get(k);
				final Model aclass30_sub2_sub4_sub6s[] = {model, model_2};
				model = new Model(2, aclass30_sub2_sub4_sub6s);
			}
		}
		
		if(i == 0 && (maleEquipOffsetX != 0 || maleEquipOffsetY != 0 || maleEquipOffsetZ != 0))
			model.translate(maleEquipOffsetX, maleEquipOffsetY, maleEquipOffsetZ);
		
		if(i == 1 && (womanEquipOffsetX != 0 || womanEquipOffsetY != 0 || womanEquipOffsetZ != 0))
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
		if(fixPriority) {
			if(model.triPri != null) {
				for(int p = 0; p < model.triPri.length; p++)
					model.triPri[p] = 10;
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
		model = Model.get(modelId);
		if(model == null) {
			return null;
		}
		if(anInt167 != 128 || anInt192 != 128 || anInt191 != 128) {
			model.scale(anInt167, anInt192, anInt191);
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
		model.calculateLighting(64 + anInt196, 768 + anInt184, -50, -10, -50, true);
		model.hoverable = true;
		if(fixPriority) {
			if(model.triPri != null) {
				for(int p = 0; p < model.triPri.length; p++)
					model.triPri[p] = 10;
			}
		}
		if(!Rasterizer3D.textureMissing)
			modelcache.put(id, model);
		return model;
	}
	
	Model method202(int i) {
		if(stackableIds != null && i > 1) {
			int j = -1;
			for(int k = 0; k < 10; k++) {
				if(i >= stackAmounts[k] && stackAmounts[k] != 0) {
					j = stackableIds[k];
				}
			}
			if(j != -1) {
				return get(j).method202(1);
			}
		}
		final Model model = Model.get(modelId);
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
	
	public void modelData(int mZ, int mR1, int mR2, int mO1, int mO2) {
		iconZoom = mZ;
		iconYaw = mR1;
		iconRoll = mR2;
		iconHorizontalOffset = mO1;
		iconVerticalOffset = mO2;
	}
	
	public void models(int mID, int mE, int fE, int mE2, int fE2) {
		modelId = mID;
		maleEquip = mE;
		femaleEquip = fE;
		maleEquipAlt = mE2;
		femaleEquipAlt = fE2;
	}
	
	private void read(Buffer buffer) {
		do {
			int opcode = buffer.getUByte();
			if(opcode == 0)
				return;
			if(opcode == 1) {
				modelId = buffer.getUShort();
			} else if(opcode == 2) {
				name = buffer.getString();
			} else if(opcode == 4) {
				iconZoom = buffer.getUShort();
			} else if(opcode == 5) {
				iconYaw = buffer.getUShort();
			} else if(opcode == 6) {
				iconRoll = buffer.getUShort();
			} else if(opcode == 7) {
				iconHorizontalOffset = buffer.getUShort();
				if(iconHorizontalOffset > 32767) {
					iconHorizontalOffset -= 65536;
				}
			} else if(opcode == 8) {
				iconVerticalOffset = buffer.getUShort();
				if(iconVerticalOffset > 32767) {
					iconVerticalOffset -= 65536;
				}
			} else if(opcode == 11) {
				stackable = true;
			} else if(opcode == 12) {
				value = buffer.getInt();
			} else if(opcode == 16) {
				membersObject = true;
			} else if(opcode == 18) {
				buffer.getUShort();
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
				groundActions[opcode - 30] = buffer.getString();
				if(groundActions[opcode - 30].equalsIgnoreCase("hidden"))
					groundActions[opcode - 30] = null;
			} else if(opcode >= 35 && opcode < 40) {
				if(actions == null)
					actions = new String[10];
				actions[opcode - 35] = buffer.getString();
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
				anInt185 = buffer.getUShort();
			} else if(opcode == 79) {
				anInt162 = buffer.getUShort();
			} else if(opcode == 90) {
				maleDialoguemodelId = buffer.getUShort();
			} else if(opcode == 91) {
				femaleDialoguemodelId = buffer.getUShort();
			} else if(opcode == 92) {
				maleDialogueHatmodelId = buffer.getUShort();
			} else if(opcode == 93) {
				femaleDialogueHatmodelId = buffer.getUShort();
			} else if(opcode == 95) {
				anInt204 = buffer.getUShort();
			} else if(opcode == 96) {
				buffer.getUByte();
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
				anInt167 = buffer.getUShort();
			} else if(opcode == 111) {
				anInt192 = buffer.getUShort();
			} else if(opcode == 112) {
				anInt191 = buffer.getUShort();
			} else if(opcode == 113) {
				anInt196 = buffer.getSByte();
			} else if(opcode == 114) {
				anInt184 = buffer.getSByte() * 5;
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
			} else if(opcode == 127) {
				buffer.getUByte();
				buffer.getUShort();
			} else if(opcode == 128) {
				buffer.getUByte();
				buffer.getUShort();
			} else if(opcode == 129) {
				buffer.getUByte();
				buffer.getUShort();
			} else if(opcode == 130) {
				buffer.getUByte();
				buffer.getUShort();
			} else if(opcode == 132) {
				int i_40_ = buffer.getUByte();
				campaigns = new int[i_40_];
				for(int i_41_ = 0; i_41_ < i_40_; i_41_++) {
					campaigns[i_41_] = buffer.getUShort();
				}
			} else if(opcode == 134) {
				buffer.getUByte();
			} else if(opcode == 139) {
				buffer.getUShort();
			} else if(opcode == 140) {
				buffer.getUShort();
			} else if(opcode == 249) {
				int count = buffer.getUByte();
				
				for(int i2 = 0; i2 != count; ++i2) {
					boolean string = buffer.getUByte() == 1;
					int key = buffer.getUMedium();
					if(string)
						buffer.getString();
					else
						buffer.getInt();
				}
				
			} else {
				System.out.println("[ObjectType] Unknown opcode: " + opcode);
				break;
			}
		} while(true);
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
		anInt204 = 0;
		iconHorizontalOffset = 0;
		iconVerticalOffset = 0;
		stackable = false;
		value = 1;
		membersObject = false;
		groundActions = null;
		actions = null;
		maleEquip = -1;
		maleEquipAlt = -1;
		maleEquipOffset = 0;
		femaleEquip = -1;
		femaleEquipAlt = -1;
		femaleEquipOffset = 0;
		anInt185 = -1;
		anInt162 = -1;
		maleDialoguemodelId = -1;
		maleDialogueHatmodelId = -1;
		femaleDialoguemodelId = -1;
		femaleDialogueHatmodelId = -1;
		stackableIds = null;
		stackAmounts = null;
		noteId = -1;
		noteTemplateId = -1;
		anInt167 = 128;
		anInt192 = 128;
		anInt191 = 128;
		anInt196 = 0;
		anInt184 = 0;
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
		anInt204 = itemDef.anInt204;
		value = 0;
		final ObjectType obj = get(lendID);
		maleDialogueHatmodelId = obj.maleDialogueHatmodelId;
		originalModelColors = itemDef.originalModelColors;
		anInt185 = obj.anInt185;
		maleEquipAlt = obj.maleEquipAlt;
		femaleDialogueHatmodelId = obj.femaleDialogueHatmodelId;
		maleDialoguemodelId = obj.maleDialoguemodelId;
		groundActions = obj.groundActions;
		maleEquip = obj.maleEquip;
		name = obj.name;
		femaleEquip = obj.femaleEquip;
		membersObject = obj.membersObject;
		femaleDialoguemodelId = obj.femaleDialoguemodelId;
		femaleEquipAlt = obj.femaleEquipAlt;
		anInt162 = obj.anInt162;
		modifiedModelColors = obj.modifiedModelColors;
		team = obj.team;
		if(obj.actions != null) {
			System.arraycopy(obj.actions, 0, actions, 0, 4);
		}
		actions[4] = "Discard";
	}
	
	private void toNote() {
		final ObjectType itemDef = get(noteTemplateId);
		modelId = itemDef.modelId;
		iconZoom = itemDef.iconZoom;
		iconYaw = itemDef.iconYaw;
		iconRoll = itemDef.iconRoll;
		anInt204 = itemDef.anInt204;
		iconHorizontalOffset = itemDef.iconHorizontalOffset;
		iconVerticalOffset = itemDef.iconVerticalOffset;
		originalModelColors = itemDef.originalModelColors;
		modifiedModelColors = itemDef.modifiedModelColors;
		final ObjectType obj = get(noteId);
		name = obj.name;
		membersObject = obj.membersObject;
		value = obj.value;
		String s = "a";
		final char c = obj.name.charAt(0);
		if(c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
			s = "an";
		}
		description = ("Swap this note at any bank for " + s + " " + obj.name + ".");
		stackable = true;
	}
	
	public void totalColors(int total) {
		originalModelColors = new int[total];
		modifiedModelColors = new int[total];
	}
	
	private void flask(String itemname, int color, int dose) {
		name = itemname + " flask" + ((dose > 0) ? " (" + dose + ")" : "");
		description = "It seems to be an enlarged vial of " + ((dose > 0) ? "" + itemname + " potion." : "nothing.");
		iconZoom = 804;
		iconYaw = 131;
		iconRoll = 198;
		iconVerticalOffset = 1;
		iconHorizontalOffset = -1;
		modifiedModelColors = new int[]{color};
		originalModelColors = new int[]{33715};
		groundActions = new String[]{null, null, "Take", null, null};
		actions = new String[]{itemname.equalsIgnoreCase("empty") ? null : "Drink", null, null, null, "Drop"};
		int model = 61741;
		if(dose == 6)
			model = 61732;
		if(dose == 5)
			model = 61729;
		if(dose == 4)
			model = 61764;
		if(dose == 3)
			model = 61727;
		if(dose == 2)
			model = 61731;
		if(dose == 1)
			model = 61812;
		modelId = model;
		anInt196 = 40;
		anInt184 = 200;
	}
	
	private void bloodMoney(int itemId) {
		ObjectType coin1 = ObjectType.get(itemId);
		this.actions = coin1.actions;
		this.groundActions = coin1.groundActions;
		this.name = "Blood money";
		this.description = "It's Blood money";
		this.originalModelColors = new int[1];
		this.modifiedModelColors = new int[1];
		this.originalModelColors[0] = 8128;
		this.modifiedModelColors[0] = 947;
		this.modelId = coin1.modelId;
		this.iconZoom = coin1.iconZoom;
		this.stackable = coin1.stackable;
		this.iconHorizontalOffset = coin1.iconHorizontalOffset;
		this.iconVerticalOffset = coin1.iconVerticalOffset;
		this.iconRoll = coin1.iconRoll;
		this.iconYaw = coin1.iconYaw;
		this.stackableIds = new int[10];
		this.stackAmounts = new int[10];
		this.stackableIds[0] = 19001;
		this.stackAmounts[0] = 2;
		this.stackableIds[1] = 19002;
		this.stackAmounts[1] = 3;
		this.stackableIds[2] = 19003;
		this.stackAmounts[2] = 4;
		this.stackableIds[3] = 19004;
		this.stackAmounts[3] = 5;
		this.stackableIds[4] = 19005;
		this.stackAmounts[4] = 25;
		this.stackableIds[5] = 19006;
		this.stackAmounts[5] = 100;
		this.stackableIds[6] = 19007;
		this.stackAmounts[6] = 250;
		this.stackableIds[7] = 19008;
		this.stackAmounts[7] = 1000;
		this.stackableIds[8] = 19009;
		this.stackAmounts[8] = 10000;
		actions = new String[]{null, null, null, null, null};
	}
	
	private void transform() {
		switch(id) {
			case 19000:
				bloodMoney(995);
				break;
			case 19001:
				bloodMoney(996);
				break;
			case 19002:
				bloodMoney(997);
				break;
			case 19003:
				bloodMoney(998);
				break;
			case 19004:
				bloodMoney(999);
				break;
			case 19005:
				bloodMoney(1000);
				break;
			case 19006:
				bloodMoney(1001);
				break;
			case 19007:
				bloodMoney(1002);
				break;
			case 19008:
				bloodMoney(1003);
				break;
			case 19009:
				bloodMoney(1004);
				break;
			case 15100:
			case 15086:
			case 15088:
			case 15090:
			case 15092:
			case 15094:
			case 15096:
			case 15098:
				actions = new String[]{"Private-roll", "Clanchat-roll", null, "Put-away", null};
				break;
			case 7478:
				this.name = "Edge Tokens";
				this.description = "It's an edge token.";
				this.stackable = true;
				actions = new String[]{null, null, null, null, null};
				break;
			case 19010:
				originalModelColors = new int[1];
				modifiedModelColors = new int[1];
				originalModelColors[0] = 933;
				modifiedModelColors[0] = 6020;
				ObjectType hat = ObjectType.get(1050);
				actions = hat.actions;
				modelId = hat.modelId;
				iconZoom = hat.iconZoom;
				iconHorizontalOffset = hat.iconHorizontalOffset;
				iconVerticalOffset = hat.iconVerticalOffset;
				iconRoll = hat.iconRoll;
				iconYaw = hat.iconYaw;
				femaleEquip = hat.femaleEquip;
				femaleEquipAlt = hat.femaleEquipAlt;
				femaleEquipOffset = hat.femaleEquipOffset;
				womanEquipOffsetX = hat.womanEquipOffsetX;
				womanEquipOffsetY = hat.womanEquipOffsetY;
				womanEquipOffsetZ = hat.womanEquipOffsetZ;
				maleEquip = hat.maleEquip;
				maleEquipAlt = hat.maleEquipAlt;
				maleEquipOffset = hat.maleEquipOffset;
				maleEquipOffsetX = hat.maleEquipOffsetX;
				maleEquipOffsetY = hat.maleEquipOffsetY;
				maleEquipOffsetZ = hat.maleEquipOffsetZ;
				name = "Black santa hat";
				description = "It's a Black Santa hat.";
				break;
			case 19011:
				actions = null;
				noteTemplateId = 799;
				noteId = 19010;
				break;
			case 19012:
				originalModelColors = new int[1];
				modifiedModelColors = new int[1];
				originalModelColors[0] = 926;
				modifiedModelColors[0] = 6020;
				ObjectType party = ObjectType.get(1048);
				actions = party.actions;
				modelId = party.modelId;
				iconZoom = party.iconZoom;
				iconHorizontalOffset = party.iconHorizontalOffset;
				iconVerticalOffset = party.iconVerticalOffset;
				iconRoll = party.iconRoll;
				iconYaw = party.iconYaw;
				femaleEquip = party.femaleEquip;
				femaleEquipAlt = party.femaleEquipAlt;
				femaleEquipOffset = party.femaleEquipOffset;
				womanEquipOffsetX = party.womanEquipOffsetX;
				womanEquipOffsetY = party.womanEquipOffsetY;
				womanEquipOffsetZ = party.womanEquipOffsetZ;
				maleEquip = party.maleEquip;
				maleEquipAlt = party.maleEquipAlt;
				maleEquipOffset = party.maleEquipOffset;
				maleEquipOffsetX = party.maleEquipOffsetX;
				maleEquipOffsetY = party.maleEquipOffsetY;
				maleEquipOffsetZ = party.maleEquipOffsetZ;
				name = "Black Partyhat";
				description = "It's a Black Partyhat.";
				break;
			case 19013:
				actions = null;
				this.noteTemplateId = 799;
				this.noteId = 19012;
				break;
			case 19014:
				originalModelColors = new int[1];
				modifiedModelColors = new int[1];
				originalModelColors[0] = 926;
				modifiedModelColors[0] = 6020;
				ObjectType hween = ObjectType.get(1053);
				actions = hween.actions;
				modelId = hween.modelId;
				iconZoom = hween.iconZoom;
				iconHorizontalOffset = hween.iconHorizontalOffset;
				iconVerticalOffset = hween.iconVerticalOffset;
				iconRoll = hween.iconRoll;
				iconYaw = hween.iconYaw;
				femaleEquip = hween.femaleEquip;
				femaleEquipAlt = hween.femaleEquipAlt;
				femaleEquipOffset = hween.femaleEquipOffset;
				womanEquipOffsetX = hween.womanEquipOffsetX;
				womanEquipOffsetY = hween.womanEquipOffsetY;
				womanEquipOffsetZ = hween.womanEquipOffsetZ;
				maleEquip = hween.maleEquip;
				maleEquipAlt = hween.maleEquipAlt;
				maleEquipOffset = hween.maleEquipOffset;
				maleEquipOffsetX = hween.maleEquipOffsetX;
				maleEquipOffsetY = hween.maleEquipOffsetY;
				maleEquipOffsetZ = hween.maleEquipOffsetZ;
				name = "Black h'ween mask";
				description = "It's a Black h'ween mask.";
				break;
			case 19015:
				actions = null;
				noteTemplateId = 799;
				noteId = 19014;
				break;
			case 20763:
			case 21371:
			case 21372:
			case 21373:
			case 21374:
			case 21375:
			case 20769:
			case 10548:
			case 6918:
			case 4109:
			case 4099:
			case 7400:
				
				fixPriority = true;
				//break;
			case 21462:
			case 21463:
			case 21464:
			case 21465:
			case 21466:
			case 21467:
			case 21468:
			case 21469:
			case 21470:
			case 21471:
			case 21472:
			case 21473:
			case 21474:
			case 21475:
			case 21476:
				actions = new String[5];
				actions[1] = "Wear";
				actions[4] = "Drop";
				break;
			case 7454:
				name = "Bronze gloves";
				break;
			case 7455:
				name = "Iron gloves";
				break;
			case 7456:
				name = "Steel gloves";
				break;
			case 7457:
				name = "Black gloves";
				break;
			case 7458:
				name = "Mithril gloves";
				break;
			case 7459:
				name = "Adamant gloves";
				break;
			case 7460:
				name = "Rune gloves";
				break;
			case 7461:
				name = "Dragon gloves";
				break;
			case 7462:
				name = "Barrows gloves";
				break;
			case 19111:
				name = "TokHaar-Kal";
				value = 60000;
				maleEquip = 62575;
				femaleEquip = 62582;
				groundActions = new String[5];
				groundActions[2] = "Take";
				iconHorizontalOffset = -4;
				modelId = 62592;
				stackable = false;
				description = "A cape made of ancient, enchanted rocks.";
				iconZoom = 2086;
				actions = new String[5];
				actions[1] = "Wear";
				actions[4] = "Drop";
				iconVerticalOffset = 0;
				iconYaw = 533;
				iconRoll = 333;
				fixPriority = true;
				break;
			case 14207:
				flask("Empty", -1, 0);
				break;
			case 14200:
				flask("Prayer", 28488, 6);
				break;
			case 14198:
				flask("Prayer", 28488, 5);
				break;
			case 14196:
				flask("Prayer", 28488, 4);
				break;
			case 14194:
				flask("Prayer", 28488, 3);
				break;
			case 14192:
				flask("Prayer", 28488, 2);
				break;
			case 14190:
				flask("Prayer", 28488, 1);
				break;
			case 14188:
				flask("Super attack", 43848, 6);
				break;
			case 14186:
				flask("Super attack", 43848, 5);
				break;
			case 14184:
				flask("Super attack", 43848, 4);
				break;
			case 14182:
				flask("Super attack", 43848, 3);
				break;
			case 14180:
				flask("Super attack", 43848, 2);
				break;
			case 14178:
				flask("Super attack", 43848, 1);
				break;
			case 14176:
				flask("Super strength", 119, 6);
				break;
			case 14174:
				flask("Super strength", 119, 5);
				break;
			case 14172:
				flask("Super strength", 119, 4);
				break;
			case 14170:
				flask("Super strength", 119, 3);
				break;
			case 14168:
				flask("Super strength", 119, 2);
				break;
			case 14166:
				flask("Super strength", 119, 1);
				break;
			case 14164:
				flask("Super defence", 8008, 6);
				break;
			case 14162:
				flask("Super defence", 8008, 5);
				break;
			case 14160:
				flask("Super defence", 8008, 4);
				break;
			case 14158:
				flask("Super defence", 8008, 3);
				break;
			case 14156:
				flask("Super defence", 8008, 2);
				break;
			case 14154:
				flask("Super defence", 8008, 1);
				break;
			case 14152:
				flask("Ranging", 36680, 6);
				break;
			case 14150:
				flask("Ranging", 36680, 5);
				break;
			case 14148:
				flask("Ranging", 36680, 4);
				break;
			case 14146:
				flask("Ranging", 36680, 3);
				break;
			case 14144:
				flask("Ranging", 36680, 2);
				break;
			case 14142:
				flask("Ranging", 36680, 1);
				break;
			case 14140:
				flask("Super antipoison", 62404, 6);
				break;
			case 14138:
				flask("Super antipoison", 62404, 5);
				break;
			case 14136:
				flask("Super antipoison", 62404, 4);
				break;
			case 14134:
				flask("Super antipoison", 62404, 3);
				break;
			case 14132:
				flask("Super antipoison", 62404, 2);
				break;
			case 14130:
				flask("Super antipoison", 62404, 1);
				break;
			case 14128:
				flask("Saradomin brew", 10939, 6);
				break;
			case 14126:
				flask("Saradomin brew", 10939, 5);
				break;
			case 14124:
				flask("Saradomin brew", 10939, 4);
				break;
			case 14122:
				flask("Saradomin brew", 10939, 3);
				break;
			case 14419:
				flask("Saradomin brew", 10939, 2);
				break;
			case 14417:
				flask("Saradomin brew", 10939, 1);
				break;
			case 14415:
				flask("Super restore", 62135, 6);
				break;
			case 14413:
				flask("Super restore", 62135, 5);
				break;
			case 14411:
				flask("Super restore", 62135, 4);
				break;
			case 14409:
				flask("Super restore", 62135, 3);
				break;
			case 14407:
				flask("Super restore", 62135, 2);
				break;
			case 14405:
				flask("Super restore", 62135, 1);
				break;
			case 14403:
				flask("Magic", 37440, 6);
				break;
			case 14401:
				flask("Magic", 37440, 5);
				break;
			case 14399:
				flask("Magic", 37440, 4);
				break;
			case 14397:
				flask("Magic", 37440, 3);
				break;
			case 14395:
				flask("Magic", 37440, 2);
				break;
			case 14393:
				flask("Magic", 37440, 1);
				break;
			case 14385:
				flask("Recover special", 38222, 6);
				break;
			case 14383:
				flask("Recover special", 38222, 5);
				break;
			case 14381:
				flask("Recover special", 38222, 4);
				break;
			case 14379:
				flask("Recover special", 38222, 3);
				break;
			case 14377:
				flask("Recover special", 38222, 2);
				break;
			case 14375:
				flask("Recover special", 38222, 1);
				break;
			case 14373:
				flask("Extreme attack", 33112, 6);
				break;
			case 14371:
				flask("Extreme attack", 33112, 5);
				break;
			case 14369:
				flask("Extreme attack", 33112, 4);
				break;
			case 14367:
				flask("Extreme attack", 33112, 3);
				break;
			case 14365:
				flask("Extreme attack", 33112, 2);
				break;
			case 14363:
				flask("Extreme attack", 33112, 1);
				break;
			case 14361:
				flask("Extreme strength", 127, 6);
				break;
			case 14359:
				flask("Extreme strength", 127, 5);
				break;
			case 14357:
				flask("Extreme strength", 127, 4);
				break;
			case 14355:
				flask("Extreme strength", 127, 3);
				break;
			case 14353:
				flask("Extreme strength", 127, 2);
				break;
			case 14351:
				flask("Extreme strength", 127, 1);
				break;
			case 14349:
				flask("Extreme defence", 10198, 6);
				break;
			case 14347:
				flask("Extreme defence", 10198, 5);
				break;
			case 14345:
				flask("Extreme defence", 10198, 4);
				break;
			case 14343:
				flask("Extreme defence", 10198, 3);
				break;
			case 14341:
				flask("Extreme defence", 10198, 2);
				break;
			case 14339:
				flask("Extreme defence", 10198, 1);
				break;
			case 14337:
				flask("Extreme magic", 33490, 6);
				break;
			case 14335:
				flask("Extreme magic", 33490, 5);
				break;
			case 14333:
				flask("Extreme magic", 33490, 4);
				break;
			case 14331:
				flask("Extreme magic", 33490, 3);
				break;
			case 14329:
				flask("Extreme magic", 33490, 2);
				break;
			case 14327:
				flask("Extreme magic", 33490, 1);
				break;
			case 14325:
				flask("Extreme ranging", 13111, 6);
				break;
			case 14323:
				flask("Extreme ranging", 13111, 5);
				break;
			case 14321:
				flask("Extreme ranging", 13111, 4);
				break;
			case 14319:
				flask("Extreme ranging", 13111, 3);
				break;
			case 14317:
				flask("Extreme ranging", 13111, 2);
				break;
			case 14315:
				flask("Extreme ranging", 13111, 1);
				break;
			case 14313:
				flask("Super prayer", 3016, 6);
				break;
			case 14311:
				flask("Super prayer", 3016, 5);
				break;
			case 14309:
				flask("Super prayer", 3016, 4);
				break;
			case 14307:
				flask("Super prayer", 3016, 3);
				break;
			case 14305:
				flask("Super prayer", 3016, 2);
				break;
			case 14303:
				flask("Super prayer", 3016, 1);
				break;
			case 14301:
				flask("Overload", 0, 6);
				break;
			case 14299:
				flask("Overload", 0, 5);
				break;
			case 14297:
				flask("Overload", 0, 4);
				break;
			case 14295:
				flask("Overload", 0, 3);
				break;
			case 14293:
				flask("Overload", 0, 2);
				break;
			case 14291:
				flask("Overload", 0, 1);
				break;
			case 14289:
				flask("Prayer renewal", 926, 6);
				break;
			case 14287:
				flask("Prayer renewal", 926, 5);
				break;
			case 15123:
				flask("Prayer renewal", 926, 4);
				break;
			case 15121:
				flask("Prayer renewal", 926, 3);
				break;
			case 15119:
				flask("Prayer renewal", 926, 2);
				break;
			case 15115:
				flask("Prayer renewal", 926, 1);
				break;
		}
	}
	
}
