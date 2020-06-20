package net.arrav.world.model;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.arrav.Client;
import net.arrav.Config;
import net.arrav.cache.unit.*;
import net.arrav.cache.unit.interfaces.Interface;
import net.arrav.graphic.Rasterizer3D;
import net.arrav.util.io.Buffer;
import net.arrav.util.string.StringUtils;

public final class Player extends Mobile {

	public static Client client;
	public int privelege;
	private long aLong1697;
	public NPCType desc;
	public boolean noTransform;
	public final int[] characterParts;
	public int team;
	private int gender;
	public String name;
	public static Long2ObjectOpenHashMap<Model> modelcache = new Long2ObjectOpenHashMap<>();
	public int combatLevel;
	public int headIcon;
	public int skullIcon;
	public int hintIcon;
	public int anInt1707;
	public int anInt1708;
	public int anInt1709;
	public boolean visible;
	public int anInt1711;
	public int anInt1712;
	public int anInt1713;
	public Model aModel_1714;
	public final int[] equipment;
	private long aLong1718;
	public int anInt1719;
	public int anInt1720;
	public int anInt1721;
	public int anInt1722;
	public int skill;
	public boolean iron;

	public Player() {
		aLong1697 = -1L;
		noTransform = false;
		characterParts = new int[5];
		visible = false;
		equipment = new int[12];
	}

