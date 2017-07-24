package net.edge.game.model;

import net.edge.cache.unit.AnimationFrame;
import net.edge.cache.unit.SpotAnimation;

public final class Projectile extends Entity {

	public final int anInt1571;
	public final int anInt1572;
	private double aDouble1574;
	private double aDouble1575;
	private double aDouble1576;
	private double aDouble1577;
	private double aDouble1578;
	private boolean aBoolean1579;
	private final int anInt1580;
	private final int anInt1581;
	private final int anInt1582;
	public final int anInt1583;
	public double aDouble1585;
	public double aDouble1586;
	public double aDouble1587;
	private final int anInt1588;
	private final int anInt1589;
	public final int lockon;
	private final SpotAnimation aSpotAnim_1592;
	private int anInt1593;
	private int anInt1594;
	public int anInt1595;
	private int anInt1596;
	public final int anInt1597;

	public Projectile(int i, int j, int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2, int l2) {
		aBoolean1579 = false;
		aSpotAnim_1592 = SpotAnimation.cache[l2];
		anInt1597 = k1;
		anInt1580 = j2;
		anInt1581 = i2;
		anInt1582 = l1;
		anInt1571 = l;
		anInt1572 = i1;
		anInt1588 = i;
		anInt1589 = j1;
		lockon = k2;
		anInt1583 = j;
		aBoolean1579 = false;
	}

	@Override
	public Model getTransformedModel() {
		final Model model = aSpotAnim_1592.getModel();
		if(model == null) {
			return null;
		}
		int j = -1;
		if(aSpotAnim_1592.animFrameSequence != null) {
			j = aSpotAnim_1592.animFrameSequence.frameList[anInt1593];
		}
		final Model model_1 = new Model(true, AnimationFrame.isNull(j), false, model);
		if(j != -1) {
			model_1.applyEffects();
			model_1.applyAnimation(j);
			model_1.triangleSkin = null;
			model_1.anIntArrayArray1657 = null;
		}
		if(aSpotAnim_1592.scaleHorizontal != 128 || aSpotAnim_1592.scaleVertical != 128) {
			model_1.scale(aSpotAnim_1592.scaleHorizontal, aSpotAnim_1592.scaleVertical, aSpotAnim_1592.scaleHorizontal);
		}
		model_1.rotate(anInt1596);
		model_1.calculateLighting(104/*64*/ + aSpotAnim_1592.lightness, 1362/*850*/ + aSpotAnim_1592.contrast, -30, -50, -30, true);
		return model_1;
	}

	public void method455(int i, int j, int k, int l) {
		if(!aBoolean1579) {
			final double d = l - anInt1580;
			final double d2 = j - anInt1581;
			final double d3 = Math.sqrt(d * d + d2 * d2);
			aDouble1585 = anInt1580 + d * anInt1589 / d3;
			aDouble1586 = anInt1581 + d2 * anInt1589 / d3;
			aDouble1587 = anInt1582;
		}
		final double d1 = anInt1572 + 1 - i;
		aDouble1574 = (l - aDouble1585) / d1;
		aDouble1575 = (j - aDouble1586) / d1;
		aDouble1576 = Math.sqrt(aDouble1574 * aDouble1574 + aDouble1575 * aDouble1575);
		if(!aBoolean1579) {
			aDouble1577 = -aDouble1576 * Math.tan(anInt1588 * 0.02454369D);
		}
		aDouble1578 = 2D * (k - aDouble1587 - aDouble1577 * d1) / (d1 * d1);
	}

	public void method456(int i) {
		aBoolean1579 = true;
		aDouble1585 += aDouble1574 * i;
		aDouble1586 += aDouble1575 * i;
		aDouble1587 += aDouble1577 * i + 0.5D * aDouble1578 * i * i;
		aDouble1577 += aDouble1578 * i;
		anInt1595 = (int) (Math.atan2(aDouble1574, aDouble1575) * 325.94900000000001D) + 1024 & 0x7ff;
		anInt1596 = (int) (Math.atan2(aDouble1577, aDouble1576) * 325.94900000000001D) & 0x7ff;
		if(aSpotAnim_1592.animFrameSequence != null) {
			for(anInt1594 += i; anInt1594 > aSpotAnim_1592.animFrameSequence.getFrame(anInt1593); ) {
				anInt1594 -= aSpotAnim_1592.animFrameSequence.getFrame(anInt1593) + 1;
				anInt1593++;
				if(anInt1593 >= aSpotAnim_1592.animFrameSequence.length) {
					anInt1593 = 0;
				}
			}
		}

	}
}
