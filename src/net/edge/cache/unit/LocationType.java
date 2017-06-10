package net.edge.cache.unit;

import net.edge.Config;
import net.edge.cache.CacheArchive;
import net.edge.game.model.Model;
import net.edge.od.OnDemandFetcher;
import net.edge.util.collect.HashLruCache;
import net.edge.util.io.Buffer;
import net.edge.Client;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public final class LocationType {

	public static Client client;

	private static int pos;
	private static Buffer data;
	private static LocationType[] cache;
	private static int[] index;

	private static final Model[] models = new Model[4];

	public static HashLruCache animatedModelCache = new HashLruCache(30);
	public static HashLruCache modelCache = new HashLruCache(500);

	public boolean aBoolean736;
	private int ambient;
	private int translateX;
	public String name;
	private int scaleZ;
	private int diffuse;
	public int sizeX;
	private int translateY;
	public int mapFunction;
	private int scaleX;
	public int anInt749;
	private boolean rotated;
	public int id;
	public boolean walkable;
	public int mapScene;
	public int childIds[];
	private int _solid;
	public int sizeY;
	public boolean adjustToTerrain;
	public boolean wall;
	private boolean unwalkableSolid;
	public boolean solid;
	public int face;
	private boolean delayShading;
	private int scaleY;
	public int[] modelIds;
	public int varBitId;
	public int offsetAmplifier;
	private int[] modelTypes;
	public String description;
	public boolean hasActions;
	public boolean castsShadow;
	public int animationId;
	private int translateZ;
	public String[] actions;
	private int[] originalModelColors;
	private int[] modifiedModelColors;
	private short[] modifiedModelTextures;
	private short[] originalModelTextures;
	private boolean old;

	private LocationType() {
		id = -1;
	}

	public static LocationType getRelative(int id) {
		if(id >= 42003)
			id += 42003;//530 offset for 667 objects.
		return getPrecise(id);
	}
	
	public static LocationType getSpecific(int id, boolean old) {
		if(!old)
			id += 42003;//667 objects.
		else if(id >= 42003)
			id += 42003;//530 offset for 667 objects.
		LocationType loc = getPrecise(id);
		loc.old = old;
		return loc;
	}

	public static LocationType getPrecise(int id) {
		if(id == 2563) {
			id += 42003;//max cape
		}
		if(id > index.length)
			id = index.length - 1;

		for(int i = 0; i < 20; i++) {
			if(cache[i].id == id)
				return cache[i];
		}

		pos = (pos + 1) % 20;
		LocationType loc = cache[pos];
		data.pos = index[id];

		loc.id = id;
		loc.renew();
		loc.read(data);
		if(id == 28139)
			loc.name = "Edge Portal";
		if(id == 24124)
			loc.actions = new String[]{"Access", null, null, null, null};
		if(id == 38828 || id == 38827)
			loc.actions = new String[]{"Add-Logs", null, "Check", null, null};
		if(id == 38817 || id == 38818 || id == 38819 || id == 38820 || id == 38821)
			loc.actions = new String[]{"Add-Logs", null, null, null, null};
		if(loc.id == 6069)
			loc.actions = new String[] { "Grab", null, null, null, null };
		if(loc.id == 4709)
			loc.mapFunction = 89;
		if(loc.id == 9678)
			loc.mapFunction = 78;

		if(loc.id == id && loc.originalModelColors == null) {
			loc.originalModelColors = new int[1];
			loc.modifiedModelColors = new int[1];
			loc.originalModelColors[0] = 0;
			loc.modifiedModelColors[0] = 1;
		}
		return loc;
	}


	public static void reset() {
		modelCache = null;
		animatedModelCache = null;
		index = null;
		cache = null;
		data = null;
	}

	public static void unpack(CacheArchive archive) {
		data = new Buffer(archive.getFile("loc.dat"));
		final Buffer idx = new Buffer(archive.getFile("loc.idx"));

		final int length = idx.getInt();
		System.out.println("[loading] loc size: " + length);
		index = new int[length];

		int pos = 4;
		for(int j = 0; j < length; j++) {
			index[j] = pos;
			pos += idx.getUShort();
		}

		cache = new LocationType[20];
		for(int i = 0; i < 20; i++) {
			cache[i] = new LocationType();
		}
	}

	public static void pack() throws IOException {
		int size = index.length;
		DataOutputStream dat = new DataOutputStream(new FileOutputStream("loc.dat"));
		DataOutputStream idx = new DataOutputStream(new FileOutputStream("loc.idx"));
		idx.writeInt(size);
		dat.writeInt(size);
		for(int i = 0; i < size; i++) {
			LocationType obj = getPrecise(i);
			int offset1 = dat.size();
			obj.write(dat);
			int offset2 = dat.size();
			int writeOffset = offset2 - offset1;
			idx.writeShort(writeOffset);
			System.out.println("writted old: " + i + " - offset: " + writeOffset);
		}
		dat.close();
		idx.close();
	}

	private void read(Buffer buffer) {
		int i = -1;
		int opcode;
		do {
			opcode = buffer.getUByte();
			if(opcode == 0)
				return;
			if(opcode == 1) {
				int k = buffer.getUByte();
				if(k > 0)
					if(modelIds == null) {
						modelTypes = new int[k];
						modelIds = new int[k];
						for(int k1 = 0; k1 < k; k1++) {
							modelIds[k1] = buffer.getUShort();
							modelTypes[k1] = buffer.getUByte();
						}
					}
			} else if(opcode == 2)
				name = buffer.getLine();
			else if(opcode == 3)
				description = buffer.getLine();
			else if(opcode == 4) {
				int l = buffer.getUByte();
				if(l > 0)
					if(modelIds == null) {
						modelTypes = null;
						modelIds = new int[l];
						for(int l1 = 0; l1 < l; l1++)
							modelIds[l1] = buffer.getUShort();
					}
			} else if(opcode == 5)
				sizeX = buffer.getUByte();
			else if(opcode == 6)
				sizeY = buffer.getUByte();
			else if(opcode == 7)
				solid = false;
			else if(opcode == 8)
				walkable = false;
			else if(opcode == 9) {
				i = buffer.getUByte();
				if(i == 1)
					hasActions = true;
			} else if(opcode == 10)
				adjustToTerrain = true;
			else if(opcode == 11)
				delayShading = false;
			else if(opcode == 12)
				wall = true;
			else if(opcode == 13) {
				animationId = buffer.getUShort();
				if(animationId == 65535)
					animationId = -1;
			} else if(opcode == 14)
				offsetAmplifier = buffer.getUByte();
			else if(opcode == 15)
				ambient = buffer.getUByte();
			else if(opcode == 16)
				diffuse = buffer.getUByte();
			else if(opcode >= 40 && opcode < 50) {
				if(actions == null)
					actions = new String[10];
				actions[opcode - 40] = buffer.getLine();
				if(actions[opcode - 40].equalsIgnoreCase("hidden"))
					actions[opcode - 40] = null;
			} else if(opcode == 17) {
				final int len = buffer.getUByte();
				originalModelColors = new int[len];
				modifiedModelColors = new int[len];
				for(int i2 = 0; i2 < len; i2++) {
					originalModelColors[i2] = buffer.getUShort();
					modifiedModelColors[i2] = buffer.getUShort();
				}
			} else if(opcode == 18) {
				final int len = buffer.getUByte();
				originalModelTextures = new short[len];
				modifiedModelTextures = new short[len];
				for(int i3 = 0; i3 < len; i3++) {
					originalModelTextures[i3] = (short) buffer.getUShort();
					modifiedModelTextures[i3] = (short) buffer.getUShort();
				}
			} else if(opcode == 19)
				mapFunction = buffer.getUShort();
			else if(opcode == 20)
				rotated = true;
			else if(opcode == 21)
				castsShadow = false;
			else if(opcode == 22)
				scaleX = buffer.getUShort();
			else if(opcode == 23)
				scaleY = buffer.getUShort();
			else if(opcode == 24)
				scaleZ = buffer.getUShort();
			else if(opcode == 25)
				mapScene = buffer.getUShort();
			else if(opcode == 26)
				face = buffer.getUByte();
			else if(opcode == 27)
				translateX = buffer.getUShort();
			else if(opcode == 28)
				translateY = buffer.getUShort();
			else if(opcode == 29)
				translateZ = buffer.getUShort();
			else if(opcode == 30)
				aBoolean736 = true;
			else if(opcode == 31)
				unwalkableSolid = true;
			else if(opcode == 32)
				_solid = buffer.getUByte();
			else if(opcode == 33) {
				varBitId = buffer.getUShort();
				if(varBitId == 65535) {
					varBitId = -1;
				}
				anInt749 = buffer.getUShort();
				if(anInt749 == 65535) {
					anInt749 = -1;
				}
				final int childCount = buffer.getUByte();
				this.childIds = new int[childCount];
				for(int c = 0; c < childCount; c++) {
					this.childIds[c] = buffer.getInt();
					if(this.childIds[c] == 65535) {
						this.childIds[c] = -1;
					}
				}
			} else {
				System.out.println("[ObjectDef " + id + "] Unknown opcode: " + opcode + ".");
				break;
			}
		} while(true);
		if(i == -1) {
			hasActions = modelIds != null && (modelTypes == null || modelTypes[0] == 10);
			if(actions != null)
				hasActions = true;
		}
		if(unwalkableSolid) {
			solid = false;
			walkable = false;
		}
		if(_solid == -1)
			_solid = solid ? 1 : 0;
	}

	private void write(DataOutputStream out) throws IOException {
		boolean actionsd = false;
		Set<Integer> written = new HashSet<>();
		do {
			if(modelIds != null && modelTypes != null && !written.contains(1)) {
				out.writeByte(1);
				out.writeByte(modelIds.length);
				if(modelIds.length > 0) {
					for(int k1 = 0; k1 < modelIds.length; k1++) {
						out.writeShort(modelIds[k1]);
						out.writeByte(modelTypes[k1]);
					}
				}
				written.add(1);
			} else if(name != null && !written.contains(2)) {
				out.writeByte(2);
				out.write(name.getBytes());
				out.writeByte(10);
				written.add(2);
			} else if(description != null && !written.contains(3)) {
				out.writeByte(3);
				out.write(description.getBytes());
				out.writeByte(10);
				written.add(3);
			} else if(modelIds != null && modelTypes == null && !written.contains(4)) {
				out.writeByte(4);
				out.writeByte(modelIds.length);
				if(modelIds.length > 0) {
					for(int anAnIntArray773 : modelIds) {
						out.writeShort(anAnIntArray773);
					}
				}
				written.add(4);
			} else if(sizeX != 1 && !written.contains(5)) {
				out.writeByte(5);
				out.writeByte(sizeX);
				written.add(5);
			} else if(sizeY != 1 && !written.contains(6)) {
				out.writeByte(6);
				out.writeByte(sizeY);
				written.add(6);
			} else if(!solid && !written.contains(7)) {
				out.writeByte(7);
				written.add(7);
			} else if(!walkable && !written.contains(8)) {
				out.writeByte(8);
				written.add(8);
			} else if(hasActions && !written.contains(9)) {
				out.writeByte(9);
				out.writeByte(hasActions ? 1 : 0);
				written.add(9);
			} else if(adjustToTerrain && !written.contains(10)) {
				out.writeByte(10);
				written.add(10);
			} else if(!delayShading && !written.contains(11)) {
				out.writeByte(11);
				written.add(11);
			} else if(wall && !written.contains(12)) {
				out.writeByte(12);
				written.add(12);
			} else if(animationId != -1 && !written.contains(13)) {
				out.writeByte(13);
				if(animationId == -1)
					animationId = 65535;
				out.writeShort(animationId);
				written.add(13);
			} else if(offsetAmplifier != 16 && !written.contains(14)) {
				out.writeByte(14);
				out.writeByte(offsetAmplifier);
				written.add(14);
			} else if(ambient != 0 && !written.contains(15)) {
				out.writeByte(15);
				out.writeByte(ambient);
				written.add(15);
			} else if(diffuse != 0 && !written.contains(16)) {
				out.writeByte(16);
				out.writeByte(diffuse);
				written.add(16);
			} else if(originalModelColors != null && !written.contains(17)) {
				out.writeByte(17);
				out.writeByte(originalModelColors.length);
				for(int i = 0; i < originalModelColors.length; i++) {
					out.writeShort(originalModelColors[i]);
					out.writeShort(modifiedModelColors[i]);
				}
				written.add(17);
			} else if(originalModelTextures != null && !written.contains(18)) {
				out.writeByte(18);
				out.writeByte(originalModelTextures.length);
				for(int i = 0; i < originalModelTextures.length; i++) {
					out.writeShort(originalModelTextures[i]);
					out.writeShort(modifiedModelTextures[i]);
				}
				written.add(18);
			} else if(mapFunction != -1 && !written.contains(19)) {
				out.writeByte(19);
				out.writeShort(mapFunction);
				written.add(19);
			} else if(rotated && !written.contains(20)) {
				out.writeByte(20);
				written.add(20);
			} else if(!castsShadow && !written.contains(21)) {
				out.writeByte(21);
				written.add(21);
			} else if(scaleX != 128 && !written.contains(22)) {
				out.writeByte(22);
				out.writeShort(scaleX);
				written.add(22);
			} else if(scaleY != 128 && !written.contains(23)) {
				out.writeByte(23);
				out.writeShort(scaleY);
				written.add(23);
			} else if(scaleZ != 128 && !written.contains(24)) {
				out.writeByte(24);
				out.writeShort(scaleZ);
				written.add(24);
			} else if(mapScene != -1 && !written.contains(25)) {
				out.writeByte(25);
				out.writeShort(mapScene);
				written.add(25);
			} else if(face != 0 && !written.contains(26)) {
				out.writeByte(26);
				out.writeByte(face);
				written.add(26);
			} else if(translateX != 0 && !written.contains(27)) {
				out.writeByte(27);
				out.writeShort(translateX);
				written.add(27);
			} else if(translateY != 0 && !written.contains(28)) {
				out.writeByte(28);
				out.writeShort(translateY);
				written.add(28);
			} else if(translateZ != 0 && !written.contains(29)) {
				out.writeByte(29);
				out.writeShort(translateZ);
				written.add(29);
			} else if(aBoolean736 && !written.contains(30)) {
				out.writeByte(30);
				written.add(30);
			} else if(unwalkableSolid && !written.contains(31)) {
				out.writeByte(31);
				written.add(31);
			} else if(_solid != -1 && !written.contains(32)) {
				out.writeByte(32);
				out.writeByte(_solid);
				written.add(32);
			} else if(childIds != null && !written.contains(33)) {
				out.writeByte(33);
				if(varBitId == -1) {
					varBitId = 0xffff;
				}
				out.writeShort(varBitId);
				if(anInt749 == -1) {
					anInt749 = 0xffff;
				}
				out.writeShort(anInt749);
				out.writeByte(childIds.length);
				for(int c : childIds) {
					out.writeShort(c);
				}
				written.add(33);
			} else if(actions != null && !actionsd) {
				for(int i = 0; i < actions.length; i++) {
					if(actions[i] != null) {
						out.writeByte(40 + i);
						out.write(actions[i].getBytes());
						out.writeByte(10);
					}
				}
				actionsd = true;
			} else {
				out.writeByte(0);
				break;
			}
		} while(true);
	}

	public void requestModels(OnDemandFetcher fetcher, LocationType loc) {
		if(modelIds == null)
			return;
		for(final int model : modelIds) {
			fetcher.passiveRequest(model & 0xffff, loc.id > 42003 ? 0 : 6);
		}
	}

	public boolean modelTypeCached(int modelType) {
		if(modelTypes == null) {
			if(modelIds == null) {
				return true;
			}
			if(modelType != 10) {
				return true;
			}
			boolean cached = true;
			for(final int element : modelIds) {
				cached &= Model.isCached(element & 0xffff, id > 42003 ? 0 : 6);
			}
			return cached;
		}
		for(int type = 0; type < modelTypes.length; type++) {
			if(modelTypes[type] == modelType) {
				return Model.isCached(modelIds[type] & 0xffff, id > 42003 ? 0 : 6);
			}
		}
		return true;
	}

	public Model getModelAt(int i, int j, int k, int l, int i1, int j1, int currAnim, int nextAnim, int end, int cycle) {
		Model model = getAnimatedModel(i, currAnim, nextAnim, end, cycle, j);
		if(model == null) {
			return null;
		}
		if(adjustToTerrain || delayShading) {
			model = new Model(adjustToTerrain, delayShading, model);
		}
		if(adjustToTerrain) {
			final int l1 = (k + l + i1 + j1) / 4;
			for(int i2 = 0; i2 < model.vertexAmt; i2++) {
				final int x = model.vertexX[i2] >> (model.upscaled ? 2 : 0);
				final int z = model.vertexZ[i2] >> (model.upscaled ? 2 : 0);
				final int l2 = k + (l - k) * (x + 64) / 128;
				final int i3 = j1 + (i1 - j1) * (x + 64) / 128;
				final int j3 = l2 + (i3 - l2) * (z + 64) / 128;
				model.vertexY[i2] += j3 - l1 << (model.upscaled ? 2 : 0);
			}
			model.computeBoundsVertical();
		}
		return model;
	}

	public boolean modelCached() {
		if(modelIds == null)
			return true;
		boolean cached = true;
		for(final int element : modelIds) {
			cached &= Model.isCached(element & 0xffff, id > 42003 ? 0 : 6);
		}
		return cached;
	}

	public LocationType getChild() {
		int i = -1;
		if(varBitId != -1) {
			final VaryingBit varBit = VaryingBit.cache[varBitId];
			final int configId = varBit.configId;
			final int lsb = varBit.leastSignificantBit;
			final int msb = varBit.mostSignificantBit;
			final int bit = Client.BIT_MASK[msb - lsb];
			i = client.variousSettings[configId] >> lsb & bit;
		} else if(anInt749 != -1) {
			i = client.variousSettings[anInt749];
		}
		if(i < 0 || i >= childIds.length || childIds[i] == -1) {
			return null;
		} else {
			return getPrecise(childIds[i] + (!old ? 42003 : 0));
		}
	}

	private Model getAnimatedModel(int j, int currAnim, int nextAnim, int end, int cycle, int l) {
		Model model = null;
		long hash;
		if(modelTypes == null) {
			if(j != 10) {
				return null;
			}
			hash = (id << 6) + l + ((long) (currAnim + 1) << 32);
			final Model cachedModel = (Model) animatedModelCache.get(hash);
			if(cachedModel != null) {
				return cachedModel;
			}
			if(modelIds == null) {
				return null;
			}
			final boolean mirror = rotated ^ l > 3;
			final int modelCount = modelIds.length;
			for(int i2 = 0; i2 < modelCount; i2++) {
				int l2 = modelIds[i2];
				if(mirror) {
					l2 += 0x10000;
				}
				model = (Model) modelCache.get(l2);
				if(model == null) {
					model = Model.get(l2 & 0xffff, id > 42003 ? 0 : 6);
					if(model == null) {
						return null;
					}
					if(mirror) {
						model.insideOut();
					}
					modelCache.put(l2, model);
				}
				if(modelCount > 1) {
					models[i2] = model;
				}
			}

			if(modelCount > 1) {
				model = new Model(modelCount, models);
			}
		} else {
			int i1 = -1;
			for(int j1 = 0; j1 < modelTypes.length; j1++) {
				if(modelTypes[j1] != j) {
					continue;
				}
				i1 = j1;
				break;
			}

			if(i1 == -1) {
				return null;
			}
			hash = (long) ((id << 8) + (i1 << 3) + l) + ((long) (currAnim + 1) << 32);
			final Model model_2 = (Model) animatedModelCache.get(hash);
			if(model_2 != null) {
				return model_2;
			}
			int modelId = modelIds[i1];
			final boolean mirror = rotated ^ l > 3;
			if(mirror) {
				modelId += 0x10000;
			}
			model = (Model) modelCache.get(modelId);
			if(model == null) {
				model = Model.get(modelId & 0xffff, id > 42003 ? 0 : 6);
				if(model == null) {
					return null;
				}
				if(mirror) {
					model.insideOut();
				}
				modelCache.put(modelId, model);
			}
		}
		boolean scale;
		scale = scaleX != 128 || scaleY != 128 || scaleZ != 128;
		boolean translate = translateX != 0 || translateY != 0 || translateZ != 0;
		final Model animatedModel = new Model(modifiedModelColors == null, AnimationFrame.isNull(currAnim), l == 0 && currAnim == -1 && !scale && !translate, model);
		if(currAnim != -1) {
			animatedModel.applyEffects();
			if(Config.TWEENING.isOn()) {
				animatedModel.applyAnimation(currAnim, nextAnim, cycle, end);
			} else {
				animatedModel.applyAnimation(currAnim);
			}
			animatedModel.triangleSkin = null;
			animatedModel.anIntArrayArray1657 = null;
		}
		while(l-- > 0) {
			animatedModel.rotate90();
		}
		if(originalModelTextures != null) {
			for(int i_53_ = 0; i_53_ < originalModelTextures.length; i_53_++) {
				animatedModel.setTexture(originalModelTextures[i_53_], modifiedModelTextures[i_53_]);
			}
		}

		if(originalModelColors != null) {
			for(int k2 = 0; k2 < originalModelColors.length; k2++) {
				animatedModel.replaceHsl(originalModelColors[k2], modifiedModelColors[k2]);
			}
		}

		if(scale) {
			animatedModel.scale(scaleX, scaleY, scaleZ);
		}
		if(translate) {
			animatedModel.translate(translateX, translateY, translateZ);
		}
		animatedModel.calculateLighting(84/*64*/ + ambient, 1280/*768*/ + diffuse * 5, -50, -10, -50, !delayShading);
		if(_solid == 1) {
			animatedModel.anInt1654 = animatedModel.maxVerticalDistUp;
		}
		animatedModelCache.put(hash, animatedModel);
		return animatedModel;
	}

	private void renew() {
		modelIds = null;
		modelTypes = null;
		name = null;
		description = null;
		modifiedModelColors = null;
		originalModelColors = null;
		sizeX = 1;
		sizeY = 1;
		solid = true;
		walkable = true;
		hasActions = false;
		adjustToTerrain = false;
		delayShading = false;
		wall = false;
		animationId = -1;
		offsetAmplifier = 16;
		ambient = 0;
		diffuse = 0;
		actions = null;
		mapFunction = -1;
		mapScene = -1;
		rotated = false;
		castsShadow = true;
		scaleX = 128;
		scaleY = 128;
		scaleZ = 128;
		face = 0;
		translateX = 0;
		translateY = 0;
		translateZ = 0;
		aBoolean736 = false;
		unwalkableSolid = false;
		_solid = -1;
		varBitId = -1;
		anInt749 = -1;
		childIds = null;
	}
}
