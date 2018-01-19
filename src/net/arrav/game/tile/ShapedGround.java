package net.arrav.game.tile;

public class ShapedGround {
	
	public int color61;
	public int color71;
	public int color81;
	public int color91;
	public int color62;
	public int color72;
	public int color82;
	public int color92;
	public boolean textured;
	
	public final boolean messed;
	public final int[] vertexX;
	public final int[] vertexZ;
	public final int[] vertexY;
	public final int[] color1;
	public final int[] color2;
	public final int[] color3;
	public final int[] tex1;
	public final int[] tex2;
	public final int[] tex3;
	public int[] texId;
	public final int firstHalfShape;
	public final int secondHalfShape;
	public final int firstMinimapColor;
	public final int secondMinimapColor;
	public static final int[] vertex2dX = new int[6];
	public static final int[] vertex2dY = new int[6];
	public static final int[] vertex2dZ = new int[6];
	public static final int[] texVertexX = new int[6];
	public static final int[] texVertexY = new int[6];
	public static final int[] texVertexZ = new int[6];
	private static final int[][] anIntArrayArray696 = {{1, 3, 5, 7}, {1, 3, 5, 7}, {1, 3, 5, 7}, {1, 3, 5, 7, 6}, {1, 3, 5, 7, 6}, {1, 3, 5, 7, 6}, {1, 3, 5, 7, 6}, {1, 3, 5, 7, 2, 6}, {1, 3, 5, 7, 2, 8}, {1, 3, 5, 7, 2, 8}, {1, 3, 5, 7, 11, 12}, {1, 3, 5, 7, 11, 12}, {1, 3, 5, 7, 13, 14}};
	private static final int[][] anIntArrayArray697 = {{0, 1, 2, 3, 0, 0, 1, 3}, {1, 1, 2, 3, 1, 0, 1, 3}, {0, 1, 2, 3, 1, 0, 1, 3}, {0, 0, 1, 2, 0, 0, 2, 4, 1, 0, 4, 3}, {0, 0, 1, 4, 0, 0, 4, 3, 1, 1, 2, 4}, {0, 0, 4, 3, 1, 0, 1, 2, 1, 0, 2, 4}, {0, 1, 2, 4, 1, 0, 1, 4, 1, 0, 4, 3}, {0, 4, 1, 2, 0, 4, 2, 5, 1, 0, 4, 5, 1, 0, 5, 3}, {0, 4, 1, 2, 0, 4, 2, 3, 0, 4, 3, 5, 1, 0, 4, 5}, {0, 0, 4, 5, 1, 4, 1, 2, 1, 4, 2, 3, 1, 4, 3, 5}, {0, 0, 1, 5, 0, 1, 4, 5, 0, 1, 2, 4, 1, 0, 5, 3, 1, 5, 4, 3, 1, 4, 2, 3}, {1, 0, 1, 5, 1, 1, 4, 5, 1, 1, 2, 4, 0, 0, 5, 3, 0, 5, 4, 3, 0, 4, 2, 3}, {1, 0, 5, 4, 1, 0, 1, 5, 0, 0, 4, 3, 0, 4, 5, 3, 0, 5, 2, 3, 0, 1, 2, 5}};

