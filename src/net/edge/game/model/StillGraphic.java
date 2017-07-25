package net.edge.game.model;

import net.edge.Config;
import net.edge.cache.unit.AnimationFrame;
import net.edge.cache.unit.SpotAnimation;

public final class StillGraphic extends Entity {

	public final int anInt1560;
	public final int anInt1561;
	public final int anInt1562;
	public final int anInt1563;
	public final int anInt1564;
	public boolean noEffects;
	private final SpotAnimation spotAnim;
	private int animFrameId;
	private int nextAnimFrameId;
	private int animFrameCycle;

	public StillGraphic(int i, int j, int l, int i1, int j1, int k1, int l1) {
		noEffects = false;
		spotAnim = SpotAnimation.cache[i1];
		anInt1560 = i;
		anInt1561 = l1;
		anInt1562 = k1;
		anInt1563 = j1;
		anInt1564 = j + l;
		noEffects = false;
	}

	@Override
	public Model getTransformedModel() {
		final Model model = spotAnim.getModel();
		if(model == null) {
			return null;
		}
		final int currAnim = spotAnim.animFrameSequence.frameList[animFrameId];
		final Model model_1 = new Model(true, AnimationFrame.isNull(currAnim), false, model);
		if(!noEffects) {
			model_1.applyEffects();
			if(Config.def.tween() && nextAnimFrameId != -1) {
				model_1.applyAnimation(currAnim, spotAnim.animFrameSequence.frameList[nextAnimFrameId], animFrameCycle, spotAnim.animFrameSequence.cycleList[animFrameId]);
			} else {
				model_1.applyAnimation(currAnim);
			}
			model_1.triangleSkin = null;
			model_1.anIntArrayArray1657 = null;
		}
		if(spotAnim.scaleHorizontal != 128 || spotAnim.scaleVertical != 128) {
			model_1.scale(spotAnim.scaleHorizontal, spotAnim.scaleVertical, spotAnim.scaleHorizontal);
		}
		if(spotAnim.rotation != 0) {
			if(spotAnim.rotation == 90) {
				model_1.rotate90();
			}
			if(spotAnim.rotation == 180) {
				model_1.rotate90();
				model_1.rotate90();
			}
			if(spotAnim.rotation == 270) {
				model_1.rotate90();
				model_1.rotate90();
				model_1.rotate90();
			}
		}
		model_1.calculateLighting(104/*64*/ + spotAnim.lightness, 1362/*850*/ + spotAnim.contrast, -30, -50, -30, true);
		return model_1;
	}

	public void moveAnimation(int cycles) {
		for(animFrameCycle += cycles; animFrameCycle > spotAnim.animFrameSequence.getFrame(animFrameId); ) {
			animFrameCycle -= spotAnim.animFrameSequence.getFrame(animFrameId) + 1;
			animFrameId++;
			if(animFrameId >= spotAnim.animFrameSequence.length && (animFrameId < 0 || animFrameId >= spotAnim.animFrameSequence.length)) {
				animFrameId = 0;
				noEffects = true;
			}
			if(Config.def.tween()) {
				nextAnimFrameId = animFrameId + 1;
			}
			if(nextAnimFrameId >= spotAnim.animFrameSequence.length) {
				nextAnimFrameId = 0;
			}
		}
	}
}
