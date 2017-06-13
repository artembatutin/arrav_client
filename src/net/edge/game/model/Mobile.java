package net.edge.game.model;

import net.edge.cache.unit.DeformSequence;

import static net.edge.Config.TEN_X_HITS;

public class Mobile extends Entity {

	public int entScreenX;
	public int entScreenY;
	public final int index = -1;
	public final int[] smallX;
	public final int[] smallY;
	public int interactingEntity;
	public int anInt1503;
	public int anInt1504;
	public int anInt1505;
	public String chatSpoken;
	public int height;
	public int turnDirection;
	public int anInt1511;
	public int anInt1512;
	public int chatColorEffect;
	public final int[] hitArray;
	public final int[] hitMarkTypes;
	public final int[] oldHitMarkTypes;
	public final int[] hitsLoopCycle;
	public int idleAnim;
	public int idleAnimFrame;
	public int idleAnimCycle;
	public int spotAnim;
	public int spotAnimFrame;
	public int spotAnimCycle;
	public int anInt1523;
	public int spotAnimOffset;
	public int smallXYIndex;
	public int anim;
	public int animFrame;
	public int animCycle;
	public int animDelay;
	public int anInt1530;
	public int chatAnimationEffect;
	public int loopCycleStatus;
	public int currentHealth;
	public int maxHealth;
	public int chatLoopCycle;
	public int anInt1537;
	public int anInt1538;
	public int anInt1539;
	public int anInt1540;
	public boolean aBoolean1541;
	public int anInt1542;
	public int anInt1543;
	public int anInt1544;
	public int anInt1545;
	public int anInt1546;
	public int anInt1547;
	public int anInt1548;
	public int anInt1549;
	public int x;
	public int y;
	public int yaw;
	public final boolean[] aBooleanArray1553;
	public int anInt1554;
	public int anInt1555;
	public int anInt1556;
	public int anInt1557;
	public int nextAnimFrame;
	public int nextIdleAnimFrame;
	public int nextSpotAnimFrame;
	public int special = 101;

	Mobile() {
		smallX = new int[10];
		smallY = new int[10];
		interactingEntity = -1;
		anInt1504 = 32;
		anInt1505 = -1;
		height = 200;
		anInt1511 = -1;
		anInt1512 = -1;
		hitArray = new int[4];
		hitMarkTypes = new int[4];
		oldHitMarkTypes = new int[4];
		hitsLoopCycle = new int[4];
		idleAnim = -1;
		spotAnim = -1;
		anim = -1;
		loopCycleStatus = -1000;
		chatLoopCycle = 100;
		anInt1540 = 1;
		aBoolean1541 = false;
		aBooleanArray1553 = new boolean[10];
		anInt1554 = -1;
		anInt1555 = -1;
		anInt1556 = -1;
		anInt1557 = -1;
	}

	public boolean isVisible() {
		return false;
	}

	public final void method446() {
		smallXYIndex = 0;
		anInt1542 = 0;
	}

	public final void moveInDir(boolean flag, int i) {
		int j = smallX[0];
		int k = smallY[0];
		if(i == 0) {
			j--;
			k++;
		}
		if(i == 1) {
			k++;
		}
		if(i == 2) {
			j++;
			k++;
		}
		if(i == 3) {
			j--;
		}
		if(i == 4) {
			j++;
		}
		if(i == 5) {
			j--;
			k--;
		}
		if(i == 6) {
			k--;
		}
		if(i == 7) {
			j++;
			k--;
		}
		if(anim >= DeformSequence.cache.length) {
			anim = -1;
		}
		if(anim != -1 && DeformSequence.cache[anim].precedenceWalking == 1) {
			anim = -1;
		}
		if(smallXYIndex < 9) {
			smallXYIndex++;
		}
		for(int l = smallXYIndex; l > 0; l--) {
			smallX[l] = smallX[l - 1];
			smallY[l] = smallY[l - 1];
			aBooleanArray1553[l] = aBooleanArray1553[l - 1];
		}
		smallX[0] = j;
		smallY[0] = k;
		aBooleanArray1553[0] = flag;
	}

	public final void setPos(int i, int j, boolean flag) {
		if(anim != -1 && DeformSequence.cache[anim].precedenceWalking == 1) {
			anim = -1;
		}
		if(!flag) {
			final int k = i - smallX[0];
			final int l = j - smallY[0];
			if(k >= -8 && k <= 8 && l >= -8 && l <= 8) {
				if(smallXYIndex < 9) {
					smallXYIndex++;
				}
				for(int i1 = smallXYIndex; i1 > 0; i1--) {
					smallX[i1] = smallX[i1 - 1];
					smallY[i1] = smallY[i1 - 1];
					aBooleanArray1553[i1] = aBooleanArray1553[i1 - 1];
				}

				smallX[0] = i;
				smallY[0] = j;
				aBooleanArray1553[0] = false;
				return;
			}
		}
		smallXYIndex = 0;
		anInt1542 = 0;
		anInt1503 = 0;
		smallX[0] = i;
		smallY[0] = j;
		x = smallX[0] * 128 + anInt1540 * 64;
		y = smallY[0] * 128 + anInt1540 * 64;
	}

	public int[] hitmarkMove = new int[4];
	public int[] moveTimer = new int[4];
	public int[] hitmarkTrans = new int[4];
	public int[] hitIcon = new int[4];
	public int[] soakDamage = new int[4];
	public int[] hitMarkPos = new int[4];

	public final void updateHitData(int markType, int damage, int l, int icon, int soak) {
		if(!TEN_X_HITS.isOn()) {
			damage = damage / 10;
			soak = soak / 10;
		}
		for(int i1 = 0; i1 < 4; i1++) {
			if(hitsLoopCycle[i1] <= l) {
				hitIcon[i1] = icon;
				hitmarkMove[i1] = 5;
				moveTimer[i1] = 2;
				hitmarkTrans[i1] = 255;
				soakDamage[i1] = soak;
				hitArray[i1] = damage;
				hitMarkTypes[i1] = markType;
				oldHitMarkTypes[i1] = getOldHitType(markType, damage);
				hitsLoopCycle[i1] = l + 70;
				hitMarkPos[i1] = 0;
				return;
			}
		}
	}

	private int getOldHitType(int type, int hit) {
		if(hit == 0) {
			return 0;
		}
		if(type >= 5) {
			type -= 5;
		}
		switch(type) {
			case 0: //Normal hit goes to the correct id.
				return 1;
			case 1: //Special hit goes to normal hit.
				return 1;
		}
		return type;
	}
}