	public ShapedGround(int y, int j, int k, int l, int texId1, int texId2, int j1, int k1, int l1, int i2, int j2, int k2, int l2, int i3, int j3, int k3, int l3, int i4, int x, int l4) {
		color61 = l1;
		color71 = i4;
		color81 = j2;
		color91 = k;
		color62 = j;
		color72 = l3;
		color82 = j1;
		color92 = k3;
		messed = !(i3 != l2 || i3 != l || i3 != k2);
		firstHalfShape = j3;
		secondHalfShape = k1;
		firstMinimapColor = i2;
		secondMinimapColor = l4;
		final char c = '\200';
		final int i5 = c / 2;
		final int j5 = c / 4;
		final int k5 = c * 3 / 4;
		final int ai[] = anIntArrayArray696[j3];
		final int l5 = ai.length;
		vertexX = new int[l5];
		vertexZ = new int[l5];
		vertexY = new int[l5];
		final int ai1[] = new int[l5];
		final int ai2[] = new int[l5];
		final int i6 = x * c;
		final int j6 = y * c;
		for(int k6 = 0; k6 < l5; k6++) {
			int l6 = ai[k6];
			if((l6 & 1) == 0 && l6 <= 8) {
				l6 = (l6 - k1 - k1 - 1 & 7) + 1;
			}
			if(l6 > 8 && l6 <= 12) {
				l6 = (l6 - 9 - k1 & 3) + 9;
			}
			if(l6 > 12 && l6 <= 16) {
				l6 = (l6 - 13 - k1 & 3) + 13;
			}
			int i7;
			int k7;
			int i8;
			int k8;
			int j9;
			if(l6 == 1) {
				i7 = i6;
				k7 = j6;
				i8 = i3;
				k8 = l1;
				j9 = j;
			} else if(l6 == 2) {
				i7 = i6 + i5;
				k7 = j6;
				i8 = i3 + l2 >> 1;
				k8 = l1 + i4 >> 1;
				j9 = j + l3 >> 1;
			} else if(l6 == 3) {
				i7 = i6 + c;
				k7 = j6;
				i8 = l2;
				k8 = i4;
				j9 = l3;
			} else if(l6 == 4) {
				i7 = i6 + c;
				k7 = j6 + i5;
				i8 = l2 + l >> 1;
				k8 = i4 + j2 >> 1;
				j9 = l3 + j1 >> 1;
			} else if(l6 == 5) {
				i7 = i6 + c;
				k7 = j6 + c;
				i8 = l;
				k8 = j2;
				j9 = j1;
			} else if(l6 == 6) {
				i7 = i6 + i5;
				k7 = j6 + c;
				i8 = l + k2 >> 1;
				k8 = j2 + k >> 1;
				j9 = j1 + k3 >> 1;
			} else if(l6 == 7) {
				i7 = i6;
				k7 = j6 + c;
				i8 = k2;
				k8 = k;
				j9 = k3;
			} else if(l6 == 8) {
				i7 = i6;
				k7 = j6 + i5;
				i8 = k2 + i3 >> 1;
				k8 = k + l1 >> 1;
				j9 = k3 + j >> 1;
			} else if(l6 == 9) {
				i7 = i6 + i5;
				k7 = j6 + j5;
				i8 = i3 + l2 >> 1;
				k8 = l1 + i4 >> 1;
				j9 = j + l3 >> 1;
			} else if(l6 == 10) {
				i7 = i6 + k5;
				k7 = j6 + i5;
				i8 = l2 + l >> 1;
				k8 = i4 + j2 >> 1;
				j9 = l3 + j1 >> 1;
			} else if(l6 == 11) {
				i7 = i6 + i5;
				k7 = j6 + k5;
				i8 = l + k2 >> 1;
				k8 = j2 + k >> 1;
				j9 = j1 + k3 >> 1;
			} else if(l6 == 12) {
				i7 = i6 + j5;
				k7 = j6 + i5;
				i8 = k2 + i3 >> 1;
				k8 = k + l1 >> 1;
				j9 = k3 + j >> 1;
			} else if(l6 == 13) {
				i7 = i6 + j5;
				k7 = j6 + j5;
				i8 = i3;
				k8 = l1;
				j9 = j;
			} else if(l6 == 14) {
				i7 = i6 + k5;
				k7 = j6 + j5;
				i8 = l2;
				k8 = i4;
				j9 = l3;
			} else if(l6 == 15) {
				i7 = i6 + k5;
				k7 = j6 + k5;
				i8 = l;
				k8 = j2;
				j9 = j1;
			} else {
				i7 = i6 + j5;
				k7 = j6 + k5;
				i8 = k2;
				k8 = k;
				j9 = k3;
			}
			vertexX[k6] = i7;
			vertexZ[k6] = i8;
			vertexY[k6] = k7;
			ai1[k6] = k8;
			ai2[k6] = j9;
		}

		final int[] ai3 = anIntArrayArray697[j3];
		final int length = ai3.length / 4;
		tex1 = new int[length];
		tex2 = new int[length];
		tex3 = new int[length];
		color1 = new int[length];
		color2 = new int[length];
		color3 = new int[length];
		if(texId1 != -1 || texId2 != -1) {
			textured = true;
			texId = new int[length];
		}
		int l7 = 0;
		for(int j8 = 0; j8 < length; j8++) {
			final int lay = ai3[l7];
			int k9 = ai3[l7 + 1];
			int i10 = ai3[l7 + 2];
			int k10 = ai3[l7 + 3];
			l7 += 4;
			if(k9 < 4) {
				k9 = k9 - k1 & 3;
			}
			if(i10 < 4) {
				i10 = i10 - k1 & 3;
			}
			if(k10 < 4) {
				k10 = k10 - k1 & 3;
			}
			tex1[j8] = k9;
			tex2[j8] = i10;
			tex3[j8] = k10;
			if(lay == 0) {
				color1[j8] = ai1[k9];
				color2[j8] = ai1[i10];
				color3[j8] = ai1[k10];
				if(texId != null) {
					texId[j8] = texId1;
				}
			} else {
				color1[j8] = ai2[k9];
				color2[j8] = ai2[i10];
				color3[j8] = ai2[k10];
				if(texId != null) {
					texId[j8] = texId2;
				}
			}
		}
	}
}