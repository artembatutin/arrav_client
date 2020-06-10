/*******************************************************************************
 * [BZip2Compressor.java] BZIP2 Compressor.
 * @note REFACTORING ISN'T REQUIRED.
 ******************************************************************************/
package net.arrav.cache.bzip2;

public final class BZip2Compressor {

	private static final BZip2Data data = new BZip2Data();

	public static int decompress(byte src[], int srcPos, byte dest[], int srcSize, int destSize) {
		synchronized(data) {
			data.src = src;
			data.srcPos = srcPos;
			data.dest = dest;
			data.destPos = 0;
			data.srcSize = srcSize;
			data.destSize = destSize;
			data.bsLive = 0;
			data.bsBuff = 0;
			data.anInt566 = 0;
			data.anInt567 = 0;
			data.anInt571 = 0;
			data.anInt572 = 0;
			data.anInt579 = 0;
			init(data);
			destSize -= data.destSize;
			return destSize;
		}
	}

	private static void method226(BZip2Data data) {
		byte byte4 = data.aByte573;
		int i = data.anInt574;
		int j = data.anInt584;
		int k = data.anInt582;
		final int[] ai = BZip2Data.ll8;
		int l = data.anInt581;
		final byte[] dest = data.dest;
		int destPos = data.destPos;
		int j1 = data.destSize;
		final int k1 = j1;
		final int l1 = data.anInt601 + 1;
		label0:
		do {
			if(i > 0) {
				do {
					if(j1 == 0) {
						break label0;
					}
					if(i == 1) {
						break;
					}
					dest[destPos] = byte4;
					i--;
					destPos++;
					j1--;
				} while(true);
				dest[destPos] = byte4;
				destPos++;
				j1--;
			}
			boolean flag = true;
			while(flag) {
				flag = false;
				if(j == l1) {
					i = 0;
					break label0;
				}
				byte4 = (byte) k;
				l = ai[l];
				final byte byte0 = (byte) (l & 0xff);
				l >>= 8;
				j++;
				if(byte0 != k) {
					k = byte0;
					if(j1 == 0) {
						i = 1;
					} else {
						dest[destPos] = byte4;
						destPos++;
						j1--;
						flag = true;
						continue;
					}
					break label0;
				}
				if(j != l1) {
					continue;
				}
				if(j1 == 0) {
					i = 1;
					break label0;
				}
				dest[destPos] = byte4;
				destPos++;
				j1--;
				flag = true;
			}
			i = 2;
			l = ai[l];
			final byte byte1 = (byte) (l & 0xff);
			l >>= 8;
			if(++j != l1) {
				if(byte1 != k) {
					k = byte1;
				} else {
					i = 3;
					l = ai[l];
					final byte byte2 = (byte) (l & 0xff);
					l >>= 8;
					if(++j != l1) {
						if(byte2 != k) {
							k = byte2;
						} else {
							l = ai[l];
							final byte byte3 = (byte) (l & 0xff);
							l >>= 8;
							j++;
							i = (byte3 & 0xff) + 4;
							l = ai[l];
							k = (byte) (l & 0xff);
							l >>= 8;
							j++;
						}
					}
				}
			}
		} while(true);
		final int i2 = data.anInt571;
		data.anInt571 += k1 - j1;
		if(data.anInt571 < i2) {
			data.anInt572++;
		}
		data.aByte573 = byte4;
		data.anInt574 = i;
		data.anInt584 = j;
		data.anInt582 = k;
		BZip2Data.ll8 = ai;
		data.anInt581 = l;
		data.dest = dest;
		data.destPos = destPos;
		data.destSize = j1;
	}

