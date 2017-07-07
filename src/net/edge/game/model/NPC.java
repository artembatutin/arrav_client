package net.edge.game.model;

import net.edge.Config;
import net.edge.cache.unit.AnimationFrame;
import net.edge.cache.unit.DeformSequence;
import net.edge.cache.unit.NPCType;
import net.edge.cache.unit.SpotAnimation;
import net.edge.media.Rasterizer3D;

public final class NPC extends Mobile {

	public NPCType type;

	public NPC() {
	}

	@Override
	public Model getTransformedModel() {
		if(type == null) {
			return null;
		}
		Model model = getModel();
		if(model == null) {
			return null;
		}
		super.height = model.maxVerticalDistUp;
		if(super.spotAnim != -1 && super.spotAnimFrame != -1) {
			final SpotAnimation spotAnim = SpotAnimation.cache[super.spotAnim];
			if(AnimationFrame.ready(spotAnim.animFrameSequence.frameList[super.spotAnimFrame])) {
				final Model model_1 = spotAnim.getModel();
				if(model_1 != null) {
					final int j = spotAnim.animFrameSequence.frameList[super.spotAnimFrame];
					final Model spotanimModel = new Model(true, AnimationFrame.isNull(j), false, model_1);
					spotanimModel.translate(0, -super.spotAnimOffset, 0);
					spotanimModel.applyEffects();
					spotanimModel.applyAnimation(j);
					spotanimModel.triangleSkin = null;
					spotanimModel.anIntArrayArray1657 = null;
					if(spotAnim.scaleHorizontal != 128 || spotAnim.scaleVertical != 128) {
						spotanimModel.scale(spotAnim.scaleHorizontal, spotAnim.scaleVertical, spotAnim.scaleHorizontal);
					}
					spotanimModel.calculateLighting(104/*64*/ + spotAnim.lightness, 1362/*850*/ + spotAnim.contrast, -30, -50, -30, true);
					final Model[] parts = {model, spotanimModel};
					model = new Model(parts);
				}
			}
		}
		if(type.boundaryDimension == 1) {
			model.hoverable = true;
		}
		return model;
	}

	@Override
	public boolean isVisible() {
		return type != null;
	}

	private Model getModel() {
		int currAnim = -1;
		int nextAnim = -1;
		int currCycle = -1;
		int nextCycle = -1;
		if(super.anim >= 0 && super.animDelay == 0 && DeformSequence.cache.length > super.anim) {
			DeformSequence seq = DeformSequence.cache[super.anim];
			if(Config.def.isTWEENING() && super.nextAnimFrame != -1) {
				nextAnim = seq.frameList[super.nextAnimFrame];
				currCycle = seq.cycleList[super.animFrame];
				nextCycle = super.animCycle;
			}
			currAnim = seq.frameList[super.animFrame];
			int idleAnim = -1;
			if(super.idleAnim >= 0 && super.idleAnim != super.anInt1511) {
				idleAnim = DeformSequence.cache[super.idleAnim].frameList[super.idleAnimFrame];
			}
			return type.getModel(idleAnim, currAnim, nextAnim, currCycle, nextCycle, DeformSequence.cache[super.anim].flowControl);
		}
		if(super.idleAnim >= 0 && super.idleAnim != 65535) {
			final DeformSequence seq = DeformSequence.cache[super.idleAnim];
			currAnim = seq.frameList[super.idleAnimFrame];
			if(Config.def.isTWEENING() && super.nextIdleAnimFrame != -1) {
				nextAnim = seq.frameList[super.nextIdleAnimFrame];
				currCycle = seq.cycleList[super.idleAnimFrame];
				nextCycle = super.idleAnimCycle;
			}
		}
		return type.getModel(-1, currAnim, nextAnim, currCycle, nextCycle, null);
	}
	
}