	@Override
	public Model getTransformedModel() {
		if(!visible) {
			return null;
		}
		Model model = getAnimatedModel();
		if(model == null) {
			return null;
		}
		super.height = model.maxVerticalDistUp;
		model.hoverable = true;
		if(noTransform) {
			return model;
		}
		if(super.spotAnim != -1 && super.spotAnimFrame != -1) {
			if(SpotAnimation.cache.length >= super.spotAnim) {
				final SpotAnimation spotAnim = SpotAnimation.cache[super.spotAnim];
				if(AnimationFrame.ready(spotAnim.animFrameSequence.frameList[super.spotAnimFrame])) {
					final Model model_2 = spotAnim.getModel();
					if(model_2 != null) {
						final Model samodel = new Model(true, AnimationFrame.isNull(super.spotAnimFrame), false, model_2);
						samodel.translate(0, -super.spotAnimOffset, 0);
						samodel.applyEffects();
						if(Config.def.tween() && super.nextSpotAnimFrame != -1) {
							samodel.applyAnimation(spotAnim.animFrameSequence.frameList[super.spotAnimFrame], spotAnim.animFrameSequence.frameList[super.nextSpotAnimFrame], super.spotAnimCycle, spotAnim.animFrameSequence.cycleList[super.spotAnimFrame]);
						} else {
							samodel.applyAnimation(spotAnim.animFrameSequence.frameList[super.spotAnimFrame]);
						}
						samodel.triangleSkin = null;
						samodel.anIntArrayArray1657 = null;
						if(spotAnim.scaleHorizontal != 128 || spotAnim.scaleVertical != 128) {
							samodel.scale(spotAnim.scaleHorizontal, spotAnim.scaleVertical, spotAnim.scaleHorizontal);
						}
						samodel.calculateLighting(104/*64*/ + spotAnim.lightness, 1362/*850*/ + spotAnim.contrast, -30, -50, -30, true);
						final Model[] parts = {model, samodel};
						model = new Model(parts);
					}
				}
			}
		}
		if(aModel_1714 != null) {
			if(client.loopCycle >= anInt1708) {
				aModel_1714 = null;
			}
			if(client.loopCycle >= anInt1707 && client.loopCycle < anInt1708 && aModel_1714 != null) {
				final Model model_1 = aModel_1714;
				model_1.translate(anInt1711 - super.x, anInt1712 - anInt1709, anInt1713 - super.y);
				if(super.turnDirection == 512) {
					model_1.rotate90();
					model_1.rotate90();
					model_1.rotate90();
				} else if(super.turnDirection == 1024) {
					model_1.rotate90();
					model_1.rotate90();
				} else if(super.turnDirection == 1536) {
					model_1.rotate90();
				}
				final Model aclass30_sub2_sub4_sub6s[] = {model, model_1};
				model = new Model(aclass30_sub2_sub4_sub6s);
				if(super.turnDirection == 512) {
					model_1.rotate90();
				} else if(super.turnDirection == 1024) {
					model_1.rotate90();
					model_1.rotate90();
				} else if(super.turnDirection == 1536) {
					model_1.rotate90();
					model_1.rotate90();
					model_1.rotate90();
				}
				model_1.translate(super.x - anInt1711, anInt1709 - anInt1712, super.y - anInt1713);
			}
		}
		model.hoverable = true;
		return model;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	public Model getAnimatedModel() {
		int currAnim = -1;
		int nextAnim = -1;
		int currCycle = -1;
		int nextCycle = -1;
		if(desc != null) {
			if(super.anim >= 0 && super.animDelay == 0 && super.anim <= DeformSequence.cache.length) {
				final DeformSequence seq = DeformSequence.cache[super.anim];
				currAnim = seq.frameList[super.animFrame];
				if(Config.def.tween() && super.nextAnimFrame != -1) {
					nextAnim = seq.frameList[super.nextAnimFrame];
					currCycle = seq.cycleList[super.animFrame];
					nextCycle = super.animCycle;
				}
			} else if(super.idleAnim >= 0) {
				final DeformSequence seq = DeformSequence.cache[super.idleAnim];
				currAnim = seq.frameList[super.idleAnimFrame];
				if(Config.def.tween() && super.nextIdleAnimFrame != -1) {
					nextAnim = seq.frameList[super.nextIdleAnimFrame];
					currCycle = seq.cycleList[super.idleAnimFrame];
					nextCycle = super.idleAnimCycle;
				}
			}
			return desc.getModel(-1, currAnim, nextAnim, currCycle, nextCycle, null);
		}
		long l = aLong1718;
		int i1 = -1;
		int j1 = -1;
		int k1 = -1;
		if(super.anim >= 0 && super.animDelay == 0) {
			final DeformSequence animation = DeformSequence.cache[super.anim];
			currAnim = animation.frameList[super.animFrame];
			if(Config.def.tween() && super.nextAnimFrame != -1) {
				nextAnim = animation.frameList[super.nextAnimFrame];
				currCycle = animation.cycleList[super.animFrame];
				nextCycle = super.animCycle;
			}
			if(super.idleAnim >= 0 && super.idleAnim != super.anInt1511) {
				i1 = DeformSequence.cache[super.idleAnim].frameList[super.idleAnimFrame];
			}
			if(animation.leftHandItem >= 0) {
				j1 = animation.leftHandItem;
				l += j1 - equipment[5] << 8;
			}
			if(animation.rightHandITem >= 0) {
				k1 = animation.rightHandITem;
				l += k1 - equipment[3] << 16;

			}
		} else if(super.idleAnim >= 0) {
			DeformSequence seq = DeformSequence.cache[super.idleAnim];
			currAnim = seq.frameList[super.idleAnimFrame];
			if(Config.def.tween() && super.nextIdleAnimFrame != -1) {
				nextAnim = seq.frameList[super.nextIdleAnimFrame];
				currCycle = seq.cycleList[super.idleAnimFrame];
				nextCycle = super.idleAnimCycle;
			}
		}
		Model model_1 = modelcache.get(l);
		if(model_1 == null) {
			boolean flag = false;
			for(int i2 = 0; i2 < 12; i2++) {
				int k2 = equipment[i2];
				if(k1 >= 0 && i2 == 3) {
					k2 = k1;
				}
				if(j1 >= 0 && i2 == 5) {
					k2 = j1;
				}
				if(k2 >= 256 && k2 < 32768 && !Identikit.cache[k2 - 256].bodyModelCached()) {
					flag = true;
				}
				if(k2 >= 32768 && !ObjectType.get(k2 - 32768).equipModelFetched(gender)) {
					flag = true;
				}
			}
			if(flag) {
				if(aLong1697 != -1L) {
					model_1 = modelcache.get(aLong1697);
				}
				if(model_1 == null) {
					return null;
				}
			}
		}
		if(model_1 == null) {
			final Model[] aclass30_sub2_sub4_sub6s = new Model[12];
			int j2 = 0;
			for(int l2 = 0; l2 < 12; l2++) {
				int i3 = equipment[l2];
				if(k1 >= 0 && l2 == 3) {
					i3 = k1;
				}
				if(j1 >= 0 && l2 == 5) {
					i3 = j1;
				}
				if(i3 >= 256 && i3 < 32768) {
					final Model model_3 = Identikit.cache[i3 - 256].getBodyModel();
					if(model_3 != null) {
						aclass30_sub2_sub4_sub6s[j2++] = model_3;
					}
				}
				if(i3 >= 32768) {
					Model model_4 = ObjectType.get(i3 - 32768).getEquipModel(gender);
					if(model_4 != null) {
						aclass30_sub2_sub4_sub6s[j2++] = model_4;
					}
				}
			}
			model_1 = new Model(j2, aclass30_sub2_sub4_sub6s);
			for(int j3 = 0; j3 < 5; j3++) {
				if(characterParts[j3] != 0) {
					model_1.replaceHsl(Client.anIntArrayArray1003[j3][0], Client.anIntArrayArray1003[j3][characterParts[j3]]);
					if(j3 == 1) {
						model_1.replaceHsl(Client.anIntArray1204[0], Client.anIntArray1204[characterParts[j3]]);
					}
				}
			}
			model_1.applyEffects();
			model_1.calculateLighting(84/*64*/, 1106/*850*/, -30, -50, -30, true);
			modelcache.put(l, model_1);
			aLong1697 = l;
		}
		if(noTransform) {
			return model_1;
		}
		final Model model_2 = Model.model;
		model_2.method464(model_1, AnimationFrame.isNull(currAnim) & AnimationFrame.isNull(i1));
		if(currAnim != -1 && i1 != -1) {
			model_2.method471(DeformSequence.cache[super.anim].flowControl, i1, currAnim);
		} else if(currAnim != -1) {
			if(Config.def.tween()) {
				model_2.applyAnimation(currAnim, nextAnim, nextCycle, currCycle);
			} else {
				model_2.applyAnimation(currAnim);
			}
		}
		model_2.computeBoundsDist();
		model_2.triangleSkin = null;
		model_2.anIntArrayArray1657 = null;
		return model_2;
	}

	public Model getModel() {
		if(!visible) {
			return null;
		}
		if(desc != null) {
			return desc.method160();
		}
		boolean flag = false;
		for(int i = 0; i < 12; i++) {
			final int j = equipment[i];
			if(j >= 256 && j < 32768 && !Identikit.cache[j - 256].headModelCached()) {
				flag = true;
			}
			if(j >= 32768 && !ObjectType.get(j - 32768).isDialogueModelCached(gender)) {
				flag = true;
			}
		}

		if(flag) {
			return null;
		}
		final Model aclass30_sub2_sub4_sub6s[] = new Model[12];
		int k = 0;
		for(int l = 0; l < 12; l++) {
			final int i1 = equipment[l];
			if(i1 >= 256 && i1 < 32768) {
				final Model model_1 = Identikit.cache[i1 - 256].getHeadModel();
				if(model_1 != null) {
					aclass30_sub2_sub4_sub6s[k++] = model_1;
				}
			}
			if(i1 >= 32768) {
				final Model model_2 = ObjectType.get(i1 - 32768).getDialogueModel(gender);
				if(model_2 != null) {
					aclass30_sub2_sub4_sub6s[k++] = model_2;
				}
			}
		}
		final Model model = new Model(k, aclass30_sub2_sub4_sub6s);
		for(int j1 = 0; j1 < 5; j1++) {
			if(characterParts[j1] != 0) {
				model.replaceHsl(Client.anIntArrayArray1003[j1][0], Client.anIntArrayArray1003[j1][characterParts[j1]]);
				if(j1 == 1) {
					model.replaceHsl(Client.anIntArray1204[0], Client.anIntArray1204[characterParts[j1]]);
				}
			}
		}
		return model;
	}

	public void updatePlayer(Buffer buffer) {
		buffer.pos = 0;
		gender = buffer.getUByte();
		headIcon = buffer.getUByte();
		skullIcon = buffer.getUByte();
		desc = null;
		team = 0;
		for(int j = 0; j < 12; j++) {
			final int k = buffer.getUByte();
			if(k == 0) {
				equipment[j] = 0;
				continue;
			}
			int i1 = buffer.getUByte();

			if(j == 6) {
				if(i1 == 0) {
					i1 = -1;
				}
			}

			equipment[j] = (k << 8) + i1;
			if(j == 0 && equipment[0] == 65535) {
				desc = NPCType.get(buffer.getUShort());
				break;
			}
			if(equipment[j] >= 32768 && equipment[j] - 32768 < ObjectType.length) {
				final int l1 = ObjectType.get(equipment[j] - 32768).team;
				if(l1 != 0) {
					team = l1;
				}
			}
		}

		for(int l = 0; l < 5; l++) {
			int j1 = buffer.getUByte();
			if(j1 < 0 || j1 >= Client.anIntArrayArray1003[l].length) {
				j1 = 0;
			}
			characterParts[l] = j1;
		}

		super.anInt1511 = buffer.getUShort();
		if(super.anInt1511 == 65535) {
			super.anInt1511 = -1;
		}
		super.anInt1512 = buffer.getUShort();
		if(super.anInt1512 == 65535) {
			super.anInt1512 = -1;
		}
		super.anInt1554 = buffer.getUShort();
		if(super.anInt1554 == 65535) {
			super.anInt1554 = -1;
		}
		super.anInt1555 = buffer.getUShort();
		if(super.anInt1555 == 65535) {
			super.anInt1555 = -1;
		}
		super.anInt1556 = buffer.getUShort();
		if(super.anInt1556 == 65535) {
			super.anInt1556 = -1;
		}
		super.anInt1557 = buffer.getUShort();
		if(super.anInt1557 == 65535) {
			super.anInt1557 = -1;
		}
		super.anInt1505 = buffer.getUShort();
		if(super.anInt1505 == 65535) {
			super.anInt1505 = -1;
		}
		name = StringUtils.formatName(StringUtils.decryptName(buffer.getLong()));
		combatLevel = buffer.getUByte();
		iron = buffer.getUByte() == 1;
		visible = true;
		aLong1718 = 0L;

		if (this.desc != null) {
			this.combatLevel = this.desc.combatLevel;
			this.anInt1511 = this.desc.standAnimationId;
			this.anInt1512 = this.desc.standAnimationId;
			this.anInt1554 = this.desc.walkAnimationId;
			this.anInt1555 = this.desc.standAnimationId;
			this.anInt1556 = this.desc.walkAnimationId;
			this.anInt1557 = this.desc.walkAnimationId;
		}

		for(int k1 = 0; k1 < 12; k1++) {
			aLong1718 <<= 4;
			if(equipment[k1] >= 256) {
				aLong1718 += equipment[k1] - 256;
			}
		}

		if(equipment[0] >= 256) {
			aLong1718 += equipment[0] - 256 >> 4;
		}
		if(equipment[1] >= 256) {
			aLong1718 += equipment[1] - 256 >> 8;
		}
		for(int i2 = 0; i2 < 5; i2++) {
			aLong1718 <<= 3;
			aLong1718 += characterParts[i2];
		}
		aLong1718 <<= 1;
		aLong1718 += gender;

		if(desc == null) {
			Interface.cache[250].modelId = (characterParts[0] << 25) + (characterParts[4] << 20) + (equipment[0] << 15) + (equipment[8] << 10) + (equipment[11] << 5) + equipment[1];
		} else {
			Interface.cache[250].modelId = (int) (0x12345678L + desc.id);
		}
	}
	
	@Override
	public double getType() {
		return Rasterizer3D.TYPES[1];
	}
	
}