	private static void init(BZip2Data data) {
		int minLens_zt = 0;
		int limit_zt[] = null;
		int base_zt[] = null;
		int perm_zt[] = null;
		data.blockSize100k = 1;
		if(BZip2Data.ll8 == null) {
			BZip2Data.ll8 = new int[data.blockSize100k * 0x186a0];
		}
		boolean flag19 = true;
		while(flag19) {
			byte magic = bsGetByte(data);
			if(magic == 23) {
				return;
			}
			magic = bsGetByte(data);
			magic = bsGetByte(data);
			magic = bsGetByte(data);
			magic = bsGetByte(data);
			magic = bsGetByte(data);
			data.anInt579++;
			magic = bsGetByte(data);
			magic = bsGetByte(data);
			magic = bsGetByte(data);
			magic = bsGetByte(data);
			magic = bsGetBit(data);
			data.blockRandomised = magic != 0;
			if(data.blockRandomised) {
				System.out.println("PANIC! RANDOMISED BLOCK!");
			}
			data.origPtr = 0;
			magic = bsGetByte(data);
			data.origPtr = data.origPtr << 8 | magic & 0xff;
			magic = bsGetByte(data);
			data.origPtr = data.origPtr << 8 | magic & 0xff;
			magic = bsGetByte(data);
			data.origPtr = data.origPtr << 8 | magic & 0xff;
			for(int i = 0; i < 16; i++) {
				final byte tmp = bsGetBit(data);
				data.inUse16[i] = tmp == 1;
			}
			for(int i = 0; i < 256; i++) {
				data.inUse[i] = false;
			}
			for(int i = 0; i < 16; i++) {
				if(data.inUse16[i]) {
					for(int j = 0; j < 16; j++) {
						final byte tmp = bsGetBit(data);
						if(tmp == 1) {
							data.inUse[i * 16 + j] = true;
						}
					}
				}
			}
			makeMaps(data);
			final int alphaSize = data.nInUse + 2;
			final int nGroups = bsR(3, data);
			final int nSelectors = bsR(15, data);
			for(int i = 0; i < nSelectors; i++) {
				int j = 0;
				do {
					final byte tmp = bsGetBit(data);
					if(tmp == 0) {
						break;
					}
					j++;
				} while(true);
				data.selectorMtf[i] = (byte) j;
			}
			final byte pos[] = new byte[6];
			for(byte v = 0; v < nGroups; v++) {
				pos[v] = v;
			}
			for(int i = 0; i < nSelectors; i++) {
				byte v = data.selectorMtf[i];
				final byte tmp = pos[v];
				for(; v > 0; v--) {
					pos[v] = pos[v - 1];
				}
				pos[0] = tmp;
				data.selector[i] = tmp;
			}
			for(int t = 0; t < nGroups; t++) {
				int curr = bsR(5, data);
				for(int i = 0; i < alphaSize; i++) {
					do {
						byte tmp = bsGetBit(data);
						if(tmp == 0) {
							break;
						}
						tmp = bsGetBit(data);
						if(tmp == 0) {
							curr++;
						} else {
							curr--;
						}
					} while(true);
					data.len[t][i] = (byte) curr;
				}
			}
			for(int t = 0; t < nGroups; t++) {
				byte minLen = 32;
				int maxLen = 0;
				for(int i = 0; i < alphaSize; i++) {
					if(data.len[t][i] > maxLen) {
						maxLen = data.len[t][i];
					}
					if(data.len[t][i] < minLen) {
						minLen = data.len[t][i];
					}
				}
				hbCreateDecodeTables(data.limit[t], data.base[t], data.perm[t], data.len[t], minLen, maxLen, alphaSize);
				data.minLens[t] = minLen;
			}
			final int eob = data.nInUse + 1;
			int groupNo = -1;
			int groupPos = 0;
			for(int i = 0; i <= 255; i++) {
				data.unzftab[i] = 0;
			}
			int j9 = 4095;
			for(int i = 15; i >= 0; i--) {
				for(int j = 15; j >= 0; j--) {
					data.yy[j9] = (byte) (i * 16 + j);
					j9--;
				}
				data.anIntArray593[i] = j9 + 1;
			}
			int i6 = 0;
			if(groupPos == 0) {
				groupNo++;
				groupPos = 50;
				final byte zt = data.selector[groupNo];
				minLens_zt = data.minLens[zt];
				limit_zt = data.limit[zt];
				perm_zt = data.perm[zt];
				base_zt = data.base[zt];
			}
			groupPos--;
			int i7 = minLens_zt;
			int l7;
			byte byte9;
			for(l7 = bsR(i7, data); l7 > limit_zt[i7]; l7 = l7 << 1 | byte9) {
				i7++;
				byte9 = bsGetBit(data);
			}
			for(int nextSym = perm_zt[l7 - base_zt[i7]]; nextSym != eob; ) {
				if(nextSym == 0 || nextSym == 1) {
					int s = -1;
					int n = 1;
					do {
						if(nextSym == 0) {
							s += n;
						} else if(nextSym == 1) {
							s += 2 * n;
						}
						n *= 2;
						if(groupPos == 0) {
							groupNo++;
							groupPos = 50;
							final byte zt = data.selector[groupNo];
							minLens_zt = data.minLens[zt];
							limit_zt = data.limit[zt];
							perm_zt = data.perm[zt];
							base_zt = data.base[zt];
						}
						groupPos--;
						int zn = minLens_zt;
						int zvec;
						byte thech;
						for(zvec = bsR(zn, data); zvec > limit_zt[zn]; zvec = zvec << 1 | thech) {
							zn++;
							thech = bsGetBit(data);
						}
						nextSym = perm_zt[zvec - base_zt[zn]];
					} while(nextSym == 0 || nextSym == 1);
					s++;
					final byte ch = data.seqToUnseq[data.yy[data.anIntArray593[0]] & 0xff];
					data.unzftab[ch & 0xff] += s;
					for(; s > 0; s--) {
						BZip2Data.ll8[i6] = ch & 0xff;
						i6++;
					}
				} else {
					int j11 = nextSym - 1;
					byte byte6;
					if(j11 < 16) {
						final int j10 = data.anIntArray593[0];
						byte6 = data.yy[j10 + j11];
						for(; j11 > 3; j11 -= 4) {
							final int k11 = j10 + j11;
							data.yy[k11] = data.yy[k11 - 1];
							data.yy[k11 - 1] = data.yy[k11 - 2];
							data.yy[k11 - 2] = data.yy[k11 - 3];
							data.yy[k11 - 3] = data.yy[k11 - 4];
						}
						for(; j11 > 0; j11--) {
							data.yy[j10 + j11] = data.yy[j10 + j11 - 1];
						}
						data.yy[j10] = byte6;
					} else {
						int l10 = j11 / 16;
						final int i11 = j11 % 16;
						int k10 = data.anIntArray593[l10] + i11;
						byte6 = data.yy[k10];
						for(; k10 > data.anIntArray593[l10]; k10--) {
							data.yy[k10] = data.yy[k10 - 1];
						}
						data.anIntArray593[l10]++;
						for(; l10 > 0; l10--) {
							data.anIntArray593[l10]--;
							data.yy[data.anIntArray593[l10]] = data.yy[data.anIntArray593[l10 - 1] + 16 - 1];
						}
						data.anIntArray593[0]--;
						data.yy[data.anIntArray593[0]] = byte6;
						if(data.anIntArray593[0] == 0) {
							int i10 = 4095;
							for(int k9 = 15; k9 >= 0; k9--) {
								for(int l9 = 15; l9 >= 0; l9--) {
									data.yy[i10] = data.yy[data.anIntArray593[k9] + l9];
									i10--;
								}
								data.anIntArray593[k9] = i10 + 1;
							}
						}
					}
					data.unzftab[data.seqToUnseq[byte6 & 0xff] & 0xff]++;
					BZip2Data.ll8[i6] = data.seqToUnseq[byte6 & 0xff] & 0xff;
					i6++;
					if(groupPos == 0) {
						groupNo++;
						groupPos = 50;
						final byte byte14 = data.selector[groupNo];
						minLens_zt = data.minLens[byte14];
						limit_zt = data.limit[byte14];
						perm_zt = data.perm[byte14];
						base_zt = data.base[byte14];
					}
					groupPos--;
					int k7 = minLens_zt;
					int j8;
					byte byte11;
					for(j8 = bsR(k7, data); j8 > limit_zt[k7]; j8 = j8 << 1 | byte11) {
						k7++;
						byte11 = bsGetBit(data);
					}
					nextSym = perm_zt[j8 - base_zt[k7]];
				}
			}
			data.anInt574 = 0;
			data.aByte573 = 0;
			data.cftab[0] = 0;
			System.arraycopy(data.unzftab, 0, data.cftab, 1, 256);
			for(int i = 1; i <= 256; i++) {
				data.cftab[i] += data.cftab[i - 1];
			}
			for(int l2 = 0; l2 < i6; l2++) {
				final byte byte7 = (byte) (BZip2Data.ll8[l2] & 0xff);
				BZip2Data.ll8[data.cftab[byte7 & 0xff]] |= l2 << 8;
				data.cftab[byte7 & 0xff]++;
			}
			data.anInt581 = BZip2Data.ll8[data.origPtr] >> 8;
			data.anInt584 = 0;
			data.anInt581 = BZip2Data.ll8[data.anInt581];
			data.anInt582 = (byte) (data.anInt581 & 0xff);
			data.anInt581 >>= 8;
			data.anInt584++;
			data.anInt601 = i6;
			method226(data);
			flag19 = data.anInt584 == data.anInt601 + 1 && data.anInt574 == 0;
		}
	}

	private static byte bsGetByte(BZip2Data data) {
		return (byte) bsR(8, data);
	}

	private static byte bsGetBit(BZip2Data data) {
		return (byte) bsR(1, data);
	}

	private static int bsR(int n, BZip2Data data) {
		int v;
		do {
			if(data.bsLive >= n) {
				final int tmp = data.bsBuff >> data.bsLive - n & (1 << n) - 1;
				data.bsLive -= n;
				v = tmp;
				break;
			}
			data.bsBuff = data.bsBuff << 8 | data.src[data.srcPos] & 0xff;
			data.bsLive += 8;
			data.srcPos++;
			data.srcSize--;
			data.anInt566++;
			if(data.anInt566 == 0) {
				data.anInt567++;
			}
		} while(true);
		return v;
	}

	private static void makeMaps(BZip2Data data) {
		data.nInUse = 0;
		for(int i = 0; i < 256; i++) {
			if(data.inUse[i]) {
				data.seqToUnseq[data.nInUse] = (byte) i;
				data.nInUse++;
			}
		}
	}

	private static void hbCreateDecodeTables(int[] limit, int[] base, int[] perm, byte[] length, int minLen, int maxLen, int alphaSize) {
		int pp = 0;
		for(int i = minLen; i <= maxLen; i++) {
			for(int j = 0; j < alphaSize; j++) {
				if(length[j] == i) {
					perm[pp] = j;
					pp++;
				}
			}
		}
		for(int i = 0; i < 23; i++) {
			base[i] = 0;
		}
		for(int i = 0; i < alphaSize; i++) {
			base[length[i] + 1]++;
		}
		for(int i = 1; i < 23; i++) {
			base[i] += base[i - 1];
		}
		for(int i = 0; i < 23; i++) {
			limit[i] = 0;
		}
		int vec = 0;
		for(int i = minLen; i <= maxLen; i++) {
			vec += base[i + 1] - base[i];
			limit[i] = vec - 1;
			vec <<= 1;
		}
		for(int i = minLen + 1; i <= maxLen; i++) {
			base[i] = (limit[i - 1] + 1 << 1) - base[i];
		}
	}
}